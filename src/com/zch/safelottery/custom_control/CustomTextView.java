package com.zch.safelottery.custom_control;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.util.ViewUtils;

public class CustomTextView {

	private Context mContext;
	
	
	public CustomTextView(Context context) {
		this.mContext = context;
	}
	

	/**
	 * @param rowTitle
	 * @return
	 */
	public TableLayout getTable(){
		TableLayout table = new TableLayout(mContext);

		table.setStretchAllColumns(true);
//		table.setShrinkAllColumns(true);
		table.setBackgroundResource(R.drawable.issue_detail_brim_ground);
		return table;
	}
	
	/**
	 * @param mList 数据 ArrayList<ArrayList<String>> List里套着一个List
	 * @return
	 */
	public TableLayout setTable(ArrayList<ArrayList<String>> mList){
		return setTable(getTable(), mList, 0);
	}
	
	/**
	 * 设置Table
	 * @param table 一个Table表
	 * @param mList 数据 ArrayList<ArrayList<String>> List里套着一个List
	 * @param color 颜色传0
	 * @return
	 */
	public TableLayout setTable(TableLayout table, ArrayList<ArrayList<String>> mList, int color){
		
		for(int i = 0, si = mList.size(); i < si; i++){
			TableRow rowTitle = new TableRow(mContext);
			rowTitle.setGravity(Gravity.CENTER);
			ArrayList<String> list = mList.get(i);
			for(int j = 0, sj = list.size(); j < sj; j++){
				TextView text = ViewUtils.getCustomText(mContext);
				text.setText(list.get(j));
				
				if(i == 0){
					text.setBackgroundResource(R.drawable.issue_detail_brim_grey);
//					text.setTextSize(ScreenUtil.px2sp(mContext, 18f));
//					字体加粗
//					TextPaint paint = text.getPaint();  
//					paint.setFakeBoldText(true);
				}else{
					text.setBackgroundResource(R.drawable.issue_detail_brim_white);
				}
				
				text.setPadding(1, ScreenUtil.dip2px(mContext, 5), 1, ScreenUtil.dip2px(mContext, 5));
				rowTitle.addView(text);
			}
			table.addView(rowTitle);
		}
		return table;
	}
	
}
