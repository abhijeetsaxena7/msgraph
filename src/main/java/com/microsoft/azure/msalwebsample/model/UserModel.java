package com.microsoft.azure.msalwebsample.model;

import java.util.Calendar;

public class UserModel {	
	private String defaultPassword;
	private String displayName;
	//format -userName@domain.com domain should be defined in settings by IT admin in integrated project. E.g username@libsys366.onmicrosoft.com
	private String userPrincipalName;	
	private Calendar dob;
	private String department;
	private String mailNickname;
	private String roleId;
	public UserModel() {
	}
	public String getDefaultPassword() {
		return defaultPassword;
	}
	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
	public Calendar getDob() {
		return dob;
	}
	public void setDob(Calendar dob) {
		this.dob = dob;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMailNickname() {
		return mailNickname;
	}
	public void setMailNickname(String mailNickname) {
		this.mailNickname = mailNickname;
	}
}
