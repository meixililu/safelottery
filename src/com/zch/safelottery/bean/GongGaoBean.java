package com.zch.safelottery.bean;


public class GongGaoBean implements SafelotteryType {
	//首页+ "?type=4"  竞足+ "?type=2" 竞蓝+ "?type=3"
	
	public static final String BeidanGG = "1";
	public static final String JczqGG = "2";
	public static final String JclqGG = "3";
	public static final String ShouyeGG = "4";
	/**编号**/
	private String id;
	/**类型**/
	private String type;
	/**标题**/
	private String title;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
