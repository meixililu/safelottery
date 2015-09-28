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
import com.zch.safelottery.activity.MessageCenterDetailsActivity;
import com.zch.safelottery.bean.NoticeBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.database.NoticeDatabaseUtil;
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
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.TimeUtils;

public final class NoticeFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
	private PullToRefreshListView listView;
	private NoticeAdapter adapter;
	private ViewHolder holder;
	private String typeId;
	private List<NoticeBean> dataList=new ArrayList<NoticeBean>();
	private LayoutInflater inflater; 
	private NoticeDatabaseUtil databaseUtil;
	private Map resultMap;
	private View view;
	private ImageLoader imageLoader;
	private TextView no_result_text;
	private  ProgressBar loadding_progressbar_m;
	private int page_num;
	private int page=1;
	private boolean isNeedRefresh;
	List<NoticeBean> listall;
	
	public static NoticeFragment newInstance(String id) {
		NoticeFragment fragment = new NoticeFragment();
		fragment.typeId = id;
		return fragment;
	}

	private String mContent = "???";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater=inflater;
		 view = inflater.inflate(R.layout.notice_list, container, false);
		 no_result_text=(TextView)view.findViewById(R.id.no_result_text);
		 loadding_progressbar_m=(ProgressBar)view.findViewById(R.id.loadding_progressbar_m);
		 initView();
		 doRequestTask();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(isNeedRefresh){
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
			isNeedRefresh = false;
		}
	}

	public void initView() {
		listView =(PullToRefreshListView)view.findViewById(R.id.notice_list_layout);
		adapter = new NoticeAdapter(dataList);
		listView.setAdapter(adapter);
		listView.setMode(Mode.BOTH);
		imageLoader = new ImageLoader(getActivity(), false, 65);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>(){

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					page=1;
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
		
		List<NoticeBean > list;
		public NoticeAdapter(List<NoticeBean> datalist) {
			this.list=datalist;
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
			if (convertView==null) {
				holder = new ViewHolder();
				convertView=inflater.inflate(R.layout.notice_list_item, null);
				holder.message_img = (ImageView) convertView.findViewById(R.id.message_img);
				holder.title_tx = (TextView) convertView.findViewById(R.id.title_tx);
				holder.time_tx = (TextView) convertView.findViewById(R.id.time_tx);
				holder.content_tx = (TextView) convertView.findViewById(R.id.content_tx);
				holder.Expired_img = (ImageView) convertView.findViewById(R.id.Expired_img);
				holder.notice_item_layout = (LinearLayout) convertView.findViewById(R.id.notice_item_layout);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final NoticeBean bean =list.get(position);
			if(bean.getIsclick().equals("1")){
				holder.title_tx.setTextColor(getResources().getColor(R.color.black));
			}else{
				holder.title_tx.setTextColor(getResources().getColor(R.color.text_grey));
			}
			if(!TextUtils.isEmpty(bean.getCover())){
				holder.message_img.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage( bean.getCover(), getActivity(), holder.message_img);
			}else{
				holder.message_img.setVisibility(View.GONE);
			}
			holder.title_tx.setText(bean.getTitle());
			String startTime=TimeUtils.getTimeDate(TimeUtils.getDateToTime(bean.getPub_datetime()));
		   String endStr=bean.getExpiration_time();
			if(!TextUtils.isEmpty(endStr)){
				String endTime=TimeUtils.getTimeDate(TimeUtils.getDateToTime(bean.getExpiration_time()));
				if(typeId.equals("12")){
					holder.time_tx.setText("发布时间:"+startTime+"至"+endTime);
				}else{
					holder.time_tx.setText("活动时间:"+startTime+"至"+endTime);
				}
			}else{
				if(typeId.equals("12")){
					holder.time_tx.setText("发布时间:"+startTime);
				}else{
					holder.time_tx.setText("活动时间:"+startTime);
				}
			}
			holder.content_tx.setText("\t\t"+bean.getDescription()+"......"+"【详情】");
			holder.notice_item_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bean.setIsclick("0");
					databaseUtil.updateIsclick(bean);
					Intent intent=new Intent(getActivity(),MessageCenterDetailsActivity.class);
					intent.putExtra("nid", bean.getId());
					intent.putExtra("Simple_title", bean.getSimple_title());
					startActivity(intent);
					isNeedRefresh = true;
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		public ImageView message_img;
		public TextView title_tx;
		public TextView content_tx;
		public TextView time_tx;
		public ImageView Expired_img;
		public LinearLayout notice_item_layout;
	}

	/**
	 * 请求数据的AsyncTask
	 * @author Messi
	 */
	private void doRequestTask(){
		if(dataList.size() == 0)
			loadding_progressbar_m.setVisibility(View.VISIBLE);
		no_result_text.setVisibility(View.GONE);
		SafelotteryHttpClient.post(getActivity(), "3004", "message", initDate(), new TypeResultHttpResponseHandler(getActivity(),true) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				try {
					String resultStr=mResult.getResult();
					resultMap = JsonUtils.stringToMap(resultStr);
					String articleList=(String) resultMap.get("articleList");
					Map map= JsonUtils.stringToMap(articleList);
					page_num=ConversionUtil.StringToInt((String) map.get("page_num"));
					String articles = (String) map.get("articles");
					LogUtil.DefalutLog("page_num:"+page_num);
					if (!TextUtils.isEmpty(articles)) {
						MessageDataParser messageDataParser = new MessageDataParser();
						listall = (List<NoticeBean>) JsonUtils.parserJsonArray(articles, messageDataParser);
						if(listall!=null){
							databaseUtil = new NoticeDatabaseUtil(getActivity());
							databaseUtil.insertNewList(listall);
						}
					}
					if(listall!=null){
						if(page == 1)
							dataList.clear();
						dataList.addAll(listall);
					}
					if(dataList == null){
						no_result_text.setVisibility(View.VISIBLE);
					}if(dataList.size() == 0){
						no_result_text.setVisibility(View.VISIBLE);
					}else{
						page++;
						adapter.notifyDataSetChanged();
						if(page > page_num){
							listView.setMode(Mode.PULL_FROM_START);
						}else{
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
	
	public String initDate() {
	    Map< Object, Object> map =new HashMap<Object, Object>();
	    map.put("type", typeId);
	    map.put("page", page);
	    map.put("pageSize", 10);
		String str = JsonUtils.toJsonStr(map);
		return str;
	}
}
