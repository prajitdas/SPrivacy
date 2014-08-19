package com.prajitdas.sprivacy.contentprovider.util.fakecontentproviders;

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
import android.provider.ContactsContract;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;

public class Contacts extends ContentProvider {
	/**
	 * fields for my content provider
	 */
	static final String PROVIDER_NAME = SPrivacyApplication.getConstFakeAuthorityPrefix()
			+SPrivacyApplication.getConstFake()
			+SPrivacyApplication.getConstContacts();
	static final String URL = "content://" + PROVIDER_NAME;
	static final Uri CONTENT_URI = Uri.parse(URL);

    /**
	 * integer values used in content URI
	 */
    static final int CONTACTS = 1;
    static final int CONTACTS_ID = 2;
    static final int CONTACTS_ID_DATA = 3;
    static final int CONTACTS_DATA = 4;
    static final int CONTACTS_GROUPS = 5;
    static final int CONTACTS_RAW_CONTACTS = 6;
    static final int CONTACTS_DATA_ID = 7;
    static final int CONTACTS_LOOKUP_ID = 8;
    static final int CONTACTS_STATUS_UPDATES = 9;
    
    DatabaseHelper dbHelper;

	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts(), CONTACTS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts()+"/#", CONTACTS_ID);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts()+"/#/data", CONTACTS_ID_DATA);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()+"lookup"
				+SPrivacyApplication.getConstSlash()+"*"
				+SPrivacyApplication.getConstSlash()+"#", CONTACTS_LOOKUP_ID);
		uriMatcher.addURI(PROVIDER_NAME, "data", CONTACTS_DATA);
		uriMatcher.addURI(PROVIDER_NAME, "data"+SPrivacyApplication.getConstSlash()+"#", CONTACTS_DATA_ID);
		uriMatcher.addURI(PROVIDER_NAME, "status_updates", CONTACTS_STATUS_UPDATES);
		uriMatcher.addURI(PROVIDER_NAME, "raw_contacts", CONTACTS_RAW_CONTACTS);
		uriMatcher.addURI(PROVIDER_NAME, "groups", CONTACTS_GROUPS);
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private static Context context;

	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "FakeContactContent";
	static final int DATABASE_VERSION = 1;
	
	/**
	 * All the tables being created
	 */
	static final String CONTACTS_TABLE_NAME = "fakeContact";
	static final String CREATE_DB_TABLE_CONTACTS = " CREATE TABLE " + CONTACTS_TABLE_NAME + " (" + 
			ContactsContract.Contacts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			ContactsContract.Contacts.LOOKUP_KEY + " TEXT, " +
			ContactsContract.Contacts.DISPLAY_NAME + " TEXT NOT NULL, " +
			ContactsContract.Contacts.PHOTO_ID + " INTEGER, " +
			ContactsContract.Contacts.STARRED + " INTEGER, " +
			ContactsContract.Contacts.HAS_PHONE_NUMBER + " INTEGER, " +
			ContactsContract.Contacts.CUSTOM_RINGTONE + " TEXT, " +
			ContactsContract.Contacts.SORT_KEY_PRIMARY + " TEXT, " +
			ContactsContract.Contacts.PHOTO_THUMBNAIL_URI + " TEXT, " + 
			ContactsContract.Contacts.SEND_TO_VOICEMAIL + " INTEGER);";

	static final String CONTACTS_DATA_TABLE_NAME = "fakeContactData";
	static final String CREATE_DB_TABLE_CONTACTS_DATA = " CREATE TABLE " + CONTACTS_DATA_TABLE_NAME + " (" + 
			ContactsContract.Data._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			ContactsContract.Data.MIMETYPE + " TEXT, " +
			ContactsContract.Data.DATA_VERSION + " INTEGER, " +
			ContactsContract.Data.IS_PRIMARY + " INTEGER, " +
			ContactsContract.Data.IS_SUPER_PRIMARY + " INTEGER, " +
			ContactsContract.Data.RAW_CONTACT_ID + " INTEGER, " +
			ContactsContract.Data.CONTACT_ID + " INTEGER, " +
			ContactsContract.Data.DATA1 + " TEXT, " +
			ContactsContract.Data.DATA2 + " TEXT, " +
			ContactsContract.Data.DATA3 + " TEXT, " +
			ContactsContract.Data.DATA4 + " TEXT, " +
			ContactsContract.Data.DATA5 + " TEXT, " +
			ContactsContract.Data.DATA6 + " TEXT, " +
			ContactsContract.Data.DATA7 + " TEXT, " +
			ContactsContract.Data.DATA8 + " TEXT, " +
			ContactsContract.Data.DATA9 + " TEXT, " +
			ContactsContract.Data.DATA10 + " TEXT, " +
			ContactsContract.Data.DATA14 + " TEXT, " +
			ContactsContract.Data.DATA15 + " TEXT);";
	
	static final String STATUS_TABLE_ID = "_id";
	static final String CONTACTS_STATUS_UPDATES_TABLE_NAME = "fakeContactStatusUpdates";
	static final String CREATE_DB_TABLE_CONTACTS_STATUS_UPDATES = " CREATE TABLE " + CONTACTS_STATUS_UPDATES_TABLE_NAME + " (" + 
			STATUS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			ContactsContract.StatusUpdates.DATA_ID + " INTEGER, " +
			ContactsContract.StatusUpdates.PROTOCOL + " INTEGER, " +
			ContactsContract.StatusUpdates.IM_HANDLE + " INTEGER);";

	static final String CONTACTS_RAW_DATA_TABLE_NAME = "fakeContactRaw";
	static final String CREATE_DB_TABLE_CONTACTS_RAW = " CREATE TABLE " + CONTACTS_RAW_DATA_TABLE_NAME + " (" + 
			ContactsContract.RawContacts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			ContactsContract.RawContacts.CONTACT_ID + " INTEGER, " +
			ContactsContract.RawContacts.ACCOUNT_NAME + " TEXT, " +
			ContactsContract.RawContacts.ACCOUNT_TYPE + " TEXT, " +
			ContactsContract.RawContacts.DELETED + " INTEGER);";
	
	static final String CONTACTS_GROUPS_TABLE_NAME = "fakeContactGroups";
	static final String CREATE_DB_TABLE_CONTACTS_GROUPS = " CREATE TABLE " + CONTACTS_GROUPS_TABLE_NAME + " (" + 
			ContactsContract.Groups._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			ContactsContract.Groups.TITLE + " TEXT, " +
			ContactsContract.Groups.DELETED + " INTEGER, " + 
			ContactsContract.Groups.SYSTEM_ID + " TEXT);";

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
			db.execSQL(CREATE_DB_TABLE_CONTACTS);
			db.execSQL(CREATE_DB_TABLE_CONTACTS_DATA);
			db.execSQL(CREATE_DB_TABLE_CONTACTS_GROUPS);
			db.execSQL(CREATE_DB_TABLE_CONTACTS_RAW);
			db.execSQL(CREATE_DB_TABLE_CONTACTS_STATUS_UPDATES);
			Log.v(SPrivacyApplication.getDebugTag(), "came into onCreate for Contacts!");
			loaDefaultData(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " +  CREATE_DB_TABLE_CONTACTS);
			db.execSQL("DROP TABLE IF EXISTS " +  CREATE_DB_TABLE_CONTACTS_DATA);
			db.execSQL("DROP TABLE IF EXISTS " +  CREATE_DB_TABLE_CONTACTS_GROUPS);
			db.execSQL("DROP TABLE IF EXISTS " +  CREATE_DB_TABLE_CONTACTS_RAW);
			db.execSQL("DROP TABLE IF EXISTS " +  CREATE_DB_TABLE_CONTACTS_STATUS_UPDATES);
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
//			values.put(ContactsContract.Contacts.LOOKUP_KEY,"148i7d4474d90b0231cf.148i52aee9f68970fc96");
			values.put(ContactsContract.Contacts.LOOKUP_KEY,"johndoe");
			values.put(ContactsContract.Contacts.DISPLAY_NAME,"John Doe");
			values.put(ContactsContract.Contacts.PHOTO_ID,"");
			values.put(ContactsContract.Contacts.HAS_PHONE_NUMBER,"1");
			values.put(ContactsContract.Contacts.STARRED,"0");
			values.put(ContactsContract.Contacts.CUSTOM_RINGTONE,"");
			values.put(ContactsContract.Contacts.SORT_KEY_PRIMARY,"John Doe");
			values.put(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,"");
			values.put(ContactsContract.Contacts.SEND_TO_VOICEMAIL,"0");
			try{
				db.insert(CONTACTS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/postal-address_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"1");
			values.put(ContactsContract.Data.CONTACT_ID,"1");
			values.put(ContactsContract.Data.DATA1,"1 Mordor Lane, Mordor");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"12345");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"Middlearth");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"1");
			values.put(ContactsContract.Data.CONTACT_ID,"1");
			values.put(ContactsContract.Data.DATA1,"4103703743");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
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
//			values.put(ContactsContract.Contacts.LOOKUP_KEY,"148i7d4474d90b0231cf.148i52aee9f68970fc78");
			values.put(ContactsContract.Contacts.LOOKUP_KEY,"janedoe");
			values.put(ContactsContract.Contacts.DISPLAY_NAME,"Jane Doe");
			values.put(ContactsContract.Contacts.PHOTO_ID,"");
			values.put(ContactsContract.Contacts.HAS_PHONE_NUMBER,"1");
			values.put(ContactsContract.Contacts.STARRED,"0");
			values.put(ContactsContract.Contacts.CUSTOM_RINGTONE,"");
			values.put(ContactsContract.Contacts.SORT_KEY_PRIMARY,"Jane Doe");
			values.put(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,"");
			values.put(ContactsContract.Contacts.SEND_TO_VOICEMAIL,"0");
			try{
				db.insert(CONTACTS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/postal-address_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"2");
			values.put(ContactsContract.Data.CONTACT_ID,"2");
			values.put(ContactsContract.Data.DATA1,"1 Mordor Lane, Mordor");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"12345");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"Middlearth");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"2");
			values.put(ContactsContract.Data.CONTACT_ID,"2");
			values.put(ContactsContract.Data.DATA1,"4103703773");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
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
//			values.put(ContactsContract.Contacts.LOOKUP_KEY,"148i7d4474d90b0231cf.148i52aee9f68970fc12");
			values.put(ContactsContract.Contacts.LOOKUP_KEY,"johnnydoe");
			values.put(ContactsContract.Contacts.DISPLAY_NAME,"Johnny Doe");
			values.put(ContactsContract.Contacts.PHOTO_ID,"");
			values.put(ContactsContract.Contacts.HAS_PHONE_NUMBER,"1");
			values.put(ContactsContract.Contacts.STARRED,"0");
			values.put(ContactsContract.Contacts.CUSTOM_RINGTONE,"");
			values.put(ContactsContract.Contacts.SORT_KEY_PRIMARY,"Johnny Doe");
			values.put(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,"");
			values.put(ContactsContract.Contacts.SEND_TO_VOICEMAIL,"0");
			try{
				db.insert(CONTACTS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/postal-address_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"3");
			values.put(ContactsContract.Data.CONTACT_ID,"3");
			values.put(ContactsContract.Data.DATA1,"1 Mordor Lane, Mordor");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"12345");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"Middlearth");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"3");
			values.put(ContactsContract.Data.CONTACT_ID,"3");
			values.put(ContactsContract.Data.DATA1,"4103703763");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
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
//			values.put(ContactsContract.Contacts.LOOKUP_KEY,"148i7d4474d90b0231cf.148i52aee9f68970fc45");
			values.put(ContactsContract.Contacts.LOOKUP_KEY,"joannadoe");
			values.put(ContactsContract.Contacts.DISPLAY_NAME,"Joanna Doe");
			values.put(ContactsContract.Contacts.PHOTO_ID,"");
			values.put(ContactsContract.Contacts.HAS_PHONE_NUMBER,"1");
			values.put(ContactsContract.Contacts.STARRED,"0");
			values.put(ContactsContract.Contacts.CUSTOM_RINGTONE,"");
			values.put(ContactsContract.Contacts.SORT_KEY_PRIMARY,"Joanna Doe");
			values.put(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,"");
			values.put(ContactsContract.Contacts.SEND_TO_VOICEMAIL,"0");
			try{
				db.insert(CONTACTS_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/postal-address_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"4");
			values.put(ContactsContract.Data.CONTACT_ID,"4");
			values.put(ContactsContract.Data.DATA1,"1 Mordor Lane, Mordor");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"12345");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"Middlearth");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
//				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
			} catch (SQLException e) {
	            Log.e("error", "Error inserting " + values, e);
				Log.v(SPrivacyApplication.getDebugTag(), "came into exception for Contacts!");
	            return -1;
			}
			values.clear();
			values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
			values.put(ContactsContract.Data.DATA_VERSION,"1");
			values.put(ContactsContract.Data.IS_PRIMARY,"0");
			values.put(ContactsContract.Data.IS_SUPER_PRIMARY,"0");
			values.put(ContactsContract.Data.RAW_CONTACT_ID,"4");
			values.put(ContactsContract.Data.CONTACT_ID,"4");
			values.put(ContactsContract.Data.DATA1,"4103703783");
			values.put(ContactsContract.Data.DATA2,"Home");
			values.put(ContactsContract.Data.DATA3,"");
			values.put(ContactsContract.Data.DATA4,"");
			values.put(ContactsContract.Data.DATA5,"");
			values.put(ContactsContract.Data.DATA6,"");
			values.put(ContactsContract.Data.DATA7,"");
			values.put(ContactsContract.Data.DATA8,"");
			values.put(ContactsContract.Data.DATA9,"");
			values.put(ContactsContract.Data.DATA10,"");
			values.put(ContactsContract.Data.DATA14,"");
			values.put(ContactsContract.Data.DATA15,"");
			try{
				db.insert(CONTACTS_DATA_TABLE_NAME, null, values);
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
			case CONTACTS_ID:
				return "vnd.android.cursor.item/contact";
			case CONTACTS:
				return "vnd.android.cursor.dir/contact";
			case CONTACTS_LOOKUP_ID:
				return "vnd.android.cursor.item/contact";
			case CONTACTS_DATA:
				return "vnd.android.cursor.item/contact";
			case CONTACTS_DATA_ID:
				return "vnd.android.cursor.item/contact";
			case CONTACTS_STATUS_UPDATES:
				return "vnd.android.cursor.item/contact";
			case CONTACTS_RAW_CONTACTS:
				return "vnd.android.cursor.item/contact";
			case CONTACTS_GROUPS:
				return "vnd.android.cursor.item/contact";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (uriMatcher.match(uri)) {
		case CONTACTS_ID:
			return query(CONTACTS_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_ID_DATA:
			return query(CONTACTS_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS:
			return query(CONTACTS_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_LOOKUP_ID:
			return query(CONTACTS_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_DATA:
			return query(CONTACTS_DATA_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_DATA_ID:
			return query(CONTACTS_DATA_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_STATUS_UPDATES:
			return query(CONTACTS_STATUS_UPDATES_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_RAW_CONTACTS:
			return query(CONTACTS_RAW_DATA_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		case CONTACTS_GROUPS:
			return query(CONTACTS_GROUPS_TABLE_NAME, uri, projection, selection, selectionArgs, sortOrder);
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
	}
	}
	
	private Cursor query(String TABLE_NAME, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// the TABLE_NAME to query on
		queryBuilder.setTables(TABLE_NAME);
		
		boolean returnNullData = false;

		switch (uriMatcher.match(uri)) {
			case CONTACTS_ID:
				queryBuilder.appendWhere(ContactsContract.Contacts._ID + "=" + uri.getLastPathSegment());
				break;
			case CONTACTS_ID_DATA:
				queryBuilder.appendWhere(ContactsContract.Contacts._ID + "=" + getUriWithID(uri).getLastPathSegment());
				break;
			// maps all database column names
			case CONTACTS:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			case CONTACTS_LOOKUP_ID:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			case CONTACTS_DATA:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			case CONTACTS_DATA_ID:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			case CONTACTS_STATUS_UPDATES:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			case CONTACTS_RAW_CONTACTS:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			case CONTACTS_GROUPS:
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
		switch (uriMatcher.match(uri)) {
			case CONTACTS_ID:
				return insert(CONTACTS_TABLE_NAME, uri, values);
			case CONTACTS_ID_DATA:
				return insert(CONTACTS_TABLE_NAME, uri, values);
			case CONTACTS:
				return insert(CONTACTS_TABLE_NAME, uri, values);
			case CONTACTS_LOOKUP_ID:
				return insert(CONTACTS_TABLE_NAME, uri, values);
			case CONTACTS_DATA:
				return insert(CONTACTS_DATA_TABLE_NAME, uri, values);
			case CONTACTS_DATA_ID:
				return insert(CONTACTS_DATA_TABLE_NAME, uri, values);
			case CONTACTS_STATUS_UPDATES:
				return insert(CONTACTS_STATUS_UPDATES_TABLE_NAME, uri, values);
			case CONTACTS_RAW_CONTACTS:
				return insert(CONTACTS_RAW_DATA_TABLE_NAME, uri, values);
			case CONTACTS_GROUPS:
				return insert(CONTACTS_GROUPS_TABLE_NAME, uri, values);
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private Uri insert(String TABLE_NAME, Uri uri, ContentValues values) {
		long row = db.insert(TABLE_NAME, "", values);
		// If record is added successfully
		if(row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);
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
//	
//    /**
//     * This interface defines constants used by contact retrieval queries.
//     */
//    private interface ContactDetailQuery {
//    	final static String CONTACT_ID = Contacts.CONTACTS_TABLE_ID;
//    	final static String DISPLAY_NAME = Contacts.DISPLAY_NAME;
//    }

	private Uri getUriWithID(Uri inputUri) {
        return Uri.parse(inputUri.toString().substring(0,inputUri.toString().lastIndexOf(inputUri.getLastPathSegment())-1));
	}
	
    /**
     * This interface defines constants used by address retrieval queries.
     * The ADDRESS_ID is not required as it is the same as the normal _ID
     */
//    private interface ContactAddressQuery {    	
//    	final static String ADDRESS_ID = StructuredPostal._ID;
//    	final static String FORMATTED_ADDRESS = StructuredPostal.FORMATTED_ADDRESS;
//    	final static String ADDRESS_TYPE = StructuredPostal.TYPE;
//    	final static String ADDRESS_LABEL = StructuredPostal.LABEL;
//    }
}