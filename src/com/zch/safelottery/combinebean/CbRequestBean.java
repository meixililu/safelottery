package com.zch.safelottery.combinebean;

public class CbRequestBean {
	
	private String nickName;// 查询的用户昵称/用户名
	private String userCode;// 用户编码
	private String lotteryId;// 彩种
	private String playId;// 玩法
	private String issue;// 期次
	private String orderStatus;// 方案状态
	private String privacy;// 保密类型  -1. 全部	0公开	1保密	2截止后公开	3仅对跟单者公开 
	private String baoDi;// 是否保底  0 是 1 否
	private String isFull;// 是否满员 0满员	1未满员
	private String sort;// 排序 1认购进度 降序 2认购进度 升序 3方案总金额 降序	4方案总金额 升序	5剩余金额 降序	6剩余金额 升序
	private int page;// 当前页数
	private int pageSize;// 每页条数

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getBaoDi() {
		return baoDi;
	}
	public void setBaoDi(String baoDi) {
		this.baoDi = baoDi;
	}
	public String getIsFull() {
		return isFull;
	}
	public void setIsFull(String isFull) {
		this.isFull = isFull;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
