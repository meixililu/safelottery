package com.zch.safelottery.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.Rsa;
import com.zch.safelottery.util.ToastUtil;

/*
 * 支付宝安全支付
 */
public class ChargeAlipaySafeActivity extends ZCHBaseActivity {

	public static final String SELLER = "2088701406173485";
	
	private EditText chargeMoney;
	private Button submit;
	private LinearLayout zchTell;
 
	private Dialog dialog;

	private String orderId ="";

	private String money;
	private String agentId;
	private String agentKey;
	private String alipayPrivateKey;
	private String agentPublicKey;
	private String alipayPublicKey;
	private String notifyUrl;
	// the handler use to receive the pay result.

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.alipay_safe_charge);
			initViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initViews() {

		chargeMoney = (EditText) findViewById(R.id.alipay_safe_charge_fillin);
		submit = (Button) findViewById(R.id.alipay_safe_charge_button);
		zchTell = (LinearLayout) findViewById(R.id.alipay_safe_charge_zch_tell);

		submit.setOnClickListener(onClickListener);
		zchTell.setOnClickListener(onClickListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int result, Intent data) {
		super.onActivityResult(requestCode, result, data);

		String action = data.getAction();
		String resultStatus = data.getStringExtra("resultStatus");
		String memo = data.getStringExtra("memo");
		String resultString = data.getStringExtra("result");
		if(resultStatus == null) resultStatus = "";
		if(resultString == null) resultString = "";
		
		if(resultStatus.equals("9000") && resultString.contains("success=\"true\"")){
			GetString.isAccountNeedRefresh = true;
			showChargeScuDialog();
		}else{
			if(TextUtils.isEmpty(memo)) memo = "操作已经取消";
			NormalAlertDialog dialog = new NormalAlertDialog(this, memo);
			dialog.setType(1);
			dialog.show();
		}
	}
	
	

	// 充值成功弹出框
	private void showChargeScuDialog() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.charge_scu_dialog, null);

		Button charge = (Button) view.findViewById(R.id.charge_scu_btn_sure);

		charge.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				dialog.dismiss();
				AccountChargeActivity.callMeFinish();
				finish();
			}
		});
		dialog = new Dialog(this, R.style.dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}

	public String getOrderInfo(String price) {
		String orderInfo = "partner=" + "\"" + agentId + "\"";
		orderInfo += "&";
		orderInfo += "seller_id=" + "\"" + SELLER + "\"";
		orderInfo += "&";
		orderInfo += "out_trade_no=" + "\"" + orderId + "\"";
		orderInfo += "&";
		orderInfo += "subject=" + "\"" + "支付宝充值" + "\"";
		orderInfo += "&";
		orderInfo += "body=" + "\"" + "中彩汇充值" + "\"";
		orderInfo += "&";
		orderInfo += "total_fee=" + "\"" + price + "\"";
		orderInfo += "&";
		orderInfo += "notify_url=" + "\"" + notifyUrl + "\"";

		// 接口名称， 定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型，定值
		orderInfo += "&payment_type=\"1\"";

		// 字符集，默认utf-8
		orderInfo += "&_input_charset=\"utf-8\"";

		// 超时时间 ，默认30分钟.
		// 设置未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		// 该功能需要联系支付宝配置关闭时间。
		orderInfo += "&it_b_pay=\"30m\"";

		// 商品展示网址,客户端可不加此参数
//		orderInfo += "&show_url=\"m.alipay.com\"";

		// verify(sign, orderInfo);

		return orderInfo;
	}

	// @SuppressWarnings("deprecation")
	// private static void verify(String sign, String content) {
	// String decodedSign = URLDecoder.decode(sign);
	// boolean isOk = Rsa.doCheck(content, decodedSign, RSA_PUBLIC);
	// System.out.println(isOk);
	// }

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	private String sign(String signType, String content) {
		return Rsa.sign(content, signType);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == R.id.alipay_safe_charge_button) {

				money = chargeMoney.getText().toString().trim();

				if (TextUtils.isEmpty(money) || money.equals("0")) {
					ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_prompt_bank);
				} else {
					doRequestTask();
				}
			} else if (v.getId() == R.id.alipay_safe_charge_zch_tell) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog=new TelePhoneShowDialog(ChargeAlipaySafeActivity.this);
					dialog.show();
				}
			} 
		}
	};
	
	private void doRequestTask() {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		SafelotteryHttpClient.post(this, "3202", "alipayIOS", initDate(), new TypeMapHttpResponseHandler(this, true) {
			
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					orderId=(String) mMap.get("orderId");
					money=(String) mMap.get("amount");
					notifyUrl=(String) mMap.get("notifyUrl");
					agentId=(String) mMap.get("agentId");
					alipayPrivateKey=(String) mMap.get("alipayPrivateKey");
					agentKey=(String) mMap.get("agentKey");
					agentPublicKey=(String) mMap.get("agentPublicKey");
					alipayPublicKey=(String) mMap.get("alipayPublicKey");
					
					String orderInfo = getOrderInfo(money);
					String sign = sign(alipayPrivateKey, orderInfo);
					try {
						sign = URLEncoder.encode(sign, "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
					String info = orderInfo + "&sign=" + "\"" + sign + "\"" + "&" + getSignType();
					
					Intent intent = new Intent();
					intent.setPackage(getPackageName());
					intent.setAction("com.alipay.mobilepay.android");
					intent.putExtra("order_info", info);
					startActivityForResult(intent, 0);
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
		StatService.onEvent(ChargeAlipaySafeActivity.this, "total-fund", "所有充值提交请求-支付宝", 1);
		
	}
	public String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		map.put("amount",money);
		String str =  JsonUtils.toJsonStr(map);
		return str;
	}

}