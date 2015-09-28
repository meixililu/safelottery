package com.zch.safelottery.setttings;

import java.util.ArrayList;
import java.util.List;

public class SplitUtil {

	private String[] a ;//所有场次的字符串数组
	private int k;//几串
	private List<String> list = new ArrayList<String>();
	public int count = 0;

	public void select() {
		String[] result = new String[k];
		subselect(0, 1, result, k);
	}
	
	private  void subselect(int head, int index, String[] r, int k) {
		for (int i = head; i < a.length + index - k; i++) {
			if (index < k) {
				r[index - 1] = a[i];
				subselect(i + 1, index + 1, r, k);
			} else if (index == k) {
				r[index - 1] = a[i];
				StringBuffer sb = new StringBuffer();
				for(int j=0;j<r.length;j++){
					String r_n = r[j];
					sb.append(r_n);
					if(j<r.length-1){
						sb.append(",");
					}
				}
				this.list.add(sb.toString());
				
				count++;
				subselect(i + 1, index + 1, r, k);
			} else {
				return;
			}
		}
	}

	public String[] getA() {
		return a;
	}

	public void setA(String[] a) {
		this.a = a;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public List<String> getList() {
		return list;
	}

	public int getCount() {
		return count;
	}
}
