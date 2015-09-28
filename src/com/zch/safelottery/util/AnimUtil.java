package com.zch.safelottery.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zch.safelottery.R;

public class AnimUtil {
	
	public static Animation getAnim(Context context, String anim){
		if(anim.equals("slide_left_in")){
			return AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
		}else if(anim.equals("slide_left_out")){
			return AnimationUtils.loadAnimation(context, R.anim.slide_left_out);
		}else if(anim.equals("slide_right_in")){
			return AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
		}else if(anim.equals("slide_right_out")){
			return AnimationUtils.loadAnimation(context, R.anim.slide_right_out);
		}else if(anim.equals("fade_in")){
			return AnimationUtils.loadAnimation(context, R.anim.fade_in);
		}else{
			return AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
		}
	}

}
