package com.zch.safelottery.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.zch.safelottery.R;
import com.zch.safelottery.util.share.BaseShare;
import com.zch.safelottery.util.share.BaseShare.ShareType;
import com.zch.safelottery.util.share.ShareUtil;
import com.zch.safelottery.util.share.ShareUtil.CallBackExt;

/**
 * 公用分享对话框
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月26日
 */
public class ShareDialog extends Dialog {

	/** 标题  */
	private String mTitle;
	/** 要分享的网页地址  */
	private String mURL;
	/** 描述  */
	private String mDesc;
	/** 本地图片引用  */
	private int mResId;
	/** 本地图片地址  */
	private String mImgPath;
	/** 分享类型@see BaseShare.ShareType  */
	private BaseShare.ShareType mShareType;
	/**  对话框所属的activity */
	private Activity mActivity;
	/** 点击分享后的回调函数  */
	private ShareUtil.CallBack mCallback;
	/**
	 * 多个分享平台分享的内容一样时使用
	 * @param activty 对话框所属的activity
	 * @param mTitle 标题
	 * @param mURL　要分享的网页地址
	 * @param mDesc　描述
	 * @param mImgPath　本地图片地址
	 * @param mShareType  分享类型@see BaseShare.ShareType
	 * @param callback 点击分享后的回调函数
	 */
	public ShareDialog(Activity activty, String mTitle, String mURL, String mDesc, int resId, String mImgPath, ShareType mShareType,
			ShareUtil.CallBack callback) {
		super(activty, R.style.mydialog);
		this.mTitle = mTitle;
		this.mURL = mURL;
		this.mDesc = mDesc;
		this.mResId = resId;
		this.mImgPath = mImgPath;
		this.mShareType = mShareType;
		this.mActivity = activty;
		this.mCallback = callback;
	}

	/**
	 * 用于不同分享平台分享的内容不同时使用
	 * @param activty 对话框所属的activity
	 * @param mTitle 标题
	 * @param mURL　要分享的网页地址
	 * @param mDesc　描述
	 * @param mImgPath　本地图片地址
	 * @param mShareType  分享类型@see BaseShare.ShareType
	 * @param callback 点击分享后的回调函数
	 */
	public ShareDialog(Activity activty, ShareType mShareType, ShareUtil.CallBackExt callback) {
		super(activty, R.style.mydialog);
		this.mShareType = mShareType;
		this.mActivity = activty;
		this.mCallback = callback;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_grid_share);
		View parent = findViewById(R.id.table);
		// 为parent添加分享功能
		if (mCallback != null && mCallback instanceof ShareUtil.CallBackExt) {
			ShareUtil.addShareForView(mActivity, parent, mShareType, (CallBackExt) mCallback);
		} else
			ShareUtil.addShareForView(mActivity, parent, mShareType, mURL, mResId, mImgPath, mTitle, mDesc, mCallback);
	}
}
