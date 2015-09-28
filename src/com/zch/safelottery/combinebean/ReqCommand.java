package com.zch.safelottery.combinebean;

import com.zch.safelottery.setttings.SystemInfo;


public class ReqCommand{
	
	private String  platform = SystemInfo.platform ; //平台
	private String  userCode = "";//GetString.currentUser != null ? GetString.currentUser.getUserId() != null ? GetString.currentUser.getUserId() : "" : "";//用户名code
	private String  hid = SystemInfo.sid; //合作方id
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
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

}
