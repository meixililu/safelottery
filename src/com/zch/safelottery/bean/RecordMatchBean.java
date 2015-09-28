package com.zch.safelottery.bean;

import java.util.List;

public class RecordMatchBean implements SafelotteryType{
	
	/*
	 *  方案详情全部节点 
	 * member json格式 用户信息
	 * programs json格式 方案信息
	 * mainIssue json格式 期次信息
	 * subGameList json格式 传统足彩对阵信息
	 * match json格式 竞技彩对阵信息
	 */
	
	//match节点数据
	
	private String guoGuan; //串关方式
	private String isAllEnd; //比赛是否全部结束
	private String isSingle; //是否单关
	private String isSingleFloat; //是否单关浮动
	private List<RecordMatchListBean> matchList; //选择的比赛列表
	public String getGuoGuan() {
		return guoGuan;
	}
	public void setGuoGuan(String guoGuan) {
		this.guoGuan = guoGuan;
	}
	public String getIsAllEnd() {
		return isAllEnd;
	}
	public void setIsAllEnd(String isAllEnd) {
		this.isAllEnd = isAllEnd;
	}
	public String getIsSingle() {
		return isSingle;
	}
	public void setIsSingle(String isSingle) {
		this.isSingle = isSingle;
	}
	public String getIsSingleFloat() {
		return isSingleFloat;
	}
	public void setIsSingleFloat(String isSingleFloat) {
		this.isSingleFloat = isSingleFloat;
	}
	public List<RecordMatchListBean> getMatchList() {
		return matchList;
	}
	public void setMatchList(List<RecordMatchListBean> matchList) {
		this.matchList = matchList;
	}
	@Override
	public String toString() {
		return "RecordMatchListBean [guoGuan=" + guoGuan + ", isAllEnd=" + isAllEnd + ", isSingle=" + isSingle + ", isSingleFloat=" + isSingleFloat + ", matchList=" + matchList + "]";
	}
}
