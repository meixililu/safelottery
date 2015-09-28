package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.adapter.ViewPagerAdapter;
import com.zch.safelottery.bean.BannerBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.LotteryInfoBean;
import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.database.BuyLotteryDatabaseUtil;
import com.zch.safelottery.dialogs.WorldcupDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.impl.LotteryIssueChangeListener;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.parser.UserInfoParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.BuyLotteryPageHelper;
import com.zch.safelottery.setttings.SLManifest;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.SharedPreferencesOperate;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;

public class BuyLotteryActivity extends BaseTabActivity {

	public static final long oneDay = 24 * 60 * 60 * 1000;
	public static final long oneHour = 3600 * 1000;
	public static final long oneMinute = 60 * 1000;
	public static final long threeMinute = 5 * 60 * 1000;
	//
	private Button logOrReg;

	/**checked为老的彩种列表界面，未选中为定制界面**/
	private ProgressBar progressbar_m;
	private ImageView connection_faile_img;
	private ViewPager viewpager_ad;
	private LinearLayout viewpager_dot_layout;
	private RelativeLayout buy_lottery_ad;
	private LinearLayout tableLayout, listLayout;
	private LinearLayout rlvContentView;

	/** 可刷新的scrollview **/
	private PullToRefreshScrollView listLv;
	private ArrayList<LotteryInfoBean> result_list;
	private ArrayList<View> adViews;// 广告页面list
	private ArrayList<View> tableViews;
	private String current_lid;
	/**存放所有彩种信息的map，整个app使用,根据彩种id获取相应数据**/
	public static Map<String, LotteryInfoBean> lotteryInfoBeansMap;

	private LayoutInflater mInflater;
	/** 倒计时类list，可以统一关闭 **/
	private ArrayList<MyCount> allCountDownThread;
	private SharedPreferences spf;

	private String todayLotteryStr;
	public static Context mContext;
	private ArrayList<BannerBean> mBannerList;
	private LotteryIssueChangeListener mLotteryIssueChangeListener;

	private Timer mTimer;
	private MyTimerTask mMyTimerTask;
	private static final int delayed = 6000;
	private boolean isRunAdTask;
	private int currentIndex;
	
	/** 执行动画 **/
	public Handler TaskHandle = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				onBannerPostExecute();
				break;
			case 2:
				autoChangeAd();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.buy_lottery);
			mContext = this;
			initUI();
			getUserInfo();
			current_lid = LotteryId.All;
			RequsetDataTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 取得保存的用户信息
	private void getUserInfo() {
		String info = SharedPreferencesOperate.getUserInfo(this, SharedPreferencesOperate.SAVE_USER_INFO);
		if (!TextUtils.isEmpty(info)) {
			GetString.isLogin = true;
			GetString.isAccountNeedReSet = true;
			GetString.userInfo = (UserInfoBean) JsonUtils.parserJsonBean(info, new UserInfoParser());
			LogUtil.DefalutLog("SafeApplication: " + GetString.userInfo.toString());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			if (GetString.isLogin) {
				logOrReg.setVisibility(View.GONE);
			} else {
				logOrReg.setVisibility(View.VISIBLE);
			}
			if (isRunAdTask) {
				if (mTimer != null) {
					mTimer.cancel();
					if (mMyTimerTask != null) {
						mMyTimerTask.cancel();
					}
					mTimer = new Timer();
					mMyTimerTask = new MyTimerTask();
					mTimer.schedule(mMyTimerTask, delayed, delayed);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (isRunAdTask) {
			cancleADTimmer();
		}
	}

	/**
	 * 初始化页面控件，注册按钮事件
	 */
	private void initUI() {
		spf = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		mInflater = LayoutInflater.from(this);
		saveNewestLotterOrder();
		LinearLayout.LayoutParams mParamsParent = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rlvContentView = new LinearLayout(this);
		rlvContentView.setLayoutParams(mParamsParent);
		rlvContentView.setOrientation(LinearLayout.VERTICAL);

		tableLayout = new LinearLayout(this);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		tableLayout.setLayoutParams(mParams);
		tableLayout.setOrientation(LinearLayout.VERTICAL);
//		listLayout = new LinearLayout(this);
//		listLayout.setOrientation(LinearLayout.VERTICAL);

		connection_faile_img = (ImageView) findViewById(R.id.connection_faile_img);
		logOrReg = (Button) findViewById(R.id.buy_lottery_login_or_register);
		progressbar_m = (ProgressBar) findViewById(R.id.lottery_result_hall_progressbar_m);
		listLv = (PullToRefreshScrollView) findViewById(R.id.buy_lottery_listview);
		
		View adViewLayout = mInflater.inflate(R.layout.buy_lottery_ad_layout, null);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		buy_lottery_ad = (RelativeLayout) adViewLayout.findViewById(R.id.buy_lottery_ad);
		buy_lottery_ad.setLayoutParams(mLayoutParams);
		buy_lottery_ad.setVisibility(View.GONE);
		viewpager_ad = (ViewPager) adViewLayout.findViewById(R.id.viewpager_ad);
		viewpager_dot_layout = (LinearLayout) adViewLayout.findViewById(R.id.viewpager_dot_layout);

		View motu_worldcup_layout = mInflater.inflate(R.layout.buy_lottery_motu_line, null);
		FrameLayout index_page_motu_item = (FrameLayout) motu_worldcup_layout.findViewById(R.id.index_page_motu_item);
		FrameLayout index_page_worldcup_item = (FrameLayout) motu_worldcup_layout.findViewById(R.id.index_page_worldcup_item);
		index_page_motu_item.setOnClickListener(motuClickListener);
		index_page_worldcup_item.setOnClickListener(motuClickListener);
		
		// 今日开奖的彩种
		todayLotteryStr = BuyLotteryPageHelper.getTodayLottryIdArray(TimeUtils.getOnlyWeek(System.currentTimeMillis()));
		MyClickListenre myClickListenre = new MyClickListenre();
		logOrReg.setOnClickListener(myClickListenre);
		connection_faile_img.setOnClickListener(myClickListenre);

		adViews = new ArrayList<View>();
		tableViews = new ArrayList<View>();
		allCountDownThread = new ArrayList<MyCount>();
		result_list = new ArrayList<LotteryInfoBean>();

		listLv.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				refresh();
			}
		});
		viewpager_ad.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				restartADCounter();
				return false;
			}
		});
		rlvContentView.addView(buy_lottery_ad);
		rlvContentView.addView(motu_worldcup_layout);
		rlvContentView.addView(tableLayout);
		listLv.addView(rlvContentView);
	}
	
	private OnClickListener motuClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.index_page_motu_item){
				toMotuActivity();
				StatService.onEvent(BuyLotteryActivity.this, "app-left-button", "首页-快速入口-左边按钮", 1);
			}else if(v.getId() == R.id.index_page_worldcup_item){
				toJZActivity();
				StatService.onEvent(BuyLotteryActivity.this, "app-right-button", "首页-快速入口-右边按钮", 1);
			}
		}
	};
	
	private void toMotuActivity(){
		Intent intent = new Intent(this,MotuActivity.class);
		startActivity(intent);
	}
	private void toJZActivity(){
		Intent intent = new Intent(this,JZActivity.class);
		startActivity(intent);
	}


	private void restartADCounter() {
		cancleADTimmer();
		mTimer = new Timer();
		mMyTimerTask = new MyTimerTask();
		mTimer.schedule(mMyTimerTask, delayed, delayed);
	}

	private void cancleADTimmer() {
		if (mTimer != null) {
			mTimer.cancel();
		}
		if (mMyTimerTask != null) {
			mMyTimerTask.cancel();
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int pos) {
			changeState(pos);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	private void changeState(int pos) {
		currentIndex = pos;
		if (viewpager_dot_layout.isShown()) {
			int size = viewpager_dot_layout.getChildCount();
			for (int i = 0; i < size; i++) {
				View mView = viewpager_dot_layout.getChildAt(i);
				if (i == pos) {
					mView.setEnabled(true);
				} else {
					mView.setEnabled(false);
				}
			}
		}
	}

	private void saveNewestLotterOrder() {
		List<String> newestList = new BuyLotteryDatabaseUtil(this).getDataList();
		if (newestList != null) {
			StringBuilder sbBuilder = new StringBuilder();
			for (int i = 0, j = newestList.size(); i < j; i++) {
				sbBuilder.append(newestList.get(i));
				sbBuilder.append("#");
			}
			int end = sbBuilder.lastIndexOf("#");
			if (end > 0) {
				String sub = sbBuilder.deleteCharAt(end).toString();
				Settings.saveSharedPreferences(spf, LotteryInfoParser.LotteryOrderStrKey, sub);
				Settings.saveSharedPreferences(spf, LotteryInfoParser.LotteryOrderDateKey, System.currentTimeMillis());
			}
		}
	}

	private void skipIntent(String lid) {
		try {
			if (getLoadValue(lid)) {
				Intent intent = getLotteryIntent(BuyLotteryActivity.this, lid);
				if (intent != null) {
					SafeApplication.dataMap.put("BuyLotteryActivity", BuyLotteryActivity.this);
					BuyLotteryActivity.this.startActivity(intent);
				}
				if (!TextUtils.isEmpty(lid)) {
					new BuyLotteryDatabaseUtil(this).updateClickNumber(lid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**验证彩种是否停售
	 * @param lid
	 * @return
	 */
	private boolean getLoadValue(String lid) {
		LotteryInfoBean item = LotteryId.getLotteryInfoBeanByLid(lid);
		if (item == null) {
			ToastUtil.diaplayMesShort(mContext, "暂无数据，请下拉刷新！");
			return false;
		}
		/**彩种是否停售,0正常销售,1 停售**/
		if (item.getIsStop().equals("0")) {
			if (item.getPrompt().equals("")) {
				if (item.getIssueInfoList() != null && item.getIssueInfoList().size() > 0) {
					return true;
				} else {
					ToastUtil.diaplayMesShort(mContext, "亲，很抱歉，期次维护中，请稍后重试！");
					return false;
				}
			} else {
				ToastUtil.diaplayMesShort(mContext, item.getPrompt());
				return false;
			}
		} else {
			ToastUtil.diaplayMesShort(mContext, "亲，该彩种暂停销售，即将开售，敬请期待！");
			return false;
		}
	}

	/**
	 * 刷新
	 */
	private void refresh() {
		if (result_list != null) {
			result_list.clear();
		}
		current_lid = LotteryId.All;
		RequsetDataTask();
	}

	/**
	 * 解析返回数据
	 * @throws Exception
	 */
	private void unparsedData() throws Exception {
		if (result_list != null) {
			lotteryInfoBeansMap = LotteryInfoParser.changeLotterInfoListToMap(this, spf, todayLotteryStr, result_list);
			showDataList();
		}
	}

	/**
	 * 显示彩种倒计时列表
	 * @param map
	 */
	private void showDataList() {
		if (lotteryInfoBeansMap != null) {
			tableViews.clear();
			String orderstr = spf.getString(LotteryInfoParser.LotteryOrderStrKey, "");
			if (!TextUtils.isEmpty(orderstr)) {
				String orderarray[] = orderstr.split("#");
				for (int i = 0, j = orderarray.length; i < j; i++) {
					LotteryInfoBean bean = lotteryInfoBeansMap.get(orderarray[i]);
					if (bean != null) {
						getLotteryListView(bean);
						tableViews.add(getLotteryTableItem(bean));
					}
				}
			}
			BuyLotteryPageHelper.showTableView(this, tableViews, tableLayout);
			rlvContentView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 *  生成一个彩种table item
	 */
	private View getLotteryTableItem(final LotteryInfoBean item) {
		View convertView = mInflater.inflate(R.layout.buy_lottery_tableview_item, null);
		try {
			ImageView item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
			TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
			TextView item_msg = (TextView) convertView.findViewById(R.id.item_msg);
			ImageView item_today = (ImageView) convertView.findViewById(R.id.item_today);
			ImageView item_hot = (ImageView) convertView.findViewById(R.id.item_hot);
			TextView item_reward = (TextView) convertView.findViewById(R.id.item_reward);

			final String lid = item.getLotteryId();
			item_icon.setImageResource(LotteryId.getLotteryIcon(lid));
			item_name.setText(LotteryId.getLotteryName(lid));
			if (item.isTodayLottery()) {
				item_today.setVisibility(View.VISIBLE);
			}
			if (lid.equals(LotteryId.K3) || lid.equals(LotteryId.K3X)) {
				item_hot.setVisibility(View.VISIBLE);
			}
			if (item.getIsStop().equals("0")) {
				if (lid.equals(LotteryId.JCZQ) || lid.equals(LotteryId.JCLQ) || lid.equals(LotteryId.BJDC)) {
					item_msg.setText(LotteryId.getLotterySubtitle(lid));
				} else {
					if (item.getIssueInfoList() != null) {
						if (item.getIssueInfoList().size() > 0) {
							IssueInfoBean mIssueInfoBean = item.getIssueInfoList().get(0);
							if (lid.equals(LotteryId.SSQ) || lid.equals(LotteryId.DLT)) {
								item_msg.setText(getMoneyForShot(mIssueInfoBean.getPrizePool(), lid));
//								item_msg.setBackgroundResource(R.drawable.jiangchi_bg);
							} else {
								item_msg.setText(LotteryId.getLotterySubtitle(lid));
							}
						} else {
							item_msg.setText("暂无期次");
							item.setPrompt("暂无期次，请下拉刷新重试");
						}
					} else {
						item_msg.setText("暂无期次");
						item.setPrompt("暂无期次，请下拉刷新重试");
					}
				}
			} else if (item.getIsStop().equals("1")) {
				item_msg.setText("暂停销售");
				item.setPrompt("暂停销售，请稍后下拉刷新重试");
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					skipIntent(lid);
				}
			});

		} catch (Exception e) {
			LogUtil.ExceptionLog("getLotteryItemView");
			e.printStackTrace();
		}
		return convertView;
	}

	/**
	 * 生成一个彩种倒计时list item
	 * @param item
	 * @return
	 */
	private void getLotteryListView(final LotteryInfoBean item) {
		try {
			final String lid = item.getLotteryId();
			if (item.getIsStop().equals("0")) {
				if (item.getIssueInfoList() != null) {
					if (item.getIssueInfoList().size() > 0) {
						IssueInfoBean mIssueInfoBean = item.getIssueInfoList().get(0);
						if (mIssueInfoBean != null) {
							MyCount count = new MyCount(mIssueInfoBean.getLeftTime(), 1000, item);
							count.start();
							allCountDownThread.add(count);
						}
					} else {
						item.setPrompt("暂无期次，请下拉刷新重试");
					}
				} else {
					item.setPrompt("暂无期次，请下拉刷新重试");
				}
			} else if (item.getIsStop().equals("1")) {
				item.setPrompt("暂停销售，请稍后下拉刷新重试");
			}
		} catch (Exception e) {
			LogUtil.ExceptionLog("getLotteryItemView");
			e.printStackTrace();
		}
	}

	private void onPreExecute() {
		BannerDataTask();
		connection_faile_img.setVisibility(View.GONE);
		rlvContentView.setVisibility(View.GONE);
		tableLayout.removeAllViews();
//		listLayout.removeAllViews();
		cancleCountDownThread(allCountDownThread);
		progressbar_m.setVisibility(View.VISIBLE);
	}

	private void RequsetDataTask() {
		onPreExecute();
		SafelotteryHttpClient.post(mContext, "3301", "", "", new TypeMapHttpResponseHandler(mContext, true) {
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try {
					result_list = (ArrayList<LotteryInfoBean>) JsonUtils.parserJsonArray((String) mMap.get("issueList"), new LotteryInfoParser());
					unparsedData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				buy_lottery_ad.setVisibility(View.GONE);
				connection_faile_img.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFinish() {
				onFinishRequest();
			}
		});
	}

	private void onFinishRequest() {
		progressbar_m.setVisibility(View.GONE);
		listLv.onRefreshComplete();
	}

	/**根据彩种id取消指定彩种的倒计时
	 * @param lid
	 */
	private void cancleCountDownByLid(String lid) {
		for (MyCount count : allCountDownThread) {
			String id = count.lid;
			if (lid.equals(id)) {
				count.cancel();
			}
		}
	}

	/**
	 * 倒计时类
	 * @author Messi
	 * 
	 */
	class MyCount extends CountDownTimer {

		private String lid;
		private String status;
		private LotteryInfoBean mLotteryInfoBean;
		private IssueInfoBean mIssueInfoBean;
		private long countdownMinute;
		private boolean isNeedRefresh;

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public MyCount(long millisInFuture, long countDownInterval, LotteryInfoBean item) {
			super(millisInFuture, countDownInterval);
			countdownMinute = millisInFuture;
			int size = item.getIssueInfoList().size();
			if (size > 0) {
				mIssueInfoBean = item.getIssueInfoList().get(0);
				status = mIssueInfoBean.getStatus();
			} else {
				mIssueInfoBean = new IssueInfoBean();
			}
			this.mLotteryInfoBean = item;
			this.lid = item.getLotteryId();
			if (lid.equals(LotteryId.SSCJX) && status.equals("0")) {
				if (countdownMinute > threeMinute) {
					isNeedRefresh = true;
				}
			}
		}

		@Override
		public void onFinish() {
			mLotteryInfoBean.setPrompt("正在获取下一期数据");
			mIssueInfoBean.setStatus("3");
			this.cancel();
			new Handler().postDelayed(new Runnable() {
				public void run() {
					getNextIssueDataTask();
				}
			}, 2000);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mIssueInfoBean.setLeftTime(millisUntilFinished);
			if (isNeedRefresh) {
				if ((countdownMinute - millisUntilFinished) > threeMinute) {
					countdownMinute = millisUntilFinished;
					getNextIssueDataTask();
				}
			}
		}

		/**内部类，共用倒计时里面的变量**/
		private void getNextIssueDataTask() {
			LogUtil.DefalutLog("---NextIssueDataTask---");
			HashMap<String, String> para = new HashMap<String, String>();
			para.put("lotteryId", lid);
			SafelotteryHttpClient.post(mContext, "3301", "", JsonUtils.toJsonStr(para), new TypeMapHttpResponseHandler(mContext, false) {
				@Override
				public void onSuccess(int statusCode, Map mMap) {
					try {
						if (mMap != null) {
							ArrayList<LotteryInfoBean> result_list = (ArrayList<LotteryInfoBean>) JsonUtils.parserJsonArray(
									(String) mMap.get("issueList"), new LotteryInfoParser());
							LotteryInfoBean lotteryInfoBean = LotteryInfoParser.getLotteryInfo(result_list);
							onPostExecute(lotteryInfoBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					if (isNeedRequest(mLotteryInfoBean)) {
						requestLotteryCountdown(mIssueInfoBean.getLeftTime(), mLotteryInfoBean, false);
					}
				}

				@Override
				public void onFinish() {
				}
			});
		}

		protected void onPostExecute(LotteryInfoBean lotteryInfoBean) {
			if (lotteryInfoBean != null) {
				mLotteryInfoBean.setIsStop(lotteryInfoBean.getIsStop());
				mLotteryInfoBean.setPrompt(lotteryInfoBean.getPrompt());
				mLotteryInfoBean.setIssueInfoList(lotteryInfoBean.getIssueInfoList());
				if (mLotteryInfoBean.getIssueInfoList().size() > 0) {
					IssueInfoBean mIssueInfoBean = mLotteryInfoBean.getIssueInfoList().get(0);
					if (mIssueInfoBean != null) {
						if (mIssueInfoBean.getLeftTime() > 0) {
							if (isNeedRefresh) {
								cancleCountDownByLid(lid);
							}
							requestLotteryCountdown(mIssueInfoBean.getLeftTime(), mLotteryInfoBean, true);
							if (mLotteryIssueChangeListener != null) {
								mLotteryIssueChangeListener.onSucceeToGetIssue(lid);
							}
						} else {
							if (isNeedRequest(mLotteryInfoBean)) {
								requestLotteryCountdown(mIssueInfoBean.getLeftTime(), mLotteryInfoBean, false);
							} else {
								LogUtil.DefalutLog("mIssueInfoBean.getLeftTime() < 0");
								mLotteryInfoBean.setPrompt("网络错误，下拉可刷新");
								getIssueFaile(lid);
							}
						}
					} else {
						if (isNeedRequest(mLotteryInfoBean)) {
							requestLotteryCountdown(mIssueInfoBean.getLeftTime(), mLotteryInfoBean, false);
						} else {
							mLotteryInfoBean.setPrompt("网络错误，下拉可刷新");
							getIssueFaile(lid);
						}
					}
				} else {
					if (isNeedRequest(mLotteryInfoBean)) {
						requestLotteryCountdown(mIssueInfoBean.getLeftTime(), mLotteryInfoBean, false);
					} else {
						mLotteryInfoBean.setPrompt("网络错误，下拉可刷新");
						getIssueFaile(lid);
					}
				}
			} else {
				if (isNeedRequest(mLotteryInfoBean)) {
					requestLotteryCountdown(mIssueInfoBean.getLeftTime(), mLotteryInfoBean, false);
				} else {
					mLotteryInfoBean.setPrompt("网络错误，下拉可刷新");
					getIssueFaile(lid);
				}
			}
		}
	}

	/**请求倒计时，每次失败之后减一，总共3次
	 * @param millisInFuture
	 * @param tv_issue_countdown
	 * @param item
	 * @param isReset
	 */
	private void requestLotteryCountdown(long millisInFuture, LotteryInfoBean item, boolean isReset) {
		MyCount count = new MyCount(millisInFuture, 1000, item);
		count.start();
		allCountDownThread.add(count);
		int num = item.getHttpRequestTime();
		if (isReset) {
			item.setHttpRequestTime(3);
		} else {
			item.setHttpRequestTime(--num);
		}
	}

	/**请求倒计时，剩余多少次，3次失败之后就不再自动获取，用户手动下拉刷新
	 * @param item
	 * @return
	 */
	private boolean isNeedRequest(LotteryInfoBean item) {
		LogUtil.DefalutLog("item.getHttpRequestTime:" + item.getHttpRequestTime());
		return item.getHttpRequestTime() > 0;
	}

	private void getIssueFaile(String tempLid) {
		if (mLotteryIssueChangeListener != null) {
			mLotteryIssueChangeListener.onFaileToGetIssue(tempLid);
		}
	}

	/**
	 * 将剩余时间显示为-天-小时-分钟-秒的格式
	 * @param millisUntilFinished
	 * @return
	 */
	public static String changeDataFormat(long millisUntilFinished) {
		long day = millisUntilFinished / oneDay;
		long hour = (millisUntilFinished % oneDay) / oneHour;
		long minute = (millisUntilFinished % oneDay % oneHour) / oneMinute;
		long second = (millisUntilFinished % oneDay % oneHour % oneMinute) / 1000;
		String sec = second >= 10 ? second + "" : "0" + second;
		if (day > 0) {
			return day + "天" + hour + "小时";
		} else if (hour > 0) {
			return hour + "小时" + minute + "分";
		} else if (minute > 0) {
			return minute + "分" + sec + "秒";
		} else {
			return sec + "秒";
		}
	}

	/**
	 * 获取进入各个彩种页面intent
	 * @param lid
	 * @param value
	 */
	public static Intent getLotteryIntent(Context mContext, String lid) throws Exception {
		LotteryInfoBean item = LotteryId.getLotteryInfoBeanByLid(lid);
		Class mClass = LotteryId.getClass(lid);
		Intent intent = null;
		if (item == null) {
			ToastUtil.diaplayMesShort(mContext, "暂无数据，请下拉刷新！");
			return null;
		}

		if (item != null && mClass != null) {
			ArrayList<IssueInfoBean> beanList = item.getIssueInfoList();
			if (beanList != null) {
				if (LotteryId.isJCORCZ(lid)) {
					intent = new Intent(mContext, mClass);
					intent.putExtra(LotteryId.INTENT_LID, lid);
				} else {
					if (beanList.size() > 0) {
						if (lid.equals(LotteryId.SFC) || lid.equals(LotteryId.RX9)) {
							SafeApplication.dataMap.put("beanList", beanList);
						}
						IssueInfoBean bean = beanList.get(0);
						if (mClass != null && bean != null) {
							intent = new Intent(mContext, mClass);
							intent.putExtra(BaseLotteryActivity.INTENT_ISSUE, bean.getName());
							intent.putExtra(LotteryId.INTENT_LID, lid);
						}
					}
				}
			}
		}
		return intent;
	}

	/**
	 * 点击事件
	 * @author Messi
	 * 
	 */
	class MyClickListenre implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buy_lottery_login_or_register:
				StatService.onEvent(BuyLotteryActivity.this, "home-login", "首页-登录注册", 1);
				if (!GetString.isLogin) {
					Intent intent1 = new Intent(BuyLotteryActivity.this, LoginActivity.class);
					intent1.putExtra("from", 0);
					startActivity(intent1);
				} else {
					Intent intent3 = new Intent(BuyLotteryActivity.this, MainTabActivity.class);
					intent3.putExtra(Settings.TABHOST, Settings.USERHOME);
					startActivity(intent3);
				}
				break;
			case R.id.connection_faile_img:
				refresh();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 取消所有倒计时
	 * @param allCountDownThread
	 */
	private void cancleCountDownThread(ArrayList<MyCount> allCountDownThread) {
		for (MyCount countdown : allCountDownThread) {
			countdown.cancel();
			countdown = null;
		}
		allCountDownThread.clear();
	}

	/**彩期次变更监听器
	 * @param listener
	 */
	public void setLotteryIssueChangeListener(LotteryIssueChangeListener listener) {
		this.mLotteryIssueChangeListener = listener;
	}

	/**将金钱转化为简短表达，例如1亿5千万元
	 * @param sum
	 * @return
	 */
	public String getMoneyForShot(String sum, String lid) {
		StringBuilder result = new StringBuilder();
		String money = "";
		if (!TextUtils.isEmpty(sum)) {
			if (sum.contains(".")) {
				money = sum.split("\\.")[0];// .是正则表达式的符号之一，需要转义
			}
			int len = money.length();
			if (len > 4) {
				if (len > 8) {
					result.append(money.substring(0, len - 8));
					result.append("亿");
					result.append(money.substring(len - 8, len - 4));
					result.append("万元");
				} else {
					result.append(money.substring(0, len - 4));
					result.append("万元");
				}
			} else {
				if (money.equals("0")) {
					if (lid.equals(LotteryId.SSQ)) {
						result.append("大奖一中到底");
					} else if (lid.equals(LotteryId.DLT)) {
						result.append("奖金达1600万");
					}
				} else {
					result.append(money);
					result.append("元");
				}
			}
		}
		return result.toString();
	}

	protected void onBannerPreExecute() {
		cancleADTimmer();
		isRunAdTask = false;
		viewpager_ad.removeAllViews();
		adViews.clear();
		if (viewpager_ad.getAdapter() != null) {
			viewpager_ad.getAdapter().notifyDataSetChanged();
		}
		currentIndex = 0;
	}

	/**广告数据请求
	 * @author Messi
	 */
	private void BannerDataTask() {
		onBannerPreExecute();
		SafelotteryHttpClient.post(mContext, "3004", "banner", "", new TypeResultHttpResponseHandler(mContext, false) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try {
					SLManifest.postLoadingIndexPageAD(BuyLotteryActivity.this, spf, mResult, TaskHandle);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}
		});
	}

	protected void onBannerPostExecute() {
		try {
			mBannerList = (ArrayList<BannerBean>) SafeApplication.dataMap.get("BannerList");
			if (mBannerList != null) {
				showAdViews();
			} else {
				buy_lottery_ad.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showAdViews() throws Exception {
		adViews.clear();
		for (final BannerBean bean : mBannerList) {
			View imgView = ImageDownload.showBanner(this, buy_lottery_ad, bean, spf);
			if (imgView != null) {
				adViews.add(imgView);
			}
		}
		int adSize = adViews.size();
		if (adSize > 0) {
			if (adSize == 1) {
				viewpager_dot_layout.setVisibility(View.GONE);
			} else {
				viewpager_dot_layout.removeAllViews();
				for (int i = 0; i < adSize; i++) {
					viewpager_dot_layout.addView(ViewUtil.getDot(this, i));
				}
				changeState(0);
				mTimer = new Timer();
				mMyTimerTask = new MyTimerTask();
				mTimer.schedule(mMyTimerTask, delayed, delayed);
				isRunAdTask = true;
			}
			ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(adViews);
			viewpager_ad.removeAllViews();
			viewpager_ad.setAdapter(myPagerAdapter);
			viewpager_ad.setOnPageChangeListener(new MyOnPageChangeListener());
			buy_lottery_ad.setVisibility(View.VISIBLE);
		} else {
			buy_lottery_ad.setVisibility(View.GONE);
		}
	}

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			if (isRunAdTask) {
				Message message = new Message();
				message.what = 2;
				TaskHandle.sendMessage(message);
			}
		}
	};

	private void autoChangeAd() {
		try {
			int adSize = adViews.size();
			if (adSize > 1) {
				currentIndex++;
				if (currentIndex >= adSize) {
					currentIndex = 0;
				}
				if (viewpager_ad != null) {
					viewpager_ad.setCurrentItem(currentIndex);
				}
			}
		} catch (Exception e) {
			cancleADTimmer();
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancleCountDownThread(allCountDownThread);
		mContext = null;
		lotteryInfoBeansMap = null;
		cancleADTimmer();
	}
	
}
