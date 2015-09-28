package com.zch.safelottery.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zch.safelottery.R;

public class AnimationUtil {

	public static Animation getRotateCenter(Context context){
		return AnimationUtils.loadAnimation(context, R.anim.rotate);
	}
}
