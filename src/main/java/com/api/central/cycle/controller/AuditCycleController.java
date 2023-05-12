package com.api.central.cycle.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.audit.entity.AuditCycle;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.cycle.delegate.AuditCycleDelegate;
import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;



@RestController
@RequestMapping("api/auditCycle")
public class AuditCycleController {
	
	@Autowired
	AuditCycleDelegate auditCycleDelegate;
	
	
	@PostMapping(value = "/createAuditCycle/{companyId}")
	public ResponseEntity<Map<String,Object>> createAuditCycle( @RequestBody AuditCycle auditcycledata){
		
		return new ResponseEntity<Map<String,Object>>(auditCycleDelegate.createAuditCycle(auditcycledata), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditCycleData/{vesselImoNo}/{auditTypeId}/{companyId}")
	public ResponseEntity<List<AuditCycle>> getAuditCycleData(@PathVariable Integer vesselImoNo, @PathVariable Integer auditTypeId,
			@PathVariable Long companyId) {
		
		return new ResponseEntity<List<AuditCycle>>(auditCycleDelegate.getAuditCycleData(vesselImoNo,
			auditTypeId,  companyId), HttpStatus.OK);
	}

	@GetMapping(value = "/getAuditCycleHistory/{auditTypeId}/{auditSubTypeId}/{vesselIMONo}/{companyId}/{pageNo}/{getDefaultSearchCount}")
	public ResponseEntity<List<AuditDetail>> getAuditCycleHistoryDate(@PathVariable Integer auditTypeId,
			@PathVariable Integer auditSubTypeId, @PathVariable Integer vesselIMONo, @PathVariable Long companyId,@PathVariable Integer pageNo,@PathVariable Integer getDefaultSearchCount ) {

		List<AuditDetail> getAuditCycleHistoryDate = auditCycleDelegate.getAuditCycleHistoryDate( auditTypeId,
				auditSubTypeId,vesselIMONo,companyId,pageNo,getDefaultSearchCount);

		return new ResponseEntity<List<AuditDetail>>(getAuditCycleHistoryDate, HttpStatus.OK);
	}
	
	/* to get the audit credit date (credit date - audit cycle created by manager) */
	@GetMapping(value = "/getAuditCreditDate/{auditTypeId}/{auditSubTypeId}/{vesselImoNo}/{companyId}/{auditSeqNo}")
	public ResponseEntity<AuditCycle> getAuditCreditDate(@PathVariable Integer auditTypeId,@PathVariable Integer auditSubTypeId, @PathVariable Integer vesselImoNo, @PathVariable Long companyId, @PathVariable Integer auditSeqNo ){
		
		return new ResponseEntity<AuditCycle>  (auditCycleDelegate.getAuditCreditDate(auditTypeId,auditSubTypeId,vesselImoNo,companyId,auditSeqNo  ),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAllCycleDate/{auditTypeId}/{vesselImoNo}/{companyId}")
	public ResponseEntity<List<AuditCycle>>  getAllCycleDate(@PathVariable Integer auditTypeId, @PathVariable Integer vesselImoNo, @PathVariable Long companyId){
		
		return new ResponseEntity<List<AuditCycle>>  (auditCycleDelegate.getAllCycleDate(auditTypeId,vesselImoNo,companyId),HttpStatus.OK);
	}
}

