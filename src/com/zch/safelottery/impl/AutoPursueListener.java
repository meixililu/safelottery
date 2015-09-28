package com.zch.safelottery.impl;

import java.util.ArrayList;

import android.view.View;

import com.zch.safelottery.bean.AutoPursueBean;

public interface AutoPursueListener {
	
	public void createScheme();
	public void submit(ArrayList<AutoPursueBean> mAutoPursueBeans,View btn);
	
}
