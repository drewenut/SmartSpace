package de.ilimitado.smartspace.android;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AndroidLFPTDBAdapter {

	private final String DBName;
	private final String DBTableName;

	private final Context context;
	private DatabaseHelper DBHelper;

	private SQLiteDatabase db;

	private class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DBName, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
	
	public AndroidLFPTDBAdapter(Context ctx, String DBName, String DBTableName, String DBTableCreateStatement) {
		this.context = ctx;
		this.DBName = DBName;
		this.DBTableName = DBTableName;
		DBHelper = new DatabaseHelper(context);
		open();
		db.execSQL(DBTableCreateStatement);
		close();
	}

	public void open(){
		db = DBHelper.getWritableDatabase();
	}

	public void close() {
		DBHelper.close();
	}

	public Cursor select(String[] columns, String selection, String[] selectionArgs, String groubBy, String having, String orderBy, String limit) {
		Cursor cursor = db.query(DBTableName,columns,selection,selectionArgs,groubBy,having,orderBy,limit);
	    if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public Cursor execSQL(String sqlQuery) {
		Cursor cursor = db.rawQuery(sqlQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public void insert(ContentValues values){
		db.insert(DBTableName, "", values);
	}

	public void execMultipleSqlQuerys(ArrayList<String> sqlQuerys) {
		for(String sqlQuery : sqlQuerys)
			db.execSQL(sqlQuery);
	}

}
