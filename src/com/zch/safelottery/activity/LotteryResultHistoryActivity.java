package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.LotteryIssueHistoryBean;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryIssueHistoryParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class LotteryResultHistoryActivity extends ZCHBaseActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "LotteryResultHistoryActivity";
	
	private PullToRefreshListView lv;
	private ArrayList<LotteryIssueHistoryBean> result_list;
	private ProgressBar progressbarM;
	private TextView mPromptTv;
	private ImageButton refresh;
	private TextView tv_title;
	private MyListViewAdapter adapter;
	
	private String lotteryId,name;
	private int page = 1;
	private int pageSize = 20;
	private int pageTotal;
	
	private String msg; //请求的Json内容
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onCreate()");
		setContentView(R.layout.lottery_result_history);
		Intent intent = getIntent();
		Bundle mBundle = intent.getBundleExtra(Settings.BUNDLE);
		if(mBundle != null){
			lotteryId = mBundle.getString(Settings.INTENT_STRING_LOTTERY_ID);
			if(!TextUtils.isEmpty(lotteryId)){
				name = LotteryId.getLotteryName(lotteryId);
			}
			//此页面只需传入一个参数：彩种id
			initUI();
			initData();
			requsetDataTask();
		}else{
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.no_legal);
		}
	}
	
	private void initUI(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initUI()");
		lv = (PullToRefreshListView) findViewById(R.id.lottery_result_history_listview);
		progressbarM = (ProgressBar) findViewById(R.id.lottery_result_history_progressbar_m);
		mPromptTv = (TextView) findViewById(R.id.lottery_result_history_text_prompt);
		refresh = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		tv_title = (TextView) findViewById(R.id.lottery_result_history_name);
		
		refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				refresh();
			}
		});
	}
	
	private void initData(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initData()");
		result_list = new ArrayList<LotteryIssueHistoryBean>();
		adapter = new MyListViewAdapter(getApplicationContext());
        lv.setAdapter(adapter);
        lv.setMode(Mode.BOTH);
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					refresh();
					break;
				case PULL_FROM_END:
					if(page <= pageTotal){
						requsetDataTask();
					}
					break;
				}
			}
		});
        tv_title.setText("开奖历史-" + name);
	}
	
	private void refresh(){
		page = 1;
		requsetDataTask();
	}
	
	
	private void requsetDataTask(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("lotteryId", lotteryId);
		map.put("page", String.valueOf(page));
		map.put("pageSize", String.valueOf(pageSize));
		msg = JsonUtils.toJsonStr(map);
		
		refresh.startAnimation(AnimationUtil.getRotateCenter(LotteryResultHistoryActivity.this));
		if(result_list.size() == 0){
			progressbarM.setVisibility(View.VISIBLE);
		}
		mPromptTv.setVisibility(View.GONE);
		
		SafelotteryHttpClient.post(this, "3301", "history", msg, new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					pageTotal = Integer.parseInt((String) mMap.get("pageTotal"));
					ArrayList<LotteryIssueHistoryBean> mList = (ArrayList<LotteryIssueHistoryBean>) JsonUtils.parserJsonArray((String) mMap.get("issueList"), new LotteryIssueHistoryParser());
					if(mList != null){
						if(page == 1){
							result_list.clear();
						}
						result_list.addAll(mList);
						adapter.notifyDataSetChanged();
						if(page == 1){
							lv.startLayoutAnimation();
						}
						page++;
					}else{
						if(page == 1) mPromptTv.setVisibility(View.VISIBLE);
					}
					if(page > pageTotal){
						lv.setMode(Mode.PULL_FROM_START);
					}else{
						lv.setMode(Mode.BOTH);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
			
			@Override
			public void onFinish(){
				lv.onRefreshComplete();
				progressbarM.setVisibility(View.GONE);
				refresh.clearAnimation();
			}
		});
	}
	
	class MyListViewAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		
		public MyListViewAdapter (Context context) {
			mInflater = LayoutInflater.from(context);
		}
		
		public int getCount() {
			return result_list.size();
		}

		public Object getItem(int position) {
			return result_list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lottery_result_history_list_item, null);
				holder = new ViewHolder();
				holder.parentLayout = (FrameLayout)convertView.findViewById(R.id.lottery_result_history_parentlayout);
				holder.item = (LinearLayout)convertView.findViewById(R.id.lottery_result_history_result);
				holder.time = (TextView)convertView.findViewById(R.id.lottery_result_history_cz_time);
				holder.issue = (TextView)convertView.findViewById(R.id.lottery_result_history_cz_issue);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			try {
			
				LotteryIssueHistoryBean mBean = result_list.get(position);
				
				holder.issue.setText("第"+ mBean.getName() +"期");
				holder.time.setText(TimeUtils.customFormatDate(mBean.getBonusTime(), TimeUtils.DateFormat, TimeUtils.DayFormat));
				holder.item.removeAllViews();
				holder.item.addView(ViewUtil.getAutoView(getApplicationContext(), lotteryId, mBean.getBonusNumber(), true, 2));
				holder.parentLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						detail(position);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}
	
	private void detail(int position){
		Intent mIntent = new Intent(LotteryResultHistoryActivity.this, IssueDetailActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(LotteryId.INTENT_LID, lotteryId);
		SafeApplication.dataMap.put(BaseLotteryActivity.INTENT_ISSUE, result_list);
		mBundle.putInt(BaseLotteryActivity.INTENT_ISSUE, position);
		mIntent.putExtra(Settings.BUNDLE, mBundle);
		startActivity(mIntent);
	}
	
	static class ViewHolder {
		FrameLayout parentLayout;
		LinearLayout item;
		TextView time;
		TextView issue;
    }
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
	}

}
