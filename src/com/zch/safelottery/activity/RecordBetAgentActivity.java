package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.GetIssueAsyncTask;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.RecordMainIssueListBean;
import com.zch.safelottery.bean.RecordMatchBean;
import com.zch.safelottery.bean.RecordNumberInforBean;
import com.zch.safelottery.bean.RecordProgramsListBean;
import com.zch.safelottery.bean.RecordSubGameListBean;
import com.zch.safelottery.bean.RecordUserInfoBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.CaiDanDialog;
import com.zch.safelottery.dialogs.CaiDanDialog.OnCaiDanDialogClickListener;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.ShareDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.jingcai.JZCGJActivity;
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
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.JC_HHGG_utils;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.LotteryResultUtils;
import com.zch.safelottery.util.StatusUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.share.BaseShare;
import com.zch.safelottery.util.share.Login_WeiboUtil;
import com.zch.safelottery.util.share.QQShareUtil;
import com.zch.safelottery.util.share.ShareUtil;
import com.zch.safelottery.util.share.WeiXinShare;

public class RecordBetAgentActivity extends ZCHBaseActivity {

	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "BuyLotteryAgentActivity";

	private ProgressBar progressbar;
	private LinearLayout scheme_linear, expand1, expand2, expand3, current_period_num_linear, hide1_lin, hide2_lin;
	private LinearLayout fanganneirong_layout, reward_result_layout, expand_send;
	private TextView title;
	private TextView initiator, scores, person_reward_sum, prize_times, bet_num, buy_mothed, scheme_state, reward_at_sum, content_title, see_all;
	private TextView multiple, scheme_id, play_mothed, bet_time, lottery_result, scheme_num;
	private TextView current_period_result, no_result, scheme_money, person_send_title, sendto, result_title;
	private Button btn_buy_lottery;
	private CheckBox hide1, hide2;
	private ImageView rewords_top_line;

	private String lotteryId;

	private View scheme_14or9, result_layout;
	private TextView danma_title, chuanfa_tv, error_tv;
	private LinearLayout scheme_14or9_layout, scheme_14or9_layout_title;
	private int isCZ;// 1为普通数字彩，2为14场任选9,3为北单竞彩
	private boolean isShowDan;
	private PullToRefreshScrollView scrollview;
	private LinearLayout scrollview_child;
	
	private String str;
	private Button buyAgainButton;

	double userMoney;
	double amount;
	double money;
	private int fenxiangnum = 0;
	private String programsOrderId;
	private RecordUserInfoBean recordUserInfoBean;
	private RecordProgramsListBean recordProgramsListBean;
	private RecordMainIssueListBean recordMainIssueListBean;
	private List<RecordSubGameListBean> recordSubGameListBean;
	private RecordMatchBean recordMatchBean;
	private String userCode;
	private String buytype;
	private String issue;
	private int sendnum = 1;// 1：赠送人 2 ：受赠人 3：自己送自己
	private int winnum = 0;// 0：未开奖 1 ：已中奖 2：未中奖
	private TextView tvShare;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			if (DEBUG) Log.d(Settings.TAG, TAG + "-onCreate()");
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.buy_lottery_agent);
			Intent intent = getIntent();
			programsOrderId = intent.getStringExtra("programsOrderId");
			userCode = GetString.userInfo.getUserCode();
			initUI();
			if (!TextUtils.isEmpty(programsOrderId)) {
				doRequestTask(1);
			} else {
				Toast.makeText(RecordBetAgentActivity.this, "方案号有误,请刷新重试！", 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-initUI()");
		title = (TextView) findViewById(R.id.tv_title);
		tvShare = (TextView) findViewById(R.id.tv_share);
		tvShare.setVisibility(View.VISIBLE);
		progressbar = (ProgressBar) findViewById(R.id.buy_lottery_agent_progressbar_big);
		scrollview = (PullToRefreshScrollView) findViewById(R.id.scrollview);
		scrollview_child = (LinearLayout) findViewById(R.id.scrollview_child);

		scheme_linear = (LinearLayout) findViewById(R.id.buy_lottery_agent_scheme_linear);
		expand3 = (LinearLayout) findViewById(R.id.buy_lottery_expand_three);
		expand_send = (LinearLayout) findViewById(R.id.buy_lottery_expand_send);

		fanganneirong_layout = (LinearLayout) findViewById(R.id.fanganneirong_layout);
		reward_result_layout = (LinearLayout) findViewById(R.id.buy_lottery_expand_rewords);
		rewords_top_line = (ImageView) findViewById(R.id.buy_lottery_expand_rewords_top_line);

		hide1_lin = (LinearLayout) findViewById(R.id.buy_lottery_hide1_lin);
		hide2_lin = (LinearLayout) findViewById(R.id.buy_lottery_hide2_lin);
		hide1_lin.setOnClickListener(new BtnOnClickListener());
		hide2_lin.setOnClickListener(new BtnOnClickListener());

		btn_buy_lottery = (Button) findViewById(R.id.buy_lottery_buy);
//		title = (RelativeLayout) findViewById(R.id.buy_lottery_title);
		initiator = (TextView) findViewById(R.id.buy_lottery_initiator);
		// scores = (TextView)findViewById(R.id.buy_lottery_scores);
		hide1 = (CheckBox) findViewById(R.id.buy_lottery_hide1);
		expand1 = (LinearLayout) findViewById(R.id.buy_lottery_expand_one);
		person_reward_sum = (TextView) findViewById(R.id.buy_lottery_reward);
		prize_times = (TextView) findViewById(R.id.buy_lottery_prize_times);
		bet_num = (TextView) findViewById(R.id.buy_lottery_number);
		multiple = (TextView) findViewById(R.id.buy_lottery_multiple);

		person_send_title = (TextView) findViewById(R.id.buy_lottery_person_send_title);
		sendto = (TextView) findViewById(R.id.buy_lottery_sendto);
		result_title = (TextView) findViewById(R.id.buy_lottery_result_title);
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
		content_title = (TextView) findViewById(R.id.buy_lottery_content_title);
		see_all = (TextView) findViewById(R.id.buy_lottery_see_all);
		current_period_result = (TextView) findViewById(R.id.buy_lottery_current_period_result);
		current_period_num_linear = (LinearLayout) findViewById(R.id.buy_lottery_current_period_num_linear);
		no_result = (TextView) findViewById(R.id.buy_lottery_current_period_no_result);

		result_layout = (View) findViewById(R.id.result_linearlayout);
		scheme_14or9 = (View) findViewById(R.id.buy_lottery_agent_scheme_14or9);
		danma_title = (TextView) findViewById(R.id.danma_title);
		scheme_14or9_layout = (LinearLayout) findViewById(R.id.cz_14or9_viewpage_list);
		scheme_14or9_layout_title = (LinearLayout) findViewById(R.id.buy_lottery_agent_scheme_14or9_title);
		chuanfa_tv = (TextView) findViewById(R.id.chuanfa_tv);

		buyAgainButton = (Button) findViewById(R.id.buy_again_button);

		BtnOnClickListener MyBtnOnClickListener = new BtnOnClickListener();
		see_all.setOnClickListener(MyBtnOnClickListener);
		tvShare.setOnClickListener(new View.OnClickListener() {
			private ShareDialog mDialog;
			@Override
			public void onClick(View v) {
				if (mDialog==null || !mDialog.isShowing()) {
					final String targetUrl = String.format(GetString.BET_RECORD_SHARE_SERVER, programsOrderId,userCode);
					mDialog = new ShareDialog(RecordBetAgentActivity.this, BaseShare.ShareType.URL,
							new ShareUtil.CallBackExt() {
						@Override
						public void onClickSina(Login_WeiboUtil sina) {
							String title = "我用@中彩票V 客户端买了一单彩票。感觉这次要中大奖，分享出来大家一起沾沾好运，快来围观O(∩_∩)O~";
							sina.share(RecordBetAgentActivity.this, targetUrl, title, "", R.drawable.record_detail_sina_share_attach, "");
						}
						
						@Override
						public void onClickQQ(QQShareUtil qq) {
							onClickQzone(qq);
						}
						
						@Override
						public void onClickQzone(QQShareUtil qq) {
							String title = "豪车豪宅，拼了！";
							String desc = "房子车子都看好了，就差彩票开大奖！";
							Bitmap bm = ImageUtil.getBitmap(RecordBetAgentActivity.this,  R.drawable.icon, "");
							String path = ImageUtil.saveBitmap(RecordBetAgentActivity.this, bm,"share.png");
							qq.shareURL(title, desc, targetUrl, path);
						}
						
						@Override
						public void onClickWeixinFriend(WeiXinShare weixin) {
							String title = "豪车豪宅，拼了！";
							String desc = "房子车子都看好了，就差彩票开大奖！你看这次能中吗？";
							weixin.share(BaseShare.ShareType.URL, targetUrl, R.drawable.icon, "", title, desc, false);
						}
						
						@Override
						public void onClickWeixinTimeline(WeiXinShare weixin) {
							String title = "我押上全部身家买了一单彩票，你看能中吗？";
							weixin.share(BaseShare.ShareType.URL, targetUrl, R.drawable.icon, "", title, "", true);
						}
						
						@Override
						public void onAfterClickShare() {
							mDialog.dismiss();
						}
					});
					mDialog.setCanceledOnTouchOutside(true);
					mDialog.show();
				}
			}
		});
		btn_buy_lottery.setOnClickListener(MyBtnOnClickListener);
		error_tv.setOnClickListener(MyBtnOnClickListener);
		buyAgainButton.setOnClickListener(MyBtnOnClickListener);
		
		scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (userCode != null && !userCode.equals("")) {
					doRequestTask(1);
				} else {
					scrollview.onRefreshComplete();
					Toast.makeText(RecordBetAgentActivity.this, "登录超时，请重新登录！", 0).show();
				}
			}
		});
	}

//	private void addTitle(){
//		titleViews = new TitleViews(this, "方案详情");
////		titleViews.setBtnName("全部");
//		title.addView(titleViews.getView());
////		titleViews.setBtnIcon(R.drawable.btn_share_bg_d);
////		titleViews.setOnClickListener(new OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
//////				StatService.onEvent(RecordBetAgentActivity.this, BaiduStatistics.Record, "个人中心—分享", 1);
////				new FenxiangUtil(RecordBetAgentActivity.this, LotteryId.getLotteryName(lotteryId), fenxiangnum);
////			}
////		});
//	}
	
	private void showData() throws Exception {
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-showData()");
		lotteryId = recordProgramsListBean.getLotteryId();

		if (!LotteryId.isValidLottery(lotteryId)) {
			buyAgainButton.setVisibility(View.VISIBLE);
		}

		initiator.setText(recordUserInfoBean.getUserName());
		person_reward_sum.setText(recordUserInfoBean.getBonusTotal());
		prize_times.setText(recordUserInfoBean.getBonusCount());

		scheme_money.setText(recordProgramsListBean.getTotalWere() + "元");

		play_mothed.setText(recordProgramsListBean.getPlayName());// 玩法

		bet_num.setText(recordProgramsListBean.getItem());// 注数
		multiple.setText(recordProgramsListBean.getMultiple());// 倍投

		/*
		 * 购买方式与赠送
		 */
		buytype = StatusUtil.getBuyType(recordProgramsListBean.getBuyType());
		buy_mothed.setText(buytype);// 购买方式显示

		if ("7".equals(recordProgramsListBean.getBuyType())) {

			buyAgainButton.setVisibility(View.GONE);

			sendOrPresentee();

			if (sendnum == 1) {
				person_send_title.setText("送给：");
				initiator.setText("受赠人");
				sendto.setText(recordUserInfoBean.getPresentMobile());
				fanganneirong_layout.setVisibility(View.GONE);
				result_layout.setVisibility(View.GONE);
				result_title.setVisibility(View.GONE);
				lottery_result.setVisibility(View.GONE);
				expand3.setVisibility(View.GONE);
				reward_result_layout.setVisibility(View.GONE);
				rewords_top_line.setVisibility(View.GONE);
			} else if (sendnum == 2) {
				initiator.setText(recordUserInfoBean.getUserName());
				person_send_title.setText("赠送人：");
				sendto.setText(recordUserInfoBean.getPresentedUserName());
			} else if (sendnum == 3) {
				initiator.setText(recordUserInfoBean.getUserName());
				person_send_title.setText("赠送人：");
				sendto.setText(recordUserInfoBean.getPresentedUserName());
			}
		}

		String ProgramsOrderId = StatusUtil.getSchemeShortId(recordProgramsListBean.getProgramsOrderId());
		scheme_id.setText(ProgramsOrderId);// 方案id
		bet_time.setText(recordProgramsListBean.getCreateTime());// 投注时间

		String orderStatus = StatusUtil.getOrderStatus(recordProgramsListBean.getOrderStatus());
		scheme_state.setText(orderStatus);// 方案状态

		if (lotteryId.equals(LotteryId.SFC)) {
			if (recordProgramsListBean.getPlayId().equals("02")) {
				lotteryId = LotteryId.RX9;
			}
		}
		/** 1为普通数字彩，2为14场任选9,3为北单竞彩 **/
		title.setText(LotteryId.getLotteryName(lotteryId) + "-方案详情");
//		titleViews.setTitleName(LotteryId.getLotteryName(lotteryId) + "-方案详情");

		winnum = ConversionUtil.StringToInt(recordProgramsListBean.getBonusStatus());
		String bonusStatusStr = StatusUtil.getBonusStatus(winnum); // 需要中奖状态，来判断是否需要隐藏中奖税后金额
		int buynum = ConversionUtil.StringToInt(recordProgramsListBean.getOrderStatus());// 获取方案状态判断是否投注失败
		isCZ = LotteryId.getLotteryNum(lotteryId);
		if (isCZ == 1) {
			if (winnum == 0) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
				fenxiangnum = 1;
				current_period_result.setText("预计开奖时间(" + recordProgramsListBean.getIssue() + "期）");
				no_result.setText(recordMainIssueListBean.getBonusTime());
			} else if(winnum==1) {
				if (recordProgramsListBean.getBonusToAccount().equals("0")) {
					resultView(buynum, "中奖未派");
					fenxiangnum = 1;
					reward_at_sum.setText("--");
				} else {
					resultView(buynum, "已派奖");
					fenxiangnum = 2;
					if(recordProgramsListBean.getBonusAmount().equals("0.00")){
						reward_at_sum.setText("<0.01元");
					}else{
					   reward_at_sum.setText(recordProgramsListBean.getBonusAmount());
					}
				}
				LotteryResultUtils.getLotteryResultView(getApplicationContext(), current_period_num_linear, recordProgramsListBean.getLotteryId(), recordProgramsListBean.getNumberInfo(),
						recordMainIssueListBean, false, true);
				expand3.setVisibility(View.VISIBLE);
			}else if(winnum==2){
				resultView(buynum, bonusStatusStr);
				fenxiangnum = 1;
				LotteryResultUtils.getLotteryResultView(getApplicationContext(), current_period_num_linear, recordProgramsListBean.getLotteryId(), recordProgramsListBean.getNumberInfo(),
						recordMainIssueListBean, false, true);
				expand3.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(recordProgramsListBean.getBigNumberInfo()) && recordProgramsListBean.getBigNumberInfo().equals("1") && recordProgramsListBean.getNumberInfo().size() == 0){
				see_all.setVisibility(View.GONE);
				if(recordProgramsListBean.getIsUpload().equals("2")){
					content_title.setText("方案未上传");
				}else{
					content_title.setText("方案内容过大，请到网站查看");
				}
			}else{
				see_all.setVisibility(View.VISIBLE);
				LotteryResultUtils.getLotteryResultView(getApplicationContext(), scheme_linear, lotteryId, recordProgramsListBean.getNumberInfo(), recordMainIssueListBean, false, false);
				scheme_num.setText("：" + recordProgramsListBean.getNumberInfo().size() + "条");// 显示方案内容
			}
		} else if (isCZ == 2) {

			if (winnum==0) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			} else if(winnum==1) {
				if (recordProgramsListBean.getBonusToAccount().equals("0")) {
					resultView(buynum, "中奖未派");
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText("--");
				} else {
					resultView(buynum, "已派奖");
					expand3.setVisibility(View.VISIBLE);
					if(recordProgramsListBean.getBonusAmount().equals("0.00")){
						reward_at_sum.setText("<0.01元");
					}else{
					   reward_at_sum.setText(recordProgramsListBean.getBonusAmount());
					}
				}
			}else if(winnum==2){
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			}
			see_all.setVisibility(View.GONE);
			scheme_14or9.setVisibility(View.VISIBLE);
			result_layout.setVisibility(View.GONE);
			isShowDan = LotteryResultUtils.checkIsShowDan(lotteryId);
			chuanfa_tv.setVisibility(View.GONE);
			if (isShowDan) {
				danma_title.setVisibility(View.VISIBLE);
			} else {
				danma_title.setVisibility(View.GONE);
			}
			LotteryResultUtils.getCZResultView(RecordBetAgentActivity.this, scheme_14or9_layout, lotteryId, recordSubGameListBean, isShowDan, recordProgramsListBean.getPlayId());
		} else if (isCZ == 3) {
			if (winnum==0) {
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			} else if(winnum==1) {
				if (recordProgramsListBean.getBonusToAccount().equals("0")) {
					resultView(buynum, "中奖未派");
					expand3.setVisibility(View.VISIBLE);
					reward_at_sum.setText("--");
				}else{
					resultView(buynum, "已派奖");
					expand3.setVisibility(View.VISIBLE);
					if(recordProgramsListBean.getBonusAmount().equals("0.00")){
						reward_at_sum.setText("<0.01元");
					}else{
					   reward_at_sum.setText(recordProgramsListBean.getBonusAmount());
					}
				}
			}else if(winnum==2){
				resultView(buynum, bonusStatusStr);
				expand3.setVisibility(View.GONE);
			}
			see_all.setVisibility(View.GONE);
			scheme_14or9.setVisibility(View.VISIBLE);
			result_layout.setVisibility(View.GONE);
			scheme_14or9_layout_title.setVisibility(View.GONE);
			if (lotteryId.equals(LotteryId.CGJ)) {
				chuanfa_tv.setText("过    关：" + "单关");
			} else {
				chuanfa_tv.setText("过    关：" + recordMatchBean.getGuoGuan());
			}
			
			scheme_14or9_layout.removeAllViews();
			scheme_14or9_layout.addView(chuanfa_tv);
			scheme_14or9_layout.addView(JC_HHGG_utils.init(this).getView(recordMatchBean.getMatchList(),lotteryId));
//			LotteryResultUtils.getJCResultView(getApplicationContext(), scheme_14or9_layout, lotteryId, recordMatchBean.getMatchList(), recordProgramsListBean.getPlayId());
		}
	}

	public void resultView(int num, String str) {
		if (num >1) {
			lottery_result.setText("-");// 开奖结果
		} else {
			lottery_result.setText(str);// 开奖结果
		}
	}

	public void sendOrPresentee() {
		if (userCode.equals(recordProgramsListBean.getPresentedUserCode()) && GetString.userInfo.getMobile().equals(recordUserInfoBean.getPresentMobile())) {
			sendnum = 3;
		} else if (userCode.equals(recordProgramsListBean.getPresentedUserCode())) {
			sendnum = 1;
		} else if (!userCode.equals(recordProgramsListBean.getPresentedUserCode())) {
			sendnum = 2;
		}
	}

	private class BtnOnClickListener implements View.OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buy_lottery_hide1_lin:
				if (hide1.isChecked()) {
					hide1.setChecked(false);
					expand1.setVisibility(View.GONE);
					if ("赠送".equals(buytype)) {
						expand_send.setVisibility(View.GONE);
					}
				} else {
					hide1.setChecked(true);
					expand1.setVisibility(View.VISIBLE);
					if ("赠送".equals(buytype)) {
						expand_send.setVisibility(View.VISIBLE);
					}
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
					getDidlog();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.buy_lottery_agent_error_tv:
				if (userCode != null && !userCode.equals("")) {
					doRequestTask(1);
				} else {
					Toast.makeText(RecordBetAgentActivity.this, "登录超时，请重新登录！", 0).show();
				}
				break;
			case R.id.buy_lottery_buy:
//				StatService.onEvent(RecordBetAgentActivity.this, BaiduStatistics.Record, "方案详情—购彩", 1);
//				Intent intent = new Intent();
//				intent.setClass(RecordBetAgentActivity.this, MainTabActivity.class);
//				intent.putExtra(Settings.TABHOST, 0);
//				RecordBetAgentActivity.this.startActivity(intent);
//				finish();
				try {
					if(lotteryId.equals(LotteryId.CGJ)){
						Intent intent2 = new Intent(RecordBetAgentActivity.this, JZCGJActivity.class);
						RecordBetAgentActivity.this.startActivity(intent2);
					}else{
						Intent mIntent = BuyLotteryActivity.getLotteryIntent(RecordBetAgentActivity.this, lotteryId);
						if(mIntent != null){
							SafeApplication.dataMap.put("BuyLotteryActivity", BuyLotteryActivity.mContext);
							startActivity(mIntent);
						}else{
							Intent intent = new Intent(RecordBetAgentActivity.this, MainTabActivity.class);
							intent.putExtra(Settings.TABHOST, 0);
							RecordBetAgentActivity.this.startActivity(intent);
							Settings.closeOtherActivity(RecordBetAgentActivity.this);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.buy_again_button:
				userMoney = Double.valueOf(GetString.userInfo.getUseAmount());
				amount = Double.parseDouble(recordUserInfoBean.getSponsorOrderAmount());
				if (userMoney >= amount) {
					if(TextUtils.isEmpty(LotteryId.getIssue(lotteryId))){
						GetIssueAsyncTask asyncTask=new GetIssueAsyncTask(RecordBetAgentActivity.this, lotteryId);
						asyncTask.setOnAsyncTaskListener(new GetIssueAsyncTask.OnAsyncTaskListener() {
							@Override
							public void onTaskPostExecuteListener(String issueOld) {
								issue = issueOld;
								buyAgainDialog();
							}
						});
						asyncTask.send();
					}else{
						issue = LotteryId.getIssue(lotteryId);
						buyAgainDialog();
					}
				} else {
					 PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(RecordBetAgentActivity.this);
					 dialog.setMoney(amount - userMoney);
					 dialog.setNegative("取消");
					 dialog.show();
				}
				break;
			}
		}

	}

	private void buyAgainDialog(){
		if(!TextUtils.isEmpty(issue)){
			String content = "期次：" + issue + "期\n金额：￥" + amount;
			NormalAlertDialog dialog = new NormalAlertDialog(RecordBetAgentActivity.this, "确认购买当前方案", content);
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
				@Override
				public void onOkBtnClick() {
					doRequestTask(2);
				}
				
				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		}else{
			ToastUtil.diaplayMesLong(getApplicationContext(), "获取期次失败，请重试");
		}
	}
	
	private void getDidlog() throws Exception{
		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(showSchemeDetail(dialog));
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private View showSchemeDetail(final Dialog dialog) throws Exception {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popview_buy_lottery_scheme_all, null);
		TextView title = (TextView) view.findViewById(R.id.buy_lotter_scheme_detail_title);
		LinearLayout content_linearlayout = (LinearLayout) view.findViewById(R.id.buy_lotter_scheme_detail_content);
		Button sure = (Button) view.findViewById(R.id.buy_lotter_scheme_detail_sure);
		title.setText(LotteryId.getLotteryName(lotteryId) + recordProgramsListBean.getPlayName() + "方案");
		LotteryResultUtils.getLotteryResultView(getApplicationContext(), content_linearlayout, lotteryId, recordProgramsListBean.getNumberInfo(), recordMainIssueListBean,true,false);
		sure.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				see_all.setClickable(true);
			}
		});
		return view;
	}

	/**
	 * @param step_one  // 1.方案详情 2.再买一遍
	 */
	private void doRequestTask(int step_one) {
		if(recordUserInfoBean == null) {
			progressbar.setVisibility(View.VISIBLE);
			scrollview_child.setVisibility(View.GONE);
		}
		
		if (step_one == 1) {
			SafelotteryHttpClient.post(this, "3306", "programsNew", initDate(step_one), new TypeMapHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Map mMap) {
					try {
						String member = (String) mMap.get("member");
						if (!TextUtils.isEmpty(member)) {
							RecordUserInfoParser recordUserInfoParser = new RecordUserInfoParser();
							recordUserInfoBean = (RecordUserInfoBean) JsonUtils.parserJsonBean(member, recordUserInfoParser);
							LogUtil.DefalutLog(recordUserInfoBean.toString());
						}
						String programs = (String) mMap.get("programs");
						if (!TextUtils.isEmpty(programs)) {
							RecordProgramsListParser recordProgramsListParser = new RecordProgramsListParser();
							recordProgramsListBean = (RecordProgramsListBean) JsonUtils.parserJsonBean(programs, recordProgramsListParser);
							LogUtil.DefalutLog(recordProgramsListBean.toString());
						}
						String mainIssue = (String) mMap.get("mainIssue");
						if (!TextUtils.isEmpty(mainIssue)) {
							RecordMainIssueListParser recordMainIssueListParser = new RecordMainIssueListParser();
							recordMainIssueListBean = (RecordMainIssueListBean) JsonUtils.parserJsonBean(mainIssue, recordMainIssueListParser);
							LogUtil.DefalutLog(recordMainIssueListBean.toString());
						}
						String subGameList = (String) mMap.get("subGameList");
						if (!TextUtils.isEmpty(subGameList)) {
							RecordSubGameListParser recordSubGameListParser = new RecordSubGameListParser();
							recordSubGameListBean = (List<RecordSubGameListBean>) JsonUtils.parserJsonArray(subGameList, recordSubGameListParser);
							LogUtil.DefalutLog(recordSubGameListBean.toString());
						}
						String match = (String) mMap.get("match");
						if (!TextUtils.isEmpty(match)) {
							RecordMatchParser matchParser = new RecordMatchParser();
							recordMatchBean = (RecordMatchBean) JsonUtils.parserJsonBean(match, matchParser);
							LogUtil.DefalutLog(recordMatchBean.toString());
						}
						scrollview_child.setVisibility(View.VISIBLE);
						showData();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
				}
				
				@Override
				public void onFinish(){
					progressbar.setVisibility(View.GONE);
					scrollview.onRefreshComplete();
				}
			});
		}else if(step_one == 2){
			StatService.onEvent(RecordBetAgentActivity.this, "total-orderagain", "所有继续投本号请求", 1);
			SafelotteryHttpClient.post(this, "3300", "", initDate(step_one), new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					try{
						String resultStr = mResult.getResult();
						if(!TextUtils.isEmpty(resultStr)){
							JSONObject json = new JSONObject(resultStr);
							if(json.has("eggAmount")){
								String eggAmount = json.getString("eggAmount");
								String caidanContent = "";
								if(json.has("eggStoryNew")){
									caidanContent = json.getString("eggStoryNew");
								}
								if(!TextUtils.isEmpty(caidanContent)){
									CaiDanDialog mCaiDanDialog = new CaiDanDialog(RecordBetAgentActivity.this, eggAmount, caidanContent);
									mCaiDanDialog.setCanceledOnTouchOutside(true);
									mCaiDanDialog.setOnCaiDanDialogClickListener(new OnCaiDanDialogClickListener() {
										@Override
										public void onClick() {
											successDialog();
										}
									});
									mCaiDanDialog.show();
									return;
								}
							}
						}
						successDialog();
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progressbar.setVisibility(View.GONE);
					scrollview.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	private void successDialog(){
		String money = String.valueOf(GetString.df.format(userMoney - amount));
		final SucceedDialog dialog = new SucceedDialog(RecordBetAgentActivity.this, lotteryId, LotteryId.getIssue(lotteryId), ConversionUtil.StringToInt(recordProgramsListBean
				.getItem()), 0, ConversionUtil.StringToInt(recordProgramsListBean.getMultiple()), amount, ConversionUtil.StringToDouble(money), "-", "完成");
		dialog.setOnDialogClickListener(new OnDialogClickListener() {
			@Override
			public void onPositiveButtonClick() {
//				new FenxiangUtil(RecordBetAgentActivity.this, LotteryId.getLotteryName(lotteryId), 1);
				Settings.closeOtherActivitySetCurTabOne(RecordBetAgentActivity.this);
				dialog.dismiss();
				finish();
			}

			@Override
			public void onNegativeButtonClick() {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public String initDate(int step) {
		Map<String, Object> map = null;
		if (step == 1) {
			map = new HashMap<String, Object>();
			map.put("userCode", userCode);
			map.put("programsOrderId", programsOrderId);
			str = JsonUtils.toJsonStr(map);
		} else if (step == 2) {
			str = getBetJson();
		}
		return str;
	}

	private String getBetJson() {
		BetLotteryBean mLotteryBean = new BetLotteryBean();
		if (GetString.userInfo != null) {
			mLotteryBean.setAppVersion(SystemInfo.softVerCode);
			mLotteryBean.setUserCode(GetString.userInfo.getUserCode());
			mLotteryBean.setLotteryId(lotteryId);
			mLotteryBean.setPlayId(recordProgramsListBean.getPlayId());
			mLotteryBean.setBuyType(1);

			mLotteryBean.setBuyAmount((long) amount);
			mLotteryBean.setTotalAmount((long) amount);

			List<BetNumberBean> mBetNumberBeanList = new ArrayList<BetNumberBean>();
			for (RecordNumberInforBean bean : recordProgramsListBean.getNumberInfo()) {
				mBetNumberBeanList.add(getBetNumberBean(bean));
			}
			mLotteryBean.setBuyNumberArray(mBetNumberBeanList);
			List<BetIssueBean> issueArray = new ArrayList<BetIssueBean>();
			issueArray.add(getBetIssueBean());
			mLotteryBean.setIssueArray(issueArray);
		}
		return JsonUtils.toJsonStr(mLotteryBean);
	}

	private BetNumberBean getBetNumberBean(RecordNumberInforBean bean) {
		BetNumberBean mBean = new BetNumberBean();
		mBean.setPlayId(bean.getPlayCode());
		mBean.setPollId(bean.getPollCode());
		mBean.setItem(ConversionUtil.StringToLong(bean.getItem()));
		mBean.setAmount((long) amount);
		mBean.setBuyNumber(bean.getNumber());
		return mBean;
	}

	private BetIssueBean getBetIssueBean() {
		BetIssueBean bean = new BetIssueBean();
		bean.setIssue(issue);
		bean.setMultiple(ConversionUtil.StringToInt(recordProgramsListBean.getMultiple()));
		return bean;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-onDestroy()");
	}

}
