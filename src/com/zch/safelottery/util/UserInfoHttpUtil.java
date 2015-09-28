package com.zch.safelottery.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.UserInfoParser;

/*
 * 查看用户信息公共类
 */
public class UserInfoHttpUtil {
	private UserInfoBean resultBean;
	private Context context;
	private int num = 1;// 1为请求3102用户信息查看接口 2为请求3200账户信息接口

	private OnHttpListenter onHttp;

	public UserInfoHttpUtil(Context context, int num, OnHttpListenter onHttp) {
		this.context = context;
		this.num = num;
		this.onHttp = onHttp;
		doRequestTask();
	}

	private void doRequestTask() {

		if (num == 1) {
			SafelotteryHttpClient.post(context, "3102", "", initDate(), new TypeSafelotteryHttpResponseHandler(context, true, new UserInfoParser()) {

				@Override
				public void onSuccess(int statusCode, SafelotteryType response) {
					if (response != null) {
						resultBean = (UserInfoBean) response;
						GetString.userInfo.setUserName(resultBean.getUserName());
						GetString.userInfo.setMobile(resultBean.getMobile());
						GetString.userInfo.setEmail(resultBean.getEmail());
						GetString.userInfo.setRealName(resultBean.getRealName());
						GetString.userInfo.setCardType(resultBean.getCardType());
						GetString.userInfo.setCardCode(resultBean.getCardCode());

						GetString.userInfo.setProvince(resultBean.getProvince());
						GetString.userInfo.setCity(resultBean.getCity());
						GetString.userInfo.setBankName(resultBean.getBankName());
						GetString.userInfo.setSubBank(resultBean.getSubBank());
						GetString.userInfo.setBankCode(resultBean.getBankCode());
						GetString.userInfo.setLoginCount(resultBean.getLoginCount());

						SharedPreferencesOperate.updateUserInfo(context, SharedPreferencesOperate.SAVE_USER_INFO,
								JsonUtils.toJsonStr(GetString.userInfo));

						if (onHttp != null) {
							onHttp.onSuccess(statusCode, response);
						}
					}
				}

				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					if (onHttp != null) {
						onHttp.onFailure(statusCode, mErrorMsg);
					}
				}
			});
		} else if (num == 2) {
			SafelotteryHttpClient.post(context, "3200", "", initDate(), new TypeSafelotteryHttpResponseHandler(context, true, new UserInfoParser()) {

				@Override
				public void onSuccess(int statusCode, SafelotteryType response) {
					if (response != null) {
						resultBean = (UserInfoBean) response;
						GetString.userInfo.setRechargeAmount(resultBean.getRechargeAmount());
						GetString.userInfo.setBonusAmount(resultBean.getBonusAmount());
						GetString.userInfo.setPresentAmount(resultBean.getPresentAmount());
						GetString.userInfo.setFreezeAmount(resultBean.getFreezeAmount());
						GetString.userInfo.setDrawAmount(resultBean.getDrawAmount());
						GetString.userInfo.setUseAmount(resultBean.getUseAmount());

						SharedPreferencesOperate.updateUserInfo(context, SharedPreferencesOperate.SAVE_USER_INFO,
								JsonUtils.toJsonStr(GetString.userInfo));
					}
				}

				@Override
				public void onFailure(int statusCode, String mErrorMsg) {

				}
			});
		}
	}

	public void setOnHttpListenter(OnHttpListenter onHttp) {
		this.onHttp = onHttp;
	}

	public interface OnHttpListenter {
		public void onSuccess(int statusCode, SafelotteryType response);

		public void onFailure(int statusCode, String mErrorMsg);
	}

	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}
}
