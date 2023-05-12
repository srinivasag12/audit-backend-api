package com.api.central.audit.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name="AUDIT_FINDING_ATTACHMENTS")
@IdClass(FindingRptAttachCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FindingRptAttach implements Serializable{
 
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FILE_SEQ_NO")
	@Range(min=1,max=100,message="File No is empty")
	private Integer fileSeqNo;
 
	@Id
	@Column(name="FINDING_SEQ_NO",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Finding No is empty")
	private Integer findingSeqNo;
	
	@Id
	@Column(name="ORIG_SEQ_NO",insertable=false,updatable=false)
	@Range(min=600000,max=999999,message="Orignial Audit No is empty")
	private Integer origAuditSeqNo;
 	 
	@Id
	@Column(name="CUR_AUDIT_SEQ_NO",insertable=false,updatable=false)
	@Range(min=600000,max=999999,message="Current Audit No is empty")
	private Integer currentAuditSeq;
	
	@Id
	@Column(name="COMPANY_ID",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID",insertable=false,updatable=false)
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
	@Id
	@Column(name="STATUS_SEQ_NO",insertable=false,updatable=false)
	@Range(min=1,max=10000,message="Status No is empty")
	private Integer statusSeqNo;
	@JsonIgnore
	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"), 
			@JoinColumn(name = "CUR_AUDIT_SEQ_NO", referencedColumnName = "CUR_AUDIT_SEQ_NO"),
			@JoinColumn(name = "ORIG_SEQ_NO", referencedColumnName = "ORIG_SEQ_NO"),
			@JoinColumn(name = "FINDING_SEQ_NO", referencedColumnName = "FINDING_SEQ_NO"),
			@JoinColumn(name = "STATUS_SEQ_NO", referencedColumnName = "STATUS_SEQ_NO"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID")})
	private FindingDetail findingDetail;
	
	@Column(name="FILE_NAME")
	@NotEmpty(message="File Name cannot be empty")
	private String fileName;
	
	
	@Column(name="FLAG")
	private Long ownerFlag;
	
	
	@Column(name="USER_INS")
	private String userIns;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	@Transient
	private byte[] findingFileByte;
	 
	public Integer getFileSeqNo() {
		return fileSeqNo;
	}

	public void setFileSeqNo(Integer fileSeqNo) {
		this.fileSeqNo = fileSeqNo;
	}

	public FindingDetail getFindingDetail() {
		return findingDetail;
	}
  
	public void setFindingDetail(FindingDetail findingDetail) {
		this.findingDetail = findingDetail;
	}

  
	public String getFileName() {
		return fileName;
	}
 
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
 
	public Long getOwnerFlag() {
		return ownerFlag;
	}
 
	public void setOwnerFlag(Long ownerFlag) {
		this.ownerFlag = ownerFlag;
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
 
	public byte[] getFindingFileByte() {
		return findingFileByte;
	}

	public void setFindingFileByte(byte[] findingFileByte) {
		this.findingFileByte = findingFileByte;
	}

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public Integer getOrigAuditSeqNo() {
		return origAuditSeqNo;
	}

	public void setOrigAuditSeqNo(Integer origAuditSeqNo) {
		this.origAuditSeqNo = origAuditSeqNo;
	}

	public Integer getCurrentAuditSeq() {
		return currentAuditSeq;
	}

	public void setCurrentAuditSeq(Integer currentAuditSeq) {
		this.currentAuditSeq = currentAuditSeq;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getStatusSeqNo() {
		return statusSeqNo;
	}

	public void setStatusSeqNo(Integer statusSeqNo) {
		this.statusSeqNo = statusSeqNo;
	}

	
	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public FindingRptAttach() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public String toString() {
		return "FindingRptAttach [fileSeqNo=" + fileSeqNo + ", findingSeqNo=" + findingSeqNo + ", origAuditSeqNo="
				+ origAuditSeqNo + ", currentAuditSeq=" + currentAuditSeq + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + ", statusSeqNo=" + statusSeqNo + ", fileName=" + fileName
				+ ", ownerFlag=" + ownerFlag + "]";
	}*/

	
 
}
