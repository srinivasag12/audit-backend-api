package com.api.central.rmiaudit.entity;

import java.sql.Date;
import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="RMI_AUDIT")
@IdClass(RmiAuditCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RmiAudit {

	@Id
	@Column(name="AUDIT_ID")
	private Integer auditId;
	
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE")
	private String auditType;
	
	@Column(name="AUDIT_SUB_TYPE")
	private String auditSubType;
	
	@Column(name="AUDIT_STATUS")
	private String auditStatus;
	
	@Column(name="VOID_REASONS")
	private String voidReasons;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CREATION_DATE")
	private java.sql.Date creationDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="LAST_UPDATE_DATE")
	private java.sql.Date  lastUpdatedDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;
	

	@Column(name = "VESSEL_NAME")
	private String vesselName;
	
	@Column(name = "VESSEL_ID")
	private Integer vesselId;
	
	
	
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
	public Integer getAuditId() {
		return auditId;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getAuditSubType() {
		return auditSubType;
	}
	public void setAuditSubType(String auditSubType) {
		this.auditSubType = auditSubType;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getVoidReasons() {
		return voidReasons;
	}
	public void setVoidReasons(String voidReasons) {
		this.voidReasons = voidReasons;
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
	public java.sql.Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(java.sql.Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	@Override
	public String toString() {
		return "RmiAudit [auditId=" + auditId + ", companyId=" + companyId + ", auditType=" + auditType
				+ ", auditSubType=" + auditSubType + ", auditStatus=" + auditStatus + ", voidReasons=" + voidReasons
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", vesselName=" + vesselName + ", vesselId=" + vesselId + "]";
	}
	
	
}
