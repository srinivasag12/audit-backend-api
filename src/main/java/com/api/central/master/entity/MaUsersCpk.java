/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaUsersCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaUsersCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaUsersCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private String emailId;

	private Long companyId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
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
		MaUsersCpk other = (MaUsersCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		return true;
	}

	public MaUsersCpk() {
		super();
	}

}
