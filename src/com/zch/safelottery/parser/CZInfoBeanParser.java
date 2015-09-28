package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.CZInfoBean;

public class CZInfoBeanParser extends AbstractParser<CZInfoBean> {

	@Override
	public CZInfoBean parse(JSONObject json) throws JSONException {
		CZInfoBean mResult = new CZInfoBean();
		if (json.has("sn")) {
			mResult.setSn(json.getString("sn"));
		}
		if (json.has("matchName")) {
			mResult.setLeague(json.getString("matchName"));
		}
		if (json.has("matchImgPath")) {
			mResult.setIcon_url(json.getString("matchImgPath"));
		}
		if (json.has("mainTeam")) {
			mResult.setHome_team(json.getString("mainTeam"));
		}
		if (json.has("guestTeam")) {
			mResult.setGuest_team(json.getString("guestTeam"));
		}
		if (json.has("endTime")) {
			mResult.setPlaying_time(json.getString("endTime"));
		}
		if (json.has("avgOdds")) {
			mResult.setAvgOdds(json.getString("avgOdds"));
		}
		return mResult;
	}
}
