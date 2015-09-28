package com.zch.safelottery.bean;

public class FeedListBean implements SafelotteryType {
	
	/**反馈id号**/
	private String id;
	/**反馈类型:1 充值问题,2购彩问题,3返奖问题,4提款问题,5软件问题,6咨询建议,0 其他**/
	private String feedType;
	/**反馈内容**/
	private String feedInfo;
	/**处理状态:0 未处理,1 已处理**/
	private String status;
	/**反馈时间**/
	private String createTime;
	/**处理人**/
	private String acceptor;
	/**处理说明**/
	private String handleInfo;
	/**处理时间**/
	private String acceptTime;
	/**处理说明:1满意,2一般,3不满意**/
	private String assess;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFeedType() {
		return feedType;
	}
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	public String getFeedInfo() {
		return feedInfo;
	}
	public void setFeedInfo(String feedInfo) {
		this.feedInfo = feedInfo;
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
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getHandleInfo() {
		return handleInfo;
	}
	public void setHandleInfo(String handleInfo) {
		this.handleInfo = handleInfo;
	}
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	public String getAssess() {
		return assess;
	}
	public void setAssess(String assess) {
		this.assess = assess;
	}
	
}
