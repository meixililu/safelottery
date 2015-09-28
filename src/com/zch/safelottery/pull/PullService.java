package com.zch.safelottery.pull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.UserSettingsActivity;
import com.zch.safelottery.activity.UserSettingsActivityPushLottery;
import com.zch.safelottery.bean.PullMsgBean;
import com.zch.safelottery.bean.PullRequestBean;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TextHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.PullMsgBeanParser;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.AlarmUtil;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;

public class PullService extends Service {

	private SharedPreferences sakePreference;
	private PullRequestBean prbean;
	
	@Override
	public void onCreate() {
		LogUtil.DefalutLog("PullService-onCreate");
		super.onCreate();
	}
	
	private boolean isSendRequest(){
		sakePreference = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		prbean = new PullRequestBean();
		String pullTimeType = sakePreference.getString(UserSettingsActivity.PullTimeCBKey, "all");
		/**首先确定当前是在用户设置的时间之内**/
		if( PullSettings.isValidTime( sakePreference,pullTimeType ) ){
			prbean.setReceiveMsgTime( PullSettings.getPullTime(sakePreference,pullTimeType) );
			if(TextUtils.isEmpty(SystemInfo.machId)){
				SystemInfo.initSystemInfo(this);
			}
			prbean.setMachId( SystemInfo.machId );
			prbean.setLotteryForecast( sakePreference.getBoolean(UserSettingsActivity.LotteryForecastCBKey, false) );
			prbean.setEndTimeRemain( sakePreference.getBoolean(UserSettingsActivity.LotteryEndTimeCBKey, false) );
			prbean.setMaxCommonId( sakePreference.getLong(PullSettings.MaxCommonIdKey, 0) );
			prbean.setMaxPrivacyId( sakePreference.getLong(PullSettings.MaxPrivacyIdKye, 0) );
			String lids = sakePreference.getString(UserSettingsActivityPushLottery.SelectedLotteryListKey, "001#113");
			if( !TextUtils.isEmpty(lids) && !lids.equals("none")){
				String[] lidList = lids.split("#");
				prbean.setLotteryidList(lidList);
			}
			return true;
		}
		return false;
	}
	
	private String getRequestData(){
		return JsonUtils.toJsonStr(prbean);
	}
	
	private String getMaxMsgId(){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("maxPrivacyId", sakePreference.getLong(PullSettings.MaxPrivacyIdKye, 0)+"");
		map.put("maxCommonId", sakePreference.getLong(PullSettings.MaxCommonIdKey, 0)+"");
		map.put("machId", SystemInfo.machId);
		return JsonUtils.toJsonStr(map);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
				try{
					LogUtil.DefalutLog("PullService-onStartCommand");
					if(isSendRequest()){
						SafelotteryHttpClient.post(GetString.PULLSERVERURL, PullSettings.ListMessage, getRequestData(),new TextHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode, Header[] headers ,String resultStr) {
								LogUtil.DefalutLog("+++receive pull msg:" + resultStr);
								if(!TextUtils.isEmpty(resultStr)){
									Map map = JsonUtils.stringToMap(resultStr);
									String intervalTimeStr = (String) map.get("intervalTime");
									String messageListStr = (String) map.get("messageList");
									List<PullMsgBean> pullMsgBeans = (List<PullMsgBean>) JsonUtils.parserJsonArray(messageListStr, new PullMsgBeanParser());
									if(pullMsgBeans != null){
										if(pullMsgBeans.size() > 0){
											for(PullMsgBean bean : pullMsgBeans){
												sendNotification(PullService.this, bean);
											}
											hasReadMessage();
										}
									}
									saveIntervalTime(intervalTimeStr);
									PullService.this.stopSelf();
								}
							}
							
							@Override
							public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
								PullService.this.stopSelf();
							}
						});
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void hasReadMessage(){
		SafelotteryHttpClient.post(GetString.PULLSERVERURL, PullSettings.ReadMessage, getMaxMsgId(),new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers ,String resultStr) {
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}
		});
	}
	
	/**显示notification
	 * @param context
	 * @param bean
	 */
	private void sendNotification(Context context,PullMsgBean bean){
		notification(context, bean);
		saveMaxId(bean);
	}
	
	private void notification(Context context,PullMsgBean bean){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
        notification.icon = R.drawable.icon;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.when = System.currentTimeMillis();
        notification.tickerText = "中彩票";

        String title = bean.getTitle();
        if(TextUtils.isEmpty(title)){
        	title = "中彩票祝您购彩愉快，中大奖！"; 
        }
        String message = bean.getContent();
        int notify_id = (int)System.currentTimeMillis();
        Intent intent = new Intent();
        intent.setClass(context, NotificationDetailsActivity.class);
        intent.putExtra(PullSettings.NOTIFICATION_TITLE, title);
        intent.putExtra(PullSettings.NOTIFICATION_MESSAGE, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     
        PendingIntent contentIntent = PendingIntent.getActivity(context, notify_id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, message, contentIntent);
        notificationManager.notify(notify_id, notification);
	}
	
	/**保存最大的消息id
	 * @param bean
	 */
	private void saveMaxId(PullMsgBean bean){
		if(bean != null){
			/** 1 私用消息 0 公共消息**/
			if(bean.getIsPrivate() == 1){
				Settings.saveSharedPreferences(sakePreference, PullSettings.MaxPrivacyIdKye, bean.getMessgeId());
			}else{
				Settings.saveSharedPreferences(sakePreference, PullSettings.MaxCommonIdKey, bean.getMessgeId());
			}
		}
	}
	
	/**保存推动间隔时间，并设置马上生效
	 * @param intervalTime
	 */
	private void saveIntervalTime(String intervalTime){
		if(!TextUtils.isEmpty(intervalTime)){
			int minute = ConversionUtil.StringToInt(intervalTime);
			if(minute >= 1){
				int miniSecond = minute * 60 * 1000;
				LogUtil.AndroidPnLog("miniSecond:"+miniSecond + "minute:"+minute);
				Settings.saveSharedPreferences(sakePreference, PullSettings.PullIntervalTimeKey, miniSecond);
				AlarmUtil.startAlarm(this,miniSecond, miniSecond);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.DefalutLog("PullService-onDestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
