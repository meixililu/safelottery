package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.util.ScreenUtil;

public class PopDialog extends Dialog {
	
	private PopViewItemOnclickListener listener;
	private Context context;
	private String[] tempText;
	
	private int[] textGone;
	
	private TextView text1, text2, text3, text4;
	public void setListener(PopViewItemOnclickListener listener) {
		this.listener = listener;
	}

	public PopDialog(Context context, int theme) {
	    super(context, theme);
	    this.context = context;
	}

	public PopDialog(Context context) {
	    this(context, R.style.popDialog);
	}

	/**
	 * 更改TextView的提示内容
	 * @param context
	 * @param theme
	 * @param tempText
	 */
	public PopDialog(Context context,  int theme, String[] tempText) {
		super(context, theme);
		this.context = context;
		this.tempText = tempText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.select_popwindow);
	    text1 = (TextView) findViewById(R.id.select_popwindow_text1);
	    text2 = (TextView) findViewById(R.id.select_popwindow_text2);
	    text3 = (TextView) findViewById(R.id.select_popwindow_text3);
	    text4 = (TextView) findViewById(R.id.select_popwindow_text4);
		text1.setOnClickListener(onClickListener);
		text2.setOnClickListener(onClickListener);
		text3.setOnClickListener(onClickListener);
		text4.setOnClickListener(onClickListener);
		
		if(tempText != null){
			text1.setText(tempText[0]);
			text2.setText(tempText[1]);
			text3.setText(tempText[2]);
			text4.setText(tempText[3]);
		}
	}
	
	public void textVisibility(int[] textGone){
		this.textGone = textGone;
	}
	private void textVisibility(int index){
		if(index == 1) text1.setVisibility(View.GONE);
		else if(index == 2) text2.setVisibility(View.GONE);
		else if(index == 3) text3.setVisibility(View.GONE);
		else if(index == 4) text4.setVisibility(View.GONE);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.select_popwindow_text1:
				 if(listener != null){
					 listener.onFirstClick(v);
				 }
				 PopDialog.this.dismiss();
				 break;
			case R.id.select_popwindow_text2:
				 if(listener != null){
					 listener.onSecondClick(v);
				 }					
				 PopDialog.this.dismiss();
				 break;
			case R.id.select_popwindow_text3:
				 if(listener != null){
					 listener.onThirdClick(v);
				 }			
				 PopDialog.this.dismiss();
				 break;
			case R.id.select_popwindow_text4:
				 if(listener != null){
					 listener.onFourthClick(v);
				 }		
				 PopDialog.this.dismiss();
				 break;
			}
		}
	};
	
	public void setPopViewPosition(){
		Window win = this.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP|Gravity.RIGHT;
		params.x = ScreenUtil.dip2px(context,14f);
		params.y = ScreenUtil.dip2px(context,34f);
		win.setAttributes(params);
		this.setCanceledOnTouchOutside(true);
	}
	
	public interface PopViewItemOnclickListener{
		public void onFirstClick(View v);
		public void onSecondClick(View v);
		public void onThirdClick(View v);
		public void onFourthClick(View v);
	}
	
	public void show(){
		super.show();
		if(textGone != null)
			for(int index : textGone)
				textVisibility(index);
	}
}
