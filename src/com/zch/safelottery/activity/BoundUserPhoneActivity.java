package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.sendlottery.ContactListViewActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;

/**
 * @author Jiang
 *
 */
public class BoundUserPhoneActivity extends ZCHBaseActivity {

	/** 跳过、验证及完成 **/
	private Button skipBtn, authcodeBtn, finishBtn;
	
	/** 输入手机号跟验证码 **/
	private EditText phoneEd, authcodeEd;
	/** 显示手机号 **/
	private TextView phoneTv;
	
	/** 显示用户名 **/
	private LinearLayout userShowLy; 
	/** 成功提示 **/
	private TextView userSucceetTv;
	/** 用户名提示 **/
	private TextView userPromptTv;
	/** 用户名 **/
	private TextView userNameTv;
	
	/** 显示作用说明 **/
	private LinearLayout explainShowLy; 
	/** 说明内容 **/
	private TextView explainTv;
	
	/** 显示验证码 **/
	private LinearLayout authCodeLy; 

	private int state;
	/** 判断状态传递KEY **/
	public static final String BOUND_STATE = "BOUND_STATE"; 
	/** 跳转页面传递KEY **/
	public static final String BOUND_FROM = "BOUND_FROM"; 
	
	/** 绑定成功完成 **/
	public static final int BOUND_FINISH_SUCCEED = 1; 
	/** 绑定未完成有活动情况 **/
	public static final int BOUND_NOT_FINISH_MOVABLE = 21;
	/** 绑定未完成无活动情况 **/
	public static final int BOUND_NOT_FINISH = 22;
	/** 绑定更改 **/
	public static final int BOUND_CHANGE = 23;
	
	private String userName; //用户名
	private String phoneNum; //手机号
	private String authcode; //验证码
	private String gameWarn;
	
	/** 是否发送验证码 **/
	private boolean isAuthcode;
	
	/** 来自的位置 **/
	private int from; 
	
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bound_user_phone);
		mContext = this;
		Bundle mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
		if(mBundle != null){
			state = mBundle.getInt(BOUND_STATE, -1);
			from = mBundle.getInt(BOUND_FROM, 0);
			gameWarn = mBundle.getString("gameWarn");
		}
//		state = BOUND_NOT_FINISH_MOVABLE;
		initUI();
		initData();
	}

	private void initUI() {
		authcodeBtn = (Button) findViewById(R.id.bound_user_phone_get_authcode);
		authcodeEd = (EditText) findViewById(R.id.bound_user_phone_authcode);
		skipBtn = (Button) findViewById(R.id.bound_user_phone_skip);
		finishBtn = (Button) findViewById(R.id.bound_user_phone_finish);
		
		userShowLy = (LinearLayout) findViewById(R.id.bound_user_phone_name_user_show);
		
		userSucceetTv = (TextView) findViewById(R.id.bound_user_phone_explain_succeet);
		userPromptTv = (TextView) findViewById(R.id.bound_user_phone_user_prompt);
		userNameTv = (TextView) findViewById(R.id.bound_user_phone_user_name);
		
		explainShowLy = (LinearLayout) findViewById(R.id.bound_user_phone_explain_show);
		explainTv = (TextView) findViewById(R.id.bound_user_phone_explain);
		
		phoneEd = (EditText) findViewById(R.id.bound_user_phone_phone_edit);
		authcodeEd = (EditText) findViewById(R.id.bound_user_phone_authcode);
		
		phoneTv = (TextView) findViewById(R.id.bound_user_phone_phone_num);
		
		authCodeLy = (LinearLayout) findViewById(R.id.bound_user_phone_authcode_show);
		
		authcodeBtn.setOnClickListener(onClickListener);
		skipBtn.setOnClickListener(onClickListener);
		finishBtn.setOnClickListener(onClickListener);
	}
	
	private void initData(){
		//取得用户信息数据
		userName = GetString.userInfo.getUserName();
		phoneNum = GetString.userInfo.getMobile();
		
		switch(state){
		case BOUND_FINISH_SUCCEED:
			boundSucceed();
			break;
		case BOUND_NOT_FINISH:
			boundEdit();
			break;
		case BOUND_NOT_FINISH_MOVABLE:
			boundEditMovable();
			break;
		case BOUND_CHANGE:
			boundChange();
			break;
		default :
//			ToastUtil.diaplayMesShort(getApplicationContext(), "不满足条件，请重试");
			finish();
			break;
		}
		
	}
	/**
	 * 隐藏所有
	 */
	private void gone(){
		userShowLy.setVisibility(View.GONE);
		explainShowLy.setVisibility(View.GONE);

		skipBtn.setVisibility(View.GONE);
		phoneEd.setVisibility(View.GONE);

		phoneTv.setVisibility(View.GONE);
		userSucceetTv.setVisibility(View.GONE);
		
		authCodeLy.setVisibility(View.GONE);
	}
	
	/**
	 * 绑定 输入信息 无活动
	 */
	private void boundEdit(){
		gone();
		
		userShowLy.setVisibility(View.VISIBLE);
		explainShowLy.setVisibility(View.VISIBLE);
		phoneEd.setVisibility(View.VISIBLE);
		authCodeLy.setVisibility(View.VISIBLE);
		
		explainTv.setText("为了您的账户安全，请绑定手机号码否则您将无法找回密码");
		userNameTv.setText(userName);
		finishBtn.setText("绑定");
	}
	
	/**
	 * 绑定 输入信息 有活动
	 */
	private void boundEditMovable(){
		gone();
		
		userShowLy.setVisibility(View.VISIBLE);
		userSucceetTv.setVisibility(View.VISIBLE);
		explainShowLy.setVisibility(View.VISIBLE);
		phoneEd.setVisibility(View.VISIBLE);
		authCodeLy.setVisibility(View.VISIBLE);
		skipBtn.setVisibility(View.VISIBLE);
		
		userPromptTv.setText(userName);
		userNameTv.setText("恭喜您，注册成功。");
		if(!TextUtils.isEmpty(gameWarn)){
			explainTv.setText("为了您的账户安全强烈建议您完善真实信息，完善后"+gameWarn);
		}else{
			explainTv.setText("为了您的账户安全强烈建议您完善真实信息。");
		}
		finishBtn.setText("确定");
	}
	
	/**
	 * 绑定 成功后
	 */
	private void boundSucceed(){
		gone();
		
		userShowLy.setVisibility(View.VISIBLE);
		phoneTv.setVisibility(View.VISIBLE);
		
		explainTv.setText("为了您的账户安全，请绑定手机号码否则您将无法找回密码");
		userNameTv.setText(userName);
		phoneTv.setText(phoneNum);
		
		userPromptTv.setText("用户名： ");
		finishBtn.setText("马上去购彩");
		authcodeBtn.setClickable(true);
		authcodeBtn.setText("修改绑定手机");
		authcodeBtn.setBackgroundResource(R.drawable.bg_btn_gray_to_darkgray_selector);
	}
	
	/**
	 * 绑定 修改
	 */
	private void boundChange(){
		gone();
		
		userShowLy.setVisibility(View.VISIBLE);
		phoneEd.setVisibility(View.VISIBLE);
		authCodeLy.setVisibility(View.VISIBLE);
		
		userNameTv.setText(userName);
		
		authcodeBtn.setText("获取验证码");
		finishBtn.setText("绑定");
	}
	
	
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			
			if (v.getId() == R.id.bound_user_phone_skip) {
				Intent intent = new Intent();
				intent.setClass(mContext, MainTabActivity.class);
				intent.putExtra(Settings.TABHOST, from);
				mContext.startActivity(intent);
				Settings.closeOtherActivity(BoundUserPhoneActivity.this);
			} else if (v.getId() == R.id.bound_user_phone_get_authcode) {
				if(state == BOUND_FINISH_SUCCEED){
					state = BOUND_CHANGE;
					initData();
				}else{
					phoneNum = phoneEd.getText().toString().trim();
					if(!TextUtils.isEmpty(phoneNum) && ContactListViewActivity.IsUserNumber(phoneNum)){
						isAuthcode = true;
						doRequestTask();
					}else{
						Toast.makeText(getApplicationContext(), "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
					}
				}
			} else if (v.getId() == R.id.bound_user_phone_finish) {
				if(state == BOUND_FINISH_SUCCEED){
					//完成成功 去购彩页面
					Settings.closeOtherActivity(BoundUserPhoneActivity.this);
					Intent intent = new Intent(mContext, MainTabActivity.class);
					intent.putExtra(Settings.TABHOST, 0);
					mContext.startActivity(intent);
				} else {
					//执行绑定功能
					phoneNum = phoneEd.getText().toString().trim();
					authcode = authcodeEd.getText().toString().trim();
					if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(authcode)) {
						doRequestTask();
					} else {
						Toast.makeText(mContext, "请填写完整的信息", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};

	private void doRequestTask() {
		String userCode = GetString.userInfo.getUserCode();
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		if(isAuthcode){
			Map<String, String> map = new HashMap<String, String>();
			map.put("userCode", userCode);
			map.put("mobile", phoneNum);
			
			SafelotteryHttpClient.post(mContext, "3105", "bindMobile", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(mContext, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					isAuthcode = false;
					authcodeBtn.setClickable(false);
					authcodeBtn.setBackgroundResource(R.drawable.dialog_bottom_button_press);
					timer.start();
					Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progresdialog.dismiss();
				}
			});
		}else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("userCode", userCode);
			map.put("mobile", phoneNum);
			map.put("code", authcode);
			
			SafelotteryHttpClient.post(mContext, "3103", "mobile", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(mContext, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					GetString.userInfo.setMobile(phoneNum); //把电话号码写入infoBean
					
					if (timer != null) {
						timer.cancel(); //内部停止不能清空
					}
					state = BOUND_FINISH_SUCCEED;
					initData();
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progresdialog.dismiss();
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	CountDownTimer timer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			authcodeBtn.setText("     " + millisUntilFinished / 1000 + "秒     ");
		}
		
		@Override
		public void onFinish() {
			authcodeBtn.setClickable(true);
			authcodeBtn.setText("获取验证码");
			authcodeBtn.setBackgroundResource(R.drawable.bg_btn_gray_to_darkgray_selector);
		}
	};
}