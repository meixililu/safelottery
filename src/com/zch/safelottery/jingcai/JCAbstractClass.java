package com.zch.safelottery.jingcai;

import android.widget.LinearLayout;

import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.lazyloadimage.ImageLoader;

public abstract class JCAbstractClass {
	
	public abstract void showItems(ImageLoader imageLoader,JZMatchBean bean,LinearLayout parent_layout );
	public abstract void showItems(JZMatchBean bean,LinearLayout parent_layout );
	
}
