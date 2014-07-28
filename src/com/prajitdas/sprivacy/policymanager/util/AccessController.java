package com.prajitdas.sprivacy.policymanager.util;

public class AccessController {
	private boolean policy;
	private int level;
	public AccessController(boolean policy, int level) {
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
}