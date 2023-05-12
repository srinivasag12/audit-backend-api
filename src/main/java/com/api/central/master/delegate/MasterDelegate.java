/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MasterDelegate.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/delegate/MasterDelegate.java $
**/
package com.api.central.master.delegate;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.api.central.master.entity.CompanyImoChange;
import com.api.central.master.entity.DocNumberChange;
import com.api.central.master.entity.LaptopPin;
import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaIhmExemptionReason;
import com.api.central.master.entity.MaPort;
import com.api.central.master.entity.MaScreens;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.MaVesselCompanyYatch;
import com.api.central.master.entity.MaVesselYatch;
import com.api.central.master.entity.UserDetailsConfig;
import com.api.central.master.entity.UserSessionDetail;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.master.model.ReportTypes;
import com.api.central.rmiaudit.entity.RmiAuditSigner;

public interface MasterDelegate {

	public MaAuditCodes saveAuditCodes(String flag, MaAuditCodes maAuditCodes);

	List<MaAuditCodes> getAuditCode();

	MaVessel saveVesselType(String flag, MaVessel maVessel);

	MaVesselCompany saveVesselCompany(String flag, MaVesselCompany maCompany);

	List<MaUsers> getEmail();
	
	List<MaIhmExemptionReason> getEcGrantedReason();
	
	List<MaUsersWOPwd> getRoleUpdate(String flag, Long companyId);

	MaUsersWOPwd saveUserDetails(String flag, MaUsers mausers);

	List<MaAuditSubtype> getAuditSubtype(String value);

	UserDetailsConfig saveConfigDetails(UserDetailsConfig userconfig);

	List<UserDetailsConfig> getConfigDetails(String flag, Long companyId);

	boolean updatePassword(String mail, String password);

	List<MaScreens> getDefaultHomeScreen();

	public List<MaUsersWOPwd> getCompanydetails(String id);

	public UserDetailsConfig saveProfileImage(UserDetailsConfig userconfig);

	public List<Long> getCompanyId();

	public List<CompanyImoChange> getCompanyImoHistory(Integer vesselImoNo, Long companyId, Integer pageNo);

	public List<DocNumberChange> getDocHistory(String companyImoNo, Long companyId, Integer pageNo);

	public Integer getCompanyImoHistoryCount(Integer vesselImoNo, Long companyId);

	public Integer getDocHistoryCount(String companyImoNo, Long companyId);

	public Map<String, Object> VerificationCodeNotification(String id);

	public List<MaUsersWOPwd> checkRandomNumber(String id);

	public MaUsersWOPwd updateLeadDetail(String userId, RmiAuditSigner ras, Long companyId);

	public void sendUserCreateMail(String flag, String emailId, Long companyId);

	public Integer getStatusResponse(int value, String id, long cId);

	public int getActiveStatusResponse(String id, long cId);

	public boolean checkVerificationCode(String userId, String verficationCode);

	public Boolean checkUserPwd(String userid, String pwd);

	public Boolean checkUserLoggedin(String userId);

	public Boolean updateUserLoginFlag(String userIde, int flag);

	public List<LaptopPin> mpinCentralData(String userId);

	public List<LaptopPin> getMpinCentralData(String id);

	public String checkUserCredentials(String emailId, String password);

	public Boolean checkLpin(String emailId, String lpin, Long companyId);

	public Boolean checkActiveStatus(String emailId, Long companyId);

	public MaVesselYatch  saveMaVesselYatch(MaVesselYatch maVesselYeatchData); 
	
	List<MaVesselYatch>  getMaVesselYatchData( Long companyId); 
	
	public MaVesselCompanyYatch saveVesselCompanyYatch(String flag, MaVesselCompanyYatch maCompanyYatch);

	public MaPort saveMaportData(String flag, MaPort maPort);
	
	public void updateLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer leadStatus);
	
	public  void saveSessionDetail(UserSessionDetail userSession);
	
	public List<MaAttachmentTypes>  saveOrUpdateReportAttach(List<ReportTypes> ReportTypesList);
	
	public MaIhmExemptionReason  saveOrUpdateEcGrantedReason(List<MaIhmExemptionReason> maIhmExemptionsReason);
	
	public MaIhmExemptionReason checkEcReasonActiveStatus(Integer reasonId);

	boolean updatePassword(String email, String password, String oldPwd);
	
	public String getLaptopPin(String email);
	
	void updateLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String lockholder);
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
	public String getUserIP(HttpServletRequest request);

	public String getPreviousDevice(String emailId);
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/
	
	/** Mobile PIN code Starts here*/
	public List<LaptopPin> mobilepinCentralData(String id);

	public List<LaptopPin> getMobilepinCentralData(String id);

	public String getMobilePin(String mail);
	/** Mobile PIN code End here*/

	Boolean checkMpin(String emailId, String mpin, Long companyId);   //Added by archana For Jira-Id MOBILE-512 on 17-01-2023
	
	
}
