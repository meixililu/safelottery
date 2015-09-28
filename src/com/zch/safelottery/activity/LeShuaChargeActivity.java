//package com.zch.safelottery.activity;
//
//import android.app.ProgressDialog;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//import com.zch.safelottery.R;
//import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
//import com.zch.safelottery.custom_control.NormalAlertDialog;
//import com.zch.safelottery.custom_control.NormalAlertDialog.OnButtonOnClickListener;
//import com.zch.safelottery.dialogs.TelePhoneShowDialog;
//import com.zch.safelottery.setttings.Settings;
//import com.zch.safelottery.util.GetString;
//
//import com.zch.safelottery.util.HttpUtil;
//import com.zch.safelottery.util.LotteryUtil;
//import com.zch.safelottery.util.Md5;
//import com.zch.safelottery.util.NumberUtil;
//import com.zch.safelottery.util.ToastUtil;
//
//public class LeShuaChargeActivity extends ZCHBaseActivity {
//	
//	private EditText chargeET;
//	private Button chargeBtn,tellCalk,tellCancle;
//	private LinearLayout chargeLayout,tellLayout;
//	
//	private byte[] data,result;
//	private String money;
//	
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        setContentView(R.layout.charge_leshua);
//        initViews();
//    }
//    
//    private void initViews(){
//    	chargeET = (EditText)findViewById(R.id.alipay_charge_fillin);
//    	chargeBtn = (Button)findViewById(R.id.alipay_charge_button);
//    	chargeLayout = (LinearLayout)findViewById(R.id.alipay_safe_charge_zch_tell);
//    	tellLayout = (LinearLayout)findViewById(R.id.aboat_tell_layout);
//    	tellCalk = (Button)findViewById(R.id.aboat_tell_ok);
//    	tellCancle = (Button)findViewById(R.id.aboat_tell_cancel);
//    	
//    	MyOnClickListener myOnClickListener = new MyOnClickListener();
//    	chargeBtn.setOnClickListener(myOnClickListener);
//    	chargeLayout.setOnClickListener(myOnClickListener);
//    	tellCalk.setOnClickListener(myOnClickListener);
//    	tellCancle.setOnClickListener(myOnClickListener);
//    }
//    
//    private void prepareData(){
//		if (Settings.DEBUG) Log.d(Settings.TAG, "-prepareData()");
//		try {
//			LotteryUtil u = new LotteryUtil();
//
//			byte[] b0 = u.int16ToByte(GetString.LESHUA_CHARGE_MSGID);
//			byte[] b1 = u.stringToByte(GetString.SID);
//			byte[] b2 = u.stringToByte(GetString.currentUser.getUserId());
//			byte[] b3 = u.stringToByte(money);
//			byte[] b4 = u.string2ToByte("");
//
//			data = u.addBytes(b0, b1);
//			data = u.addBytes(data, b2);
//			data = u.addBytes(data, b3);
//			data = u.addBytes(data, b4);
//			data = u.addSignToByte(data, GetString.SIGNKEY);
//		} catch (Exception e) {
//			
//		}
//	}
//    
//    private void unparsedData() throws Exception{
//		if (Settings.DEBUG) Log.d(Settings.TAG, "-unparsedData()");
//		if(result != null){
//			LotteryUtil u = new LotteryUtil();
//			int temp = 0;
//			int r_msgId = u.byteToInt16(result, temp); 
//			temp += 2 ;
//			if(r_msgId != 3000){
//				String systemTime = u.byteToString(result, temp);
//				temp += 1 + systemTime.length();
//				
//				String orderId = u.byteToString(result, temp);
//				temp += 1 + orderId.length();
//				
//				String transferMoney = u.byteToString(result, temp);
//				temp += 1 + transferMoney.length();
//				
//				String goodsPrice = u.byteToString(result, temp);
//				temp += 1 + goodsPrice.length();
//				
//				String userAccount = u.byteToString(result, temp);
//				temp += 1 + userAccount.length();
//				
//				int len6 = u.byteToInt8(result, temp);
//				String productName = u.byteToString(result, temp);
//				temp += 1 + len6;
//				
//				int len7 = u.byteToInt8(result, temp);
//				String productInfo = u.byteToString(result, temp);
//				temp += 1 + len7;
//				
//				int len8 = u.byteToInt8(result, temp);
//				String productProvider = u.byteToString(result, temp);
//				temp += 1 + len8;
//				
//				ToastUtil.diaplayMesShort(getApplicationContext(), "操作成功，正在启动乐刷");
//				OpenposServiceHelper helper= new OpenposServiceHelper(LeShuaChargeActivity.this);
//				if (!helper.isOpenposServiceInstalled()){
//					helper.installOpenposService();
//				} else {
//					StringBuilder sb = new StringBuilder();
//					sb.append("openpos_amount=");
//					sb.append(goodsPrice);
//					sb.append("&openpos_call_type=");
//					sb.append("2");
//					sb.append("&openpos_goods_detail=");
//					sb.append(productInfo);
//					sb.append("&openpos_goods_name=");
//					sb.append(productName);
//					sb.append("&openpos_goods_provider=");
//					sb.append(productProvider);
//					sb.append("&openpos_order_id=");
//					sb.append(orderId);
//					sb.append("&openpos_pay_amount=");
//					sb.append(userAccount);
//					sb.append("&openpos_transfer_amount=");
//					sb.append(transferMoney);
//					sb.append("&key=41001236903629671917115150160760");
//					
//					String NeedMD5String = sb.toString();
//					if (Settings.DEBUG) Log.d(Settings.TAG, "-NeedMD5String:"+NeedMD5String);
//					String sign = Md5.d5(NeedMD5String).toUpperCase();
//					if (Settings.DEBUG) Log.d(Settings.TAG, "-sign:"+sign);
//					//固定值
//					Intent intent = new Intent();
//					intent.setComponent(new ComponentName("com.openpos.android.openpos",
//					    			"com.openpos.android.openpos.CallFromApp"));
//					//参数配置
//					intent.putExtra("openpos_call_type", "2");
//					intent.putExtra("openpos_order_id", orderId);
//					intent.putExtra("openpos_transfer_amount", transferMoney);
//					intent.putExtra("openpos_amount", goodsPrice);
//					intent.putExtra("openpos_pay_amount", userAccount);
//					intent.putExtra("openpos_goods_name", productName);
//					intent.putExtra("openpos_goods_detail", productInfo);
//					intent.putExtra("openpos_goods_provider", productProvider);
//					intent.putExtra("openpos_sign", sign);
//					LeShuaChargeActivity.this.startActivityForResult(intent, 0);
//				}
//			}else{
//				if (Settings.DEBUG) Log.d(Settings.TAG, "-error");
//				ToastUtil.diaplayMesShort(getApplicationContext(), "操作失败，请稍后重试");
//			}
//		}else{
//			if (Settings.DEBUG) Log.d(Settings.TAG, "-getdata-faile");//获取数据失败
//			ToastUtil.diaplayMesShort(getApplicationContext(), "连接失败，请稍后重试");
//			HttpUtil.checkNetwork(this);
//		}
//    }
//    
//	class TradeNoTask extends AsyncTask<Void, Void, Void>{
//
//		private ProgressDialog progresdialog;
//		
//		@Override
//		protected void onPreExecute() {
//			progresdialog = ProgressDialog.show(LeShuaChargeActivity.this, "", "正在生成订单号...", true,true);
//			progresdialog.show();
//			prepareData();
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			result = new HttpProxy(GetString.SERVERURL).doHttpRequest(data, "POST");
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void r) {
//			try {
//				if(progresdialog != null){
//					if(progresdialog.isShowing()){
//						progresdialog.dismiss();
//					}
//				}
//				unparsedData();
//			} catch (Exception e) {
//				if (Settings.DEBUG) Log.d(Settings.TAG, "onPostExecute-faile");
//				ToastUtil.diaplayMesShort(getApplicationContext(), "操作失败，请重试");
//			} 
//		}
//    }
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(requestCode == 0 && resultCode == RESULT_OK){
//			NormalAlertDialog dialog = new NormalAlertDialog(LeShuaChargeActivity.this);
//			dialog.setContent("充值完成后，请耐心等待，刷新账户查询是否到账！");
//			dialog.setOk_btn_text("刷新账户");
//			dialog.setCancle_btn_text("继续充值");
//			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
//				
//				@Override
//				public void onOkBtnClick() {
//					GetString.isAccountNeedRefresh = true;
//					LeShuaChargeActivity.this.finish();
//					Intent intent = new Intent();
//	            	intent.setClass(LeShuaChargeActivity.this, MainTabActivity.class);
//	            	intent.putExtra(Settings.TABHOST, Settings.USERHOME);
//	            	LeShuaChargeActivity.this.startActivity(intent);
//				}
//				
//				@Override
//				public void onCancleBtnClick() {
//				}
//			});
//			dialog.show();
//		} 
//	}
//    
//    @Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		if(tellLayout.getVisibility() == View.VISIBLE){
//			tellLayout.setAnimation( Settings.getAnimation(getApplicationContext(),1) );
//			tellLayout.setVisibility(View.GONE);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//    
//    private class MyOnClickListener implements OnClickListener{
//		@Override
//		public void onClick(View v) {
//			switch(v.getId()){
//				case R.id.alipay_charge_button:
//					money = chargeET.getText().toString();
//					if (NumberUtil.numberZeroToTwo(money, NumberUtil.minMoney, NumberUtil.maxMoney)) {
//						OpenposServiceHelper helper= new OpenposServiceHelper(LeShuaChargeActivity.this);
//						if (!helper.isOpenposServiceInstalled()){
//							helper.installOpenposService();
//						} else {
//							new TradeNoTask().execute();
//						}
//					}else{
//						ToastUtil.diaplayMesShort(getApplicationContext(), R.string.toast_prompt_bank);
//					}
//					break;
//				case R.id.alipay_safe_charge_zch_tell:
//					if(Settings.isNeedPhone(getApplicationContext())){
//						TelePhoneShowDialog dialog=new TelePhoneShowDialog(LeShuaChargeActivity.this);
//						dialog.show();
//					}
//					break;
//			}
//		}
//    }
//    
//}