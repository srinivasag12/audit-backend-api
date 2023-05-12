package com.api.central.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "MA_CERT_REISSUE_REASON")
@IdClass(MaCertificateReissueReasonCpk.class)
public class MaCertificateReissueReason {
	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Id
	@Column(name = "REISSUE_REASON_ID")
	private Long reissueReasonId;

	@Column(name = "REISSUE_REASON_DESC")
	private String reissueReasonDesc;

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

	public String getReissueReasonDesc() {
		return reissueReasonDesc;
	}

	public void setReissueReasonDesc(String reissueReasonDesc) {
		this.reissueReasonDesc = reissueReasonDesc;
	}

	
	
}
