/**
 * 
 */
package com.api.central.util;

import java.sql.Date;

import javax.persistence.Column;

/**
 * @author mohanasundharam
 *
 */
public interface AppSQLConstant {
	public static final String DELETE_AUDIT_FINDING = "Delete from AuditFinding WHERE companyId = :companyId and auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo and findingSeqNo = :findingSeqNo ";
	public static final String DELETE_AUDIT_FINDING_DETAIL = "Delete from FindingDetail  WHERE  companyId = :companyId and auditTypeId = :auditTypeId and  origAuditSeqNo = :auditSeqNo and  findingSeqNo = :findingSeqNo";
	public static final String DELETE_AUDIT_FINDINDG_RPT_ATTACH = "delete from FindingRptAttach   where  companyId = :companyId and  auditTypeId = :auditTypeId and  origAuditSeqNo = :auditSeqNo and  findingSeqNo = :findingSeqNo";
	public static final String UPDATE_AUDIT_LOCK_STATUS = "update AuditDetail set lockStatus = :lockStatus where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo";
	public static final String UPDATE_AUDIT_LOCK_HOLDER = "update AuditDetail set lockHolder = :lockHolder where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo";
	public static final String UNLOCK_ALL_LOCKED_AUDIT_OF_CURR_USER = "update AuditDetail set lockHolder = :noLockHolder where  companyId = :companyId and  lockHolder = :currLockHolder and lockStatus<> :lockStatus";
	public static final String GET_PREV_DOC_DETAILS = "SELECT seqNo, companyImoNo, companyId, docTypeNo, docIssuer, docExpiry FROM DOCNumberChange where SEQ_NO = ( select max(seqNo)  from DOCNumberChange where companyImoNo = :companyImoNo and docTypeNo = :docTypeNo and companyId=:companyId)";
	public static final String UPDATE_AUDIT_DOC_FLAG =  "update AuditDetail set docFlag = :docFlag where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo";
	public static final String UPDATE_AUDIT_DTL_FROM_CERTIFICATE_DTL = "update AuditDetail set certificateNo=:certificateNo, certIssueDate=:certIssueDate, certExpireDate=:certExpireDate,certIssueId = :certIssueId where  companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo";
	public static final String UPDATE_AUDIT_DTL_FROM_CERTIFICATE_DTL_IHM = "update AuditDetailIhm set certificateNo=:certificateNo where  companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo";
	public static final String UPDATE_VESSEL_GRT_FROM_CERTIFICATE_DTL = "update MaVessel set grt = :grt where vesselImoNo = :vesselImoNo and companyId = :companyId";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL = "update CertificateDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo and seqNo <> :seqNo";
	
	public static final String MAKE_INACTIVE_RMI_CERTIFICATE = "update  RmiCertificates set certificateStatus = :activeStatus where auditID = :audit_id and vesselIMONumber = :vesselImo and certificateID < :certificateId";
	
	public static final String GET_CERTIFICATE_ID_FOR_RMI_IHM = " select min(certificateId) from CertificateIhmDetail where vesselImoNo = :vessel_imo_no and auditSubTypeId = :auditSubTypeId and \r\n" + 
			" seqNo = ( select min(seqNo) from CertificateIhmDetail where vesselImoNo = :vessel_imo_no and auditSubTypeId = :auditSubTypeId and \r\n" + 
			" certificateLink = (select max(certificateLink) from CertificateIhmDetail where vesselImoNo = :vessel_imo_no and auditSubTypeId = :auditSubTypeId and  auditSeqNo not in (select auditSeqNo from AuditDetailIhm where  vesselImoNo = :vessel_imo_no and auditSubTypeId = :auditSubTypeId and auditTypeId = :auditTypeId and auditStatusId = 1004 )) ) \r\n" ; 

	
	public static final String GET_MIN_CERTIFICATE_ID_FOR_RMI_IHM = "select min(certificateId) from CertificateIhmDetail where vesselImoNo = :vessel_imo_no and certIssueId =:certIssueId and auditSeqNo = :auditSeqNo and socType in (:param1,:param2) and certificateLink = (select max(certificateLink) from CertificateIhmDetail where vesselImoNo = :vessel_imo_no and auditSubTypeId = :auditSubTypeId and auditSeqNo = :auditSeqNo and socType in (:param1,:param2) )";
	public static final String UPDATE_CERT_STATUS_FOR_RMI_IHM = "update RmiIhmCertificates set certificateStatus = :certificateStatus where reviewSeqNo = :reviewSeqNo and vesselID =:vesselID and certificateID < :certificateID";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL_IHM_SOC = "update CertificateIhmDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo != :auditSeqNo and certIssueId <> :certIssueId and vesselImoNo =:vesselImoNo and socType in ('hk','eu')";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL_IHM_EXE = "update CertificateIhmDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo != :auditSeqNo and certIssueId <> :certIssueId and vesselImoNo =:vesselImoNo and socType in ('exe')";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_SOC = "update CertificateIhmDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo != :auditSeqNo and certificateLink != :certificateLink and vesselImoNo =:vesselImoNo and socType in ('hk','eu')";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_EXE = "update CertificateIhmDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo != :auditSeqNo and certificateLink != :certificateLink and vesselImoNo =:vesselImoNo and socType in ('exe')";
	public static final String REMOVE_ALL_AUDITOR_SIGN = "update AuditAuditorDetail set audSignature = null, audSignatureDate=null,delegateSign=0 where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo";
	public static final String UPDATE_AUDIT_LOCK_AND_REVIEW_STATUS = "update AuditDetail set lockStatus = :lockStatus, reviewStatus = :reviewStatus where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo";
	public static final String UPDATE_LETTER_fLAG_STATUS = "update SspReviewDetail set ltrStatus = :ltrStatus where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo";
	public static final String GET_MAX_CERT_SEQ = "SELECT MAX(seqNo) FROM CertificateDetail WHERE auditSeqNo = :auditSeqNo and companyId = :companyId and  auditTypeId = :auditTypeId";
	public static final String GET_MAX_CERT_SEQ_IHM = "SELECT MAX(seqNo) FROM CertificateIhmDetail WHERE auditSeqNo = :auditSeqNo and companyId = :companyId and  auditTypeId = :auditTypeId and socType in( :parm1,:parm2)";
	public static final String GET_MAX_CERT_SEQ_IHM_NON_FULL = "SELECT MAX(certificateLink) FROM CertificateIhmDetail WHERE  companyId = :companyId and  auditTypeId = :auditTypeId and vesselImoNo =:vesselImoNo ";
	public static final String UPDATE_PUBLISH_STATUS = "update CertificateDetail set publishStatus = :publishStatus WHERE auditSeqNo = :auditSeqNo and companyId = :companyId and  seqNo = :seqNo";
	public static final String MAKE_INACTIVE_CERTIFICATE_EXTN = "update CertificateDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo and seqNo <> :seqNo and certIssueId in (1003,1006)";
	public static final String GET_MAX_VERSION_NO = "SELECT MAX(versionId) FROM ReportData WHERE auditSeqNo = :auditSeqNo and companyId = :companyId and  auditTypeId = :auditTypeId";
	public static final String MAKE_INACTIVE_CERTIFICATE_WITHOUT_AUDIT_DTL = "update CertificateDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo and vesselImoNo = :vesselImoNo and seqNo <> :seqNo";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL_WHEN_INTERORADITIONAL = "update CertificateDetail set activeStatus = :activeStatus where certIssueId <> :certIssueId and companyId = :companyId and auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo  and seqNo <> :seqNo";
	public static final String CERTIFICATE_ORDER_NO = "SELECT CERTIFICATE_ORDER_NO.nextval from dual";
	public static final String DELETE_CERTIFICATES_IHM = "Delete from CertificateIhmDetail WHERE certIssueId = :certIssueId and companyId = :companyId and vesselImoNo = :vesselImo and certificateId in (:certificateId)";
	public static final String DELETE_CERTIFICATES_IHM_REISSUE = "Delete from CertificateIhmDetail WHERE certIssueId = :certIssueId and companyId = :companyId and vesselImoNo = :vesselImo and auditSubTypeId = :auditSubTypeId and auditSeqNo = :auditSeqNo and publishStatus = :publishStatus and socType in (:param1 , :param2)";
	
	public static final String DELETE_CERTIFICATES_IHM_AMEND_REISSUE = "Delete from CertificateIhmDetail WHERE certIssueId = :certIssueId and companyId = :companyId and vesselImoNo = :vesselImo AND certificateNo = :certificateNo";
	
	public static final String UPDATE_CERTIFICATES_PUBLISH_STATUS_IHM = "update  CertificateIhmDetail set publishStatus = 1 WHERE certIssueId = :certIssueId and companyId = :companyId and vesselImoNo = :vesselImo and auditSubTypeId = :auditSubTypeId and auditSeqNo = :auditSeqNo";

	
	
	public static final String DELETE_AUDIT_FINDING_DETAIL_DMLC_LINK      = "Delete from FindingDetail  WHERE  companyId = :companyId and auditTypeId = :auditTypeId and  currentAuditSeq = :currentAuditSeq  ";
	public static final String DELETE_AUDIT_FINDINDG_RPT_ATTACH_DMLC_LINK = "Delete from FindingRptAttach   WHERE  companyId = :companyId and  auditTypeId = :auditTypeId and  currentAuditSeq = :currentAuditSeq ";
	
	public static final String UPDATE_ISSUE_EXPIRY_DRCTINTERADD = "update CertificateDetail set certExpireDate = :certExpireDate , certIssueDate=:certIssueDate where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo and certIssueId=:certIssueId";
	public static final String hmReasonCertificateInActive ="update CertificateDetail set activeStatus = :activeStatus where  companyId = :companyId  and auditSeqNo = :auditSeqNo";
	
	public static final String hmReasonCertificateActive ="update CertificateDetail set activeStatus = :activeStatus where  companyId = :companyId  and auditSeqNo = :auditSeqNo and seqNo=(select max(seqNo) from CertificateDetail where companyId = :companyId1  and auditSeqNo = :auditSeqNo1 )";
	public static final String UPDATE_SURVEY_DATE = null;
	
	public static final String UPDATE_VESSEL_REFRESH =  "update VesselRefresh set vesselID = :vesselID, vesselName = :vesselName, officialNumber = :officialNumber, vesselPK = :vesselPK, vesselUK = :vesselUK, callSign = :callSign, grossTon = :grossTon, vesselType = :vesselType, tcApprovalStatus = :tcApprovalStatus, homePort = :homePort, registrationDate = :registrationDate, vesselStatus = :vesselStatus, classSociety = :classSociety, companyIMONumber = :companyIMONumber, companyStatus = :companyStatus, docTypeNumber = :docTypeNumber, issueDate = :issueDate, docExpiry = :docExpiry, docType = :docType, customerName = :customerName, companyAddress = :companyAddress, docIssuer = :docIssuer, keelLaidDate = :keelLaidDate, regOwnedImoNo = :regOwnedImoNo, operationCode = :operationCode, registeredCompanyAddress = :registeredCompanyAddress, registeredCompanyName = :registeredCompanyName where  imoNumber = :imoNumber";
	
	public static final String GET_TABLE_UPDATION_USERS ="select tableUpdation from MasterTableUpdate where companyId = :companyId and table_name='users'";
	public static final String UPDATE_MASTER_TABLE_UPDATE_USERS ="update MasterTableUpdate set tableUpdation=:table_updation where companyId = :companyId and table_name='users'";
	//Added by sudharsan and Chibi for JIRA-ID=5377&5378
	public static final String UPDATE_IHM_CERTIFICATE_VOID_STATUS = "update CertificateIhmDetail set activeStatus= :status where auditSeqNo= :audit_seq_no and auditSubTypeId= :sub_type_id and certificateId in (:certificate_Id) ";
	public static final String UPDATE_VOID_CERT_STATUS_FOR_RMI_IHM = "update RmiIhmCertificates set certificateStatus = :certificateStatus where reviewSeqNo = :reviewSeqNo and vesselID =:vesselID and certificateID in (:certificateID) and socType in (:socType)";
	public static final String UPDATE_CERT_STATUS_FOR_RMI_IHM_II = "update RmiIhmCertificates set certificateStatus = :certificateStatus where reviewSeqNo = :reviewSeqNo and vesselID =:vesselID and certificateID not in (:certificateID) and socType in (:socType)";
	public static final String MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_EXE_II = "update CertificateIhmDetail set activeStatus = :activeStatus where companyId = :companyId and  auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo and certificateLink != :certificateLink and vesselImoNo =:vesselImoNo and socType in ('exe')";
	
	public static final String UPDATE_SMC_VOID_STATUS_CERTIFICATE_RMI_EXTENSION1 ="update RmiCertificates set certificateStatus = :certificateStatus where auditID = :auditID";
	public static final String UPDATE_SMC_VOID_STATUS_CERTIFICATE_RMI_EXTENSION2="update RmiCertificates set certificateStatus = :certificateStatus where certificateNumber = :certificateNumber";
	public static final String GET_AUDIT_SEQ_NO_CERTIFICATE_DETAIL ="select auditSeqNo from CertificateDetail where auditTypeId = :auditTypeId and vesselImoNo = :vesselImoNo";
	public static final String UPDATE_SMC_INACTIVE_CERTIFICATE_RMI_NXT_AUDIT="update RmiCertificates set certificateStatus = :certificateStatus1 where auditID in (:auditID) and certificateStatus = :certificateStatus2  and auditID <> :auditSeqNo";
	//End here
	//public static final String UPDATE_VOID_CERT_STATUS_FOR_RMI_SMC = "update RmiCertificates set certificateStatus = :certificateStatus where auditID in (:auditID) and certificateID = :certificateID";
	public static final String GET_VESSEL_COMPANY_DETAILS="from MaVesselCompany where docIssuer = :docIssuer";
	public static final String UPDATE_AUDIT_STATUS_ID = "update AuditDetail set auditStatusId = :auditStatusId where  companyId = :companyId and  auditTypeId = :auditTypeId and  auditSeqNo = :auditSeqNo";		//added by @Ramya for Ticket-659
	
	/*added by @Ramya for Jira id - IRI-5633 - START */
	public static final String INSERT_FINDINGS = "insert into Audit_Findings values (:findingSeqNo,TO_DATE(:auditDate,'yyyy-MM-dd'),:auditCode,:companyId,:userIns,CURRENT_DATE,:auditSeqNo,:auditTypeId,:findingStatus,:serialNo,:auditStatus)";
	public static final String INSERT_FINDING_DETAILS = "INSERT INTO AUDIT_FINDINGS_DETAILS VALUES (:statusSeqNo,:currentAuditSeq,:origAuditSeqNo,:findingSeqNo,:findingCategoryId,:statusId,TO_DATE(:statusDate,'yyyy-MM-dd'),:nextActionId,:dueDate,:description,:companyId,:userIns,CURRENT_DATE,:auditTypeId,:updateDescription,:auditPlace)";
	public static final String INSERT_FINDING_RPT_DETAILS = "INSERT INTO AUDIT_FINDING_ATTACHMENTS VALUES (:currentAuditSeq,:origAuditSeqNo,:findingSeqNo,:fileSeqNo,:fileName,:flag,:companyId,:userIns,CURRENT_DATE,:statusSeqNo,:auditTypeId)";
	public static final String UPDATE_FINDINGS ="update Audit_findings set AUDIT_DATE = TO_DATE(:auditDate,'yyyy-MM-dd') , AUDIT_CODE = :auditCode,COMPANY_ID = :companyId, USER_INS =:userIns,DATE_INS = CURRENT_DATE, AUDIT_TYPE_ID = :auditTypeId, FINDING_STATUS = :findingStatus, SERIAL_NO = :serialNo, AUDIT_STATUS = :auditStatus where AUDIT_SEQ_NO = :auditSeqNo and FINDING_SEQ_NO = :findingSeqNo";
	public static final String UPDATE_FINDING_DETAILS ="update AUDIT_FINDINGS_DETAILS set CUR_AUDIT_SEQ_NO=:currentAuditSeq,FINDING_CATEGORY_ID=:findingCategoryId,STATUS_ID=:statusId,STATUS_DATE=TO_DATE(:statusDate,'yyyy-MM-dd'),NEXT_ACTION_ID	=:nextActionId,DUE_DATE=:dueDate,DESCRIPTION=:description,COMPANY_ID=:companyId,USER_INS=:userIns,DATE_INS=CURRENT_DATE,AUDIT_TYPE_ID=:auditTypeId,UPDATE_DESCRIPTION=:updateDescription,AUDIT_PLACE=:auditPlace where STATUS_SEQ_NO = :statusSeqNo and FINDING_SEQ_NO=:findingSeqNo and ORIG_SEQ_NO=:origAuditSeqNo ";
	public static final String UPDATE_FINDING_RPT_DETAILS = "update AUDIT_FINDING_ATTACHMENTS set CUR_AUDIT_SEQ_NO=:currentAuditSeq, FLAG=:flag, COMPANY_ID=:companyId,USER_INS=:userIns,DATE_INS=CURRENT_DATE,AUDIT_TYPE_ID=:auditTypeId where ORIG_SEQ_NO=:origAuditSeqNo and FINDING_SEQ_NO=:findingSeqNo and FILE_SEQ_NO=:fileSeqNo and STATUS_SEQ_NO=:statusSeqNo and FILE_NAME=:fileName";	
	public static final String FINDING_DETAIL_EXISTS = "select count(*) from audit_findings_details where STATUS_SEQ_NO = :statusSeqNo and FINDING_SEQ_NO=:findingSeqNo and ORIG_SEQ_NO=:origAuditSeqNo";
	public static final String FINDING_RPT_ATTACHEMENT_EXISTS ="select count(*) from audit_finding_attachments where ORIG_SEQ_NO=:origAuditSeqNo and FINDING_SEQ_NO=:findingSeqNo and FILE_SEQ_NO=:fileSeqNo and STATUS_SEQ_NO=:statusSeqNo";
	/*added by @Ramya for Jira id - IRI-5633 - END */
}
