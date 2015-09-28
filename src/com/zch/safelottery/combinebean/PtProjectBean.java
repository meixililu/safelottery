package com.zch.safelottery.combinebean;

// Generated 2010-4-15 10:53:19 by Hibernate Tools 3.1.0.beta4

import java.util.List;

/**
 * @struts.form include-all="true" extends="BaseForm"
 * @hibernate.class table="PT_PROJECT"
 * 
 */

public class PtProjectBean  {
	private String userCode;// 用户名
	private String hid; // 合作方id
	private String platform;// 来源
	private String lotteryCode;// 彩种
	private String playCode;//玩法
	private String issue; // 期次
	private String declaration;// 方案宣言
	private double commission;// 方案佣金比率
	private double buyWere;// 方案购买金额
	private double lastWere;// 方案保底金额
	private String secretType;// 保密类型设置
	private int totalWere;// 方案金额
	private int multiple;// 倍数

	private List<CTicket> tickets;

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

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
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

	public String getSecretType() {
		return secretType;
	}

	public void setSecretType(String secretType) {
		this.secretType = secretType;
	}

	public int getTotalWere() {
		return totalWere;
	}

	public void setTotalWere(int totalWere) {
		this.totalWere = totalWere;
	}


	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public List<CTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<CTicket> tickets) {
		this.tickets = tickets;
	}
}
