/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditRoles.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditRoles.java $
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
@Table(name = "MA_AUDIT_ROLES")
@IdClass(MaAuditRolesCpk.class)
public class MaAuditRoles {

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;

	@Id
	@Column(name = "AUDIT_ROLE_ID")
	private Long auditRoleId;

	@Column(name = "AUDIT_ROLE_DESC")
	private String auditRoleDesc;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
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

	public Long getAuditRoleId() {
		return auditRoleId;
	}

	public void setAuditRoleId(Long auditRoleId) {
		this.auditRoleId = auditRoleId;
	}

	public String getAuditRoleDesc() {
		return auditRoleDesc;
	}

	public void setAuditRoleDesc(String auditRoleDesc) {
		this.auditRoleDesc = auditRoleDesc;
	}

}
