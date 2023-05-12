/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MasterDelegate.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/delegate/MasterDelegateImpl.java $
**/

package com.api.central.master.delegate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.master.dao.MasterDao;
import com.api.central.master.entity.CompanyImoChange;
import com.api.central.master.entity.DocNumberChange;
import com.api.central.master.entity.LaptopPin;
import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaIhmExemptionReason;
import com.api.central.master.entity.MaPort;
import com.api.central.master.entity.MaScreens;
import com.api.central.master.entity.MaUserRoles;
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

@Service
@Transactional(readOnly = true)
public class MasterDelegateImpl implements MasterDelegate {

	@Autowired
	private MasterDao masterDao;

	/* saving and Updating audit code details */
	@Override
	@Transactional(readOnly = false)
	public MaAuditCodes saveAuditCodes(String flag, MaAuditCodes maAuditCodes) {
		return masterDao.saveAuditCodes(flag, maAuditCodes);
	}

	@Override
	public List<MaAuditCodes> getAuditCode() {
		return masterDao.getAuditCode();

	}

	@Override
	@Transactional(readOnly = false)
	public MaVessel saveVesselType(String flag, MaVessel maVessel) {
		return masterDao.saveVesselType(flag, maVessel);
	}

	@Override
	@Transactional(readOnly = false)
	public MaVesselCompany saveVesselCompany(String flag, MaVesselCompany maCompany) {
		return masterDao.saveVesselCompany(flag, maCompany);
	}

	public List<MaUsers> getEmail() {
		return masterDao.getEmail();
	}

	public List<MaIhmExemptionReason> getEcGrantedReason() {
		return masterDao.getEcGrantedReason();
	}

	public List<MaUsersWOPwd> getRoleUpdate(String flag, Long companyId) {
		return masterDao.getRoleUpdate(flag, companyId);
	}

	public List<MaUsersWOPwd> getCompanydetails(String id) {
		return masterDao.getCompanydetails(id);
	}

	@Override
	@Transactional(readOnly = false)
	public MaUsersWOPwd saveUserDetails(String flag, MaUsers mausers) {
		for (MaUserRoles roles : mausers.getRoles()) {
			roles.setUserRole(mausers);
		}
		return masterDao.saveUserDetails(flag, mausers);

	}

	@Override
	public List<MaAuditSubtype> getAuditSubtype(String value) {
		return masterDao.getAuditSubtype(value);
	}

	@Override
	@Transactional(readOnly = false)
	public UserDetailsConfig saveConfigDetails(UserDetailsConfig userconfig) {
		return masterDao.saveConfigDetails(userconfig);
	}

	@Override
	public List<UserDetailsConfig> getConfigDetails(String flag, Long companyId) {
		return masterDao.getConfigDetails(flag, companyId);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean updatePassword(String mail, String password) {
		return masterDao.updatePassword(mail, password);
	}
	@Override
	@Transactional(readOnly = false)
	public boolean updatePassword(String email, String password, String oldPwd) {
		return masterDao.updatePassword(email, password, oldPwd);
	}
	@Override
	public List<MaScreens> getDefaultHomeScreen() {
		return masterDao.getDefaultHomeScreen();
	}

	@Override
	@Transactional(readOnly = false)
	public UserDetailsConfig saveProfileImage(UserDetailsConfig userconfig) {
		return masterDao.saveProfileImage(userconfig);
	}

	public List<Long> getCompanyId() {
		return masterDao.getCompanyId();
	}

	@Override
	public List<CompanyImoChange> getCompanyImoHistory(Integer vesselImoNo, Long companyId, Integer pageNo) {
		return masterDao.getCompanyImoHistory(vesselImoNo, companyId, pageNo);
	}

	@Override
	public List<DocNumberChange> getDocHistory(String companyImoNo, Long companyId, Integer pageNo) {
		return masterDao.getDocHistory(companyImoNo, companyId, pageNo);
	}

	@Override
	public Integer getCompanyImoHistoryCount(Integer vesselImoNo, Long companyId) {
		return masterDao.getCompanyImoHistoryCount(vesselImoNo, companyId);
	}

	@Override
	public Integer getDocHistoryCount(String companyImoNo, Long companyId) {
		return masterDao.getDocHistoryCount(companyImoNo, companyId);
	}

	@Override
	public Map<String, Object> VerificationCodeNotification(String mail) {
		return masterDao.VerificationCodeNotification(mail);
	}

	public List<MaUsersWOPwd> checkRandomNumber(String id) {
		return masterDao.checkRandomNumber(id);
	}

	@Override
	@Transactional(readOnly = false)
	public MaUsersWOPwd updateLeadDetail(String userId, RmiAuditSigner ras, Long companyId) {
		return masterDao.updateLeadDetail(userId, ras, companyId);
	}

	@Override
	public void sendUserCreateMail(String flag, String emailId, Long companyId) {
		masterDao.sendUserCreateMail(flag, emailId, companyId);
	}

	@Override
	public Integer getStatusResponse(int value, String id, long cId) {
		// TODO Auto-generated method stub
		return masterDao.getStatusResponse(value, id, cId);

	}

	@Override
	public int getActiveStatusResponse(String id, long cId) {
		// TODO Auto-generated method stub
		return masterDao.getActiveStatusResponse(id, cId);
	}

	@Override
	public boolean checkVerificationCode(String userId, String verficationCode) {
		return masterDao.checkVerificationCode(userId, verficationCode);
	}

	@Override
	public Boolean checkUserPwd(String userId, String pwd) {
		return masterDao.checkUserPwd(userId, pwd);
	}

	@Override
	public Boolean checkUserLoggedin(String userId) {
		return masterDao.checkUserLoggedin(userId);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean updateUserLoginFlag(String userId, int flag) {
		return masterDao.updateUserLoginFlag(userId, flag);
	}

	@Override
	public List<LaptopPin> mpinCentralData(String userId) {
		return masterDao.mpinCentralData(userId);
	}

	@Override
	public List<LaptopPin> getMpinCentralData(String userId) {
		return masterDao.getMpinCentralData(userId);
	}

	@Override
	public String checkUserCredentials(String emailId, String password) {
		return masterDao.checkUserCredentials(emailId, password);
	}

	@Override
	public Boolean checkLpin(String emailId, String lpin, Long companyId) {
		return masterDao.checkLpin(emailId, lpin, companyId);
	}
	/**Added by archana For Jira-Id MOBILE-512 on 17-01-2023 Start here*/
	@Override
	public Boolean checkMpin(String emailId, String mpin, Long companyId) {
		return masterDao.checkMpin(emailId, mpin, companyId);
	}
	/**Added by archana For Jira-Id MOBILE-512 on 17-01-2023 end here*/

	@Override
	public Boolean checkActiveStatus(String emailId, Long companyId) {
		return masterDao.checkActiveStatus(emailId, companyId);
	}
	@Override
	public MaIhmExemptionReason checkEcReasonActiveStatus(Integer companyId) {
		return masterDao.checkEcReasonActiveStatus(companyId);
	}

	
	@Override
	@Transactional(readOnly = false)
	public MaVesselYatch saveMaVesselYatch(MaVesselYatch maVesselYeatchData) {

		return masterDao.saveMaVesselYatch(maVesselYeatchData);
	}

	@Override
	public List<MaVesselYatch> getMaVesselYatchData(Long companyId) {
		// TODO Auto-generated method stub
		return masterDao.getMaVesselYatchData(companyId);
	}

	@Override
	@Transactional(readOnly = false)
	public MaVesselCompanyYatch saveVesselCompanyYatch(String flag, MaVesselCompanyYatch maCompanyYatch) {
		// TODO Auto-generated method stub
		return masterDao.saveVesselCompanyYatch(flag, maCompanyYatch);
	}

	@Override
	@Transactional(readOnly = false)
	public MaPort saveMaportData(String flag, MaPort maPort) {
		return masterDao.saveMaportData(flag, maPort);
	}

	@Override
	public void updateLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer leadStatus) {
		// TODO Auto-generated method stub
		masterDao.updateLockStatus(auditTypeId, auditSeqNo, companyId, leadStatus);
	}
	
	@Override
	public void updateLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String lockholder) {
		// TODO Auto-generated method stub
		masterDao.updateLockHolder(auditTypeId, auditSeqNo, companyId, lockholder);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveSessionDetail(UserSessionDetail userSession) {
		// TODO Auto-generated method stub

		masterDao.saveSessionDetail(userSession);
	}

	@Override
	@Transactional(readOnly = false)
	public List<MaAttachmentTypes> saveOrUpdateReportAttach(List<ReportTypes> reportTypesList) {
		// Mapping ReportTypesList to MaAttachmentTypes List
		List<MaAttachmentTypes> maAttachmentTypesList = new ArrayList<>();

		for (ReportTypes reportTypes : reportTypesList) {
			MaAttachmentTypes typs = new MaAttachmentTypes();

			typs.setActiveStatus(reportTypes.getActiveStatus());
			typs.setAttachmentTypeDesc(reportTypes.getAttachmentTypeDesc());
			typs.setAttachmentTypeId(reportTypes.getAttachmentTypeId());
			typs.setAuditSubTypeId(reportTypes.getAuditSubTypeId());
			typs.setCompanyId(reportTypes.getCompanyId());
			if ((reportTypes.getAddNewReport()).intValue() == 1) {
				typs.setDateIns(new Date());
				typs.setLastUpdatedDate(new Timestamp(new Date().getTime()));
			} else {
				// below try/catch should be removed once you get only modified / added attachement types data.
				try {
					typs.setDateIns(new SimpleDateFormat("yyyy-mm-dd").parse(reportTypes.getDateIns().toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				typs.setLastUpdatedDate(new Timestamp(new Date().getTime()));
			}
			typs.setMandatory(reportTypes.getMandatory());
			typs.setUserIns(reportTypes.getUserIns());
			typs.setAuditTypeId(reportTypes.getAuditTypeId());
			maAttachmentTypesList.add(masterDao.saveOrUpdateReportAttach(typs));
		}
		// return masterDao.saveOrUpdateReportAttach(maAttachmentTypes);
		return maAttachmentTypesList;
	}

	@Override
	@Transactional(readOnly = false)
	public MaIhmExemptionReason saveOrUpdateEcGrantedReason(List<MaIhmExemptionReason> maIhmExemptionsReason) {
		// TODO Auto-generated method stub
		return masterDao.saveOrUpdateEcGrantedReason(maIhmExemptionsReason);

	}
	
	@Override
	@Transactional(readOnly = false)
	public String getLaptopPin(String userId) {
		return masterDao.getLaptopPin(userId);
	}
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
	@Override
	public String getUserIP(HttpServletRequest request){
		return masterDao.getUserIP(request);
	}
	@Override
	public String getPreviousDevice(String emailId){
		return masterDao.getPreviousDevice(emailId);
	}
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/
	
	
	/** Mobile PIN code Starts here*/
	@Override
	public List<LaptopPin> mobilepinCentralData(String id) {
		// TODO Auto-generated method stub
		return masterDao.mobilepinCentralData(id);
	}

	@Override
	public List<LaptopPin> getMobilepinCentralData(String id) {
		// TODO Auto-generated method stub
		return masterDao.getMobilepinCentralData(id);
	}

	@Override
	public String getMobilePin(String mail) {
		// TODO Auto-generated method stub
		return masterDao.getMobilePin(mail);
	}
	/** Mobile PIN code Ends here*/
}
