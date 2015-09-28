package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.SelectInfoBean;
import com.zch.safelottery.ctshuzicai.K3Activity;
import com.zch.safelottery.ctshuzicai.K3XActivity;

public class RandomSelectUtil {

	private static String pollId ;
	
	/**********
	 * 随机产生投注号码 只生成1注
	 * @param sum 机选次数
	 * @param SelectInfoBean 选择的基本信息
	 * @return 用户投注的Bean List<BetNumberBean> 
	 */
	public static List<BetNumberBean> getRandomNumber(int sum, SelectInfoBean infoBean) {
		List<BetNumberBean> list = new ArrayList<BetNumberBean>();

		try {
			int sumView = infoBean.getSumView();
			int initNum = infoBean.getInitNum();
			int item = infoBean.getItem();
			int[] sumBall = infoBean.getSumBall();
			int[] selectBall = infoBean.getSelectBall();
			boolean reqeat =  infoBean.isRepeat();
			String playId = infoBean.getPlayId();
			String[] split = infoBean.getSplit();
			String lotteryId = infoBean.getLotteryId();
			pollId = infoBean.getPollId();
			
			BetNumberBean mBean;
			String[] temp;
			if((lotteryId.equals(LotteryId.SSCCQ) || lotteryId.equals(LotteryId.SSCJX)) && (playId.equals("30") || playId.equals("11") || playId.equals("12")) ){ //时时彩大小单双
				if(playId.equals("30")){
					for(int i = 0; i < sum; i++){
						mBean = new BetNumberBean();
						temp = new String[sumView];
						for(int j = 0; j < sumView; j++){
							temp[j] = MethodUtils.getSpellBetNumber(MethodUtils.getRandomNumber(sumBall[j], selectBall[j], initNum, reqeat), split[0], initNum);
							temp[j] = temp[j].replace("0", "9");
							temp[j] = temp[j].replace("1", "0");
							temp[j] = temp[j].replace("2", "1");
							temp[j] = temp[j].replace("3", "2");
						}
						mBean.setBuyNumber(MethodUtils.getSpellBetNumber(temp, split[1]));
						mBean.setItem(item);
						mBean.setPlayId(playId);
						mBean.setPollId(pollId);
						mBean.setAmount(item << 1);
						
						LogUtil.DefalutLog(mBean);
						list.add(mBean);
					}
				}else if(playId.equals("11")){
					for(int i = 0; i < sum; i++){
						mBean = new BetNumberBean();
						temp = new String[sumView];
						int[] index = MethodUtils.getRandomNumber(sumView, 1, initNum, false);
						int[] select = MethodUtils.getRandomNumber(10, 1, initNum, reqeat);
						for(int j = 0; j < sumView; j++){
							for(int z = 0; z < index.length; z++){
								temp[j] = "_";
								if(index[z] == j){
									temp[j] = String.valueOf(select[z]);
									break;
								}
							}
						}
						mBean.setBuyNumber(MethodUtils.getSpellBetNumber(temp, split[1]));
						mBean.setItem(item);
						mBean.setPlayId(playId);
						mBean.setPollId(pollId);
						mBean.setAmount(item << 1);
						
						LogUtil.DefalutLog(mBean);
						list.add(mBean);
					}
				}else if(playId.equals("12")){
					for(int i = 0; i < sum; i++){
						mBean = new BetNumberBean();
						temp = new String[sumView];
						int[] index = MethodUtils.getRandomNumber(sumView, 2, initNum, false);
						int[] select = MethodUtils.getRandomNumber(10, 2, initNum, reqeat);
						for(int j = 0; j < sumView; j++){
							for(int z = 0; z < index.length; z++){
								temp[j] = "_";
								if(index[z] == j){
									temp[j] = String.valueOf(select[z]);
									break;
								}
							}
						}
						mBean.setBuyNumber(MethodUtils.getSpellBetNumber(temp, split[1]));
						mBean.setItem(item);
						mBean.setPlayId(playId);
						mBean.setPollId(pollId);
						mBean.setAmount(item << 1);
						
						LogUtil.DefalutLog(mBean);
						list.add(mBean);
					}
				}
			}else if( lotteryId.equals(LotteryId.K3)){
				mBean = new BetNumberBean();
				String number = null;
				if(playId.equals("01")){
					number = getK3RandomNumber( new int[]{0});
				}else if(playId.equals("02")){
					number = getK3RandomNumber( new int[]{2, 4});
				}else if(playId.equals("03")){
					number = getK3RandomNumber( new int[]{3, 5});
				}else if(playId.equals("04")){
					number = getK3RandomNumber( new int[]{8});
				}else if(playId.equals("05")){
					number = getK3RandomNumber( new int[]{6});
				}else if(playId.equals("06")){
					number = getK3RandomNumber( new int[]{7});
				}
				if(number != null){
					mBean.setPlayId(playId);
					mBean.setPollId(pollId);
					mBean.setItem(1);
					mBean.setAmount(2);
					mBean.setBuyNumber(getK3Number(playId, number));
					list.add(mBean);
				}
			}else if( lotteryId.equals(LotteryId.K3X)){
				mBean = new BetNumberBean();
				String number = null;
				if(playId.equals("01")){
					number = getK3XRandomNumber(mBean, new int[]{0, 2, 3, 6});
				}else if(playId.equals("02")){
					number = getK3XRandomNumber(mBean, new int[]{4});
				}else if(playId.equals("03")){
					number = getK3XRandomNumber(mBean, new int[]{5});
				}else if(playId.equals("04")){
					number = getK3XRandomNumber(mBean, new int[]{8});
				}else if(playId.equals("06")){
					number = getK3XRandomNumber(mBean, new int[]{7});
				}
				if(number != null){
					mBean.setPlayId(playId);
					mBean.setPollId(pollId);
					mBean.setItem(1);
					mBean.setAmount(2);
					mBean.setBuyNumber(getK3XNumber(playId, pollId, number));
					list.add(mBean);
				}
			}else{
				for(int i = 0; i < sum; i++){
					mBean = new BetNumberBean();
					temp = new String[sumView];
					
					if(lotteryId.equals(LotteryId.SYXW) || LotteryId.NSYXW.equals(lotteryId) || lotteryId.equals(LotteryId.GDSYXW)){//11选5的处理 多个View不重复
						ArrayList<int[]> list2 = new ArrayList<int[]>();
						for(int j = 0; j < sumView; j++){
							boolean b = false;
							int[] m = MethodUtils.getRandomNumber(sumBall[j], selectBall[j], initNum, reqeat);
							if(j > 0){
								//判断当前随机出来的数是否已存在
								for(int n = 0; n < list2.size(); n++){
									if(list2.get(n)[0] == m[0]){
										j--;
										b = true;
										break;
									}
								}
								if(!b){
									list2.add(m);
									temp[j] = MethodUtils.getSpellBetNumber(list2.get(j), split[0], initNum);
								}
							}else{
								list2.add(m);
								temp[j] = MethodUtils.getSpellBetNumber(list2.get(j), split[0], initNum);
							}
						}
					}else{
						for(int j = 0; j < sumView; j++){
							temp[j] = MethodUtils.getSpellBetNumber(MethodUtils.getRandomNumber(sumBall[j], selectBall[j], initNum, reqeat), split[0], initNum);
						}
					}
					
					mBean.setBuyNumber(MethodUtils.getSpellBetNumber(temp, split[1]));
					mBean.setItem(item);
					mBean.setPlayId(playId);
					mBean.setPollId(pollId);
					if(lotteryId.equals(LotteryId.DLT) && playId.equals("02")){ //大乐透追加玩法 一注3元
						mBean.setAmount(item * 3);
					}else{
						mBean.setAmount(item << 1);
					}
					
					LogUtil.DefalutLog(mBean);
					list.add(mBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static String getK3Number(String playId, String number){
		if(number.contains("\n")){
			number = number.replace("\n", "、");
		}
		if(number.contains("、")){
			return ViewUtil.getK3Number(",", number.split("、"));
		}else {
			if(playId.equals("01"))
				return number;
			else
				return ViewUtil.getK3Number(",", number.split(""));
		}
	}

	private static String getK3XNumber(String playId, String pollId, String number){
		if(number.contains("\n")){
			number = number.replace("\n", "、");
		}
		if(number.contains("、")){
			return ViewUtil.getK3Number(",", number.split("、"));
		}else {
			if(playId.equals("01") && pollId.equals("04"))
				return number;
			else
				return ViewUtil.getK3Number(",", number.split(""));
		}
	}
	
	public static String getK3RandomNumber(int[] groupId){
		Random r = new Random();
		String[][][][] mDataSum = K3Activity.mDataSum;
		int mGroupId = groupId[r.nextInt(groupId.length)];
		int row = r.nextInt(mDataSum[mGroupId].length);
		int col = r.nextInt(mDataSum[mGroupId][row].length);
		
		if(mGroupId == 2){
			pollId = "01";
		}else if(mGroupId == 3){
			pollId = "01";
		}else if(mGroupId == 4){
			pollId = "07";
		}else if(mGroupId == 5){
			pollId = "08";
		}else if(mGroupId == 6){
			pollId = "01";
		}else if(mGroupId == 7){
			pollId = "08";
		}
		return mDataSum[mGroupId][row][col][0];
	}
	
	public static String getK3XRandomNumber(BetNumberBean bean, int[] groupId){
		Random r = new Random();
		String[][][][] mDataSum = K3XActivity.mDataSum;
		int mGroupId = groupId[r.nextInt(groupId.length)];
		int row = r.nextInt(mDataSum[mGroupId].length);
		int col = r.nextInt(mDataSum[mGroupId][row].length);
		bean.setIndex(mGroupId);
		
		if(mGroupId == 0){
			pollId = "04";
		}else if(mGroupId == 2){
			pollId = "01";
		}else if(mGroupId == 3){
			pollId = "01";
		}else if(mGroupId == 4){
			pollId = "07";
		}else if(mGroupId == 5){
			pollId = "08";
		}else if(mGroupId == 6){
			pollId = "01";
		}else if(mGroupId == 7){
			pollId = "08";
		}else if(mGroupId == 8){
			pollId = "01";
		}
		return mDataSum[mGroupId][row][col][0];
	}
}
