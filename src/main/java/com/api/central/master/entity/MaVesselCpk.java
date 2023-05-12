/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaVesselCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure CentralAuditApi\src\main\java\com\api\central\master\entity\MaVesselCpk.java $
**/


package com.api.central.master.entity;

import java.io.Serializable;

public class MaVesselCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer vesselImoNo;

	private Long companyId;

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
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
		result = prime * result + ((vesselImoNo == null) ? 0 : vesselImoNo.hashCode());
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
		MaVesselCpk other = (MaVesselCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (vesselImoNo == null) {
			if (other.vesselImoNo != null)
				return false;
		} else if (!vesselImoNo.equals(other.vesselImoNo))
			return false;
		return true;
	}

	public MaVesselCpk() {
		super();

	}

}
