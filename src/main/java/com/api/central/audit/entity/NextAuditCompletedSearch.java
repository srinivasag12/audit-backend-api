package com.api.central.audit.entity;

import java.sql.Date;

public class NextAuditCompletedSearch {
	
	private Integer vesselImoNo;
	
	private Integer auditTypeId;
	
	private java.util.Date auditDate;
	
	private Integer auditStatusId;

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public java.util.Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(java.util.Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(Integer auditStatusId) {
		this.auditStatusId = auditStatusId;
	}

	/*@Override
	public String toString() {
		return "NextAuditCompletedSearch [vesselImoNo=" + vesselImoNo + ", auditTypeId=" + auditTypeId + ", auditDate="
				+ auditDate + ", auditStatusId=" + auditStatusId + "]";
	}*/
}
