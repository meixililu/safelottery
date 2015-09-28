package com.zch.safelottery.util.share;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

/**
 * QQ空间分享工具类
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月26日
 */
public class QZoneShareUtil implements BaseShare {

	// QZone分享， SHARE_TO_QQ_TYPE_DEFAULT 图文，SHARE_TO_QQ_TYPE_IMAGE 纯图
	private Tencent tencent;
	private Activity context;

	public QZoneShareUtil(Activity context) {
		this.context = context;
		tencent = Tencent.createInstance(LoginQQUtil.APP_ID, context);
	}

	/**
	 * 用异步方式启动分享
	 * @param params
	 */
	private void doShareToQzone(final Bundle params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				tencent.shareToQzone(context, params, new IUiListener() {

					@Override
					public void onCancel() {
						Message msg = handler.obtainMessage();
						msg.obj = "取消分享!";
						handler.sendMessage(msg);
					}

					@Override
					public void onError(UiError e) {
						Message msg = handler.obtainMessage();
						msg.obj = "分享失败！";
						handler.sendMessage(msg);
						LogUtil.e("", e != null ? e.errorMessage : "");
					}

					@Override
					public void onComplete(Object response) {
						Message msg = handler.obtainMessage();
						msg.obj = "分享成功！";
						handler.sendMessage(msg);
					}

				});
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = (String) msg.obj;
			ToastUtil.diaplayMesLong(context, text);
		};
	};

	public void share(int shareType, String title, String desc, String targetUrl, String pictureUrl) {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc);
		if (!TextUtils.isEmpty(targetUrl)) {
			params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		}
		// 支持传多个imageUrl
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(pictureUrl);
		// String imageUrl = "XXX";
		// params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		doShareToQzone(params);
	}

	public void shareURL(String title, String desc, String targetUrl, String pictureUrl) {
		String template = "【%1$s】（分享自 中彩票 %2$s）";
		String newTitle = String.format(template, title, targetUrl);
		share(QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, newTitle, desc, targetUrl, pictureUrl);
	}


	@Override
	public void sharePicture(String localImagePath, String title, String desc) {
		share(QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, title, desc, "", localImagePath);
		
	}


}
