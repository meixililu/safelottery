package com.zch.safelottery.setttings;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.bean.BannerBean;
import com.zch.safelottery.bean.LoadingBgAndSalesResultBean;
import com.zch.safelottery.combinebean.Result;
import com.zch.safelottery.database.NoticeDatabaseUtil;
import com.zch.safelottery.http.SafelotteryHttpClient;
import com.zch.safelottery.http.TypeResultHttpResponseHandler;
import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.parser.BannerBeanParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LoadingBgAndSalesResultParser;
import com.zch.safelottery.util.LogUtil;

/**
 * 配置信息获取
 * @author Messi
 *
 */
public class SLManifest {
	
	public static final String ConfigRequestCheckTime = "ConfigRequestCheckTime";
	public static final String LoadingBgAndSalesCheckTime = "LoadingBgAndSalesCheckTime";
	public static final String BaYuanBaoZhongKey = "kg_8yuanbaozhong";
	public static final String ShiJieBeiKey = "kg_shijiebeirukou";
	public static final String GuanJunWanFa = "kg_guanjunwanfa";
	public static final String NaoZhong = "kg_naozhong";
	public static final String ShouYeTanCeng = "kg_shouyetanceng";
	
	
	/**获取loading界面背景图及活动配置信息
	 * @param context
	 */
	public static void getLoadingBgAndSales(final Context context, final SharedPreferences sharedPrefs){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					postLoadingBgAndSalesRequest(context, sharedPrefs);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.DefalutLog("Exception-postConfigRequest()");
				}
			}
		}).start();
	}
	
	/**
	 * 获取首页广告数据 请求
	 * @param context
	 * @param sharedPrefs
	 * @throws Exception
	 */
	public static void postLoadingIndexPageAD(final Context context, final SharedPreferences sharedPrefs,
			final Result result,final Handler TaskHandle) throws Exception{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ArrayList<BannerBean> mResultBeanList = null;
					if(result != null){
						String code = result.getCode();
						if(!TextUtils.isEmpty(code) && code.equals(SystemInfo.succeeCode)){
							String resultStr = result.getResult();
							if(!TextUtils.isEmpty(resultStr)){
								HashMap<String,String> listMapStr = (HashMap<String, String>) JsonUtils.stringToMap(resultStr);
								if(listMapStr != null){
									String articleList = listMapStr.get("articleList");
									if(!TextUtils.isEmpty(articleList)){
										HashMap<String,String> articleListMap = (HashMap<String, String>) JsonUtils.stringToMap(articleList);
										String moduleList = articleListMap.get("module");
										if(!TextUtils.isEmpty(moduleList)){
											HashMap<String,String> moduleMap = (HashMap<String, String>) JsonUtils.stringToMap(moduleList);
											String positionsStr = moduleMap.get("positions");
											if(!TextUtils.isEmpty(positionsStr)){
												mResultBeanList = (ArrayList<BannerBean>) 
												JsonUtils.parserJsonArray(positionsStr, new BannerBeanParser());
												if(mResultBeanList != null){
													new NoticeDatabaseUtil(context).insertBannerList(mResultBeanList);
													for(BannerBean mResultBean : mResultBeanList){
														if(mResultBean != null){
															BannerBean old = ImageDownload.getLoadingBgAndSalesResultBean(mResultBean.getId(), sharedPrefs);
															/**是否存在，不存在直接下载**/
															if(old != null){
																String oldUrl = old.getImageUrl();
																String newUrl = mResultBean.getImageUrl();
																if(!TextUtils.isEmpty(oldUrl) && !TextUtils.isEmpty(newUrl)){
																	/**图片地址是否一样，或者还没有下载下来，直接下载**/
																	if(!old.getImageUrl().equals(mResultBean.getImageUrl()) || TextUtils.isEmpty(old.getFilePath())){
																		ImageDownload.getImagesFromWeb(mResultBean, sharedPrefs);
																	}else{
																		mResultBean.setFilePath(old.getFilePath());
																		mResultBean.setStorePosition(old.getStorePosition());
																	}
																}else{
																	ImageDownload.getImagesFromWeb(mResultBean, sharedPrefs);
																}
															}else{
																ImageDownload.getImagesFromWeb(mResultBean, sharedPrefs);
															}
														}
													}
													SafeApplication.dataMap.put("BannerList", mResultBeanList);
													Message message = new Message();
													message.what = 1;
													TaskHandle.sendMessage(message);
												}
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 发送 获取首页和活动的配置信息的 请求
	 * @param context
	 * @param sharedPrefs
	 * @throws Exception
	 */
	public static void postLoadingBgAndSalesRequest(Context context, final SharedPreferences sharedPrefs) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("length", SystemInfo.height+"");
		map.put("width", SystemInfo.width+"");
		SafelotteryHttpClient.post(context, "3002","",JsonUtils.toJsonStr(map), new TypeResultHttpResponseHandler(context,false) {
			@Override
			public void onSuccess(int statusCode, Result mResult) {
				Result result = mResult;
				if(result != null){
					String resultStr = result.getResult();
					if(!TextUtils.isEmpty(resultStr)){
						HashMap<String,String> listMapStr = (HashMap<String, String>) JsonUtils.stringToMap(resultStr);
						if(listMapStr != null){
							String mBaYuanBaoZhongKey = listMapStr.get(BaYuanBaoZhongKey);
							String mShiJieBeiKey = listMapStr.get(ShiJieBeiKey);
							String mGuanJunWanFa = listMapStr.get(GuanJunWanFa);
							String mNaoZhong = listMapStr.get(NaoZhong);
							String mShouYeTanCeng = listMapStr.get(ShouYeTanCeng);
							/**1为显示**/
							Settings.saveSharedPreferences(sharedPrefs, BaYuanBaoZhongKey, mBaYuanBaoZhongKey);
							Settings.saveSharedPreferences(sharedPrefs, ShiJieBeiKey, mShiJieBeiKey);
							Settings.saveSharedPreferences(sharedPrefs, GuanJunWanFa, mGuanJunWanFa);
							Settings.saveSharedPreferences(sharedPrefs, NaoZhong, mNaoZhong);
							Settings.saveSharedPreferences(sharedPrefs, ShouYeTanCeng, mShouYeTanCeng);
							/**下载服务器图片，暂时不用，先不删除，留着备用**/
//							String listStr = listMapStr.get("configList");
//							if(!TextUtils.isEmpty(listStr)){
//								ArrayList<LoadingBgAndSalesResultBean> mResultBeanList = (ArrayList<LoadingBgAndSalesResultBean>) JsonUtils.parserJsonArray(listStr, new LoadingBgAndSalesResultParser());
//								if(mResultBeanList != null){
//									for(LoadingBgAndSalesResultBean mResultBean : mResultBeanList){
//										if(mResultBean != null){
//											LoadingBgAndSalesResultBean old = ImageDownload.getSystemSettingResultBean(mResultBean.getPosition(), sharedPrefs);
//											/**是否存在，不存在直接下载**/
//											if(old != null){
//												/**版本是否一样，不一样直接下载**/
//												if(!old.getUpdateTime().equals(mResultBean.getUpdateTime())){
//													ImageDownload.getImagesFromWeb(mResultBean, sharedPrefs);
//												}
//											}else{
//												if( ImageDownload.isNeedToShowNewImg(mResultBean) ){
//													ImageDownload.getImagesFromWeb(mResultBean, sharedPrefs);
//												}
//											}
//										}
//									}
//								}
//							}
						}
					}
				}
			}
			@Override
			public void onFailure(int statusCode, String mErrorMsg) {
				
			}
		});
	}
	
	
	
	/**获取心跳配置信息
	 * @param context
	 */
	public static void getConfig(final Context context, final SharedPreferences sharedPrefs){
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				if(isNeedConfigRequest(context,sharedPrefs)){
//					try {
//						postConfigRequest(context);
//					} catch (Exception e) {
//						LogUtil.DefalutLog("Exception-postConfigRequest()");
//					}
//				}
//			}
//			
//		}).start();
	}
	/**检查是否需要读取心跳配置信息间隔时间
	 * @param context
	 * @return
	 */
	private static boolean isNeedConfigRequest(Context context,SharedPreferences sharedPrefs){
//		long spacingInterval = 1000 * 1;
		long spacingInterval = 1000 * 60 * 60 * 12;
		long lastCheckTime = sharedPrefs.getLong(ConfigRequestCheckTime, 0);
		if( System.currentTimeMillis()-lastCheckTime > spacingInterval ){
			return true;
		}else{
			return false;
		}
	}
	/**保存上一次读取心跳信息时间
	 * @param context
	 */
	private static void saveConfigRequestTime(Context context,String time){
		long lastRequestTime =0;
		try{
			lastRequestTime = Long.parseLong(time);
		}catch (Exception e) {
			lastRequestTime = System.currentTimeMillis();
		}
		SharedPreferences sharedPrefs = context.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putLong("ConfigRequestCheckTime", lastRequestTime);
		editor.commit();
	}
}
