package com.zch.safelottery.jingcai;

import java.util.ArrayList;

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
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.TimeUtils;

public class JZRQCLass extends JCAbstractClass {
	
	private JCBaseActivity mActivity;
	private LayoutInflater inflater;

	public JZRQCLass(JCBaseActivity mActivity, LayoutInflater inflater) {
		this.mActivity = mActivity;
		this.inflater = inflater;
	}

	@Override
	public void showItems(ImageLoader imageLoader, final JZMatchBean bean,
			LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.bd_rangqiu_item, null);
		parent_layout.addView(convertView);
		
		TextView end_time = (TextView) convertView.findViewById(R.id.end_time);
		TextView item_main = (TextView) convertView.findViewById(R.id.item_mian);
		TextView item_guest = (TextView) convertView.findViewById(R.id.item_guest);
		TextView league_name = (TextView) convertView.findViewById(R.id.league_name);
		final CheckBox spS = (CheckBox) convertView.findViewById(R.id.item_sps);
		final CheckBox spP = (CheckBox) convertView.findViewById(R.id.item_spp);
		final CheckBox spF = (CheckBox) convertView.findViewById(R.id.item_spf);
		TextView aSpS = (TextView) convertView.findViewById(R.id.item_as);
		TextView aSpP = (TextView) convertView.findViewById(R.id.item_ap);
		TextView aSpF = (TextView) convertView.findViewById(R.id.item_af);
		
		int letBall = ConversionUtil.StringToInt(bean.getLetBall());
		if(letBall == 0){
			item_main.setText( bean.getMainTeam()); 
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append(bean.getMainTeam());
			sb.append("(");
			sb.append(bean.getLetBall());
			sb.append(")");
			SpannableString msp = new SpannableString(sb.toString());
			//从pos起到end的区域变为红色
			if(letBall < 0)
				msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.green_2ca403)), sb.indexOf("("), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			else
				msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.red_e54f46)), sb.indexOf("("), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			item_main.setText( msp);
		}
		item_guest.setText( bean.getGuestTeam() ); 
		end_time.setText(  bean.getSn() + " " + TimeUtils.customFormatDate(bean.getEndFuShiTime(), TimeUtils.DateFormat, TimeUtils.MinuteFormat));
		league_name.setText( bean.getMatchName() );
		setSP(bean.getSp(),bean.selectedList,spS,spP,spF);
		setAP(bean.getAvgOdds(),aSpS,aSpP,aSpF);//均赔
		
		spS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(bean,"3",spS.isChecked(),0);
			}
		});
		spP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(bean,"1",spP.isChecked(),1);
			}
		});
		spF.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(bean,"0",spF.isChecked(),2);
			}
		});
	}
	
	@Override
	public void showItems(final JZMatchBean bean, LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.bdrq_result_item, null);
		TextView matchName = (TextView) convertView.findViewById(R.id.result_match_name);
		TextView noDate = (TextView) convertView.findViewById(R.id.result_no_date);
		TextView teamMain = (TextView) convertView.findViewById(R.id.result_team_main);
		TextView teamGuest = (TextView) convertView.findViewById(R.id.result_team_guest);
		TextView result = (TextView) convertView.findViewById(R.id.result_result);
		TextView pointScore = (TextView) convertView.findViewById(R.id.result_point_score);
		
		matchName.setText(bean.getMatchName());
		noDate.setText(bean.getWeek() + " " + bean.getSn());
		
		int letBall = ConversionUtil.StringToInt(bean.getLetBall());
		if(letBall == 0){
			teamMain.setText( bean.getMainTeam().trim()); 
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append(bean.getMainTeam());
			sb.append("(");
			sb.append(bean.getLetBall());
			sb.append(")");
			SpannableString msp = new SpannableString(sb.toString());
			//从pos起到end的区域变为红色
			if(letBall < 0)
				msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.green_2ca403)), sb.indexOf("("), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			else
				msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.red_e54f46)), sb.indexOf("("), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			teamMain.setText( msp);
		}
		teamGuest.setText(bean.getGuestTeam().trim());
		

		String MainTeamScore = bean.getMainTeamScore();
		String GuestTeamScore = bean.getGuestTeamScore();
		if(TextUtils.isEmpty(MainTeamScore) || TextUtils.isEmpty(GuestTeamScore)){
			pointScore.setText( "-" );
			result.setText("-");
		}else{
			int main = ConversionUtil.StringToInt(MainTeamScore);
			int guest = ConversionUtil.StringToInt(GuestTeamScore);
			main += letBall;
			pointScore.setText( MainTeamScore + ":" + GuestTeamScore );
			if(main > guest){
				result.setText( "胜");
				pointScore.setTextColor(mActivity.getResources().getColor(R.color.red_e54f46));
				result.setTextColor(mActivity.getResources().getColor(R.color.red_e54f46));
			}else if(main == guest){
				result.setText( "平");
				pointScore.setTextColor(mActivity.getResources().getColor(R.color.text_blue));
				result.setTextColor(mActivity.getResources().getColor(R.color.text_blue));
			}else{
				result.setText( "负");
				pointScore.setTextColor(mActivity.getResources().getColor(R.color.green_2ca403));
				result.setTextColor(mActivity.getResources().getColor(R.color.green_2ca403));
			}
		}
	
		parent_layout.addView(convertView);
	}

	/**设置胜平负选项
	 * @param sp
	 * @param selected
	 * @param spS
	 * @param spP
	 * @param spF
	 */
	public static void setSP(String sp,ArrayList<String> selectedList,CheckBox spS,CheckBox spP,CheckBox spF){
		try{
			String sps[] = sp.split("#");
			int j = sps.length;
			if(j == 3){
				spS.setText( "胜 "+sps[0] );
				spP.setText( "平 "+sps[1] );
				spF.setText( "负 "+sps[2] );
			}
			spS.setChecked(false);
			spP.setChecked(false);
			spF.setChecked(false);
			for (String secString : selectedList) {
				if(secString.equals("3")) 
					spS.setChecked(true);
				
				if(secString.equals("1")) 
					spP.setChecked(true);
				
				if(secString.equals("0")) 
					spF.setChecked(true);
			}
		}catch (Exception e) {
		}
	}
	
	/**设置均赔
	 * @param ap
	 * @param spS
	 * @param spP
	 * @param spF
	 */
	public static void setAP(String ap,TextView spS,TextView spP,TextView spF){
		try{
			String aps[] = ap.split("#");
			int j = aps.length;
			if(j >= 3){
				for (int i=0;i<j;i++) {
					if(i == 0) spS.setText(aps[0]);
					if(i == 1) spP.setText(aps[1]);
					if(i == 2) spF.setText(aps[2]);
				}
			}
		}catch (Exception e) {
		}
	}
	
	/**点击选项处理操作
	 * @param bean
	 * @param result,3 1 0
	 * @param isAdd
	 */
	public void setResult(JZMatchBean bean, String result, boolean isAdd,int pos){
		if(isAdd){
			if(bean.count == 0){
				JZBetActivity.selectNumber++;
			}
			bean.selectedList.add(result);
			bean.selectedPvList.add(bean.pvList[pos]!=null?bean.pvList[pos]:"");
			bean.count++;
		}else{
			bean.selectedList.remove(result);
			bean.selectedPvList.remove(bean.pvList[pos]!=null?bean.pvList[pos]:"");
			bean.count--;
			if(bean.count == 0){
				JZBetActivity.selectNumber--;
				bean.setHasDan(false);
			}
		}
		mActivity.setChangCi(JZBetActivity.selectNumber);
	}
	

}
