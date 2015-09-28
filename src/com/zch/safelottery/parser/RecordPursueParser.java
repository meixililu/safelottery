package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordPursueBean;

public class RecordPursueParser extends AbstractParser<RecordPursueBean>{

	@Override
	public RecordPursueBean parse(JSONObject json) throws JSONException {
		RecordPursueBean mBean = new RecordPursueBean();
		if(json.has("autoOrderId")) mBean.setAutoOrderId(json.getString("autoOrderId"));
		if(json.has("lotteryId")) mBean.setLotteryId(json.getString("lotteryId"));
		if(json.has("playId")) mBean.setPlayId(json.getString("playId"));
		if(json.has("playName")) mBean.setPlayName(json.getString("playName"));
		if(json.has("totalIssue")) mBean.setTotalIssue(json.getString("totalIssue"));
		if(json.has("successIssue")) mBean.setSuccessIssue(json.getString("successIssue"));
		if(json.has("failureIssue")) mBean.setFailureIssue(json.getString("failureIssue"));
		if(json.has("cancelIssue")) mBean.setCancelIssue(json.getString("cancelIssue"));
		if(json.has("completeAmount")) mBean.setCompleteAmount(json.getString("completeAmount"));
		if(json.has("orderStatus")) mBean.setOrderStatus(json.getString("orderStatus"));
		if(json.has("bonusStatus")) mBean.setBonusStatus(json.getString("bonusStatus"));
		if(json.has("orderAmount")) mBean.setOrderAmount(json.getString("orderAmount"));
		if(json.has("bonusAmount")) mBean.setBonusAmount(json.getString("bonusAmount"));
		if(json.has("fixBonusAmount")) mBean.setFixBonusAmount(json.getString("fixBonusAmount"));
		if(json.has("winStop")) mBean.setWinStop(json.getString("winStop"));
		if(json.has("winAmount")) mBean.setWinAmount(json.getString("winAmount"));
		if(json.has("createTime")) mBean.setCreateTime(json.getString("createTime"));
		if(json.has("acceptTime")) mBean.setAcceptTime(json.getString("acceptTime"));
		if(json.has("multiple")) mBean.setMultiple(json.getString("multiple"));
		if(json.has("item")) mBean.setItem(json.getString("item"));
		if(json.has("numberInfo")) mBean.setNumberInfo(json.getString("numberInfo"));
		
		return mBean;
	}

}
