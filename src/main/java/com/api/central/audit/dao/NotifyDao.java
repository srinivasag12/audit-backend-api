package com.api.central.audit.dao;

import java.util.List;
import java.util.Map;

import com.api.central.audit.entity.NotifyEmailDtls;
import com.api.central.audit.entity.PartialVesselLog;

public interface NotifyDao {
	
	List<Map<String, Object>> emailLogDtls(String username, Long companyid);
	
	Integer countEmails(String userName, int companyid);
	
	void changFlag(NotifyEmailDtls notifyEmail);
	
	Map<String ,Object> getEmailDtls(int emailId, Long companyId);
	
	List<PartialVesselLog> getRmiDtls();

	void updatePartialStatus(PartialVesselLog partialVesselLog);
	
	String getVersionId();	//changed by @ramya for jira id - IRI-5559

	Integer updateVersionId(int currVesrionId);

}
