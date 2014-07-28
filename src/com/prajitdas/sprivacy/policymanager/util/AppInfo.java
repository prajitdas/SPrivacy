package com.prajitdas.sprivacy.policymanager.util;


public class AppInfo {
	private int id;
	private String label;
	private String packageName;
	private String permissions;
    public AppInfo(int id, String label, String packageName, String permissions) {
		setId(id);
		setLabel(label);
		setPackageName(packageName);
		setPermissions(permissions);
	}
	public int getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}
	public String getPackageName() {
		return packageName;
	}
	public String getPermissions() {
		return permissions;
	}
	public boolean isMatch(String packageName){
		if(this.packageName.equals(packageName))
			return true;
		return false;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	public String getDetailData() {
		return packageName;
	}
	@Override
    public String toString() {
        return label+"\n"+packageName;
    }
}