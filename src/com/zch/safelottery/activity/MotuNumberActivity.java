package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.OnDialogClickListener;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.ctshuzicai.CtOrderListActivity;
import com.zch.safelottery.ctshuzicai.D3Activity;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.dialogs.SucceedDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.http.JsonHttpResponseHandler;
import com.zch.safelottery.http.RequestParams;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TextHttpResponseHandler;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.IssueInfoParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.DoubleClickUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ToastUtil;

public class MotuNumberActivity extends ZCHBaseActivity implements OnClickListener {

	private ImageView motu_img;
	private ImageView motu_middle_layout;
	private FrameLayout motu_submit;
	private LinearLayout motu_number_bg_layout;
	private LinearLayout mutu_result_layout;
	private EditText pursue_edt,multiple_edt;
	private TextView motu_number1,motu_number2,motu_number3,motu_number4,motu_number5,motu_number6,motu_number7;
	
	private String imgPath;
	private String number,uri,pursueNumber,multipleNumber;
	private int pursue,multiple,betNumber,totalAmount;
	private double remainMoney;
	private ProgressDialog mDialog;
	private List<BetIssueBean> issueArray;
	private String issue;
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motu_number_activity);
		try {
			initView();
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView(){
		Intent intent = getIntent();
		imgPath = intent.getStringExtra(MotuActivity.PARAM_PATH_KEY);
		number = intent.getStringExtra(MotuUploadActivity.NumberKey);
		uri = intent.getStringExtra(MotuUploadActivity.Uri);
		
		motu_img = (ImageView) findViewById(R.id.motu_img);
		motu_middle_layout = (ImageView) findViewById(R.id.motu_middle_layout);
		motu_number_bg_layout = (LinearLayout) findViewById(R.id.motu_number_bg_layout);
		mutu_result_layout = (LinearLayout) findViewById(R.id.mutu_result_layout);
		motu_submit = (FrameLayout) findViewById(R.id.motu_submit);
		pursue_edt = (EditText) findViewById(R.id.pursue_edt);
		multiple_edt = (EditText) findViewById(R.id.multiple_edt);
		
		motu_number1 = (TextView) findViewById(R.id.motu_number1);
		motu_number2 = (TextView) findViewById(R.id.motu_number2);
		motu_number3 = (TextView) findViewById(R.id.motu_number3);
		motu_number4 = (TextView) findViewById(R.id.motu_number4);
		motu_number5 = (TextView) findViewById(R.id.motu_number5);
		motu_number6 = (TextView) findViewById(R.id.motu_number6);
		motu_number7 = (TextView) findViewById(R.id.motu_number7);
		
		Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.muto_number_bg);
		ImageUtil.adjustImgSize(rawBitmap, motu_number_bg_layout);
		
		motu_submit.setOnClickListener(this);
	}

	private void initData() throws Exception{
		if(!TextUtils.isEmpty(imgPath)){
			bitmap = ImageUtil.getImage(imgPath);
			int degree = MotuUploadActivity.readPictureDegree(imgPath);
			bitmap = MotuUploadActivity.setPictureDegreeZero(degree, bitmap);
			motu_img.setImageBitmap(bitmap);
		}
		if(!TextUtils.isEmpty(number)){
			String[] numbers = number.split("#");
			String[] redBalls = numbers[0].split(",");
			motu_number1.setText(redBalls[0]);
			motu_number2.setText(redBalls[1]);
			motu_number3.setText(redBalls[2]);
			motu_number4.setText(redBalls[3]);
			motu_number5.setText(redBalls[4]);
			motu_number6.setText(redBalls[5]);
			motu_number7.setText(numbers[1]);
		}
		issue = LotteryId.getIssue(LotteryId.SSQ);
		betNumber = 2;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.motu_submit){
			validDataAndSubmit();
		}
	}
	
	private void validDataAndSubmit(){
		if(GetString.isLogin()){
			if(!DoubleClickUtil.isFastDoubleClick()){
				pursueNumber = pursue_edt.getText().toString();
				multipleNumber = multiple_edt.getText().toString();
				pursue = ConversionUtil.StringToInt(pursueNumber);
				multiple = ConversionUtil.StringToInt(multipleNumber);
				if(multiple > 0){
					if(!TextUtils.isEmpty(pursueNumber)){
						totalAmount = betNumber * multiple * (pursue+1);
						remainMoney = Double.valueOf(GetString.userInfo.getUseAmount());
						if (remainMoney >= totalAmount) {
							if(pursue > 0){
								getPurserIssue();
							}else{
								startTask();
							}
						}else {
							PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(this);
							dialog.setMoney(totalAmount - remainMoney);
							dialog.show();
						}
					}else{
						ToastUtil.diaplayMesShort(this, "追期不能为空");
					}
				}else{
					ToastUtil.diaplayMesShort(this, "倍投不能为0");
				}
			}
		}else{
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
		}
	}
	
	private void toNextPage(){
		Intent intent = new Intent(this, MotuToShareActivity.class);
		intent.putExtra(MotuActivity.PARAM_PATH_KEY, imgPath);
		intent.putExtra(MotuUploadActivity.NumberKey, number);
		startActivity(intent);
	}
	
	private void getPurserIssue(){
		mDialog = ProgressDialog.show(this, "", "获取预售期...", true, false);
		Map<String, String> map = new HashMap<String, String>();
		map.put("lotteryId", LotteryId.SSQ);
		map.put("page", "1");
		map.put("pageSize", "99");
		SafelotteryHttpClient.post(this, "3301", "pre", JsonUtils.toJsonStr(map), new TypeMapHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				if(mMap != null){
					String resutl = (String) mMap.get("issueList");
					List<IssueInfoBean> beanList = (List<IssueInfoBean>)JsonUtils.parserJsonArray( resutl, new IssueInfoParser());
					if(beanList != null){
						BetIssueBean betIssueBean;
						issueArray = new ArrayList<BetIssueBean>();
						for(IssueInfoBean mBean: beanList){
							betIssueBean = new BetIssueBean();
							if(mBean.getStatus().equals("0")){
								betIssueBean.setIssue(mBean.getName());
								issueArray.add(betIssueBean);
							}
						}
						if(issueArray.size() > 0){
							String newIssue = issueArray.get(0).getIssue();
							if(!TextUtils.isEmpty(newIssue) && newIssue.equals(issue)){
								issueArray.remove(0);
							}
						}
					}
					startTask();
				}
			}
			@Override
			public void onFinish() {
				mDialog.dismiss();
			};
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}
			
		});
	}
	
	private void startTask() {
		motu_submit.setEnabled(false);
		BuyLotteryTask mBuyLotteryTask = new BuyLotteryTask(this,"", getBetJson(), motu_submit);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				sendUidTask();
				final SucceedDialog dialog = new SucceedDialog(MotuNumberActivity.this, LotteryId.SSQ, issue, 1, pursue, multiple, 
						totalAmount, (remainMoney - totalAmount),"-","确定");
				// dialog.setCancelable(false);
				dialog.setOnDialogClickListener(new OnDialogClickListener() {
					@Override
					public void onPositiveButtonClick() {
					}

					@Override
					public void onNegativeButtonClick() {
						dialog.dismiss();
						toNextPage();
					}
				});
				dialog.show();
			}
		});
		mBuyLotteryTask.send();
	}
	
	private String getBetJson() {
		BetLotteryBean mLotteryBean = getBetLotteryBean();
		return JsonUtils.toJsonStr(mLotteryBean);
	}
	
	private BetLotteryBean getBetLotteryBean(){
		BetLotteryBean mLotteryBean = new BetLotteryBean();
		if(GetString.userInfo != null){
			mLotteryBean.setUserCode(GetString.userInfo.getUserCode());
		}
		mLotteryBean.setFrom("motu");//来自魔图的购买
		mLotteryBean.setAppVersion(SystemInfo.softVerCode);
		mLotteryBean.setLotteryId(LotteryId.SSQ);
		mLotteryBean.setPlayId("01");
		mLotteryBean.setBuyType(pursue > 0 ? 4 : 1);
		
		mLotteryBean.setBuyAmount(0);
		mLotteryBean.setWinStop(0);
		
		mLotteryBean.setStopAmount(0);
		mLotteryBean.setTotalAmount(totalAmount);
		
		mLotteryBean.setBuyNumberArray( getNumberArray());
		mLotteryBean.setIssueArray( getIssue() );
		return mLotteryBean;
	}
	
	private ArrayList<BetNumberBean> getNumberArray(){
		ArrayList<BetNumberBean> buyNumberArray = new ArrayList<BetNumberBean>();
		BetNumberBean mNumberBean = new BetNumberBean();
		mNumberBean.setBuyNumber(number);
		mNumberBean.setPlayId("01");
		mNumberBean.setPollId("01");
		mNumberBean.setItem(1);
		mNumberBean.setAmount(2);
		buyNumberArray.add(mNumberBean);
		return buyNumberArray;
	}
	
	/** 投注的彩期 **/
	private List<BetIssueBean> getIssue(){
		List<BetIssueBean> betIssueBeans = new ArrayList<BetIssueBean>(pursue+1);
//		写入在售期
		BetIssueBean betIssueBean = new BetIssueBean();
		betIssueBean.setIssue(issue);
		betIssueBean.setMultiple(multiple);
		betIssueBeans.add(betIssueBean);
		//有追期 添加追期期次
		if(this.issueArray != null){ 
			for(int i = 0; i < pursue; i++){
				betIssueBean = new BetIssueBean();
				betIssueBean.setIssue(issueArray.get(i).getIssue());
				betIssueBean.setMultiple(multiple);
				betIssueBeans.add(betIssueBean);
			}
		}
		return betIssueBeans;
	}
	
	private void sendUidTask(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", GetString.userInfo.getUserCode());
		RequestParams params = new RequestParams();
		params.put("data", JsonUtils.toJsonStr(map));
		String url = GetString.UploadImgServer + uri + "/";
		SafelotteryHttpClient.postNormal(url, params, new TextHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
				LogUtil.DefalutLog("sendUidTask onFailure responseString:"+responseString);
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,String responseString) {
				LogUtil.DefalutLog("sendUidTask onSuccess responseString:"+responseString);
			}
			
		});
	}

	@Override
	public void onBackPressed() {
		if(!TextUtils.isEmpty(number)){
			NormalAlertDialog dialog = new NormalAlertDialog(this);
			dialog.setTitle("提示");
			dialog.setContent("您确定退出么？一旦退出，您需要重新选择照片。");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
				@Override
				public void onOkBtnClick() {
					number = "";
					imgPath = "";
					uri = "";
					finish();
				}
				
				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap != null){
			bitmap.recycle();
		}
	}
	
}
