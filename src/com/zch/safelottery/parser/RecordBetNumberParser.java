package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordBetNumberBean;

public class RecordBetNumberParser extends AbstractParser<RecordBetNumberBean>{

	@Override
	public RecordBetNumberBean parse(JSONObject json) throws JSONException {
		RecordBetNumberBean mBean = new RecordBetNumberBean();
		if(json.has("number")) mBean.setNumber(json.getString("number"));
		if(json.has("playCode")) mBean.setPlayCode(json.getString("playCode"));
		if(json.has("pollCode")) mBean.setPollCode(json.getString("pollCode"));
		if(json.has("item")) mBean.setItem(json.getString("item"));
		if(json.has("sequence")) mBean.setSequence(json.getString("sequence"));
		return mBean;
	}
}
