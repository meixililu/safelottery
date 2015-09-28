package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class JC_BonusForecastUtil {
//	用于输出确认
//	private final boolean DEBUG = true;
//	private final String TAG = "JC_SumCountUtil";

	private int mNum; //选中的个数
	private int mBunchLen;
	private int choleLen; //带胆个数
	
	
	private int[] mBunchCount; //串的组合如: 2串1 为 2 ,4串1 为 4  == {2,4}
	
	private double tempMax, tempMin;
	private double[][][] tempPeilv;
	
	//用于切割的符号
	private String splitSign = ",";
	
	private List<Float> dataListMax;
	private List<Float> dataListMin;
	
	private List<Float> choleListMax;
	private List<Float> choleListMin;
	
	public JC_BonusForecastUtil(){
	}

	public JC_BonusForecastUtil(List<String> dataList, List<String> choleList, int[] bunchCount, int num){
		initData(dataList, choleList, bunchCount, num);
	}
	
	/***
	 * 初始化数据
	 * @param dataList 非胆选项字符串，0: 0/1: 0/1/2: 0，场次之前：分隔，选项之前/分隔；
	 * @param choleStr　定胆选项字符串，0: 0/1: 0/1/2: 0，场次之前：分隔，选项之前/分隔；
	 * @param bunchCount 串关，[2,3,4,5]
	 * @param num 总场次数
	 */
	public void initData(List<String> dataList, List<String> choleList, int[] bunchCount, int num){
		this.mNum = num;
		
		this.mBunchCount = bunchCount;
		this.mBunchLen = bunchCount.length;
		
		tempPeilv = new double[mBunchLen][num][3];
		
		//取非胆的最大最小值
		List<Bean> dataListBean = getListBean(dataList);
		
		dataListMax = getMaxData(dataListBean);
		dataListMin = getMinData(dataListBean);
		
		choleLen = choleList.size();
		//取带胆的最大最小值
		if(choleLen > 0){
			List<Bean> choleListBean = getListBean(choleList);
			
			choleListMax = getMaxData(choleListBean);
			choleListMin = getMinData(choleListBean);
			
		}
		
	}
	
	public void setBunchCount(int[] bunchCount){
		this.mBunchCount = bunchCount;
		this.mBunchLen = bunchCount.length;
	}
	
	/**
	 * 开始计算
	 * @param state 状态 STATE_COUNT注数, STATE_PEILV赔率
	 */
	public void start(){
		int pourSum; //中奖注数
		int sum = dataListMax.size();
		ArrayList<Double> maxLists = new ArrayList<Double>();
		ArrayList<Double> minLists = new ArrayList<Double>();
		//根据串关个数进行操作 计算赔率
		for(int i = 0; i < mBunchLen; i++ ){
			//命中的场次走for循环
			int bunch = mBunchCount[i] - choleLen;
			//走场次循环，计算各命中场次对应的数值
			for(int j = bunch; j <= sum; j++){
				int sumIndex = j - 1;
				spellData(maxLists, minLists, new float[bunch + choleLen], new float[bunch + choleLen], i, j, 0, 1, bunch, dataListMax.get(sumIndex), dataListMin.get(sumIndex));
				//命中的注数
				pourSum = (int)MethodUtils.C_better(j ,bunch);
				
				//把计算出来的数进行相加就是当前场次的命中值
				tempMax = 0; tempMin = 0;
				for(int z = 0, size = maxLists.size(); z < size; z++){
					tempMax += maxLists.get(z);
					tempMin += minLists.get(z);
				}

				maxLists.clear();
				minLists.clear();
				sumIndex = sumIndex + choleLen;
				tempPeilv[i][sumIndex][0] = pourSum;
				tempPeilv[i][sumIndex][1] = tempMin;
				tempPeilv[i][sumIndex][2] = tempMax;
			}
		}
		
	}
	
	/**
	 * 拼数据，通过递归方法来拼数据的组合，得到的数存到一个temp group 里。
	 * @param tempDataMax 当前的最大数组合
	 * @param tempDataMin 当前的最小数组合
	 * @param bunchIndex 当前进行操作的串关
	 * @param sum 当前进行操作的场次
	 * @param head 起初位置
	 * @param index 当前所在的位置
	 * @param bunch 串关
	 * @param lastMax 当前进行操作的场次的最后一个数
	 */
	private void spellData(List<Double> maxLists, List<Double> minLists, float[] tempDataMax, float[] tempDataMin, int bunchIndex, int sum, int head, int index, int bunch, float lastMax, float lastMin){
		//进行操作
		if(bunch - 1 == 0){
			//添加数据
			tempDataMax[index - 1] = lastMax;
			tempDataMin[index - 1] = lastMin;
				
			//如有定胆把胆的数据添加到组合里
			if(choleLen > 0){
				for(int j = 0; j < choleLen; j++){
					tempDataMax[index + j] = choleListMax.get(j);
					tempDataMin[index + j] = choleListMin.get(j);
				}
			}
			
			//计算每个组合的数据
			tempMax = 1; tempMin = 1;
			for(int j = 0, size = tempDataMax.length; j < size; j++){
				tempMax *= tempDataMax[j];
				tempMin *= tempDataMin[j];
			}
			//把组合的数据存到一个List里
			maxLists.add(tempMax);
			minLists.add(tempMin);
			
			return;
		}
		
		for(int i = head; i < sum + index - bunch; i++){
			if(index < bunch - 1){
				//添加数据
				tempDataMax[index - 1] = dataListMax.get(i);
				tempDataMin[index - 1] = dataListMin.get(i);
				spellData(maxLists, minLists, tempDataMax, tempDataMin, bunchIndex, sum, i + 1, index + 1, bunch, lastMax, lastMin);
			}else if(index == bunch - 1){
				//添加数据
				tempDataMax[index - 1] = dataListMax.get(i);
				tempDataMax[index] = lastMax;
				tempDataMin[index - 1] = dataListMin.get(i);
				tempDataMin[index] = lastMin;
					
				//如有定胆把胆的数据添加到组合里
				if(choleLen > 0){
					for(int j = 0; j < choleLen; j++){
						tempDataMax[index + j + 1] = choleListMax.get(j);
						tempDataMin[index + j + 1] = choleListMin.get(j);
					}
				}
				
				//计算每个组合的数据
				tempMax = 1; tempMin = 1;
				for(int j = 0, size = tempDataMax.length; j < size; j++){
					tempMax *= tempDataMax[j];
					tempMin *= tempDataMin[j];
				}
				maxLists.add(tempMax);
				minLists.add(tempMin);
				
			}else {
				break;
			}
		}
	}
	
	/**
	 * 取得最大的数
	 * @param tempList
	 * @return 返回最大的数的集合 ArrayList<Float>
	 */
	private ArrayList<Float> getMaxData(List<Bean> tempList){
		int size = tempList.size();
		ArrayList<Float> maxData = new ArrayList<Float>(size);
		for(int i = 0; i < size; i++){
			maxData.add(tempList.get(i).getMax());
		}
		Collections.sort(maxData, Collections.reverseOrder());
		return maxData;
	}
	
	/**
	 * 取得最小的数
	 * @param tempList
	 * @return 返回最大的数的集合 ArrayList<Float>
	 */
	private ArrayList<Float> getMinData(List<Bean> tempList){
		int size = tempList.size();
		ArrayList<Float> minData = new ArrayList<Float>(size);
		for(int i = 0; i < size; i++){
			minData.add(tempList.get(i).getMin());
		}
		Collections.sort(minData);
		return minData;
	}
	
	/**
	 * 返回赔率最大及最小
	 * @return
	 */
	public double[][] getResultReturn() {
		double[][] resultPeilv = new double[mNum][3];
		
		for(int i = 0; i < mNum; i++){
			//根据串关把各串关的数据相加得到最后要的数据
			for(int j = 0; j < mBunchLen; j++){
				for(int y = 0; y < 3; y++){
					resultPeilv[i][y] += tempPeilv[j][i][y];
				}
			}
			//当前的命中 + 上一场的数据 = 现在的命中数据
			if(i > 0){
				resultPeilv[i][1] += resultPeilv[i-1][1];
				resultPeilv[i][2] += resultPeilv[i-1][2];
			}
		}
		
		tempPeilv = null; //清空数据
		return resultPeilv;
	}
	
	/**
	 * 把List<String> 转化成 List<Bean> 对多个选择进行分解
	 * @param dataList 要进行分解的List<String>
	 * @return 得到List<Bean> 可以直接从Bean 中得到最大最小值
	 */
	private List<Bean> getListBean(List<String> dataList){
		List<Bean> listBean = new ArrayList<Bean>();
		for(String data: dataList){
			listBean.add(new Bean(data));
		}
		return listBean;
	}
	
	/***
	 * 内部的一个　Bean
	 * @author Jiang
	 */
	private class Bean{
		int size;
		String[] gDataBean;
		LinkedList<Float> listSplit = new LinkedList<Float>();
		public Bean(String data){ //对数据进行
			gDataBean = data.split("/");
			size = gDataBean.length;
			for(int i = 0; i < size; i++){
				listSplit.add(Float.parseFloat(gDataBean[i]));
			}
			Collections.sort(listSplit);
			
			gDataBean = null;
		}
		public int getSize(){
			return size;
		}
		
		public float getMin(){
			return listSplit.get(0);
		}
		
		public float getMax(){
			return listSplit.get(size -1);
		}
	}

	public void setSplitSign(String splitSign) {
		this.splitSign = splitSign;
	}
	
}
