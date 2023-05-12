/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaVesselTypeCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure CentralAuditApi\src\main\java\com\api\central\master\entity\MaVesselTypeCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaVesselTypeCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long vesselTypeId;

	private Long companyId;

	public Long getVesselTypeId() {
		return vesselTypeId;
	}

	public void setVesselTypeId(Long vesselTypeId) {
		this.vesselTypeId = vesselTypeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MaVesselTypeCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((vesselTypeId == null) ? 0 : vesselTypeId.hashCode());
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
		MaVesselTypeCpk other = (MaVesselTypeCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (vesselTypeId == null) {
			if (other.vesselTypeId != null)
				return false;
		} else if (!vesselTypeId.equals(other.vesselTypeId))
			return false;
		return true;
	}

}
