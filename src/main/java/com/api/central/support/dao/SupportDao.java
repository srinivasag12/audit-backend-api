package com.api.central.support.dao;

import java.util.List;
import java.util.Map;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SupportDetail;

public interface SupportDao {

	Map<String, String> saveData(SupportDetail supportDetail);

	List<SupportDetail> getSupportData();

	 List<AuditDetailView> getReterivedAudits(SearchCriteria auditDtls);

	SupportDetail downloadFile( Integer auditSeqNo, Integer vesselImoNo, Integer auditTypeId, Integer auditSubTypeId, Long companyId);

	Map<String, String> getLeadName(Integer auditSeqNo, Long companyId);

	List<SupportDetail> getSupportAuditDtls(SearchCriteria getSupportAuditDtls);

}
