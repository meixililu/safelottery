package com.zch.safelottery.dialogs;

import java.text.DecimalFormat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.view.TextSwitchViewGroup;

/**自定义退出dialog
 * @author Messi
 *
 */
public class CaiDanDialog extends Dialog {
	
	/** 切换持续时间，单位：毫秒  */
	private final long DURING_MILLIS = 5000;
	private LinearLayout caidan_dialog;
	private TextView caidan_text;
	private TextView caidan_number1,caidan_number2,caidan_number3,caidan_number4,caidan_number5;
	private String eggAmount;
	private String eggStoryNew;

	private OnCaiDanDialogClickListener buttonOnClickListener;

	public CaiDanDialog(Context context) {
		super(context, R.style.dialog);
	}
	
	public CaiDanDialog(Context context, String eggAmount, String eggStoryNew) {
		super(context, R.style.dialog);
		this.eggAmount = eggAmount;
		this.eggStoryNew = eggStoryNew;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    try {
			setContentView(R.layout.dialog_caidan);
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void init() throws Exception{
		caidan_dialog = (LinearLayout) findViewById(R.id.caidan_dialog);
		caidan_text = (TextView) findViewById(R.id.caidan_text);
		caidan_number1 = (TextView) findViewById(R.id.caidan_number1);
		caidan_number2 = (TextView) findViewById(R.id.caidan_number2);
		caidan_number3 = (TextView) findViewById(R.id.caidan_number3);
		caidan_number4 = (TextView) findViewById(R.id.caidan_number4);
		caidan_number5 = (TextView) findViewById(R.id.caidan_number5);
		if(!TextUtils.isEmpty(eggStoryNew)){
			caidan_text.setText(eggStoryNew);
		}
		caidan_dialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CaiDanDialog.this.dismiss();
			}
		});
		String[] amountStrs = getEggAmount(eggAmount);
		TextSwitchViewGroup tsvg = new TextSwitchViewGroup(new TextView[]{caidan_number1,caidan_number2,
				caidan_number3,caidan_number4,caidan_number5}, amountStrs);
		tsvg.start();
		closeAfterFiveSecond();
	}
	
	/**
	 * close the dialog after five second
	 */
	private void closeAfterFiveSecond(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				CaiDanDialog.this.dismiss();
			}
		}, DURING_MILLIS);
	}
	
	/**change the money to five number
	 * @param eggAmount
	 * @return
	 */
	private String[] getEggAmount(String eggAmount){
		String[] result = new String[5];
		double amount = ConversionUtil.StringToDouble(eggAmount);
		String amountStr = new DecimalFormat("000.00").format(amount);
		amountStr = amountStr.replace(".", "");
		char[] amountStrs = amountStr.toCharArray();
		for(int i=0,j=5; i<j; i++){
			result[i] = String.valueOf(amountStrs[i]);
		}
		LogUtil.DefalutLog("amountStr:"+amountStr+"---"+amountStrs.length);
		return result;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		LogUtil.DefalutLog("---------CaiDanDialog------------onStop");
		if(buttonOnClickListener != null){
			buttonOnClickListener.onClick();
		}
	}

	public interface OnCaiDanDialogClickListener {
		public void onClick();
	}
	
	public void setOnCaiDanDialogClickListener(OnCaiDanDialogClickListener buttonOnClickListener) {
		this.buttonOnClickListener = buttonOnClickListener;
	}
	
	public void setContent(String content) {
		this.eggStoryNew = content;
	}
}
