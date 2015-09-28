package com.zch.safelottery.parser;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.util.GetString;

public class UserInfoParser extends AbstractParser<UserInfoBean>{

	@Override
	public UserInfoBean parse(JSONObject json) throws JSONException {
		UserInfoBean mUserInfoBean = new UserInfoBean();
		if (json.has("userCode")) mUserInfoBean.setUserCode(json.getString("userCode"));
		if (json.has("nickName")) mUserInfoBean.setNickname(json.getString("nickName"));
		if (json.has("nickname")) mUserInfoBean.setNickname(json.getString("nickname"));
		if (json.has("userName")) mUserInfoBean.setUserName(json.getString("userName"));
		if (json.has("passWord")) mUserInfoBean.setPassWord(json.getString("passWord"));
		if (json.has("mobile")) mUserInfoBean.setMobile(json.getString("mobile"));
		if (json.has("email")) mUserInfoBean.setEmail(json.getString("email"));
		if (json.has("realName")) mUserInfoBean.setRealName(json.getString("realName"));
		if (json.has("cardType")) mUserInfoBean.setCardType(json.getString("cardType"));
		if (json.has("cardCode")) mUserInfoBean.setCardCode(json.getString("cardCode"));
		if (json.has("loginCount")) mUserInfoBean.setLoginCount(json.getString("loginCount"));
		
		if (json.has("province")) mUserInfoBean.setProvince(json.getString("province"));
		if (json.has("city")) mUserInfoBean.setCity(json.getString("city"));
		if (json.has("bankName")) mUserInfoBean.setBankName(json.getString("bankName"));
		if (json.has("subBank")) mUserInfoBean.setSubBank(json.getString("subBank"));
		if (json.has("bankCode")) mUserInfoBean.setBankCode(json.getString("bankCode"));
		
		if (json.has("rechargeAmount")) mUserInfoBean.setRechargeAmount(json.getString("rechargeAmount"));
		if (json.has("bonusAmount")) mUserInfoBean.setBonusAmount(json.getString("bonusAmount"));
		if (json.has("presentAmount")) mUserInfoBean.setPresentAmount(json.getString("presentAmount"));
		if (json.has("freezeAmount")) mUserInfoBean.setFreezeAmount(json.getString("freezeAmount"));
		if (json.has("drawAmount")) mUserInfoBean.setDrawAmount(json.getString("drawAmount"));
		if (json.has("useAmount")) mUserInfoBean.setUseAmount(json.getString("useAmount"));
		
		return mUserInfoBean;
	}
	
	public static void setData(Map resultMap){
		if(resultMap != null){
			GetString.userInfo.setProvince( (String)resultMap.get("province") );
			GetString.userInfo.setCity( (String)resultMap.get("city") );
			GetString.userInfo.setBankName( (String)resultMap.get("bankName") );
			GetString.userInfo.setSubBank( (String)resultMap.get("subBank") );
			GetString.userInfo.setBankCode( (String)resultMap.get("bankCode") );
		}
	}

}
