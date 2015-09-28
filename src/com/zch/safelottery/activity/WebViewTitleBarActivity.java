package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshWebView;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

/**
 * 使用android自己的标题+Webview
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年5月28日
 */
public class WebViewTitleBarActivity extends ZCHBaseActivity {

	public static final String URL = "url";
	public static final String TITLE = "title";
	public static final String IS_ADD_JS_INTERFACE = "is_add_js_interface";
	public static final String URL_WEP_CONNECT = "http://wap.zch168.com/olymp_index";

	private TextView mTvTitle;
	private WebView mWebView;
	private PullToRefreshWebView refreshable_webview;

	private double time = 56 * 1000;

	private String mUrl;
	private String mTitle;
	private boolean mIsAddJsInterface;

	private Thread over;
	private boolean isThread;
	private ImageButton refreshBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.web_view_title);

		mUrl = getIntent().getStringExtra(URL);
		mTitle = getIntent().getStringExtra(TITLE);
		mIsAddJsInterface = getIntent().getBooleanExtra(IS_ADD_JS_INTERFACE, false);

		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvTitle.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
		refreshBtn = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		refreshable_webview = (PullToRefreshWebView) findViewById(R.id.web_view);
		mWebView = refreshable_webview.getRefreshableView();
		refreshable_webview.setOnRefreshListener(new OnRefreshListener<WebView>() {
			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				mWebView.loadUrl(mUrl);
			}
		});

		mWebView.requestFocus();// 如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
		mWebView.getSettings().setJavaScriptEnabled(true);// 如果访问的页面中有Javascript，则webview必须设置支持Javascript。

		overtime();// 初始化线程

		// 当前页面加载
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				refreshBtn.setVisibility(View.VISIBLE);
				refreshBtn.startAnimation(AnimationUtil.getRotateCenter(WebViewTitleBarActivity.this));
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				refreshBtn.setVisibility(View.GONE);
				refreshBtn.clearAnimation();
				refreshable_webview.onRefreshComplete();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (URLUtil.isNetworkUrl(url)) {
					mWebView.loadUrl(url);
				} else {
					ToastUtil.diaplayMesLong(getApplicationContext(), "链接无效");
				}
				return true;
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				if (TextUtils.isEmpty(mTitle)) {
					mTvTitle.setText(title);
				} else
					mTvTitle.setText(mTitle);
				super.onReceivedTitle(view, title);
			}
		});

		// 判断URL 是否为有效连接
		if (!TextUtils.isEmpty(mUrl) || !mUrl.equals("null")) {
			if (URLUtil.isNetworkUrl(mUrl)) {
				mWebView.loadUrl(mUrl);
			} else {
				ToastUtil.diaplayMesLong(getApplicationContext(), "链接无效");
				finish();
			}
		} else {
			ToastUtil.diaplayMesLong(getApplicationContext(), "链接为空");
			finish();
		}

		if (mIsAddJsInterface) {
			mWebView.addJavascriptInterface(new JsCallBack(), "WebViewJavascriptBridge");// 实现往浏览器添加进一个js对象，名称为:contact
		}
	}

	class JsCallBack {
		@JavascriptInterface
		public void send(String value) {
			if (!TextUtils.isEmpty(value)) {
				String[] arr = value.split(",");
				if (arr != null && arr.length == 2) {
					String moneyStr = arr[0];
					String activityId = arr[1];
					double costMoney = Double.parseDouble(moneyStr);
					// 判断是否登录
					if (GetString.userInfo != null) {
						// 判断余额是否够用
						double remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
						if (remainMoney >= costMoney) {
							request8Yuan(activityId);
						} else {
							// 充值
							PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(WebViewTitleBarActivity.this);
							dialog.setMoney(costMoney - remainMoney);
							dialog.show();
						}
					} else {
						// 未登录
						Intent intent = new Intent(WebViewTitleBarActivity.this, LoginActivity.class);
						startActivity(intent);
					}

				} else {
					ToastUtil.diaplayMesShort(WebViewTitleBarActivity.this, "服务端返回格式不正确！");
				}
			}
		}
	}

	private ProgressDialog mDialog;

	private void request8Yuan(String activityId) {
		mDialog = ProgressDialog.show(this, "", "抢购中...", true, false);
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", activityId);
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("from", "8yuan");

		Map<String, Map<String, String>> param = new HashMap<String, Map<String, String>>();
		param.put("map", map);
		SafelotteryHttpClient.post(this, "3300", "activity", JsonUtils.toJsonStr(param), new TypeMapHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(int statusCode, @SuppressWarnings("rawtypes") Map mMap) {
				if (mMap != null) {
					String programsOrderId = (String) mMap.get("programsOrderId");
					NormalAlertDialog dialog = new NormalAlertDialog(WebViewTitleBarActivity.this);
					dialog.setTitle("购买提示");
					dialog.setContent("购买成功，请到投注记录查看详情。");
					dialog.setOk_btn_text("确定");
					dialog.setType(1);
					dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
						@Override
						public void onOkBtnClick() {
							finish();
						}

						@Override
						public void onCancleBtnClick() {
						}
					});
					dialog.show();
				} else
					ToastUtil.diaplayMesShort(WebViewTitleBarActivity.this, "服务端返回格式不正确！");
			}

			@Override
			public void onFinish() {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				LogUtil.e("", "onFinish");
			};

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
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
							Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mUrl));

							startActivity(intent);
							finish();
						}
						Thread.sleep(200);
					} catch (InterruptedException e) {
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
