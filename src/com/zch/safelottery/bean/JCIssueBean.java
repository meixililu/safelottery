package com.zch.safelottery.bean;

public class JCIssueBean implements SafelotteryType{
	
	/**场次数**/
	private String matchCount;
	/**期次列**/
	private String issue;
	
	public String getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(String matchCount) {
		this.matchCount = matchCount;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	
}
