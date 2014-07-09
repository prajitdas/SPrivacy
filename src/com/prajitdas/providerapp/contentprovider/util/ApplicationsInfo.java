package com.prajitdas.providerapp.contentprovider.util;

import java.util.ArrayList;

import com.prajitdas.providerapp.contentprovider.util.apppolicy.ApplicationPolicy;

public class ApplicationsInfo {
	private ArrayList<ApplicationPolicy> policies;

	public ApplicationsInfo() {
		setPolicies(new ArrayList<ApplicationPolicy>());
	}

	public ArrayList<ApplicationPolicy> getPolicies() {
		return policies;
	}

	public void setPolicies(ArrayList<ApplicationPolicy> policies) {
		this.policies = policies;
	}
}