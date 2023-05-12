/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name IspsBean.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/audit/entity/IspsBean.java $
**/

package com.api.central.audit.entity;

import java.io.Serializable;

public class IspsBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private String audSubTypeDesc;

	private String auditDate;

	private String auditPlace;

	private String dnc;

	private String nc;

	private String ob;

	private String auditorName;

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditPlace() {
		return auditPlace;
	}

	public void setAuditPlace(String auditPlace) {
		this.auditPlace = auditPlace;
	}

	public String getDnc() {
		return dnc;
	}

	public void setDnc(String dnc) {
		this.dnc = dnc;
	}

	public String getNc() {
		return nc;
	}

	public void setNc(String nc) {
		this.nc = nc;
	}

	public String getOb() {
		return ob;
	}

	public void setOb(String ob) {
		this.ob = ob;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	
	
	


}
