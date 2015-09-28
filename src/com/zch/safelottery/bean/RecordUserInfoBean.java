package com.zch.safelottery.bean;

import android.text.TextUtils;

public class RecordUserInfoBean implements SafelotteryType {

	private String userCode; // 发起人编码
	private String userName; // 发起人用户名
	private String bonusCount; // 中奖次数
	private String bonusTotal; // 中奖总额
	private String sponsorOrderAmount; // 发起人认购金额
	private String sponsorBonusAmount; // 发起人中奖金额
	private String loginOrderAmount; // 登录用户认购金额
	private String loginBonusAmount; // 登录用户中奖金额
	private String presentedUserName;// 赠送人用户名
	private String presentMobile;// 受赠人手机号

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

	public String getBonusCount() {
		return TextUtils.isEmpty(bonusCount) ? "0" : bonusCount;
	}

	public void setBonusCount(String bonusCount) {
		this.bonusCount = bonusCount;
	}

	public String getBonusTotal() {
		return bonusTotal;
	}

	public void setBonusTotal(String bonusTotal) {
		this.bonusTotal = bonusTotal;
	}

	public String getSponsorOrderAmount() {
		return sponsorOrderAmount;
	}

	public void setSponsorOrderAmount(String sponsorOrderAmount) {
		this.sponsorOrderAmount = sponsorOrderAmount;
	}

	public String getSponsorBonusAmount() {
		return sponsorBonusAmount;
	}

	public void setSponsorBonusAmount(String sponsorBonusAmount) {
		this.sponsorBonusAmount = sponsorBonusAmount;
	}

	public String getLoginOrderAmount() {
		return loginOrderAmount;
	}

	public void setLoginOrderAmount(String loginOrderAmount) {
		this.loginOrderAmount = loginOrderAmount;
	}

	public String getLoginBonusAmount() {
		return loginBonusAmount;
	}

	public void setLoginBonusAmount(String loginBonusAmount) {
		this.loginBonusAmount = loginBonusAmount;
	}

	public String getPresentedUserName() {
		return presentedUserName;
	}

	public void setPresentedUserName(String presentedUserName) {
		this.presentedUserName = presentedUserName;
	}

	public String getPresentMobile() {
		return presentMobile;
	}

	public void setPresentMobile(String presentMobile) {
		this.presentMobile = presentMobile;
	}

	@Override
	public String toString() {
		return "RecordUserInfoBean [userCode=" + userCode + ", userName=" + userName + ", bonusCount=" + bonusCount + ", bonusTotal=" + bonusTotal + ", sponsorOrderAmount=" + sponsorOrderAmount
				+ ", sponsorBonusAmount=" + sponsorBonusAmount + ", loginOrderAmount=" + loginOrderAmount + ", loginBonusAmount=" + loginBonusAmount + ", presentedUserName=" + presentedUserName
				+ ", presentMobile=" + presentMobile + "]";
	}

}
