package com.api.central.audit.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="SSP_REVIEW_DATA")
@IdClass(SspReviewDetailCpk.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SspReviewDetail {	
	
	@Id
	@Column(name="AUDIT_SEQ_NO",insertable=false,updatable=false)
	@Range(min=600000,max=999999,message="Company Name cannot be empty")
	private Integer auditSeqNo;
	
	@Id
	@Column(name="COMPANY_ID",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID",insertable=false,updatable=false)
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
	@Column(name="SSP_REPORT_NO")
	private String sspReportNo;
	
	@Column(name="SSP_LEAD_NAME")
	private String sspLeadName;
	
	@Column(name="SSP_REVISION_NO")
	private String sspRevisionNo;
	
	@Column(name="SSP_DMLC_AUDIT_SEQ_NO")
	private Integer sspDmlcAuditSeqNo;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="DUE_DATE")
	private Date dueDate;
	
	@Column(name="VESSEL_COMPANY_ADDRESS")

	private String vesselCompanyAddress;  
	
	@Column(name="VESSEL_COMPANY_NAME")
	
	private String vesselCompanyName;
	
	@Column(name="OFFICIAL_NO")
	
	private Long officialNo;
	
	
	@Column(name="LTRSTATUS")
	private Integer ltrStatus;
	
	@Column(name="REVIEW_SUMMARY")
	private Integer reviewSummary;
	
	@Column(name="DMLC_AUDIT_PLACE")
	private String dmlcAuditPlace;
	
	@Column(name="DMLC_ISSUED_DATE")
	private String dmlcIssuedDate;
	
	
	public String getDmlcAuditPlace() {
		return dmlcAuditPlace;
	}

	public void setDmlcAuditPlace(String dmlcAuditPlace) {
		this.dmlcAuditPlace = dmlcAuditPlace;
	}

	public String getDmlcIssuedDate() {
		return dmlcIssuedDate;
	}

	public void setDmlcIssuedDate(String dmlcIssuedDate) {
		this.dmlcIssuedDate = dmlcIssuedDate;
	}
	
	
	@JsonIgnore
 	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "AUDIT_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO"),
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID")})
	private AuditDetail auditDetail;
	
	public Integer getReviewSummary() {
		return reviewSummary;
	}

	public void setReviewSummary(Integer reviewSummary) {
		this.reviewSummary = reviewSummary;
	}

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}
	
	public String getSspReportNo() {
		return sspReportNo;
	}

	public void setSspReportNo(String sspReportNo) {
		this.sspReportNo = sspReportNo;
	}

	public String getSspLeadName() {
		return sspLeadName;
	}

	public void setSspLeadName(String sspLeadName) {
		this.sspLeadName = sspLeadName;
	}

	public String getSspRevisionNo() {
		return sspRevisionNo;
	}

	public void setSspRevisionNo(String sspRevisionNo) {
		this.sspRevisionNo = sspRevisionNo;
	}

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}
	
	public Integer getSspDmlcAuditSeqNo() {
		return sspDmlcAuditSeqNo;
	}

	public void setSspDmlcAuditSeqNo(Integer sspDmlcAuditSeqNo) {
		this.sspDmlcAuditSeqNo = sspDmlcAuditSeqNo;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
	

	public String getVesselCompanyAddress() {
		return vesselCompanyAddress;
	}

	public void setVesselCompanyAddress(String vesselCompanyAddress) {
		this.vesselCompanyAddress = vesselCompanyAddress;
	}

	public String getVesselCompanyName() {
		return vesselCompanyName;
	}

	public void setVesselCompanyName(String vesselCompanyName) {
		this.vesselCompanyName = vesselCompanyName;
	}

	public Long getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(Long officialNo) {
		this.officialNo = officialNo;
	}
	
	public Integer getLtrStatus() {
		return ltrStatus;
	}

	public void setLtrStatus(Integer ltrStatus) {
		this.ltrStatus = ltrStatus;
	}

	@Override
	public String toString() {
		return "SspReviewDetail [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId=" + auditTypeId
				+ "]";
	}
		
}
