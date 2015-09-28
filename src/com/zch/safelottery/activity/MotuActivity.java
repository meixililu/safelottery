package com.zch.safelottery.activity;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

/**
 * 魔图首页
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年4月24日
 */
public class MotuActivity extends ZCHBaseActivity implements View.OnClickListener {

	/** 传递图片路径的key */
	public static final String PARAM_PATH_KEY = "path";
	/** 从相册中选择的状态码 */
	private static final int REQEUST_CHOOSE_ALBUM = 1;
	/** 拍照的状态码 */
	private static final int REQEUST_CHOOSE_CAMERA = 2;
	
	public static final String IMAGE_PATH = "zch/images/";
	public static final File FILE_SDCARD = Environment.getExternalStorageDirectory();
	public static final File IMAGE_DIR = new File(FILE_SDCARD, IMAGE_PATH);
	

	/** 拍照  */
	private FrameLayout mBtnCamera;
	/** 从相册中选择  */
	private FrameLayout mBtnAlbum;
	/** 底部背景图片  */
	private LinearLayout iv_bottom;

	/** 拍照生成的图片完整路径,使用静态防止垃圾回收  */
	private static String mImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motu_main);
		initViews();
		initEvents();
	}

	private void initViews() {
		mBtnCamera = (FrameLayout) findViewById(R.id.btn_camera);
		mBtnAlbum = (FrameLayout) findViewById(R.id.btn_album);
		iv_bottom = (LinearLayout) findViewById(R.id.iv_bottom);
		Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.motu_main_bottom_bg);
		ImageUtil.adjustImgSize(rawBitmap, iv_bottom);
	}

	private void initEvents() {
		mBtnAlbum.setOnClickListener(this);
		mBtnCamera.setOnClickListener(this);
	}

	/**
	 * 调用相册功能
	 */
	private void doGotoAlbum() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, REQEUST_CHOOSE_ALBUM);
	}

	/**
	 * 调用拍照功能
	 */
	private void doTakePhoto() {

		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				mImagePath = getFilePath();
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File outputFile = new File(mImagePath);
				Uri outputUri = Uri.fromFile(outputFile);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
				startActivityForResult(intent, REQEUST_CHOOSE_CAMERA);
			} catch (ActivityNotFoundException e) {
				LogUtil.ErrorLog(e!=null ? e.getMessage() : "");
				e.printStackTrace();
			}
		}else
			ToastUtil.diaplayMesShort(MotuActivity.this, "请输入SD卡！");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// 从相册选择
			if (requestCode == REQEUST_CHOOSE_ALBUM) {
				if (data != null) {
					Uri uri = data.getData();
					Intent intent = new Intent(MotuActivity.this, MotuUploadActivity.class);
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						// 获取图片
						Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
						if (null == cursor) {
							ToastUtil.diaplayMesShort(MotuActivity.this, "图片没找到");
							return;
						}
						cursor.moveToFirst();
						String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
						intent.putExtra(PARAM_PATH_KEY, path);
						LogUtil.ErrorLog("返回路径:" + path);
					} else {
						intent.putExtra(PARAM_PATH_KEY, uri.getPath());
						LogUtil.ErrorLog("返回路径:" + uri.getPath());
					}
					startActivity(intent);
				}
			} else if (requestCode == REQEUST_CHOOSE_CAMERA) {
				// 拍照
				if (!TextUtils.isEmpty(mImagePath)) {
					LogUtil.ErrorLog("返回拍照路径:" + mImagePath);
					Intent intent = new Intent(MotuActivity.this, MotuUploadActivity.class);
					intent.putExtra(PARAM_PATH_KEY, mImagePath);
					startActivity(intent);
				}
			}
		}
	}

	/**
	 * 生成拍照照片路径
	 * @return 照片路径
	 */
	private String getFilePath() {
		StringBuilder filenameBuilder = new StringBuilder();
		filenameBuilder.append(IMAGE_DIR);
		filenameBuilder.append(System.currentTimeMillis()).append(".png");
		
		return filenameBuilder.toString();
	}


	
	public void onClick(View v) {
		if (v == mBtnAlbum) {
			doGotoAlbum();
		} else if (v == mBtnCamera) {
			doTakePhoto();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImagePath = null;
		System.gc();
	}
	
}
