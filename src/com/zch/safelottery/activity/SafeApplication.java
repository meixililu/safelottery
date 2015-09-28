package com.zch.safelottery.activity;

import java.util.HashMap;

import android.app.Application;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zch.safelottery.crash.CrashHandler;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.share.LoginQQUtil;
import com.zch.safelottery.util.share.Login_WeiboUtil;
import com.zch.safelottery.util.share.WeiXinShare;

public class SafeApplication extends Application {

	/** 通过Map传ArrayList<BetNumberBean> **/
	public static final String MAP_LIST_KEY = "list";
	/** 通过Map传SelectInfoBean **/
	public static final String MAP_INFO_KEY = "info";
	/** 可以用于所有activity页面之间传送数据的静态map **/
	public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
	/** 启动主页面的intent action **/
	public static final String INTENT_ACTION_ALLACTIVITY = "com.zch.safelottery.intent.action.allactivity";

	public static QQAuth QQAuth;
	// IWXAPI 是第三方app和微信通信的openapi接口
	public static IWXAPI wxApi;
	
	 

	@SuppressWarnings("static-access")
	@Override
	public void onCreate() {
		// if (GetString.TEST && getSDKVersionNumber() > 8) {
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads()
		// .detectDiskWrites()
		// .detectNetwork() // or .detectAll() for all detectable problems
		// .penaltyLog()
		// .build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects()
		// // .detectLeakedClosableObjects()
		// .penaltyLog()
		// .penaltyDeath()
		// .build());
		// }
		super.onCreate();

		try {
			/** 注册全局未处理异常捕获Handler **/
			if (!GetString.TEST) {
				CrashHandler crashHandler = CrashHandler.getInstance();
				crashHandler.init(getApplicationContext());
				crashHandler.sendPreviousReportsToServer(); // 发送以前没发送的 报告(可选)
			}
			QQAuth = QQAuth.createInstance(LoginQQUtil.APP_ID, this);
			// 通过WXAPIFactory工厂，获取IWXAPI的实例
			wxApi = WXAPIFactory.createWXAPI(this, WeiXinShare.WEIXIN_APP_ID, false);
			wxApi.registerApp(WeiXinShare.WEIXIN_APP_ID);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}
}
