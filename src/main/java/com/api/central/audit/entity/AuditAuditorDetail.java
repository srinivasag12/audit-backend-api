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

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="AUDIT_AUDITOR_DETAILS")
@IdClass(AuditAuditorDetailCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuditAuditorDetail implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID")
 	@NotEmpty(message="Auditor Name cannot be empty")
	private String userId;
	
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
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID") })
	private AuditDetail auditDetail;
	
	@Column(name="AUDIT_ROLE_ID")
	@Range(message="Auditor Type cannot be empty")
	private Integer auditRoleID;
	
	@Column(name="AUD_SIGNATURE")
	private byte[] audSignature;


	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="AUD_SIGNATURE_DATE")
	private Date audSignatureDate;
	
	@Column(name="AUD_LEAD_STATUS")
	private Integer audLeadStatus;
		
	@Column(name="USER_INS")
	private String userIns;
	
	@Column(name="DELEGATE_SIGN")
	private Integer delegateSign;
	
	@Column(name="SIGNER_NAME")
	private String signerName;
	
	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;

 	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM MA_USERS A WHERE A.USER_ID=USER_ID  AND A.COMPANY_ID = COMPANY_ID)")
	private String auditorName;
 	
 	@Formula("(SELECT A.AUDIT_ROLE_DESC FROM MA_AUDIT_ROLES A WHERE A.AUDIT_ROLE_ID=AUDIT_ROLE_ID  AND A.COMPANY_ID = COMPANY_ID)")
	private String auditRoleDesc;
 
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	public Integer getAuditRoleID() {
		return auditRoleID;
	}

	public void setAuditRoleID(Integer auditRoleID) {
		this.auditRoleID = auditRoleID;
	}

	public byte[] getAudSignature() {
		return audSignature;
	}

	public void setAudSignature(byte[] audSignature) {
		this.audSignature = audSignature;
	}

	public Integer getAudLeadStatus() {
		return audLeadStatus;
	}

	public Date getAudSignatureDate() {
		return audSignatureDate;
	}

	public void setAudSignatureDate(Date audSignatureDate) {
		this.audSignatureDate = audSignatureDate;
	}

	public void setAudLeadStatus(Integer audLeadStatus) {
		this.audLeadStatus = audLeadStatus;
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

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAuditRoleDesc() {
		return auditRoleDesc;
	}

	public void setAuditRoleDesc(String auditRoleDesc) {
		this.auditRoleDesc = auditRoleDesc;
	}

	public Integer getDelegateSign() {
		return delegateSign;
	}

	public void setDelegateSign(Integer delegateSign) {
		this.delegateSign = delegateSign;
	}

	public AuditAuditorDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "AuditAuditorDetail [userId=" + userId + ", auditSeqNo=" + auditSeqNo + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + ", auditDetail=" + auditDetail + ", auditRoleID=" + auditRoleID
				+ ", audSignature=" + Arrays.toString(audSignature) + ", audSignatureDate=" + audSignatureDate
				+ ", audLeadStatus=" + audLeadStatus + ", userIns=" + userIns + ", delegateSign=" + delegateSign
				+ ", dateIns=" + dateIns + ", auditorName=" + auditorName + ", auditRoleDesc=" + auditRoleDesc + "]";
	}

	

	
  	
}
