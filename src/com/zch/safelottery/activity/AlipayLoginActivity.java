package com.zch.safelottery.activity;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.AlixId;
import com.zch.safelottery.util.BaseHelper;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.MobileSecurePayHelper;
import com.zch.safelottery.util.MobileSecurePayer;
import com.zch.safelottery.util.PartnerConfig;
import com.zch.safelottery.util.RsaNew;

/*
 * 支付宝快捷登录
 */
public class AlipayLoginActivity extends ZCHBaseActivity {

//	public static final String DEFAULT_PARTNER = "2088101568358171";
	public static final String DEFAULT_KEY = "uxt01uurwxvstkxpmleeok76ezicp8k4";
	
	private Context context;
	
	private ProgressDialog mProgress = null;
	
	private OnAlipayLogin onLogin;

	public AlipayLoginActivity(Context context){
		this.context = context;
	}
	
	// get the selected order info for pay.
	String getOrderInfo() {

		StringBuilder sb = new StringBuilder();
		sb.append("app_name=\"mc\"&");
		sb.append("biz_type=\"trust_login\"&");
		sb.append("partner=\"");
		sb.append(PartnerConfig.PARTNER);
		sb.append("\"&app_id=\"2013101700001769\"&");
		sb.append("notify_url=\"http%3A%2F%2Fnotify.msp.hk%2Fnotify.htm\"");
		return new String(sb);
	}


	// get the sign type we use.
	String getSignType() {
		String getSignType = "sign_type=\"RSA\"";
		return getSignType;
	}

	// sign the order info.
	String sign(String signType, String orderInfo) {
		return RsaNew.sign(orderInfo, PartnerConfig.RSA_PRIVATE_NEW);
	}

	String key() {
		StringBuilder sb = new StringBuilder();
		sb.append("alipay_user_id=\"");
		sb.append(PartnerConfig.SELLER);
		sb.append("\"&userType=\"2\"");
		sb.append("&auth_code=\"");
		sb.append(DEFAULT_KEY);
		sb.append("\"");
		return new String(sb);
	}
	
	// close the progress bar
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					try {

						JSONObject objContent = BaseHelper.string2JSON(strRet, ";");
						String retVal = objContent.getString("resultStatus");
						
						String result = objContent.getString("result");
						result = result.substring(1, result.length()-1);
						if (!retVal.equals("{9000}")) {
							String memo = objContent.getString("memo");
							memo = memo.replace("\"", "");
							memo = memo.substring(1, memo.length() - 1);
							if(!TextUtils.isEmpty(memo)){
								BaseHelper.showDialog((Activity)context, "提示", memo, R.drawable.infoicon);
//							}else{
//								BaseHelper.showDialog((Activity)context, "提示", context.getResources().getString(R.string.check_sign_failed_login), android.R.drawable.ic_dialog_alert);
							}
						} else if (!TextUtils.isEmpty(result)) {
							 //登录成功
							GetString.isAccountNeedRefresh = true;
							JSONObject objResult = BaseHelper.string2JSON(result, "&");
							String alipayUserId = objResult.getString("alipay_user_id");
							String authCode = objResult.getString("auth_code");
							alipayUserId = alipayUserId.replace("\"", "");
							authCode = authCode.replace("\"", "");
								
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("unType", "ZFB");
							map.put("unUserID", alipayUserId);
							map.put("accessToken", authCode);

							onLogin.onLogin(JsonUtils.toJsonStr(map));
//							new Handler().postDelayed(new Runnable() {
//								public void run() {
//									try {
////										getUser();
//										if (Settings.DEBUG)
//											Log.d(Settings.TAG, "Prepaid phone success!!!!!");
//									} catch (Exception e) {
//
//									}
//								}
//							}, 3 * 1000);
						}

					} catch (Exception e) {
						e.printStackTrace();
						if (strRet.contains("操作已经取消")) {
							BaseHelper.showDialog((Activity)context, "提示", "操作已经取消", R.drawable.infoicon);
						}
					}
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void start(){
		
		String orderInfo = getOrderInfo();
		String signType = getSignType();
		String strsign = sign(signType, orderInfo);
		strsign = URLEncoder.encode(strsign);
		String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&" + getSignType();// + "&" + key();

		// start the pay.
		MobileSecurePayer msp = new MobileSecurePayer();

		MobileSecurePayHelper mHelper = new MobileSecurePayHelper(context);
		boolean isInstall = mHelper.detectMobile_sp();
		if (isInstall) {
			closeProgress();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, (Activity)context);
			if (bRet) {
//				mProgress = BaseHelper.showProgress(context, null, "正在加载", false, true);
			}
		}
	}

	public void setOnAlipayLogin(OnAlipayLogin onLogin){
		this.onLogin = onLogin;
	}
	
	public interface OnAlipayLogin{
		public void onLogin(String msg);
	}
}