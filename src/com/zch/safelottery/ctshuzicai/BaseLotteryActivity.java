package com.zch.safelottery.ctshuzicai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.BuyLotteryActivity;
import com.zch.safelottery.activity.CombineHallActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.activity.UserSettingsActivity;
import com.zch.safelottery.impl.LotteryIssueChangeListener;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ShakeDetector;
import com.zch.safelottery.util.ShakeDetector.OnShakeListener;

public class BaseLotteryActivity extends ZCHBaseActivity {

	public static String INTENT_ISSUE = "issue";

	protected LinearLayout deleteLayout;
	protected LinearLayout randomLayout;
	protected LinearLayout submitLayout;

	protected LinearLayout title_bar;

	/*** 底下的注数及确认 **/
	protected LinearLayout baseBottomLayout;
	/*** 选球界面 **/
	protected LinearLayout baseCtLayout;
	/** 合买 **/
	protected LinearLayout baseCombineLayout;
	/** Scroll **/
	protected ScrollView ct_scroll_view;

	protected TextView random_select_text;
	protected TextView bet_num;
	protected TextView money_num;
	protected RadioButton daigou, hemai;
	protected FrameLayout top_menu;
	
	private LinearLayout fq_bonus_foreast_layout;
	private TextView fq_bonus_foreast_tv;

	protected Vibrator vibrator;
	protected SoundPool mSoundPoll;
	protected ShakeDetector mShakeDetector;

	private int mSoundId;
	public String issue;
	public String lid;
	private BuyLotteryActivity buylotteryActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_xml);
		initViews();
		initData();
		
		/**设置彩种期次变更监听器，需要用的在子类重写相应方法即可**/
		buylotteryActivity = (BuyLotteryActivity) SafeApplication.dataMap.get("BuyLotteryActivity");
		if(buylotteryActivity != null){
			buylotteryActivity.setLotteryIssueChangeListener(new LotteryIssueChangeListener() {
				@Override
				public void onSucceeToGetIssue(String tempLid) {
					LogUtil.DefalutLog("lid:"+lid +"---"+"tempLid:"+tempLid);
					if(lid.equals(tempLid)){
						onGetIssueSuccee();
					}
				}
				@Override
				public void onFaileToGetIssue(String tempLid) {
					if(lid.equals(tempLid)){
						onGetIssueFaile();
					}
				}
			});
		}
	}
	
	private void initViews() {
		Intent intent = getIntent();
		issue = intent.getStringExtra(INTENT_ISSUE);
		lid = intent.getStringExtra(LotteryId.INTENT_LID);
		LogUtil.DefalutLog(lid + " :: " + issue);

		deleteLayout = (LinearLayout) findViewById(R.id.choice_bottom_clear);
		randomLayout = (LinearLayout) findViewById(R.id.choice_bottom_random);
		submitLayout = (LinearLayout) findViewById(R.id.choice_bottom_select);
		title_bar = (LinearLayout) findViewById(R.id.buy_lottery_base_title_bar);
		/** content view,default gone **/
		baseCtLayout = (LinearLayout) findViewById(R.id.buy_lottery_base_content_linearlayout);
		baseBottomLayout = (LinearLayout) findViewById(R.id.buy_lottery_base_bottom);
		baseCombineLayout = (LinearLayout) findViewById(R.id.buy_lottery_base_content_combine);
		/** content view,default visibility **/
		ct_scroll_view = (ScrollView) findViewById(R.id.buy_lottery_base_content);

		random_select_text = (TextView) findViewById(R.id.random_select_text);
		bet_num = (TextView) findViewById(R.id.choice_bet);
		money_num = (TextView) findViewById(R.id.choice_money_count);
		
		fq_bonus_foreast_layout = (LinearLayout)findViewById(R.id.choice_bonus_foreast_layout);
		fq_bonus_foreast_tv= (TextView) findViewById(R.id.choice_bonus_foreast_tv);

		daigou = (RadioButton) findViewById(R.id.choice_title_xuanhao);
		hemai = (RadioButton) findViewById(R.id.choice_title_hemai);

		top_menu = (FrameLayout) findViewById(R.id.choice_title_button_menu);

		randomLayout.setOnClickListener(onClickListener);
		deleteLayout.setOnClickListener(onClickListener);
		submitLayout.setOnClickListener(onClickListener);
		daigou.setOnClickListener(onClickListener);
		hemai.setOnClickListener(onClickListener);
		top_menu.setOnClickListener(onClickListener);
	}

	private void initData() {
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mSoundPoll = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSoundId = mSoundPoll.load(BaseLotteryActivity.this, R.raw.sake, 1);
	}
	
	public void setMusic(int rawId){
		mSoundId = mSoundPoll.load(BaseLotteryActivity.this, rawId, 1);
	}

	/** shake **/
	public void shake() {
		SharedPreferences sakePreference = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		boolean isSake = sakePreference.getBoolean(UserSettingsActivity.IS_SHAKE, true);
		if (isSake) {
			long[] pattern = { 0, 100, 100, 3 }; // OFF/ON/OFF/ON...
			vibrator.vibrate(pattern, -1);
		}
	}

	/** set money **/
	public void setBetAndMoney(long bet, int money) {
		bet_num.setText("您选择了" + bet + "注,");
		money_num.setText("共" + (bet * money) + "元");
	}
	
	/** set money **/
	public void setBetAndMoney(long bet) {
		bet_num.setText("您选择了" + bet + "注,");
		money_num.setText("共" + (bet << 1) + "元");
	}
	
	/** 竞彩，设置选了几场比赛  **/
	public void setChangCi(int num) {
		bet_num.setText("已投注" + num + "场");
	}

	/**
	 * 高频奖金预测
	 * @param str 显示结果
	 */
	protected void setBonusForeast(CharSequence str){
		if(TextUtils.isEmpty(str)){
			fq_bonus_foreast_layout.setVisibility(View.GONE);
		}else{
			fq_bonus_foreast_layout.setVisibility(View.VISIBLE);
			fq_bonus_foreast_tv.setText(str );
		}
	}
	
	/** change the title bar **/
	public void setTitleBar(View v) {
		title_bar.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		v.setLayoutParams(lp);
		title_bar.addView(v);
	}

	/** start Shake Lister **/
	public void startShakeLister() {
		try{
			mShakeDetector.start();
		}catch (Exception e) {
		}
	}

	/** stop Shake Lister **/
	public void stopShakeLister() {
		try{
			mShakeDetector.stop();
		}catch (Exception e) {
		}
	}

	/** init shake listener **/
	public void initShakeListener() {

		mShakeDetector = new ShakeDetector(this);
		mShakeDetector.registerOnShakeListener(new OnShakeListener() {
			public void onShake() {
				SharedPreferences sakePreference = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
				boolean isSong = sakePreference.getBoolean(UserSettingsActivity.IS_SONG, true);
				if (isSong && mSoundPoll != null) {
					AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
					float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
					float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					float volume = actualVolume / maxVolume;
					mSoundPoll.play(mSoundId, volume, volume, 1, 0, 1f);
				}
				StatService.onEvent(getApplicationContext(), "shaking", "摇晃手机", 1);
				shake(); // 启动抖动
				stateShake(); // 摇动处理事件
			}
		});
	}

	private void stateShake() {
		onShake();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.DefalutLog("BaseActivity-onDestroy()");
		if (vibrator != null) {
			vibrator.cancel();
			vibrator = null;
		}
		if (mShakeDetector != null) {
			mShakeDetector.stop();
			mShakeDetector = null;
		}
		if(buylotteryActivity != null){
			buylotteryActivity.setLotteryIssueChangeListener(null);
		}
	}

	protected OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.choice_bottom_clear:
				bottom_clear(v);
				break;
			case R.id.choice_bottom_random:
				bottom_random(v);
				break;
			case R.id.choice_bottom_select:
				bottom_submit(v);
				break;
			case R.id.choice_title_xuanhao: // 选号
				top_rbtn_daigou(v);
				break;
			case R.id.choice_title_hemai:// 合买入口
				StatService.onEvent(BaseLotteryActivity.this, "join in association", "参与合买", 1);
				top_rbtn_hemai(v);
				break;
			case R.id.choice_title_button_menu:
				top_menu(v);
				break;
			}
		}
	};

	/****
	 * 显示购彩选球，scrollview
	 * 
	 * @param cView
	 */
	protected void setcView(View cView) {
		ct_scroll_view.addView(cView);
		ct_scroll_view.setVisibility(View.VISIBLE);
		baseCombineLayout.setVisibility(View.GONE);
	}

	/****
	 * 用Linearlayout显示
	 * 
	 * @param cView
	 */
	protected void setcViewLinearlayout(View cView) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		cView.setLayoutParams(lp);
		baseCtLayout.addView(cView);
		baseCtLayout.setVisibility(View.VISIBLE);
		baseCombineLayout.setVisibility(View.GONE);
	}
	
	/**
	 * 子类调用此方法，切换为竞彩的基类
	 */
	public void changeToJC(){
		random_select_text.setText("玩法");
		money_num.setVisibility(View.GONE);
		setChangCi(0);
	}
	
	/** bottom clear button **/
	public void bottom_clear(View v) {
	};

	/** bottom select button random **/
	public void bottom_random(View v) {
	};

	/** bottom submit button **/
	public void bottom_submit(View v) {
	};

	/** top daigou radiobutton **/
	public void top_rbtn_daigou(View v) {
		
	};

	/** top hemai radiobutton **/
	public void top_rbtn_hemai(View v) {
		Intent intent = new Intent(this, CombineHallActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(LotteryId.INTENT_LID, lid);
		intent.putExtra(Settings.BUNDLE, mBundle);
		
		this.startActivity(intent);
		
		daigou.setChecked(true);
	};

	/** top menu button **/
	public void top_menu(View v) {
	};

	/** shake listener **/
	public void onShake() {
		
	};
	
	/** issue change succee listener **/
	public void onGetIssueSuccee() {
	}
	
	/** issue change faile listener **/
	public void onGetIssueFaile() {
	}
}
