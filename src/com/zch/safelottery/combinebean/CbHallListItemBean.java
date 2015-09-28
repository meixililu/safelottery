package com.zch.safelottery.combinebean;

import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.util.ConversionUtil;

public class CbHallListItemBean implements SafelotteryType {
	
	private String userCode;// 方案发起人
	private String userName;// 方案发起人
	private String nickName;// 方案发起人
	private String programsOrderId;// 方案号
	private String lotteryId;// 彩种编码
	private String lotteryName;// 彩种中文
	private String playId;// 玩法编码
	private String playName;// 玩法中文
	private String pollId;// 选号方式
	private String issue;// 期次
	private String issueStatus;// 期次状态 0 预售	1 当前期	3 结期
	private String endTime;// 期次结期时间
	private String orderStatus;// 方案状态
	private String item;// 注数
	private String multiple;// 倍数
	private String totalWere;// 方案总金额
	private String buyWere;// 认购金额
	private String lastWere;// 保底金额
	private String buyPercent;// 认购百分比
	private String lastPercent;// 保底百分比
	private String commission;// 佣金提成
	private String isTop;// 是否置顶 0 否	1是
	private String createTime;// 创建时间
	
	@Override
	public String toString() {
		return "CbHallListItemBean [userCode=" + userCode + ", userName="
				+ userName + ", programsOrderId=" + programsOrderId
				+ ", lotteryId=" + lotteryId + ", lotteryName=" + lotteryName
				+ ", playId=" + playId + ", playName=" + playName + ", pollId="
				+ pollId + ", issue=" + issue + ", issueStatus=" + issueStatus
				+ ", endTime=" + endTime + ", orderStatus=" + orderStatus
				+ ", item=" + item + ", multiple=" + multiple + ", totalWere="
				+ totalWere + ", buyWere=" + buyWere + ", lastWere=" + lastWere
				+ ", buyPercent=" + buyPercent + ", lastPercent=" + lastPercent
				+ ", commission=" + commission + ", isTop=" + isTop
				+ ", createTime=" + createTime + "]";
	}
	
	public int getSurplusWere(){
		return ConversionUtil.StringToInt(totalWere) - ConversionUtil.StringToInt(buyWere);
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getProgramsOrderId() {
		return programsOrderId;
	}
	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getLotteryName() {
		return lotteryName;
	}
	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
	public String getPlayId() {
		return playId;
	}
	public void setPlayId(String playId) {
		this.playId = playId;
	}
	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	public String getPollId() {
		return pollId;
	}
	public void setPollId(String pollId) {
		this.pollId = pollId;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public String getTotalWere() {
		return totalWere;
	}
	public void setTotalWere(String totalWere) {
		this.totalWere = totalWere;
	}
	public String getBuyWere() {
		return buyWere;
	}
	public void setBuyWere(String buyWere) {
		this.buyWere = buyWere;
	}
	public String getLastWere() {
		return lastWere;
	}
	public void setLastWere(String lastWere) {
		this.lastWere = lastWere;
	}
	public String getBuyPercent() {
		return buyPercent;
	}
	public void setBuyPercent(String buyPercent) {
		this.buyPercent = buyPercent;
	}
	public String getLastPercent() {
		return lastPercent;
	}
	public void setLastPercent(String lastPercent) {
		this.lastPercent = lastPercent;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getIsTop() {
		return isTop;
	}
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
