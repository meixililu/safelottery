package com.zch.safelottery.combinebean;

public class CbInfoRequestBean extends ReqCommand {

	public String getProgramsOrderId() {
		return programsOrderId;
	}
	public void setProgramsOrderId(String programsOrderId) {
		this.programsOrderId = programsOrderId;
	}
	
	private String programsOrderId;// 方案号
	
}
