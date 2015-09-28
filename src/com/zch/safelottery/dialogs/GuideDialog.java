package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zch.safelottery.R;
import com.zch.safelottery.util.ScreenUtil;

/**
 * 自定义退出dialog
 * 
 * @author Messi
 * 
 */
public class GuideDialog extends Dialog {

	private Context context;

	private ImageView image;
	
	private int resourceId;
	
	private GuideDialogListener mGuideDialogListener;
	
	public GuideDialog(Context mContext,int sourceId) {
		super(mContext, R.style.mydialog);
		context = mContext;
		resourceId = sourceId;
	}
	
	public GuideDialog(Context mContext,int style,int sourceId) {
		super(mContext, style);
		context = mContext;
		resourceId = sourceId;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_image);
		image = (ImageView) findViewById(R.id.dialog_image);
		image.setBackgroundResource(resourceId);
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GuideDialog.this.dismiss();
			}
		});
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		if(mGuideDialogListener != null){
			mGuideDialogListener.onDialogDismiss();
		}
	}

	/**布局位置设定
	 * @param gravity1 水平位置
	 * @param gravity2 纵向位置
	 * @param x x轴偏移
	 * @param y y轴偏移
	 */
	public void setPopViewPosition(int gravity1, int gravity2, int x, int y){
		Window win = this.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		if(gravity1 > 0 && gravity2 > 0){
			params.gravity = gravity1|gravity2;//Gravity.TOP|Gravity.CENTER;
		}else{
			params.gravity = gravity1;
		}
		params.x = ScreenUtil.dip2px(context,x);
		params.y = ScreenUtil.dip2px(context,y);
		win.setAttributes(params);
		this.setCanceledOnTouchOutside(true);
	}
	
	public interface GuideDialogListener{
		public void onDialogDismiss();
	}

	public void setmGuideDialogListener(GuideDialogListener mGuideDialogListener) {
		this.mGuideDialogListener = mGuideDialogListener;
	}
	
}
