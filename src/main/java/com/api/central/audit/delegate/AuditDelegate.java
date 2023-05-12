package com.api.central.audit.delegate;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.AuditFindingModel;
import com.api.central.audit.entity.FileToUpload;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.audit.entity.ReportData;
import com.api.central.audit.entity.StampDetails;
import com.api.central.audit.entity.VesselApprovalDetails;
import com.api.central.audit.entity.VesselDetailsHistory;
import com.api.central.audit.entity.VesselNotification;
import com.api.central.master.entity.MaVessel;
import com.api.central.refreshVesselData.VesselRefresh;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface AuditDelegate {

	AuditDetail createOrUpdateISM(AuditDetail auditDetail, String status, Integer auditTypeId, Long companyId,String updateMeth);

	AuditDetail getAuditDetail(int auditTypeId, int auditSeqNo, Long companyId);
	
	AuditDetailIhm getAuditDetailForIhm(int auditTypeId, int auditSeqNo, Long companyId);
	
	List<AuditDetailIhm> getAllIhmAuditDetail(int auditTypeId, int imoNo, Long companyId);
	
	//changed by @Ramya for Jira id - IRI-5633
	AuditFindingModel auditFindings(AuditFindingModel auditFinding, String status, String findingStatus, Integer auditTypeId, Long companyId);
	
	//added by @Ramya for Jira id - IRI-5633
	AuditFinding auditFindingsSave(AuditFinding auditFinding, String status, String findingStatus, Integer auditTypeId, Long companyId);

	List<AuditFinding> getPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq, String companyImoNo, String companyDoc);

	Map<String, Object> getPreviousAuditDetail(int auditTypeId, int vesselIMONo, Long companyId);

	String deleteFinding(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo) throws IOException;
	
	String deleteFindingDmlcLinked(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo) throws IOException;

	List<AuditFinding> currentFinding(int auditTypeId, long companyId, int currentAuditSeq);

	boolean getLaptopPreviousFinding(int auditTypeId, int auditSeqNo, Long companyId) throws JsonGenerationException, JsonMappingException, IOException;
	
	boolean getMobilePreviousFinding(int auditTypeId, int auditSeqNo, Long companyId) throws JsonGenerationException, JsonMappingException, IOException;

	boolean synchronizeAudit(String path, String auditSeqNo, Integer auditTypeId, Long companyId) throws IOException;

	void updateLockStatus(int auditTypeId, int auditSeqNo, Long companyId, Integer leadStatus);

	Map<String, String> updateLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String lockHolder);

	Map<String, String> checkAuditLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String userId);

	Map<String, Object> checkAuditLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId);

	FindingRptAttach unlinkFindingFiles(FindingRptAttach findingRptAttach, String auditTypeDesc, Long companyId);

	Map<String, Object> checkLeadStatus(Integer auditSeqNo, Integer auditTypeId, Long companyId);

	Long updateReviewerStatus(Integer audSeqNo, Integer audType, Long companyId, Integer revStatus);

	Map<String, Object> getSSPDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId, Integer auditSubTypeId, Integer auditSeqNo);

	Map<String, Object> getSSPRevisionNo(Integer vesselImoNo, Integer auditTypeId, Long companyId, Integer auditSeqNo);

	Map<String, Object> getSspIspsData(Integer vesselImoNo, Integer auditTypeId, Long companyId,
			Integer auditSubTypeId);

	Map<String, Object> getCountOfLockedAuditByCurrUser(String emailId, Long companyId);

	Map<String, Object> checkLinkedWithIspsOrMLC(Integer auditTypeId, Integer auditSeqNo, Long companyId);

	Long vesselMissingMailCall(Integer auditTypeId, Long companyId, Integer vesselImoNo, String vesselName);

	Map<String, Object> getPrevDocDetails(String compImoNo, String compDocNo, Long companyId);

	Map<String, Object> updateDocFlag(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer docFlag);

	void callSendMailCreateProc(AuditDetail audData);

	List<MaVessel> tcApprovalStatus(String id, Long companyId, Integer vesselImoNo);

	VesselApprovalDetails savetcDetails(VesselApprovalDetails vesselApprovalDetails);

	Map<String, Object> getSignature(String emailId, Long companyId);

	Map<String,Object> vesselDtlIncomplete(Integer vesselImoNo, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog);

	Map<String, Object> vesselNotExist(Integer vesselImoNo, Long companyId, String userId);

	boolean syncTimeUpdateRmiData(String string, String string2, Integer auditTypeId, Long companyId) throws IOException;

	Map<String, Object> getAuditDetailAndCheckSameAudit(Integer auditTypeId, Integer auditSeqNo, Long companyId,
			Integer vesselImoNo);
	
	void allAuditorSign(Integer auditSeqNo,Integer companyId);

	void carUpdateRemoveAuditorsSign(Integer auditSeqNo, Long companyId, Integer auditTypeId);
	
	Map<String, Object> updateLtrStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer ltrStatus);
	
	ReportData saveReportHistoryData(ReportData reportData, Integer auditTypeId, Long companyId);

	List<Map<String ,Object>> getReportData(Integer auditTypeId, Integer auditSeqNo, Long companyId);

	ReportData getReportBlobData(Integer versionId, Integer auditSeqNo, Long companyId);

	boolean createBlobData(Integer currAuditseq, Long companyId);

	boolean createBlobDataforOrg(Integer orgAuditseq, Integer auditTypeId, Long companyId);

	void callDeleteUpdatedStatusOnAudVoidProc(AuditDetail audData);

	boolean signatureGenBlobData(Integer orgAuditseq, Integer auditTypeId, Long companyId, Integer versionId);

	VesselNotification getRmiProced(VesselNotification flag, Long companyId, Integer vesselImoNo);

	boolean createBlob(AuditDetail auditDetail, Integer flag);

	Map<String, Object> getAuditDetailAndNextAdtCrtStatus(Integer auditTypeId, Long companyId, Integer auditSeqNo,
			Boolean status, Integer vesselImoNo);

	List<AuditDetail> getSspReviewDetail(int imo,int auditTypeId, Long companyId);

	Map<String, Object> getISPSInitialDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId);

	List<Map<String, Object>> getDMLCReportNos(Integer vesselImoNo, Long companyId, Integer auditTypeId);

	List<AuditFinding> getPreviousFinding(Integer auditTypeId, Integer vesselIMONo, Long companyId, String companyImoNo,
			String companyDoc);

	Map<String, Object> getAuditRelatedData(Integer auditSeqNo, Long companyId);

	Boolean dmlcFinCloserEmail(String findingSeqNos, Integer dmlcseqNo, Integer mlcseqNo, Long companyId);
	
	Map<String,Object> vesselDtlIncompleteYatch(String vesselName, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog);

	boolean removeIhmFinalReport(Integer orgAuditseq, Integer auditTypeId, Long companyId, Integer versionId);
	
	int pdfupload(StampDetails pdfUploadEntity);
	
	int uploadPdfInToMachine(FileToUpload fileToUpload);
	
	void pdfStampFile(String fileName);
	
	ResponseEntity<Resource> pdfPageNumbers(StampDetails pdfUploadEntity);
	
	ResponseEntity<Resource> auditRptAttach(String status,String fileName);
	
	List<Map<String ,Object>> auditGetStamp(String REVIEW_REPORT_NO,int AUDIT_SEQ_NO);
	
	public ResponseEntity<Map<String,Boolean>> getFileStamp(String FILE_NAME,Integer auditSeqNo);
	
	ResponseEntity<String> deleteStamp(String status, String FILE_NAME,int USER_ID);
	
	ResponseEntity<Map<String, String>> getDownloadLink(String userName, String fileType, String fileName, String reviewReportNo);
	
	ResponseEntity<String> sendMailReports(FileToUpload fileToUpload ) throws IOException;
	
	ResponseEntity<Map<String, Object>> getDMLCLocationDate(Integer vesselImoNo, Long companyId,Integer auditSeqNo, String status);
	
	public ResponseEntity<Map<String, Object>> getCompletionDate(Integer vesselImoNo,  Long companyId, Integer auditTypeId, String status, Integer auditSeqNo);
	
	public Map<String, Object> getCertificateIHM(Integer VESSEL_IMO_NO,Integer auditSeqNo);
	
	public Map<String,Object>  getCompletedStatus(Integer vesselImoNo,Integer auditTypeId);
	
	public ResponseEntity<HttpStatus> updateVesselDetails(List<VesselDetailsHistory> vesselDetailsHistory);
	
	public Map<String, Object> getVesselHistory(Integer VESSEL_IMO_NO,String status);
	
	public Map<String,Object> getVesselRefresh(Integer VESSEL_IMO_NO);
	
	public Map<String, Object> updateVesselAuto(VesselRefresh vesselRefresh);

	//added by @Ramya for Ticket-659
	boolean synchronizeLinkedDMLC(String path, String auditSeqNo, Integer auditTypeId, Long companyId)
			throws IOException;
}
