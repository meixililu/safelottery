package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.zch.safelottery.bean.ChargeAccountBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.ChargeAccountParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.GetString;

public class ChargeAccountHistoryActivity extends ZCHBaseActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "AccountActivity";
	
	private ProgressBar progressbar_m;
	private TextView noneText, oldoop;
	private PullToRefreshListView listView;
	private ImageButton refresh;
	
	private String fillResources;
	private String status;
	private int page = 1;
	private int totalpage = 0;
	
	private Animation animation;
	
	private ArrayList<ChargeAccountBean> currentList;
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
		title.setText("充值记录");
		oldoop = (TextView) findViewById(R.id.oldoop);
		progressbar_m = (ProgressBar)findViewById(R.id.account_progressbar_m);
		noneText = (TextView) findViewById(R.id.account_none);
		listView = (PullToRefreshListView) findViewById(R.id.account_list);
		refresh = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		noneText.setVisibility(View.VISIBLE);
		currentList = new ArrayList<ChargeAccountBean>();
		//allList = new ArrayList<AccountInfoForList>();
		mAdapter = new AccountDetailAdapter();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(itemListener);
		refresh.setOnClickListener(onClickListener);
		oldoop.setOnClickListener(onClickListener);
	}
	
	 private void initData(){
		 listView.setMode(Mode.BOTH);
		 listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				switch (refreshView.getCurrentMode()) {
				case PULL_FROM_START:
					refresh();
					break;
				case PULL_FROM_END:
					if(page <= totalpage)
						requsetDataTask();
					break;

				default:
					break;
				}
			}
		});
	}
	
	private String getStatus(String type) {

		if (type.equals("0")) {
			return "处理中";
		} else if (type.equals("1")) {
			return "成功";
		}else if (type.equals("2")) {
			return "失败";
		}

		return "";
	}
	
	private Dialog dialog;
    private void showTakeDetailDialog(ChargeAccountBean mBean) {
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
		money.setText("金额："+mBean.getAmount());
		sort.setText("充值类型：" + mBean.getFillResourcesName());
		status.setText("充值状态："+ getStatus(mBean.getStatus()));
		id.setText("订单号："+mBean.getOrderId());
//		someelse.setText("备注："+ mBean.getPlatform());
		memo.setVisibility(View.GONE);
		
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
	 
	 private void requsetDataTask(){
		 Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("fillResources", fillResources);
		map.put("status", status);
		map.put("page", page);
		map.put("pageSize", 20);
		
		noneText.setVisibility(View.GONE);
		if(currentList.size() == 0) 
			progressbar_m.setVisibility(View.VISIBLE);
		
		refresh.startAnimation(AnimationUtil.getRotateCenter(ChargeAccountHistoryActivity.this));
		SafelotteryHttpClient.post(this, "3202", "list", JsonUtils.toJsonStr(map), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					totalpage = Integer.parseInt( (String) mMap.get("pageTotal"));
					List<ChargeAccountBean> list = (ArrayList<ChargeAccountBean>) JsonUtils.parserJsonArray((String)mMap.get("fillList"), new ChargeAccountParser());
					if(list != null){
						if(page==1 && list.size()==0){
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
							page ++;
							
						}
						if(page > totalpage){
							listView.setMode(Mode.PULL_FROM_START);
						}else{
							listView.setMode(Mode.BOTH);
						}
					}else{
						if(page == 1)
							noneText.setVisibility(View.VISIBLE);
						else{
							page--;
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
				
				holder.sort.setText("充值类型");
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			ChargeAccountBean mBean = currentList.get(position);
			
			holder.time.setText(mBean.getCreateTime());
			holder.moneyValue.setText(mBean.getAmount());
			holder.sortValue.setText(mBean.getFillResourcesName());
			holder.notesValue.setText(getStatus(mBean.getStatus()));
			return convertView;
		}
	}
	
	static class ViewHolder{
		
		private TextView time;
		private TextView sort;
		private TextView moneyValue;
		private TextView sortValue;
		private TextView notesValue;
		
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.lotter_refresh_btn){
				refresh();
			}else if(v.getId() == R.id.oldoop){
				Intent intent = new Intent(ChargeAccountHistoryActivity.this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL,RecordBetActivity.oldv);
				ChargeAccountHistoryActivity.this.startActivity(intent);
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