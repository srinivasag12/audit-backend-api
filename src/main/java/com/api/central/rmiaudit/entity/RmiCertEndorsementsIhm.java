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
@Table(name="RMI_CERT_ENDORSEMENTS_IHM")
@IdClass(RmiCertEndorsementsIhmCpk.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class RmiCertEndorsementsIhm {

	@Column(name="REVIEW_ID")
	private Integer auditID;
	
	@Id
	@Column(name="CERTIFICATE_ID")
	private Long certificateID;
	
	@Id
	@Column(name="ENDORSEMENT_ID")
	private Long endorsementId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="ENDORSEMENT_DATE")
	private java.sql.Date endorsementDate;
	
	
	
	@Column(name="ENDORSEMENT_REASON")
	private String endorsementReason;
	

	@Column(name="ENDORSED_BY")
	private String endorsedBy;
	
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
	
	
	@Column(name="VESSEL_NAME")
	private String  vesselName;
	
	@Column(name="VESSEL_ID")
	private Integer vesselId;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="EXTENDED_DATE")
	private java.sql.Date extendedDate;

	@Transient
	private String auditType;
	
	public String getAuditType() {
		return auditType;
	}


	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}


	public Integer getAuditID() {
		return auditID;
	}


	public void setAuditID(Integer auditID) {
		this.auditID = auditID;
	}


	public Long getCertificateID() {
		return certificateID;
	}


	public void setCertificateID(Long certificateID) {
		this.certificateID = certificateID;
	}


	public Long getEndorsementId() {
		return endorsementId;
	}


	public void setEndorsementId(Long endorsementId) {
		this.endorsementId = endorsementId;
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


	public java.sql.Date getExtendedDate() {
		return extendedDate;
	}


	public void setExtendedDate(java.sql.Date extendedDate) {
		this.extendedDate = extendedDate;
	}


	@Override
	public String toString() {
		return "RmiCertEndorsementsIhm [auditID=" + auditID + ", certificateID=" + certificateID + ", endorsementId="
				+ endorsementId + ", endorsementDate=" + endorsementDate + ", endorsementReason=" + endorsementReason
				+ ", endorsedBy=" + endorsedBy + ", creationDate=" + creationDate + ", createdBy=" + createdBy
				+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy=" + lastUpdatedBy + ", vesselName="
				+ vesselName + ", vesselID=" + vesselId + ", extendedDate=" + extendedDate + "]";
	}
	
	
}
