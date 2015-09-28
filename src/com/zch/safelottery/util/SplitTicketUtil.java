package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class SplitTicketUtil {
	private final boolean DEBUG = false;
	/** 常规，正常默认的 **/
	public static final int SPLIT_GENERAL = 0x1100;
	/** 去掉重复的，不可有重复的数 **/
	public static final int SPLIT_REMOVE_REPEAT = 0x1101;
//	public static int SPLIT_REMOVE_REPEAT = 0x002;
	
	private int splitState;
	
	private ArrayList<String[]> requests;
	private ArrayList<String> results;
	private StringBuilder sb;
	private String front;
	private String back;
	private int size;
	private int bunch;
	
	public static SplitTicketUtil mSplitTicketUtil;
	private SplitTicketUtil(){
	}
	
	public static SplitTicketUtil getSplitTicketUtil(){
		if(mSplitTicketUtil == null)
			mSplitTicketUtil = new SplitTicketUtil();
		return mSplitTicketUtil;
	}
	
	/**
	 * 无组合长度 如 size为4 拆出来的每个组合的长度都为4
	 * @param str 原数据，要做拆票的数据
	 * @param front 前面的split 字符
	 * @param back 后面的split 字符
	 * @param sokutState 状态 区分当前使用的拆分格式
	 * @param size 组合的长度大小 如：size = 2 那么就是 12  13  23  33这样的组合  1，23，3
	 * @param isUnderline 是否有下划线，把空的用下划线来替代
	 */
	public void start(String str, String front, String back, int splitState){
		this.front = front;
		this.back = back;
		String[] t = str.split(front);
		this.size = t.length;
		requests = new ArrayList<String[]>(size);
		for(int i = 0; i < size; i++){
			String[] ss = t[i].split(back);
			requests.add(ss);
		}
		results = new ArrayList<String>(size);
		try{
			if(splitState == SPLIT_REMOVE_REPEAT){
				splitRemoveRepeat(results, new String[size], 0, 0);
			}else{
				splitGeneral( requests, new String[size], 0, 0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 组合的长度根据GroupSize来定 如原组合为 1 2 3 groupSize为2  结果为 12  13  23
	 * @param str 原数据，要做拆票的数据
	 * @param front 前面的split 字符
	 * @param back 后面的split 字符
	 * @param sokutState 状态 区分当前使用的拆分格式
	 * @param size 组合的长度大小 如：size = 2 那么就是 12  13  23  33这样的组合  1，23，3
	 * @param isUnderline 是否有下划线，把空的用下划线来替代
	 */
	public void start(String str, String front, String back, int groupSize, boolean isUnderline){
		this.front = front;
		this.back = back;
		String[] t = str.split(front);
		this.size = t.length;
		this.bunch = groupSize;
		results = new ArrayList<String>(size);
		try{
			if(isUnderline){
				requests = new ArrayList<String[]>(size);
				splitUnderline(requests, t, new int[bunch], 0, 0);
			}else{
				splitUnderlineNot(t, new int[bunch], 0, 0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void splitGeneral(ArrayList<String[]> requests, String[] temp, int head, int index) throws Exception{
		if(head >= size){
			sb = new StringBuilder();
			sb.append(temp[0]);
			for(int j = 1; j < size; j++){
				sb.append(front);
				sb.append(temp[j]);
			}
			results.add(sb.toString());
			if(DEBUG) System.out.println("拼 >> " + sb.toString());
			return;
		}
		
		int s = requests.get(head).length;
		for(int i = 0; i < s; i++){
			temp[head] = requests.get(head)[i];
			if(!TextUtils.isEmpty(temp[head])) splitGeneral( requests, temp, head + 1, i);
		}

//		if(DEBUG) System.out.println("head >> " + head + " -- index >> " + index + " -- sum >> " + s + " -- size >> " + size);
//		if(DEBUG) System.out.println("结束");
	}

	private void splitUnderline(ArrayList<String[]> requests, String[] t, int[] resultTemp, int head, int index) throws Exception{
		for(int i = head; i < size + 1; i++){
			if(index < bunch){
				//超出当前组合大小 返回
				if(i >= size)
					return;
				
				resultTemp[index] = i;
				if(DEBUG) System.out.println(i + " === " + t[i] + "-head -->" + head );
				//在格示成立的时候 回调
				if(!t[i].equals("_")){
					splitUnderline(requests, t, resultTemp, i + 1, index + 1);
				}
			}else if(index == bunch){
				for(int id: resultTemp){
					if(t[id].equals("_")){
						if(DEBUG) System.out.println("组合包含有_");
						return;
					}
				}
				if(DEBUG) System.out.print("结果>>>> ");
				
				resultListTemp.clear();
				for(int j = 0; j < size; j++){
					isExist = false;
					for(int id: resultTemp){
						if(j == id){
							resultListTemp.add(t[id].split(back));
							if(DEBUG) System.out.print(t[id] + " , ");
							isExist = true;
							break;
						}
					}
					if(!isExist)
						resultListTemp.add(arrayStrTemp);

					if(DEBUG) System.out.print("_ , ");
				}
				requests.add(arrayStrTemp);
				if(DEBUG) System.out.println(" <<<<结果");
				
				splitGeneral(resultListTemp, new String[size], 0, 0);
				break;
			}else{
				break;
			}
		}
	} 
	
	private void splitUnderlineNot(String[] t, int[] resultTemp, int head, int index) throws Exception{
		for(int i = head; i < size + 1; i++){
			if(index < bunch){
				//超出当前组合大小 返回
				if(i >= size)
					return;
				
				resultTemp[index] = i;
				if(DEBUG) System.out.println(i + " === " + t[i] + "-head -->" + head );
				splitUnderlineNot(t, resultTemp, i + 1, index + 1);
			}else if(index == bunch){
				sb = new StringBuilder();
				for(int a: resultTemp){
					sb.append(front);
					sb.append(t[a]);
				}
				if(front != "")
					sb.deleteCharAt(0);
				
				results.add(sb.toString());
				if(DEBUG) System.out.println("结果>>>> " + sb.toString() + " <<<<结果");
				
				break;
			}else{
				break;
			}
		}
	} 
	
	private ArrayList<String[]> resultListTemp = new ArrayList<String[]>();
	private String[] arrayStrTemp = new String[]{"_"};
	private boolean isExist;
	 
	private void splitRemoveRepeat(ArrayList<String> result, String[] temp, int head, int index) throws Exception{
		if(head >= size){
			sb = new StringBuilder(); 
			sb.append(temp[0]);
			for(int j = 1; j < size; j++){
				if(sb.indexOf(temp[j]) != -1){
					return;
				}
				sb.append(front);
				sb.append(temp[j]);
			}
			result.add(sb.toString());
			if(DEBUG) System.out.println("拼 >> " + sb.toString());
			return;
		}
		
		int s = requests.get(head).length;
		for(int i = 0; i < s; i++){
			temp[head] = requests.get(head)[i];                                                                   
			if(!TextUtils.isEmpty(temp[head])) splitRemoveRepeat(result, temp, head + 1, i);
		}
		
//		if(DEBUG) System.out.println("head >> " + head + " -- index >> " + index + " -- sum >> " + s + " -- size >> " + size);
//		if(DEBUG) System.out.println("结束");
	}

	/**
	 * 取得添加头尾的数据组合
	 * @return
	 */
	public List<String> getAddRepeatRestlt(){
		List<String> listRe = new ArrayList<String>();
		StringBuilder sb;
		String[] temp;
		for(String s: results){
			temp = s.split(front);
			//添加头
			sb = new StringBuilder();
			sb.append(temp[0]);
			sb.append(front);
			sb.append(temp[0]);
			sb.append(front);
			sb.append(temp[1]);
			listRe.add(sb.toString());
			//添加尾
			sb = new StringBuilder();
			sb.append(temp[0]);
			sb.append(front);
			sb.append(temp[1]);
			sb.append(front);
			sb.append(temp[1]);
			listRe.add(sb.toString());
		}
		return listRe;
	}
	
	/**
	 * 取得拆票结果
	 * @return
	 */
	public List<String> getRestlt(){
		return results;
	}
//	public static void main(String[] args){
//		String str = "01#02,03,04#05,06";
//		String front = "#";
//		String back = ",";
//		
//		SplitTicketUtil splitTicketUtil = new SplitTicketUtil();
//		splitTicketUtil.start(str, front, back);
//	}
}
