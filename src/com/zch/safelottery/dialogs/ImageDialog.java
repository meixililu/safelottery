package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.BannerBean;
import com.zch.safelottery.setttings.SystemInfo;

/**
 * 自定义退出dialog
 * 
 * @author Messi
 * 
 */
public class ImageDialog extends Dialog {

	private Context context;

	private ImageView image;
	
	private Bitmap mBitmap;
	
	private BannerBean loadBean;
	
	public ImageDialog(Context mContext,BannerBean bean,Bitmap draw) {
		super(mContext, R.style.dialog);
		context = mContext;
		loadBean = bean;
		mBitmap = draw;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_image);
		image = (ImageView) findViewById(R.id.dialog_image);
		if(mBitmap != null){
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(mBitmap.getWidth() < SystemInfo.width ? mBitmap.getWidth(): SystemInfo.width, mBitmap.getHeight() < SystemInfo.height? mBitmap.getHeight(): SystemInfo.height);
			image.setLayoutParams(lParams);
			Drawable drawable = new BitmapDrawable(mBitmap); 
			image.setImageDrawable(drawable);
		}else{
			this.dismiss();
		}
	}
	
}
