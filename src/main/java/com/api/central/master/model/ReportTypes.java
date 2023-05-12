package com.api.central.master.model;

import java.sql.Date;

public class ReportTypes {

	private Integer activeStatus;
	private Integer addNewReport;
	private String attachmentTypeDesc;
	private Long attachmentTypeId;
	private Long auditSubTypeId;
	private Long auditTypeId;
	private Long companyId;
	private Object dateIns;  // making these things object class coz we are using diff date formate and we are not using these any where
	private Object lastUpdatedDate;
	private Object lastUpdatedDateVal;
	private Integer mandatory;
	private Object showUpdateFlag;
	private String userIns;

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getAddNewReport() {
		return addNewReport;
	}

	public void setAddNewReport(Integer addNewReport) {
		this.addNewReport = addNewReport;
	}

	public String getAttachmentTypeDesc() {
		return attachmentTypeDesc;
	}

	public void setAttachmentTypeDesc(String attachmentTypeDesc) {
		this.attachmentTypeDesc = attachmentTypeDesc;
	}

	public Long getAttachmentTypeId() {
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Long attachmentTypeId) {
		this.attachmentTypeId = attachmentTypeId;
	}

	public Long getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Long auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Object getDateIns() {
		return dateIns;
	}

	public void setDateIns(Object dateIns) {
		this.dateIns = dateIns;
	}

	public Object getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Object lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Object getLastUpdatedDateVal() {
		return lastUpdatedDateVal;
	}

	public void setLastUpdatedDateVal(Object lastUpdatedDateVal) {
		this.lastUpdatedDateVal = lastUpdatedDateVal;
	}

	public Integer getMandatory() {
		return mandatory;
	}

	public void setMandatory(Integer mandatory) {
		this.mandatory = mandatory;
	}

	public Object getShowUpdateFlag() {
		return showUpdateFlag;
	}

	public void setShowUpdateFlag(Object showUpdateFlag) {
		this.showUpdateFlag = showUpdateFlag;
	}

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	@Override
	public String toString() {
		return "ReportTypes [activeStatus=" + activeStatus + ", addNewReport=" + addNewReport + ", attachmentTypeDesc="
				+ attachmentTypeDesc + ", attachmentTypeId=" + attachmentTypeId + ", auditSubTypeId=" + auditSubTypeId
				+ ", auditTypeId=" + auditTypeId + ", companyId=" + companyId + ", dateIns=" + dateIns
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", lastUpdatedDateVal=" + lastUpdatedDateVal + ", mandatory="
				+ mandatory + ", showUpdateFlag=" + showUpdateFlag + ", userIns=" + userIns + "]";
	}

}
