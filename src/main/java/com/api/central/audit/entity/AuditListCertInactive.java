package com.api.central.audit.entity;

import java.util.ArrayList;
import java.util.List;

public class AuditListCertInactive {

	private List<Integer> auditIds = new ArrayList<Integer>();
	private Integer certificateId;
	private Integer certIssueId;
	private String certIssueDesc;
	
	public String getCertIssueDesc() {
		return certIssueDesc;
	}

	public void setCertIssueDesc(String certIssueDesc) {
		this.certIssueDesc = certIssueDesc;
	}

	public List<Integer> getAuditIds() {
		return auditIds;
	}

	public void setAuditIds(List<Integer> auditIds) {
		this.auditIds = auditIds;
	}

	public Integer getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Integer certificateId) {
		this.certificateId = certificateId;
	}

	public Integer getCertIssueId() {
		return certIssueId;
	}

	public void setCertIssueId(Integer certIssueId) {
		this.certIssueId = certIssueId;
	}

	@Override
	public String toString() {
		return "AuditListCertInactive [auditIds=" + auditIds + ", certificateId=" + certificateId + ", certIssueId="
				+ certIssueId + "]";
	}

}
