package com.zch.safelottery.parser;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.bean.JZMatchListBean;
import com.zch.safelottery.util.TimeUtils;

public class JZMatchListBeanParser extends AbstractParser<JZMatchListBean> {

	@Override
	public JZMatchListBean parse(JSONObject json) throws JSONException {
		JZMatchListBean mResult = new JZMatchListBean();
		if (json.has("date")) {
			mResult.setDate(json.getString("date"));
		}
		if (json.has("week")) {
			mResult.setWeek(json.getString("week"));
		}
		if (json.has("matchTotal")) {
			mResult.setMatchTotal(json.getInt("matchTotal"));
		}
		if (json.has("match")) {
			String jsonArray = json.getString("match");
			ArrayList<JZMatchBean> mJZMatchBeanList = (ArrayList<JZMatchBean>) JsonUtils.parserJsonArray(jsonArray,new JZMatchBeanParser());
			mResult.setMatchList( mJZMatchBeanList );
			mResult.setStatus(1);
			String week = TimeUtils.customFormatDate(mResult.getDate(), TimeUtils.Day2Format, TimeUtils.WeekFormat);
			if(mJZMatchBeanList != null){
				mResult.setSection( week + "\t" + mJZMatchBeanList.size() + "场比赛可投" );
			}else{
				mResult.setSection( week + "\t" + 0 + "场比赛可投" );
			}
		}
		return mResult;
	}
}

	
