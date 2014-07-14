package com.prajitdas.sprivacy.policyprovider.util;

public class PolicyRules {
	private int id;
	private String appName;
	private String resource;
	private boolean policyRule;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public boolean isPolicyRule() {
		return policyRule;
	}
	public void setPolicyRule(boolean policyRule) {
		this.policyRule = policyRule;
	}
}