package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.MainTabActivity;
import com.zch.safelottery.activity.WebViewTitleBarActivity;
import com.zch.safelottery.asynctask.MyAsyncTask;
import com.zch.safelottery.asynctask.MyAsyncTask.OnAsyncTaskListener;
import com.zch.safelottery.broadcast.SafelotteryBroadcastReceiver;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.jingcai.JZCGJActivity;
import com.zch.safelottery.jingcai.WorldcupAlarmActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.util.ToastUtil;

/**
 * 自定义退出dialog
 * 
 * @author Messi
 * 
 */
public class WorldcupDialog extends Dialog implements android.view.View.OnClickListener{

	public static final String IsMuteKey = "IsMuteKey";
	private Context context;
	private ImageView worldcup_dialog_close;
	private LinearLayout worldcup_dialog_banner_img;
	private FrameLayout worldcup_dialog_custom,worldcup_dialog_cgj,worldcup_dialog_jzbet,worldcup_dialog_by;
	private WebView mWebView;
	private CheckBox worldcup_dialog_voice_cb;
	private MediaPlayer mp;
	private MyAsyncTask waitTimeTask;
	private SharedPreferences spf;
	private boolean isMute;
	private boolean isError;
	private String url = "http://m.zch168.com/htmls/activity/client_jj.jsp";
	
	
	public WorldcupDialog(Context mContext) {
		super(mContext, R.style.dialog);
		context = mContext;
		mp = new MediaPlayer();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worldcup_dialog);
		try {
			init();
			waitMinTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void playMusic() throws Exception{
		Uri mUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + SafelotteryBroadcastReceiver.randomNotificationVoice());
		mp.reset();
		mp.setDataSource(context, mUri);
		mp.prepare();
		mp.start();
	}
	
	private void init(){
		spf = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		worldcup_dialog_close = (ImageView) findViewById(R.id.worldcup_dialog_close);
		worldcup_dialog_voice_cb = (CheckBox) findViewById(R.id.worldcup_dialog_voice_cb);
		worldcup_dialog_banner_img = (LinearLayout) findViewById(R.id.worldcup_dialog_banner_img);
		worldcup_dialog_custom = (FrameLayout) findViewById(R.id.worldcup_dialog_custom);
		worldcup_dialog_cgj = (FrameLayout) findViewById(R.id.worldcup_dialog_cgj);
		worldcup_dialog_jzbet = (FrameLayout) findViewById(R.id.worldcup_dialog_jzbet);
		worldcup_dialog_by = (FrameLayout) findViewById(R.id.worldcup_dialog_by);
		mWebView = (WebView) findViewById(R.id.worldcup_dialog_wb);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName("UTF-8") ;
		isMute = spf.getBoolean(IsMuteKey, false);
		
		Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.worldcup_dialog_title_img);
		ImageUtil.adjustImgSize(rawBitmap, worldcup_dialog_banner_img, ScreenUtil.dip2px(context, 8));
		worldcup_dialog_voice_cb.setChecked(isMute);
		worldcup_dialog_close.setOnClickListener(this);
		worldcup_dialog_custom.setOnClickListener(this);
		worldcup_dialog_cgj.setOnClickListener(this);
		worldcup_dialog_jzbet.setOnClickListener(this);
		worldcup_dialog_by.setOnClickListener(this);
		worldcup_dialog_voice_cb.setOnClickListener(this);
		
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(isError){
					mWebView.setVisibility(View.GONE);
				}else{
					mWebView.setVisibility(View.VISIBLE);
				}
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				isError = true;
			}

		});
		mWebView.loadUrl(url);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(mp != null){
			mp.stop();
			mp.release();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.worldcup_dialog_close:
			WorldcupDialog.this.dismiss();
			break;
		case R.id.worldcup_dialog_custom:
			Intent intent1 = new Intent(context, WorldcupAlarmActivity.class);
			context.startActivity(intent1);
			StatService.onEvent(context, "worldcup_dialog_btn_custom", "首页世界杯弹层-定制", 1);
			break;
		case R.id.worldcup_dialog_cgj:
			Intent intent2 = new Intent(context, JZCGJActivity.class);
			context.startActivity(intent2);
			StatService.onEvent(context, "worldcup_dialog_btn_cgj", "首页世界杯弹层-猜冠军", 1);
			break;
		case R.id.worldcup_dialog_jzbet:
			Intent intent3 = new Intent(context, JZActivity.class);
			context.startActivity(intent3);
			StatService.onEvent(context, "worldcup_dialog_btn_jzbet", "首页世界杯弹层-竞足投注", 1);
			break;
		case R.id.worldcup_dialog_voice_cb:
			if(worldcup_dialog_voice_cb.isChecked()){
				if(mp != null){
					mp.stop();
				}
				StatService.onEvent(context, "worldcup_dialog_btn_mute", "首页世界杯弹层-静音", 1);
			}
			Settings.saveSharedPreferences(spf, IsMuteKey, worldcup_dialog_voice_cb.isChecked());
			break;
		case R.id.worldcup_dialog_by:
			Intent intent4 = new Intent(context,WebViewTitleBarActivity.class);
			intent4.putExtra(WebViewTitleBarActivity.TITLE, "竞彩8元保中");
			intent4.putExtra(WebViewTitleBarActivity.URL, GetString.RMB_8_YUAN_SERVER);
			intent4.putExtra(WebViewTitleBarActivity.IS_ADD_JS_INTERFACE, true);
			context.startActivity(intent4);
			StatService.onEvent(context, "worldcup_dialog_btn_bayuan", "首页世界杯弹层-8元保中", 1);
			break;
		default:
			break;
		}
	}
	
	private void waitMinTime() {
		waitTimeTask = new MyAsyncTask(context, false);
		waitTimeTask.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			@Override
			public Boolean onTaskBackgroundListener() {
				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			public void onTaskPostExecuteListener() {
				try {
					if(!isMute){
						playMusic();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		waitTimeTask.execute();
	}
}
