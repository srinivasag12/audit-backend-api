package com.api.central.cycle.dao;
import java.util.*;

import com.api.central.audit.entity.AuditCycle;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.SearchCriteria;

public interface AuditCycleDao {
	
	
	
	public Map<String, Object> createAuditCycle(AuditCycle auditCycleData);
	
	public  List<AuditCycle>  getAuditCycleData(Integer vesselImoNo,Integer auditTypeId,Long companyId);
	
	public List<AuditDetail> getAuditCycleHistoryDate( Integer auditTypeId ,Integer auditSubTypeId,Integer vesselImoNo,Long companyId,Integer pageNo,Integer getDefaultSearchCount);
	
	public AuditCycle getAuditCreditDate( Integer auditTypeId , Integer auditSubTypeId,Integer vesselImoNo, Long companyId, Integer auditSeqNo);
	
	public List<AuditCycle>getAllCycleDate(Integer auditTypeId,  Integer vesselImoNo,  Long companyId);

}
