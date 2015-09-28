package com.zch.safelottery.lazyloadimage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.BuyLotteryActivity;
import com.zch.safelottery.activity.MessageCenterDetailsActivity;
import com.zch.safelottery.activity.SafeApplication;
import com.zch.safelottery.bean.BannerBean;
import com.zch.safelottery.bean.LoadingBgAndSalesResultBean;
import com.zch.safelottery.dialogs.GuideDialog;
import com.zch.safelottery.parser.BannerBeanParser;
import com.zch.safelottery.parser.JsonUtils;
import com.zch.safelottery.parser.LoadingBgAndSalesResultParser;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.TimeUtils;

public class ImageDownload {

	/**sd卡保存文件夹名称**/
	public static final String sdPath = "/zch/banner/";
	/**私有目录保存文件夹名称**/
	public static final String dataPath = "/data/data/com.zch.safelottery/zch/banner/";
	/**首页loading背景图片对应的key**/
	public static final String LoadingBgKey = "01";
	/**活动图片对应的key**/
	public static final String SalesKey = "02";
	/**首页定制界面活动背景图片对应的key**/
	public static final String CustomBgKey = "03";
	/**已经显示过的日期对应的key**/
	public static final String HasShowDateTime = "HasShowDateTime";
	/**首页引导界面是否显示对应的key**/
	public static final String HasShowIndexPageGuide = "HasShowIndexPageGuide";
	/**11选5首次引导key**/
	public static final String GuideKeyNK3 = "GuideKeyNK3";
	/**胆拖高频首次引导key**/
	public static final String DanTuoGaoPin = "DanTuoGaoPin";
	/**设置完成之后显示一次图片对应的key**/
	public static final String FinishFirstSetIndexPageGuide = "FinishFirstSetIndexPageGuide";
	/** 银联卡充值首次引导key **/
	public static final String PhoneCallGuideKey = "PhoneCallGuideKey";
	/** banner key **/
	public static final String BannerKey = "banner";
	/** system setting key **/
	public static final String SystemSettingKey = "SystemSettingKey";
	/** banner key **/
	public static final String MOTU_TIP_BG = "motuTip";
	
	/**显示新手指引图片，给出图片，及显示位置
	 * @param mContext
	 * @param guideKey
	 * @param resourceId
	 */
	public static GuideDialog showIndexPageGuide(Context mContext,int resourceId, int gravity1, int gravity2, int x, int y){
		GuideDialog mGuideDialog = new GuideDialog(mContext,resourceId);
		mGuideDialog.setCancelable(true);
		mGuideDialog.setPopViewPosition(gravity1,gravity2,x,y);
		mGuideDialog.show();
		return mGuideDialog;
	}
	
	/**首页位置第一次引导，只展示一次的dialog，给出图片，及显示位置
	 * @param mContext
	 * @param guideKey
	 * @param resourceId
	 */
	public static void showIndexPageGuide(Context mContext,String guideKey, int resourceId, int gravity1, int gravity2, int x, int y){
		SharedPreferences sharedPrefs = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		if(!sharedPrefs.getBoolean(guideKey, false)){
			GuideDialog mGuideDialog = new GuideDialog(mContext,resourceId);
			mGuideDialog.setCancelable(true);
			mGuideDialog.setPopViewPosition(gravity1,gravity2,x,y);
			mGuideDialog.show();
			Editor editor = sharedPrefs.edit();
			editor.putBoolean(guideKey,true);
			editor.commit();
		}
	}
	
	
	  /**
	   *首页位置第一次引导，只展示一次的dialog，给出图片，及显示位置 
	   * @param mContext
	   * @param guideKey
	   * @param style
	   * @param resourceId
	   * @param gravity1
	   * @param gravity2
	   * @param x
	   * @param y
	   */
	public static void showIndexPageGuide(Context mContext,String guideKey, int style,int resourceId, int gravity1, int gravity2, int x, int y){
		SharedPreferences sharedPrefs = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
		if(!sharedPrefs.getBoolean(guideKey, false)){
			GuideDialog mGuideDialog = new GuideDialog(mContext,style,resourceId);
			mGuideDialog.setCancelable(true);
			mGuideDialog.setPopViewPosition(gravity1,gravity2,x,y);
			mGuideDialog.show();
			Editor editor = sharedPrefs.edit();
			editor.putBoolean(guideKey,true);
			editor.commit();
		}
	}
	
//	/**显示主页活动图片
//	 * @param mContext
//	 */
//	public static void showSalesActivity(Context mContext){
//		SharedPreferences sharedPrefs = mContext.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE);
//		if(isNeedToShowSalesActivity(sharedPrefs)){
//			BannerBean loadBean = ImageDownload.getLoadingBgAndSalesResultBean(SalesKey, sharedPrefs);
//			if(loadBean != null){
//				/**状态;0无效 1有效**/
//				if(isNeedToShowNewImg(loadBean)){
//					Bitmap mBitmap = getLoadingBg(loadBean);
//					if(mBitmap != null){
//						ImageDialog mImageDialog = new ImageDialog(mContext,loadBean,mBitmap);
//						mImageDialog.show();
//						saveJustShowSalesDate(sharedPrefs);
//					}else{
//						/**有配置信息但没有图片，应该是图片被删除了，删除配置信息重新下载图片**/
//						deleLoadingConfig(SalesKey, sharedPrefs);
//					}
//				}
//			}
//		}
//	}
	
	/**是否在有效期之内，状态是否是显示；
	 * @param loadBean
	 * @return
	 */
//	public static boolean isNeedToShowNewImg(BannerBean loadBean){
//		try{
//			long startTime = TimeUtils.getDateToTime(loadBean.getStartTime());
//			long endTime = TimeUtils.getDateToTime(loadBean.getEndTime());
//			if(loadBean.getStatus().equals("1") && (System.currentTimeMillis()>startTime)
//					&& (System.currentTimeMillis()<endTime)){
//				return true;
//			}
//		}catch (Exception e) {
//			LogUtil.ExceptionLog("isNeedToShowNewImg");
//		}
//		return false;
//	}

	
	/**销售活动一天显示一次策略判断，今天是否已经显示
	 * @param sharedPrefs
	 * @return
	 */
	public static boolean isNeedToShowSalesActivity(SharedPreferences sharedPrefs){
		String alreadyShowDate = sharedPrefs.getString(HasShowDateTime,"");
		String today = TimeUtils.getTimeDate(System.currentTimeMillis());
		if(alreadyShowDate.equals(today)){
			return false;
		}else{
			return true;
		}
	}
	
	/**销售活动一天显示一次,保存当前已经显示的日期
	 * @param sharedPrefs
	 */
	public static void saveJustShowSalesDate(SharedPreferences sharedPrefs){
		Editor editor = sharedPrefs.edit();
		editor.putString(HasShowDateTime, TimeUtils.getTimeDate(System.currentTimeMillis()));
		editor.commit();
	}
	
	/**显示首页loading图片
	 * @param layout
	 * @param sharedPrefs
	 * @throws Exception 
	 */
	public static void showLoadingBg(LinearLayout layout, SharedPreferences sharedPrefs) throws Exception{
		BannerBean loadBean = ImageDownload.getLoadingBgAndSalesResultBean(LoadingBgKey, sharedPrefs);
		if(loadBean != null){
			/**状态;0无效 1有效**/
//			if(isNeedToShowNewImg(loadBean)){
				Bitmap mBitmap = getLoadingBg(loadBean);
				if(mBitmap != null){
					Drawable drawable = new BitmapDrawable(mBitmap); 
					layout.setBackgroundDrawable(drawable);
				}else{
					/**有配置信息但没有图片，应该是图片被删除了，删除配置信息重新下载图片**/
					deleLoadingConfig(LoadingBgKey, sharedPrefs);
				}
//			}
		}
	}
	
	/**显示定制界面活动图片
	 * @param layout
	 * @param sharedPrefs
	 */
	public static View showBanner(final Context mContext,RelativeLayout buy_lottery_ad, final BannerBean mBannerBean, 
			SharedPreferences sharedPrefs){
		try{
			if(mBannerBean != null){
				FrameLayout itemView = new FrameLayout(mContext);
				ViewGroup.LayoutParams lParamsItem = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				itemView.setLayoutParams(lParamsItem);
				ImageView mImageView = new ImageView(mContext);
				Bitmap mBitmap = getLoadingBg(mBannerBean);
				if(mBitmap != null){
					Drawable drawable = new BitmapDrawable(mBitmap); 
					int width = mBitmap.getWidth();
					int height = mBitmap.getHeight();
					int newHeight = zoomImage(width,height);
					ViewGroup.LayoutParams lParams = new LayoutParams(SystemInfo.width, newHeight);
					mImageView.setLayoutParams(lParams);
					mImageView.setBackgroundDrawable(drawable);
					itemView.addView(mImageView);
					
					ViewGroup.LayoutParams parentLayout = new LayoutParams(SystemInfo.width, newHeight);
					buy_lottery_ad.setLayoutParams(parentLayout);	
					
					LinearLayout coverItem = new LinearLayout(mContext);
					ViewGroup.LayoutParams lParamscover = new LayoutParams(SystemInfo.width, newHeight);
					coverItem.setLayoutParams(lParamscover);
					coverItem.setBackgroundResource(R.drawable.bg_btn_none_to_gray_color_selector);
					coverItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(mContext,MessageCenterDetailsActivity.class);
							intent.putExtra("nid", mBannerBean.getId());
							intent.putExtra("Simple_title", mBannerBean.getSimple_title());
							mContext.startActivity(intent);
							StatService.onEvent(mContext, "app-banner", "首页-banner", 1);
						}
					});
					itemView.addView(coverItem);
					return itemView;
				}else{
					/**有配置信息但没有图片，应该是图片被删除了，删除配置信息重新下载图片**/
					deleLoadingConfig(BannerKey + mBannerBean.getId(), sharedPrefs);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**按屏幕分辨率扩展图片
	 * @param width
	 * @param height
	 * @return
	 */
	public static int resetImgHight(int width, int height){
		double newHeight = ((double)SystemInfo.width) / width * height;
		return (int)newHeight;
	}
	
	/**按图片的比例放大缩小图片
	 * @param width
	 * @param height
	 * @return
	 */
	public static int zoomImage(int width, int height){
		double newHeight = ((double)SystemInfo.width) / ((double)width / height);
		return (int)newHeight;
	}
	
	/**按图片的比例放大缩小图片
	 * @param width
	 * @param height
	 * @return
	 */
	public static int zoomImage(int screen, int width, int height){
		double newHeight = ((double)screen) / ((double)width / height);
		return (int)newHeight;
	}
	
	/**
	 * 下载图片
	 * @param path 地址
	 * @return 图片
	 * @throws Exception
	 */
	public static void getImagesFromWeb(BannerBean bean, SharedPreferences sharedPrefs) throws Exception {
		String urlStr = bean.getImageUrl();
		LogUtil.DefalutLog("---getImagesFromWeb:"+urlStr);
		if(!TextUtils.isEmpty(urlStr)){
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(20 * 1000);
			conn.setReadTimeout(20 * 1000);
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inStream = conn.getInputStream();
				byte[] data = readStream(inStream);
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap          
				saveBitmap(bean, bitmap, sharedPrefs);
			}
		}
	}
	
	/**保存图片
	 * @param name
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveBitmap(BannerBean bean, Bitmap bitmap, SharedPreferences sharedPrefs) throws IOException{
		String path = getSDPath();
		if(!TextUtils.isEmpty(path)){
			saveMyBitmapToSD(bean, path, bitmap, sharedPrefs);
		}else{
			saveMyBitmapToMemory(bean, bitmap, sharedPrefs);
		}
	}
	
	/** 判断SD卡是否插入 **/
	public static String getSDPath() {
		File SDdir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals( android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			SDdir = Environment.getExternalStorageDirectory();
		}
		if (SDdir != null) {
			return SDdir.getPath();
		} else {
			return null;
		}
	}
	
	/** 保存图片到SD卡 **/
	public static void saveMyBitmapToSD(BannerBean bean, String path, Bitmap bmp, SharedPreferences sharedPrefs)
			throws IOException {
		File cacheDir = new File(path + sdPath);
		if(!cacheDir.exists())
            cacheDir.mkdirs();
		File imgFile = new File(path + sdPath + bean.getId() + ".png");
		imgFile.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(imgFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			bean.setStorePosition(1);//1是保存到sd卡
			bean.setFilePath(imgFile.getPath());
			saveLoadingConfig(bean,sharedPrefs);
		} catch (Exception e) {
			e.printStackTrace();
			fOut.flush();
			fOut.close();
		}
	}
	
	/** 保存图片到手机内存 **/
	public static void saveMyBitmapToMemory(BannerBean bean, Bitmap bmp, SharedPreferences sharedPrefs)
			throws IOException {

		/**程序私有目录**/
		File cacheDir = new File(dataPath);
		if(!cacheDir.exists())
            cacheDir.mkdirs();
		File f = new File(dataPath + bean.getId() + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			bean.setStorePosition(2);//2是保存到程序私有目录
			saveLoadingConfig(bean,sharedPrefs);
		} catch (Exception e) {
			e.printStackTrace();
			fOut.flush();
			fOut.close();
		} 
	}
	
	/**
	 * 将图片数据流转成byte数组
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	private static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			inStream.close();
		} catch (Exception e) {
			outStream.close();
			inStream.close();
			e.printStackTrace();
		}
		return outStream.toByteArray();
	}    
	
	/**
	 * 保存首页背景和活动的配置文件
	 */
	public static void saveLoadingConfig(BannerBean bean, SharedPreferences sharedPrefs){
		String content = JsonUtils.toJsonStr(bean);
		Editor editor = sharedPrefs.edit();
		editor.putString(BannerKey+bean.getId(), content);
		editor.commit();
	}
	
	/**
	 * 删除首页背景和活动的配置文件
	 */
	public static void deleLoadingConfig(String key, SharedPreferences sharedPrefs){
		Editor editor = sharedPrefs.edit();
		editor.remove(key);
		editor.commit();
	}
	
	/**获取首页背景和活动的配置文件，直接返回对应的bean
	 * @param key
	 * @param sharedPrefs
	 * @return
	 */
	public static BannerBean getLoadingBgAndSalesResultBean(String key, SharedPreferences sharedPrefs){
		BannerBean mBean = null;
		try {
			String beanStr = sharedPrefs.getString(BannerKey+key, "");
			if(!TextUtils.isEmpty(beanStr)){
				mBean = new BannerBeanParser().parse( JsonUtils.stringToJson(beanStr) );
			}
		} catch (JSONException e) {
			LogUtil.ExceptionLog("getIndexPageBannerBean");
			e.printStackTrace();
		}
		return mBean;
	}
	
	/**活动的配置文件，直接返回对应的bean
	 * @param key
	 * @param sharedPrefs
	 * @return
	 */
	public static LoadingBgAndSalesResultBean getSystemSettingResultBean(String key, SharedPreferences sharedPrefs){
		LoadingBgAndSalesResultBean mBean = null;
		try {
			String beanStr = sharedPrefs.getString(SystemSettingKey + key, "");
			if(!TextUtils.isEmpty(beanStr)){
				mBean = new LoadingBgAndSalesResultParser().parse( JsonUtils.stringToJson(beanStr) );
			}
		} catch (JSONException e) {
			LogUtil.ExceptionLog("getIndexPageBannerBean");
			e.printStackTrace();
		}
		return mBean;
	}
	
	/**返回保存的bitmap
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public static Bitmap getLoadingBg(BannerBean bean) throws Exception{
		String dir = bean.getFilePath();
		Bitmap bm = null;
		if(!TextUtils.isEmpty(dir)){
			File imgFile = new File(dir);
			if (imgFile.exists()) {//若该文件存在
			    bm = ImageUtil.getImage(dir);
			}
		}
		return bm;
	}
}
