package com.api.central.cycle.delegate;
import java.util.*;

import org.springframework.web.bind.annotation.PathVariable;

import com.api.central.audit.entity.AuditCycle;
import com.api.central.audit.entity.AuditDetail;




public interface AuditCycleDelegate {
	
	



public Map<String, Object> createAuditCycle(AuditCycle auditcycledata);
	
public  List<AuditCycle>  getAuditCycleData(Integer vesselImoNo,Integer auditTypeId,Long companyId);
  

public List<AuditDetail> getAuditCycleHistoryDate( Integer auditTypeId ,Integer auditSubTypeId,Integer vesselImoNo,Long companyId,Integer pageNo,Integer getDefaultSearchCount);
	

public AuditCycle getAuditCreditDate( Integer auditTypeId  ,Integer auditSubTypeId,Integer vesselImoNo, Long companyId, Integer auditSeqNo);	

public List<AuditCycle>getAllCycleDate(Integer auditTypeId,  Integer vesselImoNo,  Long companyId);

}
