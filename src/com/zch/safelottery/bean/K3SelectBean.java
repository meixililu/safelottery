package com.zch.safelottery.bean;

public class K3SelectBean {

	@Override
	public String toString() {
		return "K3SelectBean [groupId=" + groupId + ", itemId=" + itemId
				+ ", data=" + data + ", number=" + number + "]";
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	private int groupId;
	private int itemId;
	private String data;//当前选中的数据
	private String number;//要显示的号码
}
