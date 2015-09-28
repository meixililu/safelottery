package com.zch.safelottery.result.parser;

import java.util.HashMap;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.LotteryResultUtils;
import com.zch.safelottery.util.ViewUtils;

public class SyxwLotteryParser {
	//107
	//解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context,String lid, String[] results){
		
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
			
			if(playMethod.equals("12")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						
						int isRight = 0;
						for(int n=0; n < 2; n++){
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
				
			}else if(playMethod.equals("13")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						
						int isRight = 0;
						for(int n=0; n < 3; n++){
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
			
			}else if( playMethod.equals("02")||playMethod.equals("03")||playMethod.equals("04")||playMethod.equals("05")||
					playMethod.equals("06")||playMethod.equals("07")||playMethod.equals("08") ){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					if(j >= 1){
						linearLayout.addView(ViewUtils.getBallSplit(context));
					}
					for(int i=0; i<t.length; i++){
						int isRight = 0;
						for(int n=0; n<results.length; n++){
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
				
			}else{
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
				String[] one = LotteryResultUtils.changeSymbol(temp[i].toString(), ",");
				ball.put(i+"", one);
			}
			return ball;
		}else{
			return null;
		}
	}

}
