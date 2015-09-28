package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.ChargeLakalaBean;

public class ChargeLakalaParser extends AbstractParser<ChargeLakalaBean>{

	@Override
	public ChargeLakalaBean parse(JSONObject json) throws JSONException {
		ChargeLakalaBean mBean = new ChargeLakalaBean();
		
		if(json.has("version")) mBean.setVersion(json.getString("version"));
		if(json.has("merId")) mBean.setMerId(json.getString("merId"));
		if(json.has("minCode")) mBean.setMinCode(json.getString("minCode"));
		if(json.has("orderId")) mBean.setOrderId(json.getString("orderId"));
		if(json.has("amount")) mBean.setAmount(json.getString("amount"));
		if(json.has("time")) mBean.setTime(json.getString("time"));
		if(json.has("macType")) mBean.setMacType(json.getString("macType"));
		if(json.has("notifyUrl")) mBean.setNotifyUrl(json.getString("notifyUrl"));
		if(json.has("merPwd")) mBean.setMerPwd(json.getString("merPwd"));
		
		return mBean;
	}

}
