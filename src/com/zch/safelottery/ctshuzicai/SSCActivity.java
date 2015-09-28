package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.LotteryResultHistoryActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.asynctask.MyAsyncTask;
import com.zch.safelottery.asynctask.MyAsyncTask.OnAsyncTaskListener;
import com.zch.safelottery.asynctask.MyCountTimer;
import com.zch.safelottery.asynctask.MyCountTimer.OnCountTimer;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.SelectInfoBean;
import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.dialogs.JXDialog;
import com.zch.safelottery.dialogs.JXDialog.OnButtonItemClickListener;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.MethodUtils;
import com.zch.safelottery.util.NumeralMethodUtil;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.SharedPreferencesOperate;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class SSCActivity extends BaseLotteryActivity implements OnClickListener{
	private final boolean DEBUG = Settings.DEBUG;
	private final String TAG = "TAG";
	
	private final String DSDS = "大小单双";
	private final String ZI_11 = "一星直选";
	private final String ZI_21 = "二星直选";
	private final String ZU_22 = "二星组选";
	private final String ZI_31 = "三星直选";
	private final String ZU_33 = "三星组三";
	private final String ZU_36 = "三星组六";
	private final String ZI_41 = "四星直选";
	private final String ZI_51 = "五星直选";
	private final String TONG_58 = "五星通选";
	private final String REN_11 = "任选一";
	private final String REN_21 = "任选二";
	
//	private String[] mItem = {"大小单双","一星直选","二星直选","二星组选","三星直选",
//			"三星组三","三星组六","五星直选","五星通选"};
	private String[] mItem;
	private final String[] mItemSscCq = {DSDS, ZI_11, ZI_21, ZU_22, ZI_31, ZU_33, ZU_36, ZI_51, TONG_58};
	private final String[] mItemSscJx = {DSDS, ZI_11, ZI_21, ZU_22, ZI_31, ZU_33, ZU_36, ZI_41, ZI_51, TONG_58, REN_11, REN_21};
	
	/***大小单双***/
	private final int STATE_01 = 0Xad001;
	/***一星直选***/
	private final int STATE_11 = 0Xad011;
	/***二星直选***/
	private final int STATE_21 = 0Xad021;
	/***二星组选***/
	private final int STATE_22 = 0Xad022;
	/***三星直选***/
	private final int STATE_31 = 0Xad031;
	/***三星组三***/
	private final int STATE_33 = 0Xad033;
	/***三星组六***/
	private final int STATE_36 = 0Xad036;
	/***四星直选***/
	private final int STATE_41 = 0Xad041;
	/***五星直选***/
	private final int STATE_51 = 0Xad051;
	/***五星通选***/
	private final int STATE_58 = 0Xad058;

	/***任一***/
	private final int STATE_R1 = 0Xad601;
	/***任二***/
	private final int STATE_R2 = 0Xad602;
	
	/***状态***/
	private int state;
	/** 状态ID **/
	private int sId;
	
	/***玩法***/
	private String playId;
	
	 /**** 最小选择Ball count****/
	private int minSelect = 1;
	
	private PopWindowDialog popDialog;
	
	/***Title显示***/
	private String titleName;
	/***提示***/
	private String promptExplain;
	private String promptMoney;
	
	
	private FrameLayout topMenu;
	private TextView topMenuTv;
	private ImageButton history;//开奖历史
	
	private TextView promptExplainTv;
	private TextView promptMoneyTv;
	private TextView tvIssue;
	private TextView time_m_s,time_m_g,time_s_s,time_s_g,lottery_status_tv;
	
	private LinearLayout promptMoneyLay;
	private LinearLayout layoutView;
	private LayoutInflater inflater;
	private View view;
	private ArrayList<AutoWrapView> autoViews;
	
	/** 球的选中与取消的标志 **/
	private boolean isNumberSelect = true;
	private int autoWrapIndex = -1;
	private int sumBall = 10; //总球数
	private int sumView; //当前显示几个View
	private int item = 0;//注
	private int[] numberCount;
	private String[] split;
	
	private int index;//下标 用来确认当前要更改的List
	
	private ArrayList<BetNumberBean> lotteryBeans;
	private SelectInfoBean mInfoBean;
	
	private MyCountTimer mCountTimer;
	private NumeralMethodUtil methodUtil = new NumeralMethodUtil();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.frequent_choice, null);
		setcView(view);
		
		View viewTitle = inflater.inflate(R.layout.frequent_choice_title_bar_gp, null);
		setTitleBar(viewTitle);
		
		initViews();
		initShakeListener();
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
	}

	@Override
	public void onPause() {
		super.onPause();
		stopShakeLister();
	}

	@Override
	public void onResume() {
		super.onResume();
		startShakeLister();
	}

	private void initViews() {
		topMenu = (FrameLayout) findViewById(R.id.choice_button_frequent_sort);
		topMenuTv = (TextView) findViewById(R.id.choice_tv_frequent);
		
		history = (ImageButton) findViewById(R.id.frequent_countdown_last_result);
		promptExplainTv = (TextView) findViewById(R.id.frequent_choice_introduce_text1);
		promptMoneyTv = (TextView) findViewById(R.id.frequent_choice_introduce_text2);
		promptMoneyLay = (LinearLayout) findViewById(R.id.frequent_choice_layout_introduce);
		tvIssue = (TextView)findViewById(R.id.frequent_countdown_issue);
		time_m_s = (TextView)findViewById(R.id.frequent_countdown_time_m_s);
		time_m_g = (TextView)findViewById(R.id.frequent_countdown_time_m_g);
		time_s_s = (TextView)findViewById(R.id.frequent_countdown_time_s_s);
		time_s_g = (TextView)findViewById(R.id.frequent_countdown_time_s_g);
		lottery_status_tv = (TextView)findViewById(R.id.frequent_countdown_status);
		
		topMenu.setOnClickListener(this);
		history.setOnClickListener(this);
		
		promptMoneyLay.setVisibility(View.GONE);
		
		sId = SharedPreferencesOperate.getLotteryPlay(this, lid);
		if(lid.equals(LotteryId.SSCCQ))
			mItem = mItemSscCq;
		else
			mItem = mItemSscJx;
		
		state = getState(mItem[sId]);
		
		lotteryBeans = new ArrayList<BetNumberBean>();
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setInitNum(0);
		mInfoBean.setRepeat(false);
		
		countTimer();
		
		disposeData();
		
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
		long remainTime = LotteryId.getRemainTime(lid);
		issue = LotteryId.getIssue(lid);
		if(remainTime > 0){
			if(!TextUtils.isEmpty(issue)){
				int len = issue.length();
				if(len > 2){
					tvIssue.setText(issue.subSequence(2, len));
				}
//				if(lid.equals(LotteryId.SSCJX)){
//					String status = LotteryId.getIssueStatus(lid);
//					if(status.equals("0")){
//						lottery_status_tv.setVisibility(View.VISIBLE);
//						lottery_status_tv.setText("预售中");
//					}else if(status.equals("1")){
//						lottery_status_tv.setVisibility(View.VISIBLE);
//						lottery_status_tv.setText("销售中");
//					}
//				}
				if(mCountTimer != null){
					mCountTimer.cancel();
					mCountTimer = null;
				}
				mCountTimer = new MyCountTimer( remainTime, 1000, time_m_s, time_m_g, time_s_s, time_s_g);
				mCountTimer.start();
				mCountTimer.setOnCountTimer(new OnCountTimer() {
					@Override
					public void onCountTimer() {
						ToastUtil.diaplayMesShort(getApplicationContext(),  "正在获取最新期次");
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
	
	int tempItem;
	/***处理数据**/
	private void disposeData(){
		if(DEBUG) Log.i(TAG, "disposeData");
		sumBall = 10;
		tempItem = 1;
		getCopyWriter();
		switch(state){
		case STATE_01:
			sumBall = 4;
			sumView = 2;
			minSelect = 1;
			playId = "30";
			titleName = LotteryId.getLotteryName(lid) + "-大小单双";

			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1});
			mInfoBean.setSumBall(new int[]{4, 4});
			StatService.onEvent(SSCActivity.this, "ssc-daxiaodanshuang", "时时彩-大小单双", 1);
			break;
		case STATE_11:
			sumView = 1;
			minSelect = 1;
			playId = "01";
			titleName = LotteryId.getLotteryName(lid) + "-一星直选";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1});
			mInfoBean.setSumBall(new int[]{10});
			StatService.onEvent(SSCActivity.this, "ssc-1xingzhixuan", "时时彩-一星直选", 1);
			break;
		case STATE_21:
			sumView = 2;
			minSelect = 1;
			playId = "02";
			titleName = LotteryId.getLotteryName(lid) + "-二星直选";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1});
			mInfoBean.setSumBall(new int[]{10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-2xingzhixuan", "时时彩-二星直选", 1);
			break;
		case STATE_22:
			sumView = 1;
			minSelect = 2;
			playId = "06";
			titleName = LotteryId.getLotteryName(lid) + "-二星组选";
			
			split = new String[]{",", "#"};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{10});
			StatService.onEvent(SSCActivity.this, "ssc-2xingzuxuan", "时时彩-二星组选", 1);
			break;
		case STATE_31:
			sumView = 3;
			minSelect = 1;
			playId = "03";
			titleName = LotteryId.getLotteryName(lid) + "-三星直选";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1, 1});
			mInfoBean.setSumBall(new int[]{10, 10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-3xingzhixuan", "时时彩-三星直选", 1);
			break;
		case STATE_33:
			sumView = 1;
			minSelect = 2;
			playId = "07";
			titleName = LotteryId.getLotteryName(lid) + "-三星组三";
			
			split = new String[]{",", "#"};
			mInfoBean.setPollId("02");
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{10});
			tempItem = 2;
			StatService.onEvent(SSCActivity.this, "ssc-3xingzu3", "时时彩-三星组三", 1);
			break;
		case STATE_36:
			sumView = 1;
			minSelect = 3;
			playId = "08";
			titleName = LotteryId.getLotteryName(lid) + "-三星组六";
			
			split = new String[]{",", "#"};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{3});
			mInfoBean.setSumBall(new int[]{10});
			StatService.onEvent(SSCActivity.this, "ssc-3xingzu6", "时时彩-三星组六", 1);
			break;
		case STATE_41:
			sumView = 4;
			minSelect = 1;
			playId = "04";
			titleName = LotteryId.getLotteryName(lid) + "-四星直选";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1, 1, 1});
			mInfoBean.setSumBall(new int[]{10, 10, 10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-5xingzhixuan", "时时彩-四星直选", 1);
			break;
		case STATE_51:
			sumView = 5;
			minSelect = 1;
			playId = "05";
			titleName = LotteryId.getLotteryName(lid) + "-五星直选";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1, 1, 1, 1});
			mInfoBean.setSumBall(new int[]{10, 10, 10, 10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-5xingzhixuan", "时时彩-五星直选", 1);
			break;
		case STATE_58:
			sumView = 5;
			minSelect = 1;
			playId = "05";
			titleName = LotteryId.getLotteryName(lid) + "-五星通选";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("08");
			mInfoBean.setSelectBall(new int[]{1, 1, 1, 1, 1});
			mInfoBean.setSumBall(new int[]{10, 10, 10, 10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-5xingtongxuan", "时时彩-五星通选", 1);
			break;
		case STATE_R1:
			sumView = 5;
			minSelect = 1;
			playId = "11";
			titleName = LotteryId.getLotteryName(lid) + "-任选一";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1, 1, 1, 1});
			mInfoBean.setSumBall(new int[]{10, 10, 10, 10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-5xingtongxuan", "时时彩-五星通选", 1);
			break;
		case STATE_R2:
			sumView = 5;
			minSelect = 1;
			playId = "12";
			titleName = LotteryId.getLotteryName(lid) + "-任选二";
			
			split = new String[]{"", ","};
			mInfoBean.setPollId("01");
			mInfoBean.setSelectBall(new int[]{1, 1, 1, 1, 1});
			mInfoBean.setSumBall(new int[]{10, 10, 10, 10, 10});
			StatService.onEvent(SSCActivity.this, "ssc-5xingtongxuan", "时时彩-五星通选", 1);
			break;
		}
		
		lotteryBeans.clear();
		
		topMenuTv.setText(titleName);
		promptExplainTv.setText(promptExplain);
		promptMoneyTv.setText(promptMoney);
		
//		infoBean.setInfo(sumView, sumBall, minSelect);
		numberCount = new int[sumView];
		
		addView();
		clear();
		
		mInfoBean.setSumView(sumView);
		mInfoBean.setPlayId(playId);
		mInfoBean.setItem(tempItem);
		mInfoBean.setSplit(split);
	}

	private void getCopyWriter(){
		if(lid.equals(LotteryId.SSCCQ)){
			switch(state){
			case STATE_01:
				promptExplain = "所选大小单双与开奖号码后两位一致，且顺序相同，即中奖金4元";
				break;
			case STATE_11:
				promptExplain = "所选号码与开奖号码后一位一致，即中奖金10元";
				break;
			case STATE_21:
				promptExplain = "所选号码与开奖号码后两位一致，且顺序相同，即中奖金100元";
				break;
			case STATE_22:
				promptExplain = "所选号码与开奖号码后两位一致，且顺序不限，即中奖金50元";
				break;
			case STATE_31:
				promptExplain = "所选号码与开奖号码后三位一致，且顺序相同，即中奖金1000元";
				break;
			case STATE_33:
				promptExplain = "所选号码与开奖号码后三位一致，且顺序不限，即中奖金320元";
				break;
			case STATE_36:
				promptExplain = "所选号码与开奖号码后三位一致，且顺序不限，即中奖金160元";
				break;
			case STATE_41:
				break;
			case STATE_51:
				promptExplain = "所选号码与开奖号码一致，且顺序相同，即中奖金100000元";
				break;
			case STATE_58:
				promptExplain = "每位选1个或多个号码，按顺序全部命中，命中前三或者后三，命中前二或者后二，即中奖金20000/200/20元";
				break;
			}
		}else if(lid.equals(LotteryId.SSCJX)){
			switch(state){
			case STATE_01:
				promptExplain = "所选大小单双与开奖号码后两位一致，且顺序相同，即中奖金4元";
				break;
			case STATE_11:
				promptExplain = "所选号码与开奖号码后一位一致，即中奖金11元";
				break;
			case STATE_21:
				promptExplain = "所选号码与开奖号码后两位一致，且顺序相同，即中奖金116元";
				break;
			case STATE_22:
				promptExplain = "所选号码与开奖号码后两位一致，且顺序不限，即中奖金58元，对子号奖金116元（单式不支持对子号）";
				break;
			case STATE_31:
				promptExplain = "所选号码与开奖号码后三位一致，且顺序相同，即中奖金1160元";
				break;
			case STATE_33:
				promptExplain = "所选号码与开奖号码后三位一致，且顺序不限，即中奖金385元";
				break;
			case STATE_36:
				promptExplain = "所选号码与开奖号码后三位一致，且顺序不限，即中奖金190元";
				break;
			case STATE_41:
				promptExplain = "所选号码与开奖号码后四位一致，且顺序相同，即中奖金10000元；所选号码与开奖号码前三位或后三位开奖号码一致，且顺序相同，即中奖金88元";
				break;
			case STATE_51:
				promptExplain = "所选号码与开奖号码一致，且顺序相同，即中奖金116000元";
				break;
			case STATE_58:
				promptExplain = "每位选1个或多个号码，按顺序全部命中，命中前三或者后三，命中前二或者后二，即中奖金20000/200/30元";
				break;
			case STATE_R1:
				promptExplain = "从万位到个位任意一位选择号码进行投注，所选号码与开奖号码一致，且位置相同，即中奖金11元";
				break;
			case STATE_R2:
				promptExplain = "从万位到个位任意两位选择号码进行投注，所选号码与开奖号码一致，且位置相同，即中奖金116元";
				break;
			}
			
		}
	}
	
	/**添加界面Ball**/
	private void addView(){
		if(DEBUG) Log.i(TAG, "addView : " + sumView);
		layoutView = (LinearLayout) findViewById(R.id.frequent_choice_view_ball);
		inflater = LayoutInflater.from(this);
		autoViews = new ArrayList<AutoWrapView>(sumView);
		wrapView();
	}
	
	/****
	 * 画界面
	 * @param i 当前的
	 * @return View
	 */
	private void wrapView(){
		String strMany;
		layoutView.removeAllViews();
		
		int stateBall;
		if(state == STATE_01){
			stateBall = ViewUtil.COLOR_RED_RECT;
		}else{
			stateBall = ViewUtil.COLOR_RED;
		}
		
		for(int i = 0; i < sumView; i++){
			view = inflater.inflate(R.layout.many_view_item, null);
			AutoWrapView wrapView = (AutoWrapView) view.findViewById(R.id.many_view_item_auto_view);
			TextView manyTv= (TextView) view.findViewById(R.id.many_view_item_tv);
			
			ViewUtil.initBalls(getApplicationContext(), wrapView, sumBall, stateBall, ViewUtil.INIT_NUMBER_0, onClickListener);
			
			if(sumView > 1){
				strMany = viewManyTv(5 - sumView + i);
				manyTv.setText(strMany);
			}else{
				manyTv.setVisibility(View.GONE);
			}
			
			wrapView.setId(i);
			autoViews.add(wrapView);
			
			layoutView.addView(view);
		}
	}
	
	private String viewManyTv(int id){
		switch(id){
		case 0:
			return "万位";
		case 1:
			return "千位";
		case 2:
			return "百位";
		case 3:
			return "十位";
		case 4:
			return "个位";
		default :
			return "";
		}
	}
	
	/***显示Bet and Money***/
	private void countMoney(){
		if(state == STATE_R2){
			int s = 0;
			for (int i = 0; i < sumView; i++) {
				if (i != autoWrapIndex) {
					s += numberCount[i];
				}
			}
			if (isNumberSelect)
				item += s;
			else
				item -= s;
		}else if(state == STATE_R1){
			item = 0;
			for(int i = 0; i < sumView; i++){
				item += numberCount[i];
			}
		}else{
			if(sumView == 1){
				if(numberCount[0] >= minSelect){
					item = (int) MethodUtils.C_better(numberCount[0], minSelect);
					if(state == STATE_33)
						item <<= 1;
					else if(lid.equals(LotteryId.SSCJX) && state == STATE_22 && item > 1) //江西时时彩二星组选复式支持同号
						item += numberCount[0];
					
				}else{
					item = 0;
				}
			}else{
				item = 1;
				for(int i = 0; i < sumView; i++){
					if(numberCount[i] == 0){
						item = 0;
						break;
					}else
						item *= numberCount[i];
				}
			}
		}
		setBetAndMoney(item);
	}
	
	/**清除数据**/
	private void clear() {
		if(DEBUG) Log.i(TAG, "执行Clear");
		methodUtil.clearBottomNumeral(autoViews, numberCount);
		for(int i = 0; i < sumView; i++){
			numberCount[i] = 0;
		}
		item = 0;
		countMoney();
	}
	
	private void startActivity(){
//		if(!TextUtils.isEmpty(issue)){
			SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
			SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
			
			Intent intent = new Intent();
			intent.setClass(SSCActivity.this, CtOrderListActivity.class);
			intent.putExtra("lid", lid);
			intent.putExtra("issue", issue);
			intent.putExtra("playMethod", playId);
			startActivityForResult(intent, GetString.BLRequestCode);
			clear();
//		}else{
//			ToastUtil.diaplayMesShort(getApplicationContext(),  "获取最新期次失败，请返回主页刷新重试！");
//		}
	}
	
	/***PopWindow 左下拉条***/
	private void expandPopwindow() {
		if(popDialog == null){
			popDialog = new PopWindowDialog(this, mItem, sId);
			popDialog.setOnClickListener(new OnclickFrequentListener() {
				
				@Override
				public void onClick(View v) {
					index = 0;
					final ArrayList<CheckBox> list = popDialog.getList();
					CheckBox checkBox;
					for(int i = 0, size = list.size(); i < size; i++){
						checkBox = list.get(i);
						if(v.getId() == i){
							checkBox.setChecked(true);
							state = getState(checkBox.getText().toString());
							SharedPreferencesOperate.setLotteryPlay(SSCActivity.this, lid, i);
						}else{
							if(checkBox.isChecked()) checkBox.setChecked(false);
						}
					}
					disposeData();
					popDialog.dismiss();
				}
			});
			popDialog.setPopViewPosition();
			popDialog.show();
			
		}else{
			popDialog.show();
		}
	}

	private int getState(String lidName){
		if(lidName.equals(DSDS)) state = STATE_01;
		else if(lidName.equals(ZI_11)) state = STATE_11; 
		else if(lidName.equals(ZI_21)) state = STATE_21; 
		else if(lidName.equals(ZU_22)) state = STATE_22; 
		else if(lidName.equals(ZI_31)) state = STATE_31; 
		else if(lidName.equals(ZU_33)) state = STATE_33; 
		else if(lidName.equals(ZU_36)) state = STATE_36; 
		else if(lidName.equals(ZI_41)) state = STATE_41; 
		else if(lidName.equals(ZI_51)) state = STATE_51; 
		else if(lidName.equals(TONG_58)) state = STATE_58; 
		else if(lidName.equals(REN_11)) state = STATE_R1; 
		else if(lidName.equals(REN_21)) state = STATE_R2; 
		else {
			state = -1;
			ToastUtil.diaplayMesShort(getApplicationContext(), "该玩法暂未开通");
		}
//		int state = 0;
//		switch(lidName){
//		case 0:
//			state = STATE_01;
//			break;
//		case 1:
//			state = STATE_11;
//			break;
//		case 2:
//			state = STATE_21;
//			break;
//		case 3:
//			state = STATE_22;
//			break;
//		case 4:
//			state = STATE_31;
//			break;
//		case 5:
//			state = STATE_33;
//			break;
//		case 6:
//			state = STATE_36;
//			break;
//		case 7:
//			state = STATE_51;
//			break;
//		case 8:
//			state = STATE_58;
//			break;
//		}
		return state;
	}
	
	/***
	 * CheckBox 专用
	 */
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if(v instanceof CheckBox){
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoWiew = (AutoWrapView) checkBox.getParent();
				autoWrapIndex = autoWiew.getId();
//				for(int i = 0; i < sumView; i++){
//					if(i == autoWiew.getId()){
						if(state == STATE_01){
							numberCount[autoWrapIndex] = ViewUtil.clearOnlyBallRow(autoViews.get(autoWrapIndex), checkBox.getId(), numberCount[autoWrapIndex]);
//							((CheckBox) v).setChecked(true);
						}
//						if(DEBUG) Log.i(TAG,  ((CheckBox) v).isChecked() + "onClickListener : sumView true= "+i);
						if(((CheckBox) v).isChecked()) {
							isNumberSelect = true;
							numberCount[autoWrapIndex]++;
							if(state == STATE_22){
								if(numberCount[autoWrapIndex] > 7){
									numberCount[autoWrapIndex]--;
									((CheckBox)v).setChecked(false);
									ToastUtil.diaplayMesLong(getApplicationContext(), "您好,二星组选复式最多支持7个号码");
								}
							}else if(state == STATE_58){
								if(numberCount[autoWrapIndex] > 1){
									numberCount[autoWrapIndex]--;
									((CheckBox)v).setChecked(false);
									ToastUtil.diaplayMesLong(getApplicationContext(), "您好,五星通选暂不支持复式投注");
								}
							}
						}else{
							isNumberSelect = false;
							numberCount[autoWrapIndex]--;
						}
						
						
//					}
//				}
				countMoney();
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		if(methodUtil.clearBottom(numberCount) || lotteryBeans.size() > 0){
			NormalAlertDialog dialog = new NormalAlertDialog(SSCActivity.this);
			dialog.setTitle("提示");
			dialog.setContent("您确定退出投注吗？一旦退出,您的投注内容将被清空。");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					clear();
					lotteryBeans = null;
					finish();
				}

				@Override
				public void onCancleBtnClick() {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
		}else{
			super.onBackPressed();
		}
	}
	
	@Override
	public void bottom_clear(View v) {
		if(methodUtil.clearBottom(numberCount)){
			NormalAlertDialog dialog = new NormalAlertDialog(this);
			dialog.setContent("您确定要清空当前选择的号码吗？");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					clear();
				}

				@Override
				public void onCancleBtnClick() {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
		}else{
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_2);
		}
	}


	@Override
	public void bottom_random(View v) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(1);
		data.add(5);
		data.add(10);
		data.add(50);
		JXDialog dialog = new JXDialog(SSCActivity.this, data);
		dialog.setTitle("请选择机选注数");
		dialog.setColor(1);
		dialog.setOnItemClickListener(new OnButtonItemClickListener() {
			@Override
			public void onItemClickListener(final int num) {
				randomTask(num);
			}
		});
		dialog.show();
	}


	@Override
	public void bottom_submit(View v) {
		if(item < 1){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_0);
//			ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择1注进行投注");
			return;
		}
		if(item << 1 >= GetString.TopBetMoney){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_1);
		}else{
			String balls;
			try {
				balls = ViewUtil.getSelectNums(autoViews, split, ViewUtil.SELECT_NUMBER_MIN);
				BetNumberBean mNumberBean = new BetNumberBean();
				mNumberBean.setBuyNumber(balls);
				mNumberBean.setPlayId(playId);
				if(!mInfoBean.getPollId().equals("08")){
					mNumberBean.setPollId(item > 1 ? "02" : "01");
				}else{
					mNumberBean.setPollId(mInfoBean.getPollId());
				}
				mNumberBean.setItem(item);
				mNumberBean.setAmount(item * 2);
				lotteryBeans.add(index, mNumberBean);

				LogUtil.DefalutLog(mNumberBean);
				startActivity();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void top_rbtn_daigou(View v) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void top_rbtn_hemai(View v) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void top_menu(View v) {
		expandPopwindow();
	}


	@Override
	public void onShake() {
		if(state == STATE_01){
			ViewUtil.getRandomButton(autoViews, sumBall, sumView, numberCount);
		}else if(state == STATE_R1){
			clear();
			int[] index = MethodUtils.getRandomNumber(5, 1, 0, false, false);
			int[] select = MethodUtils.getRandomNumber(10, 1, 0, true, false);
			for(int i = 0; i < index.length; i++){
				CheckBox checkBox = (CheckBox) autoViews.get(index[i]).getChildAt(select[i]);
				if(!checkBox.isChecked())
					checkBox.setChecked(true);
				
				numberCount[index[i]] = 1;
			}
		}else if(state == STATE_R2){
			clear();
			int[] index = MethodUtils.getRandomNumber(5, 2, 0, false, false);
			int[] select = MethodUtils.getRandomNumber(10, 2, 0, true, false);
			for(int i = 0; i < index.length; i++){
				CheckBox checkBox = (CheckBox) autoViews.get(index[i]).getChildAt(select[i]);
				if(!checkBox.isChecked())
					checkBox.setChecked(true);
				
				numberCount[index[i]] = 1;
			}
			//给定机选出来的1注
			item = 1;
			setBetAndMoney(item);
			return;
		}else{
			for(int i=0;i<sumView;i++){
				ViewUtil.getRandomButton(autoViews.get(i), sumBall, minSelect);
				numberCount[i] = minSelect;
			}
		}
		countMoney();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		super.onActivityResult(requestCode, resultCode, mIntent);
		if(requestCode == GetString.BLRequestCode){
			index = 0; //设置初始值为0
			lotteryBeans = (ArrayList<BetNumberBean>) SafeApplication.dataMap.get(SafeApplication.MAP_LIST_KEY);
			mInfoBean = (SelectInfoBean) SafeApplication.dataMap.get(SafeApplication.MAP_INFO_KEY);
			
			if(lotteryBeans == null || mInfoBean == null){
				return;
			}
			
			split = mInfoBean.getSplit();
			
			if(resultCode == GetString.BLAlter){
				index = mIntent.getIntExtra("index", 0);
				BetNumberBean mNumberBean = lotteryBeans.get(index);
				lotteryBeans.remove(index);
				
				item = (int) mNumberBean.getItem();
				String number = mNumberBean.getBuyNumber();
				try {
					if(!TextUtils.isEmpty(number)){
						String[] temp = number.split(split[1]);
						if(split[0].equals(",")){
							numberCount[0] = number.split(split[0]).length;
						}else{
							for(int i = 0; i < sumView; i++){
								if(!temp[i].equals("_")){
									numberCount[i] = temp[i].length();
								}else{
									numberCount[i] = 0;
								}
							}
						}
						if(state == STATE_01) {
							ViewUtil.restore(autoViews, temp, split[0] ,"SSC_30");
						} else{
							ViewUtil.restore(autoViews, temp, split[0] ,"SSC");
						}
						setBetAndMoney(item);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.choice_button_frequent_sort){
			
			expandPopwindow();
			
		}else if(v.getId() == history.getId()){
			StatService.onEvent(SSCActivity.this, "recently result", "快彩类近期开奖", 1);
			Bundle mBundle = new Bundle();
			mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID,lid);
			Intent intent = new Intent();
			intent.setClass(SSCActivity.this, LotteryResultHistoryActivity.class);
			intent.putExtra(Settings.BUNDLE,mBundle);
			startActivity(intent);
		}
	}
	
	private void randomTask(final int sum){
		MyAsyncTask tast = new MyAsyncTask(SSCActivity.this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			
			@Override
			public void onTaskPostExecuteListener() {
//				ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				startActivity();
			}
			
			@Override
			public Boolean onTaskBackgroundListener() {
				return lotteryBeans.addAll(0, RandomSelectUtil.getRandomNumber(sum, mInfoBean));
//				return false;
			}
		});
		
		tast.execute(); 
	}
}