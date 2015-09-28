package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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

public class DLTActivity extends BaseLotteryActivity {

	private View view;
	private View combineView;
	private AutoWrapView redBallView, blueBallView;
	private CheckBox dlt_choice_addto_checkbox;
	private int redNum, blueNum, item;
	private long itemDT; //胆拖注数
	private int perBetMoney = 2;
	private TextView redball_num_tv, blueball_num_tv;
	private Button radom_red_btn, radom_blue_btn, explainBtn;
	private ArrayList<AutoWrapView> autoViews;
	private ArrayList<BetNumberBean> lotteryBeans;
	private boolean isAlertBean;
	private BetNumberBean alertBean;
	private String playMethod = "01";
	private PopDialog popDialog;
	private int index;
	private SelectInfoBean mInfoBean;

	/** 筛选的下标 **/
	int[] filterSelect = new int[3];

	private LayoutInflater inflater;
	private ArrayList<View> views;
	private ViewPagerAdapter myPagerAdapter;
	private ViewPager viewPager;
	private RadioButton xuanhao_putong, xuanhao_dantuo;
	private View ctView, PTView, DTView;
	private String state = "1";// 1为普通投注 2为胆拖投注
	private AutoWrapView redBallDanmaView, redBallTuomaView, blueBallDanmaView, blueBallTuomaView;
	private ArrayList<AutoWrapView> autoViewsDT;
	private int[] numberCount = new int[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		ctView = (View) inflater.inflate(R.layout.dlt_ssq_dantuo_viewpager, null);
		setcViewLinearlayout(ctView);
		init();
		initData();
		initShakeListener();
		lid = LotteryId.DLT;
	}

	private void init() {
		views = new ArrayList<View>();
		xuanhao_putong = (RadioButton) findViewById(R.id.xuanhao_putong);
		xuanhao_dantuo = (RadioButton) findViewById(R.id.xuanhao_dantuo);
		viewPager = (ViewPager) findViewById(R.id.view_page);
		PTView = (View) inflater.inflate(R.layout.dlt_ssq_choice, null);
		DTView = (View) inflater.inflate(R.layout.dlt_dantuo_choice, null);
		views.add(PTView);
		views.add(DTView);
		initViewsPT(PTView);
		initViewsDT(DTView);
		myPagerAdapter = new ViewPagerAdapter(views);
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		xuanhao_putong.setOnClickListener(onClickListener);
		xuanhao_dantuo.setOnClickListener(onClickListener);
		dlt_choice_addto_checkbox = (CheckBox) findViewById(R.id.dlt_choice_addto_checkbox);
		dlt_choice_addto_checkbox.setVisibility(View.VISIBLE);
		dlt_choice_addto_checkbox.setOnClickListener(onClickListener);
	}

	private void initViewsPT(View view) {
		redBallView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_choice_gridview_redball);
		blueBallView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_choice_gridview_blueball);
		redball_num_tv = (TextView) view.findViewById(R.id.dlt_ssq_choice_redball_text);
		blueball_num_tv = (TextView) view.findViewById(R.id.dlt_ssq_choice_blueball_text);
		radom_red_btn = (Button) view.findViewById(R.id.dlt_ssq_choice_red_button);
		radom_blue_btn = (Button) view.findViewById(R.id.dlt_ssq_choice_blue_button);
		explainBtn = (Button) view.findViewById(R.id.stock_choice_explain_button);

		explainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DLTActivity.this, HelpDetailActivity.class);
				intent.putExtra("kind", 7);
				startActivity(intent);
			}
		});

		ViewUtil.initBalls(getApplicationContext(), redBallView, 35, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), blueBallView, 12, ViewUtil.COLOR_BLUE, ViewUtil.INIT_NUMBER_1, onClickListener);
		autoViews = new ArrayList<AutoWrapView>(2);
		lotteryBeans = new ArrayList<BetNumberBean>();
		autoViews.add(0, redBallView);
		autoViews.add(blueBallView);
		radom_red_btn.setOnClickListener(onClickListener);
		radom_blue_btn.setOnClickListener(onClickListener);
		radom_red_btn.setText("机选前区");
		radom_blue_btn.setText("机选后区");

		setBallNumText();
	}

	private void initViewsDT(View view) {
		redBallDanmaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_1);
		redBallTuomaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_2);
		blueBallDanmaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_3);
		blueBallTuomaView = (AutoWrapView) view.findViewById(R.id.dlt_ssq_dantuo_autoview_4);

		ViewUtil.initBalls(getApplicationContext(), redBallDanmaView, 35, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), redBallTuomaView, 35, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), blueBallDanmaView, 12, ViewUtil.COLOR_BLUE, ViewUtil.INIT_NUMBER_1, onClickListener);
		ViewUtil.initBalls(getApplicationContext(), blueBallTuomaView, 12, ViewUtil.COLOR_BLUE, ViewUtil.INIT_NUMBER_1, onClickListener);

		autoViewsDT = new ArrayList<AutoWrapView>();
		lotteryBeans = new ArrayList<BetNumberBean>();
		autoViewsDT.add(0, blueBallTuomaView);
		autoViewsDT.add(0, blueBallDanmaView);
		autoViewsDT.add(0, redBallTuomaView);
		autoViewsDT.add(0, redBallDanmaView);
	}

	private void initData() {
		mInfoBean = new SelectInfoBean(); // 初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setSumView(2);
		mInfoBean.setInitNum(1);
		mInfoBean.setItem(1);
		mInfoBean.setPlayId(playMethod);
		mInfoBean.setPollId("01");
		mInfoBean.setRepeat(false);
		mInfoBean.setSelectBall(new int[] { 5, 2 });
		mInfoBean.setSumBall(new int[] { 35, 12 });
		mInfoBean.setSplit(new String[] { ",", "#" });
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.dlt_choice_addto_checkbox) {
				if (dlt_choice_addto_checkbox.isChecked()) {
					perBetMoney = 3;
					playMethod = "02";
					if(state.equals("1")){
						setBetAndMoney(item, perBetMoney);
					}else if(state.equals("2")){
						setBetAndMoney(itemDT, perBetMoney);
					}
					ToastUtil.diaplayMesShort(getApplicationContext(), " 选择追加玩法");
				} else {
					perBetMoney = 2;
					playMethod = "01";
					if(state.equals("1")){
						setBetAndMoney(item, perBetMoney);
					}else if(state.equals("2")){
						setBetAndMoney(itemDT, perBetMoney);
					}
					ToastUtil.diaplayMesShort(getApplicationContext(), " 取消追加玩法");
				}
				mInfoBean.setPlayId(playMethod);
			} else if (v instanceof CheckBox) {
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoView = (AutoWrapView) checkBox.getParent();
				if (autoView.getId() == R.id.dlt_ssq_choice_gridview_redball) {
					if (((CheckBox) v).isChecked()) {
						redNum++;
					} else {
						redNum--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_choice_gridview_blueball) {
					if (((CheckBox) v).isChecked()) {
						blueNum++;
					} else {
						blueNum--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_1) {
					if (((CheckBox) v).isChecked()) {
						if (numberCount[0] > 3) {
							ToastUtil.diaplayMesShort(DLTActivity.this, "选胆数不能超过4");
							((CheckBox) v).setChecked(false);
						} else {
							numberCount[0]++;
							numberCount[1] = ViewUtil.clearOnlyBallCol(redBallTuomaView, checkBox.getId() - 1, numberCount[1]);
						}
					} else {
						numberCount[0]--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_2) {
					if (((CheckBox) v).isChecked()) {
						if (numberCount[1] > 27) {
							ToastUtil.diaplayMesShort(DLTActivity.this, "拖码数不能超过28");
							((CheckBox) v).setChecked(false);
						} else {
							numberCount[1]++;
							numberCount[0] = ViewUtil.clearOnlyBallCol(redBallDanmaView, checkBox.getId() - 1, numberCount[0]);
						}
					} else {
						numberCount[1]--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_3) {
					if (((CheckBox) v).isChecked()) {
						if (numberCount[2] > 0) {
							ToastUtil.diaplayMesShort(DLTActivity.this, "选胆数不能超过1");
							((CheckBox) v).setChecked(false);
						} else {
							numberCount[2]++;
							numberCount[3] = ViewUtil.clearOnlyBallCol(blueBallTuomaView, checkBox.getId() - 1, numberCount[3]);
						}
					} else {
						numberCount[2]--;
					}
				} else if (autoView.getId() == R.id.dlt_ssq_dantuo_autoview_4) {
					if (((CheckBox) v).isChecked()) {
						numberCount[3]++;
						numberCount[2] = ViewUtil.clearOnlyBallCol(blueBallDanmaView, checkBox.getId() - 1, numberCount[2]);
					} else {
						numberCount[3]--;
					}
				}
				countmoney();
				setBallNumText();
			} else if (v.getId() == R.id.dlt_ssq_choice_red_button) {
				JXDialog dialog = new JXDialog(DLTActivity.this);
				dialog.setTitle("请选择机选前区号码个数");
				dialog.setStart(5);
				dialog.setEnd(19);
				dialog.setColor(1);
				dialog.setOnItemClickListener(new OnButtonItemClickListener() {
					@Override
					public void onItemClickListener(int num) {
						redNum = num;
						ViewUtil.getRandomButton(redBallView, 35, num);
						countmoney();
						if ((item * perBetMoney) >= GetString.TopBetMoney) {
							ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
						}
						setBallNumText();
					}
				});
				dialog.show();
			} else if (v.getId() == R.id.dlt_ssq_choice_blue_button) {
				JXDialog dialog = new JXDialog(DLTActivity.this);
				dialog.setTitle("请选择机选后区号码个数");
				dialog.setStart(2);
				dialog.setEnd(13);
				dialog.setColor(2);
				dialog.setOnItemClickListener(new OnButtonItemClickListener() {
					@Override
					public void onItemClickListener(int num) {
						blueNum = num;
						ViewUtil.getRandomButton(blueBallView, 12, num);
						countmoney();
						if ((item * perBetMoney) >= GetString.TopBetMoney) {
							ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
						}
						setBallNumText();
					}
				});
				dialog.show();
			} else if (v.getId() == R.id.xuanhao_putong) {
				changeState("1");
			} else if (v.getId() == R.id.xuanhao_dantuo) {
				changeState("2");
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		if (requestCode == GetString.BLRequestCode) {
			index = 0; // 设置初始值为0
			lotteryBeans = (ArrayList<BetNumberBean>) SafeApplication.dataMap.get(SafeApplication.MAP_LIST_KEY);
			if (lotteryBeans == null) {
				return;
			}
			if (resultCode == GetString.BLAlter) {
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
				String[] temp = new String[4];
				if(number != null){
					String[] tempFirst = number.split("#");
					String[] redBall= tempFirst[0].split("@");
					numberCount[0] = redBall[0].split(",").length;
					numberCount[1] = redBall[1].split(",").length;
					temp[0] = redBall[0];
					temp[1] = redBall[1];
					if(tempFirst[1].contains("@")){
						String[] blueBall=tempFirst[1].split("@");
						if(blueBall[0].equals("")){
							numberCount[2]=0;
						}else{
						numberCount[2] = blueBall[0].split(",").length;
						}
						numberCount[3] = blueBall[1].split(",").length;
						temp[2] = blueBall[0];
						temp[3]= blueBall[1];
					}else{
						numberCount[2] = 0;
						numberCount[3]=tempFirst[1].split(",").length;
						temp[2] = "";
						temp[3]= tempFirst[1];
					}
				}
				setBetAndMoney(itemDT, perBetMoney);
				try {
					ViewUtil.restore(autoViewsDT, temp, "," ,"");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				changeState("1");
				xuanhao_putong.setChecked(true);
				clear();
				item = (int) mNumberBean.getItem();
				String number = mNumberBean.getBuyNumber();
				String[] temp = null;
				if (number != null) {
					temp = number.split("#");
					redNum = temp[0].split(",").length;
					blueNum = temp[1].split(",").length;
					setBallNumText();
				}
				setBetAndMoney(item, perBetMoney);
				try {
					ViewUtil.restore(autoViews, temp, ",", "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(state.equals("1")){
			startShakeLister();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopShakeLister();
		Log.d(Settings.TAG, "onPause()");
	}

	@Override
	public void onBackPressed() {
		if (lotteryBeans != null) {
			if (redNum > 0 || blueNum > 0 || lotteryBeans.size() > 0||numberCount[0] > 0 || numberCount[1] > 0 || numberCount[2] > 0||numberCount[3] > 0) {
				NormalAlertDialog dialog = new NormalAlertDialog(DLTActivity.this);
				dialog.setTitle("提示");
				dialog.setContent("您确定退出投注吗？一旦退出,您的投注将被清空。");
				dialog.setOk_btn_text("确定");
				dialog.setCancle_btn_text("取消");
				dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

					@Override
					public void onOkBtnClick() {
						clear();
						lotteryBeans.clear();
						finish();
					}

					@Override
					public void onCancleBtnClick() {
					}
				});
				dialog.show();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 清除静态数据
		SafeApplication.dataMap.clear();
	}

	@Override
	public void bottom_clear(View v) {
		if(state.equals("1")){
		if (redNum > 0 || blueNum > 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(DLTActivity.this);
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
					// TODO Auto-generated method stub

				}
			});
			dialog.show();
		}
		}else if(state.equals("2")){
			if(numberCount[0] > 0 || numberCount[1] > 0 || numberCount[2] > 0||numberCount[3] > 0){
				NormalAlertDialog dialog = new NormalAlertDialog(DLTActivity.this);
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
		if(state.equals("1")){
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.add(1);
			data.add(5);
			data.add(10);
			data.add(50);
			JXDialog dialog = new JXDialog(DLTActivity.this, data);
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
		if(state.equals("1")){
			if (redNum < 5) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择5个前区");
			} else if (blueNum < 2) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择2个后区");
			} else if (item > 0 && (item * perBetMoney) < GetString.TopBetMoney) {
				try {
					String balls = ViewUtil.getSelectNums(autoViews, ViewUtil.SELECT_SPLIT_MAX, ViewUtil.SELECT_NUMBER_MAX);
					BetNumberBean mNumberBean = new BetNumberBean();
					mNumberBean.setBuyNumber(balls);
					mNumberBean.setPlayId(playMethod);
					mNumberBean.setPollId(item > 1 ? "02" : "01");
					mNumberBean.setItem(item);
					mNumberBean.setAmount(item * perBetMoney);
					lotteryBeans.add(index, mNumberBean);
					LogUtil.DefalutLog(mNumberBean);
					startActivity();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ((item * perBetMoney) >= GetString.TopBetMoney) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
			}
		}else if(state.equals("2")){
			if(numberCount[0]==0){
				ToastUtil.diaplayMesShort(getApplicationContext(), "红球胆码至少选择1个");
			}else if(numberCount[0]+numberCount[1]<6){
				ToastUtil.diaplayMesShort(getApplicationContext(), "红球胆码加拖码至少选择6个");
			}else if(numberCount[3]<2){
				ToastUtil.diaplayMesShort(getApplicationContext(), "篮球拖码至少选择2个");
			}else if (itemDT > 0 && (itemDT * perBetMoney) < GetString.TopBetMoney) {
				try {
					String balls = ViewUtil.getSelectNumsSSQ_DLT(autoViewsDT);
					BetNumberBean mNumberBean = new BetNumberBean();
					mNumberBean.setBuyNumber(balls);
					mNumberBean.setPlayId(playMethod);
					mNumberBean.setPollId(itemDT > 1 ? "03" : "01");
					mNumberBean.setItem(itemDT);
					mNumberBean.setAmount(itemDT * perBetMoney);
					lotteryBeans.add(index, mNumberBean);
					LogUtil.DefalutLog(mNumberBean);
					startActivity();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ((itemDT * perBetMoney) > GetString.TopBetMoney) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
			}
		}
	}

	private void startActivity() {
		changeAllPlayId();
		SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
		SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
		Intent intent = new Intent();
		intent.setClass(DLTActivity.this, CtOrderListActivity.class);
		intent.putExtra("lid", lid);
		intent.putExtra("issue", issue);
		intent.putExtra("playMethod", playMethod);
		startActivityForResult(intent, GetString.BLRequestCode);
		clear();
	}

	/**
	 * 点击确定之后，修改所有选号的玩法；
	 */
	private void changeAllPlayId() {
		for (BetNumberBean bean : lotteryBeans) {
			bean.setPlayId(playMethod);
			bean.setAmount(bean.getItem() * perBetMoney);
		}
	}

	@Override
	public void top_menu(View v) {
		stateNumber();
	}

	private void stateNumber() {
		if (popDialog == null) {
			popDialog = new PopDialog(DLTActivity.this, R.style.popDialog);
			popDialog.textVisibility(new int[] { 1 });

			popDialog.setListener(new PopViewItemOnclickListener() {

				@Override
				public void onFirstClick(View v) {
				}

				@Override
				public void onSecondClick(View v) {
					Intent intent = new Intent(DLTActivity.this, ZouShiTuActivity.class);
					intent.putExtra(LotteryId.INTENT_LID, lid);
					startActivity(intent);
				}

				@Override
				public void onThirdClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.DLT);
					if (GetString.isLogin) {
						Intent intent = new Intent(DLTActivity.this, RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE, mBundle);
						startActivity(intent);
					} else {
						Intent intent = new Intent(DLTActivity.this, LoginActivity.class);
						intent.putExtra(Settings.BUNDLE, mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFourthClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.DLT);
					Intent intent = new Intent();
					intent.setClass(DLTActivity.this, LotteryResultHistoryActivity.class);
					intent.putExtra(Settings.BUNDLE, mBundle);
					startActivity(intent);
				}

			});
			popDialog.setPopViewPosition();
			popDialog.show();
		} else {
			if (popDialog.isShowing()) {
				popDialog.dismiss();
			} else {
				popDialog.show();
			}
		}
	}

	@Override
	public void onShake() {
		setBetAndMoney(1, 2);
		ViewUtil.getRandomButton(redBallView, 35, 5);
		ViewUtil.getRandomButton(blueBallView, 12, 2);
		redNum = 5;
		blueNum = 2;
		item = 1;
		setBetAndMoney(item, perBetMoney);
		setBallNumText();

	}

	public void countmoney() {
		if (state.equals("1")) {
			if (redNum >= 5 && blueNum >= 2) {
				long redn = MethodUtils.C_better(redNum, 5);
				long bluen = MethodUtils.C_better(blueNum, 2);
				item = (int) (redn * bluen);
			} else {
				item = 0;
			}
			setBetAndMoney(item, perBetMoney);
		} else if (state.equals("2")) {
			if (numberCount[0]>0&&numberCount[0] + numberCount[1] > 5 &&  numberCount[3] >= 2) {
				long redn = MethodUtils.C_better(numberCount[1], 5 - numberCount[0]);
				long bluen = MethodUtils.C_better(numberCount[3], 2-numberCount[2]);
				itemDT = (int) (redn * bluen);
			} else {
				itemDT = 0;
			}
			setBetAndMoney(itemDT, perBetMoney);
		}
	}

	private void setBallNumText() {
		if (state.equals("1")) {
		redball_num_tv.setText("已选前区 " + redNum + "个,至少选择5个");
		blueball_num_tv.setText("已选后区 " + blueNum + "个,至少选择2个");
		}
	}

	private void clear() {
		if(state.equals("1")){
			redNum = 0;
			blueNum = 0;
			item = 0;
			setBetAndMoney(0, perBetMoney);
			setBallNumText();
			ViewUtil.clearBalls(autoViews);
		}else if(state.equals("2")){
			itemDT = 0;
			numberCount[0] = 0;
			numberCount[1] = 0;
			numberCount[2] = 0;
			numberCount[3] = 0;
			setBetAndMoney(0, perBetMoney);
			ViewUtil.clearBalls(autoViewsDT);
		}
	}

	private void randomTask(final int num) {
		MyAsyncTask tast = new MyAsyncTask(DLTActivity.this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {

			@Override
			public void onTaskPostExecuteListener() {
				// ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				startActivity();
			}

			@Override
			public Boolean onTaskBackgroundListener() {
				return lotteryBeans.addAll(0, RandomSelectUtil.getRandomNumber(num, mInfoBean));
			}
		});
		tast.execute();
	}

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

	private void changeState(String id) {
		state = id;
		if (state.equals("1")) {
			startShakeLister();
			viewPager.setCurrentItem(0);
			setBetAndMoney(item,perBetMoney);
		} else if (state.equals("2")) {
			StatService.onEvent(getApplicationContext(), "daletou-dantuo", "大乐透-胆拖", 1);
			stopShakeLister();
			viewPager.setCurrentItem(1);
			setBetAndMoney(itemDT,perBetMoney);
		}
	}
}
