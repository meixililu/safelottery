package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.FeedListBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.FeedListBeanParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;
/*
 * 问题反馈
 */
public class QuestionActivity extends ZCHBaseActivity {
	
	private PullToRefreshListView lv;
	private EditText quest_ed;
	private ProgressBar top_progressbar;
	private Button quest_submit;
	private int page = 1;
	private int pageTotal;
	private int itemTotal;
	private int pageSize = 20;
	
	private ArrayList<FeedListBean> feedList;
	private MyListViewAdapter adapter;
	private String user_input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);
		initViews();
		requsetDataTask();
	}

	private void initViews() {
		lv = (PullToRefreshListView) findViewById(R.id.quest_listview);
		quest_ed = (EditText) findViewById(R.id.quest_ed);
		quest_submit = (Button) findViewById(R.id.quest_submit);
		top_progressbar = (ProgressBar) findViewById(R.id.quest_top_progressbar);
		
		quest_submit.setOnClickListener(onClickListener);
		feedList = new ArrayList<FeedListBean>();
		adapter =  new MyListViewAdapter(this);
		lv.setAdapter(adapter);
		lv.setMode(Mode.BOTH);
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
	            	page = 1;
	            	requsetDataTask();
					break;
				case PULL_FROM_END:
					if (page <= pageTotal) {
						page++;
						requsetDataTask();
					}
					break;
				}
			
			}
			
		});
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.quest_submit:
				submit();
				break;
			default:
				break;
			}
		}
	};
	
	private void submit(){
		user_input = quest_ed.getText().toString();
		if(!TextUtils.isEmpty(user_input)){
			if(GetString.userInfo != null){
				sendTask(user_input);
			}else{
				ToastUtil.diaplayMesShort(getApplicationContext(), "用户信息异常，请重新登录后再试！");
			}
		}else{
			ToastUtil.diaplayMesShort(getApplicationContext(), "请输入您的问题");
		}
	}
	
	private void requsetDataTask() {
		quest_submit.setEnabled(false);
//		if(feedList.size() == 0)
			top_progressbar.setVisibility(View.VISIBLE);
		
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("page", page+"");
		map.put("pageSize", pageSize+"");
		String questStr = JsonUtils.toJsonStr(map);
		SafelotteryHttpClient.post(this, "3204", "list", questStr, new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					pageTotal = ConversionUtil.StringToInt( (String)mMap.get("pageTotal") );
					itemTotal = ConversionUtil.StringToInt( (String)mMap.get("itemTotal") );
					String listStr = (String) mMap.get("feedList");
					if(!TextUtils.isEmpty(listStr)){
						ArrayList<FeedListBean> list = (ArrayList<FeedListBean>) JsonUtils.parserJsonArray(listStr, new FeedListBeanParser());
						if(list != null){
							if(page == 1)
								feedList.clear();
							feedList.addAll(list);
							try {
								adapter.notifyDataSetChanged();
								page++;
								if(page > pageTotal){
									lv.setMode(Mode.PULL_FROM_START);
								}else{
									lv.setMode(Mode.BOTH);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
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
			
			@Override
			public void onFinish(){
				lv.onRefreshComplete();
				quest_submit.setEnabled(true);
				top_progressbar.setVisibility(View.GONE);
			}
		});
	}
	
	
	private void sendTask(String... params){
		
		final ProgressDialog progresdialog = ProgressDialog.show(QuestionActivity.this, "", "正在提交您的反馈", true,true);
		HashMap<String ,String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("feedType", "0");
		map.put("feedInfo", params[0]);
		SafelotteryHttpClient.post(this, "3204", "user", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "提交成功");
				FeedListBean mFeedListBean = new FeedListBean();
				mFeedListBean.setFeedInfo(user_input);
				feedList.add(0,mFeedListBean);
				quest_ed.setText("");
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish(){
				progresdialog.dismiss();
			}
		});
	}
	
	class MyListViewAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		
		public MyListViewAdapter (Context context) {
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return feedList.size();
		}

		@Override
		public Object getItem(int position) {
			return feedList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.feedback_list_item, null);
				holder = new ViewHolder();
				holder.client_tv = (TextView)convertView.findViewById(R.id.client_tv);
				holder.service_tv = (TextView)convertView.findViewById(R.id.service_tv);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			FeedListBean bean = feedList.get(position);
			String handleStr = bean.getHandleInfo();
			holder.client_tv.setText(bean.getFeedInfo());
			if(!TextUtils.isEmpty(handleStr) && !handleStr.equals("null")){
				holder.service_tv.setVisibility(View.VISIBLE);
				holder.service_tv.setText(handleStr);
			}else{
				holder.service_tv.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder {
		TextView client_tv;
		TextView service_tv;
    }
}
