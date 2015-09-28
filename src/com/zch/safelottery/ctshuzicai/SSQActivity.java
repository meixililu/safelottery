package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.LotteryResultHistoryActivity;
import com.zch.safelottery.activity.RecordBetActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.activity.ZouShiTuActivity;
import com.zch.safelottery.adapter.ViewPagerAdapter;
import com.zch.safelottery.asynctask.MyAsyncTask;
import com.zch.safelottery.asynctask.MyAsyncTask.OnAsyncTaskListener;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.SelectInfoBean;
import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.dialogs.JXDialog;
import com.zch.safelottery.dialogs.JXDialog.OnButtonItemClickListener;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PopDialog;
import com.zch.safelottery.dialogs.PopDialog.PopViewItemOnclickListener;
import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.MethodUtils;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class SSQActivity extends BaseLotteryActivity {

	private View ctView,PTView,DTView;
	private Button radom_red_btn, radom_blue_btn, play_method_btn;
	private TextView redball_num_tv,blueball_num_tv;
	private AutoWrapView redBallView, blueBallView;
	private AutoWrapView redBallDanmaView, redBallTuomaView,blueBallTuomaView;
	private RadioButton xuanhao_putong,xuanhao_dantuo;
	private int redNum, blueNum;
	
	private ArrayList<AutoWrapView> autoViews;
	private ArrayList<AutoWrapView> autoViewsDT;
	private PopDialog popDialog;
	private SelectInfoBean mInfoBean; //初始选号信息
	private ArrayList<BetNumberBean> lotteryBeans;
	
	private int perBetMoney = 2;
	private String playId = "01"; //玩法
	private String state = "1"; //
	private long item; //注数
	private long itemDT; //注数
	private int index;//下标 用来确认当前要更改的List
	
	private LayoutInflater inflater;
	private ArrayList<View> views;
	private ViewPagerAdapter myPagerAdapter;
	private ViewPager viewPager;
	private int[] numberCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		ctView = (View) inflater.inflate(R.layout.dlt_ssq_dantuo_viewpager, null);
		setcViewLinearlayout(ctView);
		init();
		initData();
		initShakeListener();
		lid = LotteryId.SSQ;
	}
	
	private void init(){
		views = new ArrayList<View>();
		xuanhao_putong = (RadioButton)findViewById(R.id.xuanhao_putong);
		xuanhao_dantuo = (RadioButton)findViewById(R.id.xuanhao_dantuo);
		viewPager = (ViewPager)findViewById(R.id.view_page);
		PTView = (View) inflater.inflate(R.layout.dlt_ssq_choice, null);
		DTView = (View) inflater.inflate(R.layout.dlt_ssq_dantuo_choice, null);
		views.add(PTView);
		views.add(DTView);
		initViewsPT(PTView);
		initViewsDT(DTView);
		myPagerAdapter = new ViewPagerAdapter(views);
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		xuanhao_putong.setOnClickListener(onClickListener);
		xuanhao_dantuo.setOnClickListener(onClickListener);
		numberCount = new int[3];
	}

	private void initViewsPT(View view) {
		redBallView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_choice_gridview_redball);
		blueBallView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_choice_gridview_blueball);
		play_method_btn = (Button) view.findViewById(R.id.stock_choice_explain_button);
		radom_red_btn = (Button) view.findViewById(R.id.dlt_ssq_choice_red_button);
		radom_blue_btn = (Button) view.findViewById(R.id.dlt_ssq_choice_blue_button);
		redball_num_tv = (TextView) view.findViewById(R.id.dlt_ssq_choice_redball_text);
		blueball_num_tv = (TextView) view.findViewById(R.id.dlt_ssq_choice_blueball_text);

		ViewUtil.initBalls(getApplicationContext(), redBallView, 33, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), blueBallView, 16, ViewUtil.COLOR_BLUE, ViewUtil.INIT_NUMBER_1, onClickListener);

		autoViews = new ArrayList<AutoWrapView>();
		lotteryBeans = new ArrayList<BetNumberBean>();
		autoViews.add(0,blueBallView);
		autoViews.add(0,redBallView);
		radom_red_btn.setOnClickListener(onClickListener);
		radom_blue_btn.setOnClickListener(onClickListener);
		play_method_btn.setOnClickListener(onClickListener);
	}
	
	private void initViewsDT(View view) {
		redBallDanmaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_1);
		redBallTuomaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_2);
		blueBallTuomaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_3);
		
		ViewUtil.initBalls(getApplicationContext(), redBallDanmaView, 33, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), redBallTuomaView, 33, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), blueBallTuomaView, 16, ViewUtil.COLOR_BLUE, ViewUtil.INIT_NUMBER_1, onClickListener);

		autoViewsDT = new ArrayList<AutoWrapView>();
		lotteryBeans = new ArrayList<BetNumberBean>();
		autoViewsDT.add(0,blueBallTuomaView);
		autoViewsDT.add(0,redBallTuomaView);
		autoViewsDT.add(0,redBallDanmaView);
	}
	
	/**
	 * 初始SelectInfoBean 信息
	 */
	private void initData(){
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setSumView(2);
		mInfoBean.setInitNum(1);
		mInfoBean.setItem(1);
		mInfoBean.setPlayId(playId);
		mInfoBean.setPollId("01");
		mInfoBean.setRepeat(false);
		mInfoBean.setSelectBall(new int[]{6, 1});
		mInfoBean.setSumBall(new int[]{33, 16});
		mInfoBean.setSplit(new String[]{",", "#"});
	}
	
	public void countmoney() {
		if(state.equals("1")){
			if (redNum >= 6 && blueNum >= 1) {
				long redn = MethodUtils.C_better(redNum, 6);
				long bluen = MethodUtils.C_better(blueNum, 1);
				item = redn * bluen;
			}else{
				item = 0;
			}
			setBetAndMoney(item, perBetMoney);
		}else if(state.equals("2")){
			if( isEnough(false) ){
				long redballNum = MethodUtils.C_better(numberCount[1], 6-numberCount[0]);
				long blueballNum = MethodUtils.C_better(numberCount[2], 1);
				itemDT = redballNum * blueballNum;
			}else{
				itemDT = 0;
			}
			setBetAndMoney(itemDT, perBetMoney);
		}
	}
	
	private boolean isEnough(boolean isShowToast){
		boolean isEnough = false;
		if(numberCount[0] > 0 && numberCount[1] > 0 && numberCount[2] > 0){
			if(numberCount[1] < 2){
				if(isShowToast){
					ToastUtil.diaplayMesShort(getApplicationContext(), "红球拖码区至少选择2个");
				}
			}else if( (numberCount[1]+numberCount[0]) < 7 ){
				if(isShowToast){
					ToastUtil.diaplayMesShort(getApplicationContext(), "红球胆码加拖码总数不能少于7个");
				}
			}else{
				isEnough = true;
			}
		}else if(numberCount[0] < 1){
			if(isShowToast){
				ToastUtil.diaplayMesShort(getApplicationContext(), "红球胆码至少选择1个");
			}
		}else if(numberCount[1] < 2){
			if(isShowToast){
				ToastUtil.diaplayMesShort(getApplicationContext(), "红球拖码至少选择2个");
			}
		}else if(numberCount[2] < 1){
			if(isShowToast){
				ToastUtil.diaplayMesShort(getApplicationContext(), "蓝球至少选择1个");
			}
		}
		return isEnough;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(state.equals("1")){
			startShakeLister();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopShakeLister();
	}

	@Override
	public void onBackPressed() {
		StatService.onEvent(getApplicationContext(), "backspace", "后退键", 1);
		if(lotteryBeans != null){
			if(redNum > 0 || blueNum > 0 || lotteryBeans.size() > 0 || numberCount[0] > 0 || numberCount[1] > 0 || numberCount[2] > 0){
				NormalAlertDialog dialog = new NormalAlertDialog(SSQActivity.this);
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
						
					}
				});
				dialog.show();
			}else{
				super.onBackPressed();
			}
		}else{
			super.onBackPressed();
		}
	}

	@Override
	public void bottom_clear(View v) {
		StatService.onEvent(getApplicationContext(), "ssq clear all", "双色球下方清空", 1);
		if(state.equals("1")){
			if(redNum > 0 || blueNum > 0){
				NormalAlertDialog dialog = new NormalAlertDialog(SSQActivity.this);
				dialog.setTitle("提示");
				dialog.setContent("您确定要清空当前选择的号码吗？");
				dialog.setOk_btn_text("确定");
				dialog.setCancle_btn_text("取消");
				dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
					
					@Override
					public void onOkBtnClick() {
						clear();
					}
					
					@Override
					public void onCancleBtnClick() {
						
					}
				});
				dialog.show();
			}else{
				ToastUtil.diaplayMesShort(getApplicationContext(), "您还没选择任何号码");
			}
		}else if(state.equals("2")){
			if(numberCount[0] > 0 || numberCount[1] > 0 || numberCount[2] > 0){
				NormalAlertDialog dialog = new NormalAlertDialog(SSQActivity.this);
				dialog.setTitle("提示");
				dialog.setContent("您确定要清空当前选择的号码吗？");
				dialog.setOk_btn_text("确定");
				dialog.setCancle_btn_text("取消");
				dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
					
					@Override
					public void onOkBtnClick() {
						clear();
					}
					
					@Override
					public void onCancleBtnClick() {
						
					}
				});
				dialog.show();
			}else{
				ToastUtil.diaplayMesShort(getApplicationContext(), "您还没选择任何号码");
			}
		}
		
	}

	@Override
	public void bottom_random(View v) {
		StatService.onEvent(getApplicationContext(), "ssq randomly pick", "双色球下方机选", 1);
		if(state.equals("1")){
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.add(1);
			data.add(5);
			data.add(10);
			data.add(50);
			JXDialog dialog = new JXDialog(SSQActivity.this, data);
			dialog.setTitle("请选择机选注数");
			dialog.setColor(1);
			dialog.setOnItemClickListener(new OnButtonItemClickListener() {
				@Override
				public void onItemClickListener(int num) {
					randomTask(num);		
				}
			});
			dialog.show();
		}else if(state.equals("2")){
			ToastUtil.diaplayMesShort(getApplicationContext(), "胆拖不支持机选");
		}
	}

	@Override
	public void bottom_submit(View v) {
		try {
			if(state.equals("1")){
				if(redNum < 6){
					ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择6个红球");
				}else if(blueNum < 1){
					ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择1个蓝球");
				}else if(item > 0 && item * 2 < GetString.TopBetMoney){
					String balls = ViewUtil.getSelectNums(autoViews, ViewUtil.SELECT_SPLIT_MAX, ViewUtil.SELECT_NUMBER_MAX);
					BetNumberBean mNumberBean = new BetNumberBean();
					mNumberBean.setBuyNumber(balls);
					mNumberBean.setPlayId(playId);
					mNumberBean.setPollId(item > 1? "02": "01");
					mNumberBean.setItem(item);
					mNumberBean.setAmount(item * 2);
					lotteryBeans.add(index, mNumberBean);
					LogUtil.DefalutLog(mNumberBean.toString());
					startActivity();
				}else if((item*perBetMoney) >= GetString.TopBetMoney){
					ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
				}
			}else if(state.equals("2")){
				if( isEnough(true) ){
					if(itemDT > 0 && itemDT * 2 < GetString.TopBetMoney){
						String ballStr = ViewUtil.getSelectNumsSSQ_DLT(autoViewsDT);
						BetNumberBean mNumberBean = new BetNumberBean();
						mNumberBean.setBuyNumber(ballStr);
						mNumberBean.setPlayId(playId);
						mNumberBean.setPollId("03");
						mNumberBean.setItem(itemDT);
						mNumberBean.setAmount(itemDT * 2);
						lotteryBeans.add(index, mNumberBean);
						LogUtil.DefalutLog(mNumberBean.toString());
						startActivity();
					}else {
						ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void startActivity(){
		SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
		SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
		Intent intent = new Intent();
		intent.setClass(SSQActivity.this, CtOrderListActivity.class);
		intent.putExtra("lid", lid);
		intent.putExtra("issue", issue);
		intent.putExtra("playMethod", "01");
		startActivityForResult(intent, GetString.BLRequestCode);
		clear();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		try {
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
					String pollId = mNumberBean.getPollId();
					if(pollId.equals("03")){
						changeState("2");
						xuanhao_dantuo.setChecked(true);
						clear();
						itemDT = mNumberBean.getItem();
						String number = mNumberBean.getBuyNumber();
						String[] temp = new String[3];
						if(number != null){
							String[] tempFirst = number.split("#");
							String[] redBall= tempFirst[0].split("@");
							numberCount[0] = redBall[0].split(",").length;
							numberCount[1] = redBall[1].split(",").length;
							numberCount[2] = tempFirst[1].split(",").length;
							temp[0] = redBall[0];
							temp[1] = redBall[1];
							temp[2] = tempFirst[1];
						}
						setBetAndMoney(itemDT);
						ViewUtil.restore(autoViewsDT, temp, "," ,"");
					}else{
						changeState("1");
						xuanhao_putong.setChecked(true);
						clear();
						item = mNumberBean.getItem();
						String number = mNumberBean.getBuyNumber();
						String[] temp = null;
						if(number != null){
							temp = number.split("#");
							redNum = temp[0].split(",").length;
							blueNum = temp[1].split(",").length;
							setBallNumText();
						}
						setBetAndMoney(mNumberBean.getItem());
						ViewUtil.restore(autoViews, temp, "," ,"");
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void top_rbtn_hemai(View v) {
		super.top_rbtn_hemai(v);
	}

	@Override
	public void top_menu(View v) {
		stateNumber();
	}
	
	private void stateNumber(){
		if(popDialog == null){
			popDialog = new PopDialog(SSQActivity.this,R.style.popDialog);
			popDialog.textVisibility(new int[]{1});
			
			popDialog.setListener(new PopViewItemOnclickListener(){

				@Override
				public void onFirstClick(View v) {
				}

				@Override
				public void onSecondClick(View v) {
					Intent intent = new Intent(SSQActivity.this, ZouShiTuActivity.class);
					intent.putExtra(LotteryId.INTENT_LID, lid);
					startActivity(intent);
				}

				@Override
				public void onThirdClick(View v) {
					StatService.onEvent(getApplicationContext(), "ssq pull order", "双色球下拉投注记录", 1);
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.SSQ);
					if(GetString.isLogin){
						Intent intent  = new Intent(SSQActivity.this,RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						startActivity(intent);
					}else{
						Intent intent  = new Intent(SSQActivity.this,LoginActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFourthClick(View v) {
					StatService.onEvent(getApplicationContext(), "ssq pull result", "双色球下拉开奖信息", 1);
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.SSQ);
					Intent intent = new Intent();
					intent.setClass(SSQActivity.this, LotteryResultHistoryActivity.class);
					intent.putExtra(Settings.BUNDLE,mBundle);
					startActivity(intent);
				}
				
			});
			popDialog.setPopViewPosition();
			popDialog.show();
		}else{
			if(popDialog.isShowing()){
				popDialog.dismiss();
			}else{
				popDialog.show();
			}
		}
	}
	
	private void clear(){
		if(state.equals("1")){
			redNum = 0;
			blueNum = 0;
			item = 0;
			setBetAndMoney(0, perBetMoney);
			setBallNumText();
			ViewUtil.clearBalls(autoViews);
		}else if(state.equals("2")){
			numberCount[0] = 0;
			numberCount[1] = 0;
			numberCount[2] = 0;
			itemDT = 0;
			setBetAndMoney(0, perBetMoney);
			ViewUtil.clearBalls(autoViewsDT);
		}
	}

	@Override
	public void onShake() {
		StatService.onEvent(getApplicationContext(), "shaking", "摇晃手机", 1);
		if(state.equals("1")){
			ViewUtil.getRandomButton(redBallView, 33, 6);
			ViewUtil.getRandomButton(blueBallView, 16, 1);
			redNum = 6;
			blueNum = 1;
			item = 1;
			setBetAndMoney(item, perBetMoney);
			setBallNumText();
		}
	}

	private void setBallNumText(){
		redball_num_tv.setText("已选红球 "+ redNum +"个,至少选择6个");
		blueball_num_tv.setText("已选蓝球 "+ blueNum +"个,至少选择1个");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//清除静态数据
		SafeApplication.dataMap.clear(); 
	}
	
	private void randomTask(final int sum){
		MyAsyncTask tast = new MyAsyncTask(SSQActivity.this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			
			@Override
			public Boolean onTaskBackgroundListener() {
				return lotteryBeans.addAll(0, RandomSelectUtil.getRandomNumber(sum, mInfoBean)); //统一加到上面。
			}
			
			@Override
			public void onTaskPostExecuteListener() {
				startActivity();
			}
		});
		tast.execute();
	}
	
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v instanceof CheckBox) {
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoView = (AutoWrapView) checkBox.getParent();
				if (autoView.getId() == R.id.dlt_ssq_choice_gridview_redball) {
					if (checkBox.isChecked()) {
						redNum++;
					} else {
						redNum--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_choice_gridview_blueball) {
					if (checkBox.isChecked()) {
						blueNum++;
					} else {
						blueNum--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_1) {
					if(numberCount[0] >= 5){
						if(!checkBox.isChecked()){
							numberCount[0]--;
						}else{
							checkBox.setChecked(false);
							ToastUtil.diaplayMesShort(getApplicationContext(), "红球胆码区最多能选5个");
						}
					}else{
						if(checkBox.isChecked()) {
							numberCount[0]++;
						}else{
							numberCount[0]--;
						}
						numberCount[1] = ViewUtil.clearOnlyBallCol(redBallTuomaView, checkBox.getId() - 1, numberCount[1]);
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_2) {
					if(numberCount[1] >= 20){
						if(!checkBox.isChecked()){
							numberCount[1]--;
						}else{
							checkBox.setChecked(false);
							ToastUtil.diaplayMesShort(getApplicationContext(), "红球拖码区最多能选20个");
						}
					}else{
						if(checkBox.isChecked()) {
							numberCount[1]++;
						}else{
							numberCount[1]--;
						}
						numberCount[0] = ViewUtil.clearOnlyBallCol(redBallDanmaView, checkBox.getId() - 1, numberCount[0]);
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_3) {
					if(checkBox.isChecked()) {
						numberCount[2]++;
					}else{
						numberCount[2]--;
					}
				}
				countmoney();
				setBallNumText();
			} else if (v.getId() == R.id.dlt_ssq_choice_red_button) {
				StatService.onEvent(getApplicationContext(), "ssq-pick red", "双色球-机选红球", 1);
				JXDialog dialog = new JXDialog(SSQActivity.this);
				dialog.setTitle("请选择机选前区号码个数");
				dialog.setStart(6);
				dialog.setEnd(17);
				dialog.setColor(1);
				dialog.setOnItemClickListener(new OnButtonItemClickListener() {
					@Override
					public void onItemClickListener(int num) {
						redNum = num;
						ViewUtil.getRandomButton(redBallView, 33, num);
						countmoney();
						setBallNumText();
					}
				});
				dialog.show();
			} else if (v.getId() == R.id.dlt_ssq_choice_blue_button) {
				StatService.onEvent(getApplicationContext(), "ssq-pick blue", "双色球-机选篮球", 1);
				JXDialog dialog = new JXDialog(SSQActivity.this);
				dialog.setTitle("请选择机选后区号码个数");
				dialog.setStart(1);
				dialog.setEnd(17);
				dialog.setColor(2);
				dialog.setOnItemClickListener(new OnButtonItemClickListener() {
					@Override
					public void onItemClickListener(int num) {
						blueNum = num;
						ViewUtil.getRandomButton(blueBallView, 16, num);
						countmoney();
						setBallNumText();
					}
				});
				dialog.show();
			} else if(v.getId() == R.id.stock_choice_explain_button){
				Intent intent = new Intent(SSQActivity.this, HelpDetailActivity.class);
				intent.putExtra("kind", 6);
				startActivity(intent);
			} else if(v.getId() == R.id.xuanhao_putong){
				changeState("1");
			} else if(v.getId() == R.id.xuanhao_dantuo){
				changeState("2");
			}
		} 
	};
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				changeState("1");
				xuanhao_putong.setChecked(true);
				break;
			case 1:
				changeState("2");
				xuanhao_dantuo.setChecked(true);
				break;
			}
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}
	
	private void changeState(String id){
		state = id;
		if(state.equals("1")){
			startShakeLister();
			viewPager.setCurrentItem(0);
			setBetAndMoney(item);
		}else if(state.equals("2")){
			StatService.onEvent(getApplicationContext(), "ssq-dantuo", "双色球-胆拖", 1);
			stopShakeLister();
			viewPager.setCurrentItem(1);
			setBetAndMoney(itemDT);
		}
	}
	
}
