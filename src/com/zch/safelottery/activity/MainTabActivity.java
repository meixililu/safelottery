package com.zch.safelottery.activity;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zch.safelottery.R;
import com.zch.safelottery.dialogs.WorldcupDialog;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.jingcai.WorldcupAlarmActivity;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pull.PullSettings;
import com.zch.safelottery.setttings.SLManifest;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.ShortCut;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.AlarmUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.SharedPreferencesOperate;
import com.zch.safelottery.util.TabsUtil;
import com.zch.safelottery.util.WorldcupUtil;

public class MainTabActivity extends TabActivity implements View.OnClickListener {

	private FrameLayout item_bayuan,item_motu,item_alarm;
	public static final boolean DEBUG = Settings.DEBUG;
	private TabHost mTabHost;
	private ProgressDialog dialog;
	private SharedPreferences sharedPrefs;
	private LinearLayout main_page_tese;
	private View item_top,item_bottom;
	int currentView = 0;
	public static int maxTabIndex = 4;
	private long exitTime = 0;
	private ImageView tab_img,tab_tese_img;
	private boolean isAnimation;
	private String mBaYuanBaoZhong,mNaoZhong;

	private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DEBUG) Log.d(Settings.TAG, "MainTabActivity-onReceive");
			int index = intent.getIntExtra(Settings.TABHOST, -1);
			int newestMessage = intent.getIntExtra(Settings.NewestMessage, -1);
			int exit = intent.getIntExtra(Settings.EXIT, -1);
			if (index > -1 && index <= maxTabIndex) {
				if (mTabHost != null) {
					mTabHost.setCurrentTab(index);
				}
			} 
			if (exit == Settings.EXIT_ALL) {
				finish();
			}
			if (newestMessage == Settings.ShowMessage) {
				tab_img.setVisibility(View.VISIBLE);
				UserHomeActivity.isHasUpdateNews = true;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			registerReceiver(mLoggedOutReceiver, new IntentFilter(SafeApplication.INTENT_ACTION_ALLACTIVITY));
			setContentView(R.layout.main_tab_activity);
			initTabHost();
			/**获取最新**/
	        ShortCut.getNewestMessage(this);
	        ShortCut.getPrivateCountMessage(this);
	        /**激活**/
			ShortCut.activate(getApplicationContext(), sharedPrefs);
			/**启动推送服务**/
			AlarmUtil.startAlarm(getApplicationContext(), PullSettings.Pull_TriggerAtTime, PullSettings.Pull_Interval_Time);
			
			boolean isHasReboot = sharedPrefs.getBoolean(WorldcupAlarmActivity.WorldCupIsReboot, false);
			if(isHasReboot){
				WorldcupAlarmActivity.resetAlarmAfterReboot(this);
				Settings.saveSharedPreferences(sharedPrefs, WorldcupAlarmActivity.WorldCupIsReboot, false);
			}
			showWorldcupDialog();
		} catch (Exception e) {
			LogUtil.ExceptionLog("MainTabActivity-onCreate()");
			e.printStackTrace();
		}
	}
	
	private void showWorldcupDialog(){
		String isShowWorldcup = sharedPrefs.getString(SLManifest.ShouYeTanCeng, "0");
		if(isShowWorldcup.equals("1")){
			if(WorldcupUtil.isNeedShowWorldcupDialog(sharedPrefs.getLong(WorldcupUtil.lastTimeShowWorldcupDialgoKey, 0))){
				WorldcupDialog mWorldcupDialog = new WorldcupDialog(this);
				mWorldcupDialog.setCanceledOnTouchOutside(false);
				mWorldcupDialog.show();
				Settings.saveSharedPreferences(sharedPrefs, WorldcupUtil.lastTimeShowWorldcupDialgoKey, System.currentTimeMillis());
			}
		}
	}
	
	private void initTabHost() {
		if (DEBUG) Log.d(Settings.TAG, "MainTabActivity-initTabHost()");
		sharedPrefs = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		mTabHost = (TabHost) getTabHost();
		item_top = (View) findViewById(R.id.main_page_item_top);
		item_bottom = (View) findViewById(R.id.main_page_item_bottom);
		main_page_tese = (LinearLayout) findViewById(R.id.main_page_tese);
		item_bayuan = (FrameLayout) findViewById(R.id.main_page_item_bayuan);
		item_motu = (FrameLayout) findViewById(R.id.main_page_item_motu);
		item_alarm = (FrameLayout) findViewById(R.id.main_page_item_alarm);
		item_bayuan.setOnClickListener(this);
		item_motu.setOnClickListener(this);
		item_alarm.setOnClickListener(this);
		item_top.setOnClickListener(this);
		item_bottom.setOnClickListener(this);

		TabsUtil.addTab(mTabHost, getString(R.string.tab_main_nav_buy), R.drawable.tab_nav_icon_shop_selector, 0, new Intent(this, BuyLotteryActivity.class));// BuyLotteryActivity
		TabsUtil.addTab(mTabHost, getString(R.string.tab_main_nav_combine), R.drawable.tab_nav_combine_selector, 1, new Intent(this, CombineHallActivity.class));// BuyLotteryActivity
		TabsUtil.addTab(mTabHost, "", R.drawable.tab_nav_icon_tese_n, 2, new Intent(this, MoreActivity.class));
		TabsUtil.addTab(mTabHost, getString(R.string.tab_main_nav_lottery_hall), R.drawable.tab_nav_icon_hall_selector, 3, new Intent(this, LotteryResultHallActivity.class));// LotteryResultHallActivity
		
		tab_img = TabsUtil.addTab(mTabHost, getString(R.string.tab_main_nav_personcentered), R.drawable.tab_nav_icon_person_selector, 4, new Intent(this, UserHomeActivity.class));
		
		View mView2 = mTabHost.getTabWidget().getChildAt(2);
		tab_tese_img = (ImageView) mView2.findViewById(R.id.ivIcon);
		
		for(int i = 0; i < 5; i++){
			final int id = i;
			mTabHost.getTabWidget().getChildAt(id).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(id == 2){
						animation();
					}else {
						if(id == Settings.USERHOME){
							if (!GetString.isLogin) {
								toLogin();
							}else{
								mTabHost.setCurrentTab(id);
								currentView = id;
							}
						}else{
							mTabHost.setCurrentTab(id);
							currentView = id;
						}
					}
					baiduState(id);
				}
			});
			mTabHost.getTabWidget().getChildAt(id).setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(id == 2){
						animation();
					}else {
						if(id == Settings.USERHOME){
							if (hasFocus && !GetString.isLogin) {
								toLogin();
								currentView = id;
							}
						}else{
							mTabHost.setCurrentTab(id);
							currentView = id;
						}
					}
					baiduState(id);
				}
			});
		}
	}
	
	private void baiduState(int id){
		if(id == 0){
			StatService.onEvent(MainTabActivity.this, "nav-home", "导航-购买彩票", 1);
		}else if(id == 1){
			StatService.onEvent(MainTabActivity.this, "nav-joining", "导航-合买大厅", 1);
		}else if(id == 2){
			StatService.onEvent(MainTabActivity.this, "nav-more", "导航-特色", 1);
		}else if(id == 3){
			StatService.onEvent(MainTabActivity.this, "nav-result", "导航-开奖大厅", 1);
		}else if(id == 4){
			tab_img.setVisibility(View.GONE);
			StatService.onEvent(MainTabActivity.this, "nav-account", "导航-个人中心", 1);
		}
	}
	
	private void isShowActivity(){
		mBaYuanBaoZhong = sharedPrefs.getString(SLManifest.BaYuanBaoZhongKey, "0");
		mNaoZhong = sharedPrefs.getString(SLManifest.NaoZhong, "0");
		if(mBaYuanBaoZhong.equals("1")){
			item_bayuan.setVisibility(View.VISIBLE);
		}else{
			item_bayuan.setVisibility(View.GONE);
		}
		if(mNaoZhong.equals("1")){
			item_alarm.setVisibility(View.VISIBLE);
		}else{
			item_alarm.setVisibility(View.GONE);
		}
	}
	
	private void animation(){
		isShowActivity();
		View mView = mTabHost.getTabWidget().getChildAt(0);
		View mView1 = mTabHost.getTabWidget().getChildAt(1);
		View mView2 = mTabHost.getTabWidget().getChildAt(2);
		View mView3 = mTabHost.getTabWidget().getChildAt(3);
		View mView4 = mTabHost.getTabWidget().getChildAt(4);
		if(!isAnimation){
			isAnimation = true;
			main_page_tese.setVisibility(View.VISIBLE);
			
			ObjectAnimator itemAnimator1 = ObjectAnimator.ofFloat(item_bayuan, "y", SystemInfo.height, item_bayuan.getTop());
			itemAnimator1.setDuration(500).start();
			ObjectAnimator itemAnimator2 = ObjectAnimator.ofFloat(item_motu, "y", SystemInfo.height, item_motu.getTop());
			itemAnimator2.setDuration(500).start();
			ObjectAnimator itemAnimator4 = ObjectAnimator.ofFloat(item_alarm, "y", SystemInfo.height, item_alarm.getTop());
			itemAnimator4.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(mView, "x", mView.getLeft(), -(mView.getRight()+100));
			mObjectAnimator.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(mView1, "x", mView1.getLeft(), -(mView1.getRight()+100));
			mObjectAnimator1.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator3 = ObjectAnimator.ofFloat(mView3, "x", mView3.getLeft(), SystemInfo.width+100);
			mObjectAnimator3.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator4 = ObjectAnimator.ofFloat(mView4, "x", mView4.getLeft(), SystemInfo.width+100);
			mObjectAnimator4.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(mView2, "rotation", 0f, 720f);
			mObjectAnimator2.addListener(mListener);
			mObjectAnimator2.setDuration(500).start();
			
			ObjectAnimator bgAnimator = ObjectAnimator.ofFloat(main_page_tese, "alpha", 0.3f, 1f);
			bgAnimator.setDuration(500).start();
		}else{
			isAnimation = false;
			
			ObjectAnimator itemAnimator1 = ObjectAnimator.ofFloat(item_bayuan, "y", item_bayuan.getTop(), -500);
			itemAnimator1.setDuration(500).start();
			ObjectAnimator itemAnimator2 = ObjectAnimator.ofFloat(item_motu, "y", item_motu.getTop(), -450);
			itemAnimator2.setDuration(500).start();
			ObjectAnimator itemAnimator4 = ObjectAnimator.ofFloat(item_alarm, "y", item_alarm.getTop(), -400);
			itemAnimator4.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(mView, "x", -300, mView.getLeft());
			mObjectAnimator.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(mView1, "x", -300, mView1.getLeft());
			mObjectAnimator1.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator3 = ObjectAnimator.ofFloat(mView3, "x", SystemInfo.width+300, mView3.getLeft());
			mObjectAnimator3.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator4 = ObjectAnimator.ofFloat(mView4, "x", SystemInfo.width+300, mView4.getLeft());
			mObjectAnimator4.setDuration(500).start();
			
			ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(mView2, "rotation", 720f, 0f);
			mObjectAnimator2.addListener(mListener);
			mObjectAnimator2.setDuration(500).start();
			
		}
	}
	
	private AnimatorListener mListener = new AnimatorListener() {
		@Override
		public void onAnimationStart(Animator arg0) {
		}
		@Override
		public void onAnimationRepeat(Animator arg0) {
		}
		@Override
		public void onAnimationEnd(Animator arg0) {
			if(isAnimation){
				tab_tese_img.setImageResource(R.drawable.tab_nav_icon_tese_s);
			}else{
				tab_tese_img.setImageResource(R.drawable.tab_nav_icon_tese_n);
				ObjectAnimator bgAnimator = ObjectAnimator.ofFloat(main_page_tese, "alpha", 1f, 0f);
				bgAnimator.addListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator arg0) {
					}
					@Override
					public void onAnimationRepeat(Animator arg0) {
					}
					@Override
					public void onAnimationEnd(Animator arg0) {
						main_page_tese.setVisibility(View.GONE);
					}
					@Override
					public void onAnimationCancel(Animator arg0) {
					}
				});
				bgAnimator.setDuration(300).start();
			}
		}
		@Override
		public void onAnimationCancel(Animator arg0) {
		}
	};

	private void toLogin() {
		Intent intent = new Intent(MainTabActivity.this, LoginActivity.class);
		intent.putExtra("from", Settings.USERHOME);
		intent.putExtra(Settings.TOCLASS, MainTabActivity.class);
		startActivity(intent);
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			int tabNum = intent.getIntExtra(Settings.TABHOST, 0);
			mTabHost.setCurrentTab(tabNum);
			currentView = tabNum;
		}
	}

	private static final int newest = 1;
	private static final int log = 2;
	private static final int help = 3;
	private static final int setting = 4;
	private static final int register = 5;

	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.clear();
		onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				onKeyDown(KeyEvent.KEYCODE_BACK, event);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", 0).show();
				exitTime = System.currentTimeMillis();
			} else {
				String str = JsonUtils.toJsonStr(GetString.userInfo);
				if(!TextUtils.isEmpty(str)) SharedPreferencesOperate.updateUserInfo(this, SharedPreferencesOperate.SAVE_USER_INFO, str);
				
				logout();
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mLoggedOutReceiver);
	}

	/**
	 * 注销
	 */
	public void logout() {
		GetString.isLogin = false;
		GetString.userInfo = null;
		MessageCenterDetails2Activity.Count = 0;
		if (mTabHost.getCurrentTab() == Settings.USERHOME) {
			mTabHost.setCurrentTab(0);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.main_page_item_bayuan :
			gotoWebActivity("竞彩8元保中", GetString.RMB_8_YUAN_SERVER, true);
			break;
		case R.id.main_page_item_motu :
			toMotuActivity();
			break;
		case R.id.main_page_item_alarm :
			toAlarm();
			break;
		}
	}
	
	private void toMotuActivity(){
		Intent intent = new Intent(this,MotuActivity.class);
		startActivity(intent);
	}
	
	private void toAlarm(){
		Intent intent = new Intent(this, WorldcupAlarmActivity.class);
		this.startActivity(intent);
	}
	
	private void gotoWebActivity(String title, String url, boolean isAddJsInterface){
		Intent intent = new Intent(this,WebViewTitleBarActivity.class);
		intent.putExtra(WebViewTitleBarActivity.TITLE, title);
		intent.putExtra(WebViewTitleBarActivity.URL, url);
		intent.putExtra(WebViewTitleBarActivity.IS_ADD_JS_INTERFACE, isAddJsInterface);
		startActivity(intent);
	}

}
