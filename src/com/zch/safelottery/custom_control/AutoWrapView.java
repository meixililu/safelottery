package com.zch.safelottery.custom_control;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.zch.safelottery.util.ScreenUtil;

/**自动换行view
 * @author Messi
 *
 */
public class AutoWrapView extends ViewGroup  {
	/** 统一高度 **/
	private int height = 0;
	
	private int gap = 0;
	
	private int parentWidth;
	private int parentHeight;
	
	private boolean mCenter = false;
	
	private ArrayList<Integer> listW = new ArrayList<Integer>();
	
	public AutoWrapView(Context context) {
		super(context);
	}

	public AutoWrapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoWrapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int count = getChildCount();
		int width = 0;
		int widthSum = 0;
		int row = 1;
		parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			width = child.getMeasuredWidth() + gap;
			listW.add(width);
			height = child.getMeasuredHeight() + gap;
			
			widthSum += width;
			if(widthSum > parentWidth){
				widthSum = width;
				row++;
			}
		}
		
		parentHeight = (row == 0)? height: row * height;
		setMeasuredDimension(parentWidth, parentHeight);
		
	}

	@Override
	protected void onLayout(boolean arg0, int left, int top, int right, int bottom) {
		try {
			final int count = getChildCount();
			int width = 0;
			int row = 0;// which row lay you view relative to parent
			int lengthX = 0; // right position of child relative to parent
			int lengthY = 0; // bottom position of child relative to parent
			for (int i = 0; i < count; i++) {

				width = listW.get(i);
				
				lengthX += width;
				lengthY = row * height + height;
				
				if (lengthX > right-left) {
					row++;
					lengthX = width;
					lengthY = row * height + height;
				}
				// if it can't drawing on a same line , skip to next line
				View child = getChildAt(i);
				child.layout(lengthX - width + gap, lengthY - height + gap, lengthX, lengthY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setGap(int gap) {
		this.gap = ScreenUtil.dip2px(getContext(), gap);
	}
	
	/**
	 * 是否居中显示
	 * @param center
	 */
	public void isCenter(boolean center){
		this.mCenter = center;
	}
	
	/**
	 * 取得选中的结果
	 * @return
	 */
	public ArrayList<Integer> getSelect(){
		try {
			result.clear();
			for(int i = 0; i < getChildCount(); i++){
				CheckBox checkBox = (CheckBox) getChildAt(i);
				if(checkBox.isChecked()){
					result.add(i);
				}
			}
		} catch (Exception e) {
			result.clear();
			e.printStackTrace();
		}
		return result;
	}
	
	private ArrayList<Integer> result = new ArrayList<Integer>(); 
}