package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.FeedListBean;

public class FeedListBeanParser extends AbstractParser<FeedListBean> {

	@Override
	public FeedListBean parse(JSONObject json) throws JSONException {
		FeedListBean mResult = new FeedListBean();
		if (json.has("id")) {
			mResult.setId(json.getString("id"));
		}
		if (json.has("feedType")) {
			mResult.setFeedType(json.getString("feedType"));
		}
		if (json.has("feedInfo")) {
			mResult.setFeedInfo(json.getString("feedInfo"));
		}
		if (json.has("status")) {
			mResult.setStatus(json.getString("status"));
		}
		if (json.has("createTime")) {
			mResult.setCreateTime(json.getString("createTime"));
		}
		if (json.has("acceptor")) {
			mResult.setAcceptor(json.getString("acceptor"));
		}
		if (json.has("handleInfo")) {
			mResult.setHandleInfo(json.getString("handleInfo"));
		}
		if (json.has("acceptTime")) {
			mResult.setAcceptTime(json.getString("acceptTime"));
		}
		if (json.has("assess")) {
			mResult.setAssess(json.getString("assess"));
		}
		return mResult;
	}
}
