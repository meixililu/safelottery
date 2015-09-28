package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.combinebean.CbHallListItemBean;

public class CbHallListItemParser extends AbstractParser<CbHallListItemBean> {

	@Override
	public CbHallListItemBean parse(JSONObject json) throws JSONException {
		CbHallListItemBean mBean = new CbHallListItemBean();
		if(json.has("userCode")) mBean.setUserCode(json.getString("userCode"));
		if(json.has("userName")) mBean.setUserName(json.getString("userName"));
		if(json.has("nickName")) mBean.setNickName(json.getString("nickName"));
		if(json.has("programsOrderId")) mBean.setProgramsOrderId(json.getString("programsOrderId"));
		if(json.has("lotteryId")) mBean.setLotteryId(json.getString("lotteryId"));
		if(json.has("lotteryName")) mBean.setLotteryName(json.getString("lotteryName"));
		if(json.has("playId")) mBean.setPlayId(json.getString("playId"));
		if(json.has("playName")) mBean.setPlayName(json.getString("playName"));
		if(json.has("pollId")) mBean.setPollId(json.getString("pollId"));
		if(json.has("issue")) mBean.setIssue(json.getString("issue"));
		if(json.has("issueStatus")) mBean.setIssueStatus(json.getString("issueStatus"));
		if(json.has("endTime")) mBean.setEndTime(json.getString("endTime"));
		if(json.has("orderStatus")) mBean.setOrderStatus(json.getString("orderStatus"));
		if(json.has("item")) mBean.setItem(json.getString("item"));
		if(json.has("multiple")) mBean.setMultiple(json.getString("multiple"));
		if(json.has("totalWere")) mBean.setTotalWere(json.getString("totalWere"));
		if(json.has("buyWere")) mBean.setBuyWere(json.getString("buyWere"));
		if(json.has("lastWere")) mBean.setLastWere(json.getString("lastWere"));
		if(json.has("buyPercent")) mBean.setBuyPercent(json.getString("buyPercent"));
		if(json.has("lastPercent")) mBean.setLastPercent(json.getString("lastPercent"));
		if(json.has("commission")) mBean.setCommission(json.getString("commission"));
		if(json.has("isTop")) mBean.setIsTop(json.getString("isTop"));
		if(json.has("createTime")) mBean.setCreateTime(json.getString("createTime"));
		return mBean;
	}

}
