package com.api.central.certificate.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;


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
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.certificate.delegate.CertificateDelegate;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaScreens;
import com.api.central.rmiaudit.entity.RmiAuditUTN;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;


@RestController
@RequestMapping("api/certificate")
public class CertificateController {

	@Autowired
	AppUtil appUtil;
	
	@Autowired
	private CertificateDelegate certificateDelegate;
	
	@PostMapping(value = "/getCertSearchResult2")
	public ResponseEntity<Map<String,Object>> getCertSearchResult2(@RequestBody SearchCriteria searchCriteria) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getCertSearchResult2(searchCriteria), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditCertDetails/{auditSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getAuditCertDetails(@PathVariable Integer auditSeqNo, @PathVariable Long companyId) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getAuditCertDetails(auditSeqNo,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getCertificateDetailnew/{auditSeqNo}/{companyId}/{seqNo}/{activeStatus}")
	public ResponseEntity<Map<String,Object>> getCertificateDetailnew(@PathVariable Integer auditSeqNo, @PathVariable Long companyId,
			@PathVariable Integer seqNo, @PathVariable Integer activeStatus) {
		 
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getCertificateDetailnew(auditSeqNo,companyId,seqNo,activeStatus), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAllIhmCertificateDetail/{auditTypeId}/{vesselImo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getAllIhmCertificateDetail(@PathVariable Integer auditTypeId, @PathVariable Integer vesselImo, @PathVariable Long companyId)
	{
		 
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getAllIhmCertificateDetail(auditTypeId,vesselImo,companyId), HttpStatus.OK);
	}
	@GetMapping(value = "/checkRelatedToCurrAdt/{auditSeqNo}/{userId}/{companyId}")
	public ResponseEntity<Map<String,Object>> checkRelatedToCurrAdt(@PathVariable Integer auditSeqNo, @PathVariable Long companyId,
			@PathVariable String userId) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.checkRelatedToCurrAdt(auditSeqNo,companyId,userId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/generateCertificate2/{status}/{generateStatus}")
	public ResponseEntity<Map<String,Object>> generateCertificate2(@RequestBody CertificateDetail certificateDetail,@PathVariable String status,@PathVariable Integer generateStatus) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.generateCertificate(certificateDetail), HttpStatus.OK);
	}
	
	@PostMapping(value = "/generateCertificateWithOutAudit/{status}/{generateStatus}")
	public ResponseEntity<Map<String,Object>> generateCertificateWithOutAudit(@RequestBody CertificateDetailWithOutAudit certificateDetail,@PathVariable String status,@PathVariable Integer generateStatus) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.generateCertificateWithOutAudit(certificateDetail), HttpStatus.OK);
	}
	@PostMapping(value = "/generateCertificatePublishStatusWithOutAudit")
	public ResponseEntity<Map<String,Object>> generateCertificatePublishStatusWithOutAudit(@RequestBody CertificateDetailWithOutAudit certificateDetail) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.generateCertificatePublishStatusWithOutAudit(certificateDetail), HttpStatus.OK);
	}
	
	@GetMapping(value = "/publishCertificate/{auditSeqNo}/{companyId}/{seqNo}")
	public ResponseEntity<Map<String,Object>> publishCertificate(@PathVariable Integer auditSeqNo,@PathVariable Long companyId, @PathVariable Integer seqNo) {
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.publishCertificate(auditSeqNo,companyId,seqNo), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getUtnAndCertificateId/{auditReportNo}/{certificateNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getUtnAndCertificateId(@PathVariable String auditReportNo,@PathVariable String certificateNo,@PathVariable Long companyId) {
		
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getUtnAndCertificateId(auditReportNo,certificateNo,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getConsective/{vesselImo}/{auditSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getConsective(@PathVariable Integer vesselImo,@PathVariable Integer auditSeqNo,@PathVariable Long companyId) {

		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getConsective(vesselImo,auditSeqNo,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getPreviousInitialCount/{auditTypeId}/{vesselIMONo}/{companyId}/{subTypeId}/{auditSeqNo}")
	public ResponseEntity<Map<String,Object>> getInitialCountVal(@PathVariable Integer auditTypeId,@PathVariable Integer vesselIMONo,@PathVariable Long companyId ,@PathVariable Integer subTypeId,@PathVariable Integer auditSeqNo) {
		 Map<String, Object> map = new HashMap<String, Object>();
		return new ResponseEntity<Map<String,Object>>(certificateDelegate.getInitialCountVal(auditTypeId,vesselIMONo,companyId, map, subTypeId, auditSeqNo), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditCertificateInActive/{ImoNo}/{companyId}/{certificateNo}/{auditTypeId}/{auditDate}/{directInterorAdd}")
	public  ResponseEntity<List<CertificateDetail>> getAuditCertificateInActive(@PathVariable Integer ImoNo,@PathVariable Long companyId,@PathVariable String certificateNo,@PathVariable Integer auditTypeId, @PathVariable String auditDate,@PathVariable boolean directInterorAdd) { //added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536
		return new  ResponseEntity<List<CertificateDetail>>(certificateDelegate.getAuditCertificateInActive(ImoNo,companyId,certificateNo,auditTypeId, appUtil.convertStringToDate(auditDate),directInterorAdd), HttpStatus.OK);
	
	}
	
	@GetMapping(value = "/UpdateDirectIntermediateIssueExpiryDate/{auditTypeId}/{auditSeqNo}/{companyId}/{certExpireDate}/{certIssueDate}/{certIssueId}/{vesselIMONo}")
	public ResponseEntity<Map<String,Object>> UpdateDirectIntermediateIssueExpiryDate(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			 @PathVariable Long companyId,@PathVariable Date certExpireDate,@PathVariable Date certIssueDate,@PathVariable Integer certIssueId,@PathVariable Integer vesselIMONo){
		
		return new ResponseEntity<Map<String,Object>> (certificateDelegate.UpdateDirectIntermediateIssueExpiryDate(auditTypeId,auditSeqNo,companyId,certExpireDate,certIssueDate,certIssueId, vesselIMONo),HttpStatus.OK);
	}
	
	/* change added by cb to update the void certificate in the RMI tables TICKET-552 */
	public void updateCertificateStatusSMC(Integer auditSeqNo, Long companyid, Integer auditSubtypeid){
		
		certificateDelegate.updateCertificateStatusSMC(auditSeqNo,companyid,auditSubtypeid);
	}
}