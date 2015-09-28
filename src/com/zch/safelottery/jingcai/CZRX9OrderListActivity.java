package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.CZInfoBean;
import com.zch.safelottery.combine.CombineStartActivity;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.BeiTouZHDialog;
import com.zch.safelottery.dialogs.BeiTouZHDialog.BbeiTouZHDialogListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SplitUtil;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;

public class CZRX9OrderListActivity extends ZCHBaseActivity {

	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "CZRX9OrderListActivity";

	private TextView agreement_content, agreement_text, beitou_text, beitou_num, bet_sum, money_sum;
	private CheckBox agreement;
	private ListView lv;
	private View back, buy_together, submit;
	private ArrayList<CZInfoBean> result_list;
	private String selectedNum;
	private int betNum;
	private int totalMoney;
	private double remainMoney;
	private int alreadySelectNum;

	private Dialog dialog;
	private int multiple = 1;
	private ArrayList<Long> arrayList = new ArrayList<Long>();
	private String current_issue;
	private String lid;
	private String playMethod = "02";//14场和任9购买时彩种id都传300，通过玩法区分，14场为01，任9为2
	private int buyType = 1;//购买方式 	1 代购 2 合买 4 追号
	private BuyLotteryTask mBuyLotteryTask;
	private BeiTouZHDialog beiTouZHDialog;
	
	public static int FOR_REQUEST_COMBINE = 0X001;
	public static int FOR_RESULT_COMBINE = 0X002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.DefalutLog("CZRX9OrderListActivity-onCreate()");
		setContentView(R.layout.cz_rx9_orderlist_page);

		Intent intent = getIntent();
		String key = intent.getStringExtra("key");
		result_list = (ArrayList<CZInfoBean>)SafeApplication.dataMap.get(key);
		current_issue = intent.getStringExtra(BaseLotteryActivity.INTENT_ISSUE);
		lid = intent.getStringExtra(LotteryId.INTENT_LID);
		alreadySelectNum = intent.getIntExtra("alreadySelectNum", 0);
		betNum = intent.getIntExtra("betNum", 0);
		SafeApplication.dataMap.clear();
		
		if (alreadySelectNum == 9) {
			for (CZInfoBean bean : result_list) {
				bean.setSelectDan(false);
			}
			CZRX9ChoiceActivity.dantuo_count = 0;
		}
		initUI();
		setBetNumAndSum();
	}

	private void initUI() {
		LogUtil.DefalutLog("CZRX9OrderListActivity-initUI()");
		agreement_content = (TextView) findViewById(R.id.cz_14_orderlist_page_agreement_content);
		agreement_text = (TextView) findViewById(R.id.cz_14_orderlist_page_agreement_text);
		agreement = (CheckBox) findViewById(R.id.cz_14_orderlist_page_agreement);

		beitou_text = (TextView) findViewById(R.id.cz_14_orderlist_pag_beitou_text);
		beitou_num = (TextView) findViewById(R.id.cz_14_orderlist_pag_beitou_num);
		lv = (ListView) findViewById(R.id.cz_14_orderlist_page_list);
		bet_sum = (TextView) findViewById(R.id.cz_14_orderlist_pag_bet_num);
		money_sum = (TextView) findViewById(R.id.cz_14_orderlist_pag_money_sum);
		back = (View) findViewById(R.id.cz_14_orderlist_page_back);
		buy_together = (View) findViewById(R.id.cz_14_orderlist_page_buy_together);
		submit = (View) findViewById(R.id.cz_14_orderlist_page_submit);

		MyonClickListener myonClickListener = new MyonClickListener();
		back.setOnClickListener(myonClickListener);
		buy_together.setOnClickListener(myonClickListener);
		submit.setOnClickListener(myonClickListener);
		beitou_text.setOnClickListener(myonClickListener);
		beitou_num.setOnClickListener(myonClickListener);
		// agreement_text.setOnClickListener(myonClickListener);
		agreement_content.setOnClickListener(myonClickListener);

		MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this);
		lv.setAdapter(myListViewAdapter);
	}

	class MyListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyListViewAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return result_list.size();
		}

		public Object getItem(int position) {
			return result_list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.cz_rx9_orderlist_page_listview_item, null);
				holder.changci = (TextView) convertView.findViewById(R.id.changci);
				holder.duizhang = (TextView) convertView.findViewById(R.id.duizhang);
				holder.touzhu = (TextView) convertView.findViewById(R.id.touzhu);
				holder.danma = (CheckBox) convertView.findViewById(R.id.danma);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final CZInfoBean bean = result_list.get(position);
			arrayList.add(TimeUtils.getDateToTime(bean.getPlaying_time()));
			holder.changci.setText((++position) + "");
			holder.duizhang.setText(patchUpTeamName(bean.getHome_team(), bean.getGuest_team()));
			holder.touzhu.setText(selectResultForShow(bean));
			holder.danma.setChecked(bean.is_selectDan());
			if (bean.getSelect_num() == 0) {
				holder.danma.setVisibility(View.GONE);
			} else {
				holder.danma.setVisibility(View.VISIBLE);
			}
			holder.danma.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!holder.danma.isChecked()) {
						holder.danma.setChecked(false);
						bean.setSelectDan(false);
						CZRX9ChoiceActivity.dantuo_count--;
						countMoney();
					} else {
						if (alreadySelectNum == 9) {
							holder.danma.setChecked(false);
							display("您只选择了9场比赛，设胆无效");
						} else {
							if (CZRX9ChoiceActivity.dantuo_count < 8) {
								CZRX9ChoiceActivity.dantuo_count++;
								holder.danma.setChecked(true);
								bean.setSelectDan(true);
								countMoney();
							} else {
								display("胆拖场次不能超过8场");
								holder.danma.setChecked(false);
							}
						}
					}
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		TextView changci;
		TextView duizhang;
		TextView touzhu;
		CheckBox danma;
	}

	private void countMoney() {
		try {
			String number_no_dan = "";
			String dan = "";
			betNum = 0;
			if (alreadySelectNum > 9 && CZRX9ChoiceActivity.dantuo_count > 0) {
				for (CZInfoBean info : result_list) {
					if (info.getSelect_num() > 0 && info.is_selectDan()) {
						dan += selectResultForMoney(info);
						dan += ",";
					}
				}
				if (dan.length() > 0) {
					dan.substring(0, dan.length() - 1);
				}
				for (CZInfoBean info : result_list) {
					if (info.getSelect_num() > 0 && !info.is_selectDan()) {
						number_no_dan += selectResultForMoney(info);
						number_no_dan += ",";
					}
				}
			} else if (alreadySelectNum >= 9) {
				for (CZInfoBean info : result_list) {
					if (info.getSelect_num() > 0) {
						number_no_dan += selectResultForMoney(info);
						number_no_dan += ",";
					}
				}
			}
			if (number_no_dan.length() > 0) {
				number_no_dan = number_no_dan.substring(0, number_no_dan.length() - 1);
			}
			int temp_dan = 0;
			if (alreadySelectNum > 9) {
				temp_dan = CZRX9ChoiceActivity.dantuo_count;
			}
			SplitUtil splitUtil = new SplitUtil();
			splitUtil.setA(number_no_dan.split(","));
			splitUtil.setK(9 - temp_dan);
			splitUtil.select();
			List<String> list = splitUtil.getList();
			for (int i = 0; i < list.size(); i++) {
				String t = list.get(i).toString();
				if (temp_dan > 0) {
					t += "," + dan;
				}
				String[] result = t.split(",");
				int row = 1;
				for (int j = 0; j < result.length; j++) {
					row *= result[j].length();
				}
				betNum += row;
			}
			setBetNumAndSum();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String selectResultForMoney(CZInfoBean bean) {
		String num = "";
		if (bean.getWin() >= 0) {
			num = "3";
		}
		if (bean.getTied() >= 0) {
			num += "1";
		}
		if (bean.getLost() >= 0) {
			num += "0";
		}
		return num;
	}

	private String selectResultForShow(CZInfoBean bean) {
		String num = "";
		if (bean.getSelect_num() > 0) {
			int count = 0;
			if (bean.getWin() >= 0) {
				num = "3";
				count++;
			}
			if (bean.getTied() >= 0) {
				if (count > 0) {
					num += ",";
				}
				num += "1";
				count++;
			}
			if (bean.getLost() >= 0) {
				if (count > 0) {
					num += ",";
				}
				num += "0";
			}
		} else {
			num = "-";
		}

		return num;
	}

	private void pathcSelectNum() {
		selectedNum = "";
		StringBuilder danMaStr = new StringBuilder();
		StringBuilder tuoMaStr = new StringBuilder();
		if (CZRX9ChoiceActivity.dantuo_count > 0) {
			for (CZInfoBean bean : result_list) {
				String danMa = selectDanma(bean);
				String tuoMa = selectNumber(bean);
				danMaStr.append(danMa);
				danMaStr.append("*");
				tuoMaStr.append(tuoMa);
				tuoMaStr.append("*");
			}
			danMaStr.deleteCharAt(danMaStr.lastIndexOf("*"));
			tuoMaStr.deleteCharAt(tuoMaStr.lastIndexOf("*"));
			selectedNum = danMaStr.append("@").append(tuoMaStr).toString();
		}else{
			for (CZInfoBean bean : result_list) {
				String tuoMa = selectNumber(bean);
				tuoMaStr.append(tuoMa);
				tuoMaStr.append("*");
			}
			tuoMaStr.deleteCharAt(tuoMaStr.lastIndexOf("*"));
			selectedNum = tuoMaStr.toString();
		}
	}
	
	private String selectNumber(CZInfoBean bean) {
		String danMa = "";
		if (bean.getSelect_num() > 0) {
			if (!bean.is_selectDan()) {
				if (bean.getWin() >= 0) {
					danMa += "3";
				}
				if (bean.getTied() >= 0) {
					danMa += "1";
				}
				if (bean.getLost() >= 0) {
					danMa += "0";
				}
			} else {
				danMa = "4";//4代表没有选的场次
			}
		} else {
			danMa = "4";//4代表没有选的场次
		}
		return danMa;
	}

	private String selectDanma(CZInfoBean bean) {
		String danMa = "";
		if (bean.getSelect_num() > 0) {
			if (bean.is_selectDan()) {
				if (bean.getWin() >= 0) {
					danMa += "3";
				}
				if (bean.getTied() >= 0) {
					danMa += "1";
				}
				if (bean.getLost() >= 0) {
					danMa += "0";
				}
			} else {
				danMa = "4";//4代表没有选的场次
			}
		} else {
			danMa = "4";//4代表没有选的场次
		}
		return danMa;
	}

	private String patchUpTeamName(String home, String guest) {
		return home + "vs" + guest;
	}

	private void setBetNumAndSum() {
		bet_sum.setText(betNum + "注,");
		money_sum.setText("共" + (betNum*multiple*2) + "元");
	}

	private class MyonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cz_14_orderlist_page_back:
				onBackPressed();
				break;
			case R.id.cz_14_orderlist_page_buy_together:
					totalMoney = getTotalMoney();
					if (betNum * 2 > 20000) {
						display("单次投注金额不能超过2万元");
					} else {
						if (agreement.isChecked()) {
							Collections.sort(arrayList);
							Bundle mBundle = new Bundle();
							mBundle.putString("endtime",TimeUtils.formatLongTimeForCustom(arrayList.get(0),TimeUtils.MonthMinuteFormat));
							Intent intent1 = new Intent(CZRX9OrderListActivity.this, CombineStartActivity.class);
							intent1.putExtra(Settings.BUNDLE, mBundle);
							startActivityForResult(intent1, FOR_REQUEST_COMBINE);
							SafeApplication.dataMap.put("BetLotteryBean",getBetLotteryBean());
						} else {
							display("请确认已同意《用户服务协议》");
						}
					}
				break;
			case R.id.cz_14_orderlist_page_submit:
				try {
					if(!DoubleClickUtil.isFastDoubleClick()){
						submit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				StatService.onEvent(CZRX9OrderListActivity.this, BaiduStatistics.buyNow, "立即投注（任选9）", 1);
				break;
			case R.id.cz_14_orderlist_pag_beitou_text:
				showBeiTouDialog();
				break;
			case R.id.cz_14_orderlist_pag_beitou_num:
				showBeiTouDialog();
				break;
			case R.id.cz_14_orderlist_page_agreement_text:
				agreement.setChecked(!agreement.isChecked());
				break;
			case R.id.cz_14_orderlist_page_agreement_content:
				Intent intent = new Intent(CZRX9OrderListActivity.this, HelpDetailActivity.class);
				intent.putExtra("kind", 14);
				startActivity(intent);
				break;
			}
		}
	}
	
	private void showBeiTouDialog(){
		if (beiTouZHDialog == null) {
			beiTouZHDialog = new BeiTouZHDialog(this,multiple,BeiTouZHDialog.Max,BeiTouZHDialog.INIT_BEITOU);
			beiTouZHDialog.listener = new BbeiTouZHDialogListener() {
				@Override
				public void onOkBtnClick(View v, int value, boolean isStopPursue) {
					multiple = value;
					beitou_num.setText(multiple + "倍");
					setBetNumAndSum();
					
				}
			};
			beiTouZHDialog.show();
		} else {
			beiTouZHDialog.setBeiTouZH(multiple, BeiTouZHDialog.Max);
			beiTouZHDialog.show();
		}
	}

//	public List<CTicket> tickets() {
//		String pollcode;
//		if (CZRX9ChoiceActivity.dantuo_count > 0) {
//			// selectedNum += "03";
//			pollcode = "03";
//		} else {
//			if (betNum > 1) {
//				// selectedNum += "02";
//				pollcode = "02";
//			} else {
//				// selectedNum += "01";
//				pollcode = "01";
//			}
//		}
//		List<CTicket> cTickets = new ArrayList<CTicket>();
//		CTicket cTicket = new CTicket();
//		cTicket.setSequence(1);
//		cTicket.setNumber(selectedNum);
//		cTicket.setPollCode("01");
//		cTicket.setItem(betNum);
//		cTicket.setPlayCode(pollcode);
//		cTickets.add(cTicket);
//		return cTickets;
//	}

	private void submit() throws Exception {
		if(GetString.isLogin){
			remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
			totalMoney = getTotalMoney();
			if(betNum * 2 > 20000){
				display("单次投注金额不能超过2万元");
			}else{
				if(remainMoney >= totalMoney){
					if(agreement.isChecked()){
						buyLottery();
					}else{
						display("请确认已同意《用户服务协议》");
					}
				}else{
					PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(this);
					dialog.setMoney(totalMoney-remainMoney);
					dialog.show();
				}
			}
		}else{
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
		}
	}

	private String getBetJson() {
		BetLotteryBean mLotteryBean = getBetLotteryBean();
		return JsonUtils.toJsonStr(mLotteryBean);
	}

	public BetLotteryBean getBetLotteryBean() {
		BetLotteryBean mLotteryBean = new BetLotteryBean();
		if(GetString.userInfo != null){
			mLotteryBean.setUserCode(GetString.userInfo.getUserCode());
		}
		mLotteryBean.setAppVersion(SystemInfo.softVerCode);
			mLotteryBean.setLotteryId(LotteryId.SFC);
			mLotteryBean.setPlayId(playMethod);
			mLotteryBean.setBuyType(buyType);
			mLotteryBean.setBuyAmount(totalMoney);
			mLotteryBean.setTotalAmount(totalMoney);
			List<BetNumberBean> mBetNumberBeanList = new ArrayList<BetNumberBean>();
			mBetNumberBeanList.add(getBetNumberBean());
			mLotteryBean.setBuyNumberArray(mBetNumberBeanList);
			List<BetIssueBean> issueArray = new ArrayList<BetIssueBean>();
			issueArray.add(getBetIssueBean());
			mLotteryBean.setIssueArray(issueArray);
		
		return mLotteryBean;
	}
	
	private BetNumberBean getBetNumberBean(){
		pathcSelectNum();
		String pollId = "";
		BetNumberBean mBean = new BetNumberBean();
		mBean.setPlayId(playMethod);
		if (CZRX9ChoiceActivity.dantuo_count > 0) {
			pollId = "03";
		}else{
			pollId = betNum > 1 ? "02":"01";
		}
		mBean.setPollId( pollId );
		mBean.setItem(betNum);
		mBean.setAmount(totalMoney);
		mBean.setBuyNumber(selectedNum);
		return mBean;
	}

	private BetIssueBean getBetIssueBean(){
		BetIssueBean bean = new BetIssueBean();
		bean.setIssue(current_issue);
		bean.setMultiple(multiple);
		return bean;
	}
	
	public void buyLottery() throws Exception{
		submit.setEnabled(false);
		mBuyLotteryTask = new BuyLotteryTask(this,"", getBetJson(),submit);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				String remark = "";
				final SucceedDialog dialog = new SucceedDialog(CZRX9OrderListActivity.this, lid, current_issue, 
						betNum, SucceedDialog.PURSUE_NULL, multiple, totalMoney, (remainMoney - totalMoney), remark);
				 dialog.setCancelable(false);
				dialog.setOnDialogClickListener(new OnDialogClickListener() {
					@Override
					public void onPositiveButtonClick() {
						Settings.closeOtherActivity(CZRX9OrderListActivity.this);
						dialog.dismiss();
						finish();
//						new FenxiangUtil(CZRX9OrderListActivity.this, LotteryId.getLotteryName(lid),1);
					}

					@Override
					public void onNegativeButtonClick() {
						dialog.dismiss();
						CZRX9OrderListActivity.this.setResult(RESULT_OK);
						finish();
					}
				});
				dialog.show();
			}

		});
		mBuyLotteryTask.send();
	}

	private int getTotalMoney() {
		return betNum * multiple * 2;
	}

	private void display(String content) {
		Toast.makeText(CZRX9OrderListActivity.this, content, 0).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-onDestroy()");
	}
	/*
	 * 合买页面返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == FOR_REQUEST_COMBINE){
			if(resultCode == FOR_RESULT_COMBINE){
				CZRX9OrderListActivity.this.setResult(RESULT_OK);
				onBackPressed();
			}
		}
	}
}
