package com.zch.safelottery.util;

public class StatusUtil {

	/**
	 * 购买类型
	 * @param buyType 类型值
	 * @return 
	 */
	public static String getBuyType(String buyType) {
		int num = ConversionUtil.StringToInt(buyType);
		switch (num) {
		case 1:
			return "代购";
		case 2:
			return "合买";
		case 4:
			return "追号";
		case 7:
			return "赠送";
		}
		return buyType;
	}

//	public static String getBuyMethod(String buyMethod){
//		if(buyMethod.equals("1")){
//			return "代购";
//		}else if(buyMethod.equals("2")){
//			return "合买";
//		}else if(buyMethod.equals("3")){
//			return "套餐";
//		}else if(buyMethod.equals("4")){
//			return "追号";
//		}else if(buyMethod.equals("5")){
//			return "合买认购";
//		}else if(buyMethod.equals("6")){
//			return "赠送";
//		}else{
//			return "";
//		}
//	}
	
	/**
	 * 方案状态
	 * @param OrderStatus 状态值
	 * @return
	 */
	public static String getOrderStatus(String OrderStatus) {
		if (OrderStatus.equals("-1")) {
			return "等待出票";
		} else if (OrderStatus.equals("0")) {
			return "订单处理中";
		} else if (OrderStatus.equals("1")) {
			return "投注成功";
		} else if (OrderStatus.equals("2")) {
			return "部分成交";
		} else if (OrderStatus.equals("3")) {
			return "投注失败";
		} else if (OrderStatus.equals("4")) {
			return "系统取消";
		} else if (OrderStatus.equals("5")) {
			return "流单";
		} else if (OrderStatus.equals("6")) {
			return "人工取消";
		} else {
			return "未知";
		}
	}

	/**
	 * 中奖状态
	 * @param bonusStatus 状态值
	 * @return
	 */
	public static String getBonusStatus(String bonusStatus) {
		return getBonusStatus(ConversionUtil.StringToInt(bonusStatus));
	}
	/**
	 * 中奖状态
	 * @param bonusStatus 状态值
	 * @return
	 */
	public static String getBonusStatus(int bonusStatus) {
		switch (bonusStatus) {
		case 0:
			return "未开奖";
		case 1:
			return "已中奖";
		case 2:
			return "未中奖";
		}
		return "";
	}

	/**
	 * 期次状态
	 * @param issueState 状态值
	 * @return "未开售" "在售" "暂停" "结期"
	 */
	public static String getIssueState(int issueState) {
		switch(issueState){
		case 0:
			return "未开售";
		case 1:
			return "在售";
		case 2:
			return "暂停";
		default:
			return "结期";
		}
	}
	
	/**
	 * 是否停追属性
	 * @param winStop 0：否 -- 1：是 -- 3：中多少钱停
	 * @param winAmount 停的钱
	 * @return
	 */
	public static String getWin(String winStop, String winAmount){
		if(winStop.equals("0")) return "否";
		if(winStop.equals("1")) return "是";
		if(winStop.equals("3")) return "中奖" + winAmount + "停追";
		return "是";
	}
	
	public static String getPrivacy(String privacy){
		int p = ConversionUtil.StringToInt(privacy);
		if(p == 0) return "完全公开";
		if(p == 1) return "完全保密";
		if(p == 2) return "截止后公开";
		if(p == 3) return "参与者公开";
		return "";
	}
	/**
	 * 隐藏中间部分 应观点要求编号无须隐藏
	 * @param schemeId 要进行操作的字符串
	 * @return 隐藏后的String
	 */
	public static String getSchemeShortId(String schemeId) {
//		return getSchemeShortId(schemeId, "***", 5);
		return schemeId;
	}
	
	/**
	 * 隐藏中间部分
	 * @param schemeId 要进行操作的字符串
	 * @param middle 隐藏部分用middle表示
	 * @param length 头及尾显示的长度
	 * @return 隐藏后的String
	 */
	public static String getSchemeShortId(String schemeId, String middle, int length) {
		int len = schemeId.length();
		String result = "";
		try {
			String front = schemeId.substring(0, length);
			String end = schemeId.substring(len - length, len);
			result = front + middle + end;
		} catch (Exception e) {
			return schemeId;
		}
		return result;
	}
	
	/**
	 * 隐藏末尾部分
	 */
	public static String getShortStr(String oldstr,int length,String newstr) {
		int len = oldstr.length();
		String result = "";
		try {
			String front = oldstr.substring(0, len-length);
			result = front +newstr;
		} catch (Exception e) {
			return oldstr;
		}
		return result;
	}
}
