package com.zch.safelottery.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.ZCHLoaddingActivity;
import com.zch.safelottery.jingcai.WorldcupAlarmActivity;
import com.zch.safelottery.pull.PullService;
import com.zch.safelottery.setttings.BaiduStatistics;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AlarmUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.MethodUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.WorldcupUtil;
/**
 * @author Messi
 * 360后台扫描完成之后通过广播启动结果界面
 */
public class SafelotteryBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(AlarmUtil.action_start_service)){
			/**启动后台pull消息服务**/
			startService(context);
			LogUtil.DefalutLog("SafelotteryBroadcastReceiver---onReceive---BootReceiver.action_start_service");
		}else if(intent.getAction().contains(AlarmUtil.action_start_worldcup_alarm)){
			/**设置世界杯赛事闹铃**/
			notification(intent,context);
			LogUtil.DefalutLog("设置世界杯赛事闹铃:onReceive---action_start_worldcup_alarm");
		}else{
			/**接口异常提示**/
			String type = intent.getStringExtra("type");
			if(type != null){
				if(type.equals("error")){
					int error_code = intent.getIntExtra("error_code", 0);
					ToastUtil.diaplayMesShort(context, "很抱歉,连接失败,请稍后再试。("+error_code+")");
					StatService.onEvent(context, "internet-error", "很抱歉,连接失败,请稍后再试。("+error_code+")", 1);
				}else if(type.equals("exception")){
					String error_code = intent.getStringExtra("error_code");
					String error_info = intent.getStringExtra("error_info");
					ToastUtil.diaplayMesShort(context, error_info);
					BaiduStatistics.setBaiduService(context, error_code, error_info);
				}
				LogUtil.DefalutLog("SafelotteryBroadcastReceiver---onReceive---type:"+type);
			}
		}
	}
	
	/**启动推送服务
	 * @param context
	 */
	public static void startService(Context context){
		Intent intent = new Intent(context,PullService.class);
		context.startService(intent);
	}
	
	private void notification(Intent mIntent, Context context){
		
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Settings.saveSharedPreferences(sharedPrefs, WorldcupUtil.lastTimeShowWorldcupDialgoKey, (long)0);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Uri mUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + randomNotificationVoice());
		String id = mIntent.getStringExtra(AlarmUtil.WorldCupMatchId);
		String content = mIntent.getStringExtra(AlarmUtil.WorldCupMatchVS);
		final Builder mBuilder = new NotificationCompat.Builder(context); 
		mBuilder.setContentTitle("黄健翔喊你下注啦")
		.setContentText(TextUtils.isEmpty(content) ? "世界杯开始了" : content)
		.setSmallIcon(R.drawable.icon)
		.setTicker("黄健翔喊你下注啦")
		.setDefaults(Notification.DEFAULT_VIBRATE)
		.setDefaults(Notification.DEFAULT_LIGHTS)
		.setSound(mUri, AudioManager.STREAM_NOTIFICATION)
		.setAutoCancel(true);
		int notify_id = (int)System.currentTimeMillis();
		Intent intent = new Intent (context, ZCHLoaddingActivity.class);
		intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent (pend);
		notificationManager.notify(notify_id, mBuilder.build());
		
		/**提示完成时候移除已经保存的信息**/
		WorldcupAlarmActivity.removeAlarmHasShow(context,id);
	}
	
	public static int randomNotificationVoice(){
		int id = MethodUtils.randomNumber(4);
		if(id == 0){
			return R.raw.hjx_voice_1;
		}else if(id == 1){
			return R.raw.hjx_voice_2;
		}else if(id == 2){
			return R.raw.hjx_voice_3;
		}else if(id == 3){
			return R.raw.hjx_voice_4;
		}
		return R.raw.hjx_voice_1;
	}
}
