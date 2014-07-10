package com.prajitdas.sprivacy.contentprovider.util.apppolicy;

public class ApplicationPolicy {
	private int id;
	private String applicationPackageName;
	private String contentType;
	private boolean allowAccess;
	public String getApplicationPackageName() {
		return applicationPackageName;
	}
	public void setApplicationPackageName(String applicationPackageName) {
		this.applicationPackageName = applicationPackageName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean isAllowAccess() {
		return allowAccess;
	}
	public void setAllowAccess(boolean allowAccess) {
		this.allowAccess = allowAccess;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void togglePolicy() {
		if(isAllowAccess())
			setAllowAccess(false);
		else
			setAllowAccess(true);
	}
	public ApplicationPolicy(int id, String applicationPackageName, String contentType,
			boolean allowAccess) {
		this.id = id; 
		this.applicationPackageName = applicationPackageName;
		this.contentType = contentType;
		this.allowAccess = allowAccess;
	}
}