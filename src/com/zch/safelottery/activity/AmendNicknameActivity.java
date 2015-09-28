package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;

public class AmendNicknameActivity extends ZCHBaseActivity {

	private TextView tvNicknameOld;
	private EditText edNicknameNew;
	private Button btnConfirm;
	
	private String nicknameNew;
	private String nicknameOld;
	
	private OnFinishListener onFinish;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.amend_nickname);
			initView();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initView(){
		tvNicknameOld = (TextView) findViewById(R.id.amend_nickname_old);
		edNicknameNew = (EditText) findViewById(R.id.amend_nickname_new);
		btnConfirm = (Button) findViewById(R.id.amend_nickname_confirm);
		nicknameOld = GetString.userInfo.getNickname();
		tvNicknameOld.setText(nicknameOld);
		btnConfirm.setOnClickListener(onClick);
	}
	
	private String getRequest(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("nickName", nicknameNew);
		return JsonUtils.toJsonStr(map);
	}
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == btnConfirm){
				nicknameNew = edNicknameNew.getText().toString();
				if(TextUtils.isEmpty(nicknameNew)){
					ToastUtil.diaplayMesLong(AmendNicknameActivity.this, "新昵称不能为空");
				} else if(nicknameNew.equals(nicknameOld)){
					ToastUtil.diaplayMesLong(AmendNicknameActivity.this, "新昵称不能与旧昵称相同");
				} else if(nicknameNew.length() < 2){
					ToastUtil.diaplayMesLong(AmendNicknameActivity.this, "长度不能小于2");
				} else if(nicknameNew.length() > 16){
					ToastUtil.diaplayMesLong(AmendNicknameActivity.this, "长度不能大于16");
				} else if(RegisterActivity.isUserNameLegal(nicknameNew)){
					ToastUtil.diaplayMesLong(AmendNicknameActivity.this, "用户名只能含有汉字、数字、字母、下划线");
				}else{
					StatService.onEvent(getApplicationContext(), "account-rename", "个人中心-修改昵称提交", 1);
					httpConnect(getRequest());
				}
			}
		}
	};
	
	private void httpConnect(String request){
		final ProgressDialog progressDialog =  ProgressDialog.show(this, "", "正在修改...", true, true);
		SafelotteryHttpClient.post(this, "3103", "nickname", request, new TypeResultHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try{
					ToastUtil.diaplayMesLong(AmendNicknameActivity.this, "修改成功");
					GetString.userInfo.setNickname(nicknameNew);
					if(onFinish != null){
						onFinish.onFinish(nicknameNew);
					}
					finish();
				}catch (Exception e) {
					e.printStackTrace();
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
		});
	}
	
	public void setOnFinish(OnFinishListener onFinish){
		this.onFinish = onFinish;
	}
	
	public interface OnFinishListener{
		public void onFinish(String msg);
	}
}
