
package com.api.central.audit.entity;


import java.io.Serializable;
import java.sql.Date;

public class CertificateData implements Serializable {

	private static final long serialVersionUID = 1L;
			
	private Integer auditTypeId;	
	
	private Long companyId;
	
	private Integer auditSeqNo;	
		
	private byte[] pdfData64;
	
	private Integer vesselImoNo;
	
	private java.util.Date auditDate;
	
	private String certificateNo;

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

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public byte[] getPdfData64() {
		return pdfData64;
	}

	public void setPdfData64(byte[] pdfData64) {
		this.pdfData64 = pdfData64;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public java.util.Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(java.util.Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}	
	
}	
	
