package com.zch.safelottery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.share.BaseShare;
import com.zch.safelottery.util.share.BaseSinaWeiboActivity;
import com.zch.safelottery.util.share.ShareUtil;

/**
 * 魔图分享
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年4月24日
 */
public class MotuShareActivity extends BaseSinaWeiboActivity implements View.OnClickListener {


	/** 继续上传照片  */
	private FrameLayout mBtnCamera;
	/** 购彩首页按钮  */
	private FrameLayout mBtnGotoBuyLottery;

	private Bitmap mBitmap;
	private String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motu_share);

		mBitmap = (Bitmap) SafeApplication.dataMap.get("mBitmap");
		filePath = (String) SafeApplication.dataMap.put("filePath", filePath);
		SafeApplication.dataMap.clear();
		initViews();
		initEvents();
		
		if (savedInstanceState != null) {
			ShareUtil.getSinaInstance(this).handlerNewIntent(getIntent(),this);
		}
	}

	private void initViews() {
		mBtnCamera = (FrameLayout) findViewById(R.id.btn_camera);
		mBtnGotoBuyLottery = (FrameLayout) findViewById(R.id.btn_goto_buy_lottery);

		
		View parent = findViewById(R.id.table);
		String title = "#魔图选号#我在@中彩票V 上传了一张图片，获得了一组双色球号码，小伙伴们快来试试吧！";
		LogUtil.e("", "========filePath==" + filePath);
		ShareUtil.addShareForView(this, parent , BaseShare.ShareType.PICTURE, "", -1, filePath, title, "", null);
	}

	private void initEvents() {
		mBtnGotoBuyLottery.setOnClickListener(this);
		mBtnCamera.setOnClickListener(this);

	}

	public void onClick(View v) {
		if (v == mBtnCamera) {
			// 启动广播 魔图相关Activity
			Intent broadcast = new Intent(SafeApplication.INTENT_ACTION_ALLACTIVITY);
			broadcast.putExtra(Settings.EXIT, Settings.EXIT_MOTU);
			sendBroadcast(broadcast);

			startActivity(new Intent(this, MotuActivity.class));
			finish();
		} else if (v == mBtnGotoBuyLottery) {
			Settings.closeOtherActivity(this);
			finish();
		} 
	}

	@Override
	protected void onNewIntent(Intent intent) {
		ShareUtil.getSinaInstance(this).handlerNewIntent(intent,this);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mBitmap != null){
			mBitmap.recycle();
		}
		System.gc();
	}

	
	/**
	 * 中彩票
		AppID：wxd67502e6df5b07c6
		AppSecret：7315325d00e25e9490a56716466743f9
	  
	   Android平台
		应用下载地址：http://www.myapp.com/downcenter/a/106592?g_f=991237
		应用签名：85011c557d6b317f17f76f5d217fbc5e
		包名：com.zch.safelottery
	 */
}
