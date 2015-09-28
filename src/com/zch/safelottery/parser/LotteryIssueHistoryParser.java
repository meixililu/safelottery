package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.LotteryIssueHistoryBean;

public class LotteryIssueHistoryParser extends AbstractParser<LotteryIssueHistoryBean>{

	@Override
	public LotteryIssueHistoryBean parse(JSONObject json) throws JSONException {
		LotteryIssueHistoryBean mBean = new LotteryIssueHistoryBean();
		if(json.has("name")){
			mBean.setName(json.getString("name"));
		}
		if(json.has("status")){
			mBean.setStatus(json.getString("status"));
		}
		if(json.has("sendStatus")){
			mBean.setSendStatus(json.getString("sendStatus"));
		}
		if(json.has("bonusStatus")){
			mBean.setBonusStatus(json.getString("bonusStatus"));
		}
		if(json.has("startTime")){
			mBean.setStartTime(json.getString("startTime"));
		}
		if(json.has("simplexTime")){
			mBean.setSimplexTime(json.getString("simplexTime"));
		}
		if(json.has("duplexTime")){
			mBean.setDuplexTime(json.getString("duplexTime"));
		}
		if(json.has("endTime")){
			mBean.setEndTime(json.getString("endTime"));
		}
		if(json.has("bonusTime")){
			mBean.setBonusTime(json.getString("bonusTime"));
		}
		if(json.has("prizePool")){
			mBean.setPrizePool(json.getString("prizePool"));
		}
		if(json.has("convertBonusTime")){
			mBean.setConvertBonusTime(json.getString("convertBonusTime"));
		}
		if(json.has("bonusNumber")){
			mBean.setBonusNumber(json.getString("bonusNumber"));
		}
		if(json.has("globalSaleTotal")){
			mBean.setGlobalSaleTotal(json.getString("globalSaleTotal"));
		}
		if(json.has("backup1")){
			mBean.setBackup1(json.getString("backup1"));
		}
		if(json.has("backup2")){
			mBean.setBackup2(json.getString("backup2"));
		}
		if(json.has("backup3")){
			mBean.setBackup3(json.getString("backup3"));
		}
		
		return mBean;
	}

	
}
