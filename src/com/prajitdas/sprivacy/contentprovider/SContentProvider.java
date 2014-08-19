package com.prajitdas.sprivacy.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.PolicyChecker;
import com.prajitdas.sprivacy.policymanager.util.AccessControl;
import com.prajitdas.sprivacy.policymanager.util.PolicyQuery;

public class SContentProvider extends ContentProvider {
	/**
	 * Forming the authority of the SPrivacy provider
	 */
	static final String PROVIDER_NAME = SPrivacyApplication.getConstSprivacyAuthority();
	static final String PROVIDER_BASE_URL = SPrivacyApplication.getConstScheme() + PROVIDER_NAME;

	/**
	 * Forming the URIs for the contents that we will provide
	 */
	static final Uri IMAGES_CONTENT_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstImages());
	static final Uri FILES_CONTENT_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstFiles());
	static final Uri VIDEOS_CONTENT_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstVideos());
	static final Uri AUDIOS_CONTENT_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstAudios());
	static final Uri CONTACTS_CONTENT_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstContacts());
	static final Uri CALL_LOGS_CONTENT_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstCallLogs());
	static final Uri CONTACTS_LOOKUP_ID_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstContacts()+
			SPrivacyApplication.getConstSlash()+
			"lookup");
//			"lookup"+
//			SPrivacyApplication.getConstSlash()+
//			"*"+
//			SPrivacyApplication.getConstSlash()+
//			"#");
	static final Uri CONTACTS_DATA_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstContacts()+
			SPrivacyApplication.getConstSlash()+
			"data");
//	static final Uri CONTACTS_DATA_ID_URI = Uri.parse(URL+
//			SPrivacyApplication.getConstSlash()+
//			SPrivacyApplication.getConstContacts()+
//			SPrivacyApplication.getConstSlash()+
//			"data"+
//			SPrivacyApplication.getConstSlash()+
//			"#");
	static final Uri CONTACTS_STATUS_UPDATES_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstContacts()+
			SPrivacyApplication.getConstSlash()+
			"status_updates");
	static final Uri CONTACTS_RAW_CONTACTS_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstContacts()+
			"raw_contacts");
	static final Uri CONTACTS_GROUPS_URI = Uri.parse(PROVIDER_BASE_URL+
			SPrivacyApplication.getConstSlash()+
			SPrivacyApplication.getConstContacts()+
			"groups");

	
    static final String _ID = "_id";
	static final String NAME = "name";

	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstImages(), SPrivacyQuery.IMAGES);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstFiles(), SPrivacyQuery.FILES);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstVideos(), SPrivacyQuery.VIDEOS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstAudios(), SPrivacyQuery.AUDIOS);

		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstCallLogs(), SPrivacyQuery.CALL_LOGS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts(), SPrivacyQuery.CONTACTS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()+"lookup"
				+SPrivacyApplication.getConstSlash()+"*"
				+SPrivacyApplication.getConstSlash()+"#", SPrivacyQuery.CONTACTS_LOOKUP_ID);
		uriMatcher.addURI(PROVIDER_NAME, "data", SPrivacyQuery.CONTACTS_DATA);
		uriMatcher.addURI(PROVIDER_NAME, "data"+SPrivacyApplication.getConstSlash()+"#", SPrivacyQuery.CONTACTS_DATA_ID);
		uriMatcher.addURI(PROVIDER_NAME, "status_updates", SPrivacyQuery.CONTACTS_STATUS_UPDATES);
		uriMatcher.addURI(PROVIDER_NAME, "raw_contacts", SPrivacyQuery.CONTACTS_RAW_CONTACTS);
		uriMatcher.addURI(PROVIDER_NAME, "groups", SPrivacyQuery.CONTACTS_GROUPS);
	}
	
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

	/**
     * This interface defines constants for the Cursor and CursorLoader
     */
	private interface RealURIsForQuery {
		//Gets the images on external SD card
		Uri imageUri = Images.Media.EXTERNAL_CONTENT_URI;
		//Gets the files on external SD card
		Uri fileUri = Files.getContentUri("external");
		Uri videoUri = Video.Media.EXTERNAL_CONTENT_URI;
		Uri audioUri = Audio.Media.EXTERNAL_CONTENT_URI;
		//Gets the contacts on the device
		Uri contactUri = Contacts.CONTENT_URI;
		/**
		 * The following URIs are to be verified for real and fake contents
		 * ----------------------------------------------------------------------------------------------
		 * Has to be concatenated with the lookup key and if possible the contact id
		 * The form is: content://com.android.contacts/contacts/lookup/<lookup_key>/<optional_contact_id>
		 */
		Uri contactLookupIDUri = Contacts.CONTENT_LOOKUP_URI;
		/**
		 * Has to be concatenated with "data"
		 * The form is: content://com.android.contacts/data
		 */
		Uri contactData = Uri.parse("content://com.android.contacts/data");
		/**
		 * Has to be concatenated with "data" and a contact_id
		 * The form is: content://com.android.contacts/data/<contact_id>
		 */
		Uri contactDataId = Uri.parse("content://com.android.contacts/data");
		/**
		 * Has to be concatenated with "status_updates"
		 * The form is: content://com.android.contacts/status_updates 
		 */
		Uri contactStatusUpdates = Uri.parse("content://com.android.contacts/status_updates");
		/**
		 * Has to be concatenated with "raw_contacts"
		 * The form is: content://com.android.contacts/raw_contacts
		 */
		Uri contactRawContacts = Uri.parse("content://com.android.contacts/raw_contacts");
		/**
		 * Has to be concatenated with "groups"
		 * The form is: content://com.android.contacts/groups
		 */
		Uri contactGroups = Uri.parse("content://com.android.contacts/groups");
		Uri callLogsUri = CallLog.Calls.CONTENT_URI;
	}

	/**
     * This interface defines constants for the Cursor and CursorLoader
     */
	private interface FakeURIsForQuery {
		Uri imageUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstImages()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstImages());
		Uri fileUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstFiles()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstFiles());
		Uri videoUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstVideos()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstVideos());
		Uri audioUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstAudios()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstAudios());
		Uri contactUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstContacts());
		/**
		 * The following URIs are to be verified for real and fake contents
		 * ----------------------------------------------------------------------------------------------
		 * Has to be concatenated with the lookup key and if possible the contact id
		 * The form is: content://com.android.contacts/contacts/lookup/<lookup_key>/<optional_contact_id>
		 */
		Uri contactLookupIDUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"lookup");
		/**
		 * Has to be concatenated with "data"
		 * The form is: content://com.android.contacts/data
		 */
		Uri contactData = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"data");
		/**
		 * Has to be concatenated with "data" and a contact_id
		 * The form is: content://com.android.contacts/data/<contact_id>
		 */
		Uri contactDataId = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"data");
		/**
		 * Has to be concatenated with "status_updates"
		 * The form is: content://com.android.contacts/status_updates 
		 */
		Uri contactStatusUpdates = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"status_updates");
		/**
		 * Has to be concatenated with "raw_contacts"
		 * The form is: content://com.android.contacts/raw_contacts
		 */
		Uri contactRawContacts = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"raw_contacts");
		/**
		 * Has to be concatenated with "groups"
		 * The form is: content://com.android.contacts/groups
		 */
		Uri contactGroups = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"groups");
		Uri callLogsUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstCallLogs()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstCallLogs());
    }

	/**
     * This interface defines constants for the Cursor and CursorLoader
     */
	private interface AnonimyzedURIsForQuery {
		Uri imageUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstImages()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstImages());
		Uri fileUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstFiles()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstFiles());
		Uri videoUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstVideos()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstVideos());
		Uri audioUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstAudios()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstAudios());
		Uri contactUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstContacts());
		/**
		 * The following URIs are to be verified for real and fake contents
		 * ----------------------------------------------------------------------------------------------
		 * Has to be concatenated with the lookup key and if possible the contact id
		 * The form is: content://com.android.contacts/contacts/lookup/<lookup_key>/<optional_contact_id>
		 */
		Uri contactLookupIDUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"lookup");
		/**
		 * Has to be concatenated with "data"
		 * The form is: content://com.android.contacts/data
		 */
		Uri contactData = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"data");
		/**
		 * Has to be concatenated with "data" and a contact_id
		 * The form is: content://com.android.contacts/data/<contact_id>
		 */
		Uri contactDataId = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"data");
		/**
		 * Has to be concatenated with "status_updates"
		 * The form is: content://com.android.contacts/status_updates 
		 */
		Uri contactStatusUpdates = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"status_updates");
		/**
		 * Has to be concatenated with "raw_contacts"
		 * The form is: content://com.android.contacts/raw_contacts
		 */
		Uri contactRawContacts = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"raw_contacts");
		/**
		 * Has to be concatenated with "groups"
		 * The form is: content://com.android.contacts/groups
		 */
		Uri contactGroups = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+"groups");
		Uri callLogsUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstCallLogs()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstCallLogs());
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private AccessControl accessControl;
	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	
	static final String DATABASE_NAME = "Content";
	
	static final String TABLE_NAME = "content";
	
	static final int DATABASE_VERSION = 1;

	static final String CREATE_DB_TABLE =
			" CREATE TABLE " + TABLE_NAME +
			" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			NAME + " TEXT NOT NULL);";
	
	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		return extras;
	}

	private Cursor dataControl(int provider, Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder, 
			Uri realURI, Uri fakeURI, Uri anonimyzedURI) {
//		Log.v(SPrivacyApplication.getDebugTag(), "Came into data control! with projection as follows"); 
//		if(projection!=null)
//			for(String p:projection)
//				Log.v(SPrivacyApplication.getDebugTag(), "data: "+p);
//		Log.v(SPrivacyApplication.getDebugTag(), "sort order "+ sortOrder);
		Cursor c = null;
		if(accessControl.isPolicy()) {
			/*------------------------------Logic for exDialer's discrepancies!!!--------------------------*/
			/**
			 * exDialer has BUGS!!!!!!!!!!!
			 * It tries to sort by "name" while the column should be sort by display_name
			 * It also tries to sort by name when the column does not exist!!!!
			 * So, added this piece of code
			 */
			boolean foundDisplayName = false;
			if(realURI.toString().contains("contacts"))
				for(String oneColumnOfTheProjection : projection)
					if(oneColumnOfTheProjection.contains("display_name"))
						foundDisplayName = true;
			if(sortOrder.equals("name") && foundDisplayName)
				sortOrder = "display_name";
			else
				sortOrder = null;
			/*---------------------------------------------------------------------------------------------*/
			
			c = getContext().getContentResolver()
					.query(realURI,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
//			Media.setUri(getRealUri(provider));
//			int idOfContact;
//			String theLookupKey;
//			if(c.moveToFirst()) {
//				do{
//					idOfContact = c.getInt(ID);
//					theLookupKey = c.getString(LOOKUP_KEY);
//				} while(c.moveToNext());
//			}
			Log.v(SPrivacyApplication.getDebugTag(), "Real URI: "+realURI.toString());
		}
		else {
			if(accessControl.getLevel()==1) {
				c = null;
			}
			else if(accessControl.getLevel()==2) {
				
//				/**
//				 * exDialer has a bug. It tries to sort by "name" while the column should be sort by display_name
//				 * So, added this piece of code
//				 * Worst possible piece of code. Change this to a better logic
//				 */
//				if(realURI.toString().contains("contacts"))
//					for(String oneColumnOfTheProjection : projection)
//						if(oneColumnOfTheProjection.contains("display_name"))
//							sortOrder = "display_name";
//				/*---------------------------------------------------------------------------------------------*/
				
				c = getContext().getContentResolver()
						.query(fakeURI,
								projection, 
								selection, 
								selectionArgs, 
								sortOrder);
//				Media.setUri(getFakeUri(provider));
				Log.v(SPrivacyApplication.getDebugTag(), "Fake URI: "+fakeURI.toString());
			}
			else {
				c = getContext().getContentResolver()
						.query(anonimyzedURI,
								projection, 
								selection, 
								selectionArgs, 
								sortOrder);
//				Media.setUri(getAnonymousUri(provider));
				Log.v(SPrivacyApplication.getDebugTag(), "Anonymized URI: "+anonimyzedURI.toString());
			}
		}
		return c;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
    	return 0;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
			case SPrivacyQuery.IMAGES:
				return "vnd.android.cursor.dir/image";
			case SPrivacyQuery.FILES:
				return "vnd.android.cursor.dir/file";
			case SPrivacyQuery.VIDEOS:
				return "vnd.android.cursor.dir/video";
			case SPrivacyQuery.AUDIOS:
				return "vnd.android.cursor.dir/audio";
			case SPrivacyQuery.CONTACTS:
				return "vnd.android.cursor.dir/contact";
			case SPrivacyQuery.CONTACTS_LOOKUP_ID:
				return "vnd.android.cursor.item/contact";
			case SPrivacyQuery.CONTACTS_DATA:
				return "vnd.android.cursor.item/contact";
			case SPrivacyQuery.CONTACTS_DATA_ID:
				return "vnd.android.cursor.item/contact";
			case SPrivacyQuery.CONTACTS_STATUS_UPDATES:
				return "vnd.android.cursor.item/contact";
			case SPrivacyQuery.CONTACTS_RAW_CONTACTS:
				return "vnd.android.cursor.item/contact";
			case SPrivacyQuery.CONTACTS_GROUPS:
				return "vnd.android.cursor.item/contact";
			case SPrivacyQuery.CALL_LOGS:
				return "vnd.android.cursor.dir/calls";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
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
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		if (sortOrder == null || sortOrder == ""){
			sortOrder = NAME;
		}

		Cursor c;
		switch (uriMatcher.match(uri)) {
			case SPrivacyQuery.IMAGES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setImageData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.FILES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setFileData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.VIDEOS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setVideoData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.AUDIOS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setAudioData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS_LOOKUP_ID:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactLookupIdData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS_DATA:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactDataData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS_DATA_ID:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactDataIdData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS_STATUS_UPDATES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactStatusUpdatesData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS_RAW_CONTACTS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactRawContactsData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CONTACTS_GROUPS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactGroupsData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case SPrivacyQuery.CALL_LOGS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setCallLogsData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
//		if (c.moveToFirst()) {
//			do {
//				Log.v(SPrivacyApplication.getDebugTag(), "id: "+c.getString(0));
//				Log.v(SPrivacyApplication.getDebugTag(), "mimetype: "+c.getString(1));
//				Log.v(SPrivacyApplication.getDebugTag(), "data_version: "+c.getString(2));
//				Log.v(SPrivacyApplication.getDebugTag(), "is_primary: "+c.getString(3));
//				Log.v(SPrivacyApplication.getDebugTag(), "is_super_primary: "+c.getString(4));
//				Log.v(SPrivacyApplication.getDebugTag(), "raw_contact_id: "+c.getString(6));
//				Log.v(SPrivacyApplication.getDebugTag(), "data1: "+c.getString(7));
//				Log.v(SPrivacyApplication.getDebugTag(), "data2: "+c.getString(8));
//				Log.v(SPrivacyApplication.getDebugTag(), "data3: "+c.getString(9));
//				Log.v(SPrivacyApplication.getDebugTag(), "data4: "+c.getString(10));
//				Log.v(SPrivacyApplication.getDebugTag(), "data5: "+c.getString(11));
//				Log.v(SPrivacyApplication.getDebugTag(), "data6: "+c.getString(12));
//				Log.v(SPrivacyApplication.getDebugTag(), "data7: "+c.getString(14));
//				Log.v(SPrivacyApplication.getDebugTag(), "data9: "+c.getString(15));
//				Log.v(SPrivacyApplication.getDebugTag(), "data10: "+c.getString(16));
//				Log.v(SPrivacyApplication.getDebugTag(), "data14: "+c.getString(17));
//			} while (c.moveToNext());
//		}
		return c;
	}
	
	private Cursor setCallLogsData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CALL_LOGS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.callLogsUri, FakeURIsForQuery.callLogsUri, AnonimyzedURIsForQuery.callLogsUri);
	}

	private Cursor setContactData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactUri, FakeURIsForQuery.contactUri, AnonimyzedURIsForQuery.contactUri);
	}

	private Cursor setContactLookupIdData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		modifyUri(uri);
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS_LOOKUP_ID, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactLookupIDUri, FakeURIsForQuery.contactLookupIDUri, AnonimyzedURIsForQuery.contactLookupIDUri);
	}

	private Cursor setContactDataData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS_DATA, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactData, FakeURIsForQuery.contactData, AnonimyzedURIsForQuery.contactData);
	}

	private Cursor setContactDataIdData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS_DATA_ID, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactDataId, FakeURIsForQuery.contactDataId, AnonimyzedURIsForQuery.contactDataId);
	}

	private Cursor setContactStatusUpdatesData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS_STATUS_UPDATES, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactStatusUpdates, FakeURIsForQuery.contactStatusUpdates, AnonimyzedURIsForQuery.contactStatusUpdates);
	}

	private Cursor setContactRawContactsData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS_RAW_CONTACTS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactRawContacts, FakeURIsForQuery.contactRawContacts, AnonimyzedURIsForQuery.contactRawContacts);
	}

	private Cursor setContactGroupsData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		Log.v(SPrivacyApplication.getDebugTag(), "URI: "+uri.toString());
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.CONTACTS_GROUPS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactGroups, FakeURIsForQuery.contactGroups, AnonimyzedURIsForQuery.contactGroups);
	}

	private Cursor setAudioData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstAudios(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.AUDIOS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.audioUri, FakeURIsForQuery.audioUri, AnonimyzedURIsForQuery.audioUri);
	}
	
	private Cursor setFileData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstFiles(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.FILES, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.fileUri, FakeURIsForQuery.fileUri, AnonimyzedURIsForQuery.fileUri);
	}

	private Cursor setImageData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to control based on app name and context
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstImages(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.IMAGES, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.imageUri, FakeURIsForQuery.imageUri, AnonimyzedURIsForQuery.imageUri);
	}
	
    private Cursor setVideoData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
    	//TODO Have to figure out how to control based on app name and context
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstVideos(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(SPrivacyQuery.VIDEOS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.videoUri, FakeURIsForQuery.videoUri, AnonimyzedURIsForQuery.videoUri);
	}
	
	private void modifyUri(Uri uri) {
		String tempUriString = null;
		switch (uriMatcher.match(uri)) {
			case SPrivacyQuery.IMAGES:
				break;
			case SPrivacyQuery.FILES:
				break;
			case SPrivacyQuery.VIDEOS:
				break;
			case SPrivacyQuery.AUDIOS:
				break;
			case SPrivacyQuery.CONTACTS:
				break;
			case SPrivacyQuery.CONTACTS_LOOKUP_ID:
				tempUriString = extractRequiredUriPartForLookupId(uri);
				Uri.withAppendedPath(RealURIsForQuery.contactLookupIDUri, tempUriString);
				Uri.withAppendedPath(FakeURIsForQuery.contactLookupIDUri, tempUriString);
				Uri.withAppendedPath(AnonimyzedURIsForQuery.contactLookupIDUri, tempUriString);
				break;
			case SPrivacyQuery.CONTACTS_DATA:
				break;
			case SPrivacyQuery.CONTACTS_DATA_ID:
				tempUriString = extractRequiredUriPartForDataId(uri);
				Uri.withAppendedPath(RealURIsForQuery.contactDataId, tempUriString);
				Uri.withAppendedPath(FakeURIsForQuery.contactDataId, tempUriString);
				Uri.withAppendedPath(AnonimyzedURIsForQuery.contactDataId, tempUriString);
				break;
			case SPrivacyQuery.CONTACTS_STATUS_UPDATES:
				break;
			case SPrivacyQuery.CONTACTS_RAW_CONTACTS:
				break;
			case SPrivacyQuery.CONTACTS_GROUPS:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private String extractRequiredUriPartForLookupId(Uri input) {
		int countOfParts = input.getPathSegments().size();
		int lookupStringLocation = -1;
		int loopCounter = 0;
		for(String part : input.getPathSegments()) {
			if(part.equals("lookup"))
				lookupStringLocation = loopCounter;
			loopCounter++;
		}
		String extractedPath = null;
		if((countOfParts-lookupStringLocation-1) == 2)
			extractedPath = "/"+input.getPathSegments().get(lookupStringLocation+1)+"/"+input.getPathSegments().get(lookupStringLocation+2);
		else if((countOfParts-lookupStringLocation-1) == 1)
			extractedPath = "/"+input.getPathSegments().get(lookupStringLocation+1);
		else
			extractedPath = "";
		return extractedPath;
	}

	private String extractRequiredUriPartForDataId(Uri input) {
		int countOfParts = input.getPathSegments().size();
		int dataStringLocation = -1;
		int loopCounter = 0;
		for(String part : input.getPathSegments()) {
			if(part.equals("data"))
				dataStringLocation = loopCounter;
			loopCounter++;
		}
		String extractedPath = null;
		if((countOfParts-dataStringLocation-1) == 1)
			extractedPath = "/"+input.getPathSegments().get(dataStringLocation+1);
		else
			extractedPath = "";
		return extractedPath;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
	
	private interface SPrivacyQuery {
		static final int IMAGES = 1;
		static final int FILES = 2;
		static final int VIDEOS = 3;
		static final int AUDIOS = 4;
		static final int CONTACTS = 5;
		static final int CONTACTS_LOOKUP_ID = 6;
		static final int CONTACTS_DATA = 7;
		static final int CONTACTS_DATA_ID = 8;
		static final int CONTACTS_STATUS_UPDATES = 9;
		static final int CONTACTS_RAW_CONTACTS = 10;
		static final int CONTACTS_GROUPS = 11;
		static final int CALL_LOGS = 12;
	}
}