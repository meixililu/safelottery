package com.zch.safelottery.bean;

public class RecordSubGameListBean implements SafelotteryType{
	
	/*
	 *  方案详情全部节点 
	 * member json格式 用户信息
	 * programs json格式 方案信息
	 * mainIssue json格式 期次信息
	 * subGameList json格式 传统足彩对阵信息
	 * match json格式 竞技彩对阵信息
	 */
	
	//subGameList节点参数
	
	private String sn; //场次号
	private String leageName; //赛事名称
	private String masterName; //主队名称
	private String masterNumber; //主队选号
	private String masterResult; //主队赛果
	private String masterSelect; //主队是否命中(0 否 1 是)
	private String guestName; //客队名称
	private String guestNumber; //客队选号
	private String guestResult; //客队赛果
	private String guestSelect; //客队是否命中(0 否 1 是)
	private String startTime; //开赛时间
	private String finalScore; //最终比分
	private String result; //赛果
	private String dan; //是否是胆(0 否 1 是)
	private String number; //选号
	private String isSelectDan; //胆是否命中(0 否 1 是)
	private String isSelect; //是否命中(0 否 1 是)
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getLeageName() {
		return leageName;
	}
	public void setLeageName(String leageName) {
		this.leageName = leageName;
	}
	public String getMasterName() {
		return masterName;
	}
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
	public String getMasterNumber() {
		return masterNumber;
	}
	public void setMasterNumber(String masterNumber) {
		this.masterNumber = masterNumber;
	}
	public String getMasterResult() {
		return masterResult;
	}
	public void setMasterResult(String masterResult) {
		this.masterResult = masterResult;
	}
	public String getMasterSelect() {
		return masterSelect;
	}
	public void setMasterSelect(String masterSelect) {
		this.masterSelect = masterSelect;
	}
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	public String getGuestNumber() {
		return guestNumber;
	}
	public void setGuestNumber(String guestNumber) {
		this.guestNumber = guestNumber;
	}
	public String getGuestResult() {
		return guestResult;
	}
	public void setGuestResult(String guestResult) {
		this.guestResult = guestResult;
	}
	public String getGuestSelect() {
		return guestSelect;
	}
	public void setGuestSelect(String guestSelect) {
		this.guestSelect = guestSelect;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getFinalScore() {
		return finalScore;
	}
	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDan() {
		return dan;
	}
	public void setDan(String dan) {
		this.dan = dan;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getIsSelectDan() {
		return isSelectDan;
	}
	public void setIsSelectDan(String isSelectDan) {
		this.isSelectDan = isSelectDan;
	}
	public String getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
	@Override
	public String toString() {
		return "RecordSubGameListBean [sn=" + sn + ", leageName=" + leageName + ", masterName=" + masterName + ", masterNumber=" + masterNumber + ", masterResult=" + masterResult + ", masterSelect="
				+ masterSelect + ", guestName=" + guestName + ", guestNumber=" + guestNumber + ", guestResult=" + guestResult + ", guestSelect=" + guestSelect + ", startTime=" + startTime
				+ ", finalScore=" + finalScore + ", result=" + result + ", dan=" + dan + ", number=" + number + ", isSelectDan=" + isSelectDan + ", isSelect=" + isSelect + "]";
	}
	
}
