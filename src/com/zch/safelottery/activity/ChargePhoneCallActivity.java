package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.dialogs.RiskNotesDialog;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.JsonHttpResponseHandler;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.ResultParser;
import com.zch.safelottery.sendlottery.ContactListViewActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.StatusUtil;
import com.zch.safelottery.util.ToastUtil;

/*
 * 电话回呼充值
 */
public class ChargePhoneCallActivity extends ZCHBaseActivity {
	private TextView charge_phonecall_name_tx, charge_phonecall_cardtype_tx, charge_phonecall_card_tx;
	private TextView charge_phonecall_bankname_tx, charge_phonecall_bankprovince_tx, charge_phonecall_bankcity_tx, charge_phonecall_banknumber_tx;
	private EditText charge_phonecall_name_edx, charge_phonecall_card_edx, charge_phonecall_phonenumber_edx, charge_phonecall_banknumber_edx,
			charge_phonecall_money_edx;
	private Button charge_phonecall_finish_btn, charge_phonecall_cardtype_btn;
	private Spinner charge_phonecall_bankname_spinner, charge_phonecall_bankprovince_spinner, charge_phonecall_bankcity_spinner;
	private int statenumber = 1;
	private boolean isnull = false;// 是否之前提交充值未成功
	private String bankname, cardtype, bankprovince, bankcity, banknumber, realname, cardnumber, mobile;
	private String banknameEnd, bankprovinceEnd, bankcityEnd, banknumberEnd, realnameEnd, cardnumberEnd, mobileEnd;
	private String moneyEnd = "100";
	private String cardtypeEnd = "1";
	private int provinceId;
	private ArrayAdapter<CharSequence> province_adapter;
	private ArrayAdapter<CharSequence> city_adapter;
	private ArrayAdapter<CharSequence> bank_adapter;
	private int popDialogY;
	private boolean ifShowBank = false;// 充值银行是否包含提款银行 false不包含
	private String[] idType = new String[] { "身份证", "军官证", "护照" };
	private int mPositionOfbankName = 0;// spinner 银行名字所在位置
	private int mPositionOfBankProvince = 0;// spinner 银行省份所在位置
	private int mPositionOfBankCity = 0;// spinner 银行城市所在位置

	private String inputAddress;
	private boolean isadddate = false;// 增加接口数据
	private String ref;
	private String respCode;
	private String resAmount;
	private ProgressBar progressbar;

	private LinearLayout phoneLayout;// 客服电话

	private int cardTypeTag = 0;// 证件类型默认选中位置序号
	private int currentCityPos = 0;

	private TextView take_bank_remind_text2;// 提醒里第二条的回冲电话标红

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_phonecall);
		initViews();
		bankTask();
	}

	private void initViews() {
		charge_phonecall_name_tx = (TextView) findViewById(R.id.charge_phonecall_name_tx);
		charge_phonecall_cardtype_tx = (TextView) findViewById(R.id.charge_phonecall_cardtype_tx);
		charge_phonecall_card_tx = (TextView) findViewById(R.id.charge_phonecall_card_tx);
		charge_phonecall_bankname_tx = (TextView) findViewById(R.id.charge_phonecall_bankname_tx);
		charge_phonecall_bankprovince_tx = (TextView) findViewById(R.id.charge_phonecall_bankprovince_tx);
		charge_phonecall_bankcity_tx = (TextView) findViewById(R.id.charge_phonecall_bankcity_tx);
		charge_phonecall_banknumber_tx = (TextView) findViewById(R.id.charge_phonecall_banknumber_tx);

		charge_phonecall_name_edx = (EditText) findViewById(R.id.charge_phonecall_name_edx);
		charge_phonecall_card_edx = (EditText) findViewById(R.id.charge_phonecall_card_edx);
		charge_phonecall_phonenumber_edx = (EditText) findViewById(R.id.charge_phonecall_phonenumber_edx);
		charge_phonecall_banknumber_edx = (EditText) findViewById(R.id.charge_phonecall_banknumber_edx);
		charge_phonecall_money_edx = (EditText) findViewById(R.id.charge_phonecall_money_edx);
		charge_phonecall_cardtype_btn = (Button) findViewById(R.id.charge_phonecall_cardtype_btn);

		charge_phonecall_bankname_spinner = (Spinner) findViewById(R.id.charge_phonecall_bankname_spinner);
		charge_phonecall_bankprovince_spinner = (Spinner) findViewById(R.id.charge_phonecall_bankprovince_spinner);
		charge_phonecall_bankcity_spinner = (Spinner) findViewById(R.id.charge_phonecall_bankcity_spinner);

		charge_phonecall_finish_btn = (Button) findViewById(R.id.charge_phonecall_finish_btn);
		phoneLayout = (LinearLayout) findViewById(R.id.charge_phonecall_tell);
		progressbar = (ProgressBar) findViewById(R.id.bound_idcard_progressbar);

		take_bank_remind_text2 = (TextView) findViewById(R.id.take_bank_remind_text2);
		String str1 = "2.提交后，您将收到银联02096585的来电，请按电话提示输入银行卡密码完成支付。";
		SpannableString msp1 = modifiercolor(str1, str1.length() - 30, str1.length() - 22);
		take_bank_remind_text2.setText(msp1);

		charge_phonecall_name_edx.setOnClickListener(clickListener);
		charge_phonecall_card_edx.setOnClickListener(clickListener);
		charge_phonecall_phonenumber_edx.setOnClickListener(clickListener);
		charge_phonecall_banknumber_edx.setOnClickListener(clickListener);
		charge_phonecall_money_edx.setOnClickListener(clickListener);
		charge_phonecall_cardtype_btn.setOnClickListener(clickListener);
		charge_phonecall_finish_btn.setOnClickListener(clickListener);
		phoneLayout.setOnClickListener(clickListener);
		boundDispose();
	}

	public SpannableString modifiercolor(String str1, int start, int end) {
		SpannableString msp = new SpannableString(str1);
		msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	private void boundDispose() {
		charge_phonecall_bankprovince_spinner.setPrompt("请选择省份");
		charge_phonecall_bankcity_spinner.setPrompt("请选择城市");
		charge_phonecall_bankname_spinner.setPrompt("请选择银行");

		province_adapter = ArrayAdapter.createFromResource(this, R.array.province_item, R.layout.spinner_text_style_nomel);
		province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		charge_phonecall_bankprovince_spinner.setAdapter(province_adapter);
		charge_phonecall_bankprovince_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				provinceId = charge_phonecall_bankprovince_spinner.getSelectedItemPosition();
				String strProvince = charge_phonecall_bankprovince_spinner.getSelectedItem().toString().trim();
				if (statenumber != 4) {
					bankprovinceEnd = strProvince;
				}
				charge_phonecall_bankcity_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						String strCity = charge_phonecall_bankcity_spinner.getSelectedItem().toString().trim();
						if (statenumber != 4) {
							bankcityEnd = strCity;
						}
					}
					public void onNothingSelected(AdapterView<?> parent) {}
				});
				select(charge_phonecall_bankcity_spinner, city_adapter, TakeBankActivity.citys[provinceId], currentCityPos);
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		bank_adapter = ArrayAdapter.createFromResource(this, R.array.phonecallbanks, R.layout.spinner_text_style_nomel);
		bank_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		charge_phonecall_bankname_spinner.setAdapter(bank_adapter);
		charge_phonecall_bankname_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (statenumber != 4) {
					banknameEnd = charge_phonecall_bankname_spinner.getSelectedItem().toString().trim();
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		charge_phonecall_bankprovince_spinner.setSelection(0);
		charge_phonecall_bankname_spinner.setSelection(0);

	}

	private void select(Spinner spin, ArrayAdapter<CharSequence> adapter, int arry, int selection) {
		adapter = ArrayAdapter.createFromResource(this, arry, R.layout.spinner_text_style_nomel);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		spin.setSelection(selection);
	}

	public void getState() {
		if (!TextUtils.isEmpty(GetString.userInfo.getRealName()) && !TextUtils.isEmpty(GetString.userInfo.getCardType())
				&& !TextUtils.isEmpty(GetString.userInfo.getCardCode())) {
			if (!TextUtils.isEmpty(GetString.userInfo.getBankName()) && !TextUtils.isEmpty(GetString.userInfo.getBankCode())) {
				if (!TextUtils.isEmpty(GetString.userInfo.getProvince()) && !TextUtils.isEmpty(GetString.userInfo.getCity())) {
					statenumber = 4;// 所有信息
				} else {
					statenumber = 3;// 没有银行省份和城市
				}
			} else {
				statenumber = 2;// 没有银行信息
			}
		} else {
			statenumber = 1;// 所有信息都没有
		}
	}

	public void setDate() {
		if (!TextUtils.isEmpty(GetString.userInfo.getBankName())) {
			ifShowBank(GetString.userInfo.getBankName());
		}
		if (!TextUtils.isEmpty(GetString.userInfo.getMobile())) {
			charge_phonecall_phonenumber_edx.setText(GetString.userInfo.getMobile());
		}
		if (statenumber == 1) {
			if (isnull) {
				charge_phonecall_name_edx.setText(realname);
				int type = ConversionUtil.StringToInt(cardtype);
				cardTypeTag = type - 1;
				charge_phonecall_cardtype_btn.setText(idType[cardTypeTag]);
				charge_phonecall_card_edx.setText(cardnumber);
				charge_phonecall_banknumber_edx.setText(banknumber);
				charge_phonecall_bankname_spinner.setSelection(mPositionOfbankName, true);
				charge_phonecall_bankprovince_spinner.setSelection(mPositionOfBankProvince, true);
				// charge_phonecall_bankcity_spinner.setSelection(bankCityTag,
				// true);
				charge_phonecall_phonenumber_edx.setText(mobile);
			}
		} else if (statenumber == 2) {
			setUserVew();
			if (isnull) {
				charge_phonecall_banknumber_edx.setText(banknumber);
				charge_phonecall_bankname_spinner.setSelection(mPositionOfbankName, true);
				charge_phonecall_bankprovince_spinner.setSelection(mPositionOfBankProvince, true);
				// charge_phonecall_bankcity_spinner.setSelection(bankCityTag,
				// true);
				charge_phonecall_phonenumber_edx.setText(mobile);
			}
		} else if (statenumber == 3) {
			setUserVew();
			if (isnull) {
				charge_phonecall_banknumber_edx.setText(banknumber);
				charge_phonecall_bankname_spinner.setSelection(mPositionOfbankName, true);
				charge_phonecall_bankprovince_spinner.setSelection(mPositionOfBankProvince, true);
				// charge_phonecall_bankcity_spinner.setSelection(bankCityTag,
				// true);
				charge_phonecall_phonenumber_edx.setText(mobile);
			} else {
				if (ifShowBank) {
					charge_phonecall_bankname_spinner.setSelection(mPositionOfbankName, true);
					charge_phonecall_banknumber_edx.setText(GetString.userInfo.getBankCode());
					banknumberEnd = GetString.userInfo.getBankCode();
				}
			}
		} else if (statenumber == 4) {
			setUserVew();
			if (ifShowBank) {

				banknameEnd = GetString.userInfo.getBankName();
				banknumberEnd = GetString.userInfo.getBankCode();
				bankprovinceEnd = GetString.userInfo.getProvince();
				bankcityEnd = GetString.userInfo.getCity();

				charge_phonecall_bankname_tx.setVisibility(View.VISIBLE);
				charge_phonecall_bankname_spinner.setVisibility(View.GONE);
				charge_phonecall_bankname_tx.setText(banknameEnd);
				charge_phonecall_bankprovince_tx.setVisibility(View.VISIBLE);
				charge_phonecall_bankprovince_spinner.setVisibility(View.GONE);
				charge_phonecall_bankprovince_tx.setText(bankprovinceEnd);
				charge_phonecall_bankcity_tx.setVisibility(View.VISIBLE);
				charge_phonecall_bankcity_spinner.setVisibility(View.GONE);
				charge_phonecall_bankcity_tx.setText(bankcityEnd);
				charge_phonecall_banknumber_tx.setVisibility(View.VISIBLE);
				charge_phonecall_banknumber_edx.setVisibility(View.GONE);
				charge_phonecall_banknumber_tx.setText(StatusUtil.getShortStr(banknumberEnd, 4, "****"));

			}
			if (isnull) {
				charge_phonecall_phonenumber_edx.setText(mobile);
			}
		}
	}

	public void setUserVew() {

		realnameEnd = GetString.userInfo.getRealName();
		cardnumberEnd = GetString.userInfo.getCardCode();
		cardtypeEnd = GetString.userInfo.getCardType();

		charge_phonecall_name_tx.setVisibility(View.VISIBLE);
		charge_phonecall_name_tx.setText(StatusUtil.getShortStr(realnameEnd, 1, "*"));
		charge_phonecall_name_edx.setVisibility(View.GONE);
		
		
		
		int type = ConversionUtil.StringToInt(GetString.userInfo.getCardType());
		charge_phonecall_cardtype_tx.setText(idType[type - 1]);
		charge_phonecall_cardtype_tx.setVisibility(View.VISIBLE);
		charge_phonecall_cardtype_btn.setVisibility(View.GONE);
		
		charge_phonecall_card_tx.setVisibility(View.VISIBLE);
		charge_phonecall_card_tx.setText(StatusUtil.getShortStr(cardnumberEnd, 4, "****"));
		charge_phonecall_card_edx.setVisibility(View.GONE);

	}

	private void bankTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(ChargePhoneCallActivity.this, "", "正在获取信息...", true, false);
		progressbar.setVisibility(View.VISIBLE);
		SafelotteryHttpClient.post(ChargePhoneCallActivity.this, "3102", "bank", initDate(), new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try {
					if (mMap != null) {
						ref = (String) mMap.get("ref");
						respCode = (String) mMap.get("respCode");
						resAmount = (String) mMap.get("resAmount");

						// 判断数据来源 如果来之用户源则将以有的数据赋给UserInfoBean
						if (!TextUtils.isEmpty((String) mMap.get("from"))) {
							if (mMap.get("from").equals("userMember")) {

								if (!TextUtils.isEmpty((String) mMap.get("realName"))) {
									GetString.userInfo.setRealName((String) mMap.get("realName"));
								}
								if (!TextUtils.isEmpty((String) mMap.get("cardType"))) {
									GetString.userInfo.setCardType((String) mMap.get("cardType"));
								}
								if (!TextUtils.isEmpty((String) mMap.get("cardCode"))) {
									GetString.userInfo.setCardCode((String) mMap.get("cardCode"));
								}
								if (!TextUtils.isEmpty((String) mMap.get("bankName"))) {
									GetString.userInfo.setBankName((String) mMap.get("bankName"));
								}
								if (!TextUtils.isEmpty((String) mMap.get("province"))) {
									GetString.userInfo.setProvince((String) mMap.get("province"));
								}
								if (!TextUtils.isEmpty((String) mMap.get("city"))) {
									GetString.userInfo.setCity((String) mMap.get("city"));
								}
								if (!TextUtils.isEmpty((String) mMap.get("bankCode"))) {
									GetString.userInfo.setBankCode((String) mMap.get("bankCode"));
								}
							}
						}

						getState();

						isnull = true;
						if (!TextUtils.isEmpty((String) mMap.get("bankName"))) {
							bankname = (String) mMap.get("bankName");
							ifShowBank(bankname);
						}
						if (!TextUtils.isEmpty((String) mMap.get("cardType"))) {
							cardtype = (String) mMap.get("cardType");
						} else {
							cardtype = "1";
						}
						if (!TextUtils.isEmpty((String) mMap.get("province"))) {
							bankprovince = (String) mMap.get("province");
							String bankProvinceStr[] = ChargePhoneCallActivity.this.getResources().getStringArray(R.array.province_item);
							mPositionOfBankProvince = ShowBankAddressTag(bankProvinceStr, bankprovince);
							LogUtil.DefalutLog("bankProvinceTag:" + mPositionOfBankProvince);
						}
						if (!TextUtils.isEmpty((String) mMap.get("city"))) {
							bankcity = (String) mMap.get("city");
							String bankCityStr[] = ChargePhoneCallActivity.this.getResources().getStringArray(
									TakeBankActivity.citys[mPositionOfBankProvince]);
							mPositionOfBankCity = ShowBankAddressTag(bankCityStr, bankcity);
							currentCityPos = mPositionOfBankCity;
							LogUtil.DefalutLog("bankCityTag:" + mPositionOfBankCity);
						}
						if (!TextUtils.isEmpty((String) mMap.get("bankCode"))) {
							banknumber = (String) mMap.get("bankCode");
						}
						if (!TextUtils.isEmpty((String) mMap.get("realName"))) {
							realname = (String) mMap.get("realName");
						}
						if (!TextUtils.isEmpty((String) mMap.get("cardCode"))) {
							cardnumber = (String) mMap.get("cardCode");
						}
						if (!TextUtils.isEmpty((String) mMap.get("mobile"))) {
							mobile = (String) mMap.get("mobile");
						}
					}
					setDate();
				} catch (Exception e) {
					if (Settings.DEBUG)
						Log.d(Settings.TAG, "onPostExecute-faile");
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}

			@Override
			public void onFinish() {
				progressbar.setVisibility(View.GONE);
				progresdialog.dismiss();
			}
		});
	}

	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

	// 证件类型弹出框
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
			ScrollView scroll = (ScrollView) findViewById(R.id.charge_phonecall_layout_scrollview);
			LinearLayout typeLny = (LinearLayout) findViewById(R.id.charge_phonecall_cardtype_layout);
			popDialogY = scroll.getTop() + typeLny.getTop() + typeLny.getHeight();
			mPopDialog = new PopWindowDialog(this, idType, cardTypeTag);
			mPopDialog.setPopViewPosition(0, popDialogY);
			mPopDialog.setOnClickListener(new OnclickFrequentListener() {

				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mPopDialog.getList();
					CheckBox checkBox;
					int id = v.getId();
					for (int i = 0, size = idType.length; i < size; i++) {
						checkBox = list.get(i);
						if (i == id) {
							if (id == 0) {
								cardtypeEnd = "1";
							} else if (id == 1) {
								cardtypeEnd = "2";
							} else if (id == 2) {
								cardtypeEnd = "3";
							} else {
								cardtypeEnd = "1";
							}
							checkBox.setChecked(true);
							charge_phonecall_cardtype_btn.setText(idType[id]);
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

	// 处理银行名字
	public void ifShowBank(String bankname) {
		String bankstr[] = ChargePhoneCallActivity.this.getResources().getStringArray(R.array.phonecallbanks);
		if (!bankname.equals("交通银行")) {
			for (int i = 0; i < bankstr.length; i++) {
				if (bankname.equals(bankstr[i])) {
					mPositionOfbankName = i;
					ifShowBank = true;
				}
			}
		}
	}

	// 处理银行位置
	public int ShowBankAddressTag(String[] bankstr, String str) {
		for (int i = 0; i < bankstr.length; i++) {
			if (bankstr[i].contains(str)) {
				return i;
			}
		}
		return 0;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.charge_phonecall_cardtype_btn) {
				popDialogShow();
			} else if (v.getId() == R.id.charge_phonecall_tell) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(ChargePhoneCallActivity.this);
					dialog.show();
				}
			} else if (v.getId() == R.id.charge_phonecall_finish_btn) {
				if (!TextUtils.isEmpty(charge_phonecall_phonenumber_edx.getText().toString().trim())) {
					mobileEnd = charge_phonecall_phonenumber_edx.getText().toString().trim();
				}
				if (!TextUtils.isEmpty(charge_phonecall_banknumber_edx.getText().toString().trim())) {
					banknumberEnd = charge_phonecall_banknumber_edx.getText().toString().trim();
				}
				if (!TextUtils.isEmpty(charge_phonecall_card_edx.getText().toString().trim())) {
					cardnumberEnd = charge_phonecall_card_edx.getText().toString().trim();
				}
				if (!TextUtils.isEmpty(charge_phonecall_money_edx.getText().toString().trim())) {
					moneyEnd = charge_phonecall_money_edx.getText().toString().trim();
				}
				if (!TextUtils.isEmpty(charge_phonecall_name_edx.getText().toString().trim())) {
					realnameEnd = charge_phonecall_name_edx.getText().toString().trim();
				}

				if (!TextUtils.isEmpty(realnameEnd)) {
					if (!TextUtils.isEmpty(cardtypeEnd)) {
						if (!TextUtils.isEmpty(cardnumberEnd)) {
							if (!TextUtils.isEmpty(mobileEnd) && ContactListViewActivity.IsUserNumber(mobileEnd)) {
								if (!TextUtils.isEmpty(banknameEnd) && !banknameEnd.equals("请选择")) {
									if (!TextUtils.isEmpty(bankprovinceEnd) && !bankprovinceEnd.equals("请选择")) {
										if (!TextUtils.isEmpty(bankcityEnd) && !bankcityEnd.equals("请选择")) {
											if (!TextUtils.isEmpty(banknumberEnd)) {
												if (!TextUtils.isEmpty(moneyEnd)) {
													int moneynum = ConversionUtil.StringToInt(moneyEnd);
													if (moneynum < 2) {
														Toast.makeText(getApplicationContext(), "充值金额必须大于等于2元", Toast.LENGTH_SHORT).show();
													} else {
														finishTask();
													}
												} else {
													Toast.makeText(getApplicationContext(), "充值金额不能为空", Toast.LENGTH_SHORT).show();
												}
											} else {
												Toast.makeText(getApplicationContext(), "请填写银行卡号", Toast.LENGTH_SHORT).show();
											}
										} else {
											Toast.makeText(getApplicationContext(), "请选择银行城市", Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(getApplicationContext(), "请选择银行省份", Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(getApplicationContext(), "请选择开户银行", Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(getApplicationContext(), "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "请输入证件号码", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "请选择证件类型", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请填写用户真实姓名", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private void finishTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(ChargePhoneCallActivity.this, "", "正在连接...", true, true);

		SafelotteryHttpClient.post(this, "3202", "phoneCall", initFinishDate(), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					Result mResult = null;
					mResult = new ResultParser().parse(response);
					if (mResult.getCode().equals(SystemInfo.succeeCode)) {
						Intent intent = new Intent(ChargePhoneCallActivity.this, ChargePhoneCallSuccessActivity.class);
						startActivity(intent);
					} else {
						if (!TextUtils.isEmpty(mResult.getResult())) {
							String resultStr = mResult.getResult();
							if (!TextUtils.isEmpty(resultStr)) {
								Map<String, String> errMap = JsonUtils.stringToMap(resultStr);
								if (errMap.get("err").contains("address")) {
									RiskNotesDialog dialog = new RiskNotesDialog(ChargePhoneCallActivity.this);
									dialog.setOnClickListener(new RiskNotesDialog.OnClickListener() {
										@Override
										public void onClick(String str) {
											inputAddress = str;
											isadddate = true;
											finishTask();
										}
									});
									dialog.show();
								} else {
									if (!TextUtils.isEmpty(errMap.get("err"))) {
										Toast.makeText(getApplicationContext(), errMap.get("err"), Toast.LENGTH_SHORT).show();
									}
								}
							}
						} else {
							if (!TextUtils.isEmpty(mResult.getMsg())) {
								String errstr = mResult.getMsg();
								Toast.makeText(getApplicationContext(), errstr, Toast.LENGTH_SHORT).show();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
			}

			@Override
			public void onFinish() {
				if (progresdialog != null) {
					if (progresdialog.isShowing()) {
						progresdialog.dismiss();
					}
				}
			}
		});
		StatService.onEvent(ChargePhoneCallActivity.this, "total-fund", "所有充值提交请求-银联卡", 1);
	}

	public String initFinishDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		map.put("amount", moneyEnd);
		map.put("realName", realnameEnd);
		map.put("cardType", cardtypeEnd);
		map.put("cardCode", cardnumberEnd);
		map.put("province", bankprovinceEnd);
		map.put("city", bankcityEnd);
		map.put("bankName", banknameEnd);
		map.put("bankCard", banknumberEnd);
		map.put("mobile", mobileEnd);
		if (isadddate == true) {
			map.put("address", inputAddress);
			map.put("ref", ref);
			map.put("respCode", respCode);
			map.put("resAmount", resAmount);
		}
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

}