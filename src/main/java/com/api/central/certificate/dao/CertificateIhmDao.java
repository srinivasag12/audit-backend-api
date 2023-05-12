package com.api.central.certificate.dao;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.CertificateDetailWithOutAuditIhm;
import com.api.central.audit.entity.SearchCriteria;

public interface CertificateIhmDao {

	public Map<String, Object> getAuditCertDetailsForIhm(Integer auditSeqNo, Long companyId);
	
	public Map<String, Object> generateCertificateIhm(CertificateIhmDetail certificateDetail,Integer count);
	
	public String populateData(List<CertificateIhmDetail> certificateDetail);
	
	public Integer MaxCertLinkNo (CertificateIhmDetail certificateDetail) ;
	
	public void InsertionOfCertDtls(Integer vesselImoNo, Integer auditSeqNo, String certificateNo, Integer auditTypeId,
			Integer auditSubTypeId, Long companyId, String string);
	
	public Map<String, Object> getUtnAndCertificateIdForIhm(String auditReportNo, String certificateNo, Long companyId);
	
	public Map<String, Object> deleteCertificateForIhm(Integer certIssueId, Long companyId, Integer vesselImo, Integer auditSubTypeId, Integer auditSeqNo, String certNo, String SocType);
	
	public Map<String,Object> getSocTypeSelectDefault(Integer certIssueId, Long companyId, Integer vesselImo, Integer auditSubTypeId, Integer auditSeqNo, Integer certLink, String socType);
	
	public Map<String, Object> getCertSearchResultForIhm(SearchCriteria searchCriteria);
	
	public Map<String, Object> getCertificateDetailForIhm(Integer auditSeqNo, Long companyId, Integer seqNo, Integer activeStatus, String SocTypes);
	
	public Map<String, Object> getAllCertificateDetailForIhm(Integer auditTypeId, Integer vesselImo, Long companyId, String socType);
	
	public Map<String, Object> getIntitalCertForDownloadIhm(Integer auditTypeId, Integer vesselImo, Long companyId);
		
	public void sendRmiData(CertificateIhmDetail certificateDetail);

	
	List<CertificateIhmDetail> getAuditCertificateInActiveForIhm(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate);
	
	public Object checkCertificateGenerateIhm(CertificateDetailWithOutAuditIhm certificateDetail);
	
	public Map<String, Object> generateCertificateWithOutAuditIhm(CertificateDetailWithOutAuditIhm certificateDetail);
	
	public Integer MaxCertLinkNoWithoutAudit (CertificateDetailWithOutAuditIhm certificateDetail) ;
	
	public void updateCertificateStatus(Integer auditSeqNo,Long companyid, Integer auditSubtypeid);  //Added by sudharsan and Chibi for JIRA-ID=5377&5378
}
