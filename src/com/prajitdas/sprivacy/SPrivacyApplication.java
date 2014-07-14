package com.prajitdas.sprivacy;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public class SPrivacyApplication extends Application {
	private static final String DEBUG_TAG = "SPrivacyApplicationDebugTag";
	private static SPrivacyApplication singleton;

	public static String getDebugTag() {
		return DEBUG_TAG;
	}

	public static SPrivacyApplication getSingleton() {
		return singleton;
	}

	public static void setSingleton(SPrivacyApplication singleton) {
		SPrivacyApplication.singleton = singleton;
	}

	public static void makeToast(Context context, String someString) {
		Toast.makeText(context, someString, Toast.LENGTH_LONG).show();
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
}