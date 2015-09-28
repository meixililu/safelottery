package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class ChargeUnionpayActivity extends ZCHBaseActivity implements OnClickListener {

	// 商户服务器地址（商户需自己实现，请参考 后台开发sdk）
	private final String MODE_RELEASE = "00"; // 正式
	private final String MODE_TEST = "01"; // 测试
	
	private Button btnNowpay;
	private LinearLayout tell;
	private EditText etTn;
	private String payMode = MODE_RELEASE;
	
	private String money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_unionpay);

//		payMode = GetString.TEST? MODE_TEST: MODE_RELEASE;
		
		etTn = (EditText) findViewById(R.id.alipay_charge_fillin);
		btnNowpay = (Button) findViewById(R.id.alipay_charge_button);
		tell = (LinearLayout) findViewById(R.id.alipay_safe_charge_zch_tell);

		btnNowpay.setOnClickListener(this);
		tell.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.alipay_charge_button: 
			money = etTn.getText().toString();
			if (TextUtils.isEmpty(money)) {
				ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_prompt_bank);
				return;
			}
			requestData();
			break;
		case R.id.alipay_safe_charge_zch_tell:
			if (Settings.isNeedPhone(getApplicationContext())) {
				TelePhoneShowDialog dialog=new TelePhoneShowDialog(ChargeUnionpayActivity.this);
				dialog.show();
			}
			break;
		default:
			break;
		}
	}

	private void requestData(){
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true,true);
		
		SafelotteryHttpClient.post(this, "3202", "yinlian", initDate(), new TypeMapHttpResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try {
					if(mMap != null){
						String tn=(String) mMap.get("tn");
						if(!TextUtils.isEmpty(tn)){
//						System.out.println(tn + "     " + payMode);
							startUPPay(tn);
						}else{
							LogUtil.CustomLog("TAG", "ChargeHuiFuiTianXiaActivity -- URL is null");
							ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
						}
					}else{
						LogUtil.CustomLog("TAG", "ChargeHuiFuiTianXiaActivity -- Map is null");
						ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
					}
				} catch (Exception e) {
					e.printStackTrace();
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

//		StatService.onEvent(this, "total-fund", "所有充值提交请求-信用卡", 1);
	}
	
	private void startUPPay(String tn) throws Exception{
		/**
		activity  用来启动一个activity的context，比如传入当前的activity
		payCls  银联Activity，固定PayActivity.class
		spId  保留使用，这里输入null 
		sysProvider  保留使用，这里输入null 
		tn  提交订单银联返回的交易流水号tn
		mode  调用插件模式，01是测试，00是正式
		*/
		UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null, tn, payMode);
	}
	
	private String initDate() {
		String userCode = GetString.userInfo.getUserCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCode);
		map.put("amount", money);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		NormalAlertDialog builder = new NormalAlertDialog(this, "支付结果通知", msg);
		builder.setType(1);
		builder.setButtonOnClickListener(new OnButtonOnClickListener() {
			
			@Override
			public void onOkBtnClick() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancleBtnClick() {
				// TODO Auto-generated method stub
				
			}
		});
		builder.show();
	}

}
