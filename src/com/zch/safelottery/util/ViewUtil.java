package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.jingcai.JLActivity;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.setttings.Settings;

public class ViewUtil {
	/***蓝球***/
	public static final int COLOR_BLUE = 2;
	/***红球***/
	public static final int COLOR_RED = 1;
	/***方球***/
	public static final int COLOR_RED_RECT = 4;
	
	/****竞彩****/
	public final static int STATE_JC = 0XFF001;
	/****农场****/
	public final static int STATE_NC = 0XFF002;
	
	/****起始位置 0****/
	public final static int INIT_NUMBER_0 = 0;
	/****起始位置 1****/
	public final static int INIT_NUMBER_1 = 1;
	
	
	/******
	 * 初始化球 添加 
	 * @param context 当前的Context
	 * @param autoView 当前要进行操作的布局AutoWrapView
	 * @param num      球数
	 * @param color    1.红球 0.蓝球 COLOR_RED红球  COLOR_BLUE蓝球  COLOR_RED_RECT方形
	 * @param initNum   初始号码 INIT_NUMBER_0从0开始，基本用于少到10个球的。INIT_NUMBER_1从1开始，基本用于多于10个球的。
	 * @param onClickListener   事件
	 */
	public static void initBalls(Context context,AutoWrapView autoView,int num,int color,int initNum,OnClickListener onClickListener) {
		try {
			if(autoView != null && onClickListener != null){
				num += initNum;
				//4个球，为时时彩的大小单双。
				if(num == 4){
					if(color == COLOR_RED_RECT){
//						String[] str = {"大","小","单","双"}; 对应9012
						autoView.setGap(ScreenUtil.dip2px(context, 10f));
						
						CheckBox checkBox_2 = XmlUtil.getBall(context, color);
						checkBox_2.setText("大" );
						checkBox_2.setId(9);
						checkBox_2.setOnClickListener(onClickListener);
						autoView.addView(checkBox_2);
						
						CheckBox checkBox_1 = XmlUtil.getBall(context, color);
						checkBox_1.setText("小" );
						checkBox_1.setId(0);
						checkBox_1.setOnClickListener(onClickListener);
						autoView.addView(checkBox_1);
						
						CheckBox checkBox_3 = XmlUtil.getBall(context, color);
						checkBox_3.setText("单" );
						checkBox_3.setId(1);
						checkBox_3.setOnClickListener(onClickListener);
						autoView.addView(checkBox_3);
						
						CheckBox checkBox_4 = XmlUtil.getBall(context, color);
						checkBox_4.setText("双" );
						checkBox_4.setId(2);
						checkBox_4.setOnClickListener(onClickListener);
						autoView.addView(checkBox_4);
					}
				}else{
					autoView.isCenter(true);
					if(num - initNum > 10){
						for (int i = initNum; i < num; i++) {
							CheckBox checkBox = XmlUtil.getBall(context, color);
							checkBox.setText(String.valueOf(i < 10 ? "0" + i : i));
							checkBox.setId(i);
							checkBox.setOnClickListener(onClickListener);
							autoView.addView(checkBox);
						}
					}else{
						for (int i = initNum; i < num; i++) {
							CheckBox checkBox = XmlUtil.getBall(context, color);
							checkBox.setText(String.valueOf(i));
							checkBox.setId(i);
							checkBox.setOnClickListener(onClickListener);
							autoView.addView(checkBox);
						}
					}
				}
			}else{
				isNull();
			}
		} catch (Exception e) {
			if( Settings.DEBUG ) {
				Log.d(Settings.TAG, "Exception:initBalls");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据号码显示View
	 * @param mContext 当前this
	 * @param lotteryId 彩种ID
	 * @param number 号码
	 * @param isSelect 是否选中状态
	 * @return AutoWrapView 用于显示
	 * @return state 用于区分不同的显示方式，比如快3：状态1表示色子图片跟和值，2表示数字跟和值
	 * @throws Exception
	 */
	public static AutoWrapView getAutoView(Context mContext, String lotteryId, String number, boolean isCheck, int state) throws Exception{
		AutoWrapView mWrapView = new AutoWrapView(mContext);
		
		String[] split = LotteryId.getLotterySplit(lotteryId);
		//color 颜色 1.红球 0.蓝球  COLOR_RED红球 COLOR_BLUE蓝球  COLOR_RED_RECT方形
		int color = LotteryId.getLotteryColor(lotteryId);
		String[] numArray = number.split(split[0]);
		
		if(color == COLOR_BLUE){ //有蓝球的情况
			getCheckBox(mContext, mWrapView, numArray[0], split[1], COLOR_RED, isCheck);
			if(numArray.length > 1) getCheckBox(mContext, mWrapView, numArray[1], split[1], COLOR_BLUE, isCheck);
		}else if(color == R.drawable.icon_k3){ //快3
			getK3Result(mContext, mWrapView, number, state);
		}else{
			for(String num: numArray){
				getCheckBox(mContext, mWrapView, num, split[1], color, isCheck);
			}
		}
		
		return mWrapView;
	}
	
	/**
	 * 根据号码显示View
	 * @param mContext 当前this
	 * @param lotteryId 彩种ID
	 * @param number 号码
	 * @param isLine 是否有分离线
	 * @param isSelect 是否选中状态
	 * @return AutoWrapView 用于显示
	 * @throws Exception
	 */
	public static AutoWrapView getAutoView(Context mContext, String lotteryId, String playId, String number, boolean isLine, boolean isCheck) throws Exception{
		AutoWrapView mWrapView = new AutoWrapView(mContext);
		String[] split = LotteryId.getLotterySplit(lotteryId);
		//color 颜色 1.红球 0.蓝球  COLOR_RED红球 COLOR_BLUE蓝球  COLOR_RED_RECT方形
		int color = LotteryId.getLotteryColor(lotteryId);
		
		//时时彩大小单双
		if((lotteryId.equals(LotteryId.SSCCQ) || lotteryId.equals(LotteryId.SSCJX) ) && playId.equals("30")){
			String[] numArray = number.split(",");
			for(String tempNum: numArray){
				if(TextUtils.isEmpty(tempNum)) continue;
				String temp = tempNum; //给初值
				temp = temp.replace("0", "小");
				temp = temp.replace("9", "大");
				temp = temp.replace("1", "单");
				temp = temp.replace("2", "双");
				getCheckBox(mContext, mWrapView, temp, split[1], color, isCheck);
			}
		}else{
			String[] numArray = number.split(split[0]);
			if(color == COLOR_BLUE){ //有蓝球的情况
				getCheckBox(mContext, mWrapView, numArray[0], split[1], COLOR_RED, isCheck);
				//七乐彩只显示一个
				if(numArray.length > 1) getCheckBox(mContext, mWrapView, numArray[1], split[1], COLOR_BLUE, isCheck);
			}else{
				for(String num: numArray){
					if(isLine) mWrapView.addView(ViewUtils.getBallSplit(mContext));
					
					getCheckBox(mContext, mWrapView, num, split[1], color, isCheck);
				}
				if(mWrapView.getChildCount() > 0) if(isLine) mWrapView.removeViewAt(0);//去掉第一个
			}
		}
		
		return mWrapView;
	}
	
	private static void getCheckBox(Context mContext, AutoWrapView mWrapView, String number, String split, int color, boolean isCheck) throws Exception{
		
		String[] temp = number.split(split);
		for(String tempNum: temp){
			if(TextUtils.isEmpty(tempNum)) continue;
			TextView mTv = XmlUtil.getBall(mContext, tempNum, color, isCheck);
			mWrapView.addView(mTv);
		}
	}
	
	/**快3
	 * @param mContext
	 * @param mWrapView
	 * @param number
	 * @param state 状态：1表示色子跟和值，2表示数字跟和值
	 */
	public  static void getK3Result(Context mContext, AutoWrapView mWrapView, String number, int state) throws Exception{
		if(!TextUtils.isEmpty(number)){
			String space = " ";
			String[] temp = number.split(",");
			if(state == 1){
				mWrapView.setGap(3);
				for(String tempNum: temp){
					if(TextUtils.isEmpty(tempNum)) continue;
					TextView mTv = XmlUtil.getBall(mContext, tempNum, true, 0, 0);
					mWrapView.addView(mTv);
				}
			}else if(state == 2){
				mWrapView.setGap(6);
				for(String tempNum: temp){
					if(TextUtils.isEmpty(tempNum)) continue;
					TextView mTv = XmlUtil.getBall(mContext, tempNum, false, R.color.black, R.dimen.middle_m);
					mWrapView.addView(mTv);
				}
				space = "    ";
			}else if(state == 3){
				mWrapView.setGap(3);
				for(String tempNum: temp){
					if(TextUtils.isEmpty(tempNum)) continue;
					TextView mTv = XmlUtil.getBall(mContext, tempNum, true, 0, 0);
					mWrapView.addView(mTv);
				}
				return;
			}
			TextView mTv = XmlUtil.getBall(mContext, space+"和值:"+LotteryId.getK3Sum(number), false, R.color.red, R.dimen.small_l);
			mWrapView.addView(mTv);
		}
	}
	
	/***
	 * 单个AutoWrapView清空 
	 * 当列只能选一个 单选
	 * @param aoutView  当前要进行操作的布局AutoWrapView
	 * @param selectNum  选择的号码
	 * @param number  当前AutoWrapView选择的个数
	 */
	public static int clearOnlyBallCol(AutoWrapView aoutView, int selectNum, int number) {
		if (aoutView != null) {
			CheckBox checkBox = getCheckBox(aoutView.getChildAt(selectNum));
			if(checkBox != null){
				if (checkBox.isChecked()) {
					checkBox.setChecked(false);
					number--;
				}
			}
		} else {
			isNull();
		}
		return number;
	}
	
	/***
	 * 单个AutoWrapView清空 
	 * 同行只能选中一个 单选
	 * @param aoutView  当前要进行操作的布局AutoWrapView
	 * @param selectNum  选择的号码
	 * @param number  当前AutoWrapView选择的个数
	 */
	public static int clearOnlyBallRow(AutoWrapView aoutView, int selectNum, int number) {
		if (aoutView != null) {
			int num = aoutView.getChildCount();
			for (int i = 0; i < num; i++) {
				CheckBox checkBox = getCheckBox(aoutView.getChildAt(i));
				if (checkBox == null)
					return number;
				
				if (checkBox.getId() != selectNum) {
					if (checkBox.isChecked()) {
						checkBox.setChecked(false);
						number--;
					}
				}
			}
		} else {
			isNull();
		}
		return number;
	}
	
	/***
	 * 多个AutoWrapView清空
	 * 
	 * @param aoutView  当前要进行操作的布局AutoWrapView
	 */
	public static void clearBalls(ArrayList<AutoWrapView> autoViews) {
		if (autoViews != null) {
			for (AutoWrapView autoView : autoViews) {
				clearBall(autoView);
			}
		} else {
			isNull();
		}
	}

	/**
	 * 计算选择的彩球
	 * @param aoutView
	 * @return
	 */
	public static int getCount(AutoWrapView aoutView) {
		if (aoutView != null) {
			int num = aoutView.getChildCount();
			int count = 0;
			for (int i = 0; i < num; i++) {
				CheckBox checkBox = getCheckBox(aoutView.getChildAt(i));
				if (checkBox == null) 
					break;
				if (checkBox.isChecked())
					count++;
			}
			return count;
		} else {
			isNull();
		}
		return 0;
	}
	
	/**
	 * 机选 改变AutoWrapView球
	 * @param aoutView 
	 * @param sum 总球数
	 * @param count 机选个数
	 */
	public static void getRandomButton(AutoWrapView aoutView,int sum,int count){
		int[] opt = MethodUtils.getRandomNumber(sum, count, 0, false);
		getRandomBall(aoutView, opt);
	}
	
	/**
	 * 机选 改变AutoWrapView球
	 * @param aoutView 
	 * @param sum 总球数
	 * @param count 机选个数
	 */
	public static void getRandomButton(ArrayList<AutoWrapView> autoViews,int sum,int count, int[] numberCount){
		
		int[] opt = MethodUtils.getRandomNumber(sum, count, 0, false, false);
		getRandomBall(autoViews, opt, numberCount);
	}
	
	public static final String SELECT_SPLIT_NULL = "";
	public static final String SELECT_SPLIT_MIN = ",";
	public static final String SELECT_SPLIT_MAX = "#";
	public static final int SELECT_NUMBER_MIN = 0;
	public static final int SELECT_NUMBER_MAX = 1;
	/**
	 * 计算所有view的球数，返回可投注格式的号码
	 * @param autoViews 多个AutoWrapView 
	 * @param split 组之间的分 "" ","  "#"
	 * @param value 值球是从0开始还是1开始 
	 * @return 
	 */
	public static String getSelectNums(ArrayList<AutoWrapView> autoViews, String split, int value) throws Exception{
		if (autoViews != null) {
			StringBuilder sb = new StringBuilder();
			if(value == SELECT_NUMBER_MIN){ //从0开始
				for (AutoWrapView autview : autoViews) {
					sb.append(getSelectNumMin(autview, "")); //为""拼
					sb.append(split);
				}
			}else if(value == SELECT_NUMBER_MAX){ //从1开始
				for (AutoWrapView autview : autoViews) {
					sb.append(getSelectNumMax(autview)); //为","拼
					sb.append(split);
				}
			}
			if(sb.length() > 0)	return sb.deleteCharAt(sb.lastIndexOf(split)).toString();
		} else {
			isNull();
		}
		return "";
	}

	public static String getSelectNums(AutoWrapView autoView, int value) throws Exception{
		if (autoView != null) {
			StringBuilder sb = new StringBuilder();
			if(value == SELECT_NUMBER_MIN){ //从0开始
				sb.append(getSelectNumMin(autoView, "")); //为""拼
			}else if(value == SELECT_NUMBER_MAX){ //从1开始
				sb.append(getSelectNumMax(autoView)); //为","拼
			}
			if(sb.length() > 0)	return sb.toString();
		} else {
			isNull();
		}
		return "";
	}
	
	/**
	 * 生成双色球大乐透选择的号码，返回可投注格式的号码
	 * @param autoViews 多个AutoWrapView 
	 * @param split 组之间的分 "" ","  "#"
	 * @param value 值球是从0开始还是1开始 
	 * @return 
	 */
	public static String getSelectNumsSSQ_DLT(ArrayList<AutoWrapView> autoViews) throws Exception{
		if (autoViews != null) {
			StringBuilder sb = new StringBuilder();
			for (int i=0,size=autoViews.size(); i<size; i++) {
				AutoWrapView autview = autoViews.get(i);
				String numbers = getSelectNumMax(autview);
				if (!TextUtils.isEmpty(numbers) && !numbers.equals("_")) {
					sb.append(numbers); // 为","拼
					if (i != size - 1) {
						if (i % 2 == 0) {
							sb.append("@");
						} else {
							sb.append("#");
						}
					}
				}
			}
			return sb.toString();
		} else {
			isNull();
		}
		return "";
	}
	
	/**
	 * 最后都切成 这个用
	 * 计算所有view的球数，返回可投注格式的号码
	 * @param autoViews 多个AutoWrapView 
	 * @param split[] 组之间的分 "" ","  "#"
	 * @param value 值球是从0开始还是1开始 
	 * @return 
	 */
	public static String getSelectNums(ArrayList<AutoWrapView> autoViews, String[] split, int value) throws Exception {
		if (autoViews != null) {
			StringBuilder sb = new StringBuilder();
			if(value == SELECT_NUMBER_MIN){ //从0开始
				for (AutoWrapView autview : autoViews) {
					sb.append(getSelectNumMin(autview, split[0])); //为""拼
					sb.append(split[1]);
				}
			}else if(value == SELECT_NUMBER_MAX){ //从1开始
				for (AutoWrapView autview : autoViews) {
					sb.append(getSelectNumMax(autview)); //为","拼
					sb.append(split[1]);
				}
			}
			
			if(sb.length() > 0){
				int length = sb.length() - 1;
				if(sb.charAt(length) == ',' || sb.charAt(length) == '#' ){
					sb.deleteCharAt(length);
				}
			}
			return sb.toString();
		} else {
			isNull();
		}
		return "";
	}
	/**
	 * 计算所有view的球数，返回可投注格式的号码 3D 跟排3用
	 * @param autoViews
	 * @param which 当前要操作的AutoWrapView 组合
	 * @return 
	 */
	public static String getSpecialViewSelectNums( ArrayList<AutoWrapView> autoViews, int[] which) throws Exception{
		if (autoViews != null) {
			StringBuilder sb = new StringBuilder();
			int size = which.length;
			if(size > 1){
				for (int i = 0; i < size; i++) {
					sb.append(getSelectNumMin(autoViews.get(which[i]), ""));
					sb.append(",");
				}
				if(sb.length() > 0)	return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
			}else{
				sb.append(getSelectNumMin(autoViews.get(which[0]), ","));
			}
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 单个view计算选择的彩球 大号加0
	 * @param aoutView
	 * @return
	 */
	private static String getSelectNumMax(AutoWrapView aoutView) throws Exception {
		if(aoutView != null){
			int size = aoutView.getChildCount();
			StringBuilder sb = new StringBuilder(size * 3);
			
			for (int i = 0; i < size; i++) {
				CheckBox checkBox = getCheckBox(aoutView.getChildAt(i));
				if (checkBox == null) 
					break;
				if (checkBox.isChecked()){
					sb.append( checkBox.getId() < 10 ? new StringBuilder("0").append(checkBox.getId()).toString() : checkBox.getId());
					sb.append(',');
				}
			}
			if(sb.length() > 0)
				return sb.deleteCharAt(sb.lastIndexOf(",")).toString(); //返回 删除最后多出来的一位字符的串
			else
				return "_";
		}
		isNull();
		return "";
	}
	
	/**
	 * 拼写快3的投注串
	 * @param spell 拼写的格式
	 * @param number 要拼的组合
	 * @return
	 */
	public static String getK3Number(String spell, String[] number){
		StringBuilder sb = new StringBuilder();
		for(String s: number){
			if(!TextUtils.isEmpty(s)){
				sb.append(s);
				sb.append(spell);
			}
		}
		return (sb.length() > 0)? sb.deleteCharAt(sb.length() - 1).toString(): "";
	}
	
	/**
	 * 还原组合
	 * @param spell
	 * @param number
	 * @return
	 */
	public static String getNumberRestore(String spell, String number){
		return number.replace(spell, "");
	}
	
	/**
	 * 单个view计算选择的彩球 小号不加0
	 * @param aoutView
	 * @param split 切分的符号，如不是"" 接收的结果要删除最后一位
	 * @return 拼好的String 
	 */
	private static String getSelectNumMin(AutoWrapView aoutView, String split) throws Exception{
		if(aoutView != null){
			int size = aoutView.getChildCount();
			int a = split.equals("") ? 1 : 2;
			
			StringBuilder sb = new StringBuilder(size * a);
			
			for (int i = 0; i < size; i++) {
				CheckBox checkBox = getCheckBox(aoutView.getChildAt(i));
				if (checkBox == null) 
					break;
				if (checkBox.isChecked()){
					sb.append( checkBox.getId());
					sb.append( split);
				}
			}
			if(TextUtils.isEmpty(split)) {
				return (sb.length() > 0)? sb.toString(): "_";
			} else {
				//返回 删除最后多出来的一位字符的串
				return sb.deleteCharAt(sb.lastIndexOf(split)).toString();
			}
		}
		return "";
	}
	/***
	 * 多个AutoWrapView清空
	 * 
	 * @param aoutView  当前要进行操作的布局AutoWrapView
	 */
	public static void clearBall(ArrayList<AutoWrapView> autoViews) {
		for(AutoWrapView view : autoViews){
			clearBall(view);
		}
	}
	
	/***
	 * 单个AutoWrapView清空
	 * 
	 * @param aoutView  当前要进行操作的布局AutoWrapView
	 */
	public static void clearBall(AutoWrapView aoutView) {
		if (aoutView != null) {
			int num = aoutView.getChildCount();
			for (int i = 0; i < num; i++) {
				CheckBox checkBox = getCheckBox(aoutView.getChildAt(i));
				if(checkBox == null)
					break ;
				if (checkBox.isChecked())
					checkBox.setChecked(false);
			}
		} else {
			isNull();
		}
	}
	
	/***
	 * 多个机选，每个view一个球
	 * 
	 * @param aoutView 当前要进行操作的布局AutoWrapView
	 * @param opt 机选的数
	 */
	public static void getRandomBall(ArrayList<AutoWrapView> autoViews, int[] opt, int[] numberCount) {
		if (autoViews != null && opt != null) {
			int len = opt.length;
			if(autoViews.size() >= len){
				for(int i = 0; i < len; i++){
					getRandomBall(autoViews.get(i), new int[]{opt[i]});
					numberCount[i] = 1;
				}
			}
		} else {
			isNull();
		}
	}
	
	/***
	 * 单个机选
	 * 
	 * @param aoutView 当前要进行操作的布局AutoWrapView
	 * @param opt 机选的数
	 */
	public static void getRandomBall(AutoWrapView aoutView, int[] opt) {
		if (aoutView != null && opt != null) {
			clearBall(aoutView);
			int len = opt.length;
			for (int i = 0; i < len; i++) {
				CheckBox checkBox = getCheckBox(aoutView.getChildAt(opt[i]));
				if (checkBox == null) 
					break;
				checkBox.setChecked(true);
			}
		} else {
			isNull();
		}
	}
	
	/**
	 * 还原页面
	 * @param autoViews    显示的view
	 * @param balls         当前选择球的数组new String[]{"红球","蓝球"}
	 * @param split         当前组合的分离符 基本为"," "" 这两种
	 * @param symbol   标志 传入格示为 彩种_玩法(SSC_30) 无特别处理可传""
	 */
	public static void restore(ArrayList<AutoWrapView> autoViews, String[] balls, String split, String symbol) throws Exception{
		if (balls == null)
			return;
		int len = balls.length;
		if (symbol.equals("SSC_30")) {
			for (int i = 0; i < len; i++) {
				if (balls[i].equals("9"))
					balls[i] = "0";
				else if (balls[i].equals("0"))
					balls[i] = "1";
				else if (balls[i].equals("1"))
					balls[i] = "2";
				else if (balls[i].equals("2"))
					balls[i] = "3";
			}
		}
		for (int i = 0; i < len; i++) {
			AutoWrapView view = autoViews.get(i);
			restoreAutoView(view, balls[i] != null ? balls[i].split(split): null);
		}
	}
	
	/**
	 * 还原页面指定view页面
	 * @param autoViews    显示的view
	 * @param balls         当前选择球的数组new String[]{"红球","蓝球"}
	 * @param split         当前组合的分离符 基本为"," "" 这两种
	 * @param symbol   标志 传入格示为 彩种_玩法(SSC_30) 无特别处理可传""
	 * @param which   当前要操作的autoViews 的下标
	 */
	public static void restore(ArrayList<AutoWrapView> autoViews, String[] balls, String split, String symbol, int[] which) throws Exception{
		int ball_len = balls.length;
		int view_len = which.length;
		if (ball_len == view_len) {
			for (int i = 0; i < view_len; i++) {
				AutoWrapView view = autoViews.get(which[i]);
				restoreAutoView(view, balls[i] != null ? balls[i].split(split) : null);
			}
		}
	}
	
	/**
	 * @param view 显示的view
	 * @param balls 当前选择球的数组new String[]{"01","02"}
	 */
	public static void restoreAutoView( AutoWrapView view, String[] balls){
			if(balls != null && view != null){
				for(String s: balls)
					if(s.equals("_"))
						return;
				
//				int offset = getOffSet(lid);
				int len = balls.length;
				int childCount = view.getChildCount();
				if(childCount > 10){
					for (int i = 0; i < len; i++) {
						if(TextUtils.isEmpty(balls[i])) continue;
						CheckBox cb = (CheckBox)view.getChildAt( Integer.parseInt(balls[i])-1 );
						cb.setChecked(true);
					}
				}else{
					for (int i = 0; i < len; i++) {
						if(TextUtils.isEmpty(balls[i])) continue;
						CheckBox cb = (CheckBox)view.getChildAt( Integer.parseInt(balls[i]) );
						cb.setChecked(true);
					}
				}
			}
	}
	
	/**
	 * 取得重复的数据
	 * @param frout 前一个数据
	 * @param black 后一个数据
	 * @return
	 */
	public static ArrayList<Integer> getRepeat(ArrayList<Integer> frout, ArrayList<Integer> black){
		ArrayList<Integer> repeat = new ArrayList<Integer>();
		for(int i: black){
			if(frout.contains(i)){
				repeat.add(i);
			}
		}
		return repeat;
	}
	
	public static int isNeedResetCbNumber(ArrayList<JZMatchBean> selectedBeans){
		int maxValue = 0;
		int qcbfSize = 0;
		int bqcSize = 0;
		int jqsSize = 0;
		for(JZMatchBean bean : selectedBeans){
			qcbfSize += bean.qcbfSelectedItem.size();
			bqcSize += bean.bqcSelectedItem.size();
			jqsSize += bean.zjqsSelectedItem.size();
		}
		if(qcbfSize > 0 || bqcSize > 0){
			maxValue = 4;
		}else if(jqsSize > 0){
			maxValue = 6;
		}else {
			maxValue = 8;
		}
		return maxValue;
	}
	
	/**
	 * 初始化串关checkbox
	 * @param context
	 * @param autoView
	 * @param num
	 * @param onClickListener
	 */
	public static String[] initCGCheckBox(int num,String lid,String method,ArrayList<JZMatchBean> selectedBeans){
		if(num == 1)
			return new String[]{"单关"};
		if(lid.equals(LotteryId.JCZQ)){
			if(method.equals(JZActivity.WF_HHGG)){
				int maxValue = isNeedResetCbNumber(selectedBeans);
				if(num > maxValue){
					num = maxValue;
				}
			}else if(method.equals(JZActivity.WF_RQSPF) || method.equals(JZActivity.WF_SPF)){
				if(num > 8){
					num = 8;
				}
			}else if(method.equals(JZActivity.WF_QCBF)|| method.equals(JZActivity.WF_BQC)){
				if(num > 4){
					num = 4;
				}
			}else if(method.equals(JZActivity.WF_JQS)){
				if(num > 6){
					num = 6;
				}
			}else{
				if(num > 8){
					num = 8;
				}
			}
		}else if(lid.equals(LotteryId.JCLQ)){
			if(method.equals(JLActivity.WF_SFC)){
				if(num > 4){
					num = 4;
				}
			}else{
				if(num > 8){
					num = 8;
				}
			}
		}
		String[] s = new String[num - 1];
		for(int i = 2; i <= num; i++){
			s[i-2] = i + "串1";
		}
		return s;
	}

	/**
	 * 把串关的checkbox变成不可点击
	 * @param autoView
	 * @param num
	 */
	public static void disableCheckBox( List<CheckBox> listChecks,int num ){
		try{
			int count = listChecks.size();
			for(int i = 0; i < count; i++){
				CheckBox checkBox = listChecks.get(i);
				checkBox.setEnabled(true);
			}
			for (int i = 2; i <= num; i++) {
				if(num > 1 && count > (i-2)){
					CheckBox checkBox = listChecks.get(i-2);
					checkBox.setChecked(false);
					checkBox.setEnabled(false);
				}
			}
		}catch (Exception e) {
			LogUtil.ExceptionLog("ViewUtil-disableCheckBox");
		}
	}
	
	/**
	 * 获取被选中的checkbox的id，即串关数
	 * @param autoView
	 * @param num
	 */
	public static int[] getCheckBoxId(List<CheckBox> listChecks){
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		int count = listChecks.size();
		for (int i = 0; i < count; i++) {
			CheckBox checkBox = getCheckBox(listChecks.get(i));
			if (checkBox == null) 
				break;
			if(checkBox.isChecked()){
				int c = ConversionUtil.StringToInt(checkBox.getText().toString().replace("串1", ""));
				resultList.add(c);
			}
		}
		int len = resultList.size();
		int[] results = new int[len];
		for (int i=0; i<len; i++) {
			results[i] = resultList.get(i);
		}
		return results;
	}
	
	/**
	 * 获取被选中的checkbox的串关数
	 * @param autoView
	 * @param String 返回串关
	 */
	public static String getCheckBoxStr( List<CheckBox> listChecks){
		if(listChecks != null){
			StringBuilder sb = new StringBuilder();
			int count = listChecks.size();
			for (int i = 0; i < count; i++) {
				CheckBox checkBox = getCheckBox(listChecks.get(i));
				if (checkBox == null) 
					break;
				if(checkBox.isChecked()){
					sb.append(checkBox.getText());
					sb.append(",");
				}
			}
			if(sb.length() > 0)	return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
		}
		return "";
	}
	
	/**
	 * 获取被选中的checkbox的id，即串关数
	 * @param autoView
	 * @param replaceOld 要替换字符
	 * @param replaceBack 替换后字符
	 * @return 返回串关
	 */
	public static String getCheckBoxStr( List<CheckBox> listChecks, char replaceOld, char replaceBack){
		if(listChecks != null){
			StringBuilder sb = new StringBuilder("");
			int count = listChecks.size();
			for (int i = 0; i < count; i++) {
				CheckBox checkBox = getCheckBox(listChecks.get(i));
				if (checkBox == null) 
					break;
				if(checkBox.isChecked()){
					String temp = checkBox.getText().toString();
					temp = temp.replace(replaceOld, replaceBack);
					sb.append(temp);
					sb.append(",");
				}
			}
			if(sb.length() > 0)	return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
		}
		return "";
	}
	
	/**
	 * 判断当前是否为CheckBox
	 * @param obj 传入判断的参数
	 * @return CheckBox
	 */
	private static CheckBox getCheckBox(Object obj){
		if(obj instanceof CheckBox)
			return (CheckBox) obj;
		else
			return null;
	}
	
	/**
	 * 输出
	 */
	private static void isNull(){
		if( Settings.DEBUG ) Log.d("TAG", "AutoWrapView　为空");
	}
	
	
	/**新手引导
	 * @param mContext
	 * @param res
	 * @return
	 */
	public static View getGuideImage(Context mContext,int res){
		ImageView img = new ImageView(mContext);
		ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		img.setLayoutParams(mParams);
		img.setBackgroundResource(res);
		return img;
	}
	
	/**自己画选中的圆点
	 * @param mContext
	 * @return
	 */
	public static ImageView getDot(Context mContext,int index){
		ImageView img = new ImageView(mContext);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(mContext, 7),ScreenUtil.dip2px(mContext, 7));
		mParams.leftMargin = ScreenUtil.dip2px(mContext, 3);
		mParams.rightMargin = ScreenUtil.dip2px(mContext, 3);
		img.setLayoutParams(mParams);
		img.setBackgroundResource(R.drawable.dot_selector);
		if(index == 0){
			img.setEnabled(true);
		}else{
			img.setEnabled(false);
		}
		return img;
	}
	
}
