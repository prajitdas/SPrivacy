package com.prajitdas.sprivacy.contentprovider;

import android.net.Uri;

public class PrivacyAwareContentContracts {
    private static final String FAKE_IMAGE_PROVIDER_NAME = "com.prajitdas.sprivacy.contentprovider.util.fake";
    private static final String FAKE_IMAGE_URL = "content://" + FAKE_IMAGE_PROVIDER_NAME;
    private static final Uri FAKE_IMAGE_CONTENT_URI = Uri.parse(FAKE_IMAGE_URL);

    private static final String REAL_IMAGE_PROVIDER_NAME = "com.prajitdas.sprivacy.contentprovider.util.real";
    private static final String REAL_IMAGE_URL = "content://" + REAL_IMAGE_PROVIDER_NAME;
    private static final Uri REAL_IMAGE_CONTENT_URI = Uri.parse(REAL_IMAGE_URL);

    public static Uri getImageContentUri() {
		if(isThisAllowed())
			return REAL_IMAGE_CONTENT_URI;
		return FAKE_IMAGE_CONTENT_URI;
	}
	
	private static boolean isThisAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
}