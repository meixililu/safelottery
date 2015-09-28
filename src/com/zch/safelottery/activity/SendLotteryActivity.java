package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.BetIssueBean;
import com.zch.safelottery.bean.BetLotteryBean;
import com.zch.safelottery.bean.BetNumberBean;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.LotteryInfoBean;
import com.zch.safelottery.bean.SelectInfoBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.sendlottery.ContactInfo;
import com.zch.safelottery.sendlottery.ContactListViewActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.RandomSelectUtil;
import com.zch.safelottery.util.SelectInfoUtil;
import com.zch.safelottery.util.ToastUtil;

public class SendLotteryActivity extends ZCHBaseActivity {

	private TextView lottery_type_sp, lottery_num_sp;
	private PopWindowDialog mDialogType, mDialogNumSp;
	
	private TextView lottery_num_tv;
	private Button select_friend_btn;
	private EditText friend_et, username_et, my_words_et;
	private Button submit_layout;
	private Button submit_free;
//	private boolean isFinishCreate = false;
	private TextView remain_text;
	private LinearLayout contact_list_layout;
	private ArrayList<ContactInfo> result_list;
	private List<BetNumberBean> buyNumberArray;
	private List<BetIssueBean> issueArray;
	
	private String wNumberStr;
	private static final int CONTACT_REQUEST_CODE = 2;
	private static final int JZ_REQUEST_CODE = 1;
	private String name;
	
	/** 发送数据 **/
	private String msg;
	/** 得到结果 **/
	private Result result;
	
	private String item;
	private long amount;
	private String playId;
	private String smsText;
	private String realname;
	
	private int totalAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Settings.DEBUG)
			Log.d(Settings.TAG, "-onCreate()");
		setContentView(R.layout.send_lottery);
		
		initUI();
	}

	private void initUI() {
		if (Settings.DEBUG) Log.d(Settings.TAG, "-initUI()");
		select_friend_btn = (Button) findViewById(R.id.send_select_friend_btn);
		friend_et = (EditText) findViewById(R.id.send_friend_et);
		lottery_type_sp = (TextView) findViewById(R.id.send_lottery_type);
		lottery_num_sp = (TextView) findViewById(R.id.send_lottery_number);
		lottery_num_tv = (TextView) findViewById(R.id.send_lottery_number_tv);
		my_words_et = (EditText) findViewById(R.id.send_my_words);
		username_et = (EditText) findViewById(R.id.send_friend_username);
		submit_layout = (Button) findViewById(R.id.send_submit);
		submit_free = (Button) findViewById(R.id.send_submit_free);
		remain_text = (TextView) findViewById(R.id.remain_text);
		contact_list_layout = (LinearLayout)findViewById(R.id.contact_list_layout);
		
		result_list = new ArrayList<ContactInfo>();
		
		name = GetString.userInfo.getRealName();
		if(!TextUtils.isEmpty(name)){
			if(name.length() > 0){
				name = GetString.userInfo.getRealName().substring(0,1);
			}
			username_et.setText( name+"**" );
		}
		
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		submit_layout.setOnClickListener(myOnClickListener);
		submit_free.setOnClickListener(myOnClickListener);
		lottery_num_sp.setOnClickListener(myOnClickListener);
		lottery_type_sp.setOnClickListener(myOnClickListener);

		my_words_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				remain_text.setText((str.length() + "/140"));
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		friend_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				if(str.length() == 11){
					String tempNum = friend_et.getText().toString();
					if (ContactListViewActivity.IsUserNumber(tempNum)) {
						ContactInfo info = new ContactInfo();
						info.setContactName("好友");
						info.setUserNumber(tempNum);
						result_list.add(info);
						contact_list_layout.addView(addView(info));
						contact_list_layout.setVisibility(View.VISIBLE);
						friend_et.setText("");
						ToastUtil.diaplayMesShort(getApplicationContext(), "添加成功");
					}else{
						ToastUtil.diaplayMesShort(getApplicationContext(), "亲，您输入的手机号格式不对，请重试");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		select_friend_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(SendLotteryActivity.this, ContactListViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("wNumberStr", "");
				intent.putExtras(bundle);
				startActivityForResult(intent, CONTACT_REQUEST_CODE);
				
			}
		});
	}
	
	private class MyOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.send_submit){
				if(lotteryId.equals("-")){
					ToastUtil.diaplayMesShort(getApplicationContext(), "请选择彩种！");
				}else{
					allPhone();
					boolean b = submit();
					if(b){
						startConnect();
					}
				}
			}else if(v.getId() == R.id.send_submit_free){
				//免费增送
				
			}else if(v.getId() == R.id.send_lottery_type){
				showDialogType();
			}else if(v.getId() == R.id.send_lottery_number){
				showDialogNumSp();
			}
		}
		
	}
	
	private boolean submit(){
		
		realname = username_et.getText().toString();
		if(realname.contains("**")){
			realname = GetString.userInfo.getRealName();
		}
		if(inputPhone.equals("") && result_list.size() == 0){
			ToastUtil.diaplayMesShort(getApplicationContext(), "请输入收件人手机号");
		}else if(realname.equals("")){
			ToastUtil.diaplayMesShort(getApplicationContext(), "请输入您的名字，以便您的朋友放心使用");
		}else {
			item = (String) lottery_num_sp.getText();
			smsText = my_words_et.getText().toString();
			
			if (item.contains("注")) {
				item = item.replace("注", "");
			}
			if(!inputPhone.equals("")){
				if(!ContactListViewActivity.IsUserNumber(inputPhone)){
					ToastUtil.diaplayMesShort(getApplicationContext(), "亲，您输入的手机号格式不对，请重试");
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private String allPhoneNum;
	private String inputPhone;
	private int phoneLength;
	private void allPhone(){
		inputPhone = friend_et.getText().toString();
		
		StringBuilder sb = new StringBuilder();
		if(result_list.size() > 0){
			for(ContactInfo info : result_list){
				sb.append(info.getUserNumber());
				sb.append("#");
			}
		}
		allPhoneNum = sb.toString();
		
		if(!inputPhone.equals("")){
			allPhoneNum += inputPhone;
		}else{
			if(allPhoneNum.length() > 2){
				allPhoneNum = allPhoneNum.substring(0, allPhoneNum.length()-1);
			}
		}
		phoneLength = allPhoneNum.split("#").length;
		
	}
	private boolean isEnoughMoney(int money, int phones){
		totalAmount = money * phones;
		double remainMoney = ConversionUtil.StringToDouble(GetString.userInfo.getUseAmount());
//		if (Settings.DEBUG) Log.d(Settings.TAG, "RemainMoney-sum:"+RemainMoney+"-"+sumMeney);
		if(totalAmount > remainMoney){
			PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(SendLotteryActivity.this);
			dialog.setMoney(GetString.df_0.format(totalAmount));
			dialog.show();
			return false;
		}
		totalAmount = money;
		return true;
	}

	//重写获取页面回传值
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case CONTACT_REQUEST_CODE: //从联系人返回
			if (resultCode == RESULT_OK) {
				contact_list_layout.removeAllViews();
				for(int i=0; i<ContactListViewActivity.contactList.size(); i++){
					if(ContactListViewActivity.contactList.get(i).getIsChecked() == true){
						result_list.add(ContactListViewActivity.contactList.get(i));
					}
				}
				for(int i = 0; i < result_list.size(); i++){
					contact_list_layout.addView( addView(result_list.get(i)) );
				}
				contact_list_layout.setVisibility(View.VISIBLE);
				ContactListViewActivity.contactList.clear();
			}
			break;
			
    	case JZ_REQUEST_CODE: //从竞足返回
    		
    		sendStatus = STATUS_JC; //设置为True 
    		BetLotteryBean mBean = (BetLotteryBean) SafeApplication.dataMap.get(SEND_MAP);
    		SafeApplication.dataMap.clear();
    		
    		if(mBean != null){
    			// 操作成功 完成
    			lottery_num_tv.setVisibility(View.VISIBLE);
    			lottery_num_sp.setVisibility(View.GONE);
    			
    			buyNumberArray = mBean.getBuyNumberArray();
        		issueArray = mBean.getIssueArray();
        		playId = mBean.getPlayId();
        		amount = mBean.getTotalAmount();
        		
        		int i = 0;
        		for(BetNumberBean b: buyNumberArray){
        			i += b.getItem();
        		}
        		item = String.valueOf(i);
        		
    			lottery_num_tv.setText(item+"注");
    			
    		}else{
    			buyNumberArray = null;
        		issueArray = null;
    			lottery_num_sp.setVisibility(View.VISIBLE);
    			lottery_num_tv.setVisibility(View.GONE);
    		}
    		
    		break;
		}
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Settings.DEBUG) Log.d(Settings.TAG, "-onDestroy()");
	}

	
	private View addView(final ContactInfo info){
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		final View convertView = inflater.inflate(R.layout.send_lottery_list, null);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView phone = (TextView) convertView.findViewById(R.id.phone);
		Button delete = (Button) convertView.findViewById(R.id.delete);
		
		name.setText(info.getContactName());
		phone.setText(info.getUserNumber());
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contact_list_layout.removeView(convertView);
				result_list.remove(info);
				ContactListViewActivity.contactList.remove(info);
			}
		});
		return convertView;
	}
	
	private class RequsetDataTask extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog progresdialog;
		
		@Override
		protected void onPreExecute() {
			progresdialog = ProgressDialog.show(SendLotteryActivity.this, "", "正在发送...", true,true);
			progresdialog.show();
			result = null;
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
//				result = new SafelotteryHttp().post(getApplicationContext(), "3300", params[0], msg, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void r) {
			progresdialog.dismiss();
			try {
				if(result.getCode().equals("0000")){
					ToastUtil.diaplayMesShort(getApplicationContext(), "提交成功，请查看投注记录");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 发送信息
	 * @param playId 
	 * @param smsText
	 * @param func 数字彩present  竞彩presentSports
	 */
	private void prepareData1(String func) {
		if(GetString.userInfo != null){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userCode", GetString.userInfo.getUserCode());
			map.put("lotteryId", lotteryId);
			map.put("playId", playId);
			map.put("buyType", "7");
			map.put("buyNumberArray", buyNumberArray);
			map.put("issueArray", issueArray);
			map.put("mobile", allPhoneNum);
			map.put("smsText", smsText);
			map.put("presentedRealName", GetString.userInfo.getRealName());
			map.put("totalAmount", String.valueOf(totalAmount));
			msg = JsonUtils.toJsonStr(map);
			new RequsetDataTask().execute(new String[]{func});
		}
	}

	/****
	 * 竞彩活动的连接
	 */
	private void startConnect(){
		if(sendStatus == STATUS_JC){ //竞彩
			if(buyNumberArray != null){
				if(isEnoughMoney((int)amount,phoneLength)){
						prepareData1("presentSports");
				}
			}else{
				ToastUtil.diaplayMesShort(getApplicationContext(), "未选择方案内容请重选");
			}
		}else{ //数字彩
			if(TextUtils.isEmpty(item)){
				ToastUtil.diaplayMesShort(getApplicationContext(), "请您选择注数");
			}else{
				int betNum = ConversionUtil.StringToInt(item);
				if(isEnoughMoney(betNum * 2,phoneLength)){
					if(getNumberArray(betNum)){
						playId = "01";
						prepareData1("present");
					}else {
						ToastUtil.diaplayMesShort(getApplicationContext(), "初始化数据失败，请重试");
					}
				}
			}
		}
	}
	
	public static String getLotteryByPosition(int pos){
		if(pos == 0){
			return "-";
		}else if(pos == 1){
			return LotteryId.JCZQ;
		}else if(pos == 2){
			return LotteryId.SSQ;
		}else if(pos == 3){
			return LotteryId.DLT;
		}else if(pos == 4){
			return LotteryId.FC;
		}else if(pos == 5){
			return LotteryId.PL3;
		}else if(pos == 6){
			return LotteryId.PL5;
		}else if(pos == 7){
			return LotteryId.QLC;
		}else if(pos == 8){
			return LotteryId.QXC;
		}else{
			return "-";
		}
	}
	

	private void showDialogType(){
    	if(mDialogType == null){
    		final String[] item = getResources().getStringArray(R.array.send_lottery_type);
    		mDialogType = new PopWindowDialog(this, item, 0);
    		
    		mDialogType.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogType.getList();
					CheckBox checkBox;
					
					for(int i = 0, size = list.size(); i < size; i++){
						checkBox = list.get(i);
						if(i == v.getId()){
							lotteryId = getLotteryByPosition( v.getId());
							checkBox.setChecked(true);
							if(lotteryId.equals(LotteryId.JCZQ)){
								sendStatus = STATUS_JC;
								Intent intent = new Intent(SendLotteryActivity.this, JZActivity.class);
								Bundle mBundle = new Bundle();
								mBundle.putInt(SEND_STATUS_KEY, sendStatus);
								intent.putExtra(Settings.BUNDLE, mBundle);
								SendLotteryActivity.this.startActivityForResult(intent, JZ_REQUEST_CODE);
							}else{
								sendStatus = STATUS_SHU;
								lottery_num_sp.setVisibility(View.VISIBLE);
					    		lottery_num_tv.setVisibility(View.GONE);
					    		
					    		infoBean = SelectInfoUtil.getInfoBean(lotteryId, "01", "01");
							}
							lottery_type_sp.setText(item[i]);
						}else{
							if(checkBox.isChecked()) checkBox.setChecked(false);
						}
					}
					mDialogType.dismiss();
				}
			});
    		
    		ScrollView scroll = (ScrollView) findViewById(R.id.send_lottery_lny);
    		LinearLayout lnyType = (LinearLayout) findViewById(R.id.send_lottery_lny_type);
    		int mDialogY = scroll.getTop() + lnyType.getTop() + lnyType.getHeight();
    		
    		mDialogType.setPopViewPosition(0, mDialogY);
    		mDialogType.show();
    	}else{
    		mDialogType.show();
    	}
    }
	
	private void showDialogNumSp(){
		if(mDialogNumSp == null){
			final String[] item = getResources().getStringArray(R.array.send_lottery_number);
			mDialogNumSp = new PopWindowDialog(this, item, 0);
			
			mDialogNumSp.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogNumSp.getList();
					CheckBox checkBox;
					
					for(int i = 0, size = list.size(); i < size; i++){
						checkBox = list.get(i);
						if(i == v.getId()){
							checkBox.setChecked(true);
							lottery_num_sp.setText(item[i]);
							
						}else{
							if(checkBox.isChecked()) checkBox.setChecked(false);
						}
					}
					mDialogNumSp.dismiss();
				}
			});
			
			ScrollView scroll = (ScrollView) findViewById(R.id.send_lottery_lny);
			LinearLayout lnyType = (LinearLayout) findViewById(R.id.send_lottery_lny_type);
			int mDialogY = scroll.getTop() + lnyType.getTop() + lnyType.getHeight();
			
			mDialogNumSp.setPopViewPosition(0, mDialogY);
			mDialogNumSp.show();
		}else{
			mDialogNumSp.show();
		}
	}

	private boolean getNumberArray(int sum){
		LotteryInfoBean item =  LotteryId.getLotteryInfoBeanByLid(lotteryId);
		if(item == null){
			return false;
		}

		if(infoBean == null)
			return false;
			
		buyNumberArray = RandomSelectUtil.getRandomNumber(sum, infoBean);
		
		ArrayList<IssueInfoBean> beanList = item.getIssueInfoList();
		issueArray = new ArrayList<BetIssueBean>(1);
		for(IssueInfoBean mInfoBean: beanList){
			if(mInfoBean.getStatus().equals("1")){
				BetIssueBean mIssueBean = new BetIssueBean();
				mIssueBean.setIssue(mInfoBean.getName());
				mIssueBean.setMultiple(1);
				issueArray.add(mIssueBean);
				break;
			}
		}
		return true;
	}
	
	private SelectInfoBean infoBean;
	
	
	public static String SEND_MAP = "map";
	private int sendStatus; //是否为竞彩送TA一注
	public static final String SEND_STATUS_KEY = "Status"; //Bundle at Key
	public static final int STATUS_SHU = 0; //送数字彩
	public static final int STATUS_JC = 1; //送竞彩
	
	private String lotteryId = "-";
	
}
