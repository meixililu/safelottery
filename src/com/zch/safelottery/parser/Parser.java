package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.SafelotteryType;

/**
 * @author Messi
 *
 * @param <T>
 */
public interface Parser<T extends SafelotteryType> {

	public abstract T parse(JSONObject json) throws JSONException;
}
