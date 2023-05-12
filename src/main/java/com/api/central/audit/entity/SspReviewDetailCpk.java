package com.api.central.audit.entity;

import java.io.Serializable;

public class SspReviewDetailCpk implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private AuditDetail auditDetail;

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditDetail == null) ? 0 : auditDetail.hashCode());
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
		SspReviewDetailCpk other = (SspReviewDetailCpk) obj;
		if (auditDetail == null) {
			if (other.auditDetail != null)
				return false;
		} else if (!auditDetail.equals(other.auditDetail))
			return false;
		return true;
	}

	public SspReviewDetailCpk() {
		super();
	}

}
