package com.api.central.support.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SupportDetail;
import com.api.central.support.delegate.SupportDelegate;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/audit/support")
public class SupportController {

	@Autowired
	private SupportDelegate supportDelegate;

	@Autowired
	AppUtil appUtil;

	@PostMapping(value = "/getReterivedAudits")
	public  ResponseEntity< List<AuditDetailView>> getReterivedAudits(@RequestBody SearchCriteria auditDtls) {
		return new ResponseEntity<>(supportDelegate.getReterivedAudits(auditDtls), HttpStatus.OK);
	}


	@PostMapping(value = "/getSupportAuditDtls")
	public  ResponseEntity<List<SupportDetail>> getSupportAuditDtls(@RequestBody SearchCriteria auditDtls) {	
		return new ResponseEntity<>(supportDelegate.getSupportAuditDtls(auditDtls), HttpStatus.OK);
	}


	@PostMapping(value = "/fileUpload")
	public  ResponseEntity<Map<String,String>> fileUpload(@RequestBody SupportDetail supportDetail) throws JsonParseException, JsonMappingException, IOException {

		StringBuilder directoryBuilder =  new StringBuilder(AppConstant.UPLOAD_DIR).append(supportDetail.getCompanyId()).append( File.separator).append("BACKUP")
				.append(File.separator).append("AuditDetails").append(File.separator).append(supportDetail.getAudTypeDesc()).append(File.separator).append(supportDetail.getAudSubTypeDesc())
				.append(File.separator).append(supportDetail.getAuditSeqNo());

		File zipFile = new File(directoryBuilder.toString());

		Map<String,String> map = new HashMap<>();

		if (zipFile.exists()) {
			zipFile.delete();
		}else{
			zipFile.mkdirs();
		}

		Path path = Paths.get(directoryBuilder + AppConstant.SEPARATOR + supportDetail.getAuditSeqNo() + AppConstant.ZIP);

		Files.write(path, supportDetail.getFileData());

		File zipFileCreated = new File(directoryBuilder + AppConstant.SEPARATOR + supportDetail.getAuditSeqNo() + AppConstant.ZIP);

		if (zipFile.exists()) {	

			if (appUtil.extractAllFiles(zipFileCreated,directoryBuilder.toString())) {

				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				AuditDetail auditDetail = new AuditDetail();

				if(new File(directoryBuilder+AppConstant.SEPARATOR+ supportDetail.getAuditSeqNo() + AppConstant.JSON).exists()){

					auditDetail = mapper.readValue(new File(directoryBuilder+AppConstant.SEPARATOR+ supportDetail.getAuditSeqNo() + AppConstant.JSON),
							AuditDetail.class);

					if(!auditDetail.getAuditSeqNo().equals(supportDetail.getAuditSeqNo())){
						map.put("val","Invalid File");
						
					}else if(!auditDetail.getVesselImoNo().equals(supportDetail.getVesselImoNo())){
						map.put("val","Invalid File");
						
					}else if(auditDetail.getAuditAuditorDetail().stream().filter(leadAuditor -> leadAuditor.getAudLeadStatus()==1 && leadAuditor.getUserId().equalsIgnoreCase(supportDetail.getLeadId())).count() != 1){
						map.put("val","Invalid File");	
						
					}else{
						supportDetail.setVesselName(auditDetail.getVesselName());
					}

				}else{
					map.put("val","Invalid File");
					FileUtils.deleteDirectory(new File(directoryBuilder.toString()));
				}
			}
			
			if(map.size() == 0){
				return new ResponseEntity<>(supportDelegate.saveData(supportDetail), HttpStatus.OK);
			}else{
				FileUtils.deleteDirectory(new File(directoryBuilder.toString()));
				return new ResponseEntity<>(map, HttpStatus.OK);
			}

		}else{
			map.put("val","Error Occurred while uploading the file please contact admin");
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/getSupportData")
	public ResponseEntity<List<SupportDetail>> getSupportData(){
		return new ResponseEntity<>(supportDelegate.getSupportData(),HttpStatus.OK);
	}

	@GetMapping(value = "/getLeadName")
	public ResponseEntity<Map<String,String>> getLeadName(@RequestParam Integer auditSeqNo,@RequestParam Long companyId){
		return new ResponseEntity<>(supportDelegate.getLeadName(auditSeqNo,companyId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/downloadReterivedAudit/{auditSeqNo}/{vesselImoNo}/{auditTypeId}/{auditSubTypeId}/{companyId}")
	public ResponseEntity<byte[]> downloadReterivedAudit(@PathVariable Integer auditSeqNo,@PathVariable Integer vesselImoNo,
			@PathVariable Integer auditTypeId, @PathVariable Integer auditSubTypeId,@PathVariable Long companyId) throws IOException {

		SupportDetail supportDtls= supportDelegate.getDownloadFile(auditSeqNo,vesselImoNo,auditTypeId,auditSubTypeId,companyId);
		
		if(supportDtls != null){

			StringBuilder directoryBuilder =  new StringBuilder(AppConstant.UPLOAD_DIR).append(supportDtls.getCompanyId()).append( File.separator).append("BACKUP")
					.append(File.separator).append("AuditDetails").append(File.separator).append(supportDtls.getAudTypeDesc()).append(File.separator).append(supportDtls.getAudSubTypeDesc())
					.append(File.separator).append(supportDtls.getAuditSeqNo());

			File zipFile = new File(directoryBuilder.toString() + AppConstant.ZIP);

			if (!zipFile.exists()) {
				Path path = Paths.get(directoryBuilder.toString() + AppConstant.ZIP);
				Files.write(path, supportDtls.getFileData());
				zipFile = new File(directoryBuilder.toString() + AppConstant.ZIP);	
			}

			return new ResponseEntity<>(supportDtls.getFileData(), appUtil.setHeaderType(zipFile.getAbsolutePath(), auditSeqNo + AppConstant.ZIP),
					HttpStatus.OK);

		}else{
			return new ResponseEntity<>( new byte[0],HttpStatus.OK);

		}


	}
	
	@GetMapping(value = "/getReterivedAuditData/{auditSeqNo}/{auditType}/{auditSubType}/{companyId}")
	public ResponseEntity<Map<String, Object>> getReterivedAuditData(@PathVariable Integer auditSeqNo,
			@PathVariable String auditType, @PathVariable String auditSubType,@PathVariable Long companyId) throws IOException {

		StringBuilder directoryBuilder =  new StringBuilder(AppConstant.UPLOAD_DIR).append(companyId).append( File.separator).append("BACKUP")
				.append(File.separator).append("AuditDetails").append(File.separator).append(auditType).append(File.separator).append(auditSubType)
				.append(File.separator).append(auditSeqNo);

		
		Map<String, Object> map = new HashMap<>();
		

		File zipFile = new File(directoryBuilder +  AppConstant.SEPARATOR + auditSeqNo + AppConstant.ZIP);

		if (zipFile.exists()) {

			if (appUtil.extractAllFiles(zipFile,directoryBuilder.toString())) {

				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				if(new File(directoryBuilder+AppConstant.SEPARATOR+ auditSeqNo+ AppConstant.JSON).exists()){
					
					AuditDetail auditDetail = new AuditDetail();
					
					auditDetail = mapper.readValue(new File(directoryBuilder+AppConstant.SEPARATOR+ auditSeqNo + AppConstant.JSON),
							AuditDetail.class);
					
					map.put("auditDetail", auditDetail);
					
					String previousPath = directoryBuilder + AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING;
					
					if (new File(previousPath+ AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING + AppConstant.JSON).exists()) {
						
						List<AuditFinding> auditFindings = new ArrayList<AuditFinding>();
						
						auditFindings = mapper.readValue(new File(previousPath + AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING + AppConstant.JSON),
								mapper.getTypeFactory().constructCollectionType(List.class, AuditFinding.class));
						
						map.put("PrevAuditFindings", auditFindings);
					}
				}
			}
		}

		return new ResponseEntity<>(map, HttpStatus.OK);

	}
	

}
