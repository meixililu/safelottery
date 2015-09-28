package com.zch.safelottery.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.ResultListBean;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.combinebean.CbBuyListBean;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.parser.CbBuyListParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.ResultListParser;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;

public class CombineBuyListDialog extends Dialog {

	private Context mContext;
	private String userCode;
	private String orderId;
	private int page = 1;
	private int pageSize = 30;
	private int pageTotal;
	
	private int flag;
	
	private TextView title;
	private TextView text1, text2, text3;
	private ListView mListView;
	private ProgressBar mProgressBar;
	
	private Button cancel;
	private Button sure;
	
	private MyListViewAdapter lv_adapter;
	
	private boolean isAsyncTask;
	
	
	private List<CbBuyListBean> resultList = new ArrayList<CbBuyListBean>();
//	List<>
	
	/**
	 * 构造
	 * @param mContext 当前this
	 * @param userCode 用户Code
	 * @param orderId 方案Id
	 */
	public CombineBuyListDialog(Context mContext, String userCode, String orderId){
		this(mContext, userCode, orderId, 0);
	}
	
	/**
	 * 构造
	 * @param mContext 当前this
	 * @param userCode 用户Code
	 * @param orderId 方案Id
	 * @param flag 当前的入口标志 1 为完成 0为未完成
	 */
	public CombineBuyListDialog(Context mContext, String userCode, String orderId, int flag){
		super(mContext, R.style.dialog);
		this.mContext = mContext;
		this.userCode = ""; //userCode;
		this.orderId = orderId;
		this.flag = flag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_list_show);
		title = (TextView) findViewById(R.id.normal_dialog_title);
		text1 = (TextView) findViewById(R.id.dialog_list_show_text1);
		text2 = (TextView) findViewById(R.id.dialog_list_show_text2);
		text3 = (TextView) findViewById(R.id.dialog_list_show_text3);
		
		mListView = (ListView) findViewById(R.id.dialog_list_show_list);
		mProgressBar = (ProgressBar) findViewById(R.id.dialog_list_show__progressbar);
		
		cancel = (Button) findViewById(R.id.normal_dialog_cancel);
		sure = (Button) findViewById(R.id.normal_dialog_sure);
		
		cancel.setVisibility(View.GONE);
		sure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		if(flag == 1){
			setContentTitle("用户名", "认购金额", "奖金分成");
		}else{
			setContentTitle("用户名", "认购时间", "认购金额");
		}
		
		setCanceledOnTouchOutside(true);
		addData();
		startTask();
	}
	
	/**
	 * 设置Title
	 * @param arg 显示的文本
	 */
	public void setTitle(String arg){
		title.setText(arg);
	}
	
	private void setContentTitle(String arg1, String arg2, String arg3){
		text1.setText(arg1);
		text2.setText(arg2);
		text3.setText(arg3);
	}
	
	private void addData(){

		lv_adapter = new MyListViewAdapter();
		mListView.setAdapter(lv_adapter);
		mListView.setOnScrollListener(new OnScrollListener() {
			int lastItem,mtotalItemCount;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(lastItem == mtotalItemCount && (scrollState == OnScrollListener.SCROLL_STATE_IDLE ||
						scrollState == OnScrollListener.SCROLL_STATE_FLING)){
					if(!isAsyncTask){
						if(page < pageTotal){
							page++;
							startTask();
						}else{
							if(mtotalItemCount > pageSize - 2){
								ToastUtil.diaplayMesShort(mContext, "已是最后一页");
							}
						}
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount;
				mtotalItemCount = totalItemCount;
			}
		});
		
	}
	
	private void startTask(){
		if(isAsyncTask){
			ToastUtil.diaplayMesShort(mContext, "正在提交上一次的请求，请稍后再试！");
		}else{
			MyAsyncTask();
		}
	}
	
	private void setData(String result){
		if(TextUtils.isEmpty(result)){
			ToastUtil.diaplayMesShort(mContext, "暂无数据");
		}else{
			List<CbBuyListBean> list = (List<CbBuyListBean>) JsonUtils.parserJsonArray(result, new CbBuyListParser());
			resultList.addAll(list);
			lv_adapter.notifyDataSetChanged();
		}
	}
	
	private void MyAsyncTask(){
		mProgressBar.setVisibility(View.VISIBLE);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("programsOrderId", orderId);
		map.put("userCode", userCode);
		map.put("page", page);
		map.put("pageSize", pageSize);
		SafelotteryHttpClient.post(mContext, "3303", "subscribe", JsonUtils.toJsonStr(map), new TypeSafelotteryHttpResponseHandler(
				mContext,true,new ResultListParser()) {
				@Override
				public void onSuccess(int statusCode, SafelotteryType result) {
					if(result instanceof ResultListBean){
						ResultListBean mBean = (ResultListBean) result;
						page = ConversionUtil.StringToInt(mBean.getPage());
						pageSize = ConversionUtil.StringToInt(mBean.getPageSize());
						pageTotal = ConversionUtil.StringToInt(mBean.getPageTotal());
						setData(mBean.getOrderList());
					}
					if(resultList.size() == 0){
						ToastUtil.diaplayMesShort(mContext, "暂无数据");
					}
				}
				@Override
				public void onFinish() {
					mProgressBar.setVisibility(View.GONE);
				}
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
				}
		});
	}
	
	private class MyListViewAdapter extends BaseAdapter {

		private CbBuyListBean mBean;
		
		public int getCount() {
			return resultList.size();
		}

		public Object getItem(int position) {
			return resultList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.combine_buy_list_item, null);
				holder.userName = (TextView) convertView.findViewById(R.id.buy_list_item_name);
				holder.timeBuy = (TextView) convertView.findViewById(R.id.buy_list_item_time);
				holder.amountBuy = (TextView) convertView.findViewById(R.id.buy_list_item_amount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			mBean = resultList.get(position);
			holder.userName.setText(mBean.getUserName());
			
			if(flag == 1){ //认购完成 查看每个人的分成奖金
				holder.timeBuy.setText(mBean.getOrderAmount() + "元");
				holder.amountBuy.setText(mBean.getBonusAmount() + "元");
			}else{
				holder.timeBuy.setText(TimeUtils.getyyMMddHHmm(mBean.getCreateTime()));
				holder.amountBuy.setText(mBean.getOrderAmount() + "元");
			}
			
			return convertView;
		}
	}
	
	static class ViewHolder {
		TextView userName;
		TextView timeBuy;
		TextView amountBuy;
	}
	
	@Override
	public void show() {
		super.show();
	}
}
