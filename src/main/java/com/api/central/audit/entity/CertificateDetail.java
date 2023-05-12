package com.api.central.audit.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.api.central.util.AppConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "CERTIFICATE_DETAIL")
@IdClass(CertificateDetailsCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CertificateDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUDIT_SEQ_NO", insertable = false, updatable = false)
	private Integer auditSeqNo;

	@Id
	@Column(name = "COMPANY_ID", insertable = false, updatable = false)
	@Range(min = 1, max = 100, message = "Company Name cannot be empty")
	private Long companyId;

	@Id
	@Column(name = "AUDIT_TYPE_ID", insertable = false, updatable = false)
	@Range(min = 1000, max = 10000, message = "Audit Type cannot be empty")
	private Integer auditTypeId;

	@Id
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	@Column(name = "CERTIFICATE_ID")
	private Long certificateId;

	@Column(name = "ENDORSEMENT_ID")
	private Long endorsementID;

	@Column(name = "AUDIT_SUB_TYPE_ID")
	private Integer auditSubTypeId;

	// @DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name = "AUDIT_DATE")
	//@NotNull
	private Date auditDate;

	@Column(name = "AUDIT_PLACE")
	private String auditPlace;

	@Transient
	private String qid;

	@Column(name = "AUDIT_REPORT_NO")
	private String auditReportNo;

	@Column(name = "CERTIFICATE_NO")

	private String certificateNo;

	@Column(name = "UTN")
	private String utn;

	@Column(name = "CERTIFICATE_ISSUE_ID")
	@Range(min = 1000, max = 10000, message = "Certificate Issued cannot be empty")
	private Integer certIssueId;

	@Column(name = "QRCODE_URL")
	private String qrCodeUrl;

	@Column(name = "CERTIFICATE_VERSION")
	private String certificateVer;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "CERT_ISSUED_DATE")
	private Date certIssueDate;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "CERT_EXPIRY_DATE")
	private Date certExpireDate;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "EXTENDED_ISSUE_DATE")
	private Date extendedIssueDate;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "EXTENDED_EXPIRY_DATE")
	private Date extendedExpireDate;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "ENDORSED_DATE")
	private Date endorsedDate;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "EXTENDED_ENDORSED_DATE")
	private Date extendedEndorsedDate;

	@Column(name = "PUBLISH_STATUS")
	private Integer publishStatus;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Column(name = "NOTES")
	private String notes;

	@Column(name = "LEAD_NAME")
	private String leadName;

	@Column(name = "ISSUER_ID")
	private String issuerId;

	@Column(name = "ISSUER_NAME")
	private String issuerName;

	@Lob
	@Column(name = "ISSUER_SIGN")
	private byte[] issuerSign;

	@Lob
	@Column(name = "SEAL")
	private byte[] seal;

	@Column(name = "TITLE")
	private String title;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "ISSUER_SIGN_DATE")
	private Date issuerSignDate;

	@Column(name = "NAME_TO_PRINT")
	private Integer nameToPrint;

	@Column(name = "SIGN_TO_PRINT")
	private Integer signToPrint;

	@Column(name = "VERIFY_DONE")
	private Integer verifyDone;

	@Column(name = "VESSEL_ID")
	private Integer vesselId;

	@Range(min = 7, max = 999999999, message = "Vessel Imo cannot be empty")
	@Column(name = "VESSEL_IMO_NO")
	private Integer vesselImoNo;

	@Column(name = "VESSEL_NAME")
	@NotEmpty(message = "Vessel Name cannot be empty")
	private String vesselName;

	@Column(name = "GRT")
	@NotNull(message = "GRT cannot be empty")
	private Integer grt;

	@Column(name = "VESSEL_TYPE")
	@NotEmpty(message = "VESSEL Type cannot be empty")
	private String vesselType;

	@Column(name = "OFFICIAL_NO")
	@Range(min = 1, message = "Official No cannot be empty")
	private Long officialNo;

	@Column(name = "PORT_OF_REGISTRY")
	@NotEmpty(message = "Port Of Registry cannot be empty")
	private String portOfRegistry;

	@Column(name = "DATE_OF_REGISTRY")
	@ShortDateFormat
	@NotNull(message = "Date Of Registry cannot be empty")
	private Date dateOfRegistry;

	@Column(name = "COMPANY_IMO_NO")
	@NotEmpty(message = "Company IMO cannot be empty")
	private String companyImoNo;

	@Column(name = "VESSEL_COMPANY_NAME")
	@NotEmpty(message = "Vessel Company Name cannot be empty")
	private String vesselCompanyName;

	@Column(name = "VESSEL_COMPANY_ADDRESS")
	@NotEmpty(message = "Vessel Company Address cannot be empty")
	private String vesselCompanyAddress;

	@Column(name = "VESSEL_UK")
	private String vesselUk;

	@Column(name = "VESSEL_PK")
	private String vesselPk;

	@Column(name = "CLASS_SOCIETY")
	private String classSociety;

	@Column(name = "CALL_SIGN")
	private String callSign;

	@Column(name = "DOC_TYPE_NUMBER")
	private String docTypeNumber;

	@Column(name = "DOC_TYPE")
	private String docTypeNo;

	@Column(name = "DOC_ISSUER")
	private String docIssuer;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Column(name = "DOC_EXPIRY")
	private Date docExpiry;

	@Column(name = "USER_INS")
	private String userIns;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@UpdateTimestamp
	//@LongDateFormat
	@Column(name = "DATE_INS")
	private java.util.Date dateIns; //modified by sudharsan for INCEDENT_TICKET-671

	@Column(name = "REISSUE_REASON")
	private Integer certReissueReason;
	
	@Column(name = "IDENTIFICATION_NO")
	private String ihmDocumentNo;

	@Column(name = "IHM_PART1_REVIEW_DONE")
	private Integer ihmPrevReview;
	
	@Column(name = "COMPLETION_SURVEY_DATE")
	@ShortDateFormat
	private Date completionSurveyDate;
	
	@Column(name = "COMPLETION_DATE")
	private String completionDate;
	
	@Column(name = "DMLCII_ISSUE_DATE")
	private String dmlcIssueDate;
	
	@Column(name = "DMLCII_ISSUE_LOCATION")
	private String dmlcIssuePlace;
	
	
	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	public String getDmlcIssueDate() {
		return dmlcIssueDate;
	}

	public void setDmlcIssueDate(String dmlcIssueDate) {
		this.dmlcIssueDate = dmlcIssueDate;
	}

	public String getDmlcIssuePlace() {
		return dmlcIssuePlace;
	}

	public void setDmlcIssuePlace(String dmlcIssuePlace) {
		this.dmlcIssuePlace = dmlcIssuePlace;
	}
	
	@Formula("(SELECT A.AUDIT_STATUS_ID FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer auditStatusId;

	@Formula("(SELECT A.AUDIT_PLACE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private String auditDtlsAuditPlace;

	@Formula("(SELECT A.DATE_OF_REGISTRY FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	@ShortDateFormat
	private Date auditDtlsdateOfRegistry;

	@Formula("(SELECT A.AUDIT_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	@ShortDateFormat
	private Date auditDtlsAuditDate;

	@Formula("(SELECT A.AUDIT_SUMMARY_ID FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer auditSummaryId;

	@Formula("(SELECT A.GRT FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer auditDtlsGrt;

	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private String auditTypeDesc;

	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = (SELECT B.AUDIT_SUB_TYPE_ID FROM AUDIT_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.COMPANY_ID = COMPANY_ID) AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private String audSubTypeDesc;

	@Formula("(SELECT A.USER_ID FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID=COMPANY_ID AND A.AUD_LEAD_STATUS = 1 AND ROWNUM=1)")
	private String leadAuditorId;

	@Formula("(SELECT MAX(A.AUDIT_SEQ_NO) FROM AUDIT_DETAILS A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND (A.AUDIT_SUB_TYPE_ID="
			+ AppConstant.INITIAL_SUB_TYPE_ID + " OR A.AUDIT_SUB_TYPE_ID=" + AppConstant.RENEWAL_SUB_TYPE_ID
			+ " ) AND A.AUDIT_SEQ_NO>AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer nextAdtSeq;

	@Formula("(SELECT A.CERTIFICATE_ISSUE_DESC FROM MA_CERTIFICATE_ISSUED A WHERE A.CERTIFICATE_ISSUE_ID = CERTIFICATE_ISSUE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private String certIssueDesc;

	// @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@ShortDateFormat
	@Formula("(SELECT A.AUD_SIGNATURE_DATE FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID=COMPANY_ID AND A.AUD_LEAD_STATUS = 1 AND ROWNUM=1)")
	private Date leadAuditorSignDate;

	@Formula("(SELECT A.LOCK_STATUS FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID )")
	private Integer lockStatus;

	@Formula("(SELECT A.LOCK_HOLDER FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private String lockHolder;

	@Formula("(SELECT A.ALLOW_NEXT FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer allowNext;

	@Formula("(SELECT A.MAKE_FINAL FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer makeFinal;

	// @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@LongDateFormat
	@Formula("(SELECT A.OPEN_MEETING_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Timestamp openMeetingDate;

	@LongDateFormat
	@Formula("(SELECT A.CLOSE_MEETING_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Timestamp closeMeetingDate;

	@Formula("(SELECT MAX(A.SEQ_NO) FROM  CERTIFICATE_DETAIL A  WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer maxSeqNo;

	@Formula("(SELECT COUNT(A.AUDIT_SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.FULL_TERM_CERT + " AND A.PUBLISH_STATUS=" + AppConstant.AUD_CERT_PUBLISH_STATUS
			+ " AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer getRenewalCertVal;

	@Formula("(SELECT COUNT(A.AUDIT_SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.VESSEL_IMO_NO = VESSEL_IMO_NO AND A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND (A.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.INTERMEDAITE_ENDORSED + " OR A.CERTIFICATE_ISSUE_ID = " + AppConstant.ADDITIONAL_ENDORSED
			+ ") AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer getEndrosedCount;

	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUDIT_SUB_TYPE_ID = "
			+ AppConstant.RENEWAL_SUB_TYPE_ID + " AND A.VESSEL_IMO_NO = VESSEL_IMO_NO AND (A.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.FULL_TERM_CERT + " OR A.CERTIFICATE_ISSUE_ID = " + AppConstant.RENEWAL_ENDORSED2
			+ ")  AND A.COMPANY_ID = COMPANY_ID AND A.PUBLISH_STATUS = " + AppConstant.AUD_CERT_PUBLISH_STATUS + " AND ROWNUM=1)")
	private String getRenewalPublishCount;

	
	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO < AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID= AUDIT_TYPE_ID AND (A.AUDIT_SUB_TYPE_ID = "+AppConstant.INITIAL_SUB_TYPE_ID+" OR A.AUDIT_SUB_TYPE_ID = "+AppConstant.RENEWAL_SUB_TYPE_ID+") AND A.VESSEL_IMO_NO = VESSEL_IMO_NO AND (A.CERTIFICATE_ISSUE_ID = "+AppConstant.FULL_TERM_CERT+" OR A.CERTIFICATE_ISSUE_ID="+AppConstant.RE_ISSUE+")  AND A.COMPANY_ID = COMPANY_ID AND A.PUBLISH_STATUS = "+AppConstant.AUD_CERT_PUBLISH_STATUS+" AND ROWNUM=1 AND A.AUDIT_SEQ_NO NOT IN ( SELECT AUD.AUDIT_SEQ_NO FROM AUDIT_DETAILS AUD WHERE AUD.VESSEL_IMO_NO = VESSEL_IMO_NO AND  AUD.AUDIT_TYPE_ID= AUDIT_TYPE_ID AND (AUD.AUDIT_SUB_TYPE_ID = "+AppConstant.INITIAL_SUB_TYPE_ID+" OR AUD.AUDIT_SUB_TYPE_ID = "+AppConstant.RENEWAL_SUB_TYPE_ID+") AND AUD.AUDIT_STATUS_ID = "+AppConstant.VOID_AUDIT_STATUS+") )")
	private String getInitialRenewalPublishCount;
	
	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) >0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.VESSEL_IMO_NO = VESSEL_IMO_NO AND A.AUDIT_SUB_TYPE_ID = "
			+ AppConstant.RENEWAL_SUB_TYPE_ID + " AND A.CERTIFICATE_ISSUE_ID =" + AppConstant.RENEWAL_ENDORSED2
			+ " AND A.COMPANY_ID=COMPANY_ID AND A.PUBLISH_STATUS = " + AppConstant.AUD_CERT_PUBLISH_STATUS
			+ " AND 0 = (SELECT CASE WHEN COUNT(B.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL B WHERE B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.AUDIT_SUB_TYPE_ID = "
			+ AppConstant.RENEWAL_SUB_TYPE_ID + " AND B.VESSEL_IMO_NO = VESSEL_IMO_NO AND B.CERTIFICATE_ISSUE_ID ="
			+ AppConstant.FULL_TERM_CERT + " AND B.COMPANY_ID=COMPANY_ID AND B.PUBLISH_STATUS = "
			+ AppConstant.AUD_CERT_PUBLISH_STATUS + ") AND ROWNUM=1)")
	private String checkOnlyRenewalEndrosed;

	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) >0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.VESSEL_IMO_NO = VESSEL_IMO_NO AND A.AUDIT_SUB_TYPE_ID = "
			+ AppConstant.RENEWAL_SUB_TYPE_ID + " AND A.CERTIFICATE_ISSUE_ID =" + AppConstant.RENEWAL_ENDORSED2
			+ " AND A.COMPANY_ID=COMPANY_ID AND A.PUBLISH_STATUS = " + AppConstant.AUD_CERT_PUBLISH_STATUS
			+ "  AND A.SEQ_NO >(SELECT B.SEQ_NO FROM CERTIFICATE_DETAIL B WHERE B.AUDIT_SEQ_NO = AUDIT_SEQ_NO  AND B.AUDIT_SUB_TYPE_ID = "
			+ AppConstant.RENEWAL_SUB_TYPE_ID + " AND B.VESSEL_IMO_NO = VESSEL_IMO_NO AND B.CERTIFICATE_ISSUE_ID ="
			+ AppConstant.FULL_TERM_CERT + " AND B.COMPANY_ID=COMPANY_ID AND B.PUBLISH_STATUS = "
			+ AppConstant.AUD_CERT_PUBLISH_STATUS + ") AND ROWNUM=1)")
	private String checkRenEndrosAftFullTerm;

	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.AUDIT_SUB_TYPE_ID = "
			+ AppConstant.RENEWAL_SUB_TYPE_ID + " AND A.VESSEL_IMO_NO = VESSEL_IMO_NO AND A.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.RENEWAL_ENDORSED1
			+ " AND A.COMPANY_ID = COMPANY_ID AND A.CERTIFICATE_NO = CERTIFICATE_NO AND A.PUBLISH_STATUS = "
			+ AppConstant.AUD_CERT_PUBLISH_STATUS + " AND ROWNUM=1)")
	private String checkAdminRenewalEndrosed;

	@Formula("(SELECT CASE WHEN B.SEQ_NO = (SELECT MAX(A.SEQ_NO) FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID=COMPANY_ID) AND B.ACTIVE_STATUS = "
			+ AppConstant.ACTIVE_STATUS
			+ "  THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL B WHERE B.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND B.VESSEL_IMO_NO = VESSEL_IMO_NO AND B.COMPANY_ID = COMPANY_ID AND B.SEQ_NO = SEQ_NO AND ROWNUM=1)")
	private Integer getExactActiveStatus;

	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.VESSEL_IMO_NO = VESSEL_IMO_NO AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.CERTIFICATE_NO = CERTIFICATE_NO AND A.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.EXTENSION + "  AND A.COMPANY_ID = COMPANY_ID AND A.PUBLISH_STATUS = "
			+ AppConstant.AUD_CERT_PUBLISH_STATUS + " AND A.AUDIT_SUB_TYPE_ID IN (" + AppConstant.INITIAL_SUB_TYPE_ID
			+ "," + AppConstant.RENEWAL_SUB_TYPE_ID + ") AND ROWNUM=1)")
	private Integer checkExtensionCert;

	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END  FROM AUDIT_DETAILS A, CERTIFICATE_DETAIL B WHERE A.AUDIT_SEQ_NO=B.AUDIT_SEQ_NO AND A.VESSEL_IMO_NO=B.VESSEL_IMO_NO AND A.COMPANY_ID=B.COMPANY_ID AND A.AUDIT_STATUS_ID <> "
			+ AppConstant.VOID_AUDIT_STATUS + " AND A.AUDIT_SUMMARY_ID <> " + AppConstant.NOT_APPROVED_SUMMARY
			+ " AND B.VESSEL_IMO_NO = VESSEL_IMO_NO AND B.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND B.CERTIFICATE_NO = CERTIFICATE_NO AND B.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.RENEWAL_ENDORSED1 + "  AND B.COMPANY_ID = COMPANY_ID AND B.PUBLISH_STATUS = "
			+ AppConstant.AUD_CERT_PUBLISH_STATUS + " AND B.AUDIT_SUB_TYPE_ID = " + AppConstant.RENEWAL_SUB_TYPE_ID
			+ " AND ROWNUM=1)")
	private Integer checkAdminRenewalCert;

	@Formula("(SELECT CASE WHEN COUNT(A.AUDIT_SEQ_NO) > 0 THEN 1 ELSE 0 END FROM CERTIFICATE_DETAIL A WHERE A.VESSEL_IMO_NO = VESSEL_IMO_NO AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.AUDIT_SEQ_NO = AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.CERTIFICATE_ISSUE_ID = "
			+ AppConstant.EXTENSION + " AND A.AUDIT_SUB_TYPE_ID = " + AppConstant.RENEWAL_SUB_TYPE_ID + " AND ROWNUM=1)")
	private Integer checkExtensionInRenewal;

	@Column(name = "CONSECTIVE_SUBSEQUENT")
	private Integer consecutiveId;

	@Transient
	private Boolean prevData;

	@Transient
	private String isSync;
	
	@Transient
	private Date extendDate;
	
	
	@Transient
	private Integer roleId;
	
	@Column(name = "REG_OWNED_IMO_NUMBER")
	private Integer regOwnedImoNo;
	
	@ShortDateFormat
	@Column(name = "KEEL_LAID_DATE")
	private Date keelLaidDate;
	
	@Column(name = "CONDITION_EC_GRANTED")
	private Integer condEcGrant;

	@Column(name = "VOYAGES_EC_GRANTED")
	private Integer voyageEcGrant;
	
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Date getExtendDate() {
		return extendDate;
	}

	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}

	@Column(name = "CERT_ORDER_NO")
	private Integer certOderNo;

	public Integer getCertOderNo() {
		return certOderNo;
	}

	public void setCertOderNo(Integer certOderNo) {
		this.certOderNo = certOderNo;
	}

	public String getIsSync() {
		return isSync;
	}

	public void setIsSync(String isSync) {
		this.isSync = isSync;
	}

	@Transient
	private Object prevAuditIssueData;

	@Transient
	private String userInsName;

	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"),
			@JoinColumn(name = "AUDIT_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID") })

	private AuditDetail auditDetail;

	@Column(name = "CERTIFICATE_LINK_SEQ")
	private Integer certificateLink;

	@Formula("(SELECT A.ROLE_ID FROM MA_USER_ROLES A WHERE A.USER_ID = ISSUER_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Integer generatedBy;

	public Integer getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(Integer generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String getGetInitialRenewalPublishCount() {
		return getInitialRenewalPublishCount;
	}

	public void setGetInitialRenewalPublishCount(String getInitialRenewalPublishCount) {
		this.getInitialRenewalPublishCount = getInitialRenewalPublishCount;
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

	public java.util.Date getDateIns() {
		return dateIns;
	}

	public void setDateIns(java.util.Date dateIns) {
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

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
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

	public Integer getGetRenewalCertVal() {
		return getRenewalCertVal;
	}

	public void setGetRenewalCertVal(Integer getRenewalCertVal) {
		this.getRenewalCertVal = getRenewalCertVal;
	}

	public Integer getGetEndrosedCount() {
		
		return getEndrosedCount;
	}

	public void setGetEndrosedCount(Integer getEndrosedCount) {
		this.getEndrosedCount = getEndrosedCount;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getGetRenewalPublishCount() {
		return getRenewalPublishCount;
	}

	public void setGetRenewalPublishCount(String getRenewalPublishCount) {
		this.getRenewalPublishCount = getRenewalPublishCount;
	}

	public String getCheckOnlyRenewalEndrosed() {
		return checkOnlyRenewalEndrosed;
	}

	public void setCheckOnlyRenewalEndrosed(String checkOnlyRenewalEndrosed) {
		this.checkOnlyRenewalEndrosed = checkOnlyRenewalEndrosed;
	}

	public String getCheckRenEndrosAftFullTerm() {
		return checkRenEndrosAftFullTerm;
	}

	public void setCheckRenEndrosAftFullTerm(String checkRenEndrosAftFullTerm) {
		this.checkRenEndrosAftFullTerm = checkRenEndrosAftFullTerm;
	}

	public String getCheckAdminRenewalEndrosed() {
		return checkAdminRenewalEndrosed;
	}

	public void setCheckAdminRenewalEndrosed(String checkAdminRenewalEndrosed) {
		this.checkAdminRenewalEndrosed = checkAdminRenewalEndrosed;
	}

	public Integer getGetExactActiveStatus() {
		return getExactActiveStatus;
	}

	public void setGetExactActiveStatus(Integer getExactActiveStatus) {
		this.getExactActiveStatus = getExactActiveStatus;
	}

	public String getAuditDtlsAuditPlace() {
		return auditDtlsAuditPlace;
	}

	public void setAuditDtlsAuditPlace(String auditDtlsAuditPlace) {
		this.auditDtlsAuditPlace = auditDtlsAuditPlace;
	}

	public Integer getAuditDtlsGrt() {
		return auditDtlsGrt;
	}

	public void setAuditDtlsGrt(Integer auditDtlsGrt) {
		this.auditDtlsGrt = auditDtlsGrt;
	}

	public Date getAuditDtlsdateOfRegistry() {
		return auditDtlsdateOfRegistry;
	}

	public void setAuditDtlsdateOfRegistry(Date auditDtlsdateOfRegistry) {
		this.auditDtlsdateOfRegistry = auditDtlsdateOfRegistry;
	}

	public Integer getCertReissueReason() {
		return certReissueReason;
	}

	public void setCertReissueReason(Integer certReissueReason) {
		this.certReissueReason = certReissueReason;
	}

	public Date getAuditDtlsAuditDate() {
		return auditDtlsAuditDate;
	}

	public void setAuditDtlsAuditDate(Date auditDtlsAuditDate) {
		this.auditDtlsAuditDate = auditDtlsAuditDate;
	}

	public Integer getAuditSummaryId() {
		return auditSummaryId;
	}

	public void setAuditSummaryId(Integer auditSummaryId) {
		this.auditSummaryId = auditSummaryId;
	}

	public Integer getCheckExtensionCert() {
		return checkExtensionCert;
	}

	public void setCheckExtensionCert(Integer checkExtensionCert) {
		this.checkExtensionCert = checkExtensionCert;
	}

	public Integer getCheckExtensionInRenewal() {
		return checkExtensionInRenewal;
	}

	public void setCheckExtensionInRenewal(Integer checkExtensionInRenewal) {
		this.checkExtensionInRenewal = checkExtensionInRenewal;
	}

	public Integer getCheckAdminRenewalCert() {
		return checkAdminRenewalCert;
	}

	public void setCheckAdminRenewalCert(Integer checkAdminRenewalCert) {
		this.checkAdminRenewalCert = checkAdminRenewalCert;
	}

	

	public Date getCompletionSurveyDate() {
		return completionSurveyDate;
	}

	public void setCompletionSurveyDate(Date completionSurveyDate) {
		this.completionSurveyDate = completionSurveyDate;
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

	@Override
	public String toString() {
		return "CertificateDetail [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId="
				+ auditTypeId + ", seqNo=" + seqNo + ", certificateId=" + certificateId + ", endorsementID="
				+ endorsementID + ", auditSubTypeId=" + auditSubTypeId + ", auditDate=" + auditDate + ", auditPlace="
				+ auditPlace + ", auditReportNo=" + auditReportNo + ", certificateNo=" + certificateNo + ", utn=" + utn
				+ ", certIssueId=" + certIssueId + ", qrCodeUrl=" + qrCodeUrl + ", certificateVer=" + certificateVer
				+ ", certIssueDate=" + certIssueDate + ", certExpireDate=" + certExpireDate + ", extendedIssueDate="
				+ extendedIssueDate + ", extendedExpireDate=" + extendedExpireDate + ", endorsedDate=" + endorsedDate
				+ ", publishStatus=" + publishStatus + ", activeStatus=" + activeStatus + ", notes=" + notes
				+ ", leadName=" + leadName + ", issuerId=" + issuerId + ", issuerName=" + issuerName
				+ ", issuerSignDate=" + issuerSignDate + ", nameToPrint=" + nameToPrint + ", signToPrint=" + signToPrint
				+ ", verifyDone=" + verifyDone + ", vesselId=" + vesselId + ", vesselImoNo=" + vesselImoNo
				+ ", vesselName=" + vesselName + ", grt=" + grt + ", vesselType=" + vesselType + ", officialNo="
				+ officialNo + ", portOfRegistry=" + portOfRegistry + ", dateOfRegistry=" + dateOfRegistry
				+ ", companyImoNo=" + companyImoNo + ", vesselCompanyName=" + vesselCompanyName
				+ ", vesselCompanyAddress=" + vesselCompanyAddress + ", vesselUk=" + vesselUk + ", vesselPk=" + vesselPk
				+ ", classSociety=" + classSociety + ", callSign=" + callSign + ", docTypeNumber=" + docTypeNumber
				+ ", docTypeNo=" + docTypeNo + ", docIssuer=" + docIssuer + ", docExpiry=" + docExpiry + ", userIns="
				+ userIns + ", dateIns=" + dateIns + ", auditStatusId=" + auditStatusId + ", auditTypeDesc="
				+ auditTypeDesc + ", audSubTypeDesc=" + audSubTypeDesc + ", leadAuditorId=" + leadAuditorId
				+ ", nextAdtSeq=" + nextAdtSeq + ",certReissueReason=" + certReissueReason + ",certIssueDesc="
				+ certIssueDesc + ", leadAuditorSignDate=" + leadAuditorSignDate + ", lockStatus=" + lockStatus
				+ ", prevData=" + prevData + ", userInsName=" + userInsName + "]";
	}

	
	

}
