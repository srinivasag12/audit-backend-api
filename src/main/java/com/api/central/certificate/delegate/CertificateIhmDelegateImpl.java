package com.api.central.certificate.delegate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.central.audit.delegate.AuditDelegate;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.CertificateDetailWithOutAuditIhm;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.certificate.dao.CertificateDao;
import com.api.central.certificate.dao.CertificateIhmDao;
import com.api.central.util.AppConstant;

@Service
@Transactional(readOnly=true)
public class CertificateIhmDelegateImpl implements CertificateIhmDelegate {
	
	
	@Autowired
	private CertificateIhmDao certificateIhmDao;
	
	@Autowired
	public AuditDelegate auditDelegate;
	
	@Override
	public Map<String, Object> getAuditCertDetailsForIhm(Integer auditSeqNo, Long companyId) {
		
		 Map<String, Object> map = certificateIhmDao.getAuditCertDetailsForIhm(auditSeqNo,companyId);
		 AuditDetailIhm audDtls = (AuditDetailIhm) map.get("auditDetail");
		 
		return getInitialCountVal(audDtls.getAuditTypeId(), audDtls.getVesselImoNo(), companyId, map, audDtls.getAuditSubTypeId(), audDtls.getAuditSeqNo());
	}
	
	@Override
	public Map<String, Object> getCertSearchResultForIhm(SearchCriteria searchCriteria) {
		
		return certificateIhmDao.getCertSearchResultForIhm(searchCriteria);
	}
	
	@Override
	public Map<String, Object> getAllCertificateDetailForIhm(Integer auditTypeId, Integer vesselImo, Long companyId, String socType) {

		Map<String, Object> map = certificateIhmDao.getAllCertificateDetailForIhm(auditTypeId,vesselImo,companyId, socType);
		//CertificateDetail cert =map (CertificateDetail) map.get("result");
		return map; 
		//return getInitialCountVal(cert.getAuditTypeId(), cert.getVesselImoNo(),  companyId, map, cert.getAuditSubTypeId(), cert.getAuditSeqNo());
	}
	
	@Override
	public Map<String, Object> getIntitalCertForDownloadIhm(Integer auditTypeId, Integer vesselImo, Long companyId) {

		Map<String, Object> map = certificateIhmDao.getIntitalCertForDownloadIhm(auditTypeId,vesselImo,companyId);
		
		return map; 
		
	}
	
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> generateCertificateWithOutAuditIhm(List<CertificateDetailWithOutAuditIhm> certificateDetailList) {
		
		//int len = certificateDetailList.size();
		//Map<String,Object> resultMap= new HashMap<String,Object>();
		Map<String, Object> map  = new  HashMap<String,Object>();
		Integer count = 0;
		
		Integer linkSeq = certificateIhmDao.MaxCertLinkNoWithoutAudit(certificateDetailList.get(0));
		
		Object Val = certificateIhmDao.checkCertificateGenerateIhm(certificateDetailList.get(0));
		if(Val != "" && Val != null && Integer.parseInt(Val.toString())==0){
			
			map.put("check", "publish the certificate");
			
		}else{
			for(int i=0 ; i < certificateDetailList.size();i++){
				CertificateDetailWithOutAuditIhm certificateDetail = certificateDetailList.get(i) ;
				count = count +1;
				certificateDetail.setCertificateLink(linkSeq);
			map = certificateIhmDao.generateCertificateWithOutAuditIhm(certificateDetail);
			CertificateIhmDetail certDetail = new CertificateIhmDetail();
			BeanUtils.copyProperties(certificateDetail,certDetail);
			 if(certificateDetail.getPublishStatus() == 1) {
				 certificateIhmDao.sendRmiData(certDetail);
			 }
			}
		}
		return map;
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
	
		 
		
		
		return map;
	}
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> generateCertificateIhm(List<CertificateIhmDetail> certificateDetailList) {
	
		int len = certificateDetailList.size();
		Map<String,Object> resultMap= new HashMap<String,Object>();
		Map<String, Object> map  = new  HashMap<String,Object>();
		Integer count = 0;
		CertificateIhmDetail certificateDetail = null; 
		List<CertificateIhmDetail> li = new ArrayList<>();
		CertificateIhmDetail cs = null;
		Integer linkSeq = certificateIhmDao.MaxCertLinkNo(certificateDetailList.get(0));
		for(int i=0 ; i < certificateDetailList.size();i++){
			certificateDetail = certificateDetailList.get(i) ;
		count = count +1;
		String certNo = certificateDetail.getCertificateNo();
		
		AuditDetailIhm auditDetail = new AuditDetailIhm(certificateDetail.getAuditSeqNo(),certificateDetail.getCompanyId(), certificateDetail.getAuditTypeId());
		
		certificateDetail.setAuditDetail(auditDetail);
		
		certificateDetail.setCertificateLink(linkSeq);
		
		 map = certificateIhmDao.generateCertificateIhm(certificateDetail,count);
		 
		 
	/*	 if(certificateDetail.getPublishStatus() == 1) {
			 certificateIhmDao.sendRmiData(certificateDetail);
		 }*/
		 cs = (CertificateIhmDetail) map.get("updatedData");
			
		 li.add(cs);
		
		}
//		certificateIhmDao.populateData(li);
		if(len > 0 && certificateDetailList.get(0).getPublishStatus() == 1) {
			 certificateIhmDao.sendRmiData(certificateDetailList.get(0));
		 }
		
		if(certificateDetail.getAuditTypeId()!=null && certificateDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID && certificateDetail.getAuditSubTypeId()==AppConstant.AMENDMENT_SUB_TYPE_ID && (certificateDetail.getCertIssueId() == AppConstant.ADDITIONAL_ENDORSED || certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE)){
			 String status1 = "";
			if(certificateDetail.getCertIssueId() == AppConstant.ADDITIONAL_ENDORSED) {
			  status1="INSERTAMEND-ADDITIONAL";
			}
			else {
				 status1="INSERTAMEND-REISSUE";
			}
			if(certificateDetail.getPublishStatus()==0)
			{certificateIhmDao.InsertionOfCertDtls(cs.getVesselImoNo(),cs.getAuditSeqNo(),cs.getCertificateNo(),cs.getAuditTypeId(),cs.getAuditSubTypeId(),cs.getCompanyId(),status1); }
			
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getCertificateDetailForIhm(Integer auditSeqNo, Long companyId, Integer seqNo, Integer activeStatus, String SocType) {

		Map<String, Object> map = certificateIhmDao.getCertificateDetailForIhm(auditSeqNo,companyId,seqNo,activeStatus,SocType);
		CertificateIhmDetail cert = (CertificateIhmDetail) map.get("result");
		
		return getInitialCountVal(cert.getAuditTypeId(), cert.getVesselImoNo(),  companyId, map, cert.getAuditSubTypeId(), cert.getAuditSeqNo());
	}
	
	@Override
	public Map<String, Object> getUtnAndCertificateIdForIhm(String auditReportNo, String certificateNo, Long companyId) {
		
		return certificateIhmDao.getUtnAndCertificateIdForIhm(auditReportNo,certificateNo,companyId);
	}

	@Override
	public Map<String, Object> deleteCertificateForIhm(Integer certIssueId, Long companyId, Integer vesselImo, Integer auditSubTypeId, Integer auditSeqNo, String certNo, String SocType) {
		
		return certificateIhmDao.deleteCertificateForIhm(certIssueId,companyId,vesselImo,auditSubTypeId, auditSeqNo,certNo,SocType);
	}
	
	@Override
	public Map<String,Object> getSocTypeSelectDefault(Integer certIssueId, Long companyId, Integer vesselImo, Integer auditSubTypeId, Integer auditSeqNo, Integer certLink, String socType) {
		
		return certificateIhmDao.getSocTypeSelectDefault(certIssueId,companyId,vesselImo,auditSubTypeId, auditSeqNo,certLink, socType);
	}
	
	@Override
	public List<CertificateIhmDetail> getAuditCertificateInActiveForIhm(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate) {
		// TODO Auto-generated method stub
		return certificateIhmDao.getAuditCertificateInActiveForIhm(imoNo,companyId,certificateNo,auditTypeId, auditDate);
	}

	//Added by sudharsan and Chibi for JIRA-ID=5377&5378
	@Override
	public void updateCertificateStatus(Integer auditSeqNo, Long companyid, Integer auditSubtypeid) {
		Log.info("Inside certificate delegateimpl");
		certificateIhmDao.updateCertificateStatus(auditSeqNo,companyid,auditSubtypeid);
		
	}
	//End here
	
}
