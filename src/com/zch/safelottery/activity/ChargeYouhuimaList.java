package com.zch.safelottery.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.database.NoticeDatabaseUtil;
import com.zch.safelottery.dialogs.ShareDialog;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.util.AnimationUtil;
import com.zch.safelottery.util.StringUtil;
import com.zch.safelottery.util.share.BaseShare;
import com.zch.safelottery.util.share.ShareUtil;

public class ChargeYouhuimaList extends ZCHBaseActivity {

	private PullToRefreshListView mLv;
	private String mArticleId;
	private ImageView connection_faile_img;
	private ProgressBar progressbar_m;
	private YouhuiMaListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_youhuima_list);
		Intent intent = getIntent();
		mArticleId = intent.getStringExtra("nid");
		initUI();
		requestDataTask();
	}

	private void initUI() {

		connection_faile_img = (ImageView) findViewById(R.id.connection_faile_img);
		progressbar_m = (ProgressBar) findViewById(R.id.lottery_result_hall_progressbar_m);

		mLv = (PullToRefreshListView) findViewById(R.id.lv);

		mAdapter = new YouhuiMaListAdapter();
		mLv.setAdapter(mAdapter);
		mLv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				requestDataTask();
			}
		});
	}

	private JSONArray mCoupon;

	private void requestDataTask() {
		SafelotteryHttpClient.post(this, "3004", "onePrivateMessage", initData(), new TypeResultHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try {
					connection_faile_img.setVisibility(View.GONE);
					String resultStr = mResult.getResult();
					Map<String, String> resultMap = JsonUtils.stringToMap(resultStr);
					String articleDetail = (String) resultMap.get("articleDetail");
					Map map = JsonUtils.stringToMap(articleDetail);
					String article = (String) map.get("article");
					Map map2 = JsonUtils.stringToMap(article);
					JSONObject json = new JSONObject(map2);
					String content = json.getString("newContent");
					if (!TextUtils.isEmpty(content)) {
						json = new JSONObject(content);
						mCoupon = json.getJSONArray("coupons");
					}
					mAdapter.notifyDataSetChanged();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				progressbar_m.setVisibility(View.GONE);
				connection_faile_img.setVisibility(View.VISIBLE);
			}

			@Override
			public void onStart() {
				progressbar_m.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFinish() {
				progressbar_m.setVisibility(View.GONE);
			}
		});
	}

	public String initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("messageId", mArticleId);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}

	class YouhuiMaListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mCoupon == null ? 0 : mCoupon.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = View.inflate(ChargeYouhuimaList.this, R.layout.charge_youhuima_list_item, null);
			}
			if (mCoupon != null && mCoupon.length() > 0) {
				try {
					JSONObject json = mCoupon.getJSONObject(position);
					final String code = json.getString("coupon");
					String amount = StringUtil.subZeroAndDot(json.getString("amount"));
					String score = String.format("%s元!", amount);

					if (convertView == null) {
						convertView = View.inflate(ChargeYouhuimaList.this, R.layout.charge_youhuima_list_item, null);
					}
					TextView tvScore = (TextView) convertView.findViewById(R.id.tv_score);
					TextView tvExpire = (TextView) convertView.findViewById(R.id.tv_expire);

					tvScore.setText(score);
					tvExpire.setText("时间:" + json.getString("exp"));
					tvScore.getPaint().setFakeBoldText(true);

					View ll = convertView.findViewById(R.id.ll);
					ll.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(ChargeYouhuimaList.this, ChargeYouHuiMaExchangeActivity.class);
							intent.putExtra("code", code);
							startActivity(intent);
						}
					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
				return convertView;
			} else
				return null;
		}
	}

}
