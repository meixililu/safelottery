package com.zch.safelottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PasswordCheckDialog;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.UserInfoHttpUtil;
import com.zch.safelottery.util.UserInfoHttpUtil.OnHttpListenter;

/*
 * 账户提款
 */
public class AccountTakeActivity extends ZCHBaseActivity {

	private View takeBank;
	private View takeRord;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_take);

		initViews();
	}

	private void initViews() {

		takeBank = (View) findViewById(R.id.account_take_bank_layout);
		takeRord = (View) findViewById(R.id.account_take_record_layout);

		takeBank.setOnClickListener(onClickListener);
		takeRord.setOnClickListener(onClickListener);

	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (v.getId() == R.id.account_take_bank_layout) {
				new UserInfoHttpUtil(AccountTakeActivity.this, 1,new OnHttpListenter() {
					@Override
					public void onSuccess(int statusCode, SafelotteryType response) {
						skipActivity();
					}
					
					@Override
					public void onFailure(int statusCode, String mErrorMsg) {
						ToastUtil.diaplayMesShort(AccountTakeActivity.this, "获取信息失败，请刷新重试");
					}
				});

			} else if (v.getId() == R.id.account_take_record_layout) {
				Intent intent = new Intent(AccountTakeActivity.this, TakeAccountHistoryActivity.class);
				intent.putExtra("type", "101");
				startActivity(intent);
			}
		}
	};

	
	
	  /**
	   * 在密码验证窗口中，点击确定按钮的操作。
	   * 
	   */
	private void onPasswordCheckDialogOK() {
		boolean isNeedToClass = false;
		if (GetString.userInfo != null) {
			String real = GetString.userInfo.getRealName();
			String idcard = GetString.userInfo.getCardCode();
			if (TextUtils.isEmpty(real) || TextUtils.isEmpty(idcard)) { // 未绑定身份证
				Intent intent = new Intent(AccountTakeActivity.this, BoundUserInfoActivity.class);
				bindUserBundle(isNeedToClass, intent);
				startActivity(intent);
			} else {
				Intent intent = new Intent(AccountTakeActivity.this, TakeBankActivity.class);
				bindTakeBundle(isNeedToClass, intent);
				startActivity(intent);
			}
		}
	}
	
	  /**
	   * 为取款的Activity绑定参数
	   * 
	   * @author 陈振国
	   * @date 2014/03/20
	   * @param isNeedToClass　是否需要toClass参数
	   * @param intent　包含目标activity的意图
	   */
	private void bindTakeBundle(boolean isNeedToClass, Intent intent) {
		Bundle mBundle = new Bundle();
		if (isNeedToClass) {
			//设置下一个跳转页面
			intent.putExtra(Settings.TOCLASS, TakeBankActivity.class);
		}
		if (!TextUtils.isEmpty(GetString.userInfo.getBankCode()) && !TextUtils.isEmpty(GetString.userInfo.getBankName())) {
			mBundle.putInt("state", TakeBankActivity.TAKE_AMOUNT);
		} else {
			mBundle.putInt("state", TakeBankActivity.TAKE_BOUND);
		}
		intent.putExtra(Settings.BUNDLE, mBundle);
	}

	
	  /**
	   * 为绑定用户信息的Activity设置参数
	   * 
	   * @author 陈振国
	   * @date 2014/03/20
	   * @param isNeedToClass 是否需要toClass参数
	   * @param intent　包含目标activity的意图
	   */
	private void bindUserBundle(boolean isNeedToClass, Intent intent) {
		Bundle mBundle = new Bundle();
		if (isNeedToClass) {
			//设置下一个跳转页面
			intent.putExtra(Settings.TOCLASS, BoundUserInfoActivity.class);
		}
		mBundle.putInt(BoundUserInfoActivity.BOUND_FROM, 9);
		mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_NOT_FINISH);
		intent.putExtra(Settings.BUNDLE, mBundle);
	}

	
	private void skipActivity() {
		if (Settings.isSettingPassword(AccountTakeActivity.this)) {// 是否设置过密码
			Intent intent = new Intent(AccountTakeActivity.this, ModifyJointPassWordActivity.class);
			if (GetString.userInfo != null) {
				String real = GetString.userInfo.getRealName();
				String idcard = GetString.userInfo.getCardCode();
				if (TextUtils.isEmpty(real) || TextUtils.isEmpty(idcard)) { // 未绑定身份证
					bindUserBundle(true, intent);
				} else {
					bindTakeBundle(true, intent);
				}
			}
			startActivity(intent);
		} else {
			final PasswordCheckDialog mDialog = new PasswordCheckDialog(this);
			mDialog.setOnClickListener(new PasswordCheckDialog.OnClickListener() {
				@Override
				public void onClick() {
					mDialog.dismiss();
					hideIME();
					onPasswordCheckDialogOK();
				}
			});
			mDialog.show();
		}
	}

	/**
	 */
	private void hideIME() {
		final InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(takeBank.getWindowToken(), 0);
	}
}