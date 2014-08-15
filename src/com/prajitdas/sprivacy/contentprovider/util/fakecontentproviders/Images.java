package com.prajitdas.sprivacy.contentprovider.util.fakecontentproviders;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.annotation.TargetApi;
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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;

public class Images extends ContentProvider {
	static final String PROVIDER_NAME = SPrivacyApplication.getConstFakeAuthorityPrefix()
			+SPrivacyApplication.getConstFake()
			+SPrivacyApplication.getConstImages();
	static final String URL = "content://" + PROVIDER_NAME;
	static final Uri CONTENT_URI = Uri.parse(URL);

	static Uri imageUri;

	static final String ID = "id";
	static final String _ID = ImageColumns._ID;

	static final int IMAGES = 1;
	
	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstImages(), IMAGES);
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private static Context context;

	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "FakeImageContent";
	static final String TABLE_NAME = "fakeImage";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE =
			" CREATE TABLE " + TABLE_NAME + " (" + 
			ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			_ID + " TEXT NOT NULL);";

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
			loaDefaultData(db, ((BitmapDrawable) context.getResources().getDrawable(R.drawable.dummy)).getBitmap());
//		    if(MediaScannerBroadcastReceiver.mMediaScanning)
//		    	addDefaultData(db, "712");//imageUri.getLastPathSegment().toString());
//		    	addDefaultData(db, "1535");//imageUri.getLastPathSegment().toString());
//		    	addDefaultData(db, imageUri.getLastPathSegment().toString());
		}
			    
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}


		private int addDefaultData(SQLiteDatabase db, String dataToInsert) {
			ContentValues values = new ContentValues();
			values.put(_ID, dataToInsert);
			try{
				db.insert(TABLE_NAME, null, values);
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
	            return -1;
			}
			return 1;
		}
	    
	    @TargetApi(Build.VERSION_CODES.KITKAT)
		private void loaDefaultData(SQLiteDatabase databaseRef, Bitmap finalBitmap) {
//		    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
		    final SQLiteDatabase db = databaseRef;
		    File myDir = context.getExternalFilesDir("images");//new File(root + "/saved_images");
		    Log.v(SPrivacyApplication.getDebugTag(), myDir.getAbsolutePath());
//		    myDir.mkdirs();
		    String fname = "image.png";
		    File file = new File(myDir, fname);
		    if (file.exists())
		        file.delete();
		    try {
		        FileOutputStream out = new FileOutputStream(file);
		        finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		        out.flush();
		        out.close();
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    
		    // Tell the media scanner about the new file so that it is
		    // immediately available to the user.
		    MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null,
		            new MediaScannerConnection.OnScanCompletedListener() {
		                public void onScanCompleted(String path, Uri uri) {
		                    Log.i("ExternalStorage", "Scanned " + path + ":");
		                    Log.i("ExternalStorage", "-> getEncodedPath=" + uri.getEncodedPath());
		                    Log.i("ExternalStorage", "-> uri=" + uri);
		                    Log.i("ExternalStorage", "-> getAuthority=" + uri.getAuthority());
		                    Log.i("ExternalStorage", "-> getLastPathSegment=" + uri.getLastPathSegment());
		                    Log.i("ExternalStorage", "-> imageUri=" + uri);
		                    imageUri = uri;
//		    		    	addDefaultData(db, imageUri.getLastPathSegment().toString());
		    		    	addDefaultData(db, imageUri.toString());
		                }
		    });
		}
	}
	
	@Override
	public boolean onCreate() {
		context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		/**
		* Create a write able database which will trigger its 
		* creation if it doesn't already exist.
		*/
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
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
//		Cursor cursor = queryBuilder.query(db, projection, selection, 
//		selectionArgs, null, null, sortOrder);
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		/** 
		* register to watch a content URI for changes
		*/
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = db.insert(TABLE_NAME, "", values);
		// If record is added successfully
		if(row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			Log.v(SPrivacyApplication.getDebugTag(), "URI is: "+newUri.toString());
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)){
			case IMAGES:
				// delete all the records of the table
				count = db.delete(TABLE_NAME, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unsupported URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)){
			case IMAGES:
				count = db.update(TABLE_NAME, values, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unsupported URI " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}