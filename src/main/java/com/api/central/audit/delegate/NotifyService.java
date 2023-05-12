package com.api.central.audit.delegate;

import java.util.List;
import java.util.Map;

import com.api.central.audit.entity.NotifyEmailDtls;
import com.api.central.audit.entity.PartialVesselLog;

public interface NotifyService {

	public List<Map<String, Object>> getEmailLogDtls(String username, Long companyid);
	
	public Integer emailCount(String userName, int companyid);

	public Map<String, Object> updateFlag(NotifyEmailDtls notifyEmail);
	
	public List<PartialVesselLog> rmiScheduler();

	public void updatePartialLogFlag(PartialVesselLog partialVesselLog);

	public String checkVersionId();	//changed by @ramya for jira id - IRI-5559

//	public Integer updateVersionId();
}
