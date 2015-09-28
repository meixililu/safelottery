package com.zch.safelottery.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class ListLineFeedView {
	private Context mContext;
	private List< ? extends View> list;
	
	private int size;
	private int row; //行
	private int col; //列
	
	/**
	 * @param mContext 当然的Activity
	 * @param list 显示View的List列表
	 */
	public ListLineFeedView( Context mContext, List<? extends View> list){
		this(mContext, list, 4);
	}
	
	/**
	 * @param mContext 当然的Activity
	 * @param list 显示View的List列表
	 * @param col 列
	 */
	public ListLineFeedView( Context mContext, List<? extends View> list, int col){
		this.mContext = mContext;
		setData(list, col);
	}
	
	public void setData(List<? extends View> list, int col){
		this.list = list;
		size = list.size();
		initData(size, col);
	}
	
	private void initData(int size, int col){
		this.col = col;
		row = size/col;
		if(size % col > 0){
			row += 1;
		}
	}
	
	public View getView(){
		final LinearLayout rowView = new LinearLayout(mContext);
		rowView.setOrientation(LinearLayout.VERTICAL);
		for(int i = 0; i < row; i++){ //每行添加
			rowView.addView(addRowView(i, col));
		}
		return rowView;
	}
	
	private int index;
	private View addRowView(int row,int col){
		final LinearLayout colView = new LinearLayout(mContext);
		colView.setOrientation(LinearLayout.HORIZONTAL);
		colView.setWeightSum(col);
		for(int i = 0; i < col; i++){ // 每列
			index = col * row + i;
			if(index < size){
				colView.addView(list.get(index));
			}else{
				break;
			}
		}
		return colView;
	}
	
	public interface OnClickListListener{
		public void onClick(View v);
	}
}
