package com.api.central.audit.entity;


import java.sql.Date;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="AUDIT_DETAILS")
@IdClass(AuditDetailCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VesselStatement {


	
	
	@Id
	@Column(name="AUDIT_SEQ_NO")
	@Range(min=600000,max=999999,message="Company Name cannot be empty")
	private Integer auditSeqNo;
	
	@Id
	@Column(name="COMPANY_ID")
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
 	
	@Range(min=7,max=999999999,message="Vessel Name cannot be empty")
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
 	@Column(name="AUDIT_SUB_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Sub Type cannot be empty")
 	private Integer auditSubTypeId;
	
	@Column(name="CERTIFICATE_NO")
	@NotEmpty(message="Certificate No cannot be empty")
	private String certificateNo;

	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	@NotNull
	private Date auditDate;
	
	@ShortDateFormat
	@Column(name="CLOSE_MEETING_DATE")
	private Date closeMeetingDate;
	
	@Column(name="AUDIT_PLACE")
	private String auditPlace;
	
	@Column(name="COMPANY_IMO_NO")
	@NotEmpty(message="Company IMO cannot be empty")
	private String companyImoNo;
	
	@Column(name="COMPANY_DOC")
	private String companyDoc;
	
	@Column(name="DOC_TYPE_NUMBER")
	private String docTypeNumber;
		
	@Column(name="AUDIT_STATUS_ID")	
	@Range(min=0,max=1000000,message="Audit Status cannot be empty")
	private Integer auditStatusId;
	
	@Column(name="AUDIT_SUMMARY_ID")	
	@Range(min=0,max=1000000,message="Audit Summary cannot be empty")
	private Integer auditSummaryId;
	
	@Column(name="scope")
	private Integer scope;
				   
	@Column(name="CERTIFICATE_ISSUE_ID")
	@Range(min=1000,max=10000,message="Certificate Issued cannot be empty")
 	private Integer certIssueId;
	

	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CERT_EXPIRY_DATE")
	 private Date certExpireDate;
	

	@ShortDateFormat
	@Column(name="CERT_ISSUED_DATE")
	private Date certIssueDate;
	
	
	
	   
	@ShortDateFormat
  	@Formula("(SELECT B.EXTENDED_ISSUE_DATE FROM CERTIFICATE_DETAIL B  WHERE B.SEQ_NO = ( SELECT MAX(A.SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND A.COMPANY_ID = COMPANY_ID) AND B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.COMPANY_ID = COMPANY_ID)")
	private Date  ExtendedIssueDate;
	
  	@ShortDateFormat
	@Formula("(SELECT B.EXTENDED_EXPIRY_DATE FROM CERTIFICATE_DETAIL B  WHERE B.SEQ_NO = ( SELECT MAX(A.SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND A.COMPANY_ID = COMPANY_ID) AND B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.COMPANY_ID = COMPANY_ID)")
	private Date  ExtendedExpiryDate;
  	
  	
  	
	
  	
	public Date getExtendedIssueDate() {
		return ExtendedIssueDate;
	}

	public void setExtendedIssueDate(Date extendedIssueDate) {
		ExtendedIssueDate = extendedIssueDate;
	}

	public Date getExtendedExpiryDate() {
		return ExtendedExpiryDate;
	}

	public void setExtendedExpiryDate(Date extendedExpiryDate) {
		ExtendedExpiryDate = extendedExpiryDate;
	}
	
	
	
	
	
	

	@Formula("(SELECT A.VESSEL_COMPANY_NAME FROM  MA_VESSEL_COMPANY A  WHERE A.COMPANY_IMO_NO=COMPANY_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String companyName;
	
	
	

	@Formula("(SELECT A.OFFICIAL_NO FROM  MA_VESSEL A  WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String officialNo;
	
	@Formula("(SELECT A.VESSEL_NAME FROM  MA_VESSEL A  WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String vesselName;
	
	@Formula("(SELECT A.DOC_ISSUER FROM DOC_NUMBER_CHANGE A WHERE A.SEQ_NO = ( SELECT MAX(B.SEQ_NO)  FROM DOC_NUMBER_CHANGE B WHERE B.COMPANY_IMO_NO = COMPANY_IMO_NO AND B.DOC_TYPE_NO = COMPANY_DOC AND B.COMPANY_ID=COMPANY_ID))")
	private String docIssuer;
	
	@Formula("(SELECT A.DOC_EXPIRY FROM DOC_NUMBER_CHANGE A WHERE A.SEQ_NO = ( SELECT MAX(B.SEQ_NO)  FROM DOC_NUMBER_CHANGE B WHERE B.COMPANY_IMO_NO = COMPANY_IMO_NO AND B.DOC_TYPE_NO = COMPANY_DOC AND B.COMPANY_ID=COMPANY_ID))")
	private String docExpiry;
	
	/*@Formula("(SELECT A.PUBLISH_STATUS FROM  CERTIFICATE_DETAIL A  WHERE A.SEQ_NO = (SELECT MAX(A.SEQ_NO) FROM CERTIFICATE_DETAIL B WHERE B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID))")
	private Integer publishStatus;*/
	
	
	@Formula("(SELECT B.PUBLISH_STATUS FROM CERTIFICATE_DETAIL B  WHERE B.SEQ_NO = ( SELECT MAX(A.SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND A.COMPANY_ID = COMPANY_ID) AND B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.COMPANY_ID = COMPANY_ID)")
	private Integer publishStatus;
	

	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audTypeDesc;
	
	
	
	@Formula("(SELECT A.AUDIT_STATUS_DESC FROM  MA_AUDIT_STATUS A  WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_STATUS_ID = AUDIT_STATUS_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditStatusDesc;
	
	@Formula("(SELECT B.CERTIFICATE_NO FROM CERTIFICATE_DETAIL B  WHERE B.SEQ_NO = ( SELECT MAX(A.SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND A.COMPANY_ID = COMPANY_ID) AND B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.COMPANY_ID = COMPANY_ID)")
	private String  certificateNo2;
	
	
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(targetEntity = AuditFinding.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false)
	@OrderBy(clause = "FINDING_SEQ_NO ASC")
	private List<AuditFinding> auditFinding = new ArrayList<AuditFinding>();
 	
  	@Fetch(FetchMode.SUBSELECT)
  	@OneToMany(targetEntity = AuditAuditorDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = true)
  	@OrderBy(clause = "AUD_LEAD_STATUS DESC")
  	private List<AuditAuditorDetail> auditAuditorDetail = new ArrayList<AuditAuditorDetail>();
  	
  	
  	
	
	
	
	//veesel info starts
	
	@Column(name = "VESSEL_NAME")
	private String vesselNameAud;
		
	@Column(name = "VESSEL_TYPE")
	private String vesselTypeAud;
	
	
	@Column(name = "OFFICIAL_NO")
	private String officialNoAud;
	
	@Column(name = "DOC_TYPE_NO")
	private String docTypeNoAud;
	
	@Column(name = "DOC_ISSUER")
	private String docIssuerAud;
	
	
	@Column(name="DOC_EXPIRY")
	@ShortDateFormat
	private Date docExpiryAud;
	
	@Column(name = "VESSEL_ADDRESS")
	private String companyAddressAud;
	
	@Column(name="GRT")
	private Integer grt;
	
	
	@Column(name="DATE_OF_REGISTRY")
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	private Date dateOfRegistry;
	
	
	//veesel info ends
	
	
	

  	
  	
  	
  	
	public String getCertificateNo2() {
		return certificateNo2;
	}

	public String getVesselNameAud() {
		return vesselNameAud;
	}

	public void setVesselNameAud(String vesselNameAud) {
		this.vesselNameAud = vesselNameAud;
	}

	public String getVesselTypeAud() {
		return vesselTypeAud;
	}

	public void setVesselTypeAud(String vesselTypeAud) {
		this.vesselTypeAud = vesselTypeAud;
	}

	public String getOfficialNoAud() {
		return officialNoAud;
	}

	public void setOfficialNoAud(String officialNoAud) {
		this.officialNoAud = officialNoAud;
	}

	public String getDocTypeNoAud() {
		return docTypeNoAud;
	}

	public void setDocTypeNoAud(String docTypeNoAud) {
		this.docTypeNoAud = docTypeNoAud;
	}

	public String getDocIssuerAud() {
		return docIssuerAud;
	}

	public void setDocIssuerAud(String docIssuerAud) {
		this.docIssuerAud = docIssuerAud;
	}

	public Date getDocExpiryAud() {
		return docExpiryAud;
	}

	public void setDocExpiryAud(Date docExpiryAud) {
		this.docExpiryAud = docExpiryAud;
	}

	public String getCompanyAddressAud() {
		return companyAddressAud;
	}

	public void setCompanyAddressAud(String companyAddressAud) {
		this.companyAddressAud = companyAddressAud;
	}

	public Integer getGrt() {
		return grt;
	}

	public void setGrt(Integer grt) {
		this.grt = grt;
	}

	public Date getDateOfRegistry() {
		return dateOfRegistry;
	}

	public void setDateOfRegistry(Date dateOfRegistry) {
		this.dateOfRegistry = dateOfRegistry;
	}

	public void setCertificateNo2(String certificateNo2) {
		this.certificateNo2 = certificateNo2;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
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

	public String getAuditPlace() {
		return auditPlace;
	}

	public void setAuditPlace(String auditPlace) {
		this.auditPlace = auditPlace;
	}

	public Integer getAuditSummaryId() {
		return auditSummaryId;
	}

	public void setAuditSummaryId(Integer auditSummaryId) {
		this.auditSummaryId = auditSummaryId;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getCompanyDoc() {
		return companyDoc;
	}

	public void setCompanyDoc(String companyDoc) {
		this.companyDoc = companyDoc;
	}

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public Integer getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(Integer auditStatusId) {
		this.auditStatusId = auditStatusId;
	}

	public Integer getCertIssueId() {
		return certIssueId;
	}

	public void setCertIssueId(Integer certIssueId) {
		this.certIssueId = certIssueId;
	}

	public Date getCertExpireDate() {
		return certExpireDate;
	}

	public void setCertExpireDate(Date certExpireDate) {
		this.certExpireDate = certExpireDate;
	}

	public Date getCertIssueDate() {
		return certIssueDate;
	}

	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(String officialNo) {
		this.officialNo = officialNo;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getDocIssuer() {
		return docIssuer;
	}

	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}

	public String getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(String docExpiry) {
		this.docExpiry = docExpiry;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getAudTypeDesc() {
		return audTypeDesc;
	}

	public void setAudTypeDesc(String audTypeDesc) {
		this.audTypeDesc = audTypeDesc;
	}

	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}

	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}

	public List<AuditFinding> getAuditFinding() {
		return auditFinding;
	}

	public void setAuditFinding(List<AuditFinding> auditFinding) {
		this.auditFinding = auditFinding;
	}

	public List<AuditAuditorDetail> getAuditAuditorDetail() {
		return auditAuditorDetail;
	}

	public void setAuditAuditorDetail(List<AuditAuditorDetail> auditAuditorDetail) {
		this.auditAuditorDetail = auditAuditorDetail;
	}

	public Date getCloseMeetingDate() {
		return closeMeetingDate;
	}

	public void setCloseMeetingDate(Date closeMeetingDate) {
		this.closeMeetingDate = closeMeetingDate;
	}

	@Override
	public String toString() {
		return "VesselStatement [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId=" + auditTypeId
				+ ", vesselImoNo=" + vesselImoNo + ", auditSubTypeId=" + auditSubTypeId + ", certificateNo="
				+ certificateNo + ", auditDate=" + auditDate + ", closeMeetingDate=" + closeMeetingDate
				+ ", auditPlace=" + auditPlace + ", companyImoNo=" + companyImoNo + ", companyDoc=" + companyDoc
				+ ", docTypeNumber=" + docTypeNumber + ", auditStatusId=" + auditStatusId + ", auditSummaryId="
				+ auditSummaryId + ", scope=" + scope + ", certIssueId=" + certIssueId + ", certExpireDate="
				+ certExpireDate + ", certIssueDate=" + certIssueDate + ", ExtendedIssueDate=" + ExtendedIssueDate
				+ ", ExtendedExpiryDate=" + ExtendedExpiryDate + ", companyName=" + companyName + ", officialNo="
				+ officialNo + ", vesselName=" + vesselName + ", docIssuer=" + docIssuer + ", docExpiry=" + docExpiry
				+ ", publishStatus=" + publishStatus + ", audSubTypeDesc=" + audSubTypeDesc + ", audTypeDesc="
				+ audTypeDesc + ", auditStatusDesc=" + auditStatusDesc + ", certificateNo2=" + certificateNo2
				+ ", auditFinding=" + auditFinding + ", auditAuditorDetail=" + auditAuditorDetail + ", vesselNameAud="
				+ vesselNameAud + ", vesselTypeAud=" + vesselTypeAud + ", officialNoAud=" + officialNoAud
				+ ", docTypeNoAud=" + docTypeNoAud + ", docIssuerAud=" + docIssuerAud + ", docExpiryAud=" + docExpiryAud
				+ ", companyAddressAud=" + companyAddressAud + ", grt=" + grt + ", dateOfRegistry=" + dateOfRegistry
				+ "]";
	}
	
	

}
