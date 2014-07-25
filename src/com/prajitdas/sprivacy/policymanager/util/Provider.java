package com.prajitdas.sprivacy.policymanager.util;

public class Provider {
	private int id;
	private String name;
	private String authority;
	private String readPermission;
	private String writePermission;
	public Provider(int id, String name, String authority,
			String readPermission, String writePermission) {
		super();
		this.id = id;
		this.name = name;
		this.authority = authority;
		this.readPermission = readPermission;
		this.writePermission = writePermission;
	}
	public String getAuthority() {
		return authority;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
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
	public void setName(String name) {
		this.name = name;
	}
	public void setReadPermission(String readPermission) {
		this.readPermission = readPermission;
	}
	public void setWritePermission(String writePermission) {
		this.writePermission = writePermission;
	}
	@Override
	public String toString(){
		return name;
	}
}