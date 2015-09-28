package com.zch.safelottery.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordNumberInforBean;
import com.zch.safelottery.bean.RecordProgramsListBean;

public class RecordProgramsListParser extends AbstractParser<RecordProgramsListBean>{
	@Override
	public RecordProgramsListBean parse(JSONObject json) throws JSONException {
		RecordProgramsListBean programsListBean = new RecordProgramsListBean();
		
		if (json.has("userCode")) {
			programsListBean.setUserCode(json.getString("userCode"));
        } 
		if (json.has("presentedUserCode")) {
			programsListBean.setPresentedUserCode(json.getString("presentedUserCode"));
        } 
		if (json.has("programsOrderId")) {
			programsListBean.setProgramsOrderId(json.getString("programsOrderId"));
        } 
		if (json.has("lotteryId")) {
			programsListBean.setLotteryId(json.getString("lotteryId"));
        } 
		if (json.has("playId")) {
			programsListBean.setPlayId(json.getString("playId"));
        } 
		if (json.has("playName")) {
			programsListBean.setPlayName(json.getString("playName"));
        } 
		if (json.has("pollId")) {
			programsListBean.setPollId(json.getString("pollId"));
        } 
		if (json.has("issue")) {
			programsListBean.setIssue(json.getString("issue"));
        } 
		if (json.has("buyType")) {
			programsListBean.setBuyType(json.getString("buyType"));
        } 
		if (json.has("numberInfo")) {
			programsListBean.setNumberInfo((List<RecordNumberInforBean>)JsonUtils.parserJsonArray(json.getString("numberInfo"),new RecordNumberInforParser()));
        } 
		if (json.has("orderStatus")) {
			programsListBean.setOrderStatus(json.getString("orderStatus"));
        } 
		if (json.has("sendStatus")) {
			programsListBean.setSendStatus(json.getString("sendStatus"));
        } 
		if (json.has("bonusStatus")) {
			programsListBean.setBonusStatus(json.getString("bonusStatus"));
        } 
		if (json.has("privacy")) {
			programsListBean.setPrivacy(json.getString("privacy"));
        } 
		if (json.has("item")) {
			programsListBean.setItem(json.getString("item"));
        } 
		if (json.has("multiple")) {
			programsListBean.setMultiple(json.getString("multiple"));
        } 
		if (json.has("totalWere")) {
			programsListBean.setTotalWere(json.getString("totalWere"));
        } 
		if (json.has("buyWere")) {
			programsListBean.setBuyWere(json.getString("buyWere"));
        } 
		if (json.has("minWere")) {
			programsListBean.setMinWere(json.getString("minWere"));
        } 
		if (json.has("lastWere")) {
			programsListBean.setLastWere(json.getString("lastWere"));
        } 
		if (json.has("commission")) {
			programsListBean.setCommission(json.getString("commission"));
			
        } if (json.has("description")) {
			programsListBean.setDescription(json.getString("description"));
        } 
        if (json.has("bonusAmount")) {
			programsListBean.setBonusAmount(json.getString("bonusAmount"));
        } 
        if (json.has("fixBonusAmount")) {
			programsListBean.setFixBonusAmount(json.getString("fixBonusAmount"));
        } 
        if (json.has("ticketCount")) {
			programsListBean.setTicketCount(json.getString("ticketCount"));
        } 
        if (json.has("bonusTicket")) {
			programsListBean.setBonusTicket(json.getString("bonusTicket"));
        } 
        if (json.has("successTicket")) {
			programsListBean.setSuccessTicket(json.getString("successTicket"));
        } 
        if (json.has("failureTicket")) {
			programsListBean.setFailureTicket(json.getString("failureTicket"));
        } 
        if (json.has("createTime")) {
			programsListBean.setCreateTime(json.getString("createTime"));
        } 
        if (json.has("isTop")) {
			programsListBean.setIsTop(json.getString("isTop"));
        } 
        if (json.has("isUpload")) {
			programsListBean.setIsUpload(json.getString("isUpload"));
        } 
        if (json.has("bigBonus")) {
			programsListBean.setBigBonus(json.getString("bigBonus"));
        } 
        if (json.has("bonusToAccount")) {
			programsListBean.setBonusToAccount(json.getString("bonusToAccount"));
        } 
        if (json.has("filePath")) {
			programsListBean.setFilePath(json.getString("filePath"));
        }   
        if (json.has("renGouCount")) {
			programsListBean.setRenGouCount(json.getString("renGouCount"));
        } 
        
        if (json.has("isCancelPrograms")) {
        	programsListBean.setIsCancelPrograms(json.getString("isCancelPrograms"));
        } 
        if (json.has("buyPercent")) {
        	programsListBean.setBuyPercent(json.getString("buyPercent"));
        } 
        if (json.has("lastPercent")) {
        	programsListBean.setLastPercent(json.getString("lastPercent"));
        } 
        if (json.has("commissionAmount")) {
        	programsListBean.setCommissionAmount(json.getString("commissionAmount"));
        } 
        if (json.has("itemBonusAmount")) {
        	programsListBean.setItemBonusAmount(json.getString("itemBonusAmount"));
        } 
        if (json.has("bonusClass")) {
        	programsListBean.setBonusClass(json.getString("bonusClass"));
        } 
        if (json.has("showNumber")) {
        	programsListBean.setShowNumber(json.getString("showNumber"));
        } 
        if (json.has("bigNumberInfo")) {
        	programsListBean.setBigNumberInfo(json.getString("bigNumberInfo"));
        } 
		return programsListBean;
	}
}
