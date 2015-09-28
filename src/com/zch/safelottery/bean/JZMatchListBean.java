package com.zch.safelottery.bean;

import java.util.ArrayList;

public class JZMatchListBean implements SafelotteryType{
	
	/**期次**/
	private String date;
	/**星期**/
	private String week;
	/**返回的场次数**/
	private int matchTotal;
	/**场次列表**/
	private ArrayList<JZMatchBean> matchList = new ArrayList<JZMatchBean>();
	
	private ArrayList<JZMatchBean> backupList = new ArrayList<JZMatchBean>();
	/**日期星期**/
	private String section;
	/**状态：1为展开显示，2未展示**/
	private int status;
	
	private String issue;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public int getMatchTotal() {
		return matchTotal;
	}
	public void setMatchTotal(int matchTotal) {
		this.matchTotal = matchTotal;
	}
	public ArrayList<JZMatchBean> getMatchList() {
		return matchList;
	}
	public void setMatchList(ArrayList<JZMatchBean> matchList) {
		this.matchList = matchList;
	}
	public ArrayList<JZMatchBean> getBackupList() {
		return backupList;
	}
	public void setBackupList(ArrayList<JZMatchBean> backupList) {
		this.backupList = backupList;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	@Override
	public String toString() {
		return "JZMatchListBean [date=" + date + ", week=" + week
				+ ", matchTotal=" + matchTotal + ", matchList=" + matchList
				+ ", backupList=" + backupList + ", section=" + section + "]";
	}
	
}
