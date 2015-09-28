package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.LoginManager;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.util.SharedPreferencesOperate;

public class RegisterActivity extends ZCHBaseActivity {

	public static String userNamePattern = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
	private EditText userName;
	private EditText userPassword;
	private EditText rePassword;
	private EditText registerkey, register_phone, register_phonekey;
	private TextView title_text, repassword_text, registerKeyObtain;
	private LinearLayout normal_register, bang_register, register_key;
	private Button submit, register_finish_btn;
	private Dialog dialog;
	private RadioButton key_rbtn, manual_rbtn;
	private View manual_view;
	private int whichBtn = 1;
	private int from;
	private int namelength = 0;
	private int pwdlength = 0;
	private String name;
	private String password;
	private String repeat_pwd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		from = getIntent().getIntExtra("from", -1);
		initViews();
	}

	private void initViews() {
		title_text = (TextView) findViewById(R.id.register_title_text);
		repassword_text = (TextView) findViewById(R.id.register_layout_repassword_text);
		userName = (EditText) findViewById(R.id.register_name);
		userPassword = (EditText) findViewById(R.id.register_password);
		rePassword = (EditText) findViewById(R.id.register_repassword);
		submit = (Button) findViewById(R.id.button_submit);
		register_finish_btn = (Button) findViewById(R.id.register_finish_btn);
		key_rbtn = (RadioButton) findViewById(R.id.register_key_rbtn);
		manual_rbtn = (RadioButton) findViewById(R.id.register_top_menu_manual_rbtn);
		manual_view = (View) findViewById(R.id.register_layout_manual);
		bang_register = (LinearLayout) findViewById(R.id.band_user_illustrate);
		normal_register = (LinearLayout) findViewById(R.id.register_layout_head);
		register_key = (LinearLayout) findViewById(R.id.register_key);
		registerkey = (EditText) findViewById(R.id.register_key_etx);
		registerKeyObtain = (TextView) findViewById(R.id.register_key_obtain);
		submit.setOnClickListener(onClickListener);
		key_rbtn.setOnClickListener(onClickListener);
		manual_rbtn.setOnClickListener(onClickListener);
		register_finish_btn.setOnClickListener(onClickListener);
		registerKeyObtain.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (v.getId() == R.id.register_top_menu_manual_rbtn) {
				showAutoRegister(1);
			} else if (v.getId() == R.id.register_key_rbtn) {
				showAutoRegister(2);
			} else if (v.getId() == R.id.register_finish_btn || v.getId() == R.id.button_submit) {
				name = userName.getText().toString().trim();
				namelength = name.length();
				password = userPassword.getText().toString().trim();
				pwdlength = password.length();
				repeat_pwd = rePassword.getText().toString().trim();
				if (whichBtn == 1) {
					if (TextUtils.isEmpty(name)) {
						Toast.makeText(getApplicationContext(), "请填写用户名", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(password)) {
						Toast.makeText(getApplicationContext(), "请填写密码", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(repeat_pwd)) {
						Toast.makeText(getApplicationContext(), "请填写确认密码", Toast.LENGTH_SHORT).show();
					} else if (!password.equals(repeat_pwd)) {
						Toast.makeText(getApplicationContext(), "密码填写不一致", Toast.LENGTH_SHORT).show();
					} else if (namelength < 2 || namelength > 16) {
						Toast.makeText(getApplicationContext(), "用户名长度必须在2-16之间", Toast.LENGTH_SHORT).show();
					} else if (pwdlength < 6 || pwdlength > 16) {
						Toast.makeText(getApplicationContext(), "密码长度必须在6-16之间", Toast.LENGTH_SHORT).show();
					} else if (isUserNameLegal(name)) {
						Toast.makeText(getApplicationContext(), "用户名只能含有汉字、数字、字母、下划线", Toast.LENGTH_SHORT).show();
					} else {
						doRequestTask();
					}
				} else if (whichBtn == 2) {
					// 优惠码注册
				}
			}
		}
	};

	// 1普通注册,2优惠码注册
	private void showAutoRegister(int which) {
		whichBtn = which;
		if (which == 1) {
			register_key.setVisibility(View.GONE);
		} else if (which == 2) {
			register_key.setVisibility(View.VISIBLE);
		}
	}

	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		SafelotteryHttpClient.post(this, "3100", "", initDate(), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					String userCode = (String) mMap.get("userCode");
					if(!TextUtils.isEmpty(userCode)){
						UserInfoBean userInfoBean = new UserInfoBean();
						userInfoBean.setUserCode(userCode);
						userInfoBean.setUserName(name);
						userInfoBean.setNickname(name);
						GetString.userInfo = userInfoBean;
						GetString.isLogin = true;
						GetString.isAccountNeedRefresh = true;
						SharedPreferencesOperate.updateUserInfo(getApplicationContext(), SharedPreferencesOperate.SAVE_USER_INFO, JsonUtils.toJsonStr(GetString.userInfo));
						
						String gameWarn = (String)mMap.get("gameWarn");
//					    gameWarn = "";
						if( !TextUtils.isEmpty(gameWarn) ){
							Intent intent = new Intent(RegisterActivity.this, BoundUserPhoneActivity.class); //跳入手机绑定
							Bundle mBundle = new Bundle();
							mBundle.putInt(BoundUserPhoneActivity.BOUND_FROM, from);
							mBundle.putString("gameWarn", gameWarn);
							mBundle.putInt(BoundUserPhoneActivity.BOUND_STATE, BoundUserPhoneActivity.BOUND_NOT_FINISH_MOVABLE);
							intent.putExtra(Settings.BUNDLE, mBundle);
							startActivity(intent);
						}else{
							Intent intent = new Intent();
							intent.setClass(RegisterActivity.this, MainTabActivity.class);
							intent.putExtra(Settings.TABHOST, Settings.USERHOME);
							SafeApplication.dataMap.put("notNeedPwd", "yes");
							RegisterActivity.this.startActivity(intent);
						}
						LoginManager.saveLoginState(RegisterActivity.this, true, true, name, password,false);
						RegisterActivity.this.finish();
						Settings.closeOtherActivity(RegisterActivity.this);
					}
				}else{
					LogUtil.CustomLog("TAG", "server return Map is null");
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
			
			@Override
			public void onFinish(){
				progresdialog.dismiss();
			}
		});
		StatService.onEvent(RegisterActivity.this, "total-sign", "所有点注册的请求", 1);
	}

	public String initDate() {
		String Md5PwdStr = Md5.d5(password);// 密码MD5加密
		password = Md5PwdStr;
		Map<String, String> map = new HashMap<String, String>();
		map.put("password", Md5PwdStr);
		map.put("userName", name);
		String str =  JsonUtils.toJsonStr(map);
		return str;
	}

	/**
	 * 用户名是否合法
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean isUserNameLegal(String userName) {
		Pattern p = Pattern.compile(userNamePattern);
		Matcher m = p.matcher(userName);
		return !m.find();
	}

}