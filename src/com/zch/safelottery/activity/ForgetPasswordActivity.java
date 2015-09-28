package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.view.TitleViews;

public class ForgetPasswordActivity extends ZCHBaseActivity {

	private EditText phoneNumber;
	private EditText checkCode;
	private Button getCheckCode;
	private String mobile;
	private String code;
	private int step_one = 1;// 1.获取验证码 2.找回密码

	private RelativeLayout title;
	
	private TitleViews titleViews;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_password);
		initViews();
		addTitle();
	}

	private void initViews() {

		title = (RelativeLayout) findViewById(R.id.forget_password_title);
		phoneNumber = (EditText) findViewById(R.id.forget_password_phonenumber);
		checkCode = (EditText) findViewById(R.id.forget_password_check_code);
		getCheckCode = (Button) findViewById(R.id.forget_password_getcheckcode_button);

		getCheckCode.setOnClickListener(onClickListener);
	}
	
	private void addTitle(){
		titleViews = new TitleViews(this, "找回密码");
		titleViews.setBtnName("下一步");
//		titleViews.setBtnIcon(R.drawable.y11_title_button);
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				step_one=2;
				code = checkCode.getText().toString();
				if (code != null && !code.trim().equals("") && code.length() == 6) {
					doRequestTask();
				} else {
					Toast.makeText(getApplicationContext(), "请填写正确的验证码", 1).show();
				}
			}
		});
	}

	CountDownTimer timer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			getCheckCode.setText("     " + millisUntilFinished / 1000 + "秒     ");
		}

		@Override
		public void onFinish() {
			getCheckCode.setBackgroundResource(R.drawable.bg_btn_gray_to_darkgray_selector);
			getCheckCode.setClickable(true);
			getCheckCode.setText("获取验证码");
		}
	};
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == R.id.forget_password_getcheckcode_button) {
				step_one=1;
				mobile = phoneNumber.getText().toString();
				if (mobile != null && !mobile.trim().equals("") && mobile.trim().length() == 11) {
					doRequestTask();
				} else {
					Toast.makeText(getApplicationContext(), "请填写正确的手机号码", 1).show();
				}
			}
		}
	};

	
	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		if(step_one == 1){
			
			SafelotteryHttpClient.post(this, "3105", "forgetPassword", initDate(step_one), new TypeMapHttpResponseHandler(this, true) {

				@Override
				public void onSuccess(int statusCode, Map mMap) {
					getCheckCode.setClickable(false);
					getCheckCode.setBackgroundResource(R.drawable.dialog_bottom_button_press);
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

		}else if(step_one == 2){
			
			SafelotteryHttpClient.post(this, "3106", "forgetPassword", initDate(step_one), new TypeMapHttpResponseHandler(this, true) {

				@Override
				public void onSuccess(int statusCode, Map mMap) {
				    String userCode= (String) mMap.get("userCode");
					Intent intent = new Intent(ForgetPasswordActivity.this, NewPasswordActivity.class);
					intent.putExtra("userCode", userCode);
					startActivity(intent);
					finish();
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

	public String initDate(int step) {
		Map<String, String> map = null;
		if (step == 1) {
			map = new HashMap<String, String>();
			map.put("mobile", mobile);
		} else if (step == 2) {
			String code = checkCode.getText().toString();
			map = new HashMap<String, String>();
			map.put("mobile", mobile);
			map.put("code", code);
		}
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

}