package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.UserInfoHttpUtil;
import com.zch.safelottery.util.UserInfoHttpUtil.OnHttpListenter;

public class ChargeLdysActivity extends ZCHBaseActivity {

	private EditText chargeET;
	private Button chargeBtn;
	private LinearLayout chargeLayout;

	private String money;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_huifutianxia);
		initViews();
	}

	private void initViews() {
		chargeET = (EditText) findViewById(R.id.alipay_charge_fillin);
		chargeBtn = (Button) findViewById(R.id.alipay_charge_button);
		chargeLayout = (LinearLayout) findViewById(R.id.alipay_safe_charge_zch_tell);
		
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		chargeBtn.setOnClickListener(myOnClickListener);
		chargeLayout.setOnClickListener(myOnClickListener);
	}

	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		
		SafelotteryHttpClient.post(this, "3202", "umPay", initDate(), new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					String url=(String) mMap.get("requestUrl");
					if(!TextUtils.isEmpty(url)){
						Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
						startActivityForResult(intent,0);
					}else{
						LogUtil.CustomLog("TAG", "ChargeLdysActivity -- URL is null");
						ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
					}
				}else{
					LogUtil.CustomLog("TAG", "ChargeLdysActivity -- Map is null");
					ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
				}
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
		StatService.onEvent(ChargeLdysActivity.this, "total-fund", "所有充值提交请求-联动优势", 1);
	}

	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		map.put("amount", money);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

	 /**
	   * 检查是否绑定用户信息
	   * 
	   * @param toClass
	   */
	public static final String CREDIT_PAY_JSON_DATA_KEY= "CREDIT";
	private void checkBindBankAndUserInfo(){
		new UserInfoHttpUtil(ChargeLdysActivity.this, 1,new OnHttpListenter() {
			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
				if (GetString.userInfo != null) {
					String real = GetString.userInfo.getRealName();
					String idcard = GetString.userInfo.getCardCode();
					if (TextUtils.isEmpty(real) || TextUtils.isEmpty(idcard)) { // 未绑定身份证
						Intent intent = new Intent(ChargeLdysActivity.this,BoundUserInfoActivity.class);
						Bundle mBundle = new Bundle();
						//设置下一个跳转页面,在这里不会跳转，主要是为了判断是从这里过来的，特殊对待
						mBundle.putSerializable(Settings.TOCLASS, ChargeLdysActivity.class);
						mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_NOT_FINISH);
						mBundle.putString(CREDIT_PAY_JSON_DATA_KEY, initDate());
						intent.putExtra(Settings.BUNDLE, mBundle);
						startActivity(intent);
						
					} else {
						doRequestTask();
					}
				}else
					ToastUtil.diaplayMesShort(ChargeLdysActivity.this, "获取信息失败，请刷新重试");
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				ToastUtil.diaplayMesShort(ChargeLdysActivity.this, "获取信息失败，请刷新重试");
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(ChargeLdysActivity.this);
			dialog.setContent("充值完成后，请耐心等待，刷新账户查询是否到账！");
			dialog.setOk_btn_text("刷新账户");
			dialog.setCancle_btn_text("继续充值");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					GetString.isAccountNeedRefresh = true;
					ChargeLdysActivity.this.finish();
					Intent intent = new Intent();
					intent.setClass(ChargeLdysActivity.this, MainTabActivity.class);
					intent.putExtra(Settings.TABHOST, Settings.USERHOME);
					ChargeLdysActivity.this.startActivity(intent);
				}

				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		}
	}

	private class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alipay_charge_button:
				money = chargeET.getText().toString().trim();
				if (TextUtils.isEmpty(money) || money.equals("0")) {
					ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_prompt_bank);
				} else {
					checkBindBankAndUserInfo();
				}
				break;
			case R.id.alipay_safe_charge_zch_tell:
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(ChargeLdysActivity.this);
					dialog.show();
				}
				break;
			}
		}
	}

}