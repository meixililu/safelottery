package com.zch.safelottery.result.parser;

import java.util.HashMap;

import android.content.Context;
import android.widget.TextView;

import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.ViewUtils;

public class XyncLotteryParser {
	//114
	//解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context,String lid, String[] results){
		
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		if(results != null){
			for(int i=0; i<results.length; i++){
				TextView btn = ViewUtils.get_Xync_Img(context,(results[i]),1);
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
				for(int i=0; i<t.length; i++){
					TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),2);
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
			
			if(playMethod.equals("01")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						if(results[0].equals(t[i])){
							TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),1);
							linearLayout.addView(btn);
						}else{
							TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),2);
							linearLayout.addView(btn);
						}
					}
				}
				
			}else if(playMethod.equals("02")){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						if(Integer.parseInt(results[0]) > 18){
							TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),1);
							linearLayout.addView(btn);
						}else{
							TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),2);
							linearLayout.addView(btn);
						}
					}
				}
			
			}else if( playMethod.equals("03")||playMethod.equals("04")||
					playMethod.equals("05")||playMethod.equals("06")||
					playMethod.equals("07")||playMethod.equals("08") ){
				
				for(int j=0; j<len; j++){
					String[] t = map.get(j+"");
					for(int i=0; i<t.length; i++){
						int isRight = 0;
						for(int n=0; n<results.length; n++){
							if(results[n].equals(t[i])){
								isRight++;
							}
						}
						if(isRight > 0){
							TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),1);
							linearLayout.addView(btn);
						}else{
							TextView btn = ViewUtils.get_Xync_Img(context,(t[i]),2);
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
				String[] one = temp[i].split(",");
				ball.put(i+"", one);
			}
			return ball;
		}else{
			return null;
		}
	}
	
	
	
	public static int getXyncImg(String temp_num, int color){
//		int temp =  R.drawable.item_1;
//		try{
//			int num = Integer.parseInt(temp_num);
//			switch(num){
//				case 1:
//					if(color == 1){
//						temp = R.drawable.item_1;
//					}else{
//						temp = R.drawable.item_n_1;
//					}
//					break;
//				case 2:
//					if(color == 1){
//						temp = R.drawable.item_2;
//					}else{
//						temp = R.drawable.item_n_2;
//					}
//					break;
//				case 3:
//					if(color == 1){
//						temp = R.drawable.item_3;
//					}else{
//						temp = R.drawable.item_n_3;
//					}
//					break;
//				case 4:
//					if(color == 1){
//						temp = R.drawable.item_4;
//					}else{
//						temp = R.drawable.item_n_4;
//					}
//					break;
//				case 5:
//					if(color == 1){
//						temp = R.drawable.item_5;
//					}else{
//						temp = R.drawable.item_n_5;
//					}
//					break;
//				case 6:
//					if(color == 1){
//						temp = R.drawable.item_6;
//					}else{
//						temp = R.drawable.item_n_6;
//					}
//					break;
//				case 7:
//					if(color == 1){
//						temp = R.drawable.item_7;
//					}else{
//						temp = R.drawable.item_n_7;
//					}
//					break;
//				case 8:
//					if(color == 1){
//						temp = R.drawable.item_8;
//					}else{
//						temp = R.drawable.item_n_8;
//					}
//					break;
//				case 9:
//					if(color == 1){
//						temp = R.drawable.item_9;
//					}else{
//						temp = R.drawable.item_n_9;
//					}
//					break;
//				case 10:
//					if(color == 1){
//						temp = R.drawable.item_10;
//					}else{
//						temp = R.drawable.item_n_10;
//					}
//					break;
//				case 11:
//					if(color == 1){
//						temp = R.drawable.item_11;
//					}else{
//						temp = R.drawable.item_n_11;
//					}
//					break;
//				case 12:
//					if(color == 1){
//						temp = R.drawable.item_12;
//					}else{
//						temp = R.drawable.item_n_12;
//					}
//					break;
//				case 13:
//					if(color == 1){
//						temp = R.drawable.item_13;
//					}else{
//						temp = R.drawable.item_n_13;
//					}
//					break;
//				case 14:
//					if(color == 1){
//						temp = R.drawable.item_14;
//					}else{
//						temp = R.drawable.item_n_14;
//					}
//					break;
//				case 15:
//					if(color == 1){
//						temp = R.drawable.item_15;
//					}else{
//						temp = R.drawable.item_n_15;
//					}
//					break;
//				case 16:
//					if(color == 1){
//						temp = R.drawable.item_16;
//					}else{
//						temp = R.drawable.item_n_16;
//					}
//					break;
//				case 17:
//					if(color == 1){
//						temp = R.drawable.item_17;
//					}else{
//						temp = R.drawable.item_n_17;
//					}
//					break;
//				case 18:
//					if(color == 1){
//						temp = R.drawable.item_18;
//					}else{
//						temp = R.drawable.item_n_18;
//					}
//					break;
//				case 19:
//					if(color == 1){
//						temp = R.drawable.item_19;
//					}else{
//						temp = R.drawable.item_n_19;
//					}
//					break;
//				case 20:
//					if(color == 1){
//						temp = R.drawable.item_20;
//					}else{
//						temp = R.drawable.item_n_20;
//					}
//					break;
//			}
//			
//			
//		}catch(Exception e){
//			
//		}
		return 0;
	}
}
