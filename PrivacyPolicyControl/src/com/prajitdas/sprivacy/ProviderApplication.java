package com.prajitdas.sprivacy;

import com.prajitdas.sprivacy.contentprovider.util.ApplicationPolicy;
import com.prajitdas.sprivacy.contentprovider.util.ApplicationsInfo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public class ProviderApplication extends Application {
	private static ApplicationsInfo applicationsInfo = new ApplicationsInfo();
	private static final String DEBUG_TAG = "ProviderApplicationDebugTag";
	private static ProviderApplication singleton;
	
	static {
		applicationsInfo.getPolicies().add(new ApplicationPolicy(0, "contentParser", "images", false));
	}

	public static String getDebugTag() {
		return DEBUG_TAG;
	}

	public static ProviderApplication getSingleton() {
		return singleton;
	}

	public static void setSingleton(ProviderApplication singleton) {
		ProviderApplication.singleton = singleton;
	}

	public static ApplicationsInfo getApplicationsInfo() {
		return applicationsInfo;
	}

	public static void setApplicationsInfo(ApplicationsInfo applicationsInfo) {
		ProviderApplication.applicationsInfo = applicationsInfo;
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