package com.zch.safelottery.util;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.zch.safelottery.setttings.Settings;

public class AppInstallHelper {

	/**检测系统是否安装某个应用
	 * @param context
	 * @param strPackage
	 * @return
	 */
	public static boolean checkPackage(Context context, String strPackage) {
		boolean bInstallPackage = false;
		List<?> packs = context.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = (PackageInfo) packs.get(i);
			if (p.versionName == null) {
				continue;
			}
			if(Settings.DEBUG) 
				Log.d("Pack", p.packageName);
			if (strPackage.equals(p.packageName)) {
				bInstallPackage = true;
				break;
			}
		}
		return bInstallPackage;
	}


}
