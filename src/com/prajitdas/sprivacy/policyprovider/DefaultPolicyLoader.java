package com.prajitdas.sprivacy.policyprovider;

import java.util.ArrayList;

import com.prajitdas.sprivacy.policyprovider.util.PolicyRules;

public class DefaultPolicyLoader {
	
	private ArrayList<PolicyRules> defaultPolicies;
	
	public ArrayList<PolicyRules> getDefaultPolicies() {
		return defaultPolicies;
	}

	public void setDefaultPolicies(ArrayList<PolicyRules> defaultPolicies) {
		this.defaultPolicies = defaultPolicies;
	}

	public DefaultPolicyLoader() {
		defaultPolicies = new ArrayList<PolicyRules>();
		defaultPolicies.add(naiveWayToAddPolicy());
	}

	/**
	 * At the moment the default policy is being loaded in a naive way have to work on this to improve it
	 */
	private PolicyRules naiveWayToAddPolicy() { 
		PolicyRules temp = new PolicyRules();
		temp.setId(0);
		temp.setAppName("com.prajitdas.parserapp");	    
	    temp.setResource("Images");
	    temp.setPolicyRule(false);
	    return temp;
	}
}