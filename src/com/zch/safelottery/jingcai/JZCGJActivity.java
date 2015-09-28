package com.zch.safelottery.jingcai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.LotteryResultHallActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.bean.JZMatchListBean;
import com.zch.safelottery.bean.LotteryIssueHallBean;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JZMatchBeanParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryIssueHallParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;

/**
 * 竞足-猜冠军
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月5日
 */
public class JZCGJActivity extends ZCHBaseActivity {

	public static String INTENT_ISSUE = "issue";
	public static String ACTION_REFRESH_CHOOSE_NUM = "action_refresh_choose_num";

	public String issue;
	public String lid;

	protected Button cancelBtn;
	protected Button confirmBtn;
	protected Button finishBtn;
	protected TextView bet_num;

	private ProgressBar progressbar_m;
	private ImageView connection_faile_img;

	protected LinearLayout baseCtLayout;
	protected LinearLayout baseBottomLayout;

	private View mHeader;
	private PullToRefreshListView mLv;

	public int selectNumber = 0;// 选择场次数
	private int requestCode = 1000;
	private JZCGJAdapter mAdapter;
	private ArrayList<JZMatchBean> allResult = new ArrayList<JZMatchBean>();
	protected JZMatchListBean mResultBean;
	private TextView mTvStopDate;

	public static HashSet<JZMatchBean> mSelectItem = new HashSet<JZMatchBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_cgj);
		initUI();
		registerReceiver();
	}

	private void initUI() {

		cancelBtn = (Button) findViewById(R.id.jc_base_cancel);
		confirmBtn = (Button) findViewById(R.id.jc_base_confirm);
		finishBtn = (Button) findViewById(R.id.jc_base_jc_finish);

		/** content view,default gone **/
		baseCtLayout = (LinearLayout) findViewById(R.id.jc_base_content_linearlayout);
		baseBottomLayout = (LinearLayout) findViewById(R.id.jc_base_bottom);

		bet_num = (TextView) findViewById(R.id.jc_base_count);
		connection_faile_img = (ImageView) findViewById(R.id.connection_faile_img);
		progressbar_m = (ProgressBar) findViewById(R.id.progressbar_m);

		mLv = (PullToRefreshListView) findViewById(R.id.lv);
		ListView lv = mLv.getRefreshableView();
		mHeader = View.inflate(this, R.layout.jz_cgj_header, null);
		mTvStopDate = (TextView) mHeader.findViewById(R.id.tv_stop_date);
		lv.addHeaderView(mHeader, null, false);

		mAdapter = new JZCGJAdapter(this, allResult, getLayoutInflater());
		mLv.setAdapter(mAdapter);
		mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int rightId = (int) parent.getAdapter().getItemId(position);
				JZMatchBean item = allResult.get(rightId);
				CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
				if (cb != null) {
					String userSelect = item.getUserSelect();
					if (TextUtils.isEmpty(userSelect)) {
						item.setUserSelect(item.getSn());
						cb.setChecked(true);
						mSelectItem.add(item);
					} else {
						item.setUserSelect("");
						cb.setChecked(false);
						mSelectItem.remove(item);
					}
					setChangCi(mSelectItem.size());
				}
			}
		});

		mLv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mSelectItem.clear();
				setChangCi(mSelectItem.size());
				doRequestDataTask();
			}
		});
		
		doRequestDataTask();
	}

	private void doRequestDataTask() {
		SafelotteryHttpClient.post(this, "3302", "gjb", "", new TypeMapHttpResponseHandler(this, true) {
			@Override
			public void onStart() {
				connection_faile_img.setVisibility(View.GONE);
				if (allResult.size() == 0) {
					progressbar_m.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFinish() {
				progressbar_m.setVisibility(View.GONE);
				mLv.onRefreshComplete();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, @SuppressWarnings("rawtypes") Map mMap) {
				try {
					allResult = (ArrayList<JZMatchBean>) JsonUtils.parserJsonArray((String) mMap.get("matchList"), new JZMatchBeanParser());
			
					mResultBean = new JZMatchListBean();
					mResultBean.setMatchList(allResult);
					//拼停售时间
					String timeStr = (String) mMap.get("endtime");
					if (!TextUtils.isEmpty(timeStr)) {
						StringBuilder time = new StringBuilder();
						time.append(timeStr.subSequence(0, 4));
						time.append("-");
						time.append(timeStr.subSequence(4, 6));
						time.append("-");
						time.append(timeStr.subSequence(6, 8));
						time.append(" ");
						time.append(timeStr.subSequence(8, 10));
						time.append(":");
						time.append(timeStr.subSequence(10, 12));
						mResultBean.setDate(time.toString());
					}

					String statusStr = (String) mMap.get("status");
					if (!TextUtils.isEmpty(statusStr)) {
						mResultBean.setStatus(Integer.parseInt(statusStr));
					}
					mResultBean.setIssue((String) mMap.get("issue"));
					for (int i = 0; i < allResult.size(); i++) {
						allResult.get(i).setIssue(mResultBean.getIssue());
					}
					
					if (mResultBean != null) {
						mTvStopDate.setText("停售时间：" + mResultBean.getDate());
					}
					mAdapter.setData(allResult);
					mAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				if (allResult.size() == 0) {
					connection_faile_img.setVisibility(View.VISIBLE);
				}
			}

		});
	}

	/** 竞彩，设置选了几场比赛  **/
	public void setChangCi(int num) {
		bet_num.setText("已选" + num + "场");
	}

	public void bottom_clear(View v) {
		if (mSelectItem.size() > 0) {
			mSelectItem.clear();
			for (int i = 0; i < allResult.size(); i++) {
				allResult.get(i).setUserSelect(null);
			}
			mAdapter.setData(allResult);
			mAdapter.notifyDataSetChanged();
			setChangCi(0);
		} else {
			ToastUtil.diaplayMesShort(this, "您还没选择任何场次");
		}

	}

	public void bottom_submit(View v) {
		if (mSelectItem.size() > 0) {
			SafeApplication.dataMap.put("selectedBean", selectedBean());
			Intent intent = new Intent(this, JCOrderListActivity.class);
			intent.putExtra("lid", LotteryId.CGJ);
			intent.putExtra("playMethod", "01");
			intent.putExtra("issue", mResultBean.getIssue());
			intent.putExtra("selectNumber", mSelectItem.size());
			// intent.putExtra("danCount", danCount);
			// intent.putExtra(Settings.BUNDLE, mBundle);
			startActivityForResult(intent, requestCode);
		} else {
			ToastUtil.diaplayMesShort(getApplicationContext(), "至少选择1场比赛");
		}
	}

	private ArrayList<JZMatchBean> selectedBean() {
		ArrayList<JZMatchBean> resultList = new ArrayList<JZMatchBean>();
		for (JZMatchBean bean : allResult) {
			if (!TextUtils.isEmpty(bean.getUserSelect())) {
				resultList.add(bean);
			}
		}
		return resultList;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (this.requestCode == requestCode && resultCode == RESULT_OK) {
			bottom_clear(null);
		}
	}

	@Override
	public void onBackPressed() {
		if (selectNumber > 0) {
			NormalAlertDialog dialog = new NormalAlertDialog(this);
			dialog.setTitle("提示");
			dialog.setContent("您确定退出投注吗？一旦退出,您的投注将被清空。");
			dialog.setOk_btn_text("确定");
			dialog.setCancle_btn_text("取消");
			dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
				@Override
				public void onOkBtnClick() {
					finish();
					selectNumber = 0;
				}

				@Override
				public void onCancleBtnClick() {
				}
			});
			dialog.show();
		} else {
			super.onBackPressed();
		}
	}

	private RefreshChooseNumReceiver mReceiver;

	private void registerReceiver() {
		mReceiver = new RefreshChooseNumReceiver();
		IntentFilter filter = new IntentFilter(ACTION_REFRESH_CHOOSE_NUM);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		mSelectItem.clear();
		super.onDestroy();
	}

	class RefreshChooseNumReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			setChangCi(mSelectItem.size());
		}

	}
//	{
//	    "result": {
//	        "endtime": "20140714234500",
//	        "status": "1",
//	        "issue": "201407141001",
//	        "matchList": [
//	            {
//	                "sn": "01",
//	                "sp": "2.65",
//	                "name": "巴西",
//	                "league": "世界杯"
//	            },
//	            {
//	                "sn": "02",
//	                "sp": "4.4",
//	                "name": "德国",
//	                "league": "世界杯"
//	            }
//	        ]
//	    }
//	}
//	==============================
//	{
//	    "userCode": "20130725095635976074",
//	    "lotteryId": "202",
//	    "playId": "01",
//	    "buyType": "1",
//	    "buyNumberArray": [
//	        {
//	            "buyNumber": "201407141001:01:0;201407141001:05:0|1*1",
//	            "playId": "01",
//	            "pollId": "01",
//	            "item": "2",
//	            "amount": "4"
//	        }
//	    ],
//	    "issueArray": [
//	        {
//	            "issue": "201407141001",
//	            "multiple": "1"
//	        }
//	    ],
//	    "totalAmount": "4"
//	}

}
