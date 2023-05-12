/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditCodesCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditCodesCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaAuditCodesCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private String auditCode;

	private Long companyId;

	private Long auditTypeId;

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public MaAuditCodesCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditCode == null) ? 0 : auditCode.hashCode());
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
		MaAuditCodesCpk other = (MaAuditCodesCpk) obj;
		if (auditCode == null) {
			if (other.auditCode != null)
				return false;
		} else if (!auditCode.equals(other.auditCode))
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
