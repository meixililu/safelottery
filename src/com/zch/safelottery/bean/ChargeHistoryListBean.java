package com.zch.safelottery.bean;

public class ChargeHistoryListBean implements SafelotteryType{

	private String page; // 当前页
	private String pageSize; // 每页记录
	private String pageTotal;// 总页数
	private String itemTotal;// 总记录
	private String fillList;// 充值记录列表
	//下面数据属于fillList节点
	private String userCode;// 用户编号
	private String orderId;// 订单号
	private String outOrderId;//外部订单号
	private String fillResources;// 充值渠道
	private String fillResourcesName;// 充值渠道中文名称
	private String amount;// 发起充值金额
	private String realAmount;// 实到金额
	private String sid;// 渠道号
	private String platform;// 平台
	private String status;//充值状态
	private String createTime;// 充值时间
	private String acceptTime;// 处理时间
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(String pageTotal) {
		this.pageTotal = pageTotal;
	}
	public String getItemTotal() {
		return itemTotal;
	}
	public void setItemTotal(String itemTotal) {
		this.itemTotal = itemTotal;
	}
	public String getFillList() {
		return fillList;
	}
	public void setFillList(String fillList) {
		this.fillList = fillList;
	}
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
	public String getOutOrderId() {
		return outOrderId;
	}
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
	public String getFillResources() {
		return fillResources;
	}
	public void setFillResources(String fillResources) {
		this.fillResources = fillResources;
	}
	public String getFillResourcesName() {
		return fillResourcesName;
	}
	public void setFillResourcesName(String fillResourcesName) {
		this.fillResourcesName = fillResourcesName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(String realAmount) {
		this.realAmount = realAmount;
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

	
}
