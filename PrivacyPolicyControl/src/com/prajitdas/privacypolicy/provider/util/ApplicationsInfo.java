package com.prajitdas.privacypolicy.provider.util;

import java.util.ArrayList;

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