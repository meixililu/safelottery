package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordSubGameListBean;
public class RecordSubGameListParser extends AbstractParser<RecordSubGameListBean>{
	@Override
	public RecordSubGameListBean parse(JSONObject json) throws JSONException {
		RecordSubGameListBean subGameListBean = new RecordSubGameListBean();
		
		if (json.has("sn")) {
			subGameListBean.setSn(json.getString("sn"));
        } 
		if (json.has("leageName")) {
			subGameListBean.setLeageName(json.getString("leageName"));
        } 
		if (json.has("masterName")) {
			subGameListBean.setMasterName(json.getString("masterName"));
        } 
		if (json.has("masterNumber")) {
			subGameListBean.setMasterNumber(json.getString("masterNumber"));
        } 
		if (json.has("masterResult")) {
			subGameListBean.setMasterResult(json.getString("masterResult"));
        } 
		if (json.has("masterSelect")) {
			subGameListBean.setMasterSelect(json.getString("masterSelect"));
        } 
		if (json.has("guestName")) {
			subGameListBean.setGuestName(json.getString("guestName"));
        } 
		if (json.has("guestNumber")) {
			subGameListBean.setGuestNumber(json.getString("guestNumber"));
        } 
		if (json.has("guestResult")) {
			subGameListBean.setGuestResult(json.getString("guestResult"));
        } 
		if (json.has("guestSelect")) {
			subGameListBean.setGuestSelect(json.getString("guestSelect"));
        } 
		if (json.has("startTime")) {
			subGameListBean.setStartTime(json.getString("startTime"));
        } 
		if (json.has("finalScore")) {
			subGameListBean.setFinalScore(json.getString("finalScore"));
        } 
		if (json.has("result")) {
			subGameListBean.setResult(json.getString("result"));
        } 
		if (json.has("dan")) {
			subGameListBean.setDan(json.getString("dan"));
        } 
		if (json.has("number")) {
			subGameListBean.setNumber(json.getString("number"));
        } 
		if (json.has("isSelectDan")) {
			subGameListBean.setIsSelectDan(json.getString("isSelectDan"));
        } 
		if (json.has("isSelect")) {
			subGameListBean.setIsSelect(json.getString("isSelect"));
        } 
		return subGameListBean;
	}
}
