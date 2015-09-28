package com.zch.safelottery.bean;

public class IssueInfoBean implements SafelotteryType {
	/**彩种编码**/
	private String lotteryId;
	/**期次**/
	private String name;
	/**状态;0预售,1 当前期,3结期**/
	private String status;
	/**发票状态;0等待出票,1出票**/
	private String sendStatus;
	/**返奖状态;0 未返奖,1 已返奖**/
	private String bonusStatus;
	/**官方开始时间**/
	private String startTime;
	/**单式截止时间**/
	private String simplexTime;
	/**复式截止时间**/
	private String duplexTime;
	/**官方截止时间**/
	private String endTime;
	/**预计返奖时间**/
	private String bonusTime;
	/**奖池滚存**/
	private String prizePool;
	/**最晚兑奖时间**/
	private String convertBonusTime;
	/**开奖号码**/
	private String bonusNumber;
	/**全国销量**/
	private String globalSaleTotal;
	/**备用字段**/
	private String backup1;
	/**备用字段  存放大乐透生肖乐的销量**/
	private String backup2;
	/**备用字段**/
	private String backup3;
	/**是否算奖**/
	private String operatorsAward;
	/**奖级**/
	private String bonusClass;
	
	/**系统时间**/
	private String sysTime;
	/**截止投注剩余时间,毫秒**/
	private long leftTime;
	
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
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
	public String getOperatorsAward() {
		return operatorsAward;
	}
	public void setOperatorsAward(String operatorsAward) {
		this.operatorsAward = operatorsAward;
	}
	public String getBonusClass() {
		return bonusClass;
	}
	public void setBonusClass(String bonusClass) {
		this.bonusClass = bonusClass;
	}
	
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}
	public long getLeftTime() {
		return leftTime;
	}
	public void setLeftTime(long leftTime) {
		this.leftTime = leftTime;
	}
}
