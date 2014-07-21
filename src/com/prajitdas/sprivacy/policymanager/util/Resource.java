package com.prajitdas.sprivacy.policymanager.util;

public class Resource {
	private int id;
	private String name;
	public Resource(int id, String resName) {
		setId(id);
		setName(resName);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}