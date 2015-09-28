package com.zch.safelottery.jingcai;

import java.util.ArrayList;
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
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.asynctask.PublicTask;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.bean.JZMatchListBean;
import com.zch.safelottery.custom_control.AmazingAdapter;
import com.zch.safelottery.custom_control.AmazingListView;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.parser.JZMatchListBeanParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;

public class JLBetActivity extends JCBaseActivity {

	public static String[] items = {"胜负", "让分胜负", "胜分差", "大小分"};
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
	private String GGFS;
	private String issue = "";
	private String issueNewest = "";
	private int currentPage = 1;
	private int requestCode = 1000;

	private ArrayList<JZMatchListBean> AllList;
	public ArrayList<Pair<String, ArrayList<JZMatchBean>>> all;
	private ImageLoader imageLoader;
	public static int selectNumber = 0;// 选择场次数
	private int danCount;// 选择场次数
	private JCAbstractClass mJlItemClass;
	
	private PopWindowDialog popDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			inflater = LayoutInflater.from(this);
			view = (View) inflater.inflate(R.layout.jingcai_football_activity, null);
			setcViewLinearlayout(view);
			initViews();
			init();
			doRequestTask();
		} catch (Exception e) {
			LogUtil.ExceptionLog("JLBetActivity-onCreate()");
			e.printStackTrace();
		}
	}
	/**
	 * 获取初始数据
	 */
	private void init() {
		Intent intent = getIntent();
		playMethod = intent.getStringExtra("playMethod");
		lid = intent.getStringExtra("lid");
		GGFS = "02";//过关方式，应该从上一界面传入
		top_menu_tv.setText(items[selectPlayMethodId(playMethod)]);
	}
	/**
	 * 初始化控件
	 */
	private void initViews() {
		AllList = new ArrayList<JZMatchListBean>();
		all = new ArrayList<Pair<String, ArrayList<JZMatchBean>>>();
		imageLoader = new ImageLoader(getApplicationContext(), false, 65);
		sectionLV = (AmazingListView) view.findViewById(R.id.section_list_view);
		mProgressBar = (ProgressBar) view.findViewById(R.id.loadding_progressbar_m);
		no_result_text = (TextView) view.findViewById(R.id.no_result_text);
		jc_header_section = (TextView) view.findViewById(R.id.jc_header_section);

		MyClickListenre mMyClickListenre = new MyClickListenre();
		jc_header_section.setOnClickListener(mMyClickListenre);

		View sectionHeaderView = inflater.inflate(R.layout.jingcai_section_header, sectionLV, false);
		sectionLV.setPinnedHeaderView(sectionHeaderView);
		sectionAdapter = new SectionListAdapter();
		sectionLV.setAdapter(sectionAdapter);
	}

	private void doRequestTask() {
		
		HashMap<String, String> postMap = new HashMap<String, String>();
		postMap.put("lotteryId", LotteryId.JCLQ);
		postMap.put("playId", playMethod);
		postMap.put("pollId", GGFS);
		postMap.put("issue", "");
		postMap.put("sn", "");
		postMap.put("page", "1");
		postMap.put("pageSize", "200");
		
		SafelotteryHttpClient.post(this, "3302", "", JsonUtils.toJsonStr(postMap), new TypeMapHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Map mMap) {
					if(mMap != null){
						String matchTotalNum = (String) mMap.get("matchTotal");
						String matchList = (String) mMap.get("matchList");
						try {
							 AllList = (ArrayList<JZMatchListBean>) JsonUtils.parserJsonArray(matchList,new JZMatchListBeanParser());
						   if(AllList != null){
							getSectionListItems(0);
						   }
						} catch (Exception e) {
							LogUtil.DefalutLog("JLBetActivity-Exception-onSuccess-parserJsonArray");
						}
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
				}
			});
		}
		
	/**
	 * 更改列表显示的数据
	 * 
	 * @param position
	 */
	private void getSectionListItems(int position) {
		all.clear();
		for (JZMatchListBean bean : AllList) {
			ArrayList<JZMatchBean> mJCBDBeanList = bean.getMatchList();
			String sectionString = bean.getSection();
			int size = mJCBDBeanList.size();
			if (size > 0) {
				all.add(new Pair<String, ArrayList<JZMatchBean>>(sectionString, mJCBDBeanList));
			} else {
				mJCBDBeanList.add(new JZMatchBean());
				all.add(new Pair<String, ArrayList<JZMatchBean>>(sectionString, mJCBDBeanList));
			}
		}
		sectionAdapter.notifyDataSetChanged();
		sectionLV.setSelection(position);
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

		/** 绑定section，处理事件 **/
		@Override
		protected void bindSectionHeader(View view, final int position, boolean displaySectionHeader) {
			LinearLayout parentLayout = (LinearLayout) view.findViewById(R.id.section_parent_layout);
			if (displaySectionHeader) {
				parentLayout.setVisibility(View.VISIBLE);
				final String sectionString = getSections()[getSectionForPosition(position)];
				TextView lSectionTitle = (TextView) view.findViewById(R.id.header_section);
				CheckBox section_expend_cb = (CheckBox) view.findViewById(R.id.section_expend_cb);
				lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
				section_expend_cb.setChecked(isSectionChecked(sectionString));
				section_expend_cb.setClickable(false);
				parentLayout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						onClickSection(sectionString, position);
					}
				});
			} else {
				parentLayout.setVisibility(View.GONE);
			}
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			final String sectionString = getSections()[getSectionForPosition(position)];
			TextView header_section = (TextView) header.findViewById(R.id.header_section);
			CheckBox section_expend_cb = (CheckBox) header.findViewById(R.id.section_expend_cb);
			header_section.setText(sectionString);
			section_expend_cb.setChecked(isSectionChecked(sectionString));
			jc_header_section.setText(sectionString);
		}

		@Override
		public View getAmazingView(int position, View convertView, ViewGroup parent) {
			View res = convertView;
			JZMatchBean mJCBDBean = getItem(position);
			if (res == null)
				res = inflater.inflate(R.layout.jingcai_section_layout, null);
			LinearLayout parent_layout = (LinearLayout) res.findViewById(R.id.parent_layout);

			if (TextUtils.isEmpty(mJCBDBean.getSn())) {
				parent_layout.setVisibility(View.GONE);
			} else {
				parent_layout.setVisibility(View.VISIBLE);
				parent_layout.removeAllViews();
				showItems(mJCBDBean, parent_layout);
			}

			return res;
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0)
				section = 0;
			if (section >= all.size())
				section = all.size() - 1;
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
	
	/**
	 * section的checkbox状态操作
	 * 状态：1为展开显示，2未展示
	 * @param sectionString
	 * @return
	 */
	private boolean isSectionChecked(String sectionString) {
		for (JZMatchListBean bean : AllList) {
			if (bean.getSection().equals(sectionString)) {
				if (bean.getStatus() == 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 显示数据
	 * 
	 * @param bean
	 * @param parent_layout
	 */
	private void showItems(final JZMatchBean bean, LinearLayout parent_layout) {
		if(playMethod.equals(JLActivity.WF_SF)){
//			if (!(mJlItemClass instanceof JLSFCLass)) {
				mJlItemClass = new JLSFCLass(this, inflater, playMethod, false);
//			}
			mJlItemClass.showItems(imageLoader, bean, parent_layout);
		}else if (playMethod.equals(JLActivity.WF_RFSF) || playMethod.equals(JLActivity.WF_DXF)) {
//			if (!(mJlItemClass instanceof JLSFCLass)) {
				mJlItemClass = new JLSFCLass(this, inflater, playMethod, true);
//			}
			mJlItemClass.showItems(imageLoader, bean, parent_layout);
		} else if (playMethod.equals(JLActivity.WF_SFC)) {
//			if (!(mJlItemClass instanceof JLSFCCLass)) {
				mJlItemClass=new JLSFCCLass(this, inflater,sectionAdapter);
//			}
			mJlItemClass.showItems(imageLoader, bean, parent_layout);
		}
	}

	/**
	 * 点击section事件处理
	 * 
	 * @param sectionString
	 * @param position
	 */
	private void onClickSection(String sectionString, int position) {
		try {
			for (JZMatchListBean bean : AllList) {
				if (bean.getSection().equals(sectionString)) {
					if (bean.getStatus() == 1) {
						bean.setBackupList(bean.getMatchList());
						bean.setMatchList(new ArrayList<JZMatchBean>());
						bean.setStatus(2);
						getSectionListItems(position - 1);
					} else if (bean.getStatus() == 2) {
						bean.setMatchList(bean.getBackupList());
						bean.setBackupList(new ArrayList<JZMatchBean>());
						bean.setStatus(1);
						getSectionListItems(position - 1);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.ExceptionLog("JZBetActivity-onClickSection()");
			e.printStackTrace();
		}
	}

	/**
	 * 点击事件
	 * 
	 * @author Messi
	 * 
	 */
	class MyClickListenre implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.jc_header_section:// 覆盖在section上面的层，完全透明的
				String sectionString = jc_header_section.getText().toString();
				onClickSection(sectionString, 0);
				break;

			}
		}

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
	protected void isNeedClose(int ationCode) {
		super.isNeedClose(ationCode);
		if (ationCode == Settings.EXIT_JCZQ) {
			finish();
		}
	}

	@Override
	public void bottom_clear(View v) {
		super.bottom_clear(v);
		if (selectNumber > 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(JLBetActivity.this);
			dialog.setTitle("提示");
			dialog.setContent("您确定要清空当前选择的场次吗？");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
				public void onOkBtnClick() {
					clearAllList();
					sectionAdapter.notifyDataSetChanged();
					selectNumber = 0;
					setChangCi(0);
				}

				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		} else {
			Toast.makeText(getApplicationContext(), "您还没选择任何场次", 1).show();
		}

	}

	@Override
	public void bottom_finish(View v) {
		super.bottom_finish(v);
		Intent intent = new Intent(this, JCFinishActivity.class);
		intent.putExtra("issue", issueNewest);
		intent.putExtra("playMethod", playMethod);
		intent.putExtra("lid", lid);
		startActivity(intent);
	}
	
	@Override
	public void bottom_submit(View v) {
		super.bottom_submit(v);
		if (selectNumber > 1) {
			SafeApplication.dataMap.put("selectedBean", selectedBean());
			Intent intent = new Intent(this, JCOrderListActivity.class);
			intent.putExtra("lid", lid);
			intent.putExtra("playMethod", playMethod);
			intent.putExtra("issue", issue);
			intent.putExtra("selectNumber", selectNumber);
			intent.putExtra("danCount", danCount);
			startActivityForResult(intent, requestCode);
		} else {
			ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择2场比赛");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(this.requestCode == requestCode && resultCode == RESULT_OK){
			sectionAdapter.notifyDataSetChanged();
			selectNumber=0;
			setChangCi(0);
		}
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
					doRequestTask();
					popDialog.dismiss();
				}
			});
			popDialog.setPopViewPosition();
			popDialog.show();
		}else{
			popDialog.show();
		}
		
	}

	private ArrayList<JZMatchBean> selectedBean() {
		danCount = 0;
		ArrayList<JZMatchBean> resultList = new ArrayList<JZMatchBean>();
		for (JZMatchListBean bean : AllList) {
			if (bean.getMatchList().size() > 0) {
				isSelected(bean.getMatchList(), resultList);
			} 
			if (bean.getBackupList().size() > 0) {
				isSelected(bean.getBackupList(), resultList);
			}
		}
		return resultList;
	}
	
	private int selectPlayMethodId(String playMethodName){
		if(playMethodName.equals(JLActivity.WF_SF)){
			return 0;
		}else if(playMethodName.equals(JLActivity.WF_RFSF)){
			return 1;
		}else if(playMethodName.equals(JLActivity.WF_SFC)){
			return 2;
		}else if(playMethodName.equals(JLActivity.WF_DXF)){
			return 3;
		}
		return 0;
	}
	
	private String selectPlayMethod(String playMethodName){
		top_menu_tv.setText(playMethodName);
		if(playMethodName.equals("胜负")){
			playMethod = JLActivity.WF_SF;
		}else if(playMethodName.equals("让分胜负")){
			playMethod = JLActivity.WF_RFSF;
		}else if(playMethodName.equals("胜分差")){
			playMethod = JLActivity.WF_SFC;
		}else if(playMethodName.equals("大小分")){
			playMethod = JLActivity.WF_DXF;
//		}else if(playMethodName.equals("混合过关")){
//			playMethod = JZActivity.WF_RQSPF;
		}
		return playMethod;
	}
	
	private void isSelected(ArrayList<JZMatchBean> list, ArrayList<JZMatchBean> resultList) {
		for (JZMatchBean bean : list) {
			if (bean.count > 0) {
				resultList.add(bean);
				bean.getFinalPVResult();
			}
			if (bean.isHasDan()) {
				danCount++;
			}
		}
	}

	/**
	 * 清除所有选择的对阵
	 */
	private void clearAllList() {
		for (JZMatchListBean bean : AllList) {
			if (bean.getMatchList().size() > 0) {
				clearSelectResult(bean.getMatchList());
			} 
			if (bean.getBackupList().size() > 0) {
				clearSelectResult(bean.getBackupList());
			}
		}
	}

	/**
	 * 清楚指定list里面所有选项
	 * 
	 * @param list
	 */
	public static void clearSelectResult(ArrayList<JZMatchBean> list) {
		for (JZMatchBean bean : list) {
			bean.clear();
		}
	}
	@Override
	public void onBackPressed() {
		if(selectNumber>0){
			NormalAlertDialog dialog = new NormalAlertDialog(JLBetActivity.this);
			dialog.setTitle("提示");
			dialog.setContent("您确定退出投注吗？一旦退出,您的投注将被清空。");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
				@Override
				public void onOkBtnClick() {
					finish();
					selectNumber=0;
				}
				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		}else{
			super.onBackPressed();
		}
	}
}
