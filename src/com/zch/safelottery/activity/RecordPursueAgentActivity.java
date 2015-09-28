package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordBetNumberBean;
import com.zch.safelottery.bean.RecordPursueBean;
import com.zch.safelottery.bean.RecordPursueListBean;
import com.zch.safelottery.bean.RecordUserInfoBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.RecordBetNumberParser;
import com.zch.safelottery.parser.RecordPursueListParser;
import com.zch.safelottery.parser.RecordPursueParser;
import com.zch.safelottery.parser.RecordUserInfoParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.HttpUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.LotteryResultUtils;
import com.zch.safelottery.util.StatusUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;
import com.zch.safelottery.util.XmlUtil;

public class RecordPursueAgentActivity extends ZCHBaseActivity {
	
	private final boolean DEBUG = Settings.DEBUG;
	private final String TAG = "BuyLotteryAgentActivity";
	
	/**撤消返回**/
	private String returnValue;
	
	private Dialog dialog;
	private LinearLayout scheme_linear,expand1,expand2, expand3,current_period_num_linear,hide1_lin,hide2_lin;
	
	private LinearLayout lotteryResultLinear;
	
	private TextView title;
	private TextView initiatorNameText,nitiatorNameTextPrizeSumText,initiatorNameTextPrizeNumText;
	private TextView schemeSumText,playMothedText,pourNumText,multipleText,buyMothedText,winningStop, scheme_id,buyTimeText;
	private TextView pursueStateName, pursueStateText,schemePrizeSumText;
	
	private TextView schemeContentSumText,see_all;
	private TextView current_period_result, current_period_result_all;
	private Button btn_buy_lottery;
	private CheckBox hide1, hide2;
	private String lotteryId;
	private String autoOrderId, userCode;
	private String programsOrderId;
//	private String schemeContent; //方案内容(号)
//						系统时间  彩种  追号方案  玩法中文描述  玩法编码  投注时间  发起人  战绩  方案总金额  注数  
//	private String lotteryId,playDescribe,playCoding,buyTime,initiatorName,gains,schemeSum,pour;
//						倍数  追号状态  方案中奖总金额  发起人中奖总金额  起起人中奖总次数  方案内容(号)  追期总期数  扩展
//	private String mulriple,pursueState,schemePrizeSum,initiatorNameTextPrizeSum,initiatorNameTextPrizeNum,schemeContent,pursueSum,extend;
    
	private RecordUserInfoBean mUserBean; //用户信息
    private RecordPursueBean mAutoBean; //追号订单
    private ArrayList<RecordPursueListBean> mProList; //追号列表 开奖的
    private ArrayList<RecordBetNumberBean> mBetList; //方案内容 投注的
    
    private ProgressBar progressbar_big;
    private TextView prompt;
    private PullToRefreshScrollView scrollview;
    private LinearLayout scrollview_child;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_lottery_agent);
		Intent intent = getIntent();
		autoOrderId = intent.getStringExtra("autoOrderId");
		
		try{
			userCode = GetString.userInfo.getUserCode();
			
			initUI();
			if(userCode != null && !userCode.equals("")){
				requsetDataHttp();
			}else{
				Toast.makeText(RecordPursueAgentActivity.this, "登录超时，请重新登录！", 0).show();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initUI(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initUI()");
		progressbar_big = (ProgressBar)findViewById(R.id.buy_lottery_agent_progressbar_big);
		prompt = (TextView) findViewById(R.id.buy_lottery_agent_error_tv);
		scrollview = (PullToRefreshScrollView)findViewById(R.id.scrollview);
		scrollview_child = (LinearLayout) findViewById(R.id.scrollview_child);
		
		Button again = (Button) findViewById(R.id.buy_again_button);
		again.setVisibility(View.GONE); //没有再买一遍
		
		scheme_linear = (LinearLayout)findViewById(R.id.buy_lottery_agent_scheme_linear);
		expand3 = (LinearLayout)findViewById(R.id.buy_lottery_expand_three);
		
		hide1_lin = (LinearLayout)findViewById(R.id.buy_lottery_hide1_lin);
		hide2_lin = (LinearLayout)findViewById(R.id.buy_lottery_hide2_lin);
		hide1_lin.setOnClickListener(new BtnOnClickListener());
		hide2_lin.setOnClickListener(new BtnOnClickListener());
		
		btn_buy_lottery = (Button)findViewById(R.id.buy_lottery_buy);
		title = (TextView) findViewById(R.id.tv_title);
		initiatorNameText = (TextView)findViewById(R.id.buy_lottery_initiator);
		hide1 = (CheckBox)findViewById(R.id.buy_lottery_hide1);
		expand1 = (LinearLayout)findViewById(R.id.buy_lottery_expand_one);
		nitiatorNameTextPrizeSumText = (TextView)findViewById(R.id.buy_lottery_reward);
		initiatorNameTextPrizeNumText = (TextView)findViewById(R.id.buy_lottery_prize_times);
		pourNumText = (TextView)findViewById(R.id.buy_lottery_number);
		multipleText = (TextView)findViewById(R.id.buy_lottery_multiple);
		
		hide2 = (CheckBox)findViewById(R.id.buy_lottery_hide2);
		expand2 = (LinearLayout)findViewById(R.id.buy_lottery_expand_two);
		scheme_id = (TextView)findViewById(R.id.buy_lottery_scheme_id);
		
		playMothedText = (TextView)findViewById(R.id.buy_lottery_play_mothed);
		buyMothedText = (TextView)findViewById(R.id.buy_lottery_buy_mothed);
		winningStop = (TextView) findViewById(R.id.buy_lottery_winning_stop);
		LinearLayout winningStopLay = (LinearLayout) findViewById(R.id.buy_lottery_winning_stop_lay);
		buyTimeText = (TextView)findViewById(R.id.buy_lottery_bet_time);
		pursueStateName = (TextView) findViewById(R.id.buy_lottery_scheme_state_name);
		pursueStateText = (TextView)findViewById(R.id.buy_lottery_scheme_state);
		lotteryResultLinear = (LinearLayout)findViewById(R.id.buy_lottery_result_linear);
		schemePrizeSumText = (TextView)findViewById(R.id.buy_lottery_reward_at_sum);
		schemeSumText = (TextView)findViewById(R.id.buy_lottery_scheme_money);
		
		schemeContentSumText = (TextView)findViewById(R.id.buy_lottery_scheme_num);
		see_all = (TextView)findViewById(R.id.buy_lottery_see_all);
		current_period_result = (TextView)findViewById(R.id.buy_lottery_current_period_result);
		current_period_result_all = (TextView) findViewById(R.id.buy_lottery_result_num_all);
		current_period_num_linear = (LinearLayout)findViewById(R.id.buy_lottery_current_period_num_linear);
//		no_result = (TextView) findViewById(R.id.buy_lottery_current_period_no_result);
		
		winningStopLay.setVisibility(View.VISIBLE);
		current_period_result_all.setVisibility(View.VISIBLE);
		BtnOnClickListener MyBtnOnClickListener = new BtnOnClickListener();
		see_all.setOnClickListener(MyBtnOnClickListener);
		btn_buy_lottery.setOnClickListener(MyBtnOnClickListener);
		current_period_result_all.setOnClickListener(MyBtnOnClickListener);
		
		mProList = new ArrayList<RecordPursueListBean>();
		
		dialog = new Dialog(this, R.style.dialog);
		
		scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(userCode != null && !userCode.equals("")){
					requsetDataHttp();
				}else{
					scrollview.onRefreshComplete();
					Toast.makeText(RecordPursueAgentActivity.this, "登录超时，请重新登录！", 0).show();
				}
			}
		});
	}
	
	
	private void showData() throws Exception{
		if(mAutoBean == null) {
			HttpUtil.checkNetwork(getApplicationContext());
			return;
		}
		
			if(returnValue!=null && !returnValue.equals("0000")){
				scheme_linear.removeAllViews();
//			scheme_linear.addView(child);
//			LotteryResultUtils.getLotteryResultView(getApplicationContext(), scheme_linear, lotteryId, schemeContent, res, false, playCoding);
				currentPeriodLinear();
				return;
			}
			
			lotteryId = mAutoBean.getLotteryId();
			String multiple = "";
			String buyMothed = "";
			if(mProList != null && mProList.size() > 0){
				String s0 = mProList.get(0).getMultiple();
				String s1 = mProList.get(mProList.size() - 1).getMultiple();
				if(!s0.equals(s1)){
					multiple = s0 + "-" + s1;
					buyMothed = "盈利追号";
				}else{
					multiple = mAutoBean.getMultiple();
					buyMothed = "追号";
				}
			}
			
			title.setText(LotteryId.getLotteryName(lotteryId)+"-追号详情");
			
			initiatorNameText.setText(mUserBean.getUserName()); //发起人
			nitiatorNameTextPrizeSumText.setText(mUserBean.getBonusTotal()); //中奖金额
			initiatorNameTextPrizeNumText.setText(mUserBean.getBonusCount()); //中奖次数
			
			pourNumText.setText(mAutoBean.getItem());
			multipleText.setText(multiple);
			scheme_id.setText( StatusUtil.getSchemeShortId(autoOrderId) );
			buyTimeText.setText( mAutoBean.getCreateTime());
			playMothedText.setText(mAutoBean.getPlayName());
			buyMothedText.setText(buyMothed);
			winningStop.setText( StatusUtil.getWin(mAutoBean.getWinStop(), mAutoBean.getWinAmount()));
			pursueStateName.setText("追号状态：");
			pursueStateText.setText( getOrderStatus(mAutoBean));
			lotteryResultLinear.setVisibility(View.GONE);
			schemeSumText.setText(mAutoBean.getOrderAmount());
			
			if(TextUtils.isEmpty(mAutoBean.getBonusAmount()) || mAutoBean.getBonusAmount().equals("0.00")){
				expand3.setVisibility(View.GONE);
			}else{
				schemePrizeSumText.setText(mAutoBean.getBonusAmount() + "元");
			}
			
			schemeContentSumText.setText( "：" + mBetList.size() + "条" );
			
			pursueSum = mProList.size(); //列表总个数
			
			current_period_result.setText("追号列表："+ pursueSum +"条");
			
			scheme_linear.removeAllViews();
			for(int i = 0, size = mBetList.size(); i < size; i++){
				if(i > 0) scheme_linear.addView(XmlUtil.getDashed(getApplicationContext()));
				scheme_linear.addView(LotteryResultUtils.getLotteryResultUnkonwViewSingle(getApplicationContext(), lotteryId, mBetList.get(i).getNumber(), mBetList.get(i).getPlayCode(),mBetList.get(i).getPollCode()));
//				scheme_linear.addView(ViewUtil.getAutoView(getApplicationContext(), lotteryId, mAutoBean.getPlayId(), mBetList.get(i).getNumber(), true, false));
				if(i >= 4) break;
			}
			currentPeriodLinear();
		
	}
	
	private int pursueSum;
	/**追号列表显示**/
	private void currentPeriodLinear() throws Exception{
		current_period_num_linear.removeAllViews();
		for (int i = 0; i < pursueSum; i++) {
			if (i == 5) {
				break;
			}
			current_period_num_linear.addView(correntPeriodView(mProList.get(i)));
		}
	}
	/**
	 * 追号列表View
	 * **/
	private View correntPeriodView(final RecordPursueListBean mListBean){
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		final View correntView = inflater.inflate(R.layout.pursue_period_num_result_item, null);
		
		LinearLayout pursue_period_no_num = (LinearLayout) correntView.findViewById(R.id.pursue_period_no_num);
		LinearLayout pursue_period_yes_num = (LinearLayout) correntView.findViewById(R.id.pursue_period_yes_num);
		LinearLayout pursue_period_linear = (LinearLayout) correntView.findViewById(R.id.pursue_period_linear);//显示开奖号码
		
		final TextView issue = (TextView) correntView.findViewById(R.id.pursue_period_issue);
		final TextView state = (TextView) correntView.findViewById(R.id.pursue_period_state);
		final Button retract = (Button) correntView.findViewById(R.id.pursue_period_retract);
		final TextView prize = (TextView) correntView.findViewById(R.id.pursue_period_prize);
		
		pursue_period_yes_num.setVisibility(View.GONE);
		pursue_period_no_num.setVisibility(View.VISIBLE);
		
		issue.setText("开奖号码("+mListBean.getIssue()+"期) " + mListBean.getMultiple() + "倍");
		if (mListBean.getOrderStatus() == 4) {
			state.setText("系统撤消");
		} else if (mListBean.getOrderStatus() == 6) {
			state.setText("用户撤消");
		} else if (!TextUtils.isEmpty(mListBean.getBonusNumber()) && !mListBean.getBonusNumber().equals("null") && !mListBean.getBonusNumber().equals("-")) {
			pursue_period_yes_num.setVisibility(View.VISIBLE);
			pursue_period_no_num.setVisibility(View.GONE);
			if(mListBean.getBonusToAccount().equals("1")){
				prize.setText("￥" + mListBean.getBonusAmount());
			}

			try {
				pursue_period_linear.addView(ViewUtil.getAutoView(getApplicationContext(), lotteryId, mListBean.getBonusNumber(), true, 1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return correntView;
		} else {
			state.setText(StatusUtil.getIssueState(mListBean.getIssueStatus()));
			if (mListBean.getIssueStatus() == 0) {
				if (mListBean.getOrderStatus() == -1 || mListBean.getOrderStatus() == 0) {
					if (mListBean.getSendStatus() == 0)
						retract.setVisibility(View.VISIBLE);
				}
			}
		}
		// 点击撤消事件
		retract.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NormalAlertDialog dialog = new NormalAlertDialog(
						RecordPursueAgentActivity.this);
				dialog.setContent("您确定要撤消当前的追号吗？");
				dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
					@Override
					public void onOkBtnClick() {
						programsOrderId = mListBean.getProgramsOrderId();
						retract.setEnabled(false);

						requsetDataHttp(state, retract, mListBean);
					}

					@Override
					public void onCancleBtnClick() {
					}
				});
				dialog.show();
			}
		});
		return correntView;
	}
	
//	private String getPursueState(String state){
//		if(state.equals("0")) return "进行中";
//		if(state.equals("1")) return "已完成";
//		return "";
//	}
	private String getOrderStatus(RecordPursueBean mBean){
		if(mBean.getOrderStatus().equals("1")){
			return "已完成";
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append("在追(");
			sb.append(Integer.parseInt(mBean.getSuccessIssue()) + Integer.parseInt(mBean.getFailureIssue()) + Integer.parseInt(mBean.getCancelIssue()));
			sb.append("/");
			sb.append(mBean.getTotalIssue());
			sb.append(")");
			return sb.toString();
		}
	}
	
	private void requsetDataHttp(final TextView state, final Button retract, final RecordPursueListBean mListBean){
		prompt.setVisibility(View.GONE);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userCode", userCode);
		map.put("autoOrderId", autoOrderId);
		map.put("programsOrderId", programsOrderId);
		String msg = JsonUtils.toJsonStr(map);
			final ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在执行……");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			SafelotteryHttpClient.post(this, "3304", "autoOrder", msg, new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if(mResult != null){
						if(mResult.getCode().equals("0000")){
							mListBean.setOrderStatus(6);
							state.setText("用户撤消");
							retract.setVisibility(View.GONE);
						}else{
							ToastUtil.diaplayMesShort(getApplicationContext(), mResult.getMsg());
						}
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progressDialog.dismiss();
					retract.setEnabled(true);
				}
			});
	}
	
	private void requsetDataHttp(){
		prompt.setVisibility(View.GONE);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userCode", userCode);
		map.put("autoOrderId", autoOrderId);
		map.put("programsOrderId", programsOrderId);
		String msg = JsonUtils.toJsonStr(map);
		btn_buy_lottery.setVisibility(View.GONE);
//		progressbar.setVisibility(View.VISIBLE);
		if(mAutoBean == null){
			progressbar_big.setVisibility(View.VISIBLE);
			scrollview_child.setVisibility(View.GONE);
		}
		
		SafelotteryHttpClient.post(this, "3306", "autoOrder", msg, new TypeMapHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Map mMap) {
					try{
						mUserBean = (RecordUserInfoBean) JsonUtils.parserJsonBean((String) mMap.get("member"), new RecordUserInfoParser());
						mAutoBean = (RecordPursueBean) JsonUtils.parserJsonBean((String) mMap.get("autoOrder"), new RecordPursueParser());
						mProList = (ArrayList<RecordPursueListBean>) JsonUtils.parserJsonArray((String) mMap.get("programsList"), new RecordPursueListParser());
						mBetList = (ArrayList<RecordBetNumberBean>) JsonUtils.parserJsonArray(mAutoBean.getNumberInfo(), new RecordBetNumberParser());
						scrollview_child.setVisibility(View.VISIBLE);
						showData();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
				}
				
				@Override
				public void onFinish(){
					progressbar_big.setVisibility(View.GONE);
					btn_buy_lottery.setVisibility(View.VISIBLE);
					scrollview.onRefreshComplete();
				}
			});
	}
	
	private class BtnOnClickListener implements View.OnClickListener{

		public void onClick(View v) {
			switch(v.getId()){
				case R.id.buy_lottery_hide1_lin:
					if(hide1.isChecked()){
						hide1.setChecked(false);
						expand1.setVisibility(View.GONE);
					}else{
						hide1.setChecked(true);
						expand1.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.buy_lottery_hide2_lin:
					if(hide2.isChecked()){
						hide2.setChecked(false);
						expand2.setVisibility(View.GONE);
					}else{
						hide2.setChecked(true);
						expand2.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.buy_lottery_see_all:
					if(!dialog.isShowing()) showSchemeDetail();
					break;
				case R.id.buy_lottery_result_num_all:
//					progressDialog.show();
					if(!dialog.isShowing()) showPursueDetail();
					break;
				case R.id.buy_lottery_buy:
					Intent intent = new Intent();
	            	intent.setClass(RecordPursueAgentActivity.this, MainTabActivity.class);
	            	intent.putExtra(Settings.TABHOST, 0);
	            	RecordPursueAgentActivity.this.startActivity(intent);
	            	finish();
					break;
			}
		}
	}
	private void showSchemeDetail() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popview_buy_lottery_scheme_all,null);
		TextView title = (TextView) view.findViewById(R.id.buy_lotter_scheme_detail_title);
		LinearLayout content_linearlayout = (LinearLayout) view	.findViewById(R.id.buy_lotter_scheme_detail_content);
		Button sure = (Button) view.findViewById(R.id.buy_lotter_scheme_detail_sure);
		title.setText(LotteryId.getLotteryName(lotteryId) + "方案");
		sure.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		try {
			for(int i = 0, size = mBetList.size(); i < size; i++){
				if(i > 0) content_linearlayout.addView(XmlUtil.getDashed(getApplicationContext()));
				content_linearlayout.addView(LotteryResultUtils.getLotteryResultUnkonwViewSingle(getApplicationContext(), lotteryId, mBetList.get(i).getNumber(), mBetList.get(i).getPlayCode(),mBetList.get(i).getPollCode()));
//				content_linearlayout.addView(ViewUtil.getAutoView(getApplicationContext(), lotteryId, mAutoBean.getPlayId(), mBetList.get(i).getNumber(), true, false));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			dialog.setContentView(view);
			dialog.show();
	}
	
	private void showPursueDetail() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popview_pursue_scheme_all, null);
		TextView title = (TextView) view.findViewById(R.id.buy_lotter_scheme_detail_title);
		LinearLayout content_linearlayout = (LinearLayout) view	.findViewById(R.id.buy_lotter_scheme_detail_content);
		Button sure = (Button) view.findViewById(R.id.buy_lotter_scheme_detail_sure);
		title.setText(LotteryId.getLotteryName(lotteryId) + "列表");
		sure.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					currentPeriodLinear();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
//		if (schemeContent != null && !schemeContent.equals("")) {
			for (int i = 0; i < pursueSum; i++) {
				content_linearlayout.addView(correntPeriodView(mProList.get(i)));
			}
			dialog.setContentView(view);
			dialog.show();
			dialog.setOnKeyListener(new OnKeyListener() {
				
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK ){
						try {
							currentPeriodLinear();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dialog.dismiss();
					}
					return false;
				}
			});
//		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
	}

}
