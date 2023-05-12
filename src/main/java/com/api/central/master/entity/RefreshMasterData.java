/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name RefreshMasterData.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure CentralAuditApi\src\main\java\com\api\central\master\entity\RefreshMasterData.java $
**/


package com.api.central.master.entity;

import java.io.Serializable;

public class RefreshMasterData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer vessel;

	private Integer auditCodes;

	private Integer vesselcompany;

	private Integer attachmentTypes;

	private Integer auditSearchSource;

	private Integer	auditStatus;

	private Integer auditSubtype;

	private Integer auditSummary;

	private Integer auditType;

	private Integer certificateIssued;

	private Integer company;

	private Integer FindingsCategory;

	private Integer findingsStatus;

	private Integer roles;

	private Integer users;

	private Integer vesselType;

	private Integer auditRoles;

	private Integer configDetails;

	private Integer port;

	public Integer getVessel() {
		return vessel;
	}

	public void setVessel(Integer vessel) {
		this.vessel = vessel;
	}

	public Integer getAuditCodes() {
		return auditCodes;
	}

	public void setAuditCodes(Integer auditCodes) {
		this.auditCodes = auditCodes;
	}

	public Integer getVesselcompany() {
		return vesselcompany;
	}

	public void setVesselcompany(Integer vesselcompany) {
		this.vesselcompany = vesselcompany;
	}

	public Integer getAttachmentTypes() {
		return attachmentTypes;
	}

	public void setAttachmentTypes(Integer attachmentTypes) {
		this.attachmentTypes = attachmentTypes;
	}

	public Integer getAuditSearchSource() {
		return auditSearchSource;
	}

	public void setAuditSearchSource(Integer auditSearchSource) {
		this.auditSearchSource = auditSearchSource;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getAuditSubtype() {
		return auditSubtype;
	}

	public void setAuditSubtype(Integer auditSubtype) {
		this.auditSubtype = auditSubtype;
	}

	public Integer getAuditSummary() {
		return auditSummary;
	}

	public void setAuditSummary(Integer auditSummary) {
		this.auditSummary = auditSummary;
	}

	public Integer getAuditType() {
		return auditType;
	}

	public void setAuditType(Integer auditType) {
		this.auditType = auditType;
	}

	public Integer getCertificateIssued() {
		return certificateIssued;
	}

	public void setCertificateIssued(Integer certificateIssued) {
		this.certificateIssued = certificateIssued;
	}

	public Integer getCompany() {
		return company;
	}

	public void setCompany(Integer company) {
		this.company = company;
	}

	

	public Integer getFindingsCategory() {
		return FindingsCategory;
	}

	public void setFindingsCategory(Integer findingsCategory) {
		FindingsCategory = findingsCategory;
	}

	public Integer getFindingsStatus() {
		return findingsStatus;
	}

	public void setFindingsStatus(Integer findingsStatus) {
		this.findingsStatus = findingsStatus;
	}

	public Integer getRoles() {
		return roles;
	}

	public void setRoles(Integer roles) {
		this.roles = roles;
	}

	public Integer getUsers() {
		return users;
	}

	public void setUsers(Integer users) {
		this.users = users;
	}

	public Integer getVesselType() {
		return vesselType;
	}

	public void setVesselType(Integer vesselType) {
		this.vesselType = vesselType;
	}

	public Integer getAuditRoles() {
		return auditRoles;
	}

	public void setAuditRoles(Integer auditRoles) {
		this.auditRoles = auditRoles;
	}

	public Integer getConfigDetails() {
		return configDetails;
	}

	public void setConfigDetails(Integer configDetails) {
		this.configDetails = configDetails;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	
}
