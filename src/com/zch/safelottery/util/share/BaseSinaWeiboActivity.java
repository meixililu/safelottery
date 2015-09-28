package com.zch.safelottery.util.share;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class BaseSinaWeiboActivity extends ZCHBaseActivity implements IWeiboHandler.Response {

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			ToastUtil.diaplayMesShort(this, "分享成功！");
			LogUtil.e("", "============分享成功！");
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			ToastUtil.diaplayMesShort(this, "取消分享！");
			LogUtil.e("", "=============取消分享！");
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			ToastUtil.diaplayMesShort(this, "分享失败！");
			LogUtil.e("", "=============分享失败！");
			break;
		}
	}
}
