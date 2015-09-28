package com.zch.safelottery.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.setttings.Settings;


public class ChargeOfflineBankListActivity extends ZCHBaseActivity {
	private RelativeLayout charge_offline_bank_zch_tell;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.charge_offline_bank_list);
		charge_offline_bank_zch_tell=(RelativeLayout)findViewById(R.id.charge_offline_bank_zch_tell);
		charge_offline_bank_zch_tell.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog=new TelePhoneShowDialog(ChargeOfflineBankListActivity.this);
					dialog.show();
				}
			}
		});
	}
}
