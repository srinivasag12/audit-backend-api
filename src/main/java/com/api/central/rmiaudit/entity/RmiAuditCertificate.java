package com.api.central.rmiaudit.entity;

import org.springframework.format.annotation.DateTimeFormat;



public class RmiAuditCertificate {

	
	private Integer auditID;
	
	private Integer reviewSeqNo;
	
	private Long companyId;
	
	private Long certificateID;
	
	private String  certificateNumber;
	
	private String  uniqueTrackingNumber;
	
	private String  certificateURL;
	
	private String  certificateReasons;
	
	private String issueDate;
	
	private String    expirationDate;
	
	private String  issuedBy;
	
	private String  certificateStatus;
	
	private String  vesselName;
	
	private Integer vesselID;
	
	private String  vesselUK;
	
	private String  vesselPK;
	
	private String  vesselIMONumber;
	
	private Integer grossTonnage;
	
	private String  classSociety;
	
	private String  officialNumber;
	
	private String  callSign;
	
	private String  docTypeNumber;
	
	private String  docType;
	
	private String  docIssuer;
	
	private String    docExpiry;
	
	private String    creationDate;
	
	private String  createdBy;
	
	private String lastUpdateDate;
	
	private String  lastUpdatedBy;
	
	private String companyIMONumber;
	
	private String certificateIssueType;
	
	private String placeofissue;
	
	private String  extendDate;
	
	private String auditType;
	
	private String socType;
	
	private String surveryCompletionDate;
	
	private String documentNo;
	
	public Integer getReviewSeqNo() {
		return reviewSeqNo;
	}
	public void setReviewSeqNo(Integer reviewSeqNo) {
		this.reviewSeqNo = reviewSeqNo;
	}
	public String getSocType() {
		return socType;
	}
	public void setSocType(String socType) {
		this.socType = socType;
	}
	public String getSurveryCompletionDate() {
		return surveryCompletionDate;
	}
	public void setSurveryCompletionDate(String surveryCompletionDate) {
		this.surveryCompletionDate = surveryCompletionDate;
	}
	public String getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
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
	public String getCertificateReasons() {
		return certificateReasons;
	}
	public void setCertificateReasons(String certificateReasons) {
		this.certificateReasons = certificateReasons;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String  getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
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
	public String getVesselUK() {
		return vesselUK;
	}
	public void setVesselUK(String vesselUK) {
		this.vesselUK = vesselUK;
	}
	public String getVesselPK() {
		return vesselPK;
	}
	public void setVesselPK(String vesselPK) {
		this.vesselPK = vesselPK;
	}
	public String getVesselIMONumber() {
		return vesselIMONumber;
	}
	public void setVesselIMONumber(String vesselIMONumber) {
		this.vesselIMONumber = vesselIMONumber;
	}
	public Integer getGrossTonnage() {
		return grossTonnage;
	}
	public void setGrossTonnage(Integer grossTonnage) {
		this.grossTonnage = grossTonnage;
	}
	public String getClassSociety() {
		return classSociety;
	}
	public void setClassSociety(String classSociety) {
		this.classSociety = classSociety;
	}
	public String getOfficialNumber() {
		return officialNumber;
	}
	public void setOfficialNumber(String officialNumber) {
		this.officialNumber = officialNumber;
	}
	public String getCallSign() {
		return callSign;
	}
	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}
	public String getDocTypeNumber() {
		return docTypeNumber;
	}
	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getDocIssuer() {
		return docIssuer;
	}
	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}
	public String getDocExpiry() {
		return docExpiry;
	}
	public void setDocExpiry(String docExpiry) {
		this.docExpiry = docExpiry;
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
		System.err.println("lastupdatedate"+lastUpdateDate);
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		System.err.println("lastupdatedate"+lastUpdateDate);
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getCompanyIMONumber() {
		return companyIMONumber;
	}
	public void setCompanyIMONumber(String companyIMONumber) {
		this.companyIMONumber = companyIMONumber;
	}
	public String getCertificateIssueType() {
		return certificateIssueType;
	}
	public void setCertificateIssueType(String certificateIssueType) {
		this.certificateIssueType = certificateIssueType;
	}
	public String getPlaceofissue() {
		return placeofissue;
	}
	public void setPlaceofissue(String placeofissue) {
		this.placeofissue = placeofissue;
	}
	@Override
	public String toString() {
		return "RmiAuditCertificate [auditID=" + auditID + ", companyId=" + companyId + ", certificateID=" + certificateID
				+ ", certificateNumber=" + certificateNumber + ", uniqueTrackingNumber=" + uniqueTrackingNumber
				+ ", certificateURL=" + certificateURL + ", certificateReasons=" + certificateReasons + ", issueDate="
				+ issueDate + ", expirationDate=" + expirationDate + ", issuedBy=" + issuedBy + ", certificateStatus="
				+ certificateStatus + ", vesselName=" + vesselName + ", vesselID=" + vesselID + ", vesselUK=" + vesselUK
				+ ", vesselPK=" + vesselPK + ", vesselIMONumber=" + vesselIMONumber + ", grossTonnage=" + grossTonnage
				+ ", classSociety=" + classSociety + ", officialNumber=" + officialNumber + ", callSign=" + callSign
				+ ", docTypeNumber=" + docTypeNumber + ", docType=" + docType + ", docIssuer=" + docIssuer
				+ ", docExpiry=" + docExpiry + ", creationDate=" + creationDate + ", createdBy=" + createdBy
				+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy=" + lastUpdatedBy + ", companyIMONumber="
				+ companyIMONumber + ", certificateIssueType=" + certificateIssueType + ", placeofissue=" + placeofissue
				+ ", extendDate=" + extendDate + ", auditType=" + auditType + "]";
	}
	
	

	
}
