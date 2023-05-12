/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaUserRolesCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaUserRolesCpk.java $
**/
package com.api.central.master.entity;

import java.io.Serializable;

public class MaUserRolesCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long roleId;

	private MaUsers userRole;

	public Long getRoleId() {
		return roleId;
	}

	public void ListRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public MaUsers getUserRole() {
		return userRole;
	}

	public void setUserRole(MaUsers userRole) {
		this.userRole = userRole;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public MaUserRolesCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaUserRolesCpk other = (MaUserRolesCpk) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (userRole == null) {
			if (other.userRole != null)
				return false;
		} else if (!userRole.equals(other.userRole))
			return false;
		return true;
	}

}
