package com.prajitdas.sprivacy.contentprovider;

import android.net.Uri;

public interface ProviderContstants {
    public static final String PROVIDER_NAME = "com.prajitdas.sprivacy.contentprovider.util";
    
    public static final String REAL = ".real";
    public static final String FAKE = ".fake";

    public static final String FAKE_IMAGE_URL = "content://" + PROVIDER_NAME + FAKE + "/images";
    public static final Uri FAKE_IMAGE_CONTENT_URI = Uri.parse(FAKE_IMAGE_URL);

    public static final String REAL_IMAGE_URL = "content://" + PROVIDER_NAME + REAL + "/images";
    public static final Uri REAL_IMAGE_CONTENT_URI = Uri.parse(REAL_IMAGE_URL);

    public static final String FAKE_FILE_URL = "content://" + PROVIDER_NAME + FAKE + "/files";
    public static final Uri FAKE_FILE_CONTENT_URI = Uri.parse(FAKE_FILE_URL);

    public static final String REAL_FILE_URL = "content://" + PROVIDER_NAME + REAL + "/files";
    public static final Uri REAL_FILE_CONTENT_URI = Uri.parse(REAL_FILE_URL);
}