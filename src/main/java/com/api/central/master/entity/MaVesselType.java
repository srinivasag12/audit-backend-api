/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaVesselType.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure CentralAuditApi\src\main\java\com\api\central\master\entity\MaVesselType.java $
**/
package com.api.central.master.entity;

import java.io.Serializable;
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
@Table(name = "MA_VESSEL_TYPE")
@IdClass(MaVesselTypeCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class MaVesselType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VESSEL_TYPE_ID")
	private Long vesselTypeId;

	@Column(name = "VESSEL_TYPE_NAME")
	private String vesselTypeName;

	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;

	public Long getVesselTypeId() {
		return vesselTypeId;
	}

	public void setVesselTypeId(Long vesselTypeId) {
		this.vesselTypeId = vesselTypeId;
	}

	public String getVesselTypeName() {
		return vesselTypeName;
	}

	public void setVesselTypeName(String vesselTypeName) {
		this.vesselTypeName = vesselTypeName;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
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
