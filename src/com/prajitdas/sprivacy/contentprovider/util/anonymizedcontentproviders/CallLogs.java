package com.prajitdas.sprivacy.contentprovider.util.anonymizedcontentproviders;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;

public class CallLogs extends ContentProvider {
	/**
	 * fields for my content provider
	 */
	static final String PROVIDER_NAME = SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
			+SPrivacyApplication.getConstAnnonymous()
			+SPrivacyApplication.getConstCallLogs();
	static final String URL = "content://" + PROVIDER_NAME;
	static final Uri CONTENT_URI = Uri.parse(URL);

    /**
	 * integer values used in content URI
	 */
    static final int CALL_LOGS = 1;
    
    DatabaseHelper dbHelper;

	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstCallLogs(), CALL_LOGS);
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private static Context context;

	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "AnonymousCallLogsContent";
	static final int DATABASE_VERSION = 1;
	
	/**
	 * All the tables being created
	 */
	static final String CALL_LOGS_TABLE_NAME = "anonymousCallLogs";
	static final String CREATE_DB_TABLE_CALL_LOGS = " CREATE TABLE " + CALL_LOGS_TABLE_NAME + " (" + 
			CallLog.Calls._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			CallLog.Calls.TYPE + " INTEGER, " +
			CallLog.Calls.NEW + " INTEGER, " +
			CallLog.Calls.IS_READ + " INTEGER, " +
			CallLog.Calls.DATE + " INTEGER, " +
			CallLog.Calls.DURATION + " INTEGER, " +
			CallLog.Calls.NUMBER + " TEXT);";
	
	/**
	* Helper class that actually creates and manages 
	* the provider's underlying data repository.
	*/
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DB_TABLE_CALL_LOGS);
			loaDefaultData(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " +  CALL_LOGS_TABLE_NAME);
			onCreate(db);
		}
		
		private int loaDefaultData(SQLiteDatabase db) {
			int returnValue = -1;
			returnValue = loadDataSet1(db);
			returnValue = loadDataSet2(db);
			returnValue = loadDataSet3(db);
			returnValue = loadDataSet4(db);
			return returnValue;
		}

		private int loadDataSet1(SQLiteDatabase db) {
			//Data Set 1
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.TYPE,"1");
			values.put(CallLog.Calls.NEW,"0");
			values.put(CallLog.Calls.IS_READ,"0");
			values.put(CallLog.Calls.DATE,"1408486065");
			values.put(CallLog.Calls.DURATION,"100");
			values.put(CallLog.Calls.NUMBER,"4103703434");
			try{
				db.insert(CALL_LOGS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			return 1;
		}
		private int loadDataSet2(SQLiteDatabase db) {
			//Data Set 2
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.TYPE,"1");
			values.put(CallLog.Calls.NEW,"0");
			values.put(CallLog.Calls.IS_READ,"0");
			values.put(CallLog.Calls.DATE,"1408486152");
			values.put(CallLog.Calls.DURATION,"100");
			values.put(CallLog.Calls.NUMBER,"4103703434");
			try{
				db.insert(CALL_LOGS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			return 1;
		}
		private int loadDataSet3(SQLiteDatabase db) {
			//Data Set 3
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.TYPE,"2");
			values.put(CallLog.Calls.NEW,"0");
			values.put(CallLog.Calls.IS_READ,"0");
			values.put(CallLog.Calls.DATE,"1408486015");
			values.put(CallLog.Calls.DURATION,"100");
			values.put(CallLog.Calls.NUMBER,"4103703434");
			try{
				db.insert(CALL_LOGS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			return 1;
		}
		private int loadDataSet4(SQLiteDatabase db) {
			//Data Set 4
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.TYPE,"3");
			values.put(CallLog.Calls.NEW,"0");
			values.put(CallLog.Calls.IS_READ,"0");
			values.put(CallLog.Calls.DATE,"1408386065");
			values.put(CallLog.Calls.DURATION,"100");
			values.put(CallLog.Calls.NUMBER,"4103703434");
			try{
				db.insert(CALL_LOGS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			return 1;
		}
	}
	
	@Override
	public boolean onCreate() {
		context = getContext();
		dbHelper = new DatabaseHelper(context);
		/**
		* Create a write able database which will trigger its 
		* creation if it doesn't already exist.
		*/
		db = dbHelper.getWritableDatabase();
		return (db == null)? false:true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
			case CALL_LOGS:
				return "vnd.android.cursor.dir/calls";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// the TABLE_NAME to query on
		queryBuilder.setTables(CALL_LOGS_TABLE_NAME);
		
		boolean returnNullData = false;

		switch (uriMatcher.match(uri)) {
			case CALL_LOGS:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == ""){
			// No sorting-> sort on names by default
			sortOrder = "_id";
		}
//		Cursor cursor = queryBuilder.query(db, projection, selection, 
//				selectionArgs, null, null, sortOrder);
		Cursor cursor = null;
		if(!returnNullData) {
			cursor = queryBuilder.query(db, projection, null, null, null, null, null);
			/** 
			* register to watch a content URI for changes
			*/
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
//		long row = db.insert(CALL_LOGS_TABLE_NAME, "", values);
//		// If record is added successfully
//		if(row > 0) {
//			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
//			getContext().getContentResolver().notifyChange(newUri, null);
//			return newUri;
//		}
//		throw new SQLException("Fail to add a new record into " + uri);
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
//		int count = 0;
//		switch (uriMatcher.match(uri)){
//			case CONTACTS_ID:
//				String id = uri.getLastPathSegment();	//gets the id
//				count = db.delete(TABLE_NAME, CONTACTS_TABLE_ID +  " = " + id + 
//						(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
//				break;
//			case CONTACTS:
//				// delete all the records of the table
//				count = db.delete(TABLE_NAME, selection, selectionArgs);
//				break;
//			default: 
//				throw new IllegalArgumentException("Unsupported URI " + uri);
//		}
//
//		getContext().getContentResolver().notifyChange(uri, null);
//		return count;
		/**
		 * Do nothing and pretend to have changed one row
		 */
		return 1;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
//		int count = 0;
//		switch (uriMatcher.match(uri)){
//			case CONTACTS_ID:
//				count = db.update(TABLE_NAME, values, CONTACTS_TABLE_ID + " = " + uri.getLastPathSegment() + 
//						(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
//				break;
//			case CONTACTS:
//				count = db.update(TABLE_NAME, values, selection, selectionArgs);
//				break;
//			default: 
//				throw new IllegalArgumentException("Unsupported URI " + uri );
//		}
//		getContext().getContentResolver().notifyChange(uri, null);
//		return count;
		/**
		 * Do nothing and pretend to have changed one row
		 */
		return 1;
	}
}