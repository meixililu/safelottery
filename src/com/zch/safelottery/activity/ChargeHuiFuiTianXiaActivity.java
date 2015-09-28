package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

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
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
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

public class ChargeHuiFuiTianXiaActivity extends ZCHBaseActivity {

	private EditText chargeET;
	private Button chargeBtn;
	private LinearLayout chargeLayout;

	private String money;
	private TextView banktx,shownotice,accountway;

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
		shownotice=(TextView)findViewById(R.id.shownotice);
		accountway=(TextView)findViewById(R.id.accountway);
		accountway.setText("银行卡快捷支付");
		banktx=(TextView)findViewById(R.id.showBank);
		banktx.setText(R.string.bank3);
		shownotice.setText(R.string.limit);
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		chargeBtn.setOnClickListener(myOnClickListener);
		chargeLayout.setOnClickListener(myOnClickListener);
	}

	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		
		SafelotteryHttpClient.post(this, "3202", "hftx", initDate(), new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					String url=(String) mMap.get("requestUrl");
					if(TextUtils.isEmpty(url)){
						Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
						startActivityForResult(intent, 0);
					}else{
						LogUtil.CustomLog("TAG", "ChargeHuiFuiTianXiaActivity -- URL is null");
						ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
					}
				}else{
					LogUtil.CustomLog("TAG", "ChargeHuiFuiTianXiaActivity -- Map is null");
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

		StatService.onEvent(ChargeHuiFuiTianXiaActivity.this, "total-fund", "所有充值提交请求-信用卡", 1);
	}

	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		map.put("amount", money);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(ChargeHuiFuiTianXiaActivity.this);
			dialog.setContent("充值完成后，请耐心等待，刷新账户查询是否到账！");
			dialog.setOk_btn_text("刷新账户");
			dialog.setCancle_btn_text("继续充值");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					GetString.isAccountNeedRefresh = true;
					ChargeHuiFuiTianXiaActivity.this.finish();
					Intent intent = new Intent();
					intent.setClass(ChargeHuiFuiTianXiaActivity.this, MainTabActivity.class);
					intent.putExtra(Settings.TABHOST, Settings.USERHOME);
					ChargeHuiFuiTianXiaActivity.this.startActivity(intent);
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
					doRequestTask();
				}
				break;
			case R.id.alipay_safe_charge_zch_tell:
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(ChargeHuiFuiTianXiaActivity.this);
					dialog.show();
				}
				break;
			}
		}
	}

}