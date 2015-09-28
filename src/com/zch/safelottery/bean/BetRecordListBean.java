package com.zch.safelottery.bean;

public class BetRecordListBean implements SafelotteryType {
	//orderList节点
	private String userCode;// 用户编号
	private String orderId;// 订单号
	private String programsOrderId;// 方案号
	private String autoOrderId;// 追号订单号
	private String orderStatus;// 订单状态
	private String bonusStatus;// 中奖状态
	private String orderType;// 订单类型
	private String orderAmount;// 订单金额
	private String bonusAmount;// 税后中奖金额
	private String fixBonusAmount;// 税前中奖金额
	private String programsAmount;// 方案金额
	private String lotteryId;// 彩种编码
	private String playId;// 玩法编码
	private String pollId;// 选号方式
	private String issue;// 期次
	private String sendStatus;// 发单状态
	private String programsStatus;// 方案状态
	private String buyType;// 投注方式
	private String createTime;// 创建时间
	private String bonusToAccount;//是否派奖
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProgramsOrderId() {
		return programsOrderId;
	}
	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}
	public String getAutoOrderId() {
		return autoOrderId;
	}
	public void setAutoOrderId(String autoOrderId) {
		this.autoOrderId = autoOrderId;
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
	public String getProgramsAmount() {
		return programsAmount;
	}
	public void setProgramsAmount(String programsAmount) {
		this.programsAmount = programsAmount;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getPlayId() {
		return playId;
	}
	public void setPlayId(String playId) {
		this.playId = playId;
	}
	public String getPollId() {
		return pollId;
	}
	public void setPollId(String pollId) {
		this.pollId = pollId;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getProgramsStatus() {
		return programsStatus;
	}
	public void setProgramsStatus(String programsStatus) {
		this.programsStatus = programsStatus;
	}
	public String getBuyType() {
		return buyType;
	}
	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getBonusToAccount() {
		return bonusToAccount;
	}
	public void setBonusToAccount(String bonusToAccount) {
		this.bonusToAccount = bonusToAccount;
	}
	@Override
	public String toString() {
		return "BetRecordListBean [userCode=" + userCode + ", orderId=" + orderId + ", programsOrderId=" + programsOrderId + ", autoOrderId=" + autoOrderId + ", orderStatus=" + orderStatus
				+ ", bonusStatus=" + bonusStatus + ", orderType=" + orderType + ", orderAmount=" + orderAmount + ", bonusAmount=" + bonusAmount + ", fixBonusAmount=" + fixBonusAmount
				+ ", programsAmount=" + programsAmount + ", lotteryId=" + lotteryId + ", playId=" + playId + ", pollId=" + pollId + ", issue=" + issue + ", sendStatus=" + sendStatus
				+ ", programsStatus=" + programsStatus + ", buyType=" + buyType + ", createTime=" + createTime + ", bonusToAccount=" + bonusToAccount + "]";
	}
	
}
