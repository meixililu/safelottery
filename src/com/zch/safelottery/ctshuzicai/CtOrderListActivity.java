package com.zch.safelottery.ctshuzicai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.AutoPursueActivity;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.SelectInfoBean;
import com.zch.safelottery.combine.CombineStartActivity;
import com.zch.safelottery.dialogs.BeiTouZHDialog;
import com.zch.safelottery.dialogs.BeiTouZHDialog.BbeiTouZHDialogListener;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.exception.RandomException;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.parser.IssueInfoParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.AnimUtil;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.OrderListPageUtil;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.SplitTicketUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class CtOrderListActivity extends ZCHBaseActivity {

	private Context mContext;
	
	public static int FOR_REQUEST_COMBINE = 0X001;
	public static int For_AutoPursue = 1000;
	public static int FOR_RESULT_COMBINE = 0X002;
	
	private TextView titleTv, ruleContentTv, zhuihaoTv, beitouTv;
	private LinearLayout addNormalNum, addRandomNum, contentLayout;
	private LinearLayout clearLayout, startHemai, submitLayout;
	private LinearLayout beitouLayout, zhuihaoLayout;
	private CheckBox rule_cbx;
	private TextView betNumTv, moneySumTv;
	private Button autoPursue;

	private LayoutInflater inflater;
	private BeiTouZHDialog beiTouDialog;
	private BeiTouZHDialog pursueDialog;
	private BuyLotteryTask mBuyLotteryTask;

	private String lotteryId;
	private String playId;
	private String issue;
	private int winStop;
	private int betNumSum;
	private int multiple = 1;
	private int pursue;
	private long buyAmount;
	private long stopAmount;
	private long totalAmount;
	private List<BetNumberBean> buyNumberArray;
	private List<BetIssueBean> issueArray;
	private List<BetIssueBean> betIssueBeans;
	private double remainMoney;

	private SelectInfoBean mInfoBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list_page);
		mContext = this;

		Intent intent = getIntent();
		lotteryId = intent.getStringExtra("lid");
		playId = intent.getStringExtra("playMethod");
		issue = intent.getStringExtra("issue");
		buyNumberArray = (ArrayList<BetNumberBean>) SafeApplication.dataMap.get(SafeApplication.MAP_LIST_KEY);
		mInfoBean = (SelectInfoBean) SafeApplication.dataMap.get(SafeApplication.MAP_INFO_KEY);
		inflater = LayoutInflater.from(getApplicationContext());
		initViews();
		initData();
//		new LoginManager().startAutologin(mContext); //自动登录
	}

	private void initViews() {
		titleTv = (TextView) findViewById(R.id.order_list_page_title);
		ruleContentTv = (TextView) findViewById(R.id.order_list_page_rule_content);
		addNormalNum = (LinearLayout) findViewById(R.id.order_list_page_add_normal);
		addRandomNum = (LinearLayout) findViewById(R.id.order_list_page_add_random);
		contentLayout = (LinearLayout) findViewById(R.id.order_list_page_content);
		beitouLayout = (LinearLayout) findViewById(R.id.order_list_page_beitou_layout);
		zhuihaoLayout = (LinearLayout) findViewById(R.id.order_list_page_zhuihao_layout);

		rule_cbx = (CheckBox) findViewById(R.id.order_list_page_rule_cbx);
		autoPursue = (Button) findViewById(R.id.order_list_page_auto_pursue);
		zhuihaoTv = (TextView) findViewById(R.id.order_list_page_zhuihao);
		beitouTv = (TextView) findViewById(R.id.order_list_page_beitou);
		betNumTv = (TextView) findViewById(R.id.choice_bet);
		moneySumTv = (TextView) findViewById(R.id.choice_money_count);

		clearLayout = (LinearLayout) findViewById(R.id.order_list_page_clear);
		startHemai = (LinearLayout) findViewById(R.id.order_list_page_hemai);
		submitLayout = (LinearLayout) findViewById(R.id.order_list_page_submit);

		ruleContentTv.setOnClickListener(onClickListener);
		addNormalNum.setOnClickListener(onClickListener);
		addRandomNum.setOnClickListener(onClickListener);
		beitouLayout.setOnClickListener(onClickListener);
		zhuihaoLayout.setOnClickListener(onClickListener);
		clearLayout.setOnClickListener(onClickListener);
		startHemai.setOnClickListener(onClickListener);
		submitLayout.setOnClickListener(onClickListener);
		autoPursue.setOnClickListener(onClickListener);
		
		if(lotteryId.equals(LotteryId.SSCCQ) || lotteryId.equals(LotteryId.SSCJX) || lotteryId.equals(LotteryId.SYXW) || lotteryId.equals(LotteryId.NSYXW) 
				|| lotteryId.equals(LotteryId.GDSYXW) || lotteryId.equals(LotteryId.K3) || lotteryId.equals(LotteryId.K3X) ){
			autoPursue.setVisibility(View.VISIBLE);
			startHemai.setVisibility(View.GONE);
		}else{
			autoPursue.setVisibility(View.GONE);
		}
		
		titleTv.setText(LotteryId.getLotteryName(lotteryId) + "投注列表");
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		/** init lottery item **/
		int count = 0;
		int size = buyNumberArray.size();
		for (BetNumberBean bean : buyNumberArray) {
			contentLayout.addView(showLotteryBeanItem(bean, count, size));
			count++;
		}
		/** set money and bet number **/
		setBetNumAndMoney();
		setBeitouZHTv();
	}

	/**
	 * 显示每条Item
	 * 
	 * @param bean
	 *            BetNumberBean每条的数据
	 * @param count
	 *            当前第几条
	 * @param size
	 *            总条数
	 * @return View
	 */
	private View showLotteryBeanItem(final BetNumberBean bean, int count, int size) {
		final View view = (View) inflater.inflate(R.layout.order_list_page_item, null);
		try {
			LinearLayout ballLayout = (LinearLayout) view.findViewById(R.id.order_list_page_item_ball_layout);
			LinearLayout ballLayoutView = (LinearLayout) view.findViewById(R.id.order_list_page_item_view);
			CheckBox showDelete = (CheckBox) view.findViewById(R.id.order_list_page_item_show_delete);
			TextView redBall = (TextView) view.findViewById(R.id.order_list_page_item_redballs);
			TextView blueBall = (TextView) view.findViewById(R.id.order_list_page_item_blueballs);
			TextView betNum = (TextView) view.findViewById(R.id.order_list_page_item_bet_num);
			final Button delete_btn = (Button) view.findViewById(R.id.order_list_page_item_delete_btn);
			ImageView img = (ImageView) view.findViewById(R.id.order_list_page_item_dotted_line);
			TextView markTv = (TextView) view.findViewById(R.id.order_list_page_item_play_method);
			
			betNumSum += bean.getItem();
			betNum.setText(bean.getItem() + "注");
			
			String[] balls = OrderListPageUtil.getBalls(bean, mInfoBean, markTv);

			redBall.setText(balls[0] != null ? specialDispose(ballLayoutView, redBall, balls[0]) : "");

			blueBall.setText(balls[1] != null ? balls[1] : "");

			if (size == 1) {
				img.setVisibility(View.GONE);
			} else if (count == (size - 1)) {
				img.setVisibility(View.GONE);
			}

			showDelete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						delete_btn.setVisibility(View.VISIBLE);
					} else {
						delete_btn.setVisibility(View.GONE);
					}
				}
			});
			delete_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					contentLayout.removeView(view);
					buyNumberArray.remove(bean);
					betNumSum -= bean.getItem();
					setBeitouZHTv();
					setBetNumAndMoney();
				}
			});
			ballLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = buyNumberArray.indexOf(bean);
					Intent intent = new Intent();
					intent.putExtra("index", position);
					setResult(GetString.BLAlter, intent);
					finish();
				}
			});
		} catch (Exception e) {
			if (Settings.DEBUG) {
				LogUtil.ExceptionLog("showLotteryBeanItem");
				e.printStackTrace();
			}
		}
		return view;
	}

	/*****
	 * 除 数字外的特别信息显示
	 * @param ballLayoutView
	 * @param redBall
	 * @param balls
	 * @return
	 */
	private String specialDispose(LinearLayout ballLayoutView, TextView redBall, String balls) {
		if ((lotteryId.equals(LotteryId.SSCCQ) || lotteryId.equals(LotteryId.SSCJX)) && playId.equals("30")) { // 时时彩// 大小单双玩法
			balls = balls.replace("9", "大");
			balls = balls.replace("0", "小");
			balls = balls.replace("1", "单");
			balls = balls.replace("2", "双");
		}
		return balls;
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.order_list_page_rule_content:
				Intent intent_xieyi = new Intent(CtOrderListActivity.this, HelpDetailActivity.class);
				intent_xieyi.putExtra("kind", 14);
				startActivity(intent_xieyi);
				break;
			case R.id.order_list_page_add_normal:
				StatService.onEvent(CtOrderListActivity.this, "order-add picking", "投注列表-增加普通号码", 1);
				setResult(GetString.BLAdd);
				finish();
				break;
			case R.id.order_list_page_add_random:
				StatService.onEvent(CtOrderListActivity.this, "order-add radomly", "投注列表-增加机选号码", 1);
				if (mInfoBean != null) {
					buyNumberArray.addAll(0, RandomSelectUtil.getRandomNumber(1, mInfoBean)); // 随机生成的一注
																								// 添加到第一位
				} else {
					try {
						throw new RandomException();
					} catch (RandomException e) {
						e.printStackTrace();
					}
				}
				View view = showLotteryBeanItem(buyNumberArray.get(0), 0, 2);
				contentLayout.addView(view, 0);
				view.startAnimation(AnimUtil.getAnim(getApplicationContext(), "fade_in"));
				setBetNumAndMoney();
				break;
			case R.id.order_list_page_beitou_layout:
				showBeitouDialog();
				break;
			case R.id.order_list_page_zhuihao_layout:
				getPurserIssue(0);
				break;
			case R.id.order_list_page_clear:
				if (buyNumberArray.size() > 0) {
					NormalAlertDialog dialog = new NormalAlertDialog(CtOrderListActivity.this);
					dialog.setTitle("提示");
					dialog.setContent("您确定要清空当前选择的号码吗？");
					dialog.setOk_btn_text("确定");
					dialog.setCancle_btn_text("取消");
					dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
						public void onOkBtnClick() {
							contentLayout.removeAllViews();
							buyNumberArray.clear();
							multiple = 1;
							pursue = 0;
							betNumSum = 0;
							setBeitouZHTv();
							setBetNumAndMoney();
						}

						@Override
						public void onCancleBtnClick() {
						}
					});
					dialog.show();
				} else {
					ToastUtil.diaplayMesShort(getApplicationContext(), "您还没选择任何号码");
				}

				break;
			case R.id.order_list_page_hemai:
				StatService.onEvent(CtOrderListActivity.this, "order-start a joinin", "投注列表-发起合买", 1);
				if (!rule_cbx.isChecked()) {
					ToastUtil.diaplayMesShort(getApplicationContext(), "请确认已同意《用户服务协议》");
				} else {
					if (betNumSum > 0) {
							if (pursue == 0) {
								 getIssue();
								 SafeApplication.dataMap.put("BetLotteryBean", getBetLotteryBean());
									Bundle mBundle = new Bundle();
									Intent intent = new Intent(CtOrderListActivity.this, CombineStartActivity.class);
									intent.putExtra(Settings.BUNDLE, mBundle);
									startActivityForResult(intent, FOR_REQUEST_COMBINE);
							} else {
								ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_show_hemai_pursue);
							}
					} else {
						ToastUtil.diaplayMesShort(getApplicationContext(), "投注列表为空");
					}
			}
				
				break;
			case R.id.order_list_page_submit:
				if (!rule_cbx.isChecked()) {
					ToastUtil.diaplayMesShort(getApplicationContext(), "请确认已同意《用户服务协议》");
				} else {
					if(!DoubleClickUtil.isFastDoubleClick()){
						betSubmit();
					}
				}
				break;
			case R.id.order_list_page_auto_pursue://盈利追号
				if (!rule_cbx.isChecked()) {
					ToastUtil.diaplayMesShort(getApplicationContext(), "请确认已同意《用户服务协议》");
				} else {
					getPurserIssue(2);
				}
				getEvent(lotteryId);
				break;
			}
		}
	};

	private void getEvent(String lid){
		if(lid.equals(LotteryId.K3)){
			StatService.onEvent(CtOrderListActivity.this, "kuai3-setprofit", "快三-盈利追号", 1);
		}else if(lid.equals(LotteryId.SSCCQ)){
			StatService.onEvent(CtOrderListActivity.this, "ssccq-setprofit", "重庆时时彩-盈利追号", 1);
		}else if(lid.equals(LotteryId.SSCJX)){
			StatService.onEvent(CtOrderListActivity.this, "sscjx-setprofit", "江西时时彩-盈利追号", 1);
		}else if(lid.equals(LotteryId.SYXW)){
			StatService.onEvent(CtOrderListActivity.this, "5in11-setprofit", "山东11选5-盈利追号", 1);
		}else if(lid.equals(LotteryId.NSYXW)){
			StatService.onEvent(CtOrderListActivity.this, "new5in11-setprofit", "安徽11选5-盈利追号", 1);
		}else if(lid.equals(LotteryId.GDSYXW)){
			StatService.onEvent(CtOrderListActivity.this, "gd5in11-setprofit", "广东11选5-盈利追号", 1);
		}
	}
	
	private void betSubmit() {
		if (betNumSum > 0) {
			if (GetString.isLogin) {
				remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
				if (remainMoney >= totalAmount) {
					String newIssue = getIssue();
					if(TextUtils.isEmpty(newIssue)){
						startTask();
					}else if (issue.equals(newIssue)) {
						startTask();
					} else {
						issueOutOfDate(newIssue);
					}
				} else if (remainMoney < totalAmount) {
					PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(CtOrderListActivity.this);
					dialog.setMoney(totalAmount - remainMoney);
					dialog.show();
				}
			} else {
				Intent intent = new Intent(CtOrderListActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		} else {
			ToastUtil.diaplayMesShort(getApplicationContext(), "投注列表为空");
		}
	}

	/** 投注的彩期 **/
	private String getIssue(){
		betIssueBeans = new ArrayList<BetIssueBean>(pursue+1);
		String newIssue = LotteryId.getIssue(lotteryId);
//		写入在售期
		BetIssueBean betIssueBean = new BetIssueBean();
		betIssueBean.setIssue(issue);
		betIssueBean.setMultiple(multiple);
		betIssueBeans.add(betIssueBean);
		
		//有追期 添加追期期次
		if(this.issueArray != null){ 
			for(int i = 0; i < pursue; i++){
				betIssueBean = new BetIssueBean();
				betIssueBean.setIssue(issueArray.get(i).getIssue());
				betIssueBean.setMultiple(multiple);
				betIssueBeans.add(betIssueBean);
			}
		}
		
		return newIssue; //返回当前最新的期次
	}
	
	private void startTask() {
		submitLayout.setEnabled(false);
		mBuyLotteryTask = new BuyLotteryTask(this,"", getBetJson(),submitLayout);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				
				final SucceedDialog dialog = new SucceedDialog(CtOrderListActivity.this, lotteryId, issue, betNumSum, pursue, multiple, totalAmount, (remainMoney - totalAmount));
				// dialog.setCancelable(false);
				dialog.setOnDialogClickListener(new OnDialogClickListener() {
					@Override
					public void onPositiveButtonClick() {
						Settings.closeOtherActivitySetCurTabOne(CtOrderListActivity.this);
						dialog.dismiss();
						finish();
//						new FenxiangUtil(CtOrderListActivity.this, LotteryId.getLotteryName(lotteryId), 1);
					}

					@Override
					public void onNegativeButtonClick() {
						dialog.dismiss();
						buyNumberArray.clear();
						finish();
					}
				});
				dialog.show();
			}
		});
		mBuyLotteryTask.send();
	}
	
	private String getBetJson() {
		BetLotteryBean mLotteryBean = getBetLotteryBean();
		if(!checkNumberIsRight(mLotteryBean)){
			return "";
		}
		return JsonUtils.toJsonStr(mLotteryBean);
	}
	
	private BetLotteryBean getBetLotteryBean(){
		BetLotteryBean mLotteryBean = new BetLotteryBean();
		if(GetString.userInfo != null){
			mLotteryBean.setUserCode(GetString.userInfo.getUserCode());
		}
		
		mLotteryBean.setAppVersion(SystemInfo.softVerCode);
		mLotteryBean.setLotteryId(lotteryId);
		mLotteryBean.setPlayId(playId);
		mLotteryBean.setBuyType(pursue > 0 ? 4 : 1);
		
		mLotteryBean.setBuyAmount(buyAmount);
		mLotteryBean.setWinStop(winStop);
		
		mLotteryBean.setStopAmount(stopAmount);
		mLotteryBean.setTotalAmount(totalAmount);
		
		mLotteryBean.setBuyNumberArray( getNumberArray(buyNumberArray));
		mLotteryBean.setIssueArray(betIssueBeans);
		return mLotteryBean;
	}
	
	private List<BetNumberBean> getNumberArray(List<BetNumberBean> buyNumberArray){
		if((lotteryId.equals(LotteryId.SYXW) || lotteryId.equals(LotteryId.NSYXW) || lotteryId.equals(LotteryId.GDSYXW)) && (playId.equals("10") || playId.equals("11"))){
			SplitTicketUtil mSplitTicketUtil = SplitTicketUtil.getSplitTicketUtil();
			ArrayList<BetNumberBean> tempLists = new ArrayList<BetNumberBean>();
			for(BetNumberBean mBean: buyNumberArray){
				mSplitTicketUtil.start(mBean.getBuyNumber(), ViewUtil.SELECT_SPLIT_MAX, ViewUtil.SELECT_SPLIT_MIN, SplitTicketUtil.SPLIT_REMOVE_REPEAT);
				List<String> ballLists = mSplitTicketUtil.getRestlt();
				ArrayList<BetNumberBean> tempBeans = new ArrayList<BetNumberBean>();
				for(String s: ballLists){
					BetNumberBean mNumberBean = new BetNumberBean();
					mNumberBean.setBuyNumber(s);
					mNumberBean.setPlayId(playId);
					mNumberBean.setPollId("01");
					mNumberBean.setItem(1);
					mNumberBean.setAmount(2);
					tempBeans.add(mNumberBean);
					LogUtil.DefalutLog(mNumberBean);
				}
				tempLists.addAll(tempBeans);
			}
			buyNumberArray = tempLists;
		}else if(lotteryId.equals(LotteryId.SSCJX) ){
			if(playId.equals("12")){
				SplitTicketUtil mSplitTicketUtil = SplitTicketUtil.getSplitTicketUtil();
				ArrayList<BetNumberBean> tempLists = new ArrayList<BetNumberBean>();
				for(BetNumberBean mBean: buyNumberArray){
					mSplitTicketUtil.start(mBean.getBuyNumber(), ViewUtil.SELECT_SPLIT_MIN, ViewUtil.SELECT_SPLIT_NULL, 2, true);
					List<String> ballLists = mSplitTicketUtil.getRestlt();
					ArrayList<BetNumberBean> tempBeans = new ArrayList<BetNumberBean>();
					for(String s: ballLists){
						BetNumberBean mNumberBean = new BetNumberBean();
						mNumberBean.setBuyNumber(s);
						mNumberBean.setPlayId(playId);
						mNumberBean.setPollId("01");
						mNumberBean.setItem(1);
						mNumberBean.setAmount(2);
						tempBeans.add(mNumberBean);
						LogUtil.DefalutLog(mNumberBean);
					}
					tempLists.addAll(tempBeans);
				}
				buyNumberArray = tempLists;
			}else if(playId.equals("11")){
				SplitTicketUtil mSplitTicketUtil = SplitTicketUtil.getSplitTicketUtil();
				ArrayList<BetNumberBean> tempLists = new ArrayList<BetNumberBean>();
				for(BetNumberBean mBean: buyNumberArray){
					mSplitTicketUtil.start(mBean.getBuyNumber(), ViewUtil.SELECT_SPLIT_MIN, ViewUtil.SELECT_SPLIT_NULL, 1, true);
					List<String> ballLists = mSplitTicketUtil.getRestlt();
					ArrayList<BetNumberBean> tempBeans = new ArrayList<BetNumberBean>();
					for(String s: ballLists){
						BetNumberBean mNumberBean = new BetNumberBean();
						mNumberBean.setBuyNumber(s);
						mNumberBean.setPlayId(playId);
						mNumberBean.setPollId("01");
						mNumberBean.setItem(1);
						mNumberBean.setAmount(2);
						tempBeans.add(mNumberBean);
						LogUtil.DefalutLog(mNumberBean);
					}
					tempLists.addAll(tempBeans);
				}
				buyNumberArray = tempLists;
			}else if(playId.equals("07")){
				SplitTicketUtil mSplitTicketUtil = SplitTicketUtil.getSplitTicketUtil();
				ArrayList<BetNumberBean> tempLists = new ArrayList<BetNumberBean>();
				for(BetNumberBean mBean: buyNumberArray){
					mSplitTicketUtil.start(mBean.getBuyNumber(), ViewUtil.SELECT_SPLIT_MIN, ViewUtil.SELECT_SPLIT_NULL, 2, false);
					List<String> ballLists = mSplitTicketUtil.getAddRepeatRestlt();
					ArrayList<BetNumberBean> tempBeans = new ArrayList<BetNumberBean>();
					for(String s: ballLists){
						BetNumberBean mNumberBean = new BetNumberBean();
						mNumberBean.setBuyNumber(s);
						mNumberBean.setPlayId(playId);
						mNumberBean.setPollId("01");
						mNumberBean.setItem(1);
						mNumberBean.setAmount(2);
						tempBeans.add(mNumberBean);
						LogUtil.DefalutLog(mNumberBean);
					}
					tempLists.addAll(tempBeans);
				}
				buyNumberArray = tempLists;
			}else if(playId.equals("08")){
				SplitTicketUtil mSplitTicketUtil = SplitTicketUtil.getSplitTicketUtil();
				ArrayList<BetNumberBean> tempLists = new ArrayList<BetNumberBean>();
				for(BetNumberBean mBean: buyNumberArray){
					mSplitTicketUtil.start(mBean.getBuyNumber(), ViewUtil.SELECT_SPLIT_MIN, ViewUtil.SELECT_SPLIT_NULL, 3, false);
					List<String> ballLists = mSplitTicketUtil.getRestlt();
					ArrayList<BetNumberBean> tempBeans = new ArrayList<BetNumberBean>();
					for(String s: ballLists){
						BetNumberBean mNumberBean = new BetNumberBean();
						mNumberBean.setBuyNumber(s);
						mNumberBean.setPlayId(playId);
						mNumberBean.setPollId("01");
						mNumberBean.setItem(1);
						mNumberBean.setAmount(2);
						tempBeans.add(mNumberBean);
						LogUtil.DefalutLog(mNumberBean);
					}
					tempLists.addAll(tempBeans);
				}
				buyNumberArray = tempLists;
			}
		}else{
			if(LotteryId.K3X.equals(lotteryId) && playId.equals("02")){
				for(BetNumberBean mBean: buyNumberArray){
					if(mBean.getIndex() == 4){
						String[] number = mBean.getBuyNumber().split(";");
						StringBuilder sb = new StringBuilder();
						for(String n: number){
							sb.append(";");
							sb.append(n.substring(0, 1));
						}
						if(sb.length() > 0) sb.deleteCharAt(0);
						mBean.setBuyNumber(sb.toString());
					}
				}
			}
		}
		return buyNumberArray;
	}
	
	private boolean checkNumberIsRight(BetLotteryBean mLotteryBean){
		String playId = mLotteryBean.getPlayId();
		for(BetNumberBean bean : buyNumberArray){
			if(!bean.getPlayId().equals(playId)){
				return false;
			}
		}
		return true;
	}

	private void issueOutOfDate(final String newIssue) {
		NormalAlertDialog dialog = new NormalAlertDialog(CtOrderListActivity.this);
		dialog.setTitle("期次变更提示");
		dialog.setContent("您当前投注的期次已变更为" + newIssue + "期，是否继续投注？");
		dialog.setOk_btn_text("继续投注");
		dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
			@Override
			public void onOkBtnClick() {
				issue = newIssue;
				if(pursue > 0){ //选择追期的情况下 
					issueArray = null;
					getPurserIssue(1); //重新去获取追期列表
				}else{
					startTask();
				}
			}

			@Override
			public void onCancleBtnClick() {

			}
		});
		dialog.show();
	}

	private void showBeitouDialog() {
		if (beiTouDialog == null) {
			beiTouDialog = new BeiTouZHDialog(mContext, multiple, BeiTouZHDialog.Max, BeiTouZHDialog.INIT_BEITOU);
			beiTouDialog.listener = new BbeiTouZHDialogListener() {
				@Override
				public void onOkBtnClick(View v, int value, boolean isStopPursue) {
					multiple = value;
					setBeitouZHTv();
					setBetNumAndMoney();
				}
			};
		} else {
			beiTouDialog.setBeiTouZH(multiple, BeiTouZHDialog.Max);
		}
		beiTouDialog.show();
	}
	
	private void showPursueDialog() {
		if(issueArray.size() <= 0){
			ToastUtil.diaplayMesShort(mContext, "当前无可追期次");
			return;
		}
		if (pursueDialog == null) {
			pursueDialog = new BeiTouZHDialog(mContext, pursue, issueArray.size(), BeiTouZHDialog.INIT_PURSUE);
			pursueDialog.listener = new BbeiTouZHDialogListener() {
				@Override
				public void onOkBtnClick(View v, int value, boolean isStopPursue) {
					pursue = value > 0? value - 1: value;
					setBeitouZHTv();
					setBetNumAndMoney();
					if(isStopPursue){
						winStop = 1;
					}else{
						winStop = 0;
					}
				}
			};
		} else {
			pursueDialog.setBeiTouZH(pursue + 1, issueArray.size());
		}
		pursueDialog.show();
	}


	private List<IssueInfoBean> beanList;
	private ProgressDialog mDialog;
	/**
	 * 取得可追期次
	 * @param mark 标实  0：显示dialog，1：购买彩票，2：盈利追号
	 * @return
	 */
	private void getPurserIssue(final int mark){
		if(issueArray == null){
			mDialog = ProgressDialog.show(mContext, "", "获取预售期...", true, true);
			Map<String, String> map = new HashMap<String, String>();
			map.put("lotteryId", lotteryId);
			map.put("page", "1");
			map.put("pageSize", "99");
			SafelotteryHttpClient.post(this, "3301", "pre", JsonUtils.toJsonStr(map), new TypeMapHttpResponseHandler(this,true) {
				@Override
				public void onSuccess(int statusCode, Map mMap) {
					if(mMap != null){
						String resutl = (String) mMap.get("issueList");
						beanList = (List<IssueInfoBean>)JsonUtils.parserJsonArray( resutl, new IssueInfoParser());
						if(beanList != null){
							BetIssueBean betIssueBean;
							issueArray = new ArrayList<BetIssueBean>();
							for(IssueInfoBean mBean: beanList){
								betIssueBean = new BetIssueBean();
								if(mBean.getStatus().equals("0")){
									betIssueBean.setIssue(mBean.getName());
									issueArray.add(betIssueBean);
								}
							}
							if(issueArray.size() > 0){
								String newIssue = issueArray.get(0).getIssue();
								if(!TextUtils.isEmpty(newIssue) && newIssue.equals(issue)){
									issueArray.remove(0);
								}
							}
							if(mark == 1){
								getIssue();
								startTask();
							}else if(mark == 0){
								showPursueDialog();
							}else if(mark == 2){
								toAutoPursueActivity();
							}
						}
					}
				}
				@Override
				public void onFinish() {
					mDialog.dismiss();
				};
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
				}
				
			});
		}else{
			if(mark == 2){
				toAutoPursueActivity();
			}else{
				showPursueDialog();
			}
		}
	}
	
	private void toAutoPursueActivity(){
		if (betNumSum > 0) {
			multiple = 1;
			pursue = 0;
			setBeitouZHTv();
			setBetNumAndMoney();
			getIssue();
			SafeApplication.dataMap.put("BetLotteryBean", getBetLotteryBean());
			SafeApplication.dataMap.put("issueArray", issueArray);
			Intent intent = new Intent(CtOrderListActivity.this, AutoPursueActivity.class);
			intent.putExtra("issue", issue);
			startActivityForResult(intent, For_AutoPursue);
		} else {
			ToastUtil.diaplayMesShort(getApplicationContext(), "投注列表为空");
		}
	}
	
	private void setBeitouZHTv() {
		beitouTv.setText(multiple + "倍");
		zhuihaoTv.setText(pursue + "期");
	}

	private void setBetNumAndMoney() {
		//计算投注的Money
		totalAmount = 0;
		for(BetNumberBean bean : buyNumberArray){ //对列表进行累加
			totalAmount += bean.getAmount();
		}
		totalAmount = totalAmount * multiple * (pursue + 1);
		
		betNumTv.setText("共" + betNumSum + "注,");
		moneySumTv.setText("共" + totalAmount + "元");
	}

	@Override
	public void onBackPressed() {
		if (buyNumberArray.size() > 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(CtOrderListActivity.this);
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
		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void clear() {
		buyNumberArray.clear();
		multiple = 1;
		pursue = 0;
		finish();
	}
	/*
	 * 合买页面返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == FOR_REQUEST_COMBINE){
			if(resultCode == FOR_RESULT_COMBINE){
				clear();
			}
		}else if(requestCode == For_AutoPursue){
			if(resultCode == RESULT_OK){
				clear();
			}
		}
	}
}
