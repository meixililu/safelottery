package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.activity.SendLotteryActivity;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.asynctask.MyAsyncTask;
import com.zch.safelottery.asynctask.MyAsyncTask.OnAsyncTaskListener;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.bean.YuCeBean;
import com.zch.safelottery.combine.CombineStartActivity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.BeiTouZHDialog;
import com.zch.safelottery.dialogs.BonusYuceDialog;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.jingcai.JCAdapter.JCDingDanClickListener;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.JC_BetCountUtil;
import com.zch.safelottery.util.JC_BonusForecastUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.ViewUtil;
import com.zch.safelottery.util.XmlUtil;
import com.zch.safelottery.view.ListLineFeedView;
import com.zch.safelottery.view.ListLineFeedView.OnClickListListener;

public class JCOrderListActivity extends ZCHBaseActivity {

	private LayoutInflater inflater;
	private TextView jc_title_text;// title
	private TextView jc_yuce;// 奖金预测
	private TextView jc_bet_num;// 共0注
	private TextView jc_money;// 共0元
	private LinearLayout jc_cg_layout;// 串关
	private FrameLayout jc_bottom_back;// 返回修改
	private Button jc_bottom_hemai;// 发起合买
	private Button jc_bottom_submit;// 立即投注
	private Button jc_base_cancel;// 清空
	private EditText jc_beitou_et;// 倍投数
	private ListView jc_content_lv;// 对阵ListView

	private String lid;
	private String playMethod;
	private String issue;
	private int selectNumber;// 已选场次数
	private int danCount;// 定胆数
	private int multiple = 1;
	private int buyType = 1;// 购买方式 1 代购 2 合买 4 追号
	private long betNum;
	private long totalMoney = 1;
	private double remainMoney;
	private ArrayList<JZMatchBean> selectedBeans;
	private JCAdapter mAdapter;// 胜负彩
	private BuyLotteryTask mBuyLotteryTask;
	private BeiTouZHDialog beiTouZHDialog;
	private ArrayList<Long> arrayList = new ArrayList<Long>();

	public static int FOR_REQUEST_COMBINE = 0X001;
	public static int FOR_RESULT_COMBINE = 0X002;
	private List<CheckBox> listChecks;
	/** 计算注数 **/
	private JC_BetCountUtil betCountUtil;
	/** 计算赔率 **/
	private JC_BonusForecastUtil bonusForecastUtil;
	private boolean isXieyiCheck = true;
	private int sendStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
		if (mBundle != null) {
			sendStatus = mBundle.getInt(SendLotteryActivity.SEND_STATUS_KEY);
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.jzrq_order_list);
		inflater = LayoutInflater.from(this);
		initData();
		initViews();
		customViews();
	}

	private void initData() {
		Intent intent = getIntent();
		lid = intent.getStringExtra("lid");
		playMethod = intent.getStringExtra("playMethod");
		issue = intent.getStringExtra("issue");
		selectNumber = intent.getIntExtra("selectNumber", 0);
		danCount = intent.getIntExtra("danCount", 0);
		selectedBeans = (ArrayList<JZMatchBean>) SafeApplication.dataMap.get("selectedBean");
		SafeApplication.dataMap.clear();
		//如果不是冠军玩法才加这个
		if (!LotteryId.CGJ.endsWith(lid)) {
			for (int i = 0; i < selectedBeans.size(); i++) {
				arrayList.add(TimeUtils.getDateToTime(selectedBeans.get(i).getEndFuShiTime()));
			}
		}
		clearDan();
		LogUtil.DefalutLog("selectNumber:" + selectNumber + "--" + "danCount:" + danCount);
	}

	private void initViews() {
		jc_title_text = (TextView) findViewById(R.id.jc_title_text);
		jc_yuce = (TextView) findViewById(R.id.jc_yuce);
		jc_bet_num = (TextView) findViewById(R.id.jc_bet_num);
		jc_money = (TextView) findViewById(R.id.jc_money);
		jc_cg_layout = (LinearLayout) findViewById(R.id.jc_cg_layout);
		jc_bottom_back = (FrameLayout) findViewById(R.id.jc_bottom_back);
		jc_bottom_hemai = (Button) findViewById(R.id.jc_bottom_hemai);
		jc_bottom_submit = (Button) findViewById(R.id.jc_bottom_submit);
		jc_base_cancel = (Button) findViewById(R.id.jc_base_cancel);
		jc_beitou_et = (EditText) findViewById(R.id.jc_beitou_et);
		jc_content_lv = (ListView) findViewById(R.id.jc_content_lv);

		if (sendStatus == SendLotteryActivity.STATUS_JC) {
			jc_bottom_hemai.setVisibility(View.GONE);
		}
		//猜冠军玩法不支持合买
		if (LotteryId.CGJ.equals(lid)) {
			jc_bottom_hemai.setVisibility(View.GONE);
		}
		if (lid.equals(LotteryId.JCZQ) && playMethod.equals(JZActivity.WF_HHGG)) {
			jc_yuce.setVisibility(View.GONE);
		}
		// 冠军玩法　
		if (LotteryId.CGJ.endsWith(lid)) {
			jc_cg_layout.setVisibility(View.GONE);
			jc_yuce.setEnabled(false);
			betNum = selectNumber;
			setBetNum();
			
		} else {
			jc_yuce.setOnClickListener(onClickListener);
		}

		jc_bottom_submit.setOnClickListener(onClickListener);
		jc_base_cancel.setOnClickListener(onClickListener);
		jc_bottom_back.setOnClickListener(onClickListener);
		jc_bottom_hemai.setOnClickListener(onClickListener);

		jc_beitou_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.equals("0")) {
					ToastUtil.diaplayMesShort(JCOrderListActivity.this, "倍投数必须大于0");
					jc_beitou_et.setText("1");
					multiple = 1;
				}
				multiple = ConversionUtil.StringToInt(str);
				setBetNum();
			}
		});
	}

	/**
	 * 根据不同彩种玩法绘制界面
	 */
	private void customViews() {
		listChecks = XmlUtil.getCheckBox(this, R.layout.chuanguan_checkbox, clickL,
				ViewUtil.initCGCheckBox(selectNumber, lid, playMethod, selectedBeans), -1);
		ListLineFeedView listView = new ListLineFeedView(this, listChecks, 5);

		jc_cg_layout.addView(listView.getView());
		jc_content_lv.addFooterView(xieyiFooterView());
		ViewUtil.disableCheckBox(listChecks, danCount);
		jc_content_lv.setBackgroundResource(R.drawable.bd_order_content);
		if (lid.equals(LotteryId.JCZQ)) {
			if (playMethod.equals(JZActivity.WF_RQSPF)) {
				mAdapter = new JZRQAdapter(this, selectedBeans, inflater, danCount);
				mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
					@Override
					public void onCheck(int danNum) {
						danCount = danNum;
						ViewUtil.disableCheckBox(listChecks, danCount);
						LogUtil.DefalutLog("danCount:" + danCount);
						countBet();
					}
				});
				jc_content_lv.setAdapter(mAdapter);
			} else if (playMethod.equals(JZActivity.WF_SPF)) {
				mAdapter = new JZSPFAdapter(this, selectedBeans, inflater, danCount);
				mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
					@Override
					public void onCheck(int danNum) {
						danCount = danNum;
						ViewUtil.disableCheckBox(listChecks, danCount);
						LogUtil.DefalutLog("danCount:" + danCount);
						countBet();
					}
				});
				jc_content_lv.setAdapter(mAdapter);
			} else if (playMethod.equals(JZActivity.WF_QCBF)) {
				mAdapter = new JZQCBFAdapter(this, selectedBeans, inflater, danCount);
				mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
					@Override
					public void onCheck(int danNum) {
						danCount = danNum;
						ViewUtil.disableCheckBox(listChecks, danCount);
						LogUtil.DefalutLog("danCount:" + danCount);
						countBet();
					}
				});
				jc_content_lv.setAdapter(mAdapter);

			} else if (playMethod.equals(JZActivity.WF_BQC)) {
				mAdapter = new JZBQCAdapter(this, selectedBeans, inflater, danCount);
				mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
					@Override
					public void onCheck(int danNum) {
						danCount = danNum;
						ViewUtil.disableCheckBox(listChecks, danCount);
						LogUtil.DefalutLog("danCount:" + danCount);
						countBet();
					}
				});
				jc_content_lv.setAdapter(mAdapter);

			} else if (playMethod.equals(JZActivity.WF_JQS)) {
				mAdapter = new JZJQSAdapter(this, selectedBeans, inflater, danCount);
				mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
					@Override
					public void onCheck(int danNum) {
						danCount = danNum;
						ViewUtil.disableCheckBox(listChecks, danCount);
						LogUtil.DefalutLog("danCount:" + danCount);
						countBet();
					}
				});
				jc_content_lv.setAdapter(mAdapter);

			} else if (playMethod.equals(JZActivity.WF_HHGG)) {
				mAdapter = new JZHHGGAdapter(this, selectedBeans, inflater, danCount);
				mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
					@Override
					public void onCheck(int danNum) {
						danCount = danNum;
						ViewUtil.disableCheckBox(listChecks, danCount);
						LogUtil.DefalutLog("danCount:" + danCount);
						countBet();
					}
				});
				jc_content_lv.setAdapter(mAdapter);
			} 
		} else if (lid.equals(LotteryId.JCLQ)) {
			mAdapter = new JLAllAdapter(this, selectedBeans, inflater, danCount, playMethod);
			mAdapter.setmJCDingDanClickListener(new JCDingDanClickListener() {
				@Override
				public void onCheck(int danNum) {
					danCount = danNum;
					ViewUtil.disableCheckBox(listChecks, danCount);
					LogUtil.DefalutLog("danCount:" + danCount);
					countBet();
				}
			});
			jc_content_lv.setAdapter(mAdapter);

		}else if (lid.equals(LotteryId.CGJ)) {
			 if (playMethod.equals("01")) {
				 mAdapter = new JZCGJOrderAdapter(this, selectedBeans, inflater);
				 jc_content_lv.setAdapter(mAdapter);
			 }
		} 
	}

	private OnClickListListener clickL = new OnClickListListener() {
		@Override
		public void onClick(View v) {
			countBet();
		}
	};

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.jc_yuce) {
				if (isBeitouHasValue()) {
					if (betNum > 0) {
						// 赔率Dialog
						countBonus();
					} else {
						Toast.makeText(getApplicationContext(), "至少选择一种过关方式", 1).show();
					}
				}
			} else if (v.getId() == R.id.jc_bottom_submit) {
				if (isBeitouHasValue()) {
					if (GetString.isLogin) {
						if (betNum > 0) {
							remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
							if (remainMoney < totalMoney) {
								PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(JCOrderListActivity.this);
								dialog.setMoney(String.valueOf(totalMoney - remainMoney));
								dialog.show();
							} else {
								if (sendStatus == SendLotteryActivity.STATUS_JC) {
									sendLottery();
								} else {
									if (!DoubleClickUtil.isFastDoubleClick()) {
										startTask();
									}
								}
							}
						} else {
							ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择一种过关方式");
						}
					} else {
						Intent intent = new Intent(JCOrderListActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				}
			} else if (v.getId() == R.id.jc_base_cancel) {
				selectedBeans.clear();
				mAdapter.notifyDataSetChanged();
				betNum = 0;
				setBetNum();
			} else if (v.getId() == R.id.jc_bottom_back) {
				onBackPressed();
			} else if (v.getId() == R.id.jc_bottom_hemai) {
				if (isBeitouHasValue()) {
					if (betNum > 0) {
						Collections.sort(arrayList);
						Bundle mBundle = new Bundle();
						mBundle.putString("endtime", TimeUtils.formatLongTimeForCustom(arrayList.get(0), TimeUtils.MonthMinuteFormat));
						Intent intent = new Intent(JCOrderListActivity.this, CombineStartActivity.class);
						intent.putExtra(Settings.BUNDLE, mBundle);
						startActivityForResult(intent, FOR_REQUEST_COMBINE);
						SafeApplication.dataMap.put("BetLotteryBean", getBetLotteryBean());
					} else {
						ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择一种过关方式");
					}
				}
			}
		}
	};

	private boolean isBeitouHasValue() {
		if (!isXieyiCheck) {
			ToastUtil.diaplayMesShort(this, "请确认已同意《用户服务协议》");
			return false;
		} else if (selectedBeans.size() == 0) {
			ToastUtil.diaplayMesShort(this, "没有对阵，请返回选择比赛对阵");
			return false;
		} else if (TextUtils.isEmpty(jc_beitou_et.getText().toString())) {
			ToastUtil.diaplayMesShort(this, "倍投数不能为空");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 送他一注
	 */
	private void sendLottery() {

		SafeApplication.dataMap.put(SendLotteryActivity.SEND_MAP, getBetJson());

		// 启动广播 关闭竞足相关Activity
		Intent broadcast = new Intent(SafeApplication.INTENT_ACTION_ALLACTIVITY);
		broadcast.putExtra(Settings.EXIT, Settings.EXIT_JCZQ);
		sendBroadcast(broadcast);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void isNeedClose(int ationCode) {
		super.isNeedClose(ationCode);
		if (ationCode == Settings.EXIT_JCZQ) {
			finish();
		}
	}

	private void clearDan() {
		if (danCount >= selectNumber) {
			danCount = 0;
			for (JZMatchBean bean : selectedBeans) {
				bean.setHasDan(false);
			}
		}
	}

	private void getSelectList() {
		dataList.clear();
		choleList.clear();
		for (JZMatchBean bean : selectedBeans) {
			if (bean.isHasDan()) {
				choleList.add(bean.getUserSelectSp());
			} else {
				dataList.add(bean.getUserSelectSp());
			}
		}
	}

	// 最终结果---20121102001:2,6:0;20121102002:2,6:0;20121102003:1:0|2*1,3*1-00-12
	// 计算注数需要的格式---0:0/1:0/1/2:0，场次之前：分隔，选项之前/分隔；
	List<String> dataList = new ArrayList<String>();
	List<String> choleList = new ArrayList<String>();
	int[] cgList;

	/**
	 * 计算注数
	 */
	private void countBet() {
		cgList = ViewUtil.getCheckBoxId(listChecks);
		if (cgList.length > 0) {
			getSelectList();
			// LogUtil.CustomLog("TAG", "noDanStr:" + noDanSb.toString() + "---"
			// + "hasDanStr:" + hasDanSb.toString());

			taskCount(dataList, choleList, cgList);
		} else {
			betNum = 0;
			setBetNum();
		}
	}

	/**
	 * 计算赔率
	 */
	private void countBonus() {

		if (cgList.length > 0) {
			// LogUtil.CustomLog("TAG", "noDanStr:" + noDanSb.toString() + "---"
			// + "hasDanStr:" + hasDanSb.toString());

			taskBonus(dataList, choleList, cgList);
		} else {
			betNum = 0;
			setBetNum();
		}
	}

	private void taskCount(final List<String> dataList, final List<String> choleList, final int[] cgList) {
		MyAsyncTask tast = new MyAsyncTask(this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {

			@Override
			public void onTaskPostExecuteListener() {
				// ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				betNum = betCountUtil.getCount();
				setBetNum();
			}

			@Override
			public Boolean onTaskBackgroundListener() {
				try {
					if (betCountUtil != null)
						betCountUtil = null;
					betCountUtil = new JC_BetCountUtil(dataList, choleList, cgList, selectedBeans.size());
					// betCountUtil.initData(dataList, choleList, cgList,
					// selectedBeans.size());
					betCountUtil.start();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		});

		tast.execute();
	}

	private void taskBonus(final List<String> dataList, final List<String> choleList, final int[] cgList) {
		MyAsyncTask tast = new MyAsyncTask(this);
		tast.setOnAsyncTaskListener(new OnAsyncTaskListener() {

			@Override
			public Boolean onTaskBackgroundListener() {
				try {
					bonusForecastUtil = new JC_BonusForecastUtil(dataList, choleList, cgList, selectedBeans.size());
					bonusForecastUtil.start();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			public void onTaskPostExecuteListener() {
				// ToastUtil.diaplayMesShort(getApplicationContext(), "机选完成");
				double[][] resultReturn = bonusForecastUtil.getResultReturn();

				ArrayList<YuCeBean> mListYuce = new ArrayList<YuCeBean>();
				int index;
				YuCeBean y;
				for (int i = 2; i <= selectNumber; i++) {
					index = i - 1;
					if (resultReturn[index][0] > 0) {
						y = new YuCeBean();
						y.setmCount(i);
						y.setmBet((int) resultReturn[index][0]);
						y.setmMin(((resultReturn[index][1] * 2)) * multiple);
						y.setmMax(((resultReturn[index][2] * 2)) * multiple);
						mListYuce.add(y);
					}
				}
				Collections.reverse(mListYuce); // 排序

				// 启动Dialog
				BonusYuceDialog bonusYuceDialog = new BonusYuceDialog(JCOrderListActivity.this, mListYuce, inflater);
				bonusYuceDialog.setData(ViewUtil.getCheckBoxStr(listChecks), selectNumber, betNum, multiple);
				bonusYuceDialog.show();
			}
		});

		tast.execute();
	}

	/**
	 * 拼投注用的number 串
	 * 
	 * @return 拼好的投注串:
	 *         20121105002:1:0;20121105003:0,1:0;20121105004:1:1;20121105005
	 *         :1:1;20121105006:1:0|4*1,5*1-00-7
	 */
	private String spellBuyNumber() {
		StringBuffer sbBuffer = new StringBuffer();
		if (playMethod.equals(JZActivity.WF_HHGG)) {
			for (JZMatchBean bean : selectedBeans) {
				for (String seletedStr : bean.userSelectBuyList) {
					sbBuffer.append(seletedStr);
					sbBuffer.append(bean.isHasDan() ? "1" : "0");
					sbBuffer.append(";");
				}
			}
		} else if (LotteryId.CGJ.endsWith(lid)) {
			for (JZMatchBean bean : selectedBeans) {
				//期次+sn+不设胆
				sbBuffer.append(bean.getIssue());
				sbBuffer.append(":");
				sbBuffer.append(bean.getSn());
				sbBuffer.append(":");
				sbBuffer.append("0"); //不设置胆
				sbBuffer.append(";");
			}
		} else {
			for (JZMatchBean bean : selectedBeans) {
				sbBuffer.append(bean.getUserSelectBuy());
				sbBuffer.append(bean.isHasDan() ? "1" : "0");
				sbBuffer.append(";");
			}
		}
		sbBuffer.deleteCharAt(sbBuffer.lastIndexOf(";"));
		sbBuffer.append("|");
		if (LotteryId.CGJ.endsWith(lid)) {
			sbBuffer.append("1*1");
		}else{
			sbBuffer.append(ViewUtil.getCheckBoxStr(listChecks, '串', '*'));
		}
		LogUtil.CustomLog("TAG", "拼的字符串：" + sbBuffer.toString());
		return sbBuffer.toString();
	}

	private void setBetNum() {
		totalMoney = betNum * 2 * multiple;// 计算Money
		jc_bet_num.setText(GetString.df_0.format(betNum) + "注  ");
		jc_money.setText("共" + GetString.df_0.format(totalMoney) + "元");
		
		//冠军玩法处理
		if (LotteryId.CGJ.endsWith(lid) && selectedBeans!=null && selectedBeans.size()>0) {
			ArrayList<Double> list = new ArrayList<Double>();
			for (int i = 0; i < selectedBeans.size(); i++) {
				JZMatchBean bean = selectedBeans.get(i);
				list.add(Double.parseDouble(bean.getSp()));
			}
			Collections.sort(list);
			String htmlTemplate = "理论奖金<br/><font color='red'>%s-%s</font>";
			String htmlTemplate2 = "理论奖金<br/><font color='red'>%s</font>";
			String minMoney = GetString.df.format(list.get(0) * 2 * multiple);
			String html = "";
			if (list.size()>1) {
				String maxMoney = GetString.df.format(list.get(list.size() - 1) * 2 * multiple);
				html = String.format(htmlTemplate, minMoney, maxMoney);
			}else
			{
				html = String.format(htmlTemplate2, minMoney);
			}
			jc_yuce.setText(Html.fromHtml(html));
		}
	}

	private BetLotteryBean getBetJson() {
		// issue = "13008";
		BetLotteryBean mLotteryBean = getBetLotteryBean();
		return mLotteryBean;
	}

	public BetLotteryBean getBetLotteryBean() {
		BetLotteryBean mLotteryBean = new BetLotteryBean();
		if (GetString.userInfo != null) {
			mLotteryBean.setUserCode(GetString.userInfo.getUserCode());
		}
		mLotteryBean.setAppVersion(SystemInfo.softVerCode);
		mLotteryBean.setLotteryId(lid);
		mLotteryBean.setPlayId(playMethod);
		mLotteryBean.setBuyType(buyType);
		mLotteryBean.setBuyAmount(totalMoney);
		mLotteryBean.setTotalAmount(totalMoney);
		List<BetNumberBean> mBetNumberBeanList = new ArrayList<BetNumberBean>();
		mBetNumberBeanList.add(getBetNumberBean());
		mLotteryBean.setBuyNumberArray(mBetNumberBeanList);
		List<BetIssueBean> issueArray = new ArrayList<BetIssueBean>();
		issueArray.add(getBetIssueBean());
		mLotteryBean.setIssueArray(issueArray);

		return mLotteryBean;
	}

	private BetNumberBean getBetNumberBean() {
		BetNumberBean mBean = new BetNumberBean();
		mBean.setPlayId(playMethod);
		String pollId = "02";
		if (JZActivity.WF_HHGG.equals(playMethod)) {
			pollId = "00";
		}else if (LotteryId.CGJ.endsWith(lid)){
			pollId = "01";
		}
		// 过关
		mBean.setPollId(pollId);
		mBean.setItem(betNum);
		mBean.setAmount(totalMoney);
		mBean.setBuyNumber(spellBuyNumber());
		return mBean;
	}

	private BetIssueBean getBetIssueBean() {
		BetIssueBean bean = new BetIssueBean();
		bean.setIssue(getBetIssue());
		bean.setMultiple(multiple);
		return bean;
	}

	private void startTask() {
		jc_bottom_submit.setEnabled(false);
		mBuyLotteryTask = new BuyLotteryTask(this, "sports", JsonUtils.toJsonStr(getBetJson()), jc_bottom_submit);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				// 竞彩显示备注信息
				String remark = "";// "您投注时的竞彩奖金指数有可能在出票时发生变化，实际数值以投注记录中的数值为准！";
				final SucceedDialog dialog = new SucceedDialog(JCOrderListActivity.this, lid, issue, betNum, SucceedDialog.PURSUE_NULL, multiple,
						totalMoney, (remainMoney - totalMoney), remark);
				dialog.setCancelable(false);
				dialog.setOnDialogClickListener(new OnDialogClickListener() {
					@Override
					public void onPositiveButtonClick() {
						Settings.closeOtherActivitySetCurTabOne(JCOrderListActivity.this);
						dialog.dismiss();
						finish();
						// new FenxiangUtil(JCOrderListActivity.this,
						// LotteryId.getLotteryName(lid), 1);
					}

					@Override
					public void onNegativeButtonClick() {
						dialog.dismiss();
						JZBetActivity.clearSelectResult(selectedBeans);
						JZBetActivity.selectNumber = 0;
						setResult(RESULT_OK);
						finish();
					}
				});
				dialog.show();
			}
		});
		mBuyLotteryTask.send();
	}

	private String getBetIssue() {
		issue = selectedBeans.get(0).getIssue();
		return issue;
	}

	/* 合买页面返回 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FOR_REQUEST_COMBINE) {
			if (resultCode == FOR_RESULT_COMBINE) {
				JZBetActivity.clearSelectResult(selectedBeans);
				JZBetActivity.selectNumber = 0;
				setResult(RESULT_OK);
				onBackPressed();
			}
		}
	}

	private View xieyiFooterView() {
		View xieyiView = inflater.inflate(R.layout.jc_touzhu_xieyi, null);
		final CheckBox xieyiCB = (CheckBox) xieyiView.findViewById(R.id.zch_order_list_page_rule_cbx);
		final TextView rule_content = (TextView) xieyiView.findViewById(R.id.zch_order_list_page_rule_content);
		xieyiCB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isXieyiCheck = xieyiCB.isChecked();
			}
		});
		rule_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent_xieyi = new Intent(JCOrderListActivity.this, HelpDetailActivity.class);
				intent_xieyi.putExtra("kind", 14);
				startActivity(intent_xieyi);
			}
		});
		return xieyiView;
	}
}
