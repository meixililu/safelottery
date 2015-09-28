package com.zch.safelottery.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.CardCodeChecking;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.UserInfoHttpUtil;
import com.zch.safelottery.util.UserInfoHttpUtil.OnHttpListenter;

/**
 * @author Jiang
 *
 */
public class BoundUserInfoActivity extends ZCHBaseActivity {

	private PullToRefreshScrollView scroll_view;
	private LinearLayout layout_card;
	/** 跳过及完成 **/
	private Button finish;
	/** 输入姓名跟证件号 **/
	private EditText realNameEd, cardCodeEd;
	/** 显示姓名跟证件号 **/
	private TextView realNameTv, cardCodeTv;

	/** 显示证件类型 **/
	private TextView cardTypeTv;
	/** 选择证件类型 **/
	private Button cardTypeBtn;

	/** 显示作用说明 **/
	private LinearLayout explainShow;

	/** 显示用户名 **/
	private LinearLayout userNameShow;
	/** 用户名 **/
	private TextView userNameTv;
	/** 恭喜您提示 **/
	private TextView explainTv;

	/** 显示手机 **/
	private LinearLayout phoneShow;
	/** 手机号码 **/
	private TextView phoneTv;

	/** 变更提示 **/
	private LinearLayout friendshipShow;

	/** 拨打电话 **/
	private TextView dial;

	private int state;
	/** 判断状态 **/
	public static final String BOUND_STATE = "BOUND_STATE";
	/** 跳转页面 **/
	public static final String BOUND_FROM = "BOUND_FROM";

	/** 绑定完成 **/
	public static final int BOUND_FINISH = 1;
	/** 绑定未完成 **/
	public static final int BOUND_NOT_FINISH = 2;
	/** 绑定成功 **/
	public static final int BOUND_SUCCEED = 3;

	private String userName; // 用户名
	private String realName; // 真名
	private String cardCode; // 身份证号
	private String cardType = "1"; // 身份类型

	/** 来自的位置 **/
	private int from;

	/** popDialogY的坐标 **/
	private int popDialogY;

	private String mCreditPayInfoJson;

	private Serializable toClass;

	private Context mContext;

	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bound_user_info);
		mContext = this;
		mHandler = new Handler();
		Bundle mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
		if (mBundle != null) {
			mCreditPayInfoJson = mBundle.getString(ChargeLdysActivity.CREDIT_PAY_JSON_DATA_KEY);
			state = mBundle.getInt(BOUND_STATE, -1);
			from = mBundle.getInt(BOUND_FROM, -1);
			toClass = mBundle.getSerializable(Settings.TOCLASS);
		}
		// state = BOUND_NOT_FINISH;
		initUI();
		requestUserInfo();
	}

	private void initUI() {
		scroll_view = (PullToRefreshScrollView) findViewById(R.id.bound_user_info_lny);
		layout_card = (LinearLayout) findViewById(R.id.registerkey_bound_card_layout);

		finish = (Button) findViewById(R.id.bound_user_info_finish);

		explainTv = (TextView) findViewById(R.id.bound_user_info_explain);
		explainShow = (LinearLayout) findViewById(R.id.bound_user_info_explain_show);

		userNameShow = (LinearLayout) findViewById(R.id.bound_user_info_name_user_show);
		userNameTv = (TextView) findViewById(R.id.bound_user_info_user_name);

		realNameEd = (EditText) findViewById(R.id.bound_user_info_real_edit);
		cardCodeEd = (EditText) findViewById(R.id.bound_user_info_card_edit);
		realNameTv = (TextView) findViewById(R.id.bound_user_info_real_text);
		cardCodeTv = (TextView) findViewById(R.id.bound_user_info_card_text);

		cardTypeTv = (TextView) findViewById(R.id.bound_user_info_type_text);
		cardTypeBtn = (Button) findViewById(R.id.bound_user_info_type_btn);

		phoneShow = (LinearLayout) findViewById(R.id.bound_user_info_phone_show);
		phoneTv = (TextView) findViewById(R.id.bound_user_info_phone);

		friendshipShow = (LinearLayout) findViewById(R.id.bount_user_info_friendship_show);

		dial = (TextView) findViewById(R.id.bound_user_info_dial);

		finish.setOnClickListener(onClickListener);
		dial.setOnClickListener(onClickListener);

		cardTypeBtn.setOnClickListener(onClickListener);

		scroll_view.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				requestUserInfo();
			}
		});
	}

	private void requestUserInfo() {
		layout_card.setVisibility(View.GONE);

		OnHttpListenter callback = new OnHttpListenter() {

			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
				layout_card.setVisibility(View.VISIBLE);
				String name = GetString.userInfo.getRealName();
				String cardCode = GetString.userInfo.getCardCode();
				if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cardCode)) {
					state = BOUND_FINISH;
				}else
					state = BOUND_NOT_FINISH;
				initData();
				scroll_view.onRefreshComplete();
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				ToastUtil.diaplayMesShort(mContext, "获取信息失败，请刷新重试");
				scroll_view.onRefreshComplete();
			}

		};
		new UserInfoHttpUtil(this, 1, callback);
	}

	private void initData() {
		userName = GetString.userInfo.getUserName();

		switch (state) {
		case BOUND_FINISH:
			boundFinish();
			break;
		case BOUND_NOT_FINISH:
			boundEdit();
			break;
		case BOUND_SUCCEED:
			boundSucceed();
			break;
		default:
			ToastUtil.diaplayMesShort(getApplicationContext(), "不满足条件，请重试");
			finish();
			break;
		}

	}

	/**
	 * 隐藏所有
	 */
	private void gone() {
		explainTv.setVisibility(View.GONE);
		explainShow.setVisibility(View.GONE);
		userNameShow.setVisibility(View.GONE);
		realNameEd.setVisibility(View.GONE);
		cardCodeEd.setVisibility(View.GONE);
		realNameTv.setVisibility(View.GONE);
		cardCodeTv.setVisibility(View.GONE);
		phoneShow.setVisibility(View.GONE);
		phoneTv.setVisibility(View.GONE);
		friendshipShow.setVisibility(View.GONE);

		cardTypeTv.setVisibility(View.GONE);
		cardTypeBtn.setVisibility(View.GONE);
	}

	/**
	 * 绑定 输入信息
	 */
	private void boundEdit() {
		gone();

		userNameShow.setVisibility(View.VISIBLE);
		explainShow.setVisibility(View.VISIBLE);
		realNameEd.setVisibility(View.VISIBLE);
		cardCodeEd.setVisibility(View.VISIBLE);
		cardTypeBtn.setVisibility(View.VISIBLE);
		userNameTv.setText(userName);

		if (toClass != null) {

			explainTv.setVisibility(View.VISIBLE);
			explainTv.setText("请填写充值银行卡的个人信息。");
			explainTv.setTextColor(getResources().getColor(R.color.green));
			explainShow.setVisibility(View.GONE);
			userNameShow.setVisibility(View.GONE);
		}

		finish.setText("确定");
	}

	/**
	 * 绑定 成功后
	 */
	private void boundSucceed() {
		gone();

		explainTv.setVisibility(View.VISIBLE);
		realNameTv.setVisibility(View.VISIBLE);
		cardCodeTv.setVisibility(View.VISIBLE);
		friendshipShow.setVisibility(View.VISIBLE);
		cardTypeTv.setVisibility(View.VISIBLE);

		realNameTv.setText(realName);
		cardCodeTv.setText(cardCode);
		cardTypeTv.setText(getType(cardType));

		finish.setText("马上去购彩");
	}

	/**
	 * 绑定 完成后
	 */
	private void boundFinish() {
		gone();

		// 取得用户信息
		realName = GetString.userInfo.getRealName();
		cardCode = GetString.userInfo.getCardCode();
		cardType = GetString.userInfo.getCardType();

		userNameShow.setVisibility(View.VISIBLE);
		realNameTv.setVisibility(View.VISIBLE);
		cardCodeTv.setVisibility(View.VISIBLE);
		friendshipShow.setVisibility(View.VISIBLE);
		cardTypeTv.setVisibility(View.VISIBLE);

		userNameTv.setText(userName);
		realNameTv.setText(realName);
		cardCodeTv.setText(cardCode);
		cardTypeTv.setText(getType(cardType));
		finish.setText("马上去购彩");
	}

	private String getType(String type) {
		int mType = ConversionUtil.StringToInt(type);
		if (mType == 1) {
			return "身份证";
		} else if (mType == 2) {
			return "军官证";
		} else if (mType == 3) {
			return "护照";
		}
		return "其他";
	}

	private PopWindowDialog mPopDialog;

	private void popDialogShow() {
		if (mPopDialog != null) {
			if (mPopDialog.isShowing()) {
				mPopDialog.dismiss();
			} else {
				mPopDialog.show();
			}
		} else {
			// 敢得当前控件所在的位置
			PullToRefreshScrollView scroll = (PullToRefreshScrollView) findViewById(R.id.bound_user_info_lny);
			LinearLayout typeLny = (LinearLayout) findViewById(R.id.bound_user_info_type_lny);
			popDialogY = scroll.getTop() + typeLny.getTop() + typeLny.getHeight();

			// int[] location =new int[2];
			// cardTypeBtn.getLocationInWindow(location);
			// popDialogY = location[1];

			final String[] temp = new String[] { "身份证", "军官证", "护照" };
			mPopDialog = new PopWindowDialog(this, temp, 0);
			mPopDialog.setPopViewPosition(0, popDialogY);
			mPopDialog.setOnClickListener(new OnclickFrequentListener() {

				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mPopDialog.getList();
					CheckBox checkBox;
					int id = v.getId();
					for (int i = 0, size = temp.length; i < size; i++) {
						checkBox = list.get(i);
						if (i == id) {
							if (id == 0) {
								cardType = "1";
							} else if (id == 1) {
								cardType = "2";
							} else if (id == 2) {
								cardType = "3";
							} else {
								cardType = "1";
							}
							checkBox.setChecked(true);
							cardTypeBtn.setText(temp[id]);
						} else {
							if (checkBox.isChecked())
								checkBox.setChecked(false);
						}
					}
					mPopDialog.dismiss();

				}
			});

		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.bound_user_info_finish) {
				if (state == BOUND_NOT_FINISH) {
					realName = realNameEd.getText().toString().trim();
					cardCode = cardCodeEd.getText().toString().trim();
					if (!TextUtils.isEmpty(realName)) {
						if (!TextUtils.isEmpty(cardCode)) {
							if (TextUtils.isEmpty(cardCode))
								cardType = "1";
							if (cardType.equals("1")) {
								if (new CardCodeChecking().isValidatedAllIdcard(cardCode)) {
									doRequestTask();
								} else {
									ToastUtil.diaplayMesShort(getApplicationContext(), "身份证号不合法，请重试");
								}
							} else {
								doRequestTask(); // 其他类型直接请求
							}
						} else {
							ToastUtil.diaplayMesShort(getApplicationContext(), "请输入证件号码");
						}
					} else {
						ToastUtil.diaplayMesShort(getApplicationContext(), "请输入真实姓名");
					}
				} else {
					// 去购彩页面
					Intent intent = new Intent(mContext, MainTabActivity.class);
					intent.putExtra(Settings.TABHOST, 0);
					mContext.startActivity(intent);
					finish();
				}
			} else if (id == R.id.bound_user_info_type_btn) {
				popDialogShow();
			} else if (id == R.id.bound_user_info_dial) {
				// 拨打电话
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(mContext);
					dialog.show();
				}
			}
		}
	};

	private String dequestData() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", userCode);
		map.put("realName", realName);
		map.put("cardCode", cardCode);
		map.put("cardType", cardType);
		return JsonUtils.toJsonStr(map);
	}

	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true, true);

		SafelotteryHttpClient.post(this, "3103", "real", dequestData(), new TypeResultHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Result mResult) {
				GetString.userInfo.setRealName(realName);
				GetString.userInfo.setCardCode(cardCode);
				GetString.userInfo.setCardType(cardType);
				if (from == 9) { // 提款 跳转绑定银行
					ToastUtil.diaplayMesShort(getApplicationContext(), "成功绑定身份信息");
					Intent intent = new Intent(mContext, TakeBankActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putInt("state", TakeBankActivity.TAKE_BOUND);
					intent.putExtra(Settings.BUNDLE, mBundle);
					startActivity(intent);
					finish();
				} else {

					if (toClass != null) {
						ToastUtil.diaplayMesLong(BoundUserInfoActivity.this, "个人信息填写成功!");
						// 真实信息绑定成功，提示5秒后，自动跳入下一页面
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {

								/*
								 * 点击信用卡充值，首先进入输入金额页面，输入金额后，
								 * 先检查姓名和身份证号是否存在，如果存在则直接跳到流程页面，
								 * 否则跳到绑定页面，绑定完成再跳到流程页面。电话回呼还按原来的，先跳绑定。
								 */
								if (ChargeLdysActivity.class.equals(toClass)) {
									//信用卡
									creditPay();
								} else {
									//银联卡
									Intent intent = new Intent();
									intent.setClass(BoundUserInfoActivity.this, (Class) toClass);
									startActivity(intent);
								}
							}
						}, 1000);

					} else {
						state = BOUND_SUCCEED;
						new UserInfoHttpUtil(BoundUserInfoActivity.this, 1, null);
						initData();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}

			@Override
			public void onFinish() {
				progresdialog.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(BoundUserInfoActivity.this);
			dialog.setContent("充值完成后，请耐心等待，刷新账户查询是否到账！");
			dialog.setOk_btn_text("刷新账户");
			dialog.setCancle_btn_text("继续充值");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					GetString.isAccountNeedRefresh = true;
					BoundUserInfoActivity.this.finish();
					Intent intent = new Intent();
					intent.setClass(BoundUserInfoActivity.this, MainTabActivity.class);
					intent.putExtra(Settings.TABHOST, Settings.USERHOME);
					BoundUserInfoActivity.this.startActivity(intent);
				}

				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		}
	}

	private void creditPay() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true, true);
		SafelotteryHttpClient.post(this, "3202", "umPay", mCreditPayInfoJson, new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if (mMap != null) {
					String url = (String) mMap.get("requestUrl");
					if (!TextUtils.isEmpty(url)) {
						Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
						startActivityForResult(intent, 0);
					} else {
						LogUtil.CustomLog("TAG", "ChargeLdysActivity -- URL is null");
						ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
					}
				} else {
					LogUtil.CustomLog("TAG", "ChargeLdysActivity -- Map is null");
					ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}

			@Override
			public void onFinish() {
				progresdialog.dismiss();
			}

		});
		StatService.onEvent(BoundUserInfoActivity.this, "total-fund", "所有充值提交请求-联动优势", 1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}