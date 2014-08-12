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
	static final String PROVIDER_NAME = SPrivacyApplication.getConstSprivacyAuthority();
	static final String URL = "content://" + PROVIDER_NAME;

	static final Uri IMAGES_CONTENT_URI = Uri.parse(URL 
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstImages());
	
	static final Uri FILES_CONTENT_URI = Uri.parse(URL 
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstFiles());
	static final Uri VIDEOS_CONTENT_URI = Uri.parse(URL 
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstVideos());
	
	static final Uri AUDIOS_CONTENT_URI = Uri.parse(URL 
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstAudios());
	static final Uri CONTACTS_CONTENT_URI = Uri.parse(URL 
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstContacts());
	static final Uri CONTACT_DATA_CONTENT_URI = Uri.parse(URL 
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstContacts()
			+ SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstLookup());
	
	static final String CONTACTS_PROVIDER_NAME = PROVIDER_NAME + SPrivacyApplication.getConstSlash() + SPrivacyApplication.getConstContacts();

//    // columns queried by the contacts app
//	final static int ID = 0;
//    final static int LOOKUP_KEY = 1;
//    final static int DISPLAY_NAME = 2;
//    final static int PHOTO_THUMBNAIL_DATA = 3;
//    final static int SORT_KEY = 4;
    
    static final String _ID = "_id";
	static final String NAME = "name";

	static final int IMAGES = 1;
	static final int FILES = 2;
	static final int VIDEOS = 3;
	static final int AUDIOS = 4;
	static final int CONTACTS = 5;
	static final int CONTACTS_ID = 6;
	
	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstImages(), IMAGES);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstFiles(), FILES);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstVideos(), VIDEOS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstAudios(), AUDIOS);
		uriMatcher.addURI(PROVIDER_NAME, SPrivacyApplication.getConstContacts(), CONTACTS);
		uriMatcher.addURI(CONTACTS_PROVIDER_NAME, SPrivacyApplication.getConstLookup(), CONTACTS_ID);
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
		Uri contactDataUri = Contacts.CONTENT_LOOKUP_URI;
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
		Uri contactDataUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstFakeAuthorityPrefix()
				+SPrivacyApplication.getConstFake()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstLookup());
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
		Uri contactDataUri = Uri.parse(SPrivacyApplication.getConstScheme()
				+SPrivacyApplication.getConstAnonymizedAuthorityPrefix()
				+SPrivacyApplication.getConstAnnonymous()
				+SPrivacyApplication.getConstContacts()
				+SPrivacyApplication.getConstSlash()
				+SPrivacyApplication.getConstLookup());
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

//	private Uri getRealUri(int choice) {
//		switch(choice) {
//			case 1:
//				return Uri.parse("content://media/external/images/media");
//			case 2:
//				return Uri.parse("content://media/external/files/media");
//			case 3:
//				return Uri.parse("content://media/external/videos/media");
//			case 4:
//				return Uri.parse("content://media/external/audios/media");
//			default:
//				return Uri.parse("content://contacts");
//		}
//	}
//
//	private Uri getFakeUri(int choice) {
//		switch(choice) {
//			case 1:
//				return Uri.parse("content://media/external/images/media");
//			case 2:
//				return Uri.parse("content://media/external/files/media");
//			case 3:
//				return Uri.parse("content://media/external/videos/media");
//			case 4:
//				return Uri.parse("content://media/external/audios/media");
//			default:
//				return Uri.parse("content://contacts");
//		}
//	}
//
//	private Uri getAnonymousUri(int choice) {
//		switch(choice) {
//			case 1:
//				return Uri.parse("content://media/external/images/media");
//			case 2:
//				return Uri.parse("content://media/external/files/media");
//			case 3:
//				return Uri.parse("content://media/external/videos/media");
//			case 4:
//				return Uri.parse("content://media/external/audios/media");
//			default:
//				return Uri.parse("content://contacts");
//		}
//	}

	private Cursor dataControl(int provider, Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder, 
			Uri realURI, Uri fakeURI, Uri anonimyzedURI) {
		Log.v(SPrivacyApplication.getDebugTag(), 
				"Real URI: "+realURI.toString()+
				"\nFake URI: "+fakeURI.toString()+
				"\nAnonymized URI: "+anonimyzedURI.toString());
		Cursor c = null;
		if(accessControl.isPolicy()) {
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
		}
		else {
			if(accessControl.getLevel()==1) {
				c = null;
			}
			else if(accessControl.getLevel()==2) {
				c = getContext().getContentResolver()
						.query(fakeURI,
								null, 
								null, 
								null, 
								null);
//				Media.setUri(getFakeUri(provider));
			}
			else {
				c = getContext().getContentResolver()
						.query(anonimyzedURI,
								null, 
								null, 
								null, 
								null);
//				Media.setUri(getAnonymousUri(provider));
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
			case IMAGES:
				return "vnd.android.cursor.dir/image";
			case FILES:
				return "vnd.android.cursor.dir/file";
			case VIDEOS:
				return "vnd.android.cursor.dir/video";
			case AUDIOS:
				return "vnd.android.cursor.dir/audio";
			case CONTACTS:
				return "vnd.android.cursor.dir/contact";
			case CONTACTS_ID:
				return "vnd.android.cursor.dir/contact";
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
			case IMAGES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setImageData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case FILES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setFileData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case VIDEOS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setVideoData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case AUDIOS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setAudioData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case CONTACTS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case CONTACTS_ID:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactSingleData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return c;
	}
	
	private Cursor setAudioData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to return dummy data based on access levels.
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstAudios(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(AUDIOS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.audioUri, FakeURIsForQuery.audioUri, AnonimyzedURIsForQuery.audioUri);
	}
	
	private Cursor setContactData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to return dummy data based on access levels.
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(CONTACTS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactUri, FakeURIsForQuery.contactUri, AnonimyzedURIsForQuery.contactUri);
	}
	
	private Cursor setContactSingleData(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to return dummy data based on access levels.
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstContacts(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(CONTACTS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.contactDataUri, FakeURIsForQuery.contactDataUri, AnonimyzedURIsForQuery.contactDataUri);
	}

	private Cursor setFileData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to return dummy data based on access levels.
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstFiles(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(FILES, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.fileUri, FakeURIsForQuery.fileUri, AnonimyzedURIsForQuery.fileUri);
	}

	private Cursor setImageData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to return dummy data based on access levels.
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstImages(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(IMAGES, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.imageUri, FakeURIsForQuery.imageUri, AnonimyzedURIsForQuery.imageUri);
	}
	
    private Cursor setVideoData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		//TODO Have to figure out how to return dummy data based on access levels.
		accessControl = PolicyChecker.isDataAccessAllowed(new PolicyQuery(
				SPrivacyApplication.getConstVideos(), 
				SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
				null), getContext());
		return dataControl(VIDEOS, uri, projection, selection, selectionArgs, sortOrder, 
				RealURIsForQuery.videoUri, FakeURIsForQuery.videoUri, AnonimyzedURIsForQuery.videoUri);
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}	
//	public static class Media {
//		public static void setUri(Uri uri){
//			EXTERNAL_CONTENT_URI = uri;
//		}
//		public static Uri EXTERNAL_CONTENT_URI;// = Uri.parse("content://media/external/images/media");
//		public static Bitmap getBitmap(ContentResolver cr, Uri url) throws FileNotFoundException, IOException {
//			return Images.Media.getBitmap(cr, url);
//		}
//	}
}