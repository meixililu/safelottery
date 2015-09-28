package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.TakeAccountBean;

public class TakeAccountParser extends AbstractParser<TakeAccountBean>{

	@Override
	public TakeAccountBean parse(JSONObject json) throws JSONException {
		TakeAccountBean mBean = new TakeAccountBean();
		
		if(json.has("orderId")) mBean.setOrderId(json.getString("orderId"));
		if(json.has("drawResources")) mBean.setDrawResources(json.getString("drawResources"));
		if(json.has("amount")) mBean.setAmount(json.getString("amount"));
		if(json.has("fee")) mBean.setFee(json.getString("fee"));
		if(json.has("sid")) mBean.setSid(json.getString("sid"));
		if(json.has("platform")) mBean.setPlatform(json.getString("platform"));
		if(json.has("status")) mBean.setStatus(json.getString("status"));
		if(json.has("createTime")) mBean.setCreateTime(json.getString("createTime"));
		if(json.has("acceptTime")) mBean.setAcceptTime(json.getString("acceptTime"));
		if(json.has("memo")) mBean.setMemo(json.getString("memo"));
		
		return mBean;
	}

}
