package com.zch.safelottery.setttings;


import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.AccountChargeActivity;
import com.zch.safelottery.activity.BuyLotteryActivity;
import com.zch.safelottery.activity.MotuActivity;
import com.zch.safelottery.activity.RecordBetActivity;
import com.zch.safelottery.activity.RecordPursueActivity;
import com.zch.safelottery.activity.SendLotteryActivity;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TableUIUtil;

public class BuyLotteryPageHelper {
	
	
	/**根据星期几返回对应的星期数组
	 * @param week
	 * @return
	 */
	public static String getTodayLottryIdArray(String week){
		if(week.equals("星期一")){
			return "002,113,108,109,004";
		}else if(week.equals("星期二")){
			return"002,001,108,109,110";
		}else if(week.equals("星期三")){
			return"002,113,108,109,004";
		}else if(week.equals("星期四")){
			return"002,001,108,109";
		}else if(week.equals("星期五")){
			return"002,108,109,110,004";
		}else if(week.equals("星期六")){
			return"002,113,108,109";
		}else if(week.equals("星期日")){
			return"002,001,108,109,110";
		}else if(week.equals("Monday")){
			return"002,113,108,109,004";
		}else if(week.equals("Tuesday")){
			return"002,001,108,109,110";
		}else if(week.equals("Wednesday")){
			return"002,113,108,109,004";
		}else if(week.equals("Thursday")){
			return"002,001,108,109";
		}else if(week.equals("Friday")){
			return"002,108,109,110,004";
		}else if(week.equals("Saturday")){
			return"002,113,108,109";
		}else if(week.equals("Sunday")){
			return"002,001,108,109,110";
		}
		return"002,113,108,109,004";
	}
	
	public static void showTableView(Context mContext, ArrayList<View> tableViews, LinearLayout tableLv){
		int len = tableViews.size();
		int row = len/3;
		if(len % 3 > 0){
			row += 1;
		}
		for(int n=0; n<row; n++){
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			View mView = getLotteryTableLine(mContext,tableViews,n,len);
			mView.setLayoutParams(mParams);
			tableLv.addView( mView );
			tableLv.addView(TableUIUtil.getDividingLineHorizontal(mContext,R.color.line_color_index));
			tableLv.addView(TableUIUtil.getDividingLineHorizontal(mContext,R.color.white));
		}
		
		
	}
	
	public static View getLotteryTableLine(Context mContext, ArrayList<View> tableViews,int row,int size){
		LinearLayout mLayout = new LinearLayout(mContext);
		mLayout.setWeightSum(3f);
		for (int i = 0; i < 3; i++) {
			int index = row * 3 + i;
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(0, FILL_PARENT, 1);
			View itemView = null;
			if(index < size){
				itemView = tableViews.get(index);
			}else{
				itemView = new LinearLayout(mContext);
			}
			mLayout.addView(itemView, mParams);
			if(i != 2){
				mLayout.addView(TableUIUtil.getDividingLineVertical(mContext,R.color.line_color_index));
				mLayout.addView(TableUIUtil.getDividingLineVertical(mContext,R.color.white));
			}
		}
		return mLayout;
	}
	
	/**返回今日开奖文字
	 * @param lid
	 * @return
	 */
	public static String getTodayLottryIssue(String lid){
		return "第" + LotteryId.getIssue(lid) + "期";
	}
	
	/**返回今日开奖剩余时间
	 * @param lid
	 * @return
	 */
	public static String getTodayLottryRemainTime(String lid){
		return "结束还剩 "+ BuyLotteryActivity.changeDataFormat( LotteryId.getRemainTime(lid));
	}
	
	/**功能模块是否需要登录
	 * @param className
	 * @return
	 */
	public static boolean isNeedLogin(String className){
		if(className.equals(RecordBetActivity.class.getName())){
			return true;
		}else if(className.equals(RecordPursueActivity.class.getName())){
			return true;
		}else if(className.equals(AccountChargeActivity.class.getName())){
			return true;
		}else if(className.equals(SendLotteryActivity.class.getName())){
			return true;
		}
		else{
			return false;
		}
	}
	
}
