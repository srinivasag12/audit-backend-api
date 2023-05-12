/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditSummary.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditSubtypeCpk.java $
**/
package com.api.central.master.entity;

import java.io.Serializable;

public class MaAuditSubtypeCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long auditSubtypeId;

	private Long companyId;

	private Long auditTypeId;

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Long getAuditSubtypeId() {
		return auditSubtypeId;
	}

	public void setAuditSubtypeId(Long auditSubtypeId) {
		this.auditSubtypeId = auditSubtypeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditSubtypeId == null) ? 0 : auditSubtypeId.hashCode());
		result = prime * result + ((auditTypeId == null) ? 0 : auditTypeId.hashCode());
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
		MaAuditSubtypeCpk other = (MaAuditSubtypeCpk) obj;
		if (auditSubtypeId == null) {
			if (other.auditSubtypeId != null)
				return false;
		} else if (!auditSubtypeId.equals(other.auditSubtypeId))
			return false;
		if (auditTypeId == null) {
			if (other.auditTypeId != null)
				return false;
		} else if (!auditTypeId.equals(other.auditTypeId))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

	public MaAuditSubtypeCpk() {
		super();
	}

}
