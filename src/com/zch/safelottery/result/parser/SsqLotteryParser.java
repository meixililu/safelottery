package com.zch.safelottery.result.parser;

import java.util.HashMap;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryResultUtils;
import com.zch.safelottery.util.ViewUtils;

public class SsqLotteryParser {
	//001
	//解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context,String lid, String[] results){
		
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		if(results != null){
			for(int i=0; i<results.length-1; i++){
				TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(results[i]),"red","");
				linearLayout.addView(btn);
			}
			TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(results[results.length-1]),"blue","");
			linearLayout.addView(btn);
		}
		return linearLayout;
	}
	
	//解析未开奖（一注）
	public static void getUnkonwResultView(Context context,AutoWrapView linearLayout,String result,String playMethod){
		LogUtil.DefalutLog("getUnkonwResultView--result:"+result+"-playMethod:"+playMethod);
		HashMap<String,String[]> map = changeStringToSet(result);
		if(map != null){
				
			String[] redball = map.get("red");
			String[] blueball = map.get("blue");
			if(redball != null){
				for(int i=0; i<redball.length; i++){
					TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(redball[i]),"","red");
					linearLayout.addView(btn);
				}
			}
			
			if(blueball != null){
				for(int i=0; i<blueball.length; i++){
					TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(blueball[i]),"","blue");
					linearLayout.addView(btn);
				}
			}
				
		}
	}
	
	//解析已开奖（一注）
	public static void getkonwResultView(Context context,AutoWrapView linearLayout,String userData,String[] results){
		HashMap<String,String[]> map = changeStringToSet(userData);
		if(map != null){
				
			String[] redball = map.get("red");
			String[] blueball = map.get("blue");
			if(redball != null){
				for(int i=0; i<redball.length; i++){
					int isRight = 0;
					for(int n=0; n < results.length-1; n++){
						if(results[n].equals(redball[i])){
							isRight++;
						}
					}
					if(isRight > 0){
						TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(redball[i]),"red","");
						linearLayout.addView(btn);
					}else{
						TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(redball[i]),"","red");
						linearLayout.addView(btn);
					}
				}
				
				if(blueball != null){
					for(int i=0; i<blueball.length; i++){
						if(results[results.length-1].equals(blueball[i])){
							TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(blueball[i]),"blue","");
							linearLayout.addView(btn);
						}else{
							TextView btn = ViewUtils.Bla_scheme_ball(context,LotteryResultUtils.doubleString(blueball[i]),"","blue");
							linearLayout.addView(btn);
						}
					}
				}
			}
				
		}
	}
	
	
	//将一注彩票的字符串转化为字符数组
	public static HashMap<String,String[]> changeStringToSet(String result){
		if(!result.contains("-") && !result.equals("")){
			HashMap<String,String[]> ball = new HashMap<String,String[]>();
				
			String[] red;
			String[] blue;
			String[] temp = result.split("#");
				
			if(temp.length == 2){
				red = LotteryResultUtils.changeSymbol(temp[0].toString(), ",");
				blue = temp[1].toString().split(",");
				ball.put("red", red);
				ball.put("blue", blue);
			}
			return ball;
		}else{
			return null;
		}
	}

}
