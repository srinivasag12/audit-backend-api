/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditSearchSource.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditSearchSource.java $
**/

package com.api.central.master.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MA_AUDIT_SEARCH_SOURCE")
@IdClass(MaAuditSearchSourceCpk.class)
public class MaAuditSearchSource {
	@Id
	@Column(name = "AUDIT_SOURCE_ID")
	private Long auditSourceId;

	@Column(name = "AUDIT_SOURCE_DESC")
	private String auditSourceDesc;

	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;

	public Long getAuditSourceId() {
		return auditSourceId;
	}

	public void setAuditSourceId(Long auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public String getAuditSourceDesc() {
		return auditSourceDesc;
	}

	public void setAuditSourceDesc(String auditSourceDesc) {
		this.auditSourceDesc = auditSourceDesc;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

}
