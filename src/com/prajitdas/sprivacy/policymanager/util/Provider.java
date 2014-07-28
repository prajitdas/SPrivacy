package com.prajitdas.sprivacy.policymanager.util;

public class Provider {
	private int id;
	private String label;
	private String providerName;
	private String authority;
	private String readPermission;
	private String writePermission;
	public Provider(int id, String label, String providerName,
			String authority, String readPermission, String writePermission) {
		setId(id);
		setLabel(label);
		setProviderName(providerName);
		setAuthority(authority);
		setReadPermission(readPermission);
		setWritePermission(writePermission);
	}
	public String getAuthority() {
		return authority;
	}
	public int getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}
	public String getProviderName() {
		return providerName;
	}
	public String getReadPermission() {
		return readPermission;
	}
	public String getWritePermission() {
		return writePermission;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public void setReadPermission(String readPermission) {
		this.readPermission = readPermission;
	}
	public void setWritePermission(String writePermission) {
		this.writePermission = writePermission;
	}
	public String getDetailData() {
		return authority;
	}	@Override
	public String toString(){
		return label+"\n"+authority;
	}
}