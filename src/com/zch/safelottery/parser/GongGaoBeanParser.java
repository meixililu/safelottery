package com.zch.safelottery.parser;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.GongGaoBean;

public class GongGaoBeanParser extends AbstractParser<GongGaoBean> {

	@Override
	public GongGaoBean parse(JSONObject json) throws JSONException {
		GongGaoBean mResult = new GongGaoBean();
		if (json.has("id")) {
			mResult.setId(json.getString("id"));
		}
		if (json.has("type")) {
			mResult.setType(json.getString("type"));
		}
		if (json.has("title")) {
			mResult.setTitle(json.getString("title"));
		}
		return mResult;
	}
	
	public static String getGongGaoTitleList(ArrayList<GongGaoBean> beanList){
		String resultStr = "中彩汇祝您购彩愉快，中大奖！";
		if(beanList != null){
			StringBuilder sb = new StringBuilder();
			int i = 1;
			for(GongGaoBean bean: beanList){
				sb.append(i++);
				sb.append("、");
				sb.append(bean.getTitle());
				sb.append("\t");
			}
			resultStr = sb.toString();
		}
		return resultStr;
	}
}
