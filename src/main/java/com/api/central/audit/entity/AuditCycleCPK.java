package com.api.central.audit.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Range;

public class AuditCycleCPK implements Serializable{
	
	private Long companyId;
	
	private Integer vesselImoNo;
	
	private Integer auditTypeId;
	
	
	private Integer cycleSeqNo;
	
	public Integer getCycleSeqNo() {
		return cycleSeqNo;
	}

	public void setCycleSeqNo(Integer cycleSeqNo) {
		this.cycleSeqNo = cycleSeqNo;
	}

	

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditTypeId == null) ? 0 : auditTypeId.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((cycleSeqNo == null) ? 0 : cycleSeqNo.hashCode());
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
		AuditCycleCPK other = (AuditCycleCPK) obj;
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
		if (cycleSeqNo == null) {
			if (other.cycleSeqNo != null)
				return false;
		} else if (!cycleSeqNo.equals(other.cycleSeqNo))
			return false;
		if (vesselImoNo == null) {
			if (other.vesselImoNo != null)
				return false;
		} else if (!vesselImoNo.equals(other.vesselImoNo))
			return false;
		return true;
	}

	
	
	

	

	
	
	


}