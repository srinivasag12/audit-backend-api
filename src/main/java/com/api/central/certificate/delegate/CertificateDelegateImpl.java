package com.api.central.certificate.delegate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.central.audit.delegate.AuditDelegate;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.certificate.dao.CertificateDao;
import com.api.central.util.AppConstant;

@Service
@Transactional(readOnly=true)
public class CertificateDelegateImpl implements CertificateDelegate{

	@Autowired
	private CertificateDao certificateDao;
	
	@Autowired
	public AuditDelegate auditDelegate;
	
	@Override
	public Map<String, Object> getCertificateDetailnew(Integer auditSeqNo, Long companyId, Integer seqNo, Integer activeStatus) {

		Map<String, Object> map = certificateDao.getCertificateDetailnew(auditSeqNo,companyId,seqNo,activeStatus);
		CertificateDetail cert = (CertificateDetail) map.get("result");
		
		return getInitialCountVal(cert.getAuditTypeId(), cert.getVesselImoNo(),  companyId, map, cert.getAuditSubTypeId(), cert.getAuditSeqNo());
	}
	
	@Override
	public Map<String, Object> getAllIhmCertificateDetail(Integer auditTypeId, Integer vesselImo, Long companyId) {

		Map<String, Object> map = certificateDao.getAllIhmCertificateDetail(auditTypeId,vesselImo,companyId);
		//CertificateDetail cert =map (CertificateDetail) map.get("result");
		return map; 
		//return getInitialCountVal(cert.getAuditTypeId(), cert.getVesselImoNo(),  companyId, map, cert.getAuditSubTypeId(), cert.getAuditSeqNo());
	}
	
	@Override
	public List<CertificateDetail> getAuditCertificateInActive(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate,boolean directInterorAdd) { //added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536
		// TODO Auto-generated method stub
		return certificateDao.getAuditCertificateInActive(imoNo,companyId,certificateNo,auditTypeId, auditDate,directInterorAdd);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> generateCertificate(CertificateDetail certificateDetail) {
		
		String certNo = certificateDetail.getCertificateNo();
		
		AuditDetail auditDetail = new AuditDetail(certificateDetail.getAuditSeqNo(),certificateDetail.getCompanyId(), certificateDetail.getAuditTypeId());
		
		certificateDetail.setAuditDetail(auditDetail);
		
		Map<String, Object> map = certificateDao.generateCertificate(certificateDetail);
		
		CertificateDetail cs = (CertificateDetail) map.get("updatedData");
			
		
		if( certificateDetail.getAuditSubTypeId() !=null && (( (certificateDetail.getAuditSubTypeId() == AppConstant.INTERMEDIATE_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID) && certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) || ((certificateDetail.getAuditSubTypeId() == AppConstant.INITIAL_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId() == AppConstant.RENEWAL_SUB_TYPE_ID) && certificateDetail.getCertIssueId() == AppConstant.EXTENSION)) ){
  
			String status;
			if(certNo != null && certNo != ""){
				status = (certificateDetail.getCertIssueId() == AppConstant.EXTENSION) ? "UPDATE-EXTENSION" : "UPDATE-INTERORADD";
			}else{
				status = (certificateDetail.getCertIssueId() == AppConstant.EXTENSION) ? "INSERT-EXTENSION" : "INSERT-INTERORADD";
			}
			
			certificateDao.InsertionOfCertDtls(cs.getVesselImoNo(),cs.getAuditSeqNo(),cs.getCertificateNo(),cs.getAuditTypeId(),cs.getAuditSubTypeId(),cs.getCompanyId(),status);
			
		}else if(certificateDetail.getAuditTypeId()!=null && certificateDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID){
			String status1="INSERTAMEND-INTERORADD";
//			String status;
//			if(certNo != null && certNo != ""){
//				status = (certificateDetail.getCertIssueId() == AppConstant.EXTENSION) ? "UPDATE-EXTENSION" : "UPDATE-INTERORADD";
//			}else{
//				status = (certificateDetail.getCertIssueId() == AppConstant.EXTENSION) ? "INSERT-EXTENSION" : "INSERT-INTERORADD";
//			}
			if(certificateDetail.getPublishStatus()==0)
			{certificateDao.InsertionOfCertDtls(cs.getVesselImoNo(),cs.getAuditSeqNo(),cs.getCertificateNo(),cs.getAuditTypeId(),cs.getAuditSubTypeId(),cs.getCompanyId(),status1); }
			
		}
		
		// condition added by cb for certificate related changes
				if((certificateDetail.getCertIssueId()== AppConstant.RE_ISSUE)&&(certificateDetail.getAuditSubTypeId()==AppConstant.INTERMEDIATE_SUB_TYPE_ID ||certificateDetail.getAuditSubTypeId()==AppConstant.ADDITIONAL_SUB_TYPE_ID)) {
					String status2=null;
					if(certificateDetail.getPublishStatus()==0){
						status2="endorsement-reissue0";
					}else if(certificateDetail.getPublishStatus()==1) {
						status2="endorsement-reissue1";
					}
					certificateDao.MakePreviousCertificateInactive(certificateDetail.getAuditSeqNo(),certificateDetail.getCertificateNo(),certificateDetail.getAuditTypeId(),certificateDetail.getCompanyId(),certificateDetail.getVesselImoNo(),certificateDetail.getSeqNo(),certificateDetail.getCertificateId(),status2);
				}
		
		return map;
	}
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> generateCertificatePublishStatusWithOutAudit(CertificateDetailWithOutAudit certificateDetail) {
		Object Val = certificateDao.checkCertificateGenerate(certificateDetail);
		Map<String, Object> map = new HashMap<String, Object>();
		if(Val != "" && Val != null && Integer.parseInt(Val.toString())==0) {
			
			map.put("check", "present");
			
		}
		else {
			map.put("check", "Not present");
		}
		return map;
	}
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> generateCertificateWithOutAudit(CertificateDetailWithOutAudit certificateDetail) {
		
		Object Val = certificateDao.checkCertificateGenerate(certificateDetail);
		if(Val != "" && Val != null && Integer.parseInt(Val.toString())==0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("check", "publish the certificate");
			return map;
		}else{
			return certificateDao.generateCertificateWithOutAudit(certificateDetail);
		}
	}

	@Override
	public List<CertificateDetail> getCertificateData(Long companyId, String utn) {
		
		return certificateDao.getCertificateData(companyId, utn);
	}
	@Override
	public List<CertificateDetail> getCertificateData(Long companyId, String utn,String certNo) {
		
		return certificateDao.getCertificateData(companyId,utn,certNo);
	}

	@Override
	public Map<String, Object> checkRelatedToCurrAdt(Integer auditSeqNo, Long companyId, String userId) {
		
		return certificateDao.checkRelatedToCurrAdt(auditSeqNo,companyId, userId);
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> syncTimeUpdateRmiData(CertificateDetail certificateDetail) {
		
		return certificateDao.syncTimeUpdateRmiData(certificateDetail);
	}

	@Override
	public Map<String, Object> getCertSearchResult2(SearchCriteria searchCriteria) {
		
		return certificateDao.getCertSearchResult2(searchCriteria);
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> publishCertificate(Integer auditSeqNo, Long companyId, Integer seqNo) {
		
		return certificateDao.publishCertificate(auditSeqNo,companyId,seqNo);
	}

	@Override
	public Map<String, Object> getUtnAndCertificateId(String auditReportNo, String certificateNo, Long companyId) {
		
		return certificateDao.getUtnAndCertificateId(auditReportNo,certificateNo,companyId);
	}

	@Override
	public Map<String, Object> getAuditCertDetails(Integer auditSeqNo, Long companyId) {
		
		 Map<String, Object> map = certificateDao.getAuditCertDetails(auditSeqNo,companyId);
		 AuditDetail audDtls = (AuditDetail) map.get("auditDetail");
		 
		return getInitialCountVal(audDtls.getAuditTypeId(), audDtls.getVesselImoNo(), companyId, map, audDtls.getAuditSubTypeId(), audDtls.getAuditSeqNo());
	}

	@Override
	public Map<String, Object> getConsective(Integer vesselImo,Integer auditSeqNo,Long companyId) {
		return certificateDao.getConsective(vesselImo,auditSeqNo,companyId);
	}

	@Override
	public Map<String, Object> getInitialCountVal(Integer auditTypeId, Integer vesselIMONo, Long companyId, Map<String, Object> map, Integer subTypeId, Integer auditSeqNo) {
		
		 Map<String, Object> auditDate = auditDelegate.getPreviousAuditDetail(auditTypeId, vesselIMONo, companyId);
		 List<AuditDetailView> lst = (List<AuditDetailView>)auditDate.get("prevAuditDtl");
		 
		 if( subTypeId!=null && (subTypeId == AppConstant.INTERMEDIATE_SUB_TYPE_ID || subTypeId == AppConstant.ADDITIONAL_SUB_TYPE_ID)){
			 map.put("initalCount", auditDate.get("initalCount"));
			 
			 long renewalCount = lst.stream()
						.filter( item -> (item.getAuditSubTypeId() ==AppConstant.RENEWAL_SUB_TYPE_ID && item.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (item.getAuditSummaryId() != null ? item.getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true)
						&& (item.getCertificateDetail().stream().filter(certData -> certData.getCertIssueId() == AppConstant.FULL_TERM_CERT).count() > 0))).count();
			 
			 map.put("renewalCount", renewalCount);
			 
		 }
		 if(subTypeId !=null ){
		 if( subTypeId == AppConstant.INTERIM_SUB_TYPE_ID ){

			 map.put("checkIntermCert", certificateDao.getCurrentCertCount(auditSeqNo,companyId,auditTypeId,subTypeId,vesselIMONo,AppConstant.INTERIM_CERT));
			 
		 }else if(subTypeId == AppConstant.INITIAL_SUB_TYPE_ID || subTypeId == AppConstant.RENEWAL_SUB_TYPE_ID){

			 map.put("checkFullTermCert", certificateDao.getCurrentCertCount(auditSeqNo,companyId,auditTypeId,subTypeId,vesselIMONo,AppConstant.FULL_TERM_CERT));

		 }else if(subTypeId == AppConstant.INTERMEDIATE_SUB_TYPE_ID){
			 
			 map.put("checkIntermediateCert", certificateDao.getCurrentCertCount(auditSeqNo,companyId,auditTypeId,subTypeId,vesselIMONo,AppConstant.INTERMEDAITE_ENDORSED));
			 
		 }else if(subTypeId == AppConstant.ADDITIONAL_SUB_TYPE_ID){
			 
			 map.put("checkAdditionalCert", certificateDao.getCurrentCertCount(auditSeqNo,companyId,auditTypeId,subTypeId,vesselIMONo,AppConstant.ADDITIONAL_ENDORSED));
			 
			 long intermCount = lst.stream()
						.filter( item -> (item.getAuditSubTypeId() ==AppConstant.INTERIM_SUB_TYPE_ID && item.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (item.getAuditSummaryId() != null ? item.getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) )).count();
			 
			 map.put("intermCount", intermCount);
			 
		 }
		 for(int i = 0; i < lst.size();i++) {
			 if(lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS) {
		 if(lst.get(i).getReviewStatus() == AppConstant.ACCEPTED_REVIEW_STATUS ) {
			 map.put("managerDisableDropdown", true);
			 break;
		 }
		 else {
			 map.put("managerDisableDropdown", false);
			 break;
		 }
		 }
		 }
		 }
		 
		 if(lst.size()>0){
			 
			 
			 for(int k=0;k <lst.size();k++){
				 
				 if(lst.get(k).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(k).getAuditSummaryId() != null ? lst.get(k).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) && !(lst.get(k).getAuditSeqNo().equals(auditSeqNo)) && (lst.get(k).getAuditTypeId().equals(auditTypeId))){
					 map.put("previousAuditDate", lst.get(k).getAuditDate());
					 break;
				 }
			 }
			 
//			 for(int k=0;k <lst.size();k++){
//				 
//				 if(lst.get(k).getAuditStatusId() == AppConstant.VOID_AUDIT_STATUS && lst.get(k).getAuditSubTypeId()== AppConstant.INITIAL_SUB_TYPE_ID && lst.get(k).getAuditTypeId() == AppConstant.MLC_TYPE_ID && subTypeId == AppConstant.INTERMEDIATE_SUB_TYPE_ID){
//					 map.put("disableReissue", true);
//					// break;
//				 }
//			 }
 
			 int prevCount = 0, prevRenewalCertType = 0, countVal = 0;
			 
			 if(subTypeId == AppConstant.INTERMEDIATE_SUB_TYPE_ID){

				 for(int i=0;i <lst.size();i++){
					 
					 if((lst.get(i).getAuditSubTypeId().equals(AppConstant.INTERMEDIATE_SUB_TYPE_ID)) && !(lst.get(i).getAuditSeqNo().equals(auditSeqNo)) && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true)){
						 countVal = 1; 
					 }
				 }

				 if(countVal == 0){

					 for(int i=0;i <lst.size();i++){
						 if(lst.get(i).getAuditSubTypeId() == AppConstant.INITIAL_SUB_TYPE_ID && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true)){
							 map.put("intermediateMinAuditDate", lst.get(i).getAuditDate());
							 break; 
						 }
					 }
				 }
			 }
			 
			 			 
			 for(int i=0;i<lst.size();i++){

				 if(subTypeId == AppConstant.RENEWAL_SUB_TYPE_ID){
					 if(prevCount == 0 && auditSeqNo > lst.get(i).getAuditSeqNo() && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true)){

						 map.put("prevAuditDtl", lst.get(i));
						 prevCount++; 
					 }

					 if(prevRenewalCertType == 0 && auditSeqNo > lst.get(i).getAuditSeqNo() && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) && lst.get(i).getAuditSubTypeId() == AppConstant.RENEWAL_SUB_TYPE_ID){

						 if( lst.get(i).getCertificateDetail().stream().filter(e  -> e.getCertIssueId() == AppConstant.FULL_TERM_CERT).count() >0){
							 map.put("prevRenewalIsFullterm", true);
						 }else{
							 map.put("prevRenewalIsRenewalEndrosed", true);
						 } 
						 prevRenewalCertType++;
					 }
				 }

				 if(!auditSeqNo.equals(lst.get(i).getAuditSeqNo()) && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true)){

					 if(lst.get(i).getCertificateDetail().stream().filter(e -> e.getPublishStatus() == 0 && (e.getCertIssueId() != AppConstant.EXTENSION && e.getCertIssueId() != AppConstant.RE_ISSUE)).count() > 0){
						 map.put("certificateNotPublish", true);
					 }
				 }
			 }	
			 
			 
			 if(subTypeId!=null && lst.stream().filter(e ->  e.getAuditSeqNo() > auditSeqNo && e.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (e.getAuditSummaryId() != null ? e.getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) ).count() > 0){


				 if(subTypeId == AppConstant.RENEWAL_SUB_TYPE_ID || subTypeId == AppConstant.INITIAL_SUB_TYPE_ID){

					 for(int i=0;i<lst.size();i++){

						 if(lst.get(i).getAuditSeqNo()> auditSeqNo && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true)){

							 map.put("nxtAuditInterOrAdd", true); 

						 }

//						 long count =  lst.stream().filter(e -> ((e.getAuditSubTypeId()== AppConstant.RENEWAL_SUB_TYPE_ID && (e.getCertificateDetail().stream().filter(ce -> ce.getCertIssueId() == AppConstant.FULL_TERM_CERT).count() == 0))|| e.getAuditSubTypeId()== AppConstant.INTERMEDIATE_SUB_TYPE_ID || e.getAuditSubTypeId()== AppConstant.ADDITIONAL_SUB_TYPE_ID) && e.getMakeFinal() == 0 && e.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (e.getAuditSummaryId() != null ? e.getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) ).count();
						 long count =  lst.stream().filter(e -> (e.getAuditSubTypeId()== AppConstant.RENEWAL_SUB_TYPE_ID || e.getAuditSubTypeId()== AppConstant.INTERMEDIATE_SUB_TYPE_ID || e.getAuditSubTypeId()== AppConstant.ADDITIONAL_SUB_TYPE_ID) && e.getMakeFinal() == 0 && e.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (e.getAuditSummaryId() != null ? e.getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) ).count();


						 if(count == 0){

							 if(lst.get(i).getAuditSeqNo()> auditSeqNo && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) && (lst.get(i).getAuditSubTypeId() == AppConstant.INITIAL_SUB_TYPE_ID || lst.get(i).getAuditSubTypeId() == AppConstant.RENEWAL_SUB_TYPE_ID)){


								 if(lst.get(i).getAuditSubTypeId() == AppConstant.INITIAL_SUB_TYPE_ID){

									 map.put("nextAudit", true); 
									 break;
								 }else{

									 count =  lst.stream().filter(e -> e.getAuditSubTypeId()== AppConstant.RENEWAL_SUB_TYPE_ID && e.getLeadSign() == null  && e.getAuditStatusId() == AppConstant.COMMENCED_AUDIT_STATUS).count();
									 int val = 0;
									 if(count == 0){
										 val = 1;

									 }else if(count > 0){
										 val = 1;

									 }else{
										 map.put("nextAudit", true); 
										 break;

									 }
									 
									 if(val == 1){
										 
										 for(int j =0;j<lst.get(i).getCertificateDetail().size();j++){

											 if(lst.get(i).getCertificateDetail().size() > 0){

												 if(lst.get(i).getAuditSubTypeId() == AppConstant.RENEWAL_SUB_TYPE_ID && lst.get(i).getCertificateDetail().get(j).getCertIssueId() == AppConstant.FULL_TERM_CERT){
													 map.put("nextAudit", true); 
													 break;
												 }
											 }
										 }

										 AuditDetailView maxValue = lst.stream().max(Comparator.comparing(v -> v.getAuditSeqNo())).get();

										 if(maxValue.getCertificateDetail().size() > 0){

											 List<CertificateDetail> certDtls =  maxValue.getCertificateDetail().stream().filter(e -> e.getMaxSeqNo() == e.getSeqNo()).collect(Collectors.toList());

											 if(certDtls.size() > 0){
												 map.put("previousCertExpiryDate", certDtls.get(0).getExtendedExpireDate());
											 }
										 }
									 }
								 }
							 }		
						 }else if (lst.get(i).getAuditSubTypeId()== AppConstant.INTERMEDIATE_SUB_TYPE_ID || lst.get(i).getAuditSubTypeId() == AppConstant.RENEWAL_SUB_TYPE_ID){
							 map.put("nextAudit", false); 
							 break;
						 }
						 else {
							 map.put("nextAudit", true); 
							 break;
						 }
					 }
				 }else {

					 for(int i=0;i<lst.size();i++){
						
						 if(lst.get(i).getAuditSeqNo()> auditSeqNo && lst.get(i).getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (lst.get(i).getAuditSummaryId() != null ? lst.get(i).getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) && (lst.get(i).getScope() != 1001 && lst.get(i).getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID)){

				
									 System.out.println("seq number for testingh "+lst.get(i).getAuditSeqNo());
							 map.put("checkNxtAuditCreate", true); 

						 }
					 }

					 long count =  lst.stream().filter(e -> (e.getAuditSubTypeId()== AppConstant.INTERIM_SUB_TYPE_ID || e.getAuditSubTypeId()== AppConstant.INTERMEDIATE_SUB_TYPE_ID || e.getAuditSubTypeId()== AppConstant.ADDITIONAL_SUB_TYPE_ID || e.getAuditSubTypeId()== AppConstant.RENEWAL_SUB_TYPE_ID) && e.getLeadSign() == null && e.getAuditStatusId() == AppConstant.COMMENCED_AUDIT_STATUS && e.getMakeFinal() == 0 && e.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS && (e.getAuditSummaryId() != null ? e.getAuditSummaryId() != AppConstant.NOT_APPROVED_SUMMARY : true) ).count();

					 if(count > 0){
						 map.put("nextAudit", true); 
					 }
				 }
			 }
		 }
		 if(auditDate.get("carFindMaxStatusDate") != null){
			 map.put("carFindMaxStatusDate", auditDate.get("carFindMaxStatusDate"));
		 }
		
		
		return map;
	}

	@Override
	public String decryptCrypto(String decryptStr) {
		
		return certificateDao.decryptCrypto(decryptStr);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> UpdateDirectIntermediateIssueExpiryDate(Integer auditTypeId, Integer auditSeqNo, Long companyId,java.util.Date certExpireDate, java.util.Date certIssueDate,Integer certIssueId,Integer vesselIMONo) {
		
		return certificateDao.UpdateDirectIntermediateIssueExpiryDate(auditTypeId, auditSeqNo, companyId, certExpireDate,certIssueDate,certIssueId,vesselIMONo);
	}
	
	@Override
	/*method added by cb for making the smc void certificates inactive TICKET-552 */
	public void updateCertificateStatusSMC(Integer auditSeqNo, Long companyid, Integer auditSubtypeid) {
			//Log.info("Inside certificate delegateimpl");
			certificateDao.updateCertificateStatusSMC(auditSeqNo,companyid,auditSubtypeid);
			
		
		
	}
}

