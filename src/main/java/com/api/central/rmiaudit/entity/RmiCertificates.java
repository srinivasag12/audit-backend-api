package com.api.central.rmiaudit.entity;

import java.sql.Date;
import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="RMI_CERTIFICATES")
@IdClass(RmiCertificatesCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class RmiCertificates {

	@Column(name="AUDIT_ID")
	private Integer auditID;
	
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Id
	@Column(name="CERTIFICATE_ID")
	private Long certificateID;
	
	@Id
	@Column(name="CERTIFICATE_NUMBER")
	private String  certificateNumber;
	
	@Column(name="UNIQUE_TRACKING_NUMBER")
	private String  uniqueTrackingNumber;
	
	@Column(name="CERTIFICATE_URL")
	private String  certificateURL;
	
	@Column(name="CERTIFICATE_REASONS")
	private String  certificateReasons;
	
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
	
	@Column(name="VESSEL_UK")
	private String  vesselUK;
	
	@Column(name="VESSEL_PK")
	private String  vesselPK;
	
	@Column(name="VESSEL_MO_NUMBER")
	private String  vesselIMONumber;
	
	@Column(name="GROSS_TONNAGE")
	private Integer grossTonnage;
	
	@Column(name="CLASS_SOCIETY")
	private String  classSociety;
	
	@Column(name="OFFICIAL_NUMBER")
	private String  officialNumber;
	
	@Column(name="CALL_SIGN")
	private String  callSign;
	
	@Column(name="DOC_TYPE_NUMBER")
	private String  docTypeNumber;
	
	@Column(name="DOC_TYPE")
	private String  docType;
	
	@Column(name="DOC_ISSUER")
	private String  docIssuer;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DOC_EXPIRY")
	private java.sql.Date    docExpiry;
	
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
	
	@Column(name="COMPANY_IMO_NUMBER")
	private String companyIMONumber;
	
	@Column(name="CERTIFICATE_ISSUE_TYPE")
	private String certificateIssueType;
	
	@Column(name="PLACE_OF_ISSUE")
	private String placeofissue;
	
	@Transient
	private java.sql.Date  extendDate;
	
	@Transient
	private String auditType;
	

	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	
	public java.sql.Date getExtendDate() {
		return extendDate;
	}
	public void setExtendDate(java.sql.Date extendDate) {
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
	public java.sql.Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(java.sql.Date issueDate) {
		this.issueDate = issueDate;
	}
	public java.sql.Date  getExpirationDate() {
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
	public java.sql.Date getDocExpiry() {
		return docExpiry;
	}
	public void setDocExpiry(java.sql.Date docExpiry) {
		this.docExpiry = docExpiry;
	}
	public java.util.Date getCreationDate() {
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
	public java.util.Date getLastUpdateDate() {
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
		return "RmiCertificates [auditID=" + auditID + ", companyId=" + companyId + ", certificateID=" + certificateID
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
