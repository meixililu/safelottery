package com.zch.safelottery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zch.safelottery.fragment.MessageFragment;
import com.zch.safelottery.fragment.NoticeFragment;

public class MainPageAdapter extends FragmentPagerAdapter {
	
	public static final String[] CONTENT = new String[] {  "活动专题", "公告资讯", "私人消息" };

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	if(position==0){
    		return NoticeFragment.newInstance("15");
    	}else if(position==1){
    		return NoticeFragment.newInstance("12");
    	}else if(position==2){
    		return MessageFragment.newInstance("");
    	}else{
    		return NoticeFragment.newInstance("15");
    	}
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position].toUpperCase();
    }
}