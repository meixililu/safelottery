package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LotteryId;

public class SucceedDialog extends Dialog {

	public static final int PURSUE_NULL = 0XFFFF01;

	// private Dialog dialog;
	private Context mContext;
	private String mLid, mIssue;
	private String mBet;
	private String mBalance;
	private String positive, negative;
	private String mRemark;
	private String mConsume;
	private int mPursue, mMultiple;
	private OnDialogClickListener clickListener;

	private Button shareBtn;
	private Button continueBtn;

	private int number = 0;

	public SucceedDialog(Context context) {
		super(context, R.style.dialog);
		this.mContext = context;
	}

	/****
	 * 初始化数据．追号可以传空,其他不能为空
	 * 
	 * @param context
	 * @param mLid
	 *            彩种
	 * @param mIssue
	 *            期次
	 * @param mBet
	 *            注数
	 * @param mPursue
	 *            追号
	 * @param mMultiple
	 *            倍数
	 * @param mConsume
	 *            消费
	 * @param mBalance
	 *            余额
	 */
	public SucceedDialog(Context context, String mLid, String mIssue, double mBet, int mPursue, int mMultiple, double mConsume, double mBalance) {
		super(context, R.style.dialog);
		this.mContext = context;
		this.mLid = LotteryId.getLotteryName(mLid);
		this.mIssue = mIssue;
		this.mBet = GetString.df_0.format(mBet);
		this.mPursue = mPursue;
		this.mMultiple = mMultiple;
		this.mConsume = GetString.df.format(mConsume);
		this.mBalance = GetString.df.format(mBalance);
		this.positive = "返回首页";
		this.negative = "继续购彩";
	}

	/**
	 * 竞彩专用。增加备注显示
	 * 
	 * @param context
	 * @param mLid
	 * @param mIssue
	 * @param mBet
	 * @param mPursue
	 * @param mMultiple
	 * @param mConsume
	 * @param mBalance
	 * @param mRemark
	 */
	public SucceedDialog(Context context, String mLid, String mIssue, double mBet, int mPursue, int mMultiple, double mConsume, double mBalance, String mRemark) {
		super(context, R.style.dialog);
		this.mContext = context;
		this.mLid = LotteryId.getLotteryName(mLid);
		this.mIssue = mIssue;
		this.mBet = GetString.df_0.format(mBet);
		this.mPursue = mPursue;
		this.mMultiple = mMultiple;
		this.mConsume = GetString.df.format(mConsume);
		this.mBalance = GetString.df.format(mBalance);
		this.positive = "返回首页";
		this.negative = "继续购彩";
		this.mRemark = mRemark;
	}

	// 合买不要注数，追号
	public SucceedDialog(Context context, String mLid, String mIssue, int mMultiple, double mConsume, double mBalance, int num, String playid) {
		super(context, R.style.dialog);
		this.mContext = context;
		if (mLid.equals("300")) {
			if (playid.equals("01")) {
				this.mLid = "传统足球14场";
			} else if (playid.equals("02")) {
				this.mLid = "任选九";
			}
		} else {
			this.mLid = LotteryId.getLotteryName(mLid);
		}
		this.mIssue = mIssue;
		this.mMultiple = mMultiple;
		this.mConsume = GetString.df.format(mConsume);
		this.mBalance = GetString.df.format(mBalance);
		this.positive = "-";
		this.negative = "继续购彩";
		this.number = num;
	}

	/****
	 * 初始化数据．追号可以传空,其他不能为空 带提示,提示可以传空
	 * 
	 * @param context
	 * @param mLid
	 *            彩种
	 * @param mIssue
	 *            期次
	 * @param mBet
	 *            注数
	 * @param mPursue
	 *            追号
	 * @param mMultiple
	 *            倍数
	 * @param mConsume
	 *            消费
	 * @param mBalance
	 *            余额
	 * @param positive
	 *            左边正的提示 不想显示传　"－"
	 * @param negative
	 *            右边反的提示
	 */
	public SucceedDialog(Context context, String mLid, String mIssue, double mBet, int mPursue, int mMultiple, double mConsume, double mBalance, String positive, String negative) {
		super(context, R.style.dialog);
		this.mContext = context;
		this.mLid = LotteryId.getLotteryName(mLid);
		this.mIssue = mIssue;
		this.mBet = GetString.df_0.format(mBet);
		this.mPursue = mPursue;
		this.mMultiple = mMultiple;
		this.mConsume = GetString.df.format(mConsume);
		this.mBalance = GetString.df.format(mBalance);
		this.positive = positive;
		this.negative = negative;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// LayoutInflater inflater = LayoutInflater.from(context);
		// dialog = new Dialog(context, R.style.dialog);
		// new Dialog(context).setCancelable(false);
		// new Dialog(context).dismiss();
		setContentView(R.layout.dialog_succeed);
		TextView issue = (TextView) findViewById(R.id.buy_lottery_suc_issue);
		TextView pursue = (TextView) findViewById(R.id.buy_lottery_suc_pursue);
		TextView multiple = (TextView) findViewById(R.id.buy_lottery_suc_multiple);
		TextView consume = (TextView) findViewById(R.id.buy_lottery_suc_consume);
		TextView balance = (TextView) findViewById(R.id.buy_lottery_suc_balance);
		TextView buy_lottery_title_tx = (TextView) findViewById(R.id.buy_lottery_title_tx);
		TextView buy_lottery_suc_remark = (TextView) findViewById(R.id.buy_lottery_suc_remark);

		shareBtn = (Button) findViewById(R.id.buy_lottery_suc_share);
		continueBtn = (Button) findViewById(R.id.buy_lottery_suc_continue);

		GetString.isAccountNeedRefresh = true;

		if (!TextUtils.isEmpty(mBalance))
			GetString.userInfo.setUseAmount(mBalance);

		this.setCancelable(false);
		if (number == 1) {
			buy_lottery_title_tx.setText("方案已发起，请等待出票");
			issue.setText(mLid + " " + mIssue + "期   ");
			multiple.setText("倍投：" + mMultiple + "倍");
			consume.setText("消费：￥" + mConsume);
			pursue.setVisibility(View.GONE);
			balance.setText("余额：￥" + mBalance);
		} else {
			issue.setText(mLid + " " + mIssue + "期   " + mBet + "注");
			if (mPursue != PURSUE_NULL)
				pursue.setText("追号：" + mPursue + "期");
			else {
				pursue.setVisibility(View.GONE);
			}
			multiple.setText("倍投：" + mMultiple + "倍");
			consume.setText("消费：￥" + mConsume);
			balance.setText("余额：￥" + mBalance);

		}
		if(mMultiple == -1){
			multiple.setText("倍投：盈利追号");
		}
		if (!TextUtils.isEmpty(positive)) {
			shareBtn.setText(positive);
		}

		if (!TextUtils.isEmpty(negative)) {
			continueBtn.setText(negative);
		}

		if (positive.equals("-")) {
			shareBtn.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(mRemark)) {
			buy_lottery_suc_remark.setText(mRemark);
			buy_lottery_suc_remark.setVisibility(View.VISIBLE);
		}

		shareBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickListener != null) {
					clickListener.onPositiveButtonClick();
				}
			}
		});

		continueBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (clickListener != null) {
					clickListener.onNegativeButtonClick();
				}
			}
		});

	}

	public void setOnDialogClickListener(OnDialogClickListener clickListener) {
		this.clickListener = clickListener;
	}

}
