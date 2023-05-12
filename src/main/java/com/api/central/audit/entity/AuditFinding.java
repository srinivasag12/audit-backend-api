package com.api.central.audit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

 
@Entity
@Table(name="AUDIT_FINDINGS")
@IdClass(AuditFindingCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
 public class AuditFinding implements Serializable{
 
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FINDING_SEQ_NO",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Finding No is empty")
	private Integer findingSeqNo;
	
	@Id
	@Column(name="AUDIT_SEQ_NO",insertable=false,updatable=false)
	private Integer auditSeqNo;
 	 		
	@Id
	@Column(name="COMPANY_ID",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID",insertable=false,updatable=false)
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
	@Id
	@Column(name="FINDING_STATUS")
	@Range(min=0,max=1,message="Finding Status is empty")
	private Integer findingStatus;
	
	@JsonIgnore
	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"),
			@JoinColumn(name = "AUDIT_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID")
			})
	
	private AuditDetail auditDetail;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	private Date auditDate;
  	
	@Column(name="AUDIT_CODE")
	@NotEmpty(message="Audit Findings Code cannot be empty")
	private String auditCode;
	
	@Column(name="USER_INS")
	private String userIns;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	@Column(name="SERIAL_NO")
	private String serialNo;
	
	@Column(name="AUDIT_STATUS")
	private Integer auditStatus;
	
	@OneToMany(targetEntity = FindingDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditFinding", orphanRemoval = true)
	@OrderBy(clause = "STATUS_SEQ_NO ASC")
	private List<FindingDetail> findingDetail = new ArrayList<FindingDetail>();

	@Formula("(SELECT A.AUDIT_ELEMENTS FROM MA_AUDIT_CODES A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID  AND A.AUDIT_CODE=AUDIT_CODE AND A.COMPANY_ID = COMPANY_ID)")
	private String auditElements;
	
	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM MA_USERS A WHERE A.SEQUENCE_NUMBER=(SELECT A.USER_ID FROM AUDIT_AUDITOR_DETAILS A WHERE A.COMPANY_ID=COMPANY_ID AND A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUD_LEAD_STATUS = 1)  AND A.COMPANY_ID = COMPANY_ID)")
	private String auditorName;

	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = (SELECT B.AUDIT_SUB_TYPE_ID FROM AUDIT_DETAILS B WHERE B.COMPANY_ID=COMPANY_ID AND B.AUDIT_SEQ_NO = AUDIT_SEQ_NO) AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Formula("(SELECT A.AUDIT_SUB_TYPE_ID FROM AUDIT_DETAILS A WHERE A.COMPANY_ID=COMPANY_ID AND A.AUDIT_SEQ_NO = AUDIT_SEQ_NO)")
	private Integer auditSubTypeId;

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
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

	public List<FindingDetail> getFindingDetail() {
		return findingDetail;
	}

	public void setFindingDetail(List<FindingDetail> findingDetail) {
		this.findingDetail = findingDetail;
	}

	public String getAuditElements() {
		return auditElements;
	}

	public void setAuditElements(String auditElements) {
		this.auditElements = auditElements;
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

	public AuditFinding(Integer findingSeqNo, Integer auditSeqNo, AuditDetail auditDetail) {
		super();
		this.findingSeqNo = findingSeqNo;
		this.auditSeqNo = auditSeqNo;
		this.auditDetail = auditDetail;
	}

	public Integer getFindingStatus() {
		return findingStatus;
	}

	public void setFindingStatus(Integer findingStatus) {
		this.findingStatus = findingStatus;
	}

	public AuditFinding() {
		super();
	}

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	@Override
	public String toString() {
		return "AuditFinding [findingSeqNo=" + findingSeqNo + ", auditSeqNo=" + auditSeqNo + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + ", findingDetail=" + findingDetail + "]";
	}

 }
