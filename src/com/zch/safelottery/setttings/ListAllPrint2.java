package com.zch.safelottery.setttings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;



public class ListAllPrint2 {  
    /** 
     * 使用createList方法，填充参数列表传递过来的List，默认是Integer,一般是这个类型，你可以修改别的类型 
     */  
    public void createList(int n,List list){  
        if(n==0){  
            n=3;  
        }  
        for(int i=1;i<=n;i++){  
            list.add(i);  
        }  
    }  
    /** 
     * printAll是输出全排列的递归调用方法，list是传入的list，用LinkedList实现， 
     * 而prefix用于转载以及输出的数据 
     * length用于记载初始list的长度，用于判断程序结束。 
     */  
    public void printAll(List candidate, String prefix,int length){  
        if(prefix.length()==length)  
        	Log.d(Settings.TAG, "-----------------------prefix:"+prefix);  
        for (int i = 0; i < candidate.size(); i++) {  
             List temp = new LinkedList(candidate);  
             printAll(temp, prefix + temp.remove(i),length);  
        }  
    }  
    
    
  
    /** 
     * 测试代码 
     */  
    public static void test() {  
        // TODO Auto-generated method stub  
        ArrayList<Integer> list=new ArrayList<Integer>();  
        ListAllPrint2 lap = new ListAllPrint2();  
        lap.createList(5, list);  
        lap.printAll(list,"",list.size());  
    }  
}  
