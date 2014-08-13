package com.prajitdas.sprivacy.contentprovider.util.fakecontentproviders;

import java.util.HashMap;

import com.prajitdas.sprivacy.SPrivacyApplication;

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
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.text.TextUtils;
import android.util.Log;

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
	 *  fields for the database
	 */
	static final String CONTACTS_TABLE_ID = "_id";
    /**
     * The display name for the contact.
     * <P>Type: TEXT</P>
     */
    static final String DISPLAY_NAME = "display_name";
    /**
     * Reference to the row in the data table holding the photo.  A photo can
     * be referred to either by ID (this field) or by URI (see {@link #PHOTO_THUMBNAIL_URI}
     * and {@link #PHOTO_URI}).
     * If PHOTO_ID is null, consult {@link #PHOTO_URI} or {@link #PHOTO_THUMBNAIL_URI},
     * which is a more generic mechanism for referencing the contact photo, especially for
     * contacts returned by non-local directories (see {@link Directory}).
     *
     * <P>Type: INTEGER REFERENCES data(_id)</P>
     */
    public static final String PHOTO_ID = "photo_id";
    /**
     * Photo file ID of the full-size photo.  If present, this will be used to populate
     * {@link #PHOTO_URI}.  The ID can also be used with
     * {@link FakeContract.DisplayPhoto#CONTENT_URI} to create a URI to the photo.
     * If this is present, {@link #PHOTO_ID} is also guaranteed to be populated.
     *
     * <P>Type: INTEGER</P>
     */
    static final String PHOTO_FILE_ID = "photo_file_id";
    /**
     * A URI that can be used to retrieve the contact's full-size photo.
     * If PHOTO_FILE_ID is not null, this will be populated with a URI based off
     * {@link FakeContract.DisplayPhoto#CONTENT_URI}.  Otherwise, this will
     * be populated with the same value as {@link #PHOTO_THUMBNAIL_URI}.
     * A photo can be referred to either by a URI (this field) or by ID
     * (see {@link #PHOTO_ID}). If either PHOTO_FILE_ID or PHOTO_ID is not null,
     * PHOTO_URI and PHOTO_THUMBNAIL_URI shall not be null (but not necessarily
     * vice versa).  Thus using PHOTO_URI is a more robust method of retrieving
     * contact photos.
     *
     * <P>Type: TEXT</P>
     */
    static final String PHOTO_URI = "photo_uri";
    /**
     * A URI that can be used to retrieve a thumbnail of the contact's photo.
     * A photo can be referred to either by a URI (this field or {@link #PHOTO_URI})
     * or by ID (see {@link #PHOTO_ID}). If PHOTO_ID is not null, PHOTO_URI and
     * PHOTO_THUMBNAIL_URI shall not be null (but not necessarily vice versa).
     * If the content provider does not differentiate between full-size photos
     * and thumbnail photos, PHOTO_THUMBNAIL_URI and {@link #PHOTO_URI} can contain
     * the same value, but either both shall be null or both not null.
     *
     * <P>Type: TEXT</P>
     */
    static final String PHOTO_THUMBNAIL_URI = "photo_thumb_uri";
    /**
     * Flag that reflects the {@link Groups#GROUP_VISIBLE} state of any
     * {@link CommonDataKinds.Gr			// maps all database column names
oupMembership} for this contact.
     */
    static final String IN_VISIBLE_GROUP = "in_visible_group";
    /**
     * Flag that reflects whether this contact represents the user's
     * personal profile entry.
     */
    static final String IS_USER_PROFILE = "is_user_profile";
    /**
     * An indicator of whether this contact has at least one phone number. "1" if there is
     * at least one phone number, "0" otherwise.
     * <P>Type: INTEGER</P>
     */
    static final String HAS_PHONE_NUMBER = "has_phone_number";
    /**
     * An opaque value that contains hints on how to find the contact if
     * its row id changed as a result of a sync or aggregation.
     */
    static final String LOOKUP_KEY = "lookup";
    /**
     * Timestamp (milliseconds since epoch) of when this contact was last updated.  This
     * includes updates to all data associated with this contact including raw contacts.  Any
     * modification (including deletes and inserts) of underlying contact data are also
     * reflected in this timestamp.
     */
    static final String CONTACT_LAST_UPDATED_TIMESTAMP =
            "contact_last_updated_timestamp";
    /**
     * Sort key that takes into account locale-based traditions for sorting
     * names in address books.  The default
     * sort key is {@link #DISPLAY_NAME_PRIMARY}.  For Chinese names
     * the sort key is the name's Pinyin spelling, and for Japanese names
     * it is the Hiragana version of the phonetic name.
     */
    public static final String SORT_KEY_PRIMARY = "sort_key";
    
    /**
	 * integer values used in content URI
	 */
    static final int CONTACTS = 1;
    static final int CONTACTS_ID = 2;
    static final int CONTACTS_ID_DATA = 3;
    
    DatabaseHelper dbHelper;

	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts(), CONTACTS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts()+"/#", CONTACTS_ID);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts()+"/#/data", CONTACTS_ID_DATA);
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private static Context context;

	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "FakeContactContent";
	static final String TABLE_NAME = "fakeContact";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME + " (" + 
			CONTACTS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			DISPLAY_NAME + " TEXT NOT NULL, " +
			SORT_KEY_PRIMARY + " TEXT NOT NULL, " +
			PHOTO_ID + " INTEGER, " +
			PHOTO_FILE_ID + " INTEGER, " +
			PHOTO_URI + " TEXT, " +
			PHOTO_THUMBNAIL_URI + " TEXT, " +
			IN_VISIBLE_GROUP + " INTEGER, " +
			IS_USER_PROFILE + " INTEGER, " +
			HAS_PHONE_NUMBER + " INTEGER, " +
			LOOKUP_KEY + " TEXT, " +
			CONTACT_LAST_UPDATED_TIMESTAMP + " INTEGER, "+
			ContactAddressQuery.FORMATTED_ADDRESS + " TEXT, "+
			ContactAddressQuery.ADDRESS_TYPE + " TEXT, "+
			ContactAddressQuery.ADDRESS_LABEL + " TEXT);";

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
			Log.v(SPrivacyApplication.getDebugTag(), "came into onCreate for Contacts!");
			loaDefaultData(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}
		
		private int loaDefaultData(SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(DISPLAY_NAME,"John Doe");
			values.put(SORT_KEY_PRIMARY,"John Doe");
			values.put(PHOTO_ID,"");
			values.put(PHOTO_FILE_ID,"");
			values.put(PHOTO_URI,"");
			values.put(PHOTO_THUMBNAIL_URI,"");
			values.put(IN_VISIBLE_GROUP,"1");
			values.put(IS_USER_PROFILE,"");
			values.put(HAS_PHONE_NUMBER,"4567890123");
			values.put(LOOKUP_KEY,"johndoe");
			values.put(CONTACT_LAST_UPDATED_TIMESTAMP,"1407544837");
			values.put(ContactAddressQuery.FORMATTED_ADDRESS,"1 Mordor Lane, Mordor, Middlearth");
			values.put(ContactAddressQuery.ADDRESS_TYPE,"Home");
			values.put(ContactAddressQuery.ADDRESS_LABEL,"Home");

			try{
				db.insert(TABLE_NAME, null, values);
				Log.v(SPrivacyApplication.getDebugTag(), "came into loadDefaultData for Contacts!");
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
				return "vnd.android.cursor.dir/contact";
			case CONTACTS:
				return "vnd.android.cursor.dir/contact";
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
			case CONTACTS_ID:
				queryBuilder.appendWhere(CONTACTS_TABLE_ID + "=" + uri.getLastPathSegment());
				break;
			case CONTACTS_ID_DATA:
				queryBuilder.appendWhere(CONTACTS_TABLE_ID + "=" + getUriWithID(uri).getLastPathSegment());
				break;
			// maps all database column names
			case CONTACTS:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == ""){
			// No sorting-> sort on names by default
			sortOrder = CONTACTS_TABLE_ID;
		}
//		Cursor cursor = queryBuilder.query(db, projection, selection, 
//				selectionArgs, null, null, sortOrder);
		Cursor cursor = queryBuilder.query(db, projection, null, null, null, null, null);
		/** 
		* register to watch a content URI for changes
		*/
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	
	private Uri getUriWithID(Uri inputUri) {
        return Uri.parse(inputUri.toString().substring(0,inputUri.toString().lastIndexOf(inputUri.getLastPathSegment())-1));
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
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
		int count = 0;
		switch (uriMatcher.match(uri)){
			case CONTACTS_ID:
				String id = uri.getLastPathSegment();	//gets the id
				count = db.delete(TABLE_NAME, CONTACTS_TABLE_ID +  " = " + id + 
						(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
				break;
			case CONTACTS:
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
			case CONTACTS_ID:
				count = db.update(TABLE_NAME, values, CONTACTS_TABLE_ID + " = " + uri.getLastPathSegment() + 
						(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
				break;
			case CONTACTS:
				count = db.update(TABLE_NAME, values, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unsupported URI " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
//	
//    /**
//     * This interface defines constants used by contact retrieval queries.
//     */
//    private interface ContactDetailQuery {
//    	final static String CONTACT_ID = Contacts.CONTACTS_TABLE_ID;
//    	final static String DISPLAY_NAME = Contacts.DISPLAY_NAME;
//    }

    /**
     * This interface defines constants used by address retrieval queries.
     * The ADDRESS_ID is not required as it is the same as the normal _ID
     */
    private interface ContactAddressQuery {    	
//    	final static String ADDRESS_ID = StructuredPostal._ID;
    	final static String FORMATTED_ADDRESS = StructuredPostal.FORMATTED_ADDRESS;
    	final static String ADDRESS_TYPE = StructuredPostal.TYPE;
    	final static String ADDRESS_LABEL = StructuredPostal.LABEL;
    }
}