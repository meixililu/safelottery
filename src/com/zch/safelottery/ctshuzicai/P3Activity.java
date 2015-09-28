package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.LotteryResultHistoryActivity;
import com.zch.safelottery.activity.RecordBetActivity;
import com.zch.safelottery.activity.SafeApplication;
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
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.MethodUtils;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class P3Activity extends BaseLotteryActivity {

	private View view, itemView;
	private AutoWrapView redBallView;
	private int perBetMoney = 2;
	private int item;
	private int[] arrayNum = new int[5];
	private ArrayList<AutoWrapView> autoViews;
	private PopDialog popDialog;
	private boolean isAlertBean;
	private ArrayList<BetNumberBean> lotteryBeans;
	private LayoutInflater inflater;
	private LinearLayout tempLayou;
	private TextView many_view_item_tv, pl3_3d_declare;
	private RadioButton radioZhiX, radioZu3, radioZu6;
	private String playId = "01";
	// 玩法左右滑动
	private ArrayList<View> views;
	private MyPagerAdapter myPagerAdapter;
	private ViewPager scrollScreen;
	String balls;
	int[] viewNum = null;
	
	private SelectInfoBean mInfoBean;
	
	/** 筛选的下标 **/
	int[] filterSelect = new int[3];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		view = (View) inflater.inflate(R.layout.pl3_3d_choice, null);
		setcViewLinearlayout(view);
		initViews();
		initData();
		initShakeListener();
	}

	private View showLotteryIdItem(int i) {
		if (i == 0) {
			ScrollView scrollView = new ScrollView(this);
			scrollView.setVerticalScrollBarEnabled(false);
			tempLayou = ziViews(3);
			many_view_item_tv.setVisibility(View.VISIBLE);
			scrollView.addView(tempLayou);
			return scrollView;
		} else if (i == 1) {
			tempLayou = ziViews(1);
			many_view_item_tv.setVisibility(View.GONE);
		} else if (i == 2) {
			tempLayou = ziViews(1);
			many_view_item_tv.setVisibility(View.GONE);
		}
		return tempLayou;
	}

	private LinearLayout ziViews(int size) {
		LinearLayout layout = new LinearLayout(this);
		for (int i = 0; i < size; i++) {
			layout.addView(initShowView(i));
		}

		layout.setOrientation(LinearLayout.VERTICAL);
		LogUtil.CustomLog("TAG", "LinearLayout = " + size);
		return layout;
	}

	private View initShowView(int i) {
		LayoutInflater inflater = LayoutInflater.from(this);
		itemView = (View) inflater.inflate(R.layout.many_view_item, null);
		redBallView = (AutoWrapView) itemView.findViewById(R.id.many_view_item_auto_view);
		redBallView.setId(i);
		many_view_item_tv = (TextView) itemView.findViewById(R.id.many_view_item_tv);
		autoViews.add(redBallView);
		outTv(i);
		ViewUtil.initBalls(getApplicationContext(), redBallView, 10, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_0, onClickListener);
		return itemView;
	}

	private void outTv(int i) {
		if (i == 0) {
			many_view_item_tv.setText("百位");
		} else if (i == 1) {
			many_view_item_tv.setText("十位");
		} else if (i == 2) {
			many_view_item_tv.setText("个位");
		}
	}

	private void initViews() {
		autoViews = new ArrayList<AutoWrapView>(3);
		lotteryBeans = new ArrayList<BetNumberBean>();
		scrollScreen = (ViewPager) findViewById(R.id.scroll_screen_view);
		radioZhiX = (RadioButton) findViewById(R.id.pl3_3d_method_zx);
		radioZu3 = (RadioButton) findViewById(R.id.pl3_3d_method_zs);
		radioZu6 = (RadioButton) findViewById(R.id.pl3_3d_method_zl);
		pl3_3d_declare = (TextView) findViewById(R.id.pl3_3d_declare);
		radioZhiX.setOnClickListener(onClickListener);
		radioZu3.setOnClickListener(onClickListener);
		radioZu6.setOnClickListener(onClickListener);

		views = new ArrayList<View>(3);
		for (int i = 0; i < 3; i++) {
			views.add(showLotteryIdItem(i));
		}

		myPagerAdapter = new MyPagerAdapter(views);
		scrollScreen.setAdapter(myPagerAdapter);
		scrollScreen.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void initData(){
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setInitNum(0);
		mInfoBean.setSumView(3);
		mInfoBean.setItem(1);
		mInfoBean.setPlayId(playId);
		mInfoBean.setPollId("01");
		mInfoBean.setRepeat(true);
		mInfoBean.setSelectBall(new int[]{1, 1, 1});
		mInfoBean.setSumBall(new int[]{10, 10, 10});
		mInfoBean.setSplit(new String[]{"", ","});
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v instanceof CheckBox) {
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoView = (AutoWrapView) checkBox.getParent();
				if (playId.equals("01")) {
					addcount(checkBox, autoView);
					if (arrayNum[0] >= 1 && arrayNum[1] >= 1 && arrayNum[2] >= 1) {
						countmoney();
					} else {
						setBetAndMoney(0, perBetMoney);
						item = 0;
					}
				} else if (playId.equals("03")) {
					if (checkBox.isChecked()) {
						arrayNum[3]++;
					} else {
						arrayNum[3]--;
					}
					if(arrayNum[3] > 9){
						checkBox.setChecked(false);
						arrayNum[3]--;
						ToastUtil.diaplayMesShort(getApplicationContext(), "当前玩法限制最多投注9个号码");
					}
					if (arrayNum[3] >= 2) {
						countmoney();
					} else {
						setBetAndMoney(0, perBetMoney);
						item = 0;
					}
				} else if (playId.equals("04")) {
					if (checkBox.isChecked()) {
						arrayNum[4]++;
					} else {
						arrayNum[4]--;
					}
					if(arrayNum[4] > 9){
						checkBox.setChecked(false);
						arrayNum[4]--;
						ToastUtil.diaplayMesShort(getApplicationContext(), "当前玩法限制最多投注9个号码");
					}
					if (arrayNum[4] >= 3) {
						countmoney();
					} else {
						setBetAndMoney(0, perBetMoney);
						item = 0;
					}
				}
			} else if (v.getId() == R.id.pl3_3d_method_zx) {
				scrollScreen.setCurrentItem(0);
			} else if (v.getId() == R.id.pl3_3d_method_zs) {
				scrollScreen.setCurrentItem(1);
			} else if (v.getId() == R.id.pl3_3d_method_zl) {
				scrollScreen.setCurrentItem(2);
			}
		}

		private void addcount(CheckBox checkBox, AutoWrapView autoView) {
			if (autoView.getId() == 0) {
				if (checkBox.isChecked()) {
					arrayNum[0]++;
				} else {
					arrayNum[0]--;
				}
			} else if (autoView.getId() == 1) {
				if (checkBox.isChecked()) {
					arrayNum[1]++;
				} else {
					arrayNum[1]--;
				}
			} else if (autoView.getId() == 2) {
				if (checkBox.isChecked()) {
					arrayNum[2]++;
				} else {
					arrayNum[2]--;
				}
			}
		}
	};

	public void countmoney() {
		if (playId.equals("01")) {
			item = (int) (arrayNum[0] * arrayNum[1] * arrayNum[2]);
		} else if (playId.equals("03")) {
			item = (int) MethodUtils.C_better(arrayNum[3], 2) << 1;
		} else if (playId.equals("04")) {
			item = (int) MethodUtils.C_better(arrayNum[4], 3);
		}
		setBetAndMoney(item, perBetMoney);
	}

	@Override
	public void onResume() {
		super.onResume();
			startShakeLister();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopShakeLister();
	}

	@Override
	public void onBackPressed() {
			if (arrayNum[0] > 0 || arrayNum[1] > 0 || arrayNum[2] > 0 || arrayNum[3] > 0 || arrayNum[4] > 0 || lotteryBeans.size() > 0) {
				NormalAlertDialog dialog = new NormalAlertDialog(P3Activity.this);
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
	}

	@Override
	public void bottom_clear(View v) {
		if (arrayNum[0] > 0 || arrayNum[1] > 0 || arrayNum[2] > 0 || arrayNum[3] > 0 || arrayNum[4] > 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(P3Activity.this);
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
		} else {
			ToastUtil.diaplayMesShort(getApplicationContext(), "您还没选择任何号码");
		}
	}

	@Override
	public void bottom_random(View v) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(1);
		data.add(5);
		data.add(10);
		data.add(50);
		JXDialog dialog = new JXDialog(P3Activity.this, data);
		dialog.setTitle("请选择机选注数");
		dialog.setColor(1);
		dialog.setOnItemClickListener(new OnButtonItemClickListener() {
			@Override
			public void onItemClickListener(int num) {
				randomTask(num);
			}
		});
		dialog.show();
	}

	@Override
	public void bottom_submit(View v) {
		try {
			if (playId.equals("01")) {
				zxcansubmit();
				viewNum = new int[] { 0, 1, 2 };
					balls = ViewUtil.getSpecialViewSelectNums(autoViews, viewNum);
			} else if (playId.equals("03")) {
				if (arrayNum[3] < 2) {
					ToastUtil.diaplayMesShort(getApplicationContext(), " 至少选择两个数");
				}
				viewNum = new int[] { 3 };
				balls = ViewUtil.getSpecialViewSelectNums(autoViews, viewNum);
			} else if (playId.equals("04")) {
				if (arrayNum[4] < 3) {
					ToastUtil.diaplayMesShort(getApplicationContext(), " 至少选择三个数");
				}
				viewNum = new int[] { 4 };
				balls = ViewUtil.getSpecialViewSelectNums(autoViews, viewNum);
			}
	
			if (item > 0 && (item * perBetMoney) < GetString.TopBetMoney) {
				
				BetNumberBean mNumberBean = new BetNumberBean();
				mNumberBean.setBuyNumber(balls);
				mNumberBean.setPlayId(playId);
				mNumberBean.setPollId(item > 1? "02": "01");
				mNumberBean.setItem(item);
				mNumberBean.setAmount(item * 2);
				lotteryBeans.add(index, mNumberBean);
				
				LogUtil.DefalutLog(mNumberBean);
				startActivity();
				
			} else if ((item * perBetMoney) >= GetString.TopBetMoney) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "投注金额不能超过2万元");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void zxcansubmit() {
		if (arrayNum[0] <= 0) {
			ToastUtil.diaplayMesShort(getApplicationContext(), "百位至少选择一个数");
		} else if (arrayNum[1] <= 0) {
			ToastUtil.diaplayMesShort(getApplicationContext(), "十位至少选择一个数");
		} else if (arrayNum[2] <= 0) {
			ToastUtil.diaplayMesShort(getApplicationContext(), "个位至少选择一个数");
		}
	}

	private void startActivity() {
		SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
		SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
		Intent intent = new Intent();
		intent.setClass(P3Activity.this, CtOrderListActivity.class);
		intent.putExtra("issue", issue);
		intent.putExtra("lid", lid);
		intent.putExtra("playMethod", playId);
		startActivityForResult(intent, GetString.BLRequestCode);
		clear();
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
				
				try {
					item = (int) mNumberBean.getItem();
					playId = mNumberBean.getPlayId();
					String number = mNumberBean.getBuyNumber();
					String[] temp = number.split("#");
					if(number != null){
						if (playId.equals("01")) {
							temp = number.split(",");
							viewNum = new int[] { 0, 1, 2 };
							for(int i = 0; i < 3; i++){
								arrayNum[i] = temp[i].length();
							}
							ViewUtil.restore(autoViews, temp, "" ,"", viewNum);
						} else if (playId.equals("03")) {
							viewNum = new int[] { 3 };
							arrayNum[3] = temp[0].split(",").length;
							ViewUtil.restore(autoViews, temp, "," ,"", viewNum);
						} else if (playId.equals("04")) {
							viewNum = new int[] { 4 };
							arrayNum[4] = temp[0].split(",").length;
							ViewUtil.restore(autoViews, temp, "," ,"", viewNum);
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
	public void top_menu(View v) {
			stateNumber();
	}
	
	private void stateNumber(){
		if(popDialog == null){
			popDialog = new PopDialog(P3Activity.this,R.style.popDialog);
			popDialog.textVisibility(new int[]{1, 2});
			
			popDialog.setListener(new PopViewItemOnclickListener(){

				@Override
				public void onFirstClick(View v) {
				}

				@Override
				public void onSecondClick(View v) {
				}

				@Override
				public void onThirdClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.PL3);
					if(GetString.isLogin){
						Intent intent  = new Intent(P3Activity.this,RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						startActivity(intent);
					}else{
						Intent intent  = new Intent(P3Activity.this,LoginActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFourthClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.PL3);
					Intent intent = new Intent();
					intent.setClass(P3Activity.this, LotteryResultHistoryActivity.class);
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


	private void clear() {
		for (int i = 0; i < arrayNum.length; i++) {
			arrayNum[i] = 0;
		}
		item = 0;
		isAlertBean = false;
		setBetAndMoney(0, perBetMoney);
		ViewUtil.clearBalls(autoViews);
	}

	@Override
	public void onShake() {
		if (playId.equals("01")) {
			for (int i = 0; i < 3; i++) {
				ViewUtil.getRandomButton(autoViews.get(i), 10, 1);
				arrayNum[i] = 1;
			}
		} else if (playId.equals("03")) {
			ViewUtil.getRandomButton(autoViews.get(3), 10, 2);
			arrayNum[3] = 2;
		} else if (playId.equals("04")) {
			ViewUtil.getRandomButton(autoViews.get(4), 10, 3);
			arrayNum[4] = 3;
		}
		countmoney();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//清除静态数据
		SafeApplication.dataMap.clear(); 
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
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			LogUtil.SystemLog("OnPageChangeListener : " + arg0 + "   " + isAlertBean);
			switch (arg0) {
			case 0:
				clear();
				lotteryBeans.clear();
				pl3_3d_declare.setText(getApplicationContext().getResources().getText(R.string.pl3_zx_prompt));
				radioZhiX.setChecked(true);
				playId = "01";
				
				mInfoBean.setSumView(3);
				mInfoBean.setItem(1);
				mInfoBean.setPlayId(playId);
				mInfoBean.setPollId("01");
				mInfoBean.setRepeat(true);
				mInfoBean.setSelectBall(new int[]{1, 1, 1});
				mInfoBean.setSumBall(new int[]{10, 10, 10});
				mInfoBean.setSplit(new String[]{"", ","});
				break;
			case 1:
				clear();
				lotteryBeans.clear();
				pl3_3d_declare.setText(getApplicationContext().getResources().getText(R.string.pl3_zs_prompt));
				radioZu3.setChecked(true);
				playId = "03";
				
				mInfoBean.setSumView(1);
				mInfoBean.setItem(2);
				mInfoBean.setPlayId(playId);
				mInfoBean.setPollId("02");
				mInfoBean.setRepeat(false);
				mInfoBean.setSelectBall(new int[]{2});
				mInfoBean.setSumBall(new int[]{10});
				mInfoBean.setSplit(new String[]{",", "#"});
				break;
			case 2:
				clear();
				lotteryBeans.clear();
				pl3_3d_declare.setText(getApplicationContext().getResources().getText(R.string.pl3_zl_prompt));
				radioZu6.setChecked(true);
				playId = "04";
				
				mInfoBean.setSumView(1);
				mInfoBean.setItem(1);
				mInfoBean.setPlayId(playId);
				mInfoBean.setPollId("01");
				mInfoBean.setRepeat(false);
				mInfoBean.setSelectBall(new int[]{3});
				mInfoBean.setSumBall(new int[]{10});
				mInfoBean.setSplit(new String[]{",", "#"});
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	private void randomTask(final int num){
		MyAsyncTask tast = new MyAsyncTask(P3Activity.this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			
			@Override
			public void onTaskPostExecuteListener() {
//				ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				startActivity();
			}
			
			@Override
			public Boolean onTaskBackgroundListener() {
				return lotteryBeans.addAll(0, RandomSelectUtil.getRandomNumber( num, mInfoBean));
			}
		});
		tast.execute();
	}
	
	private int index;
}
