package com.zch.safelottery.bean;

public class TakeAccountBean implements SafelotteryType{
	
	private String orderId;//订单号
	private String drawResources;//提现渠道
	private String amount;//发起提现金额
	private String fee;//提现手续费
	private String sid;//渠道号
	private String platform;//平台
	private String status;//提现状态
	private String createTime;//充值时间
	private String acceptTime;//处理时间
	private String memo;//备注
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDrawResources() {
		return drawResources;
	}
	public void setDrawResources(String drawResources) {
		this.drawResources = drawResources;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
