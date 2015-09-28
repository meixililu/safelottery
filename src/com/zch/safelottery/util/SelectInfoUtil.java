package com.zch.safelottery.util;

import com.zch.safelottery.bean.SelectInfoBean;

public class SelectInfoUtil {
	
	/**
	 * 得到SelectInfoBean
	 * @param lotteryId 彩种Id
	 * @param playId 玩法
	 * @param pollId 选号类型 除特别类型外都传""
	 * @return
	 */
	public static SelectInfoBean getInfoBean(String lotteryId, String playId, String pollId){
		int sumView = 0; //View的个数
		int initNum = 0;// 初始值
		int item = 1;// 初始值
		int[] sumBall = null;  // 总球数 数组的长度对应View个数
		int[] selectBall = null;// 选择的球数 数组的长度对应View个数
		boolean repeat = false;// 是否可重复
		String[] split = null; //固定两个 从最里层开始
		
		if(lotteryId.equals(LotteryId.SSQ)){
			sumView = 2;
			initNum = 1;
			sumBall = new int[]{33, 16};
			selectBall = new int[]{6, 1};
			split = new String[]{",", "#"};
			repeat = false;
		}else if(lotteryId.equals(LotteryId.DLT)){
			sumView = 2;
			initNum = 1;
			sumBall = new int[]{35, 12};
			selectBall = new int[]{5, 2};
			split = new String[]{",", "#"};
			repeat = false;
		}else if(lotteryId.equals(LotteryId.FC)){
			initNum = 0;
			if(playId.equals("01")){
				sumView = 3;
				sumBall = new int[]{10, 10, 10};
				selectBall = new int[]{1, 1, 1};
				split = new String[]{"", ","};
				repeat = true;
			}else if(playId.equals("02")){
				sumView = 1;
				item = 2;
				sumBall = new int[]{10};
				selectBall = new int[]{2};
				split = new String[]{",", "#"};
				repeat = false;
			}else if(playId.equals("03")){
				sumView = 1;
				sumBall = new int[]{10};
				selectBall = new int[]{3};
				split = new String[]{",", "#"};
				repeat = false;
			}
			
		}else if(lotteryId.equals(LotteryId.PL3)){
			initNum = 0;
			if(playId.equals("01")){
				sumView = 3;
				sumBall = new int[]{10, 10, 10};
				selectBall = new int[]{1, 1, 1};
				split = new String[]{"", ","};
				repeat = true;
			}else if(playId.equals("03")){
				sumView = 1;
				item = 2;
				sumBall = new int[]{10};
				selectBall = new int[]{2};
				split = new String[]{",", "#"};
				repeat = false;
			}else if(playId.equals("04")){
				sumView = 1;
				sumBall = new int[]{10};
				selectBall = new int[]{3};
				split = new String[]{",", "#"};
				repeat = false;
			}
		}else if(lotteryId.equals(LotteryId.PL5)){
			sumView = 5;
			initNum = 0;
			sumBall = new int[]{10, 10, 10, 10, 10};
			selectBall = new int[]{1, 1, 1, 1, 1};
			split = new String[]{"", ","};
			repeat = true;
		}else if(lotteryId.equals(LotteryId.QXC)){
			sumView = 7;
			initNum = 0;
			sumBall = new int[]{10, 10, 10, 10, 10, 10, 10};
			selectBall = new int[]{1, 1, 1, 1, 1, 1, 1};
			split = new String[]{"", ","};
			repeat = true;
		}else if(lotteryId.equals(LotteryId.QLC)){
			sumView = 1;
			initNum = 1;
			sumBall = new int[]{30};
			selectBall = new int[]{7};
			split = new String[]{",", "#"};
			repeat = false;
		}else if(lotteryId.equals(LotteryId.SSCCQ) || lotteryId.equals(LotteryId.SSCJX) ){
			if(playId.equals("01")){
				
			}else if(playId.equals("02")){
			}else if(playId.equals("03")){
			}else if(playId.equals("04")){
			}else if(playId.equals("05")){
			}else if(playId.equals("07")){
			}else if(playId.equals("08")){
			}else if(playId.equals("30")){
				sumView = 2;
				
			}
		}else if(lotteryId.equals(LotteryId.SYXW)  || LotteryId.NSYXW.equals(lotteryId) || lotteryId.equals(LotteryId.GDSYXW)){
			if(playId.equals("01")){
				
			}else if(playId.equals("02")){
			}else if(playId.equals("03")){
			}else if(playId.equals("04")){
			}else if(playId.equals("05")){
			}else if(playId.equals("06")){
			}else if(playId.equals("07")){
			}else if(playId.equals("08")){
			}else if(playId.equals("09")){
			}else if(playId.equals("10")){
			}else if(playId.equals("11")){
			}else if(playId.equals("12")){
			}else if(playId.equals("13")){
				
			}
		}
		
		SelectInfoBean infoBean = new SelectInfoBean();
		infoBean.setSumView(sumView);
		infoBean.setInitNum(initNum);
		infoBean.setItem(item);
		infoBean.setSumBall(sumBall);
		infoBean.setSelectBall(selectBall);
		infoBean.setPollId(pollId);
		infoBean.setSplit(split);
		infoBean.setRepeat(repeat);
		infoBean.setLotteryId(lotteryId);
		infoBean.setPlayId(playId);
		
		return infoBean;
	}
}
