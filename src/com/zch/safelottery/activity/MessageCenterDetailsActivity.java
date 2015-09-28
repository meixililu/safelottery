package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.database.NoticeDatabaseUtil;
import com.zch.safelottery.dialogs.ShareDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshWebView;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.share.BaseShare;
import com.zch.safelottery.util.share.ShareUtil;

public class MessageCenterDetailsActivity extends ZCHBaseActivity {
	
	private TextView title;
	private PullToRefreshWebView mPullToRefreshWebView;
	private WebView webContent;
	private String articleId;
	private Map resultMap;
	private ProgressBar loadding_progressbar_m;
	private String titlestr;
	private String content;
	private String mSimple_title;
	private TextView tvShare;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center_details);
        try {
        	   Intent intent=getIntent();
        	   articleId = intent.getStringExtra("nid");
        	   mSimple_title = intent.getStringExtra("Simple_title");
        	   initViews();
        	   requestData();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void initViews() {
		loadding_progressbar_m=(ProgressBar)findViewById(R.id.loadding_progressbar_m);
		title = (TextView) findViewById(R.id.message_details_title_tx);
		tvShare = (TextView) findViewById(R.id.tv_share);
		tvShare.setVisibility(View.VISIBLE);
		mPullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.web_view);
		webContent = mPullToRefreshWebView.getRefreshableView();
		webContent.getSettings().setDefaultTextEncodingName("UTF-8") ;
		webContent.getSettings().setTextSize(TextSize.NORMAL);
		webContent.getSettings().setJavaScriptEnabled(true); 
		
		mPullToRefreshWebView.setOnRefreshListener(new OnRefreshListener<WebView>(){
			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				requestData();
			}
		});
		
		webContent.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				mPullToRefreshWebView.onRefreshComplete();
			}
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(URLUtil.isNetworkUrl(url)){
					webContent.loadUrl(url);
				}else{
					ToastUtil.diaplayMesLong(getApplicationContext(), "链接无效");
				}
				return true;
			}
		});
	}
	
	private void requestData(){
	   if(!TextUtils.isEmpty(mSimple_title)){
		   title.setVisibility(View.GONE);
		   webContent.loadUrl(mSimple_title);
	   }else{
		   doRequestTask();
	   }
	}
	/**
	 * 请求数据的AsyncTask
	 * 
	 * @author Messi
	 * 
	 */
	private void doRequestTask() {
		loadding_progressbar_m.setVisibility(View.VISIBLE);
		SafelotteryHttpClient.post(this, "3004", "oneArticle", initDate(), new TypeResultHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try {
					String resultStr=mResult.getResult();
					resultMap = JsonUtils.stringToMap(resultStr);
					String articleDetail=(String) resultMap.get("articleDetail");
					Map map= JsonUtils.stringToMap(articleDetail);
					String article = (String) map.get("article");
					Map map2=JsonUtils.stringToMap(article);
					JSONObject json=new JSONObject(map2);
					titlestr=json.getString("title");
				    content=json.getString("content");
				    if(!TextUtils.isEmpty(content)){
						loadding_progressbar_m.setVisibility(View.GONE);
						title.setText(titlestr);
						new NoticeDatabaseUtil(MessageCenterDetailsActivity.this).updateIsclick(articleId);
						webContent.loadDataWithBaseURL("about:blank",content.toString(),"text/html", "UTF-8",null);
					}
				    tvShare.setOnClickListener(new View.OnClickListener() {
						private ShareDialog mDialog;
						@Override
						public void onClick(View v) {
							
							String targetUrl = "http://www.zch168.com";
							mDialog = new ShareDialog(MessageCenterDetailsActivity.this, titlestr, targetUrl , "",R.drawable.icon, "", BaseShare.ShareType.URL,
									new ShareUtil.CallBack() {
										@Override
										public void onAfterClickShare() {
											mDialog.dismiss();
										}
									});
							mDialog.setCanceledOnTouchOutside(true);
							mDialog.show();
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public String initDate() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("articleId",articleId);
		String str =  JsonUtils.toJsonStr(map);
		return str;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SafeApplication.dataMap.clear();
	}
}