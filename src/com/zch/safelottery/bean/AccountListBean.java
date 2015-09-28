package com.zch.safelottery.bean;

public class AccountListBean implements SafelotteryType {
	// accountLogList节点
	private String userCode;// 用户编号
	private String eventType;// 操作类型
	private String type;// 操作二级类型
	private String eventCode;// 操作码
	private String eventName;// 操作码中文
	private String buyType;// 购买类型
	private String orderId;// 订单号
	private String programsOrderId;// 方案号
	private String autoOrderId;// 追号订单号
	private String changeAmount;// 发生额
	private String freezeAmount;// 操作冻结金
	private String amountNew;// 可用余额
	private String memo;// 描述信息
	private String createTime;// 创建时间
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getBuyType() {
		return buyType;
	}
	public void setBuyType(String buyType) {
		this.buyType = buyType;
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
	public String getChangeAmount() {
		return changeAmount;
	}
	public void setChangeAmount(String changeAmount) {
		this.changeAmount = changeAmount;
	}
	public String getFreezeAmount() {
		return freezeAmount;
	}
	public void setFreezeAmount(String freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
	public String getAmountNew() {
		return amountNew;
	}
	public void setAmountNew(String amountNew) {
		this.amountNew = amountNew;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "AccountListBean [userCode=" + userCode + ", eventType=" + eventType + ", type=" + type + ", eventCode=" + eventCode + ", eventName=" + eventName + ", buyType=" + buyType
				+ ", orderId=" + orderId + ", programsOrderId=" + programsOrderId + ", autoOrderId=" + autoOrderId + ", changeAmount=" + changeAmount + ", freezeAmount=" + freezeAmount
				+ ", amountNew=" + amountNew + ", memo=" + memo + ", createTime=" + createTime + "]";
	}
}
