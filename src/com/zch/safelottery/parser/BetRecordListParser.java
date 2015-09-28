package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.BetRecordListBean;
public class BetRecordListParser extends AbstractParser<BetRecordListBean>{

	@Override
	public BetRecordListBean parse(JSONObject json) throws JSONException {
		BetRecordListBean betRecordListBean = new BetRecordListBean();
		
		if (json.has("userCode")) {
			betRecordListBean.setUserCode(json.getString("userCode"));
        } 
		if (json.has("orderId")) {
			betRecordListBean.setOrderId(json.getString("orderId"));
        } 
		if (json.has("programsOrderId")) {
			betRecordListBean.setProgramsOrderId(json.getString("programsOrderId"));
        } 
		if (json.has("autoOrderId")) {
			betRecordListBean.setAutoOrderId(json.getString("autoOrderId"));
        } 
		if (json.has("orderStatus")) {
			betRecordListBean.setOrderStatus(json.getString("orderStatus"));
        } 
		if (json.has("bonusStatus")) {
			betRecordListBean.setBonusStatus(json.getString("bonusStatus"));
        } 
		if (json.has("orderType")) {
			betRecordListBean.setOrderType(json.getString("orderType"));
        } 
		if (json.has("orderAmount")) {
			betRecordListBean.setOrderAmount(json.getString("orderAmount"));
        } 
		if (json.has("bonusAmount")) {
			betRecordListBean.setBonusAmount(json.getString("bonusAmount"));
        } 
		if (json.has("fixBonusAmount")) {
			betRecordListBean.setFixBonusAmount(json.getString("fixBonusAmount"));
        } 
		if (json.has("programsAmount")) {
			betRecordListBean.setProgramsAmount(json.getString("programsAmount"));
        } 
		if (json.has("lotteryId")) {
			betRecordListBean.setLotteryId(json.getString("lotteryId"));
        } 
		if (json.has("playId")) {
			betRecordListBean.setPlayId(json.getString("playId"));
        } 
		if (json.has("pollId")) {
			betRecordListBean.setPollId(json.getString("pollId"));
        } 
		if (json.has("issue")) {
			betRecordListBean.setIssue(json.getString("issue"));
        } 
		if (json.has("sendStatus")) {
			betRecordListBean.setSendStatus(json.getString("sendStatus"));
        } 
		if (json.has("programsStatus")) {
			betRecordListBean.setProgramsStatus(json.getString("programsStatus"));
        } 
		if (json.has("buyType")) {
			betRecordListBean.setBuyType(json.getString("buyType"));
        } 
		if (json.has("createTime")) {
			betRecordListBean.setCreateTime(json.getString("createTime"));
        } 
		if (json.has("bonusToAccount")) {
			betRecordListBean.setBonusToAccount(json.getString("bonusToAccount"));
        } 
		return betRecordListBean;
	}
}
