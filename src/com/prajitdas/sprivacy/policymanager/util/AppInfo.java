package com.prajitdas.sprivacy.policymanager.util;

import android.content.pm.PermissionInfo;

public class AppInfo {
	private int id;
	private String name;
	private String permissions;
	public AppInfo(int id, String name, PermissionInfo[] permissions) {
		super();
		setId(id);
		setName(name);
		setPermissions(permissions);
	}
	public AppInfo(int id, String name, String permissions) {
		super();
		setId(id);
		setName(name);
		setPermissions(permissions);
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPermissions() {
		return permissions;
	}
    public boolean isMatch(String appName){
		if(name.equals(appName))
			return true;
		return false;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPermissions(PermissionInfo[] permissions) {
		if(permissions != null && permissions.length > 0) {
			StringBuffer tempPerm = new StringBuffer();
			for(int i = 0; i < permissions.length; i++) {
				tempPerm.append(permissions[i].name);
				if(i != permissions.length-1)
					tempPerm.append(",");
			}
			this.permissions = tempPerm.toString();
		}
		else
			this.permissions = "";
	}
	private void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	@Override
    public String toString() {
        return name;
    }
}