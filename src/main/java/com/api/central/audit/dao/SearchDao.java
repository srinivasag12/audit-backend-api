package com.api.central.audit.dao;

import java.util.List;


import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CertificateData;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.SearchCriPort;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.VesselStatement;
import com.api.central.master.entity.MaPort;



public interface SearchDao {

	Long getSearchCount(SearchCriteria searchCriteria);
	
	List<AuditDetailView> getSearchResult(SearchCriteria searchCriteria);

	List<AuditDetail> getAuditCertificate(Long companyId);

	CertificateData auditCertificateData(CertificateData certificatedata, String status);

	List<AuditDetailView> getSearchResultReport(SearchCriteria searchCriteria);

	List<CertificateData> previousAuditCertificate(int vesselImoNum, String certificateNo);

	List<VesselStatement> getVesselSearchData(Integer imoNo, Long companyId);

	Long getReviewData(SearchCriteria searchCriteria);

	List<AuditDetailView> getReviewrResult(SearchCriteria searchCriteria);

	List<CertificateDetail> getAuditCertificateData(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate, boolean directInterorAdd);
	
	List<CertificateIhmDetail> getAuditCertificateDataForIhm(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate);
	
	List<AuditDetailView> getLatestCreatedVesselCompanyImo(String emailId,Long companyId);

	List<MaPort> getPortSearchResult(SearchCriPort searchCriteria);
	
	
	
	

	
	
}
