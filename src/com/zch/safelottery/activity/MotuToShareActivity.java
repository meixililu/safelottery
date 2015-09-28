package com.zch.safelottery.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.SDcardUtil;
import com.zch.safelottery.util.ToastUtil;

public class MotuToShareActivity extends ZCHBaseActivity implements
		OnClickListener {

	private ImageView motu_img;
	private Button share_btn;
	private LinearLayout motu_number_bg_layout;
	private LinearLayout mutu_result_layout, motu_share_root_layout;
	private RelativeLayout motu_share_view, motu_share_menu_layout;
	private TextView motu_number1, motu_number2, motu_number3, motu_number4,
			motu_number5, motu_number6, motu_number7;

	private String imgPath;
	private String number;
	protected SoundPool mSoundPoll;
	private int mSoundId;
	private String filePath;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motu_to_share_activity);
		try {
			initView();
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		Intent intent = getIntent();
		imgPath = intent.getStringExtra(MotuActivity.PARAM_PATH_KEY);
		number = intent.getStringExtra(MotuUploadActivity.NumberKey);

		motu_img = (ImageView) findViewById(R.id.motu_img);
		share_btn = (Button) findViewById(R.id.share_btn);
		motu_number_bg_layout = (LinearLayout) findViewById(R.id.motu_number_bg_layout);
		mutu_result_layout = (LinearLayout) findViewById(R.id.mutu_result_layout);
		motu_share_root_layout = (LinearLayout) findViewById(R.id.motu_share_root_layout);
		motu_share_view = (RelativeLayout) findViewById(R.id.motu_share_view);
		motu_share_menu_layout = (RelativeLayout) findViewById(R.id.motu_share_menu_layout);

		motu_number1 = (TextView) findViewById(R.id.motu_number1);
		motu_number2 = (TextView) findViewById(R.id.motu_number2);
		motu_number3 = (TextView) findViewById(R.id.motu_number3);
		motu_number4 = (TextView) findViewById(R.id.motu_number4);
		motu_number5 = (TextView) findViewById(R.id.motu_number5);
		motu_number6 = (TextView) findViewById(R.id.motu_number6);
		motu_number7 = (TextView) findViewById(R.id.motu_number7);

		Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.motu_to_share_img_to_show);
		ImageUtil.adjustImgSize(rawBitmap, motu_number_bg_layout);

		mSoundPoll = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSoundId = mSoundPoll.load(this, R.raw.camera, 1);

		share_btn.setOnClickListener(this);
	}

	private void initData() throws Exception {
		if (!TextUtils.isEmpty(imgPath)) {
			bitmap = ImageUtil.getImage(imgPath);
			int degree = MotuUploadActivity.readPictureDegree(imgPath);
			bitmap = MotuUploadActivity.setPictureDegreeZero(degree,
					bitmap);
			motu_img.setImageBitmap(bitmap);
		}
		if (!TextUtils.isEmpty(number)) {
			String[] numbers = number.split("#");
			String[] redBalls = numbers[0].split(",");
			motu_number1.setText(redBalls[0]);
			motu_number2.setText(redBalls[1]);
			motu_number3.setText(redBalls[2]);
			motu_number4.setText(redBalls[3]);
			motu_number5.setText(redBalls[4]);
			motu_number6.setText(redBalls[5]);
			motu_number7.setText(numbers[1]);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.share_btn) {
			toSharePage();
		}
	}

	private void toSharePage() {
		showFullScreen();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				try {
					playMusic();
					Bitmap mBitmap = convertViewToBitmap(motu_share_view);
					saveBitmap(mBitmap);
					SafeApplication.dataMap.put("mBitmap", mBitmap);
					SafeApplication.dataMap.put("filePath", filePath);
					Intent intent = new Intent(MotuToShareActivity.this,
							MotuShareActivity.class);
					startActivity(intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 200);

	}

	private void playMusic() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		mSoundPoll.play(mSoundId, volume, volume, 1, 0, 1f);
	}

	private void showFullScreen() {
		motu_number_bg_layout.setBackgroundResource(R.drawable.motu_to_share_img_to_share);
		motu_share_menu_layout.setVisibility(View.GONE);
		motu_share_root_layout.requestLayout();
	}

	public Bitmap convertViewToBitmap(View view) {
		view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		int height = this.getWindowManager().getDefaultDisplay().getHeight();
		// 去掉标题栏
		Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
		view.destroyDrawingCache();

		return b;
	}

	public void saveBitmap(Bitmap bitmap) throws IOException {
		String dir = "/DCIM/";
		String sdcardDir = SDcardUtil.isDirExits(this, dir);
		if(!TextUtils.isEmpty(sdcardDir)){
			filePath = sdcardDir + "image_" + System.currentTimeMillis() + ".png";
			File file = new File(filePath);
			file.createNewFile();
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
					out.flush();
					out.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			ToastUtil.diaplayMesShort(this, "请插入SD卡");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap != null){
			bitmap.recycle();
		}
	}

}
