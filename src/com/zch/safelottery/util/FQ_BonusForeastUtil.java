package com.zch.safelottery.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FQ_BonusForeastUtil {
	
	
	/**
	 * @param bet 中奖注数
	 * @param bonus 每注奖金
	 * @return 中奖集合最小跟最大值
	 */
	public static double[] countBonus(List<Integer> bets, int[] bonus){
		if(bonus == null || bets == null)
			return null;
		
		LinkedList<Double> list = new LinkedList<Double>();
		for(float temp: bonus){
			for(int bet: bets){
				list.add((double) (bet * temp));
			}
		}
		
		return new double[]{Collections.min(list), Collections.max(list)};
	}
	
	/**
	 * 计算中奖注数
	 * @param lid
	 * @param method
	 * @param mSelectBall
	 * @return
	 */
	public static LinkedList<Integer> countBet(String lid, String method, int mSelectBall){
		LinkedList<Integer> list = new LinkedList<Integer>();
		int bet = 0;
		if(lid.equals(LotteryId.SYXW) || lid.equals(LotteryId.NSYXW) || lid.equals(LotteryId.GDSYXW)){
			if(method.equals("01")){
				list.add(1);
			}else if(method.equals("02")){
				int betFirst = (int)MethodUtils.C_better(mSelectBall - 6, 2);
				bet = (int)MethodUtils.C_better(mSelectBall > 5 ? 5 : mSelectBall, 2);
				list.add(betFirst);
				list.add(bet);
			}else if(method.equals("03")){
				int betFirst = (int)MethodUtils.C_better(mSelectBall - 6, 3);
				bet = (int)MethodUtils.C_better(mSelectBall > 5 ? 5 : mSelectBall, 3);
				list.add(betFirst);
				list.add(bet);
			}else if(method.equals("04")){
				int betFirst = (int)MethodUtils.C_better(mSelectBall - 6, 4);
				bet = (int)MethodUtils.C_better(mSelectBall > 5 ? 5 : mSelectBall, 4);
				list.add(betFirst);
				list.add(bet);
			}else if(method.equals("05")){
				bet = 1;
				list.add(bet);
			}else if(method.equals("06")){
				bet =  (int) MethodUtils.C_better(mSelectBall - 5, 1);
				list.add(bet);
			}else if(method.equals("07")){
				bet =  (int) MethodUtils.C_better(mSelectBall - 5, 2);
				list.add(bet);
			}else if(method.equals("08")){
				bet =  (int) MethodUtils.C_better(mSelectBall - 5, 3);
				list.add(bet);
			}else if(method.equals("10")){
				bet = 1;
				list.add(bet);
			}else if(method.equals("11")){
				bet = 1;
				list.add(bet);
			}else if(method.equals("12")){
				bet = 1;
				list.add(bet);
			}else if(method.equals("13")){
				bet = 1;
				list.add(bet);
			}
		}
		return list;
	}
}
