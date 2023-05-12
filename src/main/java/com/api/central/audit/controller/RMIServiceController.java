package com.api.central.audit.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.api.central.master.delegate.MasterDelegate;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.UserSessionDetail;
import com.api.central.master.entity.VesselBean;
import com.api.central.rmiaudit.entity.RmiAuditSigner;
import com.api.central.rmiaudit.entity.RmiAuditUTN;
import com.api.central.rmiaudit.entity.RmiIhmCustomers;
import com.api.central.typeahead.delegate.LoadMasterService;
import com.api.central.util.AppConstant;
import com.api.central.util.RestTemplateUtil;

@RestController
@RequestMapping("api/rmiService")
public class RMIServiceController {

	/*
	 * @Autowired private RMIServiceDelegate interDelegate;
	 */
	@Autowired
	public LoadMasterService loadMasterService;
	@Autowired
	private MasterDelegate masterDelegate;

	// @Autowired
	// private RestTemplate restTemplate;

	@Autowired
	private RestTemplateUtil restUtil;

	/* to get specific vessel detail from client(RMI) */
	@GetMapping(value = "/vesselSpecificDtl/{companyId}/{userId}/{vesselIMONo}/{docTypeNum}")
	public ResponseEntity<Map<String, Object>> getVesselDetails(@PathVariable long companyId,
			@PathVariable String userId, @PathVariable String vesselIMONo, @PathVariable String docTypeNum) {

		Map<String, Object> result = new HashMap<String, Object>();
		RestTemplate restTemplate = new RestTemplate();
		MaVessel vessel = null;
		MaVesselCompany vesselCompany = null;

		// VesselBean vb =
		// restTemplate.getForObject("http://rmi.bsaudit.com//RMIInterSys/ws/vesselDetails/"+vesselIMONo,
		// VesselBean.class);
		// http://192.168.1.102:7001/RMIInterSys
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		VesselBean vb = restTemplate
				.exchange(AppConstant.RMI_URL + "/ws2/vesselSpecificDtl/" + vesselIMONo + "/" + docTypeNum,
						HttpMethod.GET, request, VesselBean.class)
				.getBody();

		/*
		 * RmiAuditSigner ras = restTemplate.getForObject(AppConstant.RMI_URL+
		 * "/ws1/auditorSignandSeal/"+"SUGU", RmiAuditSigner.class);
		 * 
		 * if(ras!=null){
		 * 
		 * masterDelegate.updateLeadDetail(userId,ras,companyId); }
		 */

		if (vb != null) {

			vessel = new MaVessel();
			vesselCompany = new MaVesselCompany();

			vessel.setCompanyId(companyId);
			vessel.setVesselId(vb.getVesselID());
			vessel.setVesselImoNo(vb.getImoNumber());
			vessel.setVesselName(vb.getVesselName());
			vessel.setOfficialNo(vb.getOfficialNumber());
			vessel.setVesselPk(vb.getVesselPK());
			vessel.setVesselUk(vb.getVesselUK());
			vessel.setGrt(vb.getGrossTon());
			vessel.setVesselType(vb.getVesselType());
			vessel.setDateOfRegistry(vb.getRegistrationDate());

			vessel.setVesselStatus(vb.getVesselStatus());

			if (vb.getTcApprovalStatus().equals("Approved")) {
				vessel.setTcApprovalStatus(1);
			} else {
				vessel.setTcApprovalStatus(0);
			}

			if (vb.getVesselStatus().equals("Active") || vb.getVesselStatus().equals("Pending")) {
				vessel.setActiveStatus(1);
			} else {
				vessel.setActiveStatus(0);
			}

			vessel.setCompanyImoNo(vb.getCompanyIMONumber());
			vessel.setDocTypeNumber(vb.getDocTypeNumber());
			vessel.setVesselType(vb.getVesselType());
			vessel.setUserIns(userId);
			vessel.setDateIns(new java.sql.Date(new Date().getTime()));

			vessel.setCallSign(vb.getCallSign());
			vessel.setClassSociety(vb.getClassSociety());
			vessel.setPortOfRegistry(vb.getHomePort());
			vessel.setKeelLaidDate(vb.getKeelLaidDate());
			vessel.setRegOwnedImoNo(vb.getRegOwnedImoNo());
			vessel.setOperationCode(vb.getOperationCode());
			vessel.setRegisteredCompanyName(vb.getRegisteredCompanyName());
			vessel.setRegisteredCompanyAddress(vb.getRegisteredCompanyAddress());
			System.out.println("b.getOperationCode()is " + vb.getOperationCode());
			// vessel.setIssueDate(vb.getIssueDate());

			vesselCompany.setCompanyId(companyId);
			vesselCompany.setCompanyImoNo(vb.getCompanyIMONumber());
			vesselCompany.setDocTypeNo(vb.getDocType());
			vesselCompany.setVesselCompanyAddress(vb.getCompanyAddress());
			vesselCompany.setDocIssuer(vb.getDocIssuer());
			vesselCompany.setDocExpiry(vb.getDocExpiry());
			vesselCompany.setVesselCompanyName(vb.getCustomerName());
			vesselCompany.setUserIns(userId);
			vesselCompany.setDateIns(new java.sql.Date(new Date().getTime()));
			vesselCompany.setCompanyStatus(vb.getCompanyStatus());

			vessel.setVesselCompany(vesselCompany);

			masterDelegate.saveVesselCompany("CREATE", vesselCompany);

			masterDelegate.saveVesselType("CREATE", vessel);

			loadMasterService.updateCompanydata("CREATE", vesselCompany.getCompanyId(),
					vesselCompany.getCompanyImoNo());

			loadMasterService.updatevesseldata("CREATE", vessel.getCompanyId(), vessel.getVesselImoNo());
		}

		result.put("vsselDtl", vessel);
		// result.put("leadSign", ras.getSignature());
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/vesselDataWithoutUpdate/{companyId}/{userId}/{vesselIMONo}/{docTypeNum}")
	public ResponseEntity<Map<String, Object>> getVessel(@PathVariable long companyId, @PathVariable String userId,
			@PathVariable String vesselIMONo, @PathVariable String docTypeNum) {

		Map<String, Object> result = new HashMap<String, Object>();
		RestTemplate restTemplate = new RestTemplate();
		MaVessel vessel = null;
		MaVesselCompany vesselCompany = null;

		// VesselBean vb =
		// restTemplate.getForObject("http://rmi.bsaudit.com//RMIInterSys/ws/vesselDetails/"+vesselIMONo,
		// VesselBean.class);
		// http://192.168.1.102:7001/RMIInterSys
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		VesselBean vb = restTemplate
				.exchange(AppConstant.RMI_URL + "/ws2/vesselSpecificDtl/" + vesselIMONo + "/" + docTypeNum,
						HttpMethod.GET, request, VesselBean.class)
				.getBody();

		/*
		 * RmiAuditSigner ras = restTemplate.getForObject(AppConstant.RMI_URL+
		 * "/ws1/auditorSignandSeal/"+"SUGU", RmiAuditSigner.class);
		 * 
		 * if(ras!=null){
		 * 
		 * masterDelegate.updateLeadDetail(userId,ras,companyId); }
		 */

		if (vb != null) {

			vessel = new MaVessel();
			vesselCompany = new MaVesselCompany();

			vessel.setCompanyId(companyId);
			vessel.setVesselId(vb.getVesselID());
			vessel.setVesselImoNo(vb.getImoNumber());
			vessel.setVesselName(vb.getVesselName());
			vessel.setOfficialNo(vb.getOfficialNumber());
			vessel.setVesselPk(vb.getVesselPK());
			vessel.setVesselUk(vb.getVesselUK());
			vessel.setGrt(vb.getGrossTon());
			vessel.setVesselType(vb.getVesselType());
			vessel.setDateOfRegistry(vb.getRegistrationDate());

			vessel.setVesselStatus(vb.getVesselStatus());

			if (vb.getTcApprovalStatus().equals("Approved")) {
				vessel.setTcApprovalStatus(1);
			} else {
				vessel.setTcApprovalStatus(0);
			}

			if (vb.getVesselStatus().equals("Active") || vb.getVesselStatus().equals("Pending")) {
				vessel.setActiveStatus(1);
			} else {
				vessel.setActiveStatus(0);
			}

			vessel.setCompanyImoNo(vb.getCompanyIMONumber());
			vessel.setDocTypeNumber(vb.getDocTypeNumber());
			vessel.setVesselType(vb.getVesselType());
			vessel.setUserIns(userId);
			vessel.setDateIns(new java.sql.Date(new Date().getTime()));

			vessel.setCallSign(vb.getCallSign());
			vessel.setClassSociety(vb.getClassSociety());
			vessel.setPortOfRegistry(vb.getHomePort());

			// vessel.setIssueDate(vb.getIssueDate());

			vesselCompany.setCompanyId(companyId);
			vesselCompany.setCompanyImoNo(vb.getCompanyIMONumber());
			vesselCompany.setDocTypeNo(vb.getDocType());
			vesselCompany.setVesselCompanyAddress(vb.getCompanyAddress());
			vesselCompany.setDocIssuer(vb.getDocIssuer());
			vesselCompany.setDocExpiry(vb.getDocExpiry());
			vesselCompany.setVesselCompanyName(vb.getCustomerName());
			vesselCompany.setUserIns(userId);
			vesselCompany.setDateIns(new java.sql.Date(new Date().getTime()));
			vesselCompany.setCompanyStatus(vb.getCompanyStatus());

			vessel.setVesselCompany(vesselCompany);

		}

		result.put("vsselDtl", vessel);
		// result.put("leadSign", ras.getSignature());
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	/* to get vessel detail from client(RMI) */
	@GetMapping(value = "/getVesselDetail/{companyId}/{userId}/{vesselIMONo}/{searchBy}")
	public ResponseEntity<List<VesselBean>> getVesselDetail(@PathVariable long companyId, @PathVariable String userId,
			@PathVariable String vesselIMONo, @PathVariable String searchBy) {

		RestTemplate restTemplate = new RestTemplate();
		/*
		 * ResponseEntity<String> response = restTemplate.getForEntity(
		 * "http://192.168.1.86:8068/RMIInterSys/ws/vesselSpecificDtl/"+
		 * vesselIMONo, String.class);
		 */
		// VesselBean vb =
		// restTemplate.getForObject("http://rmi.bsaudit.com//RMIInterSys/ws/vesselDetails/"+vesselIMONo,
		// VesselBean.class);
		// http://192.168.1.102:7001/RMIInterSys

		// VesselBean[] vb =
		// restTemplate.getForObject("http://192.168.1.86:8068/RMIInterSys/ws/vesselDetails/"+vesselIMONo,
		// VesselBean[].class);

		// ResponseEntity<? extends ArrayList<VesselBean>> responses=
		// restTemplate.getForEntity("http://localhost:8079/RMIInterSys/ws/vesselDetails/"+vesselIMONo,
		// (Class<? extends ArrayList<VesselBean>>)ArrayList.class);

		// ResponseEntity<VesselBean[]> responses =
		// restTemplate.getForEntity(AppConstant.RMI_URL+"/ws2/vesselDetails/"+vesselIMONo,
		// VesselBean[].class);

		if (searchBy.equals("vesselName")) {
			vesselIMONo = vesselIMONo.toUpperCase();
		}
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		ResponseEntity<VesselBean[]> responses = restTemplate.exchange(
				AppConstant.RMI_URL + "/ws2/vesselDetails/" + vesselIMONo + "/" + searchBy, HttpMethod.GET, request,
				VesselBean[].class);

		List<VesselBean> list = Arrays.asList(responses.getBody());

		// return new ResponseEntity<List<VesselBean>>(responses.getBody(),
		// HttpStatus.OK);
		return new ResponseEntity<List<VesselBean>>(list, HttpStatus.OK);
	}

	/*
	 * @GetMapping(value =
	 * "/getVesselDetail3/{companyId}/{vesselIMONo}/{searchBy}") public
	 * ResponseEntity<List<VesselInfoV>> getVesselDetail3(@PathVariable long
	 * companyId,@PathVariable String vesselIMONo,
	 * 
	 * @PathVariable String searchBy) {
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * ResponseEntity<VesselInfoV[]> responses = restTemplate.getForEntity(
	 * "http://bsaudit.register-iri.com/RMIInterSys/ws2/vesselDetails/_%/vesselName",
	 * VesselInfoV[].class);
	 * //AppConstant.RMI_URL+"/ws2/vesselDetails/"+vesselIMONo+"/"+searchBy,
	 * VesselInfoV[].class);
	 * 
	 * List<VesselInfoV> list = Arrays.asList(responses.getBody());
	 * 
	 * masterDelegate.saveVesselInfoV(list);
	 * 
	 * return new ResponseEntity<List<VesselInfoV>>(list, HttpStatus.OK); }
	 */

	/* while creating an audit, to get auditor signature */
	@GetMapping(value = "/auditorSignandSeal/{signer}/{companyId}")
	public ResponseEntity<RmiAuditSigner> auditorSignandSeal(@PathVariable String signer,
			@PathVariable Long companyId) {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		RmiAuditSigner ras = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignandSeal/" + signer,
				HttpMethod.GET, request, RmiAuditSigner.class).getBody();

		return new ResponseEntity<RmiAuditSigner>(ras, HttpStatus.OK);
	}

	@GetMapping(value = "/getUTN")
	public ResponseEntity<RmiAuditUTN> getUTN() {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		RmiAuditUTN ras = restTemplate
				.exchange(AppConstant.RMI_URL + "/ws1/getUTN", HttpMethod.GET, request, RmiAuditUTN.class).getBody();

		return new ResponseEntity<RmiAuditUTN>(ras, HttpStatus.OK);
	}

	@GetMapping(value = "/getUTNForIhm/{count}")
	public ResponseEntity<Map<String, RmiAuditUTN[]>> getUTNForIhm(@PathVariable Integer count) {
		Map<String, RmiAuditUTN[]> rasList = new HashMap<String, RmiAuditUTN[]>();
		RmiAuditUTN[] ras = new RmiAuditUTN[count];
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		for (int i = 0; i < count; i++) {
			RestTemplate restTemplate = new RestTemplate();

			ras[i] = restTemplate
					.exchange(AppConstant.RMI_URL + "/ws1/getUTN", HttpMethod.GET, request, RmiAuditUTN.class)
					.getBody();

		}
		rasList.put("UTN", ras);
		return new ResponseEntity<Map<String, RmiAuditUTN[]>>(rasList, HttpStatus.OK);
	}

	@GetMapping(value = "/listAuditorSignAndSeal/{signer}/{companyId}")
	public ResponseEntity<List<RmiAuditSigner>> listAuditorSignAndSeal(@PathVariable String signer,
			@PathVariable Long companyId) {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		ResponseEntity<RmiAuditSigner[]> responses = restTemplate.exchange(
				AppConstant.RMI_URL + "/ws1/listAuditorSignAndSeal/" + signer, HttpMethod.GET, request,
				RmiAuditSigner[].class);

		List<RmiAuditSigner> list = Arrays.asList(responses.getBody());

		return new ResponseEntity<List<RmiAuditSigner>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/auditorSignAndSeal/{officialId}/{companyId}")
	public ResponseEntity<RmiAuditSigner> auditorSignAndSeal(@PathVariable Long officialId,
			@PathVariable Long companyId) {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		RmiAuditSigner ras = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignAndSeal/" + officialId,
				HttpMethod.GET, request, RmiAuditSigner.class).getBody();

		return new ResponseEntity<RmiAuditSigner>(ras, HttpStatus.OK);
	}

	@GetMapping(value = "/checkrolesPresent/{officialId}/{companyId}")
	public Map<String, String> checkrolesPresent(@PathVariable Long officialId, @PathVariable Long companyId) {
		Map<String, String> maps = new HashMap<String, String>();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		try {
			System.err.println(restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignAndSeal/" + officialId,
					HttpMethod.GET, request, RmiAuditSigner.class).getBody());
		} catch (Exception ex) {
			// TODO: handle exception
		}
		RmiAuditSigner ras = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignAndSeal/" + officialId,
				HttpMethod.GET, request, RmiAuditSigner.class).getBody();

		if (ras == null) {
			maps.put("result", "notpresent");
		} else if (ras != null && ras.getSignature() != null) {
			maps.put("result", "present");
		} else if (ras.getSignature() == null) {
			maps.put("result", "rmiSignMissing");
		} else {
			maps.put("result", "notpresent");
		}
		return maps;

	}
	/**Added by sudharsan 
	 *For Jira-Id IRI-5482
	 *on 16-09-2022 Start Here*/
	@GetMapping(value = "/checkrolesPresent/{emailId}/{officialId}/{companyId}/{device}")
	public Map<String, String> checkrolesPresent(@PathVariable Long officialId, @PathVariable Long companyId,
			@PathVariable String emailId, @PathVariable String device,HttpServletRequest request_ip) {
		String User_IP = masterDelegate.getUserIP(request_ip);
		Map<String, String> maps = new HashMap<String, String>();
		if(device.equalsIgnoreCase(AppConstant.DEVICE_WEB)){
			
			//Map<String, String> maps = new HashMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
			RmiAuditSigner ras = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignAndSeal/" + officialId,
			HttpMethod.GET, request, RmiAuditSigner.class).getBody();

			if (ras == null) {
				maps.put("result", "notpresent");
			} else if (ras != null && !ras.getStatus().equalsIgnoreCase("active")) {
				maps.put("result", "inActiveUser");
			} else if (ras != null && ras.getSignature() != null) {
				maps.put("result", "present");

				UserSessionDetail userSession = new UserSessionDetail();
				userSession.setUserId(emailId);
				userSession.setCompanyId(companyId);
				userSession.setStatus("Active");
				userSession.setDevice(device);
				userSession.setLocation(User_IP);
				System.out.println("userId  is66666666666666 ===555=" + emailId);
				
				masterDelegate.saveSessionDetail(userSession);
				
			} else if (ras != null && ras.getSignature() == null) {
				maps.put("result", "rmiSignMissing");
			} else {
				maps.put("result", "notpresent");
			}
			return maps;
			}
		
		else if(device.equalsIgnoreCase(AppConstant.DEVICE_DESKTOP) || device.equalsIgnoreCase(AppConstant.DEVICE_MOBILE) ){
			//Map<String, String> maps = new HashMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
			try {
				System.err.println(restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignAndSeal/" + officialId,
						HttpMethod.GET, request, RmiAuditSigner.class).getBody());
			} catch (Exception ex) {
				// TODO: handle exception
			}
			RmiAuditSigner ras = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/auditorSignAndSeal/" + officialId,
					HttpMethod.GET, request, RmiAuditSigner.class).getBody();

			if (ras == null) {
				maps.put("result", "notpresent");
			} else if (ras != null && ras.getSignature() != null) {
				maps.put("result", "present");
			} else if (ras.getSignature() == null) {
				maps.put("result", "rmiSignMissing");
			} else {
				maps.put("result", "notpresent");
			}
			
			UserSessionDetail userSession = new UserSessionDetail();
			userSession.setUserId(emailId);
			userSession.setCompanyId(companyId);
			userSession.setStatus("Active");
			userSession.setDevice(device);
			userSession.setLocation(User_IP);
			masterDelegate.saveSessionDetail(userSession);
			return maps;
		}
		/**Added by sudharsan 
		 *For Jira-Id IRI-5482
		 *on 16-09-2022 End here*/
		return maps;
	}

	@PostMapping(value = "/listAuditorsNotInOfficialIds")
	public ResponseEntity<List<RmiAuditSigner>> listAuditorsNotInOfficialIds(@RequestBody List<Integer> officialIds) {

		RestTemplate restTemplate = new RestTemplate();

		// HttpHeaders headers = new HttpHeaders();

		HttpEntity<List<Integer>> request = new HttpEntity<List<Integer>>(officialIds, restUtil.getHeaders());

		ResponseEntity<RmiAuditSigner[]> responses = restTemplate.postForEntity(
				AppConstant.RMI_URL + "/ws1/listAuditorsNotInOfficialIds", request, RmiAuditSigner[].class);

		List<RmiAuditSigner> list = Arrays.asList(responses.getBody());

		return new ResponseEntity<List<RmiAuditSigner>>(list, HttpStatus.OK);

	}

	@GetMapping(value = "/checkVesselImoNorExist/{vesselImoNo}")
	public ResponseEntity<Integer> checkVesselImoNorExist(@PathVariable String vesselImoNo) {
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		return new ResponseEntity<Integer>(
				restTemplate.exchange(AppConstant.RMI_URL + "/ws2/checkVesselImoNorExist/" + vesselImoNo,
						HttpMethod.GET, request, Integer.class).getBody(),
				HttpStatus.OK);
	}

	@GetMapping(value = "/getRmiData/{tableName}/{key}")
	public ResponseEntity<Map<String, Object>> getRmiData(@PathVariable String tableName, @PathVariable String key) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		Map<String, Object> responses = new HashMap<>();
		responses = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/getTableData/" + tableName + "/" + key,
				HttpMethod.GET, request, Map.class).getBody();
		return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.OK);

	}

	@GetMapping(value = "/getIhmPortData")
	public ResponseEntity<Map<String, Object>> getRmiData() {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		Map<String, Object> responses = new HashMap<>();
		responses = restTemplate
				.exchange(AppConstant.RMI_URL + "/ws1/getIhmPortData", HttpMethod.GET, request, Map.class).getBody();
		return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.OK);

	}

	@GetMapping(value = "/allUserRmi")
	public ResponseEntity<List<RmiAuditSigner>> allUserRmi() {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());

		ResponseEntity<RmiAuditSigner[]> responses = restTemplate.exchange(AppConstant.RMI_URL + "/ws1/allUsers",
				HttpMethod.GET, request, RmiAuditSigner[].class);

		List<RmiAuditSigner> list = Arrays.asList(responses.getBody());

		return new ResponseEntity<List<RmiAuditSigner>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/getAllRmiIhmCustomers")
	public ResponseEntity<List<RmiIhmCustomers>> getAllRmiIhmCustomers() {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());

		ResponseEntity<RmiIhmCustomers[]> responses = restTemplate.exchange(
				AppConstant.RMI_URL + "/ws2/getAllRmiIhmCustomers", HttpMethod.GET, request, RmiIhmCustomers[].class);

		List<RmiIhmCustomers> list = Arrays.asList(responses.getBody());

		return new ResponseEntity<List<RmiIhmCustomers>>(list, HttpStatus.OK);
	}

	@PostMapping(value = "/listAuditorsInOfficialIds")
	public ResponseEntity<List<RmiAuditSigner>> listAuditorsInOfficialIds(@RequestBody List<Integer> officialIds) {

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		List<RmiAuditSigner> list = null;
		HttpEntity<List<Integer>> request = new HttpEntity<List<Integer>>(officialIds, restUtil.getHeaders());
		try {

			ResponseEntity<RmiAuditSigner[]> responses = restTemplate.postForEntity(
					AppConstant.RMI_URL + "/ws1/listAuditorsInOfficialIds", request, RmiAuditSigner[].class);

			list = Arrays.asList(responses.getBody());
		} catch (Exception e) {

		}

		return new ResponseEntity<List<RmiAuditSigner>>(list, HttpStatus.OK);

	}
}
