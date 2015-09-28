package com.zch.safelottery.util;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordMatchListBean;
import com.zch.safelottery.jingcai.JZActivity;

public class JC_HHGG_utils {
	private Context mContext;
	private View mContentView;
	private View mContentItemView;
	private LinearLayout mLayout;
	private List<RecordMatchListBean> result_list;
	private String lotteryId;

	private JC_HHGG_utils(Context context) {
		this.mContext = context;
	}

	public static JC_HHGG_utils init(Context context) {
		return new JC_HHGG_utils(context);
	}

	public View getView(List<RecordMatchListBean> result_list, String lotteryId) {
		this.result_list = result_list;
		this.lotteryId = lotteryId;
		mLayout = new LinearLayout(mContext);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0, s = result_list.size(); i < s; i++) {
			if (lotteryId.equals(LotteryId.CGJ)) {
				//如果是猜冠军，那么走这个
				mLayout.addView(getCGJRowViewContent(i));
			} else
				mLayout.addView(getViewContent(mContentView, i, lotteryId));
		}
		return mLayout;
	}

	
	  /**
	   * 获取猜冠军投注选项。
	   * @param i 索引号
	   * @return 一行选项
	   */
	private View getCGJRowViewContent(int i) {
		TextView tv = new TextView(mContext);
		int dip2px = ScreenUtil.dip2px(mContext, 5);
		tv.setPadding(dip2px, dip2px, dip2px, dip2px);
		if (result_list != null && result_list.size() > i) {
			RecordMatchListBean item = result_list.get(i);
			if ("1".equals(item.getBonusStatus())) {// 中奖
				tv.setTextColor(mContext.getResources().getColor(R.color.red_e54f46));
				tv.setText(item.getMatchName() + "(" + item.getSp()+")");
			} else {// 未中奖
				tv.setTextColor(mContext.getResources().getColor(R.color.gray));
				tv.setText(item.getMatchName() + "(" + item.getSp()+")");
			}
		}
		return tv;
	}

	private View getViewContent(View contentView, int postion, String lotteryId) {
		Holder mHolder;
		if (contentView == null) {
			// 初始化所有控件
			mHolder = new Holder();
			contentView = LayoutInflater.from(mContext).inflate(R.layout.jc_resut_hhggl_item, null);
			mHolder.changci = (TextView) contentView.findViewById(R.id.changci);
			mHolder.duizhen = (TextView) contentView.findViewById(R.id.duizhen);
			mHolder.result_content = (LinearLayout) contentView.findViewById(R.id.result_content);
			mHolder.danma = (CheckBox) contentView.findViewById(R.id.danma);
			contentView.setTag(mHolder);
		} else {
			mHolder = (Holder) contentView.getTag();
		}

		RecordMatchListBean mBean = result_list.get(postion);
		mHolder.changci.setText(mBean.getMatchNo());

		// 合计比分
		String score = mBean.getMainTeamScore() + ":" + mBean.getGuestTeamScore();
		if (score.equals(":")) {
			score = " VS ";
		}
		String letBall = mBean.getLetBall();
		String team;
		if(lotteryId.equals(LotteryId.JCLQ)){
			if (TextUtils.isEmpty(letBall))
				team = mBean.getMainTeam() + "(主) " + score + " " + mBean.getGuestTeam();
			else
				team = mBean.getMainTeam() + "(主" + letBall + ") " + " " + score + " " + mBean.getGuestTeam();
		}else{
			if (TextUtils.isEmpty(letBall))
				team = mBean.getMainTeam() + " " + score + " " + mBean.getGuestTeam();
			else
				team = mBean.getMainTeam() + "(" + letBall + ") " + " " + score + " " + mBean.getGuestTeam();
			
		}

		SpannableString spa = new SpannableString(team);
		if (!TextUtils.isEmpty(letBall)) {
			int star = team.indexOf(letBall);
			if (ConversionUtil.StringToInt(letBall) >= 0) {
				spa.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_e54f46)), star, star + letBall.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				spa.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.green_2ca403)), star, star + letBall.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		mHolder.duizhen.setText(spa);

		if (mBean.getDan().equals("1")) {
			mHolder.danma.setChecked(true);
		} else {
			mHolder.danma.setVisibility(View.GONE);
		}

		// 内容
		String[] caiguo = null;
		if (!TextUtils.isEmpty(mBean.getResult())) {
			caiguo = mBean.getResult().split("&");
		}
		String[] touzhu = mBean.getNumber().split("&");
		String[] palyCode = mBean.getPlayCode().split("&");
		for (int i = 0, s = touzhu.length; i < s; i++) {
			if (i > 0)
				mHolder.result_content.addView(XmlUtil.getLine(mContext));
			if (caiguo == null || caiguo.length == 0) {
				mHolder.result_content.addView(getViewContentItem(mContentItemView, touzhu[i], "", palyCode[i], postion));
			} else {
				mHolder.result_content.addView(getViewContentItem(mContentItemView, touzhu[i], caiguo[i], palyCode[i], postion));
			}
		}
		return contentView;
	}

	private View getViewContentItem(View contentView, String touzhu, String caiguo, String playCode, int postion) {
		ContentHolder mHolder;
		if (contentView == null) {
			mHolder = new ContentHolder();
			contentView = LayoutInflater.from(mContext).inflate(R.layout.jc_resut_content_item, null);
			mHolder.caiguo = (TextView) contentView.findViewById(R.id.caiguo);
			mHolder.touzhu = (TextView) contentView.findViewById(R.id.touzhu);
			contentView.setTag(mHolder);
		} else {
			mHolder = (ContentHolder) contentView.getTag();
		}

		mHolder.touzhu.setText(equalsWin(touzhu, caiguo, playCode));
		mHolder.caiguo.setText(caiguo);
		return contentView;
	}

	private SpannableString equalsWin(String touzhu, String caiguo, String playCode) {
		String[] result = touzhu.split(",");
		StringBuilder sb = new StringBuilder();
		String temp;
		String red = "";
		for (int i = 0, s = result.length; i < s; i++) {
			if (i % 3 == 0) {
				if (i > 0)
					sb.append("\n");
			} else {
				sb.append("  ");
			}
			if (lotteryId.equals(LotteryId.JCZQ) && playCode.equals(JZActivity.WF_RQSPF)) {
				result[i] = "让" + result[i];
				if (!TextUtils.isEmpty(caiguo) && !caiguo.contains("让")) {
					caiguo = "让" + caiguo;
				}
			}
			sb.append(result[i]);
			if (red.equals("") && !TextUtils.isEmpty(caiguo)) {
				temp = result[i].substring(0, caiguo.length());
				if (temp.equals(caiguo)) {
					red = result[i];
				}
			}
		}
		SpannableString spa = new SpannableString(sb.toString());
		if (!TextUtils.isEmpty(red)) {
			int star = sb.indexOf(red);
			if (star >= 0) {
				spa.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_e54f46)), star, star + red.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spa;
	}

	private static class Holder {
		TextView changci;
		TextView duizhen;
		LinearLayout result_content;
		CheckBox danma;
	}

	private static class ContentHolder {
		TextView touzhu;
		TextView caiguo;
	}
}
