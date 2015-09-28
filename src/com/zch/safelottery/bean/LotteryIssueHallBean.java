package com.zch.safelottery.bean;

public class LotteryIssueHallBean implements SafelotteryType{

	@Override
	public String toString() {
		return "LotteryIssueHallBean [lotteryId=" + lotteryId + ", issue="
				+ issue + ", bonusNumber=" + bonusNumber + ", bonusTime="
				+ bonusTime + "]";
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getBonusNumber() {
		return bonusNumber;
	}
	public void setBonusNumber(String bonusNumber) {
		this.bonusNumber = bonusNumber;
	}
	public String getBonusTime() {
		return bonusTime;
	}
	public void setBonusTime(String bonusTime) {
		this.bonusTime = bonusTime;
	}
	
	private String lotteryId; //彩种编码
	private String issue; //期次
	private String bonusNumber; //开奖号码
	private String bonusTime; //开奖时间 yyyy-MM-dd HH:mm:ss
	
}
