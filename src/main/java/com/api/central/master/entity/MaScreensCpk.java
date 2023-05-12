/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaScreensCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaScreensCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaScreensCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private String screenData;

	private String screenId;

	private Long companyId;

	public String getScreenData() {
		return screenData;
	}

	public void setScreenData(String screenData) {
		this.screenData = screenData;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MaScreensCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((screenData == null) ? 0 : screenData.hashCode());
		result = prime * result + ((screenId == null) ? 0 : screenId.hashCode());
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
		MaScreensCpk other = (MaScreensCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (screenData == null) {
			if (other.screenData != null)
				return false;
		} else if (!screenData.equals(other.screenData))
			return false;
		if (screenId == null) {
			if (other.screenId != null)
				return false;
		} else if (!screenId.equals(other.screenId))
			return false;
		return true;
	}

}
