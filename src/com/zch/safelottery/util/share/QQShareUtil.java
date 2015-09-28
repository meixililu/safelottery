package com.zch.safelottery.util.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class QQShareUtil implements BaseShare {

	private int mExtarFlag = 0x00;
	private Activity context;
	private QQShare mQQShare;

	public QQShareUtil(Activity context) {
		this.context = context;
	}

	/**
	 * 用异步方式启动分享
	 * @param params
	 */
	private void doShareToQQ(final Bundle params) {
		if (SafeApplication.QQAuth == null) {
			QQAuth qqAuth = QQAuth.createInstance(LoginQQUtil.APP_ID, context);
			SafeApplication.QQAuth = qqAuth;
		}
		mQQShare = new QQShare(context, SafeApplication.QQAuth.getQQToken());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				mQQShare.shareToQQ(context, params, new IUiListener() {
					@Override
					public void onCancel() {
						Message msg = handler.obtainMessage();
						msg.obj = "取消分享！";
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

	
	
	protected void share(int shareType, String title, String desc, String targetUrl, String pictureUrl) {
	
//		shareType = (shareType==BaseShare.ShareType.URL.ordinal() ? QQShare.SHARE_TO_QQ_TYPE_DEFAULT : QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		String appName = context.getString(R.string.app_name);

		final Bundle params = new Bundle();
		if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
			params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
		}
		if (!TextUtils.isEmpty(pictureUrl)) {
			if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
				params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, pictureUrl);
			} else {
				params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, pictureUrl);
			}
		}

		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
		doShareToQQ(params);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = (String) msg.obj;
			ToastUtil.diaplayMesLong(context, text);
		};
	};
	
	

	@Override
	public void shareURL(String title, String desc, String targetUrl, String pictureUrl) {
		share(QQShare.SHARE_TO_QQ_TYPE_DEFAULT, title, desc, targetUrl, pictureUrl);
	}


	@Override
	public void sharePicture(String localImagePath, String title, String desc) {
		share(QQShare.SHARE_TO_QQ_TYPE_IMAGE, title, desc, "", localImagePath);
	}

}
