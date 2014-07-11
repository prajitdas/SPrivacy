package com.prajitdas.privacypolicy.util;

import android.net.Uri;

/**
 * This interface defines constants for the provider.
 */
public interface PolicyQuery {
	Uri baseUri = Uri.parse("content://com.prajitdas.privacypolicy.provider/policies");//PolicyProvider.getContentUri();
	String[] projection = null;
	String selection = null;
    String[] selectionArgs = null;
    String sort = " appName DESC";
}