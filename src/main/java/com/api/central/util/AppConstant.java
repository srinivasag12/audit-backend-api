/**
 * 
 */
package com.api.central.util;

/**
 * @author mohanasundharam
 *
 */
public interface AppConstant {
	   public static final String CREATE = "Create";
	   public static final String NEW = "NEW";
	   public static final String ENDORSE = "ENDORSE";
	   public static final String UPDATE = "Update";
 	   public static final String NEW_FINDING = "NF";
 	   public static final String PREVIOUS_FINDING = "PF";
 	   public static final String PREVIOUS_FINDING_MOB = "pf";
 	   public static final int FINDING_STATUS = 0;
 	   public static final String AUDIT_DETAIL = "PAD";
 	   public static final String AUDIT_DETAIL_MOB = "pad";
 	   public static final int AUDIT_STATUS = 1002;
  	   public static final String ISM_SRC =  "ISM";
  	   public static final String ISPS_SRC =  "ISPS";
  	   public static final String MLC_SRC =  "MLC";
  	   public static final String DMLC_SRC =  "DMLC";
  	   public static final String IHM_SRC =  "IHM";
  	   public static final int ISM_TYPE_ID =  1001;
	   public static final int ISPS_TYPE_Id = 1002;
	   public static final int MLC_TYPE_ID =  1003;
	   public static final int SSP_TYPE_ID =  1004;
	   public static final int DMLC_TYPE_ID =  1005;
	   public static final int IHM_TYPE_ID =  1006;
	   public static final int SOPEP_TYPE_ID  = 1007;
	   public static final int STS_TYPE_ID  = 1008;
	   public static final int SMPEP_TYPE_ID  = 1009;
	   public static final int BWS_TYPE_ID  = 1010;
	   public static final int VOC_TYPE_ID  = 1011;
	   public static final int SDR_TYPE_ID  = 1012;   // Changed by Sudharsan (SWR to SDR)
	   public static final int COW_TYPE_ID  = 1013;   // Added by Kiran
  	   public static final String SEPARATOR =  "/";
  	   public static final String ZIP =  ".zip";
  	   public static final String JSON =  ".json";
  	   public final static String TYPE_ZIP_VALUE="application/zip";
  	   public static final int CLOSE_FINDING_STATUS_ID = 1005;
  	   public static final int COMMENCED_AUDIT_STATUS = 1001;
  	   public static final int COMPLETED_AUDIT_STATUS = 1002;
  	   public static final int VOID_AUDIT_STATUS = 1004;
  	   public static final int CLOSED_AUDIT_STATUS = 1003;
  	   public static final int REOPEN_AUDIT_STATUS = 1005;
  	   public static final int AUD_LEAD_STATUS = 1;
  	   public static final int AUDITOR_ROLE_ID = 1001;
	   public static final int ADMIN_ROLE_ID = 1002;
	   public static final int MANAGER_ROLE_ID = 1003;
	   public static final int OBSERVER_ROLE_ID = 1004;
	   public static final int IHM_MANAGER = 1006;
	   public static final int AUDIT_AUDITOR_ROLE_ID = 1001;
	   public static final int AUDIT_OBSERVER_ROLE_ID = 1002;
	   public static final int AUDIT_REVIEWER_ROLE_ID = 1003;
	   public static final int INITIATE_REVIEW_STATUS = 1;
	   public static final int REVERT_REVIEW_STATUS = 0;
	   public static final int SSP_INITIAL_AUD_SUBTYPEID = 1001;
	   public static final int INITIAL_IHM_SUB_TYPE_ID = 1001;
	   public static final int AMENDMENT_SUB_TYPE_ID = 1002;
	   public static final int CAR_UPDATED_CURRENT_SEQ = 600000;
	   public static final int INTERIM_SUB_TYPE_ID = 1001;
	   public static final int INITIAL_SUB_TYPE_ID = 1002;
	   public static final int RENEWAL_SUB_TYPE_ID = 1004;
	   public static final int INTERMEDIATE_SUB_TYPE_ID = 1003;
	   public static final int ADDITIONAL_SUB_TYPE_ID = 1005;
	   public static final int FINDING_OBS= 1004;
	   public static final String DIST =  "dist";
	   public static final String ROUTES =  "routes";
	   public static final String CERTIFICATE_SEQUENCE = "CERTIFICATESEQNO";
	   public static final String UTN_SEQUENCE = "UTNSEQUENCENO";
	   public static final String CERTIFICATE_ID_SEQ = "CERTIFICATE_ID_SEQNO";
	   public static final int INACTIVE_STATUS = 0;
	   public static final int ACTIVE_STATUS = 1;
	   public static final int ihmUserOnly = 2;
	   public static final int notIhmUser = 0;
	   public static final int onlyPlanAprroval = 2;
	   public static final int planAprrovalAuthorised = 1;
	   public static final int INITIAL_CERT_ISSUE = 1001;
	   public static final int ADMINISTRATIVE_CERT_ISSUE = 1002;
	   public static final int OPEN_FOR_CAR_STATUS = 2;
	   public static final int LINKED_WITH_MLC = 3;
	   public static final int ACCEPT_STATUS = 1;
	   public static final int NOTACCEPT_STATUS = 0;
	   public static final int NOT_APPROVED_SUMMARY = 1005;
	   public static final int RETRIEVED_STATUS = 1;
	   public static final int INTERIM_CERT = 1001;
	   public static final int FULL_TERM_CERT = 1002;
	   public static final int ACCEPTED_REVIEW_STATUS = 2;
	   public static final int EXTENSION = 1003;
	   public static final int INTERMEDAITE_ENDORSED = 1004;
	   public static final int ADDITIONAL_ENDORSED = 1005;
	   public static final int RENEWAL_ENDORSED1 = 1006;
	   public static final int RENEWAL_ENDORSED2 = 1007;
	   public static final int RE_ISSUE = 1008;
	   public static final int ISM_NO_AUD_CERT_AUDITSEQ = 600001;
	   public static final int ISPS_NO_AUD_CERT_AUDITSEQ = 600002;
	   public static final int MLC_NO_AUD_CERT_AUDITSEQ =  600003;
	   public static final int IHM_NO_AUD_CERT_AUDITSEQ =  600004;
	   public static final int AUD_CERT_PUBLISH_STATUS =  1;
	   public static final String CERTIFICATE_ORDER_NO = "CERTIFICATE_ORDER_NO";
	   public static final String UPLOAD_DIR = "c:\\iri\\upload\\prod\\";
	   public static final String cert_url="https://verify.register-iri.com/qr/docVerify?qid=";
	   public static final String FILE_UPLOAD_DIR ="C:\\CentralAuditApi\\FileUpload\\";
	   public static final String FILE_PATH_REPORT ="C://CentralAuditApi//FileUpload//";
	   /**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
	   public static final String DEVICE_WEB = "Web";
	   public static final String DEVICE_MOBILE = "Mobile";
	   public static final String DEVICE_DESKTOP = "Laptop";
	   /**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/
	   //Added by sudharsan for Jira-Id IRI-5678 Start here
	   public static final String REISSUEREASON_1 = "ORACLE DATA ERROR";
	   public static final String REISSUEREASON_2 = "ORACLE DATA CHANGE";
	   public static final String REISSUEREASON_3 = "ISSUER ERROR - DATES";
	   public static final String REISSUEREASON_4 = "ISSUER ERRPR - OTHERS";
	   public static final String REISSUEREASON_5 = "OWNER REQUEST - REPLACEMENT";
	   public static final String REISSUEREASON_6 = "OWNER REQUEST - EXTENSION";
	   public static final String REISSUEREASON_7 = "OWNER REQUEST - OTHER";
	   public static final String REISSUEREASON_8 = "OTHER";
	   //Added by sudharsan for Jira-Id IRI-5678 End here
	   // public static final String RMI_URL = "http://bsaudit.register-iri.com/RMIInterSys";
	   // public static final String RMI_URL = "http://182.76.27.245:7001/RMIInterSys";
//	   	 public static final String RMI_URL = "http://192.168.1.165:7001/RMIInterSys";
	   // public static final String RMI_URL = "http://192.168.182.98:7001/RMIInterSys";
	   public static final String RMI_URL = "http://3.7.127.112:7001/RMIInterSys";
	   public static final boolean isStaggingApplication =  true;
	}