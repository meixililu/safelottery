package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.AccountListBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.AccountListParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;

/*
 * 账户明细
 */
public class AccountActivity extends ZCHBaseActivity {

	public static final boolean DEBUG = Settings.DEBUG;
	public static final String TAG = "AccountActivity";

	private TextView detailSelect, timeSelect;
	private PopWindowDialog mDialogDetail, mDialogTime;

	private TextView noneText, oldoop;
	private PullToRefreshListView listView;
	private ImageButton refresh;

	private String eventType = "999";// 资金类型
	private String dateType = "";// 时间段
	private int page = 1;
	private int pageTotal = 0;
	private AccountDetailAdapter mAdapter;
	private Animation animation;
	private List<AccountListBean> accountLogList = new ArrayList<AccountListBean>();
	// private String type=null;
	// private String eventCode=null;
	private Dialog dialog;

	private ProgressBar progressbar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account);
		String type = getIntent().getStringExtra("type");
		if (type != null && !type.equals("")) {
			eventType = type;
		}
		initViews();
	}

	private void initViews() {
		progressbar = (ProgressBar) findViewById(R.id.account_progressbar_m);
		detailSelect = (TextView) findViewById(R.id.account_select_text);
		oldoop = (TextView) findViewById(R.id.oldoop);
		timeSelect = (TextView) findViewById(R.id.account_time);
		noneText = (TextView) findViewById(R.id.account_none);
		listView = (PullToRefreshListView) findViewById(R.id.account_list);
		refresh = (ImageButton) findViewById(R.id.lotter_refresh_btn);
		animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
		listView.setOnItemClickListener(itemListener);
		refresh.setOnClickListener(onClickListener);
		detailSelect.setOnClickListener(onClickListener);
		timeSelect.setOnClickListener(onClickListener);
		oldoop.setOnClickListener(onClickListener);
		initData();
	}

	private void initData() {
		mAdapter = new AccountDetailAdapter();
		listView.setAdapter(mAdapter);
		listView.setMode(Mode.BOTH);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				switch (refreshView.getCurrentMode()) {
				case PULL_FROM_START:
					refresh();
					break;
				case PULL_FROM_END:
					if (page <= pageTotal) {
						doRequestTask();
					}
					break;

				default:
					break;
				}
			}
		});

		refresh();
	}

	private String getEventType(String type) {
		if (type.equals("全部")) {
			eventType = "";
		} else if (type.equals("账户充值")) {
			eventType = "001";
		} else if (type.equals("投注退款")) {
			eventType = "002";
		} else if (type.equals("撤单返款")) {
			eventType = "003";
		} else if (type.equals("奖金派送")) {
			eventType = "004";
		} else if (type.equals("活动奖励")) {
			eventType = "005";
		} else if (type.equals("佣金提成")) {
			eventType = "006";
		} else if (type.equals("额度加")) {
			eventType = "007";
		} else if (type.equals("购买彩票")) {
			eventType = "008";
		} else if (type.equals("提现成功")) {
			eventType = "009";
		} else if (type.equals("提现手续费")) {
			eventType = "010";
		} else if (type.equals("额度减")) {
			eventType = "011";
		} else if (type.equals("合买保底")) {
			eventType = "012";
		} else if (type.equals("保底返款")) {
			eventType = "013";
		} else if (type.equals("提现申请")) {
			eventType = "014";
		} else if (type.equals("提现驳回")) {
			eventType = "015";
		}
		return eventType;
	}

	private void showDialogDetail() {
		if (mDialogDetail == null) {
			//敢得当前控件所在的位置
			LinearLayout lny = (LinearLayout) findViewById(R.id.account_select);
			int popDialogY = lny.getTop() + lny.getHeight();
			
			final ArrayList<String> temp = Settings.getAccountType();
			final String[] item = (String[]) temp.toArray(new String[temp.size()]);
			mDialogDetail = new PopWindowDialog(this, item, 0);
			mDialogDetail.setPopViewPosition(0, popDialogY);
			
			mDialogDetail.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogDetail.getList();
					CheckBox checkBox;

					for (int i = 0, size = list.size(); i < size; i++) {
						checkBox = list.get(i);
						if (i == v.getId()) {
							checkBox.setChecked(true);
							getEventType(checkBox.getText().toString());
							detailSelect.setText(temp.get(i));
							refresh();
						} else {
							if (checkBox.isChecked())
								checkBox.setChecked(false);
						}
					}
					mDialogDetail.dismiss();
				}
			});
			
			mDialogDetail.show();
		} else {
			if(mDialogDetail.isShowing())
				mDialogDetail.dismiss();
			else
				mDialogDetail.show();
		}
	}

	private void showDialogTime() {
		if (mDialogTime == null) {
			//敢得当前控件所在的位置
			LinearLayout lny = (LinearLayout) findViewById(R.id.account_select);
			int popDialogY = lny.getTop() + lny.getHeight();
			
			final String[] item = (String[]) Settings.getAccountTime().toArray(new String[Settings.getAccountTime().size()]);
			mDialogTime = new PopWindowDialog(this, item, 0);
			mDialogTime.setPopViewPosition(0, popDialogY);
			
			mDialogTime.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogTime.getList();
					CheckBox checkBox;

					for (int i = 0, size = list.size(); i < size; i++) {
						checkBox = list.get(i);
						if (i == v.getId()) {
							checkBox.setChecked(true);
							dateType = "0" + v.getId();// 时间类型 00全部
														// 01当天,02最近一周,03最近一月,04最近三月,05最近半年
							refresh();
							timeSelect.setText(item[i]);
						} else {
							if (checkBox.isChecked())
								checkBox.setChecked(false);
						}
					}
					mDialogTime.dismiss();
				}
			});

			mDialogTime.show();
		} else {
			if(mDialogTime.isShowing())
				mDialogTime.dismiss();
			else
				mDialogTime.show();
		}
	}

	// private void getType(String eventcode) {
	// if (eventcode.equals("00000")) {
	// type="充值成功";
	// eventCode= "账户充值";
	// } else if (eventcode.equals("00100")) {
	// type="出票失败";
	// eventCode= "投注失败";
	// } else if (eventcode.equals("00101")) {
	// type="撤单";
	// eventCode= "投注撤销";
	// } else if (eventcode.equals("00103")) {
	// type="合买撤资";
	// eventCode= "投注撤单";
	// } else if (eventcode.equals("00104")) {
	// type="追号失败";
	// eventCode= "投注失败";
	// } else if (eventcode.equals("00105")) {
	// type="追号撤单";
	// eventCode= "投注撤单";
	// } else if (eventcode.equals("00106")) {
	// type="赠送失败";
	// eventCode= "派奖";
	// } else if (eventcode.equals("00200")) {
	// type="返奖成功";
	// eventCode= "账户返奖";
	// } else if (eventcode.equals("00400")) {
	// type="合买返佣";
	// eventCode= "账户返佣";
	// } else if (eventcode.equals("00700")) {
	// type="赠金";
	// eventCode= "赠金";
	// } else if (eventcode.equals("10000")) {
	// type="购买成功";
	// eventCode= "购买彩票";
	// } else if (eventcode.equals("10001")) {
	// type="追号成功";
	// eventCode= "购买彩票";
	// } else if (eventcode.equals("10003")) {
	// type="认购成功";
	// eventCode= "购买彩票";
	// } else if (eventcode.equals("10004")) {
	// type="赠送成功";
	// eventCode= "赠送彩票";
	// } else if (eventcode.equals("20000")) {
	// type="提现冻结";
	// eventCode= "提现申请";
	// } else if (eventcode.equals("20001")) {
	// type="保底冻结";
	// eventCode= "合买保底";
	// } else if (eventcode.equals("20100")) {
	// type="提现失败";
	// eventCode= "提现驳回";
	// } else if (eventcode.equals("20101")) {
	// type="保底退还";
	// eventCode= "保底解冻";
	// }
	// }

	class AccountDetailAdapter extends BaseAdapter {

		private LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public int getCount() {

			return accountLogList.size();
		}

		public Object getItem(int position) {

			return accountLogList.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {

				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.account_detail_item, null);
				holder.time = (TextView) convertView.findViewById(R.id.account_detail_item_time);
				holder.sort = (TextView) convertView.findViewById(R.id.account_detail_item_sort);
				TextView notes = (TextView) convertView.findViewById(R.id.account_detail_item_notes);

				holder.moneyValue = (TextView) convertView.findViewById(R.id.account_detail_item_money_value);
				holder.sortValue = (TextView) convertView.findViewById(R.id.account_detail_item_sort_value);
				holder.notesValue = (TextView) convertView.findViewById(R.id.account_detail_item_notes_value);

				notes.setText("备注");
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AccountListBean mBean = (AccountListBean) accountLogList.get(position);

			holder.time.setText(TimeUtils.getLongtimeToShorttime(mBean.getCreateTime()));
			holder.moneyValue.setText(mBean.getChangeAmount()); //转成整数
			// getType(accountListBean.getEventCode());
			String eventname = mBean.getEventName();
			if (eventname.contains("-")) {
				String str[] = eventname.split("-");
				holder.sortValue.setText(str[1]);
			} else {
				holder.sortValue.setText(eventname);
			}
			String memo = mBean.getMemo().replace("支付", "");

			if (mBean.getEventCode().equals("10200")) {
				holder.notesValue.setText("提现交易");
			} else {
				holder.notesValue.setText(memo);
			}
			if (mBean.getEventType().equals("00") && mBean.getEventType().equals("0")) {
				holder.sort.setText("充值类型");
			} else {
				holder.sort.setText("资金类型");
			}
			return convertView;
		}
	}

	static class ViewHolder {

		private TextView time;
		private TextView sort;
		private TextView moneyValue;
		private TextView sortValue;
		private TextView notesValue;

	}

	private OnItemClickListener itemListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AccountListBean mBean = (AccountListBean) accountLogList.get(position - 1);
			if (mBean.getType().equals("00") && mBean.getEventType().equals("1")) {
				if(mBean.getBuyType().equals("2")){
					Intent intent = new Intent();
					intent.setClass(AccountActivity.this, RecordBetCombineAgentActivity.class);
					intent.putExtra("programsOrderId", mBean.getProgramsOrderId());
					intent.putExtra("userCode", GetString.userInfo.getUserCode());
					startActivity(intent);
				}else{
					Intent intent = new Intent();
					intent.setClass(AccountActivity.this, RecordBetAgentActivity.class);
					intent.putExtra("programsOrderId", mBean.getProgramsOrderId());
					intent.putExtra("userCode", GetString.userInfo.getUserCode());
					startActivity(intent);
				}
			} else {
				showAwardDetailDialog(mBean);
			}
		}
	};

	/*
	 * 明细详情弹出框
	 */
	private void showAwardDetailDialog(AccountListBean mBean) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.account_detail_dialog, null);

		TextView time = (TextView) view.findViewById(R.id.account_detail_time);
		TextView money = (TextView) view.findViewById(R.id.account_detail_amount);
		TextView sort = (TextView) view.findViewById(R.id.account_detail_short);
		TextView status = (TextView) view.findViewById(R.id.account_detail_status);
		TextView order_id = (TextView) view.findViewById(R.id.account_detail_order_id);
		TextView memo = (TextView) view.findViewById(R.id.account_detail_memo);
		Button buyall = (Button) view.findViewById(R.id.account_detail_close);

		ImageView line4 = (ImageView) view.findViewById(R.id.account_detail_line4);
		
		line4.setVisibility(View.GONE);
		status.setVisibility(View.GONE);

		if (mBean.getEventType().equals("00") && mBean.getEventType().equals("0")) {
			sort.setText("充值类型：" + mBean.getEventName());
		} else {
			sort.setText("资金类型：" + mBean.getEventName());
		}
		
		
//		status.setText("奖励原因：" + mBean.getEventName());
		time.setText("交易时间：" + mBean.getCreateTime());
		money.setText("发生额：" + mBean.getChangeAmount());
		order_id.setText("订单号：" + mBean.getOrderId());
		memo.setText("备注：" + mBean.getMemo());
		
		buyall.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = new Dialog(this, R.style.dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();
	}

	private void refresh(){
		page = 1;
		pageTotal = 0;
		doRequestTask();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == refresh.getId()) {
				refresh();
			} else if (v.getId() == R.id.account_select_text) {
				showDialogDetail();
			} else if (v.getId() == R.id.account_time) {
				showDialogTime();
			}else if(v.getId() == R.id.oldoop){
				Intent intent = new Intent(AccountActivity.this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL,RecordBetActivity.oldv);
				AccountActivity.this.startActivity(intent);
			}
		}
	};

	private void doRequestTask() {
		SafelotteryHttpClient.post(this, "3201", "list", initDate(), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				pageTotal = ConversionUtil.StringToInt((String) mMap.get("pageTotal"));
				String accountLogListStr = (String) mMap.get("accountLogList");
				List list = (List<AccountListBean>) JsonUtils.parserJsonArray(accountLogListStr, new AccountListParser());
				if (list != null) {
					if (list.size() == 0 && page == 1) {
						noneText.setVisibility(View.VISIBLE);
						accountLogList.clear();
						mAdapter.notifyDataSetChanged();
					} else {
						if(page == 1)
							accountLogList.clear();
						
						noneText.setVisibility(View.GONE);
						accountLogList.addAll(list);
						mAdapter.notifyDataSetChanged();
						page++;
					}
					if(page > pageTotal){
						listView.setMode(Mode.PULL_FROM_START);
					}else{
						listView.setMode(Mode.BOTH);
					}
				} else {
					ToastUtil.diaplayMesShort(getApplicationContext(), "暂无数据");
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				if (accountLogList.size() == 0) {
					noneText.setVisibility(View.VISIBLE);
				} else {
					noneText.setVisibility(View.GONE);
				}
				listView.onRefreshComplete();
			}
			
			@Override
			public void onStart(){
				if (accountLogList.size() == 0 ) 
					progressbar.setVisibility(View.VISIBLE);
				
				noneText.setVisibility(View.GONE);
				refresh.startAnimation(AnimationUtil.getRotateCenter(AccountActivity.this));
			}
			
			@Override
			public void onFinish(){
				progressbar.setVisibility(View.GONE);
				refresh.clearAnimation();
				listView.onRefreshComplete();
			}
		});
	}

	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		map.put("eventType", eventType);
		map.put("dateType", dateType);
		map.put("page", page);
		map.put("pageSize", "10");
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

}