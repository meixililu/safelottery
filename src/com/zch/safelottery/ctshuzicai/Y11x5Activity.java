package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.LotteryResultHistoryActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.activity.ZouShiTuActivity;
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
import com.zch.safelottery.dialogs.PopWindowMultiDialog;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.FQ_BonusForeastUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.MethodUtils;
import com.zch.safelottery.util.NumeralMethodUtil;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.SharedPreferencesOperate;
import com.zch.safelottery.util.SplitTicketUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;
import com.zch.safelottery.util.XmlUtil;
import com.zch.safelottery.view.ListLineFeedView;

public class Y11x5Activity extends BaseLotteryActivity implements OnClickListener{
	public final boolean DEBUG = Settings.DEBUG;
	public final String TAG = "TAG";
	
	//普通
	/***前一***/
	private final int STATE_FRONT_1 = 0Xad000001;
	/***前二直选***/
	private final int STATE_FRONT_2_DIRECTLY = 0Xad000002;
	/***前二组选***/
	private final int STATE_FRONT_2_GROUP = 0Xad000003;
	/***前三直选***/
	private final int STATE_FRONT_3_DIRECTLY = 0Xad000004;
	/***前三组选***/
	private final int STATE_FRONT_3_GROUP = 0Xad000005;
	/***任二***/
	private final int STATE_ARBITRARILY_2 = 0Xad000006;
	/***任三***/
	private final int STATE_ARBITRARILY_3 = 0Xad000007;
	/***任四***/
	private final int STATE_ARBITRARILY_4 = 0Xad000008;
	/***任五***/
	private final int STATE_ARBITRARILY_5 = 0Xad000009;
	/***任六***/
	private final int STATE_ARBITRARILY_6 = 0Xad000010;
	/***任七***/
	private final int STATE_ARBITRARILY_7 = 0Xad000011;
	/***任八***/
	private final int STATE_ARBITRARILY_8 = 0Xad000012;
	
	//胆拖
	/***前二组选***/
	private final int STATE_FRONT_2_GROUP_TOWED = 0Xad000f03;
	/***前三组选***/
	private final int STATE_FRONT_3_GROUP_TOWED = 0Xad000f05;
	/***任二***/
	private final int STATE_ARBITRARILY_2_TOWED = 0Xad000f06;
	/***任三***/
	private final int STATE_ARBITRARILY_3_TOWED = 0Xad000f07;
	/***任四***/
	private final int STATE_ARBITRARILY_4_TOWED = 0Xad000f08;
	/***任五***/
	private final int STATE_ARBITRARILY_5_TOWED = 0Xad000f09;
	/***任六***/
	private final int STATE_ARBITRARILY_6_TOWED = 0Xad000f10;
	/***任七***/
	private final int STATE_ARBITRARILY_7_TOWED = 0Xad000f11;
	
	/***状态***/
	private int state;
	/** 状态ID **/
	private int sId;
	/** 操作标 是分别操作普通投注 1  胆拖投注 2 **/
	private int type;
	
	/***玩法***/
	private String playId;
	/***选号方式***/
	private String pollId;;
	
//	private String[] mItem = {"前一：奖金13元","前二直选：奖金130元","前二组选：奖金65元","前三直选：奖金1170元","前三组选：奖金195元",
//			"任选二：奖金6元","任选三：奖金19元","任选四：奖金78元","任选五：奖金540元","任选六：奖金90元","任选七：奖金26元","任选八：奖金9元"};
	private String[] mItem1 = {"前一","前二直选","前二组选","前三直选","前三组选",
			"任选二","任选三","任选四","任选五","任选六","任选七","任选八"};
	private String[] mItem2 = {"前二组选","前三组选","任选二","任选三","任选四",
			"任选五","任选六","任选七"};
	private PopWindowMultiDialog popDialog;
	
	/***Title显示***/
	private String titleName;
	/***提示***/
	private String promptExplain;
	private String promptMoney;
	
	private String mTowedPromptExplain;
	
	private FrameLayout topMenu;
	private TextView topMenuTv;
	private Button topChartBtn;
	private ImageButton history;//开奖历史
	
	private TextView promptExplainTv;
	private TextView promptMoneyTv;
	private TextView tvIssue;
	private TextView time_m_s,time_m_g,time_s_s,time_s_g;
	private LinearLayout layoutView;
	

	private LinearLayout lay;
	private TextView tvTowedPromptExplain;
	private LinearLayout layTowedView;
	
	private LayoutInflater inflater;
	private View view;
	private ArrayList<AutoWrapView> autoViews;
	private AutoWrapView auto;
	
	private int sumBall = 11; //总球数
	private int sumView; //当前显示几个View
	private int item = 0;//注数
//	private int mBonusBet;//中奖注数
	private int[] mBonusA;//每注的奖金
	
	 /**** 最小选择Ball count****/
	private int minSelect = 1;
	
	/** 选择胆码个数 **/
	private int selectTowed = 0;
	
	/** 选中的号码个数 **/
	private int[] numberCount;
	/** 重复的号码个数 **/
	private int[] repeatCount;
	/** 重复无效的注数 **/
	private int repeatItem;
	
	private ArrayList<BetNumberBean> lotteryBeans;
	private BetNumberBean alertBean;
	private SelectInfoBean mInfoBean;
	
	private boolean isAlertBean;
	
	private String lotteryName;
	
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
		
		initShakeListener();
		initViews();
		
//		ImageDownload.showIndexPageGuide(this,ImageDownload.DanTuoGaoPin,R.drawable.guide_dantuo_l,
//				Gravity.TOP,Gravity.LEFT,70,22);
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
		if(pollId.equals("03")){
			stopShakeLister();
		}else{
			startShakeLister();
		}
	}

	private void initViews() {
		topMenu = (FrameLayout) findViewById(R.id.choice_button_frequent_sort);
		topMenuTv = (TextView) findViewById(R.id.choice_tv_frequent);
		topChartBtn = (Button) findViewById(R.id.choice_btn_frequet_chart);
		
		history = (ImageButton) findViewById(R.id.frequent_countdown_last_result);
		promptExplainTv = (TextView) findViewById(R.id.frequent_choice_introduce_text1);
		promptMoneyTv = (TextView) findViewById(R.id.frequent_choice_introduce_text2);
		layoutView = (LinearLayout) findViewById(R.id.frequent_choice_view_ball);
		tvIssue = (TextView)findViewById(R.id.frequent_countdown_issue);
		time_m_s = (TextView)findViewById(R.id.frequent_countdown_time_m_s);
		time_m_g = (TextView)findViewById(R.id.frequent_countdown_time_m_g);
		time_s_s = (TextView)findViewById(R.id.frequent_countdown_time_s_s);
		time_s_g = (TextView)findViewById(R.id.frequent_countdown_time_s_g);
		
		//胆相关
		lay = (LinearLayout) findViewById(R.id.frequent_choice_towed_lay);
		tvTowedPromptExplain = (TextView) findViewById(R.id.frequent_choice_introduce_text1_towed);
		layTowedView = (LinearLayout) findViewById(R.id.frequent_choice_view_ball_towed);
		
		addViewTowed();
		
		topChartBtn.setVisibility(View.VISIBLE);
		
		tvIssue.setText(issue);
		topMenu.setOnClickListener(this);
		topChartBtn.setOnClickListener(this);
		history.setOnClickListener(this);
		
		sId = SharedPreferencesOperate.getLotteryPlay(getApplicationContext(), lid);
		type = SharedPreferencesOperate.getLotteryPlay(getApplicationContext(), lid + "type");
		if(type == 2){
			state = getState2(sId);
		}else{
			state = getState1(sId);
		}
		
		lotteryBeans = new ArrayList<BetNumberBean>();
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setRepeat(false);
		mInfoBean.setInitNum(1);
		mInfoBean.setSplit(new String[]{",", "#"});
		mInfoBean.setLotteryId(lid);
		
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
				tvIssue.setText(issue);
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
			ToastUtil.diaplayMesShort(getApplicationContext(), "期次已过期，请返回主页刷新重试！");
		}
	}
	
	/***处理数据**/
	private void disposeData(){
		if(DEBUG) Log.i(TAG, "disposeData");
		mInfoBean.setItem(1);
		mInfoBean.setPollId("01");
		lotteryName = LotteryId.getLotteryName(lid);
		
		switch(state){
		case STATE_FRONT_1:
			sumView = 1;
			minSelect = 1;
			playId = "01";
			titleName = lotteryName + "-普通-前一";
			promptExplain = "至少选中2个号码投注，命中开奖号码第1位即中奖！";
			promptMoney = "奖金：13元";
			
			mInfoBean.setItem(2);
			mInfoBean.setPollId("02");
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{13};
			StatService.onEvent(Y11x5Activity.this, "5in11-qian1", "11选5-前一", 1);
			break;
		case STATE_FRONT_2_DIRECTLY:
			sumView = 2;
			minSelect = 1;
			playId = "10";
			titleName = lotteryName + "-普通-前二直选";
			promptExplain = "每位各选1个或多个号码投注，按位置全部命中即中奖！";
			promptMoney = "奖金：130元";
			
			mInfoBean.setSelectBall(new int[]{1, 1});
			mInfoBean.setSumBall(new int[]{11, 11});
			mBonusA = new int[]{130};
			StatService.onEvent(Y11x5Activity.this, "5in11-qian2zhixuan", "11选5-前二直选", 1);
			break;
		case STATE_FRONT_2_GROUP:
			sumView = 1;
			minSelect = 2;
			playId = "12";
			titleName = lotteryName + "-普通-前二组选";
			promptExplain = "至少选择2个号码投注，投注号码与开奖号码前两位一致，位置不限即为中奖！";
			promptMoney = "奖金：65元";
			
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{65};
			StatService.onEvent(Y11x5Activity.this, "5in11-qian2zuxuan", "11选5-前二组选", 1);
			break;
		case STATE_FRONT_3_DIRECTLY:
			sumView = 3;
			minSelect = 1;
			playId = "11";
			titleName = lotteryName + "-普通-前三直选";
			promptExplain = "每位各选1个或多个号码投注，按位置全部命中即中奖！";
			promptMoney = "奖金：1170元";
			
			mInfoBean.setSelectBall(new int[]{1, 1, 1});
			mInfoBean.setSumBall(new int[]{11, 11, 11});
			mBonusA = new int[]{1170};
			StatService.onEvent(Y11x5Activity.this, "5in11-qian3zhixuan", "11选5-前三直选", 1);
			break;
		case STATE_FRONT_3_GROUP:
			sumView = 1;
			minSelect = 3;
			playId = "13";
			titleName = lotteryName + "-普通-前三组选";
			promptExplain = "至少选择3个号码投注，投注号码与开奖号码前三位一致，位置不限即为中奖！";
			promptMoney = "奖金：195元";
			
			mInfoBean.setSelectBall(new int[]{3});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{195};
			StatService.onEvent(Y11x5Activity.this, "5in11-qian3zuxuan", "11选5-前三组选", 1);
			break;
		case STATE_ARBITRARILY_2:
			sumView = 1;
			minSelect = 2;
			playId = "02";
			titleName = lotteryName + "-普通-任选二";
			promptExplain = "至少选择2个号码投注，命中任意2位即中奖！";
			promptMoney = "奖金：6元";
			
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{6};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan2", "11选5-任选二", 1);
			break;
		case STATE_ARBITRARILY_3:
			sumView = 1;
			minSelect = 3;
			playId = "03";
			titleName = lotteryName + "-普通-任选三";
			promptExplain = "至少选择3个号码投注，命中任意3位即中奖！";
			promptMoney = "奖金：19元";
			
			mInfoBean.setSelectBall(new int[]{3});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{19};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan3", "11选5-任选三", 1);
			break;
		case STATE_ARBITRARILY_4:
			sumView = 1;
			minSelect = 4;
			playId = "04";
			titleName = lotteryName + "-普通-任选四";
			promptExplain = "至少选择4个号码投注，命中任意4位即中奖！";
			promptMoney = "奖金：78元";
			
			mInfoBean.setSelectBall(new int[]{4});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{78};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan4", "11选5-任选四", 1);
			break;
		case STATE_ARBITRARILY_5:
			sumView = 1;
			minSelect = 5;
			playId = "05";
			titleName = lotteryName + "-普通-任选五";
			promptExplain = "至少选择5个号码投注，全部命中即中奖！";
			promptMoney = "奖金：540元";
			
			mInfoBean.setSelectBall(new int[]{5});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{540};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan5", "11选5-任选五", 1);
			break;
		case STATE_ARBITRARILY_6:
			sumView = 1;
			minSelect = 6;
			playId = "06";
			titleName = lotteryName + "-普通-任选六";
			promptExplain = "至少选择6个号码投注，选号中任意5位与开奖号码一致即中奖！";
			promptMoney = "奖金：90元";
			
			mInfoBean.setSelectBall(new int[]{6});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{90};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan6", "11选5-任选六", 1);
			break;
		case STATE_ARBITRARILY_7:
			sumView = 1;
			minSelect = 7;
			playId = "07";
			titleName = lotteryName + "-普通-任选七";
			promptExplain = "至少选择7个号码投注，选号中任意5位与开奖号码一致即中奖！";
			promptMoney = "奖金：26元";
			
			mInfoBean.setSelectBall(new int[]{7});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{26};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan7", "11选5-任选七", 1);
			break;
		case STATE_ARBITRARILY_8:
			sumView = 1;
			minSelect = 8;
			playId = "08";
			titleName = lotteryName + "-普通-任选八";
			promptExplain = "选择8个号码投注，选号中任意5位与开奖号码一致即中奖！";
			promptMoney = "奖金：9元";
			
			mInfoBean.setSelectBall(new int[]{8});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{9};
			StatService.onEvent(Y11x5Activity.this, "5in11-renxuan8", "11选5-任选八", 1);
			break;
		case STATE_FRONT_2_GROUP_TOWED:
			sumView = 1;
			minSelect = 2;
			playId = "12";
			titleName = lotteryName + "-胆拖-前二组选";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多1个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中前两位位置不限即中奖65元";
			
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{65};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-qian2", "11选5-胆拖-前二组选", 1);
			break;
		case STATE_FRONT_3_GROUP_TOWED:
			sumView = 1;
			minSelect = 3;
			playId = "13";
			titleName = lotteryName + "-胆拖-前三组选";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多2个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中前三位位置不限即中奖195元";
			
			mInfoBean.setSelectBall(new int[]{3});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{195};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-qian3", "11选5-胆拖-前三组选", 1);
			break;
		case STATE_ARBITRARILY_2_TOWED:
			sumView = 1;
			minSelect = 2;
			playId = "02";
			titleName = lotteryName + "-胆拖-任选二";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多1个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中任意2位即中奖6元";
			
			mInfoBean.setSelectBall(new int[]{2});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{6};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-2", "11选5-胆拖-任选二", 1);
			break;
		case STATE_ARBITRARILY_3_TOWED:
			sumView = 1;
			minSelect = 3;
			playId = "03";
			titleName = lotteryName + "-胆拖-任选三";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多2个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中任意3位即中奖19元";
			
			mInfoBean.setSelectBall(new int[]{3});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{19};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-3", "11选5-胆拖-任选三", 1);
			break;
		case STATE_ARBITRARILY_4_TOWED:
			sumView = 1;
			minSelect = 4;
			playId = "04";
			titleName = lotteryName + "-胆拖-任选四";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多3个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中任意4位即中奖78元";
			
			mInfoBean.setSelectBall(new int[]{4});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{78};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-4", "11选5-胆拖-任选四", 1);
			break;
		case STATE_ARBITRARILY_5_TOWED:
			sumView = 1;
			minSelect = 5;
			playId = "05";
			titleName = lotteryName + "-胆拖-任选五";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多4个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中任意5位即中奖540元";
			
			mInfoBean.setSelectBall(new int[]{5});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{540};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-5", "11选5-胆拖-任选五", 1);
			break;
		case STATE_ARBITRARILY_6_TOWED:
			sumView = 1;
			minSelect = 6;
			playId = "06";
			titleName = lotteryName + "-胆拖-任选六";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多5个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中任意5位即中奖90元";
			
			mInfoBean.setSelectBall(new int[]{6});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{90};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-6", "11选5-胆拖-任选六", 1);
			break;
		case STATE_ARBITRARILY_7_TOWED:
			sumView = 1;
			minSelect = 7;
			playId = "07";
			titleName = lotteryName + "-胆拖-任选七";
			mTowedPromptExplain = "我认为必出的号码，至少1个，最多6个";
			promptExplain = "我认为可能出的号码";
			promptMoney = "命中任意5位即中奖26元";
			
			mInfoBean.setSelectBall(new int[]{7});
			mInfoBean.setSumBall(new int[]{11});
			mBonusA = new int[]{26};
			StatService.onEvent(Y11x5Activity.this, "5in11-dt-7", "11选5-胆拖-任选七", 1);
			break;
		}
		isAlertBean = false;
		lotteryBeans.clear();
		
		topMenuTv.setText(titleName);
		promptExplainTv.setText(promptExplain);
		promptMoneyTv.setText(promptMoney);
		
		tvTowedPromptExplain.setText(mTowedPromptExplain);
		
//		infoBean.setInfo(sumView, sumBall, minSelect);
		numberCount = new int[sumView];
		if(sumView > 2){
			repeatCount = new int[sumView + 1];
		}
		addView();
		countMoney();
		
		mInfoBean.setSumView(sumView);
		mInfoBean.setPlayId(playId);
		
	}

	private void setTowed(boolean bool){
		if(bool){
			lay.setVisibility(View.VISIBLE);
		}else{
			lay.setVisibility(View.GONE);
		}
	}
	
	/**添加界面Ball**/
	private void addView(){
		if(DEBUG) Log.i(TAG, "addView : " + sumView);
		inflater = LayoutInflater.from(this);
		autoViews = new ArrayList<AutoWrapView>(sumView);
		
		wrapView();
		
	}

	/**
	 * 添加胆码选择数据
	 */
	private void addViewTowed(){
		if(DEBUG) Log.i(TAG, "addView : " + sumView);
		layTowedView.removeAllViews();
		selectTowed = 0;
		view = LayoutInflater.from(this).inflate(R.layout.many_view_item, null);
		auto = (AutoWrapView) view.findViewById(R.id.many_view_item_auto_view);
		TextView manyTv= (TextView) view.findViewById(R.id.many_view_item_tv);
		manyTv.setText("胆码");
		ViewUtil.initBalls(getApplicationContext(), auto, sumBall, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener2);
		layTowedView.addView(view);
	}
	
	/****
	 * 画界面
	 * @param i 当前的
	 * @return View
	 */
	private void wrapView(){
		if(DEBUG) Log.i(TAG, "wrapView : ");
		String strMany;
		layoutView.removeAllViews();
		for(int i = 0; i < sumView; i++){
			view = inflater.inflate(R.layout.many_view_item, null);
			AutoWrapView wrapView = (AutoWrapView) view.findViewById(R.id.many_view_item_auto_view);
			TextView manyTv= (TextView) view.findViewById(R.id.many_view_item_tv);
			
			ViewUtil.initBalls(getApplicationContext(), wrapView, sumBall, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
			
			if(sumView > 1){
				strMany = viewManyTv(i);
				manyTv.setText(strMany);
	//			if(DEBUG) Log.i(TAG, "wrapViewId : "+wrapView.getId());
			}else{
				if(pollId.equals("03")){
					manyTv.setText("拖码");
				}else{
					manyTv.setVisibility(View.GONE);
				}
			}
			
			wrapView.setId(i);
			autoViews.add(wrapView);
			
			layoutView.addView(view);
		}
	}
	
	private String viewManyTv(int id){
		switch(id){
		case 0:
			return "一位";
		case 1:
			return "二位";
		case 2:
			return "三位";
		default :
			return "";
		}
	}
	
	/***显示Bet and Money***/
	private void countMoney(){
		if(sumView == 1){
			if(pollId.equals("03")){
				if(selectTowed > 0){
					if(numberCount[0] > minSelect - selectTowed){
						item = (int) MethodUtils.C_better(numberCount[0], minSelect - selectTowed);
					}else{
						item = 0;
					}
				}else{
					item = 0;
				}
			}else{
				if(numberCount[0] >= minSelect){
					item = (int) MethodUtils.C_better(numberCount[0], minSelect);
//				 if(DEBUG) Log.i(TAG, numberCount[0] + " ---> mBetCount : "+mBetCount);
				}else{
					item = 0;
				}
			}
		}else{
			if(sumView == 2){
				repeatItem = ViewUtil.getRepeat(autoViews.get(0).getSelect(), autoViews.get(1).getSelect()).size();
			}else if(sumView == 3){
				repeatCount[0] = ViewUtil.getRepeat(autoViews.get(0).getSelect(), autoViews.get(1).getSelect()).size();
				repeatCount[1] = ViewUtil.getRepeat(autoViews.get(0).getSelect(), autoViews.get(2).getSelect()).size();
				repeatCount[2] = ViewUtil.getRepeat(autoViews.get(1).getSelect(), autoViews.get(2).getSelect()).size();
				repeatCount[3] = ViewUtil.getRepeat(ViewUtil.getRepeat(autoViews.get(0).getSelect(), autoViews.get(1).getSelect()), ViewUtil.getRepeat(autoViews.get(0).getSelect(), autoViews.get(2).getSelect())).size();
				repeatItem = repeatCount[0] * numberCount[2] + (repeatCount[1] * numberCount[1] - repeatCount[3]) + (repeatCount[2] * numberCount[0] - repeatCount[3]);
			}
			item = 1;
			for(int i = 0; i < sumView; i++){
				if(numberCount[i] == 0){
					item = 0;
					break;
				}else
					item *= numberCount[i];
				
//				if(DEBUG) Log.i(TAG, i + " ---> mBetCount : "+mBetCount);
			}
			item = item - repeatItem;
		}
		setBetAndMoney(item);
		if(item > 0){
			//奖金预测
			double[] bonusDou = FQ_BonusForeastUtil.countBonus(FQ_BonusForeastUtil.countBet(lid, playId, numberCount[0]), mBonusA);
			double[] profitDou = {bonusDou[0] - item * 2, bonusDou[1] - item * 2};
			
			String[] bonus = {GetString.df_0.format(bonusDou[0]), GetString.df_0.format(bonusDou[1])};
			String[] profit = {GetString.df_0.format(profitDou[0]), GetString.df_0.format(profitDou[1])};
			String teamStr = "如中奖[奖金:" + bonus[0] + "元至" + bonus[1] + "元,盈利:" + profit[0] + "元至" + profit[1] + "元]";
			int index = "如中奖[奖金:".length();
			
			//改变Money颜色
			SpannableString msp = new SpannableString(teamStr);
			msp.setSpan(new ForegroundColorSpan(Color.RED), index, index = index + bonus[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			msp.setSpan(new ForegroundColorSpan(Color.RED), index = index + "元至".length(), index = index + bonus[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			msp.setSpan(new ForegroundColorSpan(Color.RED), index = index + "元,盈利:".length(), index = index + profit[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			msp.setSpan(new ForegroundColorSpan(Color.RED), index = index + "元至".length(), index = index + profit[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			setBonusForeast(msp);
		}else{
			setBonusForeast(null);
		}
	}
	
	/**清除数据**/
	private void clear() {
		if(DEBUG) Log.i(TAG, "执行Clear");
		methodUtil.clearBottomNumeral(autoViews, numberCount);
		for(int i = 0; i < sumView; i++){
			numberCount[i] = 0;
		}
		ViewUtil.clearBall(auto);
		selectTowed = 0;
		countMoney();
	}
	
	private void startActivity(){
//		if(!TextUtils.isEmpty(issue)){
			SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
			SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
			
			Intent intent = new Intent();
			intent.setClass(Y11x5Activity.this, CtOrderListActivity.class);
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
//	private void expandPopwindow() {
//		if(popDialog == null){
//			popDialog = new PopWindowDialog(this, mItem, sId);
//			popDialog.setOnClickListener(new OnclickFrequentListener() {
//				
//				@Override
//				public void onClick(View v) {
//					if(DEBUG) Log.i("TAG", "position : "+v.getId());
//					final ArrayList<CheckBox> list = popDialog.getList();
//					index = 0;
//					CheckBox checkBox;
//					for(int i = 0, size = list.size(); i < size; i++){
//						checkBox = list.get(i);
//						if(v.getId() == i){
//							checkBox.setChecked(true);
//							state = getState(i);
//							SharedPreferencesLotteryPlay.setLotteryPlay(getApplicationContext(), lid, i);
//						}else{
//							if(checkBox.isChecked()) checkBox.setChecked(false);
//						}
//					}
//					disposeData();
//					popDialog.dismiss();
//				}
//			});
//			popDialog.setPopViewPosition();
//			popDialog.show();
//			
//		}else{
//			popDialog.show();
//		}
//	}
	
	
	private List<CheckBox> list1;
	private List<CheckBox> list2;
	private void expandPopwindow() {
		if(popDialog == null){
			popDialog = new PopWindowMultiDialog(this);
			list1 =  XmlUtil.getCheckBoxWithMargin(this, R.layout.select_popwindow_frequent_item, onClick1, mItem1, -1, 0);
			list2 =  XmlUtil.getCheckBoxWithMargin(this, R.layout.select_popwindow_frequent_item, onClick2, mItem2, -1, 0);
			
			if(type == 2){
				list2.get(sId).setChecked(true);
			}else{
				list1.get(sId).setChecked(true);
			}
			
			popDialog.content("普通投注", new ListLineFeedView(this, list1));
			popDialog.content("胆拖投注", new ListLineFeedView(this, list2));
			
			popDialog.setPopViewPosition();
			popDialog.show();
			popDialog.addContent();
		}else{
			popDialog.show();
		}
	}

	private ListLineFeedView.OnClickListListener onClick1 = new ListLineFeedView.OnClickListListener() {
		
		@Override
		public void onClick(View v) {
			index = 0;
			CheckBox checkBox;
			for(int i = 0, size = list2.size(); i < size; i++){
				checkBox = list2.get(i);
				if(checkBox.isChecked()) checkBox.setChecked(false);
			}
			for(int i = 0, size = list1.size(); i < size; i++){
				checkBox = list1.get(i);
				if(v.getId() == i){
					checkBox.setChecked(true);
					state = getState1(i);
					SharedPreferencesOperate.setLotteryPlay(getApplicationContext(), lid + "type", 1);
					SharedPreferencesOperate.setLotteryPlay(getApplicationContext(), lid, i);
				}else{
					if(checkBox.isChecked()) checkBox.setChecked(false);
				}
			}
			disposeData();
			popDialog.dismiss();
		}
	};
	private ListLineFeedView.OnClickListListener onClick2 = new ListLineFeedView.OnClickListListener() {
		
		@Override
		public void onClick(View v) {
			index = 0;
			CheckBox checkBox;
			for(int i = 0, size = list1.size(); i < size; i++){
				checkBox = list1.get(i);
				if(checkBox.isChecked()) checkBox.setChecked(false);
			}
			for(int i = 0, size = list2.size(); i < size; i++){
				checkBox = list2.get(i);
				if(v.getId() == i){
					checkBox.setChecked(true);
					state = getState2(i);
					SharedPreferencesOperate.setLotteryPlay(getApplicationContext(), lid + "type", 2);
					SharedPreferencesOperate.setLotteryPlay(getApplicationContext(), lid, i);
				}else{
					if(checkBox.isChecked()) checkBox.setChecked(false);
				}
			}
			disposeData();
			popDialog.dismiss();
		}
	};
	
	private int getState1(int id){
		int state = 0;
		pollId = "01";
		setTowed(false);
		startShakeLister();
		switch(id){
		case 0:
			state = STATE_FRONT_1;
			break;
		case 1:
			state = STATE_FRONT_2_DIRECTLY;
			break;
		case 2:
			state = STATE_FRONT_2_GROUP;
			break;
		case 3:
			state = STATE_FRONT_3_DIRECTLY;
			break;
		case 4:
			state = STATE_FRONT_3_GROUP;
			break;
		case 5:
			state = STATE_ARBITRARILY_2;
			break;
		case 6:
			state = STATE_ARBITRARILY_3;
			break;
		case 7:
			state = STATE_ARBITRARILY_4;
			break;
		case 8:
			state = STATE_ARBITRARILY_5;
			break;
		case 9:
			state = STATE_ARBITRARILY_6;
			break;
		case 10:
			state = STATE_ARBITRARILY_7;
			break;
		case 11:
			state = STATE_ARBITRARILY_8;
			break;
		}
		return state;
	}
	
	private int getState2(int id){
		int state = 0;
		pollId = "03";
		setTowed(true);
		addViewTowed();
		stopShakeLister();
		switch(id){
		case 0:
			state = STATE_FRONT_2_GROUP_TOWED;
			break;
		case 1:
			state = STATE_FRONT_3_GROUP_TOWED;
			break;
		case 2:
			state = STATE_ARBITRARILY_2_TOWED;
			break;
		case 3:
			state = STATE_ARBITRARILY_3_TOWED;
			break;
		case 4:
			state = STATE_ARBITRARILY_4_TOWED;
			break;
		case 5:
			state = STATE_ARBITRARILY_5_TOWED;
			break;
		case 6:
			state = STATE_ARBITRARILY_6_TOWED;
			break;
		case 7:
			state = STATE_ARBITRARILY_7_TOWED;
			break;
		}
		return state;
	}
	
	/***
	 * CheckBox 专用
	 */
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if(DEBUG) Log.i(TAG, "onClickListener : onClick");
			if(v instanceof CheckBox){
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoWiew = (AutoWrapView) checkBox.getParent();
//				if(DEBUG) Log.i(TAG, "onClickListener : 当前点击的 CheckBox = "+autoWiew.getId());
				for(int i = 0; i < sumView; i++){
					if(i == autoWiew.getId()){
						if(DEBUG) Log.i(TAG,  ((CheckBox) v).isChecked() + "onClickListener : sumView true= "+i);
						if(((CheckBox) v).isChecked()) {
							numberCount[i]++;
						}else{
							numberCount[i]--;
						}
					}else{
						if(DEBUG) Log.i(TAG, "onClickListener : sumView false= " + i);
//						numberCount[i] = ViewUtil.clearOnlyBallCol(autoViews.get(i), checkBox.getId() - 1, numberCount[i]);
					}
				}
				if(pollId.equals("03"))
					selectTowed = ViewUtil.clearOnlyBallCol(auto, checkBox.getId() - 1, selectTowed);
				
				countMoney();
			}
		}
	};
	
	private OnClickListener onClickListener2 = new OnClickListener() {
		public void onClick(View v) {
			if(DEBUG) Log.i(TAG, "onClickListener : onClick");
			if(v instanceof CheckBox){
				shake();
				CheckBox checkBox = (CheckBox) v;
//				if(DEBUG) Log.i(TAG, "onClickListener : 当前点击的 CheckBox = "+autoWiew.getId());
				if (checkBox.isChecked()) {
					if(selectTowed < minSelect - 1){
						selectTowed++;
					}else{
						checkBox.setChecked(false);
					}
				} else {
					selectTowed--;
				}
				
				numberCount[0] = ViewUtil.clearOnlyBallCol(autoViews.get(0), checkBox.getId() - 1, numberCount[0]);
				countMoney();
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		if(methodUtil.clearBottom(numberCount) || lotteryBeans.size() > 0){
			NormalAlertDialog dialog = new NormalAlertDialog(Y11x5Activity.this);
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
		if(pollId.equals("03")){
			ToastUtil.diaplayMesLong(this, "胆拖不支持机选");
		}else{
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.add(1);
			data.add(5);
			data.add(10);
			data.add(50);
			JXDialog dialog = new JXDialog(Y11x5Activity.this, data);
			dialog.setTitle("请选择机选注数");
			dialog.setColor(1);
			dialog.setOnItemClickListener(new OnButtonItemClickListener() {
				@Override
				public void onItemClickListener(final int num) {
					if(isAlertBean){
						lotteryBeans.remove(alertBean);
					}
					randomTask(num);
				}
			});
			dialog.show();
		}
	}

	SplitTicketUtil mSplitTicketUtil;
	@Override
	public void bottom_submit(View v) {
		if(item < 1){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_0);
//			ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择1注进行投注");
			return;
		}
		
		if(state == STATE_FRONT_1){
			if(!(item > 1)){
				NormalAlertDialog mDialog = new NormalAlertDialog(this, "前一玩法暂不支持单式投注，请至少选择2个号码。");
				mDialog.setType(1);
				mDialog.show();
				return;
			}
		}
		if(item << 1 >= GetString.TopBetMoney){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_1);
		}else{
			String balls;
			try {
				mSplitTicketUtil = SplitTicketUtil.getSplitTicketUtil();
				balls = ViewUtil.getSelectNums(autoViews, ViewUtil.SELECT_SPLIT_MAX, ViewUtil.SELECT_NUMBER_MAX);
//				if(lid.equals(LotteryId.GDSYXW) && state == STATE_FRONT_2_DIRECTLY || lid.equals(LotteryId.GDSYXW) && state == STATE_FRONT_3_DIRECTLY){
//					mSplitTicketUtil.start(balls, ViewUtil.SELECT_SPLIT_MAX, ViewUtil.SELECT_SPLIT_MIN);
//					List<String> ballLists = mSplitTicketUtil.getRestlt();
//					ArrayList<BetNumberBean> tempBeans = new ArrayList<BetNumberBean>();
//					for(String s: ballLists){
//						BetNumberBean mNumberBean = new BetNumberBean();
//						mNumberBean.setBuyNumber(s);
//						mNumberBean.setPlayId(playId);
//						mNumberBean.setPollId("01");
//						mNumberBean.setItem(1);
//						mNumberBean.setAmount(2);
//						tempBeans.add(mNumberBean);
//						LogUtil.DefalutLog(mNumberBean);
//					}
//					lotteryBeans.addAll(index, tempBeans);
//				}else{
					if(pollId.equals("03")){
						balls = ViewUtil.getSelectNums(auto, ViewUtil.SELECT_NUMBER_MAX) + "@" + balls;
					}
					BetNumberBean mNumberBean = new BetNumberBean();
					mNumberBean.setBuyNumber(balls);
					mNumberBean.setPlayId(playId);
					mNumberBean.setPollId(pollId.equals("03")? pollId: item > 1? "02": "01");
					mNumberBean.setItem(item);
					mNumberBean.setAmount(item * 2);
					lotteryBeans.add(index, mNumberBean);
					LogUtil.DefalutLog(mNumberBean);
//				}
				
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
		if(state == STATE_FRONT_2_DIRECTLY || state == STATE_FRONT_3_DIRECTLY){
			ViewUtil.getRandomButton(autoViews, sumBall, sumView, numberCount);
		}else{
			for(int i=0;i<sumView;i++){
				if(state == STATE_FRONT_1){
					ViewUtil.getRandomButton(autoViews.get(i), sumBall, 2);
					numberCount[i] = 2;
				}else{
					ViewUtil.getRandomButton(autoViews.get(i), sumBall, minSelect);
					numberCount[i] = minSelect;
				}
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
			if(lotteryBeans == null){
				return;
			}
			if(resultCode == GetString.BLAlter){
				index = mIntent.getIntExtra("index", 0);
				BetNumberBean mNumberBean = lotteryBeans.get(index);
				lotteryBeans.remove(index);
				
				item = (int) mNumberBean.getItem();
				String number = mNumberBean.getBuyNumber();
				String[] temp = null;
				if(number != null){
					if(mNumberBean.getPollId().equals("03")){
						temp = number.split("@");
						selectTowed = temp[0].split(",").length;
						numberCount[0] = temp[1].split(",").length;
					}else{
						temp = number.split("#");
						for(int i = 0; i < sumView; i++){
							numberCount[i] = temp[i].split(",").length;
						}
					}
				}
				try {
					if(mNumberBean.getPollId().equals("03")){
						ViewUtil.restoreAutoView(auto, temp[0].split(","));
						ViewUtil.restoreAutoView(autoViews.get(0), temp[1].split(","));
//						ViewUtil.restore(autoViews, temp, ",","");
					}else{
						ViewUtil.restore(autoViews, temp, ",","");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				countMoney();
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.choice_button_frequent_sort){
			
			expandPopwindow();
			
		}else if(v.getId() == topChartBtn.getId()){
			Intent intent = new Intent(Y11x5Activity.this, ZouShiTuActivity.class);
			intent.putExtra(LotteryId.INTENT_LID, lid);
			startActivity(intent);
		}else if(v.getId() == history.getId()){
			StatService.onEvent(Y11x5Activity.this, "recently result", "快彩类近期开奖", 1);
			Bundle mBundle = new Bundle();
			mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID,lid);
			Intent intent = new Intent();
			intent.setClass(Y11x5Activity.this, LotteryResultHistoryActivity.class);
			intent.putExtra(Settings.BUNDLE,mBundle);
			startActivity(intent);
		}
	}
	
	private void randomTask(final int num){
		MyAsyncTask tast = new MyAsyncTask(Y11x5Activity.this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			
			@Override
			public void onTaskPostExecuteListener() {
//				ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				startActivity();
			}
			
			@Override
			public Boolean onTaskBackgroundListener() {
				return lotteryBeans.addAll(0, RandomSelectUtil.getRandomNumber( num, mInfoBean));
//				return false;
			}
		});
		
		tast.execute();
	}
	
	/** 当前更改的lotteryBeans 的位置 **/
	private int index;
}