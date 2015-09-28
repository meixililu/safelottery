package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.view.TitleViews;

public class NewPasswordActivity extends ZCHBaseActivity {
	
	private EditText newPassword;
	private EditText rePassword;
	private String userCode;
	private String password1;

	private RelativeLayout title;
	
	private TitleViews titleViews;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_password);
        try {
        	   userCode = getIntent().getStringExtra("userCode");
               initViews();
               addTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void initViews() {
		title = (RelativeLayout) findViewById(R.id.new_password_title);
		newPassword = (EditText) findViewById(R.id.new_password_newpassword);
		rePassword = (EditText) findViewById(R.id.new_password_repassword);
	}

	private void addTitle(){
		titleViews = new TitleViews(this, "重置密码");
		titleViews.setBtnName("提交");
//		titleViews.setBtnIcon(R.drawable.y11_title_button);
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 password1 = newPassword.getText().toString();
				String password2 = rePassword.getText().toString();
				if(password1 == null || password1.trim().equals("")){
					Toast.makeText(getApplicationContext(), "请填写新密码", 1).show();
				}else if(password2 == null || password2.trim().equals("")){
					Toast.makeText(getApplicationContext(), "请填写重复密码", 1).show();
				}else if(!password1.equals(password2)){
					Toast.makeText(getApplicationContext(), "密码填写不一致", 1).show();
				}else{
					doRequestTask();
				}
			}
		});
	}
	
	private void doRequestTask() {

		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		
		SafelotteryHttpClient.post(this, "3104", "reset", initDate(), new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				Toast.makeText(getApplicationContext(), "密码修改成功", 1).show();
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
		String newmd5pwd= Md5.d5(password1);//新密码MD5加密
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", userCode);
		map.put("password", newmd5pwd);
		String str =  JsonUtils.toJsonStr(map);
		return str;
	}
}