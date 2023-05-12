/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAttachmentTypes.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAttachmentTypes.java $
**/

package com.api.central.master.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;

@Entity
@Table(name = "MA_ATTACHMENT_TYPES")
@IdClass(MaAttachmentTypesCpk.class)
public class MaAttachmentTypes {

	@Id
	@Column(name = "ATTACHMENT_TYPE_ID")
	private Long attachmentTypeId;

	@Id
	@Column(name = "AUDIT_TYPE_ID")
	private Long auditTypeId;
	
	
	@Id
	@Column(name = "AUDIT_SUB_TYPE_ID")
	private Long auditSubTypeId;

	@Column(name = "ATTACHMENT_TYPE_DESC")
	private String attachmentTypeDesc;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;
	
	
	@Column(name = "MANDATORY")
	private Integer mandatory;
	
	
	
	@LongDateFormat
	@Column(name="LAST_UPDATED_DATE")
	private Timestamp lastUpdatedDate;
	
	
	
	
	

	public Timestamp getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getMandatory() {
		return mandatory;
	}

	public void setMandatory(Integer mandatory) {
		this.mandatory = mandatory;
	}

	public Long getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Long auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public Long getAttachmentTypeId() {
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Long attachmentTypeId) {
		this.attachmentTypeId = attachmentTypeId;
	}

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public String getAttachmentTypeDesc() {
		return attachmentTypeDesc;
	}

	public void setAttachmentTypeDesc(String attachmentTypeDesc) {
		this.attachmentTypeDesc = attachmentTypeDesc;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public java.util.Date getDateIns() {
		return dateIns;
	}

	public void setDateIns(java.util.Date dateIns) {
		this.dateIns = dateIns;
	}

	@Override
	public String toString() {
		return "MaAttachmentTypes [attachmentTypeId=" + attachmentTypeId + ", auditTypeId=" + auditTypeId
				+ ", auditSubTypeId=" + auditSubTypeId + ", attachmentTypeDesc=" + attachmentTypeDesc
				+ ", activeStatus=" + activeStatus + ", companyId=" + companyId + ", userIns=" + userIns + ", dateIns="
				+ dateIns + ", mandatory=" + mandatory + ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}
	
	

}
