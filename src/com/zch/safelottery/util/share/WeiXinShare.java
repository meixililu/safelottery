package com.zch.safelottery.util.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMediaMessage.IMediaObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.util.ImageUtil;

/**
 * 微信分享类</br>
 * 调用例子:</br>
 * WeiXinUtils weixin = new WeiXinUtils(this);
 * weixin.share(链接地址,图片引用,图片地址,标题,描述,是否分享到朋友圈);
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月20日
 */
public class WeiXinShare {

	/** 缩略图大小 150*150*/
	public static final int THUMB_SIZE = 150;
	/** 微信对应的AppId */
	public static final String WEIXIN_APP_ID = "wxd67502e6df5b07c6";

	private Context context;

	public WeiXinShare(Context context) {
		this.context = context;
	}

	public void share(BaseShare.ShareType shareType, String url, int resId, String urlOrPath, String title, String desc, boolean isTimeline) {
		WXMediaMessage msg = getMessage(context, shareType, url, resId, urlOrPath, title, desc);
		sendReq(msg, isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession);
	}

	/**
	 * 获取封装消息对象，同时判断是URL分享还是图片分享
	 * @param context 上下文
	 * @param url 链接地址
	 * @param resId 缩略图片与urlOrPath二选一
	 * @param urlOrPath　缩略图片
	 * @param title 标题
	 * @param desc　描述
	 * @return 消息对象
	 */
	public WXMediaMessage getMessage(Context context, BaseShare.ShareType shareType, String url, int resId, String urlOrPath, String title,
			String desc) {
		WXMediaMessage msg = null;
		if (shareType.ordinal() == BaseShare.ShareType.URL.ordinal()) {
			// String template = "【%1$s】（分享自 中彩票 %2$s）";
			// String newTitle = String.format(template, title, url);
			msg = getURLMediaMsg(context, url, resId, urlOrPath, title, desc);
		} else {
			msg = getImageMediaMsg(context, resId, urlOrPath, title, desc);
		}
		return msg;
	}

	private void sendReq(WXMediaMessage msg, int scene) {
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = scene;
		SafeApplication.wxApi.sendReq(req);
	}

	/**
	 * 获取URL地址分享对象
	 * @param context 上下文
	 * @param url 链接地址
	 * @param resId 缩略图片与urlOrPath二选一
	 * @param urlOrPath　缩略图片
	 * @param title 标题
	 * @param desc　描述
	 * @return URL地址分享对象
	 */
	public WXMediaMessage getURLMediaMsg(Context context, String url, int resId, String urlOrPath, String title, String desc) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		Bitmap thumbBmp = ImageUtil.getBitmap(context, resId, urlOrPath);
		WXMediaMessage mediaMsg = getMediaMsg(webpage, thumbBmp, title, desc);
		return mediaMsg;
	}

	/**
	 * 获取图片分享对象
	 * @param context 上下文
	 * @param resId 缩略图片与urlOrPath二选一
	 * @param urlOrPath 缩略图片
	 * @param title 标题
	 * @param desc 描述
	 * @return 图片分享对象
	 */
	public WXMediaMessage getImageMediaMsg(Context context, int resId, String urlOrPath, String title, String desc) {
		Bitmap bm = ImageUtil.getBitmap(context, resId, urlOrPath);
		WXImageObject imgObj = new WXImageObject(bm);
		WXMediaMessage mediaMsg = getMediaMsg(imgObj, bm, title, desc);
		return mediaMsg;
	}

	public WXMediaMessage getMediaMsg(IMediaObject object, Bitmap bm, String title, String desc) {
		WXMediaMessage msg = new WXMediaMessage();
		msg.title = title;
		msg.description = desc;
		msg.mediaObject = object;

		int width = bm.getWidth();
		int height = bm.getHeight();

		Matrix matrix = new Matrix();
		float scale = 1.0f * height / THUMB_SIZE;
		if (scale > 1) {
			scale = 1.0f * THUMB_SIZE / height;
		}else
			scale = 1.0f;
		matrix.postScale(scale, scale); // 长和宽放大缩小的比例
		Bitmap thumbBmp = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图
		
		return msg;
	}

	/**
	 * 生成唯一事务id
	 * @param type 类型字段串，无限制
	 * @return 事务id
	 */
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

}

/**
 * 	if (resId > 0) {// 本地资源图片
		bm = BitmapFactory.decodeResource(context.getResources(), resId);
		imgObj = new WXImageObject(bm);
	} else if (!TextUtils.isEmpty(urlOrPath)) {
		if (urlOrPath.startsWith("http")) {// 网络图片
			imgObj.imageUrl = urlOrPath;
			bm = BitmapFactory.decodeStream(new URL(urlOrPath).openStream());
		} else { // 本地图片路径
			File file = new File(urlOrPath);
			if (!file.exists()) {// 图片不存在
				Toast.makeText(context, " path = " + urlOrPath, Toast.LENGTH_LONG).show();
				return null;
			} else {// 图片存在
				imgObj.imagePath = urlOrPath;
				bm = BitmapFactory.decodeFile(urlOrPath);
			}
		}
	}
 */
