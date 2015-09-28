package com.zch.safelottery.bean;

public class RecordMatchListBean implements SafelotteryType{
	
	//match节点下matchList节点字符串数据
	
	private String playCode; //玩法编号
	private String matchNo; //赛事编号
	private String openTime; //开赛时间
	private String mainTeam; //主队
	private String guestTeam; //客队
	private String mainTeamHalfScore; //主队半场得分
	private String guestTeamHalfScore; //客队半场得分
	private String mainTeamScore; //主队全场得分
	private String guestTeamScore; //主队全场得分
	private String letBall; //让球(分)
	private String preCast; //预设总分
	private String number; //投注号码
	private String result; //赛果
	private String isSelect; //是否命中
	private String dan; //是否定胆
	private String isSelectDan; //胆是否命中
	
	
	//以下为猜冠军属性
	private String sp;
	private String bonusStatus;
	private String match;
	private String matchName;
	private String bonusAmount;
	private String multiple;
	
	public String getPlayCode() {
		return playCode;
	}
	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}
	public String getMatchNo() {
		return matchNo;
	}
	public void setMatchNo(String matchNo) {
		this.matchNo = matchNo;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getMainTeam() {
		return mainTeam;
	}
	public void setMainTeam(String mainTeam) {
		this.mainTeam = mainTeam;
	}
	public String getGuestTeam() {
		return guestTeam;
	}
	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}
	public String getMainTeamHalfScore() {
		return mainTeamHalfScore;
	}
	public void setMainTeamHalfScore(String mainTeamHalfScore) {
		this.mainTeamHalfScore = mainTeamHalfScore;
	}
	public String getGuestTeamHalfScore() {
		return guestTeamHalfScore;
	}
	public void setGuestTeamHalfScore(String guestTeamHalfScore) {
		this.guestTeamHalfScore = guestTeamHalfScore;
	}
	public String getMainTeamScore() {
		return mainTeamScore;
	}
	public void setMainTeamScore(String mainTeamScore) {
		this.mainTeamScore = mainTeamScore;
	}
	public String getGuestTeamScore() {
		return guestTeamScore;
	}
	public void setGuestTeamScore(String guestTeamScore) {
		this.guestTeamScore = guestTeamScore;
	}
	public String getLetBall() {
		return letBall;
	}
	public void setLetBall(String letBall) {
		this.letBall = letBall;
	}
	public String getPreCast() {
		return preCast;
	}
	public void setPreCast(String preCast) {
		this.preCast = preCast;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
	public String getDan() {
		return dan;
	}
	public void setDan(String dan) {
		this.dan = dan;
	}
	public String getIsSelectDan() {
		return isSelectDan;
	}
	public void setIsSelectDan(String isSelectDan) {
		this.isSelectDan = isSelectDan;
	}
	
	
	public String getSp() {
		return sp;
	}
	public void setSp(String sp) {
		this.sp = sp;
	}
	public String getBonusStatus() {
		return bonusStatus;
	}
	public void setBonusStatus(String bonusStatus) {
		this.bonusStatus = bonusStatus;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public String getBonusAmount() {
		return bonusAmount;
	}
	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	@Override
	public String toString() {
		return "RecordMatchListBean [playCode=" + playCode + ", matchNo=" + matchNo + ", openTime=" + openTime + ", mainTeam=" + mainTeam
				+ ", guestTeam=" + guestTeam + ", mainTeamHalfScore=" + mainTeamHalfScore + ", guestTeamHalfScore=" + guestTeamHalfScore
				+ ", mainTeamScore=" + mainTeamScore + ", guestTeamScore=" + guestTeamScore + ", letBall=" + letBall + ", preCast=" + preCast
				+ ", number=" + number + ", result=" + result + ", isSelect=" + isSelect + ", dan=" + dan + ", isSelectDan=" + isSelectDan + ", sp="
				+ sp + ", bonusStatus=" + bonusStatus + ", match=" + match + ", matchName=" + matchName + ", bonusAmount=" + bonusAmount
				+ ", multiple=" + multiple + "]";
	}
	
}
