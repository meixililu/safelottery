package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.LotteryIssueHistoryBean;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.custom_control.CustomTextView;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.parser.IssueInfoParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;
import com.zch.safelottery.view.TitleViews;

public class IssueDetailActivity extends ZCHBaseActivity {
	
	private ProgressBar mProgressBar;
	private LinearLayout mMiddleLny;
	private TextView mPropmtTv;
	
	private ImageView mIconImg;
	private TextView mNameTv;
	private TextView mIssueTv;
	private TextView mTimeTv;
//	private TextView ;
	private LinearLayout mNumLny;

	private LinearLayout mPrizeLny;
	private LinearLayout mBonusLny;
	
	private RelativeLayout title;

	private CustomTextView mCustomView;
	
	private Button participation;

	private ArrayList<ArrayList<String>> mPrizeList;
	private ArrayList<ArrayList<String>> mBonusList;
	
	private String lotteryId;
	private String issue;
	private int index;
	
	private boolean isRequest;
	
	private IssueInfoBean mBean;
	
	private ArrayList<LotteryIssueHistoryBean> result_list;
	
	private TitleViews titleViews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.per_issue_detail_page);
		initUI();
		initData();
		addTitle();
	}

	/**
	 * 初始化UI
	 */
	private void initUI(){
		title = (RelativeLayout) findViewById(R.id.issue_detail_title);
		
		mProgressBar = (ProgressBar) findViewById(R.id.issue_detail_progress_bar);
		mMiddleLny = (LinearLayout) findViewById(R.id.issue_detail_middle);
		mPropmtTv = (TextView) findViewById(R.id.issue_detail_text_prompt);
		
		mIconImg = (ImageView) findViewById(R.id.issue_detail_icon);
		mNameTv = (TextView) findViewById(R.id.issue_detail_name);
		mIssueTv = (TextView) findViewById(R.id.issue_detail_issue);
		mTimeTv = (TextView) findViewById(R.id.issue_detail_time);
		
		mNumLny = (LinearLayout) findViewById(R.id.issue_detail_num_result_layout);
		mPrizeLny = (LinearLayout) findViewById(R.id.issue_detail_prize);
		mBonusLny = (LinearLayout) findViewById(R.id.issue_detail_bonus);

		LinearLayout mPageUpLny = (LinearLayout) findViewById(R.id.issue_detail_to_page_up);
		LinearLayout mPageNextLny = (LinearLayout) findViewById(R.id.issue_detail_to_page_next);
		
		Listener lis = new Listener();
		mPageUpLny.setOnClickListener(lis);
		mPageNextLny.setOnClickListener(lis);
		
		mCustomView = new CustomTextView(this);
	}
	
	private void addTitle(){
		titleViews = new TitleViews(this, "期次详情");
		titleViews.setBtnName("购彩");
//		titleViews.setBtnIcon(R.drawable.y11_title_button);
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent mIntent = BuyLotteryActivity.getLotteryIntent(IssueDetailActivity.this, lotteryId);
					if(mIntent != null){
						SafeApplication.dataMap.put("BuyLotteryActivity", BuyLotteryActivity.mContext);
						startActivity(mIntent);
					}else{
						Intent intent = new Intent(IssueDetailActivity.this, MainTabActivity.class);
						intent.putExtra(Settings.TABHOST, 0);
						IssueDetailActivity.this.startActivity(intent);
						Settings.closeOtherActivity(IssueDetailActivity.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 初始化 得到数据
	 */
	private void initData(){
		Bundle mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
		result_list = (ArrayList<LotteryIssueHistoryBean>) SafeApplication.dataMap.get(BaseLotteryActivity.INTENT_ISSUE);
		SafeApplication.dataMap.clear();
		
		if(mBundle != null){
			lotteryId = mBundle.getString(LotteryId.INTENT_LID);
			index = mBundle.getInt(BaseLotteryActivity.INTENT_ISSUE);
			issue = result_list.get(index).getName();
		}
		
		doRequestTask();
	}
	
	/**
	 * 写入数据
	 */
	private void setData(){
		try {
			//清空View
			mNumLny.removeAllViews();
			mPrizeLny.removeAllViews();
			mBonusLny.removeAllViews();
			
			//修改及添加View
			mIconImg.setBackgroundResource(LotteryId.getLotteryIcon(lotteryId));
			mNameTv.setText(LotteryId.getLotteryName(lotteryId));
			mIssueTv.setText("第" + issue + "期");
			mTimeTv.setText("开奖时间：" + TimeUtils.customFormatDate(mBean.getBonusTime(), TimeUtils.DateFormat, TimeUtils.DateMinuteFormat));
			mNumLny.addView(ViewUtil.getAutoView(getApplicationContext(), lotteryId, mBean.getBonusNumber(), true, 1));
			
			getListData();
			mPrizeLny.addView(mCustomView.setTable(mCustomView.getTable(), mPrizeList, 0));
			mBonusLny.addView(mCustomView.setTable(mCustomView.getTable(), mBonusList, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getListData(){
		ArrayList<String> strTemp;
		mPrizeList = new ArrayList<ArrayList<String>>();
		strTemp= new ArrayList<String>();
		strTemp.add("本期销量");
		strTemp.add("奖池奖金");
		mPrizeList.add(strTemp);
		
		strTemp= new ArrayList<String>();
		strTemp.add(mBean.getGlobalSaleTotal());
		strTemp.add(mBean.getPrizePool());
		mPrizeList.add(strTemp);
		
		mBonusList = new ArrayList<ArrayList<String>>();
		strTemp= new ArrayList<String>();
		strTemp.add("奖项");
		strTemp.add("中奖注数");
		strTemp.add("每注金额");
		mBonusList.add(strTemp);
		
		List<Map<String, String>> list = JsonUtils.stringToList(mBean.getBonusClass());
		if(list != null){
			if(list.size() > 0){
				for(Map<String, String> map: list){
					strTemp= new ArrayList<String>();
					strTemp.add(map.get("className"));
					strTemp.add(map.get("total"));
					strTemp.add(map.get("amount"));
					mBonusList.add(strTemp);
				}
			}else{
				strTemp= new ArrayList<String>();
				strTemp.add("无");
				strTemp.add("无");
				strTemp.add("无");
				mBonusList.add(strTemp);
			}
		}
		
		
	}
	/**
	 * 请求数据
	 * @return
	 */
	private String requestData(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("lotteryId", lotteryId);
		map.put("issue", issue);
		return JsonUtils.toJsonStr(map);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
	}

	private void doRequestTask() {
		isRequest = true;
		mProgressBar.setVisibility(View.VISIBLE);
		mMiddleLny.setVisibility(View.GONE);
		mPropmtTv.setVisibility(View.GONE);
		
		mBean = null;
		
		SafelotteryHttpClient.post(this, "3301", "detail", requestData(), new TypeSafelotteryHttpResponseHandler(this, true, new IssueInfoParser()) {
			
			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
				mBean = (IssueInfoBean) response;
				mMiddleLny.setVisibility(View.VISIBLE);
				setData();
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				mPropmtTv.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onFinish(){
				isRequest = false;
				mProgressBar.setVisibility(View.GONE);
			}
		});
		
	}
	
	private class Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.issue_detail_to_page_up){
				if(isRequest){
					ToastUtil.diaplayMesShort(getApplicationContext(), "正在提交上一次的请求，请稍后再试！");
				}else{
					if(index < result_list.size()-1){
						issue = result_list.get(++index).getName();
						doRequestTask();
					}else{
						ToastUtil.diaplayMesShort(getApplicationContext(), "没有更早的期次了");
					}
				}
			}else if(id == R.id.issue_detail_to_page_next){
				if(isRequest){
					ToastUtil.diaplayMesShort(getApplicationContext(), "正在提交上一次的请求，请稍后再试！");
				}else{
					if(index > 0){
						issue = result_list.get(--index).getName();
						doRequestTask();
					}else{
						ToastUtil.diaplayMesShort(getApplicationContext(), "没有更新的期次了");
					}
				}
				
			}
		}
		
	}
}
