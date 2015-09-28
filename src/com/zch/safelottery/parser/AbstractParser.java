package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.SafelotteryType;


public abstract class AbstractParser<T extends SafelotteryType> implements Parser<T> {

	public abstract T parse(JSONObject json) throws JSONException;

}
