package com.zch.safelottery.util.share;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zch.safelottery.inteface.JointLoginListener;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.util.LogUtil;

public class LoginQQUtil {

	public static final String APP_ID = "100523152";

	private static final String API_KEY = "844a423da36d586b38cf3269aa18d520";

	private JointLoginListener mJointLoginListener;
	
	public void setmJointLoginListener(JointLoginListener mJointLoginListener) {
		this.mJointLoginListener = mJointLoginListener;
	}

	public void loginQQ(final Activity context,Tencent mTencent){
		IUiListener listener = new BaseUiListener() {
			@Override
			protected void doComplete(JSONObject values) {
				if(mJointLoginListener != null){
					mJointLoginListener.onSuccess( result(values) );
				}
			}
        };
        
        mTencent.login(context, "all", listener);
	}
	
	public static String result(JSONObject mJSONObject){
		HashMap<String,String> map = new HashMap<String,String>();
		try {
			if(mJSONObject.has("openid")){
				map.put("unUserID", mJSONObject.getString("openid"));
			}
			if(mJSONObject.has("access_token")){
				map.put("accessToken", mJSONObject.getString("access_token"));
			}
			map.put("unType", "QQ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return JsonUtils.toJsonStr(map);
	}
	
	private class BaseUiListener implements IUiListener {

		protected void doComplete(JSONObject values) {
		}

        @Override
        public void onError(UiError e) {
        	LogUtil.DefalutLog("onError:"+"code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
        	LogUtil.DefalutLog("onCancel"+ "");
        }

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject)response);
		}
    }
	
}
