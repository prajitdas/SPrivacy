package com.prajitdas.sprivacy.policymanager.util;

public class PolicyRule {
	private int id;
	private int appId;
	private String appName;
	private int resId;
	private String resName;
	private boolean policyRule;
	public int getAppId() {
		return appId;
	}
	public String getAppName() {
		return appName;
	}
	public int getId() {
		return id;
	}
	public String getPolicyText() {
//		return 	Integer.toString(id) + ", " +
//				Integer.toString(appId) + ", " + 
//				appName + ", " + 
//				Integer.toString(resId) + ", " +
//				resName + ", " +
//				policyRule;
		return 	appName + ", " + 
				resName + ", " +
				policyRule;
	}
	public int getResId() {
		return resId;
	}
	public String getResName() {
		return resName;
	}
	public boolean isPolicyRule() {
		return policyRule;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPolicyRule(boolean policyRule) {
		this.policyRule = policyRule;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public void togglePolicyRule() {
		if(isPolicyRule())
			setPolicyRule(false);
		else
			setPolicyRule(true);
	}
	@Override
	public String toString() {
		return 	appName + " | " + resName;
	}
}