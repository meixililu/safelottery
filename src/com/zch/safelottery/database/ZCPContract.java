package com.zch.safelottery.database;

import android.provider.BaseColumns;

public class ZCPContract {
	
	public static abstract class LotteryEntry implements BaseColumns {
		
	    public static final String TABLE_NAME = "lottery_id_table";
	    
	    public static final String COLUMN_NAME_Id = "id";//自增
	    
	    public static final String COLUMN_NAME_LId = "lid";
	    
	    public static final String COLUMN_NAME_NUMBER = "click_number";
	    
	    public static final String COLUMN_NAME_DEFAULT = "default_order";
	    
	    public static final String COLUMN_NAME_VALID = "valid";//0为可用，1为删除(假删除，不是真正意义上的删除)
	    
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	public static abstract class NoticeEntry implements BaseColumns {
		
	    public static final String TABLE_NAME = "notice_table";
	    
	    public static final String COLUMN_NAME_Id = "id";//自增
	    
	    public static final String COLUMN_NAME_Nid= "nid";//文章id
	    
	    public static final String COLUMN_NAME_EXPIRED= "expired";//0为有效 1为失效
	    
	    public static final String COLUMN_NAME_ISCLICK= "isclick";//0为点击，1未点击
	    
	    public static final String COLUMN_NAME_TPYE="typeid";
	    
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	public static abstract class SearchEntry implements BaseColumns {
		
		public static final String TABLE_NAME = "search_table";
		/** ID 自增 **/
		public static final String ID = "id";//自增
		/** 内容 搜索时输入的信息 **/
		public static final String CONTENT= "content";//内容
		/** 是否有效 0为有效 1为失效 **/
		public static final String EXPIRED= "expired";//0为有效 1为失效
		/** 是否置顶 0为置顶 1为不置顶**/
		public static final String STICK= "stick";
		/** 是否点击 0为点击，1未点击 **/
		public static final String ISCLICK= "isclick";//0为点击，1未点击
		/** 计数 合计点击次数 **/
		public static final String COUNT="count";//计数 合计点击次数
		/** 类型 **/
		public static final String TYPE="typeid";//类型
		
		public static final String NULLABLE = "null";
	}
	
	private ZCPContract(){} 

}