package com.api.central.audit.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.AuditFindingModel;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.audit.entity.ReportData;
import com.api.central.audit.entity.StampDetails;
import com.api.central.audit.entity.VesselApprovalDetails;
import com.api.central.audit.entity.VesselDetailsHistory;
import com.api.central.audit.entity.VesselNotification;
import com.api.central.master.entity.MaVessel;
import com.api.central.refreshVesselData.VesselRefresh;

public interface AuditDao {

	AuditDetail createOrUpdateISM(AuditDetail auditDetail, String status, String updateMeth);
	
	ReportData saveReportHistoryData(ReportData reportData);

	AuditDetail getAuditDetail(int auditTypeId, int auditSeqNo, Long companyId);

	AuditDetailIhm getAuditDetailForIhm(int auditTypeId, int auditSeqNo, Long companyId);
	
	List<AuditDetailIhm> getAllIhmAuditDetail(int auditTypeId, int imoNo, Long companyId);
	
	//changed by @Ramya for Jira id - IRI-5633
	AuditFindingModel auditFindings(AuditFindingModel auditFinding, String status);

	FindingRptAttach findingRptAttchment(FindingRptAttach findingRptAttach);

	List<AuditFinding> getPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq, String companyImoNo, String companyDoc);

	Map<String, Object> getPreviousAuditDetail(int auditTypeId, int vesselIMONo, Long companyId);

	String deleteFinding(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo);
	
	String deleteFindingDmlcLinked(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo);
	

	List<AuditFinding> currentFinding(int auditTypeId, long companyId, int currentAuditSeq);

	List<AuditFinding> getLaptopPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq);

	List<AuditDetail> getLaptopPreviousAudit(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq);

	void updateLockStatus(int auditTypeId, int auditSeqNo, Long companyId);

	Map<String, String> updateLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String lockHolder);

	void updateLockStatus(Integer auditTypeId, Integer auditSequenceNo, Long companyId, int i);

	Map<String, String> checkAuditLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String userId);

	Map<String, Object> checkAuditLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId);

	Boolean unlinkFindingFiles(FindingRptAttach findingRptAttach);

	Map<String, Object> checkLeadStatus(Integer auditSeqNo, Integer auditTypeId, Long companyId);

	Long updateReviewerStatus(Integer audSeqNo, Integer audType, Long companyId, Integer revStatus);

	Map<String, Object> getSSPRevisionNo(Integer vesselImoNo, Integer auditTypeId, Long companyId, Integer auditSeqNo);

	Map<String, Object> getSspIspsData(Integer vesselImoNo, Integer auditTypeId, Long companyId, Integer auditSubTypeId);

	Map<String, Object> getSSPDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId, Integer auditSubTypeId,
			Integer auditSeqNo);

	Map<String, Object> getCountOfLockedAuditByCurrUser(String emailId, Long companyId);

	Map<String, Object> checkLinkedWithIspsOrMLC(Integer auditTypeId, Integer auditSeqNo, Long companyId);

	Long vesselMissingMailCall(Integer auditTypeId, Long companyId, Integer vesselImoNo, String vesselName);

	Map<String, Object> getPrevDocDetails(String compImoNo, String compDocNo, Long companyId);

	Map<String, Object> updateDocFlag(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer docFlag);

	void callSendMailCreateProc(AuditDetail audData);

	List<MaVessel> tcApprovalStatus(String id, Long companyId, Integer vesselImoNo);

	VesselApprovalDetails SavetcApprovalStatus(VesselApprovalDetails vesselApprovalDetails);

	Map<String, Object> getSignature(String emailId, Long companyId);

	Map<String, Object> vesselDtlIncomplete(Integer vesselImoNo, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog);

	Map<String, Object> vesselNotExist(Integer vesselImoNo, Long companyId, String userId);

	Long checkSameAudit(Integer auditTypeId, Integer vesselImoNo, Long companyId);
	
	void allAuditorSign(Integer auditSeqNo,Integer companyId);

	void carUpdateRemoveAuditorsSign(Integer auditSeqNo, Long companyId, Integer auditTypeId);

	void removeAllAuditorSign(Integer auditSeqNo, Long companyId, Integer auditTypeId);

	void updateAuditLockAndReviewStatus(Integer auditSeqNo, Long companyId, Integer auditTypeId);
	Map<String, Object> updateLtrStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer ltrStatus);

	List<Map<String ,Object>> getReportData(Integer auditTypeId, Integer auditSeqNo, Long companyId);

	ReportData getReportBlobData(Integer versionId, Integer auditSeqNo, Long companyId);

	boolean blobData(Integer currAuditseq, Long companyId);

	boolean blobDataforOrg(Integer orgAuditseq,Integer auditTypeId, Long companyId);

	void callDeleteUpdatedStatusOnAudVoidProc(AuditDetail audData);

	boolean signatureGenBlobData(Integer orgAuditseq, Integer auditTypeId, Long companyId, Integer versionId);

	VesselNotification getRmiProced(VesselNotification flag, Long companyId, Integer vesselImo);
	
	boolean createBlob(AuditDetail auditDetail, Integer flag);

	Boolean getNextAdtCrtStatus(Integer auditTypeId, Long companyId, Integer auditSeqNo,Integer vesselImoNo);

	void deleteFindingsSyncTime(AuditDetail auditDetail, List<AuditFinding> findings);

	List<AuditDetail> getSspReviewDetail(int imo, int auditType, Long companyId);

	Map<String, Object> getISPSInitialDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId);

	List<Map<String, Object>> getDMLCReportNos(Integer vesselImoNo, Long companyId, Integer auditTypeId);

	List<AuditFinding> getPreviousFinding(Integer auditTypeId, Integer vesselIMONo, Long companyId, String companyImoNo,
			String companyDoc);

	Map<String, Object> getAuditRelatedData(Integer auditSeqNo, Long companyId);

	Boolean dmlcFinCloserEmail(String findingSeqNos, Integer dmlcseqNo, Integer mlcseqNo, Long companyId);

	String getDbmsCryptoEncode(String CnoUtn);
	
	
	Map<String,Object> vesselDtlIncompleteYatch(String vesselName, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog);

	boolean removeIhmFinalReport(Integer orgAuditseq, Integer auditTypeId, Long companyId, Integer versionId);
	
	void stampDBCall(StampDetails pdfUploadEntity);
	
	List<Map<String ,Object>> auditGetStamp(String REVIEW_REPORT_NO, int AUDIT_SEQ_NO);
	
	public ResponseEntity<Map<String,Boolean>> getFileStamp(String FILE_NAME,Integer auditSeqNo);
	
	public void delStampDet(String FILE_NAME, int USER_ID);
	
	public void getDownLink(String otp,Integer linkId,String userName, String fileType, String fileName, String reviewReportNo);
	
	public ResponseEntity<Map<String, Object>> getDMLCLocationDate(Integer vesselImoNo, Long companyId,Integer auditSeqNo, String status);
	
	public ResponseEntity<Map<String, Object>> getCompletionDate(Integer vesselImoNo,  Long companyId, Integer auditTypeId, String status, Integer auditSeqNo);
	
	public Map<String, Object> getCertificateIHM(Integer VESSEL_IMO_NO,Integer auditSeqNo);
	
	public Map<String,Object>  getCompletedStatus(Integer vesselImoNo,Integer auditTypeId);
	
	void updateVesselDetailsDB(List<VesselDetailsHistory> vesselDetailsHistory);
	
	public Map<String, Object> getVesselHistory(Integer VESSEL_IMO_NO,String status);
	
	public Map<String,Object> getVesselRefresh(Integer VESSEL_IMO_NO);
	
	public Map<String, Object> updateVesselAuto(VesselRefresh vesselRefresh);
	
	public Integer getAuditType(Integer auditSeqNo);
	
	void updateLockHolderSync(Integer auditTypeId, Integer auditSequenceNo, Long companyId, String lockHolder);

	//added by @Ramya for Ticket-659
	void updateStatusId(Integer auditTypeId, Integer auditSeqNo, Long companyId, int i);

	//added by @Ramya for Jira id - IRI-5633
	AuditFinding auditFindingsSave(AuditFinding auditFinding, String status);
}
