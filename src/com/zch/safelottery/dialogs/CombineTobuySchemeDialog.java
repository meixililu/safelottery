package com.zch.safelottery.dialogs;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordMainIssueListBean;
import com.zch.safelottery.bean.RecordMatchBean;
import com.zch.safelottery.bean.RecordProgramsListBean;
import com.zch.safelottery.bean.RecordSubGameListBean;
import com.zch.safelottery.util.JC_HHGG_utils;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.LotteryResultUtils;

/**
 * 自定义退出dialog
 * 
 * @author Messi
 * 
 */
public class CombineTobuySchemeDialog extends Dialog {

	private LinearLayout scheme_linear, current_period_num_linear;
	private TextView scheme_num;
	private View scheme_14or9;
	private TextView danma_title, chuanfa_tv;
	private LinearLayout scheme_14or9_layout, scheme_14or9_layout_title;
	
	private Context context;
	private RecordProgramsListBean recordProgramsListBean;
	private RecordMainIssueListBean recordMainIssueListBean;
	private List<RecordSubGameListBean> recordSubGameListBean;
	private RecordMatchBean recordMatchBean;

	public CombineTobuySchemeDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public CombineTobuySchemeDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public CombineTobuySchemeDialog(Context context,RecordProgramsListBean rplb, RecordMainIssueListBean rmilb,
			List<RecordSubGameListBean> lrslb, RecordMatchBean rmb) {
		super(context,R.style.popDialog);
		this.context = context;
		recordProgramsListBean = rplb;
		recordMainIssueListBean = rmilb;
		recordSubGameListBean = lrslb;
		recordMatchBean = rmb;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.combine_tobuy_scheme_dialog_layout);
			initViews();
			setCanceledOnTouchOutside(true);
			
			String lotteryId = recordProgramsListBean.getLotteryId();
			if (LotteryId.getLotteryNum(lotteryId) == 1) {
				LotteryResultUtils.getLotteryResultView(context, scheme_linear, lotteryId, 
						recordProgramsListBean.getNumberInfo(), recordMainIssueListBean, true, false);
				scheme_num.setText("：" + recordProgramsListBean.getNumberInfo().size() + "条");// 显示方案内容
			}else if (LotteryId.getLotteryNum(lotteryId) == 2) {
				scheme_14or9.setVisibility(View.VISIBLE);
				boolean isShowDan = LotteryResultUtils.checkIsShowDan(lotteryId);
				chuanfa_tv.setVisibility(View.GONE);
				if (isShowDan) {
					danma_title.setVisibility(View.VISIBLE);
				} else {
					danma_title.setVisibility(View.GONE);
				}
				LotteryResultUtils.getCZResultView(context, scheme_14or9_layout, lotteryId, recordSubGameListBean, isShowDan, recordProgramsListBean.getPlayId());
			}else if (LotteryId.getLotteryNum(lotteryId) == 3) {
				scheme_14or9.setVisibility(View.VISIBLE);
				scheme_14or9_layout_title.setVisibility(View.GONE);
				if (lotteryId.equals(LotteryId.CGJ)) {
					chuanfa_tv.setText("过    关：" + "单关");
				} else {
					chuanfa_tv.setText("过    关：" + recordMatchBean.getGuoGuan());
				}
				scheme_14or9_layout.removeAllViews();
				scheme_14or9_layout.addView(chuanfa_tv);
				scheme_14or9_layout.addView(JC_HHGG_utils.init(context).getView(recordMatchBean.getMatchList(),lotteryId));
//				LotteryResultUtils.getJCResultView(context, scheme_14or9_layout, lotteryId, recordMatchBean.getMatchList(), recordProgramsListBean.getPlayId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initViews(){
		scheme_num = (TextView) findViewById(R.id.buy_lottery_scheme_num);
		scheme_linear = (LinearLayout) findViewById(R.id.buy_lottery_agent_scheme_linear);
		scheme_14or9 = (View) findViewById(R.id.buy_lottery_agent_scheme_14or9);
		chuanfa_tv = (TextView) findViewById(R.id.chuanfa_tv);
		danma_title = (TextView) findViewById(R.id.danma_title);
		scheme_14or9_layout = (LinearLayout) findViewById(R.id.cz_14or9_viewpage_list);
		scheme_14or9_layout_title = (LinearLayout) findViewById(R.id.buy_lottery_agent_scheme_14or9_title);
	}


}
