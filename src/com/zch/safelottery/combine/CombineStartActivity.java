package com.zch.safelottery.combine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.CtOrderListActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.XmlUtil;
import com.zch.safelottery.view.ListLineFeedView;
import com.zch.safelottery.view.ListLineFeedView.OnClickListListener;

public class CombineStartActivity extends Activity {
	private String lotteryId, issue, endtime;// 彩种, 期次,结束时间
	private int buyAmount, floorsAmount, multiple;// 购买金额,保底金额
	private int privacy, commision;// 保密类型, 合买提成
	private Double remainMoney;// 账户剩余余额
	private String description;
	private List<BetIssueBean> betIssueBeanlist;
	private BetLotteryBean betLotteryBean;
	private EditText buyAmount_etx, floorsAmount_etx, combine_declaration_etx;
	private LinearLayout secrettype_layout, afterbonus_layout, combine_startsales_submit;
	private List<CheckBox> secrettypelist;
	private List<CheckBox> afterbonuslist;
	private CheckBox checkBox;
	private TextView combine_lotteryid_tx, combine_issue_tx, combine_totalAmount_tx;
	private FrameLayout combine_startsales_btn_title_remind;
	private String lastInput;
	private TextView scheme_tx;
	private int step_one = 1;// 1.数字彩发起 2.竞彩发起
	private List<String> schemelist = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.combine_startsales);
		Intent intent = getIntent();
		Bundle mBundle = intent.getBundleExtra(Settings.BUNDLE);
		if (mBundle == null) {
			ToastUtil.diaplayMesShort(getApplicationContext(), "bundle参数为NULL");
			finish();
		}
		endtime = mBundle.getString("endtime");
		betLotteryBean = (BetLotteryBean) SafeApplication.dataMap.get("BetLotteryBean");
		LogUtil.DefalutLog(betLotteryBean.toString());
		if (betLotteryBean == null) {
			ToastUtil.diaplayMesShort(getApplicationContext(), "参数为NULL");
			finish();
		}
		lotteryId = betLotteryBean.getLotteryId();
		if (betLotteryBean.getIssueArray()!=null) {
			issue = betLotteryBean.getIssueArray().get(0).getIssue();
			multiple = betLotteryBean.getIssueArray().get(0).getMultiple();
		} else {
			finish();
		}

		if ((lotteryId.equals(LotteryId.SSQ) || lotteryId.equals(LotteryId.DLT) || lotteryId.equals(LotteryId.PL3) || lotteryId.equals(LotteryId.PL5) || lotteryId.equals(LotteryId.QXC)
				|| lotteryId.equals(LotteryId.QLC) || lotteryId.equals(LotteryId.FC) || lotteryId.equals(LotteryId.RX9) || lotteryId.equals(LotteryId.SFC))) {
			step_one = 1;
		} else {
			step_one = 2;
		}
		initView();
		initSchemelList();
		setMoney();
	}

	private void initView() {
		combine_startsales_btn_title_remind = (FrameLayout) findViewById(R.id.combine_startsales_btn_title_remind);
		buyAmount_etx = (EditText) findViewById(R.id.combine_buyAmount_etx);
		floorsAmount_etx = (EditText) findViewById(R.id.combine_floorsAmount_etx);
		secrettype_layout = (LinearLayout) findViewById(R.id.combine_secrettype_layout);
		afterbonus_layout = (LinearLayout) findViewById(R.id.combine_afterbonus_layout);
		combine_lotteryid_tx = (TextView) findViewById(R.id.combine_lotteryid_tx);
		combine_issue_tx = (TextView) findViewById(R.id.combine_issue_tx);
		combine_totalAmount_tx = (TextView) findViewById(R.id.combine_totalAmount_tx);
		combine_declaration_etx = (EditText) findViewById(R.id.combine_declaration_etx);
		combine_startsales_submit = (LinearLayout) findViewById(R.id.combine_startsales_submit);
		scheme_tx = (TextView) findViewById(R.id.scheme_tx);
		// remainmoney_tx=(TextView)findViewById(R.id.remainmoney_tx);
		if (lotteryId.equals(LotteryId.SFC) && "02".equals(betLotteryBean.getPlayId())) {
			combine_lotteryid_tx.setText("彩种：" + LotteryId.getLotteryName("303"));
		} else if (lotteryId.equals(LotteryId.SFC) && "01".equals(betLotteryBean.getPlayId())) {
			combine_lotteryid_tx.setText("彩种：传统足彩14场");
		} else {
			combine_lotteryid_tx.setText("彩种：" + LotteryId.getLotteryName(lotteryId));
		}
		combine_issue_tx.setText("期次：" + issue + "期");
		combine_totalAmount_tx.setText(betLotteryBean.getTotalAmount() + "元");
		addsecrettype_layout();
		addafterbonus_layout();
		combine_startsales_submit.setOnClickListener(onClickListener);
		combine_startsales_btn_title_remind.setOnClickListener(onClickListener);
		buyAmount_etx.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				if (ConversionUtil.StringToInt(buyAmount_etx.getText().toString().trim()) > betLotteryBean.getTotalAmount()) {
					buyAmount_etx.setText(lastInput);
					if (lastInput.length() != 0) {
						buyAmount_etx.setSelection(lastInput.length() - 1);
					}
					Toast.makeText(CombineStartActivity.this, "认购金额不能大于方案总金额", Toast.LENGTH_LONG).show();
				} else if (ConversionUtil.StringToInt(buyAmount_etx.getText().toString().trim()) > ((int) betLotteryBean.getTotalAmount())
						- ConversionUtil.StringToInt(floorsAmount_etx.getText().toString().trim())) {
					buyAmount_etx.setText(lastInput);
					if (lastInput.length() != 0) {
						buyAmount_etx.setSelection(lastInput.length() - 1);
					}
					int num = ((int) betLotteryBean.getTotalAmount()) - ConversionUtil.StringToInt(floorsAmount_etx.getText().toString().trim());
					Toast.makeText(CombineStartActivity.this, "认购金额不能大于" + num + "元", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				lastInput = arg0.toString();
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				addSchemelList(2, "认购" + buyAmount_etx.getText().toString().trim() + "元,");
				addSchemelList(4, "共" + (ConversionUtil.StringToDouble(buyAmount_etx.getText().toString().trim()) + ConversionUtil.StringToDouble(floorsAmount_etx.getText().toString().trim())) + "元");
				setMoney();
			}
		});
		floorsAmount_etx.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				if (ConversionUtil.StringToInt(floorsAmount_etx.getText().toString().trim()) > betLotteryBean.getTotalAmount() - ConversionUtil.StringToInt(buyAmount_etx.getText().toString().trim())) {
					floorsAmount_etx.setText(lastInput);
					if (lastInput.length() != 0) {
						floorsAmount_etx.setSelection(lastInput.length() - 1);
					}
					int num = ((int) betLotteryBean.getTotalAmount()) - ConversionUtil.StringToInt(buyAmount_etx.getText().toString().trim());
					Toast.makeText(CombineStartActivity.this, "保底金额不能大于" + num + "元", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				lastInput = arg0.toString();
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				addSchemelList(3, "保底" + floorsAmount_etx.getText().toString().trim() + "元,");
				addSchemelList(4, "共" + (ConversionUtil.StringToDouble(buyAmount_etx.getText().toString().trim()) + ConversionUtil.StringToDouble(floorsAmount_etx.getText().toString().trim())) + "元");
				setMoney();
			}
		});
		
		combine_declaration_etx.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				if (str.length()>200) {
					String content = str.subSequence(0, 200).toString();
					combine_declaration_etx.setText(content);
					combine_declaration_etx.setSelection(content.length());
					Toast.makeText(CombineStartActivity.this, "方案宣言不能大于200个字符", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	public void initSchemelList() {
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				schemelist.add(i, "方案内容完全公开,");
			}
			if (i == 1) {
				schemelist.add(i, "佣金0%,");
			}
			if (i == 2) {
				schemelist.add(i, "认购0元,");
			}
			if (i == 3) {
				schemelist.add(i, "保底0元,");
			}
			if (i == 4) {
				schemelist.add(i, "共0元");
			}
		}
	}

	public void addSchemelList(int i, String str) {
		schemelist.set(i, str);
	}

	private void addsecrettype_layout() {
		secrettype_layout.removeAllViews();
		String secrettypestr[] = new String[] { "完全公开", "完全保密", "截止后公开", "参与者公开" };
		secrettypelist = XmlUtil.getCheckBoxWithMargin(this, R.layout.combine_startsales_secrettype_item, clickone, secrettypestr, 0, 4);
		ListLineFeedView listView = new ListLineFeedView(this, secrettypelist, 4);
		secrettype_layout.addView(listView.getView());
	}

	private void addafterbonus_layout() {
		afterbonus_layout.removeAllViews();
		String afterbonustr[] = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		afterbonuslist = XmlUtil.getCheckBoxWithMargin(this, R.layout.combine_afterbonus_item, clicktwo, afterbonustr, -1, 7);
		ListLineFeedView listView = new ListLineFeedView(this, afterbonuslist, 5);
		afterbonus_layout.addView(listView.getView());
	}

	private void setMoney() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < schemelist.size(); i++) {
			buffer.append(schemelist.get(i));
		}
		scheme_tx.setText(buffer.toString());
	}

	private OnClickListListener clickone = new OnClickListListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			for (int i = 0, size = secrettypelist.size(); i < size; i++) {
				checkBox = secrettypelist.get(i);
				if (i == id) {
					checkBox.setChecked(true);
					privacy = i;
				} else {
					if (checkBox.isChecked())
						checkBox.setChecked(false);
				}
			}
			setSchemeListMethod1();
			setMoney();
		}

		public void setSchemeListMethod1() {
			if (privacy == 0) {
				addSchemelList(0, "方案内容完全公开,");
			} else if (privacy == 1) {
				addSchemelList(0, "方案内容完全保密,");
			} else if (privacy == 2) {
				addSchemelList(0, "方案内容截止后公开,");
			} else if (privacy == 3) {
				addSchemelList(0, "方案内容参与者公开,");
			}
		}
	};
	private OnClickListListener clicktwo = new OnClickListListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			for (int i = 0, size = afterbonuslist.size(); i < size; i++) {
				checkBox = afterbonuslist.get(i);
				if (i == id) {
					if (checkBox.isChecked()) {
						commision = i + 1;
					} else {
						commision = 0;
					}
				} else {
					if (checkBox.isChecked() == true)
						checkBox.setChecked(false);
				}
			}
			setSchemeListMethod2();
			setMoney();
		}

		public void setSchemeListMethod2() {
			if (commision == 0) {
				addSchemelList(1, "佣金0%,");
			} else if (commision == 1) {
				addSchemelList(1, "佣金1%,");
			} else if (commision == 2) {
				addSchemelList(1, "佣金2%,");
			} else if (commision == 3) {
				addSchemelList(1, "佣金3%,");
			} else if (commision == 4) {
				addSchemelList(1, "佣金4%,");
			} else if (commision == 5) {
				addSchemelList(1, "佣金5%,");
			} else if (commision == 6) {
				addSchemelList(1, "佣金6%,");
			} else if (commision == 7) {
				addSchemelList(1, "佣金7%,");
			} else if (commision == 8) {
				addSchemelList(1, "佣金8%,");
			} else if (commision == 9) {
				addSchemelList(1, "佣金9%,");
			} else if (commision == 10) {
				addSchemelList(1, "佣金10%,");
			}
		}
	};

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.combine_startsales_submit) {
				buyAmount = ConversionUtil.StringToInt(buyAmount_etx.getText().toString().trim());
				floorsAmount = ConversionUtil.StringToInt(floorsAmount_etx.getText().toString().trim());
				if (buyAmount >= 1) {
					if (GetString.isLogin) {
						remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
						if (remainMoney >= buyAmount + floorsAmount) {
							if(!DoubleClickUtil.isFastDoubleClick()){
								if (LotteryId.isJCORCZ(lotteryId) == true) {
									doRequestTask();
								} else {
									String newIssue = LotteryId.getIssue(lotteryId);
									if (issue.equals(newIssue)) {
										doRequestTask();
									} else {
										issueOutOfDate(newIssue);
										getIssue();// 期次有变更，需要刷新期次列表
									}
								}
							}
						} else {
							PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(CombineStartActivity.this);
							dialog.setMoney((buyAmount + floorsAmount) - remainMoney);
							dialog.show();
						}
					} else {
						Intent intent = new Intent(CombineStartActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				} else {
					ToastUtil.diaplayMesShort(CombineStartActivity.this, "认购金额至少为1元");
				}
			} else if (v.getId() == R.id.combine_startsales_btn_title_remind) {
				Intent intent = new Intent(CombineStartActivity.this, HelpDetailActivity.class);
				intent.putExtra("kind", 2);
				startActivity(intent);
			}
		}
	};

	private void getIssue() {
		betIssueBeanlist = new ArrayList<BetIssueBean>();
		// 写入在售期
		BetIssueBean betIssueBean = new BetIssueBean();
		betIssueBean.setIssue(issue);
		betIssueBean.setMultiple(multiple);
		betIssueBeanlist.add(betIssueBean);
		betLotteryBean.setIssueArray(betIssueBeanlist);
	}

	private void doRequestTask() {
		combine_startsales_submit.setEnabled(false);
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,false);
		if (step_one == 1) {
			SafelotteryHttpClient.post(this, "3300", "", initDate(), new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if(mResult != null){
						final SucceedDialog dialog = new SucceedDialog(CombineStartActivity.this, lotteryId, issue, multiple, (buyAmount + floorsAmount), (remainMoney - (buyAmount + floorsAmount)), 1,
								betLotteryBean.getPlayId());
						dialog.setOnDialogClickListener(new OnDialogClickListener() {
							@Override
							public void onPositiveButtonClick() {
								Settings.closeOtherActivitySetCurTabOne(CombineStartActivity.this);
								dialog.dismiss();
								finish();
//								new FenxiangUtil(CombineStartActivity.this, LotteryId.getLotteryName(lotteryId), 3);
							}

							@Override
							public void onNegativeButtonClick() {
								dialog.dismiss();
								setResult(CtOrderListActivity.FOR_RESULT_COMBINE);
								finish();
							}
						});
						dialog.show();
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					
				}
				
				@Override
				public void onFinish(){
					progresdialog.dismiss();
					combine_startsales_submit.setEnabled(true);
				}
			});
		}else if (step_one == 2) {
			SafelotteryHttpClient.post(this, "3300", "sports", initDate(), new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if(mResult != null){
						final SucceedDialog dialog = new SucceedDialog(CombineStartActivity.this, lotteryId, issue, multiple, (buyAmount + floorsAmount), (remainMoney - (buyAmount + floorsAmount)), 1,
								betLotteryBean.getPlayId());
						dialog.setOnDialogClickListener(new OnDialogClickListener() {
							@Override
							public void onPositiveButtonClick() {
								Settings.closeOtherActivitySetCurTabOne(CombineStartActivity.this);
								dialog.dismiss();
								finish();
//								new FenxiangUtil(CombineStartActivity.this, LotteryId.getLotteryName(lotteryId), 3);
							}

							@Override
							public void onNegativeButtonClick() {
								dialog.dismiss();
								setResult(CtOrderListActivity.FOR_RESULT_COMBINE);
								finish();
							}
						});
						dialog.show();
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progresdialog.dismiss();
					combine_startsales_submit.setEnabled(true);
				}
			});
		}
	}

	public String initDate() {
		description = combine_declaration_etx.getText().toString().trim();
		if(TextUtils.isEmpty(description)){
			description = combine_declaration_etx.getHint().toString().trim();
		}
		betLotteryBean.setUserCode(GetString.userInfo.getUserCode());
		betLotteryBean.setBuyType(2);
		betLotteryBean.setDescription(description);
		betLotteryBean.setPrivacy(privacy);
		betLotteryBean.setCommision(commision);
		betLotteryBean.setBuyAmount(buyAmount);
		betLotteryBean.setFloorsAmount(floorsAmount);
		betLotteryBean.setWereMin("1");
		String str = JsonUtils.toJsonStr(betLotteryBean);
		LogUtil.DefalutLog(str);
		return str;
	}

	/**
	 * 期次过期提醒
	 * 
	 * @param newestLid
	 */
	private void issueOutOfDate(final String newestLid) {
		NormalAlertDialog dialog = new NormalAlertDialog(this);
		dialog.setTitle("期次变更提示");
		dialog.setContent("您当前投注的期次已变更为" + newestLid + "期，是否断续投注？");
		dialog.setOk_btn_text("断续投注");
		dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
			@Override
			public void onOkBtnClick() {
				issue = newestLid;
				doRequestTask();
			}

			@Override
			public void onCancleBtnClick() {
			}
		});
		dialog.show();
	}
}
