package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;

public class JC_BetCountUtil {

	private int num; //选中的个数
	private int dataLen;
	private int bunchLen;
	private int choleLen; //带胆个数
	
	private String choleFist;
	
	private String[] initData;
	private String[] choleStr;
	private int[] bunchCount; //串的组合如: 2串1 为 2 ,4串1 为 4  == {2,4}
	
	ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>(); // 存放tempData组合 不带胆的不分复选的组合	
	
	private StringBuilder tempBuffer;
	
	//用于返回注数
	private int count;
	
	//用于切割的符号
	private String splitSign = ",";
	
	public JC_BetCountUtil(){
	}
	
	public JC_BetCountUtil(List<String> data, List<String> choleStr, int[] bunchCount, int num){
		initData(data, choleStr, bunchCount, num);
	}
	
	/***
	 * 初始化数据
	 * @param data 非胆选项字符串，0:0/1:0/1/2:0，场次之前：分隔，选项之前/分隔；
	 * @param choleStr　定胆选项字符串，0:0/1:0/1/2:0，场次之前：分隔，选项之前/分隔；
	 * @param bunchCount 串关，[2,3,4,5]
	 * @param num 总场次数
	 */
	public void initData(List<String> data, List<String> choleStr, int[] bunchCount, int num){
		this.initData = (String[]) data.toArray(new String[data.size()]); //非胆选项数组
		this.dataLen = initData.length;
		this.num = num;
		
		this.choleStr = (String[]) choleStr.toArray(new String[choleStr.size()]); //定胆选项数组
		this.choleLen = this.choleStr.length;
		
		this.bunchCount = bunchCount;
		this.bunchLen = bunchCount.length;
		
		//根据串关个数进行操作
		for (int i = 0; i < bunchLen; i++) {
			splitGroup(i);
		}
	}
	
	/**
	 * 开始计算
	 */
	public void start(){
		count = 0;
		
		//对 list 进行编列 
		for(int i = 0; i < bunchLen; i++ ){
			ArrayList<String> tempList = lists.get(i);
			for (int j = 0, len = tempList.size(); j < len; j++) {
				splitCount(choleFist.concat(tempList.get(j)));
			}
			tempList = null;
		}

	}
	
	/***
	 * 对数据进行拼，组合，分解．再组合．
	 *  @param id 当前的串关 ID
	 */
	private void splitGroup(int id) {
		tempBuffer = new StringBuilder();//存放带胆组合		
		
		//有定胆先存放入定胆数据
		if (choleLen > 0) {
			for (int i = 0; i < choleLen; i++) {
				tempBuffer.append(choleStr[i]);
				tempBuffer.append(splitSign);
			}
		}
		
		choleFist = tempBuffer.toString();
		tempBuffer = null;
		
		//对组合的数据进行分离再组合　如1  0/1/2  0  ->  1,  0/1/2, 0 存放到tempList里
		// temp数据．用于拼组合
		 String[] tempData = new String[bunchCount[id]];
		
		ArrayList<String> tempList = new ArrayList<String>(num);
		spellData(tempList, 0, 1, tempData, bunchCount[id] - choleLen);
		lists.add(tempList);
		
		tempData = null;
		tempList = null;
	}
	
	/***
	 *  用递归方法进行组合
	 * @param head 起始位置
	 * @param index 当前位置
	 * @param tempData 操作数据
	 * @param bunch 串关
	 */
	private void spellData(ArrayList<String> tempList, int head,int index,String[] tempData,int bunch){
		
		for(int i = head; i < dataLen + index - bunch; i++){
			if(index < bunch){
				tempData[index - 1] = initData[i];
				spellData(tempList, i + 1, index + 1, tempData, bunch);
			}else if(index == bunch){
				tempData[index - 1] = initData[i];

				tempBuffer = new StringBuilder();//存放不带胆组合
				for(int j = 0; j < bunch; j++){
					tempBuffer.append(tempData[j]);
					if(j < bunch-1)
						tempBuffer.append(splitSign);
				}
				tempList.add(tempBuffer.toString());
				tempBuffer = null;
				
				spellData(tempList, i + 1, index + 1, tempData, bunch);
			}else{
				break;
			}
		}
	}
	
	/***
	 * 对复选的组合进行分离
	 * @param cData 第一个数
	 */
	private void splitCount(String cData){
		String[] data = split(cData, splitSign);
		int bunchSize = data.length;
		
		int temp = 1;
		for(int i = 0; i < bunchSize; i++){
			temp *= split(data[i],"/").length;
		}
		count += temp;
		// bunch串关 如2串1　为2
		data = null;
	}
	
	
	
	  /**
	   * 字符串分割
	   * 
	   * @param s 要分割的字符串
	   * @param sign　分割符
	   * @return　分割后的数组
	   */
	public static String[] split(String s, String sign) {
		int start = 0, pos = 0;
		List<String> list = new ArrayList<String>();
		while ((pos = s.indexOf(sign, start)) != -1 && pos < s.length() - 1) {
			String subStr = s.substring(start, pos);
			list.add(subStr);
			start = pos + 1;
		}
		if (pos != s.length() - 1) {
			String subStr = s.substring(start, s.length());
			list.add(subStr);
		}
		String[] arr = list.toArray(new String[0]);
		return arr;
	}
	
	/**
	 * 返回注数
	 * @return
	 */
	public int getCount() {
		return count;
	}
	

	public void setSplitSign(String splitSign) {
		this.splitSign = splitSign;
	}
	
	//调用说明
//	JC_BonusForecastUtil bonusForecastUtil; //定义类
//	bonusForecastUtil = new JC_BonusForecastUtil(); //初始化类
//	bonusForecastUtil.initData(groupData.toString(), groupCholeStr.toString(), groupBunchCount, GetString.bdrqList.size()); //初始化数据 选择胆的时候调用
//	bonusForecastUtil.start(state); //开始最后的计算
//	bonusForecastUtil.getCount(); //得到注数
	
}
