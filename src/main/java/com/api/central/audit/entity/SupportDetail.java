package com.api.central.audit.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*@Entity
@Table(name = "SUPPORT_DETAILS")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })*/
public class SupportDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUDIT_SEQ_NO")
	private Integer auditSeqNo;
	
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Column(name="VESSEL_NAME")
	private String vesselName;
	
	@Column(name="AUDIT_TYPE_ID")
	private Integer auditTypeId;
	
	@Column(name="AUDIT_SUB_TYPE_ID")
	private Integer auditSubTypeId;
	
	@Column(name = "FILE_PATH")
	private String filePath;
	
	@Column(name = "FILE_Data")
	private byte[] fileData;

	@Column(name = "COMPANY_ID")
	@Range(min = 1, max = 100, message = "Company Name cannot be empty")
	private Long companyId;

	@Column(name = "USER_INS")
	private String userIns;

	@LongDateFormat
	@UpdateTimestamp
	@Column(name = "DATE_INS")
	private Timestamp dateIns;

	@Column(name = "LEAD_ID")
	private String leadId;
	
	@Column(name = "OFFICIAL_NO")
	private Long officialNo;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audTypeDesc;
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Formula("(SELECT A.AUDIT_STATUS_DESC FROM  MA_AUDIT_STATUS A  WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_STATUS_ID = (SELECT AD.AUDIT_STATUS_ID FROM AUDIT_DETAILS AD WHERE AD.COMPANY_ID = COMPANY_ID AND AD.AUDIT_SEQ_NO = AUDIT_SEQ_NO ) AND A.COMPANY_ID = COMPANY_ID)")
	private String auditStatusDesc;
	
	@Formula("(SELECT A.CERTIFICATE_ISSUE_DESC FROM  MA_CERTIFICATE_ISSUED A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.CERTIFICATE_ISSUE_ID = (SELECT AD.CERTIFICATE_ISSUE_ID FROM AUDIT_DETAILS AD WHERE AD.COMPANY_ID = COMPANY_ID AND AD.AUDIT_SEQ_NO = AUDIT_SEQ_NO )  AND A.COMPANY_ID = COMPANY_ID)")
	private String audCertIssueDesc;
	
	@Formula("(SELECT A.CERTIFICATE_NO FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String certificateNo;
	
	@ShortDateFormat
	@Formula("(SELECT A.AUDIT_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Date auditDate;
	
	@Formula("(SELECT A.COMPANY_IMO_NO FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	@NotEmpty(message="Company IMO cannot be empty")
	private String companyImoNo;

	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM  MA_USERS A WHERE A.USER_ID=LEAD_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String leadName;
	
	
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

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public String getLeadId() {
		return leadId;
	}

	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}

	public Long getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(Long officialNo) {
		this.officialNo = officialNo;
	}

	public String getAudTypeDesc() {
		return audTypeDesc;
	}

	public void setAudTypeDesc(String audTypeDesc) {
		this.audTypeDesc = audTypeDesc;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}

	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}
	
	public String getAudCertIssueDesc() {
		return audCertIssueDesc;
	}

	public void setAudCertIssueDesc(String audCertIssueDesc) {
		this.audCertIssueDesc = audCertIssueDesc;
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

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	@Override
	public String toString() {
		return "SupportDetail [auditSeqNo=" + auditSeqNo + ", vesselImoNo=" + vesselImoNo + ", vesselName=" + vesselName
				+ ", auditTypeId=" + auditTypeId + ", auditSubTypeId=" + auditSubTypeId + ", filePath=" + filePath
				+ ", companyId=" + companyId + ", userIns=" + userIns + ", dateIns=" + dateIns + ", leadId=" + leadId
				+ ", officialNo=" + officialNo + ", audTypeDesc=" + audTypeDesc + ", audSubTypeDesc=" + audSubTypeDesc
				+ ", auditStatusDesc=" + auditStatusDesc + ", audCertIssueDesc=" + audCertIssueDesc + ", certificateNo="
				+ certificateNo + ", auditDate=" + auditDate + ", companyImoNo=" + companyImoNo + ", leadName="
				+ leadName + "]";
	}

}
