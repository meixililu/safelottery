package com.zch.safelottery.util;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.dialogs.UpdateDialog;
import com.zch.safelottery.dialogs.UpdateDialog.OnDownLoadDialogClickListener;
import com.zch.safelottery.http.AsyncHttpClient;
import com.zch.safelottery.http.RequestParams;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.SystemInfo;

/**
 * 检测版本更新工具类，如果有新版本则弹出对话框提示，
 * 分为：1不需要更新  2需要更新  3强制更新
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年3月20日
 */
public class VersionUpdateUtils {

	public static final String DEFAULT_UPDATE_TIP = "老版本不支持该功能，请更新版本后使用。";

	/** 更新状态：1不需要更新  2需要更新  3强制   */
	private int mStatus;
	/** 更新内容  */
	private String mUpdateNote;
	/** 下载地址  */
	private String mDownloadUrl;
	/** 最新版本号  */
	private int mNewVersion;
	/** 调用属主，必须是Activity,因为对话框需要依附于它  */
	private Activity mActivity;
	/** 回调接口  */
	private Callback mCallback;

	private boolean mIsShowProgressBar;
	private ProgressDialog mDialog = null;
	private int timeOut = 15 * 1000;

	public interface Callback {
		/**
		 * 更新完成或者不需要更新时调用
		 */
		public void onComplete();

		/**
		 * 强制更新,主要用于loading页面中改变变量的值。
		 */
		public void onForceUpdate();

		/**
		 *失败操作
		 * @param statusCode
		 * @param mErrorMsg
		 */
		public void onFailure(int statusCode, String mErrorMsg);
		
		  /**
		   * 取消更新操作
		   */
		public void onCancleBtnClick();
		  /**
		   * 后台更新操作
		   */
		public void onBackgroundBtnClick();
	}

	/**
	 * @param activity 属主activity
	 * @param callback 回调对象
	 * @param isUseServerNote　是否使用服务器返回的信息
	 */
	public VersionUpdateUtils(Context activity, Callback callback, String msg) {
		super();
		if (activity instanceof Activity) {
			this.mActivity = (Activity) activity;
		}
		this.mUpdateNote = msg;
		this.mCallback = callback;
	}
	
	/**
	 * @param activity 属主activity
	 * @param callback 回调对象
	 * @param isUseServerNote　是否使用服务器返回的信息
	 * @param timeout　请求超时时间
	 */
	public VersionUpdateUtils(Context activity, Callback callback, String msg, int timeout) {
		super();
		if (activity instanceof Activity) {
			this.mActivity = (Activity) activity;
		}
		this.timeOut = timeout;
		this.mUpdateNote = msg;
		this.mCallback = callback;
	}

	/**
	 * @param activity 属主activity
	 * @param callback 回调对象
	 * @param isUseServerNote　是否使用服务器返回的信息
	 * @param isShowProgressBar　是否显示请求进度框
	 */
	public VersionUpdateUtils(Context activity, Callback callback, String msg, boolean isShowProgressBar) {
		super();
		if (activity instanceof Activity) {
			this.mActivity = (Activity) activity;
		}
		this.mUpdateNote = msg;
		this.mCallback = callback;
		this.mIsShowProgressBar = isShowProgressBar;
	}

	/**
	 *执行联网检查是否有更新的版本
	 */


	public void checkUpdate() {
		if (mIsShowProgressBar) {
			mDialog = ProgressDialog.show(mActivity, "", "检测更新...", true, true);
		}
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appVersion", String.valueOf(SystemInfo.softVerCode));
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(timeOut);
		String msg = JsonUtils.toJsonStr(paramMap);
		RequestParams params = SafelotteryHttpClient.getRequestData(mActivity, "3001", "clientVersionCheck", msg);
		client.post(GetString.SERVERURL, params, new TypeResultHttpResponseHandler(
				mActivity, false) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				String resultStr = mResult.getResult();
				if (!TextUtils.isEmpty(resultStr)) {
					HashMap<String, String> map = (HashMap<String, String>) JsonUtils.stringToMap(resultStr);
					if (map != null) {
						mDownloadUrl = map.get("downUrl");
						mStatus = ConversionUtil.StringToInt((map.get("status")));
						mNewVersion = ConversionUtil.StringToInt((map.get("appVersion")));
						if (TextUtils.isEmpty(mUpdateNote)) {
							mUpdateNote = map.get("updateInfo");
							//防止后台返回"null"字段串,客户端进行删除
							mUpdateNote = mUpdateNote.replaceAll("null", "");
						}
					}
					// 需要更新和强制更新
					if (mStatus == 2 || mStatus == 3) {
						if (mStatus == 3 && mCallback != null) {
							mCallback.onForceUpdate();
						}
						isShowUpdateDialog();
					} else {
						if (mCallback != null) {
							mCallback.onComplete();
						}
					}
				} else {
					if (mCallback != null) {
						mCallback.onComplete();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				if (mCallback != null) {
					mCallback.onFailure(statusCode, mErrorMsg);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (mIsShowProgressBar) {
					mDialog.dismiss();;
				}
			}
		});
	}

	/**
	 * 是否需要弹出更新对话框
	 */
	private void isShowUpdateDialog() {
		if (SystemInfo.softVerCode < mNewVersion) {
			showUpdateDialog();
		} else {
			if (mCallback != null) {
				mCallback.onComplete();
			}
		}
	}

	/**
	 * 弹出更新对话框
	 */
	private void showUpdateDialog() {
		try {
			UpdateDialog dialog = new UpdateDialog(mActivity);
			if (mStatus == 2) {
				dialog.setTitle("更新提示");
				dialog.setContent(TextUtils.isEmpty(mUpdateNote) ? "有可更新的版本，是否更新？" : mUpdateNote);
			} else if (mStatus == 3) {

				dialog.setTitle("强制升级提示");
				dialog.setContent(TextUtils.isEmpty(mUpdateNote) ? "您使用的版本太低，需要更新后才能正常使用！" : mUpdateNote);
				dialog.setCancleBtnVisibility(true);
			}
			dialog.setCancelable(false);
			dialog.setDownLoadDialogClickListener(new OnDownLoadDialogClickListener() {
				@Override
				public void onCancleBtnClick() {
					if (mCallback != null) {
						mCallback.onCancleBtnClick();
					}
				}

				@Override
				public void onBackgroundBtnClick() {
					DownloadFileUtil.DownloadFile(mActivity, mDownloadUrl);
					if (mCallback != null) {
						mCallback.onBackgroundBtnClick();
					}
				}

				@Override
				public void onBrowserBtnClick() {
					Intent it = new Intent("android.intent.action.VIEW", Uri.parse(mDownloadUrl));
					mActivity.startActivityForResult(it, 100);
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.ExceptionLog(e == null ? "" : e.getMessage());
		}
	}

	/**
	 * 回调适配器
	 *
	 * @Company 北京中彩汇网络科技有限公司
	 * @author 陈振国
	 * @version 1.0.0
	 * @create 2014年3月20日
	 */
	public static class VersionUpdateAdapter implements Callback {
		@Override
		public void onComplete() {
		}

		@Override
		public void onForceUpdate() {
		}

		@Override
		public void onFailure(int statusCode, String mErrorMsg) {
		}

		@Override
		public void onCancleBtnClick() {
		}

		@Override
		public void onBackgroundBtnClick() {
		}
	}
}
