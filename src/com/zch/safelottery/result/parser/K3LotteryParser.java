package com.zch.safelottery.result.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.ViewUtil;
import com.zch.safelottery.util.ViewUtils;

public class K3LotteryParser {
	// 010
	// 解析开奖结果
	public static AutoWrapView getLotteryResultView(Context context, String lid, String[] results) {
		String result = results[0] + "," + results[1] + "," + results[2];
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		try {
			ViewUtil.getK3Result(context, linearLayout, result, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linearLayout;
	}

	// 解析未开奖（一注）
	public static void getUnkonwResultView(Context context, LinearLayout linearLayout, String result, String playMethod, String pollCode) {
		String[] str = changeStringToSet(result, playMethod, pollCode);
		if (str != null) {
			for (int i = 0; i < str.length; i++) {
				TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
//				textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
				textview.setPadding(5, 0, 0, 0);
				textview.setText(str[i]);
				textview.setGravity(Gravity.LEFT);
				linearLayout.addView(textview);
			}
		}
	}

	// 解析已开奖（一注）
	public static void getkonwResultView(Context context, AutoWrapView linearLayout, String userData, String[] sortResult, String playMethod, String pollCode) {
		List liststr = getResultList(userData, playMethod, pollCode);
		if (liststr != null) {
			getView(context, linearLayout, sortResult, playMethod, pollCode, liststr);
		}
	}

	public static void getView(Context context, AutoWrapView linearLayout, String[] sortResult, String playMethod, String pollCode, List liststr) {
		if (playMethod.equals("01")) {
			int count=0;
			for (int i = 0; i < sortResult.length; i++) {
				count=count+ConversionUtil.StringToInt(sortResult[i]);
			}
			String str=count+"";
			
			TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
			if (str.equals(liststr.get(0))) {
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
//			textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
			textview.setPadding(5, 0, 0, 0);
			textview.setText(liststr.get(0).toString());
			linearLayout.addView(textview);
		} else if (playMethod.equals("02") && pollCode.equals("01")) {
			Collections.sort(liststr);
			String datastr="";
			String resultstr="";
			for (int i = 0; i < liststr.size(); i++) {
				datastr=datastr+liststr.get(i);
			}
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
			if(datastr.equals(resultstr)){
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
//			textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
			textview.setPadding(5, 0, 0, 0);
			textview.setText(datastr);
			linearLayout.addView(textview);
		}else if (playMethod.equals("02") && pollCode.equals("07")) {
			String datastr="";
			String resultstr="";
			for (int i = 0; i < liststr.size()-1; i++) {
				datastr=datastr+liststr.get(i);
			}
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
			if(resultstr.contains(datastr)){
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
//			textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
			textview.setPadding(5, 0, 0, 0);
			textview.setText(datastr+"*");
			linearLayout.addView(textview);
		}else if (playMethod.equals("03") && pollCode.equals("01")) {
			String datastr="";
			String resultstr="";
			for (int i = 0; i < liststr.size(); i++) {
				datastr=datastr+liststr.get(i);
			}
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
			if(resultstr.equals(datastr)){
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
//			textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
			textview.setPadding(5, 0, 0, 0);
			textview.setText(datastr);
			linearLayout.addView(textview);
		}else if (playMethod.equals("03") && pollCode.equals("08")) {
			String resultstr="";
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			for (int i = 0; i < liststr.size(); i++) {
				TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
				if(resultstr.equals(liststr.get(i))){
					textview.setTextColor(context.getResources().getColor(R.color.red));
				}
//				textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
				if(i==0){
					textview.setPadding(5, 0, 0, 0);
					textview.setText(liststr.get(i).toString());
				}else{
					textview.setText(","+liststr.get(i).toString());
				}
				linearLayout.addView(textview);
			}
		}else if (playMethod.equals("04") && pollCode.equals("01")) {
			int number=0;
			String datastr="";
			String resultstr="";
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			for (int i = 0; i < liststr.size(); i++) {
				datastr=datastr+liststr.get(i).toString();
				if(resultstr.contains(liststr.get(i).toString())){
					number++;
				}
			}
			TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
			if(number==2){
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
//			textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
			textview.setPadding(5, 0, 0, 0);
			textview.setText(datastr);
			linearLayout.addView(textview);
		}else if (playMethod.equals("05") && pollCode.equals("01")) {
			Collections.sort(liststr);
			String datastr="";
			String resultstr="";
			for (int i = 0; i < liststr.size(); i++) {
				datastr=datastr+liststr.get(i);
			}
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
			if(resultstr.equals(datastr)){
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
//			textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
			textview.setPadding(5, 0, 0, 0);
			textview.setText(datastr);
			linearLayout.addView(textview);
		}else if (playMethod.equals("06") && pollCode.equals("08")) {
			String resultstr="";
			for(int j=0;j<sortResult.length;j++){
				resultstr=resultstr+sortResult[j];
			}
			for (int i = 0; i < liststr.size(); i++) {
				TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
				if(resultstr.equals(liststr.get(i))){
					textview.setTextColor(context.getResources().getColor(R.color.red));
				}
//				textview.setTextSize(context.getResources().getDimension(R.dimen.middle_m));
				if(i==0){
					textview.setPadding(5, 0, 0, 0);
					textview.setText(liststr.get(i).toString());
				}else{
					textview.setText(","+liststr.get(i).toString());
				}
				linearLayout.addView(textview);
			}
		}
	}

	// 将一注彩票的字符串转化为字符数组
	public static String[] changeStringToSet(String result, String playMethod, String pollCode) {
		if (!result.contains("-") && !result.equals("")) {
			if (playMethod.equals("01")) {
				String[] temp = new String[] { result };
				return temp;
			} else if (playMethod.equals("03") && pollCode.equals("08")) {
				String[] temp = new String[] { result };
				return temp;
			} else if (playMethod.equals("06")) {
				String[] temp = new String[] { result };
				return temp;
			} else {
				String[] temp = new String[] { result.replace(",", "") };
				return temp;
			}
		} else {
			return null;
		}
	}

	public static List getResultList(String result, String playMethod, String pollCode) {
		List<Object> list = new ArrayList<Object>();
		if(result.contains(",")){
			String[] temp = result.split(",");
			for (int i = 0; i < temp.length; i++) {
				list.add(temp[i]);
			}
		}
		else{
			list.add(result);
		}
		return list;
	}
}
