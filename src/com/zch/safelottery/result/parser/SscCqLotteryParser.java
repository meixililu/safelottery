package com.zch.safelottery.result.parser;

import java.util.HashMap;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.ViewUtils;

public class SscCqLotteryParser {
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
		HashMap<String,String[]> map = changeStringToSet(result,playMethod);
		if(map != null){
				
			String[] len_temp = map.get("len");
			int len = Integer.parseInt(len_temp[0]);
			
			if(playMethod.equals("30")){//大小单双
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						TextView btn = ViewUtils.Bla_scheme_ball(context,(dxds(t[i])),"","red");
						linearLayout.addView(btn);
					}
				}
			}else{
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
	}
	
	public static String dxds(String text){
		if(text.equals("0")){
			return "小";
		}else if(text.equals("9")){
			return "大";
		}else if(text.equals("1")){
			return "单";
		}else if(text.equals("2")){
			return "双";
		}
		return "";
	}
	
	public static boolean dxdsResult(String text, String t){
		String userSelect = dxds(t);
		StringBuilder sb = new StringBuilder();
		int result = Integer.parseInt(text);
		if(result >= 0 && result <= 4){
			sb.append("小");
		}
		if(result >= 5 && result <= 9){
			sb.append("大");
		}
		if(result == 1 || result == 3 || result == 5 || result == 7 || result == 9){
			sb.append("单");
		}
		if(result == 0 || result == 2 || result == 4 || result == 6 || result == 8){
			sb.append("双");
		}
		String resultStr = sb.toString();
		return resultStr.contains(userSelect);
		
	}
	
	//解析已开奖（一注）
	public static void getkonwResultView(Context context,AutoWrapView linearLayout,
			String userData,String[] results, String playMethod){
		HashMap<String,String[]> map = changeStringToSet(userData,playMethod);
		if(map != null){
				
			String[] len_temp = map.get("len");
			int len = Integer.parseInt(len_temp[0]);
			
			if(playMethod.equals("30")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						
						if(dxdsResult(results[3+i], t[i])){
							TextView btn = ViewUtils.Bla_scheme_ball(context,(dxds(t[i])),"red","");
							linearLayout.addView(btn);
						}else{
							TextView btn = ViewUtils.Bla_scheme_ball(context,(dxds(t[i])),"","red");
							linearLayout.addView(btn);
						}
					}
				}
				
			}else if(playMethod.equals("01") || playMethod.equals("02") || playMethod.equals("03") || playMethod.equals("04") || 
					playMethod.equals("05") || playMethod.equals("09") || playMethod.equals("20") || playMethod.equals("21")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					if(j >= 1){
						linearLayout.addView(ViewUtils.getBallSplit(context));
					}
					for(int i=0; i<t.length; i++){
						if(results[results.length-len+j].equals(t[i])){
							TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"red","");
							linearLayout.addView(btn);
						}else{
							TextView btn = ViewUtils.Bla_scheme_ball(context,(t[i]),"","red");
							linearLayout.addView(btn);
						}
					}
					
				}
			
			}else if( playMethod.equals("06")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						int isRight = 0;
						for(int n=results.length-1; n>results.length-3; n--){
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
				
			}else if( playMethod.equals("07") || playMethod.equals("08")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						int isRight = 0;
						for(int n=results.length-1; n>results.length-4; n--){
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
				
		}
	}
	
	//将一注彩票的字符串转化为字符数组
	public static HashMap<String,String[]> changeStringToSet(String result,String playMethod){
		if(!result.contains("-") && !result.equals("")){
			String[] temp = null;
			HashMap<String,String[]> ball = new HashMap<String,String[]>();
			if(playMethod.equals("06")||playMethod.equals("07")||playMethod.equals("08") || playMethod.equals("30")){
				result = result.replaceAll(",", "");
				temp=result.split(",");
			}else{
			   temp = result.split(",");
			}
			String[] lenAll = new String[1];
			lenAll[0] = temp.length+"";
			ball.put("len", lenAll);
			String[] one = null;
			for(int i=0; i<temp.length; i++){
				int len = temp[i].length();
				if(len>0){
					one=new String[len];
					for(int j=0;j<len;j++){
						String str=temp[i].substring(j,j+1);
						one[j]=str;
					}
				}
				ball.put(i+"", one);
			}
			return ball;
		}else{
			return null;
		}
	}

}
