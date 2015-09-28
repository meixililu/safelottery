package com.zch.safelottery.bean;

public class PullMsgBean implements SafelotteryType{

	//消息id
    private long messgeId;
    //是否私有消息 1 私用消息 0 公共消息
    private int isPrivate;
    //消息类型
    private String type;
    //链接url
    private String linkUrl;
    //图片url
    private String imageUrl;
    //消息标题
    private String title;
    //消息内容
    private String content;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getMessgeId() {
		return messgeId;
	}
	public void setMessgeId(long messgeId) {
		this.messgeId = messgeId;
	}
	public int getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
