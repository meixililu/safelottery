package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.LoginManager;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.view.TitleViews;

/*
 * 修改密码
 */
public class ModifyPassWordActivity extends ZCHBaseActivity {

	private EditText oldPassword;
	private EditText password;
	private EditText rePassword;
	private Button button;
	private String newmd5pwd;
	private String oldmd5pwd;
	private String firstpwd;
	private String oldpwd;

	private RelativeLayout title;
	
	private TitleViews titleViews;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modify_password_password);
		initViews();
		addTitle();
	}

	private void initViews() {
		title = (RelativeLayout) findViewById(R.id.modify_password_title);
		oldPassword = (EditText) findViewById(R.id.modify_password_oldpassword);
		password = (EditText) findViewById(R.id.modify_password_newpassword);
		rePassword = (EditText) findViewById(R.id.modify_password_repassword);
	}

	private void addTitle(){
		titleViews = new TitleViews(this, "修改密码");
		titleViews.setBtnName("提交");
//		titleViews.setBtnIcon(R.drawable.y11_title_button);
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				oldpwd = oldPassword.getText().toString().trim();
			    firstpwd = password.getText().toString().trim();
				String second = rePassword.getText().toString().trim();

				if (oldpwd == null || oldpwd.equals("")) {
					Toast.makeText(getApplicationContext(), "请填写原密码", Toast.LENGTH_SHORT).show();
				} else if (firstpwd == null || firstpwd.equals("")) {
					Toast.makeText(getApplicationContext(), "请填写新密码", Toast.LENGTH_SHORT).show();
				} else if (second == null || second.equals("")) {
					Toast.makeText(getApplicationContext(), "请填写重复密码", Toast.LENGTH_SHORT).show();
				} else if (!firstpwd.equals(second)) {
					Toast.makeText(getApplicationContext(), "两次密码填写不一致", Toast.LENGTH_SHORT).show();
				} else if (oldpwd.equals(firstpwd)) {
					Toast.makeText(getApplicationContext(), "新密码不能与原密码相同", Toast.LENGTH_SHORT).show();
				} else {
					doRequestTask();
				}
			}
		});
	}
	
	private void doRequestTask() {

		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		SafelotteryHttpClient.post(this, "3104", "edit", initDate(), new TypeResultHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
				LoginManager.saveLoginState(ModifyPassWordActivity.this,newmd5pwd);
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

	public String initDate() {
		newmd5pwd= Md5.d5(firstpwd);//新密码MD5加密
		oldmd5pwd=Md5.d5(oldpwd);//旧密码MD5加密
		String userCode = GetString.userInfo.getUserCode();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode",userCode);
		map.put("oldPassword", oldmd5pwd);
		map.put("password", newmd5pwd);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

}