package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class JLAllAdapter extends JCAdapter {

	private Context mContext;
	private int danCount;
	private int danSum;
	private String playMethod;
	
	public JLAllAdapter(Context context, ArrayList<JZMatchBean> selectedBeans, LayoutInflater inflater, int dan, String playMethod) {
		super(selectedBeans, inflater);
		mContext = context;
		danCount = dan;
		this.playMethod = playMethod;
	}

	@Override
	public View getViews(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.bdqc_order_item, null);
			holder.txMainTeam = (TextView) convertView.findViewById(R.id.main_team);
			holder.txTeamVs = (TextView) convertView.findViewById(R.id.team_vs);
			holder.txGuestTeam = (TextView) convertView.findViewById(R.id.guest_team);
			holder.txTouzhu = (TextView) convertView.findViewById(R.id.touzhu_text);
			holder.checkDan = (CheckBox) convertView.findViewById(R.id.dan_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final JZMatchBean bean = selectedBeans.get(position);
		final int beanCount = selectedBeans.size();
		holder.txMainTeam.setText(bean.getMainTeam());
		holder.txTeamVs.setText( bean.getLetBall() );
		holder.txGuestTeam.setText(bean.getGuestTeam());
		holder.txTouzhu.setText(codeToText(bean) );
		holder.checkDan.setChecked(bean.isHasDan());
		
		if(playMethod.equals(JLActivity.WF_SFC)){
			danSum = 4;
		}else{
			danSum = 8;
		}
		
		holder.checkDan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogUtil.DefalutLog("danCount:" + danCount);
				if (!holder.checkDan.isChecked()) {
					danCount--;
					bean.setHasDan(false);
					if (mJCDingDanClickListener != null) {
						mJCDingDanClickListener.onCheck(danCount);
					}
				} else if (danCount < danSum) {
					if (danCount < (beanCount - 1)) {
						danCount++;
						bean.setHasDan(true);
						if (mJCDingDanClickListener != null) {
							mJCDingDanClickListener.onCheck(danCount);
						}
					} else {
						bean.setHasDan(false);
						notAllowDan(holder.checkDan);
					}
				} else {
					bean.setHasDan(false);
					notAllowDan(holder.checkDan);
				}
			}
		});
		return convertView;
	}

	private void notAllowDan(CheckBox checkDan) {
		ToastUtil.diaplayMesShort(mContext, "设胆已达上限");
		checkDan.setChecked(false);
	}
	
	private String codeToText(JZMatchBean bean){
		StringBuffer s = new StringBuffer();
		ArrayList<String> selectedList = bean.getSelectedList();
		Collections.sort(selectedList);
		if(playMethod.equals(JLActivity.WF_SF) || playMethod.equals(JLActivity.WF_RFSF)){
			for (int i=selectedList.size();i>0;i--) {
				if (selectedList.get(i-1).equals("1")) {
					s.append("主胜");
				}else if (selectedList.get(i-1).equals("2")) {
					s.append("客胜");
				}
				s.append(",");
			}
		}else if(playMethod.equals(JLActivity.WF_DXF)){
			for (int i=selectedList.size();i>0;i--) {
				if (selectedList.get(i-1).equals("1")) {
					s.append("大分");
				}else if (selectedList.get(i-1).equals("2")) {
					s.append("小分");
				}
				s.append(",");
			}
		}else if(playMethod.equals(JLActivity.WF_SFC)){
			return bean.getNumstr();
		}
		return s.deleteCharAt(s.lastIndexOf(",")).toString();
	}

	static class Holder {
		TextView txMainTeam;
		TextView txGuestTeam;
		TextView txTeamVs;
		TextView txTouzhu;
		CheckBox checkDan;
	}
}
