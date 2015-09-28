package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordMainIssueListBean;

public class RecordMainIssueListParser extends AbstractParser<RecordMainIssueListBean>{

	@Override
	public RecordMainIssueListBean parse(JSONObject json) throws JSONException {
		RecordMainIssueListBean mainIssueListBean = new RecordMainIssueListBean();
		
		if (json.has("bonusNumber")) {
			mainIssueListBean.setBonusNumber(json.getString("bonusNumber"));
        } 
		if (json.has("startTime")) {
			mainIssueListBean.setStartTime(json.getString("startTime"));
        } 
		if (json.has("endTime")) {
			mainIssueListBean.setEndTime(json.getString("endTime"));
        } 
		if (json.has("simplexTime")) {
			mainIssueListBean.setSimplexTime(json.getString("simplexTime"));
        } 
		if (json.has("duplexTime")) {
			mainIssueListBean.setDuplexTime(json.getString("duplexTime"));
        } 
		if (json.has("bonusTime")) {
			mainIssueListBean.setBonusTime(json.getString("bonusTime"));
        } 
		if (json.has("bonusStatus")) {
			mainIssueListBean.setBonusStatus(json.getString("bonusStatus"));
        } 
		if (json.has("status")) {
			mainIssueListBean.setStatus(json.getString("status"));
        } 
		return mainIssueListBean;
	}

}
