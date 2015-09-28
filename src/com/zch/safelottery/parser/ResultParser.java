package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.combinebean.Result;

public class ResultParser extends AbstractParser<Result> {

	@Override
	public Result parse(JSONObject json) throws JSONException {
		Result mResult = new Result();
		if (json.has("result")) {
			mResult.setResult(json.getString("result"));
		}
		if (json.has("code")) {
			mResult.setCode(json.getString("code"));
		}
		if (json.has("msg")) {
			mResult.setMsg(json.getString("msg"));
		}
		return mResult;
	}
}
