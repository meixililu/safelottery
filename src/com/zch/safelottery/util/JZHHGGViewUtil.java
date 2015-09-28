package com.zch.safelottery.util;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.zch.safelottery.R;
import com.zch.safelottery.jingcai.JZActivity;

public class JZHHGGViewUtil {
	
	/**绘制竞彩足球混合过关表格 单行
	 * @param mContext
	 * @param parentView
	 * @param listener
	 * @param names
	 * @param column
	 * @return
	 */
	public static void getJZtableSingleLine(Context mContext,LinearLayout parentView, OnClickListener listener, 
			JSONArray names, String playMethod, int column, ArrayList<Integer> selectedItems) throws Exception{
		if(names != null){
			int size = names.length();
			int quyu = size % column;
			int chuyu =  size / column; 
			int lines =  chuyu;
			if(quyu > 0){
				lines++;
			}
			int count = 1;
			parentView.setWeightSum(column);
			for(int n=0; n<lines; n++){
				if(n > 0){
					ImageView dividingLine = getDividingLineHorizontal(mContext);
					parentView.addView(dividingLine);
				}
				for(int i=0; i<column; i++){
					if(count <= size){
						CheckBox itemView = getTableItem(mContext, listener, (String)names.get(count-1), playMethod, count-1, 1);
						parentView.addView(itemView);
						count++;
					}
				}
			}
			for(int position : selectedItems){
				View view = parentView.getChildAt(position);
				if(view instanceof CheckBox){
					CheckBox cb = (CheckBox)view;
					cb.setChecked(true);
				}
			}
		}
	}
	
	/**绘制竞彩足球混合过关表格 多行
	 * @param mContext
	 * @param parentView
	 * @param listener
	 * @param names
	 * @param column
	 * @return
	 */
	public static void getJZtables(Context mContext,LinearLayout parentView, OnClickListener listener, 
			JSONArray names, String playMethod, int column, ArrayList<Integer> selectedItems) throws Exception{
		if(names != null){
			int size = names.length();
			int quyu = column % size;
			int chuyu =  size / column; 
			int lines =  chuyu;
			int weight = 1;
			if(quyu > 0){
				lines++;
			}
			int count = 1;
			boolean isChecked = false;
			parentView.setWeightSum(column);
			for(int n=0; n<lines; n++){
				LinearLayout lineLayout = getLineLayout(mContext,column);
				for(int i=0; i<column; i++){
					if(count <= size){
						if(selectedItems.contains((count-1))){
							isChecked = true;
						}else{
							isChecked = false;
						}
						if(count == 13){
							CheckBox itemView = getTableItemForDoubleLine(mContext, listener, (String)names.get(count-1), playMethod,
									count-1, 2, isChecked);
							lineLayout.addView(itemView);
							count++;
							break;
						}else if(count == 18){
							CheckBox itemView = getTableItemForDoubleLine(mContext, listener, (String)names.get(count-1), playMethod,
									count-1, 3, isChecked);
							lineLayout.addView(itemView);
							count++;
							break;
						}else if(count == 31){
							CheckBox itemView = getTableItemForDoubleLine(mContext, listener, (String)names.get(count-1), playMethod,
									count-1, 2, isChecked);
							lineLayout.addView(itemView);
							count++;
							break;
						}else{
							CheckBox itemView = getTableItemForDoubleLine(mContext, listener, (String)names.get(count-1), playMethod,
									count-1, 1, isChecked);
							lineLayout.addView(itemView);
							count++;
						}
					}
				}
				parentView.addView(lineLayout);
			}
		}
	}
	
	/**生成一个checkbox
	 * @param mContext
	 * @param listener
	 * @param name
	 * @param playMethod
	 * @param id
	 * @param weight
	 * @return
	 */
	public static CheckBox getTableItem(Context mContext, OnClickListener listener, String name, String playMethod, int id, int weight){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		CheckBox mCheckbox = (CheckBox)inflater.inflate(R.layout.jz_hhgg_item, null);;
		mCheckbox.setTag(id);
		mCheckbox.setChecked(false);
		mCheckbox.setOnClickListener(listener);
		mCheckbox.setText( getPlayMethod(mContext,name,playMethod,id) );
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = weight;
		mCheckbox.setLayoutParams(params);
		return mCheckbox;
	}
	
	/**生成一个checkbox
	 * @param mContext
	 * @param listener
	 * @param name
	 * @param playMethod
	 * @param id
	 * @param weight
	 * @return
	 */
	public static CheckBox getTableItemForDoubleLine(Context mContext, OnClickListener listener, String name, String playMethod,
			int id, int weight, boolean isChecked){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		CheckBox mCheckbox = (CheckBox)inflater.inflate(R.layout.jz_hhgg_item, null);;
		mCheckbox.setTag(id);
		mCheckbox.setChecked(isChecked);
		mCheckbox.setOnClickListener(listener);
		mCheckbox.setText( getPlayMethod(mContext,name,playMethod,id) );
		mCheckbox.setBackgroundResource(R.drawable.cb_left_top_border_white_to_red_select);
		mCheckbox.setPadding(0, ScreenUtil.dip2px(mContext, 4), 0, ScreenUtil.dip2px(mContext, 4));
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = weight;
		mCheckbox.setLayoutParams(params);
		return mCheckbox;
	}
	
	/**根据玩法获取文字显示格式
	 * @param name
	 * @param playMethod
	 * @param id
	 * @return
	 */
	private static CharSequence getPlayMethod(Context mContext, String name, String playMethod, int id){
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		int start = 0;
		int end = 0;
		String normal = "";
		if(playMethod.equals(JZActivity.WF_SPF)){
			normal = JCBFUtils.getJZSPFName(id) + " ";
		}else if(playMethod.equals(JZActivity.WF_RQSPF)){
			normal = JCBFUtils.getJZSPFName(id) + " ";
		}else if(playMethod.equals(JZActivity.WF_QCBF)){
			normal = JCBFUtils.getJZQCBFName(id) + "\n";
		}else if(playMethod.equals(JZActivity.WF_JQS)){
			normal = JCBFUtils.getJZZJQSName(id) + "球" + "\n";
		}else if(playMethod.equals(JZActivity.WF_BQC)){
			normal = JCBFUtils.getJZBQCName(id) + "\n";
		}
		end = start = normal.length();
		end += name.length();
		ssb.append(normal);
		ssb.append(name);
//		ssb.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.drawable.check_color_black_to_white_select)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new AbsoluteSizeSpan((int)mContext.getResources().getDimension(R.dimen.middle_s)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return  ssb;
	}
	
	/**行layout
	 * @param mContext
	 * @param column
	 * @return
	 */
	public static LinearLayout getLineLayout(Context mContext,int column){
		LinearLayout linelayout = new LinearLayout(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		linelayout.setWeightSum(column);
		linelayout.setLayoutParams(params);
		return linelayout;
	}
	
	/**水平方向分割线
	 * @param mContext
	 * @return
	 */
	public static ImageView getDividingLineHorizontal(Context mContext){
		ImageView dividingLine = new ImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, ScreenUtil.dip2px(mContext, 1));
		dividingLine.setLayoutParams(params);
		dividingLine.setBackgroundColor(mContext.getResources().getColor(R.color.line_color));
		return dividingLine;
	}
	
	/**垂直方向分割线
	 * @param mContext
	 * @return
	 */
	public static ImageView getDividingLineVertical(Context mContext){
		ImageView dividingLine = new ImageView(mContext);
		LayoutParams params = new LayoutParams(ScreenUtil.dip2px(mContext, 1), LayoutParams.FILL_PARENT);
		dividingLine.setLayoutParams(params);
		dividingLine.setBackgroundColor(mContext.getResources().getColor(R.color.line_color));
		return dividingLine;
	}
	
	/**主队（让球） vs 客队
	 * @param mContext
	 * @param mainTeam
	 * @param letBallStr
	 * @return
	 */
	public static CharSequence getSpannableString(Context mContext, String mainTeam, String letBallStr){
		int letBall = ConversionUtil.StringToInt(letBallStr);
		if(letBall == 0){
			return mainTeam;
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append(mainTeam);
			sb.append("(");
			sb.append(letBallStr);
			sb.append(")");
			SpannableString msp = new SpannableString(sb.toString());
			//从pos起到end的区域变为红色
			if(letBall < 0)
				msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.green_2ca403)), sb.indexOf("("), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			else
				msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_e54f46)), sb.indexOf("("), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			return msp;
		}
	}
	
	public static void clearSelectedItem(LinearLayout layout){
		int count = layout.getChildCount();
		for(int i=0; i<count; i++){
			View view = layout.getChildAt(i);
			if(view instanceof CheckBox){
				CheckBox cb = (CheckBox)view;
				cb.setChecked(false);
			}else if(view instanceof LinearLayout){
				clearSelectedItem((LinearLayout)view);
			}
		}
	}
}
