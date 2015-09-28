package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.UserInfoHttpUtil;
import com.zch.safelottery.util.UserInfoHttpUtil.OnHttpListenter;

/**
 * 优惠码兑换，如果 已绑定手机号时，则不显示手机号绑定，仅展示填写优惠码
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年4月14日
 */
public class ChargeYouHuiMaExchangeActivity extends ZCHBaseActivity {

	/** 完成 **/
	private FrameLayout finishBtn;
	/** 优惠码 **/
	private EditText youhuimaEd;
	
	private LinearLayout mLlNotBindMobile;

	private Context mContext;
	private String mParamCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_youhuima_exchange);
		mContext = this;
	
		initData();
		initUI();
		loadBindMobile();
	}

	private void initUI() {
		finishBtn = (FrameLayout) findViewById(R.id.alipay_charge_button);
		youhuimaEd = (EditText) findViewById(R.id.et_youhuima);
		mLlNotBindMobile = (LinearLayout)findViewById(R.id.ll_not_bind_mobile);
		finishBtn.setOnClickListener(onClickListener);
		
		if (!TextUtils.isEmpty(mParamCode)) {
			youhuimaEd.setText(mParamCode);
			youhuimaEd.setEnabled(false);
		}
	}

	private void initData() {
		Intent intent = getIntent();
		mParamCode = intent.getStringExtra("code");
	}

	private String couponCode;
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alipay_charge_button:
				// 执行绑定功能
				couponCode = youhuimaEd.getText().toString().trim();
				if (TextUtils.isEmpty(couponCode)) {
					ToastUtil.diaplayMesShort(ChargeYouHuiMaExchangeActivity.this, "请填写优惠码！");
				} else {
					doRequestTask();
				}
				break;
			}
		}
	};

	private void loadBindMobile() {
		new UserInfoHttpUtil(ChargeYouHuiMaExchangeActivity.this, 1, new OnHttpListenter() {
			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}
		});
	}

	private void doRequestTask() {
		String userCode = GetString.userInfo.getUserCode();
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在处理中，请稍等...", true, true);
		Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", userCode);
		map.put("couponCode", couponCode);

		SafelotteryHttpClient.post(mContext, "3106", "duiHuanYouHuiMaUni", JsonUtils.toJsonStr(map),
				new TypeResultHttpResponseHandler(mContext, true) {

					@Override
					public void onSuccess(int statusCode, Result mResult) {
						String code = mResult.getCode();
						if ("0000".equals(code)) {
							String result = mResult.getResult();
							try {
								JSONObject json = new JSONObject(result);
								String rechargeAmount = json.optString("rechargeAmount", "");
								String bonusAmount = json.optString("bonusAmount", "");
								String presentAmount = json.optString("presentAmount", "");
								String freezeAmount = json.optString("freezeAmount", "");
								String drawAmount = json.optString("drawAmount", "");
								String useAmount = json.optString("useAmount", "");
								GetString.userInfo.setRechargeAmount(rechargeAmount);
								GetString.userInfo.setBonusAmount(bonusAmount);
								GetString.userInfo.setPresentAmount(presentAmount);
								GetString.userInfo.setFreezeAmount(freezeAmount);
								GetString.userInfo.setDrawAmount(drawAmount);
								GetString.userInfo.setUseAmount(useAmount);
								GetString.isAccountNeedRefresh = true;
								initData();
								showChargeScuDialog();
								return;

							} catch (JSONException e) {
								e.printStackTrace();
								LogUtil.ExceptionLog(e != null ? e.getMessage() : "");
							}

						} else {
							LogUtil.ExceptionLog("=======!0000.equals(code)");
							String msg = mResult.getMsg();
							ToastUtil.diaplayMesShort(ChargeYouHuiMaExchangeActivity.this, msg);
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

	private Dialog dialog;

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


}