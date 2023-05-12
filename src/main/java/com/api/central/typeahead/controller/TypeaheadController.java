package com.api.central.typeahead.controller;

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

import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditRoles;
import com.api.central.master.entity.MaAuditStatus;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaAuditSummary;
import com.api.central.master.entity.MaCertificateIssued;
import com.api.central.master.entity.MaCompany;
import com.api.central.master.entity.MaFindingsCategory;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.MasterTableUpdate;
import com.api.central.master.entity.RefreshMasterData;
import com.api.central.master.model.MaUserMapper;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.typeahead.delegate.TypeaheadDelegate;

@RestController
@RequestMapping("api/typeAhead")
public class TypeaheadController {
	
	
	@Autowired
	private TypeaheadDelegate typeaheadDelegate;

	@GetMapping(value="/getAuditSubType/{auditType}")
	public ResponseEntity<List<MaAuditSubtype>> getAuditSubtype(@PathVariable Long auditType){
		return new ResponseEntity<List<MaAuditSubtype>>(typeaheadDelegate.getAuditSubtype(auditType),HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllAuditSubTypes")
	public ResponseEntity<List<MaAuditSubtype>> getAllAuditSubTypes(){
		return new ResponseEntity<List<MaAuditSubtype>>(typeaheadDelegate.getAllAuditSubTypes(),HttpStatus.OK);
	}
	
	@GetMapping(value="/getAuditStatus/{audType}")
	public ResponseEntity<List<MaAuditStatus>> getAuditStatus(@PathVariable Long audType){
		return new ResponseEntity<List<MaAuditStatus>>(typeaheadDelegate.getAuditStatus(audType),HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllAuditStatus")
	public ResponseEntity<List<MaAuditStatus>> getAllAuditStatus(){
		return new ResponseEntity<List<MaAuditStatus>>(typeaheadDelegate.getAllAuditStatus(),HttpStatus.OK);
	}
	
	@GetMapping(value="/getCertificateIssued/{audType}")
	public ResponseEntity<List<MaCertificateIssued>> getCertificateIssued(@PathVariable Long audType){
		return new ResponseEntity<List<MaCertificateIssued>>(typeaheadDelegate.getCertificateIssued(audType),HttpStatus.OK);
	}
	
	@GetMapping(value="/getObsCategory/{audType}/{companyId}")
	public ResponseEntity<List<MaFindingsCategory>> getFindingCategory(@PathVariable Long audType,@PathVariable Long companyId){
		return new ResponseEntity<List<MaFindingsCategory>>(typeaheadDelegate.getFindingCategory(audType,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllFindingCategory")
	public ResponseEntity<List<MaFindingsCategory>> getFindingCategory(){
		return new ResponseEntity<List<MaFindingsCategory>>(typeaheadDelegate.getFindingCategory(), HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllFingingStatus/{companyId}")
	public ResponseEntity<List<MaFindingsStatus>> getAllFingingStatus(@PathVariable Long companyId){
		return new ResponseEntity<List<MaFindingsStatus>>(typeaheadDelegate.getFindingStatus(companyId),HttpStatus.OK);
	}
	
	@GetMapping(value="/getObsStatus/{audType}/{companyId}")
	public ResponseEntity<List<MaFindingsStatus>> getFindingStatus(@PathVariable Integer audType, @PathVariable Long companyId){
		return new ResponseEntity<List<MaFindingsStatus>>(typeaheadDelegate.getFindingStatus(audType,companyId),HttpStatus.OK);
	}
	
	@GetMapping(value="/getDistinctAudObsStatus/{companyId}")
	public ResponseEntity<Map<String, ArrayList>> getDistinctAudObsStatus(@PathVariable Long companyId){
		
		return new ResponseEntity<Map<String, ArrayList>>(typeaheadDelegate.getDistinctFindingStatus(companyId),HttpStatus.OK);
		//return new ResponseEntity<String>("{\"name\":\"Saurav\"}",HttpStatus.OK);
		
	}
	
	@GetMapping(value="/getReportTypes/{audType}")
	public ResponseEntity<List<MaAttachmentTypes>> getAttachmentTypes(@PathVariable Long audType){
		return new ResponseEntity<List<MaAttachmentTypes>>(typeaheadDelegate.getAttachmentTypes(audType), HttpStatus.OK);
	}
	
	@GetMapping(value="/getAuditSummary/{audType}")
	public ResponseEntity<List<MaAuditSummary>> getAuditSummary(@PathVariable Long audType){
		return new ResponseEntity<List<MaAuditSummary>>(typeaheadDelegate.getAuditSummary(audType), HttpStatus.OK);
	}
	
	@GetMapping(value="/getAuditCode/{audType}")
	public ResponseEntity<List<MaAuditCodes>> getAuditCodes(@PathVariable Long audType){
		return new ResponseEntity<List<MaAuditCodes>>(typeaheadDelegate.getAuditCodes(audType), HttpStatus.OK);
	}
	
	@GetMapping(value="/getVesselCompanyImoNo/{cImoNo}")
	public ResponseEntity<List<MaVesselCompany>> getVesselCompanyDetails(@PathVariable String cImoNo){
		return new ResponseEntity<List<MaVesselCompany>>(typeaheadDelegate.getVesselCompanyDetails(cImoNo),HttpStatus.OK);
	}
	
	@GetMapping(value="/getAudObsType")
	public ResponseEntity<List<MaAuditRoles>> getAuditRoles(){
		return new ResponseEntity<List<MaAuditRoles>>(typeaheadDelegate.getAuditRoles(), HttpStatus.OK);
	}
	
	@GetMapping(value="/getAudObsData/{companyId}")
	public ResponseEntity<List<MaUsersWOPwd>> getUser(@PathVariable("companyId") Long companyId){
		return new ResponseEntity<List<MaUsersWOPwd>>( new MaUserMapper().getMaUsersWOPwdList(typeaheadDelegate.getAudDetails(companyId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/getAudDetails/{companyId}")
	public ResponseEntity<List<MaUsersWOPwd>> getAudDetails(@PathVariable("companyId") Long companyId){
		return new ResponseEntity<List<MaUsersWOPwd>>(new MaUserMapper().getMaUsersWOPwdList(typeaheadDelegate.getAudDetails(companyId)), HttpStatus.OK);
	}
	
	@GetMapping(value = "/refreshData/{id}/{companyId}")
	public ResponseEntity<Map<String,Object>> getMasterData(@PathVariable("id") String value,@PathVariable("companyId") Long companyId) {
		return new ResponseEntity<Map<String,Object>>(typeaheadDelegate.getMasterData(value,companyId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/refreshMasterData/{flag}/{companyId}")
	public ResponseEntity<Map<String,Object>> masterData(@RequestBody RefreshMasterData data,@PathVariable("flag") String flag,@PathVariable("companyId") Long companyId) {
		return new ResponseEntity<Map<String,Object>>(typeaheadDelegate.getData(data,flag,companyId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/updateDbdata")
	public ResponseEntity<List<MasterTableUpdate>> updateDbdata(@RequestBody List<MasterTableUpdate> masterTableUpdates) {
		
		
		
		
		return new ResponseEntity<List<MasterTableUpdate>>(typeaheadDelegate.updateDbdata(masterTableUpdates), HttpStatus.OK);
	}
	
	@PostMapping(value = "/getUpdatedDbdata/{companyId}")
	public ResponseEntity<List<MasterTableUpdate>> getUpdatedDbdata(@PathVariable("companyId") Long companyId) {
		
		return new ResponseEntity<List<MasterTableUpdate>>(typeaheadDelegate.getUpdatedDbdata(companyId), HttpStatus.OK);
	}
	
	/* population of all fields in update mode based on emailId */
	@GetMapping(value = "/getDomainNameCentral/{companyId}")
	public ResponseEntity<List<MaCompany>> getDomainNameCentral(@PathVariable Long companyId) {
		
		return new ResponseEntity<List<MaCompany>>(typeaheadDelegate.getDomainNameCentral(companyId), HttpStatus.OK);
	}
	
	/* population of all audits, certificates, users count to display dashborard information */
	@GetMapping(value = "/getDashboardData")
	public ResponseEntity<Map<String,Object>> getDashboardData() {
		return new ResponseEntity<Map<String,Object>>(typeaheadDelegate.getDashboardData(), HttpStatus.OK);
	}
}
