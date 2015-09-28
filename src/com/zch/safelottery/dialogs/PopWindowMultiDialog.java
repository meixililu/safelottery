package com.zch.safelottery.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.view.ListLineFeedView;

public class PopWindowMultiDialog extends AlertDialog {
	
	private final String TAG = "TAG";
	private final boolean DEBUG = Settings.DEBUG;
	private Context mContext;
	
	private LinearLayout layoutView;
	private LinearLayout lay;
	
	private int mWidth;
	private int mHeight;
	private int init = -1;
	
	/**
	 * @param context 传入当前的Activity
	 */
	public PopWindowMultiDialog(Context context) {
		super(context, R.style.popDialog);
		this.mContext = context;
		lay = new LinearLayout(context);
		lay.setOrientation(LinearLayout.VERTICAL);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.select_popwindow_frequent);
	    
	    layoutView = (LinearLayout) findViewById(R.id.select_popwindow_list);
	    mWidth = LayoutParams.FILL_PARENT;
	    mHeight = LayoutParams.WRAP_CONTENT;
	}
	
	public void content(String title, ListLineFeedView feedView){
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_pop_window_multi, null);
		TextView tv = (TextView) view.findViewById(R.id.dialog_pop_multi_title);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.dialog_pop_content);
		
		tv.setText(title);
		ll.addView(feedView.getView());
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		lay.addView(view);
//		ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	
	public void remove(){
		layoutView.removeAllViews();
	}
	
	public void addContent(){
		layoutView.addView(lay);
	}
	
	public void setPopViewPosition(){
		setPopViewPosition(0, ScreenUtil.dip2px(mContext,35));
	}
	public void setPopViewPosition(int x, int y){
		Window win = this.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP|Gravity.LEFT;
		params.x = x + ScreenUtil.dip2px(mContext,6);
		params.y = y + ScreenUtil.dip2px(mContext,6);
		win.setAttributes(params);
		if(DEBUG) Log.i("TAG", "点击 setPopViewPosition" + params.width);
		this.setCanceledOnTouchOutside(true);
	}
	
	public void setWindowSize(int width, int height){
		this.getWindow().setLayout(width, height);
	}
	
	public void show(){
		super.show();
		setWindowSize(mWidth, mHeight);
	}
	//调用的写法
//	popDialogf = new PopDialogFrequent(P5Activity.this, new String[]{"哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈"}, R.style.popDialog);
}
