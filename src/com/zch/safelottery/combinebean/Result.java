package com.zch.safelottery.combinebean;

import com.zch.safelottery.bean.SafelotteryType;

public class Result implements SafelotteryType{
	
	private String code;  //响应码
	private String msg;   //响应码描述
	private String result;//具体的业务有不同的返回参数
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	

}
