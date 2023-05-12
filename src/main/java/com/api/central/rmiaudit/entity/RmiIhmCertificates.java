package com.api.central.rmiaudit.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="RMI_IHM_CERTIFICATES")
@IdClass(RmiCertificatesCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class RmiIhmCertificates {

	
	
	@Id
	@Column(name="CERTIFICATE_ID")
	private Long certificateID;
	
	@Id
	@Column(name="CERTIFICATE_NUMBER")
	private String  certificateNumber;
	
	@Column(name="REVIEW_ID")
	private Integer  reviewSeqNo;
	
	@Column(name="UNIQUE_TRACKING_NUMBER")
	private String  uniqueTrackingNumber;
	
	@Column(name="CERTIFICATE_URL")
	private String  certificateURL;
	
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	@Column(name="ISSUE_DATE")
	private java.sql.Date  issueDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="EXPIRATION_DATE")
	private java.sql.Date    expirationDate;
	
	@Column(name="ISSUED_BY")
	private String  issuedBy;
	
	@Column(name="CERTIFICATE_STATUS")
	private String  certificateStatus;
	
	@Column(name="VESSEL_NAME")
	private String  vesselName;
	
	@Column(name="VESSEL_ID")
	private Integer vesselID;
	
	
	
	
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CREATION_DATE")
	private java.sql.Date    creationDate;
	
	@Column(name="CREATED_BY")
	private String  createdBy;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="LAST_UPDATE_DATE")
	private java.sql.Date lastUpdateDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String  lastUpdatedBy;
	
	
	@Column(name="CERTIFICATE_ISSUE_TYPE")
	private String certificateIssueType;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="EXTENDED_DATE")
	private java.sql.Date  extendDate;

	@Column(name="SOC_TYPE")
	private String socType;
	
	@Column(name="DOCUMENT_NO")
	private String documentNo;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="SURVERY_COMPLETION_DATE")
	private java.sql.Date  surveryCompletionDate;
	
	public String getSocType() {
		return socType;
	}

	public void setSocType(String socType) {
		this.socType = socType;
	}

	@Transient
	private String auditType;


	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public java.sql.Date getSurveryCompletionDate() {
		return surveryCompletionDate;
	}

	public void setSurveryCompletionDate(java.sql.Date surveryCompletionDate) {
		this.surveryCompletionDate = surveryCompletionDate;
	}

	public Long getCertificateID() {
		return certificateID;
	}

	public void setCertificateID(Long certificateID) {
		this.certificateID = certificateID;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getUniqueTrackingNumber() {
		return uniqueTrackingNumber;
	}

	public void setUniqueTrackingNumber(String uniqueTrackingNumber) {
		this.uniqueTrackingNumber = uniqueTrackingNumber;
	}

	public String getCertificateURL() {
		return certificateURL;
	}

	public void setCertificateURL(String certificateURL) {
		this.certificateURL = certificateURL;
	}

	public java.sql.Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(java.sql.Date issueDate) {
		this.issueDate = issueDate;
	}

	public java.sql.Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(java.sql.Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Integer getReviewSeqNo() {
		return reviewSeqNo;
	}

	public void setReviewSeqNo(Integer reviewSeqNo) {
		this.reviewSeqNo = reviewSeqNo;
	}

	public String getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public Integer getVesselID() {
		return vesselID;
	}

	public void setVesselID(Integer vesselID) {
		this.vesselID = vesselID;
	}

	public java.sql.Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(java.sql.Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public java.sql.Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(java.sql.Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCertificateIssueType() {
		return certificateIssueType;
	}

	public void setCertificateIssueType(String certificateIssueType) {
		this.certificateIssueType = certificateIssueType;
	}

	public java.sql.Date getExtendDate() {
		return extendDate;
	}

	public void setExtendDate(java.sql.Date extendDate) {
		this.extendDate = extendDate;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	
	@Override
	public String toString() {
		return "RmiIhmCertificates [certificateID=" + certificateID + ", certificateNumber=" + certificateNumber
				+ ", uniqueTrackingNumber=" + uniqueTrackingNumber + ", certificateURL=" + certificateURL
				+ ", issueDate=" + issueDate + ", expirationDate=" + expirationDate + ", issuedBy=" + issuedBy
				+ ", certificateStatus=" + certificateStatus + ", vesselName=" + vesselName + ", vesselID=" + vesselID
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy + ", lastUpdateDate=" + lastUpdateDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", certificateIssueType=" + certificateIssueType + ", reviewSeqNo=" + reviewSeqNo
				+ ", extendDate=" + extendDate + "]";
	}

	
	
	
	
	
	

	
	
	
}
