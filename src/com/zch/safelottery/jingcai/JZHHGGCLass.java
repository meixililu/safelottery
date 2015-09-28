package com.zch.safelottery.jingcai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.dialogs.HHGGAllItemDialog;
import com.zch.safelottery.jingcai.JZBetActivity.SectionListAdapter;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.util.JZHHGGViewUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.TimeUtils;

public class JZHHGGCLass extends JCAbstractClass {
	
	private JCBaseActivity mActivity;
	private LayoutInflater inflater;
	private SectionListAdapter sectionAdapter;
	private HHGGAllItemDialog  mDialog;

	public JZHHGGCLass(JCBaseActivity mActivity, LayoutInflater inflater, SectionListAdapter sectionAdapter) {
		this.mActivity = mActivity;
		this.inflater = inflater;
		this.sectionAdapter=sectionAdapter;
	}

	@Override
	public void showItems(ImageLoader imageLoader, final JZMatchBean bean, LinearLayout parent_layout) {
		try {
			View convertView = inflater.inflate(R.layout.bd_hhgg_item, null);
			parent_layout.addView(convertView);
			TextView league_name = (TextView) convertView.findViewById(R.id.league_name);
			TextView end_time = (TextView) convertView.findViewById(R.id.end_time);
			TextView item_main = (TextView) convertView.findViewById(R.id.item_mian);
			TextView item_guest = (TextView) convertView.findViewById(R.id.item_guest);
			LinearLayout spf_layout = (LinearLayout) convertView.findViewById(R.id.spf_layout);
			LinearLayout rqspf_layout = (LinearLayout) convertView.findViewById(R.id.rqspf_layout);
			TextView aSpS = (TextView) convertView.findViewById(R.id.item_as);
			TextView aSpP = (TextView) convertView.findViewById(R.id.item_ap);
			TextView aSpF = (TextView) convertView.findViewById(R.id.item_af);
			FrameLayout all_layout = (FrameLayout) convertView.findViewById(R.id.item_hhgg_all_layout);
			CheckBox all_cb = (CheckBox) convertView.findViewById(R.id.item_hhgg_all);
			
			league_name.setText( bean.getMatchName() );
			end_time.setText(  bean.getSn() + " " + TimeUtils.customFormatDate(bean.getEndFuShiTime(), TimeUtils.DateFormat, TimeUtils.MinuteFormat));
			item_main.setText( JZHHGGViewUtil.getSpannableString(mActivity, bean.getMainTeam(), bean.getLetBall()));
			item_guest.setText( bean.getGuestTeam() );
			
			final MyOnClickListener spfOnClickListener = new MyOnClickListener(bean,JZActivity.WF_SPF,all_cb);
			final MyOnClickListener rqspfOnClickListener = new MyOnClickListener(bean,JZActivity.WF_RQSPF,all_cb);
			JZHHGGViewUtil.getJZtableSingleLine(mActivity, spf_layout, spfOnClickListener, bean.getSpfJcArray(), 
					JZActivity.WF_SPF,3,bean.spfSelectedItem);
			JZHHGGViewUtil.getJZtableSingleLine(mActivity, rqspf_layout, rqspfOnClickListener, bean.getRqspfJcArray(), 
					JZActivity.WF_RQSPF,3,bean.rqspfSelectedItem);
			setAP(bean.getAvgOdds(),aSpS,aSpP,aSpF);//均赔
			if(bean.isShowAllBtnChecked()){
				all_cb.setText("已选\n"+bean.count+"项");
				all_cb.setChecked(true);
			}else{
				all_cb.setText("展开\n全部");
				all_cb.setChecked(false);
			}
			
			all_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mDialog==null || !mDialog.isShowing()) {
						mDialog  = new HHGGAllItemDialog(mActivity,JZHHGGCLass.this,sectionAdapter,bean);
						mDialog.setSpfListenerAndRqspfListener(spfOnClickListener, rqspfOnClickListener);
						mDialog.show();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class MyOnClickListener implements View.OnClickListener {
		private JZMatchBean bean;
		private String playMethod;
		private CheckBox all_cb;
		public MyOnClickListener(JZMatchBean bean,String playMethod,CheckBox all_cb){
			this.bean = bean;
			this.all_cb = all_cb;
			this.playMethod = playMethod;
		}
		public MyOnClickListener(JZMatchBean bean,String playMethod){
			this.bean = bean;
			this.playMethod = playMethod;
		}
		@Override
		public void onClick(View view) {
			CheckBox cb = (CheckBox)view;
			Integer id = (Integer) cb.getTag();
			LogUtil.DefalutLog("onClick-id:"+id);
			if(cb.isChecked()){
				if(bean.count == 0){
					JZBetActivity.selectNumber++;
				}
				if(playMethod.equals(JZActivity.WF_SPF)){
					bean.spfSelectedItem.add(id);
				}else if(playMethod.equals(JZActivity.WF_RQSPF)){
					bean.rqspfSelectedItem.add(id);
				}else if(playMethod.equals(JZActivity.WF_QCBF)){
					bean.qcbfSelectedItem.add(id);
				}else if(playMethod.equals(JZActivity.WF_JQS)){
					bean.zjqsSelectedItem.add(id);
				}else if(playMethod.equals(JZActivity.WF_BQC)){
					bean.bqcSelectedItem.add(id);
				}
				bean.count++;
			}else{
				if(playMethod.equals(JZActivity.WF_SPF)){
					bean.spfSelectedItem.remove(id);
				}else if(playMethod.equals(JZActivity.WF_RQSPF)){
					bean.rqspfSelectedItem.remove(id);
				}else if(playMethod.equals(JZActivity.WF_QCBF)){
					bean.qcbfSelectedItem.remove(id);
				}else if(playMethod.equals(JZActivity.WF_JQS)){
					bean.zjqsSelectedItem.remove(id);
				}else if(playMethod.equals(JZActivity.WF_BQC)){
					bean.bqcSelectedItem.remove(id);
				}
				bean.count--;
				if(bean.count == 0){
					JZBetActivity.selectNumber--;
					bean.setHasDan(false);
				}
			}
			if(all_cb != null && all_cb.isChecked()){
				all_cb.setText("已选\n"+bean.count+"项");
			}
			mActivity.setChangCi(JZBetActivity.selectNumber);
		}
	};
	
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

	@Override
	public void showItems(JZMatchBean bean, LinearLayout parent_layout) {
		// TODO Auto-generated method stub
		
	}

}
