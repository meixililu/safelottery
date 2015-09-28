package com.zch.safelottery.activity;

import android.os.Bundle;

import com.baidu.mobstat.StatActivity;

public class BaseTabActivity extends StatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
