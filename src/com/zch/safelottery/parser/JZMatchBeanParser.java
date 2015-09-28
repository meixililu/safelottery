package com.zch.safelottery.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.zch.safelottery.bean.JZMatchBean;

public class JZMatchBeanParser extends AbstractParser<JZMatchBean> {

	@Override
	public JZMatchBean parse(JSONObject json) throws JSONException {
		JZMatchBean mResult = new JZMatchBean();
		if (json.has("sn")) {
			mResult.setSn(json.getString("sn"));
		}
		if (json.has("issue")) {
			mResult.setIssue(json.getString("issue"));
		}
		if (json.has("week")) {
			mResult.setWeek(json.getString("week"));
		}
		if (json.has("playId")) {
			mResult.setPlayId(json.getString("playId"));
		}
		if (json.has("pollId")) {
			mResult.setPollId(json.getString("pollId"));
		}
		if (json.has("endOperator")) {
			mResult.setEndOperator(json.getString("endOperator"));
		}
		if (json.has("matchName")) {
			mResult.setMatchName(json.getString("matchName"));
		}
		// if (json.has("matchImgPath")) {
		// mResult.setImgPath(json.getString("matchImgPath"));
		// }
		if (json.has("mainTeam")) {
			mResult.setMainTeam(json.getString("mainTeam"));
		}
		if (json.has("guestTeam")) {
			mResult.setGuestTeam(json.getString("guestTeam"));
		}
		if (json.has("letBall")) {
			String rangqiu = json.getString("letBall");
			if (TextUtils.isEmpty(rangqiu)) {
				rangqiu = "0";
			}
			mResult.setLetBall(rangqiu);
		}
		if (json.has("endTime")) {
			mResult.setEndTime(json.getString("endTime"));
		}
		if (json.has("endDanShiTime")) {
			mResult.setEndDanShiTime(json.getString("endDanShiTime"));
		}
		if (json.has("endFuShiTime")) {
			mResult.setEndFuShiTime(json.getString("endFuShiTime"));
		}
		if (json.has("mainTeamHalfScore")) {
			mResult.setMainTeamHalfScore(json.getString("mainTeamHalfScore"));
		}
		if (json.has("guestTeamHalfScore")) {
			mResult.setGuestTeamHalfScore(json.getString("guestTeamHalfScore"));
		}
		if (json.has("mainTeamScore")) {
			mResult.setMainTeamScore(json.getString("mainTeamScore"));
		}
		if (json.has("guestTeamScore")) {
			mResult.setGuestTeamScore(json.getString("guestTeamScore"));
		}
		if (json.has("sp")) {
			if (json.getString("sp").startsWith("[")) {
				JSONArray jsonArray = json.getJSONArray("sp");
				StringBuilder sb = new StringBuilder();
				int len = jsonArray.length();
				for (int i = 0; i < len; i++) {
					sb.append((String) jsonArray.get(i));
					sb.append("#");
				}
				int pos = sb.lastIndexOf("#");
				if (pos > 0) {
					sb.deleteCharAt(pos);
				}
				mResult.setSp(sb.toString());
			} else {
				mResult.setSp(json.getString("sp"));
			}

		}

		if (json.has("sp_spf")) {
			JSONArray jsonArray = json.getJSONArray("sp_spf");
			mResult.setSpfJcArray(jsonArray);
		}

		if (json.has("sp_rqspf")) {
			JSONArray jsonArray = json.getJSONArray("sp_rqspf");
			mResult.setRqspfJcArray(jsonArray);
		}

		if (json.has("sp_bf")) {
			JSONArray jsonArray = json.getJSONArray("sp_bf");
			mResult.setQcbfJcArray(jsonArray);
		}

		if (json.has("sp_zjqs")) {
			JSONArray jsonArray = json.getJSONArray("sp_zjqs");
			mResult.setZjqsJcArray(jsonArray);
		}

		if (json.has("sp_bqc")) {
			JSONArray jsonArray = json.getJSONArray("sp_bqc");
			mResult.setBqcJcArray(jsonArray);
		}

		if (json.has("winOrNegaSp")) {
			mResult.setWinOrNegaSp(json.getString("winOrNegaSp"));
		}
		if (json.has("totalGoalSp")) {
			mResult.setTotalGoalSp(json.getString("totalGoalSp"));
		}
		if (json.has("halfCourtSp")) {
			mResult.setHalfCourtSp(json.getString("halfCourtSp"));
		}
		if (json.has("scoreSp")) {
			mResult.setScoreSp(json.getString("scoreSp"));
		}

		/**竞彩篮球**/
		if (json.has("mainTeamImgPath")) {
			mResult.setMainTeamImgPath(json.getString("mainTeamImgPath"));
		}
		if (json.has("guestTeamImgPath")) {
			mResult.setGuestTeamImgPath(json.getString("guestTeamImgPath"));
		}
		if (json.has("preCast")) {
			mResult.setPreCast(json.getString("preCast"));
		}
		if (json.has("letWinOrNegaSp")) {
			mResult.setLetWinOrNegaSp(json.getString("letWinOrNegaSp"));
		}
		if (json.has("bigOrLittleSp")) {
			mResult.setBigOrLittleSp(json.getString("bigOrLittleSp"));
		}
		if (json.has("winNegaDiffSp")) {
			mResult.setWinNegaDiffSp(json.getString("winNegaDiffSp"));
		}

		if (json.has("avgOdds")) {
			mResult.setAvgOdds(json.getString("avgOdds"));
		}
		if (json.has("name")) {
			mResult.setName(json.getString("name"));
		}
		if (json.has("league")) {
			mResult.setLeague(json.getString("league"));
		}
		return mResult;
	}
}
