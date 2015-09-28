package com.zch.safelottery.combinebean;

public class CbBuyRequestBean extends ReqCommand {

	public String getProgramsOrderId() {
		return programsOrderId;
	}
	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getWere() {
		return were;
	}
	public void setWere(int were) {
		this.were = were;
	}
	
	private String programsOrderId;// 方案号
	private int amount;// 认购金额
	private int were; // 认购分数 = 认购金额
}
