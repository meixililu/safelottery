package com.zch.safelottery.inteface;

import java.util.ArrayList;
/**
 * @author messi
 * AsyncTask回调接口
 */
public interface RequestTaskListener {

	public void onPreExecute();
	public void onFinish(ArrayList<Object> resutl);
}
