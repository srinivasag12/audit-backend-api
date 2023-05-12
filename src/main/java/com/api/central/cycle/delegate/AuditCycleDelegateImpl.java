package com.api.central.cycle.delegate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.audit.entity.AuditCycle;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.cycle.dao.AuditCycleDao;

@Service
@Transactional(readOnly=true)
public class AuditCycleDelegateImpl implements AuditCycleDelegate {
	
	
	
	
	@Autowired 
	AuditCycleDao auditCycleDao;
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object>   createAuditCycle(AuditCycle audicycleData){
	
		return	auditCycleDao.createAuditCycle(audicycleData);
	
	}
	
	@Override
	public List<AuditCycle> getAuditCycleData(Integer vesselImoNo,Integer auditTypeId,Long companyId) {
		return auditCycleDao.getAuditCycleData( vesselImoNo, auditTypeId, companyId);
	}
	

	@Override
	public List<AuditDetail> getAuditCycleHistoryDate( Integer auditTypeId ,Integer auditSubTypeId,Integer vesselImoNo,Long companyId,Integer pageNo,Integer getDefaultSearchCount) {
		return auditCycleDao.getAuditCycleHistoryDate(auditTypeId , auditSubTypeId, vesselImoNo, companyId,pageNo,getDefaultSearchCount);
	}
	
	@Override
    public  AuditCycle  getAuditCreditDate( Integer auditTypeId,Integer auditSubTypeId,Integer vesselImoNo, Long companyId,Integer auditSeqNo){
		
		return	auditCycleDao.getAuditCreditDate(  auditTypeId, auditSubTypeId,vesselImoNo,  companyId, auditSeqNo);
	
	}
	
	@Override
    public  List<AuditCycle>  getAllCycleDate( Integer  auditTypeId,  Integer vesselImoNo,  Long companyId){
		
		return	auditCycleDao.getAllCycleDate(  auditTypeId,vesselImoNo,  companyId);
	
	}

}
