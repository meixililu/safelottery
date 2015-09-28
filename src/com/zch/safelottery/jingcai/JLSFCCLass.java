package com.zch.safelottery.jingcai;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.dialogs.JCGradeShowDialog;
import com.zch.safelottery.dialogs.JCGradeShowDialog.OnShowOnClickListener;
import com.zch.safelottery.jingcai.JLBetActivity.SectionListAdapter;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.util.TimeUtils;

public class JLSFCCLass extends JCAbstractClass {

	private JCBaseActivity mActivity;
	private LayoutInflater inflater;
	private SectionListAdapter sectionAdapter;

	public JLSFCCLass(JCBaseActivity mActivity, LayoutInflater inflater,SectionListAdapter sectionAdapter) {
		this.mActivity = mActivity;
		this.inflater = inflater;
		this.sectionAdapter=sectionAdapter;
	}

	public JLSFCCLass(JCBaseActivity mActivity, LayoutInflater inflater) {
		this.mActivity = mActivity;
		this.inflater = inflater;
	}

	@Override
	public void showItems(ImageLoader imageLoader, final JZMatchBean bean, LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.jl_sfc_item, null);
		parent_layout.addView(convertView);

		TextView end_time = (TextView) convertView.findViewById(R.id.end_time);
		TextView item_main = (TextView) convertView.findViewById(R.id.item_mian);
		TextView item_guest = (TextView) convertView.findViewById(R.id.item_guest);
		TextView league_name = (TextView) convertView.findViewById(R.id.league_name);
		final CheckBox item_selected = (CheckBox) convertView.findViewById(R.id.item_selected);
		TextView aSpS = (TextView) convertView.findViewById(R.id.item_as);
		TextView aSpF = (TextView) convertView.findViewById(R.id.item_af);
		
		item_main.setText( bean.getMainTeam() ); 
		item_guest.setText( bean.getGuestTeam());
		end_time.setText(  bean.getSn() + " " + TimeUtils.customFormatDate(bean.getEndFuShiTime(), TimeUtils.DateFormat, TimeUtils.MinuteFormat));
		league_name.setText(bean.getMatchName());
		setAP(bean.getAvgOdds(),aSpS, aSpF);//均赔
		setSP(item_selected, bean);
		
		item_selected.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JCGradeShowDialog showDialog = new JCGradeShowDialog(mActivity, LotteryId.JCLQ, JLActivity.WF_SFC, bean);
				showDialog.setHeiht(ScreenUtil.dip2px(mActivity, 250));
				showDialog.setOnShowClickListener(new OnShowOnClickListener() {
					@Override
					public void onOkBtnClick(){
						if(bean.getNumstr().equals("")){
							item_selected.setChecked(false);
						}else{
							item_selected.setChecked(true);
						}
						sectionAdapter.notifyDataSetChanged();
						setResult(bean, item_selected.isChecked());
					}
					@Override
					public void onCancleBtnClick() {
						if(bean.getNumstr().equals("")){
							item_selected.setChecked(false);
						}else{
							item_selected.setChecked(true);
						}
						sectionAdapter.notifyDataSetChanged();
					}
				});
				showDialog.show();
			}
		});
	}


	@Override
	public void showItems(JZMatchBean bean, LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.jl_result_item, null);
		TextView matchName = (TextView) convertView.findViewById(R.id.result_match_name);
		TextView noDate = (TextView) convertView.findViewById(R.id.result_no_date);
		TextView teamMain = (TextView) convertView.findViewById(R.id.result_team_main);
		TextView teamGuest = (TextView) convertView.findViewById(R.id.result_team_guest);
		TextView result = (TextView) convertView.findViewById(R.id.result_result);
		TextView pointScore = (TextView) convertView.findViewById(R.id.result_point_score);
		
		matchName.setText(bean.getMatchName());
		noDate.setText(bean.getWeek() + " " + bean.getSn());
		teamMain.setText(bean.getMainTeam().trim());
		teamGuest.setText(bean.getGuestTeam().trim());
		
		String MainTeamScore = bean.getMainTeamScore();
		String GuestTeamScore = bean.getGuestTeamScore();
		if(TextUtils.isEmpty(MainTeamScore) || TextUtils.isEmpty(GuestTeamScore)){
			pointScore.setText( "-" );
			result.setText("-");
		}else{
			int main = ConversionUtil.StringToInt(MainTeamScore);
			int guest = ConversionUtil.StringToInt(GuestTeamScore);
			pointScore.setText( MainTeamScore + ":" + GuestTeamScore );
			result.setText(bean.getSfcResult());
			if(main > guest){
				pointScore.setTextColor(mActivity.getResources().getColor(R.color.red_e54f46));
				result.setTextColor(mActivity.getResources().getColor(R.color.red_e54f46));
			}else if(main == guest){
				pointScore.setTextColor(mActivity.getResources().getColor(R.color.text_blue));
				result.setTextColor(mActivity.getResources().getColor(R.color.text_blue));
			}else{
				pointScore.setTextColor(mActivity.getResources().getColor(R.color.green_2ca403));
				result.setTextColor(mActivity.getResources().getColor(R.color.green_2ca403));
			}
		}
	
		parent_layout.addView(convertView);
	}
	
	/**
	 * 对阵
	 * 
	 * @param home
	 * @param rangQiu
	 * @param guest
	 * @return
	 */
	public static String teamVs(String home, String guest) {
		return home + " vs " + guest;
	}

	public static void setSP(CheckBox item_selected, JZMatchBean bean) {
		if (bean.getNumstr().equals("")) {
			item_selected.setText("请点击选择投注选项");
			item_selected.setChecked(false);
		} else {
			item_selected.setText(bean.getNumstr());
			item_selected.setChecked(true);
		}
	}

	/**
	 * 设置均赔
	 * 
	 * @param ap
	 * @param spS
	 * @param spP
	 * @param spF
	 */
	public static void setAP(String ap, TextView spS, TextView spP) {
		try {
			String aps[] = ap.split("#");
			int j = aps.length;
			if (j >= 2) {
				for (int i = 0; i < j; i++) {
					if (i == 0)
						spS.setText(aps[0]);
					if (i == 1)
						spP.setText(aps[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 点击选项处理操作
	 * 
	 * @param bean
	 * @param result
	 * @param isAdd
	 */
	public void setResult(JZMatchBean bean, boolean isAdd) {
		if (isAdd) {
			if (bean.count == 0) {
				JLBetActivity.selectNumber++;
				bean.count++;
			}
		} else {
			if(bean.count>0){
				bean.count--;
				JLBetActivity.selectNumber--;
				bean.setHasDan(false);
			}
		}
		mActivity.setChangCi(JLBetActivity.selectNumber);
	}
}
