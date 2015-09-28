package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.NoticeBean;

public class MessageDataParser extends AbstractParser<NoticeBean> {

	public NoticeBean parse(JSONObject json) throws JSONException {
		NoticeBean noticeBean = new NoticeBean();
		if (json.has("id")) {
			noticeBean.setId(json.getString("id"));
		}
		if (json.has("title")) {
			noticeBean.setTitle(json.getString("title"));
		}
		if (json.has("isReaded")) {
			noticeBean.setIsReaded(json.getString("isReaded"));
		}
		if (json.has("abstract")) {
			noticeBean.setAbstract(json.getString("abstract"));
		}
		if (json.has("context")) {
			noticeBean.setContext(json.getString("context"));
		}
		if (json.has("article_category_id")) {
			noticeBean.setArticle_category_id(json.getString("article_category_id"));
		}
		if (json.has("article_category")) {
			noticeBean.setArticle_category(json.getString("article_category"));
		}
		if (json.has("pub_datetime")) {
			noticeBean.setPub_datetime(json.getString("pub_datetime"));
		}
		if (json.has("expiration_time")) {
			noticeBean.setExpiration_time(json.getString("expiration_time"));
		}
		if (json.has("startTime")) {
			noticeBean.setStartTime(json.getString("startTime"));
		}
		if (json.has("endTime")) {
			noticeBean.setEndTime(json.getString("endTime"));
		}
		if (json.has("cover")) {
			noticeBean.setCover(json.getString("cover"));
		}
		if(json.has("simple_title")){
			noticeBean.setSimple_title(json.getString("simple_title"));
		}
		if (json.has("description")) {
			noticeBean.setDescription(json.getString("description"));
		}
		if (json.has("mtype")) {
			noticeBean.setType(json.getString("mtype"));
		}
		return noticeBean;
	}

}
