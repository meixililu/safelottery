package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.AccountListBean;

public class AccountListParser extends AbstractParser<AccountListBean>{

	@Override
	public AccountListBean parse(JSONObject json) throws JSONException {
		AccountListBean accountListBean = new AccountListBean();
		
		if (json.has("userCode")) {
			accountListBean.setUserCode(json.getString("userCode"));
        } 
		if (json.has("eventType")) {
			accountListBean.setEventType(json.getString("eventType"));
        } 
		if (json.has("type")) {
			accountListBean.setType(json.getString("type"));
        } 
		if (json.has("eventCode")) {
			accountListBean.setEventCode(json.getString("eventCode"));
        } 
		if (json.has("eventName")) {
			accountListBean.setEventName(json.getString("eventName"));
        } 
		if (json.has("buyType")) {
			accountListBean.setBuyType(json.getString("buyType"));
        } 
		if (json.has("orderId")) {
			accountListBean.setOrderId(json.getString("orderId"));
        } 
		if (json.has("programsOrderId")) {
			accountListBean.setProgramsOrderId(json.getString("programsOrderId"));
        } 
		if (json.has("autoOrderId")) {
			accountListBean.setAutoOrderId(json.getString("autoOrderId"));
        } 
		if (json.has("changeAmount")) {
			accountListBean.setChangeAmount(json.getString("changeAmount"));
        } 
		if (json.has("freezeAmount")) {
			accountListBean.setFreezeAmount(json.getString("freezeAmount"));
        } 
		if (json.has("amountNew")) {
			accountListBean.setAmountNew(json.getString("amountNew"));
        } 
		if (json.has("memo")) {
			accountListBean.setMemo(json.getString("memo"));
        } 
		if (json.has("createTime")) {
			accountListBean.setCreateTime(json.getString("createTime"));
        } 
		return accountListBean;
	}
}
