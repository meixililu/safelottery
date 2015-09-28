package com.zch.safelottery.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TextView;

import com.zch.safelottery.R;

public abstract class TabsUtil {

	public static ImageView addTab(TabHost host, String title, int drawable, int index, Intent intent) {
        TabHost.TabSpec spec = host.newTabSpec("tab" + index);
        spec.setContent(intent);
        View view = prepareTabView(host.getContext(), title, drawable);
        ImageView tab_img=(ImageView)view.findViewById(R.id.tab_img_id);
        spec.setIndicator(view);
        host.addTab(spec);
        return tab_img;
    }
	
	private static View prepareTabView(Context context, String text, int drawable) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_main_nav, null);
        ImageView iv = (ImageView) view.findViewById(R.id.ivIcon);
        iv.setImageResource(drawable);
        TextView tv = (TextView) view.findViewById(R.id.tvTitle);
        if(!TextUtils.isEmpty(text)){
        	tv.setText(text);
        }else{
        	LayoutParams mParams = new LayoutParams(ScreenUtil.dip2px(context, 48), ScreenUtil.dip2px(context, 48));
        	tv.setVisibility(View.GONE);
        	iv.setLayoutParams(mParams);
        }
        return view;
    }
}
