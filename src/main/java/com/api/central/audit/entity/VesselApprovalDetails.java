package com.api.central.audit.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.api.annotations.LongDateFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "VESSEL_APPROVAL_DETAILS")
@IdClass(VesselApprovalDetailsCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class VesselApprovalDetails {
	
	@Id
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Column(name="VESSEL_NAME")
	private String vesselName;
	
	@Column(name="COMPANY_IMO_NO")
	private String companyImoNo;
	
	@Column(name="DOC_TYPE_NUMBER")
	private String docTypeNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	@Column(name="AUDIT_DUE_DATE")
	private java.util.Date auditDueDate;
	
	@Column(name="LEAD_SEQUENCE_NUMBER")
	private String leadSequenceNumber;
	
	@Column(name="TC_APPROVAL_STATUS")
	private Integer tcApprovalStatus;
	
	@Column(name="USER_INS")
	private String userIns;
	
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	
	@Id
	@Column(name="COMPANY_ID")
	private Integer companyId;

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public java.util.Date getAuditDueDate() {
		return auditDueDate;
	}

	public void setAuditDueDate(java.util.Date auditDueDate) {
		this.auditDueDate = auditDueDate;
	}

	

	

	public String getLeadSequenceNumber() {
		return leadSequenceNumber;
	}

	public void setLeadSequenceNumber(String leadSequenceNumber) {
		this.leadSequenceNumber = leadSequenceNumber;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getTcApprovalStatus() {
		return tcApprovalStatus;
	}

	public void setTcApprovalStatus(Integer tcApprovalStatus) {
		this.tcApprovalStatus = tcApprovalStatus;
	}

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public Timestamp getDateIns() {
		return dateIns;
	}

	public void setDateIns(Timestamp dateIns) {
		this.dateIns = dateIns;
	}
	
	

}
