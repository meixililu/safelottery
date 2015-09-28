package com.zch.safelottery.util;

import java.util.HashMap;

import android.text.TextUtils;

public class StringUtil {

	
	  /**
	   * 删除小数最后的0，如果最后一位是小数点，则删除。
	   * @param s
	   * @return
	   */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	public static String subString(String str, int start, int end) {
		if (TextUtils.isEmpty(str))
			return "";

		int length = str.length();
		if (length > end) {
			return str.substring(start, end);
		} else {
			return str;
		}
	}

	/**
	 * 根据国家名字获取sn
	 * 使用范围：竞足-猜冠军
	 * @param countryName 国家名字
	 * @return 返回sn，如果没有匹配到，则返回空串
	 */
	public static String getSnByCountryName(String countryName) {
		if ("巴西".equals(countryName)) {
			return "01";
		} else if ("德国".equals(countryName)) {
			return "02";
		} else if ("阿根廷".equals(countryName)) {
			return "03";
		} else if ("西班牙".equals(countryName)) {
			return "04";
		} else if ("比利时".equals(countryName)) {
			return "05";
		} else if ("荷兰".equals(countryName)) {
			return "06";
		} else if ("意大利".equals(countryName)) {
			return "07";
		} else if ("法国".equals(countryName)) {
			return "08";
		} else if ("葡萄牙".equals(countryName)) {
			return "09";
		} else if ("哥伦比亚".equals(countryName)) {
			return "10";
		} else if ("乌拉圭".equals(countryName)) {
			return "11";
		} else if ("英格兰".equals(countryName)) {
			return "12";
		} else if ("智利".equals(countryName)) {
			return "13";
		} else if ("俄罗斯".equals(countryName)) {
			return "14";
		} else if ("科特迪瓦".equals(countryName)) {
			return "15";
		} else if ("瑞士".equals(countryName)) {
			return "16";
		} else if ("日本".equals(countryName)) {
			return "17";
		} else if ("波黑".equals(countryName)) {
			return "18";
		} else if ("克罗地亚".equals(countryName)) {
			return "19";
		} else if ("厄瓜多尔".equals(countryName)) {
			return "20";
		} else if ("墨西哥".equals(countryName)) {
			return "21";
		} else if ("美国".equals(countryName)) {
			return "22";
		} else if ("加纳".equals(countryName)) {
			return "23";
		} else if ("尼日利亚".equals(countryName)) {
			return "24";
		} else if ("希腊".equals(countryName)) {
			return "25";
		} else if ("韩国".equals(countryName)) {
			return "26";
		} else if ("喀麦隆".equals(countryName)) {
			return "27";
		} else if ("澳大利亚".equals(countryName)) {
			return "28";
		} else if ("哥斯达黎加".contains(countryName)) {
			return "29";
		} else if ("洪都拉斯".equals(countryName)) {
			return "30";
		} else if ("伊朗".equals(countryName)) {
			return "31";
		} else if ("阿尔及利亚".contains(countryName)) {
			return "32";
		} else
			return "";
	}
}
