package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.LoadingBgAndSalesResultBean;

public class LoadingBgAndSalesResultParser extends AbstractParser<LoadingBgAndSalesResultBean>{

	@Override
	public LoadingBgAndSalesResultBean parse(JSONObject json)
			throws JSONException {
		LoadingBgAndSalesResultBean mBean = new LoadingBgAndSalesResultBean();
		if(json.has("platform")){
			mBean.setPlatform(json.getString("platform"));
		}
		if(json.has("name")){
			mBean.setName(json.getString("name"));
		}
		if(json.has("position")){
			mBean.setPosition(json.getString("position"));
		}
		if(json.has("startTime")){
			mBean.setStartTime(json.getString("startTime"));
		}
		if(json.has("endTime")){
			mBean.setEndTime(json.getString("endTime"));
		}
		if(json.has("status")){
			mBean.setStatus(json.getString("status"));
		}
		if(json.has("url")){
			mBean.setUrl(json.getString("url"));
		}
		if(json.has("note")){
			mBean.setNote(json.getString("note"));
		}
		if(json.has("imageUrl")){
			mBean.setImageUrl(json.getString("imageUrl"));
		}
		if(json.has("updateTime")){
			mBean.setUpdateTime(json.getString("updateTime"));
		}
		if(json.has("isUrl")){
			mBean.setIsUrl(json.getString("isUrl"));
		}
		if(json.has("storePosition")){
			mBean.setStorePosition(json.getInt("storePosition"));
		}
		if(json.has("filePath")){
			mBean.setFilePath(json.getString("filePath"));
		}
		return mBean;
	}
	
	
}
