package com.zch.safelottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;

/*
 * 账户充值
 */
public class AccountChargeMoreActivity extends ZCHBaseActivity {
	private View charge_offline_bank;
	private View huifutianxia;
	private static AccountChargeMoreActivity thisActivity;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_charge_more);
		thisActivity = this;
		initViews();
	}

	// 关闭当前Activity
	public static void callMeFinish() {
		thisActivity.finish();
	}

	private void initViews() {
		huifutianxia = (View) findViewById(R.id.account_charge_layout_kuaijie_huifu);
		charge_offline_bank = (View) findViewById(R.id.charge_offline_bank);
		huifutianxia.setOnClickListener(onClickListener);
		charge_offline_bank.setOnClickListener(onClickListener);
	}

	@Override
	protected void onDestroy() {
		thisActivity = null;
		super.onDestroy();
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

		 if (v.getId() == R.id.charge_offline_bank) {
//				StatService.onEvent(AccountChargeMoreActivity.this, BaiduStatistics.Charge, "线下转账（大额）", 1);
				Intent intent = new Intent(AccountChargeMoreActivity.this, ChargeOfflineBankActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_kuaijie_huifu) {
//				StatService.onEvent(AccountChargeMoreActivity.this, BaiduStatistics.Charge, "信用卡(快捷支付)", 1);
				Intent intent = new Intent(AccountChargeMoreActivity.this, ChargeHuiFuiTianXiaActivity.class);
				startActivity(intent);
			} 
		}
	};
}