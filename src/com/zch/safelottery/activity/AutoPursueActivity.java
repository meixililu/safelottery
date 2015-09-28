package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.bean.AutoPursueBean;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.fragment.AutoPursueResultFragment;
import com.zch.safelottery.fragment.AutoPursueSetFragment;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.impl.AutoPursueListener;
import com.zch.safelottery.parser.IssueInfoParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.view.TitleViews;

public class AutoPursueActivity extends FragmentActivity implements AutoPursueListener{
	
	public static String isHasDefaultSettingKey = "isHasDefaultSetting";
	public static String DefaultIssueNum = "DefaultIssueNum";
	public static String DefaultMultipleNum = "DefaultMultipleNum";
	private RelativeLayout title;
	private LinearLayout pursue_content;
	private BuyLotteryTask mBuyLotteryTask;
	private View submitBtn;
	
	private FragmentManager fragmentManager;
	private SharedPreferences mSharedPreferences;
	private boolean isHasDefaultSetting;
	private BetLotteryBean mBetLotteryBean;
	private List<BetIssueBean> issueArray;
	private int dIssueNum,maxIssueSize,multiple;
	private String issue;
	private String lid;
	private int pursue;
	private long totalAmount;
	private double remainMoney;
	private long perBetMoney;
	
	private TitleViews titleViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_pursue_activity);
		init();
		addTitle();
		showView(isHasDefaultSetting,true);
	}
	
	private void init(){
		try {
			mSharedPreferences = Settings.getSharedPreferences(this);
			isHasDefaultSetting = Settings.getSharedPreferencesBoolean(mSharedPreferences, isHasDefaultSettingKey);
			mBetLotteryBean = (BetLotteryBean) SafeApplication.dataMap.get("BetLotteryBean");
			issueArray = (List<BetIssueBean>) SafeApplication.dataMap.get("issueArray");
			dIssueNum = mSharedPreferences.getInt(DefaultIssueNum, 20);
			maxIssueSize = issueArray.size()+1;
			multiple = mSharedPreferences.getInt(DefaultMultipleNum, 1);
			issue = getIntent().getStringExtra("issue");
			lid = mBetLotteryBean.getLotteryId();
			perBetMoney = mBetLotteryBean.getTotalAmount();
			LogUtil.DefalutLog("dIssueNum:"+dIssueNum+"---maxIssueSize:"+maxIssueSize+"---multiple:"+multiple);
			
			title = (RelativeLayout)findViewById(R.id.auto_pursue_title);
			pursue_content = (LinearLayout)findViewById(R.id.auto_pursue_content);
			
			fragmentManager = this.getSupportFragmentManager();
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}
	
	private void addTitle(){
		titleViews = new TitleViews(this, "代购帮助");
//		titleViews.setBtnIcon(R.drawable.btn_share_bg_d);
		titleViews.setBtnName("盈利追号设置");
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showView(false,false);
			}
		});
	}
	
	private void showView(boolean isShowResult, boolean isInit){
		if(isShowResult){
			titleViews.setTitleName("方案结果");
			titleViews.setFrameLayout(View.VISIBLE);
			AutoPursueResultFragment fragment = AutoPursueResultFragment.getInstance(mBetLotteryBean,issueArray,issue,maxIssueSize,this);
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			if(isInit){
				fragmentTransaction.add(R.id.auto_pursue_content,fragment);
			}else{
				fragmentTransaction.replace(R.id.auto_pursue_content,fragment);
			}
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}else{
			titleViews.setTitleName("盈利追号设置");
			titleViews.setFrameLayout(View.GONE);
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			AutoPursueSetFragment fragment = AutoPursueSetFragment.getInstance(maxIssueSize,dIssueNum,multiple,this);
			if(isInit){
				fragmentTransaction.add(R.id.auto_pursue_content,fragment);
			}else{
				fragmentTransaction.replace(R.id.auto_pursue_content,fragment);
			}
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
	}
	
	@Override
	public void createScheme() {
		showView(true,false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AutoPursueSetFragment.clear();
		AutoPursueResultFragment.clear();
	}
	
	@Override
	protected void onResume(){
        super.onResume();
        StatService.onResume(this);
    }

	@Override
	protected void onPause(){
        super.onPause();
        StatService.onPause(this);
    }

	@Override
	public void submit(ArrayList<AutoPursueBean> mAutoPursueBeans, View btn) {
		submitBtn = btn;
		if(!DoubleClickUtil.isFastDoubleClick()){
			if(mAutoPursueBeans != null){
				pursue = mAutoPursueBeans.size();
				if(pursue > 0){
					AutoPursueBean bean = mAutoPursueBeans.get(pursue-1);
					totalAmount = (long) ConversionUtil.StringToDouble( bean.getMoneySum().replace("元", "") );
					betSubmit(mAutoPursueBeans);
				}else{
					ToastUtil.diaplayMesShort(getApplicationContext(), "没有方案结果，请修改追号设置！");
				}
			}else{
				ToastUtil.diaplayMesShort(getApplicationContext(), "获取追号详情失败，请重试！");
			}
		}
	}
	
	private void betSubmit(ArrayList<AutoPursueBean> mAutoPursueBeans) {
		if (GetString.isLogin) {
			remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
			if (remainMoney >= totalAmount) {
				String newIssue = LotteryId.getIssue(lid);
				if(TextUtils.isEmpty(newIssue)){
					ToastUtil.diaplayMesShort(getApplicationContext(), "未能获取最新期次，请返回首页刷新重试！");
				}else if (issue.equals(newIssue)) {
					startTask(mAutoPursueBeans);
				} else {
					issueOutOfDate(newIssue,mAutoPursueBeans);
				}
			} else if (remainMoney < totalAmount) {
				PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(this);
				dialog.setMoney(totalAmount - remainMoney);
				dialog.show();
			}
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}
	
	private void issueOutOfDate(final String newIssue, final ArrayList<AutoPursueBean> mAutoPursueBeans) {
		NormalAlertDialog dialog = new NormalAlertDialog(this);
		dialog.setTitle("期次变更提示");
		dialog.setContent("您当前投注的期次已变更为" + newIssue + "期，是否继续投注？");
		dialog.setOk_btn_text("继续投注");
		dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
			@Override
			public void onOkBtnClick() {
				issue = newIssue;
				getPurserIssue(mAutoPursueBeans);
			}

			@Override
			public void onCancleBtnClick() {

			}
		});
		dialog.show();
	}
	
	private void startTask(final ArrayList<AutoPursueBean> mAutoPursueBeans) {
		submitBtn.setEnabled(false);
		mBuyLotteryTask = new BuyLotteryTask(this, "", getBetJson(mAutoPursueBeans),submitBtn);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				
				final SucceedDialog dialog = new SucceedDialog(AutoPursueActivity.this, lid, issue, perBetMoney/2, pursue, -1, totalAmount, (remainMoney - totalAmount));
				dialog.setOnDialogClickListener(new OnDialogClickListener() {
					@Override
					public void onPositiveButtonClick() {
						Settings.closeOtherActivitySetCurTabOne(AutoPursueActivity.this);
						dialog.dismiss();
						finish();
//						new FenxiangUtil(AutoPursueActivity.this, LotteryId.getLotteryName(lid), 1);
					}

					@Override
					public void onNegativeButtonClick() {
						dialog.dismiss();
						setResult(RESULT_OK);
						finish();
					}
				});
				dialog.show();
			}

		});
		mBuyLotteryTask.send();
	}
	
	/**
	 * @param mAutoPursueBeans
	 * @return
	 */
	private String getBetJson(ArrayList<AutoPursueBean> mAutoPursueBeans) {
		if(GetString.userInfo != null){
			mBetLotteryBean.setUserCode(GetString.userInfo.getUserCode());
		}
		mBetLotteryBean.setTotalAmount(totalAmount);
		mBetLotteryBean.setWinStop(1);
		mBetLotteryBean.setBuyType(4);
		mBetLotteryBean.setIssueArray(getIssue(mAutoPursueBeans));
		return JsonUtils.toJsonStr(mBetLotteryBean);
	}

	private ArrayList<BetIssueBean> getIssue(ArrayList<AutoPursueBean> mAutoPursueBeans){
		ArrayList<BetIssueBean> betIssueBeans = new ArrayList<BetIssueBean>(pursue);
		
		if(pursue > 0){ 
			AutoPursueBean mAutoPursueBean = mAutoPursueBeans.get(0);
	        //写入在售期
			BetIssueBean betIssueBean = new BetIssueBean();
			betIssueBean.setIssue(issue);
			betIssueBean.setMultiple( ConversionUtil.StringToInt(mAutoPursueBean.getMutiple().replace("倍", "")) );
			betIssueBeans.add(betIssueBean);
		
		
			for(int i = 1; i < pursue; i++){
				betIssueBean = new BetIssueBean();
				betIssueBean.setIssue(issueArray.get(i-1).getIssue());
				int mutiple = ConversionUtil.StringToInt(mAutoPursueBeans.get(i).getMutiple().replace("倍", "") );
				betIssueBean.setMultiple(mutiple);
				betIssueBeans.add(betIssueBean);
			}
		}
		
		return betIssueBeans; //返回当前在售期次
	}
	
	private void getPurserIssue(final ArrayList<AutoPursueBean> mAutoPursueBeans){
		final Map<String, String> map = new HashMap<String, String>();
		map.put("lotteryId", lid);
		map.put("page", "1");
		map.put("pageSize", "99");
		final ProgressDialog mDialog = ProgressDialog.show(AutoPursueActivity.this, "", "正在更新预售期次...", true, false);
		
		SafelotteryHttpClient.post(this, "3301", "pre", JsonUtils.toJsonStr(map), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					List<IssueInfoBean> beanList = (List<IssueInfoBean>)JsonUtils.parserJsonArray( (String)mMap.get("issueList"), new IssueInfoParser());
					BetIssueBean betIssueBean;
					issueArray = new ArrayList<BetIssueBean>();
					for (IssueInfoBean mBean : beanList) {
						betIssueBean = new BetIssueBean();
						if (mBean.getStatus().equals("0")) {
							betIssueBean.setIssue(mBean.getName());
							issueArray.add(betIssueBean);
						}
					}
					if (issueArray.size() > 0) {
						String newIssue = issueArray.get(0).getIssue();
						if (!TextUtils.isEmpty(newIssue)
								&& newIssue.equals(issue)) {
							issueArray.remove(0);
						}
					}
					startTask(mAutoPursueBeans);
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
				mDialog.dismiss();
			}
		});
	}
}
