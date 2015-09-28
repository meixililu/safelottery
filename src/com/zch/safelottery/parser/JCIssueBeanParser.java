package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.JCIssueBean;

public class JCIssueBeanParser extends AbstractParser<JCIssueBean> {

	@Override
	public JCIssueBean parse(JSONObject json) throws JSONException {
		JCIssueBean mResult = new JCIssueBean();
		if (json.has("matchCount")) {
			mResult.setMatchCount(json.getString("matchCount"));
		}
		if (json.has("issue")) {
			mResult.setIssue(json.getString("issue"));
		}
		return mResult;
	}
}
