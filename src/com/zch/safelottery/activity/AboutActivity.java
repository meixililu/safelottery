package com.zch.safelottery.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;

public class AboutActivity extends ZCHBaseActivity {

	private LinearLayout tell;
	private TextView version;
	private TextView showSidTV;
	private LinearLayout internet;
	private LinearLayout blog;
	private LinearLayout email;
	private LinearLayout wap;

	private Context context;

	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (v.getId() == R.id.aboat_tell) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(
							AboutActivity.this);
					dialog.show();
				}
			} else if (v.getId() == R.id.aboat_internet) {
//				StatService.onEvent(getApplicationContext(),
//						BaiduStatistics.AboutUs, "官网", 1);

				Intent intent = new Intent("android.intent.action.VIEW",
						Uri.parse("http://www.zch168.com"));// new
															// Intent("android.intent.action.VIEW",
															// "http://www.zch168.com");
				startActivity(intent);
			} else if (v.getId() == R.id.aboat_blog) {
//				StatService.onEvent(getApplicationContext(),
//						BaiduStatistics.AboutUs, "微博", 1);

				Intent intent = new Intent();
				intent.setClass(context, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL,
						"http://weibo.com/zhongcaipiao168");
				startActivity(intent);
			} else if (v.getId() == R.id.more_modify) {
//				StatService.onEvent(getApplicationContext(),
//						BaiduStatistics.AboutUs, "邮箱", 1);

				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "kefu@zch168.net" });
				startActivity(emailIntent);
			} else if (v.getId() == R.id.aboat_wap) {
//				StatService.onEvent(getApplicationContext(),
//						BaiduStatistics.AboutUs, "wap", 1);

				Intent intent = new Intent("android.intent.action.VIEW",
						Uri.parse("http://wap.zch168.com"));

				startActivity(intent);
			} else if (v.getId() == R.id.setting_info_remind_text) {
				ToastUtil.diaplayMesShort(getApplicationContext(), SystemInfo.sid);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			context = AboutActivity.this;
			setContentView(R.layout.aboat);
			initViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initViews() {
		internet = (LinearLayout) findViewById(R.id.aboat_internet);
		blog = (LinearLayout) findViewById(R.id.aboat_blog);
		tell = (LinearLayout) findViewById(R.id.aboat_tell);
		email = (LinearLayout) findViewById(R.id.more_modify);
		wap = (LinearLayout) findViewById(R.id.aboat_wap);

		showSidTV = (TextView) findViewById(R.id.setting_info_remind_text);

		if (Settings.isNeedUrlAndUpdate(getApplicationContext())) {
			internet.setOnClickListener(onClickListener);
			email.setOnClickListener(onClickListener);
			blog.setOnClickListener(onClickListener);
			wap.setOnClickListener(onClickListener);
		}
		showSidTV.setOnClickListener(onClickListener);
		tell.setOnClickListener(onClickListener);
		version = (TextView) findViewById(R.id.aboat_version);

		if (GetString.TEST) {
			String test = "-060301-"; //每次打完测试包 +1
			version.setText("测试版本号：" + SystemInfo.softVer + test + SystemInfo.sid);
			version.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Settings.changeToPublicVersion();
				}
			});
		} else {
			version.setText("版本号：" + SystemInfo.softVer);
		}
	}
}
