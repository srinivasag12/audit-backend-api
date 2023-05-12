/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MasterController.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/controller/MasterController.java $
**/
package com.api.central.master.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.master.delegate.MasterDelegate;
import com.api.central.master.entity.CompanyImoChange;
import com.api.central.master.entity.DocNumberChange;
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
import com.api.central.master.model.MaUserMapper;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.master.model.PsswordReset;
import com.api.central.master.model.ReportTypes;
import com.api.central.security.OAuth2RevokeConfiguration;
import com.api.central.typeahead.delegate.LoadMasterService;


@RestController
@RequestMapping("api/master")
public class MasterController {

	@Autowired
	private MasterDelegate masterDelegate;

	@Autowired
	public LoadMasterService loadMasterService;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	public OAuth2RevokeConfiguration oauth2Revoke;

	@GetMapping(value = "/getDefaultHomeScreen")
	public ResponseEntity<List<MaScreens>> getDefaultHomeScreen() {
		return new ResponseEntity<List<MaScreens>>(masterDelegate.getDefaultHomeScreen(), HttpStatus.OK);
	}

	/* saving and Updating audit code details */
	@PostMapping(value = "/saveAuditCodes/{flag}")
	public ResponseEntity<MaAuditCodes> saveAuditCodes(@PathVariable String flag,
			@RequestBody MaAuditCodes maAuditCodes) {
		MaAuditCodes result = masterDelegate.saveAuditCodes(flag, maAuditCodes);
	
		//if (result.toString().length() >= 1) {
			loadMasterService.updateAuditCodedData(flag, result.getCompanyId(), result.getAuditCode(),
					result.getAuditTypeId());

		//}

		return new ResponseEntity<MaAuditCodes>(result, HttpStatus.OK);
	}

	/* for getting audit subtypes */
	@GetMapping(value = "/getAuditSubType/{auditType}")
	public ResponseEntity<List<MaAuditSubtype>> getAuditSubtype(@PathVariable("auditType") String value) {
		return new ResponseEntity<List<MaAuditSubtype>>(masterDelegate.getAuditSubtype(value), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/getActiveStatusResponse/{comapnyId}")
	public Map<String, Object> getActiveStatusResponse(@PathVariable("comapnyId") long cId,@RequestBody String data) {
		
		JSONObject jsonObj = new JSONObject(data);
		String id = jsonObj.getString("emailId");
		Map<String, Object> map = new HashMap<String, Object>();
		int result = masterDelegate.getActiveStatusResponse(id,cId);
		map.put("success", result);
		return map;
      
		
	}
	@PostMapping(value = "/getStatusResponse/{auditTypeId}/{comapnyId}")
	public Map<String, Object> getStatusResponse(@PathVariable("auditTypeId") int value,@PathVariable("comapnyId") long cId,@RequestBody String data) {
		
		JSONObject jsonObj = new JSONObject(data);
		String id = jsonObj.getString("emailId");
		Map<String, Object> map = new HashMap<String, Object>();
		int result = masterDelegate.getStatusResponse(value,id,cId);
		map.put("success", result);
		return map;
      
		
	}

	/* saving vessel details */
	@PostMapping(value = "/saveVesselDatas/{flag}")
	public ResponseEntity<MaVessel> saveVesselType(@PathVariable String flag, @RequestBody MaVessel maVessel) {
		MaVessel result = masterDelegate.saveVesselType(flag, maVessel);
		if (result.toString().length() >= 1) {
			loadMasterService.updatevesseldata(flag, maVessel.getCompanyId(), maVessel.getVesselImoNo());
		}
		return new ResponseEntity<MaVessel>(result, HttpStatus.OK);

	}

	/* saving and updating vessel company details */
	@PostMapping(value = "/saveVesselCompany/{flag}")
	public ResponseEntity<MaVesselCompany> saveVesselCompany(@PathVariable String flag,
			@RequestBody MaVesselCompany maCompany) {
		MaVesselCompany result = masterDelegate.saveVesselCompany(flag, maCompany);
		if (result.toString().length() >= 1) {
			loadMasterService.updateCompanydata(flag, maCompany.getCompanyId(), maCompany.getCompanyImoNo());
		}
		return new ResponseEntity<MaVesselCompany>(result, HttpStatus.OK);
	}

	/* creating typeaheads for email in update mode */
	@GetMapping(value = "/getEmail")
	public ResponseEntity<List<MaUsersWOPwd>> getEmail() {
		return new ResponseEntity<List<MaUsersWOPwd>>(new MaUserMapper().getMaUsersWOPwdList(masterDelegate.getEmail()), HttpStatus.OK);
	}

	
	@GetMapping(value = "/getEcGrantedReason")
	public ResponseEntity<List<MaIhmExemptionReason>> getEcGrantedReason() {
		return new ResponseEntity<List<MaIhmExemptionReason>>(masterDelegate.getEcGrantedReason(), HttpStatus.OK);
	}
	
	
	/* saving and updating user Details */
	@PostMapping(value = "/saveUserDetails/{flag}", produces = "application/json")
	public ResponseEntity<MaUsersWOPwd> saveUserDetails(@PathVariable String flag, @RequestBody MaUsers mausers) {
		//Added by sudharsan for JIRA-id: 5318 on 13-06-2022
		if(flag.equals("UPDATEREVIEW")){
			if(mausers.getPlanApprovalReview()==0){
				mausers.setPlanApprovalReview(1);
			}
			else{
				mausers.setPlanApprovalReview(0);
			}
			masterDelegate.saveUserDetails(flag, mausers);
		}
		//End here Jira-id: 5318
		
		MaUsersWOPwd result = masterDelegate.saveUserDetails(flag, mausers);
		masterDelegate.sendUserCreateMail(flag,mausers.getEmailId(),mausers.getCompanyId());
		
		if (result.toString().length() >= 1) {
			if(mausers.getRoles().get(0).getRoleId()==1003){ 
				
				loadMasterService.updateUserdataManager(flag, mausers.getEmailId(), mausers.getOfficialId(),
						mausers.getCompanyId());
			}else{
			loadMasterService.updateUserdata(flag, mausers.getEmailId(), mausers.getSequenceNo(),
					mausers.getCompanyId());
			}
		}
		
		if(flag.equals("UPDATE") && mausers.getActiveStatus() == 0){
			
			masterDelegate.updateUserLoginFlag(mausers.getEmailId(),0);
			
			Collection<OAuth2AccessToken> AToken = tokenStore.findTokensByClientIdAndUserName("auditapp",mausers.getEmailId());		
			String refreshToken = "";

			for(OAuth2AccessToken accessToken : AToken){
				if(accessToken != null){
					OAuth2RevokeConfiguration.revokeToken(accessToken, tokenStore,refreshToken);
				}
			}
			Boolean removeRtoken = true;
			Set<String> rToken = redisTemplate.keys("refresh:*");
			if(rToken.size()>0){
				oauth2Revoke.getRefreshToken(rToken,tokenStore,mausers.getEmailId(),removeRtoken);
			}
		}
		return new ResponseEntity<MaUsersWOPwd>(result, HttpStatus.OK);

	}

	/* population of all fields in update mode based on emailId */
	@PostMapping(value = "/getRoleUpdate/{companyId}")
	public ResponseEntity<List<MaUsersWOPwd>> getRoleUpdate(@RequestBody String flag, @PathVariable Long companyId) {
		JSONObject jsonObj = new JSONObject(flag);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<MaUsersWOPwd>>(masterDelegate.getRoleUpdate(id, companyId), HttpStatus.OK);
	}
	
	

	@PostMapping(value = "/getCompanydetails")
	public ResponseEntity<List<MaUsersWOPwd>> getCompanydetails(@RequestBody String flag) {
		JSONObject jsonObj = new JSONObject(flag);
		String id = jsonObj.getString("emailId");
		
		return new ResponseEntity<List<MaUsersWOPwd>>(masterDelegate.getCompanydetails(id), HttpStatus.OK);
	}

	@PostMapping(value = "/saveConfigDetails", produces = "application/json")
	public ResponseEntity<UserDetailsConfig> saveConfigDetails(@RequestBody UserDetailsConfig userconfig) {

		UserDetailsConfig result = masterDelegate.saveConfigDetails(userconfig);
		if (result.toString().length() >= 1) {
			loadMasterService.updateUserConfigDetails(userconfig.getUserId(),userconfig.getCompanyId());
		}
		return new ResponseEntity<UserDetailsConfig>(result, HttpStatus.OK);

	

		//return new ResponseEntity<UserDetailsConfig>(masterDelegate.saveConfigDetails(userconfig), HttpStatus.OK);

	}

	@PostMapping(value = "/saveProfileImage", produces = "application/json")
	public ResponseEntity<UserDetailsConfig> saveProfileImage(@RequestBody UserDetailsConfig userconfig) {

		UserDetailsConfig result = masterDelegate.saveProfileImage(userconfig);
		if (result.toString().length() >= 1) {
			loadMasterService.updateUserConfigDetails(userconfig.getUserId(),userconfig.getCompanyId());
		}
		return new ResponseEntity<UserDetailsConfig>(result, HttpStatus.OK);

	}

	@GetMapping(value = "/getConfigDetails/{flag}/{companyId}")
	public ResponseEntity<List<UserDetailsConfig>> getConfigDetails(@PathVariable String flag,
			@PathVariable Long companyId) {
		return new ResponseEntity<List<UserDetailsConfig>>(masterDelegate.getConfigDetails(flag, companyId),
				HttpStatus.OK);

	}

	/** updating resetted password **/
	@PostMapping(value = "/updatePassword")
	public Map<String, Object> updatePassword(@RequestBody PsswordReset psswordReset ) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = masterDelegate.updatePassword(psswordReset.getEmail(), psswordReset.getNewPwd(), psswordReset.getOldPwd());
		map.put("success", result);
		return map;
	}

	/*@GetMapping(value = "/getRedisData")
	public String getRedisData() {
		List<Long> companyIds = masterDelegate.getCompanyId();
		for (Long long1 : companyIds) {
			loadMasterService.setRedisData(long1);
		}
		return "Redis Data Updated Successfully";
	}*/
	
	@GetMapping(value = "/updateRedisDataForPerticularTable/{companyId}/{tableKeyInRedis}")
	public String updateRedisDataForPerticularTable(@PathVariable Long companyId,@PathVariable String tableKeyInRedis) {
		
			loadMasterService.setRedisData(companyId,tableKeyInRedis);
		
		return "Redis Data for "+ tableKeyInRedis +"Updated Successfully";
	}

	

	@GetMapping(value = "/getCompanyImoHistoryCount/{vesselImoNo}/{companyId}")
	public ResponseEntity<Integer> getCompanyImoHistoryCount(@PathVariable Integer vesselImoNo,
			@PathVariable Long companyId) {
		return new ResponseEntity<Integer>(masterDelegate.getCompanyImoHistoryCount(vesselImoNo, companyId),
				HttpStatus.OK);
	}

	@GetMapping(value = "/getCompanyImoHistoryResult/{vesselImoNo}/{companyId}/{pageNo}")
	public ResponseEntity<List<CompanyImoChange>> getCompanyImoHistoryResult(@PathVariable Integer vesselImoNo,
			@PathVariable Long companyId, @PathVariable Integer pageNo) {
		return new ResponseEntity<List<CompanyImoChange>>(
				masterDelegate.getCompanyImoHistory(vesselImoNo, companyId, pageNo), HttpStatus.OK);
	}

	@GetMapping(value = "/getDocHistoryCount/{companyImoNo}/{companyId}")
	public ResponseEntity<Integer> getDocHistoryCount(@PathVariable String companyImoNo, @PathVariable Long companyId) {
		return new ResponseEntity<Integer>(masterDelegate.getDocHistoryCount(companyImoNo, companyId), HttpStatus.OK);
	}

	@GetMapping(value = "/getDocHistoryResult/{companyImoNo}/{companyId}/{pageNo}")
	public ResponseEntity<List<DocNumberChange>> getDocHistoryResult(@PathVariable String companyImoNo,
			@PathVariable Long companyId, @PathVariable Integer pageNo) {
		return new ResponseEntity<List<DocNumberChange>>(masterDelegate.getDocHistory(companyImoNo, companyId, pageNo),
				HttpStatus.OK);
	}

	

	
	@PostMapping(value = "/saveMaVesselYatch/{companyId}/{userId}")
	public ResponseEntity<MaVesselYatch> saveMaVesselYatch(@RequestBody MaVesselYatch MaVesselYatch,@PathVariable Long companyId,@PathVariable String userId ) {
	System.out.println("qazswsxss"+ MaVesselYatch);
	
			MaVesselCompanyYatch vesselCompany = new MaVesselCompanyYatch();
		    MaVesselYatch     vessel = null; 
		    System.out.println("745454="+ MaVesselYatch.getCompanyImoNo());
		
		if(MaVesselYatch!=null){
		    System.out.println("14575==888"+ MaVesselYatch.getCompanyImoNo()+" dd"+MaVesselYatch.getDocTypeNumber());
			vesselCompany.setCompanyImoNo(MaVesselYatch.getCompanyImoNo());
			vesselCompany.setDocTypeNo(MaVesselYatch.getDocTypeNumber());
			vesselCompany.setDocIssuer(MaVesselYatch.getDocIssuer());
			vesselCompany.setDocExpiry(MaVesselYatch.getDocExpiry());
			vesselCompany.setVesselCompanyAddress(MaVesselYatch.getCompanyAddress());
			vesselCompany.setCompanyId(MaVesselYatch.getCompanyId());
			vesselCompany.setUserIns(MaVesselYatch.getUserIns());
			vesselCompany.setDateIns(new java.sql.Date(new Date().getTime()));
			vesselCompany.setCompanyStatus(MaVesselYatch.getCompanyStatus());
			vesselCompany.setVesselCompanyName(MaVesselYatch.getCustomerName());
			
			masterDelegate.saveVesselCompanyYatch("CREATE", vesselCompany);
		}
		
		return new ResponseEntity<MaVesselYatch>(masterDelegate.saveMaVesselYatch(MaVesselYatch), HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/getMaVesselYatchData/{companyId}")
	public ResponseEntity getMaVesselYatchData(@PathVariable Long companyId) {
		
		return new ResponseEntity<List<MaVesselYatch>>(masterDelegate.getMaVesselYatchData(companyId),HttpStatus.OK);
	}
	/* @PostMapping(value = "/auditFindings/{status}/{findingType}/{auditTypeId}/{companyId}")
	public ResponseEntity<AuditFinding> auditFindings(@RequestBody AuditFinding auditFinding,
			@PathVariable String status, @PathVariable String findingType,@PathVariable Integer auditTypeId,@PathVariable Long companyId) {

		auditFinding = auditDelegate.auditFindings(auditFinding, status, findingType,auditTypeId,companyId);

		return new ResponseEntity<AuditFinding>(auditFinding, HttpStatus.CREATED);
	}*/
	
	
	@PostMapping(value = "/saveMaportData/{flag}")
	public ResponseEntity<MaPort> saveMaportData(@RequestBody MaPort maport,@PathVariable String flag) {
		
		return new ResponseEntity<MaPort>(masterDelegate.saveMaportData(flag,maport),HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/saveOrUpdateReportAttach")
	public ResponseEntity<List<MaAttachmentTypes>> saveOrUpdateReportAttach(@RequestBody List<ReportTypes> ReportTypesList) {
		return new ResponseEntity<List<MaAttachmentTypes>>(masterDelegate.saveOrUpdateReportAttach(ReportTypesList),HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/saveOrUpdateEcGrantedReason")
	public ResponseEntity<MaIhmExemptionReason> saveOrUpdateEcGrantedReason(@RequestBody List<MaIhmExemptionReason> maIhmExemptionsReason) {
		return new ResponseEntity<MaIhmExemptionReason>(masterDelegate.saveOrUpdateEcGrantedReason(maIhmExemptionsReason),HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/checkEcReasonActiveStatus/{reasonId}")
	public ResponseEntity<MaIhmExemptionReason> checkEcReasonActiveStatus(@PathVariable Integer reasonId){ 
		return new ResponseEntity<MaIhmExemptionReason>(masterDelegate.checkEcReasonActiveStatus(reasonId),HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/redisUpdateAttachmentReport/{companyId}")
	public ResponseEntity<String> updateAttachmentReport(@PathVariable Long companyId){ 
		return new ResponseEntity<String>(loadMasterService.saveOrUpdateReportAttach(companyId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/redisUpdateExemptionReasons")
	public ResponseEntity<String> redisUpdateExemptionReasons(){ 
		return new ResponseEntity<String>(loadMasterService.redisUpdateExemptionReasons(),HttpStatus.OK);
	}
	
	

}