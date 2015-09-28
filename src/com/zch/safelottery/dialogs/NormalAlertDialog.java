package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zch.safelottery.R;

/**自定义退出dialog
 * @author Messi
 *
 */
public class NormalAlertDialog{
	
	private Dialog dialog;
	private Button ok_btn,cancle_btn;
	private TextView title_tv,content_tv;
	private String title,content;
	private Context context;
	private String ok_btn_text,cancle_btn_text;
	private int type;//默认两个按钮，1为一个按钮

	private OnButtonOnClickListener buttonOnClickListener;

	public NormalAlertDialog(Context context) {
		this.context = context;
	}
	
	public NormalAlertDialog(Context context, String content) {
		this.content = content;
		this.context = context;
	}
	
	public NormalAlertDialog(Context context,String title,String content) {
		this.title = title;
		this.content = content;
		this.context = context;
	}
	
	public void show(){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_style_normal, null);
		dialog = new Dialog(context,R.style.dialog);
		ok_btn = (Button) view.findViewById(R.id.normal_dialog_sure);
		cancle_btn = (Button) view.findViewById(R.id.normal_dialog_cancel);
		content_tv = (TextView) view.findViewById(R.id.normal_dialog_text);
		title_tv = (TextView) view.findViewById(R.id.normal_dialog_title);
		
		if(type == 1){
			cancle_btn.setVisibility(View.GONE);
		}
		
		if(title != null){
			if(!title.equals("")){
				title_tv.setText(title);
			}
		}
		
		if(content != null){
			if(!content.equals("")){
				content_tv.setText(content);
			}
		}
		
		if(ok_btn_text != null){
			if(!ok_btn_text.equals("")){
				ok_btn.setText(ok_btn_text);
			}
		}
		
		if(cancle_btn_text != null){
			if(!cancle_btn_text.equals("")){
				cancle_btn.setText(cancle_btn_text);
			}
		}
		
		ok_btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(buttonOnClickListener != null){
					buttonOnClickListener.onOkBtnClick();
				}
			}
		});
		
		cancle_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(buttonOnClickListener != null){
					buttonOnClickListener.onCancleBtnClick();
				}
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}
	
	public interface OnButtonOnClickListener {
		public void onOkBtnClick();
		public void onCancleBtnClick();
	}
	
	public void setButtonOnClickListener(OnButtonOnClickListener buttonOnClickListener) {
		this.buttonOnClickListener = buttonOnClickListener;
	}
	
	public void setOk_btn_text(String ok_btn_text) {
		this.ok_btn_text = ok_btn_text;
	}

	public void setCancle_btn_text(String cancle_btn_text) {
		this.cancle_btn_text = cancle_btn_text;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setCancelable(boolean isCancle){
		if(dialog != null){
			dialog.setCancelable(isCancle);
		}
	}

}
