package com.prajitdas.sprivacy.policymanager.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;

/**
 * Class that loads the default policies from network or other sources into an ArrayList of PolicyRule
 * @TODO This class needs to be modified in order to fix how the default policies are loaded.
 */
public class DefaultDataLoader {
	private Context context;
	
	private ArrayList<AppInfo> applications;

	private ArrayList<Resource> resources;

	private ArrayList<PolicyRule> policies;

	public DefaultDataLoader(Context context) {
		setContext(context);
		applications = new ArrayList<AppInfo>();
		resources = new ArrayList<Resource>();
		policies = new ArrayList<PolicyRule>();
		naiveWayToLoadData();
		//Potentially dangerous method, read comment at the method level below
		loadExtraData();
	}

	/**
	 * We have two different representations here, for prototyping purposes we used a simple list of constants 
	 * to represent the resources and applications. However, to show that we can possibly access the app and 
	 * resource information to form more policies we include all apps and resources from the phone in the database 
	 * and is visible through show all policies. By default, unless a policy specifically states how to control 
	 * the data which will flow to the app, complete access is granted.  
	 */
	private void loadExtraData() {
		setApplicationsList();
		setProviderList();
	}

	/**
	 * Finds all the applications on the phone and stores them in a database accessible to the whole app 
	 */
	private void setApplicationsList() {
		int appCount = applications.size();
	    for(PackageInfo pack : getContext().getPackageManager().getInstalledPackages(PackageManager.GET_SIGNATURES)) {
	    	if(pack.applicationInfo.name != null)
	    		applications.add(new AppInfo(appCount++,
	    				pack.applicationInfo.name, 
		    			pack.packageName, 
		    			pack.versionName, 
		    			pack.versionCode, 
		    			pack.applicationInfo.loadIcon(getContext().getPackageManager())));
	    	else
	    		applications.add(new AppInfo(appCount++,
	    				"", 
		    			pack.packageName, 
		    			pack.versionName, 
		    			pack.versionCode, 
		    			pack.applicationInfo.loadIcon(getContext().getPackageManager())));
	    }
	}

	/**
	 * Finds all the content providers on the phone and stores them in a database accessible to the whole app 
	 */
	public void setProviderList() {
		ProviderInfo[] providers;
		int providerCount = resources.size();
		for(PackageInfo pack : getContext().getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
			providers = pack.providers;
			if (providers != null)
				for (ProviderInfo provider : providers)
					resources.add(new Resource(providerCount++, provider.name));
		}
	}
	
	public ArrayList<AppInfo> getApplications() {
		return applications;
	}

	public ArrayList<PolicyRule> getPolicies() {
		return policies;
	}

	public ArrayList<Resource> getResources() {
		return resources;
	}
	/**
	 * At the moment the default policy is being loaded in a naive way have to work on this to improve it
	 */
	private void naiveWayToLoadData() {
		AppInfo tempAppInfo = new AppInfo(1, 
										SPrivacyApplication.getConstAppname(),
										SPrivacyApplication.getConstAppname(),
										SPrivacyApplication.getConstAppname(),
										1,
										getContext().getResources().getDrawable(R.drawable.android));
		applications.add(tempAppInfo);
		
		int count = 1;
		setValues(count, SPrivacyApplication.getConstImages(), tempAppInfo);
		count++;
		setValues(count, SPrivacyApplication.getConstFiles(), tempAppInfo);
		count++;
		setValues(count, SPrivacyApplication.getConstVideos(), tempAppInfo);
		count++;
		setValues(count, SPrivacyApplication.getConstAudios(), tempAppInfo);
		count++;
		setValues(count, SPrivacyApplication.getConstContacts(), tempAppInfo);
		count++;
	}
	public void setApplications(ArrayList<AppInfo> applications) {
		this.applications = applications;
	}
	
	public void setPolicies(ArrayList<PolicyRule> policies) {
		this.policies = policies;
	}

	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}
	private void setValues(int id, String resource, AppInfo anAppInfo) {
		Resource tempRes = new Resource(id, resource);
		PolicyRule tempPolicy = new PolicyRule();
		resources.add(tempRes);
		
		tempPolicy.setId(id);
	    tempPolicy.setPolicyRule(true);
		tempPolicy.setAppId(anAppInfo.getId());
		tempPolicy.setAppName(anAppInfo.getName());
		tempPolicy.setResId(tempRes.getId());
	    tempPolicy.setResName(tempRes.getName());
		policies.add(tempPolicy);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}