package com.zch.safelottery.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zch.safelottery.lazyloadimage.ImageDownload;
import com.zch.safelottery.setttings.SystemInfo;

public class ImageUtil {

	
	  /**
	   * 获取中奖记录卡片
	   * @param context Context对象
	   * @param bonus　奖金
	   * @param name　姓名
	   * @param type　彩种
	   * @param playName 玩法
	   * @param cost 投注金额
	   * @param percent　回报率
	   * @param time　投注时间
	   * @return 拼好的Bitmap对象
	   */
	public static Bitmap getBonusBitmap(Context context,String bonus, String name, String type, String playName, String cost, String percent, String time) {
		Bitmap bg = getImageFromAssetsFile(context, "bonus_card_share_bg.png");
		Bitmap output = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		// 设置中奖金额文字格式
		final Paint paint = new Paint();
		paint.setAntiAlias(true);

		canvas.drawBitmap(bg, 0, 0, paint);
		paint.setColor(Color.WHITE);
		float textSize = 60;
		paint.setFakeBoldText(true);
		paint.setTextSize(textSize);
		float bonusWidth = paint.measureText(bonus);
		float bonusLeft = (bg.getWidth() - bonusWidth) / 2;

		FontMetrics fontMetrics = paint.getFontMetrics();
		// 计算文字高度
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		float height = 465;
		// 计算文字baseline
		canvas.drawText(bonus, bonusLeft, height + fontHeight/2, paint);

		// 设置详情文字格式
		textSize = 30;
		paint.setColor(Color.argb(0xAA, 0x5e, 0x5e, 0x5e));
		paint.setFakeBoldText(false);
		paint.setTextSize(textSize);
		float x = 35;
		float textYArr[] = { 552, 602, 650, 697, 747, 795 };
		
		fontMetrics = paint.getFontMetrics();
		// 计算文字高度
		fontHeight = fontMetrics.bottom - fontMetrics.top;
		float offset = fontHeight/2;

		canvas.drawText("姓名：" + name, x , textYArr[0]+ offset, paint);
		canvas.drawText("彩种：" + type, x, textYArr[1]+ offset, paint);
		canvas.drawText("玩法：" + playName, x, textYArr[2]+ offset, paint);
		canvas.drawText("投注金额：" + cost, x, textYArr[3]+ offset, paint);
		canvas.drawText("回报率：" + percent, x, textYArr[4]+ offset, paint);
		canvas.drawText("投注时间：" + time, x, textYArr[5]+ offset, paint);

		return output;
	}
	/**
	 * 取Assets 里的图片
	 * 
	 * @param mContext
	 *            当前this
	 * @param fileName
	 *            文件地址
	 * @return 图片
	 */
	public static Bitmap getImageFromAssetsFile(Context mContext, String fileName) {
		Bitmap image = null;
		AssetManager am = mContext.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	public static void adjustImgSize(Bitmap mBitmap, LinearLayout layout) {
		if (mBitmap != null && layout != null) {
			Drawable drawable = new BitmapDrawable(mBitmap);
			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();
			int newHeight = ImageDownload.zoomImage(width, height);
			ViewGroup.LayoutParams lParams = layout.getLayoutParams();
			lParams.width = SystemInfo.width;
			lParams.height = newHeight;
			layout.setBackgroundDrawable(drawable);
		}
	}

	public static void adjustImgSize(Bitmap mBitmap, LinearLayout layout, int margin) {
		if (mBitmap != null && layout != null) {
			Drawable drawable = new BitmapDrawable(mBitmap);
			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();
			int screen = SystemInfo.width - margin;
			int newHeight = ImageDownload.zoomImage(screen, width, height);
			ViewGroup.LayoutParams lParams = layout.getLayoutParams();
			lParams.width = screen;
			lParams.height = newHeight;
			layout.setBackgroundDrawable(drawable);
		}
	}

	public static Bitmap getImage(String path) throws Exception {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);// fis.
		byte[] imagebytes = getBytes(fis);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		if (file.length() < 20480) { // 0-20k
			opt.inSampleSize = 1;
		} else if (file.length() < 51200) { // 20-50k
			opt.inSampleSize = 1;
		} else if (file.length() < 307200) { // 50-300k
			opt.inSampleSize = 1;
		} else if (file.length() < 819200) { // 300-800k
			opt.inSampleSize = 2;
		} else if (file.length() < 1048576) { // 800-1024k
			opt.inSampleSize = 4;
		} else {
			opt.inSampleSize = 10;
		}
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length, opt);
		return bitmap;
	}

	public static byte[] getBytes(InputStream in) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		in.close();
		bos.flush();
		byte[] result = bos.toByteArray();
		return result;
	}

	public static String saveBitmap(Context context, Bitmap bitmap,String fileName) {
		String dir = "/DCIM/";
		try {
			String sdcardDir = SDcardUtil.isDirExits(context, dir);
			if (!TextUtils.isEmpty(sdcardDir)) {
				String filePath = sdcardDir + fileName;
				File file = new File(filePath);
				file.createNewFile();
				FileOutputStream out;
				out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
					out.flush();
					out.close();
				}
				return filePath;
			} else {
				ToastUtil.diaplayMesShort(context, "请插入SD卡");
				return "";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return "";
		}

	}

	/**compress image less than 100K
	 * @param image
	 * @return
	 */
	public static ByteArrayInputStream compressImage(String path) {
		ByteArrayInputStream isBm = null;
		try {
			Bitmap image = ImageUtil.getImage(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
			}
			isBm = new ByteArrayInputStream(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isBm;
	}

	/**
	  * 从网络、或者本地资源、本地路径获取图片
	  * @param context 上下文
	  * @param resId　资源id
	  * @param urlOrPath 本地路径或者网络
	  * @return Bitmap
	* @throws IOException 
	* @throws MalformedURLException 
	  */
	public static Bitmap getBitmap(Context context, int resId, String urlOrPath) {
		Bitmap bm = null;
		if (resId > 0) {// 本地资源图片
			bm = BitmapFactory.decodeResource(context.getResources(), resId);
		} else if (!TextUtils.isEmpty(urlOrPath)) {
			if (urlOrPath.startsWith("http")) {// 网络图片
				try {
					URL url = new URL(urlOrPath);
					bm = BitmapFactory.decodeStream(url.openStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { // 本地图片路径
				File file = new File(urlOrPath);
				if (!file.exists()) {// 图片不存在
					Toast.makeText(context, " path = " + urlOrPath, Toast.LENGTH_LONG).show();
					return null;
				} else {// 图片存在
					bm = BitmapFactory.decodeFile(urlOrPath);
				}
			}
		}
		return bm;
	}

}
