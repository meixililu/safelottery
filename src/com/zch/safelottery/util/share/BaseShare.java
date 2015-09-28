package com.zch.safelottery.util.share;

/**
 * 分享基类
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月24日
 */
public interface BaseShare {

	/**
	 * 分享类型
	 */
	public enum ShareType{
		//分享url地址
		URL,PICTURE;
	}
	
	void shareURL(String title,String desc,String targetUrl,String pictureUrl);
	void sharePicture(String localImagePath,String title,String desc);
}
