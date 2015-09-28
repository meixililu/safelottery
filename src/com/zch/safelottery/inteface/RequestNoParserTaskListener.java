package com.zch.safelottery.inteface;
/**
 * @author messi
 * AsyncTask回调接口
 */
public interface RequestNoParserTaskListener {

	public void onPreExecute();
	public void onFinish(byte[] resutl);
}
