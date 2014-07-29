package com.prajitdas.sprivacy.policymanager.util;

public class PolicyInfo {
	private int id;
	private int appId;
	private String appLabel;
	private int provId;
	private String provLabel;
	private boolean rule;
	private int accessLevel;
	private UserContext userContext;
	//Default constructor created to assist db operations
	public PolicyInfo() {
	}
	public PolicyInfo(int id, int appId, String appLabel, int provId,
			String provLabel, boolean rule, int accessLevel,
			UserContext userContext) {
		setId(id);
		setAppId(appId);
		setAppLabel(appLabel);
		setProvId(provId);
		setProvLabel(provLabel);
		setRule(rule);
		setAccessLevel(accessLevel);
		setUserContext(userContext);
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	public int getAppId() {
		return appId;
	}
	public String getAppLabel() {
		return appLabel;
	}
	public String getDetailData() {
		return isRule() ? "Access Granted" : "Access Denied";
	}
	public int getId() {
		return id;
	}
	public String getLabel(){
		return appLabel + " | " + provLabel;
	}
	public String getLabelData(){
		return appLabel + " | " + provLabel;
	}
	public int getProvId() {
		return provId;
	}
	public String getProvLabel() {
		return provLabel;
	}
	public UserContext getUserContext() {
		return userContext;
	}
	public boolean isRule() {
		return rule;
	}
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setProvId(int provId) {
		this.provId = provId;
	}
	public void setProvLabel(String provLabel) {
		this.provLabel = provLabel;
	}
	public void setRule(boolean rule) {
		this.rule = rule;
	}
	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	public void togglePolicyRule() {
		if(isRule())
			setRule(false);
		else
			setRule(true);
	}
	@Override
	public String toString() {
		return 	appLabel + " | " + provLabel;
	}
}