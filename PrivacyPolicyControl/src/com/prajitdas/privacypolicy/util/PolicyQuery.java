package com.prajitdas.privacypolicy.util;

import com.prajitdas.privacypolicy.provider.PolicyProvider;
import android.net.Uri;

/**
 * This interface defines constants for the provider.
 */
public interface PolicyQuery {
	Uri baseUri = PolicyProvider.getContentUri();
	String[] projection = null;
	String selection = null;
    String[] selectionArgs = null;
    String sort = " appName DESC";
}