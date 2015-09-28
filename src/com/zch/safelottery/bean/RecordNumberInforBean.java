package com.zch.safelottery.bean;

public class RecordNumberInforBean implements SafelotteryType{
	
	//programs节点下numberInfo节点字符串数据
	
	private String sequence; //序号
	private String playCode; //玩法编码
	private String pollCode; //选号方式
	private String number; //号码
	private String item; //注数
	private String numberFormat; //单式上传投注号码
	private String uploadMatch; //单式上传选择的场次
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getNumberFormat() {
		return numberFormat;
	}
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
	public String getUploadMatch() {
		return uploadMatch;
	}
	public void setUploadMatch(String uploadMatch) {
		this.uploadMatch = uploadMatch;
	}
	@Override
	public String toString() {
		return "RecordNumberInforBean [sequence=" + sequence + ", playCode=" + playCode + ", pollCode=" + pollCode + ", number=" + number + ", item=" + item + ", numberFormat=" + numberFormat
				+ ", uploadMatch=" + uploadMatch + "]";
	}
	
}
