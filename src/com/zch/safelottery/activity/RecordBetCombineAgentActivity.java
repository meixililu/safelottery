package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordMainIssueListBean;
import com.zch.safelottery.bean.RecordMatchBean;
import com.zch.safelottery.bean.RecordProgramsListBean;
import com.zch.safelottery.bean.RecordSubGameListBean;
import com.zch.safelottery.bean.RecordUserInfoBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.CombineBuyListDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.RecordMainIssueListParser;
import com.zch.safelottery.parser.RecordMatchParser;
import com.zch.safelottery.parser.RecordProgramsListParser;
import com.zch.safelottery.parser.RecordSubGameListParser;
import com.zch.safelottery.parser.RecordUserInfoParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.JC_HHGG_utils;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.LotteryResultUtils;
import com.zch.safelottery.util.StatusUtil;
import com.zch.safelottery.util.ToastUtil;

public class RecordBetCombineAgentActivity extends ZCHBaseActivity {

	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "BuyLotteryAgentActivity";
	public final int BET_RECORD_DETAIL = 1318;

	private ProgressBar progressbar;
	private Dialog dialog;
	private LinearLayout scheme_linear, expand1, expand2, expand3, current_period_num_linear, hide1_lin, hide2_lin;
	private TextView title, initiator, person_reward_sum, prize_times, bet_num, buy_mothed, scheme_state, reward_at_sum, see_all;
	private TextView multiple, scheme_id, play_mothed, bet_time, lottery_result, scheme_num;
	private TextView current_period_result, no_result, scheme_money;

	private LinearLayout people_layout;
	private TextView people_count;

	private Button btn_buy_lottery;
	private CheckBox hide1, hide2;

	private String lotteryId;
	
	private View scheme_14or9, result_layout;
	private TextView danma_title, chuanfa_tv, error_tv;
	private LinearLayout scheme_14or9_layout, scheme_14or9_layout_title;
	private boolean isShowDan;
	private PullToRefreshScrollView scrollview;
	private LinearLayout scrollview_child;

	private String str;
	private Button backout_button;

	double userMoney;
	double amount;
	double money;
	private int step_one = 1;// 1.方案详情 2.再买一遍
	private String programsOrderId;
	private RecordUserInfoBean recordUserInfoBean;
	private RecordProgramsListBean recordProgramsListBean;
	private RecordMainIssueListBean recordMainIssueListBean;
	private List<RecordSubGameListBean> recordSubGameListBean;
	private RecordMatchBean recordMatchBean;
	private String userCode;
	private String buytype;
	private int winnum = 0;// 0：未开奖 1 ：已中奖 2：未中奖

	private TextView buy_lottery_myreward_layout, buy_lottery_afterbonus_tx, buy_lottery_schemetype_tx;

	private TextView buy_lottery_floorsAmount, buy_lottery_buyAmount, buy_lottery_afterbonus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			if (DEBUG)
				Log.d(Settings.TAG, TAG + "-onCreate()");
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.buy_lottery_combine__agent);
			Intent intent = getIntent();
			programsOrderId = intent.getStringExtra("programsOrderId");
			userCode = GetString.userInfo.getUserCode();
			initUI();
			if (!TextUtils.isEmpty(programsOrderId)) {
				doRequestTask(false);
			} else {
				Toast.makeText(RecordBetCombineAgentActivity.this, "方案号有误,请刷新重试！", 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-initUI()");
		progressbar = (ProgressBar) findViewById(R.id.buy_lottery_agent_progressbar_big);
		scrollview = (PullToRefreshScrollView) findViewById(R.id.scrollview);
		scrollview_child = (LinearLayout) findViewById(R.id.scrollview_child);

		scheme_linear = (LinearLayout) findViewById(R.id.buy_lottery_agent_scheme_linear);
		expand3 = (LinearLayout) findViewById(R.id.buy_lottery_expand_three);

		hide1_lin = (LinearLayout) findViewById(R.id.buy_lottery_hide1_lin);
		hide2_lin = (LinearLayout) findViewById(R.id.buy_lottery_hide2_lin);
		hide1_lin.setOnClickListener(new BtnOnClickListener());
		hide2_lin.setOnClickListener(new BtnOnClickListener());

		btn_buy_lottery = (Button) findViewById(R.id.buy_lottery_buy);
		title = (TextView) findViewById(R.id.buy_lottery_title);
		initiator = (TextView) findViewById(R.id.buy_lottery_initiator);
		// scores = (TextView)findViewById(R.id.buy_lottery_scores);
		hide1 = (CheckBox) findViewById(R.id.buy_lottery_hide1);
		expand1 = (LinearLayout) findViewById(R.id.buy_lottery_expand_one);
		person_reward_sum = (TextView) findViewById(R.id.buy_lottery_reward);
		prize_times = (TextView) findViewById(R.id.buy_lottery_prize_times);
		bet_num = (TextView) findViewById(R.id.buy_lottery_number);
		multiple = (TextView) findViewById(R.id.buy_lottery_multiple);

		error_tv = (TextView) findViewById(R.id.buy_lottery_agent_error_tv);

		hide2 = (CheckBox) findViewById(R.id.buy_lottery_hide2);
		expand2 = (LinearLayout) findViewById(R.id.buy_lottery_expand_two);
		scheme_id = (TextView) findViewById(R.id.buy_lottery_scheme_id);

		play_mothed = (TextView) findViewById(R.id.buy_lottery_play_mothed);
		buy_mothed = (TextView) findViewById(R.id.buy_lottery_buy_mothed);
		bet_time = (TextView) findViewById(R.id.buy_lottery_bet_time);
		scheme_state = (TextView) findViewById(R.id.buy_lottery_scheme_state);
		lottery_result = (TextView) findViewById(R.id.buy_lottery_result);
		reward_at_sum = (TextView) findViewById(R.id.buy_lottery_reward_at_sum);
		scheme_money = (TextView) findViewById(R.id.buy_lottery_scheme_money);

		scheme_num = (TextView) findViewById(R.id.buy_lottery_scheme_num);
		see_all = (TextView) findViewById(R.id.buy_lottery_see_all);
		current_period_result = (TextView) findViewById(R.id.buy_lottery_current_period_result);
		current_period_num_linear = (LinearLayout) findViewById(R.id.buy_lottery_current_period_num_linear);
		no_result = (TextView) findViewById(R.id.buy_lottery_current_period_no_result);

		people_layout = (LinearLayout) findViewById(R.id.combine_people_layout);
		people_count = (TextView) findViewById(R.id.buy_lottery_combine_people_count);

		result_layout = (View) findViewById(R.id.result_linearlayout);
		scheme_14or9 = (View) findViewById(R.id.buy_lottery_agent_scheme_14or9);
		danma_title = (TextView) findViewById(R.id.danma_title);
		scheme_14or9_layout = (LinearLayout) findViewById(R.id.cz_14or9_viewpage_list);
		scheme_14or9_layout_title = (LinearLayout) findViewById(R.id.buy_lottery_agent_scheme_14or9_title);
		chuanfa_tv = (TextView) findViewById(R.id.chuanfa_tv);

		backout_button = (Button) findViewById(R.id.backout_button);

		buy_lottery_floorsAmount = (TextView) findViewById(R.id.buy_lottery_floorsAmount);
		buy_lottery_buyAmount = (TextView) findViewById(R.id.buy_lottery_buyAmount);
		buy_lottery_afterbonus = (TextView) findViewById(R.id.buy_lottery_afterbonus);

		buy_lottery_myreward_layout = (TextView) findViewById(R.id.buy_lottery_myreward_tx);
		buy_lottery_afterbonus_tx = (TextView) findViewById(R.id.buy_lottery_afterbonus_tx);

		buy_lottery_schemetype_tx = (TextView) findViewById(R.id.buy_lottery_schemetype_tx);

		BtnOnClickListener onClickListener = new BtnOnClickListener();
		see_all.setOnClickListener(onClickListener);
		btn_buy_lottery.setOnClickListener(onClickListener);
		error_tv.setOnClickListener(onClickListener);
		backout_button.setOnClickListener(onClickListener);
		people_layout.setOnClickListener(onClickListener);
		
		scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (userCode != null && !userCode.equals("")) {
					doRequestTask(false);
				} else {
					scrollview.onRefreshComplete();
					Toast.makeText(RecordBetCombineAgentActivity.this, "登录超时，请重新登录！", 0).show();
				}
			}
		});
	}

	private void showData() throws Exception {
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-showData()");
		lotteryId = recordProgramsListBean.getLotteryId();

		initiator.setText(recordUserInfoBean.getUserName());
		person_reward_sum.setText(recordUserInfoBean.getBonusTotal());
		prize_times.setText(recordUserInfoBean.getBonusCount());

		scheme_money.setText(recordProgramsListBean.getTotalWere() + "元");
		play_mothed.setText(recordProgramsListBean.getPlayName());// 玩法

		bet_num.setText(recordProgramsListBean.getItem());// 注数
		multiple.setText(recordProgramsListBean.getMultiple());// 倍投

		buy_lottery_floorsAmount.setText(recordProgramsListBean.getLastWere() + "元");
		buy_lottery_buyAmount.setText(recordUserInfoBean.getLoginOrderAmount() + "元");
		buy_lottery_afterbonus.setText(recordProgramsListBean.getCommission() + "%");

		people_count.setText("共" + recordProgramsListBean.getRenGouCount() + "人");
		/*
		 * 购买方式
		 */
		buytype = StatusUtil.getBuyType(recordProgramsListBean.getBuyType());
		buy_mothed.setText(buytype);// 购买方式显示

		String ProgramsOrderId = StatusUtil.getSchemeShortId(recordProgramsListBean.getProgramsOrderId());
		scheme_id.setText(ProgramsOrderId);// 方案id
		bet_time.setText(recordProgramsListBean.getCreateTime());// 投注时间

		String orderStatus = StatusUtil.getOrderStatus(recordProgramsListBean.getOrderStatus());
		scheme_state.setText(orderStatus);// 方案状态

		if(recordProgramsListBean.getIsCancelPrograms().equals("0")||!userCode.equals(recordUserInfoBean.getUserCode())){
			backout_button.setVisibility(View.GONE);
		}
		
		if (recordProgramsListBean.getOrderStatus().equals("6")) {
			backout_button.setText("已撤单");
			backout_button.setClickable(false);
		}

		if (lotteryId.equals(LotteryId.SFC)) {
			if (recordProgramsListBean.getPlayId().equals("02")) {
				lotteryId = LotteryId.RX9;
			}
		}
		/** 1为普通数字彩，2为14场任选9,3为北单竞彩 **/
		title.setText(LotteryId.getLotteryName(lotteryId) + "-方案详情");

		winnum = ConversionUtil.StringToInt(recordProgramsListBean.getBonusStatus());
		String bonusStatusStr = StatusUtil.getBonusStatus(winnum); // 需要中奖状态，来判断是否需要隐藏中奖税后金额
		int buynum = ConversionUtil.StringToInt(recordProgramsListBean.getOrderStatus());// 获取方案状态判断是否投注失败

		if (LotteryId.getLotteryNum(lotteryId) == 1) {
			if (winnum == 0) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
				current_period_result.setText("预计开奖时间(" + recordProgramsListBean.getIssue() + "期）");
				no_result.setText(recordMainIssueListBean.getBonusTime());
			} else if (winnum == 1) {
				if (recordProgramsListBean.getBonusToAccount().equals("0")) {
					resultView(buynum, "中奖未派");
					LotteryResultUtils.getLotteryResultView(getApplicationContext(), current_period_num_linear, recordProgramsListBean.getLotteryId(), recordProgramsListBean.getNumberInfo(),
							recordMainIssueListBean, false, true);
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText("--");
				} else {
					resultView(buynum, "已派奖");
					LotteryResultUtils.getLotteryResultView(getApplicationContext(), current_period_num_linear, recordProgramsListBean.getLotteryId(), recordProgramsListBean.getNumberInfo(),
							recordMainIssueListBean, false, true);
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText(recordProgramsListBean.getBonusAmount());
					if (recordProgramsListBean.getBuyType().equals("2")) {
						if(recordUserInfoBean.getLoginBonusAmount().equals("0.00")){
							buy_lottery_myreward_layout.setText("<0.01元");
						}else{	
							buy_lottery_myreward_layout.setText(recordUserInfoBean.getLoginBonusAmount());
						}
						buy_lottery_afterbonus_tx.setText(recordProgramsListBean.getCommissionAmount());
						backout_button.setVisibility(View.GONE);
					}
				}
			} else if (winnum == 2) {
				resultView(buynum, bonusStatusStr);
				LotteryResultUtils.getLotteryResultView(getApplicationContext(), current_period_num_linear, recordProgramsListBean.getLotteryId(), recordProgramsListBean.getNumberInfo(),
						recordMainIssueListBean, false, true);
				expand3.setVisibility(View.GONE);
			}
			
			// 方案内容是否显示
			if(!TextUtils.isEmpty(recordProgramsListBean.getBigNumberInfo()) && recordProgramsListBean.getBigNumberInfo().equals("1") && recordProgramsListBean.getNumberInfo().size() == 0){
				see_all.setVisibility(View.GONE);
				if(recordProgramsListBean.getIsUpload().equals("2")){
					buy_lottery_schemetype_tx.setText("方案未上传");
				}else{
					buy_lottery_schemetype_tx.setText("方案内容过大，请到网站查看");
				}
			}else{
				see_all.setVisibility(View.VISIBLE);
				LotteryResultUtils.getLotteryResultView(getApplicationContext(), scheme_linear, lotteryId, recordProgramsListBean.getNumberInfo(), recordMainIssueListBean, false, false);
				buy_lottery_schemetype_tx.setText("方案内容(" + StatusUtil.getPrivacy(recordProgramsListBean.getPrivacy()) + ")");
				scheme_num.setText("：" + recordProgramsListBean.getNumberInfo().size() + "条");// 显示方案内容
			}
		} else if (LotteryId.getLotteryNum(lotteryId) == 2) {
			if (winnum == 0) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			} else if (winnum == 1) {
				if (recordProgramsListBean.getBonusToAccount().equals("0")) {
					resultView(buynum, "中奖未派");
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText("--");
				} else {
					resultView(buynum, "已派奖");
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText(recordProgramsListBean.getBonusAmount());
					if (recordProgramsListBean.getBuyType().equals("2")) {
						if(recordUserInfoBean.getLoginBonusAmount().equals("0.00")){
							buy_lottery_myreward_layout.setText("<0.01元");
						}else{	
							buy_lottery_myreward_layout.setText(recordUserInfoBean.getLoginBonusAmount());
						}
						buy_lottery_afterbonus_tx.setText(recordProgramsListBean.getCommissionAmount());
						backout_button.setVisibility(View.GONE);
					}
				}
			} else if (winnum == 2) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			}
			see_all.setVisibility(View.GONE);
			result_layout.setVisibility(View.GONE);
			isShowDan = LotteryResultUtils.checkIsShowDan(lotteryId);
			chuanfa_tv.setVisibility(View.GONE);
			if (isShowDan) {
				danma_title.setVisibility(View.VISIBLE);
			} else {
				danma_title.setVisibility(View.GONE);
			}

			// 方案内容是否显示
			buy_lottery_schemetype_tx.setText("方案内容(" + StatusUtil.getPrivacy(recordProgramsListBean.getPrivacy()) + ")");
			if (recordProgramsListBean.getShowNumber().equals("1")) {
				scheme_14or9.setVisibility(View.VISIBLE);
				LotteryResultUtils.getCZResultView(RecordBetCombineAgentActivity.this, scheme_14or9_layout, lotteryId, recordSubGameListBean, isShowDan, recordProgramsListBean.getPlayId());
			}
		} else if (LotteryId.getLotteryNum(lotteryId) == 3) {
			if (winnum == 0) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			} else if (winnum == 1) {
				if (recordProgramsListBean.getBonusToAccount().equals("0")) {
					resultView(buynum, "中奖未派");
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText("--");
				} else {
					resultView(buynum, "已派奖");
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText(recordProgramsListBean.getBonusAmount());
					if (recordProgramsListBean.getBuyType().equals("2")) {
						if(recordUserInfoBean.getLoginBonusAmount().equals("0.00")){
							buy_lottery_myreward_layout.setText("<0.01元");
						}else{	
							buy_lottery_myreward_layout.setText(recordUserInfoBean.getLoginBonusAmount());
						}
						buy_lottery_afterbonus_tx.setText(recordProgramsListBean.getCommissionAmount());
						backout_button.setVisibility(View.GONE);
					}
				}
			} else if (winnum == 2) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			}
			see_all.setVisibility(View.GONE);
			result_layout.setVisibility(View.GONE);
			scheme_14or9_layout_title.setVisibility(View.GONE);

			// 方案内容是否显示
			buy_lottery_schemetype_tx.setText("方案内容(" + StatusUtil.getPrivacy(recordProgramsListBean.getPrivacy()) + ")");
			if (recordProgramsListBean.getShowNumber().equals("0")) {
				chuanfa_tv.setVisibility(View.GONE);
			} else {
				scheme_14or9.setVisibility(View.VISIBLE);
				if (lotteryId.equals(LotteryId.CGJ)) {
					chuanfa_tv.setText("过    关：" + "单关");
				} else {
					chuanfa_tv.setText("过    关：" + recordMatchBean.getGuoGuan());
				}
				scheme_14or9_layout.removeAllViews();
				scheme_14or9_layout.addView(chuanfa_tv);
				scheme_14or9_layout.addView(JC_HHGG_utils.init(this).getView(recordMatchBean.getMatchList(),lotteryId));
//				LotteryResultUtils.getJCResultView(getApplicationContext(), scheme_14or9_layout, lotteryId, recordMatchBean.getMatchList(), recordProgramsListBean.getPlayId());
			}
		}
	}

	public void resultView(int num, String str) {
		if (num > 1) {
			lottery_result.setText("-");// 开奖结果
		} else {
			lottery_result.setText(str);// 开奖结果
		}
	}

	private class BtnOnClickListener implements View.OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buy_lottery_hide1_lin:
				if (hide1.isChecked()) {
					hide1.setChecked(false);
					expand1.setVisibility(View.GONE);
				} else {
					hide1.setChecked(true);
					expand1.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.buy_lottery_hide2_lin:
				if (hide2.isChecked()) {
					hide2.setChecked(false);
					expand2.setVisibility(View.GONE);
				} else {
					hide2.setChecked(true);
					expand2.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.buy_lottery_see_all:
				try {
					see_all.setClickable(false);
					showSchemeDetail();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.buy_lottery_agent_error_tv:
				if (userCode != null && !userCode.equals("")) {
					doRequestTask(false);
				} else {
					Toast.makeText(RecordBetCombineAgentActivity.this, "登录超时，请重新登录！", 0).show();
				}
				break;
			case R.id.buy_lottery_buy:
//				StatService.onEvent(RecordBetCombineAgentActivity.this, BaiduStatistics.Record, "方案详情—购彩", 1);
//				Intent intent = new Intent();
//				intent.setClass(RecordBetCombineAgentActivity.this, MainTabActivity.class);
//				intent.putExtra(Settings.TABHOST, 0);
//				RecordBetCombineAgentActivity.this.startActivity(intent);
//				finish();
				try {
					Intent mIntent = BuyLotteryActivity.getLotteryIntent(RecordBetCombineAgentActivity.this, lotteryId);
					if(mIntent != null){
						SafeApplication.dataMap.put("BuyLotteryActivity", BuyLotteryActivity.mContext);
						startActivity(mIntent);
					}else{
						Intent intent = new Intent(RecordBetCombineAgentActivity.this, MainTabActivity.class);
						intent.putExtra(Settings.TABHOST, 0);
						RecordBetCombineAgentActivity.this.startActivity(intent);
						Settings.closeOtherActivity(RecordBetCombineAgentActivity.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.backout_button:
				NormalAlertDialog dialog = new NormalAlertDialog(RecordBetCombineAgentActivity.this);
				dialog.setTitle("提示");
				dialog.setContent("您确定要撤单吗？如果认购加保底大于等于方案总金额的50%将无法撤单。");
				dialog.setOk_btn_text("确定");
				dialog.setCancle_btn_text("取消");
				dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
					@Override
					public void onOkBtnClick() {
						step_one = 2;
						doRequestTask(true);
					}

					@Override
					public void onCancleBtnClick() {
					}
				});
				dialog.show();
				break;
			case R.id.combine_people_layout:
				showDialog();
				break;
			}
		}
	}

	private void showDialog() {
		if (winnum == 1) {
			CombineBuyListDialog mDialog = new CombineBuyListDialog(this, userCode, recordProgramsListBean.getProgramsOrderId(), 1);
			mDialog.show();
		} else {
			CombineBuyListDialog mDialog = new CombineBuyListDialog(this, userCode, recordProgramsListBean.getProgramsOrderId(), 0);
			mDialog.show();
		}
	}

	private void showSchemeDetail() throws Exception {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popview_buy_lottery_scheme_all, null);
		TextView title = (TextView) view.findViewById(R.id.buy_lotter_scheme_detail_title);
		LinearLayout content_linearlayout = (LinearLayout) view.findViewById(R.id.buy_lotter_scheme_detail_content);
		Button sure = (Button) view.findViewById(R.id.buy_lotter_scheme_detail_sure);
		title.setText(LotteryId.getLotteryName(lotteryId) + recordProgramsListBean.getPlayName() + "方案");
		sure.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				see_all.setClickable(true);
				dialog.dismiss();
			}
		});
		LotteryResultUtils.getLotteryResultView(getApplicationContext(), content_linearlayout, lotteryId, recordProgramsListBean.getNumberInfo(), recordMainIssueListBean, true, false);
		dialog = new Dialog(this, R.style.dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}

	private void doRequestTask(boolean ifnew) {
		if(recordUserInfoBean == null) {
			progressbar.setVisibility(View.VISIBLE);
			scrollview_child.setVisibility(View.GONE);
		}
		final ProgressDialog progresdialog = new ProgressDialog(this);
		progresdialog.setMessage("正在处理中，请稍等...");
		progresdialog.setCancelable(true);
		if(ifnew){
			progresdialog.show();
		}
		if (step_one == 1) {
			SafelotteryHttpClient.post(this, "3306", "programsNew", initDate(step_one), new TypeMapHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Map mMap) {

					if (mMap != null) {
						String member = (String) mMap.get("member");
						if (member != null) {
							RecordUserInfoParser recordUserInfoParser = new RecordUserInfoParser();
							recordUserInfoBean = (RecordUserInfoBean) JsonUtils.parserJsonBean(member, recordUserInfoParser);
							LogUtil.DefalutLog(recordUserInfoBean.toString());
						}
						String programs = (String) mMap.get("programs");
						if (programs != null) {
							RecordProgramsListParser recordProgramsListParser = new RecordProgramsListParser();
							recordProgramsListBean = (RecordProgramsListBean) JsonUtils.parserJsonBean(programs, recordProgramsListParser);
							LogUtil.DefalutLog(recordProgramsListBean.toString());
						}
						String mainIssue = (String) mMap.get("mainIssue");
						if (mainIssue != null) {
							RecordMainIssueListParser recordMainIssueListParser = new RecordMainIssueListParser();
							recordMainIssueListBean = (RecordMainIssueListBean) JsonUtils.parserJsonBean(mainIssue, recordMainIssueListParser);
							LogUtil.DefalutLog(recordMainIssueListBean.toString());
						}
						String subGameList = (String) mMap.get("subGameList");
						if (subGameList != null) {
							RecordSubGameListParser recordSubGameListParser = new RecordSubGameListParser();
							recordSubGameListBean = (List<RecordSubGameListBean>) JsonUtils.parserJsonArray(subGameList, recordSubGameListParser);
							LogUtil.DefalutLog(recordSubGameListBean.toString());
						}
						String match = (String) mMap.get("match");
						if (match != null) {
							RecordMatchParser matchParser = new RecordMatchParser();
							recordMatchBean = (RecordMatchBean) JsonUtils.parserJsonBean(match, matchParser);
							LogUtil.DefalutLog(recordMatchBean.toString());
						}
						try {
							scrollview_child.setVisibility(View.VISIBLE);
							showData();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						LogUtil.CustomLog("TAG", "RecordBetCombineAgentActivity -- mMap is null");
						ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
						finish();
					}
				
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progressbar.setVisibility(View.GONE);
					scrollview.onRefreshComplete();
				}
			});
		} else if (step_one == 2) {
			SafelotteryHttpClient.post(this, "3304", "programs", initDate(step_one), new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					// TODO Auto-generated method stub
					Toast.makeText(RecordBetCombineAgentActivity.this, "撤单成功", Toast.LENGTH_SHORT).show();
					backout_button.setText("已撤单");
					backout_button.setClickable(false);
					scheme_state.setText("人工取消");// 方案状态
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progressbar.setVisibility(View.GONE);
					progresdialog.dismiss();
				}
			});
		}
	}

	public String initDate(int step) {
		Map<String, Object> map = null;
		if (step == 1) {
			map = new HashMap<String, Object>();
			map.put("userCode", userCode);
			map.put("programsOrderId", programsOrderId);
			str = JsonUtils.toJsonStr(map);
		} else if (step == 2) {
			map = new HashMap<String, Object>();
			map.put("userCode", userCode);
			map.put("programsOrderId", programsOrderId);
			str = JsonUtils.toJsonStr(map);
		}
		return str;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-onDestroy()");
	}

}
