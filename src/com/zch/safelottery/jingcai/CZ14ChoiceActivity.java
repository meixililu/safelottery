package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.CombineHallActivity;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.LotteryResultHistoryActivity;
import com.zch.safelottery.activity.RecordBetActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.bean.CZInfoBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PopDialog;
import com.zch.safelottery.dialogs.PopDialog.PopViewItemOnclickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.parser.CZInfoBeanParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;

public class CZ14ChoiceActivity extends ZCHBaseActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "CZ14ChoiceActivity";
	public static final String CHANG = "场";
	public static final int RequestCode = 14;
	
	private Spinner issue_spinner;
	private TextView issue_end_time,selected_num,bet_sum,money_sum,error_prompt;
	private FrameLayout top_menu_btn;
	private ListView lv;
	private ProgressBar progressbar;
	private MyListViewAdapter lv_adapter;
	private ArrayList<CZInfoBean> result_list;
	private ArrayList<Integer> selectNum;
	private View clear_view,random_view,submit_view,sum_layout,bottom_layout,declare;
	private ImageView split_img;
	
	private RadioButton cz_14_choice_xh,cz_14_choice_hm;
	private PopDialog popDialog;
	private TextView text1,text2,text3,text4;
	
	private String lid;
	private String current_issue;
	private String current_issue_end_time;
	private int betNum;
	private int alreadySelectNum;
	private ArrayList<IssueInfoBean> issueBeanList;
	private ArrayList<String> issueList;
	
	private LinearLayout showCombine;
	private RelativeLayout showBet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.cz_14_choice_page);
			StatService.onEvent(getApplicationContext(), "14chang", "14场", 1);
//		    addBetAndCombine();
			initData();
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initData() throws Exception {
		Intent intent = getIntent();
		current_issue = intent.getStringExtra(BaseLotteryActivity.INTENT_ISSUE);
		lid = intent.getStringExtra(LotteryId.INTENT_LID);
		issueList = new ArrayList<String>();
		issueBeanList = (ArrayList<IssueInfoBean>) SafeApplication.dataMap.get("beanList");
		SafeApplication.dataMap.clear();
		if(issueBeanList != null){
			for(IssueInfoBean bean : issueBeanList){
				issueList.add(bean.getName());
			}
		}else{
			ToastUtil.diaplayMesShort(getApplicationContext(), "获取期次信息失败，请返回主页刷新重试！");
		}
	}
	
	private void initUI() throws Exception {
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initUI()");
		issue_spinner = (Spinner)findViewById(R.id.cz_14_choice_issue_spinner);
		issue_end_time = (TextView)findViewById(R.id.cz_14_choice_issue_end_time);
		top_menu_btn = (FrameLayout)findViewById(R.id.choice_title_button_menu);
		selected_num = (TextView)findViewById(R.id.cz_14_choice_select_numbers);
		lv = (ListView)findViewById(R.id.cz_14_choice_listview);
		bet_sum = (TextView)findViewById(R.id.cz_14_choice_bet_sum);
		money_sum = (TextView)findViewById(R.id.cz_14_choice_money_sum);
		progressbar = (ProgressBar)findViewById(R.id.cz_14_choice_progressbar);
		error_prompt = (TextView)findViewById(R.id.cz_14_choice_none);
		clear_view = (View)findViewById(R.id.cz_14_choice_clear);
		random_view = (View)findViewById(R.id.cz_14_choice_random);
		submit_view = (View)findViewById(R.id.cz_14_choice_select);
		
		sum_layout = (View)findViewById(R.id.cz_14_choice_sum_layout);
		bottom_layout = (View)findViewById(R.id.cz_14_choice_bottom);
		split_img = (ImageView)findViewById(R.id.cz_14_choice_split);
		declare = (View)findViewById(R.id.cz_14_choice_declare);
		
		cz_14_choice_xh = (RadioButton)findViewById(R.id.choice_title_xuanhao);
		cz_14_choice_hm = (RadioButton)findViewById(R.id.choice_title_hemai);
		
		MyonClickListener myonClickListener = new MyonClickListener();
		top_menu_btn.setOnClickListener(myonClickListener);
		clear_view.setOnClickListener(myonClickListener);
		random_view.setOnClickListener(myonClickListener);
		submit_view.setOnClickListener(myonClickListener);
		error_prompt.setOnClickListener(myonClickListener);
		cz_14_choice_xh.setOnClickListener(myonClickListener);
		cz_14_choice_hm.setOnClickListener(myonClickListener);
		
		result_list = new ArrayList<CZInfoBean>();
		selectNum = new ArrayList<Integer>();
		lv_adapter = new  MyListViewAdapter(this);
		lv.setAdapter(lv_adapter);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_style_nomel, issueList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        issue_spinner.setAdapter(adapter);
        issue_spinner.setOnItemSelectedListener(new SpinnerListener());
	}
	
	private void addBetAndCombine(){
		showCombine = (LinearLayout) findViewById(R.id.jc_show_combine);
		showBet = (RelativeLayout) findViewById(R.id.jc_show_bet);
	}
	
	/**
	 * 获取期次截止时间
	 */
	private void getIssueEndTime(){
		if(issueBeanList != null){
			for(IssueInfoBean bean : issueBeanList){
				if( bean.getName().equals(current_issue) ){
					current_issue_end_time = bean.getDuplexTime();
				}
			}
		}
	}
	/**
	 * 设置期次截止时间
	 */
	private void setIssueEndTime(){
		if(!TextUtils.isEmpty(current_issue_end_time)){
			issue_end_time.setText(TimeUtils.customFormatDate(current_issue_end_time, TimeUtils.DateFormat, TimeUtils.MonthMinuteFormat));
		}
	}
	
	private void showData(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-showData()");
		lv_adapter.notifyDataSetChanged();
	}

	private class SpinnerListener implements AdapterView.OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long arg3) {
			if(arg0.getId() == R.id.cz_14_choice_issue_spinner){
				current_issue = issueList.get(position);
				getIssueEndTime();
				setIssueEndTime();
				doRequestTask();
				setBetNumAndSum(0);
				setSelectNumText(0);
				alreadySelectNum = 0;
				selected_num.setText( setSelectNumText(alreadySelectNum) );
			}
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
    }
	
	class MyListViewAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		public ImageLoader imageLoader;
		private Context context;
		
		public MyListViewAdapter (Context context) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
			imageLoader = new ImageLoader(context,false,65);
		}
		
		public int getCount() {
			return result_list.size();
		}

		public Object getItem(int position) {
			return result_list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.cz_14_choice_page_listview_item_selling, null);
				holder.league_icon = (ImageView)convertView.findViewById(R.id.cz_14_choice_listview_item_league_icon);
				holder.league = (TextView)convertView.findViewById(R.id.cz_14_choice_listview_item_league);
				holder.vs = (TextView)convertView.findViewById(R.id.cz_14_choice_page_listview_item_vs);
				holder.end_time = (TextView)convertView.findViewById(R.id.cz_14_choice_page_listview_item_vs_end_time);
				holder.sp_win = (TextView)convertView.findViewById(R.id.cz_14_choice_page_listview_item_sp_win);
				holder.sp_tied = (TextView)convertView.findViewById(R.id.cz_14_choice_page_listview_item_sp_tied);
				holder.sp_lost = (TextView)convertView.findViewById(R.id.cz_14_choice_page_listview_item_sp_lost);
				
				holder.cb_win = (CheckBox)convertView.findViewById(R.id.cz_14_choice_page_listview_item_cb_win);
				holder.cb_tied = (CheckBox)convertView.findViewById(R.id.cz_14_choice_page_listview_item_cb_tied);
				holder.cb_lost = (CheckBox)convertView.findViewById(R.id.cz_14_choice_page_listview_item_cb_lost);
				holder.cb_all = (CheckBox)convertView.findViewById(R.id.cz_14_choice_page_listview_item_cb_all);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			final CZInfoBean bean = result_list.get(position);
			imageLoader.DisplayImage(bean.getIcon_url(), context, holder.league_icon);
			holder.league.setText(bean.getLeague());
			holder.vs.setText( patchUpTeamName(bean.getHome_team(), bean.getGuest_team(), position) );
			holder.sp_win.setText( getSPByPosition(bean.getAvgOdds(), 0) );
			holder.sp_tied.setText( getSPByPosition(bean.getAvgOdds(), 1) );
			holder.sp_lost.setText( getSPByPosition(bean.getAvgOdds(), 2) );
			
			
			holder.end_time.setText( TimeUtils.customFormatDate(bean.getPlaying_time(), TimeUtils.DateFormat, TimeUtils.MonthMinuteFormat) );
			holder.cb_win.setChecked( bean.getWin() >= 0 );
			holder.cb_tied.setChecked( bean.getTied() >= 0 );
			holder.cb_lost.setChecked( bean.getLost() >= 0 );
			holder.cb_all.setChecked( bean.is_selectDan() );
			if(bean.is_selectDan()){
				holder.cb_all.setText("清");
			}else{
				holder.cb_all.setText("包");
			}
			
			holder.cb_win.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(holder.cb_win.isChecked()){
						bean.setWin(3);
					}else{
						bean.setWin(-1);
					}
					checkIsSelectAll(holder.cb_win, holder.cb_lost, holder.cb_tied, holder.cb_all,bean);
					selected_num.setText( setSelectNumText(countSelectNum(selectNum, bean, position)) );
				}
			});
			holder.cb_tied.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(holder.cb_tied.isChecked()){
						bean.setTied(1);
					}else{
						bean.setTied(-1);
					}
					checkIsSelectAll(holder.cb_win, holder.cb_lost, holder.cb_tied, holder.cb_all,bean);
					selected_num.setText( setSelectNumText(countSelectNum(selectNum, bean, position)) );
				}
			});
			holder.cb_lost.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(holder.cb_lost.isChecked()){
						bean.setLost(0);
					}else{
						bean.setLost(-1);
					}
					checkIsSelectAll(holder.cb_win, holder.cb_lost, holder.cb_tied, holder.cb_all,bean);
					selected_num.setText( setSelectNumText(countSelectNum(selectNum, bean, position)) );
				}
			});
			holder.cb_all.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					if(holder.cb_all.isChecked()){
						holder.cb_all.setText("清");
						bean.setSelectDan(true);
						selectAllOrNot(holder.cb_win, holder.cb_lost, holder.cb_tied, true);
						bean.setWin(3);
						bean.setTied(1);
						bean.setLost(0);
					}else{
						holder.cb_all.setText("包");
						bean.setSelectDan(false);
						selectAllOrNot(holder.cb_win, holder.cb_lost, holder.cb_tied, false);
						bean.setTied(-1);
						bean.setLost(-1);
						bean.setWin(-1);
					}
					selected_num.setText( setSelectNumText(countSelectNum(selectNum, bean, position)));
			}});
			
			return convertView;
		}
	}
	
	static class ViewHolder {
		ImageView league_icon;
		TextView league;
		TextView vs;
		TextView end_time;
		TextView sp_win;
		TextView sp_tied;
		TextView sp_lost;
		
		CheckBox cb_win;
		CheckBox cb_tied;
		CheckBox cb_lost;
		CheckBox cb_all;
    }
	
	private int countSelectNum(ArrayList<Integer> list, CZInfoBean bean, int pos){
		int num = 0;
		if(bean.getWin() >= 0){
			num++;
		}
		if(bean.getTied() >= 0){
			num++;
		}
		if(bean.getLost() >= 0){
			num++;
		}
		bean.setSelect_num(num);
		
		alreadySelectNum = 0;
		for(CZInfoBean info : result_list){
			if( info.getSelect_num() > 0 ){
				alreadySelectNum++;
			}
		}
		if(alreadySelectNum == 14){
			betNum = 1;
			for(CZInfoBean info : result_list){
				if( info.getSelect_num() > 0 ){
					betNum *= info.getSelect_num();
				}
			}
			setBetNumAndSum(betNum);
		}else{
			setBetNumAndSum(0);
		}
		return alreadySelectNum;
	}
	
	private void checkIsSelectAll(CheckBox cb_win,CheckBox cb_tied,CheckBox cb_lost,CheckBox cb_all,CZInfoBean bean){
		if( cb_win.isChecked() && cb_tied.isChecked() && cb_lost.isChecked() ){
			cb_all.setText("清");
			bean.setSelectDan(true);
			cb_all.setChecked(true);
		}
		if( !cb_win.isChecked() || !cb_tied.isChecked() || !cb_lost.isChecked() ){
			cb_all.setText("包");
			bean.setSelectDan(false);
			cb_all.setChecked(false);
		}
	}
	
	private void selectAllOrNot(CheckBox cb_win,CheckBox cb_tied,CheckBox cb_lost,boolean isCheck){
		cb_win.setChecked(isCheck);
		cb_tied.setChecked(isCheck);
		cb_lost.setChecked(isCheck);
	}
	
	private String setSelectNumText(int num){
		return "已选"+ num +"场";
	}
	
	private void setBetNumAndSum(int betNum){
		bet_sum.setText( "共"+betNum+"注,");
		money_sum.setText( "共"+(betNum*2)+"元");
	}
	
	private void clearUpSelected(){
		if( alreadySelectNum > 0 ){
			NormalAlertDialog dialog = new NormalAlertDialog(CZ14ChoiceActivity.this);
			dialog.setTitle("提示");
			dialog.setContent("您确定要清空当前选择的号码吗？");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
				@Override
				public void onOkBtnClick() {
					clearList();
				}
				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
			
		}else{
			display("您还没选择任何号码");
		}
	}
	
	private void clearList(){
		for(CZInfoBean info : result_list){
			info.clear();
		}
		setBetNumAndSum(0);
		setSelectNumText(0);
		alreadySelectNum = 0;
		selected_num.setText( setSelectNumText(alreadySelectNum) );
		lv_adapter.notifyDataSetChanged();
	}
	
	private class MyonClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.choice_title_button_menu:
						topMenu();
					break;
				case R.id.cz_14_choice_clear:
					clearUpSelected();
					break;
				case R.id.cz_14_choice_random:
					Intent detail = new Intent(CZ14ChoiceActivity.this,HelpDetailActivity.class);
					detail.putExtra("kind", 19);
					startActivity(detail);
					break;
				case R.id.cz_14_choice_select:
					if(betNum * 2 > 20000){
						display("单次投注金额不能超过2万元");
					}else if( alreadySelectNum == 14 ){
						SafeApplication.dataMap.put("result_list", result_list);
						Intent intent = new Intent();
						intent.setClass(CZ14ChoiceActivity.this, CZ14OrderListActivity.class);
						intent.putExtra(BaseLotteryActivity.INTENT_ISSUE,current_issue);
						intent.putExtra(LotteryId.INTENT_LID,LotteryId.SFC);
						intent.putExtra("betNum", betNum);
						intent.putExtra("key", "result_list");
						startActivityForResult(intent,RequestCode);
					}else{
						display("至少选择一注号码");
					}
					break;
				case R.id.select_popwindow_text1:
					 hidePopView();
					break;
				case R.id.select_popwindow_text2:
					 display("暂未开通，敬请期待");
					 hidePopView();					
					break;
				case R.id.select_popwindow_text3:
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.SFC);
					if(GetString.isLogin){
						Intent intent  = new Intent(CZ14ChoiceActivity.this, RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						startActivity(intent);
					}else{
						Intent intent  = new Intent(CZ14ChoiceActivity.this, LoginActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
					hidePopView();			
					break;
				case R.id.select_popwindow_text4:
					Bundle mBundle1 = new Bundle();
					mBundle1.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.SFC);
					Intent intent = new Intent();
					intent.setClass(CZ14ChoiceActivity.this, LotteryResultHistoryActivity.class);
					intent.putExtra(Settings.BUNDLE,mBundle1);
					startActivity(intent);
					hidePopView();			
					break;
				case R.id.cz_14_choice_none:
					doRequestTask();
					break;
				case R.id.choice_title_xuanhao:
					break;
				case R.id.choice_title_hemai:
					top_rbtn_hemai();
					break;
			}
		}
	}
	
	/** top hemai radiobutton **/
	public void top_rbtn_hemai() {
		Intent intent = new Intent(this, CombineHallActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(LotteryId.INTENT_LID, lid);
		intent.putExtra(Settings.BUNDLE, mBundle);
		
		this.startActivity(intent);
		
		cz_14_choice_xh.setChecked(true);
	};
	
	private String patchUpTeamName(String home,String guest,int num){
		return (++num) + "." + home + "vs" + guest;
	}
	
	private String getSPByPosition(String sp, int pos){
		if(!TextUtils.isEmpty(sp)){
			String[] sp_sz = sp.split("#");
			return sp_sz[pos];
		}
		return "";
	}
	
	private void doRequestTask() {
		
		HashMap postMap = new HashMap<String, String>();
		postMap.put("lotteryId", LotteryId.SFC);
		postMap.put("issue", current_issue);
		SafelotteryHttpClient.post(this, "3302", "subGame", JsonUtils.toJsonStr(postMap), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					String issueList = (String) mMap.get("issueList");
					String subGameList = (String) mMap.get("subGameList");
					try {
						result_list = (ArrayList<CZInfoBean>) JsonUtils.parserJsonArray(subGameList,new CZInfoBeanParser());
						showData();
					} catch (Exception e) {
						LogUtil.DefalutLog("CZ14ChoiceActivity-Exception-onSuccess-parserJsonArray");
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
			
			@Override
			public void onStart(){
				error_prompt.setVisibility(View.GONE);
				result_list.clear();
				lv_adapter.notifyDataSetChanged();
				progressbar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onFinish(){
				progressbar.setVisibility(View.GONE);
			}
		});
	}
	
	private void display(String content){
		Toast.makeText(CZ14ChoiceActivity.this, content, 0).show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (popDialog != null) {
				if (popDialog.isShowing()) {
					popDialog.dismiss();
					return true;
				}
			}

			if (alreadySelectNum > 0) {
				NormalAlertDialog dialog = new NormalAlertDialog(CZ14ChoiceActivity.this);
				dialog.setTitle("提示");
				dialog.setContent("您确定退出投注吗？一旦退出,您的投注将被清空。");
				dialog.setOk_btn_text("确定");
				dialog.setCancle_btn_text("取消");
				dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
					
					@Override
					public void onOkBtnClick() {
						finish();
					}
					
					@Override
					public void onCancleBtnClick() {
						// TODO Auto-generated method stub
						
					}
				});
				dialog.show();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void hidePopView(){
		if(popDialog != null){
			if(popDialog.isShowing()){
				popDialog.dismiss();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RequestCode && resultCode == RESULT_OK){
			clearList();
		}
	}
	
	private void topMenu(){
		if(popDialog == null){
			popDialog = new PopDialog(this,R.style.popDialog);
			popDialog.textVisibility(new int[]{1, 2});
			
			popDialog.setListener(new PopViewItemOnclickListener(){

				@Override
				public void onFirstClick(View v) {
				}

				@Override
				public void onSecondClick(View v) {
					selectDaigouOrHeimai(false);
				}

				@Override
				public void onThirdClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.SFC);
					if(GetString.isLogin){
						Intent intent  = new Intent(CZ14ChoiceActivity.this,RecordBetActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						startActivity(intent);
					}else{
						Intent intent  = new Intent(CZ14ChoiceActivity.this,LoginActivity.class);
						intent.putExtra(Settings.BUNDLE,mBundle);
						intent.putExtra(Settings.TOCLASS, RecordBetActivity.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFourthClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.SFC);
					Intent intent = new Intent();
					intent.setClass(CZ14ChoiceActivity.this, LotteryResultHistoryActivity.class);
					intent.putExtra(Settings.BUNDLE,mBundle);
					startActivity(intent);
				}
				
			});
			popDialog.setPopViewPosition();
			popDialog.show();
		}else{
			popDialog.show();
		}
	}
	
	/**
	 * 选择代购或者合买，true为代购，false为合买
	 * @param value
	 */
	public void selectDaigouOrHeimai(boolean value){
		ToastUtil.diaplayMesShort(CZ14ChoiceActivity.this, "暂未开通，敬请期待");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
		result_list.clear();
		result_list = null;
		current_issue = null;
		current_issue_end_time= null;
	}
}
