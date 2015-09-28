package com.zch.safelottery.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.view.TitleViews;

public class HelpDetailActivity extends ZCHBaseActivity {
	
	private WebView wv;
	private int kind;
	private LinearLayout title;
	private Bundle mBundle;
	
	private String webTagTocation;
	
	private TitleViews titleViews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_detail);
		
		Intent intent = getIntent();
		kind = intent.getIntExtra("kind", 1);
		webTagTocation = intent.getStringExtra(Settings.WEBTAGLOCATION);
		mBundle = new Bundle();
		mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
		initViews();
		addTitle();
		addData();
	}
	private void initViews() {
		title = (LinearLayout) findViewById(R.id.help_detail_title);
		
		wv = (WebView) findViewById(R.id.ssq_webview);
		wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	view.loadUrl(url);
                return true;   
            }
 
        });
        wv.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){//载入进度改变而触发 
             	if(progress==100){
             		if(!TextUtils.isEmpty(webTagTocation)){
             			wv.loadUrl("javascript:loca('"+ webTagTocation + "')");//调用网页中的方法
             		}
            	}   
             	super.onProgressChanged(view, progress);   
            }   
        });
	}
	
	private void addTitle(){
		titleViews = new TitleViews(this, "代购帮助");
//		titleViews.setBtnIcon(R.drawable.btn_share_bg_d);
		titleViews.setBtnName("去购彩");
		title.addView(titleViews.getView());
		if(mBundle == null){
			titleViews.setFrameLayout(View.GONE);
		}else{
			titleViews.setFrameLayout(View.VISIBLE);
		}
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String lid = mBundle.getString(LotteryId.INTENT_LID);
				String issue = mBundle.getString(BaseLotteryActivity.INTENT_ISSUE);
				if (!LotteryId.isJCORCZ(lid)){
					if(TextUtils.isEmpty(issue)){
						ToastUtil.diaplayMesShort(getApplicationContext(), "暂无可售期次");
						return;
					}
				}
				Intent intent = new Intent();
				intent.setClass(HelpDetailActivity.this, (Class)mBundle.getSerializable(Settings.TOCLASS));
				intent.putExtra(LotteryId.INTENT_LID, lid);
				intent.putExtra(BaseLotteryActivity.INTENT_ISSUE, issue);
				HelpDetailActivity.this.startActivity(intent);
			}
		});
	}
	
	private void addData(){
		if(kind ==1){
			titleViews.setTitleName("代购帮助");
        	wv.loadUrl("file:///android_asset/help/daigou.html");
        }else if(kind ==2){
        	titleViews.setTitleName("合买帮助");
        	wv.loadUrl("file:///android_asset/help/hemai.html");
        }else if(kind ==3){
        	titleViews.setTitleName("账户帮助");
        	wv.loadUrl("file:///android_asset/help/account.html");
        }else if(kind ==4){
        	titleViews.setTitleName("充值提款帮助");
        	wv.loadUrl("file:///android_asset/help/chongzhi.html");
        }else if(kind ==5){
        	titleViews.setTitleName("兑奖帮助");
        	wv.loadUrl("file:///android_asset/help/duijiang.html");
        }else if(kind ==6){
        	titleViews.setTitleName("双色球玩法帮助");
        	wv.loadUrl("file:///android_asset/help/ssq/ssq_howto.html");
        }else if(kind ==7){
        	titleViews.setTitleName("大乐透玩法帮助");
        	wv.loadUrl("file:///android_asset/help/dlt/dlt_howto.html");
        }else if(kind ==8){
        	titleViews.setTitleName("福彩3D玩法帮助");
        	wv.loadUrl("file:///android_asset/help/x3d/x3d_howto.html");
        }else if(kind ==9){
        	titleViews.setTitleName("排列3玩法帮助");
        	wv.loadUrl("file:///android_asset/help/pl3/pl3_howto.html");
        }else if(kind ==10){
        	titleViews.setTitleName("山东11选5玩法帮助");
        	wv.loadUrl("file:///android_asset/help/y11/y11_howto.html");
        }else if(kind ==101){
        	titleViews.setTitleName("安徽11选5玩法帮助");
        	wv.loadUrl("file:///android_asset/help/y11new/y11_howto.html");
        }else if(kind ==105){
        	titleViews.setTitleName("广东11选5玩法帮助");
        	wv.loadUrl("file:///android_asset/help/y11new/y11_howto.html");
        }else if(kind ==1101){
        	titleViews.setTitleName("重庆时时彩玩法帮助");
        	wv.loadUrl("file:///android_asset/help/ssccq/ssc_howto.html");
        }else if(kind ==1102){
        	titleViews.setTitleName("江西时时彩玩法帮助");
        	wv.loadUrl("file:///android_asset/help/sscjx/ssc_howto.html");
        }else if(kind ==31){
        	titleViews.setTitleName("快3玩法帮助");
        	wv.loadUrl("file:///android_asset/help/k3/k3_howto.html");
        }else if(kind ==32){
        	titleViews.setTitleName("江苏快3玩法帮助");
        	wv.loadUrl("file:///android_asset/help/k3/k3_howto.html");
        }else if(kind ==12){
        	titleViews.setTitleName("快乐8玩法帮助");
        	wv.loadUrl("file:///android_asset/help/kl8/kl8_howto.html");
        }else if(kind ==13){
        	titleViews.setTitleName("幸运农场玩法帮助");
        	wv.loadUrl("file:///android_asset/help/klnc/klnc_howto.html");
        }else if(kind ==14){
        	titleViews.setTitleName("中彩汇用户服务协议");
        	wv.loadUrl("file:///android_asset/help/xieyi.html");
        }else if(kind ==15){
        	titleViews.setTitleName("排列五玩法帮助");
        	wv.loadUrl("file:///android_asset/help/pl5/pl5_howto.html");
        }else if(kind ==16){
        	titleViews.setTitleName("七星彩玩法帮助");
        	wv.loadUrl("file:///android_asset/help/qxc/qxc_howto.html");
        }else if(kind ==17){
        	titleViews.setTitleName("七乐彩玩法帮助");
        	wv.loadUrl("file:///android_asset/help/qlc/qlc_howto.html");
        }else if(kind ==18){
        	titleViews.setTitleName("PK拾玩法帮助");
        	wv.loadUrl("file:///android_asset/help/pks.html");
        }else if(kind ==19){
        	titleViews.setTitleName("14场胜负玩法帮助");
        	wv.loadUrl("file:///android_asset/help/chang14.html");
        }else if(kind ==20){
        	titleViews.setTitleName("任选9玩法帮助");
        	wv.loadUrl("file:///android_asset/help/renxuan9.html");
        }else if(kind ==21){
        	titleViews.setTitleName("竞彩足球玩法帮助");
        	wv.loadUrl("file:///android_asset/help/jingzu.html");
        }else if(kind ==22){
        	titleViews.setTitleName("竞彩篮球玩法帮助");
        	wv.loadUrl("file:///android_asset/help/jinglan.html");
        }else if(kind ==23){
        	titleViews.setTitleName("北京单场玩法帮助");
        	wv.loadUrl("file:///android_asset/help/beidan.html");
        }else if(kind ==24){
        	titleViews.setTitleName("冠军竞猜玩法帮助");
        	wv.loadUrl("file:///android_asset/help/guanjun.html");
        }else if(kind ==25){
        	titleViews.setTitleName("冠亚军竞猜玩法帮助");
        	wv.loadUrl("file:///android_asset/help/guanyajun.html");
        }else if(kind ==26){
        	titleViews.setTitleName("什么是保底");
        	wv.loadUrl("file:///android_asset/help/shenmeshibaodi.html");
        }else if(kind ==27){
        	titleViews.setTitleName("常见问题");
        	wv.loadUrl("file:///android_asset/help/faq.html");
        }
	}
}
