package com.zch.safelottery.util;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zch.safelottery.R;

public class TableUIUtil {

	/**获取竞足页面
	 * @param mContext
	 * @param parentView
	 * @param listener
	 * @param names
	 * @param icons
	 * @param column
	 * @return
	 */
	public static View getJZtable(Context mContext,LinearLayout parentView, OnClickListener listener, 
			ArrayList<String> names, ArrayList<Integer> icons, ArrayList<Integer> mIds, int column){
		if(names != null && icons != null){
			int size = icons.size();
			int quyu = size % column;
			int chuyu =  size / column; 
			int lines = quyu > 0 ? (chuyu+1) : chuyu;
			int count = 0;
			for(int n=0; n<lines; n++){
				LinearLayout lineLayout = getLineLayout(mContext,column);
				ImageView dividingLine = getDividingLineHorizontal(mContext);
				for(int i=1; i<=column; i++){
					if(count < size){
						FrameLayout itemView = getTableItem(mContext, listener, names.get(count), icons.get(count), mIds.get(count));
						lineLayout.addView(itemView);
					}else{
						FrameLayout itemView = getTableItem(mContext, listener, null, -1, count);
						lineLayout.addView(itemView);
					}
					count++;
					ImageView dividingLineVertical = getDividingLineVertical(mContext);
					if(i != column){
						lineLayout.addView(dividingLineVertical);
					}
				}
				parentView.addView(lineLayout);
				parentView.addView(dividingLine);
			}
		}
		return null;
	}
	
	/**获取竞篮的页面
	 * @param mContext
	 * @param parentView
	 * @param listener
	 * @param names
	 * @param icons
	 * @param column
	 * @return
	 */
	public static View getJZtable(Context mContext,LinearLayout parentView, OnClickListener listener, 
			String[] names, int[] icons, int column){
		if(names != null && icons != null){
			int size = icons.length;
			int quyu = size % column;
			int chuyu =  size / column; 
			int lines = quyu > 0 ? (chuyu+1) : chuyu;
			int count = 0;
			for(int n=0; n<lines; n++){
				LinearLayout lineLayout = getLineLayout(mContext,column);
				ImageView dividingLine = getDividingLineHorizontal(mContext);
				for(int i=1; i<=column; i++){
					if(count < size){
						FrameLayout itemView = getTableItem(mContext, listener, names[count], icons[count], count);
						lineLayout.addView(itemView);
					}else{
						FrameLayout itemView = getTableItem(mContext, listener, null, -1, count);
						lineLayout.addView(itemView);
					}
					count++;
					ImageView dividingLineVertical = getDividingLineVertical(mContext);
					if(i != column){
						lineLayout.addView(dividingLineVertical);
					}
				}
				parentView.addView(lineLayout);
				parentView.addView(dividingLine);
			}
		}
		return null;
	}
	
	/**一个格子的layout
	 * @param mContext
	 * @param listener
	 * @param name
	 * @param icon
	 * @param id
	 * @return
	 */
	public static FrameLayout getTableItem(Context mContext, OnClickListener listener, String name, int icon, int id){
		FrameLayout frameLayout = new FrameLayout(mContext);
		LayoutParams paramsFrameLayout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		paramsFrameLayout.weight = 1;
		paramsFrameLayout.gravity = Gravity.CENTER;
		frameLayout.setLayoutParams(paramsFrameLayout);
		frameLayout.setId(id);
		frameLayout.setOnClickListener(listener);
		frameLayout.setForeground(mContext.getResources().getDrawable(R.drawable.framelayout_foreground_cover_selector));

		LinearLayout linelayout = new LinearLayout(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		linelayout.setOrientation(LinearLayout.VERTICAL);
		if(icon != -1){
			if (!TextUtils.isEmpty(name)) {
				ImageView iconImg = new ImageView(mContext);
				LayoutParams paramsImg = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramsImg.topMargin = ScreenUtil.dip2px(mContext, 3);
				paramsImg.gravity = Gravity.CENTER;
				iconImg.setImageDrawable(mContext.getResources().getDrawable(icon));
				iconImg.setScaleType(ScaleType.CENTER_INSIDE);
				iconImg.setLayoutParams(paramsImg);

				linelayout.addView(iconImg);
				TextView nameTV = new TextView(mContext);
				LayoutParams paramsTV = params;
				paramsTV.bottomMargin = ScreenUtil.dip2px(mContext, 4);
				nameTV.setGravity(Gravity.CENTER);
				nameTV.setTextColor(mContext.getResources().getColor(R.color.text_black));
				nameTV.setTextSize(18);
				if (name.length() > 1) {
					nameTV.setText(name);
				}
				nameTV.setLayoutParams(paramsTV);
				linelayout.addView(nameTV);
				linelayout.setLayoutParams(params);
			}
			
			frameLayout.addView(linelayout);
			
			//如果没有名称并且有图标，说明是新活动,添加标记
			if (name.length() == 1) {
				RelativeLayout rl = new RelativeLayout(mContext);
				LayoutParams llParam = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				rl.setGravity(Gravity.RIGHT|Gravity.TOP);
				rl.setLayoutParams(llParam);
				
				ImageView iconImg = new ImageView(mContext);
				LayoutParams paramsImg = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				if(name.equals("2")){
					iconImg.setImageResource(R.drawable.jz_tag_worldcup);
				}else{
					iconImg.setImageResource(R.drawable.jz_huodong);
				}
				iconImg.setScaleType(ScaleType.MATRIX);
				iconImg.setLayoutParams(paramsImg);
				
				rl.addView(iconImg);
				frameLayout.addView(rl);
			}
		}
		
		return frameLayout;

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
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 1);
		dividingLine.setLayoutParams(params);
		dividingLine.setBackgroundColor(mContext.getResources().getColor(R.color.line_color));
		return dividingLine;
	}
	
	/**水平方向分割线
	 * @param mContext
	 * @return
	 */
	public static ImageView getDividingLineHorizontal(Context mContext,int color){
		ImageView dividingLine = new ImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 1);
		dividingLine.setLayoutParams(params);
		dividingLine.setBackgroundColor(mContext.getResources().getColor(color));
		return dividingLine;
	}
	
	/**垂直方向分割线
	 * @param mContext
	 * @return
	 */
	public static ImageView getDividingLineVertical(Context mContext){
		ImageView dividingLine = new ImageView(mContext);
		LayoutParams params = new LayoutParams(1, LayoutParams.FILL_PARENT);
		dividingLine.setLayoutParams(params);
		dividingLine.setBackgroundColor(mContext.getResources().getColor(R.color.line_color));
		return dividingLine;
	}
	
	/**垂直方向分割线
	 * @param mContext
	 * @return
	 */
	public static ImageView getDividingLineVertical(Context mContext,int color){
		ImageView dividingLine = new ImageView(mContext);
		LayoutParams params = new LayoutParams(1, LayoutParams.FILL_PARENT);
		dividingLine.setLayoutParams(params);
		dividingLine.setBackgroundColor(mContext.getResources().getColor(color));
		return dividingLine;
	}
}
