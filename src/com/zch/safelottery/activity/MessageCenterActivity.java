package com.zch.safelottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.adapter.MainPageAdapter;
import com.zch.safelottery.view.PagerSlidingTabStrip;

public class MessageCenterActivity extends FragmentActivity{
    
    /** 个人消息的数量 **/
    private static TextView text_count;
    
    private PagerSlidingTabStrip titleIndicator;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_center);
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		text_count = (TextView) findViewById(R.id.message_count);
		pager.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
		titleIndicator = (PagerSlidingTabStrip)findViewById(R.id.indicator);
		titleIndicator.setViewPager(pager);
		titleIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
		InitImageView();
	}
	
	
	/**
     * 初始化动画
*/
    private void InitImageView() {
    	
    }
    
	@Override
	protected void onDestroy() {
		finish();
		super.onDestroy();
	}
	
	@Override
	protected void onResume(){
		setTextCount();
        super.onResume();
    }

	public static void setTextCount(){
		if(MessageCenterDetails2Activity.Count > 0){
			text_count.setVisibility(View.VISIBLE);
			text_count.setText(String.valueOf(MessageCenterDetails2Activity.Count));
		}else{
			text_count.setVisibility(View.GONE);
		}
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{
		int endIndex = 0;
		int tempStart = 0;
		int tempEnd = 0;
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			
			endIndex = arg0;
			tempStart = tempEnd;
		}
	}
}
