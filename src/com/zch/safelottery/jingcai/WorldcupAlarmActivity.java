package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
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
import com.zch.safelottery.setttings.SLManifest;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AlarmUtil;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.util.ToastUtil;

public class WorldcupAlarmActivity extends ZCHBaseActivity {

	public static final String WorldCupIsReboot = "WorldCupIsReboot";
	public static final String WorldCupMatchIsShutDown = "WorldCupMatchIsShutDown";
	public static final String SelectedMatchIdKey = "SelectedMatchIdKey";
	private LayoutInflater inflater;
	private AmazingListView sectionLV;

	private ProgressBar mProgressBar;
	private TextView no_result_text;
	private SectionListAdapter sectionAdapter;
	private SharedPreferences spf;

	private ArrayList<JZMatchListBean> AllList;
	public ArrayList<Pair<String, ArrayList<JZMatchBean>>> all;
	private JCAbstractClass mJCItemClass;//
	private String selectedMatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.worldcup_alarm_activity);
			initViews();
			doRequestTask();
		} catch (Exception e) {
			LogUtil.ExceptionLog("JZBetActivity-onCreate()");
			e.printStackTrace();
		}
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		spf = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		inflater = LayoutInflater.from(this);
		AllList = new ArrayList<JZMatchListBean>();
		all = new ArrayList<Pair<String, ArrayList<JZMatchBean>>>();
		selectedMatch = spf.getString(SelectedMatchIdKey, "");
		
		sectionLV = (AmazingListView) findViewById(R.id.section_list_view);
		mProgressBar = (ProgressBar) findViewById(R.id.loadding_progressbar_m);
		no_result_text = (TextView) findViewById(R.id.no_result_text);

		View worldcup_page_header = inflater.inflate(R.layout.worldcup_page_header, null, false);
		LinearLayout worldcup_banner_img = (LinearLayout) worldcup_page_header.findViewById(R.id.worldcup_banner_img);
		Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.worldcup_banner);
		ImageUtil.adjustImgSize(rawBitmap, worldcup_banner_img, ScreenUtil.dip2px(this, 14));
		sectionLV.addHeaderView(worldcup_page_header);
		
		
//		View sectionHeaderView = inflater.inflate(R.layout.worldcup_list_section_header, sectionLV, false);
//		sectionLV.setPinnedHeaderView(sectionHeaderView);
		sectionAdapter = new SectionListAdapter();
		sectionLV.setAdapter(sectionAdapter);
	}

	/**
	 * 请求数据
	 */
	private void doRequestTask() {
		all.clear();
		sectionAdapter.notifyDataSetChanged();
		HashMap<String, String> postMap = new HashMap<String, String>();
		postMap.put("lotteryId", LotteryId.JCZQ);
		postMap.put("playId", "05");
		postMap.put("pollId", "02");
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
							long begin = System.currentTimeMillis();
							AllList = (ArrayList<JZMatchListBean>) JsonUtils.parserJsonArray(matchList,new JZMatchListBeanParser());
							if(AllList != null){
								filterMatchs();
								getSectionListItems(0);
							}
							long end = System.currentTimeMillis();
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.DefalutLog("JZBetActivity-Exception-onSuccess-parserJsonArray");
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
	 * 过滤世界杯赛事
	 */
	private void filterMatchs(){
		ArrayList<JZMatchListBean> matchListBeans = new ArrayList<JZMatchListBean>();
		JZMatchListBean matchListBean = new JZMatchListBean();
		for (JZMatchListBean bean : AllList) {
			ArrayList<JZMatchBean> newList = new ArrayList<JZMatchBean>();
			ArrayList<JZMatchBean> mJCBDBeanList = bean.getMatchList();
			for(JZMatchBean matchBean : mJCBDBeanList){
				if(matchBean.getMatchName().contains("世界杯")){
					newList.add(matchBean);
				}
			}
			if(newList.size() > 0){
				bean.setMatchList(newList);
				matchListBeans.add(bean);
			}
		}
		AllList = matchListBeans;
		setSelectedMatch();
	}
	
	/**
	 * 标记已选择的闹铃
	 */
	private void setSelectedMatch(){
		String[] selecteds = selectedMatch.split(";");
		if(selecteds.length > 0){
			for (JZMatchListBean bean : AllList) {
				ArrayList<JZMatchBean> mJCBDBeanList = bean.getMatchList();
				for(JZMatchBean matchBean : mJCBDBeanList){
					String id = matchBean.getIssue() + matchBean.getSn();
					for(String selectedId : selecteds){
						String[] contents = selectedId.split(",");
						if(id.equals(contents[0])){
							matchBean.setHasDan(true);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 数据显示
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
		sectionLV.setSelection(position);
		sectionAdapter.notifyDataSetChanged();
	}

	public class SectionListAdapter extends AmazingAdapter {

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
				TextView lSectionTitle = (TextView) view.findViewById(R.id.worldcup_header_section);
				lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
			} else {
				parentLayout.setVisibility(View.GONE);
			}
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			final String sectionString = getSections()[getSectionForPosition(position)];
			LogUtil.DefalutLog("position:" + position + "--" + sectionString);
			TextView header_section = (TextView) header.findViewById(R.id.worldcup_header_section);
			header_section.setText(sectionString);
		}

		@Override
		public View getAmazingView(int position, View convertView, ViewGroup parent) {
			LogUtil.DefalutLog("" + convertView);
			View res = convertView;
			JZMatchBean mJCBDBean = getItem(position);
			if (res == null) res = inflater.inflate(R.layout.worldcup_list_section_layout, null);
			LinearLayout parent_layout = (LinearLayout) res.findViewById(R.id.parent_layout);

			if ( TextUtils.isEmpty(mJCBDBean.getSn()) ) {
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
	 * 显示数据
	 * @param bean
	 * @param parent_layout
	 */
	private void showItems(final JZMatchBean bean, LinearLayout parent_layout) {
		if(mJCItemClass == null){
			mJCItemClass = new WorldcupAlarmListItemCLass(this, inflater);
		}
		mJCItemClass.showItems(null, bean, parent_layout);
	}

	@Override
	protected void isNeedClose(int ationCode) {
		super.isNeedClose(ationCode);
		if (ationCode == Settings.EXIT_JCZQ) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getSelectedMatch();
	}
	
	/**
	 * 保存定制的赛制闹铃
	 */
	private void getSelectedMatch(){
		StringBuilder sb = new StringBuilder();
		for (JZMatchListBean bean : AllList) {
			ArrayList<JZMatchBean> mJCBDBeanList = bean.getMatchList();
			for(JZMatchBean matchBean : mJCBDBeanList){
				if(matchBean.isHasDan()){
					sb.append(matchBean.getIssue()+matchBean.getSn() + "," +
							matchBean.getEndFuShiTime() + "," +
							matchBean.getMainTeam() + " vs " + matchBean.getGuestTeam());
					sb.append(";");
				}
			}
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.lastIndexOf(";"));
		}
		Settings.saveSharedPreferences(spf, SelectedMatchIdKey, sb.toString());
		LogUtil.DefalutLog("SelectedMatchIdKey:"+sb.toString());
	}
	
	/**重启之后需要重新设置闹铃事件
	 * @param context
	 */
	public static void resetAlarmAfterReboot(Context context){
		try {
			SharedPreferences spf = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
			String mSelectedMatchIdKey = spf.getString(SelectedMatchIdKey, "");
			String[] selectedIds = mSelectedMatchIdKey.split(";");
			if(selectedIds.length > 0){
				for(int i=0,j=selectedIds.length; i<j; i++){
					String[] contents = selectedIds[i].split(",");
					long triggerAtTime = WorldcupAlarmListItemCLass.getTimeInMillis(contents[1]);
					if(triggerAtTime > System.currentTimeMillis()){
						AlarmUtil.setAlarm(context, triggerAtTime, contents[0], contents[2]);
					}else{
						removeAlarmHasShow(context,contents[0]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**移除已经提示过的闹铃
	 * @param context
	 * @param id
	 */
	public static void removeAlarmHasShow(Context context,String id){
		try {
			SharedPreferences spf = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
			String mSelectedMatchIdKey = spf.getString(SelectedMatchIdKey, "");
			String[] selectedIds = mSelectedMatchIdKey.split(";");
			if(selectedIds.length > 0){
				for(int i=0,j=selectedIds.length; i<j; i++){
					if(selectedIds[i].contains(id)){
						selectedIds[i] = "";
					}
				}
			}
			StringBuilder sb = new StringBuilder();
			for(String newContent : selectedIds){
				if(!TextUtils.isEmpty(newContent)){
					sb.append(newContent);
					sb.append(";");
				}
			}
			if(sb.length() > 0){
				sb.deleteCharAt(sb.lastIndexOf(";"));
			}
			Settings.saveSharedPreferences(spf, SelectedMatchIdKey, sb.toString());
			LogUtil.DefalutLog("New SelectedMatchIdKey:"+sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setSystemReboot(Context context){
		try {
			SharedPreferences spf = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
			Settings.saveSharedPreferences(spf, WorldCupIsReboot, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
