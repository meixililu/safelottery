package com.zch.safelottery.bean;



public class BuyLotteryCustomItemBean {
	/**八个格子中的位置**/
	private int position;
	/**背景图资源id**/
	private int resourceId;
	/**类名全称**/
	private String className;
	/**页面名称**/
	private String name;
	/**彩种id**/
	private String lid;
	/**类型，1为标题，默认0为内容**/
	private int type;
	/**是否选中**/
	private boolean isSelected;
	
	public BuyLotteryCustomItemBean(){}
	
	public BuyLotteryCustomItemBean(String className, String name, int type){
		this.className = className;
		this.name = name;
		this.type = type;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

}
