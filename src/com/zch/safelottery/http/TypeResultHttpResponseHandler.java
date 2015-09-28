/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.zch.safelottery.http;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.text.TextUtils;

import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.parser.ResultParser;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.VersionUpdateUtils;

/**
 * Used to intercept and handle the responses from requests made using {@link AsyncHttpClient}, with
 * automatic parsing into a {@link JSONObject} or {@link JSONArray}. <p>&nbsp;</p> This class is
 * designed to be passed to get, post, put and delete requests with the {@link #onSuccess(int,
 * org.apache.http.Header[], org.json.JSONArray)} or {@link #onSuccess(int,
 * org.apache.http.Header[], org.json.JSONObject)} methods anonymously overridden. <p>&nbsp;</p>
 * Additionally, you can override the other event methods from the parent class.
 */
public abstract class TypeResultHttpResponseHandler extends TextHttpResponseHandler {

	private boolean isToastError;
	private Context mContext;

	/**
	 * Creates new JsonHttpResponseHandler, with Json String encoding UTF-8
	 * @param parser 结果解析器
	 * @param isToastError  是否自动toast错误，true为自动显示，false为不显示；
	 */
	public TypeResultHttpResponseHandler(Context mContext, boolean isToastError) {
		super(DEFAULT_CHARSET);
		this.isToastError = isToastError;
		this.mContext = mContext;
	}

	/**
	 * Creates new JsonHttpResponseHandler, with Json String encoding UTF-8
	 */
	public TypeResultHttpResponseHandler() {
		super(DEFAULT_CHARSET);
	}

	/**
	 * Creates new JsonHttpRespnseHandler with given Json String encoding
	 * @param encoding String encoding to be used when parsing JSON
	 */
	public TypeResultHttpResponseHandler(String encoding) {
		super(encoding);
	}

	/**
	 * Returns when request succeeds
	 * @param statusCode http response status line
	 * @param headers    response headers if any
	 * @param response   parsed response if any
	 */
	public abstract void onSuccess(int statusCode, Result mResult);

	/**
	 * Returns when request failed
	 * @param statusCode    http response status line
	 * @param headers       response headers if any
	 * @param throwable     throwable describing the way request failed
	 * @param errorResponse parsed response if any
	 */
	public abstract void onFailure(int statusCode, String mErrorMsg);

	/**
	 * Returns when request failed
	 * @param statusCode    http response status line
	 * @param headers       response headers if any
	 * @param throwable     throwable describing the way request failed
	 * @param errorResponse parsed response if any
	 */
	public void onFaile(int statusCode, String mErrorMsg) {
		onFailure(statusCode, mErrorMsg);
		if (isToastError) {
			if (!TextUtils.isEmpty(mErrorMsg)) {
				ToastUtil.diaplayMesShort(mContext, mErrorMsg);
			} else {
				ToastUtil.diaplayMesShort(mContext, "无法链接至服务器，请稍候再试");
			}
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		LogUtil.DefalutLog("onFailure responseString:" + responseString);
		onFaile(statusCode, responseString);
	}

	@Override
	public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
		LogUtil.DefalutLog("onSuccess responseString:" + responseString);
		if (statusCode == HttpStatus.SC_OK) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						final Object jsonResponse = parseResponse(responseString);
						postRunnable(new Runnable() {
							@Override
							public void run() {
								try {
									Result mResult = null;
									if (jsonResponse instanceof JSONObject) {
										JSONObject resultJson = (JSONObject) jsonResponse;
										mResult = new ResultParser().parse(resultJson);
									}
									if (mResult != null) {
										if (!TextUtils.isEmpty(mResult.getCode())) {
											if (!mResult.getCode().equals(SystemInfo.succeeCode)) {
												if (mResult.getCode().equals(SystemInfo.updateCode)) {
													//处理版本更新
													new VersionUpdateUtils(mContext, new VersionUpdateUtils.VersionUpdateAdapter(),
															VersionUpdateUtils.DEFAULT_UPDATE_TIP).checkUpdate();
												} else if (!TextUtils.isEmpty(mResult.getMsg())) {
													onFaile(statusCode, mResult.getMsg() + "(" + mResult.getCode() + ")");
												}
											} else {
												onSuccess(statusCode, mResult);
											}
										} else {
											onFaile(statusCode, null);
										}
									} else {
										onFaile(statusCode, null);
									}
								} catch (Exception e) {
									postRunnable(new Runnable() {
										@Override
										public void run() {
											onFaile(statusCode, null);
										}
									});
									e.printStackTrace();
								}
							}
						});
					} catch (final Exception ex) {
						postRunnable(new Runnable() {
							@Override
							public void run() {
								onFaile(statusCode, null);
							}
						});
						ex.printStackTrace();
					}
				}
			}).start();
		} else {
			onFaile(statusCode, null);
		}
	}

	/**
	 * Returns Object of type {@link JSONObject}, {@link JSONArray}, String, Boolean, Integer, Long,
	 * Double or {@link JSONObject#NULL}, see {@link org.json.JSONTokener#nextValue()}
	 *
	 * @param responseBody response bytes to be assembled in String and parsed as JSON
	 * @return Object parsedResponse
	 * @throws org.json.JSONException exception if thrown while parsing JSON
	 */
	protected Object parseResponse(String jsonString) throws JSONException {
		if (null == jsonString)
			return null;
		Object result = null;
		// trim the string to prevent start with blank, and test if the string
		// is valid JSON, because the parser don't do this :(. If Json is not
		// valid this will return null
		if (jsonString != null) {
			jsonString = jsonString.trim();
			if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
				result = new JSONTokener(jsonString).nextValue();
			}
		}
		if (result == null) {
			result = jsonString;
		}
		return result;
	}

}
