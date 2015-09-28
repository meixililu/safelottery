package com.zch.safelottery.setttings;

import android.content.Context;

import com.baidu.mobstat.StatService;

public class BaiduStatistics {

	/**
	 * 百度线上统计服务
	 * @param context 操作的Context
	 * @param code 代号 错误码
	 * @param value 错误值
	 */
	public static void setBaiduService(Context context, String code, String value){
		StatService.onEvent(context, "E" + code, value, 1);
	}

	/**
	 * 百度线上统计服务
	 * @param context 操作的Context
	 * @param code 代号 错误码
	 */
	public static void setBaiduService(Context context, String code){
		StatService.onEvent(context, "E" + code, "数据失败", 1);
	}
}
