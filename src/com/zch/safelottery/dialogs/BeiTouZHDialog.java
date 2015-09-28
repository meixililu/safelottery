package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.view.NumericWheelAdapter;
import com.zch.safelottery.view.OnWheelChangedListener;
import com.zch.safelottery.view.WheelView;

public class BeiTouZHDialog extends Dialog {
	public static final int INIT_BEITOU = 0;
	public static final int INIT_PURSUE = 1;
	public static final int Max = 999;
	
	public BbeiTouZHDialogListener listener;
	private Context context;
	private int value = 1;
	private int max = 1;
	private int init;
	
	private WheelView beitou;
	
	/**
	 * @param context
	 * @param value 当前值
	 * @param max 最大值
	 * @param init 当前类型 INIT_BEITOU 倍投  INIT_PURSUE 追号
	 */
	public BeiTouZHDialog(Context context, int value, int max, int init) {
		super(context, R.style.dialog);
	    this.context = context;
	    this.value = value;
	    this.max = max;
	    this.init = init;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog_beitou_select);
	    beitou = (WheelView) findViewById(R.id.beitou);
	    final CheckBox stop_pursue_on_reward = (CheckBox) findViewById(R.id.stop_pursue_on_reward);
		Button ok = (Button) findViewById(R.id.btn_sure);
		Button cancel = (Button) findViewById(R.id.btn_cancel);
		
		TextView title = (TextView) findViewById(R.id.select_beitou_title);
		TextView name = (TextView) findViewById(R.id.select_beitou_text);

		
		if(init  == INIT_BEITOU){
			stop_pursue_on_reward.setVisibility(View.GONE);
			title.setText("倍投设置");
			name.setText("倍投");
			beitou.setAdapter(new NumericWheelAdapter(1, max, "倍"));// 设置"倍投"的显示数据
		}else if(init == INIT_PURSUE){
			stop_pursue_on_reward.setVisibility(View.VISIBLE);
			title.setText("追号设置");
			name.setText("追号");
			beitou.setAdapter(new NumericWheelAdapter(0, max, "期"));// 设置"追号"的显示数据
		}
			
		beitou.setCurrentItem(value - 1);// 初始化时显示的数据
		beitou.setCyclic(true);// 可循环滚动

		WheelView.ITEMS_TEXT_COLOR = context.getResources().getColor(R.color.text_dark);
		WheelView.VALUE_TEXT_COLOR = context.getResources().getColor(R.color.text_dark);

		OnWheelChangedListener wheelListener_beitou = new OnWheelChangedListener() {

			public void onChanged(WheelView wheel, int oldValue, int newValue) {

				value = newValue + 1;
			}
		};
		
		stop_pursue_on_reward.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				StatService.onEvent(context, "stop after win", "中奖后停追", 1);
				if(isChecked){
					if(value < 2){
						buttonView.setChecked(false);
						ToastUtil.diaplayMesShort(context, "追号期次不能小于一期");
					}
				}
			}
		});

		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogUtil.DefalutLog("BeiTouZHDialog-value:"+value);
				if(stop_pursue_on_reward.isChecked() && value < 2){
					stop_pursue_on_reward.setChecked(false);
					ToastUtil.diaplayMesShort(context, "追号期次不能小于一期");
				}else{
					if(listener != null){
						listener.onOkBtnClick(v, value, stop_pursue_on_reward.isChecked());
					}
					BeiTouZHDialog.this.dismiss();
					
				}
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				BeiTouZHDialog.this.dismiss();
			}
		});
		
		beitou.addChangingListener(wheelListener_beitou);
	}
	
	/**
	 * 重新调用Dialog 设置初始信息
	 * @param beitoushu
	 * @param max
	 */
	public void setBeiTouZH(int beitoushu, int max){
		beitou.setCurrentItem(beitoushu - 1);
		if(init == INIT_PURSUE) beitou.setAdapter(new NumericWheelAdapter(0, max, "期"));// 设置"追号"的显示数据
	}
	
	public interface BbeiTouZHDialogListener{
		public void onOkBtnClick(View v,int value,boolean isStopPursue);
	}
}
