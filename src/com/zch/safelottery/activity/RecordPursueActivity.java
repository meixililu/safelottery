package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordPursueBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.parser.RecordPursueParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.view.TitleViews;

public class RecordPursueActivity extends ZCHBaseActivity implements OnClickListener{

    private final boolean DEBUG = Settings.DEBUG;
    private final String TAG = "BetRecordActivity";
    
    private TextView type, method, oldoop;
    private PopWindowDialog mDialogType, mDialogMethod;
    
    private ProgressBar progressbar;
    private TextView load_result;
    private RelativeLayout title;
    private PullToRefreshListView lv;
    private View root_view;
    private TextView method_title;
    private ArrayList<RecordPursueBean> result_list;
    private MyListViewAdapter lv_adapter;
    
    private String lotteryId = "";
    private String userCode;
    
    private String orderStatus = "";
    
    private int page = 1;
    private int pageSize = 10;
    private int pageTotal;
    byte[] data,result;
    
    private float mDialogY;
    private boolean isAddListenner = true;
    
    private TitleViews titleViews;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.d(Settings.TAG, TAG+"-onCreate()");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bet_record); 
//        Intent intent = getIntent();
//        lotteryId = intent.getStringExtra(Settings.INTENT_STRING_LOTTERY_ID);//彩种编号
//        orderStatus = intent.getIntExtra(Settings.INTENT_STRING_BUY_METHOD,-1); //购买方式
        initUI();
        initData();
        addTitle();
        userCode = GetString.userInfo.getUserCode();
        if(userCode != null && !userCode.equals("")){
			requsetDataTask();
        }else{
        	Toast.makeText(RecordPursueActivity.this, "登录超时，请重新登录！", 0).show();
        }
    }
    
    private void initUI(){
    	if (DEBUG) Log.d(Settings.TAG, TAG+"-initUI()");
    	title = (RelativeLayout) findViewById(R.id.bet_record_title);
    	method_title = (TextView) findViewById(R.id.bet_record_method_title);
    	oldoop = (TextView) findViewById(R.id.oldoop);
    	method_title.setText("追号状态");
    	root_view = (View) findViewById(R.id.bet_record_root_view);
    	type = (TextView) findViewById(R.id.bet_record_spinner_type);
    	method = (TextView) findViewById(R.id.bet_record_spinner_buy_method);
    	
    	progressbar = (ProgressBar) findViewById(R.id.bet_record__progressbar);
    	load_result = (TextView) findViewById(R.id.bet_record_load_result);
    	lv = (PullToRefreshListView) findViewById(R.id.bet_record_listview);
    	
    	type.setText(LotteryId.getLotteryName(lotteryId));
    	method.setText("全部");
    	mDialogY = 34f + ScreenUtil.dip2px(getApplication(), 34);
    	
    	type.setOnClickListener(this);
    	method.setOnClickListener(this);
    	oldoop.setOnClickListener(this);
	}
    
    private void addTitle(){
		titleViews = new TitleViews(this, "我的追号");
		title.addView(titleViews.getView());
	}
    
    private void initData(){
        
        result_list = new ArrayList<RecordPursueBean>();
        lv_adapter = new MyListViewAdapter(getApplicationContext());
        lv.setAdapter(lv_adapter);
        //选中跳到详情
        lv.setOnItemClickListener(new ListClickListener());
        lv.setMode(Mode.BOTH);
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					refresh();
					requsetDataTask();
					break;
				case PULL_FROM_END:
					if (page <= pageTotal) {
						requsetDataTask();
					}
					break;
				}
			}
		});
    }
    
    
	private void showData(ArrayList<RecordPursueBean> mList) {
		if(mList == null){
			ToastUtil.diaplayMesShort(getApplicationContext(), "暂无相关数据请刷新重试");
		}else{
			if(page == 1){
				result_list.clear();
			}
			result_list.addAll(mList);
			if(result_list.size() > 0){
				lv_adapter.notifyDataSetChanged();
				if (page == 1)
					lv.startLayoutAnimation();
				page++;
				if(page > pageTotal){
					lv.setMode(Mode.PULL_FROM_START);
				}else{
					lv.setMode(Mode.BOTH);
				}
			}
		}
	}
    
    private void refresh(){
    	load_result.setVisibility(View.GONE);
    	page = 1;
    }
    
    private void requsetDataTask() {
    	if(result_list.size() == 0){
			progressbar.setVisibility(View.VISIBLE);
		}
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("userCode", userCode);
    	map.put("lotteryId", lotteryId);
    	map.put("orderStatus", String.valueOf(orderStatus));
    	map.put("page", String.valueOf(page));
    	map.put("pageSize", String.valueOf(pageSize));
//			map.put("bonusStatus", bonusStatus);
//			map.put("startTime", startTime);
//			map.put("endTime", endTime);
    	
		SafelotteryHttpClient.post(this, "3303", "autoOrder", JsonUtils.toJsonStr(map), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					pageTotal = Integer.parseInt((String) mMap.get("pageTotal"));
					ArrayList<RecordPursueBean> mList = (ArrayList<RecordPursueBean>) JsonUtils.parserJsonArray((String) mMap.get("orderList"), new RecordPursueParser());

					if(isAddListenner){
				        isAddListenner = false;
					}
					if(mList!=null){
						if(page==1&&mList.size()==0){
							load_result.setVisibility(View.VISIBLE);
						}else{
							load_result.setVisibility(View.GONE);
						}
					}else{
						if(page==1){
							load_result.setVisibility(View.VISIBLE);
						}
					}
					showData(mList);
				
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
				progressbar.setVisibility(View.GONE);
			}
		});
		
	}
    
    private class ListClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View arg1, int position,long arg3) {
			RecordPursueBean mBean = (RecordPursueBean) parent.getItemAtPosition(position);
			Intent intent = new Intent();
			intent.setClass(RecordPursueActivity.this, RecordPursueAgentActivity.class);
			intent.putExtra("autoOrderId", mBean.getAutoOrderId());
			startActivity(intent);
		}
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
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.bet_record_list_item, null);
				holder.lottery_name_image = (ImageView)convertView.findViewById(R.id.bet_record_lottery_name_image);
				holder.lottery_name = (TextView)convertView.findViewById(R.id.bet_record_lottery_name);
//				holder.lottery_issue = (TextView)convertView.findViewById(R.id.bet_record_lottery_issue);
				holder.lottery_time = (TextView)convertView.findViewById(R.id.bet_record_lottery_time);
				holder.lottery_money = (TextView)convertView.findViewById(R.id.bet_record_lottery_money);
				holder.lottery_reward = (TextView)convertView.findViewById(R.id.bet_record_lottery_reward);
				holder.lottery_result = (TextView)convertView.findViewById(R.id.bet_record_lottery_result);
				
				holder.lottery_record_money = (TextView) convertView.findViewById(R.id.record_money);
				holder.lottery_record_money_sum = (TextView) convertView.findViewById(R.id.record_money_sum);
				holder.lottery_record_state = (TextView) convertView.findViewById(R.id.record_state);
				
				holder.lottery_record_money.setText("已追金额");
				holder.lottery_record_money_sum.setText("总金额");
				holder.lottery_record_state.setText("追号状态");
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			RecordPursueBean mBean = result_list.get(position);
			String lotteryId = mBean.getLotteryId();
			holder.lottery_name_image.setBackgroundResource( LotteryId.getLotteryIcon(lotteryId) );
			holder.lottery_name.setText( LotteryId.getLotteryName(lotteryId) );
//			String issue = result_list.get(position).getIssue();
//			if(result_list.get(position).getLid().equals(LotteryId.SSC)){
//				issue = issue.substring(4);
//			}else if(result_list.get(position).getLid().equals(LotteryId.XYNC)){
//				issue = issue.substring(2);
//			}
//			holder.lottery_issue.setText( getIssue(lid,issue) );
			holder.lottery_time.setText( TimeUtils.getLongtimeToShorttime(mBean.getCreateTime()));
			
			holder.lottery_money.setText( "￥" + (int)ConversionUtil.StringToDouble(mBean.getCompleteAmount()));
			holder.lottery_reward.setText( "￥" + (int)ConversionUtil.StringToDouble(mBean.getOrderAmount()));
			//状态
			holder.lottery_result.setText(getOrderStatus(mBean));
			
			return convertView;
		}
	}
	
	static class ViewHolder {
		ImageView lottery_name_image;
		TextView lottery_name;
		
		TextView lottery_record_money;
		TextView lottery_record_money_sum;
		TextView lottery_record_state;
		
//		TextView lottery_issue;
		TextView lottery_time;
		TextView lottery_money;
		TextView lottery_reward;
		TextView lottery_result;
    }
	
	private String getOrderStatus(RecordPursueBean mBean){
		if(mBean.getOrderStatus().equals("1")){
			return "已完成";
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append("在追(");
			sb.append(Integer.parseInt(mBean.getSuccessIssue()) + Integer.parseInt(mBean.getFailureIssue()) + Integer.parseInt(mBean.getCancelIssue()));
			sb.append("/");
			sb.append(mBean.getTotalIssue());
			sb.append(")");
			return sb.toString();
		}
	}
	
	private void showDialogType(){
    	if(mDialogType == null){
    		//敢得当前控件所在的位置
			LinearLayout lny = (LinearLayout) findViewById(R.id.bet_record_lny);
			LinearLayout typeLny = (LinearLayout) findViewById(R.id.bet_record_type_lny);
			
			int popDialogY = lny.getTop() + typeLny.getHeight();
			
			String orderstr = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE).getString(LotteryInfoParser.LotteryOrderStrKey, "");
			final List<String> lotteryArray = LotteryId.getPursueLotteryName(orderstr, new int[]{LotteryId.LOTTERY_TYPE_JC, LotteryId.LOTTERY_TYPE_ZC});
			
    		mDialogType = new PopWindowDialog(this, lotteryArray, lotteryArray.indexOf(LotteryId.getLotteryName(lotteryId)));
    		mDialogType.setPopViewPosition(0, popDialogY);
    		
    		mDialogType.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogType.getList();
					CheckBox checkBox;
					
					for(int i = 0, size = list.size(); i < size; i++){
						checkBox = list.get(i);
						if(i == v.getId()){
							String t = lotteryArray.get(i);
							checkBox.setChecked(true);
							lotteryId = LotteryId.getLotteryLid(t);
							refresh();
							requsetDataTask();
							type.setText(t);
						}else{
							if(checkBox.isChecked()) checkBox.setChecked(false);
						}
					}
					mDialogType.dismiss();
				}
			});
    		
    		mDialogType.show();
    	}else{
    		if(mDialogType.isShowing())
    			mDialogType.dismiss();
    		else
    			mDialogType.show();
    	}
    }
	
	private void showDialogMethod(){
		if(mDialogMethod == null){
			//敢得当前控件所在的位置
			LinearLayout lny = (LinearLayout) findViewById(R.id.bet_record_lny);
			LinearLayout typeLny = (LinearLayout) findViewById(R.id.bet_record_type_lny);
			
			int popDialogY = lny.getTop() + typeLny.getHeight();
			
			final String[] item = new String[]{"全部", "进行中", "已完成"};
			mDialogMethod = new PopWindowDialog(this, item, 0);
			mDialogMethod.setPopViewPosition(0, popDialogY);
			
			mDialogMethod.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogMethod.getList();
					CheckBox checkBox;
					
					for(int i = 0, size = list.size(); i < size; i++){
						checkBox = list.get(i);
						if(i == v.getId()){
							checkBox.setChecked(true);
							
							orderStatus = String.valueOf((i - 1) < 0? "": i - 1);
							refresh();
							requsetDataTask();
							method.setText(item[i]);
						}else{
							if(checkBox.isChecked()) checkBox.setChecked(false);
						}
					}
					mDialogMethod.dismiss();
				}
			});
			
			mDialogMethod.show();
		}else{
			if(mDialogMethod.isShowing())
				mDialogMethod.dismiss();
    		else
    			mDialogMethod.show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() ==  R.id.bet_record_spinner_type){
			showDialogType();
		}else if(v.getId() == R.id.bet_record_spinner_buy_method){
			showDialogMethod();
		}else if(v.getId() == R.id.oldoop){
			Intent intent = new Intent(RecordPursueActivity.this, WebViewActivity.class);
			intent.putExtra(WebViewActivity.URL,RecordBetActivity.oldv);
			RecordPursueActivity.this.startActivity(intent);
		}
		
	}
}
