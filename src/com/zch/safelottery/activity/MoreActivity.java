package com.zch.safelottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.android.app.pay.PayTask;
import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.VersionUpdateUtils;

/*
 * 更多界面
 */
public class MoreActivity extends BaseTabActivity {

	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "MoreActivity";

	private LinearLayout help;
	private LinearLayout setting;
	private LinearLayout more_modify;
	private LinearLayout update;
	private LinearLayout aboat;
	private LinearLayout send;
	private LinearLayout guide;
	private LinearLayout changeAccount;

	private TextView changeAccountText;

	ProgressDialog progressDialog;

	private Context context;

	private int newVersion;
	private int result;
	private String downLoadUrl;
	private String subAppVersion;
	private String updateNote;
	private ImageView tab_img_id;

	private TextView modify_tx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = MoreActivity.this;
		setContentView(R.layout.more);
		initUI();
	}

	private void initUI() {
		help = (LinearLayout) findViewById(R.id.more_help);
		setting = (LinearLayout) findViewById(R.id.more_setting);
		more_modify = (LinearLayout) findViewById(R.id.more_modify);
		update = (LinearLayout) findViewById(R.id.more_update);
		send = (LinearLayout) findViewById(R.id.more_send_ta);
		aboat = (LinearLayout) findViewById(R.id.more_aboat);
		guide = (LinearLayout) findViewById(R.id.more_guide);
		changeAccount = (LinearLayout) findViewById(R.id.more_change_account);
		changeAccountText = (TextView) findViewById(R.id.more_change_account_text);

		tab_img_id = (ImageView) findViewById(R.id.tab_img_id);
		LinearLayout more_update = (LinearLayout) findViewById(R.id.more_update);
		ImageView more_update_line = (ImageView) findViewById(R.id.more_update_line);
		if (Settings.isNeedUpdateBtn(context)) {
			more_update.setVisibility(View.GONE);
			more_update_line.setVisibility(View.GONE);
		}

		modify_tx = (TextView) findViewById(R.id.modify_tx);
		if (Settings.isSettingPassword(this)) {
			modify_tx.setText("设置密码");
		}

		help.setOnClickListener(onClickListener);
		setting.setOnClickListener(onClickListener);
		more_modify.setOnClickListener(onClickListener);
		update.setOnClickListener(onClickListener);
		aboat.setOnClickListener(onClickListener);
		send.setOnClickListener(onClickListener);
		guide.setOnClickListener(onClickListener);
		changeAccount.setOnClickListener(onClickListener);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (GetString.isLogin) {
			changeAccountText.setText("退出登录");
		} else {
			changeAccountText.setText("登录");
		}

		if (Settings.isSettingPassword(MoreActivity.this)) {
			modify_tx.setText("设置密码");
		} else {
			modify_tx.setText("修改密码");
		}
	}

	private void checkVersionUpdate() {
		VersionUpdateUtils.VersionUpdateAdapter callback = new VersionUpdateUtils.VersionUpdateAdapter() {
			@Override
			public void onComplete() {
				ToastUtil.diaplayMesShort(context, "您的软件已是最新版本");
			}
		};
		new VersionUpdateUtils(this, callback, null, true).checkUpdate();
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (v.getId() == R.id.more_help) {
				// 静默下载代码
				PayTask.initialization(context,"2088701406173485");
				Intent intent = new Intent(context, HelpActivity.class);
				startActivity(intent);
				StatService.onEvent(context, "more-help","更多-购彩帮助", 1);
			} else if (v.getId() == R.id.more_setting) {
				if (GetString.isLogin) {
					Intent intent_setting = new Intent(MoreActivity.this,UserSettingsActivity.class);
					startActivity(intent_setting);
				} else {
					Intent intent = new Intent(MoreActivity.this,LoginActivity.class);
					intent.putExtra(Settings.TOCLASS,UserSettingsActivity.class);
					startActivity(intent);
				}
				StatService.onEvent(context, "more-setup","更多-软件设置", 1);
			} else if (v.getId() == R.id.more_modify) {
				modifyPassword();
				StatService.onEvent(context,"more-errorreport", "更多-问题反馈", 1);
			} else if (v.getId() == R.id.more_update) {
				checkVersionUpdate();
				StatService.onEvent(context,"more-updatecheck", "更多-检查更新", 1);
			} else if (v.getId() == R.id.more_aboat) {
				Intent intent = new Intent(context, AboutActivity.class);
				startActivity(intent);
				StatService.onEvent(context, "more-aboutme","更多-关于我们", 1);
			} else if (v.getId() == R.id.more_send_ta) {
				if (GetString.isLogin) {
					Intent intent = new Intent(context,SendLotteryActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(context, LoginActivity.class);
					intent.putExtra("from", 9);
					intent.putExtra(Settings.TOCLASS, SendLotteryActivity.class);
					startActivity(intent);
				}
			} else if (v.getId() == R.id.more_guide) {
				Intent intent = new Intent(context, GuideActivity.class);
				startActivity(intent);
				StatService.onEvent(context, "more-guide","更多-新手指南", 1);
			} else if (v.getId() == R.id.more_change_account) {
				if (GetString.isLogin) {
					GetString.isLogin = false;
					GetString.userInfo = null;

					changeAccountText.setText("登录");
					UserHomeActivity.isFirstRegister = "";
					ToastUtil.diaplayMesShort(context, "成功退出登录");
				} else {
					Intent intent = new Intent(context, LoginActivity.class);
					startActivity(intent);
				}
				StatService.onEvent(context,"more-changeaccount", "更多-退出登录/登录", 1);
			}
		}
	};
	
	private void modifyPassword(){
		if(GetString.isLogin){
			if(Settings.isSettingPassword(context)){
				Intent intent =new Intent(context,ModifyJointPassWordActivity.class);
				intent.putExtra(Settings.TOCLASS,BoundUserPhoneActivity.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent(context,ModifyPassWordActivity.class);
				startActivity(intent);
			}
		}else{
			Intent intent = new Intent(context,LoginActivity.class);
			intent.putExtra("from", 9);
			intent.putExtra(Settings.TOCLASS, ModifyPassWordActivity.class);
			startActivity(intent);
		}
	}
}
