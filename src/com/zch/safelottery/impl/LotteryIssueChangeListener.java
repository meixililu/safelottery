package com.zch.safelottery.impl;

public interface LotteryIssueChangeListener {
	public void onSucceeToGetIssue(String lid);
	public void onFaileToGetIssue(String lid);
}
