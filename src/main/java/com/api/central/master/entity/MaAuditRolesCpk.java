/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditRolesCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditRolesCpk.java $
**/


package com.api.central.master.entity;

import java.io.Serializable;

public class MaAuditRolesCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long companyId;

	private Long auditRoleId;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getAuditRoleId() {
		return auditRoleId;
	}

	public void setAuditRoleId(Long auditRoleId) {
		this.auditRoleId = auditRoleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditRoleId == null) ? 0 : auditRoleId.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
		MaAuditRolesCpk other = (MaAuditRolesCpk) obj;
		if (auditRoleId == null) {
			if (other.auditRoleId != null)
				return false;
		} else if (!auditRoleId.equals(other.auditRoleId))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

	public MaAuditRolesCpk() {
		super();

	}

}
