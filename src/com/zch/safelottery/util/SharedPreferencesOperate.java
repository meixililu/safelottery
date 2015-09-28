package com.zch.safelottery.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.zch.safelottery.setttings.Settings;

public class SharedPreferencesOperate {
	
	public static final String SAVE_USER_INFO = "SAVE_USER_INFO";
	/**
	 * 写入玩法
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setLotteryPlay(Context context, String key, int value){
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * 取得玩法
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getLotteryPlay(Context context, String key){
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		return sharedPrefs.getInt(key, 0);
	}
	
	/**
	 * 写入用户信息
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setUserInfo(Context context, String key, String value){
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 更新用户信息
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void updateUserInfo(Context context, String key, String value){
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		if(!TextUtils.isEmpty(sharedPrefs.getString(key, ""))){
			Editor editor = sharedPrefs.edit();
			editor.putString(key, value);
			editor.commit();
		}
	}
	
	/**
	 * 取得用户信息
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getUserInfo(Context context, String key){
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		return sharedPrefs.getString(key, "");
	}
	
	/**
	 * 删除信息
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key){
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.remove(key);
		editor.commit();
	}
}
