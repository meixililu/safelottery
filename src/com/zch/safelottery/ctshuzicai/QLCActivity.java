package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.HelpDetailActivity;
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

public class QLCActivity extends BaseLotteryActivity{
	
	private AutoWrapView redBallView;
	private Button explainBtn;
	private TextView redball_num_tv;
	private int redNum,item;
	private PopDialog popDialog;
	private final int perBetMoney = 2;
	private final int sum = 30;
	
	private ArrayList<BetNumberBean> lotteryBeans;
	private ArrayList<AutoWrapView> autoViews;
	private SelectInfoBean mInfoBean;
	
	/** 筛选的下标 **/
	int[] filterSelect = new int[3];
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.qlc_choice, null);
		setcView(view);
		initView();
		initData();
		initShakeListener();
	}
	
	private void initView(){
		redBallView = (AutoWrapView) findViewById(R.id.qlc_choice_gridview_redball);
		explainBtn = (Button) findViewById(R.id.stock_choice_explain_button);
		redball_num_tv = (TextView) findViewById(R.id.qlc_choice_redball_text);
		explainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QLCActivity.this, HelpDetailActivity.class);
				intent.putExtra("kind", 17);
				startActivity(intent);
			}
		});
		
		ViewUtil.initBalls(getApplicationContext(), redBallView, sum, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_1, onClickListener);
		lotteryBeans = new ArrayList<BetNumberBean>();
		autoViews = new ArrayList<AutoWrapView>();
		autoViews.add(redBallView);
	}
	
	private void initData(){
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setSumView(1);
		mInfoBean.setInitNum(1);
		mInfoBean.setItem(1);
		mInfoBean.setPlayId("01");
		mInfoBean.setPollId("01");
		mInfoBean.setRepeat(false);
		mInfoBean.setSelectBall(new int[]{7});
		mInfoBean.setSumBall(new int[]{30});
		mInfoBean.setSplit(new String[]{",", "#"});
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v instanceof CheckBox) {
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoView = (AutoWrapView) checkBox.getParent();
				if (autoView.getId() == R.id.qlc_choice_gridview_redball) {
					if (((CheckBox) v).isChecked()) {
//						if(redNum>16){
//							ToastUtil.diaplayMesShort(getApplicationContext(), "最多选择16个号码");
//							return;
//						}
						redNum++;
					} else {
						redNum--;
					}
				}
				countMoney();
//				setBallNumText();
			}else{
			}
		}
	};
	private void countMoney(){
		if (redNum >= 7) {
			item = (int) MethodUtils.C_better(redNum, 7);
		}else{
			item = 0;
		}
		setBetAndMoney(item, perBetMoney);
		redball_num_tv.setText("已选"+redNum+"个，至少选择7个");
	}
	
	@Override
	public void onBackPressed() {
			if(redNum > 0 || lotteryBeans.size() > 0){
				NormalAlertDialog dialog = new NormalAlertDialog(QLCActivity.this);
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
		if (redNum == 0) {
			ToastUtil.diaplayMesShort(this, R.string.toast_show_text_2);
			return;
		}
		
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
	}

	@Override
	public void bottom_random(View v) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(1);
		data.add(5);
		data.add(10);
		data.add(50);
		JXDialog dialog = new JXDialog(QLCActivity.this, data);
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
		if(item < 1){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_0);
//			ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择1注进行投注");
			return;
		}
		if(item *  perBetMoney >= GetString.TopBetMoney){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_1);
		}else{
			String balls;
			try {
				balls = ViewUtil.getSelectNums(autoViews, ViewUtil.SELECT_SPLIT_MAX, ViewUtil.SELECT_NUMBER_MAX);
				BetNumberBean mNumberBean = new BetNumberBean();
				mNumberBean.setBuyNumber(balls);
				mNumberBean.setPlayId("01");
				mNumberBean.setPollId(item > 1? "02": "01");
				mNumberBean.setItem(item);
				mNumberBean.setAmount(item * perBetMoney);
				lotteryBeans.add(index, mNumberBean);
				
				LogUtil.DefalutLog(mNumberBean);
				startActivity();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void startActivity(){
		SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
		SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
		Intent intent = new Intent();
		intent.setClass(QLCActivity.this, CtOrderListActivity.class);
		intent.putExtra("lid", lid);
		intent.putExtra("issue", issue);
		intent.putExtra("playMethod", "01");
		startActivityForResult(intent, GetString.BLRequestCode);
		clear();
	}

	@Override
	public void top_menu(View v) {
			stateNumber();
	}
	
	private void stateNumber(){
		if(popDialog == null){
			popDialog = new PopDialog(QLCActivity.this,R.style.popDialog);
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
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.QLC);
					if(GetString.isLogin){
						Intent intent  = new Intent(QLCActivity.this,RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						startActivity(intent);
					}else{
						Intent intent  = new Intent(QLCActivity.this,LoginActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFourthClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.QLC);
					Intent intent = new Intent();
					intent.setClass(QLCActivity.this, LotteryResultHistoryActivity.class);
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


	@Override
	public void onShake() {
		ViewUtil.getRandomButton(redBallView, sum, 7);
		redNum = 7;
		countMoney();
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
	protected void onDestroy() {
		super.onDestroy();
		
		//清除静态数据
		SafeApplication.dataMap.clear(); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
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
					temp = number.split("#");
					redNum = temp[0].split(",").length;
					countMoney();
				}
				setBetAndMoney(item);
				try {
					ViewUtil.restore(autoViews, temp, "," ,"");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void clear(){
		ViewUtil.clearBall(redBallView);
		redNum = 0;
		countMoney();
	}
	private void randomTask(final int num){
		MyAsyncTask tast = new MyAsyncTask(QLCActivity.this);
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
