package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.IssueInfoParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;

@SuppressLint("JavascriptInterface")
public class WebViewActivity extends ZCHBaseActivity {
	// private static final int finish = 0x001;
	public static final String URL = "url";
	public static final String IS_ADD_JS_INTERFACE = "is_add_js_interface";
	public static final String URL_WEP_CONNECT = "http://wap.zch168.com/olymp_index";

	private ProgressBar mProgressBar;

	private WebView mWebView;
	// private Handler mHandler = new Handler();

	private double time = 56 * 1000;

	private String url;
	private boolean isAddJsInterface;

	private Thread over;
	private boolean isThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.web_view);

		// url = "http://www.111111.com";
		url = getIntent().getStringExtra(URL);
		isAddJsInterface = getIntent().getBooleanExtra(IS_ADD_JS_INTERFACE, false);
		mProgressBar = (ProgressBar) findViewById(R.id.web_progress);
		mWebView = (WebView) findViewById(R.id.web_view);
		mWebView.requestFocus();// 如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
		mWebView.getSettings().setJavaScriptEnabled(true);// 如果访问的页面中有Javascript，则webview必须设置支持Javascript。

		overtime();// 初始化线程

		// 当前页面加载
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// Log.i("TAG", "shouldOverrideUrlLoading");
				if (URLUtil.isNetworkUrl(url)) {
					mWebView.loadUrl(url);
				} else {
					ToastUtil.diaplayMesLong(getApplicationContext(), "链接无效");
					finish();
				}
				return true;
			}

		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// WebViewActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				// newProgress * 100);
//				Log.i("TAG", "newProgress 进度 : " + newProgress);
				mProgressBar.setProgress(newProgress);

				if (newProgress < 100) {
					isThread = true;
				} else {
					isThread = false;
				}

				super.onProgressChanged(view, newProgress);

			}

		});

		// 判断URL 是否为有效连接
		if (!TextUtils.isEmpty(url) || !url.equals("null")) {
			if (URLUtil.isNetworkUrl(url)) {
				mWebView.loadUrl(url);
			} else {
				ToastUtil.diaplayMesLong(getApplicationContext(), "链接无效");
				finish();
			}
		} else {
			ToastUtil.diaplayMesLong(getApplicationContext(), "链接为空");
			finish();
		}

		if (isAddJsInterface) {
			mWebView.addJavascriptInterface(new JsCallBack(), "WebViewJavascriptBridge");// 实现往浏览器添加进一个js对象，名称为:contact
		}
	}

	private class JsCallBack {

		@SuppressWarnings("unused")
		public void send(String value) {
			if (!TextUtils.isEmpty(value)) {
				String[] arr = value.split(",");
				if (arr!=null && arr.length==2) {
					String moneyStr = arr[0];
					String activityId = arr[1];
					double costMoney = Double.parseDouble(moneyStr);
					//判断余额是否够用
					double remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
					if (remainMoney >= costMoney) {
						request8Yuan(activityId);
					}else {
						//充值
						PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(WebViewActivity.this);
						dialog.setMoney(costMoney - remainMoney);
						dialog.show();
					}
					
				}else{
					ToastUtil.diaplayMesShort(WebViewActivity.this,"服务端返回格式不正确！");
				}
			}
		}

	}
	
	private ProgressDialog mDialog;
	private void request8Yuan(String activityId){
		mDialog = ProgressDialog.show(this, "", "操作中...", true, false);
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", activityId);
		map.put("userCode", GetString.userInfo.getUserCode());
		
		Map<String, Map<String, String>> param = new HashMap<String, Map<String, String>>();
		param.put("map", map);
		SafelotteryHttpClient.post(this, "3300", "activity", JsonUtils.toJsonStr(param), new TypeMapHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(int statusCode, @SuppressWarnings("rawtypes") Map mMap) {
				if(mMap != null){
					String programsOrderId = (String) mMap.get("programsOrderId");
					ToastUtil.diaplayMesShort(WebViewActivity.this,"操作成功！");
				}else
					ToastUtil.diaplayMesShort(WebViewActivity.this,"服务端返回格式不正确！");
			}
			@Override
			public void onFinish() {
				mDialog.dismiss();
			};
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				LogUtil.e("", mErrorMsg);
			}
		});
	}
	

	private void overtime() {
		over = new Thread() {
			public void run() {
				while (isThread) {
					try {
						LogUtil.CustomLog("TAG", "执行线程");
						if (time > 0) {
							time -= 200;
						} else {
							Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));

							startActivity(intent);
							finish();
						}
						// else time = 5000;
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		over.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			// isThread = false;
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}
