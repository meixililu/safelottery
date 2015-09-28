package com.zch.safelottery.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.MainTabActivity;
import com.zch.safelottery.http.AsyncHttpResponseHandler;
import com.zch.safelottery.http.SafelotteryHttpClient;

public class DownloadFileUtil {
	
	/**sd卡保存文件夹名称**/
	public static final String sdPath = "/zch/update/";
	/**应用程序内部存储文件夹名称**/
	public static final String apkPath = "/apk/update/";

	public static String apkDownloadPath = "";
	
	public static int record = -2;
	
	public static void DownloadFile(final Context mContext, String url){
		if(!TextUtils.isEmpty(url)){
			record = -2;
			apkDownloadPath = "";
			final NotificationManager mNotifyManager  = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			final Builder mBuilder = new NotificationCompat.Builder(mContext); 
			mBuilder.setContentTitle("中彩票更新通知").setContentText("开始更新").setSmallIcon(R.drawable.icon).setTicker("中彩票开始更新").setAutoCancel(true);
			Intent intent = new Intent (mContext, MainTabActivity.class);
			intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pend = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent (pend);
			mNotifyManager.notify(0, mBuilder.build());
			SafelotteryHttpClient.getAPk(url, null, new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
					LogUtil.DefalutLog("---DownloadFile success");
					saveAPK(mContext,binaryData);
					PendingIntent pend = PendingIntent.getActivity(mContext, 0, getInstallApkIntent(apkDownloadPath), 
							PendingIntent.FLAG_UPDATE_CURRENT);
					mBuilder.setContentIntent (pend);
		            mNotifyManager.notify(0, mBuilder.build());
				}
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
					LogUtil.DefalutLog("---DownloadFile onFailure");
					mBuilder.setContentText("下载失败,请稍后重试").setProgress(0,0,false);
		            mNotifyManager.notify(0, mBuilder.build());
					error.printStackTrace();
				}
				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					if(bytesWritten <= totalSize){
						int percent = (int) (((float)bytesWritten / totalSize) * 100);
						if(percent != 100 && record != percent){
							record = percent;
							mBuilder.setProgress(100, percent, false);
							mBuilder.setContentText("更新进度"+percent+"%");
							mNotifyManager.notify(0, mBuilder.build());
						}else if(percent == 100){
							mBuilder.setContentText("下载完成").setProgress(0,0,false);
							mNotifyManager.notify(0, mBuilder.build());
						}
					}
				}
			});
		}
	}
	
	public static void saveAPK(Context mContext,byte[] binaryData){
		try {
			FileOutputStream mFileOutputStream = getFile(mContext);
			mFileOutputStream.write(binaryData);
			mFileOutputStream.flush();
			mFileOutputStream.close();
			installApk(mContext,apkDownloadPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**安装apk**/
	public static void installApk(Context mContext,String filePath){
		mContext.startActivity(getInstallApkIntent(filePath));
	}
	
	public static Intent getInstallApkIntent(String filePath){
		LogUtil.DefalutLog("---filePath:"+filePath);
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		return i;
	}
	
	public static FileOutputStream getFile(Context mContext) throws IOException{
		String path = SDcardUtil.getDownloadPath();
		if(!TextUtils.isEmpty(path)){
			File sdDir = new File(path + sdPath);
			if(!sdDir.exists()){
				sdDir.mkdirs();
			}else{
				SDcardUtil.deleteFileInDir(sdDir);
			}
			apkDownloadPath = path + sdPath + "zcp_stand_" + System.currentTimeMillis() +".apk";
			File apkFile = new File(apkDownloadPath);
			apkFile.createNewFile();
			LogUtil.DefalutLog("---saveSDDirApkPath:"+apkDownloadPath);
			return new FileOutputStream(apkFile);
		}else{
			deleteOldUpdateFile(mContext);
			String fileName = "zcp_stand_" + System.currentTimeMillis() +".apk";
			apkDownloadPath = mContext.getFilesDir() + "/" + fileName;
			LogUtil.DefalutLog("---saveInnerDirApkPath:"+apkDownloadPath);
			return mContext.openFileOutput(fileName, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
		}
	}
	
	/**删除内部存储中之前下载的apk
	 * @param mContext
	 */
	public static void deleteOldUpdateFile(Context mContext){
		File file = mContext.getFilesDir();
		File[] files = file.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			LogUtil.DefalutLog("----------logoutFiles:"+files[i].getName());
			if (files[i].isFile()) {  
				String name = files[i].getName();
				if(name.startsWith("zcp_stand_")){
					SDcardUtil.deleteFile(files[i].getAbsolutePath());  
				}
			}  
		}
	}
	
	
	
	
}
