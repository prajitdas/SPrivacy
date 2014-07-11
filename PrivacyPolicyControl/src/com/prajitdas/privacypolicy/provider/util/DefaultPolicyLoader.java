package com.prajitdas.privacypolicy.provider.util;

import com.prajitdas.privacypolicy.PrivacyPolicyApplication;

public class DefaultPolicyLoader {
	public static void loadDefaultPolicies() {
		// TODO Have to take care of proper loading of policies
		// Creating one single default policy for now
		PrivacyPolicyApplication.getApplicationsInfo().getPolicies()
			.add(new ApplicationPolicy(0, "contentParser", "images", false));
	}
}