package com.api.central.audit.entity;

import java.io.Serializable;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.api.central.util.AppConstant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="CERTIFICATE_IHM_DETAIL")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CertificateDetailWithOutAuditIhm implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	@Column(name="AUDIT_SEQ_NO")
	private Integer auditSeqNo;
 	 		
	@Id
	@Column(name="COMPANY_ID")
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
	@Id
	@Column(name="SEQ_NO")
	private Integer seqNo;
	
	@Column(name="CERTIFICATE_ID")
	private  Long certificateId;
	
	@Column(name="ENDORSEMENT_ID")
	private  Long endorsementID;
	
	
	@Column(name="AUDIT_SUB_TYPE_ID")
	private  Integer auditSubTypeId;
	
	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	@NotNull
	private Date auditDate;
	
	@Column(name="AUDIT_PLACE")
	private String auditPlace;
	
	@Transient
	private String qid;
	
	@Column(name="AUDIT_REPORT_NO")
	private String auditReportNo;
	
	@Column(name="CERTIFICATE_NO")
	
	private String certificateNo;
	
	@Column(name="UTN")
	private String utn;
	
	@Column(name="CERTIFICATE_ISSUE_ID")
	@Range(min=1000,max=10000,message="Certificate Issued cannot be empty")
 	private Integer certIssueId;
	
	@Column(name="QRCODE_URL")
	private String qrCodeUrl;
	
	@Column(name="CERTIFICATE_VERSION")
	private String certificateVer;
	
	@ShortDateFormat
	@Column(name="CERT_ISSUED_DATE")
	private Date certIssueDate;
	
	@ShortDateFormat
	@Column(name="CERT_EXPIRY_DATE")
	private Date certExpireDate;

	@ShortDateFormat
	@Column(name="EXTENDED_ISSUE_DATE")
	private Date extendedIssueDate;
	
	@ShortDateFormat
	@Column(name="EXTENDED_EXPIRY_DATE")
	private Date extendedExpireDate;
	
	@ShortDateFormat
	@Column(name="ENDORSED_DATE")
	private Date endorsedDate;
	
	@ShortDateFormat
	@Column(name="EXTENDED_ENDORSED_DATE")
	private Date extendedEndorsedDate;
	
	@Column(name="PUBLISH_STATUS")
	private Integer publishStatus;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;	
	
	@Column(name="NOTES")
	private String notes;
	
	@Column(name="LEAD_NAME")
	private String leadName;
	
	@Column(name="ISSUER_ID")
	private String issuerId;
	
	@Column(name="ISSUER_NAME")
	private String issuerName;
	
	@Lob
	@Column(name="ISSUER_SIGN")
	private byte[] issuerSign;
	
	@Lob
	@Column(name="SEAL")
	private byte[] seal;
	
	@Column(name="TITLE")
	private String title;
	
	@ShortDateFormat
	@Column(name="ISSUER_SIGN_DATE")
	private  Date issuerSignDate;
	
	@Column(name="NAME_TO_PRINT")
	private Integer nameToPrint;
	
	@Column(name="SIGN_TO_PRINT")
	private Integer signToPrint;
	
	@Column(name="VERIFY_DONE")
	private Integer verifyDone;
	
	@Column(name="VESSEL_ID")
	private  Integer vesselId;
	
	@Range(min=7,max=999999999,message="Vessel Imo cannot be empty")
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Column(name="VESSEL_NAME")
	@NotEmpty(message="Vessel Name cannot be empty")
	private String vesselName;
	
	@Column(name="GRT")
	@NotNull(message="GRT cannot be empty")
	private Integer grt;
	
	@Column(name="VESSEL_TYPE")
	@NotEmpty(message="VESSEL Type cannot be empty")
	private String vesselType;
	
	@Column(name="OFFICIAL_NO")
	@Range(min=1,message="Official No cannot be empty")
	private Long officialNo;
	
	@Column(name="PORT_OF_REGISTRY")
	@NotEmpty(message="Port Of Registry cannot be empty")
	private String portOfRegistry;
	
	@Column(name="DATE_OF_REGISTRY")
	@ShortDateFormat
	@NotNull(message="Date Of Registry cannot be empty")
	private Date dateOfRegistry;
	
	@Column(name="COMPANY_IMO_NO")
	@NotEmpty(message="Company IMO cannot be empty")
	private String companyImoNo;
	
	@Column(name="VESSEL_COMPANY_NAME")
	@NotEmpty(message="Vessel Company Name cannot be empty")
	private String vesselCompanyName;
	
	@Column(name="VESSEL_COMPANY_ADDRESS")
	@NotEmpty(message="Vessel Company Address cannot be empty")
	private String vesselCompanyAddress;
	
	@Column(name="VESSEL_UK")
	private  String vesselUk;
	
	@Column(name="VESSEL_PK")
	private  String vesselPk;
	
	@Column(name="CLASS_SOCIETY")
	private  String classSociety;
	
	@Column(name="CALL_SIGN")
	private  String callSign;
	
	@Column(name="DOC_TYPE_NUMBER")
	private  String docTypeNumber;
	
	@Column(name="DOC_TYPE")
	private  String docTypeNo;
	
	@Column(name="DOC_ISSUER")
	private  String docIssuer;
	
	@ShortDateFormat
	@Column(name="DOC_EXPIRY")
	private  Date docExpiry;
	
	@Column(name="USER_INS")
	private String userIns;
	
	@Column(name = "CERT_ORDER_NO")
	private Integer certOderNo;
	
	@Column(name = "IDENTIFICATION_NO")
	private String ihmDocumentNo;

	@Column(name = "IHM_PART1_REVIEW_DONE")
	private Integer ihmPrevReview;
	
	@Column(name = "SOC_TYPE")
	private String socType;

	@Column(name = "COMPLETION_SURVEY_DATE")
	@ShortDateFormat
	private Date completionSurveyDate;
	
	public String getSocType() {
		return socType;
	}

	public void setSocType(String socType) {
		this.socType = socType;
	}
	
	public String getIhmDocumentNo() {
		return ihmDocumentNo;
	}

	public void setIhmDocumentNo(String ihmDocumentNo) {
		this.ihmDocumentNo = ihmDocumentNo;
	}

	public Integer getIhmPrevReview() {
		return ihmPrevReview;
	}

	public void setIhmPrevReview(Integer ihmPrevReview) {
		this.ihmPrevReview = ihmPrevReview;
	}

	public Date getCompletionSurveyDate() {
		return completionSurveyDate;
	}

	public void setCompletionSurveyDate(Date completionSurveyDate) {
		this.completionSurveyDate = completionSurveyDate;
	}

	public Integer getRegOwnedImoNo() {
		return regOwnedImoNo;
	}

	public void setRegOwnedImoNo(Integer regOwnedImoNo) {
		this.regOwnedImoNo = regOwnedImoNo;
	}

	public Date getKeelLaidDate() {
		return keelLaidDate;
	}

	public void setKeelLaidDate(Date keelLaidDate) {
		this.keelLaidDate = keelLaidDate;
	}

	public Integer getCondEcGrant() {
		return condEcGrant;
	}

	public void setCondEcGrant(Integer condEcGrant) {
		this.condEcGrant = condEcGrant;
	}

	public Integer getVoyageEcGrant() {
		return voyageEcGrant;
	}

	public void setVoyageEcGrant(Integer voyageEcGrant) {
		this.voyageEcGrant = voyageEcGrant;
	}

	@Column(name = "REG_OWNED_IMO_NUMBER")
	private Integer regOwnedImoNo;
	
	@ShortDateFormat
	@Column(name = "KEEL_LAID_DATE")
	private Date keelLaidDate;
	
	@Column(name = "CONDITION_EC_GRANTED")
	private Integer condEcGrant;

	@Column(name = "VOYAGES_EC_GRANTED")
	private Integer voyageEcGrant;

	public Integer getCertOderNo() {
		return certOderNo;
	}

	public void setCertOderNo(Integer certOderNo) {
		this.certOderNo = certOderNo;
	}
	
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	@Column(name="CERTIFICATE_LINK_SEQ")
	private Integer certificateLink;
	
	@Formula("(SELECT A.AUDIT_STATUS_ID FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer auditStatusId;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditTypeDesc;
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = (SELECT B.AUDIT_SUB_TYPE_ID FROM AUDIT_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.COMPANY_ID = COMPANY_ID) AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Formula("(SELECT A.USER_ID FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID=COMPANY_ID AND A.AUD_LEAD_STATUS = 1)")
	private String leadAuditorId;
	
	@Formula("(SELECT MAX(A.AUDIT_SEQ_NO) FROM AUDIT_DETAILS A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND (A.AUDIT_SUB_TYPE_ID="+AppConstant.INITIAL_SUB_TYPE_ID+" OR A.AUDIT_SUB_TYPE_ID="+AppConstant.RENEWAL_SUB_TYPE_ID+" ) AND A.AUDIT_SEQ_NO>AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer nextAdtSeq;
	
	@Formula("(SELECT A.CERTIFICATE_ISSUE_DESC FROM MA_CERTIFICATE_ISSUED A WHERE A.CERTIFICATE_ISSUE_ID = CERTIFICATE_ISSUE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String certIssueDesc;
	
	@ShortDateFormat
	@Formula("(SELECT A.AUD_SIGNATURE_DATE FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID=COMPANY_ID AND A.AUD_LEAD_STATUS = 1)")
	private Date leadAuditorSignDate;
	
	@Formula("(SELECT A.LOCK_STATUS FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer lockStatus;
	
	@Formula("(SELECT A.LOCK_HOLDER FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private String lockHolder;
	
	@Formula("(SELECT A.ALLOW_NEXT FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer allowNext;
	
	@Formula("(SELECT A.MAKE_FINAL FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer makeFinal;
	
	@LongDateFormat
	@Formula("(SELECT A.OPEN_MEETING_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Timestamp openMeetingDate;
	
	@LongDateFormat
	@Formula("(SELECT A.CLOSE_MEETING_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Timestamp closeMeetingDate;
	
	@Formula("(SELECT MAX(A.SEQ_NO) FROM  CERTIFICATE_DETAIL A  WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer maxSeqNo;
	
	@Column(name = "CONSECTIVE_SUBSEQUENT")
	private Integer consecutiveId;
	
	@Transient
	private Boolean prevData;
	
	@Transient
	private Object prevAuditIssueData;
		
	@Transient
	private String userInsName;

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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	public Long getEndorsementID() {
		return endorsementID;
	}

	public void setEndorsementID(Long endorsementID) {
		this.endorsementID = endorsementID;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
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

	public String getAuditReportNo() {
		return auditReportNo;
	}

	public void setAuditReportNo(String auditReportNo) {
		this.auditReportNo = auditReportNo;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getUtn() {
		return utn;
	}

	public void setUtn(String utn) {
		this.utn = utn;
	}

	public Integer getCertIssueId() {
		return certIssueId;
	}

	public void setCertIssueId(Integer certIssueId) {
		this.certIssueId = certIssueId;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getCertificateVer() {
		return certificateVer;
	}

	public void setCertificateVer(String certificateVer) {
		this.certificateVer = certificateVer;
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

	public Date getExtendedIssueDate() {
		return extendedIssueDate;
	}

	public void setExtendedIssueDate(Date extendedIssueDate) {
		this.extendedIssueDate = extendedIssueDate;
	}

	public Date getExtendedExpireDate() {
		return extendedExpireDate;
	}

	public void setExtendedExpireDate(Date extendedExpireDate) {
		this.extendedExpireDate = extendedExpireDate;
	}

	public Date getEndorsedDate() {
		return endorsedDate;
	}

	public void setEndorsedDate(Date endorsedDate) {
		this.endorsedDate = endorsedDate;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public byte[] getIssuerSign() {
		return issuerSign;
	}

	public void setIssuerSign(byte[] issuerSign) {
		this.issuerSign = issuerSign;
	}

	public Date getIssuerSignDate() {
		return issuerSignDate;
	}

	public void setIssuerSignDate(Date issuerSignDate) {
		this.issuerSignDate = issuerSignDate;
	}

	public Integer getNameToPrint() {
		return nameToPrint;
	}

	public void setNameToPrint(Integer nameToPrint) {
		this.nameToPrint = nameToPrint;
	}

	public Integer getSignToPrint() {
		return signToPrint;
	}

	public void setSignToPrint(Integer signToPrint) {
		this.signToPrint = signToPrint;
	}

	public Integer getVerifyDone() {
		return verifyDone;
	}

	public void setVerifyDone(Integer verifyDone) {
		this.verifyDone = verifyDone;
	}

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
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

	public Integer getGrt() {
		return grt;
	}

	public void setGrt(Integer grt) {
		this.grt = grt;
	}

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}

	public Long getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(Long officialNo) {
		this.officialNo = officialNo;
	}

	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}

	public Date getDateOfRegistry() {
		return dateOfRegistry;
	}

	public void setDateOfRegistry(Date dateOfRegistry) {
		this.dateOfRegistry = dateOfRegistry;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getVesselCompanyName() {
		return vesselCompanyName;
	}

	public void setVesselCompanyName(String vesselCompanyName) {
		this.vesselCompanyName = vesselCompanyName;
	}

	public String getVesselCompanyAddress() {
		return vesselCompanyAddress;
	}

	public void setVesselCompanyAddress(String vesselCompanyAddress) {
		this.vesselCompanyAddress = vesselCompanyAddress;
	}

	public String getVesselUk() {
		return vesselUk;
	}

	public void setVesselUk(String vesselUk) {
		this.vesselUk = vesselUk;
	}

	public String getVesselPk() {
		return vesselPk;
	}

	public void setVesselPk(String vesselPk) {
		this.vesselPk = vesselPk;
	}

	public String getClassSociety() {
		return classSociety;
	}

	public void setClassSociety(String classSociety) {
		this.classSociety = classSociety;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public String getDocTypeNo() {
		return docTypeNo;
	}

	public void setDocTypeNo(String docTypeNo) {
		this.docTypeNo = docTypeNo;
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

	public Integer getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(Integer auditStatusId) {
		this.auditStatusId = auditStatusId;
	}

	public String getAuditTypeDesc() {
		return auditTypeDesc;
	}

	public void setAuditTypeDesc(String auditTypeDesc) {
		this.auditTypeDesc = auditTypeDesc;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public String getLeadAuditorId() {
		return leadAuditorId;
	}

	public void setLeadAuditorId(String leadAuditorId) {
		this.leadAuditorId = leadAuditorId;
	}

	public Integer getNextAdtSeq() {
		return nextAdtSeq;
	}

	public void setNextAdtSeq(Integer nextAdtSeq) {
		this.nextAdtSeq = nextAdtSeq;
	}

	public String getCertIssueDesc() {
		return certIssueDesc;
	}

	public void setCertIssueDesc(String certIssueDesc) {
		this.certIssueDesc = certIssueDesc;
	}

	public Date getLeadAuditorSignDate() {
		return leadAuditorSignDate;
	}

	public void setLeadAuditorSignDate(Date leadAuditorSignDate) {
		this.leadAuditorSignDate = leadAuditorSignDate;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Boolean getPrevData() {
		return prevData;
	}

	public void setPrevData(Boolean prevData) {
		this.prevData = prevData;
	}

	public String getUserInsName() {
		return userInsName;
	}

	public void setUserInsName(String userInsName) {
		this.userInsName = userInsName;
	}

	public Integer getAllowNext() {
		return allowNext;
	}

	public void setAllowNext(Integer allowNext) {
		this.allowNext = allowNext;
	}

	public Timestamp getOpenMeetingDate() {
		return openMeetingDate;
	}

	public void setOpenMeetingDate(Timestamp openMeetingDate) {
		this.openMeetingDate = openMeetingDate;
	}
	
	public String getLockHolder() {
		return lockHolder;
	}

	public void setLockHolder(String lockHolder) {
		this.lockHolder = lockHolder;
	}
	
	

	public byte[] getSeal() {
		return seal;
	}

	public void setSeal(byte[] seal) {
		this.seal = seal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getExtendedEndorsedDate() {
		return extendedEndorsedDate;
	}

	public void setExtendedEndorsedDate(Date extendedEndorsedDate) {
		this.extendedEndorsedDate = extendedEndorsedDate;
	}

	public Integer getMakeFinal() {
		return makeFinal;
	}

	public void setMakeFinal(Integer makeFinal) {
		this.makeFinal = makeFinal;
	}

	public Timestamp getCloseMeetingDate() {
		return closeMeetingDate;
	}

	public void setCloseMeetingDate(Timestamp closeMeetingDate) {
		this.closeMeetingDate = closeMeetingDate;
	}

	public Integer getMaxSeqNo() {
		return maxSeqNo;
	}

	public void setMaxSeqNo(Integer maxSeqNo) {
		this.maxSeqNo = maxSeqNo;
	}

	public Integer getConsecutiveId() {
		return consecutiveId;
	}

	public void setConsecutiveId(Integer consecutiveId) {
		this.consecutiveId = consecutiveId;
	}

	public Object getPrevAuditIssueData() {
		return prevAuditIssueData;
	}

	public void setPrevAuditIssueData(Object prevAuditIssueData) {
		this.prevAuditIssueData = prevAuditIssueData;
	}
	
	public Integer getCertificateLink() {
		return certificateLink;
	}

	public void setCertificateLink(Integer certificateLink) {
		this.certificateLink = certificateLink;
	}
	
	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	@Override
	public String toString() {
		return "CertificateDetailWithOutAudit [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId="
				+ auditTypeId + ", seqNo=" + seqNo + ", certificateId=" + certificateId + ", endorsementID="
				+ endorsementID + ", auditSubTypeId=" + auditSubTypeId + ", auditDate=" + auditDate + ", auditPlace="
				+ auditPlace + ", qid=" + qid + ", auditReportNo=" + auditReportNo + ", certificateNo=" + certificateNo
				+ ", utn=" + utn + ", certIssueId=" + certIssueId + ", qrCodeUrl=" + qrCodeUrl + ", certificateVer="
				+ certificateVer + ", certIssueDate=" + certIssueDate + ", certExpireDate=" + certExpireDate
				+ ", extendedIssueDate=" + extendedIssueDate + ", extendedExpireDate=" + extendedExpireDate
				+ ", endorsedDate=" + endorsedDate + ", extendedEndorsedDate=" + extendedEndorsedDate
				+ ", publishStatus=" + publishStatus + ", activeStatus=" + activeStatus + ", notes=" + notes
				+ ", socType=" + socType
				+ ", leadName=" + leadName + ", issuerId=" + issuerId + ", issuerName=" + issuerName + ", issuerSign="
				+ Arrays.toString(issuerSign) + ", seal=" + Arrays.toString(seal) + ", title=" + title
				+ ", issuerSignDate=" + issuerSignDate + ", nameToPrint=" + nameToPrint + ", signToPrint=" + signToPrint
				+ ", verifyDone=" + verifyDone + ", vesselId=" + vesselId + ", vesselImoNo=" + vesselImoNo
				+ ", vesselName=" + vesselName + ", grt=" + grt + ", vesselType=" + vesselType + ", officialNo="
				+ officialNo + ", portOfRegistry=" + portOfRegistry + ", dateOfRegistry=" + dateOfRegistry
				+ ", companyImoNo=" + companyImoNo + ", vesselCompanyName=" + vesselCompanyName
				+ ", vesselCompanyAddress=" + vesselCompanyAddress + ", vesselUk=" + vesselUk + ", vesselPk=" + vesselPk
				+ ", classSociety=" + classSociety + ", callSign=" + callSign + ", docTypeNumber=" + docTypeNumber
				+ ", docTypeNo=" + docTypeNo + ", docIssuer=" + docIssuer + ", docExpiry=" + docExpiry + ", userIns="
				+ userIns + ", certOderNo=" + certOderNo + ", ihmDocumentNo=" + ihmDocumentNo + ", ihmPrevReview="
				+ ihmPrevReview + ", completionSurveyDate=" + completionSurveyDate + ", regOwnedImoNo=" + regOwnedImoNo
				+ ", keelLaidDate=" + keelLaidDate + ", condEcGrant=" + condEcGrant + ", voyageEcGrant=" + voyageEcGrant
				+ ", dateIns=" + dateIns + ", certificateLink=" + certificateLink + ", auditStatusId=" + auditStatusId
				+ ", auditTypeDesc=" + auditTypeDesc + ", audSubTypeDesc=" + audSubTypeDesc + ", leadAuditorId="
				+ leadAuditorId + ", nextAdtSeq=" + nextAdtSeq + ", certIssueDesc=" + certIssueDesc
				+ ", leadAuditorSignDate=" + leadAuditorSignDate + ", lockStatus=" + lockStatus + ", lockHolder="
				+ lockHolder + ", allowNext=" + allowNext + ", makeFinal=" + makeFinal + ", openMeetingDate="
				+ openMeetingDate + ", closeMeetingDate=" + closeMeetingDate + ", maxSeqNo=" + maxSeqNo
				+ ", consecutiveId=" + consecutiveId + ", prevData=" + prevData + ", prevAuditIssueData="
				+ prevAuditIssueData + ", userInsName=" + userInsName + "]";
	}
	
}
