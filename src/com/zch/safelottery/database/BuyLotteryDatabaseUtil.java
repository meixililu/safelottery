package com.zch.safelottery.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zch.safelottery.util.LogUtil;

public class BuyLotteryDatabaseUtil {

	private DBOpenHelper mDbHelper;

	public BuyLotteryDatabaseUtil(Context mContext) {
		mDbHelper = new DBOpenHelper(mContext);
	}
	
	/**新增彩种
	 * @param ordstr
	 */
	public void insertNewList(List<String> newarrayList){
		int max = getMaxClickNumber();
		int defaultOrderNumber = getMaxDefaultOrder();
		for(String item : newarrayList){
			int isNew = getLidCount(item);
			if(isNew > 0){
				updateValid(item,"0");
			}else{
				insert(item, defaultOrderNumber, max+1);
			}
		}
		close();
	}
	
	/**删除list数据
	 * @param ordstr
	 */
	public void removeList(List<String> needToRemove){
		for(String item : needToRemove){
			updateValid(item,"1");
		}
	}
	
	/**插入list数据
	 * @param ordstr
	 */
	public void insertList(String ordstr){
		String list[] = ordstr.split("#");
		for(int i=0,j=list.length; i<j; i++){
			insert(list[i], j-i, 0);
		}
		close();
	}

	/**初始化插入数据
	 * @param lid
	 * @param order
	 * @return
	 */
	public long insert(String lid, int order, int clickNumber) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ZCPContract.LotteryEntry.COLUMN_NAME_LId, lid);
		values.put(ZCPContract.LotteryEntry.COLUMN_NAME_DEFAULT, order);
		values.put(ZCPContract.LotteryEntry.COLUMN_NAME_NUMBER, clickNumber);
		values.put(ZCPContract.LotteryEntry.COLUMN_NAME_VALID, "0");
		long newRowId = db.insert(ZCPContract.LotteryEntry.TABLE_NAME,ZCPContract.LotteryEntry.COLUMN_NAME_NULLABLE, values);
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
			values.put(ZCPContract.LotteryEntry.COLUMN_NAME_NUMBER, clickNumber);
			String[] args = {lid};
			db.update(ZCPContract.LotteryEntry.TABLE_NAME, values, ZCPContract.LotteryEntry.COLUMN_NAME_LId+" = ?", args);
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
			values.put(ZCPContract.LotteryEntry.COLUMN_NAME_VALID, value);
			String[] args = {lid};
			db.update(ZCPContract.LotteryEntry.TABLE_NAME, values, ZCPContract.LotteryEntry.COLUMN_NAME_LId + " = ?",args);
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
			cursor = getCursor("select * from lottery_id_table where valid = '0' order by click_number desc,default_order desc ",null);
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
	
	public String createData(Cursor cursor){
		String lid = cursor.getString(cursor.getColumnIndex(ZCPContract.LotteryEntry.COLUMN_NAME_LId));
		return lid;
	}
	
	public Cursor getCursor(String slq,String[] strs){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(slq,strs);
		return cursor;
	}

	public int getClickNumberByLid(String lid){
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select click_number from lottery_id_table where lid = ? and valid = '0'", new String[] { lid });
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
			cursor = db.rawQuery("select max(click_number) from lottery_id_table where valid = '0'", null);
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
			cursor = db.rawQuery("select max(default_order) from lottery_id_table where valid = '0'", null);
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

	public int getLidCount(String lid) {
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select count(*) from lottery_id_table where lid = ?", new String[] { lid });
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
	
	public long getCount() {
		Cursor cursor = null;
		long count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select count(*) from lottery_id_table where valid = '0'", null);
			if(cursor.moveToFirst()){
				count = cursor.getLong(0);
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
