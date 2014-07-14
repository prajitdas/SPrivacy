package com.prajitdas.sprivacy.contentprovider;

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
import android.provider.MediaStore.Images;

public class SContentProvider extends ContentProvider {
	static final String PROVIDER_NAME = "com.prajitdas.contentprovider";
	static final String URL = "content://" + PROVIDER_NAME + "/images";
	static final Uri CONTENT_URI = Uri.parse(URL);
	
	static final String _ID = "_id";
	static final String NAME = "imagefilename";
	
	static final int IMAGES = 1;
	
	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "images", IMAGES);
	}
	
	private static HashMap<String, String> IMAGES_PROJECTION_MAP;
	
	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "Content";
	static final String IMAGES_TABLE_NAME = "images";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE =
			" CREATE TABLE " + IMAGES_TABLE_NAME +
			" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			NAME + " TEXT NOT NULL);";
	
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
			db.execSQL("DROP TABLE IF EXISTS " +  IMAGES_TABLE_NAME);
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
	public Uri insert(Uri uri, ContentValues values) {
		/**
		* Add a new student record
		*/
		long rowID = db.insert(IMAGES_TABLE_NAME, "", values);
		/** 
		* If record is added successfully
		*/
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
	
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(IMAGES_TABLE_NAME);
		
		switch (uriMatcher.match(uri)) {
			case IMAGES:
				qb.setProjectionMap(IMAGES_PROJECTION_MAP);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == ""){
			/** 
			* By default sort on student names
			*/
			sortOrder = NAME;
		}
		Cursor c;
		if(isDataAccessAllowed()) {
			c = getContext().getContentResolver()
					.query(ImageQuery.baseUri,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
		}
		else
			c = null;
		return c;
	}
	
	/**
	 * This code should be able to find what policies are there to protect what content.
	 * @return
	 */
	private boolean isDataAccessAllowed() {
 
		return false;
	}

	/**
     * This interface defines constants for the Cursor and CursorLoader, based on constants defined
     * in the {@link Images.Media} class.
     */
	private interface ImageQuery {
		Uri baseUri = Images.Media.EXTERNAL_CONTENT_URI;
    }
    
    @Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		
		switch (uriMatcher.match(uri)){
			case IMAGES:
				count = db.delete(IMAGES_TABLE_NAME, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
	}
	
	getContext().getContentResolver().notifyChange(uri, null);
	return count;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		
		switch (uriMatcher.match(uri)){
			case IMAGES:
				count = db.update(IMAGES_TABLE_NAME, values, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
			/**
			* Get all student records 
			*/
			case IMAGES:
				return "vnd.android.cursor.dir/image";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
}