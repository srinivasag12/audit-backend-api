package com.api.central.rmiaudit.entity;



public class RmiAuditModel {

	private Integer auditId;
	
	private Long companyId;
	
	private String auditType;

	private String auditSubType;

	private String auditStatus;

	private String voidReasons;

	private String creationDate;
	
	private String createdBy;

	private String  lastUpdatedDate;
	
	private String lastUpdatedBy;
	
	private String vesselName;
	
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
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
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
		return "RmiAuditModel [auditId=" + auditId + ", companyId=" + companyId + ", auditType=" + auditType
				+ ", auditSubType=" + auditSubType + ", auditStatus=" + auditStatus + ", voidReasons=" + voidReasons
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", vesselName=" + vesselName + ", vesselId=" + vesselId + "]";
	}
	
}
