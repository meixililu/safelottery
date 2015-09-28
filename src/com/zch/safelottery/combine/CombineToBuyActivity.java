package com.zch.safelottery.combine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.asynctask.BuyLotteryTask;
import com.zch.safelottery.asynctask.BuyLotteryTask.BuyLotteryTaskListener;
import com.zch.safelottery.bean.RecordMainIssueListBean;
import com.zch.safelottery.bean.RecordMatchBean;
import com.zch.safelottery.bean.RecordProgramsListBean;
import com.zch.safelottery.bean.RecordSubGameListBean;
import com.zch.safelottery.bean.RecordUserInfoBean;
import com.zch.safelottery.dialogs.CombineBuyListDialog;
import com.zch.safelottery.dialogs.CombineTobuySchemeDialog;
import com.zch.safelottery.dialogs.PurchaseRechargeDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.RecordMainIssueListParser;
import com.zch.safelottery.parser.RecordMatchParser;
import com.zch.safelottery.parser.RecordProgramsListParser;
import com.zch.safelottery.parser.RecordSubGameListParser;
import com.zch.safelottery.parser.RecordUserInfoParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.StatusUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.XmlUtil;

public class CombineToBuyActivity extends Activity {
	
	private LayoutInflater mInflater;
	private TextView tv_title,tv_money;
	private PullToRefreshScrollView lv;
	private LinearLayout lvContent;
	private ImageView no_result_img;
	private ProgressBar progressbar;
	private LinearLayout bottom_layout;
	private Button submit_btn;
	private EditText buyMoney;
	
	private String userCode;
	private String programsOrderId;
	private String moneyStr;
	
	private RecordUserInfoBean recordUserInfoBean;
	private RecordProgramsListBean recordProgramsListBean;
	private RecordMainIssueListBean recordMainIssueListBean;
	private List<RecordSubGameListBean> recordSubGameListBean;
	private RecordMatchBean recordMatchBean;
	private BuyLotteryTask mBuyLotteryTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.combine_tobuy);
		initData();
		initViews();
		RequsetTask();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		Intent intent = getIntent();
		programsOrderId = intent.getStringExtra("programsOrderId");
		if(GetString.userInfo != null && !TextUtils.isEmpty(GetString.userInfo.getUserCode())){
			userCode = GetString.userInfo.getUserCode();
		}else{
			userCode = "";
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void initViews(){
		mInflater = LayoutInflater.from(this);
		tv_title = (TextView)this.findViewById(R.id.combine_tobuy_title);
		tv_money = (TextView)this.findViewById(R.id.combine_tobuy_money);
		submit_btn = (Button)this.findViewById(R.id.combine_tobuy_submit);
		bottom_layout = (LinearLayout)this.findViewById(R.id.combine_tobuy_bottom);
		lv = (PullToRefreshScrollView)this.findViewById(R.id.combine_tobuy_listview);
		lvContent = (LinearLayout) findViewById(R.id.combine_tobuy_content);
		no_result_img = (ImageView)this.findViewById(R.id.connection_faile_img);
		progressbar = (ProgressBar)this.findViewById(R.id.combine_tobuy_progressbar);
		lv.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				RequsetTask();
			}
			
		});
	}

	/**认购详情请求参数
	 * @return
	 */
	public String requestJson() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userCode", userCode);
		map.put("programsOrderId", programsOrderId);
		return JsonUtils.toJsonStr(map);
	}
	
	protected void onPreExecute() {
		no_result_img.setVisibility(View.GONE);
		bottom_layout.setVisibility(View.GONE);
		if(recordProgramsListBean == null || recordUserInfoBean == null )
			progressbar.setVisibility(View.VISIBLE);
	}
	
	private void RequsetTask(){
		onPreExecute();
		SafelotteryHttpClient.post(this, "3306", "programsNew", requestJson(), new TypeMapHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(int statusCode, Map resultMap) {
				try {
					if (resultMap != null) {
						String member = (String) resultMap.get("member");
						if (!TextUtils.isEmpty(member)) {
							recordUserInfoBean = (RecordUserInfoBean) JsonUtils.parserJsonBean(member, new RecordUserInfoParser());
							LogUtil.DefalutLog(recordUserInfoBean.toString());
						}
						String programs = (String) resultMap.get("programs");
						if (!TextUtils.isEmpty(programs)) {
							recordProgramsListBean = (RecordProgramsListBean) JsonUtils.parserJsonBean(programs, new RecordProgramsListParser());
							LogUtil.DefalutLog(recordProgramsListBean.toString());
						}
						String mainIssue = (String) resultMap.get("mainIssue");
						if (!TextUtils.isEmpty(mainIssue)) {
							recordMainIssueListBean = (RecordMainIssueListBean) JsonUtils.parserJsonBean(mainIssue, new RecordMainIssueListParser());
							LogUtil.DefalutLog(recordMainIssueListBean.toString());
						}
						String subGameList = (String) resultMap.get("subGameList");
						if (!TextUtils.isEmpty(subGameList)) {
							recordSubGameListBean = (List<RecordSubGameListBean>) JsonUtils.parserJsonArray(subGameList, new RecordSubGameListParser());
							LogUtil.DefalutLog(recordSubGameListBean.toString());
						}
						String match = (String) resultMap.get("match");
						if (!TextUtils.isEmpty(match)) {
							recordMatchBean = (RecordMatchBean) JsonUtils.parserJsonBean(match, new RecordMatchParser());
							LogUtil.DefalutLog(recordMatchBean.toString());
						}
						showData();
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() {
				progressbar.setVisibility(View.GONE);
				bottom_layout.setVisibility(View.VISIBLE);
				clear();
				lv.onRefreshComplete();
			}
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}
		});
	}
	
	private void clear(){
		moneyStr = "";
		if(buyMoney != null){
			buyMoney.setText("");
		}
		setText(tv_money, "共" + 0 + "元" );
	}
	
	/**
	 * 展示数据
	 */
	private void showData(){
		View convertView = mInflater.inflate(R.layout.combine_tobuy_content, null);
		/**part one**/
		LinearLayout percent = (LinearLayout) convertView.findViewById(R.id.combine_lottery_percent);
		TextView userName = (TextView) convertView.findViewById(R.id.combine_lottery_user_name);
		TextView moneyTotal = (TextView) convertView.findViewById(R.id.combine_lottery_money_total);
		TextView moneyBuy = (TextView) convertView.findViewById(R.id.combine_lottery_money_buy);
		TextView lotteryName = (TextView) convertView.findViewById(R.id.combine_lottery_name);
		TextView commission = (TextView) convertView.findViewById(R.id.combine_lottery_commission);
		TextView orderStatu = (TextView) convertView.findViewById(R.id.combine_lottery_order_statu);
		/**part two**/
		TextView start_time_tv = (TextView) convertView.findViewById(R.id.combine_tobuy_start_time);
		TextView play_method_tv = (TextView) convertView.findViewById(R.id.combine_tobuy_play_method);
		TextView beitou_num_tv = (TextView) convertView.findViewById(R.id.combine_tobuy_beitou_num);
		TextView scheme_stutas_tv = (TextView) convertView.findViewById(R.id.combine_tobuy_scheme_stutas);
		TextView scheme_declaration_tv = (TextView) convertView.findViewById(R.id.combine_tobuy_scheme_declaration);
		
		Button participant_btn = (Button) convertView.findViewById(R.id.combine_tobuy_participant);
		Button scheme_detail_btn = (Button) convertView.findViewById(R.id.combine_tobuy_scheme_detail);
		buyMoney = (EditText) convertView.findViewById(R.id.combine_tobuy_tobuy);
		
		if(recordProgramsListBean != null && recordUserInfoBean != null ){
			String lotteryNameStr = LotteryId.getLotteryName(recordProgramsListBean.getLotteryId(), recordProgramsListBean.getPlayId());
			
			setText( tv_title,"第" + recordProgramsListBean.getIssue() + "期 " + lotteryNameStr + "合买" );
			/**part one set content**/
			percent.addView(XmlUtil.getPercent(this, (int)ConversionUtil.StringToDouble(recordProgramsListBean.getBuyPercent().replace("%", "")), 
					(int)ConversionUtil.StringToDouble(recordProgramsListBean.getLastPercent().replace("%", ""))));
			userName.setText(recordUserInfoBean.getUserName());
			moneyTotal.setText(recordProgramsListBean.getTotalWere() + "元");
			moneyBuy.setText( recordProgramsListBean.getSurplusWere() + "元");
			lotteryName.setText( lotteryNameStr );
			commission.setText("佣" + recordProgramsListBean.getCommission().replace("%", "") + "%");
			orderStatu.setText(StatusUtil.getOrderStatus(recordProgramsListBean.getOrderStatus()));
			
			/**part two set content**/
			setText( start_time_tv,"发起时间：" + recordProgramsListBean.getCreateTime() );
			setText( play_method_tv,"玩法：" + recordProgramsListBean.getPlayName() );
			setText( beitou_num_tv,"方案倍数：" + recordProgramsListBean.getMultiple() + "倍" );
			setText( scheme_stutas_tv,"方案状态：" + StatusUtil.getOrderStatus(recordProgramsListBean.getOrderStatus()) );
			setText( scheme_declaration_tv,recordProgramsListBean.getDescription() );
			
			setText( participant_btn,"共" + recordProgramsListBean.getRenGouCount() + "人" );
			int size = 0;
			if(recordProgramsListBean.getNumberInfo() != null){
				size = recordProgramsListBean.getNumberInfo().size();
			}
			
			if(recordProgramsListBean.getShowNumber().equals("1")){
				setText( scheme_detail_btn, "共" + size + "条" );
			}else{
				setText( scheme_detail_btn, StatusUtil.getPrivacy(recordProgramsListBean.getPrivacy()) );
			}
			
			/**set click listener**/
			MyClickListener mMyClickListener = new MyClickListener();
			participant_btn.setOnClickListener(mMyClickListener);
			submit_btn.setOnClickListener(mMyClickListener);
			scheme_detail_btn.setOnClickListener(mMyClickListener);
			buyMoney.addTextChangedListener(new MyTextWatcher(1,recordProgramsListBean.getSurplusWere(),tv_money,buyMoney) );
			lvContent.removeAllViews();
			lvContent.addView(convertView);
		}
	}
	
	/**textview 设置内容
	 * @param tv
	 * @param text
	 */
	private void setText(TextView tv, String text){
		if(TextUtils.isEmpty(text)){
			tv.setText("");
		}else{
			tv.setText(text);
		}
	}
	
	private void showDialog(){
		CombineBuyListDialog mDialog = new CombineBuyListDialog(this, userCode, recordProgramsListBean.getProgramsOrderId());
		mDialog.show();
	}
	
	class MyClickListener implements View.OnClickListener{
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.combine_tobuy_participant){
				showDialog();
			}else if(v.getId() == R.id.combine_tobuy_submit){
				if(isLogin()){
					if(!TextUtils.isEmpty(moneyStr)){
						double remainMoney = ConversionUtil.StringToDouble(GetString.userInfo.getUseAmount());
						int totalAmount = ConversionUtil.StringToInt(moneyStr);
						if (remainMoney >= totalAmount) {
							buyTask();
						}else {
							PurchaseRechargeDialog dialog = new PurchaseRechargeDialog(CombineToBuyActivity.this);
							dialog.setMoney(totalAmount - remainMoney);
							dialog.show();
						}
					}else{
						diaplayToast("请输入认购金额");
					}
				}
			}else if(v.getId() == R.id.combine_tobuy_scheme_detail){
				if(isLogin()){
					if(isShowScheme( recordProgramsListBean.getShowNumber() )){
						CombineTobuySchemeDialog schemeDialog = new CombineTobuySchemeDialog(CombineToBuyActivity.this, recordProgramsListBean,
								recordMainIssueListBean, recordSubGameListBean, recordMatchBean);
						schemeDialog.show();
					}
				}
			}
		}
	}
	
	/**是否登录
	 * @return
	 */
	private boolean isLogin(){
		if(GetString.isLogin && GetString.userInfo != null){
			return true;
		}else{
			Intent intent = new Intent(CombineToBuyActivity.this, LoginActivity.class);
			CombineToBuyActivity.this.startActivity(intent);
			return false;
		}
	}
	
	/**是否显示方案，true显示，false不显示
	 * @param code
	 * @return
	 */
	private boolean isShowScheme(String code){
		return code.equals("1");
	}
	
	/**
	 * @author Messi
	 *认购金额输入框观察器
	 */
	class MyTextWatcher implements TextWatcher{
		
		private int min;
		private int max;
		private TextView money_tv;
		private String beforeText;
		private EditText et;
		
		public MyTextWatcher(int min, int max, TextView tv, EditText et){
			this.min = min;
			this.max = max;
			this.money_tv = tv;
			this.et = et;
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			beforeText = s.toString();
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			LogUtil.DefalutLog("onTextChanged-s:"+s);
			String numStr = s.toString();
			if(!TextUtils.isEmpty(numStr)){
				int num = ConversionUtil.StringToInt(numStr);
				if( num >= min && num <= max ){
					moneyStr = numStr;
					setText(money_tv, "共" + numStr + "元" );
				}else{
					moneyStr = beforeText;
					setText(money_tv, "共" + beforeText + "元" );
					setText(et, beforeText );
					et.setSelection(beforeText.length()-1);
					if(max < 1){
						diaplayToast("方案已满员，无法认购");
					}else{
						diaplayToast("认购金额最少" + min + "元," + "最多" + max + "元");
					}
				}
			}else{
				moneyStr = "";
				setText(money_tv, "共" + 0 + "元" );
			}
		}
		@Override
		public void afterTextChanged(Editable s) {
		}
	}
	
	/**显示toast
	 * @param content
	 */
	private void diaplayToast(String content){
		ToastUtil.diaplayMesShort(CombineToBuyActivity.this, content);
	}
	
	/**
	 * 认购
	 */
	private void buyTask(){
		if(GetString.userInfo == null || recordProgramsListBean == null){
			return;
		}
		submit_btn.setEnabled(false);
		final Map<String, String> map = new HashMap<String, String>();
		map.put("userCode", GetString.userInfo.getUserCode());
		map.put("programsOrderId", recordProgramsListBean.getProgramsOrderId());
		map.put("amount", moneyStr);
		mBuyLotteryTask = new BuyLotteryTask(this,"subscribe", JsonUtils.toJsonStr(map), submit_btn);
		mBuyLotteryTask.setmBuyLotteryTaskListener(new BuyLotteryTaskListener() {
			@Override
			public void onBuyLotteryTaskFinish() {
				clear();
				hideIME();
				RequsetTask();
				ToastUtil.diaplayMesLong(getApplicationContext(), "认购成功");
			}
		});
		mBuyLotteryTask.send();
	}
	
	/**
	 * 认购成功之后隐藏输入法
	 */
	private void hideIME(){
		final InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);       
		imm.hideSoftInputFromWindow(buyMoney.getWindowToken(), 0); 
	}

}
