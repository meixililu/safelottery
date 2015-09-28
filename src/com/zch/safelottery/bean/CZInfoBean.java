package com.zch.safelottery.bean;
/**
 * @author Messi
 *
 */
public class CZInfoBean implements SafelotteryType {
	
	/**场次号**/
	private String sn;
	/**联赛名称**/
	private String league;
	/**联赛图标路径**/
	private String icon_url;
	/**主队名称**/
	private String home_team;	
	/**客队名称**/
	private String guest_team;
	/**开赛时间**/
	private String playing_time;
	/**均赔:以#分隔 胜#平#负**/
	private String avgOdds;
	
	private String game_score;
	private int result_spf;
	private int result_goals;
	private String double_result;
	private int win = -1;
	private int tied = -1;
	private int lost = -1;
	/*是否设胆*/
	private boolean is_selectDan;
	private int select_num;
	public void clear(){
		win = -1;
		tied = -1;
		lost = -1;
		is_selectDan = false;
		select_num = 0;
	}
	
	public CZInfoBean(){}
	
	public CZInfoBean(String league, String icon_url, String home_team,String guest_team, String playing_time, String sp_value,
			 String game_score, int result_spf, int result_goals, String double_result) {
		super();
		this.league = league;
		this.icon_url = icon_url;
		this.home_team = home_team;
		this.guest_team = guest_team;
		this.playing_time = playing_time;
		this.game_score = game_score;
		this.result_spf = result_spf;
		this.result_goals = result_goals;
		this.double_result = double_result;
	}
	
	public String getSn() {
		return sn;
	}

	public String getAvgOdds() {
		return avgOdds;
	}

	public void setAvgOdds(String avgOdds) {
		this.avgOdds = avgOdds;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public boolean isIs_selectDan() {
		return is_selectDan;
	}

	public void setIs_selectDan(boolean is_selectDan) {
		this.is_selectDan = is_selectDan;
	}

	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getHome_team() {
		return home_team;
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
	public String getPlaying_time() {
		return playing_time;
	}
	public void setPlaying_time(String playing_time) {
		this.playing_time = playing_time;
	}
	public String getGame_score() {
		return game_score;
	}
	public void setGame_score(String game_score) {
		this.game_score = game_score;
	}
	public int getResult_spf() {
		return result_spf;
	}
	public void setResult_spf(int result_spf) {
		this.result_spf = result_spf;
	}
	public int getResult_goals() {
		return result_goals;
	}
	public void setResult_goals(int result_goals) {
		this.result_goals = result_goals;
	}
	public String getDouble_result() {
		return double_result;
	}
	public void setDouble_result(String double_result) {
		this.double_result = double_result;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getTied() {
		return tied;
	}
	public void setTied(int tied) {
		this.tied = tied;
	}
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}
	public boolean is_selectDan() {
		return is_selectDan;
	}
	public void setSelectDan(boolean select_all) {
		this.is_selectDan = select_all;
	}
	public int getSelect_num() {
		return select_num;
	}
	public void setSelect_num(int select_num) {
		this.select_num = select_num;
	}
}
