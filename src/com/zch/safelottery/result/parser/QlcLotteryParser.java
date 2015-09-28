package com.zch.safelottery.result.parser;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.LotteryResultUtils;
import com.zch.safelottery.util.ViewUtils;

public class QlcLotteryParser {

	//解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context,String lid, String[] results){
		
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		if(results != null){
			for(int i=0; i<results.length-1; i++){
				TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(results[i]),"red","");
				linearLayout.addView(btn);
			}
			if(!results[results.length-1].equals("")){
				TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(results[results.length-1]),"blue","");
				linearLayout.addView(btn);
			}
		}
		return linearLayout;
	}
	
	//解析未开奖（一注）
	public static void getUnkonwResultView(Context context,AutoWrapView linearLayout,String result,String playMethod){
		String[] ball = changeStringToSet(result);
		if(ball != null){
			for(int i=0; i<ball.length; i++){
				TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(ball[i]),"","red");
				linearLayout.addView(btn);
			}
		}
	}
	
	//解析已开奖（一注）
	public static void getkonwResultView(Context context,AutoWrapView linearLayout,
			String userData,String[] results, String playMethod){
		String[] ball = changeStringToSet(userData);
		if(ball != null){
			for(int i=0; i<ball.length; i++){
				int isRight = 0;
				for(int n=0; n < results.length-1; n++){
					if(results[n].equals(ball[i])){
						isRight++;
					}
				}
				if(isRight > 0){
					TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(ball[i]),"red","");
					linearLayout.addView(btn);
				}else if(results[results.length-1].equals(ball[i])){
					TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(ball[i]),"blue","");
					linearLayout.addView(btn);
				}else{
					TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(ball[i]),"","red");
					linearLayout.addView(btn);
				}
			}
		}
	}
	
	//将一注彩票的字符串转化为字符数组
	public static String[] changeStringToSet(String result){
		if(!result.contains("-") && !result.equals("")){
			//2014-03-18　陈振国修改　添加了胆拖的处理,七乐彩是不会有双色的，所以不用判断#号　格式：02@06,07,08,09,10,12,13
			String[] resultArray = LotteryResultUtils.changeSymbol(result, ",");
			return resultArray;
		}else{
			return null;
		}
	}

}
