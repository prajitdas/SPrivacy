package com.prajitdas.sprivacy.policymanager.util;

public class PolicyRule {
	private int id;
	private int appId;
	private String appLabel;
	private int resId;
	private String resLabel;
	private boolean rule;
	private int accessLevel;
	private UserContext userContext;
	//Default constructor created to assist db operations
	public PolicyRule() {
	}
	public PolicyRule(int id, int appId, String appLabel, int resId,
			String resLabel, boolean rule, int accessLevel,
			UserContext userContext) {
		setId(id);
		setAppId(appId);
		setAppLabel(appLabel);
		setResId(resId);
		setResLabel(resLabel);
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
	public int getId() {
		return id;
	}
	public String getPolicyText() {
		return 	appLabel + ", " + 
				resLabel + ", " +
				rule;
	}
	public int getResId() {
		return resId;
	}
	public String getResLabel() {
		return resLabel;
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
	public void setResId(int resId) {
		this.resId = resId;
	}
	public void setResLabel(String resLabel) {
		this.resLabel = resLabel;
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
		return 	appLabel + " | " + resLabel;
	}
}