package com.api.central.master.entity;

import java.io.Serializable;

public class MaCertificateIssueReasonCPk implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long companyId;

	private Long issueReasonId;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getIssueReasonId() {
		return issueReasonId;
	}

	public void setIssueReasonId(Long issueReasonId) {
		this.issueReasonId = issueReasonId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((issueReasonId == null) ? 0 : issueReasonId.hashCode());
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
		MaCertificateIssueReasonCPk other = (MaCertificateIssueReasonCPk) obj;
		if (issueReasonId == null) {
			if (other.issueReasonId != null)
				return false;
		} else if (!issueReasonId.equals(other.issueReasonId))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

	public MaCertificateIssueReasonCPk() {
		super();

	}

}