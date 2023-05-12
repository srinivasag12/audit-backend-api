package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.ShortDateFormat;

@Entity
@Table(name="AUDIT_SEARCH_RESULT")
public class SearchResult implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="AUDIT_SEQ_NO")
	private Integer auditSeqNo;
		
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Formula("(SELECT A.VESSEL_NAME FROM MA_VESSEL A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO)")
	private String vesselName;
	
	@Column(name="AUDIT_SUB_TYPE_ID")
	private Integer auditSubTypeId;
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Column(name="CERTIFICATE_NO")
	private String certificateNo;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	private Date auditDate;
	
	@Column(name="COMPANY_IMO_NO")
	private String companyImoNo;
	
	@Column(name="AUDIT_TYPE_ID")
	private Integer auditTypeId;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditTypeDesc;
	
	@Column(name="AUDIT_STATUS_ID")
	private Integer auditStatusId;
	
	@Formula("(SELECT A.AUDIT_STATUS_DESC FROM  MA_AUDIT_STATUS A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_STATUS_ID = AUDIT_STATUS_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditStatusDesc;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CERT_EXPIRY_DATE")
	private Date certExpireDate;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CERT_ISSUED_DATE")
	private Date certIssueDate;

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}



	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getAuditTypeDesc() {
		return auditTypeDesc;
	}

	public void setAuditTypeDesc(String auditTypeDesc) {
		this.auditTypeDesc = auditTypeDesc;
	}

	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}

	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Integer getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(Integer auditStatusId) {
		this.auditStatusId = auditStatusId;
	}

	public Date getCertIssueDate() {
		return certIssueDate;
	}

	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}

	public Date getCertExpireDate() {
		return certExpireDate;
	}

	public void setCertExpireDate(Date certExpireDate) {
		this.certExpireDate = certExpireDate;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
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

	/*@Override
	public String toString() {
		return "SearchResult [userId=" + userId + ", auditSeqNo=" + auditSeqNo + ", companyId=" + companyId
				+ ", vesselImoNo=" + vesselImoNo + ", vesselName=" + vesselName + ", auditSubTypeId=" + auditSubTypeId
				+ ", audSubTypeDesc=" + audSubTypeDesc + ", certificateNo=" + certificateNo + ", auditDate=" + auditDate
				+ ", companyImoNo=" + companyImoNo + ", auditTypeId=" + auditTypeId + ", auditTypeDesc=" + auditTypeDesc
				+ ", auditStatusId=" + auditStatusId + ", auditStatusDesc=" + auditStatusDesc + ", certExpireDate="
				+ certExpireDate + ", certIssueDate=" + certIssueDate + "]";
	}*/
	

}
