package com.zch.safelottery.util;

import java.util.ArrayList;

import android.content.Context;

import com.zch.safelottery.R;
import com.zch.safelottery.custom_control.AutoWrapView;

public class NumeralMethodUtil {
	/***
	 * 投注前前判断
	 * @param context
	 * @param redNum
	 */
	public boolean clearBottomJudge(Context context,int[] redNum){
		int size = redNum.length;
		for(int i=0;i<size;i++){
			if(redNum[i] == 0){
				ToastUtil.diaplayMesShort(context, R.string.toast_show_text_0);
				return true;
			}
		}
		return false;
	}
	/***
	 * 清空前判断
	 * @param context
	 * @param redNum
	 */
	public boolean clearBottom(int[] num){
		for(int mNum: num){
			if(mNum > 0){
				return true;
			}
		}
		return false;
	}

//	public boolean clearBottomJudge(Context context, int redNum) {
//		if (redNum == 0) {
//			ToastUtil.diaplayMesShort(context, "您还没选择任何号码");
//			return true;
//		}
//		return false;
//	}
	/***
	 * 清空
	 * @param autoViews 
	 * @param redNum 初始化选中球的个数
	 */
	public void clearBottomNumeral(ArrayList<AutoWrapView> autoViews,int[] redNum){
		int size = autoViews.size();
		for(int i = 0;i<size;i++){
			ViewUtil.clearBall(autoViews.get(i));
			redNum[i] = 0;
		}
	}
	
}
