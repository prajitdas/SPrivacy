package com.prajitdas.sprivacy.policymanager.util;

public class UserContext {
	private String location;
	private String activity;
	private String time;
	private String identity;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public UserContext(String location, String activity, String time,
			String identity) {
		setLocation(location);
		setActivity(activity);
		setTime(time);
		setIdentity(identity);
	}
}