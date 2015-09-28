package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.activity.ZouShiTuActivity;
import com.zch.safelottery.asynctask.MyCountTimer;
import com.zch.safelottery.asynctask.MyCountTimer.OnCountTimer;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.K3SelectBean;
import com.zch.safelottery.bean.LotteryIssueHistoryBean;
import com.zch.safelottery.bean.SelectInfoBean;
import com.zch.safelottery.custom_control.CustomRoundTextView;
import com.zch.safelottery.custom_control.CustomRoundTextView.OnRoundClickListener;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryIssueHistoryParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;
import com.zch.safelottery.view.MyIssueHistoreyListAdapter;

public class K3XActivity extends BaseLotteryActivity implements OnClickListener{

	private int textSize;
	public static String[][][][] mDataSum;
	public static final String SHARED_PREFS = "SHARED_PREFS" + LotteryId.K3X;
	
	/** 玩法和值 **/
	public static final String K3_PLAY_SUM = "01";
	
	private LayoutInflater mInflater;
	
	private int pageSum = 5;
	private int groupLength;
	/** 对应的 id of group **/
	private int mGroupId; //当前进行操作的GroupID
	/** 对应的 id of item **/
	private int mSelectId; //当前进行操作的SelectID id of CheckBox Item
	private int[] mGroupSelect;//当前进行操作的Group 组合
	private CustomRoundTextView[] mRoundGroup;
	
	// 玩法左右滑动
	private ArrayList<View> views;
	private MyPagerAdapter myPagerAdapter;
	private ViewPager scrollScreen;
	
	private RadioGroup mRadioGroup;
	private TextView mTvBet;
	private TextView mTvIssueEnd;
	private TextView mTvIssueStart;
	private TextView mTvIssuePrompt;

	private TextView time_m_s;
	private TextView time_m_g;
	private TextView time_s_s;
	private TextView time_s_g;
	
	private Button mBtnPlayExplain;
	private Button mBtnIssueHistory;
	private Button mBtnClear;
	private Button mBtnConfirm;
	
	private LinearLayout mLayoutDice;
	private LinearLayout mLayoutPage;
	
	private ProgressBar mProgressDice;
	private ProgressBar mProgressContent;
	private ProgressBar mProgressBar;
	
	private PullToRefreshListView mListView;
	
	private MyCountTimer mCountTimer;
	
	private ArrayList<K3SelectBean> mResultLists;
	
	private int mBet;
	private SelectInfoBean mInfoBean;
	private ArrayList<BetNumberBean> lotteryBeans;
	private ArrayList<LotteryIssueHistoryBean> result_list;
	
	private MyIssueHistoreyListAdapter mIssueAdapter;
	
	private TranslateAnimation mHideAnimation,mShowAnimation;
	private boolean isOpen;
	
	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.k3_choice);
		
		 initView();
		 getData();
	}

	private void initView(){
//		lid = LotteryId.K3;
		textSize = (int) getResources().getDimension(R.dimen.bigger_l);
		
		mResultLists = new ArrayList<K3SelectBean>();
		lotteryBeans = new ArrayList<BetNumberBean>();
		result_list = new ArrayList<LotteryIssueHistoryBean>();
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setInitNum(1);
		mInfoBean.setRepeat(false);
		
		mInflater = LayoutInflater.from(this);
		mListView = (PullToRefreshListView) findViewById(R.id.k3_choice_listview_issue_history);
		scrollScreen = (ViewPager) findViewById(R.id.k3_choice_content);
		mRadioGroup = (RadioGroup) findViewById(R.id.k3_choice_radio_group);
		
		mLayoutPage = (LinearLayout) findViewById(R.id.k3_choice_layout_content);
		mLayoutDice = (LinearLayout) findViewById(R.id.k3_choice_layout_dice);
		
		mProgressDice = (ProgressBar) findViewById(R.id.k3_choice_dice_progressbar);
		mProgressContent = (ProgressBar) findViewById(R.id.k3_choice_content_progress);
		mProgressBar = (ProgressBar) findViewById(R.id.k3_choice_issue_history_progressbar);
		
		mTvIssueEnd = (TextView) findViewById(R.id.k3_choice_issue_end);
		mTvIssueStart = (TextView) findViewById(R.id.k3_choice_issue_start);
		mTvIssuePrompt = (TextView) findViewById(R.id.k3_choice_issue_prompt);
		time_m_s = (TextView) findViewById(R.id.k3_choice_time_ms);
		time_m_g = (TextView) findViewById(R.id.k3_choice_time_mg);
		time_s_s = (TextView) findViewById(R.id.k3_choice_time_ss);
		time_s_g = (TextView) findViewById(R.id.k3_choice_time_sg);
		mTvBet = (TextView) findViewById(R.id.k3_choice_tv_bet_meney);
		
//		mPanel = (Panel) findViewById(R.id.k3_choice_my_panel);
//		mListView = (MyListView) findViewById(R.id.k3_choice_drawer_issue_history_listview);
		
		mBtnPlayExplain = (Button) findViewById(R.id.k3_choice_play_explain);
		mBtnIssueHistory = (Button) findViewById(R.id.k3_choice_btn_issue_history);
		mBtnClear = (Button) findViewById(R.id.k3_choice_btn_clear);
		mBtnConfirm = (Button) findViewById(R.id.k3_choice_btn_confirm);
		
		mBtnPlayExplain.setOnClickListener(this);
		mBtnIssueHistory.setOnClickListener(this);
		mBtnClear.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		mTvIssuePrompt.setOnClickListener(this);
		
		setMusic(R.raw.rotate);
		initShakeListener();
		
		mHideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,  
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);  
        mShowAnimation = new TranslateAnimation(
        		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,  
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);  
        
        mShowAnimation.setAnimationListener(mAnimationListener);
        mHideAnimation.setAnimationListener(mAnimationListener);

        mHideAnimation.setDuration(500);
        mHideAnimation.setFillAfter(true);
		
        mShowAnimation.setDuration(500);
        mShowAnimation.setFillAfter(true);
        
        sharedPrefs = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
        
        
	}
	
	private AnimationListener mAnimationListener = new AnimationListener(){
		@Override
		public void onAnimationStart(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			if(isOpen){
				mLayoutPage.clearAnimation();
				mLayoutPage.setVisibility(View.GONE);
			}else{
				mLayoutPage.setVisibility(View.VISIBLE);
			}
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	};
	
	private void getData(){
		mDataSum = mDataSums;
		groupLength = mDataSum.length;
		mRoundGroup = new CustomRoundTextView[groupLength];
		for(int i = 0; i < groupLength; i++){
			mRoundGroup[i] = CustomRoundTextView.getCustomRoundTextView(this);
			mRoundGroup[i].setId(i);
			mRoundGroup[i].setOnRoundClickListener(new OnRoundClickListener() {
				
				@Override
				public void onClick(CheckBox checkBox, int groupId, int checkId) {
					setListener(groupId, checkId);
				}
			});
		}
		
		mIssueAdapter = new MyIssueHistoreyListAdapter(this, result_list, lid);
		mListView.setAdapter(mIssueAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					refresh();
				}
	        });
		
		new RequltView().execute();
		onGetIssueSuccee();
		refresh();
	}
	
	private void refresh(){
		result_list.clear();
		mIssueAdapter.notifyDataSetChanged();
		RequsetDataTask();
	}
	
	@Override
	public void onGetIssueSuccee() {
		countTimer();
	}
	
	@Override
	public void onGetIssueFaile() {
		ToastUtil.diaplayMesShort(getApplicationContext(),  "获取最新期次失败，请返回主页刷新重试！");
	}
	
	private void countTimer(){
		long remainTime = LotteryId.getRemainTime(LotteryId.K3X);
		issue = LotteryId.getIssue(LotteryId.K3X);
		if(remainTime > 0){
			if(!TextUtils.isEmpty(issue)){
				int len = issue.length();
				if(len > 2){
					mTvIssueStart.setText("距" + issue.subSequence(len - 2, len) + "期截止");
				}
//				refresh();
//				LogUtil.CustomLog("TAG", "countTimer11");
				mCountTimer = new MyCountTimer( remainTime, 1000, time_m_s, time_m_g, time_s_s, time_s_g);
				mCountTimer.start();
				mCountTimer.setOnCountTimer(new OnCountTimer() {
					@Override
					public void onCountTimer() {
						ToastUtil.diaplayMesShort(getApplicationContext(),  "正在获取最新期次");
//						LogUtil.CustomLog("TAG", "countTimer22");
						if(mCountTimer != null) {
							mCountTimer.cancel();
							mCountTimer = null;
						}
					}
					
				});
			}else{
				ToastUtil.diaplayMesShort(getApplicationContext(),  "获取最新期次失败，请返回主页刷新重试！");
			}
		}else{
			if(mCountTimer != null) {
				mCountTimer.cancel();
				mCountTimer = null;
			}
			issue = "";
			ToastUtil.diaplayMesShort(getApplicationContext(),  "期次已过期，请返回主页刷新重试！");
		}
	}
	
	private View getView(int index){
		View view = null;
		switch(index){
		case 0:
			view = mInflater.inflate(R.layout.k3_choice_page_0, null);
			LinearLayout mLayout_0_0 = (LinearLayout) view.findViewById(R.id.k3_choice_page_0_0);
			LinearLayout mLayout_0_1 = (LinearLayout) view.findViewById(R.id.k3_choice_page_0_1);
			
			mLayout_0_0.addView(mRoundGroup[0].setTable(mDataSum[0], textSize));
			mRoundGroup[1].setSingleSelect(true);
			mLayout_0_1.addView(mRoundGroup[1].setTable(mDataSum[1], textSize));
			return view;
		case 1:
			view = mInflater.inflate(R.layout.k3_choice_page_1, null);
			LinearLayout mLayout_1_0 = (LinearLayout) view.findViewById(R.id.k3_choice_page_1_0);
			
			mLayout_1_0.addView(mRoundGroup[2].setTable(mDataSum[2], textSize));
			return view;
		case 2:
			view = mInflater.inflate(R.layout.k3_choice_page_2, null);
			LinearLayout mLayout_2_0 = (LinearLayout) view.findViewById(R.id.k3_choice_page_2_0);
			LinearLayout mLayout_2_1 = (LinearLayout) view.findViewById(R.id.k3_choice_page_2_1);
			LinearLayout mLayout_2_2 = (LinearLayout) view.findViewById(R.id.k3_choice_page_2_2);
			
			mLayout_2_0.addView(mRoundGroup[3].setTable(mDataSum[3], textSize));
			mLayout_2_1.addView(mRoundGroup[4].setTable(mDataSum[4], textSize));
			mLayout_2_2.addView(mRoundGroup[5].setTable(mDataSum[5], textSize));
			return view;
		case 3:
			view = mInflater.inflate(R.layout.k3_choice_page_3, null);
			LinearLayout mLayout_3_0 = (LinearLayout) view.findViewById(R.id.k3_choice_page_3_0);
			LinearLayout mLayout_3_1 = (LinearLayout) view.findViewById(R.id.k3_choice_page_3_1);
			
			mLayout_3_0.addView(mRoundGroup[6].setTable(mDataSum[6], textSize));
			mLayout_3_1.addView(mRoundGroup[7].setTable(mDataSum[7], textSize));
			return view;
		case 4:
			view = mInflater.inflate(R.layout.k3_choice_page_4, null);
			LinearLayout mLayout_4_0 = (LinearLayout) view.findViewById(R.id.k3_choice_page_4_0);
			
			mLayout_4_0.addView(mRoundGroup[8].setTable(mDataSum[8], textSize));
			return view;
		default:
			break;
		}
		return view;
	}
	
	/**
	 * 取彩票单子的数据
	 * @param groupId
	 * @param checkId
	 */
	private void getLotteryBean(int groupId, int checkId){
		switch(groupId){
		case 0: //和值
			getBetNumberLists("01", "04");
			break;
		case 1: //大小单双 对和值0进行操作
			getBetNumberLists("01", "04");
			break;
		case 2://二同号
			getBetNumberLists("01", "01");
			break;
		case 4://二同号复选
			getBetNumberLists("02", "07");
			break;
		case 3://三同号
			getBetNumberLists("01", "01");
			break;
		case 5://三同号通选
			getBetNumberLists("03", "08");
			break;
		case 6://三不同号
			getBetNumberLists("01", "01");
			break;
		case 7://三连号
			getBetNumberLists("06", "08");
			break;
		case 8://二不同号
			getBetNumberLists("04", "01");
			break;
		}
	}
	
	private void getBetNumberLists(String playId, String pollId){
		mInfoBean.setPlayId(playId);
		mInfoBean.setPollId(pollId);
		
		ArrayList<BetNumberBean> listBetNumber = new ArrayList<BetNumberBean>();
		if(lotteryBeans.size() > 0 && !playId.equals(lotteryBeans.get(0).getPlayId())){
			indexList = 0;
			lotteryBeans.clear();
		}
		
		for (int i = 0, size = mResultLists.size(); i < size; i++) {
			K3SelectBean b = mResultLists.get(i);
			BetNumberBean mBean = new BetNumberBean();
			mBean.setPlayId(playId);
			mBean.setPollId(pollId);
			mBean.setIndex(b.getGroupId());
			if (b.getGroupId() == 0) {
				String numbers = b.getData();
				mBean.setBuyNumber(ViewUtil.getK3Number(",", new String[] { numbers.substring(0, numbers.indexOf("\n")) }));
			} else if (b.getGroupId() == 5) {
				String number = b.getData();
				number = number.substring(0, number.lastIndexOf("\n")); // 去掉最后的一句奖金说明
				number = number.replace("\n", "、");
				mBean.setBuyNumber(ViewUtil.getK3Number(",", number.split("、")));
			} else if (b.getGroupId() == 7) {
				String number = b.getData();
				number = b.getData().substring(0, number.lastIndexOf("\n"));
				mBean.setBuyNumber(ViewUtil.getK3Number(",", number.split("、")));
			} else {
				mBean.setBuyNumber(ViewUtil.getK3Number(",", b.getData().split("")));
			}
			mBean.setItem(1);
			mBean.setAmount(2);
			listBetNumber.add(mBean);
		}
		lotteryBeans.addAll( indexList, listBetNumber);
	}
	
	/**
	 * 事件操作
	 * @param groupId 当前所操作的GroupId -1为全部清除
	 * @param checkId 当前所操作的checkId of item
	 */
	private void setListener(int groupId, int checkId){
		mGroupId = groupId;
		mSelectId = checkId;
		switch(groupId){
		case -1:
			setSelectMethod(null, true);
			break;
		case 0:
			setSelectMethod(new int[]{0}, true);
			break;
		case 2:
		case 3:
		case 6:
			setSelectMethod(new int[]{ 2, 3, 6}, true);
			break;
		case 1: //大小单双 对和值0进行操作
			specialClear(0, checkId);
			setSelectMethod(new int[]{0}, false);
			break;
		case 4:
			setSelectMethod(new int[]{ 4}, true);
			break;
		case 5:
			setSelectMethod(new int[]{ 5}, true);
			break;
		case 7:
			setSelectMethod(new int[]{7}, true);
			break;
		case 8:
			setSelectMethod(new int[]{8}, true);
			break;
		}
	}
	
	private void setSelectMethod(int[] groupSelect, boolean isClear){
		if(views != null && views.size() > 0){
			this.mGroupSelect = groupSelect;
			if(isClear){
				methodClear(groupSelect);
			}
			getBetMeney(groupSelect);
		}
	}
	
	/**
	 * 清除的方法 清除别个Round里的数据
	 * @param groupSelect 不清除的 groupId 数据
	 */
	private void methodClear(int[] groupSelect){
		int selectId = 0;
		for(int i = 0; i < groupLength; i++){
			if(groupSelect == null){
				mRoundGroup[i].clear();
			}else{
				int selectLength = groupSelect.length;
				if(selectId < selectLength && i == groupSelect[selectId]){
					selectId++;
				}else{
					mRoundGroup[i].clear();
				}
			}
		}
	}
	
	/**
	 * 取注数的方法
	 * @param groupSelect
	 */
	private void getBetMeney(int[] groupSelect){
		mResultLists.clear();
		mBet = 0;
		if(groupSelect != null){
			for(int i: groupSelect){
				mResultLists.addAll(mRoundGroup[i].getResultSelect());
			}
		}
		mBet = mResultLists.size();
		SpannableStringBuilder sb = new SpannableStringBuilder("共");
		sb.append(String.valueOf(mBet));
		sb.append("注,");
		int start = sb.length();
		sb.append(String.valueOf(mBet * 2));
		sb.append("元");
		sb.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.red)), start, sb.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		mTvBet.setText(sb);
	}
	
	/**
	 * @param groupId 进行操作的Id of group
	 * @param id 当前选中的Id of item
	 */
	private void specialClear(int groupId, int id){
		mRoundGroup[groupId].select(getCorrespondindGroup(id), 4);
	}
	
	/**
	 * 1 大小单双对应的和值
	 * @param id
	 * @return
	 */
	private int[] getCorrespondindGroup(int id){
		switch (id) {
		case 0: //大
			return new int[]{11, 12, 13, 14, 15, 16, 17};
		case 1: //小
			return new int[]{4, 5, 6, 7, 8, 9, 10};
		case 2: //单
			return new int[]{5, 7, 9, 11, 13, 15, 17};
		case 3: //双
			return new int[]{4, 6, 8, 10, 12, 14, 16};

		default:
			return null;
		}
	}
	
	private void show(){
		addRadioButton(mRadioGroup);
		
		if(views.size() == 0){
			finish();
			ToastUtil.diaplayMesLong(K3XActivity.this, "加载失败，请重新启动程序");
		}
		
		myPagerAdapter = new MyPagerAdapter(views);
		scrollScreen.setAdapter(myPagerAdapter);
		scrollScreen.setOnPageChangeListener(new MyOnPageChangeListener());
		scrollScreen.setCurrentItem(Settings.getSharedPreferencesInt(sharedPrefs, SHARED_PREFS));
		
		mProgressContent.setVisibility(View.GONE);
		
		startShakeLister();
		
		getBetMeney(null);
		
	}
	
	@Override
	public void setTitleBar(View v) {
		// TODO Auto-generated method stub
		super.setTitleBar(v);
	}

	@Override
	public void onShake() {
		switch(scrollScreen.getCurrentItem()){
		case 0:
			randomNumber(new int[]{0});
			break;
		case 1:
			randomNumber(new int[]{2});
			break;
		case 2:
			randomNumber(new int[]{3, 4, 5});
			break;
		case 3:
			randomNumber(new int[]{6, 7});
			break;
		case 4:
			randomNumber(new int[]{8});
			break;
		}
		super.onShake();
	}

	private void randomNumber(int[] groupId){
		Random r = new Random();
		mGroupId = groupId[r.nextInt(groupId.length)];
		int row = r.nextInt(mDataSum[mGroupId].length);
		int colSum = mDataSum[mGroupId][0].length;
		int col = r.nextInt(mDataSum[mGroupId][row].length);
		mRoundGroup[mGroupId].select(row * colSum + col);
		setSelectMethod(new int[]{mGroupId}, true);
	}
	
	private void addRadioButton(RadioGroup mGroup){
		for(int i = 0; i < pageSum; i++){
			final RadioButton button = new RadioButton(this);
			button.setButtonDrawable(R.drawable.round_small_check_selector);
			button.setId(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
			params.setMargins(-10, -10, -10, -10);
			button.setLayoutParams(params);
			if(i == 0) button.setChecked(true); //初始化
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					scrollScreen.setCurrentItem(button.getId());
				}
			});
			mGroup.addView(button);
		}
	}
	
	private class RequltView extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			mProgressContent.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}


		@Override
		protected void onPostExecute(Void result) {
			try{
				show();
			}catch (Exception e) {
				LogUtil.CustomLog("TAG", "RequltView onPostExecute 关闭");
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try{
				views = new ArrayList<View>(pageSum);
				for (int i = 0; i < pageSum; i++) {
					View view = getView(i);
					if(view != null){
						views.add(view);
					}else{
						pageSum--;
					}
				}
			}catch (Exception e) {
				LogUtil.CustomLog("TAG", "RequltView doInBackground 关闭");
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	/***
	 * 滑动的Adapter
	 * 
	 * @author Jiang
	 */
	public class MyPagerAdapter extends PagerAdapter {

		public ArrayList<View> mListViews;

		public MyPagerAdapter(ArrayList<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/***
	 * initial 选中
	 * 
	 * @author Jiang
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			RadioButton mRadioButton = (RadioButton) mRadioGroup.getChildAt(arg0);
			mRadioButton.setChecked(true);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void smiteActivity(){
		if(mBet < 1){
			
			ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择1注");
			
		}else if(mBet * 2 < GetString.TopBetMoney){
			try {
				startActivity();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
		}
	}
	
	protected void onPreExecute() {
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressDice.setVisibility(View.VISIBLE);
		mTvIssuePrompt.setVisibility(View.GONE);
		mTvIssueEnd.setVisibility(View.GONE);
		mLayoutDice.setVisibility(View.GONE);
	}
	
	private void RequsetDataTask(){
		onPreExecute();
		Map<String, String> map = new HashMap<String, String>();
		map.put("lotteryId", lid);
		map.put("page", "1");
		map.put("pageSize", "20");
		String msg = JsonUtils.toJsonStr(map);
		SafelotteryHttpClient.post(this, "3301", "history", msg, new TypeMapHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try {
					if(mMap != null){
						ArrayList<LotteryIssueHistoryBean> mList = (ArrayList<LotteryIssueHistoryBean>) JsonUtils.parserJsonArray(
								(String)mMap.get("issueList"), new LotteryIssueHistoryParser());
						if(mList != null){
							result_list.addAll(mList);
							mIssueAdapter.notifyDataSetChanged();
							
							LotteryIssueHistoryBean mBean = mList.get(0);
							String issue = mBean.getName();
							int len = issue.length();
							issue = (len > 2)? issue.substring(len - 2, len): "";
							String str = issue + "期开奖:和值" + LotteryId.getK3Sum(mBean.getBonusNumber());
							mTvIssueEnd.setText( Settings.setColorSpan(K3XActivity.this, str, R.color.round_table_char, str.indexOf("和"), str.length()));
							mLayoutDice.addView(ViewUtil.getAutoView(getApplicationContext(), lid, mBean.getBonusNumber(), true, 3));
						}else{
							mTvIssueEnd.setText("暂无数据");
							mTvIssuePrompt.setVisibility(View.VISIBLE);
						}
					}else{
						mTvIssueEnd.setText("暂无数据");
						mTvIssuePrompt.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
			@Override
			public void onFinish() {
				mProgressBar.setVisibility(View.GONE);
				mProgressDice.setVisibility(View.GONE);
				mTvIssueEnd.setVisibility(View.VISIBLE);
				mLayoutDice.setVisibility(View.VISIBLE);
				mListView.onRefreshComplete();
				mLayoutDice.removeAllViews();
			}
		});
	}
	
	private void startActivity(){
		if(!TextUtils.isEmpty(issue)){
			getLotteryBean(mGroupId, 1);
		
			SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
			SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
			
			Intent intent = new Intent();
			intent.setClass(this, CtOrderListActivity.class);
			intent.putExtra("lid", lid);
			intent.putExtra("issue", issue);
			intent.putExtra("playMethod", lotteryBeans.get(0).getPlayId());
			startActivityForResult(intent, GetString.BLRequestCode);
			setSelectMethod(null, true);
		}else{
			ToastUtil.diaplayMesShort(getApplicationContext(),  "获取最新期次失败，请返回主页刷新重试！");
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopShakeLister();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(views != null && views.size() > 0)
			startShakeLister();
	}
	
	@Override
	public void onBackPressed() {
		if (mBet > 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(this);
			dialog.setTitle("提示");
			dialog.setContent("您确定退出投注吗？一旦退出,您的投注内容将被清空。");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					finish();
				}

				@Override
				public void onCancleBtnClick() {

				}
			});
			dialog.show();
		}else{
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mCountTimer != null){
			mCountTimer.cancel();
			mCountTimer = null;
		}
		//清除静态数据
		SafeApplication.dataMap.clear(); 
		if(mDataSum != null)
			mDataSum = null;
		
		//保存配置信息
		Settings.saveSharedPreferences(sharedPrefs, SHARED_PREFS, scrollScreen.getCurrentItem());
	}
	
	private int indexList;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		super.onActivityResult(requestCode, resultCode, mIntent);
		if(requestCode == GetString.BLRequestCode){
			indexList = 0; //设置初始值为0
			lotteryBeans = (ArrayList<BetNumberBean>) SafeApplication.dataMap.get(SafeApplication.MAP_LIST_KEY);
			mInfoBean = (SelectInfoBean) SafeApplication.dataMap.get(SafeApplication.MAP_INFO_KEY);
			
			if(lotteryBeans == null || mInfoBean == null){
				return;
			}
			
			if(resultCode == GetString.BLAlter){
				indexList = mIntent.getIntExtra("index", 0);
				BetNumberBean mNumberBean = lotteryBeans.get(indexList);
				lotteryBeans.remove(indexList);
				
				try {
					getResultSelect(mNumberBean);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 返回选中
	 * @param mNumberBean
	 */
	private void getResultSelect(BetNumberBean mNumberBean){
		mGroupId = mNumberBean.getIndex();
		if(mGroupId == 0){
			scrollScreen.setCurrentItem(0);
		}else if(mGroupId == 2){
			scrollScreen.setCurrentItem(1);
		}else if(mGroupId == 3){
			scrollScreen.setCurrentItem(2);
		}else if(mGroupId == 4){
			scrollScreen.setCurrentItem(2);
		}else if(mGroupId == 5){
			scrollScreen.setCurrentItem(2);
		}else if(mGroupId == 6){
			scrollScreen.setCurrentItem(3);
		}else if(mGroupId == 7){
			scrollScreen.setCurrentItem(3);
		}else if(mGroupId == 8){
			scrollScreen.setCurrentItem(4);
		}
		mRoundGroup[mGroupId].select(compare(mGroupId, ViewUtil.getNumberRestore(",", mNumberBean.getBuyNumber())));
		
		setSelectMethod(new int[]{mGroupId}, true);
	}
	
	/**
	 * @param i GroupID
	 * @param j 
	 * @param number
	 */
	private int compare( int i, String number){
		int col = mDataSum[i][0].length;
		for(int j = 0, jl = mDataSum[i].length; j < jl; j++){
			for(int z = 0, zl = mDataSum[i][j].length; z < zl; z++){
	//			System.out.println(" zl ->> " + zl);
				if(mDataSum[i][j][z][0].contains(number)){
//					System.out.println(" i >> " + i + "  j >> " + j + "  z >> " + z + "  =  " + mDataSum[i][j][z][0] + " -- " + number);
					return j * col + z;//返回ID
				}
			}
		}
		return 0;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == mBtnIssueHistory.getId()){
			StatService.onEvent(K3XActivity.this, "recently result", "快彩类近期开奖", 1);
			if(isOpen){
				isOpen = false;
				mLayoutPage.startAnimation(mHideAnimation);
				mBtnIssueHistory.setBackgroundResource(R.drawable.k3_choice_issue_history_0);
			}else{
				isOpen = true;
				mLayoutPage.startAnimation(mShowAnimation);
				mBtnIssueHistory.setBackgroundResource(R.drawable.k3_choice_issue_history_1);
			}
		}else if(v.getId() == mBtnConfirm.getId()){
			LogUtil.CustomLog("TAG", "结果==》" + mResultLists.toString());
			smiteActivity();
		}else if(v.getId() == mBtnClear.getId()){
			if(mBet > 0){
				setSelectMethod(null, true);
			}
		}else if(v.getId() == mBtnPlayExplain.getId()){
			Intent intent = new Intent(K3XActivity.this, ZouShiTuActivity.class);
			intent.putExtra(LotteryId.INTENT_LID, lid);
			startActivity(intent);
		}else if(v.getId() == mTvIssuePrompt.getId()){
			refresh();
		}
	}
	
	private String[][][][] mDataSums = {
			{//0 1 2 3  page_0 和值
				{{"04", "中80元"}, {"05", "中40元"}, {"06", "中25元"}, {"07", "中16元"}, {"08", "中12元"}},
				{{"09", "中10元"}, {"10", "中9元"}, {"11", "中9元"}, {"12", "中10元"}, {"13", "中12元"}},
				{{"14", "中16元"}, {"15", "中25元"}, {"16", "中40元"}, {"17", "中80元"}}
			},{ // 1 page_0 大小单双对于和值的快选
				{{"大"}, {"小"}, {"单"}, {"双"}}
			},{ // 2 page_1 二同号单选
				{{"112"}, {"113"}, {"114"}, {"115"}, {"116"}},
				{{"122"}, {"223"}, {"224"}, {"225"}, {"226"}},
				{{"133"}, {"233"}, {"334"}, {"335"}, {"336"}},
				{{"144"}, {"244"}, {"344"}, {"445"}, {"446"}},
				{{"155"}, {"255"}, {"355"}, {"455"}, {"556"}},
				{{"166"}, {"266"}, {"366"}, {"466"}, {"566"}}
			},{ // 3 page_2 三同号单选
				{{"111"}, {"222"}, {"333"}},
				{{"444"}, {"555"}, {"666"}}
			},{ // 4 page_2 二同号复选
				{{"11*"}, {"22*"}, {"33*"}},
				{{"44*"}, {"55*"}, {"66*"}}
			},{ // 5 page_2 三同号通选
				{{"111、222\n333、444\n555、666","任一开出即中40元"}}
			},{ // 6 page_3 三不同号
				{{"123"}, {"124"}, {"125"}, {"126"}, {"134"}},
				{{"135"}, {"136"}, {"145"}, {"146"}, {"156"}},
				{{"234"}, {"235"}, {"236"}, {"245"}, {"246"}},
				{{"256"}, {"345"}, {"346"}, {"356"}, {"456"}}
			},{ // 7 page_3
				{{"123、234、345、456","三连号通选，任一开出即中10元"}}
			},{ // 8 page_4 二不同号
				{{"12"}, {"13"}, {"14"}, {"15"}, {"16"}},
				{{"23"}, {"24"}, {"25"}, {"26"}, {"34"}},
				{{"35"}, {"36"}, {"45"}, {"46"}, {"56"}}
			}
		};
}
