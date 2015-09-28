package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordNumberInforBean;

public class RecordNumberInforParser extends AbstractParser<RecordNumberInforBean> {

	@Override
	public RecordNumberInforBean parse(JSONObject json) throws JSONException {
		RecordNumberInforBean numberInforBean = new RecordNumberInforBean();
		if (json.has("sequence")) {
			numberInforBean.setSequence(json.getString("sequence"));
		}
		if (json.has("playCode")){
			numberInforBean.setPlayCode(json.getString("playCode"));
		}
		if (json.has("pollCode")){
			numberInforBean.setPollCode(json.getString("pollCode"));
		}
		if (json.has("number")){
			numberInforBean.setNumber(json.getString("number"));
		}
		if (json.has("item")){
			numberInforBean.setItem(json.getString("item"));
		}
		if (json.has("numberFormat")){
			numberInforBean.setNumberFormat(json.getString("numberFormat"));
		}
		if (json.has("uploadMatch")){
			numberInforBean.setUploadMatch(json.getString("uploadMatch"));
		}
		return numberInforBean;
	}
}
