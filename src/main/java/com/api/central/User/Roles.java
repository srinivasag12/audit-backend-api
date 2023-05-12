package com.api.central.User;

import java.io.Serializable;

public class Roles  implements Serializable {

	private static final long serialVersionUID = 1L;
				
	private int roleid;

	private String rolename;

	public Roles() {
		super();
	}

	public Roles(int roleid, String rolename) {
		super();
		this.roleid = roleid;
		this.rolename = rolename;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public String toString() {
		return "Roles [roleid=" + roleid + ", rolename=" + rolename + "]";
	}
}