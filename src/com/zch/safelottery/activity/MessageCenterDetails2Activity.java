package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshWebView;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.util.GetString;

public class MessageCenterDetails2Activity extends ZCHBaseActivity {
	
	public static int Count = 0;
	
	private TextView title;
	private PullToRefreshWebView mPullToRefreshWebView;
	private WebView webContent;
	private String messageId;
	private Map resultMap;
	private ProgressBar loadding_progressbar_m;
	private String titlestr;
	private String content;
	private String isReaded;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center_details);
        try {
        	   Intent intent=getIntent();
        	   messageId=intent.getStringExtra("nid");
        	   isReaded=intent.getStringExtra("isReaded");
               initViews();
               doRequestTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void initViews() {
		loadding_progressbar_m=(ProgressBar)findViewById(R.id.loadding_progressbar_m);
		title = (TextView) findViewById(R.id.message_details_title_tx);
		mPullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.web_view);
		webContent = mPullToRefreshWebView.getRefreshableView();
		webContent.getSettings().setDefaultTextEncodingName("UTF-8") ;
		webContent.getSettings().setTextSize(TextSize.NORMAL);
		webContent.getSettings().setJavaScriptEnabled(true); 
		mPullToRefreshWebView.setOnRefreshListener(new OnRefreshListener<WebView>(){
			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				doRequestTask();
			}
		});
	}
	
	
	/**
	 * 请求数据的AsyncTask
	 * 
	 * @author Messi
	 * 
	 */
	private void doRequestTask() {
		loadding_progressbar_m.setVisibility(View.VISIBLE);
		SafelotteryHttpClient.post(this, "3004", "onePrivateMessage", initDate(), new TypeResultHttpResponseHandler(this, true) {
			
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
						webContent.loadDataWithBaseURL("about:blank",content.toString(),"text/html", "UTF-8",null);
						//未读的时候发送标志通知本条信息已读
						if(isReaded.equals("0")){
							hadRead();
						}
					}
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
	
	private void hadRead(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("messageId", messageId);
		
		SafelotteryHttpClient.post(this, "3004", "hadReadPrivateMessage", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Result mResult) {
				isReaded = "1";
				setResult(1);
				if(Count > 0){
					Count--;
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
		map.put("messageId",messageId);
		String str =  JsonUtils.toJsonStr(map);
		return str;
	}
}