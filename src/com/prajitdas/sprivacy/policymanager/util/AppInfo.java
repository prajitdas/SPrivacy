package com.prajitdas.sprivacy.policymanager.util;

public class AppInfo {
	private int id;
	private String name;
//    private String pname;
//    private String versionName;
//    private int versionCode;
//	private Drawable icon;
    public AppInfo(int id, String name) {//, String pname, String versionName, int versionCode, Drawable icon) {
		setId(id);
		setName(name);
//		setPname(pname);
//		setVersionName(versionName);
//		setVersionCode(versionCode);
//		setIcon(icon);
	}
//	public Drawable getIcon() {
//		return icon;
//	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
//	public String getPname() {
//		return pname;
//	}
//	public int getVersionCode() {
//		return versionCode;
//	}
//	public String getVersionName() {
//		return versionName;
//	}
//	public void setIcon(Drawable icon) {
//		this.icon = icon;
//	}
    public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public void setPname(String pname) {
//		this.pname = pname;
//	}
//	public void setVersionCode(int versionCode) {
//		this.versionCode = versionCode;
//	}
//	public void setVersionName(String versionName) {
//		this.versionName = versionName;
//	}
	@Override
    public String toString() {
//        return name + "\t" + pname + "\t" + versionName + "\t" + versionCode;
        return name;
    }
}