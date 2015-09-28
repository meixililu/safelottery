package com.zch.safelottery.asynctask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;

import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.LotteryInfoBean;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeMapHttpResponseHandler;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LotteryInfoParser;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class GetIssueAsyncTask{

	private Context context;
	private String lotteryid;
	private ProgressDialog progresdialog;
	private OnAsyncTaskListener onTaskListener;
	public GetIssueAsyncTask(Context context){
		this.context = context;
	}
	public GetIssueAsyncTask(Context context, String lotteryid){
		this.context = context;
		this.lotteryid = lotteryid;
	}
	
	protected void onPreExecute() {
		progresdialog = ProgressDialog.show(context, "", "正在获取期次", true,true);
		progresdialog.show();
	}

	public void send() {
		onPreExecute();
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("lotteryId", lotteryid);
		SafelotteryHttpClient.post(context, "3301", "", JsonUtils.toJsonStr(para), new TypeMapHttpResponseHandler(context,true) {
			@Override
			public void onSuccess(int statusCode, Map mMap) {
				try {
					if(mMap != null){
						ArrayList<LotteryInfoBean> result_list = (ArrayList<LotteryInfoBean>) JsonUtils.parserJsonArray(
								(String)mMap.get("issueList"), new LotteryInfoParser());
						LotteryInfoBean lotteryInfoBean = LotteryInfoParser.getLotteryInfo(result_list);
						onPostExecute(lotteryInfoBean);
					}else{
						ToastUtil.diaplayMesShort(context, "很抱歉，加载失败，请稍后再试！");
					}
				}catch (Exception e) {
					LogUtil.ExceptionLog("MyCount---onFinish()");
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() {
				progresdialog.dismiss();
			}
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
			}
		});
	}
	
	protected void onPostExecute(LotteryInfoBean lotteryInfoBean) {
		if(lotteryInfoBean != null){
			if(onTaskListener !=null){
				if(lotteryInfoBean.getIssueInfoList().size()>0){
					IssueInfoBean mIssueInfoBean = lotteryInfoBean.getIssueInfoList().get(0);
					String issue=mIssueInfoBean.getName();
					onTaskListener.onTaskPostExecuteListener(issue);
				}
			}
		}
	}
	/***
	 *  完成Task  要实现的功能
	 * @param onTaskListener
	 */
	public void setOnAsyncTaskListener(OnAsyncTaskListener onTaskListener){
		this.onTaskListener = onTaskListener;
	}
	
	public interface OnAsyncTaskListener{
		public void onTaskPostExecuteListener(String issue);
	}
}
