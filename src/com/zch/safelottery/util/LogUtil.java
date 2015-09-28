package com.zch.safelottery.util;

import android.util.Log;

import com.zch.safelottery.bean.SafelotteryType;

public class LogUtil {
	
//	public static boolean DEBUG = GetString.TEST;
	public static boolean DEBUG = true;
	
	/**默认log，TAG为:SafeLottery
	 * @param logContent
	 */
	public static void DefalutLog(String logContent){
		if(DEBUG) Log.d("SafeLottery", logContent.replace("\"{", "{").replace("}\"", "}").replace("\\", ""));	
	}
	
	/**默认log，TAG为:SafeLottery
	 * @param logContent
	 */
	public static void DefalutLog(SafelotteryType mSafelotteryType){
		if(DEBUG) Log.d("SafeLottery", mSafelotteryType.toString().replace("\"{", "{").replace("}\"", "}").replace("\\", ""));
	}
	
	/**
	 * @param logContent
	 */
	public static void SystemLog(String logContent){
		if(DEBUG) System.out.println(logContent);
	}
	
	/**
	 * @param TAG
	 * @param logContent
	 */
	public static void CustomLog(String TAG, String logContent){
		if(DEBUG) Log.d(TAG, logContent);
	}
	
	/**
	 * @param TAG
	 * @param logContent
	 */
	public static void e(String TAG, String logContent){
		if(DEBUG) Log.e(TAG, logContent);
	}
	/**
	 * @param TAG
	 * @param logContent
	 */
	public static void e(String tag, String msg, Throwable tr){
		if(DEBUG) Log.e(tag, msg, tr);
	}

	/**
	 * @param logContent
	 */
	public static void ErrorLog(String logContent){
		if(DEBUG) Log.e("SafeLottery","Error---"+logContent);
	}
	
	/**
	 * @param logContent
	 */
	public static void ExceptionLog(String logContent){
		if(DEBUG) Log.e("SafeLottery","Exception---"+logContent);
	}
	
	/**
	 * @param androidpn
	 */
	public static void AndroidPnLog(String logContent){
		if(DEBUG) Log.d("SafeLottery","pull---"+logContent);
	}
	
}
