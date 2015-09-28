package com.zch.safelottery.view;

import java.util.Random;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.TextView;

/**
 * 数据切换view,类似数字抽奖
 *
 * @Company 北京中彩汇网络科技有限公司</br>
 * @author 陈振国</br>
 * @version 1.0.0</br>
 * @create 2014年4月2日</br>
 * 
 * 调用示例：</br>
 * <p>TextSwitchView tsv = new TextSwitchView(textview,"5");</p>
 * <p>tsv.startSwitch();</p>
 * <p>tsv.stopSwitch();</p>
 */
public class TextSwitchView implements Runnable {

	public static final int ANIMATION_INTERVAL = 30;

	private TextView mTv;
	private boolean mIsRunning = true;
	private String mResult;

	/**
	 * @param mTv　用于切换数字的TextView对象
	 * @param result 结束后应该显示的结果数字
	 */
	public TextSwitchView(TextView mTv, String result) {
		super();
		this.mTv = mTv;
		this.mResult = result;
	}

	/**
	 *开始变换数字
	 */
	public void startSwitch() {
		mIsRunning = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (mIsRunning) {
			handler.sendEmptyMessage(0);
			try {
				Thread.sleep(ANIMATION_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	  *结束变换数字
	  */
	public void stopSwitch() {
		mIsRunning = false;
		// 结束时，注意要设置成正确的文字
		mTv.setText(mResult);
	}

	private String mNum = "0";
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int num = new Random().nextInt(9);
			mNum = String.valueOf(num);
			if (!mIsRunning) 
				mTv.setText(mResult);
			else
				mTv.setText(mNum);
		};
	};
}
