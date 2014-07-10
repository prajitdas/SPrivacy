package com.prajitdas.privacypolicycontrol;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import com.prajitdas.privacypolicycontrol.util.ApplicationsInfo;

public class PrivacyPolicyControlApplication extends Application {
	private static ApplicationsInfo applicationsInfo = new ApplicationsInfo();
	private static final String DEBUG_TAG = "ProviderApplicationDebugTag";
	private static PrivacyPolicyControlApplication singleton;
	
	public static String getDebugTag() {
		return DEBUG_TAG;
	}

	public static PrivacyPolicyControlApplication getSingleton() {
		return singleton;
	}

	public static void setSingleton(PrivacyPolicyControlApplication singleton) {
		PrivacyPolicyControlApplication.singleton = singleton;
	}

	public static ApplicationsInfo getApplicationsInfo() {
		return applicationsInfo;
	}

	public static void setApplicationsInfo(ApplicationsInfo applicationsInfo) {
		PrivacyPolicyControlApplication.applicationsInfo = applicationsInfo;
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