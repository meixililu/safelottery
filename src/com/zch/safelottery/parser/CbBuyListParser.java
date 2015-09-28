package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.combinebean.CbBuyListBean;

public class CbBuyListParser extends AbstractParser<CbBuyListBean>{

	@Override
	public CbBuyListBean parse(JSONObject json) throws JSONException {
		CbBuyListBean mBean = new CbBuyListBean();
		if(json.has("userCode")) mBean.setUserCode(json.getString("userCode"));
		if(json.has("userName")) mBean.setUserName(json.getString("userName"));
		if(json.has("orderId")) mBean.setOrderId(json.getString("orderId"));
		if(json.has("orderType")) mBean.setOrderType(json.getString("orderType"));
		if(json.has("orderTypeName")) mBean.setOrderTypeName(json.getString("orderTypeName"));
		if(json.has("orderAmount")) mBean.setOrderAmount(json.getString("orderAmount"));
		if(json.has("orderStatus")) mBean.setOrderStatus(json.getString("orderStatus"));
		if(json.has("orderStatusName")) mBean.setOrderStatusName(json.getString("orderStatusName"));
		if(json.has("bonusAmount")) mBean.setBonusAmount(json.getString("bonusAmount"));
		if(json.has("fixBonusAmount")) mBean.setFixBonusAmount(json.getString("fixBonusAmount"));
		if(json.has("createTime")) mBean.setCreateTime(json.getString("createTime"));
		return mBean;
	}

}
