package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.BuyLotteryActivity;
import com.zch.safelottery.bean.IssueInfoBean;
import com.zch.safelottery.bean.LotteryInfoBean;
import com.zch.safelottery.ctshuzicai.BaseLotteryActivity;
import com.zch.safelottery.ctshuzicai.D3Activity;
import com.zch.safelottery.ctshuzicai.DLTActivity;
import com.zch.safelottery.ctshuzicai.K3Activity;
import com.zch.safelottery.ctshuzicai.K3XActivity;
import com.zch.safelottery.ctshuzicai.P3Activity;
import com.zch.safelottery.ctshuzicai.P5Activity;
import com.zch.safelottery.ctshuzicai.QLCActivity;
import com.zch.safelottery.ctshuzicai.QXCActivity;
import com.zch.safelottery.ctshuzicai.SSCActivity;
import com.zch.safelottery.ctshuzicai.SSQActivity;
import com.zch.safelottery.ctshuzicai.Y11x5Activity;
import com.zch.safelottery.jingcai.CZ14ChoiceActivity;
import com.zch.safelottery.jingcai.CZRX9ChoiceActivity;
import com.zch.safelottery.jingcai.JLActivity;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.jingcai.JZCGJActivity;
import com.zch.safelottery.setttings.Settings;

public class LotteryId {
	public static String INTENT_LID = "lid";
	
	/** 所有彩种  **/
	public static final String All = "";
	/** 双色球  **/
	public static final String SSQ = "001";
	/** 福彩3D  **/
	public static final String FC = "002";
	/** 七乐彩  **/
	public static final String QLC = "004";
	/** 重庆时时彩  **/
	public static final String SSCCQ = "006";
	/** 江西时时彩  **/
	public static final String SSCJX = "009";
	/** 11选5 (山东11选5) **/
	public static final String SYXW = "107";
	/** 新11选5 (安徽11选5) **/
	public static final String NSYXW = "106";
	/** 广东11选5  **/
	public static final String GDSYXW = "105";
	/** 排列五  **/
	public static final String PL5 = "109";
	/** 排列3  **/
	public static final String PL3 = "108";
	/** 七星彩  **/
	public static final String QXC = "110";
	/** 大乐透  **/
	public static final String DLT = "113";
	/** 竞彩足球  **/
	public static final String JCZQ = "200";
	/** 猜冠军  **/
	public static final String CGJ = "202";
	/** 竞彩篮球  **/
	public static final String JCLQ = "201";
	/** 传统足彩 9场,和14场使用相同的id **/
	public static final String SFC = "300";
	/** 传统足彩 9场 **/
	public static final String RX9 = "303";
	/** 北京单场  **/
	public static final String BJDC = "400";
	/** 传统足彩 6场半全场  **/
	public static final String LCBQC = "301";
	/** 传统足彩 4场进球彩  **/
	public static final String SCJQC = "302";
	/** 15选5  **/
	public static final String SWXW = "003";
	/** 快乐8  **/
	public static final String KL8 = "016";
	/** PK10  **/
	public static final String PKS = "017";
	/** 幸运农场  **/
	public static final String XYNC = "114";
	/** 快3-安徽快3  **/
	public static final String K3 = "010";
	/** 江苏快3-江苏快3  **/
	public static final String K3X = "011";
	
	/** 彩种icon，根据彩种id查询彩种icon **/
	public static int getLotteryIcon(String ltType){
		if(SSQ.equals(ltType)){//双色球
			return R.drawable.icon_shuangseqiu;
		}else if(FC.equals(ltType)){//福彩3D
			return R.drawable.icon_fucai;
		}else if(SSCCQ.equals(ltType)){//重庆时时彩
			return R.drawable.icon_shishicai;
		}else if(SSCJX.equals(ltType)){//江西时时彩
			return R.drawable.icon_shishicai_jx;
		}else if(PL3.equals(ltType)){//排列三
			return R.drawable.icon_pailie3;
		}else if(DLT.equals(ltType)){//大乐透
			return R.drawable.icon_daletou;
		}else if(SYXW.equals(ltType)){//山东11选5
			return R.drawable.icon_11x5;
		}else if(NSYXW.equals(ltType)){//安徽11选5 
			return R.drawable.icon_n11x5;
		}else if(GDSYXW.equals(ltType)){//广东11选5
			return R.drawable.icon_gd_11x5;
		}else if(QLC.equals(ltType)){//七乐彩
			return R.drawable.icon_qlc;
		}else if(PL5.equals(ltType)){//排列五
			return R.drawable.icon_pl5;
		}else if(QXC.equals(ltType)){//七星彩
			return R.drawable.icon_qxc;
		}else if(SFC.equals(ltType)){//胜负彩
			return R.drawable.icon_sfc;
		}else if(RX9.equals(ltType)){//任选9
			return R.drawable.icon_rx9;
		}else if(JCZQ.equals(ltType)){//竞彩足球
			return R.drawable.icon_jczq;
		}else if(JCLQ.equals(ltType)){//竞彩篮球
			return R.drawable.icon_jclq;
		}else if(BJDC.equals(ltType)){//北京单场
			return R.drawable.icon_bjdc;
		}else if(K3.equals(ltType)){//快3
			return R.drawable.icon_k3;
		}else if(K3X.equals(ltType)){//江苏快3
			return R.drawable.icon_k3;
		}else if(CGJ.equals(ltType)){//猜冠军
			return R.drawable.icon_jczq;
		}else{
			return R.drawable.icon_shuangseqiu;
		}
	}
	
	/** 彩种name，根据彩种id查询彩种name **/
	public static String getLotteryName(String ltType){
		return getLotteryName(ltType, "01");
	}
	
	public static String getLotteryName(String ltType, String playId){
		if(SSQ.equals(ltType)){
			return "双色球";
		}else if(FC.equals(ltType)){
			return "福彩3D";
		}else if(SSCCQ.equals(ltType)){
			return "重庆时时彩";
		}else if(SSCJX.equals(ltType)){
			return "江西时时彩";
		}else if(PL3.equals(ltType)){
			return "排列三";
		}else if(DLT.equals(ltType)){
			return "大乐透";
		}else if(QLC.equals(ltType)){
			return "七乐彩";
		}else if(PL5.equals(ltType)){
			return "排列五";
		}else if(QXC.equals(ltType)){
			return "七星彩";
		}else if(SYXW.equals(ltType)){
			return "山东11选5 ";
		}else if(NSYXW.equals(ltType)){
			return "安徽11选5";
		}else if(GDSYXW.equals(ltType)){
			return "广东11选5";
		}else if(SFC.equals(ltType)){
			if(playId.equals("02")){
				return "任选九";
			}else{
				return "胜负彩";
			}
		}else if(RX9.equals(ltType)){
			return "任选九";
		}else if(BJDC.equals(ltType)){
			return "北京单场";
		}else if(JCZQ.equals(ltType)){
			return "竞彩足球";
		}else if(JCLQ.equals(ltType)){
			return "竞彩篮球";
		}else if(K3.equals(ltType)){
			return "安徽快3";
		}else if(K3X.equals(ltType)){
			return "江苏快3";
		}else if(CGJ.equals(ltType)){
			return "猜冠军";
		}else if(All.equals(ltType)){
			return "全部";
		}else{
			return "未知彩种";
		}
	}
	
	/** 彩种id，根据彩种name查询彩种id **/
	public static String getLotteryLid(String ltType){
		if("双色球".equals(ltType)){
			return SSQ;
		}else if("福彩3D".equals(ltType)){
			return FC;
		}else if("重庆时时彩".equals(ltType)){
			return SSCCQ;
		}else if("江西时时彩".equals(ltType)){
			return SSCJX;
		}else if("排列三".equals(ltType)){
			return PL3;
		}else if("大乐透".equals(ltType)){
			return DLT;
		}else if("七乐彩".equals(ltType)){
			return QLC;
		}else if("排列五".equals(ltType)){
			return PL5;
		}else if("七星彩".equals(ltType)){
			return QXC;
		}else if("山东11选5".equals(ltType)){
			return SYXW;
		}else if("安徽11选5".equals(ltType)){
			return NSYXW;
		}else if("广东11选5".equals(ltType)){
			return GDSYXW;
		}else if("胜负彩".equals(ltType)){
			return SFC;
		}else if("任选九".equals(ltType)){
			return RX9;
		}else if("北京单场".equals(ltType)){
			return BJDC;
		}else if("竞彩足球".equals(ltType)){
			return JCZQ;
		}else if("竞彩篮球".equals(ltType)){
			return JCLQ;
		}else if("安徽快3".equals(ltType)){
			return K3;
		}else if("江苏快3".equals(ltType)){
			return K3X;
		}else if("猜冠军".equals(ltType)){
			return CGJ;
		}else if("全部彩种".equals(ltType)){
			return All;
		}else if("全部".equals(ltType)){
			return All;
		}else{
			return "---";
		}
	}
	
	/****
	 * 根据彩种id获取相应页面的class
	 * @param lid
	 * @return
	 */
	public static Class<?> getClass(String lid){
		if(SSQ.equals(lid)){
			return SSQActivity.class;
		}else if(FC.equals(lid)){
			return D3Activity.class;
		}else if(SYXW.equals(lid)){
			return Y11x5Activity.class;
		}else if(NSYXW.equals(lid)){
			return Y11x5Activity.class;
		}else if(GDSYXW.equals(lid)){
			return Y11x5Activity.class;
		}else if(SSCCQ.equals(lid)){
			return SSCActivity.class;
		}else if(SSCJX.equals(lid)){
			return SSCActivity.class;
		}else if(DLT.equals(lid)){
			return DLTActivity.class;
		}else if(PL3.equals(lid)){
			return P3Activity.class;
		}else if(QLC.equals(lid)){
			return QLCActivity.class;
		}else if(QXC.equals(lid)){
			return QXCActivity.class;
		}else if(PL5.equals(lid)){
			return P5Activity.class;
		}else if(JCZQ.equals(lid)){
			return JZActivity.class;
		}else if(CGJ.equals(lid)){
			return JZCGJActivity.class;
		}else if(JCLQ.equals(lid)){
			return JLActivity.class;
		}else if(SFC.equals(lid)){
			return CZ14ChoiceActivity.class;
		}else if(RX9.equals(lid)){
			return CZRX9ChoiceActivity.class;
		}else if(K3.equals(lid)){
			return K3Activity.class;
		}else if(K3X.equals(lid)){
			return K3XActivity.class;
		}
//		}else if(BJDC.equals(lid)){
//			return BDActivity.class;
//		}else if(JZGJ.equals(lid)){
//			return JZGJActivity.class;
//		}else if(JZGYJ.equals(lid)){
//			return JZGYJActivity.class;
//		}else if(XYNC.equals(lid)){
//			return LuckNCActivity.class;
//		}else if(PKS.equals(lid)){
//			return PKSChoiceActivity.class;
//		}else if(KL8.equals(lid)){
//			return KL8Activity.class;
		else{
			return null;
		}
	}
	
	public static String getLotterySubtitle(String ltType){
		if(SSQ.equals(ltType)){
			return "2元赢取500万";
		}else if(FC.equals(ltType)){
			return "三位数字赢千元";
		}else if(SSCCQ.equals(ltType)){
			return "最高赢10万";
		}else if(SSCJX.equals(ltType)){
			return "最高赢11.6万";
		}else if(PL3.equals(ltType)){
			return "天天都开奖";
		}else if(DLT.equals(ltType)){
			return "3元赢1600万";
		}else if(QLC.equals(ltType)){
			return "30选7赢百万";
		}else if(PL5.equals(ltType)){
			return "2元赢10万";
		}else if(QXC.equals(ltType)){
			return "2元赢500万";
		}else if(SYXW.equals(ltType)){
			return "返奖率59%";
		}else if(NSYXW.equals(ltType)){
			return "每天81期";
		}else if(GDSYXW.equals(ltType)){
			return "每天84期";
		}else if(SFC.equals(ltType)){
			return "猜14场赢大奖";
		}else if(RX9.equals(ltType)){
			return "猜胜负赢500万";
		}else if(BJDC.equals(ltType)){
			return "一场也能中";
		}else if(JCZQ.equals(ltType)){
			return "2串1易中奖";
		}else if(JCLQ.equals(ltType)){
			return "玩转NBA";
		}else if(K3.equals(ltType)){
			return "返奖率59%";
		}else if(K3X.equals(ltType)){
			return "简单容易玩";
		}else{
			return "好玩易中";
		}
	}
	
	/**
	 * 竞彩描述文字
	 * 
	 * @param lottery_id
	 * @return
	 */
	public static String getJCCountText(String lottery_id) {
		if (lottery_id.equals(LotteryId.JCZQ)) {
			return "返奖率高达69%";
		} else if (lottery_id.equals(LotteryId.JCLQ)) {
			return "返奖率高达69%";
		} else if (lottery_id.equals(LotteryId.BJDC)) {
			return "返奖率高达65%";
		} else {
			return "最高返奖率";
		}
	}

	/**
	 * 竞彩描述文字
	 * 
	 * @param lottery_id
	 * @return
	 */
	public static String getJCIssueText(String lottery_id) {
		if (lottery_id.equals(LotteryId.JCZQ)) {
			return "固定赔率 最高返奖";
		} else if (lottery_id.equals(LotteryId.JCLQ)) {
			return "固定赔率 最高返奖";
		} else if (lottery_id.equals(LotteryId.BJDC)) {
			return "中奖仅是举手之劳";
		} else {
			return "固定赔率 最高返奖";
		}
	}
	
	/****
	 * 根据彩种id获取 复式截止时间
	 * @param lid
	 * @return
	 */
	public static String getDuplexTime(String lid){
		IssueInfoBean bean = getIssueInfoBeanByLid(lid);
		if(bean != null){
			return bean.getDuplexTime();
		}
		return "";
	}
	
	/**根据彩种id获取彩期信息bean
	 * @param lid
	 * @return
	 */
	public static LotteryInfoBean getLotteryInfoBeanByLid(String lid){
		if(BuyLotteryActivity.lotteryInfoBeansMap != null){
			return BuyLotteryActivity.lotteryInfoBeansMap.get(lid);
		}
		return null;
	}
	
	/**根据彩种id获取期次信息bean
	 * @param lid
	 * @return
	 */
	public static IssueInfoBean getIssueInfoBeanByLid(String lid){
		try{
			LotteryInfoBean mLotteryInfoBean = getLotteryInfoBeanByLid(lid);
			IssueInfoBean bean = mLotteryInfoBean.getIssueInfoList().get(0);
			return bean;
		}catch (Exception e) {
			LogUtil.ExceptionLog("LotteryId-getIssueInfoBeanByLid");
		}
		return null;
	}
	
	/**根据彩种id获取期次
	 * @param lid
	 * @return
	 */
	public static String getIssue(String lid){
		IssueInfoBean bean = getIssueInfoBeanByLid(lid);
		if(bean != null){
			if(!bean.getStatus().equals("3")){
				return bean.getName();
			}
		}
		return "";
	}
	
	/**根据彩种期次状态
	 * @param lid
	 * @return
	 */
	public static String getIssueStatus(String lid){
		IssueInfoBean bean = getIssueInfoBeanByLid(lid);
		if(bean != null){
			return bean.getStatus();
		}
		return "";
	}
	
	/**根据彩种id获取期次剩余时间
	 * @param lid
	 * @return
	 */
	public static long getRemainTime(String lid){
		IssueInfoBean bean = getIssueInfoBeanByLid(lid);
		if(bean != null){
			return bean.getLeftTime();
		}
		return 0;
	}
	
	/****
	 * 判断是否是竞彩，竞彩没有期次断更
	 * @param lid 彩种ID
	 * @return boolean
	 */
	public static boolean isJCORCZ(String lid) {
		if (LotteryId.BJDC.equals(lid) || LotteryId.JCLQ.equals(lid) || LotteryId.JCZQ.equals(lid) || LotteryId.CGJ.equals(lid)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 中竞彩四个彩种
	 * @param lid 彩种ID
	 * @return boolean
	 */
	public static boolean isValidLottery(String lid) {
		if (LotteryId.SFC.equals(lid)) {
			return true;
		} else if (LotteryId.RX9.equals(lid)) {
			return true;
		} else if (LotteryId.JCZQ.equals(lid)) {
			return true;
		} else if (LotteryId.JCLQ.equals(lid)) {
			return true;
		} else if (LotteryId.CGJ.equals(lid)) {
			return true;
		} else if (LotteryId.BJDC.equals(lid)) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 返回颜色 根据彩种id 判断当前颜色 只支持数字彩 **/
	public static int getLotteryColor(String lid){
		
		if(SSQ.equals(lid) || DLT.equals(lid) || QLC.equals(lid)){
			return ViewUtil.COLOR_BLUE;
		}else if(SFC.equals(lid) || RX9.equals(lid)){
			return ViewUtil.COLOR_RED_RECT;
		}else if(K3.equals(lid) || K3X.equals(lid)){
			return R.drawable.icon_k3;
		}else{
			return ViewUtil.COLOR_RED;
		}
	}
	
	/** 返回颜色 根据彩种id 判断分离符 只支持数字彩 **/
	public static String[] getLotterySplit(String lid){
		String[] split = new String[2];
		if(PL3.equals(lid) || PL5.equals(lid) || QXC.equals(lid) || FC.equals(lid)){
			split[0] = ",";
			split[1] = "";
		}else if(SFC.equals(lid) || RX9.equals(lid)){
			split[0] = "\\*"; //走起
			split[1] = "\\*";
		}else{
			split[0] = "#";
			split[1] = ",";
		}
		return split;
	}
	
	public static Bundle getBundle(String lid){
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(INTENT_LID, lid);
		mBundle.putSerializable(BaseLotteryActivity.INTENT_ISSUE, getIssue(lid));
		mBundle.putSerializable(Settings.TOCLASS, getClass(lid));
		return mBundle;
	}
	
	/** 返回数字，根据彩种id返回判断数字 **/
	public static int getLotteryNum(String lid){
		
		if(SSQ.equals(lid)){
			return 1;
		}else if(FC.equals(lid)){
			return 1;
		}else if(SYXW.equals(lid)){
			return 1;
		}else if(NSYXW.equals(lid)){
			return 1;
		}else if(GDSYXW.equals(lid)){
			return 1;
		}else if(SSCCQ.equals(lid)){
			return 1;
		}else if(SSCJX.equals(lid)){
			return 1;
		}else if(DLT.equals(lid)){
			return 1;
		}else if(PL3.equals(lid)){
			return 1;
		}else if(QLC.equals(lid)){
			return 1;
		}else if(QXC.equals(lid)){
			return 1;
		}else if(PL5.equals(lid)){
			return 1;
		}else if(SFC.equals(lid)){
			return 2;
		}else if(RX9.equals(lid)){
			return 2;
		}else if(JCZQ.equals(lid)){
			return 3;
		}else if(JCLQ.equals(lid)){
			return 3;
		}else if(BJDC.equals(lid)){
			return 3;
		}else if(CGJ.equals(lid)){
			return 3;
		}else{
			return 1;
		}
	}
	
	/**
	 * 取得在售彩种 中文名
	 * @param orderstr 本地配置保存的彩种
	 * @param openLottery 当前版本已开放的彩种
	 * @return 通过这层处理后将会返回当前服务器已开放并而本地支持的彩种，并会跟据用户的排序做显示
	 */
	public static List<String> getLottery(String orderstr, List<String> openLottery){
		List<String> list = new ArrayList<String>();
		if (!TextUtils.isEmpty(orderstr)) {
			String[] ord = orderstr.split("#"); //取得当前排序配置
			for(String id: ord){
				if(openLottery.contains(id)){//是否是所须要的彩种，把须要的采种写入LotteryArray
					list.add(LotteryId.getLotteryName(id));
				}
			}
		}else{
			for(String id: openLottery){
				list.add(LotteryId.getLotteryName(id));
			}
		}
		list.add(0, LotteryId.getLotteryName(LotteryId.All));
		return list;
	}

	/**
	 * 取得在售彩种 排序后的 中文名
	 * @param orderstr 存在本地的彩种排序 this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE).getString(LotteryInfoParser.LotteryOrderStrKey, "");
	 * @param type 不要的类型 
	 * @return
	 */
	public static List<String> getPursueLotteryName(String orderstr, int type) {
		return getPursueLotteryName(orderstr, new int[]{type});
	}
	
	/**
	 * 取得在售彩种 排序后的 中文名
	 * @param orderstr 存在本地的彩种排序 this.getSharedPreferences(Settings.LOGIN_SHAREPREFRENCE, Context.MODE_PRIVATE).getString(LotteryInfoParser.LotteryOrderStrKey, "");
	 * @param types 不要的类型 数组
	 * @return
	 */
	public static List<String> getPursueLotteryName(String orderstr, int[] types) {
		List<String> lotteryArray = LotteryId.getLottery(orderstr, LotteryId.getLotteryOpen());
		List<String> removeArray = new ArrayList<String>();
		for(String name: lotteryArray){
			for(int type: types){
				if(LotteryId.getLotteryType(LotteryId.getLotteryLid(name)) == type){
					removeArray.add(name);
					break;
				}
			}
		}
		lotteryArray.removeAll(removeArray);
		return lotteryArray;
	}
	
	/**
	 * 取得在售彩种 ID
	 * @param orderstr 本地配置保存的彩种
	 * @param openLottery 当前版本已开放的彩种
	 * @return 通过这层处理后将会返回当前服务器已开放并而本地支持的彩种，并会跟据用户的排序做显示
	 */
	public static List<String> getLotteryId(String orderstr, List<String> openLottery){
		List<String> list = new ArrayList<String>();
		if (!TextUtils.isEmpty(orderstr)) {
			String[] ord = orderstr.split("#"); //取得当前排序配置
			for(String id: ord){
				if(openLottery.contains(id)){//是否是所须要的彩种，把须要的采种写入LotteryArray
					list.add(id);
				}
			}
		}else{
			for(String id: openLottery){
				list.add(id);
			}
		}
		list.add(0, LotteryId.All);
		return list;
	}
	/**
	 * 返回当前版本支持的彩种
	 * @return
	 */
	public static List<String> getLotteryOpen(){
		List<String> list = new ArrayList<String>();
		list.add(SSCCQ);
		list.add(SSCJX);
		list.add(SYXW);
		list.add(NSYXW);
		list.add(GDSYXW);
		list.add(SSQ);
		list.add(FC);
		list.add(DLT);
		list.add(PL3);
		list.add(PL5);
		list.add(QXC);
		list.add(QLC);
		list.add(JCLQ);
		list.add(JCZQ);
		list.add(CGJ);
		list.add(SFC);
		list.add(RX9);
		list.add(K3);
		list.add(K3X);
		return list;
	}
	
	/**
	 * 返回彩种类型，对彩种类型进行细分
	 * @param lid 彩种
	 * @return 彩种类型：1 福彩.SSQ FC QLC; 2 体彩.DLT PL3 PL5 QXC;  3 竞彩.JCLQ JCZQ;  4 传统足彩.SFC RX9; 5 高频彩. SSC SYXW NSYXW; -1 其他情况为
	 */
	public static int getLotteryType(String lid){
		if(lid.equals(SSQ) || lid.equals(FC) || lid.equals(QLC)) return LOTTERY_TYPE_FC;
		if(lid.equals(DLT) || lid.equals(PL3) || lid.equals(PL5) || lid.equals(QXC)) return LOTTERY_TYPE_TC;
		if(lid.equals(JCLQ) || lid.equals(JCZQ)) return LOTTERY_TYPE_JC;
		if(lid.equals(SFC) || lid.equals(RX9) ) return LOTTERY_TYPE_ZC;
		if(lid.equals(SSCCQ) || lid.equals(SSCJX) || lid.equals(SYXW) || lid.equals(NSYXW) || lid.equals(GDSYXW) || lid.equals(K3) || lid.equals(K3X)) return LOTTERY_TYPE_GP;
		return LOTTERY_TYPE_OUT;
	}
	
	public static String getK3Sum(String numbers){
		int result = 0;
		String[] temp = numbers.split(",");
		for(String tempNum: temp){
			result += ConversionUtil.StringToInt(tempNum);
		}
		return result + "";
	}
	
	/** 所有的都要 **/
	public static final int LOTTERY_TYPE_ALL = 0;
	/** 福彩.SSQ FC QLC **/
	public static final int LOTTERY_TYPE_FC = 1;
	/** 体彩.DLT PL3 PL5 QXC **/
	public static final int LOTTERY_TYPE_TC = 2;
	/** 竞彩.JCLQ JCZQ **/
	public static final int LOTTERY_TYPE_JC = 3;
	/** 传统足彩.SFC RX9 **/
	public static final int LOTTERY_TYPE_ZC = 4;
	/** 高频彩. SSC SYXW NSYXW GDSYXW **/
	public static final int LOTTERY_TYPE_GP = 5;
	/** 其他 **/
	public static final int LOTTERY_TYPE_OUT = -1;
}
