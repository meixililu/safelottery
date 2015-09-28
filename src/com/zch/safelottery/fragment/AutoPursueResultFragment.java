package com.zch.safelottery.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.AutoPursueBean;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.impl.AutoPursueListener;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AwardUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtils;

public class AutoPursueResultFragment extends Fragment {
	
	private LayoutInflater inflater;
	private LinearLayout content_layout;
	private TextView money_sum,issue_sum;
	private Button submit_btn;
	
	private BetLotteryBean mBetLotteryBean;
	private List<BetIssueBean> betIssueBeans;
	private int maxIssueSize,pursueIssueSize,mutiple;
	private SharedPreferences mSharedPreferences;
	private String ylKey;//yllNum：盈利率，yleNum：盈利额
	private int ylNumber;
	private long money; //方案总金额
	private int award; //最低奖金
	private long moneySum;
	private ArrayList<AutoPursueBean> mAutoPursueBean;
	private static AutoPursueResultFragment instance;
	private AutoPursueListener mAutoPursueListener;
	private List<BetIssueBean> issueArray;
	private String currentIssue;
	
	public static AutoPursueResultFragment getInstance(BetLotteryBean mBean, List<BetIssueBean> issueArray,
			String issue,int mIissueSize, AutoPursueListener listener){
		if(instance == null){
			instance = new AutoPursueResultFragment(mBean,mIissueSize,issue,issueArray);
			instance.setmAutoPursueListener(listener);
		}
		return instance;
	}
	
	public static void clear(){
		instance = null;
	}
	
	public AutoPursueResultFragment(BetLotteryBean mBean, int mIissueSize,String issue,List<BetIssueBean> issueArray){
		this.mBetLotteryBean = mBean;
		this.maxIssueSize = mIissueSize;
		this.issueArray = issueArray;
		currentIssue = issue;
		money = mBean.getTotalAmount();
		award = AwardUtil.award(mBean);
		LogUtil.DefalutLog("money:"+money+"---award:"+award);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.DefalutLog(this.getClass().getName()+"-onCreate");
		super.onCreate(savedInstanceState);
		mSharedPreferences = Settings.getSharedPreferences(getActivity());
		inflater = LayoutInflater.from(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(this.getClass().getName()+"-onCreateView");
		View view = inflater.inflate(R.layout.auto_pursue_result_fragment, container, false);
		initview(view);
		return view;
	}
	
	private void initview(View view){
		content_layout = (LinearLayout)view.findViewById(R.id.auto_pursue_content_layout);
		money_sum = (TextView)view.findViewById(R.id.auto_pursue_money_sum);
		issue_sum = (TextView)view.findViewById(R.id.auto_pursue_issue_sum);
		submit_btn = (Button)view.findViewById(R.id.auto_pursue_submit);
		
		submit_btn.setOnClickListener(mClickListener);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSetting();
		count();
		showView();
	}

	private void getSetting(){
		pursueIssueSize = mSharedPreferences.getInt("DefaultIssueNum", 0);
		mutiple = mSharedPreferences.getInt("DefaultMultipleNum", 0);
		ylKey = mSharedPreferences.getString("ylKey", "");
		ylNumber = mSharedPreferences.getInt(ylKey, 0);
		LogUtil.DefalutLog("ylKey:"+ylKey);
	}
	
	private String getIssueNumber(String issue){
		int size = issue.length();
		if(size > 2){
			issue = issue.substring(size-2, size);
			
		}
		return issue;
	}

	private void count(){
		long moneyCurrent; //当前投
		double awardCurrent; //当前奖金
		moneySum = 0; //累计投
		mAutoPursueBean = new ArrayList<AutoPursueBean>();
		if(money < award){
			if(pursueIssueSize > issueArray.size()){
				pursueIssueSize = issueArray.size();
			}
			for(int i=0; i<pursueIssueSize; i++){
				while(mutiple < 1000){
					awardCurrent = mutiple * award;
					moneyCurrent = mutiple * money;
					moneySum += moneyCurrent;
					long tempProfit = (long) (awardCurrent-moneySum);
					double tempProfitPercent = (double)tempProfit/moneySum*100;
					String tempProfitPercentStr = String.format("%.2f",tempProfitPercent)+"%";
					if(ylKey.equals("yllNum")){
						if(tempProfitPercent >= ylNumber){
							AutoPursueBean bean = new AutoPursueBean();
							if(i == 0){
								bean.setIssue( getIssueNumber(currentIssue) );
							}else{
								bean.setIssue( getIssueNumber(issueArray.get(i-1).getIssue()) );
							}
							bean.setMutiple(mutiple+"倍");
							bean.setMoney(moneyCurrent+"元");
							bean.setMoneySum(moneySum+"元");
							bean.setProfit(tempProfit+"元");
							bean.setProfitPercent(tempProfitPercentStr);
							mAutoPursueBean.add(bean);
							break;
						}else{
							moneySum -= moneyCurrent;
						}
					}else{
						if(tempProfit >= ylNumber){
							AutoPursueBean bean = new AutoPursueBean();
							if(i == 0){
								bean.setIssue( getIssueNumber(currentIssue) );
							}else{
								bean.setIssue( getIssueNumber(issueArray.get(i-1).getIssue()) );
							}
							bean.setMutiple(mutiple+"倍");
							bean.setMoney(moneyCurrent+"元");
							bean.setMoneySum(moneySum+"元");
							bean.setProfit(tempProfit+"元");
							bean.setProfitPercent(tempProfitPercentStr);
							mAutoPursueBean.add(bean);
							break;
						}else{
							moneySum -= moneyCurrent;
						}
					}
					mutiple++;
				}
			}
			int issueSize = mAutoPursueBean.size();
			if(issueSize == 0){
				ToastUtil.diaplayMesShort(getActivity(), "亲，是否设置的太高了，请调整后重试");
			}
		}else{
			ToastUtil.diaplayMesShort(getActivity(), "无法生成，请调整方案后重试");
			getActivity().finish();
		}
		int issueSize = mAutoPursueBean.size();
		money_sum.setText("共"+moneySum+"元");
		issue_sum.setText("本期投注为"+issueSize+"期");
	}
	
	private void showView(){
		content_layout.removeAllViews();
		for(AutoPursueBean bean : mAutoPursueBean){
			content_layout.addView( showItem(bean) );
			content_layout.addView(ViewUtils.getDashed(getActivity()));
		}
	}
	
	private OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.auto_pursue_submit){
				if(mAutoPursueListener != null){
					mAutoPursueListener.submit(mAutoPursueBean,submit_btn);
				}
			}
		}
	};
	
	private View showItem(AutoPursueBean mAutoPursueBean){
		View view = inflater.inflate(R.layout.auto_pursue_result_item, null);
		TextView issue = (TextView) view.findViewById(R.id.auto_pursue_issue);
		TextView mutiple = (TextView) view.findViewById(R.id.auto_pursue_mutiple);
		TextView current_money = (TextView) view.findViewById(R.id.auto_pursue_current_money);
		TextView sum_money = (TextView) view.findViewById(R.id.auto_pursue_sum_money);
		TextView profit = (TextView) view.findViewById(R.id.auto_pursue_profit);
		TextView profit_percent = (TextView) view.findViewById(R.id.auto_pursue_profit_percent);
		
		issue.setText(mAutoPursueBean.getIssue());
		mutiple.setText(mAutoPursueBean.getMutiple());
		current_money.setText(mAutoPursueBean.getMoney());
		sum_money.setText(mAutoPursueBean.getMoneySum());
		profit.setText(mAutoPursueBean.getProfit());
		profit_percent.setText(mAutoPursueBean.getProfitPercent());
		return view;
	}

	@Override
	public void onDestroy() {
		LogUtil.DefalutLog(this.getClass().getName()+"-onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(this.getClass().getName()+"-onDestroyView");
		super.onDestroyView();
	}
	
	public void setmAutoPursueListener(AutoPursueListener mAutoPursueListener) {
		this.mAutoPursueListener = mAutoPursueListener;
	}

}
