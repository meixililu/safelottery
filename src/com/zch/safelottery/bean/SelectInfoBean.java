package com.zch.safelottery.bean;

import com.zch.safelottery.util.LotteryId;

/**
 * SelectInfoBean用于机选 添加一注
 * @author Jiang
 */
public class SelectInfoBean {
	private String lotteryId = LotteryId.All;
	private int sumView; //View的个数
	private int initNum;// 初始值
	private int item = 1;// 初始注数
	private int[] sumBall;  // 总球数 数组的长度对应View个数
	private int[] selectBall;// 选择的球数 数组的长度对应View个数
	private boolean repeat;// 是否可重复
	private String playId;
	private String pollId; //单复式的情况下为空 "01"  "02"
	private String[] split; //固定两个 从最里层开始
	
	public String getPollId() {
		return pollId;
	}
	public void setPollId(String pollId) {
		this.pollId = pollId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getPlayId() {
		return playId;
	}
	public void setPlayId(String playId) {
		this.playId = playId;
	}
	public String[] getSplit() {
		return split;
	}
	public void setSplit(String[] split) {
		this.split = split;
	}
	public int getSumView() {
		return sumView;
	}
	public void setSumView(int sumView) {
		this.sumView = sumView;
	}
	public int[] getSumBall() {
		return sumBall;
	}
	public void setSumBall(int[] sumBall) {
		this.sumBall = sumBall;
	}
	public int[] getSelectBall() {
		return selectBall;
	}
	public void setSelectBall(int[] selectBall) {
		this.selectBall = selectBall;
	}
	public int getInitNum() {
		return initNum;
	}
	public void setInitNum(int initNum) {
		this.initNum = initNum;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}	
}
