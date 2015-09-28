package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordPursueListBean;

public class RecordPursueListParser extends AbstractParser<RecordPursueListBean>{

	@Override
	public RecordPursueListBean parse(JSONObject json) throws JSONException {
		RecordPursueListBean mBean = new RecordPursueListBean();
		if(json.has("userCode")) mBean.setUserCode(json.getString("userCode"));
		if(json.has("programsOrderId")) mBean.setProgramsOrderId(json.getString("programsOrderId"));
		if(json.has("issue")) mBean.setIssue(json.getString("issue"));
		if(json.has("item")) mBean.setItem(json.getString("item"));
		if(json.has("multiple")) mBean.setMultiple(json.getString("multiple"));
		
		if(json.has("orderStatus")) mBean.setOrderStatus(Integer.parseInt(json.getString("orderStatus")));
		if(json.has("sendStatus")) mBean.setSendStatus(Integer.parseInt(json.getString("sendStatus")));
		if(json.has("bonusStatus")) mBean.setBonusStatus(Integer.parseInt(json.getString("bonusStatus")));
		if(json.has("issueStatus")) mBean.setIssueStatus(Integer.parseInt(json.getString("issueStatus")));
		
		if(json.has("orderAmount")) mBean.setOrderAmount(json.getString("orderAmount"));
		if(json.has("bonusAmount")) mBean.setBonusAmount(json.getString("bonusAmount"));
		if(json.has("fixBonusAmount")) mBean.setFixBonusAmount(json.getString("fixBonusAmount"));
		if(json.has("bonusToAccount")) mBean.setBonusToAccount(json.getString("bonusToAccount"));
		if(json.has("bonusNumber")) mBean.setBonusNumber(json.getString("bonusNumber"));
		if(json.has("createTime")) mBean.setCreateTime(json.getString("createTime"));
		return mBean;
	}

}
