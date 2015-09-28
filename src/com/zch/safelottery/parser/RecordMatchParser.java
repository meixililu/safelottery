package com.zch.safelottery.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.RecordMatchBean;
import com.zch.safelottery.bean.RecordMatchListBean;
import com.zch.safelottery.util.LogUtil;

public class RecordMatchParser extends AbstractParser<RecordMatchBean> {

	@Override
	public RecordMatchBean parse(JSONObject json) throws JSONException {
		RecordMatchBean matchBean = new RecordMatchBean();

		if (json.has("guoGuan")) {
			matchBean.setGuoGuan(json.getString("guoGuan"));
		}
		if (json.has("isAllEnd")) {
			matchBean.setIsAllEnd(json.getString("isAllEnd"));
		}
		if (json.has("isSingle")) {
			matchBean.setIsSingle(json.getString("isSingle"));
		}
		if (json.has("isSingleFloat")) {
			matchBean.setIsSingleFloat(json.getString("isSingleFloat"));
		}
		if (json.has("matchList")) {
			RecordMatchListParser matchListParser = new RecordMatchListParser();
			String matchstr = json.getString("matchList");
			matchBean.setMatchList((List<RecordMatchListBean>) JsonUtils.parserJsonArray(matchstr, matchListParser));
			for (int i = 0; i < matchBean.getMatchList().size(); i++) {
				LogUtil.DefalutLog(matchBean.getMatchList().get(i).toString());
			}
		}
		if (json.has("ticketList")) {
			RecordMatchListParser matchListParser = new RecordMatchListParser();
			String matchstr = json.getString("ticketList");
			matchBean.setMatchList((List<RecordMatchListBean>) JsonUtils.parserJsonArray(matchstr, matchListParser));
		}
		return matchBean;
	}

}
