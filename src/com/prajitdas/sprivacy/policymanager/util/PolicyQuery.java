package com.prajitdas.sprivacy.policymanager.util;
/**
 * @author prajit.das
 */
public class PolicyQuery {
	private String providerAuthority;
	private String applicaitonPackageName;
	private UserContext userContext;
	public PolicyQuery(String providerAuthority, String applicaitonPackageName,
			UserContext userContext) {
		setProviderAuthority(providerAuthority);
		setApplicaitonPackageName(applicaitonPackageName);
		if(userContext!=null)
			setUserContext(userContext);
		else
			setUserContext(new UserContext());
	}
	public String getApplicaitonPackageName() {
		return applicaitonPackageName;
	}
	public String getProviderAuthority() {
		return providerAuthority;
	}
	public UserContext getUserContext() {
		return userContext;
	}
	public void setApplicaitonPackageName(String applicaitonPackageName) {
		this.applicaitonPackageName = applicaitonPackageName;
	}
	public void setProviderAuthority(String providerAuthority) {
		this.providerAuthority = providerAuthority;
	}
	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
}