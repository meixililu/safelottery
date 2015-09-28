package com.zch.safelottery.bean;

public class RecordPursueListBean implements SafelotteryType{
	
	private String userCode; //方案发起人
	private String programsOrderId; //方案号
	private String issue; //期次
	private String item; //注数
	private String multiple; //倍数
	private int orderStatus; //方案状态
	private int sendStatus; //发单状态
	private int bonusStatus; //中奖状态
	private int issueStatus; //期次状态
	private String orderAmount; //方案金额
	private String bonusAmount; //税后中奖金额
	private String fixBonusAmount; //税前中奖金额
	private String bonusToAccount; //是否派奖  0否 1是
	private String bonusNumber; //开奖号码
	private String createTime; //创建时间
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getProgramsOrderId() {
		return programsOrderId;
	}
	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}
	public int getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(int bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public int getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(int issueStatus) {
		this.issueStatus = issueStatus;
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
	public String getBonusToAccount() {
		return bonusToAccount;
	}
	public void setBonusToAccount(String bonusToAccount) {
		this.bonusToAccount = bonusToAccount;
	}
	public String getBonusNumber() {
		return bonusNumber;
	}
	public void setBonusNumber(String bonusNumber) {
		this.bonusNumber = bonusNumber;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "RecordPursueListBean [userCode=" + userCode
				+ ", programsOrderId=" + programsOrderId + ", issue=" + issue
				+ ", item=" + item + ", multiple=" + multiple
				+ ", orderStatus=" + orderStatus + ", sendStatus=" + sendStatus
				+ ", bonusStatus=" + bonusStatus + ", issueStatus="
				+ issueStatus + ", orderAmount=" + orderAmount
				+ ", bonusAmount=" + bonusAmount + ", fixBonusAmount="
				+ fixBonusAmount + ", bonusToAccount=" + bonusToAccount
				+ ", bonusNumber=" + bonusNumber + ", createTime=" + createTime
				+ "]";
	}
}
