package com.prajitdas.sprivacy.policyprovider.util;

import com.prajitdas.sprivacy.policyprovider.PolicyProvider;

import android.net.Uri;

/**
 * This interface defines constants for the provider.
 */
public interface PolicyQuery {
	Uri baseUri = PolicyProvider.getContentUri();
	String[] projection = null;
	String selection = null;
    String[] selectionArgs = null;
    String sort = PolicyProvider.getAppname() + " DESC";
}