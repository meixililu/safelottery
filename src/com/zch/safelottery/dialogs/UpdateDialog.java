package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.util.ToastUtil;

/**自定义退出dialog
 * @author Messi
 *
 */
public class UpdateDialog extends Dialog {
	
	private Button background_btn,download_cancle_btn;
	private TextView content_tv,browser_tv;
	private String content;
	private String ok_btn_text;
	private boolean isHideCancleBtn;

	private OnDownLoadDialogClickListener buttonOnClickListener;

	public UpdateDialog(Context context) {
		super(context, R.style.dialog);
	}
	
	public UpdateDialog(Context context, String content) {
		super(context, R.style.dialog);
		this.content = content;
	}
	
	public UpdateDialog(Context context,String title,String content) {
		super(context, R.style.dialog);
		this.content = content;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog_download);
	    init();
	}
	public void init(){
		background_btn = (Button) findViewById(R.id.download_background);
		download_cancle_btn = (Button) findViewById(R.id.download_cancle_btn);
		browser_tv = (TextView) findViewById(R.id.download_browser);
		content_tv = (TextView) findViewById(R.id.normal_dialog_text);
		
		if(!TextUtils.isEmpty(content)){
			content_tv.setText(content);
		}
		
		if(!TextUtils.isEmpty(ok_btn_text)){
			background_btn.setText(ok_btn_text);
		}
		
		if(isHideCancleBtn){
			download_cancle_btn.setVisibility(View.GONE);
		}
		
		background_btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				UpdateDialog.this.dismiss();
				if(buttonOnClickListener != null){
					ToastUtil.diaplayMesShort(getContext(), "开始更新");
					buttonOnClickListener.onBackgroundBtnClick();
				}
			}
		});
		
		browser_tv.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				UpdateDialog.this.dismiss();
				if(buttonOnClickListener != null){
					buttonOnClickListener.onBrowserBtnClick();
				}
			}
		});
		
		download_cancle_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateDialog.this.dismiss();
				if(buttonOnClickListener != null){
					buttonOnClickListener.onCancleBtnClick();
				}
			}
		});
	}
	
	public interface OnDownLoadDialogClickListener {
		public void onBackgroundBtnClick();
		public void onBrowserBtnClick();
		public void onCancleBtnClick();
	}
	
	public void setDownLoadDialogClickListener(OnDownLoadDialogClickListener buttonOnClickListener) {
		this.buttonOnClickListener = buttonOnClickListener;
	}
	
	public void setCancleBtnVisibility(boolean isHide){
		isHideCancleBtn = isHide;
	}
	
	public void setOk_btn_text(String ok_btn_text) {
		this.ok_btn_text = ok_btn_text;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
