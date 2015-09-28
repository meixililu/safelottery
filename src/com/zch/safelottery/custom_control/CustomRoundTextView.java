package com.zch.safelottery.custom_control;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.K3SelectBean;
import com.zch.safelottery.util.ViewUtils;

public class CustomRoundTextView {

	public static final int rowValue = 10000;
	private Context mContext;
	
	private ArrayList<CheckBox> mCheckLists; 
	private ArrayList<K3SelectBean> resultSelect;
	
	private int initRowLenth;
	private int mGroupId;
	private boolean singleSelect;
	
	private OnRoundClickListener onClick;
	
	private CustomRoundTextView(Context context) {
		this.mContext = context;
	}
	
	public static CustomRoundTextView getCustomRoundTextView(Context context){
		return new CustomRoundTextView(context);
	}
	
	/**
	 * @param rowTitle
	 * @return
	 */
	private LinearLayout getTable(){
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.rounded_round);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		layout.setPadding(5, 5, 5, 5);
		return layout;
	}
	
	/**
	 * 设置Table
	 * @param table 一个Table表
	 * @param mList 数据 ArrayList<ArrayList<String>> List里套着一个List
	 * @param textSize 字体大小
	 * @return
	 */
	public LinearLayout setTable( String[][][] mGroup, int textSize){
		int si = mGroup.length;
		LinearLayout table = getTable();
		table.setWeightSum(si);
		initRowLenth = mGroup[0].length;
		mCheckLists = new ArrayList<CheckBox>();
		for(int i = 0; i < si; i++){
			//列
			LinearLayout rowTitle = new LinearLayout(mContext);
			rowTitle.setGravity(Gravity.CENTER_VERTICAL);
			rowTitle.setOrientation(LinearLayout.HORIZONTAL);
			rowTitle.setWeightSum(initRowLenth);
			LinearLayout.LayoutParams paramsRowTitle = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
			rowTitle.setLayoutParams(paramsRowTitle);
			String[][] tempGroup = mGroup[i];
//			System.out.println("si = " + si + "  sj = " +tempGroup.length);
			for(int j = 0, sj = tempGroup.length; j < sj; j++){
				//初始化一个View 放数据
				final CheckBox checkBox = (CheckBox) ViewUtils.getTextGroupVertical(mContext, tempGroup[j], textSize);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 1.0f);
				checkBox.setLayoutParams(params);
				checkBox.setId(i * initRowLenth + j);
				checkBox.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changeBack(checkBox, singleSelect);
						if(onClick != null){
							onClick.onClick(checkBox, mGroupId, checkBox.getId());
						}
//						System.out.println("选中Table Item --> " + checkBox.getId() + " is select -->" + checkBox.isChecked());
					}
				});
				mCheckLists.add(checkBox);
				rowTitle.addView(checkBox);
				if(j < sj - 1 || initRowLenth > sj && i == si - 1) {
					rowTitle.addView(ViewUtils.getRoundLine(mContext, R.layout.round_line_ver));
				}
			}
			table.addView(rowTitle);
			if(i < si - 1) table.addView(ViewUtils.getRoundLine(mContext, R.layout.round_line_hor));
		}
		return table;
	}
	
	private void changeBack(CheckBox checkBox){
		changeBack(checkBox, false);
	}
	
	private void changeBack(CheckBox checkBox, boolean singleSelect){
		if(singleSelect){
			clear();
			checkBox.setChecked(true);
			checkBox.setBackgroundColor(mContext.getResources().getColor(R.color.none_thin));
			
		}else{
			if(checkBox.isChecked()){
				checkBox.setBackgroundColor(mContext.getResources().getColor(R.color.none_thin));
			}else{
				checkBox.setBackgroundColor(mContext.getResources().getColor(R.color.none));
			}
		}
	}
	/**
	 * 清除选中的框
	 */
	public void clear(){
		for(CheckBox checkBox: mCheckLists){
			if(checkBox.isChecked()){
				checkBox.setChecked(false);
				changeBack(checkBox);
			}
		}
	}
	
	/**
	 * 给值，给定一个组合，使组合里相对应的ID选中
	 * @param group 要改变的组合
	 * @param initValue 初始值
	 */
	public void select(int[] group, int initValue){
		if(group != null){
			clear(); //先清除
			for(int i: group){
				CheckBox checkBox = mCheckLists.get(i - initValue);
				checkBox.setChecked(true);
				changeBack(checkBox);
			}
		}
	}
	/**
	 * 给值，给定一个组合，使组合里相对应的ID选中
	 * @param initValue 初始值
	 */
	public void select(int select) {
		clear(); // 先清除
		CheckBox checkBox = mCheckLists.get(select);
		checkBox.setChecked(true);
		changeBack(checkBox);
	}
	
	/**
	 * 取当前选中的结果
	 * @return 返回 ArrayList<Integer> 的结果集合
	 */
	public ArrayList<K3SelectBean> getResultSelect(){
		resultSelect = new ArrayList<K3SelectBean>();
		for(CheckBox checkBox: mCheckLists){
			if(checkBox.isChecked()){
				K3SelectBean mBean = new K3SelectBean();
				mBean.setGroupId(mGroupId);
				mBean.setItemId(checkBox.getId());
				mBean.setData(checkBox.getText().toString());
				resultSelect.add(mBean);
			}
//			System.out.print(checkBox.getId() + " : ");
		}
//		System.out.println();
		return resultSelect;
	}
	
	public void setOnRoundClickListener(OnRoundClickListener onClick){
		this.onClick = onClick;
	}
	
	public interface OnRoundClickListener{
		public void onClick(CheckBox checkBox, int groupId, int checkId);
	}
	
	public int getId() {
		return mGroupId;
	}

	public void setId(int id) {
		this.mGroupId = id;
	}

	public boolean isSingleSelect() {
		return singleSelect;
	}

	public void setSingleSelect(boolean singleSelect) {
		this.singleSelect = singleSelect;
	}
}
