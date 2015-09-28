package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordUserInfoBean;

public class RecordUserInfoParser extends AbstractParser<RecordUserInfoBean>{

	@Override
	public RecordUserInfoBean parse(JSONObject json) throws JSONException {
		RecordUserInfoBean mBean = new RecordUserInfoBean();
		if(json.has("userCode")) mBean.setUserCode(json.getString("userCode"));
		if(json.has("userName")) mBean.setUserName(json.getString("userName"));
		if(json.has("bonusCount")) mBean.setBonusCount(json.getString("bonusCount"));
		if(json.has("bonusTotal")) mBean.setBonusTotal(json.getString("bonusTotal"));
		if(json.has("sponsorOrderAmount")) mBean.setSponsorOrderAmount(json.getString("sponsorOrderAmount"));
		if(json.has("sponsorBonusAmount")) mBean.setSponsorBonusAmount(json.getString("sponsorBonusAmount"));
		if(json.has("loginOrderAmount")) mBean.setLoginOrderAmount(json.getString("loginOrderAmount"));
		if(json.has("loginBonusAmount")) mBean.setLoginBonusAmount(json.getString("loginBonusAmount"));
		if(json.has("presentedUserName")) mBean.setPresentedUserName(json.getString("presentedUserName"));
		if(json.has("presentMobile")) mBean.setPresentMobile(json.getString("presentMobile"));
		return mBean;
	}
}
