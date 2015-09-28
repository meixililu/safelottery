package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.android.app.pay.PayTask;
import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.LotteryIssueHallBean;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.parser.LotteryIssueHallParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ViewUtil;

public class LotteryResultHallActivity extends BaseTabActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "LotteryResultHallActivity";

	private PullToRefreshListView lv;
	private List<LotteryIssueHallBean> mArrayLists;
	private ImageView connection_faile_img;
	private ProgressBar progressbar_m;
	private ImageButton btn_refresh;
	private MyListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lottery_result_hall);
		
		initUI();
		initData();
		requsetDataTask();
		
	}
	
	private void initUI(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initUI()");
		lv = (PullToRefreshListView) findViewById(R.id.lotter_result_hall_listview);
		connection_faile_img = (ImageView)findViewById(R.id.connection_faile_img);
		progressbar_m = (ProgressBar) findViewById(R.id.lottery_result_hall_progressbar_m);
		btn_refresh = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		
		btn_refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requsetDataTask();
				StatService.onEvent(LotteryResultHallActivity.this, "result-refresh", "开奖大厅-刷新", 1);
			}
		});
	}
	
	private void initData(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initData()");
		mArrayLists = new ArrayList<LotteryIssueHallBean>();
		adapter = new MyListViewAdapter(getApplicationContext());
		lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//跳转到下个页面 arg2默认从1开始
				jumpToHistory(mArrayLists.get(arg2 - 1).getLotteryId());
			}
		});
        lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            	requsetDataTask();
			}
		});
	}
	
	private List<String> getPursueLotteryId(){
		List<String> lotteryArray = new ArrayList<String>();
		String orderstr = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE,Context.MODE_PRIVATE).getString(LotteryInfoParser.LotteryOrderStrKey, "");
		lotteryArray = LotteryId.getLotteryId(orderstr, LotteryId.getLotteryOpen());
		return lotteryArray;
	}
	
	private void requsetDataTask() {
		
		SafelotteryHttpClient.post(this, "3307", "", "", new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onStart(){
				connection_faile_img.setVisibility(View.GONE);
				if(mArrayLists.size() == 0){
					progressbar_m.setVisibility(View.VISIBLE);
				}
				btn_refresh.startAnimation(AnimationUtil.getRotateCenter(LotteryResultHallActivity.this));
			}
			
			@Override
			public void onFinish(){
				progressbar_m.setVisibility(View.GONE);
				btn_refresh.clearAnimation();
				lv.onRefreshComplete();
			}
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					List<LotteryIssueHallBean> arrays = (List<LotteryIssueHallBean>) JsonUtils.parserJsonArray((String) mMap.get("issueList"), new LotteryIssueHallParser());
					mArrayLists.clear();
					List<String> lottery = getPursueLotteryId();
					for(String id: lottery){
						for(LotteryIssueHallBean mBean: arrays){
							if(mBean.getLotteryId().equals(id)){
								mArrayLists.add(mBean);
								arrays.remove(mBean);
								break;
							}
						}
					}
					adapter.notifyDataSetChanged();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				if(mArrayLists.size() == 0){
					connection_faile_img.setVisibility(View.VISIBLE);
				}
			}
			
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
	}
	
	class MyListViewAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		
		public MyListViewAdapter (Context context) {
			mInflater = LayoutInflater.from(context);
		}
		
		public int getCount() {
			return mArrayLists.size();
		}

		public Object getItem(int position) {
			return mArrayLists.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lottery_result_hall_list_item, null);
				holder = new ViewHolder();
				holder.item = (View)convertView.findViewById(R.id.lottery_result_item);
				holder.type = (TextView)convertView.findViewById(R.id.lottery_result_cz_type);
				holder.icon = (ImageView)convertView.findViewById(R.id.lottery_result_cz_icon);
				holder.name = (TextView)convertView.findViewById(R.id.lottery_result_cz_name);
				holder.issue = (TextView)convertView.findViewById(R.id.lottery_result_cz_issue);
				holder.result = (LinearLayout)convertView.findViewById(R.id.lottery_result_linearlayout);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			if(position == mArrayLists.size()-1){
				holder.type.setVisibility(View.VISIBLE);
			}else{
				holder.type.setVisibility(View.GONE);
			}
			LotteryIssueHallBean mBean  = mArrayLists.get(position);
//			LogUtil.DefalutLog(mBean.toString());
			final String lotteryId = mBean.getLotteryId();
			holder.icon.setImageResource(LotteryId.getLotteryIcon(lotteryId));
			holder.name.setText(LotteryId.getLotteryName(lotteryId));
			holder.issue.setText("第"+mBean.getIssue()+"期");
			
			holder.result.removeAllViews();
			
			//显示球
			try {
				holder.result.addView(ViewUtil.getAutoView(getApplicationContext(), lotteryId, mBean.getBonusNumber(), true, 1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return convertView;
		}
	}
	
	static class ViewHolder {
		View item;
		ImageView icon;
		TextView type;
		TextView name;
		TextView issue;
		LinearLayout result;
    }
	
	private void jumpToHistory(String lotteryId){
//		StatService.onEvent(getApplicationContext(), BaiduStatistics.LotteryHall, LotteryId.getLotteryName(lotteryId), 1);
		Bundle mBundle = new Bundle();
		mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID,lotteryId);
		Intent intent = new Intent();
		intent.setClass(LotteryResultHallActivity.this, LotteryResultHistoryActivity.class); //LotteryResultHistoryActivity  WebViewActivity
		intent.putExtra(Settings.BUNDLE,mBundle);
		startActivity(intent);
	}
	
	static class Holder{
		TextView tx;
	}
	
}
