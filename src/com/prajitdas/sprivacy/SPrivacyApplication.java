package com.prajitdas.sprivacy;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public class SPrivacyApplication extends Application {
	private static final String DEBUG_TAG = "SPrivacyApplicationDebugTag";

	private static final String CONST_APPNAME = "com.prajitdas.parserapp";

	private static final String CONST_ANNONYMOUS = "anonymous";

	private static final String CONST_FAKE = "fake";

	private static final String CONST_IMAGES = "images";

	private static final String CONST_FILES = "files";

	private static final String CONST_VIDEOS = "videos";

	private static final String CONST_AUDIOS = "audios";

	private static final String CONST_CONTACTS = "contacts";
	
	private static final String CONST_SLASH = "/";
	
	private static final String CONST_ACCESS_GRANTED = "Granted";
	
	private static final String CONST_ACCESS_DENIED = "Denied";
	
	private static SPrivacyApplication singleton;
	
	public static String getDebugTag() {
		return DEBUG_TAG;
	}

	public static String getConstImages() {
		return CONST_IMAGES;
	}

	public static String getConstFiles() {
		return CONST_FILES;
	}

	public static String getConstVideos() {
		return CONST_VIDEOS;
	}

	public static String getConstAudios() {
		return CONST_AUDIOS;
	}

	public static String getConstContacts() {
		return CONST_CONTACTS;
	}

	public static String getConstSlash() {
		return CONST_SLASH;
	}

	public static SPrivacyApplication getSingleton() {
		return singleton;
	}

	public static void makeToast(Context context, String someString) {
		Toast.makeText(context, someString, Toast.LENGTH_LONG).show();
	}

	public static void setSingleton(SPrivacyApplication singleton) {
		SPrivacyApplication.singleton = singleton;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setSingleton(this);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static String getConstAppname() {
		return CONST_APPNAME;
	}

	public static String getConstAccessDenied() {
		return CONST_ACCESS_DENIED;
	}

	public static String getConstAccessGranted() {
		return CONST_ACCESS_GRANTED;
	}

	public static String getConstAnnonymous() {
		return CONST_ANNONYMOUS;
	}

	public static String getConstFake() {
		return CONST_FAKE;
	}
}