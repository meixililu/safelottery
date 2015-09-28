package com.zch.safelottery.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.GetString;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.Md5;


public class SafelotteryHttpClient {
	
	private static final String key = "kkdYJjaMBSqEp6NLepGTxz2uyHn5FJxMCdGe7iAlJZWzSuvBt995uRARe2rbi8V7";

	private static AsyncHttpClient client = new AsyncHttpClient();

	/**直接请求
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		client.get(url, params, responseHandler);
	}
	
	/**中彩票接口请求
	 * @param mContext
	 * @param cmd  接口编号
	 * @param func 接口功能
	 * @param msg  请求数据
	 * @param responseHandler 结果处理
	 */
	public static void post(Context mContext,String cmd, String func, String msg, ResponseHandlerInterface responseHandler) {
		RequestParams params = getRequestData(mContext, cmd, func, msg);
		client.post(GetString.SERVERURL, params, responseHandler);
	}
	
	/**
	 * @param mContext
	 * @param cmd  接口编号
	 * @param func 接口功能
	 * @param msg  请求数据
	 * @param responseHandler 结果处理
	 */
	public static void get(Context mContext,String cmd, String func, String msg, ResponseHandlerInterface responseHandler) {
		RequestParams params = getRequestData(mContext, cmd, func, msg);
		client.get(GetString.SERVERURL, params, responseHandler);
	}
	
	/** 推送
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(String url, String action, String msg, ResponseHandlerInterface responseHandler) {
		RequestParams params = new RequestParams();
		params.put("action", action);
		params.put("msg", msg);
		LogUtil.DefalutLog("+++send pull msg:" + params.toString());
		client.post(url, params, responseHandler);
	}
	
	/** 推送
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void postNormal(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		LogUtil.DefalutLog("+++postNormal url:" + url);
		LogUtil.DefalutLog("+++postNormal msg:" + params.toString());
		client.post(url, params, responseHandler);
	}
	
	/** upload img 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void uploadImg(String url, String path, ResponseHandlerInterface responseHandler) {
		try {
			RequestParams params = new RequestParams();
//			params.put("image_file", new File(path));
			ByteArrayInputStream isBm = ImageUtil.compressImage(path);
			params.put("image_file", isBm, "a.jpeg");
			client.post(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 封装需要发送的参数
	 * @param cmd
	 * @param func
	 * @param msg
	 * @return
	 */
	public static RequestParams getRequestData(Context mContext, String cmd, String func, String msg) {
		String md5 = getMd5String(mContext, cmd, func, msg);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sysVer", SystemInfo.sysVer);
		map.put("softVer", SystemInfo.softVer);
		map.put("platform", SystemInfo.platform);
		map.put("sid", SystemInfo.sid);
		map.put("machId", SystemInfo.machId);
		map.put("cmd", cmd);
		map.put("width", SystemInfo.width + "");
		map.put("height", SystemInfo.height + "");
		map.put("md5", md5);
		map.put("func", func);
		map.put("msg", msg);
		/**最终通过一个参数msg把json数据发送给服务器**/
		RequestParams params = new RequestParams();
		String jsonStr = JsonUtils.toJsonStr(map);
		params.put("msg", jsonStr);
		LogUtil.DefalutLog("+++send msg:" + jsonStr);
		return params;
	}
	
	/**
	 * MD5加密
	 * 
	 * @param cmd
	 * @param func
	 * @param msg
	 * @return
	 */
	private static String getMd5String(Context mContext, String cmd, String func, String msg) {
		if(TextUtils.isEmpty(SystemInfo.sid)){
			SystemInfo.initSystemInfo(mContext);
		}
		StringBuilder sbBuilder = new StringBuilder();
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.add(cmd);
		resultList.add(SystemInfo.height + "");
		resultList.add(SystemInfo.machId);
		resultList.add(msg);
		resultList.add(SystemInfo.platform);
		resultList.add(SystemInfo.sid);
		resultList.add(SystemInfo.softVer);
		resultList.add(SystemInfo.sysVer);
		resultList.add(SystemInfo.width + "");
		resultList.add(key);// MD5加密
		for (String item : resultList) {
			sbBuilder.append(item);
		}
		String md5Str = sbBuilder.toString();
		return Md5.d5(md5Str);
	}
	
	/**下载apk专用，因为AsyncHttp默认使用gzip压缩，在有些机子上下载内容的totalsize为-1，更新进度无法显示，需要改为identity.
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void getAPk(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		new AsyncHttpClientForDownload().get(url, params, responseHandler);
	}

}
