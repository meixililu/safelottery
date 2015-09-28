package com.zch.safelottery.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.LotteryInfoBean;
import com.zch.safelottery.database.BuyLotteryDatabaseUtil;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;

public class LotteryInfoParser extends AbstractParser<LotteryInfoBean> {

	public static final String LotteryOrderStrKey = "ordstr";
	public static final String LotteryOrderDateKey = "LotteryOrderDateKey";
	public static final int TimeInterval = 1000 * 60 * 20;// * 60 * 24;

	@Override
	public LotteryInfoBean parse(JSONObject json) throws JSONException {
		LotteryInfoBean mLotteryInfoBean = new LotteryInfoBean();

		if (json.has("lotteryId")) {
			mLotteryInfoBean.setLotteryId(json.getString("lotteryId"));
			mLotteryInfoBean.setPrompt("");
		}
		if (json.has("isStop")) {
			mLotteryInfoBean.setIsStop(json.getString("isStop"));
		}
		if (json.has("issue")) {
			ArrayList<IssueInfoBean> issueInfoBeans = (ArrayList<IssueInfoBean>) JsonUtils
					.parserJsonArray(json.getString("issue").toString(),
							new IssueInfoParser());
			mLotteryInfoBean.setIssueInfoList(issueInfoBeans);
		}

		return mLotteryInfoBean;
	}

	/**
	 * 将所有彩种list转成map
	 * 
	 * @param result_list
	 * @return
	 */
	public static Map<String, LotteryInfoBean> changeLotterInfoListToMap(
			Context mContext, SharedPreferences spf, String todayLottery,
			ArrayList<LotteryInfoBean> result_list) {
		Map<String, LotteryInfoBean> map = new HashMap<String, LotteryInfoBean>();
		try {
			StringBuilder sbBuilder = new StringBuilder();
			for (LotteryInfoBean bean : result_list) {
				if (LotteryId.getClass(bean.getLotteryId()) != null) {// 有开发的彩种才显示
					if (todayLottery.contains(bean.getLotteryId())) {
						bean.setTodayLottery(true);
					}
					map.put(bean.getLotteryId(), bean);
					sbBuilder.append(bean.getLotteryId());
					sbBuilder.append("#");
				}
			}
			int end = sbBuilder.lastIndexOf("#");
			if (end > 0) {
				String sub = sbBuilder.deleteCharAt(end).toString();
				setLotteryOrder(mContext, spf, sub);
			}
		} catch (Exception e) {
			LogUtil.ExceptionLog("setLotteryOrder()");
			e.printStackTrace();
		}
		return map;
	}

	public static LotteryInfoBean getLotteryInfo(
			ArrayList<LotteryInfoBean> result_list) {
		if (result_list != null) {
			if (result_list.size() > 0) {
				return result_list.get(0);
			}
		}
		return null;
	}

	/**
	 * 保存彩种列表顺序
	 * 
	 * @param spf
	 * @param newStr
	 */
	public static void setLotteryOrder(Context mContext, final SharedPreferences spf, final String newStr) throws Exception {
		LogUtil.DefalutLog("LotteryInfoParser-downloadLotteryOrderList:" + newStr);
		final String ordstr = spf.getString(LotteryOrderStrKey, "");
		if (TextUtils.isEmpty(ordstr)) {
			Settings.saveSharedPreferences(spf, LotteryOrderStrKey, newStr);
			Settings.saveSharedPreferences(spf, LotteryOrderDateKey, System.currentTimeMillis());
			new BuyLotteryDatabaseUtil(mContext).insertList(newStr);
		} else {
			boolean isUpdate = spf.getBoolean("isUpdate", false);
			final String neworderstr[] = newStr.split("#");
			List<String> oldorderstr = new BuyLotteryDatabaseUtil(mContext).getDataList();
			if(oldorderstr != null){
				if(isUpdate){
					Editor editor = spf.edit();
					editor.putBoolean("isUpdate", false);
					editor.commit();
					changeLotteryOrder(mContext, spf, neworderstr, oldorderstr, false);
				}else{
					if (neworderstr.length == oldorderstr.size()) {
						if (System.currentTimeMillis() - spf.getLong(LotteryOrderDateKey, 0) > TimeInterval) {
							changeLotteryOrder(mContext, spf, neworderstr, oldorderstr, true);
						}
					} else {
						changeLotteryOrder(mContext, spf, neworderstr, oldorderstr, false);
					}
				}
			}
		}
	}

	public static void changeLotteryOrder(final Context mContext, final SharedPreferences spf, final String neworderstr[], 
			final List<String> oldarrayList,boolean isBackGround) {
		if(isBackGround){
			new Thread(new Runnable() {
				@Override
				public void run() {
					changeLotteryOrderAction(mContext,spf,neworderstr,oldarrayList);
				}
			}).start();
		}else{
			changeLotteryOrderAction(mContext,spf,neworderstr,oldarrayList);
		}
	}

	public static void changeLotteryOrderAction(final Context mContext, final SharedPreferences spf, final String neworderstr[], 
			final List<String> oldarrayList) {
			boolean isEdit = false;
			ArrayList<String> newarrayList = new ArrayList<String>();
			ArrayList<String> needToRemove = new ArrayList<String>();
			for (int i = 0, j = neworderstr.length; i < j; i++) {
				newarrayList.add(neworderstr[i]);
			}
			for (String str : oldarrayList) {
				if (newarrayList.contains(str)) {
					newarrayList.remove(str);
				} else {
					needToRemove.add(str);
				}
			}
			if(needToRemove.size() > 0){
				isEdit = true;
				new BuyLotteryDatabaseUtil(mContext).removeList(needToRemove);
			}
			if(newarrayList.size() > 0){
				isEdit = true;
				new BuyLotteryDatabaseUtil(mContext).insertNewList(newarrayList);
			}
			if(isEdit){
				List<String> newestList = new BuyLotteryDatabaseUtil(mContext).getDataList();
				if(newestList != null){
					StringBuilder sbBuilder = new StringBuilder();
					for (int i=0,j=newestList.size();i<j;i++) {
						sbBuilder.append(newestList.get(i));
						sbBuilder.append("#");
					}
					int end = sbBuilder.lastIndexOf("#");
					if (end > 0) {
						String sub = sbBuilder.deleteCharAt(end).toString();
						Settings.saveSharedPreferences(spf, LotteryInfoParser.LotteryOrderStrKey, sub);
						Settings.saveSharedPreferences(spf, LotteryInfoParser.LotteryOrderDateKey, System.currentTimeMillis());
					}
				}
			}
	}
}
