package com.prajitdas.sprivacy.policymanager.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import com.prajitdas.sprivacy.SPrivacyApplication;

/**
 * Class that loads the default policies from network or other sources into an ArrayList of PolicyRule
 * @TODO This class needs to be modified in order to fix how the default policies are loaded.
 */
public class DefaultDataLoader {
	private Context context;
	
	private ArrayList<AppInfo> applications;

	private ArrayList<ProvInfo> provInfos;

	private ArrayList<PolicyInfo> policies;
	
	private PackageManager packageManager;

	public DefaultDataLoader(Context context) {
		setContext(context);
		applications = new ArrayList<AppInfo>();
		provInfos = new ArrayList<ProvInfo>();
		policies = new ArrayList<PolicyInfo>();
		packageManager = getContext().getPackageManager();
		//Potentially dangerous method, read comment above the method below
		loadExtraData();
		policyLoad();
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
		// Flags: See below
		int flags = PackageManager.GET_META_DATA | 
		            PackageManager.GET_SHARED_LIBRARY_FILES |     
		            PackageManager.GET_UNINSTALLED_PACKAGES | 
		            PackageManager.GET_PERMISSIONS;
		int appCount = 1;//First data is at id 1 in the db so count starts at 1
		for(PackageInfo pack : packageManager.getInstalledPackages(flags)) {
		    if ((pack.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1)
	    		applications.add(new AppInfo(
	    				appCount++,
	    				packageManager.getApplicationLabel(pack.applicationInfo).toString(),
	    				pack.packageName,
	    				permissionsArrayToString(pack.requestedPermissions)));
		}
	}

	/**
	 * Finds all the content providers on the phone and stores them in a database accessible to the whole app 
	 */
	private void setProviderList() {
		ProviderInfo[] providerArray;
		PackageManager pm = getContext().getPackageManager();
		int providerCount = 1;//First data is at id 1 in the db so count starts at 1
		for(PackageInfo pack : pm.getInstalledPackages(PackageManager.GET_PROVIDERS)) {
			providerArray = pack.providers;
			if (providerArray != null)
				for (ProviderInfo provider : providerArray)
    				provInfos.add(new ProvInfo(providerCount++, 
    						pm.getApplicationLabel(provider.applicationInfo).toString(),
    						provider.name,
    						provider.authority,
    						provider.readPermission,
    						provider.writePermission));
		}
	}
	
	public ArrayList<AppInfo> getApplications() {
		return applications;
	}

	public ArrayList<PolicyInfo> getPolicies() {
		return policies;
	}

	public ArrayList<ProvInfo> getProviders() {
		return provInfos;
	}

	/**
	 * At the moment the default policy is being loaded in a naive way have to work on this to improve it
	 */
	private void policyLoad() {
		int indexOfKnownApp = findTheIndex();
		if(indexOfKnownApp >= 0) {
			int count = 0;
			setValues(count++, SPrivacyApplication.getConstImages(), applications.get(indexOfKnownApp));
			setValues(count++, SPrivacyApplication.getConstFiles(), applications.get(indexOfKnownApp));
			setValues(count++, SPrivacyApplication.getConstVideos(), applications.get(indexOfKnownApp));
			setValues(count++, SPrivacyApplication.getConstAudios(), applications.get(indexOfKnownApp));
			setValues(count++, SPrivacyApplication.getConstContacts(), applications.get(indexOfKnownApp));
		}
	}
	private int findTheIndex() {
		int i = 0;
		for(AppInfo app : applications) {
			if(app.isMatch(SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies()))
				return i;
			i++;
		}
		return -1;
	}
	public void setApplications(ArrayList<AppInfo> applications) {
		this.applications = applications;
	}
	
	public void setPolicies(ArrayList<PolicyInfo> policies) {
		this.policies = policies;
	}

	public void setResources(ArrayList<ProvInfo> provInfos) {
		this.provInfos = provInfos;
	}
	private void setValues(int id, String resource, AppInfo anAppInfo) {
		int resCount = provInfos.size()+1;// again the indexing problem so fixed by adding 1
		ProvInfo tempRes = new ProvInfo(resCount++, resource, resource, resource, resource, resource);
		provInfos.add(tempRes);
		PolicyInfo tempPolicy = new PolicyInfo(id,
				anAppInfo.getId(),
				anAppInfo.getLabel(),
				tempRes.getId(),
				tempRes.getLabel(),
				true, // By default load the policy as granted
				0, // By default load the access level as real data
				new UserContext("*","*","*","*")); // By default load context as "under any circumstance"
		policies.add(tempPolicy);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	public String permissionsArrayToString(String[] permissions) {
		StringBuffer tempPerm = new StringBuffer();
		if(permissions != null) {
			for(String perm : permissions)
				tempPerm.append(perm);
		}
		return tempPerm.toString();
	}
}