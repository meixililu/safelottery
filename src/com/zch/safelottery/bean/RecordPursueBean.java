package com.zch.safelottery.bean;

public class RecordPursueBean implements SafelotteryType{
	
	private String autoOrderId;//追号订单号
	private String lotteryId;//彩种编码
	private String playId;//玩法编码
	private String playName;//玩法编码
	private String totalIssue;//追号总期数
	private String successIssue;//成功期数
	private String failureIssue;//失败期数
	private String cancelIssue;//取消期数
	private String completeAmount;//完成金额
	private String orderStatus;//订单状态 0 进行中1 已完成
	private String bonusStatus;//中奖状态
	private String orderAmount;//订单金额
	private String bonusAmount;//税后中奖金额
	private String fixBonusAmount;//税前中奖金额
	private String winStop;//追号设置
	private String winAmount;//停追金额
	private String createTime;//创建时间
	private String acceptTime;//处理时间
	private String multiple;//倍数
	private String item;//注数
	private String numberInfo;//投注号码

	public String getAutoOrderId() {
		return autoOrderId;
	}
	public void setAutoOrderId(String autoOrderId) {
		this.autoOrderId = autoOrderId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	
	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	
	public String getPlayId() {
		return playId;
	}
	public void setPlayId(String playId) {
		this.playId = playId;
	}
	public String getTotalIssue() {
		return totalIssue;
	}
	public void setTotalIssue(String totalIssue) {
		this.totalIssue = totalIssue;
	}
	public String getSuccessIssue() {
		return successIssue;
	}
	public void setSuccessIssue(String successIssue) {
		this.successIssue = successIssue;
	}
	public String getFailureIssue() {
		return failureIssue;
	}
	public void setFailureIssue(String failureIssue) {
		this.failureIssue = failureIssue;
	}
	public String getCancelIssue() {
		return cancelIssue;
	}
	public void setCancelIssue(String cancelIssue) {
		this.cancelIssue = cancelIssue;
	}
	public String getCompleteAmount() {
		return completeAmount;
	}
	public void setCompleteAmount(String completeAmount) {
		this.completeAmount = completeAmount;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getBonusAmount() {
		return bonusAmount;
	}
	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
	}
	public String getFixBonusAmount() {
		return fixBonusAmount;
	}
	public void setFixBonusAmount(String fixBonusAmount) {
		this.fixBonusAmount = fixBonusAmount;
	}
	public String getWinStop() {
		return winStop;
	}
	public void setWinStop(String winStop) {
		this.winStop = winStop;
	}
	public String getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(String winAmount) {
		this.winAmount = winAmount;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getNumberInfo() {
		return numberInfo;
	}

	public void setNumberInfo(String numberInfo) {
		this.numberInfo = numberInfo;
	}
	
	@Override
	public String toString() {
		return "RecordPursueBean [autoOrderId=" + autoOrderId + ", lotteryId="
				+ lotteryId + ", playId=" + playId + ", totalIssue="
				+ totalIssue + ", successIssue=" + successIssue
				+ ", failureIssue=" + failureIssue + ", cancelIssue="
				+ cancelIssue + ", completeAmount=" + completeAmount
				+ ", orderStatus=" + orderStatus + ", bonusStatus="
				+ bonusStatus + ", orderAmount=" + orderAmount
				+ ", bonusAmount=" + bonusAmount + ", fixBonusAmount="
				+ fixBonusAmount + ", winStop=" + winStop + ", winAmount="
				+ winAmount + ", createTime=" + createTime + ", acceptTime="
				+ acceptTime + ", multiple=" + multiple + ", item=" + item
				+ ", numberInfo=" + numberInfo + "]";
	}
	
}
