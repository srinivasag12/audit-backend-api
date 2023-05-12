package com.api.central.master.model;

public class PsswordReset {

	private String oldPwd;
	private String newPwd;
	private String email;

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "PsswordReset [oldPwd=" + oldPwd + ", newPwd=" + newPwd + ", email=" + email + "]";
	}

}
