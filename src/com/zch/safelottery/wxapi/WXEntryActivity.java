package com.zch.safelottery.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zch.safelottery.util.ToastUtil;
import com.zch.safelottery.util.share.WeiXinShare;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	  // IWXAPI 是第三方app和微信通信的openapi接口  
    private IWXAPI api;  
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
    	super.onCreate(savedInstanceState);  
        api = WXAPIFactory.createWXAPI(this, WeiXinShare.WEIXIN_APP_ID ,false);  
        api.handleIntent(getIntent(), this);  
    }  
    
    
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
    
    
	@Override
	public void onReq(BaseReq arg0) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			ToastUtil.diaplayMesShort(this, "分享成功！");
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			ToastUtil.diaplayMesShort(this, "取消分享！");
			break;
		case BaseResp.ErrCode.ERR_SENT_FAILED:
			ToastUtil.diaplayMesShort(this, "分享失败！");
			break;
		}
		finish();
	}

}
