package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.PullMsgBean;


public class PullMsgBeanParser extends AbstractParser<PullMsgBean> {

	@Override
	public PullMsgBean parse(JSONObject json) throws JSONException {
		PullMsgBean mResult = new PullMsgBean();
		if (json.has("messgeId")) {
			mResult.setMessgeId(json.getLong("messgeId"));
		}
		if (json.has("private")) {
			mResult.setIsPrivate(json.getInt("private"));
		}
		if (json.has("type")) {
			mResult.setType(json.getString("type"));
		}
		if (json.has("linkUrl")) {
			mResult.setLinkUrl(json.getString("linkUrl"));
		}
		if (json.has("imageUrl")) {
			mResult.setImageUrl(json.getString("imageUrl"));
		}
		if (json.has("title")) {
			mResult.setTitle(json.getString("title"));
		}
		if (json.has("content")) {
			mResult.setContent(json.getString("content"));
		}
		return mResult;
	}
}
