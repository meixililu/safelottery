package com.zch.safelottery.combinebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CbInfoReultBean {

	private String userCode = "";// 用户code
	private String userName = "";// 用户名

	private double totalWere;// 总金额
	private double buyWere;// 认购金额
	private double lastWere;// 保底金额
	private double surplusWere;// 保底金额
	private String buyPercent = "";// 已购比率
	private String lastPercent = "";// 保底比率
	private String programsOrderId = "";// 方案号
	private String multiple = "";// 倍数
	private String item = "";// 总份数
	
	private String createTime = "";// 创建时间
	private String buyType = "";// 购买方式
	private String playCode = "";// 玩法
	private String commission = "";// 佣金比率
	private String commissionAmount = "";// 佣金
	
	private String subscribeUser = "";// 参与人数
	private double sponsorOrderAmount;// 发起人认购金额
	private double loginOrderAmount;// 登录人认购金额
	private double loginBonusAmount;// 登录人中奖金额
	
	private String orderStatus = "";// 方案状态
	private String bonusStatus = "";// 中奖状态
	private String bonusAmount = "";// 中奖金额
	private String fixBonusAmount = "";// 税前奖金
	private boolean isCancelPrograms;// 是否能取消方案
	private int minWere;// 最小认购份数
	private int privacy;// 保密方式
	private String isUpload = "";// 是否单式上传
	private String filePath = "";// 文件地址
	private String lotteryCode = "";// 彩种编号
	private String issue = "";// 期次
	private String numberInfo = "";// 投注号码 （仅数字彩使用）

	private double totalBonusAmount;// 中奖总额
	private String totalBonusCount = "";// 中奖次数

	private String bonusNumber = "";// 中奖号码
	private String bonusTime = "";// 开奖时间
	private String endDate = "";// 官方截期时间
	private String simpleDate = "";// 单式截期时间
	private String duplexDate = "";// 复式截期时间 传送确认为复式时间

	private String lotteryName = "";// 彩种名称
	private String playName = "";// 玩法名称
	
	private String title; //方案宣言
	private int issueStatus; //2  暂停  3 结期
	
	private List<Serializable> duiZhen;// 对阵List 足彩 竞彩使用


	public double getSurplusWere() {
		surplusWere = totalWere - buyWere;
		return surplusWere;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIssueStatus() {
		return issueStatus;
	}

	public void setIssueStatus(int issueStatus) {
		this.issueStatus = issueStatus;
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

	public double getTotalWere() {
		return totalWere;
	}

	public void setTotalWere(double totalWere) {
		this.totalWere = totalWere;
	}

	public double getBuyWere() {
		return buyWere;
	}

	public void setBuyWere(double buyWere) {
		this.buyWere = buyWere;
	}

	public double getLastWere() {
		return lastWere;
	}

	public void setLastWere(double lastWere) {
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

	public String getProgramsOrderId() {
		return programsOrderId;
	}

	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(String commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public String getSubscribeUser() {
		return subscribeUser;
	}

	public void setSubscribeUser(String subscribeUser) {
		this.subscribeUser = subscribeUser;
	}

	public double getSponsorOrderAmount() {
		return sponsorOrderAmount;
	}

	public void setSponsorOrderAmount(double sponsorOrderAmount) {
		this.sponsorOrderAmount = sponsorOrderAmount;
	}

	public double getLoginOrderAmount() {
		return loginOrderAmount;
	}

	public void setLoginOrderAmount(double loginOrderAmount) {
		this.loginOrderAmount = loginOrderAmount;
	}

	public double getLoginBonusAmount() {
		return loginBonusAmount;
	}

	public void setLoginBonusAmount(double loginBonusAmount) {
		this.loginBonusAmount = loginBonusAmount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getBonusStatus() {
		return bonusStatus;
	}

	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}

	public String getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public String getFixBonusAmount() {
		return fixBonusAmount;
	}

	public void setFixBonusAmount(String fixBonusAmount) {
		this.fixBonusAmount = fixBonusAmount;
	}

	public boolean getIsCancelPrograms() {
		return isCancelPrograms;
	}

	public void setIsCancelPrograms(boolean isCancelPrograms) {
		this.isCancelPrograms = isCancelPrograms;
	}

	public int getMinWere() {
		return minWere;
	}

	public void setMinWere(int minWere) {
		this.minWere = minWere;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(String lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getNumberInfo() {
		return isNull(numberInfo);
	}

	public void setNumberInfo(String numberInfo) {
		this.numberInfo = numberInfo;
	}

	public double getTotalBonusAmount() {
		return totalBonusAmount;
	}

	public void setTotalBonusAmount(double totalBonusAmount) {
		this.totalBonusAmount = totalBonusAmount;
	}

	public String getTotalBonusCount() {
		return totalBonusCount;
	}

	public void setTotalBonusCount(String totalBonusCount) {
		this.totalBonusCount = totalBonusCount;
	}

	public String getBonusNumber() {
		return bonusNumber;
	}

	public void setBonusNumber(String bonusNumber) {
		this.bonusNumber = bonusNumber;
	}

	public String getBonusTime() {
		return bonusTime;
	}

	public void setBonusTime(String bonusTime) {
		this.bonusTime = bonusTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSimpleDate() {
		return simpleDate;
	}

	public void setSimpleDate(String simpleDate) {
		this.simpleDate = simpleDate;
	}

	public String getDuplexDate() {
		return duplexDate;
	}

	public void setDuplexDate(String duplexDate) {
		this.duplexDate = duplexDate;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getPlayName() {
		return playName;
	}

	public void setPlayName(String playName) {
		this.playName = playName;
	}

	public List<Serializable> getDuiZhen() {
		return duiZhen;
	}

	public void setDuiZhen(ArrayList<Serializable> duiZhen) {
		this.duiZhen = duiZhen;
	}


	@Override
	public String toString() {
		return "CbInfoReultBean [programsOrderId=" + programsOrderId
				+ ", buyType=" + buyType + ", playCode=" + playCode
				+ ", orderStatus=" + orderStatus + ", bonusStatus="
				+ bonusStatus + ", bonusAmount=" + bonusAmount
				+ ", fixBonusAmount=" + fixBonusAmount + ", multiple="
				+ multiple + ", item=" + item + ", isCancelPrograms="
				+ isCancelPrograms + ", minWere=" + minWere + ", commission="
				+ commission + ", commissionAmount=" + commissionAmount
				+ ", privacy=" + privacy + ", isUpload=" + isUpload
				+ ", filePath=" + filePath + ", subscribeUser=" + subscribeUser
				+ ", sponsorOrderAmount=" + sponsorOrderAmount
				+ ", loginOrderAmount=" + loginOrderAmount
				+ ", loginBonusAmount=" + loginBonusAmount + ", lotteryCode="
				+ lotteryCode + ", issue=" + issue + ", totalWere=" + totalWere
				+ ", buyWere=" + buyWere + ", lastWere=" + lastWere
				+ ", buyPercent=" + buyPercent + ", lastPercent=" + lastPercent
				+ ", createTime=" + createTime + ", numberInfo=" + numberInfo
				+ ", userCode=" + userCode + ", userName=" + userName
				+ ", totalBonusAmount=" + totalBonusAmount
				+ ", totalBonusCount=" + totalBonusCount + ", bonusNumber="
				+ bonusNumber + ", bonusTime=" + bonusTime + ", endDate="
				+ endDate + ", simpleDate=" + simpleDate + ", duplexDate="
				+ duplexDate + ", lotteryName=" + lotteryName + ", playName="
				+ playName + ", duiZhen=" + duiZhen + "]";
	}

	private String isNull(String org){
		return org == null ? "" : org;
	}
}
