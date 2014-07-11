package com.prajitdas.privacypolicy.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class PolicyProvider extends ContentProvider {
	// fields for my content provider
	private static final String PROVIDER_NAME = "com.prajitdas.privacypolicy.provider.Policy";
	private static final String URL = "content://" + PROVIDER_NAME + "/policies";
	private static final Uri CONTENT_URI = Uri.parse(URL);
	
	// fields for the database
	private static final String ID = "id";
	private static final String APPNAME = "appname";
	private static final String RESOURCE = "resource";
	private static final String POLICY = "policy";
		
	// integer values used in content URI
	private static final int POLICIES = 1;
	private static final int POLICIES_ID = 2;
	
	private DBHelper dbHelper;
	
	// projection map for a query
	private static HashMap<String, String> PolicyMap;
	
	// maps content URI "patterns" to the integer values that were set above
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "policies", POLICIES);
		uriMatcher.addURI(PROVIDER_NAME, "policies/#", POLICIES_ID);
	}
	
	// database declarations
	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "PrivacyPolicies";
	private static final String TABLE_NAME = "policies";
	private static final int DATABASE_VERSION = 1;
	private static final String CREATE_TABLE = 
			" CREATE TABLE " + TABLE_NAME +
			" (" + ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			APPNAME + " TEXT NOT NULL, " +
			RESOURCE + " TEXT NOT NULL," +
			POLICY + " INTEGER DEFAULT 0);";
	
	
	// class that creates and manages the provider's database 
	private static class DBHelper extends SQLiteOpenHelper {
	
		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DBHelper.class.getName(), 
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ". Old data will be destroyed");
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}
	
	}
	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new DBHelper(context);
		// permissions to be writable
		database = dbHelper.getWritableDatabase();
		
		if(database == null)
			return false;
		else
			return true;	
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// the TABLE_NAME to query on
		queryBuilder.setTables(TABLE_NAME);
		
		switch (uriMatcher.match(uri)) {
			// maps all database column names
			case POLICIES:
				queryBuilder.setProjectionMap(PolicyMap);
				break;
			case POLICIES_ID:
				queryBuilder.appendWhere( ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == ""){
			// No sorting-> sort on names by default
			sortOrder = APPNAME;
		}
		Cursor cursor = queryBuilder.query(database, projection, selection, 
				selectionArgs, null, null, sortOrder);
		/** 
		* register to watch a content URI for changes
		*/
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = database.insert(TABLE_NAME, "", values);
		
		// If record is added successfully
		if(row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, 
			String[] selectionArgs) {
		int count = 0;
		
		switch (uriMatcher.match(uri)){
			case POLICIES:
				count = database.update(TABLE_NAME, values, selection, selectionArgs);
				break;
			case POLICIES_ID:
				count = database.update(TABLE_NAME, values, ID + 
				" = " + uri.getLastPathSegment() + 
				(!TextUtils.isEmpty(selection) ? " AND (" + 
				selection + ')' : ""), selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unsupported URI " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		
		switch (uriMatcher.match(uri)){
			case POLICIES:
			// delete all the records of the table
			count = database.delete(TABLE_NAME, selection, selectionArgs);
			break;
		case POLICIES_ID:
			String id = uri.getLastPathSegment();	//gets the id
			count = database.delete( TABLE_NAME, ID +  " = " + id + 
			(!TextUtils.isEmpty(selection) ? " AND (" + 
			selection + ')' : ""), selectionArgs);
			break;
		default: 
			throw new IllegalArgumentException("Unsupported URI " + uri);
		}
	
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
		
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
			// Get all policy records 
			case POLICIES:
				return "vnd.android.cursor.dir/vnd.prajitdas.privacy.policies";
			// Get a particular policy 
			case POLICIES_ID:
				return "vnd.android.cursor.item/vnd.prajitdas.privacy.policy";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	public static Uri getContentUri() {
		return CONTENT_URI;
	}

	public static String getId() {
		return ID;
	}

	public static String getAppname() {
		return APPNAME;
	}

	public static String getResource() {
		return RESOURCE;
	}

	public static String getPolicy() {
		return POLICY;
	}
}