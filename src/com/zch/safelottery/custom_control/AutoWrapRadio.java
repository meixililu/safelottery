package com.zch.safelottery.custom_control;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.zch.safelottery.util.ScreenUtil;

/**自动换行view
 * @author Messi
 *
 */
public class AutoWrapRadio extends RadioGroup {
	
	private final boolean DEBUG = true;

	private int row = 1; //行
	private int col = 0; //列
	private int ww ;
	private int offset = 0;//偏移
	private int parentWidth;
	private int parentHeight;
	
	private int gap = ScreenUtil.dip2px(getContext(), 3f); //间隙
	
	private LinearLayout.LayoutParams lp;
	
	private ArrayList<Integer> widthTempList = new ArrayList<Integer>();
	private ArrayList<Integer> widthLists = new ArrayList<Integer>();
	
	public AutoWrapRadio(Context context) {
		super(context);
	}

	public AutoWrapRadio(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	private int count;
	private int height = 0;
	private int width = 0;
	
	private int measureTime;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureTime++;
		if(measureTime <= 1){
			if(DEBUG) Log.i("TAG", "onMeasure" + measureTime);
			widthTempList.clear();
			widthLists.clear();
			
			height = 0;
			width = 0;
			col = 0;
			ww = 0;
			row = 1;
			
			count = getChildCount();
			
			parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				width = child.getMeasuredWidth();
				height = child.getMeasuredHeight();
				
				widthTempList.add(width);
				
				ww += width + gap;
				
//			if(DEBUG) Log.i("TAG", i + " width :: " + width + " height :: " + height + " ww :: " + ww + " :: " + col + " -->> " + row + " :::: " + parentWidth);
				
				if (ww > parentWidth) {
					ww -= width + gap; //减回数据
					
					//偏移 量
					offset = (parentWidth - ww) / isZero(col);
					
					widthTempList.remove(widthTempList.size() - 1);
					int size = widthTempList.size();
					
					//当前值加上多出来的值，计算成最后的值放到一个临时的List里
					for(int j = 0; j < size; j++){
						widthLists.add( widthTempList.get(j) + offset);
					}
					widthTempList.clear();
					
					//从这里重新开始
					row++;
					col = 1;
					ww = width + gap;
					widthTempList.add(width);
				} else {
					col++;
				}
				//最后一行数据不够的时候
				if(i >= count-1){
					//偏移 量
					int size = widthTempList.size();
					for(int j = 0; j < size; j++){
						widthLists.add(widthTempList.get(j));
					}
					widthTempList.clear();
				}		
			}
			if(height > 0){
				parentHeight = row * (height+gap);
			}
			for (int i = 0; i < count; i++) {
				
				final View child = this.getChildAt(i);
				
				lp = new LinearLayout.LayoutParams(widthLists.get(i), height);
				lp.gravity = Gravity.CENTER;
	//			((CheckBox)child).setWidth(widthLists.get(i));//LayoutParams(lp);
				
//				if(DEBUG) Log.i("TAG", "child --> " + child.getMeasuredWidth() + " :: " + child.getMeasuredHeight());
				child.setLayoutParams(lp);
			}
		}
//		if(DEBUG) Log.i("TAG", "parentWidth --> " + parentWidth + " : :  parentHeight --> " + parentHeight);
		setMeasuredDimension(parentWidth, parentHeight);
		
	}
	
	private int layoutTime;
	@Override
	protected void onLayout(boolean arg0, int left, int top, int right, int bottom) {
		layoutTime++;
		if(layoutTime <= 1){
			if(DEBUG) Log.i("TAG", "onLayout" + layoutTime);
			final int count = getChildCount();
			int row = 0;// which row lay you view relative to parent
			int lengthX = 0; // right position of child relative to parent
			int lengthY = 0; // bottom position of child relative to parent
			int height = 0;
			int width = 0;
			for (int i = 0; i < count; i++) {
				
				final View child = this.getChildAt(i);
				
				width = widthLists.get(i);
				height = this.height;
				
				lengthX += width + gap;
				
				if (lengthX > right-left) {
					lengthX = width + gap;
					row++;
				}
				
				lengthY = row * (height+gap) + height;
				
				// if it can't drawing on a same line , skip to next line
				child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
				
//				((RadioButton) child).setGravity(Gravity.CENTER);
				
			}
		}
	}

	private int isZero(int number){
		return number > 0 ? number : 1;
	}
	
	public void setGap(int gap) {
		this.gap = ScreenUtil.dip2px(getContext(), gap);
	}

}