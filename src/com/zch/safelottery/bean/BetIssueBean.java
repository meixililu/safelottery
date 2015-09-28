package com.zch.safelottery.bean;

public class BetIssueBean implements SafelotteryType{
	
	private String issue;//期次
	private int multiple = 1;//倍数
	
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	
}
