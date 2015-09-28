package com.zch.safelottery.util;


public class JCBFUtils {
//	/** 竞彩足球 胜平负 和 让球胜平负 的名称  **/
//	public static String[] JZSPFName = {"胜","平","负"};
//	/** 竞彩足球 胜平负 和 让球胜平负 的code  **/
//	public static String[] JZSPFCode = {"3","1","0"};
//	/** 竞彩足球 全场比分 的名称  **/
//	public static String[] JZQCBFName = {"1:0","2:0","2:1","3:0","3:1","3:2","4:0","4:1","4:2","5:0","5:1","5:2",
//		"胜其他","0:0","1:1","2:2","3:3","平其他","0:1","0:2","1:2","0:3","1:3","2:3","0:4","1:4","2:4","0:5","1:5","2:5","负其他"};
//	/** 竞彩足球 全场比分 的code  **/
//	public static String[] JZQCBFCode = {"10","20","21","30","31","32","40","41","42","50","51","52",
//		"90","00","11","22","33","99","01","02","12","03","13","23","04","14","24","05","15","25","09"};
//	/**  竞彩足球 半全场 的名称  **/
//	public static String[] JZBQCName = {"胜胜","胜平","胜负","平胜","平平","平负","负胜","负平","负负"};
//	/**  竞彩足球 半全场 的code  **/
//	public static String[] JZBQCCode = {"33","31","30","13","11","10","03","01","00"};
//	/**   竞彩足球 总进球数 的名称 和 code  **/
//	public static String[] JZZJQSNameOrCode = {"0","1","2","3","4","5","6","7+"};
	
	/**根据位置获取 竞彩足球 胜平负 和 让球胜平负 的名称
	 * @param position
	 * @return
	 */
	public static String getJZSPFName(int position){
		if (position == 0) {
			return "胜";
		} else if (position == 1) {
			return "平";
		} else if (position == 2) {
			return "负";
		} 
		return "";
	}
	
	/**根据位置获取 竞彩足球 胜平负 和 让球胜平负 的code
	 * @param position
	 * @return
	 */
	public static String getJZSPFCode(int position){
		if (position == 0) {
			return "3";
		} else if (position == 1) {
			return "1";
		} else if (position == 2) {
			return "0";
		} 
		return "";
	}

	/**根据位置获取 竞彩足球 全场比分 的名称
	 * @param position
	 * @return
	 */
	public static String getJZQCBFName(int position) {
		if (position == 0) {
			return "1:0";
		} else if (position == 1) {
			return "2:0";
		} else if (position == 2) {
			return "2:1";
		} else if (position == 3) {
			return "3:0";
		} else if (position == 4) {
			return "3:1";
		} else if (position == 5) {
			return "3:2";
		} else if (position == 6) {
			return "4:0";
		} else if (position == 7) {
			return "4:1";
		} else if (position == 8) {
			return "4:2";
		} else if (position == 9) {
			return "5:0";
		} else if (position == 10) {
			return "5:1";
		} else if (position == 11) {
			return "5:2";
		} else if (position == 12) {
			return "胜其他";
		} else if (position == 13) {
			return "0:0";
		} else if (position == 14) {
			return "1:1";
		} else if (position == 15) {
			return "2:2";
		} else if (position == 16) {
			return "3:3";
		} else if (position == 17) {
			return "平其他";
		} else if (position == 18) {
			return "0:1";
		} else if (position == 19) {
			return "0:2";
		} else if (position == 20) {
			return "1:2";
		} else if (position == 21) {
			return "0:3";
		} else if (position == 22) {
			return "1:3";
		} else if (position == 23) {
			return "2:3";
		} else if (position == 24) {
			return "0:4";
		} else if (position == 25) {
			return "1:4";
		} else if (position == 26) {
			return "2:4";
		} else if (position == 27) {
			return "0:5";
		} else if (position == 28) {
			return "1:5";
		} else if (position == 29) {
			return "2:5";
		} else if (position == 30) {
			return "负其他";
		}
		return "";
	}
	
	/**根据位置获取 竞彩足球 全场比分 的code
	 * @param position
	 * @return
	 */
	public static String getJZQCBFCode(int position) {
		if (position == 0) {
			return "10";
		} else if (position == 1) {
			return "20";
		} else if (position == 2) {
			return "21";
		} else if (position == 3) {
			return "30";
		} else if (position == 4) {
			return "31";
		} else if (position == 5) {
			return "32";
		} else if (position == 6) {
			return "40";
		} else if (position == 7) {
			return "41";
		} else if (position == 8) {
			return "42";
		} else if (position == 9) {
			return "50";
		} else if (position == 10) {
			return "51";
		} else if (position == 11) {
			return "52";
		} else if (position == 12) {
			return "90";
		} else if (position == 13) {
			return "00";
		} else if (position == 14) {
			return "11";
		} else if (position == 15) {
			return "22";
		} else if (position == 16) {
			return "33";
		} else if (position == 17) {
			return "99";
		} else if (position == 18) {
			return "01";
		} else if (position == 19) {
			return "02";
		} else if (position == 20) {
			return "12";
		} else if (position == 21) {
			return "03";
		} else if (position == 22) {
			return "13";
		} else if (position == 23) {
			return "23";
		} else if (position == 24) {
			return "04";
		} else if (position == 25) {
			return "14";
		} else if (position == 26) {
			return "24";
		} else if (position == 27) {
			return "05";
		} else if (position == 28) {
			return "15";
		} else if (position == 29) {
			return "25";
		} else if (position == 30) {
			return "09";
		}
		return "";
	}
	
	/**根据位置获取 竞彩足球 半全场 的名称
	 * @param position
	 * @return
	 */
	public static String getJZBQCName(int position) {
		if (position == 0) {
			return "胜胜";
		} else if (position == 1) {
			return "胜平";
		} else if (position == 2) {
			return "胜负";
		} else if (position == 3) {
			return "平胜";
		} else if (position == 4) {
			return "平平";
		} else if (position == 5) {
			return "平负";
		} else if (position == 6) {
			return "负胜";
		} else if (position == 7) {
			return "负平";
		} else if (position == 8) {
			return "负负";
		} 
		return "";
	}
	
	/**根据位置获取 竞彩足球 半全场 的code
	 * @param position
	 * @return
	 */
	public static String getJZBQCCode(int position) {
		if (position == 0) {
			return "33";
		} else if (position == 1) {
			return "31";
		} else if (position == 2) {
			return "30";
		} else if (position == 3) {
			return "13";
		} else if (position == 4) {
			return "11";
		} else if (position == 5) {
			return "10";
		} else if (position == 6) {
			return "03";
		} else if (position == 7) {
			return "01";
		} else if (position == 8) {
			return "00";
		} 
		return "";
	}
	

	/**根据位置获取 竞彩篮球 胜分差 的名称
	 * @param position
	 * @return
	 */
	public static String getJLSFCName(int position) {
		if (position == 0) {
			return "胜1-5分";
		} else if (position == 1) {
			return "胜6-10分";
		} else if (position == 2) {
			return "胜11-15分";
		} else if (position == 3) {
			return "胜16-20分";
		} else if (position == 4) {
			return "胜21-25分";
		} else if (position == 5) {
			return "胜26分以上";
		} else if (position == 6) {
			return "负1-5分";
		} else if (position == 7) {
			return "负6-10分";
		} else if (position == 8) {
			return "负11-15分";
		} else if (position == 9) {
			return "负16-20分";
		} else if (position == 10) {
			return "负21-25分";
		} else if (position == 11) {
			return "负26分以上";
		}
		return "";
	}
	
	/**根据位置获取 竞彩篮球 胜分差 的名称
	 * @param position
	 * @return
	 */
	public static String getJLSFCCode(int position) {
		if (position == 0) {
			return "01";
		} else if (position == 1) {
			return "02";
		} else if (position == 2) {
			return "03";
		} else if (position == 3) {
			return "04";
		} else if (position == 4) {
			return "05";
		} else if (position == 5) {
			return "06";
		} else if (position == 6) {
			return "11";
		} else if (position == 7) {
			return "12";
		} else if (position == 8) {
			return "13";
		} else if (position == 9) {
			return "14";
		} else if (position == 10) {
			return "15";
		} else if (position == 11) {
			return "16";
		} 
		return "";
	}

	/**根据位置获取 竞彩足球 总进球数 的名称 和 code
	 * @param position
	 * @return
	 */
	public static String getJZZJQSName(int position) {
		if (position == 0) {
			return "0";
		} else if (position == 1) {
			return "1";
		} else if (position == 2) {
			return "2";
		} else if (position == 3) {
			return "3";
		} else if (position == 4) {
			return "4";
		} else if (position == 5) {
			return "5";
		} else if (position == 6) {
			return "6";
		} else if (position == 7) {
			return "7+";
		}
		return "";
	}
	/**根据位置获取 竞彩足球 总进球数 的名称 和 code
	 * @param position
	 * @return
	 */
	public static String getJZZJQSCode(int position) {
		if (position == 0) {
			return "0";
		} else if (position == 1) {
			return "1";
		} else if (position == 2) {
			return "2";
		} else if (position == 3) {
			return "3";
		} else if (position == 4) {
			return "4";
		} else if (position == 5) {
			return "5";
		} else if (position == 6) {
			return "6";
		} else if (position == 7) {
			return "7";
		}
		return "";
	}
	
}
