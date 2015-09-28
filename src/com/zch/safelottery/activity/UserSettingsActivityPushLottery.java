package com.zch.safelottery.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LotteryId;

public class UserSettingsActivityPushLottery extends ZCHBaseActivity {
	
	public final static String SelectedLotteryListKey = "SelectedLotteryListKey";
	public final static String DefaultSelectedLotteryIdList = "001#113#002#004#109#108#110#300";
	private Button selectall, noselectall;
	private ArrayList<Itembean> list;
	private ListView listView;
	private JazzAdapter adapter;
	private String lotteryStr;
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_setting_push_lottery);
		initData();
		initViews();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		list = new ArrayList<Itembean>();
		String[] array = DefaultSelectedLotteryIdList.split("#");
		for (String str : array) {
			Itembean itembean = new Itembean();
			itembean.setNameid(str);
			itembean.setIfselect(false);
			list.add(itembean);
		}
		spf = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		String selectedLotteryList = spf.getString(SelectedLotteryListKey, "");
		if (TextUtils.isEmpty(selectedLotteryList)) {
			for(Itembean itembean : list){
				if(itembean.getNameid().equals(LotteryId.SSQ) || itembean.getNameid().equals(LotteryId.DLT)){
					itembean.setIfselect(true);
				}
			}
		}else if(selectedLotteryList.equals("none")){
			for(Itembean itembean : list){
				itembean.setIfselect(false);
			}
		}else{
			String[] selecteds = selectedLotteryList.split("#");
			int len = selecteds.length;
			if(len > 0){
				for(String lid : selecteds){
					for(Itembean bean : list){
						if(bean.getNameid().equals(lid)){
							bean.setIfselect(true);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 初始化控件及事件
	 */
	private void initViews(){
		selectall = (Button) findViewById(R.id.selectall);
		noselectall = (Button) findViewById(R.id.noselectall);
		listView = (ListView) findViewById(R.id.user_setting_push_list);
		adapter = new JazzAdapter(list);
		listView.setAdapter(adapter);
		selectall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Itembean bean : list) {
					bean.setIfselect(true);
				}
				adapter.notifyDataSetChanged();
			}
		});

		noselectall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Itembean bean : list) {
					bean.setIfselect(false);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		save();
	}

	private void save(){
		StringBuilder sb = new StringBuilder();
		for (Itembean bean : list) {
			if(bean.ifselect){
				sb.append("#");
				sb.append(bean.getNameid());
			}
		}
		if(sb.length() > 0){
			sb.deleteCharAt(0);
		}
		String resultStr = sb.toString();
		if( TextUtils.isEmpty(resultStr) ){
			resultStr = "none";
		}
		Settings.saveSharedPreferences(spf,SelectedLotteryListKey,resultStr);
	}

	class ViewHolder {
		public CheckBox box;
	}

	private class JazzAdapter extends ArrayAdapter<Itembean> {
		
		public JazzAdapter(ArrayList<Itembean> artists) {
			super(UserSettingsActivityPushLottery.this, R.layout.user_setting_push_lottery_items, R.id.checkBox, artists);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			ViewHolder holder;
			if (v != convertView && v != null) {
				holder = new ViewHolder();
				holder.box = (CheckBox) v.findViewById(R.id.checkBox);
				v.setTag(holder);
			}
			holder = (ViewHolder) v.getTag();
			Itembean b = getItem(position);
			String lotteryName = LotteryId.getLotteryName(b.getNameid());
			if(b.getNameid().equals(LotteryId.SFC)){
				lotteryName = "胜负彩、任选九";
			}
			holder.box.setText(lotteryName);
			holder.box.setChecked(b.isIfselect());
			holder.box.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getItem(position).setIfselect(((CompoundButton) v).isChecked());
				}
			});
			return v;
		}
	}
	
	private class Itembean {
		public String nameid;
		public boolean ifselect;

		public boolean isIfselect() {
			return ifselect;
		}

		public void setIfselect(boolean ifselect) {
			this.ifselect = ifselect;
		}

		public String getNameid() {
			return nameid;
		}

		public void setNameid(String nameid) {
			this.nameid = nameid;
		}

	}
}
