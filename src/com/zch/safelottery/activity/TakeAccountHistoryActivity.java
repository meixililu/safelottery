package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.TakeAccountBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.TakeAccountParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.GetString;

public class TakeAccountHistoryActivity extends ZCHBaseActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "AccountActivity";
	
	private ProgressBar progressbar_m;
	private TextView noneText, oldoop;
	private PullToRefreshListView listView;
	private ImageButton refresh;
	
	private String drawResources;
	private String status;
	private int page = 1;
	private int totalpage = 0;
	
	private Animation animation;
	
	private ArrayList<TakeAccountBean> currentList;
	//private ArrayList<AccountInfoForList> allList;
	private AccountDetailAdapter mAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.take_account);
        initViews();
        initData();

		requsetDataTask();
    }
    
	private void initViews() {
		animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
		
		TextView title = (TextView) findViewById(R.id.account_title);
		title.setText("提款记录");
		oldoop = (TextView) findViewById(R.id.oldoop);
		progressbar_m = (ProgressBar)findViewById(R.id.account_progressbar_m);
		noneText = (TextView) findViewById(R.id.account_none);
		listView = (PullToRefreshListView) findViewById(R.id.account_list);
		refresh = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		noneText.setVisibility(View.VISIBLE);
		currentList = new ArrayList<TakeAccountBean>();
		//allList = new ArrayList<AccountInfoForList>();
		mAdapter = new AccountDetailAdapter();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(itemListener);
		refresh.setOnClickListener(onClickListener);
		oldoop.setOnClickListener(onClickListener);
	}
	
	 private void initData(){
		 listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				switch (refreshView.getCurrentMode()) {
				case PULL_FROM_START:
					refresh();
					break;
				case PULL_FROM_END:
					if (page <= totalpage) {
						requsetDataTask();
					}
					break;

				default:
					break;
				}
			}
		});
	}
	
	private String getStatus(String type) {

		if (type.equals("0")) {
			return "待审";
		} else if (type.equals("1")) {
			return "等待打款";
		}else if (type.equals("2")) {
			return "驳回";
		}else if (type.equals("3")) {
			return "成功";
		}

		return "";
	}
	
	private Dialog dialog;
    private void showTakeDetailDialog(TakeAccountBean mBean) {
		// TODO Auto-generated method stub
    	LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.account_detail_dialog, null);
		
		TextView time = (TextView) view.findViewById(R.id.account_detail_time);
		TextView money = (TextView) view.findViewById(R.id.account_detail_amount);
		TextView sort = (TextView) view.findViewById(R.id.account_detail_short);
		TextView status = (TextView) view.findViewById(R.id.account_detail_status);
		TextView id = (TextView) view.findViewById(R.id.account_detail_order_id);
		TextView memo = (TextView) view.findViewById(R.id.account_detail_memo);
		Button close = (Button) view.findViewById(R.id.account_detail_close);
		
		time.setText("交易时间："+mBean.getCreateTime());
		money.setText("提款金额："+mBean.getAmount());
		sort.setText("提款类型：" + getResources(mBean.getDrawResources()));
		status.setText("提款状态："+ getStatus(mBean.getStatus()));
		id.setText("订单号："+mBean.getOrderId());
		memo.setText("备注："+ ((TextUtils.isEmpty(mBean.getMemo()) || mBean.getMemo().equals("null"))? "无" : mBean.getMemo()));
		
		close.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = new Dialog(this,R.style.dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();
	}
    
	private void refresh(){
		page = 1;
		totalpage = 0;
		requsetDataTask();
	}
    
	 private void requsetDataTask() {
		 noneText.setVisibility(View.GONE);
		 if(currentList.size() == 0)
			 progressbar_m.setVisibility(View.VISIBLE);
		 
		 refresh.startAnimation(AnimationUtil.getRotateCenter(TakeAccountHistoryActivity.this));
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("userCode", GetString.userInfo.getUserCode());
		 map.put("drawResources", drawResources);
		 map.put("status", status);
		 map.put("page", page);
		 map.put("pageSize", 20);
		SafelotteryHttpClient.post(this, "3203", "list", JsonUtils.toJsonStr(map), new TypeMapHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					totalpage = Integer.parseInt((String) mMap.get("pageTotal"));
					List<TakeAccountBean> list = (ArrayList<TakeAccountBean>) JsonUtils.parserJsonArray((String)mMap.get("drawList"), new TakeAccountParser());
					if(list != null){
						if(list.size()==0&&page==1){
							noneText.setVisibility(View.VISIBLE);
							currentList.clear();
							mAdapter.notifyDataSetChanged();
						}else{
							if(page == 1)
								currentList.clear();
							
							currentList.addAll(list);
							mAdapter.notifyDataSetChanged();
							noneText.setVisibility(View.GONE);
							if(page == 1)
								listView.startAnimation(animation);
							
							page++;
						}
						if(page > totalpage){
							listView.setMode(Mode.PULL_FROM_START);
						}else{
							listView.setMode(Mode.BOTH);
						}
					}else{
						if(page == 1)
							noneText.setVisibility(View.VISIBLE);
						else
							page--;
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
				progressbar_m.setVisibility(View.GONE);
				refresh.clearAnimation();
				listView.onRefreshComplete();
			}
		});
	}
	
	class AccountDetailAdapter extends BaseAdapter{

		private LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		public int getCount() {
			
			return currentList.size();
		}

		public Object getItem(int position) {
			
			return currentList.get(position);
		}

		public long getItemId(int position) {
			
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			
			if(convertView == null){
				
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.account_detail_item, null);
				holder.time = (TextView) convertView.findViewById(R.id.account_detail_item_time);
				holder.sort = (TextView) convertView.findViewById(R.id.account_detail_item_sort);
				holder.moneyValue = (TextView) convertView.findViewById(R.id.account_detail_item_money_value);
				holder.sortValue = (TextView) convertView.findViewById(R.id.account_detail_item_sort_value);
				holder.notesValue = (TextView) convertView.findViewById(R.id.account_detail_item_notes_value);
				
				holder.sort.setText("手续费");
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			TakeAccountBean ail = currentList.get(position);
			holder.time.setText(ail.getCreateTime());
			holder.moneyValue.setText(ail.getAmount());
			holder.sortValue.setText(ail.getFee());
			holder.notesValue.setText(getStatus(ail.getStatus()));
			return convertView;
		}
	}
	
	private static class ViewHolder{
		
		private TextView time;
		private TextView sort;
		private TextView moneyValue;
		private TextView sortValue;
		private TextView notesValue;
		
	}
	
	private String getResources(String resources){
		if(resources.equals("01")){
			return "银行卡";
		} else if(resources.equals("02")){
			return "支付宝";
		}
		return "其他";
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.lotter_refresh_btn){
				refresh();
			}else if(v.getId() == R.id.oldoop){
				Intent intent = new Intent(TakeAccountHistoryActivity.this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL,RecordBetActivity.oldv);
				TakeAccountHistoryActivity.this.startActivity(intent);
			}
		}
	};
	
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			showTakeDetailDialog(currentList.get(arg2 - 1));
		}
	};
}