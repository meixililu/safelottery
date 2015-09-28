package com.zch.safelottery.bean;

import java.util.List;

import com.zch.safelottery.util.ConversionUtil;

public class RecordProgramsListBean implements SafelotteryType {

	/*
	 *  方案详情全部节点 
	 * member json格式 用户信息
	 * programs json格式 方案信息
	 * mainIssue json格式 期次信息
	 * subGameList json格式 传统足彩对阵信息
	 * match json格式 竞技彩对阵信息
	 */

	  // programs节点参数

	private String userCode; // 用户编号
	private String presentedUserCode; // 赠送人编码
	private String programsOrderId; // 方案号
	private String lotteryId; // 彩种编码
	private String playId; // 玩法编码
	private String playName;//玩法名字
	private String pollId; // 选号方式
	private String issue; // 期次
	private String buyType; // 购买方式
	private List<RecordNumberInforBean> numberInfo; // 投注号码
	private String orderStatus; // 玩法编码
	private String sendStatus; // 发单状态
	private String bonusStatus; // 中奖状态
	private String privacy; // 保密方式
	private String item; // 注数
	private String multiple; // 倍数
	private String totalWere; // 总金额
	private String buyWere; // 认购金额
	private String minWere; // 最小认购金额
	private String lastWere; // 保底金额
	private String commission; // 佣金提成比例
	private String description; // 方案宣言
	private String bonusAmount; // 税后中奖金额
	private String fixBonusAmount; // 税前中奖金额
	private String ticketCount; // 总票数
	private String bonusTicket; // 中奖票数
	private String successTicket; // 成功票数
	private String failureTicket; // 失败票数
	private String createTime; // 创建时间
	private String isTop; // 是否置顶
	private String isUpload; // 是否单式上传 0:不是上传;  1:直接上传;  2:先发起后上传.
	private String bigBonus; // 是否大奖
	private String bonusToAccount; // 是否派奖
	private String filePath; // 上传文件路径
	private String renGouCount; // 认购人数
	
	private String isCancelPrograms; // 是否可以撤单:1 是,0 否
	private String buyPercent; // 认购百分比
	private String lastPercent; // 保底百分比
	private String commissionAmount; // 佣金
	private String itemBonusAmount; // 每份中奖金额
	private String bonusClass; // 奖级
	private String showNumber; // 方案是否可见：0  不可见  1 可见
	private String bigNumberInfo; // 0不是; 1是,当时1时numberInfo为””
	
	
	/**未认购
	 * @return
	 */
	public int getSurplusWere(){
		return ConversionUtil.StringToInt(totalWere) - ConversionUtil.StringToInt(buyWere);
	}
	
	public String getShowNumber() {
		return showNumber;
	}
	public void setShowNumber(String showNumber) {
		this.showNumber = showNumber;
	}
	public String getIsCancelPrograms() {
		return isCancelPrograms;
	}
	public void setIsCancelPrograms(String isCancelPrograms) {
		this.isCancelPrograms = isCancelPrograms;
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
	public String getCommissionAmount() {
		return commissionAmount;
	}
	public void setCommissionAmount(String commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	public String getItemBonusAmount() {
		return itemBonusAmount;
	}
	public void setItemBonusAmount(String itemBonusAmount) {
		this.itemBonusAmount = itemBonusAmount;
	}
	public String getBonusClass() {
		return bonusClass;
	}
	public void setBonusClass(String bonusClass) {
		this.bonusClass = bonusClass;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getPresentedUserCode() {
		return presentedUserCode;
	}
	public void setPresentedUserCode(String presentedUserCode) {
		this.presentedUserCode = presentedUserCode;
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
	public String getBuyType() {
		return buyType;
	}
	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}
	public List<RecordNumberInforBean> getNumberInfo() {
		return numberInfo;
	}
	public void setNumberInfo(List<RecordNumberInforBean> numberInfo) {
		this.numberInfo = numberInfo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
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
	public String getMinWere() {
		return minWere;
	}
	public void setMinWere(String minWere) {
		this.minWere = minWere;
	}
	public String getLastWere() {
		return lastWere;
	}
	public void setLastWere(String lastWere) {
		this.lastWere = lastWere;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(String ticketCount) {
		this.ticketCount = ticketCount;
	}
	public String getBonusTicket() {
		return bonusTicket;
	}
	public void setBonusTicket(String bonusTicket) {
		this.bonusTicket = bonusTicket;
	}
	public String getSuccessTicket() {
		return successTicket;
	}
	public void setSuccessTicket(String successTicket) {
		this.successTicket = successTicket;
	}
	public String getFailureTicket() {
		return failureTicket;
	}
	public void setFailureTicket(String failureTicket) {
		this.failureTicket = failureTicket;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getIsTop() {
		return isTop;
	}
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	public String getIsUpload() {
		return isUpload;
	}
	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}
	public String getBigBonus() {
		return bigBonus;
	}
	public void setBigBonus(String bigBonus) {
		this.bigBonus = bigBonus;
	}
	public String getBonusToAccount() {
		return bonusToAccount;
	}
	public void setBonusToAccount(String bonusToAccount) {
		this.bonusToAccount = bonusToAccount;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getRenGouCount() {
		return renGouCount;
	}
	public void setRenGouCount(String renGouCount) {
		this.renGouCount = renGouCount;
	}

	public String getBigNumberInfo() {
		return bigNumberInfo;
	}

	public void setBigNumberInfo(String bigNumberInfo) {
		this.bigNumberInfo = bigNumberInfo;
	}
	@Override
	public String toString() {
		return "RecordProgramsListBean [userCode=" + userCode + ", presentedUserCode=" + presentedUserCode + ", programsOrderId=" + programsOrderId + ", lotteryId=" + lotteryId + ", playId=" + playId
				+ ", playName=" + playName + ", pollId=" + pollId + ", issue=" + issue + ", buyType=" + buyType + ", numberInfo=" + numberInfo + ", orderStatus=" + orderStatus + ", sendStatus="
				+ sendStatus + ", bonusStatus=" + bonusStatus + ", privacy=" + privacy + ", item=" + item + ", multiple=" + multiple + ", totalWere=" + totalWere + ", buyWere=" + buyWere
				+ ", minWere=" + minWere + ", lastWere=" + lastWere + ", commission=" + commission + ", description=" + description + ", bonusAmount=" + bonusAmount + ", fixBonusAmount="
				+ fixBonusAmount + ", ticketCount=" + ticketCount + ", bonusTicket=" + bonusTicket + ", successTicket=" + successTicket + ", failureTicket=" + failureTicket + ", createTime="
				+ createTime + ", isTop=" + isTop + ", isUpload=" + isUpload + ", bigBonus=" + bigBonus + ", bonusToAccount=" + bonusToAccount + ", filePath=" + filePath + ", renGouCount="
				+ renGouCount + ", isCancelPrograms=" + isCancelPrograms + ", buyPercent=" + buyPercent + ", lastPercent=" + lastPercent + ", commissionAmount=" + commissionAmount
				+ ", itemBonusAmount=" + itemBonusAmount + ", bonusClass=" + bonusClass + ", showNumber=" + showNumber + ", bigNumberInfo=" + bigNumberInfo + "]";
	}
}
