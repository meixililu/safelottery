package com.zch.safelottery.bean;


public class PullRequestBean {
	
	/**机器码**/
	private String machId;
	/**最大的公告消息id**/
	private long maxCommonId;
	/**最大的私密消息id**/
	private long maxPrivacyId;
	/**开奖号码id列表**/
	private String[] lotteryidList;
	/**当日彩种预报**/
	private boolean lotteryForecast;
	/**截止时间提醒**/
	private boolean endTimeRemain;
	/**接受推动消息的时间设置：1为全天，0为从不，时间段用#号连接**/
	private String receiveMsgTime;
	
	public String getMachId() {
		return machId;
	}

	public void setMachId(String machId) {
		this.machId = machId;
	}

	public long getMaxCommonId() {
		return maxCommonId;
	}

	public void setMaxCommonId(long maxCommonId) {
		this.maxCommonId = maxCommonId;
	}

	public long getMaxPrivacyId() {
		return maxPrivacyId;
	}

	public void setMaxPrivacyId(long maxPrivacyId) {
		this.maxPrivacyId = maxPrivacyId;
	}

	public String[] getLotteryidList() {
		return lotteryidList;
	}

	public void setLotteryidList(String[] lotteryidList) {
		this.lotteryidList = lotteryidList;
	}

	public boolean isLotteryForecast() {
		return lotteryForecast;
	}

	public void setLotteryForecast(boolean lotteryForecast) {
		this.lotteryForecast = lotteryForecast;
	}

	public boolean isEndTimeRemain() {
		return endTimeRemain;
	}

	public void setEndTimeRemain(boolean endTimeRemain) {
		this.endTimeRemain = endTimeRemain;
	}

	public String getReceiveMsgTime() {
		return receiveMsgTime;
	}

	public void setReceiveMsgTime(String receiveMsgTime) {
		this.receiveMsgTime = receiveMsgTime;
	}
	
}
