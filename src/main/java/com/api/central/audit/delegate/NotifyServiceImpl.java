package com.api.central.audit.delegate;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.audit.dao.NotifyDao;
import com.api.central.audit.entity.NotifyEmailDtls;
import com.api.central.audit.entity.PartialVesselLog;

@Service
@Transactional(readOnly=true)
public class NotifyServiceImpl implements NotifyService{
	
	@Autowired
	private NotifyDao notifydao;
	
	@Override
	public List<Map<String, Object>> getEmailLogDtls(String username,Long companyid) {
		return notifydao.emailLogDtls(username,companyid);
	}
	
	@Override
	public Integer emailCount(String userName, int companyid) {
		return notifydao.countEmails(userName, companyid);
	}

	@Override
	public Map<String, Object> updateFlag(NotifyEmailDtls notifyEmail) {
		notifydao.changFlag(notifyEmail);
		return notifydao.getEmailDtls(notifyEmail.getEmailId(),notifyEmail.getCompanyId());		
	}
	
	@Override
	public List<PartialVesselLog> rmiScheduler() {
		return notifydao.getRmiDtls();
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePartialLogFlag(PartialVesselLog partialVesselLog) {
		notifydao.updatePartialStatus(partialVesselLog);
	}
	
	@Override
	public String checkVersionId() {
		return notifydao.getVersionId();			//changed by @ramya for jira id - IRI-5559
	}


//	@Override
//	@Transactional(readOnly = false)
//	public Integer updateVersionId() {
//		return notifydao.updateVersionId(notifydao.getVersionId()+1);
//	}	
}
