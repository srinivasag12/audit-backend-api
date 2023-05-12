package com.api.central.support.delegate;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SupportDetail;
import com.api.central.support.dao.SupportDao;
import com.api.central.util.AppUtil;

@Service
@Transactional(readOnly=true)
public class SupportDelegateImpl implements SupportDelegate{
	
	@Autowired
	private SupportDao supportDao;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	AppUtil appUtil;

	@Override
	@Transactional(readOnly = false)
	public Map<String, String> saveData(SupportDetail supportDetail){
		return supportDao.saveData(supportDetail);	
	}

	@Override
	public List<SupportDetail> getSupportData() {
		return supportDao.getSupportData();
	}

	@Override
	public  List<AuditDetailView> getReterivedAudits(SearchCriteria auditDtls) {
		
		return supportDao.getReterivedAudits(auditDtls);
	}

	@Override
	public SupportDetail getDownloadFile(Integer auditSeqNo, Integer vesselImoNo, Integer auditTypeId,
			Integer auditSubTypeId, Long companyId) {
		
		return supportDao.downloadFile(auditSeqNo,vesselImoNo,auditTypeId,auditSubTypeId,companyId);
	}

	@Override
	public Map<String, String> getLeadName(Integer auditSeqNo,Long companyId) {
		return supportDao.getLeadName(auditSeqNo,companyId);
	}

	@Override
	@Transactional(readOnly = false)
	public List<SupportDetail> getSupportAuditDtls(SearchCriteria getSupportAuditDtls) {
		return supportDao.getSupportAuditDtls(getSupportAuditDtls);
	}

}
