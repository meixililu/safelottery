package com.zch.safelottery.jingcai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;

public class JCBaseActivity extends ZCHBaseActivity {

	public static String INTENT_ISSUE = "issue";

	protected Button cancelBtn;
	protected Button confirmBtn;
	protected Button finishBtn;

	protected LinearLayout title_bar;

	/*** 选球界面 **/
	protected LinearLayout baseCtLayout;
	protected LinearLayout baseBottomLayout;

	protected TextView bet_num;
	protected TextView top_menu_tv;
	protected FrameLayout top_menu;
	
	public String issue;
	public String lid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jc_base_activity);
		initViews();
	}
	
	private void initViews() {
		Intent intent = getIntent();
		issue = intent.getStringExtra(INTENT_ISSUE);
		lid = intent.getStringExtra(LotteryId.INTENT_LID);
		LogUtil.DefalutLog(lid + " :: " + issue);

		cancelBtn = (Button) findViewById(R.id.jc_base_cancel);
		confirmBtn = (Button) findViewById(R.id.jc_base_confirm);
		finishBtn = (Button) findViewById(R.id.jc_base_jc_finish);
		
		title_bar = (LinearLayout) findViewById(R.id.jc_base_title_bar);
		/** content view,default gone **/
		baseCtLayout = (LinearLayout) findViewById(R.id.jc_base_content_linearlayout);
		baseBottomLayout = (LinearLayout) findViewById(R.id.jc_base_bottom);

		bet_num = (TextView) findViewById(R.id.jc_base_count);
		
		top_menu = (FrameLayout) findViewById(R.id.choice_button_frequent_sort);
		top_menu_tv = (TextView) findViewById(R.id.choice_tv_frequent);

		cancelBtn.setOnClickListener(onClickListener);
		confirmBtn.setOnClickListener(onClickListener);
		finishBtn.setOnClickListener(onClickListener);
		top_menu.setOnClickListener(onClickListener);
	}
	
	/** 竞彩，设置选了几场比赛  **/
	public void setChangCi(int num) {
		bet_num.setText("已选" + num + "场");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.DefalutLog("BaseActivity-onDestroy()");
	}

	protected OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.jc_base_cancel:
				bottom_clear(v);
				break;
			case R.id.jc_base_confirm:
				bottom_submit(v);
				break;
			case R.id.jc_base_jc_finish:
				bottom_finish(v);
				break;
			case R.id.choice_button_frequent_sort:
				top_menu(v);
				break;
			}
		}
	};

	/****
	 * 用Linearlayout显示
	 * @param cView
	 */
	protected void setcViewLinearlayout(View cView) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		cView.setLayoutParams(lp);
		baseCtLayout.addView(cView);
		baseCtLayout.setVisibility(View.VISIBLE);
	}
	
	/** bottom clear button **/
	public void bottom_clear(View v) {
	};

	/** bottom submit button **/
	public void bottom_submit(View v) {
	};
	
	/** bottom finish button **/
	public void bottom_finish(View v) {
	};

	/** top menu button **/
	public void top_menu(View v) {
	};

}
