package com.zch.safelottery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshWebView;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;

public class ZouShiTuActivity extends FragmentActivity implements OnClickListener{

	private PullToRefreshWebView refreshable_webview;
	private WebView mWebView;
	private FrameLayout zoushitu_top_menu;
	private TextView zoushitu_top_menu_text;
	private ImageButton refreshBtn;
	
	private String[] items;
	private PopWindowDialog popDialog;
	private int index = 0;
	private String lid;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoushitu_layout);
		initData();
		initViews();
	}
	
	private void initData(){
		Intent intent = getIntent();
		lid = intent.getStringExtra(LotteryId.INTENT_LID);
	}
	
	private void initViews(){
		refreshable_webview = (PullToRefreshWebView)findViewById(R.id.refreshable_webview);
		zoushitu_top_menu = (FrameLayout)findViewById(R.id.zoushitu_top_menu);
		zoushitu_top_menu_text = (TextView)findViewById(R.id.zoushitu_top_menu_text);
		refreshBtn = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		mWebView = refreshable_webview.getRefreshableView();
		mWebView.getSettings().setJavaScriptEnabled(true);
		zoushitu_top_menu.setOnClickListener(this);
		
		refreshable_webview.setOnRefreshListener(new OnRefreshListener<WebView>(){
			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				mWebView.loadUrl(url);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				refreshBtn.setVisibility(View.VISIBLE);
				refreshBtn.startAnimation(AnimationUtil.getRotateCenter(ZouShiTuActivity.this));
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				refreshBtn.setVisibility(View.GONE);
				refreshBtn.clearAnimation();
				refreshable_webview.onRefreshComplete();
			}
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(URLUtil.isNetworkUrl(url)){
					mWebView.loadUrl(url);
				}else{
					ToastUtil.diaplayMesLong(getApplicationContext(), "链接无效");
				}
				return true;
			}
		});
		loadUrl();
	}
	
	private void loadUrl(){
		url = getCurrentLotteryUrl(lid,index);
		zoushitu_top_menu_text.setText( LotteryId.getLotteryName(lid) + " - " + items[index]);
		if(URLUtil.isNetworkUrl(url)){
			mWebView.loadUrl(url);
		}else{
			ToastUtil.diaplayMesLong(getApplicationContext(), "无效的链接");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zoushitu_top_menu:
			if(popDialog == null){
				popDialog = new PopWindowDialog(ZouShiTuActivity.this, items, index);
				popDialog.setOnClickListener(new OnclickFrequentListener(){
					@Override
					public void onClick(View v) {
						for(CheckBox c: popDialog.getList()){
							if(c.getId() == v.getId()){
								index = c.getId();
								c.setChecked(true);
							}else{
								c.setChecked(false);
							}
						}
						loadUrl();
						popDialog.dismiss();
					}
				});
				popDialog.setPopViewPosition();
				popDialog.show();
			}else{
				popDialog.show();
			}
			break;
		}
	}
	
	private String getCurrentLotteryUrl(String lid, int index){
		if(lid.equals(LotteryId.SSQ)){
			items = new String[]{"基本", "奇偶", "大小", "质合"};
			if(index == 0){
				return "http://m.zch168.com/chartServlet?action=ssq&type=index";//基本
			}else if(index == 1){
				return "http://m.zch168.com/chartServlet?action=ssq&type=jiou";//奇偶 
			}else if(index == 2){
				return "http://m.zch168.com/chartServlet?action=ssq&type=daxiao";//大小
			}else if(index == 3){
				return "http://m.zch168.com/chartServlet?action=ssq&type=zhihe";//质合
			}
		}else if(lid.equals(LotteryId.DLT)){
			items = new String[]{"基本", "奇偶", "大小", "质合"};
			if(index == 0){
				return "http://m.zch168.com/chartServlet?action=dlt&type=index";//基本
			}else if(index == 1){
				return "http://m.zch168.com/chartServlet?action=dlt&type=jiou";//奇偶 
			}else if(index == 2){
				return "http://m.zch168.com/chartServlet?action=dlt&type=daxiao";//大小 
			}else if(index == 3){
				return "http://m.zch168.com/chartServlet?action=dlt&type=zhihe";//质合
			}
		}else if(lid.equals(LotteryId.SYXW) || lid.equals(LotteryId.NSYXW) || lid.equals(LotteryId.GDSYXW)){
			items = new String[]{"任5组选", "前三组选", "前三直选"};
			if(index == 0){
				return "http://m.zch168.com/chartServlet?action=11xuan5&type=r5zx&lotteryId="+lid;//任5组选
			}else if(index == 1){
				return "http://m.zch168.com/chartServlet?action=11xuan5&type=q3zx&lotteryId="+lid;//前三组选
			}else if(index == 2){
				return "http://m.zch168.com/chartServlet?action=11xuan5&type=q3&lotteryId="+lid;//前三直选
			}
		}else if(lid.equals(LotteryId.K3) || lid.equals(LotteryId.K3X)){
			items = new String[]{"基本", "形态", "和值"};
			if(index == 0){
				return "http://m.zch168.com/chartServlet?action=kuai3&type=index&lotteryId="+lid;//基本
			}else if(index == 1){
				return "http://m.zch168.com/chartServlet?action=kuai3&type=xingtai&lotteryId="+lid;//形态
			}else if(index == 2){
				return "http://m.zch168.com/chartServlet?action=kuai3&type=hezhi&lotteryId="+lid;//和值
			}
		}
		return "";
	}

}
