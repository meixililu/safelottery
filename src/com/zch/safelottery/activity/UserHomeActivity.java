package com.zch.safelottery.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.SafelotteryType;
import com.zch.safelottery.bean.UserInfoBean;
import com.zch.safelottery.dialogs.GuideDialog;
import com.zch.safelottery.dialogs.GuideDialog.GuideDialogListener;
import com.zch.safelottery.dialogs.PasswordCheckDialog;
import com.zch.safelottery.dialogs.TelePhoneShowDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeSafelotteryHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.UserInfoParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.ShortCut;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.SharedPreferencesOperate;
import com.zch.safelottery.util.StringUtil;
import com.zch.safelottery.util.ToastUtil;

public class UserHomeActivity extends BaseTabActivity {

	private final int AMENT_NICKNAME = 0x2202;
	public static boolean isHasUpdateNews = false;
	
	private PullToRefreshScrollView scrollview;
	private TextView currentAccount,message_count;
	private TextView remainMoney,toSettingBtn;
	private TextView canUseMoney;
	private TextView freezeMoney;
	private TextView username_edit;
	private TextView unpay_text;

	private View unpay_cover,kefu_cover;
	private View accountCharge;
	private View accountTake;
	private View accountDetail;
	private View message_cover;
	private View buyRcord,reward_record;
	private View myZhuihao;
	
	private View mInfo;
	private View mBound;
	private TextView mobile,info;
	
	private View zchTell;
	
	public static String isFirstRegister;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_home);
		initViews();
		setData(null);
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			if (!GetString.isLogin()){
				Intent intent = new Intent(this, MainTabActivity.class);
				intent.putExtra(Settings.TABHOST, 0);
				startActivity(intent);
			}else if(GetString.isAccountNeedReSet){
				setData(null);
				GetString.isAccountNeedReSet = false;
			}
			if (GetString.isAccountNeedRefresh) {
				UserTask();
				GetString.isAccountNeedRefresh = false;
			}
			String tempStr = (String) SafeApplication.dataMap.get("isFirstRegister");//是否是第一次注册，非空为第一次注册，空即不是第一次注册
			if(!TextUtils.isEmpty(tempStr)){
				isFirstRegister = tempStr;
				SafeApplication.dataMap.clear();
			}
			isShowUnBoundle();
			if(isHasUpdateNews){
				message_count.setVisibility(View.VISIBLE);
				isHasUpdateNews = false;
			}else{
				message_count.setVisibility(View.GONE);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setData(UserInfoBean mBean) {
		
		if (GetString.userInfo != null) {
			UserInfoBean user = GetString.userInfo;
			if(mBean != null){
				user.setRechargeAmount(mBean.getRechargeAmount());
				user.setBonusAmount(mBean.getBonusAmount());
				user.setPresentAmount(mBean.getPresentAmount());
				user.setFreezeAmount(mBean.getFreezeAmount());
				user.setDrawAmount(mBean.getDrawAmount());
				user.setUseAmount(mBean.getUseAmount());

				SharedPreferencesOperate.updateUserInfo(UserHomeActivity.this, SharedPreferencesOperate.SAVE_USER_INFO, JsonUtils.toJsonStr(GetString.userInfo));
			}

			double award = ConversionUtil.StringToDouble(user.getPresentAmount());
			if(award <= 0){
				remainMoney.setText("￥" + user.getUseAmount());
			}else{
				remainMoney.setText("￥" + user.getUseAmount() +" (含赠金￥" + user.getPresentAmount() +")");
			}
			canUseMoney.setText("￥" + (user.getDrawAmount() == null? "0.00": user.getDrawAmount()));
			freezeMoney.setText("￥" + user.getFreezeAmount());
			currentAccount.setText(StringUtil.subString(user.getNickname(), 0, 8));
			if (GetString.userInfo.getNickname().equals(GetString.userInfo.getUserName())) {
				username_edit.setVisibility(View.VISIBLE);
			}else{
				username_edit.setVisibility(View.GONE);
			}
		}
	}

	private void initViews() {
		scrollview = (PullToRefreshScrollView) findViewById(R.id.refreshable_scrollview); 
		currentAccount = (TextView) findViewById(R.id.user_home_current_account);
		remainMoney = (TextView) findViewById(R.id.user_home_remain_money);
		canUseMoney = (TextView) findViewById(R.id.user_home_can_use_money);
		freezeMoney = (TextView) findViewById(R.id.user_home_freeze_money);
		unpay_text = (TextView) findViewById(R.id.user_home_unpay_text);
		toSettingBtn = (TextView) findViewById(R.id.user_home_to_settings);

		unpay_cover = (View) findViewById(R.id.user_home_unpay_cover);
		accountCharge = (View) findViewById(R.id.user_home_charge);
		accountTake = (View) findViewById(R.id.user_home_take);
		message_cover = (View) findViewById(R.id.user_home_message_cover);
		message_count = (TextView) findViewById(R.id.user_home_message_count);
		buyRcord = (View) findViewById(R.id.user_home_query_buy_record);
		reward_record = (View) findViewById(R.id.user_home_query_reward_record);
		kefu_cover = (View) findViewById(R.id.user_home_kefu_cover);
		
		accountDetail = (View) findViewById(R.id.user_home_account_query_detail);
		myZhuihao = (View) findViewById(R.id.user_home_zhuihao);
		mInfo = (View) findViewById(R.id.user_home_info);
		mBound = (View) findViewById(R.id.user_home_bound);
		
		username_edit = (TextView) findViewById(R.id.username_edit);
		username_edit.setOnClickListener(onClickListener);
		
		zchTell = (View) findViewById(R.id.user_home_zch);
		mobile = (TextView) findViewById(R.id.bound_mobile_is_no);
		info = (TextView) findViewById(R.id.bound_info_is_no);

		toSettingBtn.setOnClickListener(onClickListener);
		accountCharge.setOnClickListener(onClickListener);
		accountTake.setOnClickListener(onClickListener);
		accountDetail.setOnClickListener(onClickListener);
		buyRcord.setOnClickListener(onClickListener);
		myZhuihao.setOnClickListener(onClickListener);
		mInfo.setOnClickListener(onClickListener);
		mBound.setOnClickListener(onClickListener);
		zchTell.setOnClickListener(onClickListener);
		kefu_cover.setOnClickListener(onClickListener);
		message_cover.setOnClickListener(onClickListener);
		reward_record.setOnClickListener(onClickListener);

		scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				UserTask();
			}
		});
	}
	
	private void isShowUnBoundle(){
		if(TextUtils.isEmpty(GetString.userInfo.getMobile())){
			mobile.setVisibility(View.VISIBLE);
		}else{
			mobile.setVisibility(View.GONE);
		}
		if(TextUtils.isEmpty(GetString.userInfo.getRealName()) || TextUtils.isEmpty(GetString.userInfo.getCardCode())){
			info.setVisibility(View.VISIBLE);
		}else{
			info.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 绑定手机
	 */
	private void  userPhone(){
		if(GetString.userInfo != null){
			Intent intent = new Intent(UserHomeActivity.this,BoundUserPhoneActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt(BoundUserPhoneActivity.BOUND_FROM, 3);
			if(TextUtils.isEmpty(GetString.userInfo.getMobile())){
				mBundle.putInt(BoundUserPhoneActivity.BOUND_STATE, BoundUserPhoneActivity.BOUND_NOT_FINISH);
			}else{
				mBundle.putInt(BoundUserPhoneActivity.BOUND_STATE, BoundUserPhoneActivity.BOUND_FINISH_SUCCEED);
			}
				
			intent.putExtra(Settings.BUNDLE, mBundle);
			startActivity(intent);
		}
	}
	
	/**
	 * 用户信息
	 */
	private void userInfo(){
		if(GetString.userInfo != null){
			Intent intent = new Intent(UserHomeActivity.this,BoundUserInfoActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt(BoundUserInfoActivity.BOUND_FROM, 3);
			if(TextUtils.isEmpty(GetString.userInfo.getRealName()) || TextUtils.isEmpty(GetString.userInfo.getCardCode())){
				mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_NOT_FINISH);
			}else{
				mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_FINISH);
			}
			intent.putExtra(Settings.BUNDLE, mBundle);
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == AMENT_NICKNAME){
			if(GetString.userInfo != null){
				if (GetString.userInfo.getNickname().equals(GetString.userInfo.getUserName())) {
					username_edit.setVisibility(View.VISIBLE);
				}else{
					username_edit.setVisibility(View.GONE);
				}
				currentAccount.setText(StringUtil.subString(GetString.userInfo.getNickname(), 0, 8));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == R.id.user_home_charge) {
				Intent intent = new Intent(UserHomeActivity.this, AccountChargeActivity.class);
				startActivity(intent);
				StatService.onEvent(UserHomeActivity.this, "account-fund", "个人中心-账户充值", 1);
			} else if (v.getId() == R.id.user_home_take) {
				Intent intent = new Intent(UserHomeActivity.this, AccountTakeActivity.class);
				startActivity(intent);
				StatService.onEvent(UserHomeActivity.this, "account-withdraw", "个人中心-账户提款", 1);
			} else if (v.getId() == R.id.user_home_account_query_detail) {
				Intent intent = new Intent(UserHomeActivity.this, AccountActivity.class);
				startActivity(intent);
				StatService.onEvent(UserHomeActivity.this, "account-details", "个人中心-账户明细", 1);
			} else if (v.getId() == R.id.user_home_query_buy_record) {
				Bundle mBundle = new Bundle();
				mBundle.putString(Settings.INTENT_STRING_LOTTERY_ID, LotteryId.All);
				Intent intent = new Intent(UserHomeActivity.this, RecordBetActivity.class);
				intent.putExtra(Settings.BUNDLE,mBundle);
				startActivity(intent);
				StatService.onEvent(UserHomeActivity.this, "account-record", "个人中心-投注记录", 1);
			} else if (v.getId() == R.id.user_home_zhuihao) {
				Intent intent = new Intent(UserHomeActivity.this, RecordPursueActivity.class);
				startActivity(intent);
				StatService.onEvent(UserHomeActivity.this, "account-zhuihao", "个人中心-我的追号", 1);
			} else if (v.getId() == R.id.user_home_to_settings) {
				toMoreActivity();
				StatService.onEvent(UserHomeActivity.this, "account-to-settings", "个人中心-设置按钮", 1);
			} else if (v.getId() == R.id.user_home_info) {
				if(GetString.isLogin){
					if(!TextUtils.isEmpty(isFirstRegister) && isFirstRegister.equals("notNeedPwd")){
						userInfo();
					}else if(Settings.isSettingPassword(UserHomeActivity.this)){
						Intent intent =new Intent(UserHomeActivity.this,ModifyJointPassWordActivity.class);
						intent.putExtra(Settings.TOCLASS,BoundUserInfoActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putInt(BoundUserInfoActivity.BOUND_FROM, 3);
						if(TextUtils.isEmpty(GetString.userInfo.getRealName()) || TextUtils.isEmpty(GetString.userInfo.getCardCode())){
							mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_NOT_FINISH);
						}else{
							mBundle.putInt(BoundUserInfoActivity.BOUND_STATE, BoundUserInfoActivity.BOUND_FINISH);
						}
						intent.putExtra(Settings.BUNDLE, mBundle);
						startActivity(intent);
					}else{
						final PasswordCheckDialog mDialog = new PasswordCheckDialog(UserHomeActivity.this);
						mDialog.setOnClickListener(new PasswordCheckDialog.OnClickListener() {
							
							@Override
							public void onClick() {
								mDialog.dismiss();
								userInfo();
							}
						});
						mDialog.show();
					}
				}else{
					Intent intent = new Intent(UserHomeActivity.this,LoginActivity.class);
					intent.putExtra("from", 9);
					intent.putExtra(Settings.TOCLASS, BoundUserInfoActivity.class);
					startActivity(intent);
				}
				StatService.onEvent(UserHomeActivity.this, "account-profile", "个人中心-个人信息", 1);
			} else if (v.getId() == R.id.user_home_bound) {
				if(GetString.isLogin){
					if(!TextUtils.isEmpty(isFirstRegister) && isFirstRegister.equals("notNeedPwd")){
						userPhone();
					}else if(Settings.isSettingPassword(UserHomeActivity.this)){
						Intent intent =new Intent(UserHomeActivity.this,ModifyJointPassWordActivity.class);
						intent.putExtra(Settings.TOCLASS,BoundUserPhoneActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putInt(BoundUserPhoneActivity.BOUND_FROM, 3);
						if(TextUtils.isEmpty(GetString.userInfo.getMobile())){
							mBundle.putInt(BoundUserPhoneActivity.BOUND_STATE, BoundUserPhoneActivity.BOUND_NOT_FINISH);
						}else{
							mBundle.putInt(BoundUserPhoneActivity.BOUND_STATE, BoundUserPhoneActivity.BOUND_FINISH_SUCCEED);
						}
						intent.putExtra(Settings.BUNDLE, mBundle);
						startActivity(intent);
					}else{
						final PasswordCheckDialog mDialog = new PasswordCheckDialog(UserHomeActivity.this);
						mDialog.setOnClickListener(new PasswordCheckDialog.OnClickListener() {
							
							@Override
							public void onClick() {
								mDialog.dismiss();
								userPhone();
							}
						});
						mDialog.show();
					}
				}else{
					Intent intent = new Intent(UserHomeActivity.this,LoginActivity.class);
					intent.putExtra("from", 9);
					intent.putExtra(Settings.TOCLASS, BoundUserPhoneActivity.class);
					startActivity(intent);
				}
				StatService.onEvent(UserHomeActivity.this, "account-bondphone", "个人中心-手机绑定", 1);
			} else if (v.getId() == R.id.username_edit) {
				if (username_edit.isShown() && GetString.userInfo.getNickname().equals(GetString.userInfo.getUserName())) {
					Intent intent = new Intent(UserHomeActivity.this, AmendNicknameActivity.class);
					startActivityForResult(intent, AMENT_NICKNAME);
				} 
//				StatService.onEvent(UserHomeActivity.this, "", "个人中心-修改昵称", 1);
			} else if (v.getId() == R.id.user_home_zch) {
				showKefuDialog();
			} else if (v.getId() == R.id.user_home_kefu_cover) {
				toKefuCenter();
			} else if (v.getId() == R.id.user_home_message_cover) {
				toUserMessageCenter();
			} else if (v.getId() == R.id.user_home_query_reward_record) {
				
			} 
		}
	};
	//
	private void toMoreActivity(){
		Intent intent = new Intent(UserHomeActivity.this, MoreActivity.class);
		startActivity(intent);
	}
	//客服电话
	private void showKefuDialog(){
		if (Settings.isNeedPhone(UserHomeActivity.this)) {
			TelePhoneShowDialog dialog = new TelePhoneShowDialog(UserHomeActivity.this);
			dialog.show();
		}
		StatService.onEvent(UserHomeActivity.this, "account-serviceNo.", "个人中心-客服电话", 1);
	}
	//在线客服
	private void toKefuCenter(){
		if (GetString.isLogin) {
			Intent intent = new Intent(UserHomeActivity.this, QuestionActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
			intent.putExtra("from", 9);
			intent.putExtra(Settings.TOCLASS, QuestionActivity.class);
			startActivity(intent);
		}
		StatService.onEvent(UserHomeActivity.this, "account-online-service", "个人中心-在线客服", 1);
	}
	//消息中心
	private void toUserMessageCenter(){
		try {
			message_count.setVisibility(View.GONE);
			Intent intent = new Intent(UserHomeActivity.this, MessageCenterActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StatService.onEvent(UserHomeActivity.this, "account-message", "个人中心-消息中心", 1);
	}

	public void UserTask()  {
		final ProgressDialog progresdialog = ProgressDialog.show(this, "", "正在更新用户信息", true,true);
		//获取新消息
		ShortCut.getPrivateCountMessage(this);
		SafelotteryHttpClient.post(this, "3200", "", "{\"userCode\":\""+GetString.userInfo.getUserCode() + "\"}", new TypeSafelotteryHttpResponseHandler(this, true, new UserInfoParser()) {
			
			@Override
			public void onSuccess(int statusCode, SafelotteryType response) {
				// TODO Auto-generated method stub
				try{
					ToastUtil.diaplayMesShort(UserHomeActivity.this, "更新成功");
					setData((UserInfoBean)response);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
			
			@Override
			public void onFinish(){
				scrollview.onRefreshComplete();
				progresdialog.dismiss();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isFirstRegister = null;
		isHasUpdateNews = false;
	}
}