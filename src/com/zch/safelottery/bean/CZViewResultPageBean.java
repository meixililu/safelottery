package com.zch.safelottery.bean;

public class CZViewResultPageBean {
	/** 场次 **/
	private String id;	
	/** 主队 **/
	private String home_team;
	/** 客队 **/
	private String guest_team;
	/** 让球、让分 **/
	private String rangqiu_fen;
	/** 预设总分 **/
	private String yushe_result;
	/** 全场比分 **/
	private String score;
	/** 半场比分 **/
	private String bc_score;
	/** 总进球数、总分 **/
	private String total_score;
	/** 赛果 **/
	private String result;
	/** 投注内容 **/
	private String scheme;
	/** 是否设胆 **/
	private boolean hasDan;
	/** 开奖sp **/
	private String result_sp;
	
	public CZViewResultPageBean(){}
	
	public CZViewResultPageBean(String home_team,String guest_team, String score,
			 String result, String scheme, boolean hasDan) {
		super();
		this.score = score;
		this.home_team = home_team;
		this.guest_team = guest_team;
		this.result = result;
		this.scheme = scheme;
		this.hasDan = hasDan;
	}
	public CZViewResultPageBean(String id, String home_team,String guest_team, String rangqiu_fen, String yushe_result,
			String score,String bc_result, String total_score, String result, String scheme, boolean hasDan, String result_sp) {
		super();
		this.id = id;
		this.home_team = home_team;
		this.guest_team = guest_team;
		this.rangqiu_fen = rangqiu_fen;
		this.yushe_result = yushe_result;
		this.score = score;
		this.bc_score = bc_result;
		this.total_score = total_score;
		this.result = result;
		this.scheme = scheme;
		this.hasDan = hasDan;
		this.result_sp = result_sp;
	}

	@Override
	public String toString() {
		return "CZViewResultPageBean [id=" + id + ", home_team=" + home_team
				+ ", guest_team=" + guest_team + ", rangqiu_fen=" + rangqiu_fen
				+ ", yushe_result=" + yushe_result + ", score=" + score
				+ ", bc_score=" + bc_score + ", total_score=" + total_score
				+ ", result=" + result + ", scheme=" + scheme + ", hasDan="
				+ hasDan + ", result_sp=" + result_sp + "]";
	}
	
	public String getBc_score() {
		return bc_score;
	}
	public void setBc_score(String bc_score) {
		this.bc_score = bc_score;
	}
	public String getResult_sp() {
		return result_sp;
	}
	public void setResult_sp(String result_sp) {
		this.result_sp = result_sp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBc_result() {
		return bc_score;
	}
	public void setBc_result(String bc_result) {
		this.bc_score = bc_result;
	}
	public String getHome_team() {
		return home_team;
	}
	public boolean isHasDan() {
		
		return hasDan;
	}
	public String getRangqiu() {
		return rangqiu_fen;
	}
	public String getRangqiu_fen() {
		return rangqiu_fen;
	}
	public void setRangqiu_fen(String rangqiu_fen) {
		this.rangqiu_fen = rangqiu_fen;
	}
	public String getTotal_score() {
		return total_score;
	}
	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}
	public void setRangqiu(String rangqiu) {
		this.rangqiu_fen = rangqiu;
	}
	public String getYushe_result() {
		return yushe_result;
	}
	public void setYushe_result(String yushe_result) {
		this.yushe_result = yushe_result;
	}
	public void setHasDan(boolean hasDan) {
		this.hasDan = hasDan;
	}
	public void setHome_team(String home_team) {
		this.home_team = home_team;
	}
	public String getGuest_team() {
		return guest_team;
	}
	public void setGuest_team(String guest_team) {
		this.guest_team = guest_team;
	}
	public String getScore() {
		return score.equals("")  ?  "vs":score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
}
