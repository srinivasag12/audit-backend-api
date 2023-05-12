/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditStatusCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditStatusCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaAuditStatusCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long auditStatusId;

	private Long companyId;

	private Long auditTypeId;

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Long getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(Long auditStatusId) {
		this.auditStatusId = auditStatusId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MaAuditStatusCpk() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditStatusId == null) ? 0 : auditStatusId.hashCode());
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
		MaAuditStatusCpk other = (MaAuditStatusCpk) obj;
		if (auditStatusId == null) {
			if (other.auditStatusId != null)
				return false;
		} else if (!auditStatusId.equals(other.auditStatusId))
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

}
