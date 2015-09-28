package com.zch.safelottery.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.ChargeYouhuimaList;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.MessageCenterActivity;
import com.zch.safelottery.activity.MessageCenterDetails2Activity;
import com.zch.safelottery.bean.NoticeBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.MessageDataParser;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.Mode;
import com.zch.safelottery.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.zch.safelottery.pulltorefresh.PullToRefreshListView;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;

public final class MessageFragment extends Fragment {

	private static final String KEY_CONTENT = "TestFragment:Content";

	/** 总个数 **/
	private int itemTotal;

	private PullToRefreshListView listView;
	private NoticeAdapter adapter;
	private ViewHolder holder;
	private String typeId;
	private List<NoticeBean> dataList = new ArrayList<NoticeBean>();
	private LayoutInflater inflater;
	private Map resultMap;
	private View view;
	private ImageLoader imageLoader;
	private TextView no_result_text;
	private ProgressBar loadding_progressbar_m;
	private int page_num;
	private int page = 1;
	List<NoticeBean> listall;
	private int positionBean;

	public static MessageFragment newInstance(String id) {
		MessageFragment fragment = new MessageFragment();
		fragment.typeId = id;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.notice_list, container, false);
		no_result_text = (TextView) view.findViewById(R.id.no_result_text);
		loadding_progressbar_m = (ProgressBar) view.findViewById(R.id.loadding_progressbar_m);
		initView();
		doRequestTask();
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// 相当于Fragment的onResume
		if (isVisibleToUser) {
			if ((!GetString.isLogin || GetString.userInfo == null)) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(intent, 2);
			}
		} else {
			// 相当于Fragment的onPause
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				dataList.get(positionBean).setIsReaded("1");
				adapter.notifyDataSetChanged();
			}
		} else if (requestCode == 2) {
			if (GetString.isLogin) {
				page = 1;
				doRequestTask();
			}
		}
	}

	public void initView() {
		listView = (PullToRefreshListView) view.findViewById(R.id.notice_list_layout);
		adapter = new NoticeAdapter(dataList);
		listView.setAdapter(adapter);
		listView.setMode(Mode.BOTH);
		imageLoader = new ImageLoader(getActivity(), false, 65);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					page = 1;
					doRequestTask();
					break;
				case PULL_FROM_END:
					if (page <= page_num) {
						doRequestTask();
					}
					break;
				}
			}

		});

	}

	private class NoticeAdapter extends BaseAdapter {

		List<NoticeBean> list;

		public NoticeAdapter(List<NoticeBean> datalist) {
			this.list = datalist;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			final NoticeBean bean = list.get(position);

			int gray = getResources().getColor(R.color.text_grey);
			if ("1".equals(bean.getType())) {
				View v = inflater.inflate(R.layout.notice_youhuima_list_item, null);
				TextView tvExpire = (TextView) v.findViewById(R.id.tv_expire);
				TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
				tvTitle.setText(bean.getTitle());
				String startTime = bean.getStartTime();
				String endTime = bean.getEndTime();
				if (!TextUtils.isEmpty(endTime) && !endTime.equals("null")) {
					tvExpire.setText("时间：" + startTime.replaceAll("-", ".") + "-" + endTime.replaceAll("-", "."));
				} else {
					tvExpire.setText("时间：" + startTime.replaceAll("-", "."));
				}

				if (bean.getIsReaded().equals("0")) {
					int youhuiColor = getResources().getColor(R.color.black);
					tvExpire.setTextColor(youhuiColor);
					tvTitle.setTextColor(youhuiColor);
				} else {
					tvExpire.setTextColor(gray);
					tvTitle.setTextColor(gray);
				}
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), ChargeYouhuimaList.class);
						positionBean = position;
						intent.putExtra("nid", bean.getId());
						intent.putExtra("isReaded", bean.getIsReaded());
						startActivityForResult(intent, 1);
					}
				});
				
				return v;
			} else {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.notice_list_item, null);
				holder.message_img = (ImageView) convertView.findViewById(R.id.message_img);
				holder.title_tx = (TextView) convertView.findViewById(R.id.title_tx);
				holder.time_tx = (TextView) convertView.findViewById(R.id.time_tx);
				holder.content_tx = (TextView) convertView.findViewById(R.id.content_tx);
				holder.Expired_img = (ImageView) convertView.findViewById(R.id.Expired_img);
				holder.notice_mark_new = (ImageView) convertView.findViewById(R.id.notice_mark_new);
				holder.notice_item_layout = (LinearLayout) convertView.findViewById(R.id.notice_item_layout);
				convertView.setTag(holder);

				if (bean.getIsReaded().equals("0")) {
					holder.title_tx.setTextColor(getResources().getColor(R.color.black));
					holder.notice_mark_new.setVisibility(View.VISIBLE);
				} else {
					holder.title_tx.setTextColor(gray);
					holder.notice_mark_new.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(bean.getCover())) {
					holder.message_img.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(bean.getCover(), getActivity(), holder.message_img);
				} else {
					holder.message_img.setVisibility(View.GONE);
				}
				holder.title_tx.setText(bean.getTitle());
				String startTime = bean.getStartTime();
				String endTime = bean.getEndTime();
				if (!TextUtils.isEmpty(endTime) && !endTime.equals("null")) {
					holder.time_tx.setText("时间:" + startTime + "至" + endTime);
				} else {
					holder.time_tx.setText("时间:" + startTime);
				}
				holder.content_tx.setText("\t\t" + bean.getAbstract() + "......" + "【详情】");
				holder.notice_item_layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), MessageCenterDetails2Activity.class);
						positionBean = position;
						intent.putExtra("nid", bean.getId());
						intent.putExtra("isReaded", bean.getIsReaded());
						startActivityForResult(intent, 1);
					}
				});
				return convertView;
			}
		}
	}

	class ViewHolder {
		public ImageView message_img;
		public TextView title_tx;
		public TextView content_tx;
		public TextView time_tx;
		public ImageView Expired_img;
		public ImageView notice_mark_new;
		public LinearLayout notice_item_layout;
	}

	/**
	 * 请求数据的AsyncTask
	 * @author Messi
	 */
	private void doRequestTask() {
		if (!GetString.isLogin || GetString.userInfo == null) {
			listView.onRefreshComplete();
			return;
		}

		if (dataList.size() == 0)
			loadding_progressbar_m.setVisibility(View.VISIBLE);

		no_result_text.setVisibility(View.GONE);
		SafelotteryHttpClient.post(getActivity(), "3004", "privateMessage", initDate(), new TypeResultHttpResponseHandler(getActivity(), true) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try {
					String resultStr = mResult.getResult();
					resultMap = JsonUtils.stringToMap(resultStr);
					page_num = ConversionUtil.StringToInt((String) resultMap.get("pageTotal"));
					page = ConversionUtil.StringToInt((String) resultMap.get("page"));
					itemTotal = ConversionUtil.StringToInt((String) resultMap.get("itemTotal"));
					MessageCenterDetails2Activity.Count = ConversionUtil.StringToInt((String) resultMap.get("noReadedCount"));

					MessageCenterActivity.setTextCount();

					LogUtil.DefalutLog("page_num:" + page_num);

					String articleList = (String) resultMap.get("noticeList");
					if (!TextUtils.isEmpty(articleList)) {
						MessageDataParser messageDataParser = new MessageDataParser();
						listall = (List<NoticeBean>) JsonUtils.parserJsonArray(articleList, messageDataParser);
					}
					if (listall != null) {
						if (page == 1)
							dataList.clear();
						dataList.addAll(listall);
					}
					if (!GetString.isLogin) {
						no_result_text.setText("暂未登录");
					}
					if (dataList == null) {
						no_result_text.setVisibility(View.VISIBLE);
					}
					if (dataList.size() == 0) {
						no_result_text.setVisibility(View.VISIBLE);
					} else {
						adapter.notifyDataSetChanged();
						page++;
						if (page > page_num) {
							listView.setMode(Mode.PULL_FROM_START);
						} else {
							listView.setMode(Mode.BOTH);
						}
					}
				} catch (Exception e) {
					LogUtil.ExceptionLog("BuyLotteryActivity-RequsetDataTask-doInBackground");
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				listView.onRefreshComplete();
				loadding_progressbar_m.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}
		});
	}

	private String initDate() {

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("userCode", GetString.userInfo == null ? "" : GetString.userInfo.getUserCode());
		map.put("page", page);
		map.put("pageSize", 10);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}
}
