package com.zch.safelottery.ctshuzicai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.baidu.mobstat.StatActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;

public class ZCHBaseActivity extends StatActivity {

	private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int index = intent.getIntExtra(Settings.EXIT, -1);
			if (index == Settings.EXIT_ALL || index == Settings.EXIT_OTHER) {
				if (index == Settings.EXIT_ALL) {
					clearShareAccount();
				}
				finish();
			} else {
				isNeedClose(index);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(mLoggedOutReceiver, new IntentFilter(SafeApplication.INTENT_ACTION_ALLACTIVITY));
	}

	/**
	 * 清除分享账号
	 */
	private void clearShareAccount() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mLoggedOutReceiver);
	}

	/**处理特殊广播需求，子类@Override此方法进行处理操作
	 * @param ationCode
	 */
	protected void isNeedClose(int ationCode) {
	}

}
