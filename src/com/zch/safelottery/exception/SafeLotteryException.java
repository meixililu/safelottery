package com.zch.safelottery.exception;

import android.content.Context;
import android.content.Intent;

import com.zch.safelottery.util.LogUtil;

/**异常信息提示
 * @author Messi
 *
 */
public class SafeLotteryException extends Exception {

	private static final long serialVersionUID = 1L;
	
    public SafeLotteryException(Context mContext, int code) {
        LogUtil.ExceptionLog("SafeLotteryException:"+code);
        Intent intent = new Intent();
        intent.setAction("com.zch.safelottery.safelottery.broadcast");
        intent.putExtra("type", "error");
        intent.putExtra("error_code", code);
        mContext.sendBroadcast(intent);
    }
    
    public SafeLotteryException(Context mContext, String code, String message) {
        super(message);
        LogUtil.ExceptionLog("SafeLotteryException:"+message);
        Intent intent = new Intent();
        intent.setAction("com.zch.safelottery.safelottery.broadcast");
        intent.putExtra("type", "exception");
        intent.putExtra("error_info", message);
        intent.putExtra("error_code", code);
        mContext.sendBroadcast(intent);
    }
    
}
