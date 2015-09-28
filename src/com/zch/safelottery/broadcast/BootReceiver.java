 package com.zch.safelottery.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.zch.safelottery.jingcai.WorldcupAlarmActivity;
import com.zch.safelottery.pull.PullSettings;
import com.zch.safelottery.util.AlarmUtil;
import com.zch.safelottery.util.HttpUtil;
import com.zch.safelottery.util.LogUtil;

/**开机自动启动
 * @author Messi
 */
public class BootReceiver extends BroadcastReceiver {
	
	public static final String action_boot = "android.intent.action.BOOT_COMPLETED";
	public static final String action_shutdown = "android.intent.action.ACTION_SHUTDOWN";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(action_boot)){ 
			/**开机启动完成广播**/
			AlarmUtil.startAlarm(context, PullSettings.Pull_TriggerAtTime, PullSettings.Pull_Interval_Time);
			WorldcupAlarmActivity.resetAlarmAfterReboot(context);
			LogUtil.AndroidPnLog("开机启动完成广播");
		}else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){ 
			/**网络变化广播**/
			if(HttpUtil.isNetworkAvailable(context)){
				AlarmUtil.startAlarm(context, PullSettings.Pull_TriggerAtTime, PullSettings.Pull_Interval_Time);
				LogUtil.AndroidPnLog("有网络的时候");
			}else{
				AlarmUtil.cancelAlarm(context,AlarmUtil.action_start_service);
				LogUtil.AndroidPnLog("无网络的时候");
			}
		}else if(intent.getAction().equals(action_shutdown)){
			LogUtil.AndroidPnLog("系统关机");
			WorldcupAlarmActivity.setSystemReboot(context);
		}
	}

}
