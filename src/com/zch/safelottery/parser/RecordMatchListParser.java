package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordMatchListBean;
public class RecordMatchListParser extends AbstractParser<RecordMatchListBean>{
	@Override
	public RecordMatchListBean parse(JSONObject json) throws JSONException {
		RecordMatchListBean matchListBean = new RecordMatchListBean();
		
		if (json.has("matchNo")) {
			matchListBean.setMatchNo(json.getString("matchNo"));
        } 
		if (json.has("playCode")) {
			matchListBean.setPlayCode(json.getString("playCode"));
		} 
		if (json.has("openTime")) {
			matchListBean.setOpenTime(json.getString("openTime"));
        } 
		if (json.has("mainTeam")) {
			matchListBean.setMainTeam(json.getString("mainTeam"));
        } 
		if (json.has("guestTeam")) {
			matchListBean.setGuestTeam(json.getString("guestTeam"));
        } 
		if (json.has("mainTeamHalfScore")) {
			matchListBean.setMainTeamHalfScore(json.getString("mainTeamHalfScore"));
        } 
		if (json.has("guestTeamHalfScore")) {
			matchListBean.setGuestTeamHalfScore(json.getString("guestTeamHalfScore"));
        } 
		if (json.has("mainTeamScore")) {
			matchListBean.setMainTeamScore(json.getString("mainTeamScore"));
        } 
		if (json.has("guestTeamScore")) {
			matchListBean.setGuestTeamScore(json.getString("guestTeamScore"));
        } 
		if (json.has("letBall")) {
			matchListBean.setLetBall(json.getString("letBall"));
        } 
		if (json.has("preCast")) {
			matchListBean.setPreCast(json.getString("preCast"));
        } 
		if (json.has("number")) {
			matchListBean.setNumber(json.getString("number"));
        } 
		if (json.has("result")) {
			matchListBean.setResult(json.getString("result"));
        } 
		if (json.has("isSelect")) {
			matchListBean.setIsSelect(json.getString("isSelect"));
        } 
		if (json.has("dan")) {
			matchListBean.setDan(json.getString("dan"));
        } 
		if (json.has("isSelectDan")) {
			matchListBean.setIsSelectDan(json.getString("isSelectDan"));
		} 
		
		
		if (json.has("sp")) {
			matchListBean.setSp(json.getString("sp"));
		} 
		if (json.has("bonusStatus")) {
			matchListBean.setBonusStatus(json.getString("bonusStatus"));
		} 
		if (json.has("match")) {
			matchListBean.setMatch(json.getString("match"));
		} 
		if (json.has("matchName")) {
			matchListBean.setMatchName(json.getString("matchName"));
		} 
		if (json.has("bonusAmount")) {
			matchListBean.setBonusAmount(json.getString("bonusAmount"));
		} 
		if (json.has("multiple")) {
			matchListBean.setMultiple(json.getString("multiple"));
		} 
		return matchListBean;
	}
}
