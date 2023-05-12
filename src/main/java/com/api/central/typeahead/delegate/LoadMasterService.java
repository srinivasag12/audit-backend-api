/*******************************************************************************
 * Copyright 2016 BSOL Systems- IVMASNG To Present - All rights reserved
 * $History:  � Filename: 
 * * *****************  Version 1  *****************
 * $Author:  � your name          Date: Current Date    Time: Current Time
 * $Created in /com/bsol/ivmas/XXXXX/XXXXX � package name
 *******************************************************************************/
package com.api.central.typeahead.delegate;


import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.api.central.config.AppHibSession;
import com.api.central.master.dao.MasterDao;
import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditRoles;
import com.api.central.master.entity.MaAuditSearchSource;
import com.api.central.master.entity.MaAuditStatus;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaAuditSummary;
import com.api.central.master.entity.MaAuditType;
import com.api.central.master.entity.MaCertificateIssueReason;
import com.api.central.master.entity.MaCertificateIssued;
import com.api.central.master.entity.MaCertificateReissueReason;
import com.api.central.master.entity.MaCompany;
import com.api.central.master.entity.MaFindingsCategory;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaIhmExemptionReason;
import com.api.central.master.entity.MaPort;
import com.api.central.master.entity.MaRegion;
import com.api.central.master.entity.MaRoles;
import com.api.central.master.entity.MaScreens;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.MaVesselType;
import com.api.central.master.entity.UserDetailsConfig;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.security.OAuth2RevokeConfiguration;
import com.api.central.typeahead.dao.TypeaheadDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service
public class LoadMasterService implements  ServletContextAware {
	@Autowired
	private TypeaheadDao typeaheadDao;
	
	@Autowired
	private MasterDao masterDao;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
				
		Collection<OAuth2AccessToken> AToken = tokenStore.findTokensByClientId("auditapp");
		String refreshToken = "";
		for(OAuth2AccessToken accessToken : AToken){
			if(accessToken != null){				
				OAuth2RevokeConfiguration.revokeToken(accessToken, tokenStore,refreshToken);
			}
		}
		
		Set<String> rToken = redisTemplate.keys("refresh:*");
		if(rToken.size()>0){
			for(String rvalue : rToken){
				String[] str = rvalue.split(":");
				OAuth2RefreshToken tokenVal = tokenStore.readRefreshToken(str[1]);
				tokenStore.removeRefreshToken(tokenVal);
			}
		}
		
		List<Long> companyIds = masterDao.getCompanyId();
		
		for (Long long1 : companyIds) {
			
			//setRedisData(long1);

		}
	}
	
	
	public void refreshMasterData(Long companyId){
		setRedisData(companyId);
	}
	
	public void setRedisData(Long companyId, String tableKeyInRedis){
		
		List<MaVessel> vessel=masterDao.getImoNo(companyId);
		redisTemplate.delete("vessel-"+companyId);
		redisTemplate.opsForValue().set("vessel-"+companyId,new Gson().toJson(vessel));
		
		/*Method m;
		try{
			
			m = masterDao.getClass().getMethod("getImoNo",Long.class);
			
			redisTemplate.delete(tableKeyInRedis+"-"+companyId);
			redisTemplate.opsForValue().set(tableKeyInRedis+"-"+companyId,new Gson().toJson(m.invoke(masterDao,companyId)));
			
		}catch (NoSuchMethodException e){
			e.printStackTrace();
		}catch (SecurityException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}*/
	}
	
	public void setRedisData(Long companyId){
		ObjectMapper mapper = new ObjectMapper();
		Log.info(companyId);
		List<MaAuditStatus> auditStatus =typeaheadDao.getAllAuditStatus(companyId);
		System.out.println("auditStatus"+auditStatus);
		redisTemplate.delete("auditStatus-"+companyId);
		redisTemplate.opsForValue().set("auditStatus-"+companyId,new Gson().toJson(auditStatus));
		
		List<MaAttachmentTypes> attachmentTypes=typeaheadDao.getAllAttachmentTypes(companyId);
		redisTemplate.delete("attachmentTypes-"+companyId);
		redisTemplate.opsForValue().set("attachmentTypes-"+companyId,new Gson().toJson(attachmentTypes));
		
		List<MaPort> port=masterDao.getAllPort(companyId);
		redisTemplate.delete("port-"+companyId);
		redisTemplate.opsForValue().set("port-"+companyId,new Gson().toJson(port));
		
		List<MaAuditCodes> auditCodes=masterDao.getAuditCodes(companyId);
		redisTemplate.delete("auditCodes-"+companyId);
		redisTemplate.opsForValue().set("auditCodes-"+companyId,new Gson().toJson(auditCodes));
		
		List<MaAuditRoles> auditRoles=typeaheadDao.getAuditRoles(companyId);
		redisTemplate.delete("auditRoles-"+companyId);
		redisTemplate.opsForValue().set("auditRoles-"+companyId,new Gson().toJson(auditRoles));
		
		List<MaAuditSearchSource> auditSearchSource=typeaheadDao.getAuditSearchSource(companyId);
		redisTemplate.delete("auditSearchSource-"+companyId);
		redisTemplate.opsForValue().set("auditSearchSource-"+companyId,new Gson().toJson(auditSearchSource));
		
		List<MaAuditSubtype> auditSubType=typeaheadDao.getAllAuditSubTypes(companyId);
		redisTemplate.delete("auditSubType-"+companyId);
		redisTemplate.opsForValue().set("auditSubType-"+companyId,new Gson().toJson(auditSubType));
		
		List<MaAuditSummary> auditSummary=typeaheadDao.getallAuditSummary(companyId);
		redisTemplate.delete("auditSummary-"+companyId);
		redisTemplate.opsForValue().set("auditSummary-"+companyId,new Gson().toJson(auditSummary));
		
		List<MaAuditType> auditType=masterDao.getAuditType(companyId);
		redisTemplate.delete("auditType-"+companyId);
		redisTemplate.opsForValue().set("auditType-"+companyId,new Gson().toJson(auditType));
		
		List<MaCertificateIssued> certIssue=typeaheadDao.getAllCertificates(companyId);
		redisTemplate.delete("certIssue-"+companyId);
		redisTemplate.opsForValue().set("certIssue-"+companyId,new Gson().toJson(certIssue));
		
		List<MaCompany> company=typeaheadDao.getAllCompanyDet(companyId);
		redisTemplate.delete("company-"+companyId);
		redisTemplate.opsForValue().set("company-"+companyId,new Gson().toJson(company));
		
		List<MaFindingsCategory> findings=typeaheadDao.getAllFindingCategory(companyId);
		redisTemplate.delete("findings-"+companyId);
		redisTemplate.opsForValue().set("findings-"+companyId,new Gson().toJson(findings));
		
		List<MaFindingsStatus> status=typeaheadDao.getAllFindingStatus(companyId);
		redisTemplate.delete("status-"+companyId);
		redisTemplate.opsForValue().set("status-"+companyId,new Gson().toJson(status));
		
		List<MaRoles> rolesDet=masterDao.getMaRoles(companyId);
		redisTemplate.delete("rolesDet-"+companyId);
		redisTemplate.opsForValue().set("rolesDet-"+companyId,new Gson().toJson(rolesDet));
		
		
		List<MaUsersWOPwd> users = masterDao.getUsers(companyId);

		redisTemplate.delete("users-" + companyId);
		try {
			redisTemplate.opsForValue().set("users-" + companyId, mapper.writeValueAsString(users));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		List<MaVessel> vessel=masterDao.getImoNo(companyId);
		redisTemplate.delete("vessel-"+companyId);
		redisTemplate.opsForValue().set("vessel-"+companyId,new Gson().toJson(vessel));
		
		List<MaVesselCompany> vesselcmpny=masterDao.getCmpnyDet(companyId);
		redisTemplate.delete("vesselcmpny-"+companyId);
		redisTemplate.opsForValue().set("vesselcmpny-"+companyId,new Gson().toJson(vesselcmpny));
		
		List<MaVesselType> vesselType=masterDao.getVesselTypes(companyId);
		redisTemplate.delete("vesselType-"+companyId);
		redisTemplate.opsForValue().set("vesselType-"+companyId,new Gson().toJson(vesselType));
		
		List<MaScreens> screenData=masterDao.getScreenDetails(companyId);
		redisTemplate.delete("screenData-"+companyId);
		redisTemplate.opsForValue().set("screenData-"+companyId,new Gson().toJson(screenData));
		
		
		List<MaCertificateIssueReason> CertificateIssueReason=masterDao.getCertificateIssueReason(companyId);
		System.out.print("sfdf");
		redisTemplate.delete("CertificateIssueReason-"+companyId);
		redisTemplate.opsForValue().set("CertificateIssueReason-"+companyId,new Gson().toJson(CertificateIssueReason));
		
		List<MaIhmExemptionReason> EcGrantedReason=masterDao.getEcGrantedReason();
		System.out.print("sfdf");
		redisTemplate.delete("EcGrantedReason-");
		redisTemplate.opsForValue().set("EcGrantedReason-",new Gson().toJson(EcGrantedReason));
		
		List<MaRegion> region=masterDao.getRegion(companyId);
		redisTemplate.delete("region-"+companyId);
		redisTemplate.opsForValue().set("region-"+companyId,new Gson().toJson(region));
		
		List<MaCertificateReissueReason> CertificateReissueReason=masterDao.getCertificateReissueReason(companyId);
		System.out.print("sfdf");
		redisTemplate.delete("CertificateReissueReason-"+companyId);
		redisTemplate.opsForValue().set("CertificateReissueReason-"+companyId,new Gson().toJson(CertificateReissueReason));
		
		List<UserDetailsConfig> DetailsConfig=masterDao.getDetailsConfig(companyId);
		redisTemplate.delete("DetailsConfig-"+companyId);
		redisTemplate.opsForValue().set("DetailsConfig-"+companyId,new Gson().toJson(DetailsConfig));
		
	}
	
	/*saving AuditCodes in redis db*/
	public void updateAuditCodedData(String status,long companyId,String codes ,Long auditTypeId)
	{
		TypeToken<List<MaAuditCodes>> auditToken = new TypeToken<List<MaAuditCodes>>(){};
		List<MaAuditCodes> maAuditCodeList = new Gson().fromJson(redisTemplate.opsForValue().get("auditCodes-"+companyId),auditToken.getType()) ;
		List<MaAuditCodes> masterList= masterDao.updateAuditCodes(codes,companyId,auditTypeId);
		if(status.equals("UPDATE")){
			maAuditCodeList.removeIf(mb->mb.getCompanyId().equals(companyId)&&mb.getAuditCode().equals(codes)&&mb.getAuditTypeId().equals(auditTypeId));
		}
		maAuditCodeList.addAll(masterList);
		redisTemplate.opsForValue().set("auditCodes-"+companyId,new Gson().toJson(maAuditCodeList));
	}
	
	public void updatevesseldata(String status,long companyId,Integer vesselImo)
	{
		
		
		TypeToken<List<MaVessel>> auditToken = new TypeToken<List<MaVessel>>(){};
		List<MaVessel> maVesselList = new Gson().fromJson(redisTemplate.opsForValue().get("vessel-"+companyId),auditToken.getType()) ;
		List<MaVessel> masterList= masterDao.updateVessel(companyId,vesselImo);
		maVesselList.removeIf(mb->mb.getCompanyId().equals(companyId)&&mb.getVesselImoNo().equals(vesselImo));
		maVesselList.addAll(masterList);
		redisTemplate.opsForValue().set("vessel-"+companyId,new Gson().toJson(maVesselList));
	}

	public void updateCompanydata(String status, Long companyId, String companyImoNo) {
	
		
		TypeToken<List<MaVesselCompany>> auditToken = new TypeToken<List<MaVesselCompany>>(){};
		List<MaVesselCompany> maVesselCompanyList = new Gson().fromJson(redisTemplate.opsForValue().get("vesselcmpny-"+companyId),auditToken.getType()) ;
		List<MaVesselCompany> masterList= masterDao.updateVesselCompany(companyId,companyImoNo);
		maVesselCompanyList.removeIf(mb->mb.getCompanyId().equals(companyId)&&mb.getCompanyImoNo().equals(companyImoNo));
		maVesselCompanyList.addAll(masterList);
		redisTemplate.opsForValue().set("vesselcmpny-"+companyId,new Gson().toJson(maVesselCompanyList));
	}

	public void updateUserdata(String status, String emailId, String sequenceNo, Long companyId) {

		ObjectMapper mapper = new ObjectMapper();
		List<MaUsersWOPwd> users = masterDao.getUsers(companyId);

		redisTemplate.delete("users-" + companyId);
		try {
			redisTemplate.opsForValue().set("users-" + companyId, mapper.writeValueAsString(users));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}		
	}
	
      public void updateUserdataManager(String status, String emailId, Long officilId, Long companyId) {
		
    	  
		TypeToken<List<MaUsers>> auditToken = new TypeToken<List<MaUsers>>(){};
		List<MaUsersWOPwd> maUserList = new Gson().fromJson(redisTemplate.opsForValue().get("users-"+companyId),auditToken.getType()) ;
		List<MaUsersWOPwd> masterList= masterDao.updateUserdataManager(companyId,officilId,emailId);
		if(status.equals("UPDATE")){
			maUserList.removeIf(mb->mb.getCompanyId().equals(companyId)&&(mb.getOfficialId()!=null &&  mb.getOfficialId().equals(officilId) )&&mb.getEmailId().equals(emailId));
			}
		maUserList.addAll(masterList);
		redisTemplate.opsForValue().set("users-"+companyId,new Gson().toJson(maUserList));
		
	}


	public void updateUserConfigDetails(String userId, Long companyId) {
		
		/*List<UserDetailsConfig> userConfigList=masterDao.getConfigDetail(companyId);
		System.out.println("number of users******* "+userConfigList.size());
		redisTemplate.delete("DetailsConfig-"+companyId);
		redisTemplate.opsForValue().set("DetailsConfig-"+companyId,new Gson().toJson(userConfigList));*/
		
		TypeToken<List<UserDetailsConfig>> auditToken = new TypeToken<List<UserDetailsConfig>>(){};
		List<UserDetailsConfig> userConfigList = new Gson().fromJson(redisTemplate.opsForValue().get("DetailsConfig-"+companyId),auditToken.getType()) ;
		List<UserDetailsConfig> masterList= masterDao.updateUserConfigDetails(companyId,userId);
		userConfigList.removeIf(mb->mb.getCompanyId()!=null && mb.getCompanyId().equals(companyId) && mb.getUserId()!=null &&  mb.getUserId().equals(userId));
		userConfigList.addAll(masterList);
		redisTemplate.opsForValue().set("DetailsConfig-"+companyId,new Gson().toJson(userConfigList));
		
	}
	
	public String saveOrUpdateReportAttach(Long companyId) {
		
		List<MaAttachmentTypes> attachmentTypes=typeaheadDao.getAllAttachmentTypes(companyId);
		redisTemplate.delete("attachmentTypes-"+companyId  );
		redisTemplate.opsForValue().set("attachmentTypes-"+companyId,new Gson().toJson(attachmentTypes));
		return "UPDATE";
	}
	
public String redisUpdateExemptionReasons() {
		
		List<MaIhmExemptionReason> exemptionReasons=typeaheadDao.redisUpdateExemptionReasons();
		redisTemplate.delete("EcGrantedReason-"  );
		redisTemplate.opsForValue().set("EcGrantedReason-",new Gson().toJson(exemptionReasons));
		return "UPDATE";
	}
	
	
  public boolean saveOrUpdatePort(Long companyId) {
	
	List<MaPort> port=masterDao.getAllPort(companyId);
	redisTemplate.delete("port-"+companyId);
	redisTemplate.opsForValue().set("port-"+companyId,new Gson().toJson(port));
	return true;
	}
	
}