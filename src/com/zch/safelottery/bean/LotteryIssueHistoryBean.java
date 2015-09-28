package com.zch.safelottery.bean;

public class LotteryIssueHistoryBean implements SafelotteryType {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getSimplexTime() {
		return simplexTime;
	}
	public void setSimplexTime(String simplexTime) {
		this.simplexTime = simplexTime;
	}
	public String getDuplexTime() {
		return duplexTime;
	}
	public void setDuplexTime(String duplexTime) {
		this.duplexTime = duplexTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getBonusTime() {
		return bonusTime;
	}
	public void setBonusTime(String bonusTime) {
		this.bonusTime = bonusTime;
	}
	public String getPrizePool() {
		return prizePool;
	}
	public void setPrizePool(String prizePool) {
		this.prizePool = prizePool;
	}
	public String getConvertBonusTime() {
		return convertBonusTime;
	}
	public void setConvertBonusTime(String convertBonusTime) {
		this.convertBonusTime = convertBonusTime;
	}
	public String getBonusNumber() {
		return bonusNumber;
	}
	public void setBonusNumber(String bonusNumber) {
		this.bonusNumber = bonusNumber;
	}
	public String getGlobalSaleTotal() {
		return globalSaleTotal;
	}
	public void setGlobalSaleTotal(String globalSaleTotal) {
		this.globalSaleTotal = globalSaleTotal;
	}
	public String getBackup1() {
		return backup1;
	}
	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}
	public String getBackup2() {
		return backup2;
	}
	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}
	public String getBackup3() {
		return backup3;
	}
	public void setBackup3(String backup3) {
		this.backup3 = backup3;
	}
	
	private String name; //期次
	private String status; //状态
	private String sendStatus; //发票状态
	private String bonusStatus; //返奖状态
	private String startTime; //官方开始时间
	private String simplexTime; //单式截止时间
	private String duplexTime; //复式截止时间
	private String endTime; //官方截止时间
	private String bonusTime; //预计返奖时间
	private String prizePool; //奖池滚存
	private String convertBonusTime; //最晚兑奖时间
	private String bonusNumber; //开奖号码
	private String globalSaleTotal; //全国销量
	private String backup1; //备用字段
	private String backup2; //备用字段  存放大乐透生肖乐的销量
	private String backup3; //备用字段
	
}
