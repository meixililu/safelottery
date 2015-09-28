package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.XmlUtil;
import com.zch.safelottery.view.ListLineFeedView;
import com.zch.safelottery.view.ListLineFeedView.OnClickListListener;

public class ChargeChinaMoblieActivity extends ZCHBaseActivity {

	private final String[] ope = {"请选择", "中国移动", "中国联通", "中国电信"};
	/** 请选择 默认值 **/
	private final int TYPE_OUT = -1;
	/** 中国移动 **/
	private final int TYPE_MOBILE = 0;
	/** 中国联通 **/
	private final int TYPE_UNICOM = 1;
	/** 中国电信 **/
	private final int TYPE_TELECOM = 3;
	
	
	private EditText cardNumber;
	private EditText cardPassword;
	private Button submitButton;
	private Button operatorsSelect;
	private LinearLayout denomination;
	private LinearLayout zchTell;

	/** 卡类型 **/
	private int cardType = -1;// 运营商 0代表移动，1代表联通，3代表电信 -1请选择 
	/** 选中的卡类型的下标 **/
	private int operators = 0;
	private int money;
	private int checkedId;
	
	private double moneyRatio;
	
	private int cardnum = 17;
    private int pwdnum = 18;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.china_moblie_charge);

		initViews();
	}

	private void initViews() {

		cardNumber = (EditText) findViewById(R.id.china_moblie_charge_number);
		cardPassword = (EditText) findViewById(R.id.china_moblie_charge_password);
		operatorsSelect = (Button) findViewById(R.id.china_moble_charge_operators_elect);
		submitButton = (Button) findViewById(R.id.china_moblie_charge_button);
		
		denomination = (LinearLayout) findViewById(R.id.china_moblie_charge_denomination);
		zchTell = (LinearLayout) findViewById(R.id.china_moblie_charge_zch_tell);

		operatorsSelect.setOnClickListener(onClickListener);
		submitButton.setOnClickListener(onClickListener);
		zchTell.setOnClickListener(onClickListener);
		
		TextView remind = (TextView)findViewById(R.id.china_moblie_charge_remind);
		//改变字段中部分字符的颜色
		String r = getResources().getString(R.string.charge_remind);
		SpannableString msp = new SpannableString(r);
		int end = r.indexOf("2. ");
		msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.content_txt_red)), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		remind.setText(msp);
		getDenomination(1);
		operatorsSelect.setText(cardType(TYPE_OUT));
	}

	private void getDenomination(int cardType){
		money = 0;
		operatorsSelect.setText(cardType(cardType));
		switch(cardType){
			case TYPE_MOBILE:
				cardnum = 17;
				pwdnum = 18;
				moneyRatio = 0.03;
				addOpt(new String[]{"10元", "30元", "50元", "100元"});
				break;
			case TYPE_UNICOM:
				cardnum = 15;
				pwdnum = 19;
				moneyRatio = 0.03;
				addOpt(new String[]{"20元", "30元", "50元", "100元"});
				break;
			case TYPE_TELECOM:
				cardnum = 19;
				pwdnum = 18;
				moneyRatio = 0.03;
				addOpt(new String[]{ "50元", "100元"});
				break;
//			default :
//				addOpt(new String[]{});
		}
		cardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardnum)});
		cardPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pwdnum)});
	}
	
	private List<CheckBox> listChecks;
	private void addOpt(String[] deno){
		denomination.removeAllViews();
		
		listChecks = XmlUtil.getCheckBox(this, R.layout.chuanguan_checkbox, clickL, deno, TYPE_OUT);
		ListLineFeedView listView = new ListLineFeedView(this, listChecks, ope.length);
	    
		denomination.addView(listView.getView());
	}
	
	private OnClickListListener clickL = new OnClickListListener() {
		
		@Override
		public void onClick(View v) {
			CheckBox checkBox;
			int id = v.getId();
			for(int i = 0, size = listChecks.size(); i < size; i++){
				checkBox = listChecks.get(i);
				if(i == id){
					money = ConversionUtil.StringToInt(checkBox.getText().toString().replace("元", ""));
					checkBox.setChecked(true);
				}else{
					if(checkBox.isChecked()) checkBox.setChecked(false);
				}
			}
			
		}
	};
	
	private boolean getCardVerify(int cardType, String number){
		try {
			if(cardType == TYPE_MOBILE){
				return true;
			}else if(cardType == TYPE_UNICOM){
				return true;
			}else if(cardType == TYPE_TELECOM){
//				System.out.println("number.charAt(3) :: " + number.charAt(3));
//				if(number.charAt(3) == '1')
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void opePopDialogShow() {
		// 敢得当前控件所在的位置

		int popDialogY = SystemInfo.height / 2 - denomination.getHeight();

		final PopWindowDialog mOpePopDialog = new PopWindowDialog(this, ope,	operators);
		mOpePopDialog.setPopViewPosition(0, popDialogY);
		mOpePopDialog.setOnClickListener(new OnclickFrequentListener() {

			@Override
			public void onClick(View v) {
				final ArrayList<CheckBox> list = mOpePopDialog.getList();
				CheckBox checkBox;
				operators = v.getId();
				for (int i = 0, size = ope.length; i < size; i++) {
					checkBox = list.get(i);
					if (i == operators) {
						if (operators == 1) {
							cardType = TYPE_MOBILE;
						} else if (operators == 2) {
							cardType = TYPE_UNICOM;
						} else if (operators == 3) {
							cardType = TYPE_TELECOM;
						} else {
							cardType = TYPE_OUT;
						}
						checkBox.setChecked(true);
						getDenomination(cardType);
					} else {
						if (checkBox.isChecked())
							checkBox.setChecked(false);
					}
				}
				mOpePopDialog.dismiss();

			}
		});
		mOpePopDialog.show();
	}
	
	private void showChargeScuDialog() {
		NormalAlertDialog dialog = new NormalAlertDialog(ChargeChinaMoblieActivity.this);
		dialog.setContent("服务器处理成功，请在3-5分钟后，查看充值结果。");
		dialog.setOk_btn_text("刷新账户");
		dialog.setCancle_btn_text("继续充值");
		dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

			@Override
			public void onOkBtnClick() {
				GetString.isAccountNeedRefresh = true;
				finish();
				Intent intent = new Intent();
				intent.setClass(ChargeChinaMoblieActivity.this, MainTabActivity.class);
				intent.putExtra(Settings.TABHOST, Settings.USERHOME);
				ChargeChinaMoblieActivity.this.startActivity(intent);
			}

			@Override
			public void onCancleBtnClick() {
			}
		});
		
		dialog.show();
	}

	private String cardType(int pre){
		switch (pre) {
		case TYPE_MOBILE:
			return "中国移动";
		case TYPE_UNICOM:
			return "中国联通";
		case TYPE_TELECOM:
			return "中国电信";
		default:
			return "请选择";
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			checkedId = v.getId();

			if (checkedId == R.id.china_moble_charge_operators_elect) {
				opePopDialogShow();
			}else if (checkedId == R.id.china_moblie_charge_button) {

				final String card = cardNumber.getText().toString().trim();
				final String pwd = cardPassword.getText().toString().trim();
				
				if(cardType == TYPE_OUT){
					ToastUtil.diaplayMesShort(getApplicationContext(), "请选择卡类型");
				}else if(money == 0){
					ToastUtil.diaplayMesShort(getApplicationContext(), "请选择充值面额");					
				}else if (TextUtils.isEmpty(card)) {
					ToastUtil.diaplayMesShort(getApplicationContext(), "请填写卡号");					
//				}else if (!getCardVerify(cardType, card)) {
//					ToastUtil.diaplayMesShort(getApplicationContext(), "请填写正确卡号");					
				} else if (TextUtils.isEmpty(pwd)) {
					ToastUtil.diaplayMesShort(getApplicationContext(), "请填写密码");					
				}else if (card.length()!=cardnum) {
					ToastUtil.diaplayMesLong(getApplicationContext(), "卡号位数不对,请重新输入"+cardnum+"位卡号");					
				}else if (pwd.length()!=pwdnum) {
					ToastUtil.diaplayMesLong(getApplicationContext(), "密码位数不对,请重新输入"+pwdnum+"位密码");					
				}else {
					float m = 0.00f;
					m = (float) (money - money * moneyRatio);
					NormalAlertDialog dialog = new NormalAlertDialog(ChargeChinaMoblieActivity.this);
					dialog.setTitle("充值提示");
					String str = "您当前选择的是"+cardType(cardType) + "充值卡" +",面值"+money+"元,到账金额"+m+"元.请您认真核对,否则会导致充值失败并且充值卡失效!您确认提交吗?";
					dialog.setContent(str);
					dialog.setOk_btn_text("确认提交");
					dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
						@Override
						public void onOkBtnClick() {
							chargeTask(card, pwd);
						}
						
						@Override
						public void onCancleBtnClick() {
						}
					});
					dialog.show();
				}
			} else if (checkedId == R.id.china_moblie_charge_zch_tell) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog( ChargeChinaMoblieActivity.this);
					dialog.show();
				}
			}
		}
	};
	
	private void chargeTask(String cardCode, String cardPass) {

		final ProgressDialog progresdialog = ProgressDialog.show(ChargeChinaMoblieActivity.this, "", "正在充值...", true, true);


		if(Settings.DEBUG) Log.i("TAG", " - currentKind : " + cardType + " - chargeMoney :" + money);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("amount", String.valueOf(money));
		map.put("bossType", String.valueOf(cardType));
		map.put("cardNumber", cardCode);
		map.put("cardPwd", cardPass);
		SafelotteryHttpClient.post(this, "3202", "oufei", JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				if(mResult != null){
					Map<String, String> map = JsonUtils.stringToMap(mResult.getResult());
					if(map.get("returncode").equals("120")){
						showChargeScuDialog();
						GetString.isAccountNeedRefresh = true;
					}else{
						if(!TextUtils.isEmpty(mResult.getResult())){
							ToastUtil.diaplayMesShort(getApplicationContext(), map.get("msg") + "(" + map.get("returncode") + ")" );
						}else{
							ToastUtil.diaplayMesShort(getApplicationContext(), mResult.getMsg());
						}
					}
				}else{
					LogUtil.CustomLog("TAG", "ChargeChinaMoblieActivity -- Result is null");
					ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
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
		
		StatService.onEvent(ChargeChinaMoblieActivity.this, "total-fund", "所有充值提交请求-充值卡", 1);
	}
}