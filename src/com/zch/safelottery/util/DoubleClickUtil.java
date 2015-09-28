package com.zch.safelottery.util;

public class DoubleClickUtil {
	
	private static long lastClickTime;
	
	public static boolean isFastDoubleClick(){
		long time = System.currentTimeMillis();  
		long timeD = time - lastClickTime; 
		if ( 0 < timeD && timeD < 1200) { 
			LogUtil.DefalutLog("---FastDoubleClick---");
			return true;
		}
		lastClickTime = time;   
		return false;
	}

}
