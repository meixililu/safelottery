package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import com.zch.safelottery.util.NumeralMethodUtil;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class QXCActivity extends BaseLotteryActivity {
	
	private LinearLayout manyLayout;
	
	private PopDialog popDialog;
	private final int perBetMoney = 2;
	private final int sum = 10;
	private final int row = 7;
	private int[] arrayNum = new int[row];
	private int item;
	private int index;
	
	private ArrayList<BetNumberBean> lotteryBeans;
	private ArrayList<AutoWrapView> autoViews;
	private SelectInfoBean mInfoBean;
	
	private NumeralMethodUtil methodUtil = new NumeralMethodUtil();
	
	/** 筛选的下标 **/
	int[] filterSelect = new int[3];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.qxc_choice, null);
		setcView(view);
		initView();
		initData();
		initShakeListener();
	}
	private void initView(){
		manyLayout = (LinearLayout) findViewById(R.id.qxc_choice_layout);
		Button explainBtn = (Button) findViewById(R.id.stock_choice_explain_button);
		TextView prompt = (TextView) findViewById(R.id.qxc_choice_prompt);
		prompt.setText("每位至少选择1个号码投注，所选号码与开奖号码全部相同且顺序一致，即中奖500万元。");
		
		explainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QXCActivity.this, HelpDetailActivity.class);
				intent.putExtra("kind", 16);
				startActivity(intent);
			}
		});
		
		lotteryBeans = new ArrayList<BetNumberBean>();
		autoViews = new ArrayList<AutoWrapView>(7);
//		autoViews.add(null);
		
		for(int i = 0;i<row;i++){
			manyLayout.addView(wrapView(i));
		}
	}
	
	private void initData(){
		mInfoBean = new SelectInfoBean(); //初始选号信息
		mInfoBean.setLotteryId(lid);
		mInfoBean.setSumView(row);
		mInfoBean.setInitNum(0);
		mInfoBean.setItem(1);
		mInfoBean.setPlayId("01");
		mInfoBean.setPollId("01");
		mInfoBean.setRepeat(true);
		mInfoBean.setSelectBall(new int[]{1, 1, 1, 1, 1, 1, 1});
		mInfoBean.setSumBall(new int[]{10, 10, 10, 10, 10, 10, 10});
		mInfoBean.setSplit(new String[]{"", ","});
	}
	
	private View wrapView(int i){
		String strMany;
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.many_view_item, null);
		TextView manyTv= (TextView) view.findViewById(R.id.many_view_item_tv);
		AutoWrapView wrapView = (AutoWrapView) view.findViewById(R.id.many_view_item_auto_view);
		strMany = viewManyTv(i);
		manyTv.setText(strMany);
			
		ViewUtil.initBalls(getApplicationContext(), wrapView, sum, ViewUtil.COLOR_RED, ViewUtil.INIT_NUMBER_0, onClickListener);
		
		wrapView.setId(i);
		autoViews.add(wrapView);
	
		return view;
	}
	private String viewManyTv(int id){
		switch(id){
		case 0:
			return "第一位";
		case 1:
			return "第二位";
		case 2:
			return "第三位";
		case 3:
			return "第四位";
		case 4:
			return "第五位";
		case 5:
			return "第六位";
		case 6:
			return "第七位";
		default :
			return "";
		}
	}
	
	@Override
	public void onBackPressed() {
			if(methodUtil.clearBottom(arrayNum) || lotteryBeans.size() > 0){
				NormalAlertDialog dialog = new NormalAlertDialog(QXCActivity.this);
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
		for(int i=0;i<row;i++){
			if(arrayNum[i] >0){
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
				return;
			}
		}
		ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_2);
	}

	@Override
	public void bottom_random(View v) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(1);
		data.add(5);
		data.add(10);
		data.add(50);
		JXDialog dialog = new JXDialog(QXCActivity.this, data);
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
		if(methodUtil.clearBottomJudge(getApplicationContext(), arrayNum))
			return;
		
		if(item *  perBetMoney >= GetString.TopBetMoney){
			ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_text_1);
		}else{
			String balls;
			try {
				balls = ViewUtil.getSelectNums(autoViews, ViewUtil.SELECT_SPLIT_MIN, ViewUtil.SELECT_NUMBER_MIN);
				BetNumberBean mNumberBean = new BetNumberBean();
				mNumberBean.setBuyNumber(balls);
				mNumberBean.setPlayId("01");
				mNumberBean.setPollId(item > 1? "02": "01");
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
	private void startActivity(){
		SafeApplication.dataMap.put(SafeApplication.MAP_LIST_KEY, lotteryBeans);
		SafeApplication.dataMap.put(SafeApplication.MAP_INFO_KEY, mInfoBean);
		Intent intent = new Intent();
		intent.setClass(QXCActivity.this, CtOrderListActivity.class);
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
			popDialog = new PopDialog(QXCActivity.this,R.style.popDialog);
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
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.QXC);
					if(GetString.isLogin){
						Intent intent  = new Intent(QXCActivity.this,RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						startActivity(intent);
					}else{
						Intent intent  = new Intent(QXCActivity.this,LoginActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFourthClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.QXC);
					Intent intent = new Intent();
					intent.setClass(QXCActivity.this, LotteryResultHistoryActivity.class);
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
		for(int i=0;i<row;i++){
			ViewUtil.getRandomButton(autoViews.get(i), sum, 1);
			arrayNum[i] = 1;
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
					temp = number.split(",");
					for(int i = 0; i < row; i++){
						arrayNum[i] = temp[i].length();
					}
				}
				setBetAndMoney(item);
				try {
					ViewUtil.restore(autoViews, temp, "" ,"");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
			startShakeLister();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//清除静态数据
		SafeApplication.dataMap.clear(); 
	}
	
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v instanceof CheckBox){
				shake();
				CheckBox checkBox = (CheckBox) v;
				AutoWrapView autoWiew = (AutoWrapView) checkBox.getParent();
				switch (autoWiew.getId()) {
				case 0:
					if(((CheckBox) v).isChecked())
						arrayNum[0]++;
					else
						arrayNum[0]--;
					break;
				case 1:
					if(((CheckBox) v).isChecked())
						arrayNum[1]++;
					else
						arrayNum[1]--;
					break;
				case 2:
					if(((CheckBox) v).isChecked())
						arrayNum[2]++;
					else
						arrayNum[2]--;
					break;
				case 3:
					if(((CheckBox) v).isChecked())
						arrayNum[3]++;
					else
						arrayNum[3]--;
					break;
				case 4:
					if(((CheckBox) v).isChecked())
						arrayNum[4]++;
					else
						arrayNum[4]--;
					break;
				case 5:
					if(((CheckBox) v).isChecked())
						arrayNum[5]++;
					else
						arrayNum[5]--;
					break;
				case 6:
					if(((CheckBox) v).isChecked())
						arrayNum[6]++;
					else
						arrayNum[6]--;
					break;

				default:
					break;
				}
				countMoney();
			}else{
				
			}
		}
	};
	
	private void countMoney(){
		item = 1;
		for(int i=0;i<row;i++){
			if(arrayNum[i] == 0){
				item = 0;
				setBetAndMoney(item, perBetMoney);
				return ;
			}else
				item *= arrayNum[i];
		}
		setBetAndMoney(item);
	}
	
	private void clear(){
		methodUtil.clearBottomNumeral(autoViews, arrayNum);
		countMoney();
	}
	
	private void randomTask(final int num){
		MyAsyncTask tast = new MyAsyncTask(QXCActivity.this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {
			
			@Override
			public void onTaskPostExecuteListener() {
//				ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				startActivity();
			}
			
			@Override
			public Boolean onTaskBackgroundListener() {
				return lotteryBeans.addAll(0, RandomSelectUtil.getRandomNumber(num, mInfoBean));
			}
		});
		tast.execute();
	}
}
