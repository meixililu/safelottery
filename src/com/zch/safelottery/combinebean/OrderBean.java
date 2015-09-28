package com.zch.safelottery.combinebean;

import android.text.TextUtils;

import com.zch.safelottery.util.GetString;

public class OrderBean{

	private String userCode;// 用户名
	private String hid; // 合作方id
	private String platform;// 来源

	private String lotteryCode;// 彩种
	private String lotteryName;// 彩种名称
	private String programsOrderId;// 方案号
	private String orderId;// 订单号
	private String playCode;// 玩法
	private String playName;// 玩法名称
	private String pollCode;// 投注方式
	private String pollName;// 投注方式名称
	private String issue;// 期次
	private String totalWere;// 方案总金额
	private String buyWere;// 已购买金额
	private String lastWere;// 保底金额
	private String bonusAmount;// 保底金额
	private String buyPercent;// 认购比率
	private String lastPercent;// 保底比率
	private String createTime;// 投注时间
	private String userName;// 用户名
	private String bonusStatus;// 中奖状态
	private String orderStatus;// 方案状态
	private String buyType;// 购买方式
	private String orderType;// 认购方式
	private String flag;//1-受赠人，2-赠送人       根据bet_method=10取用
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLotteryCode() {
		return lotteryCode;
	}
	public void setLotteryCode(String lotteryCode) {
		this.lotteryCode = lotteryCode;
	}
	public String getLotteryName() {
		return lotteryName;
	}
	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
	public String getProgramsOrderId() {
		return programsOrderId;
	}
	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPlayCode() {
		return playCode;
	}
	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}
	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	public String getPollCode() {
		return pollCode;
	}
	public void setPollCode(String pollCode) {
		this.pollCode = pollCode;
	}
	public String getPollName() {
		return pollName;
	}
	public void setPollName(String pollName) {
		this.pollName = pollName;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getBuyType() {
		return buyType;
	}
	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public String getBonusAmount() {
		try{
			if(!TextUtils.isEmpty(bonusAmount)){
				double amount = Double.parseDouble(bonusAmount);
				return GetString.df.format(amount);
			}else{
				return "";
			}
		}catch (Exception e) {
		}
		return bonusAmount;
	}
	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
	}
	
	
}
