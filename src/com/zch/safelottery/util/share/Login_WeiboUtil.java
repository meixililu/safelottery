/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.zch.safelottery.util.share;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.zch.safelottery.inteface.JointLoginListener;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

/**
 * 新浪分享</br>
 * 调用例子：<br/>
 * 1.在activity的onCreate方法中执行下面两句：<br/>
 * 		Login_WeiboUtil weibo = new Login_WeiboUtil(this);<br/>
 * 		weibo.register();<br/>
 * 2.weibo.share(url, title, desc, resId, urlOrPath);<br/>
 * 
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月20日
 */
public class Login_WeiboUtil {
	// 应用的key 请到官方申请正式的appkey替换APP_KEY
	public static final String APP_KEY = "2283085917";

	// 替换为开发者REDIRECT_URL
	public static final String REDIRECT_URL = "http://www.zch168.com";

	// 新支持scope：支持传入多个scope权限，用逗号分隔
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

	public static final String CLIENT_ID = "client_id";
	public static final String RESPONSE_TYPE = "response_type";
	public static final String USER_REDIRECT_URL = "redirect_uri";
	public static final String DISPLAY = "display";
	public static final String USER_SCOPE = "scope";
	public static final String PACKAGE_NAME = "packagename";
	public static final String KEY_HASH = "key_hash";
	public WeiboAuth mWeibo;
	private Oauth2AccessToken mAccessToken;
	private Activity Mycontext;
	private JointLoginListener mJointLoginListener;

	/** 微博微博分享接口实例 */
	private IWeiboShareAPI mWeiboShareAPI = null;

	public void setmJointLoginListener(JointLoginListener mJointLoginListener) {
		this.mJointLoginListener = mJointLoginListener;
	}

	public void login(Activity context, SsoHandler mSsoHandler) {
		try {
			Mycontext = context;
			// mWeibo.anthorize(new AuthDialogListener());
			mSsoHandler.authorize(new AuthDialogListener());
		} catch (Exception e) {
			ToastUtil.diaplayMesShort(Mycontext, "未安装微博客户端！");
			e.printStackTrace();
		}
	}

	class AuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				String token = values.getString("access_token");
				String expires_in = values.getString("expires_in");
				String uid = values.getString("uid");
				LogUtil.DefalutLog("WeiboAuthListener---token:" + token + "---uid:" + uid);
				if (mJointLoginListener != null) {
					mJointLoginListener.onSuccess(result(uid, token));
				}
			} else {
				Toast.makeText(Mycontext, "验证失败", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onCancel() {
			// Toast.makeText(Mycontext, "Auth cancel",
			// Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(Mycontext, "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public static String result(String uid, String token) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("unUserID", uid);
		map.put("unType", "WB");
		map.put("accessToken", token);
		return JsonUtils.toJsonStr(map);
	}

	public void registerApp(Activity context) {
		this.Mycontext = context;
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this.Mycontext, APP_KEY);

		// 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
		// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
		// NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
		mWeiboShareAPI.registerApp();
	}

	public void share(Activity context, String url, String title, String desc, int resId, String urlOrPath) {

		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
				// 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351
				// 时，支持同时分享多条消息，
				// 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
				sendMultiMessage(url, title, desc, resId, urlOrPath);
			} else {
				sendSingleMessage(title, desc, resId, urlOrPath, url);
			}
		} else {
			ToastUtil.diaplayMesLong(context, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。");
		}
	}

	private void sendMultiMessage(String url, String title, String desc, int resId, String urlOrPath) {
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		WeiboMultiMessage weiboMulMessage = new WeiboMultiMessage();
		
		if (!TextUtils.isEmpty(url)) {
			weiboMulMessage.mediaObject = getWebpageObj(url, title, desc, resId, urlOrPath);
//			String template = "【%1$s】（分享自 中彩票 %2$s）";
//			weiboMulMessage.textObject = getTextObj(String.format(template, title, url));
		}
		if (!TextUtils.isEmpty(title)) {
			weiboMulMessage.textObject = getTextObj(title);
		}
		if (!TextUtils.isEmpty(urlOrPath) || resId > 0)
			weiboMulMessage.imageObject = getImageObj(resId, urlOrPath);


		request.multiMessage = weiboMulMessage;
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		// 3. 发送请求消息到微博，唤起微博分享界面
		mWeiboShareAPI.sendRequest(request);

	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。
	 * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
	 * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
	 * 
	 * @param hasText    分享的内容是否有文本
	 * @param hasImage   分享的内容是否有图片
	 * @param hasWebpage 分享的内容是否有网页
	 */
	private void sendSingleMessage(String title, String desc, int resId, String urlOrPath, String targetUrl) {

		// 1. 初始化微博的分享消息
		// 用户可以分享文本、图片、网页、音乐、视频中的一种
		WeiboMessage weiboMessage = new WeiboMessage();
		if (TextUtils.isEmpty(desc)) {
			String template = "【%1$s】（分享自 中彩票 %2$s）";
			weiboMessage.mediaObject = getTextObj(String.format(template, title, targetUrl));
		}
		if (resId > 0 || !TextUtils.isEmpty(urlOrPath)) {
			weiboMessage.mediaObject = getImageObj(resId, urlOrPath);
		}
		if (!TextUtils.isEmpty(targetUrl)) {
			weiboMessage.mediaObject = getWebpageObj(targetUrl, title, desc, resId, urlOrPath);
		}

		// 2. 初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		// 3. 发送请求消息到微博，唤起微博分享界面
		mWeiboShareAPI.sendRequest(request);
	}

	/**
	 * 创建文本消息对象。
	 * 
	 * @return 文本消息对象。
	 */
	private TextObject getTextObj(String text) {
		TextObject textObject = new TextObject();
		textObject.text = text;
		return textObject;
	}

	/**
	* 创建图片消息对象。
	* 
	* @return 图片消息对象。
	*/
	private ImageObject getImageObj(int resId, String urlOrPath) {
		ImageObject imageObject = new ImageObject();
		Bitmap bm = ImageUtil.getBitmap(this.Mycontext, resId, urlOrPath);
		imageObject.setImageObject(bm);
		return imageObject;
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj(String url, String title, String desc, int resId, String urlOrPath) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = desc;

		// 设置 Bitmap 类型的图片到视频对象里
		Bitmap bm = ImageUtil.getBitmap(this.Mycontext, resId, urlOrPath);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, WeiXinShare.THUMB_SIZE, WeiXinShare.THUMB_SIZE, true);
		mediaObject.setThumbImage(thumbBmp);
		mediaObject.actionUrl = url;
		mediaObject.defaultText = "";
		return mediaObject;
	}

	public IWeiboShareAPI getmWeiboShareAPI() {
		return mWeiboShareAPI;
	}

	public void setmWeiboShareAPI(IWeiboShareAPI mWeiboShareAPI) {
		this.mWeiboShareAPI = mWeiboShareAPI;
	}

	public void handlerNewIntent(Intent intent,IWeiboHandler.Response resp) {
		if (mWeiboShareAPI != null) {
			mWeiboShareAPI.handleWeiboResponse(intent, resp);
		}
	}

}
