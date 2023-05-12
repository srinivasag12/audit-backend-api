package com.api.central.audit.entity;

import java.io.Serializable;

public class VesselApprovalDetailsCpk implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private Integer vesselImoNo;
	
	private Integer companyId;

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
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
		VesselApprovalDetailsCpk other = (VesselApprovalDetailsCpk) obj;
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

	public VesselApprovalDetailsCpk() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
