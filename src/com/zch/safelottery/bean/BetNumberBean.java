package com.zch.safelottery.bean;

public class BetNumberBean implements SafelotteryType{

	private int index;
	private String buyNumber; //投注号码
	private String playId = ""; //玩法编码
	private String pollId = ""; //选号方式
	private long item = 1; //注数
	private long amount = 2; //金额

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getBuyNumber() {
		return buyNumber;
	}
	public void setBuyNumber(String buyNumber) {
		this.buyNumber = buyNumber;
	}
	public String getPlayId() {
		return playId;
	}
	public void setPlayId(String playId) {
		this.playId = playId;
	}
	public String getPollId() {
		return pollId;
	}
	public void setPollId(String pollId) {
		this.pollId = pollId;
	}
	public long getItem() {
		return item;
	}
	public void setItem(long item) {
		this.item = item;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "BetNumberBean [buyNumber=" + buyNumber + ", playId=" + playId
				+ ", pollId=" + pollId + ", item=" + item + ", amount="
				+ amount + "]";
	}
}
