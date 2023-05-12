/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MasterDao.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/dao/MasterDao.java $
**/
package com.api.central.master.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.api.central.master.entity.CompanyImoChange;
import com.api.central.master.entity.DocNumberChange;
import com.api.central.master.entity.LaptopPin;
import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaAuditType;
import com.api.central.master.entity.MaCertificateIssueReason;
import com.api.central.master.entity.MaCertificateReissueReason;
import com.api.central.master.entity.MaIhmExemptionReason;
import com.api.central.master.entity.MaPort;
import com.api.central.master.entity.MaRegion;
import com.api.central.master.entity.MaRoles;
import com.api.central.master.entity.MaScreens;
import com.api.central.master.entity.MaUserRoles;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.MaVesselCompanyYatch;
import com.api.central.master.entity.MaVesselType;
import com.api.central.master.entity.MaVesselYatch;
import com.api.central.master.entity.UserDetailsConfig;
import com.api.central.master.entity.UserSessionDetail;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.rmiaudit.entity.RmiAuditSigner;

public interface MasterDao {

	List<MaAuditType> getAuditType(Long companyId);

	List<MaAuditCodes> getAuditCodes(Long companyId);

	public MaAuditCodes saveAuditCodes(String flag, MaAuditCodes maAuditCodes);

	List<MaAuditCodes> getAuditCode();

	List<MaVesselType> getVesselTypes(Long companyId);

	List<MaVessel> getImoNo(Long companyId);

	public MaVessel saveVesselType(String flag, MaVessel maVessel);

	List<MaVesselCompany> getCompanyImoNo();

	public MaVesselCompany saveVesselCompany(String flag, MaVesselCompany maCompany);

	List<MaUsers> getEmail();

	List<MaRoles> getMaRoles(Long companyId);

	MaUsersWOPwd saveUserDetails(String flag, MaUsers mausers);

	List<MaAuditSubtype> getAuditSubtype(String value);

	UserDetailsConfig saveConfigDetails(UserDetailsConfig userconfig);

	List<UserDetailsConfig> getConfigDetails(String flag, Long companyId);
	
	List<UserDetailsConfig> getConfigDetail(Long companyId);

	boolean updatePassword(String emailId, String password);

	List<MaScreens> getDefaultHomeScreen();

	List<MaUsersWOPwd> getUsers(Long companyId);
	
	List<MaUsersWOPwd> getUserDetails(Long companyId,String emailId);

	List<Long> getCompanyId();

	List<MaVesselCompany> getCmpnyDet(Long companyId);

	List<MaUserRoles> getUserRoles();

	List<MaScreens> getScreenDetails(Long companyId);

	List<MaUsersWOPwd> getRoleUpdate(String flag, Long companyId);

	List<MaUsersWOPwd> getCompanydetails(String id);

	List<UserDetailsConfig> getDetailsConfig(Long companyId);

	UserDetailsConfig saveProfileImage(UserDetailsConfig userconfig);

	List<CompanyImoChange> getCompanyImoHistory(Integer vesselImoNo, Long companyId, Integer pageNo);

	List<DocNumberChange> getDocHistory(String companyImoNo, Long companyId, Integer pageNo);

	Integer getCompanyImoHistoryCount(Integer vesselImoNo, Long companyId);

	Integer getDocHistoryCount(String companyImoNo, Long companyId);

	Map<String, Object> VerificationCodeNotification(String mail);

	List<MaUsersWOPwd> checkRandomNumber(String id);

	List<MaPort> getAllPort(Long companyId);

	List<MaCertificateIssueReason> getCertificateIssueReason(Long companyId);

	List<MaIhmExemptionReason> getEcGrantedReason();
	
	MaUsersWOPwd updateLeadDetail(String userId, RmiAuditSigner ras, Long companyId);

	List<MaAuditCodes> updateAuditCodes(String codes, long companyId, Long auditTypeId);

	List<MaVessel> updateVessel(long companyId, Integer vesselImo);

	List<MaVesselCompany> updateVesselCompany(Long companyId, String companyImoNo);

	List<MaUsersWOPwd> updateUserdata(Long companyId, String sequenceNo, String emailId);

	List<UserDetailsConfig> updateUserConfigDetails(Long companyId, String userId);

	void sendUserCreateMail(String flag, String emailId, Long companyId);

	List<MaRegion> getRegion(Long companyId);

	Integer getStatusResponse(int value, String id, long cId);

	Integer getActiveStatusResponse(String id, long cId);

	boolean checkVerificationCode(String userId, String verficationCode);

	Boolean checkUserPwd(String userId, String pwd);

	Boolean checkUserLoggedin(String userId);

	Boolean updateUserLoginFlag(String userId, int flag);

	List<LaptopPin> mpinCentralData(String userId);

	List<LaptopPin> getMpinCentralData(String userId);

	String checkUserCredentials(String emailId, String password);

	Boolean checkLpin(String emailId, String lpin, Long companyId);

	Boolean checkActiveStatus(String emailId, Long companyId);
	
	List<MaCertificateReissueReason> getCertificateReissueReason(Long companyId);
	
	public MaVesselYatch  saveMaVesselYatch(MaVesselYatch maVesselYeatchData); 
	
	List<MaVesselYatch>  getMaVesselYatchData( Long companyId); 
	
	public MaVesselCompanyYatch saveVesselCompanyYatch(String flag, MaVesselCompanyYatch maCompanyYatch);

	void updateLockStatus(Integer auditTypeId, Integer auditSequenceNo, Long companyId, int i);
	
	public MaPort saveMaportData(String flag, MaPort maPort);
	
	List<MaUsersWOPwd> updateUserdataManager(Long companyId, Long officilId, String emailId);           

	public MaIhmExemptionReason checkEcReasonActiveStatus(Integer reasonId);
	
	public  void saveSessionDetail(UserSessionDetail userSession);
	
	public MaAttachmentTypes saveOrUpdateReportAttach(MaAttachmentTypes MaAttachmentTypes);
	
	public MaIhmExemptionReason saveOrUpdateEcGrantedReason(List<MaIhmExemptionReason> maIhmExemptionsReason);

	boolean updatePassword(String emailId, String password, String OldPwd);
	
	public String getLaptopPin(String emailId);
	
	void updateLockHolder(Integer auditTypeId, Integer auditSequenceNo, Long companyId, String lockHolder);
	
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
	public String getUserIP(HttpServletRequest request);

	String getPreviousDevice(String emailId);
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/
	
	/** Mobile PIN code Starts here*/
	List<LaptopPin> mobilepinCentralData(String id);

	List<LaptopPin> getMobilepinCentralData(String id);

	String getMobilePin(String mail);
	/** Mobile PIN code Ends here*/

	Boolean checkMpin(String emailId, String mpin, Long companyId);  //added by archana for Jira-Id MOBILE-512 on 17-01-2023
}

