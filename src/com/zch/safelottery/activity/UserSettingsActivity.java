package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.order.DragSortListView;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;

public class UserSettingsActivity extends ZCHBaseActivity implements OnClickListener {
	/** Called when the activity is first created. */

	private CheckBox sakeBox;
	private CheckBox songBox;

	private RadioButton radioNumber;
	private RadioButton radioPush;
	private SharedPreferences sakePreference;

	private TextView tvPrompt;
	private LayoutInflater inflater;
	private DragSortListView listView;
	// 玩法左右滑动
	private final int sumView = 3;
	private ArrayList<View> views;
	private MyPagerAdapter myPagerAdapter;
	private ViewPager scrollScreen;

	public static String IS_SHAKE = "IS_SHAKE";// 摇晃
	public static String IS_SONG = "IS_SONG";// 声音
	// 推送
	private TextView starttime;
	private TextView endtime;
	private int mHour;
	private int mMinute;
	private static final int SHOW_START_TIMEPICK = 0;
	private static final int SHOW_END_TIMEPICK = 1;
	private int timenum;
	private int type = 0;
	private JazzAdapter adapter;
	private ArrayList<JazzArtist> list;
	private View view;
	private CheckBox checkBox1;// 设置是否推送开奖
	private CheckBox checkBox2;// 设置是否推送截止时间
	private CheckBox checkBox3;// 设置是否全天推送
	private CheckBox checkBox4;// 设置某些时间段推送
	private CheckBox checkBox5;// 设置从不接收消息
	private RelativeLayout user_setting_push_lottery;
	
	public final static String LotteryForecastCBKey = "LotteryForecastCBKey";
	public final static String LotteryEndTimeCBKey = "LotteryEndTimeCBKey";
	public final static String PullTimeCBKey = "PullTimeCBKey";
	public final static String StartTimeKey = "StartTimeKey";
	public final static String EndTimeKey = "EndTimeKey";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.user_setting);

			initData();
			initView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		sakePreference = getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		scrollScreen = (ViewPager) findViewById(R.id.user_setting_screen_view);
		radioNumber = (RadioButton) findViewById(R.id.user_setting_number);
		radioPush = (RadioButton) findViewById(R.id.user_setting_push);

		radioNumber.setOnClickListener(this);
		radioPush.setOnClickListener(this);

		views = new ArrayList<View>(sumView);
		views.add(settingNumber());
		views.add(settingPush());
		myPagerAdapter = new MyPagerAdapter(views);
		scrollScreen.setAdapter(myPagerAdapter);
		scrollScreen.setOnPageChangeListener(new MyOnPageChangeListener());
		scrollScreen.setCurrentItem(type);
		
	}

	/****
	 * 推送设置
	 * 
	 * @return
	 */
	private View settingPush() {
		inflater = LayoutInflater.from(this);
		view = (View) inflater.inflate(R.layout.user_setting_push, null);
		checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
		checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
		checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
		checkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
		checkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
		user_setting_push_lottery = (RelativeLayout) view.findViewById(R.id.user_setting_push_lottery);
		LinearLayout push_forecast = (LinearLayout) view.findViewById(R.id.user_setting_push_forecast);
		LinearLayout push_endtime = (LinearLayout) view.findViewById(R.id.user_setting_push_endtime);
		starttime = (TextView) view.findViewById(R.id.start_time);
		endtime = (TextView) view.findViewById(R.id.endtime);

		push_forecast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBox1.setChecked(!checkBox1.isChecked());
			}
		});
		push_endtime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-push-openda", "更多-软设-推送-截止时间提醒", 1);
				checkBox2.setChecked(!checkBox2.isChecked());
			}
		});
		checkBox3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-push-allday", "更多-软设-推送-接收时间全天", 1);
				if (checkBox3.isChecked()) {
					checkBox4.setChecked(false);
					checkBox5.setChecked(false);
				} else {
					checkBox3.setChecked(true);
				}
			}
		});
		checkBox4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-push-settim", "更多-软设-推送-接收时间设置", 1);
				if (checkBox4.isChecked()) {
					checkBox3.setChecked(false);
					checkBox5.setChecked(false);
				} else {
					checkBox4.setChecked(true);
				}
			}
		});
		checkBox5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-push-never", "更多-软设-推送-从不接收", 1);
				if (checkBox5.isChecked()) {
					checkBox4.setChecked(false);
					checkBox3.setChecked(false);
				} else {
					checkBox5.setChecked(true);
				}
			}
		});

		starttime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = UserSettingsActivity.SHOW_START_TIMEPICK;
				UserSettingsActivity.this.dateandtimeHandler.sendMessage(msg);
				timenum = 0;
			}
		});
		endtime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = UserSettingsActivity.SHOW_END_TIMEPICK;
				UserSettingsActivity.this.dateandtimeHandler.sendMessage(msg);
				timenum = 1;
			}
		});

		user_setting_push_lottery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-push-result", "更多-软设-推送-开奖号码", 1);
				Intent intent = new Intent(UserSettingsActivity.this,UserSettingsActivityPushLottery.class);
				UserSettingsActivity.this.startActivity(intent);
			}
		});
		setPullSettings();
		return view;
	}
	
	/**
	 * 显示推送设置信息
	 */
	private void setPullSettings(){
		checkBox1.setChecked( sakePreference.getBoolean(LotteryForecastCBKey, false) );
		checkBox2.setChecked( sakePreference.getBoolean(LotteryEndTimeCBKey, false) );
		setReceiveTimeSetting( sakePreference.getString(PullTimeCBKey, "all") );
		starttime.setText( sakePreference.getString(StartTimeKey, "09:00") );
		endtime.setText( sakePreference.getString(EndTimeKey, "22:00") );
	}
	
	/**显示时间设置
	 * @return
	 */
	private void setReceiveTimeSetting(String values){
		if(values.equals("all")){
			checkBox3.setChecked(true);
		}else if(values.equals("custom")){
			checkBox4.setChecked(true);
		}else{
			checkBox5.setChecked(true);
		}
	}

	/****
	 * 选号设置界面
	 * @return
	 */
	private View settingNumber() {
		inflater = LayoutInflater.from(this);
		View view = (View) inflater.inflate(R.layout.user_setting_number, null);
		tvPrompt = (TextView) view.findViewById(R.id.user_setting_prompt);
		sakeBox = (CheckBox) view.findViewById(R.id.user_setting_sake_setting);
		songBox = (CheckBox) view.findViewById(R.id.user_setting_song_setting);
		sakeBox.setChecked(sakePreference.getBoolean(IS_SHAKE, true));
		songBox.setChecked(sakePreference.getBoolean(IS_SONG, true));
		sakeBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-pick-vib", "更多-软设-选号-震动", 1);
				Settings.saveSharedPreferences(sakePreference,IS_SHAKE, isChecked);
			}
		});
		songBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				StatService.onEvent(UserSettingsActivity.this, "more-set-pick-sound", "更多-软设-选号-音效", 1);
				Settings.saveSharedPreferences(sakePreference,IS_SONG, isChecked);
			}
		});

		return view;
	}

	/****
	 * 排序设置界面
	 * 
	 * @return
	 */
//	private View settingSort() {
//		inflater = LayoutInflater.from(this);
//		View view = inflater.inflate(R.layout.user_setting_sort, null);
//		tvPrompt = (TextView) view.findViewById(R.id.user_setting_prompt);
//		listView = (DragSortListView) view.findViewById(R.id.user_setting_sort_list);
//
//		tvPrompt.setText("按住右边按钮，滑动到您想放置的位置即可");
//
//		listView.setDropListener(onDrop);
//		listView.setRemoveListener(onRemove);
//
//		list = new ArrayList<JazzArtist>();
//
//		List<String> array = getPursueLotteryId();
//
//		JazzArtist ja;
//		for (String lid : array) {
//			ja = new JazzArtist();
//			ja.lid = lid;
//			ja.name = LotteryId.getLotteryName(lid);
//			ja.icon = LotteryId.getLotteryIcon(lid);
//			list.add(ja);
//		}
//
//		adapter = new JazzAdapter(list);
//		listView.setAdapter(adapter);
//
//		return view;
//	}

	/**排序彩种列表
	 * @return
	 */
	private List<String> getPursueLotteryId() {
		List<String> lotteryArray = new ArrayList<String>();
		String orderstr = sakePreference.getString(LotteryInfoParser.LotteryOrderStrKey, "");
		lotteryArray = LotteryId.getLotteryId(orderstr,LotteryId.getLotteryOpen());
		lotteryArray.remove(0);// 去掉全部
		return lotteryArray;
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			StatService.onEvent(UserSettingsActivity.this, "more-set-list-move", "更多-软设-排序-彩种移动", 1);
			JazzArtist item = adapter.getItem(from);
			adapter.remove(item);
			adapter.insert(item, to);
			LogUtil.DefalutLog(to + " :  " + from + "   :  " + adapter.toString());
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			adapter.remove(adapter.getItem(which));
		}
	};

	private class JazzArtist {
		public String name;
		public String lid;
		public int icon;

		@Override
		public String toString() {
			return name;
		}
	}

	private class ViewHolder {
		public TextView nameView;
		public ImageView iconView;
	}

	/**可拖动的listview adapter
	 * @author Messi
	 */
	private class JazzAdapter extends ArrayAdapter<JazzArtist> {

		public JazzAdapter(ArrayList<JazzArtist> artists) {
			super(UserSettingsActivity.this,
					R.layout.user_setting_sort_list_item,
					R.id.user_setting_sort_text, artists);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			ViewHolder holder;
			if (v != convertView && v != null) {
				holder = new ViewHolder();
				holder.iconView = (ImageView) v.findViewById(R.id.user_setting_sort_icon);
				holder.nameView = (TextView) v.findViewById(R.id.user_setting_sort_text);
				v.setTag(holder);
			}
			holder = (ViewHolder) v.getTag();
			holder.iconView.setBackgroundResource(getItem(position).icon);
			holder.nameView.setText(getItem(position).name);
			return v;
		}
	}

	/***
	 * 左右滑动ViewPager Adapter
	 * @author Jiang
	 */
	public class MyPagerAdapter extends PagerAdapter {

		public ArrayList<View> mListViews;

		public MyPagerAdapter(ArrayList<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/***
	 * 界面左右滑动事件
	 * @author Jiang
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			LogUtil.SystemLog("OnPageChangeListener : " + arg0 + "   ");
			switch (arg0) {
			case 0:
				StatService.onEvent(UserSettingsActivity.this, "more-set-No", "更多-软件设置-选号设置", 1);
				radioNumber.setChecked(true);
				break;
			case 1:
				StatService.onEvent(UserSettingsActivity.this, "more-set-push", "更多-软件设置-推送设置", 1);
				radioPush.setChecked(true);
				break;
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == radioNumber.getId()) {
			scrollScreen.setCurrentItem(0);
			StatService.onEvent(UserSettingsActivity.this, "more-set-No", "更多-软件设置-选号设置", 1);
		} else if (v.getId() == radioPush.getId()) {
			scrollScreen.setCurrentItem(1);
			StatService.onEvent(UserSettingsActivity.this, "more-set-push", "更多-软件设置-推送设置", 1);
		}
	}

	@Override
	public void onBackPressed() {
		/**推送设置保存**/
		savePullSettings();
		finish();
	}
	
	/**
	 * 保存推送设置信息
	 */
	private void savePullSettings(){
		Settings.saveSharedPreferences(sakePreference, LotteryForecastCBKey, checkBox1.isChecked());
		Settings.saveSharedPreferences(sakePreference, LotteryEndTimeCBKey, checkBox2.isChecked());
		Settings.saveSharedPreferences(sakePreference, PullTimeCBKey, getReceiveTimeSetting());
		Settings.saveSharedPreferences(sakePreference, StartTimeKey, starttime.getText().toString());
		Settings.saveSharedPreferences(sakePreference, EndTimeKey, endtime.getText().toString());
	}
	
	/**保存时间设置
	 * @return
	 */
	private String getReceiveTimeSetting(){
		if(checkBox3.isChecked()){
			return "all";
		}else if(checkBox4.isChecked()){
			return "custom";
		}else{
			return "never";
		}
	}

	/**
	 * 更新时间显示
	 */
	private void updateTimeDisplay() {
		if (timenum == 0) {
			starttime.setText(new StringBuilder().append(mHour).append(":").append((mMinute < 10) ? "0" + mMinute : mMinute));
		} else {
			endtime.setText(new StringBuilder().append(mHour).append(":").append((mMinute < 10) ? "0" + mMinute : mMinute));
		}
	}

	/**
	 * 时间控件事件
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTimeDisplay();
		}
	};

	/**
	 * 处理日期和时间控件的Handler
	 */
	Handler dateandtimeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UserSettingsActivity.SHOW_START_TIMEPICK:
				showDialog(1);
			case UserSettingsActivity.SHOW_END_TIMEPICK:
				showDialog(1);
				break;
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case 1:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		}
	}

}