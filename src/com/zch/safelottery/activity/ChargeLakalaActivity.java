package com.zch.safelottery.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.ChargeLakalaBean;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.parser.ChargeLakalaParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AppInstallHelper;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.Md5;
import com.zch.safelottery.util.ToastUtil;

public class ChargeLakalaActivity extends Activity {
	public final int KEY_REQUEST = 9;
	/***拉卡拉 包***/
	private final String PAYBY_LAKALA_PACKAGENAME = "com.lakala.android";
	/***拉卡拉 类***/
	private final String PAYBY_LAKALA_CLASSNAME = "com.lakala.android.payplatform.PayByLakalaActivity";
	private final String KEY_ISTRANSACTION = "transactionOk";
	
	private final String DOWNLOAD_APK = 	"http://download.lakala.com.cn/Lakala_Android.apk";
	
	private EditText chargeET;
	private Button chargeBtn;
	private LinearLayout chargeLayout;

	private String money;
	
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.charge_lakala);
		initViews();
		
		mContext = this;
	}

	private void initViews() {
		chargeET = (EditText) findViewById(R.id.alipay_charge_fillin);
		chargeBtn = (Button) findViewById(R.id.alipay_charge_button);
		chargeLayout = (LinearLayout) findViewById(R.id.alipay_safe_charge_zch_tell);

		MyOnClickListener myOnClickListener = new MyOnClickListener();
		chargeBtn.setOnClickListener(myOnClickListener);
		chargeLayout.setOnClickListener(myOnClickListener);
	}

	private void showActivity(ChargeLakalaBean mBean){
		// 固定值
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(PAYBY_LAKALA_PACKAGENAME, PAYBY_LAKALA_CLASSNAME));
		// 参数配置
		Bundle bundle = new Bundle();
		bundle.putString("ver",mBean.getVersion());//20060301为固定值
		
		bundle.putString("merId",mBean.getMerId());//2306是拉卡拉的固定账单号  123456789为商户号
		bundle.putString("orderId",mBean.getOrderId()); //bill_trd_md5_s为拉卡拉为商户  分配的商户订单号
		bundle.putString("time",mBean.getTime());//交易时间
		bundle.putString("amount",mBean.getAmount()); //100.20 以元为单位，小数点后两位
		bundle.putString("minCode",mBean.getMinCode()); //2231为拉卡拉为快捷商户分配
		
		bundle.putString("macType", mBean.getMacType());//效验类型 ，"2"为MD5验证.
		
		String SEPARATOR = "|";
		// 快捷签名
		StringBuilder sb = new StringBuilder();
		sb.append(mBean.getVersion()).append(SEPARATOR).append(mBean.getMerId()).append(SEPARATOR)
			.append(mBean.getMerPwd()).append(SEPARATOR).append(mBean.getOrderId()).append(SEPARATOR).append(mBean.getAmount()).append(SEPARATOR)
						.append("").append(SEPARATOR).append(mBean.getNotifyUrl())
						.append(SEPARATOR).append(mBean.getMacType()).append(SEPARATOR)
						.append(mBean.getMinCode()).append(SEPARATOR);
		String mac =  Md5.d5(sb.toString());//HashUtil为MD5加密处理类，
		bundle.putString("mac",mac);

//		if(TextUtils.isEmpty(expriredtime)){
//			bundle.putString("expriredtime", expriredtime);//失效时间 (不是必须传递参数)
//		}
//		if(TextUtils.isEmpty(desc)){
//			bundle.putString("desc", desc); //订单描述的信息(不是必须传递参数))
//		}
//		if(TextUtils.isEmpty(randnum)){
//			bundle.putString("randnum",randnum);//随机数(不是必须传递参数)
//		}
//		if(TextUtils.isEmpty(productName)){
//			bundle.putString("productName", productName); //商品名称(不是必须传递参数)
//		}
		
		intent.putExtra("payInformation",bundle);
		
		ChargeLakalaActivity.this.startActivityForResult(intent, KEY_REQUEST);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == KEY_REQUEST && resultCode == RESULT_OK){
			 isTransaction(data);
		}
		
	}

	private void isTransaction(Intent intent) {
//		Intent intent = getIntent();
		String isTransactionOk = intent.getStringExtra(KEY_ISTRANSACTION);
		if(null!=isTransactionOk&&isTransactionOk.equals("true")){
			GetString.isAccountNeedRefresh = true;
//			Toast.makeText(this, "充值成功", Toast.LENGTH_LONG).show();
			
			NormalAlertDialog dialog = new NormalAlertDialog(ChargeLakalaActivity.this);
			dialog.setContent("充值成功，请耐心等待，刷新账户查询是否到账！");
			dialog.setOk_btn_text("更新账户");
			dialog.setCancle_btn_text("继续充值");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {

				@Override
				public void onOkBtnClick() {
					GetString.isAccountNeedRefresh = true;
					finish();
					Intent intent = new Intent();
					intent.setClass(ChargeLakalaActivity.this, MainTabActivity.class);
					intent.putExtra(Settings.TABHOST, Settings.USERHOME);
					ChargeLakalaActivity.this.startActivity(intent);
				}

				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
			
		}else if(null!=isTransactionOk&&isTransactionOk.equals("false")){
			Toast.makeText(this, "充值失败", Toast.LENGTH_LONG).show();
		}
	}
	
	private void doRequestTask(){
		final ProgressDialog progresDialog = ProgressDialog.show(ChargeLakalaActivity.this, "", "正在生成订单号...", true,true);

		SafelotteryHttpClient.post(mContext, "3202", "lakalaPay", prepareData(), new TypeSafelotteryHttpResponseHandler(this, true, new ChargeLakalaParser()) {
			
			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
				if(response != null){
					showActivity((ChargeLakalaBean) response);
				}else{
					LogUtil.CustomLog("TAG", "ChargeLakalaActivity -- Safelottery is null");
					ToastUtil.diaplayMesShort(getApplicationContext(), "请求失败，请重试");
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish(){
				if(progresDialog != null){
					if(progresDialog.isShowing()){
						progresDialog.dismiss();
					}
				}
			}
		});
		StatService.onEvent(ChargeLakalaActivity.this, "total-fund", "所有充值提交请求-拉卡拉", 1);
	}
	
	private String prepareData(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("amount", money);
		return JsonUtils.toJsonStr(map);
	}
	
	private class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alipay_charge_button:
				money = chargeET.getText().toString().trim();
				if (!TextUtils.isEmpty(money) && !money.equals("0")) {
					money = String.valueOf(Integer.parseInt(money));
					if (!AppInstallHelper.checkPackage(getApplicationContext(), "com.lakala.android")) {
						NormalAlertDialog dialog = new NormalAlertDialog(ChargeLakalaActivity.this);
						dialog.setContent("您还没有安装拉卡拉手机客户端，点击确定进入官方网站进行下载");
						dialog.setOk_btn_text("确定");
						dialog.setCancle_btn_text("取消");
						dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
							@Override
							public void onOkBtnClick() {
								// 下载 Lakala APK  地址为垃卡垃提供
								 Intent it = new Intent("android.intent.action.VIEW", Uri.parse(DOWNLOAD_APK));
								 startActivity(it);
							}
								@Override
							public void onCancleBtnClick() {
							}
						});
						dialog.show();
					} else {
						
						doRequestTask();
					}
				} else {
					ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_prompt_bank);
				}
				break;
			case R.id.alipay_safe_charge_zch_tell:
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog=new TelePhoneShowDialog(ChargeLakalaActivity.this);
					dialog.show();
				}
				break;
			}
		}
	}

}