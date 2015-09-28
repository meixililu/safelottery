package com.zch.safelottery.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.zch.safelottery.util.ToastUtil;

public class MyAsyncTask extends AsyncTask<Integer, Void, Boolean>{

	private ProgressDialog progresdialog;
	private Context context;
	private String content = "正在处理中，请稍等...";;
	private boolean isShowDialog = true;
	
	private OnAsyncTaskListener onTaskListener;
	public MyAsyncTask(Context context){
		this.context = context;
	}
	public MyAsyncTask(Context context, String content){
		this.context = context;
		this.content = content;
	}
	public MyAsyncTask(Context context, boolean isShowDialog){
		this.context = context;
		this.isShowDialog = isShowDialog;
	}
	public MyAsyncTask(Context context, boolean isShowDialog,String content){
		this.context = context;
		this.isShowDialog = isShowDialog;
		this.content = content;
	}
	@Override
	protected void onPreExecute() {
		if(isShowDialog){
			progresdialog = ProgressDialog.show(context, "", content, true,true);
			progresdialog.show();
		}
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		boolean bool = false;
		if(onTaskListener != null)
			bool = onTaskListener.onTaskBackgroundListener();
		
		return bool;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(isShowDialog){
			progresdialog.dismiss();
		}
		if(result){
			if(onTaskListener !=null){
				onTaskListener.onTaskPostExecuteListener();
			}
		}else{
			ToastUtil.diaplayMesShort(context, "很抱歉，加载失败，请稍后再试！");
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
		public Boolean onTaskBackgroundListener();
		public void onTaskPostExecuteListener();
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isExecute(AsyncTask<?, ?, ?> asyncTask){
		AsyncTask.Status aStatus = asyncTask.getStatus();
		if(aStatus != AsyncTask.Status.FINISHED){
			ToastUtil.diaplayMesShort(context, "正在提交上一次的请求，请稍后再试！");
			return true;
		}
		return false;
	}
}
