/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name sspBean.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/audit/entity/sspBean.java $
**/

package com.api.central.audit.entity;

public class sspBean {
	
	private String audSubTypeDescs;
	
	private String auditDates;
	
	private String auditPlaces;
	
	private String auditorNames;
	
	private String certificateNos;
	
	private String certIssueDates;

	public String getAudSubTypeDescs() {
		return audSubTypeDescs;
	}

	public void setAudSubTypeDescs(String audSubTypeDescs) {
		this.audSubTypeDescs = audSubTypeDescs;
	}

	public String getAuditDates() {
		return auditDates;
	}

	public void setAuditDates(String auditDates) {
		this.auditDates = auditDates;
	}

	public String getAuditPlaces() {
		return auditPlaces;
	}

	public void setAuditPlaces(String auditPlaces) {
		this.auditPlaces = auditPlaces;
	}

	public String getAuditorNames() {
		return auditorNames;
	}

	public void setAuditorNames(String auditorNames) {
		this.auditorNames = auditorNames;
	}

	public String getCertificateNos() {
		return certificateNos;
	}

	public void setCertificateNos(String certificateNos) {
		this.certificateNos = certificateNos;
	}

	public String getCertIssueDates() {
		return certIssueDates;
	}

	public void setCertIssueDates(String certIssueDates) {
		this.certIssueDates = certIssueDates;
	}
	
	

}
