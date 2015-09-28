package com.zch.safelottery.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	public static void diaplayMesShort(Context context, int mes){
		Toast.makeText(context, mes, 0).show();
	}
	public static void diaplayMesShort(Context context, String mes){
		Toast.makeText(context, mes, 0).show();
	}
	
	public static void diaplayMesLong(Context context, String mes){
		Toast.makeText(context, mes, 1).show();
	}

}
