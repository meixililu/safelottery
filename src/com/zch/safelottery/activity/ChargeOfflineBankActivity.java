package com.zch.safelottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;


public class ChargeOfflineBankActivity extends ZCHBaseActivity {
	private LinearLayout nongyelayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.charge_offline_bank);
		initview();
	}

	public void initview(){
		nongyelayout=(LinearLayout)findViewById(R.id.charge_offline_bank_nongye);
		nongyelayout.setOnClickListener(onClickListener);
	}
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (v.getId() == R.id.charge_offline_bank_nongye) {
				Intent intent = new Intent(ChargeOfflineBankActivity.this, ChargeOfflineBankListActivity.class);
				startActivity(intent);
			} 
		}
	};
}
