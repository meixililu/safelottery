package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.UserInfoParser;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;

public class TakeBankActivity extends ZCHBaseActivity {

	private int state;
	/** 绑定 **/
	public static final int TAKE_BOUND = 1;
	/** 已绑定 提现 **/
	public static final int TAKE_AMOUNT = 2;

	private LinearLayout moneyShowLy;
	private TextView card_prompt;
	private TextView remainTv;
	private TextView remainMoneyTv;
	private EditText takeMoneyTv;
	private EditText bankNumberEd;
	private EditText reNumberEd;
	private Spinner bankNameSpn;
	private Spinner provinceSpn;
	private Spinner citySpn;
	private Button submitBtn;
	private Button finishBtn;
	private EditText branchEd;
	private TextView branchTv;
	private LinearLayout relayoutLy;
	private TextView bankNumberTv;
	private TextView bankNameTv;
	private TextView adressTv;
	private LinearLayout cityLy;
	private LinearLayout branchLy;
	private ScrollView take_bank_scrollview;
	private ProgressBar progressbar_s,progressbar_m;
	private TextView take_bank_prompt;

	private ArrayAdapter province_adapter;
	private ArrayAdapter city_adapter;
	private ArrayAdapter bank_adapter;
	private int provinceId;

	private String money;
	private String bankNumber;
	private String province;
	private String city;
	private String bankName;
	private String branch;
	private View zchTell;
	private int isNoNeedBrancd;// 1为需要省市开户地址，2为只需要省市，3为不需要省市开户地址
	private String[] noNeedBranchBankList;
	private String[] justNeedCityBankList;
	private Dialog dialog;
	private double canTakeCashMoney;
	private double remandMoney;

	private Context mContext;
	

	public static int[] citys = { R.array.select_item, R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item,
			R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item,
			R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item,
			R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item, R.array.guangxi_province_item, R.array.hainan_province_item,
			R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item,
			R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.taiwan_province_item,R.array.xianggang_province_item,R.array.aomen_province_item};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_bank);
		mContext = this;
		initView();
		userTask();
	}

	private void initView() {
		card_prompt = (TextView) findViewById(R.id.take_bank_card_prompt);
		moneyShowLy = (LinearLayout) findViewById(R.id.take_bank_take_money);
		remainTv = (TextView) findViewById(R.id.take_bank_remain_tv);
		zchTell = (View) findViewById(R.id.take_bank_zch_tell);
		remainMoneyTv = (TextView) findViewById(R.id.take_bank_remain);
		takeMoneyTv = (EditText) findViewById(R.id.take_bank_money);
		bankNameSpn = (Spinner) findViewById(R.id.take_bank_bankname_spinner);
		bankNumberEd = (EditText) findViewById(R.id.take_bank_card_number);
		reNumberEd = (EditText) findViewById(R.id.take_bank_renumber);
		provinceSpn = (Spinner) findViewById(R.id.take_bank_city_province);
		citySpn = (Spinner) findViewById(R.id.take_bank_city_city);
		submitBtn = (Button) findViewById(R.id.take_bank_button_submit);
		finishBtn = (Button) findViewById(R.id.take_bank_finish);
		branchEd = (EditText) findViewById(R.id.take_bank_card_branch);
		branchTv = (TextView) findViewById(R.id.take_bank_car_branch_text);
		relayoutLy = (LinearLayout) findViewById(R.id.take_bank_banknumber_agin);
		bankNumberTv = (TextView) findViewById(R.id.take_bank_cardnumber_text);
		bankNameTv = (TextView) findViewById(R.id.take_bank_name_text);
		adressTv = (TextView) findViewById(R.id.take_bank_adress_text);
		cityLy = (LinearLayout) findViewById(R.id.take_bank_city_layout);
		branchLy = (LinearLayout) findViewById(R.id.take_bank_branch);
		progressbar_s = (ProgressBar) findViewById(R.id.take_bank_progressbar_s);
		progressbar_m = (ProgressBar) findViewById(R.id.take_bank_progressbar_m);
		take_bank_scrollview = (ScrollView) findViewById(R.id.take_bank_scrollview);
		take_bank_prompt = (TextView) findViewById(R.id.take_bank_prompt);

		submitBtn.setOnClickListener(onClickListener);
		finishBtn.setOnClickListener(onClickListener);
		zchTell.setOnClickListener(onClickListener);
		take_bank_prompt.setOnClickListener(onClickListener);

		noNeedBranchBankList = this.getResources().getStringArray(R.array.banks_no_need_brancd);
		justNeedCityBankList = this.getResources().getStringArray(R.array.banks_just_need_city);
	}

	private void initDate() {
		switch (state) {
		case TAKE_BOUND:
			getBoundBank();
			break;
		case TAKE_AMOUNT:
			getTakeAmount();
			break;

		default:
			break;
		}
	}

	private void allGone() {
		moneyShowLy.setVisibility(View.GONE);
		remainTv.setVisibility(View.GONE);
		bankNameSpn.setVisibility(View.GONE);
		bankNumberEd.setVisibility(View.GONE);
		card_prompt.setVisibility(View.GONE);
		reNumberEd.setVisibility(View.GONE);
		relayoutLy.setVisibility(View.GONE);
		provinceSpn.setVisibility(View.GONE);
		branchEd.setVisibility(View.GONE);
		citySpn.setVisibility(View.GONE);
		cityLy.setVisibility(View.GONE);
		branchLy.setVisibility(View.GONE);

		bankNumberTv.setVisibility(View.GONE);
		bankNameTv.setVisibility(View.GONE);
		adressTv.setVisibility(View.GONE);
		branchTv.setVisibility(View.GONE);
	}

	/**
	 * 绑定银行
	 */
	private void getBoundBank() {
		allGone();

		bankNameSpn.setVisibility(View.VISIBLE);
		bankNumberEd.setVisibility(View.VISIBLE);
		card_prompt.setVisibility(View.VISIBLE);
		reNumberEd.setVisibility(View.VISIBLE);
		relayoutLy.setVisibility(View.VISIBLE);
		provinceSpn.setVisibility(View.VISIBLE);
		branchEd.setVisibility(View.VISIBLE);
		citySpn.setVisibility(View.VISIBLE);
		cityLy.setVisibility(View.VISIBLE);
		branchLy.setVisibility(View.VISIBLE);

		boundDispose();
	}

	private void boundDispose() {

		remainMoneyTv.setText("请设置银行卡！");
		bankNameSpn.setPrompt("请选择银行");
		provinceSpn.setPrompt("请选择省份");
		citySpn.setPrompt("请选择城市");

		province_adapter = ArrayAdapter.createFromResource(this, R.array.province_item, R.layout.spinner_text_style_nomel);
		province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		provinceSpn.setAdapter(province_adapter);

		provinceSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				provinceId = provinceSpn.getSelectedItemPosition();
				select(citySpn, city_adapter, citys[provinceId], 0);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		bank_adapter = ArrayAdapter.createFromResource(this, R.array.banks, R.layout.spinner_text_style_nomel);
		bank_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bankNameSpn.setAdapter(bank_adapter);
		bankNameSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				bankName = bankNameSpn.getSelectedItem().toString().trim();
				isNeedBranch(bankName);
				if (isNoNeedBrancd == 3) {
					branchEd.setText("");
					cityLy.setVisibility(View.GONE);
					branchLy.setVisibility(View.GONE);
				} else if (isNoNeedBrancd == 2) {
					branchEd.setText("");
					cityLy.setVisibility(View.VISIBLE);
					branchLy.setVisibility(View.GONE);
				} else {
					branchEd.setHint("请输入开户支行");
					cityLy.setVisibility(View.VISIBLE);
					branchLy.setVisibility(View.VISIBLE);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	private void select(Spinner spin, ArrayAdapter<CharSequence> adapter, int arry, int selection) {
		adapter = ArrayAdapter.createFromResource(this, arry, R.layout.spinner_text_style_nomel);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
	}

	/**
	 * 取款
	 */
	private void getTakeAmount() {
		allGone();

		moneyShowLy.setVisibility(View.VISIBLE);
		remainTv.setVisibility(View.VISIBLE);
		bankNumberTv.setVisibility(View.VISIBLE);
		bankNameTv.setVisibility(View.VISIBLE);
		adressTv.setVisibility(View.VISIBLE);

		takeDispose();
	}

	private void takeDispose() {
		if (GetString.userInfo != null) {
			String temp = GetString.userInfo.getDrawAmount();
			canTakeCashMoney = Double.parseDouble(TextUtils.isEmpty(temp) ? "0" : temp);
			remainMoneyTv.setText("￥" + GetString.df.format(canTakeCashMoney));

			bankNumberTv.setText(GetString.userInfo.getBankCode());
			bankNameTv.setText(GetString.userInfo.getBankName());
			String tempProvince = GetString.userInfo.getProvince();
			String tempCity = GetString.userInfo.getCity();
			String tempSubBank = GetString.userInfo.getSubBank();
			if(!TextUtils.isEmpty(tempProvince)){
				cityLy.setVisibility(View.VISIBLE);
				adressTv.setText(tempProvince + " " + tempCity);
			}
			if (GetString.userInfo.getBankName().equals("华夏银行") && TextUtils.isEmpty(tempSubBank)) {
				branchLy.setVisibility(View.VISIBLE);
				branchTv.setVisibility(View.GONE);
				branchEd.setVisibility(View.VISIBLE);
			}else if(!TextUtils.isEmpty(tempSubBank)){
				branchLy.setVisibility(View.VISIBLE);
				branchTv.setVisibility(View.VISIBLE);
				branchTv.setText(tempSubBank);
			}
		}
	}

	/**
	 * 提款金额不能大于可提现金额
	 * 
	 * @param moneyStr
	 * @return
	 */
	private boolean isMoneyRight(String moneyStr) {
		try {
			double money = Double.parseDouble(moneyStr);
			if (GetString.userInfo != null) {
				remandMoney = Double.parseDouble(GetString.userInfo.getDrawAmount());
			}
			return remandMoney >= money;
		} catch (Exception e) {
		}
		return false;
	}

	// 1为需要省市开户地址，2为只需要省市，3为不需要省市开户地址
	private void isNeedBranch(String bank) {
		isNoNeedBrancd = 1;
		for (String item : noNeedBranchBankList) {
			if (item.equals(bank)) {
				isNoNeedBrancd = 3;// 无需填写省市开户地址
				return;
			}
		}
		for (String item : justNeedCityBankList) {
			if (item.equals(bank)) {
				isNoNeedBrancd = 2;// 只需填写省市
				return;
			}
		}
	}

	private void takeMoneyTask(){
		if (state == TAKE_BOUND) {
			final ProgressDialog progresdialog = ProgressDialog.show(TakeBankActivity.this, "", "正在提交...", true, true);
			progresdialog.show();
			Map<String, String> map = new HashMap<String, String>();
			map.put("userCode", GetString.userInfo.getUserCode());
			map.put("province", province);
			map.put("city", city);
			map.put("subBankName", branch);
			map.put("bankName", bankName);
			map.put("bankCard", bankNumber);
			SafelotteryHttpClient.post(this, "3103", "bank", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if (mResult.getCode().equals("0000")) {
						state = TAKE_AMOUNT;
						GetString.userInfo.setProvince(province);
						GetString.userInfo.setCity(city);
						GetString.userInfo.setSubBank(branch);
						GetString.userInfo.setBankName(bankName);
						GetString.userInfo.setBankCode(bankNumber);
						ToastUtil.diaplayMesShort(mContext, "银行卡绑定成功");

						initDate();
					} else {
						ToastUtil.diaplayMesShort(mContext, mResult.getMsg());
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progresdialog.dismiss();
				}
			});
		}else if (state == TAKE_AMOUNT) {
			final ProgressDialog progresdialog = ProgressDialog.show(TakeBankActivity.this, "", "正在提交...", true, true);
			progresdialog.show();
			Map<String, String> map = new HashMap<String, String>();
			map.put("userCode", GetString.userInfo.getUserCode());
			map.put("amount", money);
			if (!TextUtils.isEmpty(branch)) {
				map.put("subBank", branch);
			}
			SafelotteryHttpClient.post(this, "3203", "bank", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					if (mResult.getCode().equals("0000")) {
						// 提款成功处理
						ToastUtil.diaplayMesShort(mContext, "提款成功");
						int TakeMoney = ConversionUtil.StringToInt(money);
						if (GetString.userInfo != null) {
							/**
							 * 成功之后修改本地数据，手续费是从可提现金额中扣除的，比如提款10，可提现金额大于12就扣除12元，
							 * 如果小于12大于等于10，那么服务器就从10元中扣除2元手续费
							 **/
							if (remandMoney - TakeMoney >= 2) {
								GetString.userInfo.setDrawAmount((remandMoney - TakeMoney - 2) + "");
							} else if (remandMoney - TakeMoney >= 0) {
								GetString.userInfo.setDrawAmount((remandMoney - TakeMoney) + "");
							}
							
							if ( GetString.userInfo.getBankName().equals("华夏银行") 
									&& TextUtils.isEmpty(GetString.userInfo.getSubBank()) 
									&& !TextUtils.isEmpty(branch) ) {
								GetString.userInfo.setSubBank(branch);
							}
						}
						GetString.isAccountNeedRefresh = true;
						finish();
					} else {
						ToastUtil.diaplayMesShort(mContext, mResult.getMsg());
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					progresdialog.dismiss();
				}
			});
		}
	}

	public void showTakeScuDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.take_money_scu_dialog, null);
		Button ok = (Button) view.findViewById(R.id.take_money_scu_btn_sure);
		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
				GetString.isAccountNeedRefresh = true;
				finish();
			}
		});

		dialog = new Dialog(this, R.style.dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();
		dialog.setCancelable(false);

	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.take_bank_zch_tell) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(TakeBankActivity.this);
					dialog.show();
				}
			} else if (id == R.id.take_bank_button_submit || id == R.id.take_bank_finish) {

				money = takeMoneyTv.getText().toString().trim();
				branch = branchEd.getText().toString().trim();

				if (state == TAKE_BOUND) {
					
					province = provinceSpn.getSelectedItem().toString();
					city = citySpn.getSelectedItem().toString();
					bankNumber = bankNumberEd.getText().toString().trim();
					String reNumber = reNumberEd.getText().toString().trim();
					if (TextUtils.isEmpty(bankNumber)) {
						displayMsg("请填写银行卡号");
					}else if (TextUtils.isEmpty(reNumber)) {
						displayMsg("请填写重复银行卡号");
					}else if (!bankNumber.equals(reNumber)) {
						displayMsg("银行卡号填写不一致");
					}else if(isNoNeedBrancd == 1 && !isSelectCity()){// 1为需要省市开户地址，2为只需要省市，3为不需要省市开户地址
						//nothing
					}else if(isNoNeedBrancd == 1 && isFillBankAddress()){
						//nothing
					}else if (isNoNeedBrancd == 2 && !isSelectCity()) {
						//nothing
					}else{
						if (isNoNeedBrancd == 3) {
							province = "";
							city = "";
						}
						takeMoneyTask();
//						StatService.onEvent(TakeBankActivity.this, BaiduStatistics.Drawdown, "提款银行信息以及个人信息完善,点击提交", 1);
					}
				} else {
					if (TextUtils.isEmpty(money)) {
						Toast.makeText(getApplicationContext(), "请填写提款金额", 0).show();
					} else if (Integer.parseInt(money) <= 0) {
						Toast.makeText(getApplicationContext(), "提款金额应大于0元", 0).show();
					} else if ( GetString.userInfo.getBankName().equals("华夏银行") 
							&& TextUtils.isEmpty(GetString.userInfo.getSubBank()) 
							&& TextUtils.isEmpty(branch) ) {
							displayMsg("请填写开户支行");
					} else {
						if (isMoneyRight(money)) {
							takeMoneyTask();
						} else {
							Toast.makeText(getApplicationContext(), "提款金额不能大于可提现金额", 0).show();
						}
					}
				}
			}else if(id == R.id.take_bank_prompt){
				userTask();
			}

		}
	};
	
	/**省市选择与否判断
	 * @return
	 */
	private boolean isSelectCity(){
		boolean isSelect = false;
		if (TextUtils.isEmpty(province) || province.equals("请选择")) {
			displayMsg("请选择省份");
		}else if (TextUtils.isEmpty(city) || city.equals("请选择")) {
			displayMsg("请选择城市");
		}else{
			isSelect = true;
		}
		return isSelect;
	}
	
	/**填写开户支行判断
	 * @return
	 */
	private boolean isFillBankAddress(){
		boolean isFillBankAddress = TextUtils.isEmpty(branch);
		if(isFillBankAddress){
			displayMsg("请填写开户支行");
		}
		return isFillBankAddress;
	}
	
	/**显示toast信息
	 * @param content
	 */
	private void displayMsg(String content){
		Toast.makeText(getApplicationContext(), content, 0).show();
	}
	
	private void userTask() {
		if(GetString.userInfo != null){
			take_bank_scrollview.setVisibility(View.GONE);
			take_bank_prompt.setVisibility(View.GONE);
			submitBtn.setVisibility(View.GONE);
			progressbar_s.setVisibility(View.VISIBLE);
			progressbar_m.setVisibility(View.VISIBLE);
			
			HashMap<String,String> requestMap = new HashMap<String,String>();
			requestMap.put("userCode", GetString.userInfo.getUserCode());
			SafelotteryHttpClient.post( this, "3102", "", JsonUtils.toJsonStr(requestMap), new TypeMapHttpResponseHandler(this, true) {
				
				@Override
				public void onSuccess(int statusCode, Map mMap) {
					if(mMap != null){
						take_bank_scrollview.setVisibility(View.VISIBLE);
						UserInfoParser.setData(mMap);
						if(!TextUtils.isEmpty(GetString.userInfo.getBankCode()) && !TextUtils.isEmpty(GetString.userInfo.getBankName())){
							state = TAKE_AMOUNT;
						}else{
							state = TAKE_BOUND;
						}
						initDate();
					}else{
						take_bank_prompt.setVisibility(View.VISIBLE);
					}
				}
				
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(){
					submitBtn.setVisibility(View.VISIBLE);
					progressbar_s.setVisibility(View.GONE);
					progressbar_m.setVisibility(View.GONE);
				}
			});
		}
	}
	
}
