package com.zch.safelottery.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.zch.safelottery.R;
import com.zch.safelottery.adapter.ViewPagerAdapter;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.util.ViewUtil;

public class GuideActivity extends ZCHBaseActivity implements OnPageChangeListener {

	 private ViewPager viewpager;
	 private ViewPagerAdapter vpAdapter;
	 private ArrayList<View> views;
	private int currentIndex;
	private int isFirstLoad;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        initViews();
    }

    private void initViews() {
    	Intent intent = getIntent();
    	isFirstLoad = intent.getIntExtra("isFirstLoad", 0);
        views = new ArrayList<View>();
        
        View firstView = ViewUtil.getGuideImage(this,R.drawable.loadding_bg);
        
        
//        firstView.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				goHome();
//				return false;
//			}
//		});
        views.add(firstView);

        vpAdapter = new ViewPagerAdapter(views);
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        viewpager.setAdapter(vpAdapter);
        viewpager.setOnPageChangeListener(this);
    }

	@Override
	public void onBackPressed() {
		goHome();
	}
	
    private void goHome() {
    	if (isFirstLoad == 1) {
			Intent intent = new Intent();
			intent.setClass(GuideActivity.this, MainTabActivity.class);
			startActivity(intent);
			finish();
		} else {
			finish();
		}
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
    }

}