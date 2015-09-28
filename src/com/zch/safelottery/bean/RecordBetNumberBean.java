package com.zch.safelottery.bean;

public class RecordBetNumberBean implements SafelotteryType{

	private String number; //投注号码
	private String playCode; //玩法编码
	private String pollCode; //选号方式
	private String item; //注数
	private String sequence; //
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPlayCode() {
		return playCode;
	}
	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}
	public String getPollCode() {
		return pollCode;
	}
	public void setPollCode(String pollCode) {
		this.pollCode = pollCode;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	@Override
	public String toString() {
		return "RecordBetNumberBean [number=" + number + ", playCode="
				+ playCode + ", pollCode=" + pollCode + ", item=" + item
				+ ", sequence=" + sequence + "]";
	}
}
