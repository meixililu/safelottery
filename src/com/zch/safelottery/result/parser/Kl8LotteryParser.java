package com.zch.safelottery.result.parser;

import java.util.HashMap;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.ViewUtils;

public class Kl8LotteryParser {
	//016
	//解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context,String lid, String[] results){
		
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		if(results != null){
			for(int i=0; i<results.length; i++){
				
				if(i < results.length-1){
					TextView btn = ViewUtils.Bla_scheme_ball(context,(results[i]),"red","");
					linearLayout.addView(btn);
				}else{
					TextView feipan = ViewUtils.Bla_feipan_ball(context,"X"+results[i]);
					linearLayout.addView(feipan);
				}
			}
		}
		return linearLayout;
	}
	
	//解析未开奖（一注）
	public static void getUnkonwResultView(Context context,AutoWrapView linearLayout,String result,String playMethod){
		HashMap<String,String[]> map = changeStringToSet(result);
		if(map != null){
				
			String[] ball = map.get("ball");
			for(int i=0; i<ball.length; i++){
				TextView btn = ViewUtils.Bla_scheme_ball(context,(ball[i]),"","red");
				linearLayout.addView(btn);
			}
			
		}
	}
	
	//解析已开奖（一注）
	public static void getkonwResultView(Context context,AutoWrapView linearLayout,
			String userData,String[] results, String playMethod){
		HashMap<String,String[]> map = changeStringToSet(userData);
		if(map != null){
				
			String[] t = map.get("ball");
			
			for(int i=0; i<t.length; i++){
				int isRight = 0;
				for(int n=0; n<results.length-1; n++){
					if(results[n].equals(t[i])){
						isRight++;
					}
				}
				if(isRight > 0){
					TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"red","");
					linearLayout.addView(btn);
				}else{
					TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"","red");
					linearLayout.addView(btn);
				}
			}
		}
	}
	
	//将一注彩票的字符串转化为字符数组
	public static HashMap<String,String[]> changeStringToSet(String result){
		if(!result.contains("-") && !result.equals("")){
			HashMap<String,String[]> ball = new HashMap<String,String[]>();
				
			String[] temp = result.split(",");
			ball.put("ball",temp);
			return ball;
		}else{
			return null;
		}
	}
	
}
