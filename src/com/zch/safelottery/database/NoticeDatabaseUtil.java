package com.zch.safelottery.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zch.safelottery.bean.BannerBean;
import com.zch.safelottery.bean.NoticeBean;
import com.zch.safelottery.util.ConversionUtil;

public class NoticeDatabaseUtil {

	private DBOpenHelper mDbHelper;

	public NoticeDatabaseUtil(Context mContext) {
		mDbHelper = new DBOpenHelper(mContext);
	}
	
	/**新增消息
	 * @param ordstr
	 */
	public void insertNewList(List<NoticeBean> dataList){
		try {
			if(dataList!=null){
				for(NoticeBean itembean : dataList){
					NoticeBean noticeBean=getIdcontent(itembean.getId());
					String id=noticeBean.getId();
					if(TextUtils.isEmpty(id)){
						insert(itembean);
					}else{
						itembean.setIsclick(noticeBean.getIsclick());
					}
				}
			}
			close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**新增消息
	 * @param ordstr
	 */
	public void insertBannerList(List<BannerBean> dataList){
		try {
			if(dataList!=null){
				for(BannerBean itembean : dataList){
					NoticeBean noticeBean = getIdcontent(itembean.getId());
					String id = noticeBean.getId();
					if(TextUtils.isEmpty(id)){
						insert(itembean);
					}else{
						itembean.setIsclick(noticeBean.getIsclick());
					}
				}
			}
			close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**初始化插入数据
	 * @param lid
	 * @param order
	 * @return
	 */
	public long insert(NoticeBean bean) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_Nid, ConversionUtil.StringToInt(bean.getId()));
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_EXPIRED, bean.getExpired());
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_TPYE, bean.getArticle_category_id());
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK, bean.getIsclick());
		long newRowId = db.insert(ZCPContract.NoticeEntry.TABLE_NAME,ZCPContract.NoticeEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	
	/**初始化插入数据,banner
	 * @param lid
	 * @param order
	 * @return
	 */
	public long insert(BannerBean bean) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_Nid, ConversionUtil.StringToInt(bean.getId()));
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_EXPIRED, "0");
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_TPYE, bean.getArticle_category_id());
		values.put(ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK, bean.getIsclick());
		long newRowId = db.insert(ZCPContract.NoticeEntry.TABLE_NAME,ZCPContract.NoticeEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	
	/**
	 * @param isclick 0为点击，1为未点击
	 */
	public void updateIsclick(NoticeBean bean){
		String nid=bean.getId();
		String isclick=bean.getIsclick();
		String expired=bean.getExpired();
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK, isclick);
			values.put(ZCPContract.NoticeEntry.COLUMN_NAME_EXPIRED, expired);
			String[] args = {nid};
			db.update(ZCPContract.NoticeEntry.TABLE_NAME, values, ZCPContract.NoticeEntry.COLUMN_NAME_Nid + " = ?",args);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	/**
	 * @param isclick 0为点击，1为未点击
	 */
	public void updateIsclick(String nid){
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK, "0");
			String[] args = {nid};
			db.update(ZCPContract.NoticeEntry.TABLE_NAME, values, ZCPContract.NoticeEntry.COLUMN_NAME_Nid + " = ?",args);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			close();
		}
	}

	public NoticeBean getIdcontent(String nid) {
		String isClick ;
		String Expired;
		int id;
		Cursor cursor = null;
		NoticeBean bean=new NoticeBean();
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from notice_table where nid = ?", new String[] { nid });
			if(cursor.moveToFirst()){
				isClick = cursor.getString(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK));
				Expired=cursor.getString(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_EXPIRED));
				id=cursor.getInt(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_Nid));
				bean.setId(id+"");
				bean.setIsclick(isClick);
				bean.setExpired(Expired);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return bean;
	}
	
//	public List<NoticeBean> getAllData() {
//		String isClick ;
//		String Expired;
//		String id;
//		Cursor cursor = null;
//		List<NoticeBean> list = new ArrayList<NoticeBean>();
//		try {
//			SQLiteDatabase db = mDbHelper.getWritableDatabase();
//			cursor = db.rawQuery("select * from notice_table", null);
//			while(cursor.moveToNext()){
//				String typeid = cursor.getString(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_TPYE));
//				isClick = cursor.getString(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK));
//				Expired=cursor.getString(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_EXPIRED));
//				id=cursor.getString(cursor.getColumnIndex(ZCPContract.NoticeEntry.COLUMN_NAME_Nid));
//				NoticeBean bean=new NoticeBean();
//				bean.setId(id);
//				bean.setIsclick(isClick);
//				bean.setExpired(Expired);
//				bean.setArticle_category_id(typeid);
//				LogUtil.DefalutLog("----getAllData:"+bean.toString());
//				list.add(bean);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			close();
//		}
//		return list;
//	}
	
	public int getMaxIdByType(String type) {
		Cursor cursor = null;
		int count = 0;
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			cursor = db.rawQuery("select max(nid) from notice_table where typeid = ? ", new String[] { type });
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}
		return count;
	}
	
	
	public void close(){
		if(mDbHelper != null){
			mDbHelper.close();
		}
	}

}
