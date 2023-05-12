package com.api.central.master.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.api.annotations.LongDateFormat;

@Entity
@Table(name = "MA_IHM_EXEMPTION_REASON")
@IdClass(MaIhmExemptionReasonCpk.class)

public class MaIhmExemptionReason {
	
	@Id
	@Column(name = "REASON_ID")
	private Integer reasonId;
	
	@Column(name = "EC_GRANTED_REASON")
	private String ecGrantedReason;
	
	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;
	
	@LongDateFormat
	@Column(name="UPDATED_ON")
	private Timestamp updatedOn;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Transient
	private Boolean reasonCount;
	
	
	public Boolean getReasonCount() {
		return reasonCount;
	}

	public void setReasonCount(Boolean reasonCount) {
		this.reasonCount = reasonCount;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getEcGrantedReason() {
		return ecGrantedReason;
	}

	public void setEcGrantedReason(String ecGrantedReason) {
		this.ecGrantedReason = ecGrantedReason;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	
}
