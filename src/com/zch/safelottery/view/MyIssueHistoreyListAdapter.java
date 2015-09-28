package com.zch.safelottery.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.LotteryIssueHistoryBean;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ViewUtil;

public class MyIssueHistoreyListAdapter  extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private ArrayList<LotteryIssueHistoryBean> result_list;
	private Context mContext;
	private String lotteryId;
	
	public MyIssueHistoreyListAdapter (Context context, ArrayList<LotteryIssueHistoryBean> result_list, String lotteryId) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.result_list = result_list;
		this.lotteryId = lotteryId;
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
			convertView = mInflater.inflate(R.layout.k3_issue_history_list_item, null);
			holder = new ViewHolder();
			holder.item = (LinearLayout)convertView.findViewById(R.id.lottery_result_history_result);
			holder.time = (TextView)convertView.findViewById(R.id.lottery_result_history_cz_time);
			holder.issue = (TextView)convertView.findViewById(R.id.lottery_result_history_cz_issue);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		LotteryIssueHistoryBean mBean = result_list.get(position);
		
		holder.issue.setText("第"+ mBean.getName() +"期");
		holder.time.setText(TimeUtils.customFormatDate(mBean.getBonusTime(), TimeUtils.DateFormat, TimeUtils.DayFormat));
		
		holder.item.removeAllViews();
		try {
			holder.item.addView(ViewUtil.getAutoView(mContext, lotteryId, mBean.getBonusNumber(), true, 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class ViewHolder {
		LinearLayout item;
		TextView time;
		TextView issue;
    }
}
