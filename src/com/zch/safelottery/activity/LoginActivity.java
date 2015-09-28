package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.Tencent;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.AlipayLoginActivity.OnAlipayLogin;
import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.inteface.JointLoginListener;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.UserInfoParser;
import com.zch.safelottery.setttings.LoginManager;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.ShortCut;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.share.LoginQQUtil;
import com.zch.safelottery.util.share.Login_WeiboUtil;
public class LoginActivity extends ZCHBaseActivity {
    /** Called when the activity is first created. */
	
	private EditText userName;
	private EditText userPassWord;
	private Button register,registerbig;
	private Button login,baidu_button;
	private TextView forgetPassword;
	private CheckBox boxAutologin;
	private CheckBox boxPassword;
	private ImageButton btnUserClear;
	private ImageButton btnPasswordClear;
	private LinearLayout login_weibo_view,login_zhifubao_view,login_qq_view,login_renren_view;
	private SharedPreferences loginPreference;
	
	private int from;
	private Class<?> toClass;
	private Bundle mBundle;
	
	private Context mContext;
	
	private String mName; //用户名
	private String mPassword; //密码
	
	private boolean isEditChange = false;
	
	private boolean isJointLogin = false;//是否是联合登录，true为联合登录，false为普通登陆
	private Tencent mTencent;
	private WeiboAuth mWeibo;
	private SsoHandler mSsoHandler;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginPreference = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
        initViews();
        mContext = this;
        
        Intent intent = getIntent();
        if(intent != null){
        	from = intent.getIntExtra("from", 0);
        	toClass = (Class<?>) intent.getSerializableExtra(Settings.TOCLASS);
        	mBundle = intent.getBundleExtra(Settings.BUNDLE);
        	int num=loginPreference.getInt(Settings.REMEBER_ISJIONT, -1);
        	if(num==1){
        		isJointLogin=true;
            }else {
              	isJointLogin=false;
              	checkNamePassword();
            }
        }
    }

	private void initViews() {
		
		btnUserClear = (ImageButton) findViewById(R.id.ib_user_clear);
		btnPasswordClear = (ImageButton) findViewById(R.id.ib_password_clear);
		userName = (EditText) findViewById(R.id.login_name);
		userPassWord = (EditText) findViewById(R.id.login_password);
		register = (Button) findViewById(R.id.login_register_button);
		login = (Button) findViewById(R.id.login_login_button);
		registerbig=(Button) findViewById(R.id. login_registerbig_button);
		forgetPassword = (TextView) findViewById(R.id.login_forget_password);
		boxPassword = (CheckBox) findViewById(R.id.login_checkbox_password);
		boxAutologin = (CheckBox) findViewById(R.id.login_checkbox_autologin);
		baidu_button = (Button) findViewById(R.id.login_baidu_button);
		
		login_weibo_view=(LinearLayout) findViewById(R.id.login_weibo_view);
		login_zhifubao_view=(LinearLayout) findViewById(R.id.login_zhifubao_view);
		login_qq_view=(LinearLayout) findViewById(R.id.login_qq_view);
		login_renren_view=(LinearLayout) findViewById(R.id.login_renren_view);
		login_weibo_view.setOnClickListener(onClickListener);
		login_zhifubao_view.setOnClickListener(onClickListener);
		login_qq_view.setOnClickListener(onClickListener);
		login_renren_view.setOnClickListener(onClickListener);
		btnUserClear.setOnClickListener(onClickListener);
		btnPasswordClear.setOnClickListener(onClickListener);
		
		boxPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(!isChecked){
					boxAutologin.setChecked(false);
				}
				boxPassword.setChecked(isChecked);
			}
		});
		
		boxAutologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					boxAutologin.setChecked(isChecked);
					boxPassword.setChecked(isChecked);
				}else{
					boxAutologin.setChecked(isChecked);
				}
			}
		});
		userName.setFilters(new InputFilter[]{new MyInputFilter(1)}); //Listeners userName的实时事件
		userPassWord.setFilters(new InputFilter[]{new MyInputFilter(2)}); //Listeners userPassWord的实时事件
		
		
		userName.setOnTouchListener(touchListener);
		userPassWord.setOnTouchListener(touchListener);
		login.setOnClickListener(onClickListener);
		register.setOnClickListener(onClickListener);
		registerbig.setOnClickListener(onClickListener);
		forgetPassword.setOnClickListener(onClickListener);
		baidu_button.setOnClickListener(onClickListener);
	}
	
	private void checkNamePassword() {
		mPassword = LoginManager.resetLoginState(loginPreference, userName, userPassWord, boxPassword, boxAutologin);
	}
	
	/**
	 * 登录成功后跳转到目标Activity
	 */
	private void backToHome(){
		if(toClass != null){
			Intent intent = new Intent(this, toClass);
			if(from == Settings.USERHOME){
				intent.putExtra(Settings.TABHOST, from);
			}
			if(mBundle != null){
				intent.putExtra(Settings.BUNDLE, mBundle);
			}
			this.startActivity(intent);
		}
		ShortCut.getPrivateCountMessage(this);
		finish();
	}
	
	/*****
	 * 保存用户名跟密码
	 * @param name 用户名
	 * @param password 密码
	 */
	private void savePtPassword(String name, String password) {
		 LoginManager.saveLoginState(this,boxPassword.isChecked(),boxAutologin.isChecked(),name,password,isJointLogin);
	}
	/*****
	 * 保存联合登录用户名跟密码
	 * @param name 用户名
	 * @param password 密码
	 */
	private void saveJiontPassword(String name, String password) {
		 LoginManager.saveLoginState(this,true,true,name,password,isJointLogin);
	}
	
	
//	/*****
//	 * 保存联合用户名跟密码
//	 * @param name 用户名
//	 * @param password 密码
//	 */
//	private void JointsavePassword(String name, String password) {
//		 LoginManager.saveJiontLoginState(this,isJointLogin,name,password);
//	}
	
	/**
	 * 点击登录执行
	 */
	private void startLogin(){
		if(isJointLogin){
			mName = loginPreference.getString(Settings.LOGIN_NAME, "");
    		mPassword = loginPreference.getString(Settings.LOGIN_PASSWORD, "");
    		if(TextUtils.isEmpty(mName)||TextUtils.isEmpty(mPassword)){
    			ToastUtil.diaplayMesShort(mContext, "联合自动登录验证失败,请重新选择登录");
    		}else{
    			String moibleInfo=SystemInfo.getPhoneInfo(LoginActivity.this);
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("type", "1");
    			map.put("info", mName);
    			map.put("password", mPassword);
    			map.put("moibleInfo", moibleInfo);
    			asyncConnect(JsonUtils.toJsonStr(map));
    		}
		}else{
			String type = "1";
			mName = userName.getText().toString().trim();
			String password = userPassWord.getText().toString().trim();
			if(TextUtils.isEmpty(mName)){
				ToastUtil.diaplayMesShort(mContext, "请填写用户名");
			}else if(TextUtils.isEmpty(password)){
				ToastUtil.diaplayMesShort(mContext, "请填写密码");
			}else {
				mPassword = isEditChange ? Md5.d5(password) : mPassword;
				String moibleInfo=SystemInfo.getPhoneInfo(LoginActivity.this);
				Map<String, String> map = new HashMap<String, String>();
				map.put("type", type);
				map.put("info", mName);
				map.put("password", mPassword);
				map.put("moibleInfo", moibleInfo);
				asyncConnect(JsonUtils.toJsonStr(map));
			}
		}
	}
	
	class MyInputFilter implements InputFilter{
		int mEdit;
		int mark;
		/** 
		 * @param edit 1为用户名 2为密码
		 */
		public MyInputFilter(int edit){
			this.mEdit = edit;
		}
		/***
		 * New 当前类的时候才会执行
		 */
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			try {
				if(mark++ < 1) return source; //初始化时直接返回
				
				//对Edit 进行更改时
				if(!TextUtils.isEmpty(source.toString())){
					isEditChange = true;
				}
				// 如果按回退键
				if (source.length() < 1 && (dend - dstart >= 1)) {
					isEditChange = true;
					source = dest.subSequence(dstart, dend - 1);
				}
				if(mEdit == 1){
//					if(userPassWord.getText().toString().trim().equals(";';';';';'")) //当前密码是否为默认密码
//						userPassWord.setText("");
				}
				// 其他情况直接返回输入的内容
			} catch (Exception e) {
				userName.setFilters(new InputFilter[]{}); 
				userPassWord.setFilters(new InputFilter[]{});
				e.printStackTrace();
			}
			return source;
		}
	}
		
	private void asyncConnect(String... params) {
		final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "正在登录...", true, true);
		if(isJointLogin){
			SafelotteryHttpClient.post(mContext, "3107", "", params[0], new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if(mResult != null){
						GetString.isLogin = true;
						GetString.isAccountNeedReSet = true;
						GetString.userInfo = (UserInfoBean) JsonUtils.parserJsonBean(mResult.getResult(), new UserInfoParser());
						saveJiontPassword(GetString.userInfo.getUserName(),GetString.userInfo.getPassWord()); //联合登录成功保存用户信息及状态
						backToHome();
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progressDialog.dismiss();
				}
			});//联合登陆
		}else{
			SafelotteryHttpClient.post(mContext, "3101", "", params[0], new TypeResultHttpResponseHandler(this, true){

				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if(mResult != null){
						GetString.isLogin = true;
						GetString.isAccountNeedReSet = true;
						GetString.userInfo = (UserInfoBean) JsonUtils.parserJsonBean(mResult.getResult(), new UserInfoParser());
						savePtPassword(mName, mPassword); //登录成功保存用户信息及状态
						backToHome();
					}
				}

				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progressDialog.dismiss();
				}
			});//普通登陆
		}
		StatService.onEvent(mContext, "total-login", "所有点登录的请求", 1);
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.login_login_button){
				//藏匿输入法
				final InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);       
				imm.hideSoftInputFromWindow(userName.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);  
				imm.hideSoftInputFromWindow(userPassWord.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);  
				isJointLogin = false;
				startLogin();
			}else if(id == R.id.login_register_button){
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				intent.putExtra("from", from);
				startActivity(intent);
			}else if(id == R.id.login_registerbig_button){
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				intent.putExtra("from", from);
				startActivity(intent);
			}else if(id == R.id.login_forget_password){
				Intent intent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
				startActivity(intent);
			}
			else if(id == R.id.login_weibo_view){
				mWeibo = new WeiboAuth(LoginActivity.this, Login_WeiboUtil.APP_KEY, Login_WeiboUtil.REDIRECT_URL, Login_WeiboUtil.SCOPE);
		        mSsoHandler = new SsoHandler(LoginActivity.this, mWeibo);
				isJointLogin = true;
				//微博
				Login_WeiboUtil login_WeiboUtil = new Login_WeiboUtil();
				login_WeiboUtil.login(LoginActivity.this,mSsoHandler);
				login_WeiboUtil.setmJointLoginListener(new JointLoginListener() {
					@Override
					public void onSuccess(String msg) {
						asyncConnect(msg);
					}
				});
			}
			else if(id == R.id.login_zhifubao_view){
				//支付宝
				isJointLogin = true;
				AlipayLoginActivity alipayLoginActivity = new AlipayLoginActivity(mContext);
				alipayLoginActivity.start();
				alipayLoginActivity.setOnAlipayLogin(new OnAlipayLogin() {
					
					@Override
					public void onLogin(String msg) {
						asyncConnect(msg);
					}
				});
			}
			else if(id == R.id.login_qq_view){
				mTencent = Tencent.createInstance(LoginQQUtil.APP_ID, LoginActivity.this);
				isJointLogin = true;
				LoginQQUtil mLoginQQUtil = new LoginQQUtil();
				mLoginQQUtil.loginQQ(LoginActivity.this, mTencent);
				mLoginQQUtil.setmJointLoginListener(new JointLoginListener() {
					@Override
					public void onSuccess(String msg) {
						asyncConnect(msg);
					}
				});
			}
			else if(id == R.id.login_renren_view){
				
			}
			else if(id == R.id.ib_user_clear){
				userName.setText("");
			}
			else if(id == R.id.ib_password_clear){
				userPassWord.setText("");
			}
			
		}
	};
	
	private View.OnTouchListener touchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN==event.getAction()) {
				int id = v.getId();
				if(id == R.id.login_name){
					btnUserClear.setVisibility(View.VISIBLE);
					btnPasswordClear.setVisibility(View.GONE);
				}
				else if(id == R.id.login_password){
					btnUserClear.setVisibility(View.GONE);
					btnPasswordClear.setVisibility(View.VISIBLE);
				}
			}
			return false;
		}
	};
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(mTencent != null){
			mTencent.onActivityResult(requestCode, resultCode, data) ;
		}
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}