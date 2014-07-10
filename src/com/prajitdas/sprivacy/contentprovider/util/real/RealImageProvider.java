package com.prajitdas.sprivacy.contentprovider.util.real;

import com.prajitdas.sprivacy.ProviderApplication;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;

public class RealImageProvider {
	private static final String PROVIDER_NAME = "com.prajitdas.sprivacy.contentprovider.util.real";
	private static final String URL = "content://" + PROVIDER_NAME + "/images";
	public static final Uri CONTENT_URI = Uri.parse(URL);
	
	private static final int IMAGES = 1;
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "images", IMAGES);
	}
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
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
}