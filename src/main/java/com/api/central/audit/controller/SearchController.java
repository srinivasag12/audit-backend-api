package com.api.central.audit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.central.audit.delegate.SearchDelegate;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CertificateData;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.SearchCriPort;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.VesselStatement;
import com.api.central.master.entity.MaPort;
import com.api.central.util.AppUtil;


@RestController
@RequestMapping("api/audit/search")
public class SearchController {


	@Autowired
	AppUtil appUtil;
	
	@Autowired
	private SearchDelegate searchDelegate;
	
	@PostMapping(value = "/getSearchCount")
	public ResponseEntity<Long> getSearchCount(@RequestBody SearchCriteria searchCriteria) {
		return new ResponseEntity<Long>(searchDelegate.getSearchCount(searchCriteria), HttpStatus.OK);
	}
	
	@PostMapping(value = "/getSearchResult")
	public ResponseEntity<List<AuditDetailView>> getSearchResult(@RequestBody SearchCriteria searchCriteria) {
		return new ResponseEntity<List<AuditDetailView>>(searchDelegate.getSearchResult(searchCriteria), HttpStatus.OK);
	
	}
	
	@PostMapping(value = "/getVesselSearchData/{ImoNo}/{companyId}")
	public  ResponseEntity<List<VesselStatement>> getVesselSearchData(@PathVariable Integer ImoNo,@PathVariable Long companyId) {
		return new  ResponseEntity<List<VesselStatement>>(searchDelegate.getVesselSearchData(ImoNo,companyId), HttpStatus.OK);
	
	}
	
	@GetMapping(value = "/getLatestCreatedVesselCompanyImo/{emailId}/{companyId}")
	public ResponseEntity<List<AuditDetailView>> getLatestCreatedVesselCompanyImo(@PathVariable String emailId,@PathVariable Long companyId) {
	
			return new ResponseEntity<List<AuditDetailView>>(searchDelegate.getLatestCreatedVesselCompanyImo(emailId,companyId), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/getAuditCertificate/{companyId}")
	public ResponseEntity<List<AuditDetail>> getAuditCertificate(@PathVariable Long companyId) {
	
			return new ResponseEntity<List<AuditDetail>>(searchDelegate.getAuditCertificate(companyId), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/certificateData/{status}")
	public ResponseEntity<CertificateData> auditCertificateData(@RequestBody CertificateData certificatedata,@PathVariable String status) {
		
		return new ResponseEntity<CertificateData>(searchDelegate.auditCertificateData(certificatedata, status), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/getPreviousCertificate/{vesselImoNum}/{certificateNo}")
	public ResponseEntity<List<CertificateData>> previousAuditCertificate(@PathVariable String vesselImoNum, @PathVariable String certificateNo)
	{
		return new ResponseEntity<List<CertificateData>>(searchDelegate.previousAuditCertificate(Integer.parseInt(vesselImoNum), certificateNo), HttpStatus.OK);
	}
	
	@PostMapping(value="reviewerDataCount")
	public ResponseEntity<Long> getReviewDataCount(@RequestBody SearchCriteria searchCriteria){
		return new ResponseEntity<Long>(searchDelegate.getReviewData(searchCriteria),HttpStatus.OK);
	}
	
	@PostMapping(value="reviewerDataSearch")
	public ResponseEntity<List<AuditDetailView>> getReviewDataSearch(@RequestBody SearchCriteria searchCriteria){
		return new ResponseEntity<List<AuditDetailView>>(searchDelegate.getReviewrResult(searchCriteria),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditCertificateData/{ImoNo}/{companyId}/{certificateNo}/{auditTypeId}/{auditDate}/{directInterorAdd}")
	public  ResponseEntity<List<CertificateDetail>> getCertificateDetails(@PathVariable Integer ImoNo,@PathVariable Long companyId,@PathVariable String certificateNo,@PathVariable Integer auditTypeId, @PathVariable String auditDate, @PathVariable boolean directInterorAdd) {
		System.out.println("inside controller");
		return new  ResponseEntity<List<CertificateDetail>>(searchDelegate.getAuditCertificateData(ImoNo,companyId,certificateNo,auditTypeId, appUtil.convertStringToDate(auditDate), directInterorAdd), HttpStatus.OK);
	
	}
 	
	@GetMapping(value = "/getAuditCertificateDataForIhm/{ImoNo}/{companyId}/{certificateNo}/{auditTypeId}/{auditDate}")
	public  ResponseEntity<List<CertificateIhmDetail>> getAuditCertificateDataForIhm(@PathVariable Integer ImoNo,@PathVariable Long companyId,@PathVariable String certificateNo,@PathVariable Integer auditTypeId, @PathVariable String auditDate) {
		return new  ResponseEntity<List<CertificateIhmDetail>>(searchDelegate.getAuditCertificateDataForIhm(ImoNo,companyId,certificateNo,auditTypeId, appUtil.convertStringToDate(auditDate)), HttpStatus.OK);
	
	}
	
	@PostMapping(value = "/getPortSearchResult")
	public ResponseEntity<List<MaPort>> getPortSearchResult(@RequestBody SearchCriPort searchCriteria) {
		return new ResponseEntity<List<MaPort>>(searchDelegate.getPortSearchResult(searchCriteria), HttpStatus.OK);
	}
	
}