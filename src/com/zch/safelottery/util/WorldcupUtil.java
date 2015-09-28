package com.zch.safelottery.util;

public class WorldcupUtil {

	public static final String lastTimeShowWorldcupDialgoKey = "lastTimeShowWorldcupDialgoKey";
	public static final long ThreeHour = 1000 * 60 * 60 * 3;
	
	public static boolean isNeedShowWorldcupDialog(long lastTime){
		if(System.currentTimeMillis() - lastTime > ThreeHour){
			return true;
		}else{
			return false;
		}
	}
}
