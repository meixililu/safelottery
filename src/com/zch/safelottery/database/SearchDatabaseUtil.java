package com.zch.safelottery.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zch.safelottery.util.LogUtil;

public class SearchDatabaseUtil {

	private DBOpenHelper mDbHelper;

	public SearchDatabaseUtil(Context mContext) {
		mDbHelper = new DBOpenHelper(mContext);
	}
	
	/**删除list数据
	 * @param ordstr
	 */
	public void removeList(List<String> needToRemove){
		for(String item : needToRemove){
			updateValid(item,"1");
		}
	}
	
	/**初始化插入数据
	 * @param name
	 * @param order
	 * @return
	 */
	public long insert(String name, int order, int clickNumber) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ZCPContract.SearchEntry.CONTENT, name);
		values.put(ZCPContract.SearchEntry.COUNT, clickNumber);
		values.put(ZCPContract.SearchEntry.EXPIRED, "0");
		long newRowId = db.insert(ZCPContract.SearchEntry.TABLE_NAME, ZCPContract.SearchEntry.NULLABLE, values);
		return newRowId;
	}
	
	/**
	 * @param lid
	 */
	public void updateClickNumber(String lid){
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			int clickNumber = getClickNumberByLid(lid);
			clickNumber += 1;
			ContentValues values = new ContentValues();
			LogUtil.DefalutLog("updateClickNumber:"+clickNumber);
			values.put(ZCPContract.SearchEntry.COUNT, clickNumber);
			String[] args = {lid};
			db.update(ZCPContract.SearchEntry.TABLE_NAME, values, ZCPContract.SearchEntry.CONTENT+" = ?", args);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	/**
	 * @param value 0为可用，1为删除(假删除，不是真正意义上的删除)
	 */
	public void updateValid(String lid, String value){
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(ZCPContract.SearchEntry.CONTENT, value);
			String[] args = {lid};
			db.update(ZCPContract.SearchEntry.TABLE_NAME, values, ZCPContract.SearchEntry.CONTENT + " = ?",args);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	public List<String> getDataList() {
		List<String> strs = new ArrayList<String>();
		Cursor cursor = null;
		try {
			cursor = getCursor("select * from search_table where valid = '0' order by click_number desc,default_order desc ",null);
			LogUtil.DefalutLog("getDataList-cursor.getColumnCount:"+cursor.getCount());
			while (cursor.moveToNext()) {
				String temp = createData(cursor);
//				LogUtil.DefalutLog("BuyLotteryDatabaseUtil-getDataList:"+temp);
				strs.add( temp );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}
		return strs;
	}
	
	public Cursor getCursor(String slq,String[] strs){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(slq,strs);
		return cursor;
	}
	
	public String createData(Cursor cursor){
		String lid = cursor.getString(cursor.getColumnIndex(ZCPContract.SearchEntry.CONTENT));
		return lid;
	}
	
	public int getClickNumberByLid(String lid){
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select click_number from search_table where lid = ? and valid = '0'", new String[] { lid });
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return count;
	}
	
	public int getMaxClickNumber(){
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select max(click_number) from search_table where valid = '0'", null);
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return count;
	}
	
	public int getMaxDefaultOrder(){
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select max(default_order) from search_table where valid = '0'", null);
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return count;
	}

	public int getCount(String name) {
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select count(*) from search_table where lid = ?", new String[] { name });
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return count;
	}
	
	public void close(){
		if(mDbHelper != null){
			mDbHelper.close();
		}
	}

}
