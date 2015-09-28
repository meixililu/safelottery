package com.zch.safelottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.UserInfoHttpUtil;
import com.zch.safelottery.util.UserInfoHttpUtil.OnHttpListenter;

/*
 * 账户充值
 */
public class AccountChargeActivity extends ZCHBaseActivity {
	private LinearLayout chinaTellLayout;
	private LinearLayout phonecalllayout;
	private LinearLayout unionpaylayout;
	private LinearLayout mLlYouhuimaExchange;
	private View alipaySafe;
	private View kjpay;
	private View chargecard;
	private View chinaTell;
	private View chargeHistory;
	private View zchTell, leshua, banHuiTong, lakala, yinlian,chargeMore;
	private boolean tellPayIsExpand = false;
	private static AccountChargeActivity thisActivity;
	private TextView account_charge_alipay_safe_tx, account_charge_kuaijie_safe_tx, charge_web_tx,account_charge_phonecall_tx;
	private TextView account_charge_unionpay_tx;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_charge);
		thisActivity = this;
		initViews();
//		/**银联卡充值首次引导**/
//		ImageDownload.showIndexPageGuide(this,ImageDownload.PhoneCallGuideKey,R.drawable.guide_phonecall,
//				Gravity.TOP,Gravity.CENTER,0,65);
	}

	// 关闭当前Activity
	public static void callMeFinish() {
		thisActivity.finish();
	}

	private void initViews() {
		chinaTellLayout = (LinearLayout) findViewById(R.id.account_charge_layout_china_tellpay);
		phonecalllayout = (LinearLayout) findViewById(R.id.account_charge_layout_phonecall);
		unionpaylayout = (LinearLayout) findViewById(R.id.account_charge_layout_unionpay);
		//如果渠道号为8001，则显示
		String channel = getString(R.string.disp_youhuima_channel);
		mLlYouhuimaExchange = (LinearLayout) findViewById(R.id.account_charge_layout_youhuima_exchange);
		mLlYouhuimaExchange.setVisibility(channel.equals(SystemInfo.sid) ? View.VISIBLE : View.GONE);
		
		chinaTellLayout.setVisibility(View.GONE);
		leshua = (View) findViewById(R.id.account_charge_layout_leshua);
		lakala = (View) findViewById(R.id.account_charge_layout_lakala);
		yinlian = (View) findViewById(R.id.account_charge_layout_yinlian);
		banHuiTong = (View) findViewById(R.id.account_charge_layout_banhuitong);
		alipaySafe = (View) findViewById(R.id.account_charge_layout_alipay_safe);
		chargecard = (View) findViewById(R.id.account_charge_layout_card);
		chinaTell = (View) findViewById(R.id.account_charge_layout_china_tellpay);
		kjpay = findViewById(R.id.account_charge_layout_kuaijie_safe);
		chargeHistory = (View) findViewById(R.id.account_charge_layout_histroy);
		zchTell = (View) findViewById(R.id.account_charge_layout_zch_tell);
		account_charge_phonecall_tx=(TextView)findViewById(R.id.account_charge_phonecall_tx);
		chargeMore=(View)findViewById(R.id.account_charge_layout_more);
		
		mLlYouhuimaExchange.setOnClickListener(onClickListener);
		leshua.setOnClickListener(onClickListener);
		yinlian.setOnClickListener(onClickListener);
		banHuiTong.setOnClickListener(onClickListener);
		alipaySafe.setOnClickListener(onClickListener);
		kjpay.setOnClickListener(onClickListener);
		chargecard.setOnClickListener(onClickListener);
		chinaTell.setOnClickListener(onClickListener);
		chargeHistory.setOnClickListener(onClickListener);
		zchTell.setOnClickListener(onClickListener);
		lakala.setOnClickListener(onClickListener);
		chargeMore.setOnClickListener(onClickListener);
		phonecalllayout.setOnClickListener(onClickListener);
		unionpaylayout.setOnClickListener(onClickListener);

		account_charge_alipay_safe_tx = (TextView) findViewById(R.id.account_charge_alipay_safe_tx);
		account_charge_kuaijie_safe_tx = (TextView) findViewById(R.id.account_charge_kuaijie_safe_tx);
		account_charge_unionpay_tx = (TextView) findViewById(R.id.account_charge_unionpay_tx);
		charge_web_tx = (TextView) findViewById(R.id.charge_web_tx);
		String str1 = "支付宝快捷支付(荐)";
		String str2 = "信用卡(荐)";
		String str3 = "电脑IE浏览器访问cp.zch168.com充值";
		String str4 = "储蓄卡回呼充值（优惠）";
		String str5 = "银联快捷支付(新)";
		SpannableString msp1 = modifiercolor(str1, str1.length() - 3, str1.length());
		SpannableString msp2 = modifiercolor(str2, str2.length() - 3, str2.length());
		SpannableString msp3 = modifiercolor(str3, 2, 7);
		SpannableString msp4 =modifiercolor(str4, str4.length() - 4, str4.length());
		SpannableString msp5 =modifiercolor(str5, str5.indexOf("("), str5.length());
		account_charge_alipay_safe_tx.setText(msp1);
		account_charge_kuaijie_safe_tx.setText(msp2);
		charge_web_tx.setText(msp3);
		account_charge_phonecall_tx.setText(msp4);
		account_charge_unionpay_tx.setText(msp5);
	}

	public SpannableString modifiercolor(String str1, int start, int end) {
		SpannableString msp = new SpannableString(str1);
		msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	@Override
	protected void onDestroy() {
		thisActivity = null;
		super.onDestroy();
	}

	
	  /**
	   * 描述。
	   * 
	   * @param toClass
	   */
	private void checkBindBankAndUserInfo(final Class<? extends Activity> toClass){
		new UserInfoHttpUtil(AccountChargeActivity.this, 1,new OnHttpListenter() {
			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
				if (GetString.userInfo != null) {
					String real = GetString.userInfo.getRealName();
					String idcard = GetString.userInfo.getCardCode();
					if (TextUtils.isEmpty(real) || TextUtils.isEmpty(idcard)) { // 未绑定身份证
						Intent intent = new Intent(AccountChargeActivity.this,BoundUserInfoActivity.class);
						Bundle mBundle = new Bundle();
						//设置下一个跳转页面
						mBundle.putSerializable(Settings.TOCLASS, toClass);
						mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_NOT_FINISH);
						intent.putExtra(Settings.BUNDLE, mBundle);
						startActivity(intent);
						
					} else {
						Intent intent = new Intent(AccountChargeActivity.this, toClass);
						startActivity(intent);
					}
				}else
					ToastUtil.diaplayMesShort(AccountChargeActivity.this, "获取信息失败，请刷新重试");
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				ToastUtil.diaplayMesShort(AccountChargeActivity.this, "获取信息失败，请刷新重试");
			}
		});
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == R.id.account_charge_layout_youhuima_exchange) {
				//优惠码兑换
				Intent intent = new Intent(AccountChargeActivity.this, ChargeYouHuiMaExchangeActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_alipay_safe) {
//				StatService.onEvent(AccountChargeActivity.this, BaiduStatistics.Charge, "支付宝快捷支付", 1);
				Intent intent = new Intent(AccountChargeActivity.this, ChargeAlipaySafeActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_card) {
//				StatService.onEvent(AccountChargeActivity.this, BaiduStatistics.Charge, "充值卡充值", 1);
				Intent intent = new Intent(AccountChargeActivity.this, ChargeChinaMoblieActivity.class);
				startActivity(intent);

			} else if (v.getId() == R.id.account_charge_layout_histroy) {
//				StatService.onEvent(AccountChargeActivity.this, BaiduStatistics.Charge, "充值记录", 1);
				Intent intent = new Intent(AccountChargeActivity.this, ChargeAccountHistoryActivity.class);
				intent.putExtra("type", "000");
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_zch_tell) {
				if (Settings.isNeedPhone(getApplicationContext())) {
					TelePhoneShowDialog dialog = new TelePhoneShowDialog(AccountChargeActivity.this);
					dialog.show();
				}
			} else if (v.getId() == R.id.account_charge_layout_kuaijie_safe) {
				Intent intent = new Intent(AccountChargeActivity.this,ChargeLdysActivity .class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_more) {
				Intent intent = new Intent(AccountChargeActivity.this,AccountChargeMoreActivity .class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_leshua) {
				// StatService.onEvent(AccountChargeActivity.this,
				// BaiduStatistics.Charge, "乐刷安全快捷充值", 1);
				// Intent intent = new Intent(AccountChargeActivity.this,
				// ChargeLeShuaActivity.class);
				// startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_banhuitong) {
//				StatService.onEvent(AccountChargeActivity.this, BaiduStatistics.Charge, "储蓄卡(快捷支付)", 1);
				Intent intent = new Intent(AccountChargeActivity.this, ChargeBanFuTongActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_lakala) {
//				StatService.onEvent(AccountChargeActivity.this, BaiduStatistics.Charge, "拉卡拉安全快捷充值", 1);
				Intent intent = new Intent(AccountChargeActivity.this, ChargeLakalaActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.account_charge_layout_phonecall) {
				checkBindBankAndUserInfo(ChargePhoneCallActivity.class);
			} else if (v.getId() == R.id.account_charge_layout_unionpay) {
				Intent intent = new Intent(AccountChargeActivity.this, ChargeUnionpayActivity.class);
				startActivity(intent);
			}
		}
	};
}