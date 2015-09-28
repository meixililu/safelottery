package com.zch.safelottery.pull;

import android.content.SharedPreferences;

import com.zch.safelottery.activity.UserSettingsActivity;
import com.zch.safelottery.util.TimeUtils;

public class PullSettings {

	/**推送启动之后第一次时间**/
	public static final int Pull_TriggerAtTime = 1000 * 10;//;
	/**推送间隔时间**/
	public static final int Pull_Interval_Time = 1000 * 60 * 10;//;
	/**推送间隔时间Key**/
	public static final String PullIntervalTimeKey = "PullIntervalTimeKey";
	/**推动获取消息**/
	public static final String ListMessage  = "listMessage";
	/**推动通知服务器当前接受的最大消息id**/
	public static final String ReadMessage = "readMessage";
	/**最大的公告消息id key**/
	public static final String MaxCommonIdKey = "MaxCommonIdKey";
	/**最大的私密消息id key**/
	public static final String MaxPrivacyIdKye = "MaxPrivacyIdKye";
	
	/**开奖号码**/
    public static final String MESSAGE_TYPE_BONUS_NUMBER = "01";
    /**中奖**/
    public static final String MESSAGE_TYPE_BONUS = "02";
    /**投注失败**/
    public static final String MESSAGE_TYPE_ORDER_FAILURE = "03";
    /**当日开奖彩种预报**/
    public static final String MESSAGE_TYPE_OPEN_BONUS = "04";
    /**截止投注时间提醒**/
    public static final String MESSAGE_TYPE_END_ISSUE_NOTICE = "05";
    /**一周未投注提醒**/
    public static final String MESSAGE_TYPE_WEEK_NOT_ORDER = "06";
    /**周末推送**/
    public static final String MESSAGE_TYPE_WEEKEND_NOTICE = "07";
    /**节假日推送**/
    public static final String MESSAGE_TYPE_HOLIDAYS_NOTICE = "08";
    /**生日推送**/
    public static final String MESSAGE_TYPE_BIRTHDAY_NOTICE = "09";
    /**月初 月中推送**/
    public static final String MESSAGE_TYPE_HANDWORK_NOTICE = "10";
    
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";

    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

    public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";

    public static final String NOTIFICATION_URI = "NOTIFICATION_URI";
    
    public static final String NOTIFICATION_ZCH_MSGTYPE = "NOTIFICATION_ZCH_MSGTYPE";
    
    public static final String NOTIFICATION_ZCH_MSGID = "NOTIFICATION_ZCH_MSGID";
    
    /**当前是在用户设置的时间之内
     * @param sakePreference
     * @param type
     * @return
     */
    public static boolean isValidTime(SharedPreferences sakePreference,String type){
    	if(type.equals("all")){
    		return true;
    	}else if(type.equals("custom")){
    		String startTime = sakePreference.getString(UserSettingsActivity.StartTimeKey, "09:00");
    		String current = TimeUtils.formatLongTimeForCustom(System.currentTimeMillis(), TimeUtils.MinuteFormat);
    		String endTime = sakePreference.getString(UserSettingsActivity.EndTimeKey, "22:59");
    		if(isPullTimeValid(startTime, current, endTime)){
    			return true;
    		}else{
    			return false;
    		}
    	}else{
    		return false;
    	}
    }
    
    /**当前时间是否在有效期内
     * @param startTime
     * @param current
     * @param endTime
     * @return
     */
    public static boolean isPullTimeValid(String startTime, String current, String endTime){
    	int startTimeint = TimeUtils.getTimeForMilliscond(startTime);
    	int currentint = TimeUtils.getTimeForMilliscond(current);
    	int endTimeint = TimeUtils.getTimeForMilliscond(endTime);
    	if(startTimeint < currentint  && currentint < endTimeint){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**获取用户推动时间设置
     * @param sakePreference
     * @param type
     * @return
     */
    public static String getPullTime(SharedPreferences sakePreference,String type){
    	if(type.equals("custom")){
    		String startTime = sakePreference.getString(UserSettingsActivity.StartTimeKey, "09:00");
    		String endTime = sakePreference.getString(UserSettingsActivity.EndTimeKey, "22:59");
    		return startTime + "#" + endTime;
    	}else{
    		return type;
    	}
    }
}
