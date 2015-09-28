package com.zch.safelottery.setttings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;

public class Settings {

	public static final String TAG = "SafeLottery";
	public static final boolean DEBUG = GetString.TEST;
	public static final String init = "error";
	/** Intent传递tabhost下标的key **/
	public static final String TABHOST = "tabHost";
	/** 是否显示有最新消息key **/
	public static final String NewestMessage = "NewestMessage";
	/** 登录之后要跳往的activity **/
	public static final String TOCLASS = "toClass";
	/** 登录之后跳往activity要传的参数 **/
	public static final String BUNDLE = "bundle";
	/** 登录之后跳往activity要传的参数 **/
	public static final String WEBTAGLOCATION = "webtaglocation";
	/** 用户中心的下标 **/
	public static final int USERHOME = 4;
	/** 注册SHAREPREFRENCE **/
	public static final String LOGIN_SHAREPREFRENCE = "isLogin";
	/** 首页倒计时请求间隔时间 **/
	public static final int HTTP_TIMESPAN = 2 * 1000;
	public static final String EXIT = "exit";
	/** 广播指令，关闭所有activity **/
	public static final int EXIT_ALL = 1000;
	/** 广播指令，显示有最新消息 **/
	public static final int ShowMessage = 3000;
	/** 广播指令，关闭除主页之外的所有activity **/
	public static final int EXIT_OTHER = 2000;
	/** 广播指令，关闭竞彩足球所有activity **/
	public static final int EXIT_JCZQ = 1001;
	/** 广播指令，关闭魔图所有activity **/
	public static final int EXIT_MOTU = 1002;
	public static final int Per_Page_Num = 10;
	public static final int C = 10;

	/** 客服电话 **/
	public static final String ROBE_PHONE = "tel:400-00-58511";
	
	public static final String INTENT_STRING_LOTTERY_ID = "lid";
	public static final String INTENT_STRING_TRADE_TIME = "trade_time";
	public static final String INTENT_STRING_FOLLOW_STATE = "follow_state";
	public static final String INTENT_STRING_BUY_METHOD = "buy_method";
	public static final String INTENT_STRING_LOTTERY_RESULT = "lottery_result";

	// 用户登录 SHAREPREFRENCE 存储
	public static final String LOGIN_TYPE = "type";
	public static final String LOGIN_NAME = "name";
	public static final String LOGIN_PASSWORD = "password";
	public static final String REMEBER_PASSWORD = "remeber_password";
	public static final String REMEBER_AUTOLOGIN = "remeber_autologin";
	public static final String REMEBER_ISJIONT = "remeber_isjiont";

	/** 测试版点击版本号切换至正式，但推动仍然是测试服务器，暂不能切换，正式一定不能切换成测试 **/
	public static void changeToPublicVersion() {
		if (GetString.TEST) {
			GetString.SERVERURL = "http://testv.zch168.com/clientInterface";// 服务器地址
			GetString.TEST = false;
		}
	}

	public static String getChanel(Context ctx) {
		String CHANNELID = "8001";
		try {
			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get("CHANNEL");
			if (value != null) {
				CHANNELID = value.toString();
			}
			if(TextUtils.isEmpty(CHANNELID)){
				CHANNELID = "8001";
			}
		} catch (Exception e) {
			CHANNELID = "8001";
		}
		return CHANNELID;
	}

	public static String getLotteryNewestTime(long time) {
		if (time != 0) {
			Date date = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		}
		return "即将开奖";
	}

	public static String shortString(String issue) {
		if (!issue.equals("") && issue != null && issue.length() > 2) {
			return issue.substring(2);
		}
		return issue;
	}
	
	// 账户类型
	/*
	 * 0 – 账户充值 * 9 - 账户提款 1 – 奖金派送 * 10 - 提款手续费 2 – 活动奖励 * 11 - 提款冻结 3 - VIP返点
	 * * 12 – 保底冻结 4 - 合买返点 * 13 - 购买彩票 5 - 奖金补偿 6 - 佣金提成 7 - 保底返款 8 - 撤单返款
	 */
	public static ArrayList<String> getAccountType() {
		ArrayList<String> lotteryTypeList = new ArrayList<String>();
		lotteryTypeList.add("全部");// 999
		lotteryTypeList.add("账户充值");
		lotteryTypeList.add("投注退款");
		lotteryTypeList.add("撤单返款");
		lotteryTypeList.add("奖金派送");
		lotteryTypeList.add("活动奖励");
		lotteryTypeList.add("佣金提成");
		lotteryTypeList.add("额度加");
		lotteryTypeList.add("购买彩票");
		lotteryTypeList.add("提现成功");
		lotteryTypeList.add("提现手续费");
		lotteryTypeList.add("额度减");
		lotteryTypeList.add("合买保底");
		lotteryTypeList.add("保底返款");
		lotteryTypeList.add("提现申请");
		lotteryTypeList.add("提现驳回");
		return lotteryTypeList;
	}

	/*
	 * 0 全部 1最近一周 2当天 3一个月 4三个月 5 半年
	 */
	public static ArrayList<String> getAccountTime() {
		ArrayList<String> lotteryTypeList = new ArrayList<String>();
		lotteryTypeList.add("全部");
		lotteryTypeList.add("当天");
		lotteryTypeList.add("最近一周");
		lotteryTypeList.add("最近一月");
		lotteryTypeList.add("最近三月");
		lotteryTypeList.add("最近半年");
		return lotteryTypeList;
	}

	// 购买方式
	public static ArrayList<String> getBuyMethodList() {
		ArrayList<String> lotteryTypeList = new ArrayList<String>();
		lotteryTypeList.add("全部");
		lotteryTypeList.add("代购");
		lotteryTypeList.add("发起合买");
		lotteryTypeList.add("自动跟单");
		lotteryTypeList.add("合买认购");
		lotteryTypeList.add("保底认购");
		lotteryTypeList.add("追号");
		lotteryTypeList.add("赠送彩票");
		lotteryTypeList.add("受赠彩票");
		return lotteryTypeList;
	}

	// 购买方式
	public static ArrayList<String> getLotteryResultList() {
		ArrayList<String> lotteryResultList = new ArrayList<String>();
		lotteryResultList.add("全部");
		lotteryResultList.add("未开奖");
		lotteryResultList.add("已中奖");
		lotteryResultList.add("未中奖");
		return lotteryResultList;
	}

	// 我的追号 交易时间
	public static ArrayList<String> getTradeTimeList() {
		ArrayList<String> tradeTimeList = new ArrayList<String>();
		tradeTimeList.add("全部");// 0
		tradeTimeList.add("今日");// 1
		tradeTimeList.add("近三天");// 2
		tradeTimeList.add("本周");// 3
		tradeTimeList.add("本月");// 4
		tradeTimeList.add("最近半年");// 5
		return tradeTimeList;
	}

	// 追号状态
	public static ArrayList<String> getLotterFollowStateList() {
		ArrayList<String> tradeTimeList = new ArrayList<String>();
		tradeTimeList.add("全部状态");// -1
		tradeTimeList.add("正在追号");// 0
		tradeTimeList.add("追号结束");// 1
		tradeTimeList.add("中奖停追 ");// 2
		tradeTimeList.add("异常停追");// 3
		return tradeTimeList;
	}

	// 弹出电话框动画
	public static Animation getAnimation(Context context, int method) {
		if (method == 0) {
			return AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		} else {
			return AnimationUtils.loadAnimation(context, R.anim.push_up_out);
		}

	}

	public static boolean isNeedUpdateBtn(Context context) {
		String sids = context.getResources().getString(R.string.no_update);
		return sids.contains(SystemInfo.sid);
	}

	public static boolean isNeedPhone(Context context) {
		String sids = context.getResources().getString(R.string.no_phone);
		return !sids.contains(SystemInfo.sid);
	}

	public static boolean isNeedUrlAndUpdate(Context context) {
		String sids = context.getResources().getString(R.string.no_url_and_update);
		return !sids.contains(SystemInfo.sid);
	}

	/**
	 * 发送广播关闭除了主页之外的所有页面
	 * 
	 * @param context
	 */
	public static void closeOtherActivity(Context context) {
		/** 关闭其他所有activity **/
		Intent broadcast = new Intent(SafeApplication.INTENT_ACTION_ALLACTIVITY);
		broadcast.putExtra(Settings.EXIT, Settings.EXIT_OTHER);
		context.sendBroadcast(broadcast);
	}
	
	/**
	 * 发送广播关闭除了主页之外的所有页面,并且设置第tabIndex为当前页面
	 * 
	 * @param context
	 * @param tabIndex　tab索引
	 */
	public static void closeOtherActivity(Context context,int tabIndex) {
		/** 关闭其他所有activity **/
		Intent broadcast = new Intent(SafeApplication.INTENT_ACTION_ALLACTIVITY);
		broadcast.putExtra(Settings.EXIT, Settings.EXIT_OTHER);
		broadcast.putExtra(Settings.TABHOST, tabIndex);
		context.sendBroadcast(broadcast);
	}
	/**
	 * 发送广播关闭除了主页之外的所有页面,并且设置第1个tab为当前页面
	 * 
	 * @param context
	 */
	public static void closeOtherActivitySetCurTabOne(Context context) {
		/** 关闭其他所有activity **/
		closeOtherActivity(context, 0);
	}
	
	/**获取配置文件类
	 * @param context
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, String value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, boolean value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, long value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, int value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * 取配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static int getSharedPreferencesInt(SharedPreferences sharedPrefs, String key) {
		LogUtil.DefalutLog("key-value:" + key );
		return sharedPrefs.getInt(key, 0);
	}
	/**
	 * 取配置信息
	 * @param context
	 * @param value
	 * @param key
	 */
	public static boolean getSharedPreferencesBoolean(SharedPreferences sharedPrefs, String key) {
		return sharedPrefs.getBoolean(key, false);
	}

	public static SpannableStringBuilder setColorSpan(Context mContext, String str, int color, int start, int end){
		SpannableStringBuilder sb = null;
		try {
			sb = new SpannableStringBuilder(str);
			sb.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(color)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	
	public static boolean isSettingPassword(Context context) {
		SharedPreferences loginPreference = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		String password = loginPreference.getString(Settings.LOGIN_PASSWORD, "");
		if(!TextUtils.isEmpty(password)){
			if(password.trim().length()==8){
				return true;
			}
		}
		return false;
	}
}
