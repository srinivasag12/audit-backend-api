package com.api.central.audit.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

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
import org.springframework.format.annotation.DateTimeFormat;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

 
@Entity
@Table(name="AUDIT_REPORT_ATTACHMENTS")
@IdClass(AuditRptAttachCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuditRptAttach implements Serializable{
 
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQ_NO")
	private Integer seqNo;
	
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
	@JsonIgnore
 	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "AUDIT_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO"),
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID")})
	private AuditDetail auditDetail;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="ATTACHMENT_TYPE_ID")
	@Range(min=1000,max=10000,message="Attachment Type cannot be empty")
 	private Integer attachmentTypeId;
		
	@Column(name="USER_INS")
	private String userIns;
	
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;

	@Column(name="TYPE_NAME")
	private String otherType;
	
	@Formula("(SELECT A.ATTACHMENT_TYPE_DESC FROM MA_ATTACHMENT_TYPES A WHERE  A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.ATTACHMENT_TYPE_ID = ATTACHMENT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID and rownum=1)")
	private String attchmentTypeDesc;
		 
	
	@Transient
	private byte[] fileByte;
	
	
	@Column(name="ATTACHMENT_TYPE_DESC")
	private String attchTypeDescAudit;
	
	@Column(name = "MANDATORY")
	private Integer mandatory;
	
	public Integer getMandatory() {
		return mandatory;
	}

	public void setMandatory(Integer mandatory) {
		this.mandatory = mandatory;
	}

	
	
		 
	

	public String getAttchTypeDescAudit() {
		return attchTypeDescAudit;
	}

	public void setAttchTypeDescAudit(String attchTypeDescAudit) {
		this.attchTypeDescAudit = attchTypeDescAudit;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

 	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

 	public String getFileName() {
		return fileName;
	}

 	public void setFileName(String fileName) {
		this.fileName = fileName;
	} 	 

 	public Integer getAttachmentTypeId() {
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Integer attachmentTypeId) {
		this.attachmentTypeId = attachmentTypeId;
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

 	public String getAttchmentTypeDesc() {
		return attchmentTypeDesc;
	}
 
	public void setAttchmentTypeDesc(String attchmentTypeDesc) {
		this.attchmentTypeDesc = attchmentTypeDesc;
	}

 	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

 	public byte[] getFileByte() {
		return fileByte;
	}

	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
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

	public AuditRptAttach() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	@Override
	public String toString() {
		return "AuditRptAttach [seqNo=" + seqNo + ", auditSeqNo=" + auditSeqNo + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + ", auditDetail=" + auditDetail + ", fileName=" + fileName
				+ ", comments=" + comments + ", attachmentTypeId=" + attachmentTypeId + ", userIns=" + userIns
				+ ", dateIns=" + dateIns + ", otherType=" + otherType + ", attchmentTypeDesc=" + attchmentTypeDesc
				+ ", fileByte=" +""  + ", attchTypeDescAudit=" + attchTypeDescAudit
				+ ", mandatory=" + mandatory + "]";
	}

	
	
    
}
