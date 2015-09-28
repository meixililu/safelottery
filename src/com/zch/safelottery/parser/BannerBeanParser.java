package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.BannerBean;

public class BannerBeanParser extends AbstractParser<BannerBean>{

	@Override
	public BannerBean parse(JSONObject json)
			throws JSONException {
		BannerBean mBean = new BannerBean();
		if(json.has("cover")){
			mBean.setImageUrl(json.getString("cover"));
		}
		if(json.has("simple_title")){
			mBean.setSimple_title(json.getString("simple_title"));
		}
		if(json.has("id")){
			mBean.setId(json.getString("id"));
		}
		if(json.has("storePosition")){
			mBean.setStorePosition(json.getInt("storePosition"));
		}
		if(json.has("filePath")){
			mBean.setFilePath(json.getString("filePath"));
		}
		if(json.has("article_category_id")){
			mBean.setArticle_category_id(json.getString("article_category_id"));
		}
		return mBean;
	}
	
	
}
