package com.api.central.rmiaudit.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="RMI_AUDIT_CERT_ENDORSEMENTS")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RmiAuditCertEndorsements {

	@Column(name="AUDIT_ID")
	private Integer auditID;
	
    @Column(name="COMPANY_ID")
	private Long companyId;
	
	@Column(name="CERTIFICATE_ID")
	private Long certificateID;
	
	@Id
	@Column(name="ENDORSEMENT_ID")
	private Long endorsementID;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="ENDORSEMENT_DATE")
	private java.sql.Date    endorsementDate;
	
	@Column(name="ENDORSEMENT_REASON")
	private String  endorsementReason;
	
	@Column(name="ENDORSED_BY")
	private String  endorsedBy;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CREATION_DATE")
	private java.sql.Date creationDate;
	
	@Column(name="CREATED_BY")
	private String  createdBy;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="LAST_UPDATE_DATE")
	private java.sql.Date lastUpdateDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String  lastUpdatedBy;
	
	@Column(name = "VESSEL_NAME")
	private String vesselName;
	
	@Column(name = "VESSEL_ID")
	private Integer vesselId;
	
	
	
	@ShortDateFormat
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
	public java.sql.Date getEndorsementDate() {
		return endorsementDate;
	}
	public void setEndorsementDate(java.sql.Date endorsementDate) {
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
	@Override
	public String toString() {
		return "RmiAuditCertEndorsements [auditID=" + auditID + ", companyId=" + companyId + ", certificateID="
				+ certificateID + ", endorsementID=" + endorsementID + ", endorsementDate=" + endorsementDate
				+ ", endorsementReason=" + endorsementReason + ", endorsedBy=" + endorsedBy + ", creationDate="
				+ creationDate + ", createdBy=" + createdBy + ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", vesselName=" + vesselName + ", vesselId=" + vesselId + ", extendDate=" + extendDate
				+ ", auditType=" + auditType + "]";
	}
	
	
	
	
}
