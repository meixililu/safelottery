package com.zch.safelottery.asynctask;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.dialogs.CaiDanDialog;
import com.zch.safelottery.dialogs.CaiDanDialog.OnCaiDanDialogClickListener;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.LogUtil;
/**
 * @author Messi
 *购买彩票专用task
 */
public class BuyLotteryTask {

	private BuyLotteryTaskListener mBuyLotteryTaskListener;
	private ProgressDialog progresdialog;
	private Context mContext;
	private String func;
	private String msg;
	private View submitBtn;
	
	public BuyLotteryTask(Context mContext, String func, String msg){
		this.mContext = mContext;
		this.func = func;
		this.msg = msg;
	}
	
	public BuyLotteryTask(Context mContext, String func, String msg, View submitBtn){
		this.mContext = mContext;
		this.func = func;
		this.msg = msg;
		if(submitBtn != null){
			this.submitBtn = submitBtn;
			submitBtn.setEnabled(false);
		}
	}
	
	protected void onPreExecute() {
		progresdialog = ProgressDialog.show(mContext, "", "正在购买...", true,false);
		progresdialog.show();
	}

	public void send() {
		try {
			onPreExecute();
			SafelotteryHttpClient.post(mContext, "3300", isEmpty(func), isEmpty(msg), 
					new TypeResultHttpResponseHandler(mContext,true) {
				@Override
				public void onSuccess(int statusCode, Result mResult) {
					onPostExecute(mResult);
				}
				@Override
				public void onFailure(int statusCode, String mErrorMsg) {
				}
				@Override
				public void onFinish() {
					progresdialog.dismiss();
					if(submitBtn != null){
						submitBtn.setEnabled(true);
					}
				}
			});
		} catch (Exception e) {
			LogUtil.ExceptionLog("BuyLotteryTask---sendDataTask");
			e.printStackTrace();
		}
	}
	
	protected void onPostExecute(Result resutl) {
		if(resutl != null){
			if(resutl.getCode().equals("0000")){
				try {
					isHasCaidan(resutl);
				} catch (Exception e) {
					showSuccessDialog();
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private void isHasCaidan(Result resutl) throws Exception{
		String resultStr = resutl.getResult();
		if(!TextUtils.isEmpty(resultStr)){
			JSONObject json = new JSONObject(resultStr);
			if(json.has("eggAmount")){
				String eggAmount = json.getString("eggAmount");
				String caidanContent = "";
				if(json.has("eggStoryNew")){
					caidanContent = json.getString("eggStoryNew");
				}
				if(!TextUtils.isEmpty(eggAmount)){
					CaiDanDialog mCaiDanDialog = new CaiDanDialog(mContext,eggAmount,caidanContent);
					mCaiDanDialog.setCanceledOnTouchOutside(true);
					mCaiDanDialog.setOnCaiDanDialogClickListener(new OnCaiDanDialogClickListener() {
						@Override
						public void onClick() {
							showSuccessDialog();
						}
					});
					mCaiDanDialog.show();
				}else{
					showSuccessDialog();
				}
			}else{
				showSuccessDialog();
			}
		}else{
			showSuccessDialog();
		}
	}
	
	private void showSuccessDialog(){
		if(mBuyLotteryTaskListener != null){
			GetString.isAccountNeedRefresh = true;
			mBuyLotteryTaskListener.onBuyLotteryTaskFinish();
		}
	}
	
	private String isEmpty(String content){
		if(TextUtils.isEmpty(content)){
			return "";
		}else{
			return content;
		}
	}
	
	public interface BuyLotteryTaskListener{
		public void onBuyLotteryTaskFinish();
	}
	
	public void setmBuyLotteryTaskListener(BuyLotteryTaskListener mBuyLotteryTaskListener) {
		this.mBuyLotteryTaskListener = mBuyLotteryTaskListener;
	}

}
