package com.prajitdas.sprivacy.policymanager.util;
/**
 * @purpose: Util class to handle policy and access level information
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class AccessControl {
	private boolean policy;
	private int level;
	public AccessControl(boolean policy, int level) {
		setPolicy(policy);
		setLevel(level);
	}
	public int getLevel() {
		return level;
	}
	public boolean isPolicy() {
		return policy;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setPolicy(boolean policy) {
		this.policy = policy;
	}
	@Override
	public String toString() {
		if(isPolicy())
			return "Access Granted, Real Data";
		else {
			if(getLevel() == 1)
				return "Access Denied, No Data";
			else if(getLevel() == 2)
				return "Access Denied, Fake Data";
			else
				return "Access Denied, Anonymous Data";
		}
	}
}