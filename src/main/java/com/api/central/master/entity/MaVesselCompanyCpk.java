/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaVesselCompanyCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure CentralAuditApi\src\main\java\com\api\central\master\entity\MaVesselCompanyCpk.java $
**/


package com.api.central.master.entity;

import java.io.Serializable;

public class MaVesselCompanyCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyImoNo;

	private Long companyId;

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MaVesselCompanyCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyImoNo == null) ? 0 : companyImoNo.hashCode());
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
		MaVesselCompanyCpk other = (MaVesselCompanyCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyImoNo == null) {
			if (other.companyImoNo != null)
				return false;
		} else if (!companyImoNo.equals(other.companyImoNo))
			return false;
		return true;
	}

}
