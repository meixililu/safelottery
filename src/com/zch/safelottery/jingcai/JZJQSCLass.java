package com.zch.safelottery.jingcai;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.jingcai.JZBetActivity.SectionListAdapter;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.JCBFUtils;
import com.zch.safelottery.util.TimeUtils;

public class JZJQSCLass extends JCAbstractClass {

	private JCBaseActivity mActivity;
	private LayoutInflater inflater;
	private SectionListAdapter sectionAdapter;

	public JZJQSCLass(JCBaseActivity mActivity, LayoutInflater inflater,SectionListAdapter sectionAdapter) {
		this.mActivity = mActivity;
		this.inflater = inflater;
		this.sectionAdapter=sectionAdapter;
	}

	public JZJQSCLass(JCBaseActivity mActivity, LayoutInflater inflater) {
		this.mActivity = mActivity;
		this.inflater = inflater;
	}

	@Override
	public void showItems(ImageLoader imageLoader, final JZMatchBean bean, LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.bd_jqs_item, null);
		parent_layout.addView(convertView);

		TextView end_time = (TextView) convertView.findViewById(R.id.end_time);
		TextView item_main = (TextView) convertView.findViewById(R.id.item_mian);
		TextView item_guest = (TextView) convertView.findViewById(R.id.item_guest);
		TextView league_name = (TextView) convertView.findViewById(R.id.league_name);
		final CheckBox[] item_selected = new CheckBox[8];
		item_selected[0] = (CheckBox) convertView.findViewById(R.id.item_jqs_0);
		item_selected[1] = (CheckBox) convertView.findViewById(R.id.item_jqs_1);
		item_selected[2] = (CheckBox) convertView.findViewById(R.id.item_jqs_2);
		item_selected[3] = (CheckBox) convertView.findViewById(R.id.item_jqs_3);
		item_selected[4] = (CheckBox) convertView.findViewById(R.id.item_jqs_4);
		item_selected[5] = (CheckBox) convertView.findViewById(R.id.item_jqs_5);
		item_selected[6] = (CheckBox) convertView.findViewById(R.id.item_jqs_6);
		item_selected[7] = (CheckBox) convertView.findViewById(R.id.item_jqs_7);
		
		TextView aSpS = (TextView) convertView.findViewById(R.id.item_as);
		TextView aSpP = (TextView) convertView.findViewById(R.id.item_ap);
		TextView aSpF = (TextView) convertView.findViewById(R.id.item_af);

		end_time.setText( bean.getSn() + " " + TimeUtils.customFormatDate(bean.getEndFuShiTime(), TimeUtils.DateFormat, TimeUtils.MinuteFormat));
		item_main.setText( bean.getMainTeam());
		item_guest.setText( bean.getGuestTeam());
		league_name.setText(bean.getMatchName());
		setAP(bean.getAvgOdds(),aSpS,aSpP,aSpF);//均赔
		
		String[] sp = bean.getSp().split("#");
		for(int i = 0; i < 8; i++){
			final int ind = i;
			
			String s = (ind == 7? "7+": ind) + " " + sp[ind];
			SpannableString spa = new SpannableString(s);
			spa.setSpan(new AbsoluteSizeSpan(mActivity.getResources().getDimensionPixelSize(R.dimen.small_l)), s.lastIndexOf(" "), s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			item_selected[i].setText(spa);
			item_selected[i].setChecked(false);
			
			item_selected[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setResult(bean, item_selected, item_selected[ind].isChecked(), ind);
				}
			});
		}
		
		for(String s: bean.selectedList){
//			int ind = Integer.parseInt(s);
			item_selected[Integer.parseInt(s)].setChecked(true);
		}
	}


	@Override
	public void showItems(JZMatchBean bean, LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.bdrq_result_item, null);
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
			result.setText( String.valueOf(main + guest));
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

	/**点击选项处理操作
	 * @param bean
	 * @param result,3 1 0
	 * @param isAdd
	 */
	public void setResult(JZMatchBean bean, CheckBox[] item_selected, boolean isAdd,int pos){
		if(isAdd){
			if(bean.count == 0){
				JZBetActivity.selectNumber++;
			}
			bean.selectedList.add(String.valueOf(pos));
			bean.selectedPvList.add(bean.pvList[pos]!=null?bean.pvList[pos]:"");
			bean.count++;
		}else{
			bean.selectedList.remove(String.valueOf(pos));
			bean.selectedPvList.remove(bean.pvList[pos]!=null?bean.pvList[pos]:"");
			bean.count--;
			if(bean.count == 0){
				JZBetActivity.selectNumber--;
				bean.setHasDan(false);
			}
		}
		StringBuilder sb = new StringBuilder();
		for(String s: bean.selectedList){
			sb.append(JCBFUtils.getJZZJQSName(Integer.parseInt(s)));
			sb.append(",");
		}
		if(sb.length() > 0) 
			sb.deleteCharAt(sb.length() - 1);
		
		bean.setNumstr(sb.toString());
		mActivity.setChangCi(JZBetActivity.selectNumber);
	}

	/**
	 * 设置均赔
	 * 
	 * @param ap
	 * @param spS
	 * @param spP
	 * @param spF
	 */
	public static void setAP(String ap, TextView spS, TextView spP, TextView spF) {
		try {
			String aps[] = ap.split("#");
			int j = aps.length;
			if (j >= 3) {
				for (int i = 0; i < j; i++) {
					if (i == 0)
						spS.setText(aps[0]);
					if (i == 1)
						spP.setText(aps[1]);
					if (i == 2)
						spF.setText(aps[2]);
				}
			}
		} catch (Exception e) {
		}
	}

}
