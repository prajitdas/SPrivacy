package com.prajitdas.sprivacy.contentprovider.util.fakecontentproviders;

import java.util.HashMap;

import com.prajitdas.sprivacy.SPrivacyApplication;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class Images extends ContentProvider {
	static final String PROVIDER_NAME = SPrivacyApplication.getConstFake()+SPrivacyApplication.getConstImages();
	static final String URL = "content://" + PROVIDER_NAME;

	static final String _ID = "_id";
	static final String IMAGE = "image";

	static final int IMAGES = 1;
	
	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstImages(), IMAGES);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "Content";
	static final String TABLE_NAME = "fakeImage";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE =
			" CREATE TABLE " + TABLE_NAME +
			" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			IMAGE + " BLOB NOT NULL);";

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
			db.execSQL(CREATE_DB_TABLE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}
	}
	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
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
			case IMAGES:
				return "vnd.android.cursor.dir/image";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// the TABLE_NAME to query on
		queryBuilder.setTables(TABLE_NAME);
		switch (uriMatcher.match(uri)) {
			// maps all database column names
			case IMAGES:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == ""){
			// No sorting-> sort on names by default
			sortOrder = _ID;
		}
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		/**
		* register to watch a content URI for changes
		*/
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}