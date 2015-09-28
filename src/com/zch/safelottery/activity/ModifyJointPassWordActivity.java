package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.view.TitleViews;

/*
 * 修改密码
 */
public class ModifyJointPassWordActivity extends ZCHBaseActivity {
    private String password;
	private EditText passwordEdx;
	private EditText rePasswordEdx;
	private Button modify_password_submit;
	private Class<?> toClass;
	private Bundle mBundle;
	
	private RelativeLayout title;
	
	private TitleViews titleViews;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modify_joint_password);
		initViews();
		addTitle();
		Intent intent = getIntent();
		if(intent != null){
	        	toClass = (Class<?>) intent.getSerializableExtra(Settings.TOCLASS);
	        	mBundle = intent.getBundleExtra(Settings.BUNDLE);
	        }
	}

	private void initViews() {
		title = (RelativeLayout) findViewById(R.id.modify_password_title);
		passwordEdx= (EditText) findViewById(R.id.dialog_password_ed);
		rePasswordEdx = (EditText) findViewById(R.id.dialog_Repeat_password_ed);
		modify_password_submit=(Button)findViewById(R.id.modify_password_submit);
		
		modify_password_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}

	private void addTitle(){
		titleViews = new TitleViews(this, "设置密码");
		titleViews.setBtnName("提交");
//		titleViews.setBtnIcon(R.drawable.y11_title_button);
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	private void submit(){
		password = passwordEdx.getText().toString().trim();
		String RepeatPassword = rePasswordEdx.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.diaplayMesLong(ModifyJointPassWordActivity.this, "请输入您的登录密码");
		} else if (password.length() < 6 && password.length() > 16) {
			ToastUtil.diaplayMesLong(ModifyJointPassWordActivity.this, "密码长度必须在6-16位之间");
		} else if (!password.equals(RepeatPassword)) {
			ToastUtil.diaplayMesLong(ModifyJointPassWordActivity.this, "两次输入密码不一致");
		}else{
			doRequestTask();
		}
	}

	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		SafelotteryHttpClient.post(this, "3104", "reset", initDate(), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				progresdialog.dismiss();
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				replaceSavePassword();
				backToHome();
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
		});
	}
	public void replaceSavePassword() {
		SharedPreferences loginPreference = ModifyJointPassWordActivity.this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = loginPreference.edit();
		editor.putString(Settings.LOGIN_PASSWORD, password);
		editor.commit();
	}
	
	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, String> map = new HashMap<String, String>();
		password = Md5.d5(password);
		map.put("userCode",userCode);
		map.put("password", password);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}
	
	/**
	 * 设置成功后跳转到目标Activity
	 */
	private void backToHome(){
		if(toClass != null){
			Intent intent = new Intent(this, toClass);
			if(mBundle != null){
				intent.putExtra(Settings.BUNDLE, mBundle);
			}
			this.startActivity(intent);
		}
		finish();
	}
	
}