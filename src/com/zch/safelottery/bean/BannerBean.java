package com.zch.safelottery.bean;


public class BannerBean implements SafelotteryType{

	/**图片地址 cover**/
	private String cover;
	/**minisite simple_title**/
	private String simple_title;
	/**文章id**/
	private String id;
	/**图片保存位置：1是sd卡，2是程序私有目录**/
	private int storePosition;
	/**图片最终保存位置**/
	private String filePath;
	/**是否点击**/
	private String isclick="1";//0为点击 1为未点击
	/**文字类型id**/
	private String article_category_id;
	
	public String getSimple_title() {
		return simple_title;
	}
	public void setSimple_title(String simple_title) {
		this.simple_title = simple_title;
	}
	public String getArticle_category_id() {
		return article_category_id;
	}
	public void setArticle_category_id(String article_category_id) {
		this.article_category_id = article_category_id;
	}
	public String getImageUrl() {
		return cover;
	}
	public void setImageUrl(String imageUrl) {
		this.cover = imageUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStorePosition() {
		return storePosition;
	}
	public void setStorePosition(int storePosition) {
		this.storePosition = storePosition;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getIsclick() {
		return isclick;
	}
	public void setIsclick(String isclick) {
		this.isclick = isclick;
	}
}
