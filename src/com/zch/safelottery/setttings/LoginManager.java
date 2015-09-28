package com.zch.safelottery.setttings;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.http.AsyncHttpClient;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.UserInfoParser;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.SharedPreferencesOperate;

public class LoginManager {
	
	public static final int remeber = 1;
	public static final int doNotRemeber = 0;
	public static int AutoLoginRetryTime = 1;
	
	/**
	 * 替换密码
	 * @param password 密码
	 */
	public static void saveLoginState(Context mContext, String password) {
		SharedPreferences loginPreference = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = loginPreference.edit();
		//替换密码
		editor.putString(Settings.LOGIN_PASSWORD, password);
		editor.commit();
	}

	/**
	 * 记住用户选择的普通登录状态
	 * @param mContext 当前的Context
	 * @param isSavePWD 是否保存密码
	 * @param isAutologin 是否自动登录
	 * @param name 用户名
	 * @param password 密码
	 * @param isJiont 是否联合登录
	 */
	public static void saveLoginState(Context mContext, boolean isSavePWD, boolean isAutologin, String name, String password,boolean isJiont) {
		saveUserInfo(mContext, isAutologin);
		
		SharedPreferences loginPreference = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = loginPreference.edit();
		
		//记住用户名
		editor.putString(Settings.LOGIN_NAME, name);
		
		//记住密码
		if (isSavePWD) {
			editor.putString(Settings.LOGIN_PASSWORD, password);
			editor.putInt(Settings.REMEBER_PASSWORD, remeber);
		} else {
			editor.putString(Settings.LOGIN_PASSWORD, "");
			editor.putInt(Settings.REMEBER_PASSWORD, doNotRemeber);
		}
		
		//是否自动登录
		if(isAutologin)
			editor.putInt(Settings.REMEBER_AUTOLOGIN, remeber);
		else
			editor.putInt(Settings.REMEBER_AUTOLOGIN, doNotRemeber);
			
		if(isJiont)
			editor.putInt(Settings.REMEBER_ISJIONT, remeber);
		else
			editor.putInt(Settings.REMEBER_ISJIONT, doNotRemeber);
		
		editor.commit();
	}
	
	
	/**
	 * 保存用户信息 不保存的情况下就删除用户信息
	 * @param mContext
	 * @param isAutologin 
	 */
	public static void saveUserInfo(Context mContext, boolean isAutologin){
		if(isAutologin){
			SharedPreferencesOperate.setUserInfo(mContext, SharedPreferencesOperate.SAVE_USER_INFO, JsonUtils.toJsonStr(GetString.userInfo));
		}else{
			SharedPreferencesOperate.remove(mContext, SharedPreferencesOperate.SAVE_USER_INFO);
		}
	}
	
//	/**
//	 * 记住用户选择的联合登录状态
//	 * @param mContext 当前的Context
//	 * @param isSavePWD 是否保存密码
//	 * @param isAutologin 是否自动登录
//	 * @param name 用户名
//	 * @param password 密码
//	 */
//	public static void saveJiontLoginState(Context mContext, boolean isJiont, String name, String password) {
//		SharedPreferences loginPreference = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
//		Editor editor = loginPreference.edit();
//		//记住用户名
//		editor.putString(Settings.LOGIN_NAME, name);
//		//记住密码
//		editor.putString(Settings.LOGIN_PASSWORD, password);
//		editor.putInt(Settings.REMEBER_PASSWORD, remeber);
//		//是否自动登录
//		editor.putInt(Settings.REMEBER_ISJIONT, remeber);
//		//是否为联合登录
//		
//		editor.commit();
//	}
	
	
	/**
	 * 还原用户的选择状态
	 * @param loginPreference 传入SharedPreferences
	 * @param userName 输入用户名EditText
	 * @param userPassWord 输入密码EditText
	 * @param passwordBox 保存密码的CheckBox
	 * @param boxAutologin 自动登录的CheckBox
	 * @return 用户密码
	 */
	public static String resetLoginState(SharedPreferences loginPreference,EditText userName,EditText userPassWord, CheckBox passwordBox, CheckBox boxAutologin ){
		String name = loginPreference.getString(Settings.LOGIN_NAME, "");
		String password = loginPreference.getString(Settings.LOGIN_PASSWORD, "");
		int REMEBER_AUTOLOGIN = loginPreference.getInt(Settings.REMEBER_AUTOLOGIN, -1);
		int REMEBER_PASSWORD = loginPreference.getInt(Settings.REMEBER_PASSWORD, -1);
		
		if(!TextUtils.isEmpty(name)){
			userName.setText(name);
		}
		if(!TextUtils.isEmpty(password)){
			userPassWord.setText(";';';';';'");
		}
		if( REMEBER_AUTOLOGIN == 1 || REMEBER_AUTOLOGIN == -1 ){
			boxAutologin.setChecked(true);
		}else{
			boxAutologin.setChecked(false);
		}
		if( REMEBER_PASSWORD == 1 || REMEBER_PASSWORD == -1 ){
			passwordBox.setChecked(true);
		}else{
			passwordBox.setChecked(false);
		}
		return password;
	}
	
	/**清除保存的用户名和密码
	 * @param mContext 传入一个当前的Context
	 */
	public static void clearPreferenceLogin(Context mContext) {

		SharedPreferences loginPreference = mContext.getSharedPreferences( Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = loginPreference.edit();
		editor.putString(Settings.LOGIN_NAME, "");
		editor.putString(Settings.LOGIN_PASSWORD, "");
		editor.commit();
	}

	
	/**
	 * 开始自动登录
	 * @param mContext 当前this
	 */
	public static void startAutologin(final Context mContext){
		SharedPreferences loginPreference = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		//当用户选择自动登录
		int isAutologin = loginPreference.getInt(Settings.REMEBER_AUTOLOGIN, -1);
		if(isAutologin == remeber){
			String type = loginPreference.getString(Settings.LOGIN_TYPE, "1");
			String name = loginPreference.getString(Settings.LOGIN_NAME, "");
			String password = loginPreference.getString(Settings.LOGIN_PASSWORD, "");
			//当用户名或密码不为空的时候 自动登录
			if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
				final Map<String, String> map = new HashMap<String, String>();
				map.put("type", type);
				map.put("info", name);
				map.put("password", password);
				autologinThread(mContext, JsonUtils.toJsonStr(map));
			}
		}
	}
	
	/**
	 * 后台执行登录
	 * @param name 用户名
	 * @param password 密码
	 */
	public static void autologinThread(final Context mContext, final String msg){
		try {
			SafelotteryHttpClient.post(mContext, "3101", "", msg, new TypeSafelotteryHttpResponseHandler(mContext,false,new UserInfoParser()) {
				@Override
				public void onSuccess(int statusCode, SafelotteryType response) {
					if(response != null){
						GetString.userInfo = (UserInfoBean) response;

						SharedPreferencesOperate.updateUserInfo(mContext, SharedPreferencesOperate.SAVE_USER_INFO, JsonUtils.toJsonStr(GetString.userInfo));
						GetString.isLogin = true;
						GetString.isAccountNeedReSet = true;
						LogUtil.DefalutLog("autologinThread---自动登录成功");
					}
				}
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
//					String info = SharedPreferencesOperate.getUserInfo(mContext, SharedPreferencesOperate.SAVE_USER_INFO);
//					LogUtil.DefalutLog("保存的信息：" + info);
//					if(!TextUtils.isEmpty(info)){
//						GetString.userInfo = (UserInfoBean) JsonUtils.parserJsonBean(info, new UserInfoParser());
//						GetString.isLogin = true;
//						GetString.isAccountNeedReSet = true;
//						LogUtil.DefalutLog("autologinThread---自动登录失败,取之前保存的信息: " + GetString.userInfo.toString());
//					}else{
						if(AutoLoginRetryTime > 0){
							AutoLoginRetryTime--;
							startAutologin(mContext);
						}
						LogUtil.DefalutLog("autologinThread---onFailure---AutoLoginRetryTime:"+AutoLoginRetryTime);
//					}
				}
			});
			StatService.onEvent(mContext, "total-autologin", "所有打开自动登请求", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
