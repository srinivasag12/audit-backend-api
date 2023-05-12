package com.api.central.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "MA_CERT_ISSUE_REASON")
@IdClass(MaCertificateIssueReasonCPk.class)
public class MaCertificateIssueReason {
	
	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Id
	@Column(name = "ISSUE_REASON_ID")
	private Long issueReasonId;

	@Column(name = "ISSUE_REASON_DESC")
	private String issueReasonDesc;

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

	public String getIssueReasonDesc() {
		return issueReasonDesc;
	}

	public void setIssueReasonDesc(String issueReasonDesc) {
		this.issueReasonDesc = issueReasonDesc;
	}
	
	
}
