package com.zch.safelottery.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.http.JsonHttpResponseHandler;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.ResultParser;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.util.ToastUtil;

/**
 * 验证登录密码
 * @author Jiang
 *
 */
public class PasswordCheckDialog extends Dialog {

	private String password;
	private int count; //计数，用来计算密码错误次数
	private Context mContext;
	private Button btn;
	private OnClickListener mClick;
	
	public PasswordCheckDialog(Context context) {
		super(context, R.style.dialog);
		this.mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_password_checking);
		
		final EditText ed = (EditText) findViewById(R.id.dialog_password_ed);
		
		btn = (Button) findViewById(R.id.dialog_password_confirm);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(count >= 5){
					ToastUtil.diaplayMesLong(mContext, "您的账户已被锁定，2小时后自动解锁");
				}else{
					password = ed.getText().toString().trim();
					if(!TextUtils.isEmpty(password)){
						if(password.length() >= 6 && password.length() <= 16){
							password = Md5.d5(password);
							doRequestTask();
						}else{
							ToastUtil.diaplayMesLong(mContext, "密码长度必须在6-16位之间");
						}
					}else{
						ToastUtil.diaplayMesLong(mContext, "请输入您的登录密码");
					}
				}
			}
		});
	}
	
	private void doRequestTask() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("password", password);
		SafelotteryHttpClient.post(mContext, "3104", "check", JsonUtils.toJsonStr(map), new JsonHttpResponseHandler() {
			
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					Result result = new ResultParser().parse(response);

					if(result != null){
						if(result.getCode().equals("0000")){//正确
							ToastUtil.diaplayMesShort(mContext, "验证通过");
							PasswordCheckDialog.this.dismiss();
							if(mClick != null){
								mClick.onClick();
							}
						}else{
							if(result.getResult() != null){
								try {
									JSONObject json = new JSONObject(result.getResult());
									String temp = null;
									if(json.has("errCount")) temp = json.getString("errCount");
									
									count = TextUtils.isEmpty(temp)? 0: Integer.parseInt(temp);
									
									if(count < 4){
										ToastUtil.diaplayMesShort(mContext, "您还有" + (5 - count) +"次机会");
									}else if(count == 4){
										ToastUtil.diaplayMesLong(mContext, "您还有1次机会，输错后提款、个人信息、手机绑定将被锁2小时");
									}else{
										ToastUtil.diaplayMesLong(mContext, "您的账户已被锁定，2小时后自动解锁");
									}
								} catch (NumberFormatException e) {
									e.printStackTrace();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								ToastUtil.diaplayMesShort(mContext, result.getMsg());
							}
						}
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				ToastUtil.diaplayMesShort(mContext, "请求失败，请重试");
			}
		});
	}

	public interface OnClickListener{
		public void onClick();
	}
	
	public void setOnClickListener(OnClickListener mClick){
		this.mClick = mClick;
	}
}
