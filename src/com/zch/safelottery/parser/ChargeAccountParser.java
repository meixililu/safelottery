package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.ChargeAccountBean;

public class ChargeAccountParser extends AbstractParser<ChargeAccountBean> {

	@Override
	public ChargeAccountBean parse(JSONObject json) throws JSONException {
		ChargeAccountBean mBean = new ChargeAccountBean();
		
		if(json.has("orderId")) mBean.setOrderId(json.getString("orderId"));
		if(json.has("outOrderId")) mBean.setOutOrderId(json.getString("outOrderId"));
		if(json.has("fillResources")) mBean.setFillResources(json.getString("fillResources"));
		if(json.has("fillResourcesName")) mBean.setFillResourcesName(json.getString("fillResourcesName"));
		if(json.has("amount")) mBean.setAmount(json.getString("amount"));
		if(json.has("realAmount")) mBean.setRealAmount(json.getString("realAmount"));
		if(json.has("sid")) mBean.setSid(json.getString("sid"));
		if(json.has("platform")) mBean.setPlatform(json.getString("platform"));
		if(json.has("status")) mBean.setStatus(json.getString("status"));
		if(json.has("createTime")) mBean.setCreateTime(json.getString("createTime"));
		if(json.has("acceptTime")) mBean.setAcceptTime(json.getString("acceptTime"));
		
		return mBean;
	}

}
