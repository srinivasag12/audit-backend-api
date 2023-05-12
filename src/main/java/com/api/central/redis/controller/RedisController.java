package com.api.central.redis.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.audit.delegate.NotifyService;
import com.api.central.audit.delegate.SearchDelegate;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.certificate.delegate.CertificateDelegate;
import com.api.central.master.delegate.MasterDelegate;
import com.api.central.master.entity.LaptopPin;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.typeahead.delegate.LoadMasterService;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;



@RestController
@RequestMapping("redis")
public class RedisController {
	
	@Autowired
	private MasterDelegate masterDelegate;

	@Autowired
	public LoadMasterService loadMasterService;
	
	@Autowired
	private CertificateDelegate certificateDelegate;
	
	@Autowired
	private SearchDelegate searchDelegate;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	AppUtil appUtil;
	
	@Autowired
	private NotifyService notifyservice;
	
	private static final Logger log = LoggerFactory.getLogger(RedisController.class);
	
	@GetMapping(value = "/getRedisData")
	public String getRedisData() {
		List<Long> companyIds = masterDelegate.getCompanyId();
		for (Long long1 : companyIds) {
			loadMasterService.setRedisData(long1);
		}
		return "Redis Data Updated Successfully";
	}
	
	@GetMapping(value="/getVersionId")
	public ResponseEntity<Map<String,String>> getVersionId(){		//changed by @ramya for jira id - IRI-5559
		String getVersion=notifyservice.checkVersionId();
		Map<String, String> lapVersion = new HashMap<String, String>();
		lapVersion.put("versionId", getVersion);
		return new ResponseEntity<Map<String,String>>(lapVersion, HttpStatus.OK);
	}

	@PostMapping(value = "/updateLaptopSoftElectron")
	public ResponseEntity<Map<String, Object>> updateLaptopSoftElectron() {
		
		log.info("In updateLaptopSoftElectron()");
		Map<String, Object> zipMap = new HashMap<String, Object>();
		
		try {
			
			log.info("insde try");
			
			String distZipFile = servletContext.getContextPath() + AppConstant.SEPARATOR + "app" + AppConstant.ZIP;
//		    String distZipFile = "F:/IRI/codeBase/LaptopAuditElectron/dist/win-unpacked/resources/app.zip";
			
			File file = new File(distZipFile);
			if (file.exists()) {
				log.info("File exists");
			}
			log.info("paths - "+distZipFile);
			byte[] bsDistZipFile = null;

			bsDistZipFile = appUtil.getFileByte(distZipFile);
			
			log.info("byte length  - "+bsDistZipFile.length);
			
			zipMap.put("distZipFile", bsDistZipFile);
			
			log.info("zipMap size - "+zipMap.size());
			
			return new ResponseEntity<Map<String, Object>>(zipMap, HttpStatus.OK);
			
		} catch (Exception e) {
			
			log.info("zipMap errrr - "+zipMap.size());
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(zipMap, HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/certificateViewerDetails/{companyId}")
	public ResponseEntity<List<CertificateDetail>> getCertificateData(@PathVariable Long companyId,@RequestParam String qid) throws Throwable {
		
		 String CertNoAndUtn=certificateDelegate.decryptCrypto(qid);
	     String GetUtn=CertNoAndUtn.split(" ")[1];
	     String getCertNo=CertNoAndUtn.split(" ")[0];
	     	     
		return new ResponseEntity<List<CertificateDetail>>(certificateDelegate.getCertificateData(companyId,GetUtn,getCertNo), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditCertificateData/{ImoNo}/{companyId}/{certificateNo}/{auditTypeId}/{auditDate}")
	public  ResponseEntity<List<CertificateDetail>> getCertificateDetails(@PathVariable Integer ImoNo,@PathVariable Long companyId,@PathVariable String certificateNo,@PathVariable Integer auditTypeId, @PathVariable String auditDate, @PathVariable boolean directInterorAdd) { //added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536
		return new  ResponseEntity<List<CertificateDetail>>(searchDelegate.getAuditCertificateData(ImoNo,companyId,certificateNo,auditTypeId, appUtil.convertStringToDate(auditDate),directInterorAdd), HttpStatus.OK);
	
	}
	
	/*** Updating Verification code in db ***/
	@PostMapping(value = "/verificationCodeNotification")
	public Map<String, Object> verificationCodeNotification(@RequestBody String mail) {
		JSONObject jsonObj = new JSONObject(mail);
		String id = jsonObj.getString("emailId");
		Map<String, Object> map = new HashMap<String, Object>();
		 Map<String, Object> result = masterDelegate.VerificationCodeNotification(id);
		map.put("success", result);
		return map;
	}
	
	@PostMapping(value = "/checkRandomNumber")
	public ResponseEntity<List<MaUsersWOPwd>> checkRandomNumber(@RequestBody String flag) {
		JSONObject jsonObj = new JSONObject(flag);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<MaUsersWOPwd>>(masterDelegate.checkRandomNumber(id), HttpStatus.OK);
	}
	
	/** updating resetted password **/
	@PostMapping(value = "/updateLoginPassword/{mail}/{password}")
	public Map<String, Object> updateLoginPassword(@PathVariable String mail, @PathVariable String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = masterDelegate.updatePassword(mail, password);
		map.put("success", result);
		return map;
	}
	
	@GetMapping(value = "/checkVerificationCode/{userId}/{verficationCode}")
	public ResponseEntity<Boolean> checkVerificationCode(@PathVariable String userId, @PathVariable String verficationCode) {
		
		return new ResponseEntity<Boolean>(masterDelegate.checkVerificationCode(userId,verficationCode), HttpStatus.OK);
	}
	
	@PostMapping(value = "/checkUserInfo")
	public ResponseEntity<Boolean> checkUserPwd(@RequestBody String checkPwd) {
		JSONObject jsonObj = new JSONObject(checkPwd);
		String userId = jsonObj.getString("emailId");
		String pwd = jsonObj.getString("pwd");
		return new ResponseEntity<Boolean>(masterDelegate.checkUserPwd(userId,pwd), HttpStatus.OK);
	}
	
	@GetMapping(value = "/checkUserLoggedin")
	public ResponseEntity<Boolean> checkUserLoggedin(@RequestParam("username") String emailId) {		
		return new ResponseEntity<Boolean>(masterDelegate.checkUserLoggedin(emailId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/updateUserloginFlag")
	public ResponseEntity<Boolean> updateUserloginFlag(@RequestParam("username") String emailId) {
		int flag = 0;
		return new ResponseEntity<Boolean>(masterDelegate.updateUserLoginFlag(emailId,flag), HttpStatus.OK);
	}
	
	@PostMapping(value = "/checkUserCredentials")
	public ResponseEntity<Map<String,String>> checkUserCredentials(@RequestBody String checkUserDtls) {
		
		JSONObject jsonObj = new JSONObject(checkUserDtls);
		String emailId = jsonObj.getString("emailId");
		String lpin = jsonObj.getString("lpin");
		Long companyId = jsonObj.getLong("companyId");

		Boolean checkLapUnique = masterDelegate.checkLpin(emailId,lpin,companyId);
		Boolean userPresent = masterDelegate.checkActiveStatus(emailId,companyId);
		
		HttpStatus checkStatus = null;
		Map<String,String> map = new HashMap<String,String>();
		
		if(checkLapUnique){
			if(!userPresent){
				map.put("data", "Inactive");
				checkStatus = HttpStatus.OK;
			}else{
				map.put("data", "ok");
				checkStatus = HttpStatus.OK;
			}
		}else{
			map.put("data", "invalid laptop pin");
			checkStatus = HttpStatus.OK;
		}
		return new ResponseEntity<Map<String,String>>(map, checkStatus);
	}
	
	@PostMapping(value = "/mpinCentralData")
	public ResponseEntity<List<LaptopPin>> mpinCentralData(@RequestBody String userId) {
		JSONObject jsonObj = new JSONObject(userId);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<LaptopPin>>(masterDelegate.mpinCentralData(id),HttpStatus.OK);
	}
	
	@PostMapping(value = "/getMpinCentralData")
	public ResponseEntity<List<LaptopPin>> getMpinCentralData(@RequestBody String userId) {
		JSONObject jsonObj = new JSONObject(userId);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<LaptopPin>>(masterDelegate.getMpinCentralData(id),HttpStatus.OK);
	}
	
	@GetMapping(value="/updateLockStatusDelAdt/{auditTypeId}/{auditSeqNo}/{companyId}/{emailId}/{leadStatus}/{lpin}")
	public ResponseEntity<Boolean> updateLockStatusDelAdt(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable String emailId,@PathVariable Integer leadStatus, @PathVariable String lpin){
		
		Boolean checkLapUnique = masterDelegate.checkLpin(emailId, lpin, companyId);
		log.info("lpin, checkLapUnique => " + lpin + ", " + checkLapUnique);
		
		if(checkLapUnique){
			masterDelegate.updateLockStatus(auditTypeId, auditSeqNo, companyId, leadStatus);
			masterDelegate.updateLockHolder(auditTypeId,auditSeqNo,companyId,"");
		}		
		
		return new ResponseEntity<Boolean>(checkLapUnique,HttpStatus.OK);
	}
	
	/**Added by archana For Jira-Id MOBILE-512 on 17-01-2023 Start here*/
	@GetMapping(value="/updateLockStatusDelMobAdt/{auditTypeId}/{auditSeqNo}/{companyId}/{emailId}/{leadStatus}/{mpin}")
	public ResponseEntity<Boolean> updateLockStatusDelMobAdt(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable String emailId,@PathVariable Integer leadStatus, @PathVariable String mpin){
		
		Boolean checkMobUnique = masterDelegate.checkMpin(emailId, mpin, companyId);
		log.info("mpin, checkMobUnique => " + mpin + ", " + checkMobUnique);
		
		if(checkMobUnique){
			masterDelegate.updateLockStatus(auditTypeId, auditSeqNo, companyId, leadStatus);
			masterDelegate.updateLockHolder(auditTypeId,auditSeqNo,companyId,"");
		}		
		
		return new ResponseEntity<Boolean>(checkMobUnique,HttpStatus.OK);
	}
	/**Added by archana For Jira-Id MOBILE-512 on 17-01-2023 end here*/
	
	@PostMapping(value = "/getLaptopPin")
	public String getLaptopPin(@RequestBody String mail) {
		String result = masterDelegate.getLaptopPin(mail);
		return result;
	}
	
	/** Mobile PIN code Starts here*/
	@PostMapping(value = "/mobilepinCentralData")
	public ResponseEntity<List<LaptopPin>> mobilepinCentralData(@RequestBody String userId) {
		JSONObject jsonObj = new JSONObject(userId);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<LaptopPin>>(masterDelegate.mobilepinCentralData(id),HttpStatus.OK);
	}
	
	@PostMapping(value = "/getMobilepinCentralData")
	public ResponseEntity<List<LaptopPin>> getMobilepinCentralData(@RequestBody String userId) {
		JSONObject jsonObj = new JSONObject(userId);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<LaptopPin>>(masterDelegate.getMobilepinCentralData(id),HttpStatus.OK);
	}
	
	@PostMapping(value = "/getMobilePin")
	public String getMobilePin(@RequestBody String mail) {
		String result = masterDelegate.getMobilePin(mail);
		return result;
	}
	/**MOBILE PIN code End here*/
}
