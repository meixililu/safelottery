package com.zch.safelottery.combinebean;

import com.zch.safelottery.bean.SafelotteryType;

public class CbBuyListBean implements SafelotteryType{
	private String userCode; //方案发起人
	private String userName; //方案发起人
	private String orderId; //订单号
	private String orderType; //订单类型
	private String orderTypeName; //订单类型中文
	private String orderAmount; //认购金额
	private String orderStatus; //订单状态
	private String orderStatusName; //订单状态中文
	private String bonusAmount; //中奖金额
	private String fixBonusAmount; //税前中奖金额
	private String createTime; //创建时间
	
	@Override
	public String toString() {
		return "CbBuyListBean [userCode=" + userCode + ", userName=" + userName
				+ ", orderId=" + orderId + ", orderType=" + orderType
				+ ", orderTypeName=" + orderTypeName + ", orderAmount="
				+ orderAmount + ", orderStatus=" + orderStatus
				+ ", orderStatusName=" + orderStatusName + ", bonusAmount="
				+ bonusAmount + ", fixBonusAmount=" + fixBonusAmount
				+ ", createTime=" + createTime + "]";
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusName() {
		return orderStatusName;
	}
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
