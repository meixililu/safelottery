package com.zch.safelottery.bean;

public class NoticeBean implements SafelotteryType {

	private String id;// 文章id
	private String title;// 标题
	private String cover;// 封面地址
	/**minisite simple_title**/
	private String simple_title;
	private String article_category_id;// 类别id
	private String article_category;// 类别
	private String pub_datetime;// 发布时间
	private String expiration_time;// 结束时间
	private String startTime;// 消息发布时间
	private String endTime;// 消息结束时间
	private String expired = "0";// 0为有效 1为失效
	private String isclick = "1";// 0为点击 1为未点击
	private String isReaded = "0";// 0:未读 1:已读
	private String abstracts;// 摘要
	private String context;// 内容
	private String description;// 简介
	private String type;// 消息类型1-优惠码

	public NoticeBean() {
	}
	public String getSimple_title() {
		return simple_title;
	}

	public void setSimple_title(String simple_title) {
		this.simple_title = simple_title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArticle_category_id() {
		return article_category_id;
	}

	public void setArticle_category_id(String article_category_id) {
		this.article_category_id = article_category_id;
	}

	public String getArticle_category() {
		return article_category;
	}

	public void setArticle_category(String article_category) {
		this.article_category = article_category;
	}

	public String getPub_datetime() {
		return pub_datetime;
	}

	public void setPub_datetime(String pub_datetime) {
		this.pub_datetime = pub_datetime;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public String getIsclick() {
		return isclick;
	}

	public void setIsclick(String isclick) {
		this.isclick = isclick;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getExpiration_time() {
		return expiration_time;
	}

	public void setExpiration_time(String expiration_time) {
		this.expiration_time = expiration_time;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIsReaded() {
		return isReaded;
	}

	public void setIsReaded(String isReaded) {
		this.isReaded = isReaded;
	}

	public String getAbstract() {
		return abstracts;
	}

	public void setAbstract(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "NoticeBean [id=" + id + ", title=" + title + ", cover=" + cover + ", simple_title=" + simple_title + ", article_category_id="
				+ article_category_id + ", article_category=" + article_category + ", pub_datetime=" + pub_datetime + ", expiration_time="
				+ expiration_time + ", startTime=" + startTime + ", endTime=" + endTime + ", expired=" + expired + ", isclick=" + isclick
				+ ", isReaded=" + isReaded + ", abstracts=" + abstracts + ", context=" + context + ", description=" + description + ", type=" + type
				+ "]";
	}


}
