/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaVesselCompany.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaVesselCompany.java $
**/
package com.api.central.master.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "MA_VESSEL_COMPANY_YATCH")
@IdClass(MaVesselCompanyCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class MaVesselCompanyYatch implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMPANY_IMO_NO")
	private String companyImoNo;

	@Column(name = "DOC_TYPE_NO")
	private String docTypeNo;

	@Column(name = "DOC_ISSUER")
	private String docIssuer;

	@ShortDateFormat
	@Column(name = "DOC_EXPIRY")
	private Date docExpiry;

	@Column(name = "VESSEL_COMPANY_NAME")
	private String vesselCompanyName;

	@Column(name = "VESSEL_COMPANY_ADDRESS")
	private String vesselCompanyAddress;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;
	
	@Column(name = "COMPANY_STATUS")
	private String companyStatus;
	
	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getDocTypeNo() {
		return docTypeNo;
	}

	public void setDocTypeNo(String docTypeNo) {
		this.docTypeNo = docTypeNo;
	}

	public String getDocIssuer() {
		return docIssuer;
	}

	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}

	public Date getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(Date docExpiry) {
		this.docExpiry = docExpiry;
	}

	public String getVesselCompanyName() {
		return vesselCompanyName;
	}

	public void setVesselCompanyName(String vesselCompanyName) {
		this.vesselCompanyName = vesselCompanyName;
	}

	public String getVesselCompanyAddress() {
		return vesselCompanyAddress;
	}

	public void setVesselCompanyAddress(String vesselCompanyAddress) {
		this.vesselCompanyAddress = vesselCompanyAddress;
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

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}

	
	
}
