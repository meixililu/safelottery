package com.zch.safelottery.combinebean;

import java.util.List;

public class ListReturnBean{

	private int pageSize;// 每页条数
	private int page;// 当前页
	private int pageTotal;// 总页数
	private int toatl;// 总条数

	private List<OrderBean> itemList;// 内容

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getToatl() {
		return toatl;
	}

	public void setToatl(int toatl) {
		this.toatl = toatl;
	}

	public List<OrderBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderBean> itemList) {
		this.itemList = itemList;
	}
	
	
}
