package com.api.central.audit.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Range;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="PARTIAL_VESSEL_DETAILS_LOG")
@IdClass(PartialVesselLogCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PartialVesselLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Id
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Id
	@Column(name="VESSEL_ID")
	private Integer vesselId;
	
	@Column(name="DUE_DATE")
	@ShortDateFormat
	private Date dueDate;
	
	@Column(name="STATUS")
	private Integer status;
	
	@Column(name="VESSEL_NAME")
	private String vesselName;
	
	@Transient
	private Integer auditTypeId;
	
	

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PartialVesselLog [companyId=" + companyId + ", userId=" + userId + ", vesselImoNo=" + vesselImoNo
				+ ", vesselId=" + vesselId + ", dueDate=" + dueDate + ", status=" + status + ", vesselName="
				+ vesselName + ", auditTypeId=" + auditTypeId + "]";
	}

	
	
}
