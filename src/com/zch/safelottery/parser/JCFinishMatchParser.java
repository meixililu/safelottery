package com.zch.safelottery.parser;


import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.zch.safelottery.bean.JZMatchBean;

public class JCFinishMatchParser extends AbstractParser<JZMatchBean> {

	@Override
	public JZMatchBean parse(JSONObject json) throws JSONException {
		JZMatchBean mResult = new JZMatchBean();
		if (json.has("week")) {
			mResult.setWeek(json.getString("week"));
		}
		if (json.has("sn")) {
			mResult.setSn(json.getString("sn"));
		}
		if (json.has("matchName")) {
			mResult.setMatchName(json.getString("matchName"));
		}
		if (json.has("imgPath")) {
			mResult.setImgPath(json.getString("matchImgPath"));
		}
		if (json.has("mainTeam")) {
			mResult.setMainTeam(json.getString("mainTeam"));
		}
		if (json.has("guestTeam")) {
			mResult.setGuestTeam(json.getString("guestTeam"));
		}
		if (json.has("letBall")) {
			String rangqiu = json.getString("letBall");
			if(TextUtils.isEmpty(rangqiu)){
				rangqiu = "0";
			}
			mResult.setLetBall(rangqiu);
		}
		if (json.has("endTime")) {
			mResult.setEndTime(json.getString("endTime"));
		}
		if (json.has("mainTeamHalfScore")) {
			mResult.setMainTeamHalfScore(json.getString("mainTeamHalfScore"));
		}
		if (json.has("guestTeamHalfScore")) {
			mResult.setGuestTeamHalfScore(json.getString("guestTeamHalfScore"));
		}
		if (json.has("mainTeamScore")) {
			String mainTeamScore = json.getString("mainTeamScore");
			mResult.setMainTeamScore(mainTeamScore.equals("null") ? "":mainTeamScore);
		}
		if (json.has("guestTeamScore")) {
			String guestTeamScore = json.getString("guestTeamScore");
			mResult.setGuestTeamScore(guestTeamScore.equals("null") ? "":guestTeamScore);
		}
		if (json.has("bqcspfResult")) {
			String bqcspfResult = json.getString("bqcspfResult");
			mResult.setBqcspfResult(bqcspfResult.equals("null") ? "":bqcspfResult);
		}
		
		/**竞彩篮球**/
		if (json.has("rfshResult")) {
			String bqcspfResult = json.getString("rfshResult");
			mResult.setRfshResult(bqcspfResult.equals("null") ? "":bqcspfResult);
		}
		if (json.has("dxfResult")) {
			String bqcspfResult = json.getString("dxfResult");
			mResult.setDxfResult(bqcspfResult.equals("null") ? "":bqcspfResult);
		}
		if (json.has("sfcResult")) {
			String bqcspfResult = json.getString("sfcResult");
			mResult.setSfcResult(bqcspfResult.equals("null") ? "":bqcspfResult);
		}
		if (json.has("mainTeamImgPath")) {
			mResult.setMainTeamImgPath(json.getString("mainTeamImgPath"));
		}
		if (json.has("guestTeamImgPath")) {
			mResult.setGuestTeamImgPath(json.getString("guestTeamImgPath"));
		}
		if (json.has("preCast")) {
			mResult.setPreCast(json.getString("preCast"));
		}
		return mResult;
	}
}
