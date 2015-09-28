package com.zch.safelottery.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.zch.safelottery.broadcast.SafelotteryBroadcastReceiver;
import com.zch.safelottery.pull.PullSettings;
import com.zch.safelottery.setttings.Settings;

public class AlarmUtil {

	public static final String WorldCupMatchIsShutDown = "WorldCupMatchIsShutDown";
	public static final String WorldCupMatchVS = "WorldCupMatchVS";
	public static final String WorldCupMatchId = "WorldCupMatchId";
	public static final String action_start_service = "com.zch.safelottery.startservice";	
	public static final String action_start_worldcup_alarm = "zch.worldcualarm.service.";
	
	public static void setAlarm(Context context,long triggerAtTime,String id, String mNotificationName){
		String action = action_start_worldcup_alarm + id;
        Intent intent = new Intent(context, SafelotteryBroadcastReceiver.class);  
        intent.setAction(action);
        intent.putExtra(WorldCupMatchId, id);
        intent.putExtra(WorldCupMatchVS, mNotificationName);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
        am.set(AlarmManager.RTC, triggerAtTime, sender);
//		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 8000, 10000, sender);
		LogUtil.DefalutLog("-----setAlarm_Calendar-----action:" + action + "---triggerAtTime:"+triggerAtTime);
	}
	
	/**设置拉取信息服务的闹铃
	 * @param context
	 * @param triggerAtTime
	 * @param interval
	 */
	public static void startAlarm(Context context, int triggerAtTime, int interval){
		SharedPreferences sakePreference = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
        Intent intent = new Intent(context, SafelotteryBroadcastReceiver.class);  
        intent.setAction(action_start_service);  
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);  
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
        int intervalTime = sakePreference.getInt(PullSettings.PullIntervalTimeKey, interval);
        long currentTiem = SystemClock.elapsedRealtime();
        // ELAPSED_REALTIME 在指定的延时过后，发送广播，但不唤醒设备。
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime+currentTiem, intervalTime, sender);
        LogUtil.AndroidPnLog("currentTiem:"+currentTiem+"triggerAtTime:"+triggerAtTime+"interval:"+interval+"intervalTime:"+intervalTime);
	}
	
	/**取消拉取信息服务的闹铃
	 * @param context
	 */
	public static void cancelAlarm(Context context,String action){
        Intent intent = new Intent(context, SafelotteryBroadcastReceiver.class);  
        intent.setAction(action);  
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);  
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
        am.cancel(sender);
        LogUtil.DefalutLog("------------cancelAlarm action:" + action);
	}
}
