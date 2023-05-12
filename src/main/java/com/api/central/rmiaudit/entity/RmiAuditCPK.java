package com.api.central.rmiaudit.entity;

import java.io.Serializable;

public class RmiAuditCPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer auditId;
	
	private String auditType;

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditId == null) ? 0 : auditId.hashCode());
		result = prime * result + ((auditType == null) ? 0 : auditType.hashCode());
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
		RmiAuditCPK other = (RmiAuditCPK) obj;
		if (auditId == null) {
			if (other.auditId != null)
				return false;
		} else if (!auditId.equals(other.auditId))
			return false;
		if (auditType == null) {
			if (other.auditType != null)
				return false;
		} else if (!auditType.equals(other.auditType))
			return false;
		return true;
	}
	
	
}
