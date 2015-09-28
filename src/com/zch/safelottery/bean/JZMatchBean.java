package com.zch.safelottery.bean;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;

import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.util.JCBFUtils;

public class JZMatchBean implements SafelotteryType{

	/**场次号**/
	private String sn;
	/**国家名称**/
	private String name;
	/**世界杯**/
	private String league;
	/**期次**/
	private String issue;
	/**星期**/
	private String week;
	/**玩法编号**/
	private String playId;
	/**选号方式**/
	private String pollId;
	/**场次状态;0 初始,1 正常,2完成**/
	private String endOperator;
	/**联赛名称**/
	private String matchName;
	/**联赛图标**/
	private String imgPath;
	/**主队名称**/
	private String mainTeam;
	/**客队名称**/
	private String guestTeam;
	/**主队图标,竞彩篮球**/
	private String mainTeamImgPath;
	/**客队图标,竞彩篮球**/
	private String guestTeamImgPath;
	/**预设总分,竞彩篮球**/
	private String preCast;
	/**让球**/
	private String letBall;
	/**官方截止时间**/
	private String endTime;
	/**单式截止时间**/
	private String endDanShiTime;
	/**复式截止时间**/
	private String endFuShiTime;
	/**主队半场得分**/
	private String mainTeamHalfScore;
	/**客队半场得分**/
	private String guestTeamHalfScore;
	/**主队得分**/
	private String mainTeamScore;
	/**客队得分**/
	private String guestTeamScore;
	/** 竞足赛果 **/
	private String bqcspfResult;
	/** 竞蓝赛果 **/
	private String rfshResult;
	private String dxfResult;
	private String sfcResult;
	/**sp列表**/
	private String sp;
	/**胜平负玩法最终sp值**/
	private String winOrNegaSp;
	/**总进球数玩法最终sp值**/
	private String totalGoalSp;
	/**半全场玩法最终sp值**/
	private String halfCourtSp;
	/**比分玩法最终sp值**/
	private String scoreSp;
	
	/**均赔**/
	private String avgOdds;
	
	/**让分胜负玩法最终sp值,竞彩篮球**/
	private String letWinOrNegaSp;
	/**大小分玩法最终sp值,竞彩篮球**/
	private String bigOrLittleSp;
	/**胜分差玩法最终sp值,竞彩篮球**/
	private String winNegaDiffSp;
	
	/**混合过关sp Array 以及 混合过关每种玩法用户的选择项集合List**/
	private JSONArray spfJcArray;
	private JSONArray rqspfJcArray;
	private JSONArray qcbfJcArray;
	private JSONArray zjqsJcArray;
	private JSONArray bqcJcArray;
	public ArrayList<Integer> spfSelectedItem = new ArrayList<Integer>();
	public ArrayList<Integer> rqspfSelectedItem = new ArrayList<Integer>();
	public ArrayList<Integer> qcbfSelectedItem = new ArrayList<Integer>();
	public ArrayList<Integer> zjqsSelectedItem = new ArrayList<Integer>();
	public ArrayList<Integer> bqcSelectedItem = new ArrayList<Integer>();
	
	private boolean hasDan;//是否设胆
	public int count;//计数器
	private String userSelect;//用于存放用户选择的最终结果
	private String userSelectSp;//用于存放用户选择的最终X结果sp值，0:0/1:0/1/2:0，场次之间用：号，选项之间用/；
    private String numstr="";//显示所选结果
    public String[] pvList;//初始赔率集合
    private String userSelectBuy;//非混合过关 用于存放用户投注购买参数
    public ArrayList<String> userSelectBuyList = new ArrayList<String>();//混合过关 结果集合
    
    //非混合过关 使用的记录用户选择结果的集合
	public ArrayList<String> selectedList = new ArrayList<String>();//结果集合
	public ArrayList<String> selectedPvList = new ArrayList<String>();//选择赔率集合
	
	public void getFinalPVResult(){
		StringBuilder sbBuilder;

		//拼赔率
		sbBuilder = new StringBuilder();
		for(String str : selectedPvList){
			sbBuilder.append(str);
			sbBuilder.append("/");
		}
		if(sbBuilder.length() > 0){
			userSelectSp = sbBuilder.deleteCharAt(sbBuilder.lastIndexOf("/")).toString();
		}
		
		//拼投注结果
		sbBuilder = new StringBuilder();
		for(String str : selectedList){
			sbBuilder.append(str);
			sbBuilder.append(",");
		}
		if(sbBuilder.length() > 0){
			userSelect = sbBuilder.deleteCharAt(sbBuilder.lastIndexOf(",")).toString();
		}
		
		//拼投注串
		sbBuilder = new StringBuilder();
		sbBuilder.append(issue);
		sbBuilder.append(sn);
		sbBuilder.append(":");
		sbBuilder.append(userSelect);
		sbBuilder.append(":");
		userSelectBuy = sbBuilder.toString();
	}
	
	public void getHHGGResult(){
		try {
			//拼sp值串
			userSelectSp = getSelectedSpValue();
			userSelectBuyList.clear();
			if(spfSelectedItem.size() > 0){
				userSelectBuyList.add( getSelectedNumber(JZActivity.WF_SPF) );
			}
			if(rqspfSelectedItem.size() > 0){
				userSelectBuyList.add( getSelectedNumber(JZActivity.WF_RQSPF) );
			}
			if(qcbfSelectedItem.size() > 0){
				userSelectBuyList.add( getSelectedNumber(JZActivity.WF_QCBF) );
			}
			if(zjqsSelectedItem.size() > 0){
				userSelectBuyList.add( getSelectedNumber(JZActivity.WF_JQS) );
			}
			if(bqcSelectedItem.size() > 0){
				userSelectBuyList.add( getSelectedNumber(JZActivity.WF_BQC) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**根据玩法获取用户选择项对应的投注code
	 * @param playMethod
	 * @return
	 * @throws Exception
	 */
	public String getSelectedNumber(String playMethod) throws Exception {
		StringBuilder sbBuilder = new StringBuilder();
		if(playMethod.equals(JZActivity.WF_SPF)){
			for(int position : spfSelectedItem){
				sbBuilder.append( JCBFUtils.getJZSPFCode(position) );
				sbBuilder.append(",");
			}
		}else if(playMethod.equals(JZActivity.WF_RQSPF)){
			for(int position : rqspfSelectedItem){
				sbBuilder.append( JCBFUtils.getJZSPFCode(position) );
				sbBuilder.append(",");
			}
		}else if(playMethod.equals(JZActivity.WF_QCBF)){
			for(int position : qcbfSelectedItem){
				sbBuilder.append( JCBFUtils.getJZQCBFCode(position) );
				sbBuilder.append(",");
			}
		}else if(playMethod.equals(JZActivity.WF_JQS)){
			for(int position : zjqsSelectedItem){
				sbBuilder.append( JCBFUtils.getJZZJQSCode(position) );
				sbBuilder.append(",");
			}
		}else if(playMethod.equals(JZActivity.WF_BQC)){
			for(int position : bqcSelectedItem){
				sbBuilder.append( JCBFUtils.getJZBQCCode(position) );
				sbBuilder.append(",");
			}
		}
		if(sbBuilder.length() > 0){
			sbBuilder.deleteCharAt(sbBuilder.lastIndexOf(",")).toString();
		}
		StringBuilder ResultsbBuilder = new StringBuilder();
		ResultsbBuilder.append(issue);
		ResultsbBuilder.append(sn);
		ResultsbBuilder.append(":");
		ResultsbBuilder.append(playMethod);
		ResultsbBuilder.append(":");
		ResultsbBuilder.append(sbBuilder);
		ResultsbBuilder.append(":");
		
		return ResultsbBuilder.toString();
	}
	
	public void clear(){
		selectedList.clear();
		selectedPvList.clear();
		/**混合过关**/
		spfSelectedItem.clear();
		rqspfSelectedItem.clear();
		qcbfSelectedItem.clear();
		zjqsSelectedItem.clear();
		bqcSelectedItem.clear();
		userSelectBuyList.clear();
		hasDan = false;//是否设胆
		count = 0;//计数器
		userSelect = "";//用于存放用户选择的最终结果
		userSelectSp = "";;//用于存放用户选择的最终结果sp值，0:0/1:0/1/2:0，场次之间用：号，选项之间用/；
	    numstr = "";//显示所选结果
	}
	
	/**混合过关所有选择的sp值拼成一个串，奖金预测使用
	 * @return
	 * @throws Exception
	 */
	public String getSelectedSpValue() throws Exception {
		StringBuilder sbBuilder = new StringBuilder();
		for(int position : spfSelectedItem){
			sbBuilder.append((String)spfJcArray.get(position));
			sbBuilder.append("/");
		}
		for(int position : rqspfSelectedItem){
			sbBuilder.append((String)rqspfJcArray.get(position));
			sbBuilder.append("/");
		}
		for(int position : qcbfSelectedItem){
			sbBuilder.append((String)qcbfJcArray.get(position));
			sbBuilder.append("/");
		}
		for(int position : zjqsSelectedItem){
			sbBuilder.append((String)zjqsJcArray.get(position));
			sbBuilder.append("/");
		}
		for(int position : bqcSelectedItem){
			sbBuilder.append((String)bqcJcArray.get(position));
			sbBuilder.append("/");
		}
		if(sbBuilder.length() > 0){
			sbBuilder.deleteCharAt(sbBuilder.lastIndexOf("/")).toString();
		}
		return sbBuilder.toString();
	}
	
	/**混合过关用户所有选择结果，页面显示
	 * @return
	 * @throws Exception
	 */
	public void getSelectedValueForPage(){
		StringBuilder sbBuilder = new StringBuilder();
		for(int position : spfSelectedItem){
			sbBuilder.append( JCBFUtils.getJZSPFName(position) );
			sbBuilder.append(" ");
		}
		for(int position : rqspfSelectedItem){
			sbBuilder.append( "让"+JCBFUtils.getJZSPFName(position) );
			sbBuilder.append(" ");
		}
		for(int position : qcbfSelectedItem){
			sbBuilder.append( JCBFUtils.getJZQCBFName(position) );
			sbBuilder.append(" ");
		}
		for(int position : zjqsSelectedItem){
			sbBuilder.append( JCBFUtils.getJZZJQSName(position)+"球" );
			sbBuilder.append(" ");
		}
		for(int position : bqcSelectedItem){
			sbBuilder.append( JCBFUtils.getJZBQCName(position) );
			sbBuilder.append(" ");
		}
//		if(sbBuilder.length() > 0){
//			sbBuilder.deleteCharAt(sbBuilder.lastIndexOf(",")).toString();
//		}
		numstr = sbBuilder.toString();
	}
	
	/**展开全部按钮的状态判断
	 * @return
	 */
	public boolean isShowAllBtnChecked(){
		int sum = qcbfSelectedItem.size() + zjqsSelectedItem.size() + bqcSelectedItem.size();
		return sum > 0;
	}
	
	/**对阵  主队 让球 客队
	 * @param rangQiu 让球 在没有让球的情况下传入"vs"
	 * @return 主队 让球 客队  A -1 B
	 */
	public String teamVs(String rangQiu){
		return new StringBuilder(mainTeam).append(" ").append(rangQiu).append(" ").append(guestTeam).toString();
	}

	public String getBqcspfResult() {
		return bqcspfResult;
	}

	public void setBqcspfResult(String bqcspfResult) {
		this.bqcspfResult = bqcspfResult;
	}

	public String getRfshResult() {
		return rfshResult;
	}

	public void setRfshResult(String rfshResult) {
		this.rfshResult = rfshResult;
	}

	public String getDxfResult() {
		return dxfResult;
	}

	public void setDxfResult(String dxfResult) {
		this.dxfResult = dxfResult;
	}

	public String getSfcResult() {
		return sfcResult;
	}

	public void setSfcResult(String sfcResult) {
		this.sfcResult = sfcResult;
	}

	public String getAvgOdds() {
		return avgOdds;
	}

	public void setAvgOdds(String avgOdds) {
		this.avgOdds = avgOdds;
	}

	public String getUserSelectBuy() {
		return userSelectBuy;
	}

	public void setUserSelectBuy(String userSelectBuy) {
		this.userSelectBuy = userSelectBuy;
	}

	public String[] getPvList() {
		return pvList;
	}

	public void setPvList(String[] pvList) {
		this.pvList = pvList;
	}

	public String getPreCast() {
		return preCast;
	}

	public String getMainTeamImgPath() {
		return mainTeamImgPath;
	}

	public void setMainTeamImgPath(String mainTeamImgPath) {
		this.mainTeamImgPath = mainTeamImgPath;
	}

	public String getGuestTeamImgPath() {
		return guestTeamImgPath;
	}

	public void setGuestTeamImgPath(String guestTeamImgPath) {
		this.guestTeamImgPath = guestTeamImgPath;
	}

	public void setPreCast(String preCast) {
		this.preCast = preCast;
	}

	public String getLetWinOrNegaSp() {
		return letWinOrNegaSp;
	}

	public void setLetWinOrNegaSp(String letWinOrNegaSp) {
		this.letWinOrNegaSp = letWinOrNegaSp;
	}

	public String getBigOrLittleSp() {
		return bigOrLittleSp;
	}

	public void setBigOrLittleSp(String bigOrLittleSp) {
		this.bigOrLittleSp = bigOrLittleSp;
	}

	public String getWinNegaDiffSp() {
		return winNegaDiffSp;
	}

	public void setWinNegaDiffSp(String winNegaDiffSp) {
		this.winNegaDiffSp = winNegaDiffSp;
	}

	public boolean isHasDan() {
		return hasDan;
	}

	public void setHasDan(boolean hasDan) {
		this.hasDan = hasDan;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUserSelect() {
		return userSelect;
	}

	public void setUserSelect(String userSelect) {
		this.userSelect = userSelect;
	}

	public String getUserSelectSp() {
		return userSelectSp;
	}

	public void setUserSelectSp(String userSelectSp) {
		this.userSelectSp = userSelectSp;
	}

	public String getNumstr() {
		return numstr;
	}

	public void setNumstr(String numstr) {
		this.numstr = numstr;
	}

	public ArrayList<String> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(ArrayList<String> selectedList) {
		this.selectedList = selectedList;
	}

	public ArrayList<String> getSelectedPvList() {
		return selectedPvList;
	}

	public void setSelectedPvList(ArrayList<String> selectedPvList) {
		this.selectedPvList = selectedPvList;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMainTeam() {
		return mainTeam;
	}

	public void setMainTeam(String mainTeam) {
		this.mainTeam = mainTeam;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getPlayId() {
		return playId;
	}

	public void setPlayId(String playId) {
		this.playId = playId;
	}

	public String getPollId() {
		return pollId;
	}

	public void setPollId(String pollId) {
		this.pollId = pollId;
	}

	public String getEndOperator() {
		return endOperator;
	}

	public void setEndOperator(String endOperator) {
		this.endOperator = endOperator;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public String getLetBall() {
		return letBall;
	}

	public void setLetBall(String letBall) {
		this.letBall = letBall;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndDanShiTime() {
		return endDanShiTime;
	}

	public void setEndDanShiTime(String endDanShiTime) {
		this.endDanShiTime = endDanShiTime;
	}

	public String getEndFuShiTime() {
		return endFuShiTime;
	}

	public void setEndFuShiTime(String endFuShiTime) {
		this.endFuShiTime = endFuShiTime;
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

	public String getWinOrNegaSp() {
		return winOrNegaSp;
	}

	public void setWinOrNegaSp(String winOrNegaSp) {
		this.winOrNegaSp = winOrNegaSp;
	}

	public String getTotalGoalSp() {
		return totalGoalSp;
	}

	public void setTotalGoalSp(String totalGoalSp) {
		this.totalGoalSp = totalGoalSp;
	}

	public String getHalfCourtSp() {
		return halfCourtSp;
	}

	public void setHalfCourtSp(String halfCourtSp) {
		this.halfCourtSp = halfCourtSp;
	}

	public String getScoreSp() {
		return scoreSp;
	}

	public void setScoreSp(String scoreSp) {
		this.scoreSp = scoreSp;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
		pvList = sp.split("#");
	}

	public JSONArray getSpfJcArray() {
		return spfJcArray;
	}

	public void setSpfJcArray(JSONArray spfJcArray) {
		this.spfJcArray = spfJcArray;
	}

	public JSONArray getRqspfJcArray() {
		return rqspfJcArray;
	}

	public void setRqspfJcArray(JSONArray rqspfJcArray) {
		this.rqspfJcArray = rqspfJcArray;
	}

	public JSONArray getQcbfJcArray() {
		return qcbfJcArray;
	}

	public void setQcbfJcArray(JSONArray qcbfJcArray) {
		this.qcbfJcArray = qcbfJcArray;
	}

	public JSONArray getZjqsJcArray() {
		return zjqsJcArray;
	}

	public void setZjqsJcArray(JSONArray zjqsJcArray) {
		this.zjqsJcArray = zjqsJcArray;
	}

	public JSONArray getBqcJcArray() {
		return bqcJcArray;
	}

	public void setBqcJcArray(JSONArray bqcJcArray) {
		this.bqcJcArray = bqcJcArray;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	@Override
	public String toString() {
		return "JZMatchBean [sn=" + sn + ", issue=" + issue + ", week=" + week
				+ ", playId=" + playId + ", pollId=" + pollId
				+ ", endOperator=" + endOperator + ", matchName=" + matchName
				+ ", imgPath=" + imgPath + ", mainTeam=" + mainTeam
				+ ", guestTeam=" + guestTeam + ", mainTeamImgPath="
				+ mainTeamImgPath + ", guestTeamImgPath=" + guestTeamImgPath
				+ ", preCast=" + preCast + ", letBall=" + letBall
				+ ", endTime=" + endTime + ", endDanShiTime=" + endDanShiTime
				+ ", endFuShiTime=" + endFuShiTime + ", mainTeamHalfScore="
				+ mainTeamHalfScore + ", guestTeamHalfScore="
				+ guestTeamHalfScore + ", mainTeamScore=" + mainTeamScore
				+ ", guestTeamScore=" + guestTeamScore + ", bqcspfResult="
				+ bqcspfResult + ", rfshResult=" + rfshResult + ", dxfResult="
				+ dxfResult + ", sfcResult=" + sfcResult + ", sp=" + sp
				+ ", winOrNegaSp=" + winOrNegaSp + ", totalGoalSp="
				+ totalGoalSp + ", halfCourtSp=" + halfCourtSp + ", scoreSp="
				+ scoreSp + ", avgOdds=" + avgOdds + ", letWinOrNegaSp="
				+ letWinOrNegaSp + ", bigOrLittleSp=" + bigOrLittleSp
				+ ", winNegaDiffSp=" + winNegaDiffSp + ", spfJcArray="
				+ spfJcArray + ", rqspfJcArray=" + rqspfJcArray
				+ ", qcbfJcArray=" + qcbfJcArray + ", zjqsJcArray="
				+ zjqsJcArray + ", bqcJcArray=" + bqcJcArray
				+ ", spfSelectedItem=" + spfSelectedItem
				+ ", rqspfSelectedItem=" + rqspfSelectedItem
				+ ", qcbfSelectedItem=" + qcbfSelectedItem
				+ ", zjqsSelectedItem=" + zjqsSelectedItem
				+ ", bqcSelectedItem=" + bqcSelectedItem + ", hasDan=" + hasDan
				+ ", count=" + count + ", userSelect=" + userSelect
				+ ", userSelectSp=" + userSelectSp + ", numstr=" + numstr
				+ ", pvList=" + Arrays.toString(pvList) + ", userSelectBuy="
				+ userSelectBuy + ", selectedList=" + selectedList
				+ ", selectedPvList=" + selectedPvList + "]";
	}

	

}
