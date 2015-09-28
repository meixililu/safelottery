package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.text.TextUtils;

public class MethodUtils {

	/**
	 * 计算注数 阶乘算法
	 * @param n 总球数
	 * @param m 选择的球数
	 * @return 注数
	 */
	public static long C_better(int n, int m) {
		if (n / 2 < m) m = n - m; // 当n/2小于m
		long n1 = 1, n2 = 1;
		for (long i = n, j = 1; j <= m; n1 *= i--, n2 *= j++);// 不满足条件 直接跳过
		return n1 / n2;
	}
	
	/**
	 * 拼串
	 * @param number 号码组 int[]
	 * @param split 分离符
	 * @param init 起始位是否是从0开始
	 * @return String 结果串
	 */
	public static String getSpellBetNumber(int[] number, String split, int init){
		StringBuilder sb = new StringBuilder();
		if(init == 0){
			for(int temp : number){
				sb.append(temp);
				sb.append(split);
			}
		}else{
			for(int temp : number){
				sb.append(temp < 10? "0" + temp: temp);
				sb.append(split);
			}
		}
		if(TextUtils.isEmpty(split)) return sb.toString();
		else return sb.deleteCharAt(sb.lastIndexOf(split)).toString();
	}

	/**
	 * 拼串
	 * @param number 号码串 String[]
	 * @param split 分离符
	 * @return String 结果串
	 */
	public static String getSpellBetNumber(String[] number, String split){
		StringBuilder sb = new StringBuilder();
		for(String temp : number){
			sb.append(temp);
			sb.append(split);
		}
		if(TextUtils.isEmpty(split)) return sb.toString();
		else return sb.deleteCharAt(sb.lastIndexOf(split)).toString();
	}
	
	/**
	 * 随机生成的号码
	  * @param total 总球数
	 * @param number 生成的个数
	 * @param init 起始数 0跟1
	 * @param isRepeat 是否可重复
	 * @return 随机生成的数组 int[]
	 */
	public static int[] getRandomNumber(int total, int number, int init, boolean isRepeat){
		if(isRepeat)
			return repeatYes(total, number, init, true);
		else 
			return repeatNo(total, number, init, true);
	}
	
	/**
	 * 随机生成的号码
	 * @param total 总球数
	 * @param number 生成的个数
	 * @param init 起始数 0跟1
	 * @param isRepeat 是否可重复
	 * @param isSort 是否排序
	 * @return 随机生成的数组 int[]
	 */
	public static int[] getRandomNumber(int total, int number, int init, boolean isRepeat, boolean isSort){
		if(isRepeat)
			return repeatYes(total, number, init, isSort);
		else 
			return repeatNo(total, number, init, isSort);
	}
	
	/**
	 * 不可重复
	 */
	private static int[] repeatNo(int total, int number, int init, boolean isSort) {
		int[] result = new int[number];
		List<Integer> list = new ArrayList<Integer>();
		Random r = new Random();

		for (int i = init, size = total + init; i < size; i++) {
			list.add(i);
		}

		for (int j = 0; j < number; j++) {
			int index = r.nextInt(list.size());
			result[j] = list.get(index);
			list.remove(index);
		}
		if(isSort) sort(result);
		return result;
	}

	/**
	 * 可重复
	 */
	private static int[] repeatYes(int total, int number, int init, boolean isSort) {
		int[] result = new int[number];
		Random r = new Random();
		for (int j = 0; j < number; j++) {
			result[j] = r.nextInt(total + init);
		}
		if(isSort) sort(result);
		return result;
	}
	
	/**
	 * 排序
	 * @param array
	 */
	private static void sort(int[] array) {
		int temp;
		for (int i = 0; i < array.length; i++) {
			boolean flag = false;
			for (int j = 0; j < array.length - 1 - i; j++) {
				if (array[j] > array[j + 1]) {
					temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					flag = true;
				}
			}
			if (!flag) {
				break;
			}
		}
	}
	
	/**产生一个随机数，不包括max，0到max-1
	 * @param max
	 * @return
	 */
	public static int randomNumber(int max){
		return new Random().nextInt(max);
	}
}
