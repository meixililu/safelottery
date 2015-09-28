package com.zch.safelottery.jingcai;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.TimeUtils;

public class JLSFCLass extends JCAbstractClass {
	
	private JCBaseActivity mActivity;
	private LayoutInflater inflater;
	private String playMethod;
	private boolean isRangfen = true;
	
	public JLSFCLass(JCBaseActivity mActivity, LayoutInflater inflater, String playMethod) {
		this.mActivity = mActivity;
		this.inflater = inflater;
		this.playMethod = playMethod;
	}
	public JLSFCLass(JCBaseActivity mActivity, LayoutInflater inflater, String playMethod, boolean isRangfen) {
		this.mActivity = mActivity;
		this.inflater = inflater;
		this.playMethod = playMethod;
		this.isRangfen = isRangfen;
	}

	@Override
	public void showItems(ImageLoader imageLoader, final JZMatchBean bean,
			LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.jl_sf_items, null);
		parent_layout.addView(convertView);
		
		TextView league_name = (TextView) convertView.findViewById(R.id.league_name);
		TextView end_time = (TextView) convertView.findViewById(R.id.end_time);
		TextView item_main = (TextView) convertView.findViewById(R.id.item_mian);
		TextView item_guest = (TextView) convertView.findViewById(R.id.item_guest);
		TextView item_left = (TextView) convertView.findViewById(R.id.item_left);
		ImageView item_left_line = (ImageView) convertView.findViewById(R.id.item_left_line);
		final CheckBox spS = (CheckBox) convertView.findViewById(R.id.item_sps);
		final CheckBox spF = (CheckBox) convertView.findViewById(R.id.item_spf);
		TextView aSpS = (TextView) convertView.findViewById(R.id.item_as);
		TextView aSpF = (TextView) convertView.findViewById(R.id.item_af);
		
		league_name.setText(bean.getMatchName());
		end_time.setText(  bean.getSn() + " " + TimeUtils.customFormatDate(bean.getEndFuShiTime(), TimeUtils.DateFormat, TimeUtils.MinuteFormat));
		
		item_main.setText(bean.getMainTeam());
		item_guest.setText(bean.getGuestTeam());
		
		checkText(bean, spS, spF);
		setAP(bean.getAvgOdds(), aSpS, aSpF);//均赔
		
		if(isRangfen){
			if(playMethod.equals(JLActivity.WF_RFSF)){
				item_left.setText(bean.getLetBall());
			}else if(playMethod.equals(JLActivity.WF_DXF)){
				item_left.setText( getPreCast(bean.getPreCast()) );
			}
		}else{
			item_left.setVisibility(View.GONE);
			item_left_line.setVisibility(View.GONE);
		}
		
		spS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(bean,"1",spS.isChecked(),0);
			}
		});
		spF.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(bean,"2",spF.isChecked(),1);
			}
		});
	}
	
	private String getPreCast(String preCast){
		if(preCast.contains(".")){
			int pos = preCast.indexOf(".");
			int len = preCast.length();
			if(pos > 0 && (len-pos-1)>=2){
				return preCast.substring( 0, (pos+2) );
			}
		}
		return preCast;
	}
	
	@Override
	public void showItems(final JZMatchBean bean, LinearLayout parent_layout) {
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
		int letBall = ConversionUtil.StringToInt(bean.getLetBall());
		if(TextUtils.isEmpty(MainTeamScore) || TextUtils.isEmpty(GuestTeamScore)){
			pointScore.setText( "-" );
			result.setText("-");
		}else{
			int main = ConversionUtil.StringToInt(MainTeamScore);
			int guest = ConversionUtil.StringToInt(GuestTeamScore);
			main += letBall;
			pointScore.setText( MainTeamScore + ":" + GuestTeamScore );
			if(playMethod.equals(JLActivity.WF_SF)){
				if(main > guest){
					result.setText("主胜");
				}else if(main == guest){
					result.setText("主平");
				}else{
					result.setText("主负");
				}
			}else if(playMethod.equals(JLActivity.WF_RFSF)){
				result.setText(bean.getRfshResult());
			}else if(playMethod.equals(JLActivity.WF_DXF)){
				result.setText(bean.getDxfResult() + "," + bean.getPreCast());
			}
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
	
	/**设置均赔
	 * @param ap
	 * @param spS
	 * @param spP
	 * @param spF
	 */
	public static void setAP(String ap,TextView spS, TextView spF){
		try{
			String aps[] = ap.split("#");
			int j = aps.length;
			if(j >= 2){
				for (int i=0;i<j;i++) {
					if(i == 0) spS.setText(aps[0]);
					if(i == 1) spF.setText(aps[1]);
				}
			}
		}catch (Exception e) {
		}
	}
	
	private void checkText(JZMatchBean bean, CheckBox homeCheck, CheckBox guestCheck){
		String[] spGroup = bean.getSp().split("#");
		if(playMethod.equals(JLActivity.WF_SF) || playMethod.equals(JLActivity.WF_RFSF)){
			homeCheck.setText( "主胜 " + spGroup[0] ); 
			guestCheck.setText( "主负 " + spGroup[1] ); 
		}else if(playMethod.equals(JLActivity.WF_DXF)){
			homeCheck.setText( "大 " + spGroup[0] ); 
			guestCheck.setText( "小 " + spGroup[1] ); 
		}
		
		for( String temp: bean.getSelectedList()){
			if(temp.equals("1")){
				homeCheck.setChecked(true);
			}
			if(temp.equals("2")){
				guestCheck.setChecked(true);
			}
		}
		
	}
	
	/**点击选项处理操作
	 * @param bean
	 * @param result
	 * @param isAdd
	 */
	public void setResult(JZMatchBean bean, String result, boolean isAdd,int pos){
		if(isAdd){
			if(bean.count == 0){
				JLBetActivity.selectNumber++;
			}
			bean.selectedList.add(result);
			bean.selectedPvList.add(bean.pvList[pos]!=null?bean.pvList[pos]:"");
			bean.count++;
		}else{
			bean.selectedList.remove(result);
			bean.selectedPvList.remove(bean.pvList[pos]!=null?bean.pvList[pos]:"");
			bean.count--;
			if(bean.count == 0){
				JLBetActivity.selectNumber--;
				bean.setHasDan(false);
			}
		}
		mActivity.setChangCi(JLBetActivity.selectNumber);
	}
	

}
