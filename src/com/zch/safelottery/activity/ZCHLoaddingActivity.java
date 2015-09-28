package com.zch.safelottery.activity;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.MyAsyncTask;
import com.zch.safelottery.asynctask.MyAsyncTask.OnAsyncTaskListener;
import com.zch.safelottery.broadcast.BootReceiver;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.UpdateDialog;
import com.zch.safelottery.dialogs.UpdateDialog.OnDownLoadDialogClickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pull.PullSettings;
import com.zch.safelottery.setttings.LoginManager;
import com.zch.safelottery.setttings.SLManifest;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.ShortCut;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.DownloadFileUtil;
import com.zch.safelottery.util.VersionUpdateUtils;

public class ZCHLoaddingActivity extends ZCHBaseActivity {

//	private LinearLayout loading_bg_layout;

	private boolean isUpdateFinish = false;
	private boolean isForceUpdate = false;
	private boolean finishWaiting = false;

	private SharedPreferences sharedPrefs;
	// 第一次启动的话启动引导界面
	private int isFirstLoad;

	private MyAsyncTask waitTimeTask;
	private int result;
	private String downLoadUrl;
	private int newVersion = 0;
	private String subAppVersion;
	private String updateNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadding);
		try {
			/**打开百度统计debug，发布时需要关闭，注释此代码**/
			// StatService.setDebugOn(false);
			SystemInfo.initSystemInfo(this);
			initViews();
			checkVersionUpdate();// 启动后台线程
			/**自动登录**/
			LoginManager.AutoLoginRetryTime = 1;
			LoginManager.startAutologin(getApplicationContext());
			/**配置文件获取**/
			SLManifest.postLoadingBgAndSalesRequest(this, sharedPrefs);
			waitMinTime();
			/**添加快捷方式**/
			ShortCut.addShortcut(this, sharedPrefs);
		} catch (Exception e) {
			isUpdateFinish = true;
			finishWaiting = true;
			next();
			e.printStackTrace();
		}
	}

	private void initViews() throws Exception {
		sharedPrefs = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		isFirstLoad = sharedPrefs.getInt("isFirstLoad", 0);
//		loading_bg_layout = (LinearLayout) findViewById(R.id.loading_bg_layout);
//		ImageDownload.showLoadingBg(loading_bg_layout, sharedPrefs);
	}

	private void next() {
		if (!isForceUpdate) {
			if (isUpdateFinish && finishWaiting) {
//				if (isFirstLoad != SystemInfo.softVerCode) {
//					Intent intent = new Intent(ZCHLoaddingActivity.this, GuideActivity.class);
//					intent.putExtra("isFirstLoad", 1);
//					startActivity(intent);
//					finish();
//					setIsFirstLoad();
//				} else {
					Intent intent = new Intent();
					intent.setClass(ZCHLoaddingActivity.this, MainTabActivity.class);
					startActivity(intent);
					finish();
//				}
			}
		}
	}
	
	private void setIsFirstLoad() {
		Editor editor = sharedPrefs.edit();
		editor.putInt("isFirstLoad", SystemInfo.softVerCode);
		editor.putBoolean("isUpdate", true);
		editor.commit();
	}

	private void checkVersionUpdate() {

		//回调函数，用于处理版本更新过程中的操作
		VersionUpdateUtils.VersionUpdateAdapter callback = new VersionUpdateUtils.VersionUpdateAdapter() {
			@Override
			public void onComplete() {
				finishUpdate();
			}

			@Override
			public void onForceUpdate() {
				isForceUpdate = true;
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				finishUpdate();
			}
			@Override
			public void onCancleBtnClick() {
				finishUpdate();
			}
			@Override
			public void onBackgroundBtnClick() {
				finishUpdate();
			}
		};
		//执行更新
		new VersionUpdateUtils(this, callback, null, 4000).checkUpdate();
	}

	
	private void finishUpdate() {
		isUpdateFinish = true;
		next();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			finishUpdate();
		}
	}

	/**
	 * 防止在wifi情况下，接口请求过快导致页面没有展示就进入下一个页面了。
	 */
	private void waitMinTime() {
		waitTimeTask = new MyAsyncTask(this, false);
		waitTimeTask.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			@Override
			public Boolean onTaskBackgroundListener() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			public void onTaskPostExecuteListener() {
				finishWaiting = true;
				next();
			}
		});
		waitTimeTask.execute();
	}
}