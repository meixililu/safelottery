package com.zch.safelottery.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.SelectInfoBean;

public class OrderListPageUtil {

	/**
	 * 显示球的Item
	 * @param mInfoBean 选择信息的mBean
	 * @param numbers 选中的号码
	 * @param markTv 带特殊玩法的显示说明 如：大乐透追加
	 * @return
	 * @throws Exception
	 */
	public static String[] getBalls(BetNumberBean mNumberBean, SelectInfoBean mInfoBean, TextView markTv) throws Exception {
		String playMethod = mNumberBean.getPlayId(); 
		String pollId = mNumberBean.getPollId(); 
		String numbers = mNumberBean.getBuyNumber();
		String lid = mInfoBean.getLotteryId(); 
		String[] ball = new String[2];
		String[] tempStrings;
		if (lid.equals(LotteryId.SSQ)) {
			if(pollId.equals("03")){
				numbers = candleDantuo(numbers);
			}
			tempStrings = numbers.split("#");
			if (tempStrings[0] != null) {
				ball[0] = "红球：" + tempStrings[0].replaceAll(",", " ");
			}
			if (tempStrings[1] != null) {
				ball[1] = "蓝球：" + tempStrings[1].replaceAll(",", " ");
			}
		} else if (lid.equals(LotteryId.DLT)) {
			tempStrings = numbers.split("#");
			if (tempStrings[0] != null) {
				if(pollId.equals("03")){
					tempStrings[0] = candleDantuo(tempStrings[0]);
				}
				ball[0] = "前区：" + tempStrings[0].replaceAll(",", " ");
			}
			if (tempStrings[1] != null) {
				if(pollId.equals("03")){
					tempStrings[1] = candleDantuo(tempStrings[1]);
				}
				ball[1] = "后区：" + tempStrings[1].replaceAll(",", " ");
			}
			
			if(playMethod.equals("02")){ //大乐透 显示追加
				markTv.setText("追加");
				markTv.setVisibility(View.VISIBLE);
			}else{
				markTv.setVisibility(View.GONE);
			}
		} else if (lid.equals(LotteryId.K3) || lid.equals(LotteryId.K3X)) {
			ball[0] = getK3Number(playMethod, pollId, numbers);
			ball[1] = getPlayMethodName(lid, playMethod, pollId);
		} else {
			if(!TextUtils.isEmpty(mInfoBean.getSplit()[0])){
				numbers = numbers.replaceAll(",", " ");
				numbers = numbers.replaceAll("#", " | ");
			}else{
				numbers = numbers.replaceAll("", " ");
				numbers = numbers.replaceAll(",", " | ");
				
				if(numbers.indexOf(" ") == 0){
					StringBuilder sb = new StringBuilder(numbers);
					numbers = sb.deleteCharAt(0).toString();
				}
			}
			ball[0] = candleDantuo(numbers);
			ball[1] = getPlayMethodName(lid, playMethod, pollId);

		}
		return ball;
	}
	
	public static String candleDantuo(String numbers){
		if(numbers.contains("@")){
			numbers = numbers.replace("@", ") ");
			numbers = "(" + numbers;
		}
		return numbers;
	}

	private static String getK3Number(String playMethod, String pollId, String numbers){
		if(playMethod.equals("03")){
			if(pollId.equals("01")){
				return numbers;
			}else if(pollId.equals("08")){
				return "三同号通选";
			}
		}else if(playMethod.equals("06")){
			return "三连号";
		}else{
			return numbers;
		}
		return "暂不支持当前选号方式";
	}
	
	public static String getPlayMethodName(String lid, String playMethod, String pollId) {
		
		if (LotteryId.FC.equals(lid)) {
			if (playMethod.equals("01")) {
				return "直选";
			} else if (playMethod.equals("02")) {
				return "组三";
			} else if (playMethod.equals("03")) {
				return "组六";
			}
		}else if (LotteryId.PL3.equals(lid)) {
				if (playMethod.equals("01")) {
					return "直选";
				} else if (playMethod.equals("03")) {
					return "组三";
				} else if (playMethod.equals("04")) {
					return "组六";
				}
		} else if (LotteryId.SYXW.equals(lid) || LotteryId.NSYXW.equals(lid) || lid.equals(LotteryId.GDSYXW)) {
			if (playMethod.equals("01")) {
				return "前一";
			} else if (playMethod.equals("10")) {
				return "前二直选";
			} else if (playMethod.equals("12")) {
				return "前二组选";
			} else if (playMethod.equals("11")) {
				return "前三直选";
			} else if (playMethod.equals("13")) {
				return "前三组选";
			} else if (playMethod.equals("02")) {
				return "任选二";
			} else if (playMethod.equals("03")) {
				return "任选三";
			} else if (playMethod.equals("04")) {
				return "任选四";
			} else if (playMethod.equals("05")) {
				return "任选五";
			} else if (playMethod.equals("06")) {
				return "任选六";
			} else if (playMethod.equals("07")) {
				return "任选七";
			} else if (playMethod.equals("08")) {
				return "任选八";
			}
		} else if (LotteryId.SSCCQ.equals(lid) || LotteryId.SSCJX.equals(lid)) {
			if (playMethod.equals("30")) {
				return "大小单双";
			} else if (playMethod.equals("01")) {
				return "一星直选";
			} else if (playMethod.equals("02")) {
				return "二星直选";
			} else if (playMethod.equals("06")) {
				return "二星组选";
			} else if (playMethod.equals("03")) {
				return "三星直选";
			} else if (playMethod.equals("04")) {
				return "四星直选";
			} else if (playMethod.equals("07")) {
				return "三星组三";
			} else if (playMethod.equals("08")) {
				return "三星组六";
			} else if (playMethod.equals("05")) {
				if(pollId.equals("08")){
					return "五星通选";
				}else{
					return "五星直选";
				}
			} else if (playMethod.equals("11")) {
				return "任选一";
			} else if (playMethod.equals("12")) {
				return "任选二";
			}
		} else if (LotteryId.K3.equals(lid)) {
			if(playMethod.equals("01")){
				return "和值";
			}else if(playMethod.equals("02")){
				if(pollId.equals("01")){
					return "二同号单选";
				}else if(pollId.equals("07")){
					return "二同号复选";
				}
			}else if(playMethod.equals("03")){
				if(pollId.equals("01")){
					return "三同号单选";
				}else if(pollId.equals("08")){
					return "三同号通选";
				}
			}else if(playMethod.equals("04")){
				return "二不同号";
			}else if(playMethod.equals("05")){
				return "三不同号";
			}else if(playMethod.equals("06")){
				return "三连号";
			}
		} else if (LotteryId.K3X.equals(lid)) {
			if(playMethod.equals("01")){
				if(pollId.equals("01")){
					return "单式";
				}else if(pollId.equals("04")){
					return "和值";
				}
			}else if(playMethod.equals("02")){
				if(pollId.equals("07")){
					return "二同号复选";
				}
			}else if(playMethod.equals("03")){
				if(pollId.equals("08")){
					return "三同号通选";
				}
			}else if(playMethod.equals("04")){
				return "二不同号";
			}else if(playMethod.equals("06")){
				return "三连号";
			}
		}
		return LotteryId.getLotteryName(lid);
	}

}
