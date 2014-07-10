package com.prajitdas.sprivacy.contentprovider.util.real;

import com.prajitdas.sprivacy.ProviderApplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class RealImageProvider extends ContentProvider {
	private static final String PROVIDER_NAME = "com.prajitdas.sprivacy.contentprovider.util.real";
	private static final String URL = "content://" + PROVIDER_NAME + "/images";
	public static final Uri CONTENT_URI = Uri.parse(URL);
	
	private static final int IMAGES = 1;
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "images", IMAGES);
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.v(ProviderApplication.getDebugTag(), ImageQuery.baseUri.toString()
											+projection.toString()
											+selection
											+selectionArgs.toString()
											+sortOrder);
		return Media.query(	ProviderApplication.getSingleton().getContentResolver(),
							ImageQuery.baseUri,
							projection, 
							selection, 
							selectionArgs, 
							sortOrder);
	}
	/**
     * This interface defines constants for the Cursor and CursorLoader, based on constants defined
     * in the {@link Images.Media} class.
     */
    private interface ImageQuery {
		Uri baseUri = Images.Media.EXTERNAL_CONTENT_URI;
//		String[] projection = { ImageColumns._ID };
//		String selection = ImageColumns.BUCKET_DISPLAY_NAME + " = 'Camera'";
//	    String[] selectionArgs = null;
//	    String sort = ImageColumns._ID + " DESC LIMIT 1";
    }
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
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