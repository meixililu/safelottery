package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.BetRecordListBean;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PopDialog;
import com.zch.safelottery.dialogs.PopDialog.PopViewItemOnclickListener;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.BetRecordListParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.view.TitleViews;

public class RecordBetActivity extends ZCHBaseActivity {

	// bet record new interface
	public static final String oldv = "http://oldv.zch168.com";

	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "BetRecordActivity";

	private TextView type, method, oldoop;
	private PopWindowDialog mDialogType, mDialogMethod;

	private ProgressBar progressbar;
	private TextView load_result;
	private RelativeLayout title;
	private PullToRefreshListView lv;
	private PopDialog mPopDialog;
	private List<SafelotteryType> result_list;
	private MyListViewAdapter lv_adapter;
	
	private String lottery_id = "";
	private String playId = "";
	private String userCode;
	private String orderType = "";
	private String bonusStatus = "";
	
	private int total_page_num;
	private int page_num = 1;

	private TitleViews titleViews;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.bet_record);
			Bundle mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
			if(mBundle != null){
				lottery_id = mBundle.getString(Settings.INTENT_STRING_LOTTERY_ID);
			}
			if(TextUtils.isEmpty(lottery_id)){
				lottery_id = LotteryId.All;
			}

			initUI();
			initData();
			addTitle();
			if (GetString.userInfo != null) {
				if (GetString.userInfo.getUserCode() != null) {
					userCode = GetString.userInfo.getUserCode();
					if (!TextUtils.isEmpty(userCode)) {
						doRequestTask();
					} else {
						Toast.makeText(RecordBetActivity.this, "登录超时，请重新登录！", 0).show();
					}
				} else {
					Toast.makeText(RecordBetActivity.this, "登录超时，请重新登录！", 0).show();
				}
			} else {
				Toast.makeText(RecordBetActivity.this, "登录超时，请重新登录！", 0).show();
			}

		} catch (Exception e) {
			LogUtil.ExceptionLog("RecordBetActivity-onCreate()");
			e.printStackTrace();
		}
	}

	private void initUI() {
		if (DEBUG) Log.d(Settings.TAG, TAG + "-initUI()");
		type = (TextView) findViewById(R.id.bet_record_spinner_type);
		oldoop = (TextView) findViewById(R.id.oldoop);
		method = (TextView) findViewById(R.id.bet_record_spinner_buy_method);
		title = (RelativeLayout) findViewById(R.id.bet_record_title);

		progressbar = (ProgressBar) findViewById(R.id.bet_record__progressbar);
		load_result = (TextView) findViewById(R.id.bet_record_load_result);
		lv = (PullToRefreshListView) findViewById(R.id.bet_record_listview);

		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		type.setText(LotteryId.getLotteryName(lottery_id));
		method.setText("全部");

		BtnOnClickListener btnListener = new BtnOnClickListener();
		type.setOnClickListener(btnListener);
		method.setOnClickListener(btnListener);
		oldoop.setOnClickListener(btnListener);

	}
	
	private void addTitle(){
		titleViews = new TitleViews(this, "投注记录");
		titleViews.setBtnName("全部");
		titleViews.setBtnIcon(R.drawable.y11_title_button);
		title.addView(titleViews.getView());
		titleViews.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopView();
			}
		});
	}
	
	private void initData() {
		result_list = new ArrayList<SafelotteryType>();
		lv_adapter = new MyListViewAdapter(getApplicationContext());
		lv.setAdapter(lv_adapter);
		lv.setOnItemClickListener(new ListClickListener());
		lv.setMode(Mode.BOTH);
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					refresh();
					doRequestTask();
					break;
				case PULL_FROM_END:
					if (page_num <= total_page_num) {
						doRequestTask();
					}
					break;
				}
			}
		});
	}

	private void refresh() {
		load_result.setVisibility(View.GONE);
		page_num = 1;
	}

	
	/**
	 * 实现ListView Item　跳转到投注详情　BuyLotteryAgentActivity
	 * */
	private class ListClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
			BetRecordListBean betRecordListBean = (BetRecordListBean) result_list.get(position - 1);
			String programsOrderId = betRecordListBean.getProgramsOrderId();
			String buyType = betRecordListBean.getBuyType();
			String userCode = betRecordListBean.getUserCode();
			String orderType=betRecordListBean.getOrderType();
			Intent intent = new Intent();
			if(findOrderType(orderType)==true){
				intent.putExtra("programsOrderId", programsOrderId);
				intent.putExtra("userCode", userCode);
				intent.putExtra("buyType", buyType);
				intent.setClass(RecordBetActivity.this, RecordBetAgentActivity.class);
			}else{
				intent.putExtra("programsOrderId", programsOrderId);
				intent.putExtra("userCode", userCode);
				intent.putExtra("buyType", buyType);
				intent.setClass(RecordBetActivity.this, RecordBetCombineAgentActivity.class);
			}
			
			
			// if (buyType.equals("2")) {
			// // intent.setClass(BetRecordActivity.this,
			// // CombineDetailsActivity.class);
			// } else {
			// intent.setClass(RecordBetActivity.this,
			// BuyLotteryAgentActivity.class);
			// }
			startActivity(intent);
		}

		public boolean findOrderType(String orderType) {
			if(orderType.equals("0")||orderType.equals("8")||orderType.equals("10")||orderType.equals("-10")){
				return true ;
			}
			return false;
		}
	}

	class MyListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyListViewAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
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
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.bet_record_list_item, null);
				holder.lottery_name_image = (ImageView) convertView.findViewById(R.id.bet_record_lottery_name_image);
				holder.lottery_name = (TextView) convertView.findViewById(R.id.bet_record_lottery_name);
				holder.lottery_issue = (TextView) convertView.findViewById(R.id.bet_record_lottery_issue);
				holder.lottery_time = (TextView) convertView.findViewById(R.id.bet_record_lottery_time);
				holder.lottery_money = (TextView) convertView.findViewById(R.id.bet_record_lottery_money);
				holder.lottery_reward = (TextView) convertView.findViewById(R.id.bet_record_lottery_reward);
				holder.lottery_result = (TextView) convertView.findViewById(R.id.bet_record_lottery_result);
				holder.bet_time = (TextView) convertView.findViewById(R.id.bet_record_lottery_time_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BetRecordListBean betRecordListBean = (BetRecordListBean) result_list.get(position);

			String lid = betRecordListBean.getLotteryId();
			if(lid.equals(LotteryId.SFC))
				if(betRecordListBean.getPlayId().equals("02"))
					lid = LotteryId.RX9;
			
			holder.lottery_name.setText(LotteryId.getLotteryName(lid));
			holder.lottery_name_image.setBackgroundResource(LotteryId.getLotteryIcon(lid));
//			LogUtil.DefalutLog("issue========" + betRecordListBean.getIssue() + "方案号" + betRecordListBean.getProgramsOrderId());
			String issue = betRecordListBean.getIssue();
			int issueLen = issue.length();
			if (lid.equals(LotteryId.SSCCQ) || lid.equals(LotteryId.SSCJX) ) {
				if (issueLen > 5) {
					issue = issue.substring(4);
				}
			} else if (lid.equals(LotteryId.CGJ)) {
				if (issueLen > 5) {
					issue = issue.substring(0, 4);
				}
			}
			holder.lottery_issue.setText(getIssue(lid, issue));
			holder.lottery_time.setText(TimeUtils.getLongtimeToShorttime(betRecordListBean.getCreateTime()));

			if (betRecordListBean.getOrderType().equals("10")) {
				holder.bet_time.setText("赠送时间：");
				holder.lottery_reward.setText("--");
				holder.lottery_result.setText(betRecordListBean.getBonusStatus().equals("3") ? "赠送失败" : "赠送成功");
			} else if (betRecordListBean.getOrderType().equals("-10")) {
				holder.bet_time.setText("受赠时间：");
				holder.lottery_reward.setText("--");
				holder.lottery_result.setText(betRecordListBean.getBonusStatus().equals("3") ? "赠送失败" : "赠送成功");
			} else {
				holder.bet_time.setText("下注时间：");
				int num=ConversionUtil.StringToInt(betRecordListBean.getProgramsStatus());
				if(num>=3){
					holder.lottery_result.setText("投注失败");
					holder.lottery_reward.setText("--");
				}else{
					if(betRecordListBean.getBonusStatus().equals("0")){
						holder.lottery_result.setText("未开奖");
						holder.lottery_reward.setText("--");
					}else if(betRecordListBean.getBonusStatus().equals("1")){
						if(betRecordListBean.getBonusToAccount().equals("0")){
							holder.lottery_result.setText("中奖未派");
							holder.lottery_reward.setText("--");
						}else{
							holder.lottery_result.setText("已派奖");
							String bonusAmount=betRecordListBean.getBonusAmount();
							if(bonusAmount.equals("0.00")){
								holder.lottery_reward.setText("<0.01元");
							}else{
								double da = ConversionUtil.StringToDouble(bonusAmount);
								int ia = (int) da;
								if(da - ia >= 0.01)
									holder.lottery_reward.setText("￥" + bonusAmount);
								else
									holder.lottery_reward.setText("￥" + ia);
							}
						}
					}else if(betRecordListBean.getBonusStatus().equals("2")){
						holder.lottery_result.setText("未中奖");
						holder.lottery_reward.setText("￥0");
					}
				}
			}
			
			holder.lottery_money.setText("￥" + (int)ConversionUtil.StringToDouble(betRecordListBean.getOrderAmount()));
			
			if (betRecordListBean.getBonusStatus().equals("1")) {
				holder.lottery_reward.setTextColor(getResources().getColor(R.color.red));
				holder.lottery_result.setTextColor(getResources().getColor(R.color.red));
			} else if (betRecordListBean.getBonusStatus().equals("2")) {
				holder.lottery_reward.setTextColor(getResources().getColor(R.color.green));
				holder.lottery_result.setTextColor(getResources().getColor(R.color.green));
			} else{
				holder.lottery_reward.setTextColor(getResources().getColor(R.color.buy_lottery_count_down));
				holder.lottery_result.setTextColor(getResources().getColor(R.color.buy_lottery_count_down));
			}
			return convertView;
		}
	}

	static class ViewHolder {
		ImageView lottery_name_image;
		TextView lottery_name;
		TextView lottery_issue;
		TextView lottery_time;
		TextView lottery_money;
		TextView lottery_reward;
		TextView lottery_result;
		TextView bet_time;
	}

	private String getIssue(String lid, String issue) {
		if (LotteryId.JCLQ.equals(lid) || LotteryId.JCZQ.equals(lid)) {
			return issue;
		} else if (lid.equals(LotteryId.CGJ)) {
			return issue;
		} else {
			if (issue.length() > 7) {
				String issuestr = issue.substring(4, issue.length());
				return issuestr + "期";
			} else {
				return issue + "期";
			}
		}
	}

	private void showDialogType() {
		if (mDialogType == null) {
			//敢得当前控件所在的位置
			LinearLayout lny = (LinearLayout) findViewById(R.id.bet_record_lny);
			LinearLayout typeLny = (LinearLayout) findViewById(R.id.bet_record_type_lny);
			
			int popDialogY = lny.getTop() + typeLny.getHeight();
			
			String orderstr = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE).getString(LotteryInfoParser.LotteryOrderStrKey, "");
			final List<String> temp = LotteryId.getPursueLotteryName(orderstr, LotteryId.LOTTERY_TYPE_ALL);
			temp.add("猜冠军");
			mDialogType = new PopWindowDialog(this, temp, temp.indexOf(LotteryId.getLotteryName(lottery_id)));
			mDialogType.setPopViewPosition(0, popDialogY);
			mDialogType.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogType.getList();
					CheckBox checkBox;

					for (int i = 0, size = list.size(); i < size; i++) {
						checkBox = list.get(i);
						if (i == v.getId()) {
							String t = temp.get(i);
							checkBox.setChecked(true);
							lottery_id = LotteryId.getLotteryLid(t);
							refresh();
							doRequestTask();
							type.setText(t);
						} else {
							if (checkBox.isChecked())
								checkBox.setChecked(false);
						}
					}
					mDialogType.dismiss();
				}
			});

			mDialogType.show();
		} else {
			if(mDialogType.isShowing())
				mDialogType.dismiss();
			else
				mDialogType.show();
		}
	}

	private void showDialogMethod() {
		if (mDialogMethod == null) {
			//敢得当前控件所在的位置
			LinearLayout lny = (LinearLayout) findViewById(R.id.bet_record_lny);
			LinearLayout typeLny = (LinearLayout) findViewById(R.id.bet_record_type_lny);
			
			int popDialogY = lny.getTop() + typeLny.getHeight();
			
			final String[] item = (String[]) Settings.getBuyMethodList().toArray(new String[Settings.getBuyMethodList().size()]);
			mDialogMethod = new PopWindowDialog(this, item, 0);
			mDialogMethod.setPopViewPosition(0, popDialogY);
			mDialogMethod.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogMethod.getList();
					CheckBox checkBox;
					for (int i = 0, size = list.size(); i < size; i++) {
						checkBox = list.get(i);
						if (i == v.getId()) {
							String str = item[i];
							checkBox.setChecked(true);
							orderType = getBuy_method(str);
							refresh();
							doRequestTask();
							method.setText(str);
						} else {
							if (checkBox.isChecked())
								checkBox.setChecked(false);
						}
					}
					mDialogMethod.dismiss();
				}
			});

			mDialogMethod.show();
		} else {
			if(mDialogMethod.isShowing())
				mDialogMethod.dismiss();
			else
				mDialogMethod.show();
		}
	}

	// 根据位置返回订单类型
	public String getBuy_method(String type) {
		if(type.equals("全部")) return "";
		if(type.equals("代购")) return "0";
		if(type.equals("发起合买")) return "1";
		if(type.equals("自动跟单")) return "2";
		if(type.equals("合买认购")) return "3";
		if(type.equals("保底认购")) return "4";
		if(type.equals("追号")) return "8";
		if(type.equals("赠送彩票")) return "10";
		if(type.equals("受赠彩票")) return "-10";
		return "";
	}

	private class BtnOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bet_record_spinner_type:
				showDialogType();
				break;
			case R.id.bet_record_spinner_buy_method:
				showDialogMethod();
				break;
			case R.id.oldoop:
				Intent intent = new Intent(RecordBetActivity.this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL,oldv);
				RecordBetActivity.this.startActivity(intent);
				break;
			}
		}
	}

	private void doRequestTask() {
		if(result_list.size() == 0){
			progressbar.setVisibility(View.VISIBLE);
		}
		SafelotteryHttpClient.post(this, "3303", "order", initDate(), new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try{
					total_page_num = ConversionUtil.StringToInt((String) mMap.get("pageTotal"));
					String accountLogListStr = (String) mMap.get("orderList");
					BetRecordListParser betRecordListParser = new BetRecordListParser();
					
					List<SafelotteryType> list=(List<SafelotteryType>) JsonUtils.parserJsonArray(accountLogListStr, betRecordListParser);
					if(list != null){
						if(list.size() == 0 && page_num == 1){
							load_result.setVisibility(View.VISIBLE);
							result_list.clear();
							lv_adapter.notifyDataSetChanged();
						}else{
							load_result.setVisibility(View.GONE);
							if(page_num == 1){
								result_list.clear();
							}
							result_list.addAll(list);
							lv_adapter.notifyDataSetChanged();
							page_num++;
						}
						if(page_num > total_page_num){
							lv.setMode(Mode.PULL_FROM_START);
						}else{
							lv.setMode(Mode.BOTH);
						}
					}else{
						ToastUtil.diaplayMesShort(getApplicationContext(), "暂无数据");
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				if(result_list == null || result_list.size() == 0){
					load_result.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onFinish(){
				progressbar.setVisibility(View.GONE);
				lv.onRefreshComplete();
			}

		});
	}

	public String initDate() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		
		if(lottery_id.equals(LotteryId.RX9)){
			map.put("lotteryId", LotteryId.SFC);
			playId = "02";
		}else if(lottery_id.equals(LotteryId.SFC)){
			map.put("lotteryId", LotteryId.SFC);
			playId = "01";
		}else{
			map.put("lotteryId", lottery_id);
			playId = "";
		}
		LogUtil.DefalutLog("current page number:"+page_num);
		map.put("playId", playId);
		map.put("orderType", orderType);
		map.put("bonusStatus", bonusStatus);
		map.put("page", page_num);
		map.put("pageSize", "10");
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.d(Settings.TAG, TAG + "-onDestroy()");
	}

	private void showPopView() {
		if (mPopDialog == null) {
			mPopDialog = new PopDialog(this, R.style.popDialog, new String[] { "全部", "未开奖", "已中奖", "未中奖" });
			mPopDialog.setListener(new PopViewItemOnclickListener() {

				@Override
				public void onFirstClick(View v) {
					titleViews.setBtnName("全部");
					bonusStatus = "";
					refresh();
					doRequestTask();
				}

				@Override
				public void onSecondClick(View v) {
					titleViews.setBtnName("未开奖");
					bonusStatus = "0";
					refresh();
					doRequestTask();
				}

				@Override
				public void onThirdClick(View v) {
					titleViews.setBtnName("已中奖");
					bonusStatus = "1";
					refresh();
					doRequestTask();
				}

				@Override
				public void onFourthClick(View v) {
					titleViews.setBtnName("未中奖");
					bonusStatus = "2";
					refresh();
					doRequestTask();
				}

			});
			mPopDialog.setPopViewPosition();
			mPopDialog.show();
		} else {
			mPopDialog.show();
		}
	}

}
