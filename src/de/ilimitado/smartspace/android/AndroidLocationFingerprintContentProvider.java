package de.ilimitado.smartspace.android;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class AndroidLocationFingerprintContentProvider extends ContentProvider {

    private static final String LOG_TAG = "AndroidLocationFingerprintContentProvider";

    private static final String DATABASE_NAME = "RadioMap.db";
    private static final int DATABASE_VERSION = 1;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        
        String encodedPath = uri.getEncodedPath();
        encodedPath = encodedPath.substring(1);
    	
    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(encodedPath);

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,	null, null);

		// Tell the cursor what uri to watch, so it knows when its source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        String tableCreationString = initialValues.getAsString("tableCreationString");
        db.execSQL(tableCreationString);
        initialValues.remove("tableCreationString");
        
        String encodedPath = uri.getEncodedPath();
        encodedPath = encodedPath.substring(1);
		long rowId = db.insert(encodedPath, null, initialValues);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String encodedPath = uri.getEncodedPath();
        encodedPath = encodedPath.substring(1);
        int count = db.delete(encodedPath, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String encodedPath = uri.getEncodedPath();
        encodedPath = encodedPath.substring(1);
        count = db.update(encodedPath, values, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    
    @Override
    public String getType(Uri uri) {
    	return "vnd.android.cursor.dir/vnd.smartspace.LocationFingerprints"; //ist das korrekt??
    }
}
