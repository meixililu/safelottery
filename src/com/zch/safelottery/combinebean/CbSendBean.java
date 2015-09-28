package com.zch.safelottery.combinebean;

public class CbSendBean {

	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getSumPage() {
		return sumPage;
	}
	public void setSumPage(int sumPage) {
		this.sumPage = sumPage;
	}
	public int getPerPage() {
		return perPage;
	}
	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}
	public int getRequestPage() {
		return requestPage;
	}
	public void setRequestPage(int requestPage) {
		this.requestPage = requestPage;
	}
	private String lid;         //彩种
	private int sort;           //排序方式
	private int sumPage;     //总页数
	private int perPage;      //每页条数
	private int requestPage; //排序方式
	
}
