package com.api.central.rmiaudit.entity;

public class RmiAuditCertEndorsementModel {

	private Integer auditID;

	private Long companyId;

	private Long certificateID;

	private Long endorsementID;
	
	private String    endorsementDate;

	private String  endorsementReason;

	private String  endorsedBy;

	private String creationDate;

	private String  createdBy;

	private String lastUpdateDate;

	private String  lastUpdatedBy;

	private String vesselName;

	private Integer vesselId;

	private String  extendDate;
	
	private String auditType;
	
	private Integer reviewSeqNo;
	
	public Integer getReviewSeqNo() {
		return reviewSeqNo;
	}
	public void setReviewSeqNo(Integer reviewSeqNo) {
		this.reviewSeqNo = reviewSeqNo;
	}
	
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	
	public String getExtendDate() {
		return extendDate;
	}
	public void setExtendDate(String extendDate) {
		this.extendDate = extendDate;
	}
	
	
	public String getVesselName() {
		return vesselName;
	}
	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	public Integer getVesselId() {
		return vesselId;
	}
	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}
	public Integer getAuditID() {
		return auditID;
	}
	public void setAuditID(Integer auditID) {
		this.auditID = auditID;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getCertificateID() {
		return certificateID;
	}
	public void setCertificateID(Long certificateID) {
		this.certificateID = certificateID;
	}
	public Long getEndorsementID() {
		return endorsementID;
	}
	public void setEndorsementID(Long endorsementID) {
		this.endorsementID = endorsementID;
	}
	public String getEndorsementDate() {
		return endorsementDate;
	}
	public void setEndorsementDate(String endorsementDate) {
		this.endorsementDate = endorsementDate;
	}
	public String getEndorsementReason() {
		return endorsementReason;
	}
	public void setEndorsementReason(String endorsementReason) {
		this.endorsementReason = endorsementReason;
	}
	public String getEndorsedBy() {
		return endorsedBy;
	}
	public void setEndorsedBy(String endorsedBy) {
		this.endorsedBy = endorsedBy;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	@Override
	public String toString() {
		return "RmiAuditCertEndorsementModel [auditID=" + auditID + ", companyId=" + companyId + ", certificateID="
				+ certificateID + ", endorsementID=" + endorsementID + ", endorsementDate=" + endorsementDate
				+ ", endorsementReason=" + endorsementReason + ", endorsedBy=" + endorsedBy + ", creationDate="
				+ creationDate + ", createdBy=" + createdBy + ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", vesselName=" + vesselName + ", vesselId=" + vesselId + ", extendDate=" + extendDate
				+ ", auditType=" + auditType + "]";
	}
	
	
}
