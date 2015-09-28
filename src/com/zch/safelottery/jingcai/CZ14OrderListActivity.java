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
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;

public class CZ14OrderListActivity extends ZCHBaseActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "CZ14OrderListActivity";
	
	private TextView agreement_content,agreement_text,beitou_text,beitou_num,bet_sum,money_sum;
	private CheckBox agreement;
	private ListView lv;
	private View back,buy_together,submit;
	private ArrayList<CZInfoBean> result_list;
	private String selectedNum;
	private int betNum;
	private int totalMoney;
	private double remainMoney;
	
	private Dialog dialog;
	private int multiple = 1;
	private ArrayList<Long> arrayList = new ArrayList<Long>();
	private String current_issue;
	private String lid;
	private String playMethod = "01";//14场和任9购买时彩种id都传300，通过玩法区分，14场为01，任9为2
	private int buyType = 1;//购买方式 	1 代购 2 合买 4 追号
	private BuyLotteryTask mBuyLotteryTask;
	private BeiTouZHDialog beiTouZHDialog;
	
	public static int FOR_REQUEST_COMBINE = 0X001;
	public static int FOR_RESULT_COMBINE = 0X002;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.DefalutLog("CZ14OrderListActivity-onCreate()");
		setContentView(R.layout.cz_14_orderlist_page);
		
		Intent intent = getIntent();
		String key = intent.getStringExtra("key");
		result_list = (ArrayList<CZInfoBean>)SafeApplication.dataMap.get(key);
		current_issue = intent.getStringExtra(BaseLotteryActivity.INTENT_ISSUE);
		lid = intent.getStringExtra(LotteryId.INTENT_LID);
		SafeApplication.dataMap.clear();
		betNum = intent.getIntExtra("betNum", 0);
		
		initUI();
		setBetNumAndSum();
	}

	private void initUI(){
		LogUtil.DefalutLog("CZ14OrderListActivity-initUI()");
		agreement_content = (TextView)findViewById(R.id.cz_14_orderlist_page_agreement_content);
		agreement_text = (TextView)findViewById(R.id.cz_14_orderlist_page_agreement_text);
		agreement = (CheckBox)findViewById(R.id.cz_14_orderlist_page_agreement);
		
		beitou_text = (TextView)findViewById(R.id.cz_14_orderlist_pag_beitou_text);
		beitou_num = (TextView)findViewById(R.id.cz_14_orderlist_pag_beitou_num);
		lv = (ListView)findViewById(R.id.cz_14_orderlist_page_list);
		bet_sum = (TextView)findViewById(R.id.cz_14_orderlist_pag_bet_num);
		money_sum = (TextView)findViewById(R.id.cz_14_orderlist_pag_money_sum);
		back = (View)findViewById(R.id.cz_14_orderlist_page_back);
		buy_together = (View)findViewById(R.id.cz_14_orderlist_page_buy_together);
		submit = (View)findViewById(R.id.cz_14_orderlist_page_submit);
		
		MyonClickListener myonClickListener = new MyonClickListener();
		back.setOnClickListener(myonClickListener);
		buy_together.setOnClickListener(myonClickListener);
		submit.setOnClickListener(myonClickListener);
		beitou_text.setOnClickListener(myonClickListener);
		beitou_num.setOnClickListener(myonClickListener);
//		agreement_text.setOnClickListener(myonClickListener);
		agreement_content.setOnClickListener(myonClickListener);
		
		MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this);
		lv.setAdapter(myListViewAdapter);
	}
	
	class MyListViewAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		
		public MyListViewAdapter (Context context) {
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
				convertView = mInflater.inflate(R.layout.cz_14_orderlist_page_listview_item, null);
				holder.changci = (TextView)convertView.findViewById(R.id.changci);
				holder.duizhang = (TextView)convertView.findViewById(R.id.duizhang);
				holder.touzhu = (TextView)convertView.findViewById(R.id.touzhu);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			CZInfoBean bean = result_list.get(position);
			arrayList.add(TimeUtils.getDateToTime(bean.getPlaying_time()));
			holder.changci.setText( (++position)+"" );
			holder.duizhang.setText( patchUpTeamName(bean.getHome_team(), bean.getGuest_team()) );
			holder.touzhu.setText( selectResult(bean, false) );
			return convertView;
		}
	}
	
	static class ViewHolder {
		TextView changci;
		TextView duizhang;
		TextView touzhu;
    }
	
	/**投注号码格式拼接
	 * @param bean
	 * @param isBet 是否是投注使用，投注无需逗号分隔，界面显示则有逗号
	 * @return
	 */
	private String selectResult(CZInfoBean bean, boolean isBet){
		String num = "";
		int count = 0;
		if(bean.getWin() >= 0){
			num = "3";
			count++;
		}
		if(bean.getTied() >= 0){
			if(count > 0 && !isBet){
				num += ",";
			}
			num += "1";
			count++;
		}
		if(bean.getLost() >= 0){
			if(count > 0 && !isBet){
				num += ",";
			}
			num += "0";
		}
		return num;
	}
	
	private void pathcSelectNum(){
		selectedNum = "";
		for(CZInfoBean bean : result_list){
			String num = selectResult(bean, true);
			selectedNum += num;
			selectedNum += "*";
		}
		int len = selectedNum.lastIndexOf("*");
		selectedNum = selectedNum.substring(0, len);
	}
	
	private String patchUpTeamName(String home,String guest){
		return home + " vs " + guest;
	}
	
	private void setBetNumAndSum(){
		bet_sum.setText( betNum+"注,");
		money_sum.setText( "共"+(betNum*multiple*2)+"元");
	}
	
	private class MyonClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.cz_14_orderlist_page_back:
					onBackPressed();
					break;
				case R.id.cz_14_orderlist_page_buy_together:
						totalMoney = getTotalMoney();
						if(betNum * 2 > 20000){
							display("单次投注金额不能超过2万元");
						}else{
							if(agreement.isChecked()){
								Collections.sort(arrayList);
								Bundle mBundle = new Bundle();
								mBundle.putString("endtime",TimeUtils.formatLongTimeForCustom(arrayList.get(0),TimeUtils.MonthMinuteFormat));
								Intent intent1 = new Intent(CZ14OrderListActivity.this, CombineStartActivity.class);
								intent1.putExtra(Settings.BUNDLE, mBundle);
								startActivityForResult(intent1, FOR_REQUEST_COMBINE);
								SafeApplication.dataMap.put("BetLotteryBean",getBetLotteryBean());
							}else{
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
//					StatService.onEvent(CZ14OrderListActivity.this, BaiduStatistics.buyNow, "立即投注（传统14场）", 1);
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
					Intent intent = new Intent(CZ14OrderListActivity.this,HelpDetailActivity.class);
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
//		List<CTicket> cTickets = new ArrayList<CTicket>();
//		CTicket cTicket = new CTicket();
//		cTicket.setSequence(1);
//		cTicket.setNumber(selectedNum);
//		cTicket.setPollCode("01");
//		cTicket.setItem(betNum);
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
					PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(CZ14OrderListActivity.this);
					dialog.setMoney(totalMoney-remainMoney);
					dialog.show();
				}
			}
		}else{
			Intent intent = new Intent(CZ14OrderListActivity.this,LoginActivity.class);
			startActivity(intent);
		}
	}
	
	private String getBetJson() {
		// issue = "13008";
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
		BetNumberBean mBean = new BetNumberBean();
		mBean.setPlayId(playMethod);
		mBean.setPollId( betNum > 1 ? "02":"01");
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
//		if (mBuyLotteryTask != null) {
//			AsyncTask.Status aStatus = mBuyLotteryTask.getStatus();
//			if (aStatus != AsyncTask.Status.FINISHED) {
//				ToastUtil.diaplayMesShort(getApplicationContext(), "正在提交上一次的请求，请稍后再试！");
//				return;
//			}
//		}
		submit.setEnabled(false);
		mBuyLotteryTask = new BuyLotteryTask(this,"", getBetJson(),submit);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				String remark = "";
				final SucceedDialog dialog = new SucceedDialog(CZ14OrderListActivity.this, lid, current_issue, 
						betNum, SucceedDialog.PURSUE_NULL, multiple, totalMoney, (remainMoney - totalMoney), remark);
				 dialog.setCancelable(false);
				dialog.setOnDialogClickListener(new OnDialogClickListener() {
					@Override
					public void onPositiveButtonClick() {
						Settings.closeOtherActivitySetCurTabOne(CZ14OrderListActivity.this);
						dialog.dismiss();
						finish();
//						new FenxiangUtil(CZ14OrderListActivity.this, LotteryId.getLotteryName(lid),1);
					}

					@Override
					public void onNegativeButtonClick() {
						dialog.dismiss();
						CZ14OrderListActivity.this.setResult(RESULT_OK);
						finish();
					}
				});
				dialog.show();
			}
		});
		mBuyLotteryTask.send();
	}
	
	private int getTotalMoney(){
		return betNum*multiple*2;
	}
	
	private void display(String content){
		Toast.makeText(CZ14OrderListActivity.this, content, 0).show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
	}
	/*
	 * 合买页面返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == FOR_REQUEST_COMBINE){
			if(resultCode == FOR_RESULT_COMBINE){
				CZ14OrderListActivity.this.setResult(RESULT_OK);
				onBackPressed();
			}
		}
	}
}
