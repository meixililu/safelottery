package com.zch.safelottery.combine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.bean.ResultListBean;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.combinebean.CbHallListItemBean;
import com.zch.safelottery.combinebean.CbRequestBean;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.parser.CbHallListItemParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.ResultListParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.StatusUtil;
import com.zch.safelottery.util.XmlUtil;

public class CombineViewPageItem {

	private Context mContext;
	private LayoutInflater inflater;
	private View combineView;

	private PullToRefreshListView lv;
	private MyListViewAdapter lv_adapter;
	private ProgressBar progressBar;
	private ImageView faileImg;
	private TextView faileText;

	// 搜索
	private LinearLayout searchLay;
	private EditText searchEdit;
	private Button searchBtn;

	// 发给服务器的数据
	private String issue;
	private String orderStatus;
	private String privacy;
	private String baoDi;
	private String isFull;
	private String sort;// 排序 1 进度降序 2 进度升序 3 总金额降序 4 总金额升序

	/** 搜索用户名 **/
	private String nickName;

	private int page;
	private int pageSize = 10;
	private int pageTotal;

	private String lid;// 彩种
	private String playId;// 玩法

	/** 同步任务 后台线程是否在加载 **/
	private boolean isAsyncTask = false;

	/**放前面页面的信息**/
	private List<CbHallListItemBean> resultList = new ArrayList<CbHallListItemBean>();

	private long amount;

	public CombineViewPageItem(Context context) {
		// super(context);
		// TODO Auto-generated constructor stub
	}

	public CombineViewPageItem(Context context, String lid) {
		// super(context);
		this.mContext = context;
		this.lid = lid;

		inflater = LayoutInflater.from(context);

		initView();
		initData();
	}

	/**
	 * 设置彩种
	 * @param lid 彩种
	 */
	public void setLid(String lid) {
		// 当选择不同彩种时才重新加载内容
		if (lid != null) {
			this.lid = lid;
		} else {
			this.lid = LotteryId.All;
			LogUtil.CustomLog("TAG", "传入的彩种为null");
		}
		refresh();
	}

	/**
	 * 选择排序跟类型
	 * @param sort 1 进度降序  2 进度升序  3 总金额降序 4 总金额升序
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * 搜索 
	 * @param nickName 要搜索的相关字段
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 打开搜索功能
	 * @param bool 是否打开搜索功能 true打开 false关闭
	 */
	public void setSearchOpen(boolean bool) {
		searchLay.setVisibility(bool ? View.VISIBLE : View.GONE);
	}

	public View getCombineView() {
		LinearLayout.LayoutParams lParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		combineView.setLayoutParams(lParams);

		return combineView;
	}

	private void initView() {
		combineView = inflater.inflate(R.layout.combine_lottery_page_item, null);
		lv = (PullToRefreshListView) combineView.findViewById(R.id.combine_lottery_listView);
		progressBar = (ProgressBar) combineView.findViewById(R.id.combine_lottery_progressbar_m);
		faileImg = (ImageView) combineView.findViewById(R.id.connection_faile_img);
		faileText = (TextView) combineView.findViewById(R.id.connection_faile_text);

		searchLay = (LinearLayout) combineView.findViewById(R.id.combin_lottery_search);
		searchEdit = (EditText) combineView.findViewById(R.id.combin_lottery_search_edit);
		searchBtn = (Button) combineView.findViewById(R.id.combin_lottery_search_btn);

		searchBtn.setOnClickListener(onClickListener);
	}

	private void initData() {
		lv_adapter = new MyListViewAdapter(mContext);
		lv.setAdapter(lv_adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0 && position <= resultList.size()) {
					Intent intent = new Intent(mContext, CombineToBuyActivity.class);
					intent.putExtra("programsOrderId", resultList.get(position - 1).getProgramsOrderId());
					mContext.startActivity(intent);
				} else {
					LogUtil.CustomLog("TAG", "下标不对，当前选中 : " + (position - 1));
				}
			}
		});

		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				PullToRefreshBase.Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					refresh();
					break;
				case PULL_FROM_END:
					if (page <= pageTotal) {
						startTask();
					}
					break;
				default:
					break;
				}
			}
		});

		// refresh();
	}

	/**
	 * 刷新
	 */
	public void refresh() {
		LogUtil.CustomLog("TAG", "CombineViewPageItem -- refresh");
		page = 1;
		startTask();
	}

	/**
	 * 启动连接
	 */
	private void startTask() {
		startTask(nickName);
	}

	/**
	 * 启动连接
	 */
	private void startTask(String nickName) {
		CbRequestBean bean = new CbRequestBean();
		if (lid.equals(LotteryId.RX9)) { // 把任选9的ID 转换成 SFC 玩法"02"
			bean.setLotteryId(LotteryId.SFC);
			playId = "02";
		} else if (lid.equals(LotteryId.SFC)) {
			bean.setLotteryId(lid);
			playId = "01";
		} else {
			bean.setLotteryId(lid);
			playId = "";
		}
		bean.setNickName(nickName);
		// bean.setUserCode(GetString.userInfo.getUserCode());
		bean.setPlayId(playId);
		bean.setIssue(issue);
		bean.setOrderStatus(orderStatus);
		bean.setPrivacy(privacy);
		bean.setBaoDi(baoDi);
		bean.setIsFull(isFull);
		bean.setSort(sort);
		bean.setPage(page);
		bean.setPageSize(pageSize);

		Gson gson = new Gson();
		String requestData = gson.toJson(bean);

		if (TextUtils.isEmpty(nickName)) {
			MyAsyncTask("programs", requestData);
		} else {
			searchBtn.setEnabled(false);
			MyAsyncTask("programsNick", requestData);
		}
	}

	/**
	 * listview item 上面直接进行认购，暂不需要
	 */
	private void startBuyTask(CbHallListItemBean mBean) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("programsOrderId", mBean.getProgramsOrderId());
		map.put("amount", String.valueOf(amount));
		BuyLotteryTask mBuyLotteryTask = new BuyLotteryTask(mContext, "subscribe", JsonUtils.toJsonStr(map));
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {

			}
		});
		mBuyLotteryTask.send();
	}

	private void setData(String result) {
		if (!TextUtils.isEmpty(result)) {
			if (page == 1)
				resultList.clear();
			List<CbHallListItemBean> list = (List<CbHallListItemBean>) JsonUtils.parserJsonArray(result, new CbHallListItemParser());
			resultList.addAll(list);
			lv_adapter.notifyDataSetChanged();
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.combin_lottery_search_btn) { // 开始查找
				StatService.onEvent(mContext, "join-search", "合买-搜索提交按钮", 1);

				nickName = searchEdit.getText().toString();
				setNickName(nickName);
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);

				// 发送请求　接收到的数据放到resultList 里
				refresh();
			}
		}
	};

	protected void onPreExecute() {
		if (resultList.size() == 0)
			progressBar.setVisibility(View.VISIBLE);
		faileImg.setVisibility(View.GONE);
		faileText.setVisibility(View.GONE);
	}

	private void MyAsyncTask(String params1, String params2) {
		onPreExecute();
		SafelotteryHttpClient.post(mContext, "3303", params1, params2,
				new TypeSafelotteryHttpResponseHandler(mContext, true, new ResultListParser()) {
					@Override
					public void onSuccess(int statusCode, SafelotteryType response) {
						if (response != null) {
							if (response instanceof ResultListBean) {
								ResultListBean mBean = (ResultListBean) response;
								page = ConversionUtil.StringToInt(mBean.getPage());
								pageSize = ConversionUtil.StringToInt(mBean.getPageSize());
								pageTotal = ConversionUtil.StringToInt(mBean.getPageTotal());
								setData(mBean.getProgramsList());

								page++;
								if (page > pageTotal) {
									lv.setMode(Mode.PULL_FROM_START);
								} else {
									lv.setMode(Mode.BOTH);
								}
							}
							if (resultList.size() == 0) {
								faileText.setText("暂无数据，下拉刷新");
								faileText.setVisibility(View.VISIBLE);
							}
						}
					}

					@Override
					public void onFailure(int statusCode, String mErrorMsg) {
						if (page == 1) {
							faileImg.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFinish() {
						searchBtn.setEnabled(true);
						lv.onRefreshComplete();
						progressBar.setVisibility(View.GONE);
					}
				});
	}

	private class MyListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private CbHallListItemBean mBean;

		public MyListViewAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return resultList.size();
		}

		public Object getItem(int position) {
			return resultList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.combine_lottery_items, null);
				holder.percent = (LinearLayout) convertView.findViewById(R.id.combine_lottery_percent);
				holder.userName = (TextView) convertView.findViewById(R.id.combine_lottery_user_name);
				holder.moneyTotal = (TextView) convertView.findViewById(R.id.combine_lottery_money_total);
				holder.moneyBuy = (TextView) convertView.findViewById(R.id.combine_lottery_money_buy);
				holder.lotteryName = (TextView) convertView.findViewById(R.id.combine_lottery_name);
				holder.commission = (TextView) convertView.findViewById(R.id.combine_lottery_commission);
				holder.orderStatu = (TextView) convertView.findViewById(R.id.combine_lottery_order_statu);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.percent.removeAllViews();

			mBean = resultList.get(position);
			holder.percent.addView(XmlUtil.getPercent(mContext, (int) ConversionUtil.StringToDouble(mBean.getBuyPercent().replace("%", "")),
					(int) ConversionUtil.StringToDouble(mBean.getLastPercent().replace("%", ""))));
			holder.userName.setText(mBean.getNickName());
			holder.moneyTotal.setText(mBean.getTotalWere() + "元");
			holder.moneyBuy.setText(mBean.getSurplusWere() + "元");
			holder.lotteryName.setText(LotteryId.getLotteryName(mBean.getLotteryId(), mBean.getPlayId()));
			holder.commission.setText("佣" + mBean.getCommission().replace("%", "") + "%");
			holder.orderStatu.setText(StatusUtil.getOrderStatus(mBean.getOrderStatus()));

			return convertView;
		}
	}

	static class ViewHolder {
		LinearLayout percent;
		TextView userName;
		TextView moneyTotal;
		TextView moneyBuy;
		TextView lotteryName;
		TextView commission;
		TextView orderStatu;
	}

}
