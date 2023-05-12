package com.api.central.master.entity;

import java.io.Serializable;

public class MaCertificateReissueReasonCpk implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long companyId;

	private Long reissueReasonId;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getReissueReasonId() {
		return reissueReasonId;
	}

	public void setReissueReasonId(Long reissueReasonId) {
		this.reissueReasonId = reissueReasonId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reissueReasonId == null) ? 0 : reissueReasonId.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
		MaCertificateReissueReasonCpk other = (MaCertificateReissueReasonCpk) obj;
		if (reissueReasonId == null) {
			if (other.reissueReasonId != null)
				return false;
		} else if (!reissueReasonId.equals(other.reissueReasonId))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

	public MaCertificateReissueReasonCpk() {
		super();

	}
	
}
