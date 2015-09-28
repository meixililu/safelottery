package com.zch.safelottery.bean;

import java.util.ArrayList;

/**
 * @author Messi
 *
 */
public class LotteryInfoBean implements SafelotteryType {
	/**彩种id**/
	private String lotteryId;
	/**彩种是否停售,0 否,1 是**/
	private String isStop;
	/**彩种期次详情列表**/
	private ArrayList<IssueInfoBean> issueInfoList;
	/**彩种状态提示语，空为正常，其他直接提示即可**/
	private String prompt;
	/**彩种倒计时完了之后请求次数，允许失败几次**/
	private int httpRequestTime = 3;
	/**是否今日开奖**/
	private boolean isTodayLottery;
	
	public boolean isTodayLottery() {
		return isTodayLottery;
	}

	public void setTodayLottery(boolean isTodayLottery) {
		this.isTodayLottery = isTodayLottery;
	}

	public int getHttpRequestTime() {
		return httpRequestTime;
	}

	public void setHttpRequestTime(int httpRequestTime) {
		this.httpRequestTime = httpRequestTime;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getIsStop() {
		return isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}

	public ArrayList<IssueInfoBean> getIssueInfoList() {
		return issueInfoList;
	}

	public void setIssueInfoList(ArrayList<IssueInfoBean> issueInfoList) {
		this.issueInfoList = issueInfoList;
	}

	@Override
	public String toString() {
		return "LotteryInfoBean [lotteryId=" + lotteryId + ", isStop=" + isStop
				+ ", issueInfoList=" + issueInfoList + "]";
	}
	
}
