package com.api.central.audit.entity;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.api.central.util.AppConstant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="AUDIT_DETAILS")
@IdClass(AuditDetailCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuditDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
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
	
 	
	
	
 	@Column(name="AUDIT_SUB_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Sub Type cannot be empty")
 	private Integer auditSubTypeId;
	
	@Column(name="CERTIFICATE_NO")
	private String certificateNo;
	
	@Column(name="LETTER_NO")
	private String letterNo;

	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone=JsonFormat.DEFAULT_TIMEZONE)
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	
	private Date auditDate;
	
	@Column(name="AUDIT_PLACE")
	private String auditPlace;
	
	@Transient
	private String qid;
	
	
		
	@Column(name="AUDIT_STATUS_ID")	
	@Range(min=0,max=1000000,message="Audit Status cannot be empty")
	private Integer auditStatusId;
	
	@Column(name="AUDIT_REPORT_NO")
	private String auditReportNo;
				   
	@Column(name="CERTIFICATE_ISSUE_ID")
	@Range(min=1000,max=10000,message="Certificate Issued cannot be empty")
 	private Integer certIssueId;
	
	@Column(name="INTERNAL_AUDIT_DATE")
	private String interalAuditDate;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name="CERT_EXPIRY_DATE")
	 private Date certExpireDate;
	

	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name="CERT_ISSUED_DATE")
	private Date certIssueDate;
	
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@LongDateFormat
    @Column(name="OPEN_MEETING_DATE")
	private Timestamp openMeetingDate;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@LongDateFormat
	@Column(name="CLOSE_MEETING_DATE")
	private Timestamp closeMeetingDate;
	
	@Column(name="AUDIT_SUMMARY_ID")
 	private Integer auditSummaryId;
	
	@Column(name="LOCK_STATUS")
	private Integer lockStatus;
	
	@Lob
	@Column(name="NARRATIVE_SUMMARY")
	private byte[] narrativeSummary;
	
	@Column(name="USER_INS")
	private String userIns;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	//@ShortDateFormat
	
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	@Column(name="LOCK_HOLDER")
	private String lockHolder;
	
	@Column(name="CERTIFICATE_DATA")
	private byte[] certificateData;     
	
	@Column(name="REVIEW_STATUS")
	private Integer reviewStatus;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name="ENDORSE_EXPIRY_DATE")
	private Date endorseExpireDate;
	
	@Column(name="DOC_FLAG")
	private Integer docFlag;
	
	@Column(name="CERTIFICATE_VERSION")
	private String certificateVer;
	
	@Column(name="VOID_REASONS")
	private String voidReason;
	
	
	
	@Column(name="REJECTION_REASON")
	private String rejectionReason;
	
	@Column(name="ALLOW_NEXT")
	private Integer allowNext;
	
	@Column(name="MAKE_FINAL")
	private Integer makeFinal;
	
	@Column(name="scope")
	private Integer scope;
	
	@Column(name="CREDIT_DATE")
	@ShortDateFormat
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date creditDate;
	
	 
	
	@Column(name="ADDITIONAL_SURVEY")
	private String additionalSurvey;
	
	
	@Column(name="MLC_DMLC_LINK")
	private Integer mlcDmlcLink;
	
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
	
	@Range(min=7,max=999999999,message="Vessel Name cannot be empty")
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	
	@Column(name="COMPANY_IMO_NO")
	@NotEmpty(message="Company IMO cannot be empty")
	private String companyImoNo;
	
	@Column(name="COMPANY_DOC")
	private String companyDoc;
	
	@Column(name="DOC_TYPE_NUMBER")
	private String docTypeNumber;
	
	@Column(name="DATE_OF_REGISTRY")
	//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	private Date dateOfRegistry;
	
	//vessel inof ends
	
	
	@Formula("(SELECT A.VESSEL_COMPANY_NAME FROM  MA_VESSEL_COMPANY A  WHERE A.COMPANY_IMO_NO=COMPANY_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String companyName;
	
	@Formula("(SELECT A.VESSEL_COMPANY_ADDRESS FROM  MA_VESSEL_COMPANY A  WHERE A.COMPANY_IMO_NO=COMPANY_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String companyAddress;
	
	@Formula("(SELECT A.OFFICIAL_NO FROM  MA_VESSEL A  WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String officialNo;
	
	@Formula("(SELECT A.LTRSTATUS FROM SSP_REVIEW_DATA A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID)")
	private String sspLtrStatus;
	
	@Formula("(SELECT A.SSP_REVISION_NO FROM SSP_REVIEW_DATA A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID)")
	private String sspRevisionNo;
	
	@Formula("(SELECT A.VESSEL_NAME FROM  MA_VESSEL A  WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String vesselName;
	
	@Formula("(SELECT A.DOC_ISSUER FROM DOC_NUMBER_CHANGE A WHERE A.SEQ_NO = ( SELECT MAX(B.SEQ_NO)  FROM DOC_NUMBER_CHANGE B WHERE B.COMPANY_IMO_NO = COMPANY_IMO_NO AND B.DOC_TYPE_NO = COMPANY_DOC AND B.COMPANY_ID=COMPANY_ID))")
	private String docIssuer;
	
	@ShortDateFormat
	@Formula("(SELECT A.DOC_EXPIRY FROM DOC_NUMBER_CHANGE A WHERE A.SEQ_NO = ( SELECT MAX(B.SEQ_NO)  FROM DOC_NUMBER_CHANGE B WHERE B.COMPANY_IMO_NO = COMPANY_IMO_NO AND B.DOC_TYPE_NO = COMPANY_DOC AND B.COMPANY_ID=COMPANY_ID))")
	private Date docExpiry;
	
	@Formula("(SELECT A.AUD_SUMMARY_DESC FROM MA_AUDIT_SUMMARY A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUMMARY_ID = AUDIT_SUMMARY_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSummaryDesc;
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audTypeDesc;
	
	@Formula("(SELECT A.CERTIFICATE_ISSUE_DESC FROM  MA_CERTIFICATE_ISSUED A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.CERTIFICATE_ISSUE_ID = CERTIFICATE_ISSUE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audCertIssueDesc;
	
	@Formula("(SELECT A.AUDIT_STATUS_DESC FROM  MA_AUDIT_STATUS A  WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_STATUS_ID = AUDIT_STATUS_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditStatusDesc;
	
	@Formula("(SELECT MAX(B.STATUS_DATE) FROM  AUDIT_FINDINGS_DETAILS B WHERE B.COMPANY_ID=COMPANY_ID AND B.CUR_AUDIT_SEQ_NO = "+AppConstant.CAR_UPDATED_CURRENT_SEQ+" AND B.ORIG_SEQ_NO IN (SELECT A.AUDIT_SEQ_NO FROM AUDIT_DETAILS A WHERE A.AUDIT_STATUS_ID <> "+AppConstant.VOID_AUDIT_STATUS+" AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_SEQ_NO < AUDIT_SEQ_NO AND A.VESSEL_IMO_NO = VESSEL_IMO_NO )  )")
	private String carFindMaxStatusDate;
	
	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM MA_USERS A WHERE A.USER_ID=(SELECT B.USER_ID FROM AUDIT_AUDITOR_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.AUD_LEAD_STATUS = 1)  AND A.COMPANY_ID = COMPANY_ID)")
	private String leadAuditorName;
	
	@Formula("(SELECT A.SEQUENCE_NUMBER FROM MA_USERS A WHERE A.USER_ID=(SELECT B.USER_ID FROM AUDIT_AUDITOR_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.AUD_LEAD_STATUS = 1)  AND A.COMPANY_ID = COMPANY_ID)")
	private String leadAuditorId;
	
	@Formula("(SELECT A.TC_APPROVAL_STATUS FROM MA_VESSEL A WHERE  A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String tcApprovalStatus;
	
	@Formula("(SELECT A.UTN FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM CERTIFICATE_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.COMPANY_ID = COMPANY_ID))")
	private String utn;
	
	@Formula("(SELECT A.CERTIFICATE_ID FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM CERTIFICATE_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.COMPANY_ID = COMPANY_ID))")
	private  Long certificateId;
	
	@Formula("(SELECT A.VESSEL_TYPE FROM MA_VESSEL A WHERE  A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID = COMPANY_ID)")
	private String vesselTypeName;
	
	@Formula("(SELECT A.AUD_SIGNATURE FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUD_LEAD_STATUS="+AppConstant.ACCEPT_STATUS+")")
	private byte[] leadSign;
	
	
	@Formula("(SELECT A.AUD_SIGNATURE FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_ROLE_ID="+AppConstant.AUDIT_REVIEWER_ROLE_ID+")")
	private byte[] reviewerSign;
	
	
	@Formula("(SELECT A.CREDIT_DATE FROM AUDIT_CYCLE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID AND  A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.AUDIT_SEQ_NO=AUDIT_SEQ_NO )")
	@ShortDateFormat     //added by sudharsan for Jira-id = IRI-5440
	private Date creditDateFromCyle;
	
	@Formula("(SELECT A.OFFICIAL_ID FROM MA_USERS A WHERE A.USER_ID=(SELECT B.USER_ID FROM AUDIT_AUDITOR_DETAILS B WHERE B.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND B.AUD_LEAD_STATUS = 1)  AND A.COMPANY_ID = COMPANY_ID)")
	private Long officialId;
	
	@Formula("(SELECT A.VESSEL_ID FROM  MA_VESSEL A  WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer vesselId;
	
	
	@Column(name="AUDIT_COMPLETE_LAPTOP")
	private  Integer auditComplteLaptop;
	

	@Fetch(FetchMode.SUBSELECT)	
 	@OneToMany(targetEntity = AuditFinding.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false)
	@OrderBy(clause = "FINDING_SEQ_NO ASC")
	private List<AuditFinding> auditFinding = new ArrayList<AuditFinding>();
 	
  	@Fetch(FetchMode.SUBSELECT)
  	@OneToMany(targetEntity = AuditAuditorDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = true)
  	@OrderBy(clause = "AUD_LEAD_STATUS DESC,AUDIT_ROLE_ID ASC")
  	private List<AuditAuditorDetail> auditAuditorDetail = new ArrayList<AuditAuditorDetail>();
 	  
 	@Fetch(FetchMode.SUBSELECT)
 	@OneToMany(targetEntity = AuditRptAttach.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = true)
 	@OrderBy(clause = "ATTACHMENT_TYPE_ID ASC")
 	private List<AuditRptAttach> auditRptAttach = new ArrayList<AuditRptAttach>();
 	
	@Fetch(FetchMode.SUBSELECT)
 	@OneToMany(targetEntity = SspReviewDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false)
 	private List<SspReviewDetail> sspReviewDetail = new ArrayList<SspReviewDetail>();
 	 
	@Fetch(FetchMode.SUBSELECT)
  	@OneToMany(targetEntity = CertificateDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false)
  	@OrderBy(clause = "ACTIVE_STATUS DESC, CERT_ORDER_NO DESC")
  	private List<CertificateDetail> certificateDetail = new ArrayList<CertificateDetail>();
	
  	@OneToOne(targetEntity = AuditCycle.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false ,optional = false)
  	@OrderBy(clause = "AUDIT_DATE DESC")
  	private AuditCycle auditCycle;
  	
  	@Formula("(SELECT A.CYCLE_GEN_NO FROM AUDIT_CYCLE A WHERE A.CYCLE_SEQ_NO  = AUDIT_SEQ_NO AND  A.AUDIT_TYPE_ID=AUDIT_TYPE_ID  AND A.COMPANY_ID = COMPANY_ID AND  A.VESSEL_IMO_NO=VESSEL_IMO_NO AND ROWNUM=1)")
	private Integer cycleGenNo;
  	
	@Transient
  	private Integer ihmReasonCertificate;
	
 	
  	public Integer getIhmReasonCertificate() {
		return ihmReasonCertificate;
	}

	public void setIhmReasonCertificate(Integer ihmReasonCertificate) {
		this.ihmReasonCertificate = ihmReasonCertificate;
	}
	
	@Transient
	private Integer maxSeqNo;

	public Integer getMaxSeqNo() {
		return maxSeqNo;
	}

	public void setMaxSeqNo(Integer maxSeqNo) {
		this.maxSeqNo = maxSeqNo;
	}

	@Transient
  	private List<CertificateDetailWithOutAudit> certificateWithoutAudit;
  	
	
  	@Transient
	private String userInsName;
	
	@Transient
	private boolean sync;
	
	@Transient
	private byte[] seal;
	
	@Transient
	private String signer;
	
	@Column(name="TITLE")
	private String title;
	
	@Transient
	private byte[] signature;
	
	@Transient
	private Integer userRole;
	
	
	@Column(name="VENDOR_NAME")
	private  String vendorName;
	
	@Column(name="VENDOR_ADDRESS")
	private  String vendorAddress;
	
	
	
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	
	

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public Long getOfficialId() {
		return officialId;
	}

	public void setOfficialId(Long officialId) {
		this.officialId = officialId;
	}

	public String getCertificateVer() {
		return certificateVer;
	}

	public void setCertificateVer(String certificateVer) {
	
		this.certificateVer = certificateVer;
	}
	
	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		System.out.println("12345");
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
	
	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
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

	public String getAuditReportNo() {
		return auditReportNo;
	}

	public void setAuditReportNo(String auditReportNo) {
		this.auditReportNo = auditReportNo;
	}

	public Integer getCertIssueId() {
		return certIssueId;
	}

	public void setCertIssueId(Integer certIssueId) {
		this.certIssueId = certIssueId;
	}

	public String getInteralAuditDate() {
		return interalAuditDate;
	}

	public void setInteralAuditDate(String interalAuditDate) {
		this.interalAuditDate = interalAuditDate;
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
	
	public Timestamp getOpenMeetingDate() {
		return openMeetingDate;
	}

	public void setOpenMeetingDate(Timestamp openMeetingDate) {
		this.openMeetingDate = openMeetingDate;
	}

	public Timestamp getCloseMeetingDate() {
		return closeMeetingDate;
	}

	public void setCloseMeetingDate(Timestamp closeMeetingDate){
		this.closeMeetingDate = closeMeetingDate;
	}

	public Integer getAuditSummaryId() {
		return auditSummaryId;
	}

	public void setAuditSummaryId(Integer auditSummaryId) {
		this.auditSummaryId = auditSummaryId;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public byte[] getNarrativeSummary() {
		return narrativeSummary;
	}

	public void setNarrativeSummary(byte[] narrativeSummary) {
		this.narrativeSummary = narrativeSummary;
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

	public List<AuditRptAttach> getAuditRptAttach() {
		return auditRptAttach;
	}

	public void setAuditRptAttach(List<AuditRptAttach> auditRptAttach) {
		this.auditRptAttach = auditRptAttach;
	}

	public String getAudSummaryDesc() {
		return audSummaryDesc;
	}

	public void setAudSummaryDesc(String audSummaryDesc) {
		this.audSummaryDesc = audSummaryDesc;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getAudCertIssueDesc() {
		return audCertIssueDesc;
	}

	public void setAudCertIssueDesc(String audCertIssueDesc) {
		this.audCertIssueDesc = audCertIssueDesc;
	}

	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}

	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}

	public String getLockHolder() {
		return lockHolder;
	}
	
    public void setLockHolder(String lockHolder) {
		this.lockHolder = lockHolder;
	}
	
	public AuditDetail(Integer auditSeqNo, Long companyId, Integer auditTypeId) {
		super();
		this.auditSeqNo = auditSeqNo;
		this.companyId = companyId;
		this.auditTypeId = auditTypeId;
	}

	public AuditDetail() {
		super();
	}

	public byte[] getCertificateData() {
		return certificateData;
	}

	public void setCertificateData(byte[] certificateData) {
		this.certificateData = certificateData;
	}
	
	public Integer getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	
	public String getDocIssuer() {
		return docIssuer;
	}

	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}

	public Date getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(Date docExpiry) {
		this.docExpiry = docExpiry;
	}

	public List<SspReviewDetail> getSspReviewDetail() {
		return sspReviewDetail;
	}

	public void setSspReviewDetail(List<SspReviewDetail> sspReviewDetail) {
		this.sspReviewDetail = sspReviewDetail;
	}

	public String getCarFindMaxStatusDate() {
		return carFindMaxStatusDate;
	}

	public void setCarFindMaxStatusDate(String carFindMaxStatusDate) {
		this.carFindMaxStatusDate = carFindMaxStatusDate;
	}
	
	public String getLeadAuditorName() {
		return leadAuditorName;
	}

	public void setLeadAuditorName(String leadAuditorName) {
		this.leadAuditorName = leadAuditorName;
	}
	
	public Date getEndorseExpireDate() {
		return endorseExpireDate;
	}

	public void setEndorseExpireDate(Date endorseExpireDate) {
		this.endorseExpireDate = endorseExpireDate;
	}

	public Integer getDocFlag() {
		return docFlag;
	}

	public void setDocFlag(Integer docFlag) {
		this.docFlag = docFlag;
	}

	public String getLeadAuditorId() {
		return leadAuditorId;
	}

	public void setLeadAuditorId(String leadAuditorId) {
		this.leadAuditorId = leadAuditorId;
	}
	
	public Integer getGrt() {
		return grt;
	}

	public void setGrt(Integer grt) {
		this.grt = grt;
	}

	public String getTcApprovalStatus() {
		return tcApprovalStatus;
	}

	public void setTcApprovalStatus(String tcApprovalStatus) {
		this.tcApprovalStatus = tcApprovalStatus;
	}

	public String getUtn() {
		return utn;
	}

	public void setUtn(String utn) {
		this.utn = utn;
	}

	public String getVoidReason() {
		return voidReason;
	}

	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}

	public String getUserInsName() {
		return userInsName;
	}

	public void setUserInsName(String userInsName) {
		this.userInsName = userInsName;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	
	public String getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(String officialNo) {
		this.officialNo = officialNo;
	}
	
	public byte[] getReviewerSign() {
		return reviewerSign;
	}

	public void setReviewerSign(byte[] reviewerSign) {
		this.reviewerSign = reviewerSign;
	}
	
	public Integer getAllowNext() {
		return allowNext;
	}

	public void setAllowNext(Integer allowNext) {
		this.allowNext = allowNext;
	}
	
	public Date getDateOfRegistry() {
		return dateOfRegistry;
	}

	public void setDateOfRegistry(Date dateOfRegistry) {
		this.dateOfRegistry = dateOfRegistry;
	}

	public String getAudTypeDesc() {
		return audTypeDesc;
	}

	public void setAudTypeDesc(String audTypeDesc) {
		this.audTypeDesc = audTypeDesc;
	}

	public String getVesselTypeName() {
		return vesselTypeName;
	}

	public void setVesselTypeName(String vesselTypeName) {
		this.vesselTypeName = vesselTypeName;
	}
	
	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	public byte[] getLeadSign() {
		return leadSign;
	}

	public void setLeadSign(byte[] leadSign) {
		this.leadSign = leadSign;
	}

	public Integer getMakeFinal() {
		return makeFinal;
	}

	public void setMakeFinal(Integer makeFinal) {
		this.makeFinal = makeFinal;
	}

	public List<CertificateDetail> getCertificateDetail() {
		return certificateDetail;
	}

	public void setCertificateDetail(List<CertificateDetail> certificateDetail) {
		this.certificateDetail = certificateDetail;
	}
	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	
	public String getSspLtrStatus() {
		return sspLtrStatus;
	}

	public void setSspLtrStatus(String sspLtrStatus) {
		this.sspLtrStatus = sspLtrStatus;
	}
	
	public String getSspRevisionNo() {
		return sspRevisionNo;
	}

	public void setSspRevisionNo(String sspRevisionNo) {
		this.sspRevisionNo = sspRevisionNo;
	}

	public Date getCreditDate() {
		
		
		return creditDate;
	}

	public void setCreditDate(Date creditDate) {
		
		this.creditDate = creditDate;
	}
	

	public String getAdditionalSurvey() {
		return additionalSurvey;
	}

	public void setAdditionalSurvey(String additionalSurvey) {
		this.additionalSurvey = additionalSurvey;
	}

	public Date getCreditDateFromCyle() {
		return creditDateFromCyle;
	}

	public void setCreditDateFromCyle(Date creditDateFromCyle) {
		this.creditDateFromCyle = creditDateFromCyle;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public byte[] getSeal() {
		return seal;
	}

	public void setSeal(byte[] seal) {
		this.seal = seal;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public AuditCycle getAuditCycle() {
		
		return auditCycle;
	}

	public void setAuditCycle(AuditCycle auditCycle) {
	
		this.auditCycle = auditCycle;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}
	
	public List<CertificateDetailWithOutAudit> getCertificateWithoutAudit() {
		return certificateWithoutAudit;
	}

	public void setCertificateWithoutAudit(List<CertificateDetailWithOutAudit> certificateWithoutAudit) {
		this.certificateWithoutAudit = certificateWithoutAudit;
	}
	public Integer getAuditComplteLaptop() {
		return auditComplteLaptop;
	}

	public void setAuditComplteLaptop(Integer auditComplteLaptop) {  
		this.auditComplteLaptop = auditComplteLaptop;
	}
	
	

	public Integer getMlcDmlcLink() {
		return mlcDmlcLink;
	}

	public void setMlcDmlcLink(Integer mlcDmlcLink) {
		this.mlcDmlcLink = mlcDmlcLink;
	}

	
	public Integer getCycleGenNo() {
		return cycleGenNo;
	}

	public void setCycleGenNo(Integer cycleGenNo) {
		this.cycleGenNo = cycleGenNo;
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

	public String getCompanyAddressAud() {
		return companyAddressAud;
	}

	public void setCompanyAddressAud(String companyAddressAud) {
		this.companyAddressAud = companyAddressAud;
	}

	
	@Override
	public String toString() {
		return "AuditDetail [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId=" + auditTypeId+ ", userRole=" + userRole
				+ ", vesselImoNo=" + vesselImoNo + ", auditSubTypeId=" + auditSubTypeId + ", auditDate=" + auditDate
				+ ", certExpireDate=" + certExpireDate + ", certIssueDate=" + certIssueDate + ", openMeetingDate="
				+ openMeetingDate + ", closeMeetingDate=" + closeMeetingDate + ", leadAuditorName=" + leadAuditorName
				+ ", sync=" + sync + "]";
	}
	


	


	
}
