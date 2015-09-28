package com.zch.safelottery.jingcai;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.PublicTask;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.bean.JZMatchListBean;
import com.zch.safelottery.custom_control.AmazingAdapter;
import com.zch.safelottery.custom_control.AmazingListView;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JCFinishMatchParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;

public class JCFinishActivity extends JCBaseActivity {

	private final String[] JZ = {"胜平负", "让球胜平负", "全场比分", "总进球数", "半全场"};
//	private final String[] JL = {"胜负", "让分胜负", "胜分差", "大小分"};
	private String[] items;
	
	private LayoutInflater inflater;
	private View view;
	private AmazingListView sectionLV;

	private PublicTask mPublicTask;
	private ProgressBar mProgressBar;
	private TextView no_result_text;
	private TextView jc_header_section;
	private SectionListAdapter sectionAdapter;

	private String lid;
	private String playMethod;
	private String issue = "";
	private int rangqiu = 2;
	private String changci = "";
	private String status; // 0默认 1完成赛事
	private int currentPage = 1;
	private int perPageNum = 100;
	
	private ArrayList<JZMatchListBean> AllList;
	public ArrayList<Pair<String, ArrayList<JZMatchBean>>> all;
	public static int selectNumber = 0;//选择场次数
	private JCAbstractClass mJCItemClass;//
	
	private PopWindowDialog popDialog;

	private boolean isHasOnsaleData;//是否有可投注赛事
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			inflater = LayoutInflater.from(this);
			view = (View) inflater.inflate(R.layout.jingcai_football_activity, null);
			setcViewLinearlayout(view);
			
			initViews();
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		Intent intent = getIntent();
		issue = intent.getStringExtra("issue");
		lid =  intent.getStringExtra("lid");
		playMethod = intent.getStringExtra("playMethod");
		isHasOnsaleData = intent.getBooleanExtra("isChang", false);
		if( playMethod.equals(JZActivity.WF_HHGG) ){
			playMethod = JZActivity.WF_SPF;
		}
		status = "1";
		if(lid.equals(LotteryId.JCLQ)){
			items = JLBetActivity.items;
		}else{
			items = JZ;
		}
		top_menu_tv.setText(items[selectPlayMethodId(playMethod)]);
		getIssue();
	}
	
	/**
	 * 进算完场期次
	 * @param issue 当前的最新期次
	 * @return 完场期次集合
	 * @throws ParseException
	 */
	private void getIssue() {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			if(!TextUtils.isEmpty(issue)){
				cal.setTime(sdf.parse(issue));
			}else{
				cal.setTimeInMillis(System.currentTimeMillis());
			}
			AllList.clear();
			for(int i=0;i<5;i++){
				String issue = sdf.format(cal.getTime());
				cal.add(Calendar.DATE, -1);
				JZMatchListBean bean = new JZMatchListBean();
				bean.setDate(issue);
				bean.setSection( getSection(issue, 0) );
				AllList.add(bean);
				doRequestTask(0,issue);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void initViews() {
		baseBottomLayout.setVisibility(View.GONE);
		finishBtn.setVisibility(View.GONE);
		
		AllList = new ArrayList<JZMatchListBean>();
		all = new ArrayList<Pair<String, ArrayList<JZMatchBean>>>();
		sectionLV = (AmazingListView) view.findViewById(R.id.section_list_view);
		mProgressBar = (ProgressBar) view.findViewById(R.id.loadding_progressbar_m);
		no_result_text = (TextView) view.findViewById(R.id.no_result_text);
		jc_header_section = (TextView)view.findViewById(R.id.jc_header_section);
		
		MyClickListenre mMyClickListenre = new MyClickListenre();
		jc_header_section.setOnClickListener(mMyClickListenre);
		
		View sectionHeaderView = inflater.inflate(R.layout.jingcai_section_header, sectionLV, false);
		sectionLV.setPinnedHeaderView(sectionHeaderView);
		sectionAdapter = new SectionListAdapter();
		sectionLV.setAdapter(sectionAdapter);
	}

	private void doRequestTask(final int position,final String requestIssue) {
		
		HashMap<String, String> postMap = new HashMap<String, String>();
		postMap.put("lotteryId", lid);
		postMap.put("issue", requestIssue);
		SafelotteryHttpClient.post(this, "3302", "history", JsonUtils.toJsonStr(postMap), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					String matchTotalNum = (String) mMap.get("matchTotal");
					String matchList = (String) mMap.get("matchList");
					ArrayList<JZMatchBean> jCMatchBeanList = (ArrayList<JZMatchBean>) JsonUtils.parserJsonArray(matchList,new JCFinishMatchParser());
					setData(requestIssue, jCMatchBeanList);
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
			
			@Override
			public void onStart(){
				mProgressBar.setVisibility(View.VISIBLE);
				no_result_text.setVisibility(View.GONE);
			}
			
			@Override
			public void onFinish(){
				mProgressBar.setVisibility(View.GONE);
				try {
					getSectionListItems(position);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private void setData(String currentIssue, ArrayList<JZMatchBean> tempList){
		for (JZMatchListBean bean : AllList) {
			if(bean.getDate().equals(currentIssue)){
				if(tempList != null){
					bean.setSection( getSection(currentIssue,tempList.size()) );
					bean.setMatchList(tempList);
					bean.setStatus(1);
				}
			}
		}
	}
	
	/**更改列表显示的数据
	 * @param position
	 */
	private void getSectionListItems(int position){
		all.clear();
		for (JZMatchListBean bean : AllList) {
			ArrayList<JZMatchBean> mJCBDBeanList = bean.getMatchList();
			String sectionString = bean.getSection();
			int size = mJCBDBeanList.size();
			if(size > 0){
				all.add( new Pair<String, ArrayList<JZMatchBean>>(sectionString,mJCBDBeanList) );
			}else{
				mJCBDBeanList.add(new JZMatchBean());
				all.add( new Pair<String, ArrayList<JZMatchBean>>( sectionString,mJCBDBeanList) );
			}
		}
		sectionAdapter.notifyDataSetChanged();
		sectionLV.setSelection(position);
	}
	
	/**section文字拼接
	 * @param currentIssue
	 * @param changCiSum
	 * @return
	 */
	private String getSection(String currentIssue, int changCiSum){
		String week = TimeUtils.getWeek(currentIssue);
		return week + "\t 已结束" + changCiSum + "场";
	}
	
	class SectionListAdapter extends AmazingAdapter {
		
		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < all.size(); i++) {
				res += all.get(i).second.size();
			}
			return res;
		}

		@Override
		public JZMatchBean getItem(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return all.get(i).second.get(position - c);
				}
				c += all.get(i).second.size();
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		protected void onNextPageRequested(int page) {
		}

		/**绑定section，处理事件**/
		@Override
		protected void bindSectionHeader(View view, final int position, boolean displaySectionHeader) {
			LinearLayout parentLayout = (LinearLayout) view.findViewById(R.id.section_parent_layout);
			if (displaySectionHeader) {
				parentLayout.setVisibility(View.VISIBLE);
				final String sectionString = getSections()[getSectionForPosition(position)];
				TextView lSectionTitle = (TextView) view.findViewById(R.id.header_section);
				CheckBox section_expend_cb = (CheckBox) view.findViewById(R.id.section_expend_cb);
				lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
				section_expend_cb.setChecked( isSectionChecked(sectionString) );
				section_expend_cb.setClickable(false);
				parentLayout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onClickSection(sectionString,position);
					}
				});
			} else {
				parentLayout.setVisibility(View.GONE);
			}
		}
		
		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			final String sectionString = getSections()[getSectionForPosition(position)];
			LogUtil.DefalutLog("position:"+position+"--"+sectionString);
			TextView header_section = (TextView)header.findViewById(R.id.header_section);
			CheckBox section_expend_cb = (CheckBox)header.findViewById(R.id.section_expend_cb);
			header_section.setText( sectionString );
			section_expend_cb.setChecked( isSectionChecked(sectionString) );
			jc_header_section.setText( sectionString );
		}

		@Override
		public View getAmazingView(int position, View convertView, ViewGroup parent) {
			LogUtil.DefalutLog(""+convertView);
			View res = convertView;
			JZMatchBean mJCBDBean = getItem(position);
			if (res == null) res = inflater.inflate(R.layout.jingcai_section_layout, null);
			LinearLayout parent_layout = (LinearLayout) res.findViewById(R.id.parent_layout);

			if( TextUtils.isEmpty(mJCBDBean.getSn()) ){
				parent_layout.setVisibility(View.GONE);
			}else{
				parent_layout.setVisibility(View.VISIBLE);
				parent_layout.removeAllViews();
				showItems(mJCBDBean, parent_layout);
			}
			
			return res;
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0) section = 0;
			if (section >= all.size()) section = all.size() - 1;
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (section == i) { 
					return c;
				}
				c += all.get(i).second.size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return i;
				}
				c += all.get(i).second.size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			String[] res = new String[all.size()];
			for (int i = 0; i < all.size(); i++) {
				res[i] = all.get(i).first;
			}
			return res;
		}
	}
	
	/**显示数据
	 * @param bean
	 * @param parent_layout
	 */
	private void showItems(final JZMatchBean bean,LinearLayout parent_layout){
		if(lid.equals(LotteryId.JCZQ)){
			if(playMethod.equals(JZActivity.WF_RQSPF)){
				if(!(mJCItemClass instanceof JZRQCLass))
					mJCItemClass = new JZRQCLass(this, inflater);
				mJCItemClass.showItems( bean, parent_layout );
			}else if(playMethod.equals(JZActivity.WF_SPF)){
				if(!(mJCItemClass instanceof JZSPFCLass))
					mJCItemClass = new JZSPFCLass(this, inflater);
				mJCItemClass.showItems( bean, parent_layout );
			}else if(playMethod.equals(JZActivity.WF_QCBF)){
				if(!(mJCItemClass instanceof JZQCBFCLass))
					mJCItemClass = new JZQCBFCLass(this, inflater);
				mJCItemClass.showItems( bean, parent_layout );
			}else if(playMethod.equals(JZActivity.WF_JQS)){
				if(!(mJCItemClass instanceof JZJQSCLass))
					mJCItemClass = new JZJQSCLass(this, inflater);
				mJCItemClass.showItems( bean, parent_layout );
			}else if(playMethod.equals(JZActivity.WF_BQC)){
				if(!(mJCItemClass instanceof JZBQCCLass))
					mJCItemClass = new JZBQCCLass(this, inflater);
				mJCItemClass.showItems( bean, parent_layout );
			}
		}else if(lid.equals(LotteryId.JCLQ)){
			if (playMethod.equals(JLActivity.WF_SF) || playMethod.equals(JLActivity.WF_RFSF) || playMethod.equals(JLActivity.WF_DXF)) {
					mJCItemClass = new JLSFCLass(this, inflater, playMethod);
				mJCItemClass.showItems(bean, parent_layout);
			} else if (playMethod.equals(JLActivity.WF_SFC)) {
					mJCItemClass=new JLSFCCLass(this, inflater);
				mJCItemClass.showItems(bean, parent_layout);
			}
		}
	}
	
	/**点击section事件处理
	 * @param sectionString
	 * @param position
	 */
	private void onClickSection(String sectionString,int position){
		try {
			for (JZMatchListBean bean : AllList) {
				if(bean.getSection().equals(sectionString)){
					if(bean.getStatus() == 0){
						issue = bean.getDate();
						doRequestTask(position-1,issue);
					}else if(bean.getStatus() == 1){
						bean.setBackupList(bean.getMatchList());
						bean.setMatchList(new ArrayList<JZMatchBean>());
						getSectionListItems(position-1);
						bean.setStatus(2);
					}else if(bean.getStatus() == 2){
						bean.setMatchList(bean.getBackupList());
						bean.setBackupList(new ArrayList<JZMatchBean>());
						getSectionListItems(position-1);
						bean.setStatus(1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**点击事件
	 * @author Messi
	 *
	 */
	class MyClickListenre implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.jc_header_section://覆盖在section上面的层，完全透明的
				String sectionString = jc_header_section.getText().toString();
				onClickSection(sectionString,0);
				break;
				
			}
		}
		
	}
	
	/**section的checkbox状态操作
	 * @param sectionString
	 * @return
	 */
	private boolean isSectionChecked(String sectionString){
		for (JZMatchListBean bean : AllList) {
			if(bean.getSection().equals(sectionString)){
				if(bean.getStatus() == 1){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		selectNumber = 0;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void top_menu(View v) {
		super.top_menu(v);
		final Context mContext = this;
		if(popDialog == null){
			popDialog = new PopWindowDialog(mContext, items, selectPlayMethodId(playMethod));
			top_menu_tv.setText(items[selectPlayMethodId(playMethod)]);
			popDialog.setOnClickListener(new OnclickFrequentListener(){

				@Override
				public void onClick(View v) {
					for(CheckBox c: popDialog.getList()){
						if(c.getId() == v.getId()){
							c.setChecked(true);
						}else{
							c.setChecked(false);
						}
					}
					selectPlayMethod(items[v.getId()]);
					getIssue();
					popDialog.dismiss();
				}
			});
			popDialog.setPopViewPosition();
			popDialog.show();
		}else{
			popDialog.show();
		}
		
	}
	
	private String selectPlayMethod(String playMethodName){
		top_menu_tv.setText(playMethodName);
		if(lid.equals(LotteryId.JCZQ)){
			if(playMethodName.equals("胜平负")){
				playMethod = JZActivity.WF_SPF;
			}else if(playMethodName.equals("让球胜平负")){
				playMethod = JZActivity.WF_RQSPF;
			}else if(playMethodName.equals("全场比分")){
				playMethod = JZActivity.WF_QCBF;
			}else if(playMethodName.equals("总进球数")){
				playMethod = JZActivity.WF_JQS;
			}else if(playMethodName.equals("半全场")){
				playMethod = JZActivity.WF_BQC;
			}
		}else if(lid.equals(LotteryId.JCLQ)){
			if(playMethodName.equals("胜负")){
				playMethod = JLActivity.WF_SF;
			}else if(playMethodName.equals("让分胜负")){
				playMethod = JLActivity.WF_RFSF;
			}else if(playMethodName.equals("胜分差")){
				playMethod = JLActivity.WF_SFC;
			}else if(playMethodName.equals("大小分")){
				playMethod = JLActivity.WF_DXF;
			}
		}
		return playMethod;
	}
	
	private int selectPlayMethodId(String playMethodName){
		top_menu_tv.setText(playMethodName);
		if(lid.equals(LotteryId.JCZQ)){
			if(playMethodName.equals(JZActivity.WF_SPF)){
				return 0;
			}else if(playMethodName.equals(JZActivity.WF_RQSPF)){
				return 1;
			}else if(playMethodName.equals(JZActivity.WF_QCBF)){
				return 2;
			}else if(playMethodName.equals(JZActivity.WF_JQS)){
				return 3;
			}else if(playMethodName.equals(JZActivity.WF_BQC)){
				return 4;
			}
		}else if(lid.equals(LotteryId.JCLQ)){
			if(playMethodName.equals(JLActivity.WF_SF)){
				return 0;
			}else if(playMethodName.equals(JLActivity.WF_RFSF)){
				return 1;
			}else if(playMethodName.equals(JLActivity.WF_SFC)){
				return 2;
			}else if(playMethodName.equals(JLActivity.WF_DXF)){
				return 3;
			}
		}
		return 0;
	}
}
