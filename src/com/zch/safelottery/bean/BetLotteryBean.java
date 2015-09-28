package com.zch.safelottery.bean;

import java.util.List;

public class BetLotteryBean {
	private int appVersion; //软件版本
	
	private String userCode; //用户编号
	private String lotteryId; //彩种编码
	private String playId; //玩法编码	
	private String description; //合买宣言
	private String wereMin; //最少认购份数
	private String from; //来源，来自哪里的购买
	
	private int buyType; //购买方式 	1 代购 2 合买 4 追号
	private int privacy; //保密类型  0: 公开1: 保密	2: 截止后公开3: 仅对跟单者公开
	private int commision; //合买提成 0-10
	private int winStop; //追号设置  0: 中奖后不停止1: 中奖后停止	3: 中奖到多少金额停止
	
	private long buyAmount; //合买认购金额
	private long floorsAmount; //保底金额
	private long stopAmount; //停追金额
	private long totalAmount; //方案总金额
	
	private List<BetNumberBean> buyNumberArray; //选号列表
	private List<BetIssueBean> issueArray; //期次列表

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWereMin() {
		return wereMin;
	}
	public void setWereMin(String wereMin) {
		this.wereMin = wereMin;
	}
	public int getBuyType() {
		return buyType;
	}
	public void setBuyType(int buyType) {
		this.buyType = buyType;
	}
	public int getPrivacy() {
		return privacy;
	}
	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}
	public int getCommision() {
		return commision;
	}
	public void setCommision(int commision) {
		this.commision = commision;
	}
	public int getWinStop() {
		return winStop;
	}
	public void setWinStop(int winStop) {
		this.winStop = winStop;
	}
	public long getBuyAmount() {
		return buyAmount;
	}
	public void setBuyAmount(long buyAmount) {
		this.buyAmount = buyAmount;
	}
	public long getFloorsAmount() {
		return floorsAmount;
	}
	public void setFloorsAmount(long floorsAmount) {
		this.floorsAmount = floorsAmount;
	}
	public long getStopAmount() {
		return stopAmount;
	}
	public void setStopAmount(long stopAmount) {
		this.stopAmount = stopAmount;
	}
	public long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<BetNumberBean> getBuyNumberArray() {
		return buyNumberArray;
	}
	public void setBuyNumberArray(List<BetNumberBean> buyNumberArray) {
		this.buyNumberArray = buyNumberArray;
	}
	public List<BetIssueBean> getIssueArray() {
		return issueArray;
	}
	public void setIssueArray(List<BetIssueBean> issueArray) {
		this.issueArray = issueArray;
	}
	@Override
	public String toString() {
		return "BetLotteryBean [appVersion=" + appVersion + ", userCode="
				+ userCode + ", lotteryId=" + lotteryId + ", playId=" + playId
				+ ", description=" + description + ", wereMin=" + wereMin
				+ ", from=" + from + ", buyType=" + buyType + ", privacy="
				+ privacy + ", commision=" + commision + ", winStop=" + winStop
				+ ", buyAmount=" + buyAmount + ", floorsAmount=" + floorsAmount
				+ ", stopAmount=" + stopAmount + ", totalAmount=" + totalAmount
				+ ", buyNumberArray=" + buyNumberArray + ", issueArray="
				+ issueArray + "]";
	}
	
}
