package com.zch.safelottery.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

public class HttpUtil {

	public static void checkNetwork(final Context mContext){
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();    
        if (networkinfo == null || !networkinfo.isAvailable()) {  // 当前网络不可用  
        	Toast.makeText(mContext, "没有可用的网络连接,请打开网络连接！", 0).show();
        }else{
        	Toast.makeText(mContext, "暂无数据", 0).show();
        }
	}
	
	public static String checkNetwork(final Context mContext, int a){
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);    
		NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();    
		if (networkinfo == null || !networkinfo.isAvailable()) {  // 当前网络不可用  
			StatService.onEvent(mContext, "internet-error", "没有可用的网络连接,请打开网络连接！", 1);
			return "没有可用的网络连接,请打开网络连接！";
		}else{
			StatService.onEvent(mContext, "internet-error", "网络连接错误，请稍候再试", 1);
			return "网络连接错误，请稍候再试";
		}
	}
	
	public static boolean isNetworkAvailable(Context mContext){
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();    
        if (networkinfo != null && networkinfo.isAvailable()) {  
        	return true;
        }else{
        	// 当前网络不可用  
        	return false;
        }
	}
}
