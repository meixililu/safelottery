package com.zch.safelottery.bean;

public class ChargeLakalaBean implements SafelotteryType{

	private String version;//版本号
	private String merId;//商户号
	private String minCode;//商户快捷编号
	private String orderId;//订单号
	private String amount;//充值金额
	private String time;//充值时间
	private String macType;//校验类型
	private String notifyUrl;//回调地址
	private String merPwd;//商户密码
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getMinCode() {
		return minCode;
	}
	public void setMinCode(String minCode) {
		this.minCode = minCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMacType() {
		return macType;
	}
	public void setMacType(String macType) {
		this.macType = macType;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getMerPwd() {
		return merPwd;
	}
	public void setMerPwd(String merPwd) {
		this.merPwd = merPwd;
	}
}
