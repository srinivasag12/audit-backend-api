package com.api.central.certificate.dao;

import java.util.List;
import java.util.Map;

import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.SearchCriteria;

public interface CertificateDao {

	public List<CertificateDetail> getCertificateData(Long companyId, String utn);

	public Map<String, Object> checkRelatedToCurrAdt(Integer auditSeqNo, Long companyId, String userId);

	public Map<String, Object> syncTimeUpdateRmiData(CertificateDetail certificateDetail);

	public Map<String, Object> generateCertificate(CertificateDetail certificateDetail);
	
	public Map<String, Object> getCertificateDetailnew(Integer auditSeqNo, Long companyId, Integer seqNo, Integer activeStatus);

	public Map<String, Object> getAllIhmCertificateDetail(Integer auditTypeId, Integer vesselImo, Long companyId);
	
	public Map<String, Object> getCertSearchResult2(SearchCriteria searchCriteria);

	public Map<String, Object> publishCertificate(Integer auditSeqNo, Long companyId, Integer seqNo);

	public Map<String, Object> getUtnAndCertificateId(String auditReportNo, String certificateNo, Long companyId);

	public Map<String, Object> getAuditCertDetails(Integer auditSeqNo, Long companyId);

	public Map<String, Object> getConsective(Integer vesselImo, Integer auditSeqNo, Long companyId);

	public Map<String, Object> generateCertificateWithOutAudit(CertificateDetailWithOutAudit certificateDetail);
	
	List<CertificateDetail> getAuditCertificateInActive(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate,boolean directInterorAdd); //added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536

	public Object checkCertificateGenerate(CertificateDetailWithOutAudit certificateDetail);

	public void InsertionOfCertDtls(Integer vesselImoNo, Integer auditSeqNo, String certificateNo, Integer auditTypeId,
			Integer auditSubTypeId, Long companyId, String string);

	public Boolean getCurrentCertCount(Integer auditSeqNo, Long companyId, Integer auditTypeId, Integer subTypeId,
			Integer vesselIMONo, Integer CertType);

	public String decryptCrypto(String decryptStr);
	
	public List<CertificateDetail> getCertificateData(Long companyId, String utn,String certNo);
	
	public Map<String, Object> UpdateDirectIntermediateIssueExpiryDate(Integer auditTypeId, Integer auditSeqNo, Long companyId, java.util.Date certExpireDate, java.util.Date certIssueDate,Integer certIssueId,Integer vesselIMONo);

	/*method added by cb for making the smc void certificates inactive TICKET-552 */
	public void updateCertificateStatusSMC(Integer auditSeqNo, Long companyid, Integer auditSubtypeid);

	public void MakePreviousCertificateInactive(Integer auditSeqNo, String certificateNo, Integer auditTypeId,
			Long companyId, Integer vesselImoNo, Integer seqNo, Long certificateId, String status);

}
