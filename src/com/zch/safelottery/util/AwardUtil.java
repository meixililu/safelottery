package com.zch.safelottery.util;

import java.util.List;

import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;

public class AwardUtil {

	
	/**
	 * @param lid 先按彩种区分大类
	 * @param playMethod
	 * @return
	 */
	public static int award(BetLotteryBean bean){
		
		String lid = bean.getLotteryId();
		String playMethod = bean.getPlayId();
		String pollId = "";
		if(bean.getBuyNumberArray() != null && bean.getBuyNumberArray().size() > 0){
			pollId = bean.getBuyNumberArray().get(0).getPollId();
		}
		if(lid.equals(LotteryId.SYXW) || lid.equals(LotteryId.NSYXW) || lid.equals(LotteryId.GDSYXW)){
			if(playMethod.equals("01")){
				return 13;
			}else if(playMethod.equals("10")){
				return 130;
			}else if(playMethod.equals("12")){
				return 65;
			}else if(playMethod.equals("11")){
				return 1170;
			}else if(playMethod.equals("13")){
				return 195;
			}else if(playMethod.equals("02")){
				return 6;
			}else if(playMethod.equals("03")){
				return 19;
			}else if(playMethod.equals("04")){
				return 78;
			}else if(playMethod.equals("05")){
				return 540;
			}else if(playMethod.equals("06")){
				return 90;
			}else if(playMethod.equals("07")){
				return 26;
			}else if(playMethod.equals("08")){
				return 9;
			}
		}else if(lid.equals(LotteryId.SSCCQ)){
			if(playMethod.equals("30")){
				return 4;
			}else if(playMethod.equals("01")){
				return 10;
			}else if(playMethod.equals("02")){
				return 100;
			}else if(playMethod.equals("04")){
				return 88;
			}else if(playMethod.equals("06")){
				return 50;
			}else if(playMethod.equals("03")){
				return 1000;
			}else if(playMethod.equals("07")){
				return 320;
			}else if(playMethod.equals("08")){
				return 160;
			}else if(playMethod.equals("05") && !pollId.equals("08")){
				return 100000;
			}else if(playMethod.equals("05") && pollId.equals("08")){
				return 20;
			}
		}else if(lid.equals(LotteryId.SSCJX)){
			if(playMethod.equals("30")){
				return 4;
			}else if(playMethod.equals("01")){
				return 11;
			}else if(playMethod.equals("02")){
				return 116;
			}else if(playMethod.equals("04")){
				return 88;
			}else if(playMethod.equals("06")){
				return 58;
			}else if(playMethod.equals("03")){
				return 1160;
			}else if(playMethod.equals("07")){
				return 385;
			}else if(playMethod.equals("08")){
				return 190;
			}else if(playMethod.equals("05") && !pollId.equals("08")){
				return 116000;
			}else if(playMethod.equals("05") && pollId.equals("08")){
				return 30;
			}else if(playMethod.equals("11")){
				return 11;
			}else if(playMethod.equals("12")){
				return 116;
			}
		}else if(lid.equals(LotteryId.K3) ){
			if(playMethod.equals("01")){
				return k3SelectedNumberAward(bean.getBuyNumberArray());
			}else if(playMethod.equals("02") && pollId.equals("01")){
				return 80;
			}else if(playMethod.equals("02") && pollId.equals("07")){
				return 15;
			}else if(playMethod.equals("03") && pollId.equals("01")){
				return 240;
			}else if(playMethod.equals("03") && pollId.equals("08")){
				return 40;
			}else if(playMethod.equals("05") && pollId.equals("01")){
				return 40;
			}else if(playMethod.equals("06") && pollId.equals("08")){
				return 10;
			}else if(playMethod.equals("04") && pollId.equals("01")){
				return 8;
			}
		}else if(lid.equals(LotteryId.K3X)){
			int index = bean.getBuyNumberArray().get(0).getIndex();
			if(index == 0){
				return k3SelectedNumberAward(bean.getBuyNumberArray());
			}else if(index == 2){
				return 80;
			}else if(index == 4){
				return 15;
			}else if(index == 3){
				return 240;
			}else if(index == 5){
				return 40;
			}else if(index == 6){
				return 40;
			}else if(index == 7){
				return 10;
			}else if(index == 8){
				return 8;
			}
		}
		return 0;
	}
	
	public static int k3SelectedNumberAward(List<BetNumberBean> beans){
		int result = 100;
		if(beans != null){
			for(BetNumberBean item : beans){
				String numberStr = item.getBuyNumber();
				int temp = k3HezhiAward(numberStr);
				if(temp < result){
					result = temp;
				}
			}
		}
		return result == 100 ? 0 : result;
	}
	
	public static int k3HezhiAward(String num){
		if(num.equals("4") || num.equals("04") || num.equals("17")){
			return 80;
		}else if(num.equals("5") || num.equals("05") || num.equals("16")){
			return 40;
		}else if(num.equals("6") || num.equals("06") || num.equals("15")){
			return 25;
		}else if(num.equals("7") || num.equals("07") || num.equals("14")){
			return 16;
		}else if(num.equals("8") || num.equals("08") || num.equals("13")){
			return 12;
		}else if(num.equals("9") || num.equals("09") || num.equals("12")){
			return 10;
		}else if(num.equals("10") || num.equals("11")){
			return 9;
		}
		return 0;
	}
}
