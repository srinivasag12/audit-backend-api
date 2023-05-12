package com.api.central.User;

import java.io.Serializable;
import java.util.Set;

public class UserDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
		
	private Long companyid;
	
	private Set<Roles> roles;
		
	private String password;
	
	private int activestatus;

	public UserDetail() {
		super();
	}

	public UserDetail(String username, Long companyid, Set<Roles> roles, String password, int activestatus) {
		super();
		this.username = username;
		this.companyid = companyid;
		this.roles = roles;
		this.password = password;
		this.activestatus = activestatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getActivestatus() {
		return activestatus;
	}

	public void setActivestatus(int activestatus) {
		this.activestatus = activestatus;
	}

	@Override
	public String toString() {
		return "UserDetails [username=" + username + ", companyid=" + companyid + ", roles=" + roles + ", password="
				+ password + ", activestatus=" + activestatus + "]";
	}	
}
