package com.zch.safelottery.result.parser;

import java.util.HashMap;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.ViewUtils;

public class PksLotteryParser {
	//017
	//解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context,String lid, String[] results){
		//自动换行
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		if(results != null){
			for(int i=0; i<results.length; i++){
				TextView btn = ViewUtils.Bla_scheme_ball(context,(results[i]),"red","");
				linearLayout.addView(btn);
			}
		}
		return linearLayout;
	}
	
	//解析未开奖（一注）
	public static void getUnkonwResultView(Context context,AutoWrapView linearLayout,String result,String playMethod){
		HashMap<String,String[]> map = changeStringToSet(result);
		if(map != null){
				
			String[] len_temp = map.get("len");
			int len = Integer.parseInt(len_temp[0]);
			
			for(int j=0; j<len; j++){
				String[] t = map.get(j+"");
				if(j >= 1){
					linearLayout.addView(ViewUtils.getBallSplit(context));
				}
				for(int i=0; i<t.length; i++){
					TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"","red");
					linearLayout.addView(btn);
				}
			}
			
		}
	}
	
	//解析已开奖（一注）
	public static void getkonwResultView(Context context,AutoWrapView linearLayout,
			String userData,String[] results, String playMethod){
		HashMap<String,String[]> map = changeStringToSet(userData);
		if(map != null){
				
			String[] len_temp = map.get("len");
			int len = Integer.parseInt(len_temp[0]);
				
			for(int j=0; j<len; j++){
				String[] t = map.get(j+"");
				if(j >= 1){
					linearLayout.addView(ViewUtils.getBallSplit(context));
				}
				for(int i=0; i<t.length; i++){
					if(results[j].equals(t[i])){
						TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"red","");
						linearLayout.addView(btn);
					}else{
						TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"","red");
						linearLayout.addView(btn);
					}
				}
			}
				
		}
	}
	
	//将一注彩票的字符串转化为字符数组
	public static HashMap<String,String[]> changeStringToSet(String result){
		if(!result.contains("-") && !result.equals("")){
			HashMap<String,String[]> ball = new HashMap<String,String[]>();
				
			String[] temp = result.split("#");
			String[] len = new String[1];
			len[0] = temp.length+"";
			ball.put("len", len);
			
			for(int i=0; i<temp.length; i++){
				String[] one = temp[i].split(",");
				ball.put(i+"", one);
			}
			return ball;
		}else{
			return null;
		}
	}

}
