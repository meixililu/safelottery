package com.zch.safelottery.bean;

public class RecordMainIssueListBean implements SafelotteryType{
	/*
	 *  方案详情全部节点 
	 * member json格式 用户信息
	 * programs json格式 方案信息
	 * mainIssue json格式 期次信息
	 * subGameList json格式 传统足彩对阵信息
	 * match json格式 竞技彩对阵信息
	 */
	//subGameList节点数据
	
	private String bonusNumber; //开奖号码
	private String startTime; //官方开始时间
	private String endTime; //官方截止时间
	private String simplexTime; //单式截止时间
	private String duplexTime; //复式截止时间
	private String bonusTime; //返奖时间
	private String bonusStatus; //返奖状态
	private String status; //期次状态
	public String getBonusNumber() {
		return bonusNumber;
	}
	public void setBonusNumber(String bonusNumber) {
		this.bonusNumber = bonusNumber;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSimplexTime() {
		return simplexTime;
	}
	public void setSimplexTime(String simplexTime) {
		this.simplexTime = simplexTime;
	}
	public String getDuplexTime() {
		return duplexTime;
	}
	public void setDuplexTime(String duplexTime) {
		this.duplexTime = duplexTime;
	}
	public String getBonusTime() {
		return bonusTime;
	}
	public void setBonusTime(String bonusTime) {
		this.bonusTime = bonusTime;
	}
	public String getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "RecordMainIssueListBean [bonusNumber=" + bonusNumber + ", startTime=" + startTime + ", endTime=" + endTime + ", simplexTime=" + simplexTime + ", duplexTime=" + duplexTime
				+ ", bonusTime=" + bonusTime + ", bonusStatus=" + bonusStatus + ", status=" + status + "]";
	}
}
