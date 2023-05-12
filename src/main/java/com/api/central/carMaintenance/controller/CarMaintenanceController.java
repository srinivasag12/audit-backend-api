package com.api.central.carMaintenance.controller;

/**
 * @author sourav ghadai
 *
 */
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.audit.entity.CarSearchCriteria;
import com.api.central.audit.entity.CarSearchResult;
import com.api.central.audit.entity.FindingDetail;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.carMaintenance.delegate.CarMaintenanceDelegate;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;

@RestController
@RequestMapping("api/carMaintenance")
public class CarMaintenanceController {

	@Autowired
	private CarMaintenanceDelegate carMaintenanceDelegate;
	
	@Autowired
	AppUtil appUtil;

	@Autowired
	ServletContext servletContext;
	
	@PostMapping(value = "/getHistorySearchCount")
	public ResponseEntity<Long> getHistorySearchCount(@RequestBody CarSearchCriteria carSearchCriteria) {
		return new ResponseEntity<Long>(carMaintenanceDelegate.getHistorySearchCount(carSearchCriteria), HttpStatus.OK);
	}
	
	@PostMapping(value = "/getHistorySearchResult")
	public ResponseEntity<List<CarSearchResult>> getHistorySearchResult(@RequestBody CarSearchCriteria carSearchCriteria) {
		return new ResponseEntity<List<CarSearchResult>>(carMaintenanceDelegate.getHistorySearchResult(carSearchCriteria), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getHistoryFindingData/{auditSeqNo}/{findingSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getHistoryFindingData(@PathVariable Integer auditSeqNo, @PathVariable Integer findingSeqNo,@PathVariable Long companyId) {
		
		return new ResponseEntity<Map<String,Object>>(carMaintenanceDelegate.getHistoryFindingData(auditSeqNo,findingSeqNo,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/checkAuditCompleted/{vesselImoNo}/{auditTypeId}/{auditDate}/{auditStatusId}/{companyId}")
	public ResponseEntity<Map<String,Object>> checkAuditCompleted(@PathVariable Integer vesselImoNo, @PathVariable Integer auditTypeId, @PathVariable Date auditDate,@PathVariable Integer auditStatusId,@PathVariable Long companyId) {
		
		return new ResponseEntity<Map<String,Object>>(carMaintenanceDelegate.checkAuditCompleted(vesselImoNo, auditTypeId, auditDate, auditStatusId,companyId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/updateCarMaintainanceFinding/{updateFlag}/{auditTypeDesc}/{companyId}")
	public ResponseEntity<FindingDetail> updateCarMaintainanceFinding(@PathVariable String updateFlag,@PathVariable String auditTypeDesc,@RequestBody FindingDetail findingDetail,@PathVariable Long companyId) {
		
		return new ResponseEntity<FindingDetail>(carMaintenanceDelegate.updateCarMaintainanceFinding(findingDetail,updateFlag,auditTypeDesc,companyId), HttpStatus.OK);
		//return new ResponseEntity<AuditFinding>(carMaintenanceDelegate.updateCarMaintainance(auditFinding,auditTypeDesc), HttpStatus.OK);
		
		}
	
	@GetMapping(value = "/downloadFindingFile/{auditSeqNo}/{findingSeqNo}/{fileName}/{statusSeqNo}/{auditTypeDesc}/{companyId}")
	public ResponseEntity<byte[]> downloadFindingFile(@PathVariable String auditSeqNo,
			@PathVariable String findingSeqNo, @PathVariable String fileName, @PathVariable String statusSeqNo,@PathVariable String auditTypeDesc,@PathVariable Long companyId) {
		String path = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + auditTypeDesc
				+ AppConstant.SEPARATOR + auditSeqNo + AppConstant.SEPARATOR + findingSeqNo + AppConstant.SEPARATOR
				+ statusSeqNo + AppConstant.SEPARATOR + fileName;
		return new ResponseEntity<byte[]>(appUtil.getFileByte(path), appUtil.setHeaderType(path, fileName),
				HttpStatus.OK);
	}
	
	@PostMapping(value = "/unlinkFindingFiles/{auditTypeDesc}/{companyId}")
	public ResponseEntity<FindingRptAttach> unlinkFindingFiles(@PathVariable String auditTypeDesc,@RequestBody FindingRptAttach findingRptAttach,@PathVariable Long companyId) {
		
		return new ResponseEntity<FindingRptAttach>(carMaintenanceDelegate.unlinkFindingFiles(findingRptAttach,auditTypeDesc,companyId), HttpStatus.OK);
		
		}
	
	@GetMapping(value = "/checkAuditorAndNextAdtData/{auditSeqNo}/{userId}/{companyId}/{findingSeqNo}")
	public ResponseEntity<Map<String,Object>> checkAuditorAndNextAdtData(@PathVariable Integer auditSeqNo, @PathVariable Long companyId,
			@PathVariable String userId, @PathVariable Integer findingSeqNo) {
		return new ResponseEntity<Map<String,Object>>(carMaintenanceDelegate.checkAuditorAndNextAdtData(auditSeqNo,companyId,userId, findingSeqNo), HttpStatus.OK);
	}
	
}
