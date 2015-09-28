package com.zch.safelottery.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class SDcardUtil {

	/**获取sd卡路径
	 * @return
	 */
	public static String getDownloadPath() {
		File SDdir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals( android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			SDdir = Environment.getExternalStorageDirectory();
		}
		if (SDdir != null) {
			return SDdir.getPath();
		} else {
			return null;
		}
	}
	
	public static String isDirExits(Context mContext,String path) throws IOException{
		String sdcard = SDcardUtil.getDownloadPath();
		if(!TextUtils.isEmpty(path)){
			File sdDir = new File(sdcard + path);
			if(!sdDir.exists()){
				sdDir.mkdirs();
			}
			return sdcard + path;
		}else{
			return "";
		}
	}
	
	/**删除文件夹里面的所有文件
	 * @param cacheDir
	 */
	public static void deleteFileInDir(File cacheDir){
		if(cacheDir.isDirectory()){
			File[] files = cacheDir.listFiles();  
			for (int i = 0; i < files.length; i++) {  
				if (files[i].isFile()) {  
					boolean flag = deleteFile(files[i].getAbsolutePath());  
					if (!flag) break;  
				} 
			}
		}
	}
	
	/**删除文件夹里面的单个文件
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFile(String sPath) {  
		File file = new File(sPath);  
	    /**路径为文件且不为空则进行删除**/  
	    if (file.isFile() && file.exists()) {  
	        return file.delete();  
	    }  
	    return false;  
	}  
	
}
