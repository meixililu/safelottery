package com.zch.safelottery.combinebean;

public class CTicket  {

	/**
	 * 
	 */
	private int sequence;// 排序
	private String number;// 投注号码
	private String playCode;// 玩法ID
	private String pollCode;// 数字彩：单复式；竞彩：过关单关
	private int item;// 注数

	public String getPollCode() {
		return pollCode;
	}

	public void setPollCode(String pollCode) {
		this.pollCode = pollCode;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
