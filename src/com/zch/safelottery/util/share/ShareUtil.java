package com.zch.safelottery.util.share;

import android.app.Activity;
import android.view.View;

import com.zch.safelottery.R;
import com.zch.safelottery.util.ToastUtil;

/**
 *　为view添加分享功能,注意布局文件中的id必须与预定义的id一致。
 *  所以尽量使用inclucde方法实现id统一。具体ID如下：
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月26日
 */
public class ShareUtil {

	/**  微信分享 */
	private static WeiXinShare weixin;
	/**  新浪分享 */
	private static Login_WeiboUtil sina;

	/**
	 *　为view添加分享功能（注意：不同分享平台分享的内容不同时使用）
	 * @param context activity对象
	 * @param parent 包括所有分享按钮的view或者最顶级的view均可
	 * @param shareType 分享类型@see BaseShare.ShareType
	 * @param targetUrl 要分享网页的地址
	 * @param resId　要分享图片的引用值
	 * @param urlOrPath　要分享图片的本地路径或者网络地址
	 * @param title　标题
	 * @param desc　描述
	 * @param callback　点击分享后的回调函数
	 */
	public static void addShareForView(final Activity context, View parent, final BaseShare.ShareType shareType, final CallBackExt callback) {

		if (callback == null) {
			ToastUtil.diaplayMesLong(context, "高级分享的回调方法不能为空！");
			return;
		}
		View btnShareToFriendCircle = parent.findViewById(R.id.btn_share_friend_circle);
		View btnShareToQQZone = parent.findViewById(R.id.btn_share_qq_zone);
		View btnShareToWeixin = parent.findViewById(R.id.btn_share_weixin);
		View btnShareToQQ = parent.findViewById(R.id.btn_share_qq);
		View btnShareToSina = parent.findViewById(R.id.btn_share_sina);

		final Login_WeiboUtil sinaShare = getSinaInstance(context);
		final QQShareUtil qqShare = new QQShareUtil(context);

		// 为各个分享平台添加事件
		View.OnClickListener clickEvent = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_share_friend_circle:
					WeiXinShare weixin = getWeixinInstance(context);
					callback.onClickWeixinTimeline(weixin);
					break;
				case R.id.btn_share_weixin:
					weixin = new WeiXinShare(context);
					callback.onClickWeixinFriend(weixin);
					break;
				case R.id.btn_share_qq_zone:
					callback.onClickQzone(qqShare);
					break;
				case R.id.btn_share_qq:
					callback.onClickQQ(qqShare);
					break;
				case R.id.btn_share_sina:
					callback.onClickSina(sinaShare);
					break;
				}
				if (callback != null) {
					callback.onAfterClickShare();
				}
			}
		};
		btnShareToFriendCircle.setOnClickListener(clickEvent);
		btnShareToQQZone.setOnClickListener(clickEvent);
		btnShareToWeixin.setOnClickListener(clickEvent);
		btnShareToQQ.setOnClickListener(clickEvent);
		btnShareToSina.setOnClickListener(clickEvent);
	}

	/**
	 *　为view添加分享功能
	 * @param context activity对象
	 * @param parent 包括所有分享按钮的view或者最顶级的view均可
	 * @param shareType 分享类型@see BaseShare.ShareType
	 * @param targetUrl 要分享网页的地址
	 * @param resId　要分享图片的引用值
	 * @param urlOrPath　要分享图片的本地路径或者网络地址
	 * @param title　标题
	 * @param desc　描述
	 * @param callback　点击分享后的回调函数
	 */
	public static void addShareForView(final Activity context, View parent, final BaseShare.ShareType shareType, final String targetUrl,
			final int resId, final String urlOrPath, final String title, final String desc, final CallBack callback) {

		View btnShareToFriendCircle = parent.findViewById(R.id.btn_share_friend_circle);
		View btnShareToQQZone = parent.findViewById(R.id.btn_share_qq_zone);
		View btnShareToWeixin = parent.findViewById(R.id.btn_share_weixin);
		View btnShareToQQ = parent.findViewById(R.id.btn_share_qq);
		View btnShareToSina = parent.findViewById(R.id.btn_share_sina);

		final Login_WeiboUtil sinaShare = getSinaInstance(context);
		final QQShareUtil qqShare = new QQShareUtil(context);

		// 为各个分享平台添加事件
		View.OnClickListener clickEvent = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_share_friend_circle:
					WeiXinShare weixin = getWeixinInstance(context);
					weixin.share(shareType, targetUrl, resId, urlOrPath, title, desc, true);
					break;
				case R.id.btn_share_weixin:
					weixin = new WeiXinShare(context);
					weixin.share(shareType, targetUrl, resId, urlOrPath, title, desc, false);
					break;
				case R.id.btn_share_qq_zone:
					// 因为目前ＱＱ空间不支持纯图片分享
					/* QZoneShareUtil qzoneShare = new QZoneShareUtil(context);
					 * if (shareType.ordinal() ==
					 * BaseShare.ShareType.PICTURE.ordinal()) {
					 * qzoneShare.sharePicture(urlOrPath, title, desc); } else
					 * qzoneShare.shareURL(title, desc, targetUrl, urlOrPath);
					 * break; */

				case R.id.btn_share_qq:
					if (shareType.ordinal() == BaseShare.ShareType.PICTURE.ordinal()) {
						qqShare.sharePicture(urlOrPath, title, desc);
					} else
						qqShare.shareURL(title, desc, targetUrl, urlOrPath);
					break;
				case R.id.btn_share_sina:
					sinaShare.share(context, targetUrl, title, desc, resId, urlOrPath);
					break;
				}
				if (callback != null) {
					callback.onAfterClickShare();
				}
			}
		};
		btnShareToFriendCircle.setOnClickListener(clickEvent);
		btnShareToQQZone.setOnClickListener(clickEvent);
		btnShareToWeixin.setOnClickListener(clickEvent);
		btnShareToQQ.setOnClickListener(clickEvent);
		btnShareToSina.setOnClickListener(clickEvent);
	}

	/**
	 * 分享后的回调类
	 *
	 * @Company 北京中彩汇网络科技有限公司
	 * @author 陈振国
	 * @version 1.0.0
	 * @create 2014年6月26日
	 */
	public interface CallBack {
		void onAfterClickShare();
	}

	/**
	 * 用于不同分享平台分享的内容不同时
	 *
	 * @Company 北京中彩汇网络科技有限公司
	 * @author 陈振国
	 * @version 1.0.0
	 * @create 2014年6月26日
	 */
	public interface CallBackExt extends CallBack {
		void onClickSina(Login_WeiboUtil sina);

		void onClickQQ(QQShareUtil qq);

		void onClickQzone(QQShareUtil qq);

		void onClickWeixinFriend(WeiXinShare weixin);

		void onClickWeixinTimeline(WeiXinShare weixin);
	}

	public static synchronized WeiXinShare getWeixinInstance(Activity context) {
		if (weixin == null) {
			weixin = new WeiXinShare(context);
		}
		return weixin;
	};

	public static synchronized Login_WeiboUtil getSinaInstance(Activity context) {
		if (sina == null) {
			sina = new Login_WeiboUtil();
			sina.registerApp(context);
		}
		return sina;
	};
}
