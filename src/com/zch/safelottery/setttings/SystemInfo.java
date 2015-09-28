package com.zch.safelottery.setttings;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.zch.safelottery.util.LogUtil;

public class SystemInfo {

	public static String sysVer = "";// android4.0
	public static String softVer = "";// 客户端软件版本1.3.0
	public static final String platform = "04";// 手机平台,区分应用版本（标准版:04/竞彩版:10）
	public static String sid = "";// 渠道号
	public static String machId = "";// 设备编号,机器码
	public static int height;// 分辨率高
	public static int width;// 分辨率宽
	public static String func = "";// 功能名称
	public static String msg = "";// 具体的业务参数(json格式)
	public static String md5 = "";// MD5加密串
	
	public static int softVerCode = 20109;// 客户端SDK系统版本
	
	public static final String succeeCode = "0000";
	/**接口返回此编码说明客户端需要升级才能查看此功能**/
	public static final String updateCode = "0005";
	public static String macAddress = "";//mac地址  暂时不用

	
	/**
	 * 获取设备id，系统sdk，版本号，渠道号等信息
	 * 
	 * @throws NameNotFoundException
	 */
	public static void initSystemInfo(Context mContext) {
		try {
			TelephonyManager phoneMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = phoneMgr.getDeviceId();
			if (!TextUtils.isEmpty(imei)) {
				SystemInfo.machId = imei;
			} else {
				String android_id = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
				SystemInfo.machId = android_id;
			}

			String os = Build.VERSION.RELEASE;
			if (!TextUtils.isEmpty(imei)) {
				SystemInfo.sysVer = "android "+os;
			}

			SystemInfo.sid = Settings.getChanel(mContext);

			PackageManager pm = mContext.getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_CONFIGURATIONS);

			SystemInfo.softVerCode = pinfo.versionCode;
			SystemInfo.softVer = pinfo.versionName;
			
			DisplayMetrics dm = new DisplayMetrics();
//			mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
			dm = mContext.getResources().getDisplayMetrics();

			SystemInfo.width = dm.widthPixels;
			SystemInfo.height = dm.heightPixels;
			
//			getMacAddress(mContext);
			LogUtil.DefalutLog("SystemInfo.width:"+SystemInfo.width+"---"+"SystemInfo.height:"+SystemInfo.height);
		} catch (Exception e) {
			LogUtil.ExceptionLog("class SystemInfo-initSystemInfo()");
			e.printStackTrace();
		}
	}
	
	/**获取mac地址
	 * @param mContext
	 */
	public static void getMacAddress(Context mContext){
		try{
			WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			macAddress = info.getMacAddress();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getPhoneInfo(Activity mContext){
		StringBuffer buffer=new StringBuffer();
		try{
			String model=android.os.Build.MODEL;   // 手机型号
			String manufacturer=android.os.Build.MANUFACTURER;//手机厂商
			String release=android.os.Build.VERSION.RELEASE;  // android系统版本号
			TelephonyManager tel = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String SimSerialNumber=tel.getSimSerialNumber();//返回SIM卡的序列号 READ_PHONE_STATE
			buffer.append(model+"#");
			buffer.append(manufacturer+"#");
			buffer.append(release+"#");
			buffer.append(SystemInfo.machId+"#");
			buffer.append(SimSerialNumber);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
