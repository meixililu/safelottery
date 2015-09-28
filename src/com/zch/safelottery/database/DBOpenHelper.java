package com.zch.safelottery.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zch.safelottery.util.LogUtil;

public class DBOpenHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "zcp.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
			+ ZCPContract.LotteryEntry.TABLE_NAME + " (" + ZCPContract.LotteryEntry.COLUMN_NAME_Id
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ ZCPContract.LotteryEntry.COLUMN_NAME_LId + TEXT_TYPE + COMMA_SEP
			+ ZCPContract.LotteryEntry.COLUMN_NAME_NUMBER + INTEGER + COMMA_SEP
			+ ZCPContract.LotteryEntry.COLUMN_NAME_DEFAULT + INTEGER + COMMA_SEP
			+ ZCPContract.LotteryEntry.COLUMN_NAME_VALID + TEXT_TYPE + " )";
	
	private static final String SQL_CREATE_NOTICE = "CREATE TABLE IF NOT EXISTS "
			+ ZCPContract.NoticeEntry.TABLE_NAME + " (" + ZCPContract.NoticeEntry.COLUMN_NAME_Id
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ ZCPContract.NoticeEntry.COLUMN_NAME_Nid + INTEGER + COMMA_SEP
			+ ZCPContract.NoticeEntry.COLUMN_NAME_EXPIRED + TEXT_TYPE + COMMA_SEP
			+ ZCPContract.NoticeEntry.COLUMN_NAME_TPYE+TEXT_TYPE+COMMA_SEP
			+ ZCPContract.NoticeEntry.COLUMN_NAME_ISCLICK + TEXT_TYPE + " )";

	private static final String SQL_CREATE_SEARCH = "CREATE TABLE IF NOT EXISTS "
			+ ZCPContract.SearchEntry.TABLE_NAME + " (" 
			+ ZCPContract.SearchEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ ZCPContract.SearchEntry.COUNT+INTEGER+COMMA_SEP
			+ ZCPContract.SearchEntry.TYPE + COMMA_SEP
			+ ZCPContract.SearchEntry.EXPIRED + TEXT_TYPE + COMMA_SEP
			+ ZCPContract.SearchEntry.CONTENT + TEXT_TYPE + COMMA_SEP
			+ ZCPContract.SearchEntry.STICK + TEXT_TYPE + COMMA_SEP
			+ ZCPContract.SearchEntry.ISCLICK + TEXT_TYPE + " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ ZCPContract.LotteryEntry.TABLE_NAME;

	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.DefalutLog("DBOpenHelper-onCreate-sql:"+SQL_CREATE_ENTRIES);
		db.execSQL(SQL_CREATE_ENTRIES);
		LogUtil.DefalutLog("DBOpenHelper-onCreate-sql:"+SQL_CREATE_NOTICE);
		db.execSQL(SQL_CREATE_NOTICE);
//		LogUtil.DefalutLog("DBOpenHelper-onCreate-sql:"+SQL_CREATE_SEARCH);
//		db.execSQL(SQL_CREATE_SEARCH);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
