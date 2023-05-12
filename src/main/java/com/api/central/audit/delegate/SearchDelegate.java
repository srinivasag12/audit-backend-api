package com.api.central.audit.delegate;

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

public interface SearchDelegate {

	public  Long getSearchCount(SearchCriteria searchCriteria);
	
	public  List<AuditDetailView> getSearchResult(SearchCriteria searchCriteria);

	public List<AuditDetail> getAuditCertificate(Long companyId);

	List<AuditDetailView> getSearchResultReport(SearchCriteria searchCriteria);
	
	public CertificateData auditCertificateData(CertificateData certificatedata, String status);
	
	public List<AuditDetailView> getLatestCreatedVesselCompanyImo(String emailId,Long companyId);

	List<CertificateData> previousAuditCertificate(int vesselImoNum, String certificateNo);

	public List<VesselStatement> getVesselSearchData(Integer imoNo, Long companyId);

	public Long getReviewData(SearchCriteria searchCriteria);

	public List<AuditDetailView> getReviewrResult(SearchCriteria searchCriteria);

	public List<CertificateDetail> getAuditCertificateData(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate, boolean directInterorAdd);
	
	public List<CertificateIhmDetail> getAuditCertificateDataForIhm(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate);

	List<MaPort> getPortSearchResult(SearchCriPort searchCriteria);	
	
}
