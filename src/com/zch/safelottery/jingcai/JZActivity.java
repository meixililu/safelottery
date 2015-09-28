package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.WebViewTitleBarActivity;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.AsyncHttpClient;
import com.zch.safelottery.http.RequestParams;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.SLManifest;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TableUIUtil;

public class JZActivity extends ZCHBaseActivity {

	public static final String WF_SPF = "05";
	public static final String WF_RQSPF = "01";
	public static final String WF_QCBF = "04";
	public static final String WF_JQS = "02";
	public static final String WF_BQC= "03";
	public static final String WF_HHGG= "10";
	
	public static final String WF_8_YUAN= "11";//8元保中
	public static final String WF_WORLD_CUP= "12";//世界杯
	public static final String WF_CGJ= "13";//猜冠军

	private PullToRefreshScrollView scrollview;
	private LinearLayout jz_parent_layout;
	private SharedPreferences spf;
	private Bundle mBundle;
	private String mBaYuanBaoZhong,mShiJieBei,mGuanJun,mNaoZhong;
	private ArrayList<String> mNames;
	private ArrayList<Integer> mIcons;
	private ArrayList<Integer> mIds;
	private ProgressBar mProgressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_choice);
		try {
			initViews();
			postLoadingBgAndSalesRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initViews() {
		spf = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		mBaYuanBaoZhong = spf.getString(SLManifest.BaYuanBaoZhongKey, "0");
		mShiJieBei = spf.getString(SLManifest.ShiJieBeiKey, "0");
		mGuanJun = spf.getString(SLManifest.GuanJunWanFa, "0");
		mNaoZhong = spf.getString(SLManifest.NaoZhong, "0");
		
		scrollview = (PullToRefreshScrollView) findViewById(R.id.scrollview); 
		scrollview.setMode(Mode.DISABLED);
		jz_parent_layout = (LinearLayout) findViewById(R.id.jz_parent_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.loadding_progressbar_m);
	}
	
	private void getDatas(){
		mNames = new ArrayList<String>();
		mIcons = new ArrayList<Integer>();
		mIds = new ArrayList<Integer>();
		if(mShiJieBei.equals("1")){
			mNames.add("1");
			mIcons.add(R.drawable.jc_worldwup_logo_btn);
			mIds.add(0);
		}
		if(mBaYuanBaoZhong.equals("1")){
			mNames.add("1");
			mIcons.add(R.drawable.caoping_icon_8yuan);
			mIds.add(1);
		}
		if(mNaoZhong.equals("1")){
			mNames.add("2");
			mIcons.add(R.drawable.worldcup_custom_icon);
			mIds.add(10);
		}
		mNames.add("胜平负");
		mIcons.add(R.drawable.jz_icon_spf);
		mIds.add(2);
		mNames.add("让球胜平负");
		mIcons.add(R.drawable.jz_icon_rqspf);
		mIds.add(3);
		mNames.add("全场比分");
		mIcons.add(R.drawable.jz_icon_qcbf);
		mIds.add(4);
		mNames.add("总进球数");
		mIcons.add(R.drawable.jz_icon_zjqs);
		mIds.add(5);
		mNames.add("半全场");
		mIcons.add(R.drawable.jz_icon_bqc);
		mIds.add(6);
		mNames.add("混合过关");
		mIcons.add(R.drawable.jz_icon_hhgg);
		mIds.add(7);
		if(mGuanJun.equals("1")){
			//猜冠军
			mNames.add("猜冠军");
			mIcons.add(R.drawable.jz_icon_cgj);
			mIds.add(8);
			
		}
		
		TableUIUtil.getJZtable(this, jz_parent_layout, clickListener, mNames, mIcons, mIds, 3);
	}

	/**
	 * 发送 获取首页和活动的配置信息的 请求
	 * @param context
	 * @throws Exception
	 */
	private void postLoadingBgAndSalesRequest() throws Exception{
		mProgressBar.setVisibility(View.VISIBLE);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("length", SystemInfo.height+"");
		map.put("width", SystemInfo.width+"");
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(4000);
		String msg = JsonUtils.toJsonStr(map);
		RequestParams params = SafelotteryHttpClient.getRequestData(this, "3002","", msg);
		client.post(GetString.SERVERURL, params, new TypeResultHttpResponseHandler(this,false) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				Result result = mResult;
				if(result != null){
					String resultStr = result.getResult();
					if(!TextUtils.isEmpty(resultStr)){
						HashMap<String,String> listMapStr = (HashMap<String, String>) JsonUtils.stringToMap(resultStr);
						if(listMapStr != null){
							String mBaYuanBaoZhongKey = listMapStr.get(SLManifest.BaYuanBaoZhongKey);
							String mShiJieBeiKey = listMapStr.get(SLManifest.ShiJieBeiKey);
							String mGuanJunWanFa = listMapStr.get(SLManifest.GuanJunWanFa);
							String naoZhong = listMapStr.get(SLManifest.NaoZhong);
							String mShouYeTanCeng = listMapStr.get(SLManifest.ShouYeTanCeng);
							/**1为显示**/
							Settings.saveSharedPreferences(spf, SLManifest.BaYuanBaoZhongKey, mBaYuanBaoZhongKey);
							Settings.saveSharedPreferences(spf, SLManifest.ShiJieBeiKey, mShiJieBeiKey);
							Settings.saveSharedPreferences(spf, SLManifest.GuanJunWanFa, mGuanJunWanFa);
							Settings.saveSharedPreferences(spf, SLManifest.NaoZhong, naoZhong);
							Settings.saveSharedPreferences(spf, SLManifest.ShouYeTanCeng, mShouYeTanCeng);
						}
					}
				}
				getDatas();
			}
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				getDatas();
			}
			@Override
			public void onFinish() {
				mProgressBar.setVisibility(View.GONE);
			}
		});
	}
	
	  /**
	   * 跳转到还android自定义标题的webview页面
	   * @param title 页面标题，如果为null或者空串，则默认显示网页的标题
	   * @param url 页面地址
	   * @param isAddJsInterface 是否需要添加js接口
	   */
	private void gotoWebActivity(String title, String url, boolean isAddJsInterface){
		Intent intent = new Intent(JZActivity.this,WebViewTitleBarActivity.class);
		intent.putExtra(WebViewTitleBarActivity.TITLE, title);
		intent.putExtra(WebViewTitleBarActivity.URL, url);
		intent.putExtra(WebViewTitleBarActivity.IS_ADD_JS_INTERFACE, isAddJsInterface);
		startActivity(intent);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case 0://世界杯
				gotoWebActivity("世界杯", GetString.WORLD_CUP_SERVER, true);
				StatService.onEvent(JZActivity.this, "ft-first-button", "竞足页-第一个按钮-世界杯", 1);
				break;
			case 1://8元保中
				gotoWebActivity("竞彩8元保中", GetString.RMB_8_YUAN_SERVER, true);
				StatService.onEvent(JZActivity.this, "ft-second-button", "竞足页-第二个按钮-8元保中", 1);
				break;
			case 10://世界杯赛事提醒
				Intent intent = new Intent(JZActivity.this, WorldcupAlarmActivity.class);
				JZActivity.this.startActivity(intent);
				StatService.onEvent(JZActivity.this, "jingzu-saishialarm", "竞足页-赛事闹铃", 1);
				break;
			case 2://胜平负
				startIntent(WF_SPF);
				StatService.onEvent(JZActivity.this, "jingzu-shengpingfu", "竞足-胜平负", 1);
				break;
			case 3://让球胜平负
				startIntent(WF_RQSPF);
				StatService.onEvent(JZActivity.this, "jingzu-shengpingfu", "竞足-胜平负", 1);
				break;
			case 4://全场比分
				startIntent(WF_QCBF);
				StatService.onEvent(JZActivity.this, "jingzu-quanchang", "竞足-全场比分", 1);
				break;
			case 5://进球数
				startIntent(WF_JQS);
				StatService.onEvent(JZActivity.this, "jingzu-jinqiu", "竞足-总进球数", 1);
				break;
			case 6://半全场
				startIntent(WF_BQC);
				StatService.onEvent(JZActivity.this, "jingzu-banquanchang", "竞足-半全场", 1);
				break;
			case 7://混合过关
				startIntent(WF_HHGG);
				StatService.onEvent(JZActivity.this, "jingzu-hunheguoguan", "竞足-混合过关", 1);
				break;
			case 8://猜冠军
				startIntent(WF_CGJ);
				StatService.onEvent(JZActivity.this, "jingzu-caiguanjun", "竞足-猜冠军", 1);
				break;
//			case R.id.bd_choice_second_img://猜冠亚军
//				ToastUtil.diaplayMesShort(getApplicationContext(), "暂无数据");
//				break;
////			case R.id.bd_choice_champion_img://猜冠军
////				ToastUtil.diaplayMesShort(getApplicationContext(), "暂无数据");
////				break;
			default:
				break;
			}
		}
	};
	
	private void startIntent(String playMethod){
		LogUtil.DefalutLog("竞彩足球玩法："+playMethod);
		Intent intent = new Intent(JZActivity.this, JZBetActivity.class);
		if (WF_CGJ.equals(playMethod)) {
			intent = new Intent(JZActivity.this, JZCGJActivity.class);
		}
		intent.putExtra("playMethod", playMethod);
		intent.putExtra("lid", LotteryId.JCZQ);
		intent.putExtra(Settings.BUNDLE, mBundle);
		startActivity(intent);
	}

	@Override
	protected void isNeedClose(int ationCode) {
		super.isNeedClose(ationCode);
		if (ationCode == Settings.EXIT_JCZQ) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
