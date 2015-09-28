package com.zch.safelottery.view;

import android.os.Handler;
import android.widget.TextView;

/**
 * 设置一组数字切换
 *
 * @Company 北京中彩汇网络科技有限公司</br>
 * @author 陈振国</br>
 * @version 1.0.0</br>
 * @create 2014年4月2日</br>
 * 
 * 调用示例：</br>
 * <p>TextSwitchViewGroup tsvg = new TextSwitchViewGroup(new TextView[]{mTv1,mTv2,mTv3}, new String[]{"8","8","8"})</p>
 * <p>tsvg.start();</p>
 */
public class TextSwitchViewGroup {

	/** 切换持续时间，单位：毫秒  */
	private final long DURING_MILLIS = 2500;
	private TextSwitchView[] mTsvArray;

	/**
	 * @param textViewArray 用于切换数字的TextView数组
	 * @param resultArray　结束后应该显示的结果数组
	 * @throws Exception 如果切换数字的TextView个数与结果数组个数不一致，则抛出异常
	 */
	public TextSwitchViewGroup(TextView[] textViewArray, String[] resultArray) throws Exception {
		if (textViewArray.length != resultArray.length) {
			throw new Exception("切换数字的TextView个数与结果数组个数不一致，请检查！");
		}

		int len = resultArray.length;
		mTsvArray = new TextSwitchView[len];
		for (int i = 0; i < len; i++) {
			mTsvArray[i] = new TextSwitchView(textViewArray[i], resultArray[i]);
		}
	}

	/**
	 * 启动数字切换
	 * 
	 */
	public void start() {
		final int length = mTsvArray.length;
		for (int i = 0; i < length; i++) {
			mTsvArray[i].startSwitch();
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < length; i++) {
					mTsvArray[i].stopSwitch();
				}
			}
		}, DURING_MILLIS);
	}

}
