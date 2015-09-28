package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.JCIssueBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshScrollView;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TableUIUtil;
import com.zch.safelottery.util.ToastUtil;

public class JLActivity extends ZCHBaseActivity {
	
	public static final String WF_SF = "01";
	public static final String WF_RFSF = "02";
	public static final String WF_SFC = "03";
	public static final String WF_DXF = "04";
	
	private PullToRefreshScrollView scrollview;
	private LinearLayout jz_parent_layout;
	private Bundle mBundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_choice);
		initViews();
		StatService.onEvent(getApplicationContext(), "jinglanqiu", "竞彩篮球", 1);
	}

	private void initViews() {
		TextView title = (TextView) findViewById(R.id.jz_choice_title);
		title.setText("竞彩篮球");

		scrollview = (PullToRefreshScrollView) findViewById(R.id.scrollview); 
		jz_parent_layout = (LinearLayout) findViewById(R.id.jz_parent_layout);
		scrollview.setMode(Mode.DISABLED);
		String[] names = {"胜负","让分胜负","胜分差","大小分"};
		int[] icons = {R.drawable.jl_shengfu, R.drawable.jl_rangfenshengfu, R.drawable.jl_shengfencha,
				R.drawable.jl_daxiaofen};
		
		TableUIUtil.getJZtable(this, jz_parent_layout, clickListener, names, icons, 3);
		
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case 0://胜负
				startIntent(WF_SF);
//				StatService.onEvent(getApplicationContext(), "jingzu-shengpingfu", "竞足-胜平负", 1);
				break;
			case 1://让分胜负
				startIntent(WF_RFSF);
//				StatService.onEvent(getApplicationContext(), "jingzu-quanchang", "竞足-全场比分", 1);
				break;
			case 2://胜分差
//				StatService.onEvent(getApplicationContext(), "jingzu-jinqiu", "竞足-总进球数", 1);
				startIntent(WF_SFC);
				break;
			case 3://大小分
//				StatService.onEvent(getApplicationContext(), "jingzu-banquanchang", "竞足-半全场", 1);
				startIntent(WF_DXF);
				break;
			default:
				break;
			}
		}
	};
	
	private void startIntent(String playMethod){
		LogUtil.DefalutLog("竞彩篮球玩法："+playMethod);
		Intent intent = new Intent(this, JLBetActivity.class);
		intent.putExtra("playMethod", playMethod);
		intent.putExtra("lid", LotteryId.JCLQ);
		intent.putExtra(Settings.BUNDLE, mBundle);
		startActivity(intent);
	}
	
	/**完场赛事
	 * @param wf
	 */
	private void toJCFinishActivity(String wf){
		Intent intent = new Intent(this, JCFinishActivity.class);
		intent.putExtra("issue", "");
		intent.putExtra("playMethod", wf);
		intent.putExtra("lid", LotteryId.JCLQ);
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