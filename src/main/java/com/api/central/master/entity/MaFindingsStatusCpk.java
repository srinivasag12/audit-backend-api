/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaFindingsStatusCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaFindingsStatusCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaFindingsStatusCpk implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer findingsStatusId;

	private Long companyId;

	private Integer auditTypeId;

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Integer getFindingsStatusId() {
		return findingsStatusId;
	}

	public void setFindingsStatusId(Integer findingsStatusId) {
		this.findingsStatusId = findingsStatusId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MaFindingsStatusCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditTypeId == null) ? 0 : auditTypeId.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((findingsStatusId == null) ? 0 : findingsStatusId.hashCode());
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
		MaFindingsStatusCpk other = (MaFindingsStatusCpk) obj;
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
		if (findingsStatusId == null) {
			if (other.findingsStatusId != null)
				return false;
		} else if (!findingsStatusId.equals(other.findingsStatusId))
			return false;
		return true;
	}

}
