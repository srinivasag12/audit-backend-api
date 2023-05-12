package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.api.central.util.AppConstant;

@Entity
@Table(name="AUDIT_DETAILS_VIEW")
@IdClass(AuditDetailViewCPK.class)
public class AuditDetailView implements Serializable{
	
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
	
 	
	@Range(min=7,max=999999999,message="Vessel Name cannot be empty")
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
 	@Column(name="AUDIT_SUB_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Sub Type cannot be empty")
 	private Integer auditSubTypeId;
	
	@Column(name="CERTIFICATE_NO")
	@NotEmpty(message="Certificate No cannot be empty")
	private String certificateNo;
	
	
	@Column(name="LETTER_NO")
	@NotEmpty(message="Letter No cannot be empty")
	private String letterNo;
	
	@Column(name="AUDIT_REPORT_NO")
	private String auditReportNo;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@NotNull 
	@Column(name="AUDIT_DATE")
	private Date auditDate;
		
	@Column(name="COMPANY_IMO_NO")
	@NotEmpty(message="Company IMO cannot be empty")
	private String companyImoNo;
	
	@Column(name="COMPANY_DOC")
	private String companyDoc;
		
	@Column(name="AUDIT_STATUS_ID")	
	@Range(min=0,max=1000000,message="Audit Status cannot be empty")
	private Integer auditStatusId;
	
	@Column(name="CERTIFICATE_ISSUE_ID")
	@Range(min=1000,max=10000,message="Certificate Issued cannot be empty")
 	private Integer certIssueId;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CERT_EXPIRY_DATE")
	private Date certExpireDate;
	
	@Transient
	private boolean IsSignAttached;
	 
	@Column(name="ADDITIONAL_SURVEY")
	private String additionalSurvey;
	


	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CERT_ISSUED_DATE")
	private Date certIssueDate;
	 
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="AUD_LEAD_STATUS")
	private Integer audLeadStatus;
	
	@Column(name="REVIEW_STATUS")
	private Integer reviewStatus;
	
	@Column(name="LOCK_STATUS")
	private Integer lockStatus;
	
	@Column(name="ALLOW_NEXT")
	private Integer allowNext;
	
	@Column(name="PUBLISH_STATUS")
	private Integer publishStatus;
	
	@Column(name="IS_AUDIT_LOCKED_BY_MANAGER")
	private Integer isAuditLockedByManager;
	
	@Column(name="SCOPE")
	private Integer scope;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CREDIT_DATE")
	private Date creditDate;
	
	@Column(name="OFFICIAL_NO")
	private Long officialNo;
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	@Formula("(SELECT A.CERTIFICATE_ISSUE_DESC FROM  MA_CERTIFICATE_ISSUED A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.CERTIFICATE_ISSUE_ID = CERTIFICATE_ISSUE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audCertIssueDesc;
	
	@Formula("(SELECT A.AUDIT_STATUS_DESC FROM  MA_AUDIT_STATUS A  WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_STATUS_ID = AUDIT_STATUS_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditStatusDesc;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditTypeDesc;
	
	@Formula("(SELECT A.VESSEL_NAME FROM MA_VESSEL A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID=COMPANY_ID)")
	private String vesselName;
	
	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM MA_USERS A WHERE A.USER_ID=(SELECT B.USER_ID FROM AUDIT_AUDITOR_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.AUD_LEAD_STATUS = 1)  AND A.COMPANY_ID = COMPANY_ID)")
	private String auditorName;
	
	@Formula("(SELECT A.AUDIT_ROLE_ID FROM AUDIT_AUDITOR_DETAILS A WHERE A.USER_ID=USER_ID AND A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer auditorRoleId;
	
	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM MA_USERS A WHERE A.USER_ID=(SELECT B.LOCK_HOLDER FROM AUDIT_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.COMPANY_ID = COMPANY_ID )  AND A.COMPANY_ID = COMPANY_ID)")
	private String lockHolder;
	
	@Formula("(SELECT A.LOCK_HOLDER FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private String lockHolderId;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Formula("(SELECT A.ENDORSE_EXPIRY_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Date endorseExpireDate;
	
	
	@Formula("(SELECT A.INTERNAL_AUDIT_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String interalAuditDate;
	
	@LongDateFormat
	@Formula("(SELECT A.OPEN_MEETING_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Timestamp openMeetingDate;
	
	@Formula("(SELECT A.DOC_FLAG FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Integer docFlag;
	
	@Formula("(SELECT A.UTN FROM CERTIFICATE_DETAIL A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM CERTIFICATE_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.COMPANY_ID = COMPANY_ID))")
	private String utn;
	
	@Formula("(SELECT A.DOC_TYPE_NUMBER FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private String docTypeNumber;
	
	@Formula("(SELECT A.MAKE_FINAL FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID)")
	private Integer makeFinal;
	
	@Formula("(SELECT A.AUD_SIGNATURE FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_ROLE_ID="+AppConstant.AUDIT_REVIEWER_ROLE_ID+")")
	private byte[] reviewerSign;
	
	@Formula("(SELECT A.SSP_DMLC_AUDIT_SEQ_NO FROM SSP_REVIEW_DATA A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID)")
	private Integer sspDmlcAuditSeqNo;
	
	@Formula("(SELECT A.SSP_REVISION_NO FROM SSP_REVIEW_DATA A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID)")
	private String sspRevisionNo;
	
	@Formula("(SELECT A.LTRSTATUS FROM SSP_REVIEW_DATA A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID)")
	private String sspLtrStatus;
	

	@Formula("(SELECT A.AUD_SIGNATURE FROM AUDIT_AUDITOR_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.COMPANY_ID = COMPANY_ID AND A.AUD_LEAD_STATUS="+AppConstant.ACCEPT_STATUS+")")
	private byte[] leadSign;
	
	@Formula("(SELECT A.AUDIT_SUMMARY_ID FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Integer auditSummaryId;
	
	@LongDateFormat
	@Formula("(SELECT A.CLOSE_MEETING_DATE FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private Timestamp closeMeetingDate;
	
	@Formula("(SELECT A.VESSEL_NAME FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private String vesselNameAud;
	
	@Fetch(FetchMode.SUBSELECT)
  	@OneToMany(targetEntity = CertificateDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false)
  	@OrderBy(clause = "SEQ_NO DESC")
  	private List<CertificateDetail> certificateDetail = new ArrayList<CertificateDetail>();
	
	@Fetch(FetchMode.SUBSELECT)
  	@OneToMany(targetEntity = CertificateIhmDetail.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditDetail", orphanRemoval = false)
  	@OrderBy(clause = "SEQ_NO DESC")
  	private List<CertificateIhmDetail> certificateIhmDetail = new ArrayList<CertificateIhmDetail>();
	
	public List<CertificateIhmDetail> getCertificateIhmDetail() {
		return certificateIhmDetail;
	}

	public void setCertificateIhmDetail(List<CertificateIhmDetail> certificateIhmDetail) {
		this.certificateIhmDetail = certificateIhmDetail;
	}

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public String getUtn() {
		return utn;
	}

	public void setUtn(String utn) {
		this.utn = utn;
	}

	public Integer getAuditorRoleId() {
		return auditorRoleId;
	}

	public void setAuditorRoleId(Integer auditorRoleId) {
		this.auditorRoleId = auditorRoleId;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
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

	public String getAdditionalSurvey() {
		return additionalSurvey;
	}

	public void setAdditionalSurvey(String additionalSurvey) {
		this.additionalSurvey = additionalSurvey;
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

	public String getAuditTypeDesc() {
		return auditTypeDesc;
	}

	public void setAuditTypeDesc(String auditTypeDesc) {
		this.auditTypeDesc = auditTypeDesc;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	
	public Integer getAudLeadStatus() {
		return audLeadStatus;
	}

	public void setAudLeadStatus(Integer audLeadStatus) {
		this.audLeadStatus = audLeadStatus;
	}
	
	public String getLockHolder() {
		return lockHolder;
	}

	public void setLockHolder(String lockHolder) {
		this.lockHolder = lockHolder;
	}

	public Integer getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Integer getAllowNext() {
		return allowNext;
	}

	public void setAllowNext(Integer allowNext) {
		this.allowNext = allowNext;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	
	public Integer getIsAuditLockedByManager() {
		return isAuditLockedByManager;
	}

	public void setIsAuditLockedByManager(Integer isAuditLockedByManager) {
		this.isAuditLockedByManager = isAuditLockedByManager;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public String getAuditReportNo() {
		return auditReportNo;
	}

	public void setAuditReportNo(String auditReportNo) {
		this.auditReportNo = auditReportNo;
	}

	public String getLockHolderId() {
		return lockHolderId;
	}

	public void setLockHolderId(String lockHolderId) {
		this.lockHolderId = lockHolderId;
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

	public String getInteralAuditDate() {
		return interalAuditDate;
	}

	public void setInteralAuditDate(String interalAuditDate) {
		this.interalAuditDate = interalAuditDate;
	}

	public byte[] getReviewerSign() {
		return reviewerSign;
	}

	public void setReviewerSign(byte[] reviewerSign) {
		this.reviewerSign = reviewerSign;
	}

	public String getSspRevisionNo() {
		return sspRevisionNo;
	}

	public void setSspRevisionNo(String sspRevisionNo) {
		this.sspRevisionNo = sspRevisionNo;
	}

	public String getSspLtrStatus() {
		return sspLtrStatus;
	}

	public void setSspLtrStatus(String sspLtrStatus) {
		this.sspLtrStatus = sspLtrStatus;
	}

	
	
	public Timestamp getOpenMeetingDate() {
		return openMeetingDate;
	}

	public void setOpenMeetingDate(Timestamp openMeetingDate) {
		this.openMeetingDate = openMeetingDate;
	}
	
	
	public byte[] getLeadSign() {
		return leadSign;
	}

	public void setLeadSign(byte[] leadSign) {
		this.leadSign = leadSign;
	}

	public List<CertificateDetail> getCertificateDetail() {
		return certificateDetail;
	}

	public void setCertificateDetail(List<CertificateDetail> certificateDetail) {
		this.certificateDetail = certificateDetail;
	}

	public Integer getAuditSummaryId() {
		return auditSummaryId;
	}

	public void setAuditSummaryId(Integer auditSummaryId) {
		this.auditSummaryId = auditSummaryId;
	}
	
	public Date getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}

	public Integer getSspDmlcAuditSeqNo() {
		return sspDmlcAuditSeqNo;
	}

	public void setSspDmlcAuditSeqNo(Integer sspDmlcAuditSeqNo) {
		this.sspDmlcAuditSeqNo = sspDmlcAuditSeqNo;
	}

	public Timestamp getCloseMeetingDate() {
		return closeMeetingDate;
	}

	public void setCloseMeetingDate(Timestamp closeMeetingDate) {
		this.closeMeetingDate = closeMeetingDate;
	}
	
	public Long getOfficialNo() { 
		return officialNo;
	}

	public void setOfficialNo(Long officialNo) {
		this.officialNo = officialNo;
	}

	public AuditDetailView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMakeFinal() {
		return makeFinal;
	}

	public void setMakeFinal(Integer makeFinal) {
		this.makeFinal = makeFinal;
	}

	public static String dateFormat(java.util.Date date){
		
		SimpleDateFormat toFormat = new SimpleDateFormat("MMM-dd-yyyy");
		
		return toFormat.format(date);
	}
	
	
	
	public String getVesselNameAud() {
		return vesselNameAud;
	}

	public void setVesselNameAud(String vesselNameAud) {
		this.vesselNameAud = vesselNameAud;
	}
	public boolean getIsSignAttached() {
		return IsSignAttached;
	}

	public void setIsSignAttached(boolean isSignAttached) {
		IsSignAttached = isSignAttached;
	}

	public String toJson() {
		String result="";
		
		result = "{\"" + (auditSeqNo != null ? "auditSeqNo\" : \"" + auditSeqNo + "\", " : "")
				+ (companyId != null ? "\"companyId\" : \"" + companyId + "\", " : "")
				+ (auditTypeId != null ? "\"auditTypeId\" : \"" + auditTypeId + "\", " : "")
				+ (vesselImoNo != null ? "\"vesselImoNo\" : \"" + vesselImoNo + "\", " : "")
				+ (auditSubTypeId != null ? "\"auditSubTypeId\" : \"" + auditSubTypeId + "\", " : "")
				+ (certificateNo != null ? "\"certificateNo\" : \"" + certificateNo + "\", " : "")
				+ (auditReportNo != null ? "\"auditReportNo\" : \"" + auditReportNo + "\", " : "")
				+ (auditDate != null ? "\"auditDate\" : \"" + dateFormat(auditDate) + "\", " : "")
				+ (companyImoNo != null ? "\"companyImoNo\" : \"" + companyImoNo + "\", " : "")
				+ (companyDoc != null ? "\"companyDoc\" : \"" + companyDoc + "\", " : "")
				+ (auditStatusId != null ? "\"auditStatusId\" : \"" + auditStatusId + "\", " : "")
				+ (certIssueId != null ? "\"certIssueId\" : \"" + certIssueId + "\", " : "")
				+ (certExpireDate != null ? "\"certExpireDate\" : \"" + dateFormat(certExpireDate) + "\", " : "")
				+ (certIssueDate != null ? "\"certIssueDate\" : \"" + dateFormat(certIssueDate) + "\", " : "")
				+ (userId != null ? "\"userId\" : \"" + userId + "\", " : "")
				+ (audLeadStatus != null ? "\"audLeadStatus\" : \"" + audLeadStatus + "\", " : "")
				+ (reviewStatus != null ? "\"reviewStatus\" : \"" + reviewStatus + "\", " : "")
				+ (isAuditLockedByManager != null ? "\"isAuditLockedByManager\" : \"" + isAuditLockedByManager + "\", " : "")
				+ (lockStatus != null ? "\"lockStatus\" : \"" + lockStatus + "\", " : "")
				+ (audSubTypeDesc != null ? "\"audSubTypeDesc\" : \"" + audSubTypeDesc + "\", " : "")
				+ (additionalSurvey != null ? "\"additionalSurvey\" : \"" + additionalSurvey + "\", " : "")
				+ (audCertIssueDesc != null ? "\"audCertIssueDesc\" : \"" + audCertIssueDesc + "\", " : "")
				+ (auditStatusDesc != null ? "\"auditStatusDesc\" : \"" + auditStatusDesc + "\", " : "")
				+ (auditTypeDesc != null ? "\"auditTypeDesc\" : \"" + auditTypeDesc + "\", " : "")
				+ (vesselName != null ? "\"vesselName\" : \"" + vesselName + "\", " : "")
				+ (auditorName != null ? "\"auditorName\" : \"" + auditorName + "\", " : "")
				+ (auditorRoleId != null ? "\"auditorRoleId\" : \"" + auditorRoleId + "\", " : "")
				+ (lockHolder != null ? "\"lockHolder\" : \"" + lockHolder + "\", " : "")
				+ (lockHolderId != null ? "\"lockHolderId\" : \"" + lockHolderId + "\", " : "")
				+ (endorseExpireDate != null ? "\"endorseExpireDate\" : \"" + dateFormat(endorseExpireDate) : "");
		
		if(result.lastIndexOf(',')== result.length()-2 ){
			result = result.substring(0, result.length()-2)+" }";
			return result;
		}
		result = result + "}";
		return result;
	}

	@Override
	public String toString() {
		return "AuditDetailView [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId=" + auditTypeId
				+ ", vesselImoNo=" + vesselImoNo + ", auditSubTypeId=" + auditSubTypeId + ", certificateNo="
				+ certificateNo + ", letterNo=" + letterNo + ", auditReportNo=" + auditReportNo + ", auditDate="
				+ auditDate + ", companyImoNo=" + companyImoNo + ", companyDoc=" + companyDoc + ", auditStatusId="
				+ auditStatusId + ", certIssueId=" + certIssueId + ", certExpireDate=" + certExpireDate
				+ ", IsSignAttached=" + IsSignAttached + ", additionalSurvey=" + additionalSurvey + ", certIssueDate="
				+ certIssueDate + ", userId=" + userId + ", audLeadStatus=" + audLeadStatus + ", reviewStatus="
				+ reviewStatus + ", lockStatus=" + lockStatus + ", allowNext=" + allowNext + ", publishStatus="
				+ publishStatus + ", isAuditLockedByManager=" + isAuditLockedByManager + ", scope=" + scope + ", creditDate="
				+ creditDate + ", officialNo=" + officialNo + ", audSubTypeDesc=" + audSubTypeDesc
				+ ", audCertIssueDesc=" + audCertIssueDesc + ", auditStatusDesc=" + auditStatusDesc + ", auditTypeDesc="
				+ auditTypeDesc + ", vesselName=" + vesselName + ", auditorName=" + auditorName + ", auditorRoleId="
				+ auditorRoleId + ", lockHolder=" + lockHolder + ", lockHolderId=" + lockHolderId
				+ ", endorseExpireDate=" + endorseExpireDate + ", interalAuditDate=" + interalAuditDate
				+ ", openMeetingDate=" + openMeetingDate + ", docFlag=" + docFlag + ", utn=" + utn + ", docTypeNumber="
				+ docTypeNumber + ", makeFinal=" + makeFinal + ", reviewerSign=" + Arrays.toString(reviewerSign)
				+ ", sspDmlcAuditSeqNo=" + sspDmlcAuditSeqNo + ", sspRevisionNo=" + sspRevisionNo + ", sspLtrStatus="
				+ sspLtrStatus + ", leadSign=" + Arrays.toString(leadSign) + ", auditSummaryId=" + auditSummaryId
				+ ", closeMeetingDate=" + closeMeetingDate + ", vesselNameAud=" + vesselNameAud + ", certificateDetail="
				+ certificateDetail + ", certificateIhmDetail=" + certificateIhmDetail + "]";
	}
	
}

