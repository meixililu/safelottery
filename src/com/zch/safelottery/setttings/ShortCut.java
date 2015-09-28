package com.zch.safelottery.setttings;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.MessageCenterDetails2Activity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.database.NoticeDatabaseUtil;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;

public class ShortCut {

	private static final String isAddShortCut = "isAddShortCut";
	private static final String lastActivateTime = "lastActivateTime";
	public final static int MSG_ID = 1100;

	/**
	 * 检测是否已经创建过快捷方式
	 * 
	 * @param mContext
	 * @param setting
	 * @return
	 */
	public static boolean hasShortcut(Context mContext,
			SharedPreferences setting) {
		return setting.getBoolean(isAddShortCut, false);
	}

	/**
	 * 创建快捷方式
	 * 
	 * @param mContext
	 * @param setting
	 */
	public static void addShortcut(final Activity mContext,
			final SharedPreferences setting) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (!ShortCut.hasShortcut(mContext, setting)) {
						Intent localIntent1 = new Intent(
								"android.intent.action.MAIN");
						String str1 = mContext.getClass().getName();
						localIntent1.setClassName(mContext, str1);
						localIntent1
								.addCategory("android.intent.category.LAUNCHER");
						// 这里添加2个flag 可以 消除 在按home 键时，再点快捷方式重启程序的bug
						localIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						localIntent1
								.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						Intent localIntent2 = new Intent();
						localIntent2.putExtra(
								"android.intent.extra.shortcut.INTENT",
								localIntent1);
						String str2 = mContext.getString(R.string.app_name);
						localIntent2.putExtra(
								"android.intent.extra.shortcut.NAME", str2);
						Intent.ShortcutIconResource localShortcutIconResource = Intent.ShortcutIconResource
								.fromContext(mContext.getApplicationContext(),
										R.drawable.icon);
						localIntent2.putExtra(
								"android.intent.extra.shortcut.ICON_RESOURCE",
								localShortcutIconResource);
						localIntent2
								.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
						localIntent2.putExtra("duplicate", false); // 不允许重复创建
						mContext.sendBroadcast(localIntent2);

						// 快捷方式已经创建修改配置文件
						SharedPreferences.Editor localEditor = setting.edit();
						localEditor.putBoolean(isAddShortCut, true);
						localEditor.commit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	/**
	 * 激活设备
	 * 
	 * @param mContext
	 * @param setting
	 */
	public static void activate(Context mContext,
			final SharedPreferences setting) {
		if (isNeedActivate(mContext, setting)) {
			String msg = "{\"appVersion\":\"" + SystemInfo.softVerCode + "\"}";
			SafelotteryHttpClient.post(mContext, "3000", "clientActive", msg,
					new TypeResultHttpResponseHandler(mContext, false) {
						@Override
						public void onSuccess(int statusCode, Result mResult) {
							if (mResult != null) {
								String code = mResult.getCode();
								if (!TextUtils.isEmpty(code)
										&& code.equals(SystemInfo.succeeCode)) {
									saveLastActivateTime(setting);
									LogUtil.DefalutLog("激活成功");
								}
							}
						}

						@Override
						public void onFailure(int statusCode, String mErrorMsg) {
						}
					});
		}
	}

	/**
	 * 是否需要调用激活接口
	 * 
	 * @param mContext
	 * @param setting
	 * @return
	 */
	public static boolean isNeedActivate(Context mContext,
			SharedPreferences setting) {
		long spacingInterval = 1000 * 60 * 60 * 6;
		// long spacingInterval = 1000 ;
		long lastCheckTime = setting.getLong(lastActivateTime, 0);
		if (System.currentTimeMillis() - lastCheckTime > spacingInterval) {
			return true;
		} else {
			return false;
		}
	}

	public static void saveLastActivateTime(SharedPreferences setting) {
		long lastRequestTime = System.currentTimeMillis();
		// 已激活，修改配置文件
		SharedPreferences.Editor localEditor = setting.edit();
		localEditor.putLong(lastActivateTime, lastRequestTime);
		localEditor.commit();
	}

	/**
	 * 是否有最新消息
	 * 
	 * @param mContext
	 * @param setting
	 */
	public static void getNewestMessage(final Context mContext) {
		try {
			int maxIdGG = new NoticeDatabaseUtil(mContext).getMaxIdByType("12");
			int maxIdHd = new NoticeDatabaseUtil(mContext).getMaxIdByType("15");
			LogUtil.DefalutLog("---getNewestMessage:" + maxIdGG + "_" + maxIdHd);
			Map<String, String> map = new HashMap<String, String>();
			map.put("category", "12_15");
			map.put("id", maxIdGG + "_" + maxIdHd);
			SafelotteryHttpClient.post(mContext, "3004", "isNew", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(mContext, false) {
						@Override
						public void onSuccess(int statusCode, Result mResult) {
							if (mResult != null) {
								String code = mResult.getCode();
								if (!TextUtils.isEmpty(code) && code.equals(SystemInfo.succeeCode)) {
									String resultStr = mResult.getResult();
									if (!TextUtils.isEmpty(resultStr)) {
										HashMap<String, String> listMapStr = (HashMap<String, String>) JsonUtils.stringToMap(resultStr);
										if (listMapStr != null) {
											String infoList = listMapStr.get("info");
											if (!TextUtils.isEmpty(infoList)) {
												HashMap<String, String> contentStr = (HashMap<String, String>) JsonUtils.stringToMap(infoList);
												if (contentStr != null) {
													String status = contentStr.get("status");
													String has_new = contentStr	.get("has_new");
													if (!TextUtils.isEmpty(status) && status.equals("0")) {
														if (!TextUtils.isEmpty(has_new) && has_new.equals("true")) {
															/** 发送广播，我新增消息 **/
															Intent broadcast = new Intent(SafeApplication.INTENT_ACTION_ALLACTIVITY);
															broadcast.putExtra(Settings.NewestMessage, Settings.ShowMessage);
															mContext.sendBroadcast(broadcast);
														}
													}
												}
											}
										}
									}
								}
							}
						}

						@Override
						public void onFailure(int statusCode, String mErrorMsg) {

						}
					});
		} catch (Exception e) {
			LogUtil.DefalutLog("获取最新消息，异常");
			e.printStackTrace();
		}
	}
	
	public static void getPrivateCountMessage(final Context mContext) {
		if(GetString.userInfo == null){
			LogUtil.DefalutLog("用户信息为空 不请求");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		
		SafelotteryHttpClient.post(mContext, "3004", "noReadedCountPrivateMessage", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(mContext, false) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try{
					String code = mResult.getCode();
					if (code.equals(SystemInfo.succeeCode)) {
						String resultStr = mResult.getResult();
						HashMap<String, String> listMapStr = (HashMap<String, String>) JsonUtils.stringToMap(resultStr);
						int count = ConversionUtil.StringToInt(listMapStr.get("noReadedCount"));
						MessageCenterDetails2Activity.Count = count;
						if (count > 0) {
							/** 发送广播，我新增消息 **/
							Intent broadcast = new Intent(SafeApplication.INTENT_ACTION_ALLACTIVITY);
							broadcast.putExtra(Settings.NewestMessage, Settings.ShowMessage);
							mContext.sendBroadcast(broadcast);
						}
					}
				}catch (Exception e) {
					LogUtil.DefalutLog("Exception -- 不做处理");
				}
			}
				
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
		});
		
	}
}
