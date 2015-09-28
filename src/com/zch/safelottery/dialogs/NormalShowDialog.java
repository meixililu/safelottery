package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;

/**
 * 自定义退出dialog
 * 
 * @author Messi
 * 
 */
public class NormalShowDialog extends Dialog {

	private View contentView;

	private Dialog dialog;
	private Button ok_btn, cancle_btn;
	private LinearLayout contentLayout;

	private TextView title_tv;
	private String title;
	private Context context;
	private String ok_btn_text, cancle_btn_text;
	private int type;// 默认两个按钮，1为一个按钮

	private OnShowOnClickListener buttonOnClickListener;

	public NormalShowDialog(Context context) {
		super(context);
		this.context = context;
	}

	public NormalShowDialog(Context context, String title, View content) {
		super(context);
		this.title = title;
		this.contentView = content;
		this.context = context;
	}

	public void show() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_style_show, null);
		dialog = new Dialog(context, R.style.dialog);
		ok_btn = (Button) view.findViewById(R.id.normal_dialog_sure);
		cancle_btn = (Button) view.findViewById(R.id.normal_dialog_cancel);
		contentLayout = (LinearLayout) view
				.findViewById(R.id.dialog_style_show_layout);
		title_tv = (TextView) view.findViewById(R.id.normal_dialog_title);

		if (type == 1) {
			cancle_btn.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(title)) {
			title_tv.setText(title);
		}

		if (contentView != null) {
			contentLayout.addView(contentView);
		}

		if (!TextUtils.isEmpty(ok_btn_text)) {
			ok_btn.setText(ok_btn_text);
		}

		if (!TextUtils.isEmpty(cancle_btn_text)) {
			cancle_btn.setText(cancle_btn_text);
		}

		ok_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (buttonOnClickListener != null) {
					buttonOnClickListener.onOkBtnClick();
				}
			}
		});

		cancle_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (buttonOnClickListener != null) {
					buttonOnClickListener.onCancleBtnClick();
				}
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}

	public interface OnShowOnClickListener {
		public void onOkBtnClick();

		public void onCancleBtnClick();
	}

	public void setOnShowClickListener(
			OnShowOnClickListener buttonOnClickListener) {
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

	public void addView(View content) {
		this.contentView = content;
	}

	public void setType(int type) {
		this.type = type;
	}

}
