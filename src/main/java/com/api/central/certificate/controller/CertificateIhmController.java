package com.api.central.certificate.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.CertificateDetailWithOutAuditIhm;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.certificate.delegate.CertificateIhmDelegate;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaScreens;
import com.api.central.rmiaudit.entity.RmiAuditUTN;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;

@RestController
@RequestMapping("api/certificateIhm")
public class CertificateIhmController {
	
	@Autowired
	AppUtil appUtil;

	@Autowired
	private CertificateIhmDelegate certificateIhmDelegate;
	
	@GetMapping(value = "/getAuditCertDetailsForIhm/{auditSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getAuditCertDetailsForIhm(@PathVariable Integer auditSeqNo, @PathVariable Long companyId) {
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.getAuditCertDetailsForIhm(auditSeqNo,companyId), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/generateCertificateWithOutAuditForIhm/{status}/{generateStatus}")
	public ResponseEntity<Map<String,Object>> generateCertificateWithOutAuditIhm(@RequestBody List<CertificateDetailWithOutAuditIhm> certificateDetail,@PathVariable String status,@PathVariable Integer generateStatus) {
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.generateCertificateWithOutAuditIhm(certificateDetail), HttpStatus.OK);
	}
	@PostMapping(value = "/getCertSearchResultForIhm")
	public ResponseEntity<Map<String,Object>> getCertSearchResultForIhm(@RequestBody SearchCriteria searchCriteria) {
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.getCertSearchResultForIhm(searchCriteria), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getCertificateDetailForIhm/{auditSeqNo}/{companyId}/{seqNo}/{activeStatus}/{socType}")
	public ResponseEntity<Map<String,Object>> getCertificateDetailForIhm(@PathVariable Integer auditSeqNo, @PathVariable Long companyId,
			@PathVariable Integer seqNo, @PathVariable Integer activeStatus,@PathVariable String socType) {
		 
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.getCertificateDetailForIhm(auditSeqNo,companyId,seqNo,activeStatus,socType), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAllCertificateDetailForIhm/{auditTypeId}/{vesselImo}/{companyId}/{socType}")
	public ResponseEntity<Map<String,Object>> getAllCertificateDetailForIhm(@PathVariable Integer auditTypeId, @PathVariable Integer vesselImo, @PathVariable Long companyId, @PathVariable String socType)
	{
		 
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.getAllCertificateDetailForIhm(auditTypeId,vesselImo,companyId,socType), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/getIntitalCertForDownloadIhm/{auditTypeId}/{vesselImo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getIntitalCertForDownloadIhm(@PathVariable Integer auditTypeId, @PathVariable Integer vesselImo, @PathVariable Long companyId)
	{
		 
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.getIntitalCertForDownloadIhm(auditTypeId,vesselImo,companyId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/generateCertificateForIhm/{status}/{generateStatus}")
	public ResponseEntity<Map<String,Object>> generateCertificateForIhm(@RequestBody List<CertificateIhmDetail> certificateDetail,@PathVariable String status,@PathVariable Integer generateStatus) {
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.generateCertificateIhm(certificateDetail), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSocTypeSelectDefault/{certIssueId}/{companyId}/{vesselImo}/{auditSubTypeId}/{auditSeqNo}/{certLink}/{socType}")
	public ResponseEntity<Map<String,Object>> getSocTypeSelectDefault(@PathVariable Integer certIssueId,@PathVariable Long companyId,@PathVariable Integer vesselImo,@PathVariable Integer auditSubTypeId,@PathVariable Integer auditSeqNo, @PathVariable Integer certLink ,@PathVariable String socType) {
		
		return new ResponseEntity<Map<String,Object>> (certificateIhmDelegate.getSocTypeSelectDefault(certIssueId,companyId,vesselImo,auditSubTypeId, auditSeqNo, certLink, socType), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getUtnAndCertificateIdForIhm/{auditReportNo}/{certificateNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getUtnAndCertificateIdForIhm(@PathVariable String auditReportNo,@PathVariable String certificateNo,@PathVariable Long companyId) {
		
		return new ResponseEntity<Map<String,Object>>(certificateIhmDelegate.getUtnAndCertificateIdForIhm(auditReportNo,certificateNo,companyId), HttpStatus.OK);
	}
	@GetMapping(value = "/getAuditCertificateInActiveForIhm/{ImoNo}/{companyId}/{certificateNo}/{auditTypeId}/{auditDate}")
	public  ResponseEntity<List<CertificateIhmDetail>> getAuditCertificateInActiveForIhm(@PathVariable Integer ImoNo,@PathVariable Long companyId,@PathVariable String certificateNo,@PathVariable Integer auditTypeId, @PathVariable String auditDate) {
		return new  ResponseEntity<List<CertificateIhmDetail>>(certificateIhmDelegate.getAuditCertificateInActiveForIhm(ImoNo,companyId,certificateNo,auditTypeId, appUtil.convertStringToDate(auditDate)), HttpStatus.OK);
	
	}
	
	@GetMapping(value = "/deleteCertificateForIhm/{certIssueId}/{companyId}/{vesselImo}/{auditSubTypeId}/{auditSeqNo}/{certNo}/{socType}")
	public  ResponseEntity<Map<String,Object>> deleteCertificateForIhm(@PathVariable Integer certIssueId,@PathVariable Long companyId,@PathVariable Integer vesselImo,@PathVariable Integer auditSubTypeId,@PathVariable Integer auditSeqNo ,@PathVariable String certNo, @PathVariable String socType) {
		return new ResponseEntity<Map<String,Object>> (certificateIhmDelegate.deleteCertificateForIhm(certIssueId,companyId,vesselImo,auditSubTypeId, auditSeqNo,certNo,socType), HttpStatus.OK);
	
	}
	//Added by sudharsan and Chibi for JIRA-ID=5377&5378
	public void updateCertificateStatus(Integer auditSeqNo, Long companyid, Integer auditSubtypeid){
		Log.info("Inside certificate controller");
		certificateIhmDelegate.updateCertificateStatus(auditSeqNo,companyid,auditSubtypeid);
	}
	//End here
}
