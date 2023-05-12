/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaPortCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaPortCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaPortCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long portId;

	private Long companyId;

	public Long getPortId() {
		return portId;
	}

	public void setPortId(Long portId) {
		this.portId = portId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MaPortCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((portId == null) ? 0 : portId.hashCode());
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
		MaPortCpk other = (MaPortCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (portId == null) {
			if (other.portId != null)
				return false;
		} else if (!portId.equals(other.portId))
			return false;
		return true;
	}

}
