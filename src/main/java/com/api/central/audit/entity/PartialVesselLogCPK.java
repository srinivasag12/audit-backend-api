package com.api.central.audit.entity;

import java.io.Serializable;

public class PartialVesselLogCPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long companyId;
	
	private String userId;
	
	private Integer vesselId;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((vesselId == null) ? 0 : vesselId.hashCode());
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
		PartialVesselLogCPK other = (PartialVesselLogCPK) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (vesselId == null) {
			if (other.vesselId != null)
				return false;
		} else if (!vesselId.equals(other.vesselId))
			return false;
		return true;
	}
	
}
