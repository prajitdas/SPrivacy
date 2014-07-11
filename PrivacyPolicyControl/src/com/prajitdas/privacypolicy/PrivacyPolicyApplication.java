package com.prajitdas.privacypolicy;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import com.prajitdas.privacypolicy.provider.util.ApplicationsInfo;

public class PrivacyPolicyApplication extends Application {
	private static ApplicationsInfo applicationsInfo = new ApplicationsInfo();
	private static final String DEBUG_TAG = "ProviderApplicationDebugTag";
	private static PrivacyPolicyApplication singleton;

	public static String getDebugTag() {
		return DEBUG_TAG;
	}

	public static PrivacyPolicyApplication getSingleton() {
		return singleton;
	}

	public static void setSingleton(PrivacyPolicyApplication singleton) {
		PrivacyPolicyApplication.singleton = singleton;
	}

	public static ApplicationsInfo getApplicationsInfo() {
		return applicationsInfo;
	}

	public static void setApplicationsInfo(ApplicationsInfo applicationsInfo) {
		PrivacyPolicyApplication.applicationsInfo = applicationsInfo;
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