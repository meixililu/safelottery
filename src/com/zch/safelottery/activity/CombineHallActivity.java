package com.zch.safelottery.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.combine.CombineLottery;
import com.zch.safelottery.dialogs.PopWindowDialog;
import com.zch.safelottery.dialogs.PopWindowDialog.OnclickFrequentListener;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;

public class CombineHallActivity extends BaseTabActivity implements OnClickListener{

	private LinearLayout layout;
	private CombineLottery combineLottery;
	
	private FrameLayout filterListenerFrame;
	private TextView filterNameTv;
	private TextView filterBtnNameTv;
	private ImageView filterBtnIconImage;
	
	private String lotteryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_combine_hall);
		
		initView();
	}
	
	private void initView(){
		filterListenerFrame = (FrameLayout) findViewById(R.id.title_filtrate_listener_frame);
		filterNameTv = (TextView) findViewById(R.id.title_filtrate_name);
		filterBtnNameTv = (TextView) findViewById(R.id.title_filtrate_btn_name);
		filterBtnIconImage = (ImageView) findViewById(R.id.title_filtrate_btn_icon);
		layout = (LinearLayout) findViewById(R.id.main_combine_hall_layout);
		
		filterListenerFrame.setOnClickListener(this);
		
		filterNameTv.setText("合买大厅");
		filterBtnIconImage.setImageResource(R.drawable.y11_title_button);
		filterBtnIconImage.setVisibility(View.VISIBLE);
		filterBtnNameTv.setVisibility(View.VISIBLE);
		
		Bundle mBundle = getIntent().getBundleExtra(Settings.BUNDLE);
		if(mBundle != null) //数据接收
			lotteryId = mBundle.getString(LotteryId.INTENT_LID);
		if(lotteryId == null) // 彩种不能为null
			lotteryId = LotteryId.All;
		
		filterBtnNameTv.setText(LotteryId.getLotteryName(lotteryId).equals(LotteryId.getLotteryName(LotteryId.All)) ? "全部彩种" :  LotteryId.getLotteryName(lotteryId));
		
		LogUtil.CustomLog("TAG", "lotteryId :: " + lotteryId);
		addView(lotteryId);
	}
	
	public void addView(String lotteryId){
		this.lotteryId = lotteryId;
		combineLottery = new CombineLottery(this, lotteryId);
		View view = combineLottery.getCombineView();
		layout.addView(view);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.title_filtrate_listener_frame:
			stateCombine();
			
			break;
		}
	}

	private PopWindowDialog mDialogType;
	/**
	 * 筛选Dialog
	 */
	private void stateCombine(){
		if (mDialogType == null) {
			//敢得当前控件所在的位置
			int popDialogY = filterListenerFrame.getTop() + filterListenerFrame.getHeight();
			
			String orderstr = this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE).getString(LotteryInfoParser.LotteryOrderStrKey, "");
			final List<String> temp = LotteryId.getPursueLotteryName(orderstr, LotteryId.LOTTERY_TYPE_GP);
			for(int i = 0, size = temp.size(); i < size; i++){
				if(temp.get(i).equals(LotteryId.getLotteryName(LotteryId.All))){
					temp.remove(i);
					temp.add(i, "全部彩种");
				}
			}
			
			String lidName = LotteryId.getLotteryName(lotteryId);
			mDialogType = new PopWindowDialog(this, temp, temp.indexOf(lidName.equals("全部") ? "全部彩种" : lidName));
			mDialogType.setPopViewPosition(0, popDialogY);
			mDialogType.setOnClickListener(new OnclickFrequentListener() {
				@Override
				public void onClick(View v) {
					final ArrayList<CheckBox> list = mDialogType.getList();
					CheckBox checkBox;

					for (int i = 0, size = list.size(); i < size; i++) {
						checkBox = list.get(i);
						if (i == v.getId()) {
							String t = temp.get(i);
							checkBox.setChecked(true);
							lotteryId = LotteryId.getLotteryLid(t);
							combineLottery.setLid(lotteryId);
							filterBtnNameTv.setText(t);
							
							if(lotteryId.equals(LotteryId.RX9)){
								StatService.onEvent(CombineHallActivity.this, "join-renxuan9", "合买大厅-右上-任选九", 1);
							}else if(lotteryId.equals(LotteryId.SFC)){
								StatService.onEvent(CombineHallActivity.this, "join-shengfucai", "合买大厅-右上-胜负彩", 1);
							}else if(lotteryId.equals(LotteryId.JCLQ)){
								StatService.onEvent(CombineHallActivity.this, "join-basketball", "合买大厅-右上-竞篮", 1);
							}else if(lotteryId.equals(LotteryId.DLT)){
								StatService.onEvent(CombineHallActivity.this, "join-dlt", "合买大厅-右上-大乐透", 1);
							}else if(lotteryId.equals(LotteryId.QXC)){
								StatService.onEvent(CombineHallActivity.this, "join-7xing", "合买大厅-右上-七星彩", 1);
							}else if(lotteryId.equals(LotteryId.PL5)){
								StatService.onEvent(CombineHallActivity.this, "join-pai5", "合买大厅-右上-排列五", 1);
							}else if(lotteryId.equals(LotteryId.PL3)){
								StatService.onEvent(CombineHallActivity.this, "join-pai3", "合买大厅-右上-排列三", 1);
							}else if(lotteryId.equals(LotteryId.QLC)){
								StatService.onEvent(CombineHallActivity.this, "join-7le", "合买大厅-右上-七乐彩", 1);
							}else if(lotteryId.equals(LotteryId.FC)){
								StatService.onEvent(CombineHallActivity.this, "join-3D", "合买大厅-右上-福彩3D", 1);
							}else if(lotteryId.equals(LotteryId.SSQ)){
								StatService.onEvent(CombineHallActivity.this, "join-ssq", "合买大厅-右上-双色球", 1);
							}else if(lotteryId.equals(LotteryId.JCZQ)){
								StatService.onEvent(CombineHallActivity.this, "join-soccer", "合买大厅-右上-竞足", 1);
							}else if(lotteryId.equals(LotteryId.All)){
								StatService.onEvent(CombineHallActivity.this, "join-select-all", "合买大厅-右上-全部彩种", 1);
							}
						} else {
							if (checkBox.isChecked())
								checkBox.setChecked(false);
						}
					}
					mDialogType.dismiss();
				}
			});

			mDialogType.show();
		} else {
			if(mDialogType.isShowing())
				mDialogType.dismiss();
			else
				mDialogType.show();
		}
	}
	
}
