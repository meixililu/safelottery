package com.zch.safelottery.bean;


public class LoadingBgAndSalesResultBean implements SafelotteryType{

	private String platform;
	private String name;
	private String position;
	private String startTime;
	private String endTime;
	private String status;
	private String url;
	private String note;
	private String imageUrl;
	private String updateTime;
	private String isUrl;
	private int storePosition;
	private String filePath;
	
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public int getStorePosition() {
		return storePosition;
	}
	public void setStorePosition(int storePosition) {
		this.storePosition = storePosition;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsUrl() {
		return isUrl;
	}
	public void setIsUrl(String isUrl) {
		this.isUrl = isUrl;
	}
	@Override
	public String toString() {
		return "LoadingBgAndSalesResultBean [platform=" + platform + ", name="
				+ name + ", position=" + position + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", status=" + status + ", url="
				+ url + ", note=" + note + ", imageUrl=" + imageUrl
				+ ", updateTime=" + updateTime + ", isUrl=" + isUrl
				+ ", storePosition=" + storePosition + ", filePath=" + filePath
				+ "]";
	}
	
	
}
