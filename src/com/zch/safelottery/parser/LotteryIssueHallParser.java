package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.LotteryIssueHallBean;

public class LotteryIssueHallParser extends AbstractParser<LotteryIssueHallBean>{

	@Override
	public LotteryIssueHallBean parse(JSONObject json) throws JSONException {
		LotteryIssueHallBean mBean = new LotteryIssueHallBean();
		if(json.has("lotteryId")) mBean.setLotteryId(json.getString("lotteryId"));
		if(json.has("issue")) mBean.setIssue(json.getString("issue"));
		if(json.has("bonusNumber")) mBean.setBonusNumber(json.getString("bonusNumber"));
		if(json.has("bonusTime")) mBean.setBonusTime(json.getString("bonusTime"));
		
		return mBean;
	}

}
