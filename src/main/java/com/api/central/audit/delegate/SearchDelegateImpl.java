package com.api.central.audit.delegate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.central.audit.dao.SearchDao;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CertificateData;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.SearchCriPort;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.VesselStatement;
import com.api.central.master.entity.MaPort;



@Service
@Transactional(readOnly=true)
public class SearchDelegateImpl implements SearchDelegate {
	
	@Autowired
	private SearchDao searchDao;

	@Override
	public Long getSearchCount(SearchCriteria searchCriteria) {
		return searchDao.getSearchCount(searchCriteria);
	}

	@Override
	public List<AuditDetailView> getSearchResult(SearchCriteria searchCriteria) {
		return searchDao.getSearchResult(searchCriteria);
	}
	
	
	@Override
	public List<VesselStatement> getVesselSearchData(Integer imoNo,Long companyId) {
		// TODO Auto-generated method stub
		return searchDao.getVesselSearchData(imoNo,companyId);
	}
	
	@Override
	public List<AuditDetailView> getSearchResultReport(SearchCriteria searchCriteria) {
		return searchDao.getSearchResultReport(searchCriteria);
	}

	@Override
	public List<AuditDetail> getAuditCertificate(Long companyId) {
		// TODO Auto-generated method stub
		return searchDao.getAuditCertificate(companyId);
	};
	
	@Override
	@Transactional(readOnly = false)
	public CertificateData auditCertificateData(CertificateData certificatedata, String status){

	
		
		return searchDao.auditCertificateData(certificatedata, status);
	}

	
	
	@Override
	@Transactional(readOnly = false)
	public List<CertificateData> previousAuditCertificate(int vesselImoNum, String certificateNo){

		List<CertificateData> prevauditdata = new ArrayList<CertificateData>();

		prevauditdata = searchDao.previousAuditCertificate(vesselImoNum, certificateNo);

		return prevauditdata;
	}

	@Override
	public Long getReviewData(SearchCriteria searchCriteria) {
		return searchDao.getReviewData(searchCriteria);
	}

	@Override
	public List<AuditDetailView> getReviewrResult(SearchCriteria searchCriteria) {
		return searchDao.getReviewrResult(searchCriteria);
	}

	@Override
	public List<CertificateDetail> getAuditCertificateData(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate, boolean directInterorAdd) {
		// TODO Auto-generated method stub
		return searchDao.getAuditCertificateData(imoNo,companyId,certificateNo,auditTypeId, auditDate,directInterorAdd);  //added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536
	}

	@Override
	public List<CertificateIhmDetail> getAuditCertificateDataForIhm(Integer imoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate) {
		// TODO Auto-generated method stub
		return searchDao.getAuditCertificateDataForIhm(imoNo,companyId,certificateNo,auditTypeId, auditDate);
	}
	
	@Override
	public List<AuditDetailView> getLatestCreatedVesselCompanyImo(String emailId,Long companyId) {
		// TODO Auto-generated method stub
		return searchDao.getLatestCreatedVesselCompanyImo(emailId,companyId);
	}

	@Override
	public List<MaPort> getPortSearchResult(SearchCriPort searchCriteria) {
		return searchDao.getPortSearchResult(searchCriteria);
	};
	
}
