package com.zch.safelottery.activity;

import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.http.JsonHttpResponseHandler;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class MotuUploadActivity extends ZCHBaseActivity implements OnClickListener {

	/** 传递双色球号码的key */
	public static final String NumberKey = "NumberKey";
	public static final String Uri = "Uri";
	
	private ImageView motu_img;
	private FrameLayout motu_submit;
	private ProgressDialog progressDialog;
	private String imgPath;
	private Bitmap bitmap;
	private String uri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motu_upload_activity);
		try {
			initView();
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView(){
		Intent intent = getIntent();
		imgPath = intent.getStringExtra(MotuActivity.PARAM_PATH_KEY);
		
		motu_img = (ImageView) findViewById(R.id.motu_img);
		motu_submit = (FrameLayout) findViewById(R.id.motu_submit);
		motu_submit.setOnClickListener(this);
		
	}

	private void initData() throws Exception{
		if(!TextUtils.isEmpty(imgPath)){
			bitmap = ImageUtil.getImage(imgPath);
			int degree = readPictureDegree(imgPath);
			bitmap = setPictureDegreeZero(degree, bitmap);
			motu_img.setImageBitmap(bitmap);
		}
	}
	
	/**
	 * 获取图片的旋转角度
	 * @param path 图片路径
	 * @return 图片角度
	 */

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;

	}

	/**
	 * 将图片的旋转角度置为0
	 * 
	 * @param angle 角度
	 * @param bitmap 图片
	 * @return 新的bitmap
	 */
	public static Bitmap setPictureDegreeZero(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.motu_submit){
			uploadImgTask();
		}
	}
	
	private void uploadImgTask(){
		progressDialog = ProgressDialog.show(this, "", "获取幸运号码 0%", true, false);
		SafelotteryHttpClient.uploadImg(GetString.UploadImgServer + "/gen_code", imgPath, new JsonHttpResponseHandler(){

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				if(totalSize > 0){
					progressDialog.setMessage("获取幸运号码 "+ (int)( (double)bytesWritten/totalSize*100 ) +"%");
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				try {
					LogUtil.DefalutLog("response:"+response.toString());
					if(response != null){
						if(response.has("uri")){
							uri = response.getString("uri");
						}
						if(response.has("code")){
							String code = response.getString("code");
							Intent intent = new Intent(MotuUploadActivity.this, MotuNumberActivity.class);
							intent.putExtra(NumberKey, code);
							intent.putExtra(Uri, uri);
							intent.putExtra(MotuActivity.PARAM_PATH_KEY, imgPath);
							startActivity(intent);
							finish();
						}
					}else{
						ToastUtil.diaplayMesLong(MotuUploadActivity.this, "获取幸运号码失败，请重试");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				LogUtil.DefalutLog("uploadImg:onFailure");
				ToastUtil.diaplayMesLong(MotuUploadActivity.this, "网络连接错误，请重试");
			}

			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
			
		});
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap != null){
			bitmap.recycle();
		}
	}
	
}
