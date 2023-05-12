/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History â€“ File Name MasterDaoImpl.java $
******************  Version 1.0.0 *****************
* & Author â€“ Tharani priya   DateTime â€“ Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/dao/MasterDaoImpl.java $
**/

package com.api.central.master.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.api.central.audit.dao.AuditDaoImpl;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.config.AppHibSession;
import com.api.central.master.entity.CompanyImoChange;
import com.api.central.master.entity.DocNumberChange;
import com.api.central.master.entity.LaptopPin;
import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaAuditType;
import com.api.central.master.entity.MaCertificateIssueReason;
import com.api.central.master.entity.MaCertificateReissueReason;
import com.api.central.master.entity.MaCompany;
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
import com.api.central.master.model.MaUserMapper;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.rmiaudit.entity.RmiAuditSigner;
import com.api.central.typeahead.dao.TypeaheadDao;
import com.api.central.typeahead.delegate.LoadMasterService;
import com.api.central.util.AppConstant;
import com.api.central.util.AppSQLConstant;  

@Component
@SuppressWarnings("unchecked")
@Repository
@Transactional
public class MasterDaoImpl implements MasterDao {
	
	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	private TypeaheadDao typeaheadDao;
	
	private static final Logger log = LoggerFactory.getLogger(MasterDaoImpl.class);
	
	@Autowired
	private LoadMasterService LoadMasterService;
		
	
	/* Fetching values for auditType Dropdown from auditType db */
	@Override
	public List<MaAuditType> getAuditType(Long companyId) {

		Criteria cr = hibsession.getSession().createCriteria(MaAuditType.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaAuditType> maList = cr.list();
		return maList;
	}

	@Override
	public List<MaUserRoles> getUserRoles() {

		Criteria cr = hibsession.getSession().createCriteria(MaUserRoles.class);
		List<MaUserRoles> roleList = cr.list();
		return roleList;
	}
	
	@Override
	public List<MaUsersWOPwd> getUsers(Long companyId) {

		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("companyId", companyId));
				
		List<MaUsers> usersList = cr.list();
		return new MaUserMapper().getMaUsersWOPwdList(usersList);
	}
	
	@Override
	public List<MaUsersWOPwd> getUserDetails(Long companyId,String emailId) {

		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("emailId", emailId));
		List<MaUsers> usersList = cr.list();
		return new MaUserMapper().getMaUsersWOPwdList(usersList);
	}
	
	/* creating typeaheads for auditCode in update mode */
	@Override
	public List<MaAuditCodes> getAuditCodes(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaAuditCodes.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaAuditCodes> caList = cr.list();
		return caList;

	}

	/* saving and Updating audit code details */
	@Override
	@Transactional
	public MaAuditCodes saveAuditCodes(String flag, MaAuditCodes maAuditCodes) {
		if (flag.equals("CREATE")) {
			hibsession.getSession().save(maAuditCodes);
		} else if (flag.equals("UPDATE")) {
			hibsession.getSession().merge(maAuditCodes);
		}
		return maAuditCodes;

	}

	/* validation for unique auditCode */
	public List<MaAuditCodes> getAuditCode() {
		Criteria cr = hibsession.getSession().createCriteria(MaAuditCodes.class);
		List<MaAuditCodes> caList = cr.list();
		return caList;

	}

	/* Fetching values for VesselType Dropdown from VesselType db */
	@Override
	public List<MaVesselType> getVesselTypes(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaVesselType.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaVesselType> maList = cr.list();
		return maList;

	}

	/* creating typeaheads for Imono and vesselName in update mode */
	@Override
	public List<MaVessel> getImoNo(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaVessel.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaVessel> maList = cr.list();
		return maList;

	}

	/* Fetching values from vesselcompany db for companyImono */
	@Override
	public List<MaVesselCompany> getCompanyImoNo() {
		Criteria cr = hibsession.getSession().createCriteria(MaVesselCompany.class);
		List<MaVesselCompany> maList = cr.list();
		return maList;

	}

	/* saving vessel details */
	@Override
	@Transactional
	public MaVessel saveVesselType(String flag, MaVessel maVessel) {
		if (flag.equals("CREATE")) {	
			hibsession.getSession().saveOrUpdate(maVessel);
		} else if (flag.equals("UPDATE")) {		
			hibsession.getSession().merge(maVessel);
		}
		return maVessel;

	}

	

	
	/* saving and updating vessel company details */
	@Override
	public MaVesselCompany saveVesselCompany(String flag, MaVesselCompany maCompany) {
		if (flag.equals("CREATE")) {
			hibsession.getSession().saveOrUpdate(maCompany);
		} else if (flag.equals("UPDATE")) {
			hibsession.getSession().merge(maCompany);
		}

		return maCompany;
	}
	
	/* saving and updating Port details */
	@Override
	public MaPort saveMaportData(String flag, MaPort maPort) {
		boolean tempFlag=false;
		
		if (flag.equals("CREATE")) {
			try{
			Object count = hibsession.getSession().createSQLQuery("select max(PORT_ID) from ma_port").uniqueResult();
		    Long j= Long.parseLong(count.toString());
		    maPort.setPortId( (j+1));
		 	hibsession.getSession().saveOrUpdate(maPort);
		 	tempFlag= true;
			} catch(NumberFormatException ex){
				// handle your exception
			}
				
		} else if (flag.equals("UPDATE")) {
			hibsession.getSession().merge(maPort);
			tempFlag= true;
		}
		if (tempFlag && maPort.getCompanyId()!=null) {
			LoadMasterService.saveOrUpdatePort(maPort.getCompanyId());
		}
		

		return maPort;
	}
	/*
	 * Fetching values for Vesselname and company Dropdown from
	 * VesselCompanyDetails db
	 */
	@Override
	public List<MaVesselCompany> getCmpnyDet(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaVesselCompany.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaVesselCompany> maList = cr.list();
		return maList;

	}

	
	

	/* creating typeaheads for email in update mode */
	@Override
	public List<MaUsers> getEmail() {
		List<MaUsers> maList = hibsession.getSession().createCriteria(MaUsers.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("emailId"))
						.add(Projections.property("firstName"))
						.add(Projections.property("lastName"))
						.add(Projections.property("activeStatus"))
						.add(Projections.property("signature"))
						.add(Projections.property("phoneNo"))
						.add(Projections.property("address"))
						.add(Projections.property("userIdentification"))
						.add(Projections.property("sequenceNo"))
						.add(Projections.property("password")))
				.list();
		return maList;
	}

	/*population of all fields in update mode based on emailId*/
	@Override
	public List<MaUsersWOPwd> getRoleUpdate(String flag,Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("emailId", flag).ignoreCase())
				.add(Restrictions.eq("companyId", companyId));
		List<MaUsers> userList = cr.list();
		return new MaUserMapper().getMaUsersWOPwdList(userList);
	}
	
	
	
	@Override
	public List<MaUsersWOPwd> getCompanydetails(String flag) {
		System.out.println("emailId");
		System.out.println(flag);
		
		List<MaUsers> userList = null;
		try{
		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("emailId", flag).ignoreCase());
		
		 userList = cr.list();
		System.out.println(userList);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return new MaUserMapper().getMaUsersWOPwdList(userList);
	}

	@Override
	public List<MaRoles> getMaRoles(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaRoles.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaRoles> masList = cr.list();
		return masList;

	}

	/* saving and updating user Details */
	@Override
	public MaUsersWOPwd saveUserDetails(String flag, MaUsers mausers) {
		if (flag.equals("CREATE")) {
			hibsession.getSession().save(mausers);
		} else if (flag.equals("UPDATE")) {
			hibsession.getSession().merge(mausers);
		}
		//Added by sudharsan for JIRA-ID:5318 on 13-06-2022
		else if (flag.equals("UPDATEREVIEW")){
			hibsession.getSession().merge(mausers);
		}
		//End here JIRA-ID:5318
		return new MaUserMapper().getMaUsersWOPwd(mausers);
	}

	@Override
	public List<MaAuditSubtype> getAuditSubtype(String value) {
		Criteria criteria = hibsession.getSession().createCriteria(MaAuditSubtype.class);
		criteria.add(Restrictions.eq("auditTypeId", Long.parseLong(value)));
		List<MaAuditSubtype> maList = criteria.list();
		return maList;

	}

	@Override
	public UserDetailsConfig saveConfigDetails(UserDetailsConfig userconfig) {
		hibsession.getSession().merge(userconfig);
		return userconfig;
	}

	@Override
	public List<UserDetailsConfig> getConfigDetails(String flag,Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(UserDetailsConfig.class)
				.add(Restrictions.eq("userId", flag).ignoreCase()).add(Restrictions.eq("companyId", companyId));
		List<UserDetailsConfig> crList = cr.list();
		return crList;
	}
	
	@Override
	public List<UserDetailsConfig> getConfigDetail(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(UserDetailsConfig.class)
				.add(Restrictions.eq("companyId", companyId));
		List<UserDetailsConfig> crList = cr.list();
		return crList;
	}

	/** updating resetted password **/
	@Override
	public boolean updatePassword(String emailId, String password, String OldPwd) {
		// try {
		// password = URLDecoder.decode(password, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// get the use details

		int result = 0;
		Query getUserDetails = hibsession.getSession().createQuery("from MaUsers u where u.emailId =:email");
		getUserDetails.setString("email", emailId);

		List<MaUsers> users = getUserDetails.list();

		if (users.get(0).getPassword().equals(OldPwd)) {

			Query passupdate = hibsession.getSession()
					.createQuery("update MaUsers set password = :password where emailId=:emailId");
			passupdate.setString("password", password);
			passupdate.setString("emailId", emailId);
			result = passupdate.executeUpdate();
		}
		if (result == 1)
			return true;
		else
			return false;
}

	@Override
	public List<MaScreens> getDefaultHomeScreen() {
		Criteria cr = hibsession.getSession().createCriteria(MaScreens.class);
		List<MaScreens> maList = cr.list();
		return maList;
	}

	@Override
	public List<Long> getCompanyId() {
		
		List<Long> maList = hibsession.getSession().createCriteria(MaCompany.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("companyId")))
				.list();
		return maList;
		
	}

	@Override
	public List<MaScreens> getScreenDetails(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaScreens.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaScreens> maList = cr.list();
		return maList;
	}

	@Override
	public List<UserDetailsConfig> getDetailsConfig(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(UserDetailsConfig.class)
				.add(Restrictions.eq("companyId", companyId));
		List<UserDetailsConfig> maList = cr.list();
		return maList;
	}

	
	
	@Override
	public UserDetailsConfig saveProfileImage(UserDetailsConfig userconfig) {
		hibsession.getSession().save(userconfig);
		return userconfig;
	}

	@Override
	public List<CompanyImoChange> getCompanyImoHistory(Integer vesselImoNo, Long companyId,Integer pageNo) {
		Criteria cr = hibsession.getSession().createCriteria(CompanyImoChange.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("companyId", companyId))
				.setFirstResult(pageNo)
				.addOrder(Order.desc("seqNo"))
				.setMaxResults(5);
		return cr.list();
	}

	@Override
	public List<DocNumberChange> getDocHistory(String companyImoNo, Long companyId,Integer pageNo) {
		Criteria cr = hibsession.getSession().createCriteria(DocNumberChange.class)
				.add(Restrictions.eq("companyImoNo", companyImoNo))
				.add(Restrictions.eq("companyId", companyId))
				.setFirstResult(pageNo)
				.addOrder(Order.desc("seqNo"))
				.setMaxResults(5);
		return cr.list();
	}

	@Override
	public Integer getCompanyImoHistoryCount(Integer vesselImoNo, Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(CompanyImoChange.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("companyId", companyId));
		return cr.list().size();
	}

	@Override
	public Integer getDocHistoryCount(String companyImoNo, Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(DocNumberChange.class)
				.add(Restrictions.eq("companyImoNo", companyImoNo))
				.add(Restrictions.eq("companyId", companyId));
		return cr.list().size();
	}

	/*** Updating Verification code in db ***/
	@Override
	public Map<String, Object> VerificationCodeNotification(String emailId) {
		
		Map<String,Object> maps = new HashMap<String,Object>();
		 String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	        StringBuilder salt = new StringBuilder();
	        Random rnd = new Random();
	        while (salt.length() < 6) { // length of the random string.
	            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	            salt.append(SALTCHARS.charAt(index));
	        }
	        String verificationCode = salt.toString();
		Query passupdate = hibsession.getSession().createQuery("update MaUsers set verificationCode = :verificationCode where emailId=:emailId");
		passupdate.setString("verificationCode",verificationCode);
		passupdate.setString("emailId",emailId);
		int result=passupdate.executeUpdate();
		
		
		
		
		
       
		if(result==1)
		{
			Criteria cr=hibsession.getSession().createCriteria(MaUsers.class)
					.setProjection(Projections.projectionList()
							.add(Projections.property("activeStatus"))
							.add(Projections.property("password")))
					        .add(Restrictions.eq("emailId", emailId));
			maps.put("result",true);
			maps.put("oldValue", cr.list());
		}
		else
		{
			maps.put("result",false);
			maps.put("oldValue", null);
		}
		return maps;
	}

	@Override
	public List<MaUsersWOPwd> checkRandomNumber(String emailId) {
		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("emailId", emailId));
		List<MaUsers> userList = cr.list();
		return new MaUserMapper().getMaUsersWOPwdList(userList);
	}

	@Override
	public List<MaPort> getAllPort(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaPort.class)
				.add(Restrictions.eq("companyId", companyId))
				.addOrder(Order.asc("portId"));
		List<MaPort> caList = cr.list();
		return caList;
	}

	@Override
	public List<MaCertificateIssueReason> getCertificateIssueReason(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaCertificateIssueReason.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaCertificateIssueReason> certIssueResonList = cr.list();

		return certIssueResonList;
	}

	@Override
	public List<MaIhmExemptionReason> getEcGrantedReason() {
		Criteria cr = hibsession.getSession().createCriteria(MaIhmExemptionReason.class);
		//		.add(Restrictions.eq("activeStatus", 1));
		List<MaIhmExemptionReason> ecGrantedResonList = cr.list();

		return ecGrantedResonList;
	}
	
	@Override
	public MaUsersWOPwd updateLeadDetail(String userId, RmiAuditSigner ras, Long companyId) {
		
		MaUsers user = null;
		
		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("emailId", userId));
		
		user = (MaUsers)cr.uniqueResult();
		user.setSignature(ras.getSignature());
		
		hibsession.getSession().update(user);
		
		return new MaUserMapper().getMaUsersWOPwd(user);
	}

	@Override
	public List<MaAuditCodes> updateAuditCodes(String codes, long companyId, Long auditTypeId) {
		Criteria cr = hibsession.getSession().createCriteria(MaAuditCodes.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId))
				.add(Restrictions.eq("auditCode", codes))
				.add(Restrictions.eq("companyId", companyId));
		List<MaAuditCodes> caList = cr.list();
		return caList;
	}

	@Override
	public List<MaVessel> updateVessel(long companyId, Integer vesselImo) {
		Criteria cr = hibsession.getSession().createCriteria(MaVessel.class)
				.add(Restrictions.eq("vesselImoNo", vesselImo))
				.add(Restrictions.eq("companyId", companyId));
		List<MaVessel> caList = cr.list();
		return caList;
	}

	@Override
	public List<MaVesselCompany> updateVesselCompany(Long companyId, String companyImoNo) {
		Criteria cr = hibsession.getSession().createCriteria(MaVesselCompany.class)
				.add(Restrictions.eq("companyImoNo", companyImoNo))
				.add(Restrictions.eq("companyId", companyId));
		List<MaVesselCompany> caList = cr.list();
		return caList;
	}

	@Override
	public List<MaUsersWOPwd> updateUserdata(Long companyId, String sequenceNo, String emailId) {
		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("sequenceNo", sequenceNo))
				.add(Restrictions.eq("emailId", emailId))
				.add(Restrictions.eq("companyId", companyId));
		List<MaUsers> caList = cr.list();
		return new MaUserMapper().getMaUsersWOPwdList(caList);
	}

	@Override
	public List<UserDetailsConfig> updateUserConfigDetails(Long companyId, String userId) {
		Criteria cr = hibsession.getSession().createCriteria(UserDetailsConfig.class)
				.add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("companyId", companyId));
		List<UserDetailsConfig> caList = cr.list();
		return caList;
	}

	@Override
	public void sendUserCreateMail(String flag, String emailId, Long companyId) {

		Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL USER_ADDED_INTO_AUD(:status, :userId, :companyId)");
		callStoredProcedure_MYSQL.setString("status", flag);
		callStoredProcedure_MYSQL.setString("userId", emailId);
		callStoredProcedure_MYSQL.setLong("companyId", companyId);
		callStoredProcedure_MYSQL.executeUpdate();
		
	}

	@Override
	public List<MaRegion> getRegion(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MaRegion.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaRegion> maList = cr.list();
		return maList;
	}

	@Override
	public Integer getStatusResponse(int value, String id, long cId) {
		
		Criteria cr = hibsession.getSession().createCriteria(AuditDetailView.class)
				.add(Restrictions.eq("userId", id))
				.add(Restrictions.eq("auditTypeId", value))
				.add(Restrictions.ne("auditStatusId", 1002))
				.add(Restrictions.eq("companyId", cId));
		List<AuditDetailView> maList = cr.list();
	    int listLength=maList.size();
	   
		return listLength;
	}

	@Override
	public Integer getActiveStatusResponse(String id, long cId) {
		
		Criteria cr = hibsession.getSession().createCriteria(AuditDetailView.class)
				.add(Restrictions.eq("userId", id))
				.add(Restrictions.ne("auditStatusId", 1002))
				.add(Restrictions.eq("companyId", cId));
		List<AuditDetailView> maList = cr.list();
	    int listLength=maList.size();
	  
	    
	    
		return listLength;
	}

	@Override
	public boolean checkVerificationCode(String userId, String verificationCode) {
		
		
		String sql = "select count(user_id) from ma_users where user_id=:userId and verification_code=:verificationCode";
		
		int count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("userId", userId).setParameter("verificationCode", verificationCode).uniqueResult().toString());
		
		return count==1 ? true : false; 
	}

	@Override
	public Boolean checkUserPwd(String userId, String pwd) {
		

        
		try {
			pwd = URLDecoder.decode(pwd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
		String sql = "select count(user_id) from ma_users where user_id=:userId and password=:pwd";
		
		int count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("userId", userId).setParameter("pwd", pwd).uniqueResult().toString());
		
		return count==1 ? true : false;
	}

	@Override
	public Boolean checkUserLoggedin(String userId) {

		String sql = "select (case when check_user is not null then check_user else 0 end) as checkLoggedIn from ma_users  where user_Id=:userId";		
		int count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("userId", userId).uniqueResult().toString());
				
		return count==1 ? true : false;
	}

	@Override
	public Boolean updateUserLoginFlag(String userId, int flag) {
				
		Query userFlag = hibsession.getSession().createSQLQuery("update MA_USERS set CHECK_USER =:flag where user_Id =:userId");
		userFlag.setInteger("flag", flag);
		userFlag.setString("userId", userId);
		userFlag.executeUpdate();
		
		return true;
	}

	@Override
	public List<LaptopPin> mpinCentralData(String emailId) {
		// TODO Auto-generated method stub
		
		 String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	        StringBuilder salt = new StringBuilder();
	        Random rnd = new Random();
	        while (salt.length() < 16) { // length of the random string.
	            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	            salt.append(SALTCHARS.charAt(index));
	        }
	        String lapUniqId = salt.toString();
		log.info("lpin"+emailId);
		
		log.info("userId"+lapUniqId);  
        Date date=new Date();
//		Query userFlag = hibsession.getSession().createSQLQuery("update LAPTOP_PIN set LAP_UNIQUE_ID =:lapUniqId where USER_ID =:emailId");
		Query userFlag = hibsession.getSession().createSQLQuery("update LAPTOP_PIN set LAP_UNIQUE_ID =:lapUniqId,DATE_INS =:date where USER_ID =:emailId");
		userFlag.setString("lapUniqId", lapUniqId);
		userFlag.setString("emailId", emailId);
		userFlag.setDate("date", date);
		userFlag.executeUpdate();
		Criteria cr = hibsession.getSession().createCriteria(LaptopPin.class)
				.add(Restrictions.eq("emailId", emailId));
				
		List<LaptopPin> maList = cr.list();
	    
	  
	    
	    
		
		return maList;
	}

	@Override
	public List<LaptopPin> getMpinCentralData(String emailId) {
		Criteria cr = hibsession.getSession().createCriteria(LaptopPin.class)
				.add(Restrictions.eq("emailId", emailId));
				
		List<LaptopPin> maList = cr.list();
	   return maList;
	}

	@Override
	public String checkUserCredentials(String emailId, String pwd) {
		
		String userFlag = hibsession.getSession().createSQLQuery("SELECT CASE active_status WHEN 1 THEN 'active'  WHEN 0 THEN 'inactive' END AS status FROM ma_users  WHERE user_id = :userId AND password = :pwd")
				.setString("userId", emailId)
				.setString("pwd", pwd).uniqueResult().toString();
	
		return userFlag;
	}

	@Override
	public Boolean checkLpin(String emailId, String lpin, Long companyId) {

		String sql = "select count(user_id) from LAPTOP_PIN where user_id=:userId and LAP_UNIQUE_ID=:lpin AND COMPANY_ID=:companyId";

		int count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("userId", emailId).setParameter("lpin", lpin).setParameter("companyId", companyId).uniqueResult().toString());

		return count==1 ? true : false;
	}
	
	/**Added by archana For Jira-Id MOBILE-512 on 17-01-2023 Start here*/
	@Override
	public Boolean checkMpin(String emailId, String mpin, Long companyId) {

		String sql = "select count(user_id) from LAPTOP_PIN where user_id=:userId and MOB_UNIQUE_ID=:mpin AND COMPANY_ID=:companyId";

		int count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("userId", emailId).setParameter("mpin", mpin).setParameter("companyId", companyId).uniqueResult().toString());

		return count==1 ? true : false;
	}
	/**Added by archana For Jira-Id MOBILE-512 on 17-01-2023 end here*/
	
	@Override
	public Boolean checkActiveStatus(String emailId,  Long companyId) {

		String sql = "";

		if(companyId == 0)
			sql = "select active_status from ma_users where user_id=:userId";
		else
			sql = "select active_status from ma_users where user_id=:userId AND COMPANY_ID=:companyId";

		
		Query query = hibsession.getSession().createSQLQuery(sql).setParameter("userId", emailId);
		
		if(companyId != 0)
			query.setParameter("companyId", companyId);
		
		int count = Integer.parseInt(query.uniqueResult().toString());

		return count==1 ? true : false;
	}
	
	
	@Override
	public MaIhmExemptionReason checkEcReasonActiveStatus( Integer reasonId) {

		
	//	CertificateIhmDetail
		Criteria cr = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
				.add(Restrictions.eq("condEcGrant", reasonId))
				.add(Restrictions.eq("publishStatus", 0))
				.add(Restrictions.in("certIssueId", new Object[]{AppConstant.FULL_TERM_CERT,AppConstant.RE_ISSUE}))
				.setProjection(Projections.rowCount());
			
	
		List countList = cr.list();
		Long rowCount = null;
					if (countList!=null) {
			             rowCount = (Long) countList.get(0);
			            System.out.println("Total Results:" + rowCount);
			        }
		MaIhmExemptionReason reason = new MaIhmExemptionReason();
		reason.setReasonCount(rowCount > 0 ? true : false);

		return reason;
	}
	
	@Override
	public List<MaCertificateReissueReason> getCertificateReissueReason(Long companyId) {
		
		Criteria cr = hibsession.getSession().createCriteria(MaCertificateReissueReason.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaCertificateReissueReason> certReissueResonList = cr.list();
		log.info("reissue"+certReissueResonList);
		return certReissueResonList;
	}
	
	@Override
	@Transactional
	public MaVesselYatch saveMaVesselYatch(MaVesselYatch maVesselYeatchData) {
		
		hibsession.getSession().saveOrUpdate(maVesselYeatchData);

		return maVesselYeatchData;
	}

	
	
	
	
	
	
	
	
	@Override
	public List<MaVesselYatch> getMaVesselYatchData(Long companyId) {

		Criteria cr = hibsession.getSession().createCriteria(MaVesselYatch.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaVesselYatch> maListYatch = cr.list();
		
		
		return maListYatch;
		
	}

	@Override
	@Transactional
	public MaVesselCompanyYatch saveVesselCompanyYatch(String flag, MaVesselCompanyYatch maCompanyYatch) {
		// TODO Auto-generated method stub
		if (flag.equals("CREATE")) {
			hibsession.getSession().saveOrUpdate(maCompanyYatch);
		} else if (flag.equals("UPDATE")) {
			hibsession.getSession().merge(maCompanyYatch);
		}

		return maCompanyYatch;
		
	}
	
	@Override
	public void updateLockStatus(Integer auditTypeId, Integer auditSequenceNo, Long companyId, int i) {

		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_STATUS);
		updateAudit.setInteger("lockStatus", i);
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSequenceNo);
		updateAudit.executeUpdate();

	}
	
	// update lock holder while deleting in shipboard
	@Override
	public void updateLockHolder(Integer auditTypeId, Integer auditSequenceNo, Long companyId, String lockHolder) {

		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_HOLDER);
		if(lockHolder.equals(" ")){
			updateAudit.setString("lockHolder", "");
		}else{
			updateAudit.setString("lockHolder", lockHolder);
		}
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSequenceNo);
		updateAudit.executeUpdate();

	}
	

@Override
	public List<MaUsersWOPwd> updateUserdataManager(Long companyId, Long offialId, String emailId) {
		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("officialId", offialId))
				.add(Restrictions.eq("emailId", emailId))
				.add(Restrictions.eq("companyId", companyId));
		List<MaUsers> caList = cr.list();
		return new MaUserMapper().getMaUsersWOPwdList(caList);
	}

/* saving and Updating saveSessionDetail */
@Override
@Transactional
public void saveSessionDetail(UserSessionDetail userSession) {
	Integer count= null; 
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022  
	Date date =  new Date();
	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
	
	if(userSession.getUpdateMode()!=null && userSession.getUpdateMode()==1 )
	{
	log.info("in update mode....");
	try{
		String sql = "select MAX(SL_NO) from USER_SESSION_DETAIL where USER_ID=:userId";
		count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql).setString("userId", userSession.getUserId()).uniqueResult().toString());
		}catch(Exception e){
			count=1; 
			log.info("USER_SESSION_DETAIL max error"+e);
		}
	
	try{
	   //String s="update UserSessionDetail set status = :status,logoutTime =:logoutTime  where userId=:emailId  AND slNo=:slNo";
		String s="update UserSessionDetail set status = :status,logoutTime =:logoutTime  where userId=:emailId  AND logoutTime=null";
		Query sessionUpdate = hibsession.getSession().createQuery(s);
		sessionUpdate.setString("emailId",userSession.getUserId());
		sessionUpdate.setString("status","Inactive");
		sessionUpdate.setString("logoutTime", formatter.format(new Date()).toString());  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 

		//sessionUpdate.setInteger("slNo", count);
		sessionUpdate.executeUpdate();
		
		
	}catch(Exception e){
		e.printStackTrace();
	}
		
	}else{
		log.info("in creation mode....");
		if(userSession.getUserId()!=null) {
			log.info("in updating status text...");
			try{
				String statusQuery = "SELECT STATUS FROM USER_SESSION_DETAIL WHERE SL_NO=(SELECT MAX(SL_NO) FROM USER_SESSION_DETAIL WHERE USER_ID=:userId)";
				String status = hibsession.getSession().createSQLQuery(statusQuery).setString("userId", userSession.getUserId()).uniqueResult().toString();
				log.info("saveSessionDetail resp - " + status);
				if(status.equals("Active")) {
					String updateQuery = "UPDATE USER_SESSION_DETAIL SET STATUS = 'Inactive', logoutTime =:logoutTime WHERE SL_NO=(SELECT MAX(SL_NO) FROM USER_SESSION_DETAIL WHERE USER_ID=:userId)";
					String ss  = hibsession.getSession().createSQLQuery(updateQuery).setString("userId", userSession.getUserId()).setString("logoutTime",formatter.format(new Date()).toString()).uniqueResult().toString();  //changed by sudharsan For Jira-Id IRI-5482 on 16-09-2022
					log.info("saveSessionDetail final resp - " + ss);
				}
			}catch(Exception e){ }
		}	
	userSession.setLoginTime(formatter.format(new Date()).toString());  //changed by sudharsan For Jira-Id IRI-5482 on 16-09-2022 
	try{
	String sql = "select MAX(SL_NO) from USER_SESSION_DETAIL";
	
	 count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql).uniqueResult().toString());
	if(count==0 || count==null){
		
	}else count=count+1;
	}catch(Exception e){
		count=1;
		
	}

	if( userSession.getUserId()!=null && userSession.getCompanyId()!=null)
	{
		userSession.setSlNo(count);
		hibsession.getSession().save(userSession);
	}
	
}
}

/* saving and Updating master ma report data */
@Override
@Transactional
public MaAttachmentTypes saveOrUpdateReportAttach(MaAttachmentTypes MaAttachmentTypes) {
	// TODO Auto-generated method stub
	try{
	hibsession.getSession().saveOrUpdate(MaAttachmentTypes);
	}catch(Exception e) {
		log.info("saveOrUpdateReportAttach ", e);
	}
	return MaAttachmentTypes;
}

@Override
@Transactional
public MaIhmExemptionReason saveOrUpdateEcGrantedReason(List<MaIhmExemptionReason> maIhmExemptionsReason) {
	try{
	for(MaIhmExemptionReason data : maIhmExemptionsReason) {
		if(data.getReasonId() == 0) {
			DetachedCriteria maxId = DetachedCriteria.forClass(MaIhmExemptionReason.class)
				    .setProjection( Projections.max("reasonId") );
			
			Criteria cr = hibsession.getSession().createCriteria(MaIhmExemptionReason.class)
					.add( Property.forName("reasonId").eq(maxId));
			 List<MaIhmExemptionReason> ls = cr.list();

			data.setReasonId(ls.get(0).getReasonId() + 1);
		}
	hibsession.getSession().saveOrUpdate(data);
	}
	}catch(Exception e) {
		log.info("saveOrUpdateEcGrantedReason ", e);
	}
	
	return maIhmExemptionsReason.get(0);
}

@Override
public boolean updatePassword(String emailId, String password) {
	try {
		password = URLDecoder.decode(password, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Query passupdate = hibsession.getSession()
			.createQuery("update MaUsers set password = :password where emailId=:emailId");
	passupdate.setString("password", password);
	passupdate.setString("emailId", emailId);
	int result = passupdate.executeUpdate();
	if (result == 1)
		return true;
	else
		return false;

}

@Override
public String getLaptopPin(String emailId)
{
	String sql = "select lap_unique_id from laptop_pin where user_id=:userId";
	
	String result =hibsession.getSession().createSQLQuery(sql).setParameter("userId", emailId).uniqueResult().toString();
	return result;
	
}
/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
@Override
public String getUserIP(HttpServletRequest request){

String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];

}

// Modified by sudharsan for Jira-ID = 5608 start here
@Override
public String getPreviousDevice(String emailId){
	String device = "Web";
	String query = "select device from UserSessionDetail where slNo = (select max(slNo) from UserSessionDetail where userId=:emailId)";
	try{
	 device = hibsession.getSession().createQuery(query).setString("emailId", emailId).uniqueResult().toString();
	}
	catch(Exception exception){
		System.err.println("Exception"+exception);
	}
	System.out.println(device);
	return device;
}
// Jira-ID = 5608 End here
/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/

@Override
public List<LaptopPin> mobilepinCentralData(String emailId) {
			// TODO Auto-generated method stub
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 16) { // length of the random string.
		    int index = (int) (rnd.nextFloat() * SALTCHARS.length());
		    salt.append(SALTCHARS.charAt(index));
		}
		String mobUniqId = salt.toString();
		log.info("lpin"+emailId);
		log.info("userId"+mobUniqId);  
		Date date=new Date();
		Query userFlag = hibsession.getSession().createSQLQuery("update LAPTOP_PIN set MOB_UNIQUE_ID =:mobUniqId,DATE_INS =:date where USER_ID =:emailId");
		userFlag.setString("mobUniqId", mobUniqId);
		userFlag.setString("emailId", emailId);
		userFlag.setDate("date", date);
		userFlag.executeUpdate();
		Criteria cr = hibsession.getSession().createCriteria(LaptopPin.class)
				.add(Restrictions.eq("emailId", emailId));			
		List<LaptopPin> maList = cr.list();
		return maList;

}

@Override
public List<LaptopPin> getMobilepinCentralData(String emailId) {

	Criteria cr = hibsession.getSession().createCriteria(LaptopPin.class)
			.add(Restrictions.eq("emailId", emailId));
	List<LaptopPin> maList = cr.list();
   return maList;
}

@Override
public String getMobilePin(String emailId) {
	String sql = "select mob_unique_id from laptop_pin where user_id=:userId";
	
	String result =hibsession.getSession().createSQLQuery(sql).setParameter("userId", emailId).uniqueResult().toString();
	return result;
}
}
