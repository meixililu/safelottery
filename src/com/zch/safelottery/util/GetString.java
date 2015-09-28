package com.zch.safelottery.util;

import java.text.DecimalFormat;

import com.zch.safelottery.bean.UserInfoBean;

public class GetString {

	/** 正式 **/
 	public static String SERVERURL = "http://testv.zch168.com/clientInterface"; // 服务器地址
	public static String PULLSERVERURL = "http://push.zch168.com/pushInterface"; // 服务器地址
	public static String BET_RECORD_SHARE_SERVER = "http://10.1.3.2/shareprogram?programsOrderId=%s&userCode=%s";//投注记录分享地址
 	public static boolean TEST = false;
	
	/** 测试 **/
//	 public static String SERVERURL = "http://119.254.92.203:9090/clientInterface"; // 服务器地址
//	 public static String PULLSERVERURL = "http://119.254.92.203:9092/pushInterface"; // 服务器地址
//	 public static String BET_RECORD_SHARE_SERVER = "http://10.1.3.2/shareprogram?programsOrderId=%s&userCode=%s";//投注记录分享地址
//	 public static boolean TEST = true;

	 public static String UploadImgServer = "http://motu.zch168.com"; // 图片上传服务器地址
	 public static String RMB_8_YUAN_SERVER ="http://m.zch168.com/activity?type=android";//8元保中服务器地址
	 public static String WORLD_CUP_SERVER = "http://worldcup.zch168.com/"; // 世界杯服务器地址
	 
	 
//	 public static String UploadImgServer = "http://code.zch168.net:8001"; // 图片上传服务器地址
//	 public static String RMB_8_YUAN_SERVER = "http://202.85.208.182:8088/activity?type=android"; // 8元保中服务器地址
	
	// public static String SID = "LM20120316162"; // AppChina应用汇
	// public static String SID = "LM20120316163"; // 优亿市场
	// public static String SID = "LM20120316164"; // N多网
	// public static String SID = "LM20120316165"; // 安智市场
	// public static String SID = "LM20120316166"; // near me
	// public static String SID = "LM20120316167"; // 搜应用
	// public static String SID = "LM20120316168"; // 宝瓶网
	// public static String SID = "LM20120316169"; // 木蚂蚁
	// public static String SID = "LM20120316170"; // 安卓星空
	// public static String SID = "LM20120316171"; // 遨友手机应用广场 10

	// public static String SID = "LM20120316172"; // 安卓粉丝网
	// public static String SID = "LM20120316173"; // 爱卓网
	// public static String SID = "LM20120316174"; // 网易应用
	// public static String SID = "LM20120326175"; // 联想开发社区
	// public static String SID = "LM20120326176"; // 机锋
	// public static String SID = "LM20120326177"; // 安卓市场
	// public static String SID = "LM20120326178"; // 腾讯手机应用平台
	// public static String SID = "LM20120326179"; // motorola 智件园
	// public static String SID = "LM20120326180"; // 360应用开放平台
	// public static String SID = "LM20120326181"; // samsung apps 需要隐藏更多那的检测版本
	// 20

	// public static String SID = "LM20120326182"; // 九城游戏中心
	// public static String SID = "LM20120326183"; // 中国华为开发者社区/智慧云
	// public static String SID = "LM20120326184"; // 魅族开发者社区
	// public static String SID = "LM20120326185"; // 百度
	// public static String SID = "LM20120326186"; // HTC应用商店
	// public static String SID = "LM20120326187"; // 中国联通沃商城
	// 需要隐藏更多那的检测版本，360检测，充值运营商
	// public static String SID = "LM20120326188"; // 上海铱天
	// public static String SID = "LM20120326189"; // 上海华勤
	// public static String SID = "LM20120327190"; //上海展讯
	// public static String SID = "LM20120328191"; // 公司官网 30

	// public static String SID = "LM20120329192";// 曹鹏翔让打的包（投注站）
	// public static String SID = "LM20120405193";//悠悠村
	// public static String SID = "LM20120405194"; //91应用中心的
	// public static String SID = "LM20120410195"; // 宝软wap站
	// public static String SID = "LM20120410196";//掌智通手机内置
	// public static String SID = "LM20120410197";//凡卓
	// public static String SID = "LM20120411199";// E人E本
	// public static String SID = "LM20120411200";// 北京美斯达克信息技术有限公司子联盟4个：
	// public static String SID = "LM20120411201";
	// public static String SID = "LM20120411202"; 40

	// public static String SID = "LM20120411203";
	// public static String SID = "LM20120411204";//悠悠村子联盟5个
	// public static String SID = "LM20120411205";
	// public static String SID = "LM20120411206";
	// public static String SID = "LM20120411207";
	// public static String SID = "LM20120411208";
	// public static String SID = "LM20120411209";//北京美斯达克信息技术有限公司父联盟
	// public static String SID = "LM20120411210";//斯凯
	// public static String SID = "LM20120412211";//金立
	// public static String SID = "LM20120412212";//博 50

	// public static String SID = "LM20120413215";//移动MM
	// public static String SID = "LM20120413213";//天翼
	// public static String SID = "LM20120413214";//太平洋
	// public static String SID ="LM20120419217";// 安卓市场1
	// public static String SID ="LM20120419216";// 安卓市场1
	// public static String SID = "LM201204251";//优美通讯
	// public static String SID = "LM20120507252";//科博会
	// public static String SID = "LM201205151";//tompda
	// public static String SID = "LM20120517153";//上海智风智能
	// public static String SID = "LM20120517154";//搜博网 60

	// public static String SID = "LM20120518101";//安卓之家
	// public static String SID = "LM20120518103";// 应用搜
	// public static String SID = "LM20120521101";// 上海凡卓
	// public static String SID = "LM20120521103";// 国美代理1
	// public static String SID = "LM20120524102";//安卓联盟
	// public static String SID = "LM20120525103";//叮当下载
	// public static String SID = "LM20120525104";//历趣市场
	// public static String SID = "LM20120525105";//当乐
	// public static String SID = "LM20120525106";//UC
	// public static String SID = "LM20120525107";//豌豆荚 70

	// public static String SID = "LM20120525108";//软吧
	// public static String SID = "LM20120525110";//宝软
	// public static String SID = "LM20120525111";//安机应用市场
	// public static String SID = "LM20120525112";//安致迷
	// public static String SID = "LM20120525113";//手游天下
	// public static String SID = "LM20120525114";//乐讯
	// public static String SID = "LM20120525115";//万普
	// public static String SID = "LM20120525116";//手机软件园
	// public static String SID = "LM20120525117";//安卓软件园 80

	// public static String SID = "LM20120525118";//架势
	// public static String SID = "LM20120525119";//哇棒
	// public static String SID = "LM20120525120";//爱卓网
	// public static String SID = "LM20120525121";//爱米软件商店
	// public static String SID = "LM20120525122";//十字猫
	// public static String SID = "LM20120525123";//蜘蛛电子市场
	// public static String SID = "LM20120525124";//千尺下载
	// public static String SID = "LM20120525125";//开奇商店
	// public static String SID = "LM20120525126";//淘告
	// public static String SID = "LM20120525127";//道有道 90

	// public static String SID = "LM20120525128";//HOT软件市场
	// public static String SID = "LM20120525129";//易蛙
	// public static String SID = "LM20120525130";//安丰下载
	// public static String SID = "LM20120525131";//飞流下载
	// public static String SID = "LM20120525132";//安卓软件盒子
	// public static String SID = "LM20120525133";//宝瓶网
	// public static String SID = "LM20120525134";//悠悠村
	// public static String SID = "LM20120525135";//绿巨人安卓网
	// public static String SID = "LM20120525136";//安卓4S店
	// public static String SID = "LM20120525137";//点信 1 100

	// public static String SID = "LM20120525138";//赢典
	// public static String SID = "LM20120525139";//安有汇
	// public static String SID = "LM20120525140";// 哇麦
	// public static String SID = "LM20120528143";// 波导
	// public static String SID = "LM20120528142";// E-BEST
	// public static String SID = "LM20120528141";// 泡椒网
	// public static String SID = "LM20120528144";// NEO手机
	// public static String SID = "LM20120529145";// 深讯和
	// public static String SID = "LM20120605146";// google play
	// public static String SID = "LM20120611101";// 新浪微博有奖转发活动 110

	// public static String SID = "LM20120611103";// 人人网
	// public static String SID = "LM20120611104";// 多蒙广告
	// public static String SID = "LM20120611105";// 中国电信
	// public static String SID = "LM20120612106";// 金立预装
	// public static String SID = "LM20120613107";// 亿户通
	// public static String SID = "LM20120614108";// 联想内置
	// public static String SID = "LM20120615110 ";// 天语
	// public static String SID = "LM20120629194"; // 新华瑞德定制的渠道包
	// public static String SID = "LM20120702195 ";// 360投放sid号 以区分广告投放量及自然量
	// public static String SID = "LM20120704196 ";// 广州骏博 120

	// public static String SID = "LM20120706197 ";//91助手cps
	// public static String SID = "LM20120706199 ";// 安卓市场cps
	// public static String SID = "LM20120710210 ";//海信
	// public static String SID = "LM20120710211 ";// 酷派
	// public static String SID = "LM20120716212 ";// 朵唯：（手机内置的包）
	// public static String SID = "LM20120716214 ";// 谷果无线
	// public static String SID = "LM20120717215 ";// 快通宝
	// public static String SID = "LM20120718217 ";// 联想1手机名站
	// public static String SID = "LM20120718218 ";// 联想2精品分类
	// public static String SID = "LM20120718219 ";// 联想2实用查询 130

	// public static String SID = "LM20120718220 ";// 联想4装机必备
	// public static String SID = "LM20120718221 ";// 谷果无线2
	// public static String SID = "LM20120806101 ";// 蘑菇云真趣
	// public static String SID = "LM20120806102 ";// 深圳华为（蘑菇云真趣）
	// public static String SID = "LM20120806103 ";// 宁波波导（蘑菇云真趣）
	// public static String SID = "LM20120806104 ";// 深圳oppo（蘑菇云真趣）
	// public static String SID = "LM20120806105 ";// 深圳鼎智（蘑菇云真趣）
	// public static String SID = "LM20120806106 ";// 上海鼎为（蘑菇云真趣）
	// public static String SID = "LM20120806107 ";// 上海捷本（蘑菇云真趣）
	// public static String SID = "LM20120806108 ";// 深圳朵唯 (蘑菇云真趣） 140

	// public static String SID = "LM20120806109 ";// 深圳泰达讯（蘑菇云真趣）
	// public static String SID = "LM20120806110 ";// 深圳汉业达（蘑菇云真趣）
	// public static String SID = "LM20120806111 ";// 上海铱天（蘑菇云真趣）
	// public static String SID = "LM20120808112";// 小米
	// public static String SID = "LM20120810113";// 斯凯线上推广
	// public static String SID = "LM20120816114 ";// qq浏览器
	// public static String SID = "LM20120816115 ";// 摩讯（CPA注册）
	// public static String SID = "LM20120822117 ";// xda
	// public static String SID = "LM20120829119 ";// 闻泰
	// public static String SID = "LM20120903120 ";// 欧鹏 150

	// public static String SID = "LM20120903121 ";//父包：中科柏诚：
	// public static String SID = "LM20120903122 ";// 子包：中科柏诚1：
	// public static String SID = "LM20120903123 ";// 中科柏诚wap
	// public static String SID = "LM20120906124 ";//深讯和安卓
	// public static String SID = "LM20120919125 ";//3G安卓市场
	// public static String SID = "LM20120919126 ";//ZOL软件
	// public static String SID = "LM20120919127 ";//非凡软件
	// public static String SID = "LM20120919128 ";//网讯安卓
	// public static String SID = "LM20120919129 ";//上海承开
	// public static String SID = " LM20120920130  ";//天玄通 160

	// public static String SID = " LM20120924131  ";// 深讯和（内置）
	// public static String SID = " LM20120925132  ";// 掌星立意（线上）
	// public static String SID = " LM20120925133  ";// 掌星立意 1
	// public static String SID = " LM20120925134  ";// 掌星立意2
	// public static String SID = " LM20120925135  ";// 掌星立意3
	// public static String SID = " LM20121024138 ";//豌豆荚2
	// public static String SID = " LM20121024139  ";// 悠悠村CPA
	// public static String SID = " LM20120925135  ";// 掌星立意3
	// public static String SID = " LM20121025140  ";// 腾讯应用中心
	// public static String SID = " LM20121031141  ";// 和讯（PUSH） 170

	// public static String SID = " LM20121108143  ";// 鼎为
	// public static String SID = " LM20121108144  ";// 天奕达
	// public static String SID = " LM20121113145  ";// 指点传媒（CPA注册）
	// public static String SID = " LM20121227167  ";// 金山手机控
	// public static String SID = " LM20120525107  ";// 豌豆荚
	// public static String SID = "LM20120802234 ";// 中科金财
	// public static String SID = "LM20120802233 ";// 爱国者
	// public static String SID = " LM20121127149  ";// 凯讯通达（线上）
	// public static String SID = " LM20121129155  ";// 指点传媒(网盟注册5)
	// public static String SID = " LM20121129154  ";// 指点传媒(网盟注册4) 180

	// public static String SID = " LM20121129153  ";// 指点传媒(网盟注册3)
	// public static String SID = " LM20121129152  ";// 指点传媒(网盟注册2)
	// public static String SID = " LM20121129151  ";// 指点传媒(网盟注册1)
	// public static String SID = " LM20121130156  ";// 手游天下（分成）
	// public static String SID = " LM20121129150  ";// 启艺（激活）
	// public static String SID = " LM20121206159  ";// 淘宝
	// public static String SID = " LM20121211161  ";// 山西联通1
	// public static String SID = " LM20121211162  ";// 山西联通2
	// public static String SID = " LM20121211163  ";// 搜狗地图精品推荐
	// public static String SID = " LM20130105171  ";// 掌星立意（激活）190

	// public static String SID = " LM20130105170  ";// 微讯
	// public static String SID = " LM20130105168  ";// 爱游
	// public static String SID = " LM20130105172  ";// 宝软下载
	// public static String SID = " LM20130107175  ";//蘑菇市场
	// public static String SID = " LM20130107174  ";//APP梦工厂
	// public static String SID = " LM20130108604  ";//tianji_001
	// public static String SID = " LM2013011060501  ";//蜂帮手 （激活）
	// public static String SID = " LM2013011060502  ";//蜂帮手 （注册）
	// public static String SID = " LM20130304210  ";//国美应用汇
	// public static String SID = " LM20130305211  ";//欧朋安卓    200
	
	// public static String SID = " 800001  ";// 斯凯冒泡浏览器
	// public static String SID = " 800005  ";//LIJIA001 SID
	// public static String SID = " 800006  ";//Colin2 SID
	// public static String SID = " 800007  ";//榆钱乐园CPA SID
	// public static String SID = " 800011  ";// 深讯和1
	// public static String SID = " 800012  ";// 深讯和2
	// public static String SID = " 800013  ";// 多盟下载
	// public static String SID = " LM20130221208  ";// 安米网cpa注册
	// public static String SID = " 800008  ";//微博
	// public static String SID = " 800009  ";// 微信     210
	
	// public static String SID = " 800014  ";// 光宝自用
	// public static String SID = " 800015  ";// 金立商城2
	// public static String SID = " 800020  ";// 海尔
	// public static String SID = " 800018  ";// 酷果(激活)
	// public static String SID = " 800021  ";// 联想3实用查询（安卓）
	// public static String SID = " 800023  ";// 榆钱乐园1
	// public static String SID = " 800024  ";// 有米cpa注册1
	// public static String SID = " 800025  ";// 有米cpa注册2
	// public static String SID = " 800029  ";// 91助手CPT
	// public static String SID = " 800030  ";// 百汇CPA注册    220
	
	// public static String SID = " 800033  ";// 蛙盟
	// public static String SID = " 800034  ";//激乐爽
	// public static String SID = " 800035  ";// 指点通1
	// public static String SID = " 800037  ";// 指点通2
	// public static String SID = " 800043  ";// 又一山
	// public static String SID = " 800044  ";// 兆柏（激活）
	// public static String SID = " 800049  ";//掌众
	// public static String SID = " 800051  ";//二维码SEO
	// public static String SID = " 800052  ";//风铃创景
	// public static String SID = " 800059  ";//又一山（鼎为）  230
	
	// public static String SID = " 800077  ";//UC下载
	// public static String SID = " 800078  ";//又一山（天奕达）
	// public static String SID = " 800086  ";//web官网android本地下载
	// public static String SID = " 800088  ";//web官网android360一键安装
	// public static String SID = "800091 ";//MOP应用商店
	// public static String SID = "800092 ";//上海游翔
	// public static String SID = "800095 ";//看书网注册
	// public static String SID = "800097 ";//蘑菇云（推广）
	// public static String SID = "800098 ";//买彩票（欧鹏）
	// public static String SID = "800109 ";//彩梦CPA 240
	 
	// public static String SID = "800113 ";//天顺
	// public static String SID = "800144 ";//百度推广
	// public static String SID = "800138 ";//光宝3
	// public static String SID = "800143 ";//安智CPS
	// public static String SID = "800145 ";//联信通
	// public static String SID = "800159 ";//乐蛙 
	// public static String SID = "800162 ";//深圳信一  
	// public static String SID = "800166 ";//
	// public static String SID = "800169 ";//天语
	// public static String SID = "800170 ";//打火机上印下载短信地址的  250

	// public static String SID = "800222 ";//宁波金腾
	//public static String SID = "800223 ";//360-送话费活动
	 //public static String SID = "800225 ";//19E 
	 //public static String SID = "800229 ";//晨星游戏
	 //public static String SID = "800230 ";//晨星游戏2
	 //public static String SID = "800123 ";//海尔TV
	 //public static String SID = "800020 ";//海尔手机
	 //public static String SID = "800231 ";//斯凯线上推广2
	//public static String SID = "800232 ";//蘑菇云推广
	//public static String SID = "800237 ";//内蒙风尚
	 //public static String SID = "800239 ";//金立安卓推广
	//public static String SID = "LM20121106142 ";//掌心立意wap
	
	// 特殊渠道包
	// public static String SID = "LM20120801222 ";// 中邮
	// public static String SID = "LM20120801223 ";// 中邮1
	// public static String SID = "LM20120801224 ";// 中邮2
	// public static String SID = "LM20120801225 ";// 中邮3
	// public static String SID = "LM20120801226";// 中邮4
	// public static String SID = "LM20120801227 ";// 中邮5
	// public static String SID = "LM20120801228 ";// 中邮6
	// public static String SID = "LM20120801229 ";// 中邮7
	// public static String SID = "LM20120801230 ";// 中邮8
	// public static String SID = "LM20120801231 ";// 中邮9
	// public static String SID = "LM20120801232 ";// 中邮10

	// public static String SID = "LM20120629194 ";//新华瑞德本联盟
	// public static String SID = "LM20120709200 ";// 新华瑞德1
	// public static String SID = "LM20120709201 ";// 新华瑞德2
	// public static String SID = "LM20120709202 ";//新华瑞德3
	// public static String SID = "LM20120709203 ";// 新华瑞德4
	// public static String SID = "LM20120709204 ";// 新华瑞德5
	// public static String SID = "LM20120709205 ";// 新华瑞德6
	// public static String SID = "LM20120709206 ";// 新华瑞德7
	// public static String SID = "LM20120709207 ";//新华瑞德8
	// public static String SID = "LM20120709208 ";//新华瑞德9
	// public static String SID = "LM20120709209 ";//新华瑞德10

	// public static String SID = "LM20121022136 ";//91中彩
	// public static String SID = "LM20120716213 ";//光宝联合
	 
	 // public static String SID = "800186 ";//91助手cps（11选5）
	 // public static String SID = "800187  ";//91助手cps（时时彩）
	

	public static int TopBetMoney = 20000;
	public static int BLRequestCode = 100;
	public static int BLAdd = 200;
	public static int BLAlter = 300;

	// 格式化金钱
	public static DecimalFormat df = new DecimalFormat("0.00");
	public static DecimalFormat df_0 = new DecimalFormat("0");

	/** 用户是否登录 **/
	public static boolean isLogin = false;
	/** 联网更新用户信息 **/
	public static boolean isAccountNeedRefresh = false;
	/** 重新设置用户的信息，不上网下载 **/
	public static boolean isAccountNeedReSet = false;

	/** 保存用户信息 **/
	public static UserInfoBean userInfo;
	
	public static boolean isLogin(){
		if(isLogin && userInfo != null){
			return true;
		}else{
			return false;
		}
	}

}
