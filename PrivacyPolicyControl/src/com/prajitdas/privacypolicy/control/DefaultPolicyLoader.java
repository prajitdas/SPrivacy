package com.prajitdas.privacypolicy.control;

import android.content.ContentValues;

import com.prajitdas.privacypolicy.PrivacyPolicyApplication;
import com.prajitdas.privacypolicy.provider.PolicyProvider;

public class DefaultPolicyLoader {
	
	/**
	 * At the moment the default policy is being loaded in a dumb way have to work on this to improve it
	 */
	public static void addDefaultPolicies() { 
	    // Add a new policy record
	    ContentValues values = new ContentValues();
	
	    values.put(PolicyProvider.getAppname(), "com.prajitdas.parserapp");	    
	    values.put(PolicyProvider.getResource(), "Images");
	    values.put(PolicyProvider.getPolicy(), 1);
	    PrivacyPolicyApplication.getSingleton().getContentResolver().insert(PolicyProvider.getContentUri(), values);
	}
}