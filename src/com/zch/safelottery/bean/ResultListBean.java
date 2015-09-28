package com.zch.safelottery.bean;

public class ResultListBean implements SafelotteryType {
	private String page; //当前页
	private String pageSize; //每页记录
	private String pageTotal; //总页数	
	private String itemTotal; //总记录
	private String orderList; //投注记录列表
	private String programsList; //方案列表
	
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
	public String getOrderList() {
		return orderList;
	}
	public void setOrderList(String orderList) {
		this.orderList = orderList;
	}
	public String getProgramsList() {
		return programsList;
	}
	public void setProgramsList(String programsList) {
		this.programsList = programsList;
	}
}
