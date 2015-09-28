package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.jingcai.JCBaseActivity;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.jingcai.JZBetActivity;
import com.zch.safelottery.jingcai.JZBetActivity.SectionListAdapter;
import com.zch.safelottery.jingcai.JZHHGGCLass;
import com.zch.safelottery.jingcai.JZHHGGCLass.MyOnClickListener;
import com.zch.safelottery.util.JZHHGGViewUtil;
import com.zch.safelottery.util.LogUtil;

/**自定义退出dialog
 * @author Messi
 *
 */
public class HHGGAllItemDialog extends Dialog {
	
	private JCBaseActivity mActivity;
	private SectionListAdapter sectionAdapter;
	private JZMatchBean bean;
	private MyOnClickListener spfOnClickListener;
	private MyOnClickListener rqspfOnClickListener;
	private MyOnClickListener qcbfOnClickListener;
	private MyOnClickListener zjqsOnClickListener;
	private MyOnClickListener bqcOnClickListener;
	
	private LinearLayout spf_layout;
	private LinearLayout rqspf_layout;
	private LinearLayout qcbf_layout;
	private LinearLayout zjqs_layout;
	private LinearLayout bqc_layout;

	public HHGGAllItemDialog(JCBaseActivity mActivity,JZHHGGCLass mJZHHGGCLass, SectionListAdapter sectionAdapter,
			JZMatchBean bean) {
		super(mActivity, R.style.dialog);
		this.mActivity = mActivity;
		this.sectionAdapter = sectionAdapter;
		this.bean = bean;
		qcbfOnClickListener = mJZHHGGCLass.new MyOnClickListener(bean,JZActivity.WF_QCBF);
		zjqsOnClickListener = mJZHHGGCLass.new MyOnClickListener(bean,JZActivity.WF_JQS);
		bqcOnClickListener = mJZHHGGCLass.new MyOnClickListener(bean,JZActivity.WF_BQC);
		LogUtil.DefalutLog(bean);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    try {
			setContentView(R.layout.jz_hhgg_all_item_dialog);
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void init() throws Exception {
		TextView item_main = (TextView) findViewById(R.id.item_mian);
		TextView item_guest = (TextView) findViewById(R.id.item_guest);
		spf_layout = (LinearLayout) findViewById(R.id.spf_layout);
		rqspf_layout = (LinearLayout) findViewById(R.id.rqspf_layout);
		qcbf_layout = (LinearLayout) findViewById(R.id.qcbf_layout);
		zjqs_layout = (LinearLayout) findViewById(R.id.zjqs_layout);
		bqc_layout = (LinearLayout) findViewById(R.id.bqc_layout);
		FrameLayout btn_cancle = (FrameLayout) findViewById(R.id.jz_hhgg_dialog_btn_cancle);
		FrameLayout btn_submit = (FrameLayout) findViewById(R.id.jz_hhgg_dialog_btn_submit);
		
		item_main.setText( JZHHGGViewUtil.getSpannableString(mActivity, bean.getMainTeam(), bean.getLetBall()));
		item_guest.setText( bean.getGuestTeam() );
		
		JZHHGGViewUtil.getJZtableSingleLine(mActivity, spf_layout, spfOnClickListener, bean.getSpfJcArray(), 
				JZActivity.WF_SPF,3,bean.spfSelectedItem);
		JZHHGGViewUtil.getJZtableSingleLine(mActivity, rqspf_layout, rqspfOnClickListener, bean.getRqspfJcArray(), 
				JZActivity.WF_RQSPF,3,bean.rqspfSelectedItem);
		JZHHGGViewUtil.getJZtables(mActivity, qcbf_layout, qcbfOnClickListener, bean.getQcbfJcArray(), 
				JZActivity.WF_QCBF,7,bean.qcbfSelectedItem);
		JZHHGGViewUtil.getJZtables(mActivity, zjqs_layout, zjqsOnClickListener, bean.getZjqsJcArray(), 
				JZActivity.WF_JQS,4,bean.zjqsSelectedItem);
		JZHHGGViewUtil.getJZtables(mActivity, bqc_layout, bqcOnClickListener, bean.getBqcJcArray(), 
				JZActivity.WF_BQC,3,bean.bqcSelectedItem);
		
		btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(bean.count > 0){
					JZHHGGViewUtil.clearSelectedItem(spf_layout);
					JZHHGGViewUtil.clearSelectedItem(rqspf_layout);
					JZHHGGViewUtil.clearSelectedItem(qcbf_layout);
					JZHHGGViewUtil.clearSelectedItem(zjqs_layout);
					JZHHGGViewUtil.clearSelectedItem(bqc_layout);
					bean.clear();
					JZBetActivity.selectNumber--;
					bean.setHasDan(false);
					mActivity.setChangCi(JZBetActivity.selectNumber);
				}
			}
		});
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sectionAdapter.notifyDataSetChanged();
				HHGGAllItemDialog.this.dismiss();
			}
		});
	}
	
	public void setSpfListenerAndRqspfListener(MyOnClickListener spfOnClickListener,MyOnClickListener rqspfOnClickListener){
		this.spfOnClickListener = spfOnClickListener;
		this. rqspfOnClickListener = rqspfOnClickListener;
	}
}
