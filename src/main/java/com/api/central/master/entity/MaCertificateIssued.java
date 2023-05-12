/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaCertificateIssued.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaCertificateIssued.java $
**/

package com.api.central.master.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MA_CERTIFICATE_ISSUED")
@IdClass(MaCertificateIssuedCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class MaCertificateIssued {

	@Id
	@Column(name = "CERTIFICATE_ISSUE_ID")
	private Long certificateIssueId;

	@Id
	@Column(name = "AUDIT_TYPE_ID")
	private Long auditTypeId;

	@Column(name = "CERTIFICATE_ISSUE_DESC")
	private String certificateIssueDesc;

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

	public Long getCertificateIssueId() {
		return certificateIssueId;
	}

	public void setCertificateIssueId(Long certificateIssueId) {
		this.certificateIssueId = certificateIssueId;
	}

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public String getCertificateIssueDesc() {
		return certificateIssueDesc;
	}

	public void setCertificateIssueDesc(String certificateIssueDesc) {
		this.certificateIssueDesc = certificateIssueDesc;
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

}
