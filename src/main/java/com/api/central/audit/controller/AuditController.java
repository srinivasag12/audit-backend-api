package com.api.central.audit.controller;

import java.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.audit.delegate.AuditDelegate;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.AuditFindingModel;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.FileDownloadHistory;
import com.api.central.audit.entity.FileToUpload;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.audit.entity.ReportData;
import com.api.central.audit.entity.StampDetails;
import com.api.central.audit.entity.SyncAudit;
import com.api.central.audit.entity.VesselApprovalDetails;
import com.api.central.audit.entity.VesselDetailsHistory;
import com.api.central.audit.entity.VesselNotification;
import com.api.central.config.AppHibSession;
import com.api.central.fileDownload.FileDownloadRepository;
import com.api.central.fileDownload.FileOperationValues;
import com.api.central.master.entity.MaVessel;
import com.api.central.refreshVesselData.VesselRefresh;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;
import com.api.central.util.SequenceGenerator;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;






@RestController
@RequestMapping("api/audit/ism")
public class AuditController {

	@Autowired
	private AuditDelegate  auditDelegate;

	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Autowired
	AppUtil appUtil;

	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	FileOperationValues values;

	private String auditDate;
	
	
	@Autowired
	private FileDownloadRepository repo;
	
	private static final Logger log = LoggerFactory.getLogger(AuditController.class);
	
 	@PostMapping(value = "/create/{status}/{auditTypeId}/{companyId}")
	public ResponseEntity<AuditDetail> createISM(@Valid @RequestBody AuditDetail auditDetail,
			@PathVariable String status,@PathVariable Integer auditTypeId,@PathVariable Long companyId) {
 		
 		
 		AuditDetail audData = auditDelegate.createOrUpdateISM(auditDetail, status,auditTypeId,companyId,"notSync");
 		if(status.equalsIgnoreCase(AppConstant.CREATE)){
 			
 			auditDelegate.callSendMailCreateProc(audData);
 		}else if(status.equalsIgnoreCase(AppConstant.UPDATE)){
 			if(auditDetail.getAuditStatusId()==AppConstant.VOID_AUDIT_STATUS){
 				auditDelegate.callDeleteUpdatedStatusOnAudVoidProc(audData);
 			}
 		}

		return new ResponseEntity<AuditDetail>(audData, HttpStatus.CREATED);
	}
 	
 	@PostMapping(value = "/saveReportData/{auditTypeId}/{companyId}")
	public ResponseEntity<ReportData> saveReportData(@Valid @RequestBody ReportData reportData,
			@PathVariable Integer auditTypeId,@PathVariable Long companyId) {
 		ReportData reptData = auditDelegate.saveReportHistoryData(reportData,auditTypeId,companyId);
 		/*if(status.equalsIgnoreCase(AppConstant.CREATE)){
 			
 			auditDelegate.callSendMailCreateProc(audData);
 		}*/

		return new ResponseEntity<ReportData>(reptData, HttpStatus.CREATED);
	}

	@GetMapping(value = "/newCertificateNo/{auditTypeId}/{auditSubTypeId}/{companyId}")
	public ResponseEntity<String> newCertificateNo(@PathVariable Integer auditTypeId,@PathVariable Integer auditSubTypeId,@PathVariable Long companyId) {
		if(auditTypeId==AppConstant.SSP_TYPE_ID || auditTypeId==AppConstant.DMLC_TYPE_ID || (auditTypeId>=AppConstant.SOPEP_TYPE_ID && auditTypeId<=AppConstant.COW_TYPE_ID))  // Changed by Sudharsan (SWR to SDR)
		{
			return new ResponseEntity<String>(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(), HttpStatus.OK);
		
	   }else{
		return new ResponseEntity <String>(AppUtil.getCertificateNoFormat(auditTypeId,sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),auditSubTypeId), HttpStatus.OK);
	   }
	}

	@GetMapping(value = "/getAuditSeqNo/{auditTypeId}/{companyId}")
	public ResponseEntity<Long> getAuditSeqNo(@PathVariable Integer auditTypeId, @PathVariable Long companyId) {

		return new ResponseEntity<Long>(sequenceGenerator.generateaSequence("AUDITSEQNO"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditReportNo/{auditTypeId}/{companyId}")
	public ResponseEntity<Long> getAuditReportNo(@PathVariable Integer auditTypeId, @PathVariable Long companyId) {

		return new ResponseEntity<Long>(sequenceGenerator.generateaSequence("AUDITREPORTSEQNO"), HttpStatus.OK);
	}

	@GetMapping(value = "/getAuditDetail/{auditTypeId}/{auditSeqNo}/{companyId}")
	public ResponseEntity<AuditDetail> getAuditDetail(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId) {
		return new ResponseEntity<AuditDetail>(auditDelegate.getAuditDetail(auditTypeId,
				 auditSeqNo,  companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditDetailForIhm/{auditTypeId}/{auditSeqNo}/{companyId}")
	public ResponseEntity<AuditDetailIhm> getAuditDetailForIhm(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId) {
		return new ResponseEntity<AuditDetailIhm>(auditDelegate.getAuditDetailForIhm(auditTypeId,
				 auditSeqNo,  companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAllIhmAuditDetail/{auditTypeId}/{vesselImoNo}/{companyId}")
	public ResponseEntity<List<AuditDetailIhm>> getAllIhmAuditDetail(@PathVariable Integer auditTypeId, @PathVariable Integer vesselImoNo,
			@PathVariable Long companyId) {
		return new ResponseEntity<List<AuditDetailIhm>>(auditDelegate.getAllIhmAuditDetail(auditTypeId,
				vesselImoNo,  companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditRelatedData/{auditSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getAuditRelatedData(@PathVariable Integer auditSeqNo, @PathVariable Long companyId) {
		return new ResponseEntity<Map<String,Object>>(auditDelegate.getAuditRelatedData(
				 auditSeqNo,  companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSspReviewDetail/{imoNo}/{auditTypeId}/{companyId}")
	public ResponseEntity<List<AuditDetail>> getSspReviewDetail(@PathVariable Integer imoNo,@PathVariable Integer auditTypeId,
			@PathVariable Long companyId) {
		return new ResponseEntity<List<AuditDetail>>(auditDelegate.getSspReviewDetail(imoNo,auditTypeId,
				 companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditDetailAndCheckSameAudit/{auditTypeId}/{auditSeqNo}/{companyId}/{vesselImoNo}")
	public ResponseEntity<Map<String,Object>> getAuditDetailAndCheckSameAudit(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable Integer vesselImoNo) {
		return new ResponseEntity<Map<String,Object>>(auditDelegate.getAuditDetailAndCheckSameAudit(auditTypeId,
				 auditSeqNo,  companyId, vesselImoNo), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAuditDetailAndNextAdtCrtStatus/{auditTypeId}/{companyId}/{auditSeqNo}/{status}/{vesselImoNo}")
	public ResponseEntity<Map<String,Object>> getAuditDetailAndNextAdtCrtStatus(@PathVariable Integer auditTypeId, @PathVariable Long companyId, @PathVariable Integer auditSeqNo,
			@PathVariable Boolean status, @PathVariable Integer vesselImoNo) {
		return new ResponseEntity<Map<String,Object>>(auditDelegate.getAuditDetailAndNextAdtCrtStatus(auditTypeId,companyId,
				 auditSeqNo,  status, vesselImoNo), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSignature/{emailId}/{companyId}")
	public ResponseEntity<Map<String,Object>> getSignature(@PathVariable String emailId,@PathVariable Long companyId) {
		
		return new ResponseEntity<Map<String,Object>>(auditDelegate.getSignature(emailId,companyId), HttpStatus.OK);
	}

	@GetMapping(value = "/getAuditRptAttach/{fileName}/{auditSeqNo}/{auditTypeId}/{companyId}")
	public ResponseEntity<Resource> auditRptAttach(@PathVariable Integer auditSeqNo, @PathVariable String fileName,@PathVariable Integer auditTypeId,@PathVariable Long companyId) {
		String path = servletContext.getContextPath() + AppConstant.SEPARATOR +companyId+ AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + auditSeqNo + AppConstant.SEPARATOR + fileName;
		return new ResponseEntity<Resource>(appUtil.getFileByteStream(path), appUtil.setHeaderStreamType(path, fileName),
				HttpStatus.OK);
	}

	@GetMapping(value = "/getFindingStatusAttach/{origAuditSeqNo}/{findingSeqNo}/{fileName}/{statusSeqNo}/{auditTypeId}/{companyId}")
	public ResponseEntity<Resource> getFindingStatusAttach(@PathVariable String origAuditSeqNo,
			@PathVariable String findingSeqNo, @PathVariable String statusSeqNo, @PathVariable String fileName,@PathVariable Integer auditTypeId,@PathVariable Long companyId) {
		String path = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + origAuditSeqNo + AppConstant.SEPARATOR + findingSeqNo + AppConstant.SEPARATOR
				+ statusSeqNo + AppConstant.SEPARATOR + fileName;
		return new ResponseEntity<Resource>(appUtil.getFileByteStream(path), appUtil.setHeaderStreamType(path, fileName),
				HttpStatus.OK);
	}
	
	@PostMapping(value = "/auditFindings/{status}/{findingType}/{auditTypeId}/{companyId}")
	//changed by @Ramya for Jira id - IRI-5633
	public ResponseEntity<AuditFindingModel> auditFindings(@RequestBody AuditFindingModel auditFinding,
			@PathVariable String status, @PathVariable String findingType,@PathVariable Integer auditTypeId,@PathVariable Long companyId) {
		auditFinding = auditDelegate.auditFindings(auditFinding, status, findingType,auditTypeId,companyId);

		return new ResponseEntity<AuditFindingModel>(auditFinding, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/getPreviousFinding/{auditTypeId}/{auditDate}/{vesselIMONo}/{currentAuditSeqNo}/{companyId}/{companyImoNo}/{companyDoc}")
	public ResponseEntity<List<AuditFinding>> getPreviousFinding(@PathVariable Integer auditTypeId,
			@PathVariable String auditDate, @PathVariable Integer vesselIMONo, @PathVariable Integer currentAuditSeqNo,
			@PathVariable Long companyId, @PathVariable String companyImoNo, @PathVariable String companyDoc) {

		List<AuditFinding> auditFindings = auditDelegate.getPreviousFinding( auditTypeId,
				appUtil.convertStringToDate(auditDate), vesselIMONo,companyId,currentAuditSeqNo,companyImoNo,companyDoc);

		return new ResponseEntity<List<AuditFinding>>(auditFindings, HttpStatus.OK);
	}

	@GetMapping(value = "/currentFinding/{auditTypeId}/{currentAuditSeqNo}/{companyId}")
	public ResponseEntity<List<AuditFinding>> currentFinding(@PathVariable Integer auditTypeId,
			@PathVariable Integer currentAuditSeqNo, @PathVariable Long companyId) {
		List<AuditFinding> auditFindings = auditDelegate.currentFinding( auditTypeId,
				 companyId,  currentAuditSeqNo);

		return new ResponseEntity<List<AuditFinding>>(auditFindings, HttpStatus.OK);
	}

	@GetMapping(value = "/getPreviousAuditDetail/{auditTypeId}/{vesselIMONo}/{companyId}")
	public ResponseEntity<Map<String, Object>> getPreviousAuditDetail(@PathVariable Integer auditTypeId,
			@PathVariable Integer vesselIMONo, @PathVariable Long companyId) {

		Map<String, Object> auditDetails = auditDelegate.getPreviousAuditDetail(auditTypeId,
				vesselIMONo, companyId);

		return new ResponseEntity<Map<String, Object>>(auditDetails, HttpStatus.OK);
	}

	/**
	 * @param auditTypeId
	 * @param auditSeqNo
	 * @param companyId
	 * @param leadStatus
	 * @param timeStamp
	 * @return
	 * @throws IOException
	 */
	

	
	@PostMapping(value = "/retrieveAudit/{auditTypeId}/{auditSeqNo}/{companyId}/{leadStatus}/{timeStamp}")
	public ResponseEntity<byte[]> retrieveAudit(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable Integer leadStatus,@PathVariable Long timeStamp) throws IOException {
		String path = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + auditSeqNo;
		// commented by chibi for zipped folder not to be created with the time stamp
		// String zipFile = path+timeStamp + AppConstant.ZIP;
		String zipFile = path + AppConstant.ZIP;
		byte[] bs = null;
		auditDelegate.getLaptopPreviousFinding( auditTypeId, auditSeqNo,companyId);
		File dir = new File(path);
		if (appUtil.zipDirectory(dir, zipFile )) {
			bs = appUtil.getFileByte(zipFile);
			// line commented by chibi for deleting the audit and retrieving the audit it
			// again files are not displaying
			// appUtil.deleteFiles(path, auditSeqNo.toString());
			// auditDelegate.updateLockStatus( auditTypeId, auditSeqNo,
			// companyId,leadStatus);
		}
		return new ResponseEntity<byte[]>(bs, appUtil.setHeaderType(zipFile, auditSeqNo + AppConstant.ZIP),
				HttpStatus.OK);
	}
	
	@PostMapping(value = "/mobileRetrieveAudit1/{auditTypeId}/{auditSeqNo}/{companyId}/{leadStatus}/{timeStamp}")
	public ResponseEntity<Map<String,String>> mobileRetrieveAudit1(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable Integer leadStatus,@PathVariable Long timeStamp) throws IOException {
		/*String path = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + auditSeqNo; */
		String path = servletContext.getContextPath() + AppConstant.SEPARATOR + auditSeqNo;
		
		FilePreview file=new FilePreview();
		String zipFile = path + AppConstant.ZIP;
		Map<String,String> zipMap = new HashMap<String,String>();
		byte[] bs = null;
		auditDelegate.getMobilePreviousFinding( auditTypeId, auditSeqNo,companyId);
		File dir = new File(path);
		
		
		if (appUtil.mobileZipDirectory(dir, zipFile )) {
			bs = appUtil.getFileByte(zipFile);
			appUtil.deleteFiles(path, auditSeqNo.toString());
			//auditDelegate.updateLockStatus( auditTypeId,  auditSeqNo, companyId,leadStatus);
			
			
		}
		
		zipMap.put("zipFile",zipB64(zipFile));
		zipMap.put("fileName",auditSeqNo+".zip");
		
		return new ResponseEntity<Map<String,String>>(zipMap,HttpStatus.OK);
	}
	
	
	private static String zipB64(String zipFile) throws IOException {
	    byte[] buffer = new byte[1024];
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
	        
	            try (FileInputStream fis = new FileInputStream(zipFile)) {
	                zos.putNextEntry(new ZipEntry(zipFile));
	                int length;
	                while ((length = fis.read(buffer)) > 0) {
	                    zos.write(buffer, 0, length);
	                }
	                zos.closeEntry();
	            }
	        
	    }
	    byte[] bytes = baos.toByteArray();
	  
	    return new String(Base64.getEncoder().encodeToString(bytes));
	}
	
	
	@PostMapping(value = "/mobileRetrieveAudit/{auditTypeId}/{auditSeqNo}/{companyId}/{leadStatus}/{timeStamp}") 
	public ResponseEntity<Map<String,String>> mobileRetrieveAudit(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
				@PathVariable Long companyId, @PathVariable Integer leadStatus,@PathVariable Long timeStamp) throws IOException {
	
		Map<String,String> zipMap = new HashMap<String,String>();
		
		String path = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
					+ AppConstant.SEPARATOR + auditSeqNo;
		
		// commented by chibi for zipped folder not to be created with the time stamp
		// String zipFile = path+timeStamp + AppConstant.ZIP;
		String zipFile = path + AppConstant.ZIP;
	
		auditDelegate.getLaptopPreviousFinding( auditTypeId, auditSeqNo,companyId);
		File dir = new File(path);
		if (appUtil.mobileZipDirectory(dir, zipFile )) {
			// line commented by chibi for deleting the audit in mobile and retrieving the audit again files are not displaying
			// appUtil.deleteFiles(path, auditSeqNo.toString());
			
		}
	
		zipMap.put("zipFile",appUtil.encodeFileToBase64Binary(zipFile));
		zipMap.put("fileName",auditSeqNo+".zip");
		return new ResponseEntity<Map<String,String>>(zipMap,HttpStatus.OK);
	}
	
	
	
	
/*
	@PostMapping(value = "/mobileRetrieveAudit1/{auditTypeId}/{auditSeqNo}/{companyId}/{leadStatus}/{timeStamp}") 
	public ResponseEntity<byte[]> mobileRetrieveAudit1(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
				@PathVariable Long companyId, @PathVariable Integer leadStatus,@PathVariable Long timeStamp) throws IOException {
	
		Map<String,String> zipMap = new HashMap<String,String>();
		FilePreview preview = new FilePreview();
		String path = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
					+ AppConstant.SEPARATOR + auditSeqNo;
		
		String zipFile = path +timeStamp+ AppConstant.ZIP;
	
		auditDelegate.getLaptopPreviousFinding( auditTypeId, auditSeqNo,companyId);
		
		log.info("boolen val is==");
		log.info("boolen val is isis " +zipFile);
		File dir = new File(path);
          if (appUtil.mobileZipDirectory(dir, zipFile )) {
			
			appUtil.deleteFiles(path, auditSeqNo.toString());
			
		}
		
		byte[] fileByte = null;
		String encodedString = null;
		if (path != null) {
			log.info("Reading file bytes");
			fileByte = appUtil.getFileByte(zipFile);
			System.out.println("fileByte the String" +fileByte);
			if (fileByte != null) {
			encodedString = Base64.getMimeEncoder().encodeToString(fileByte);
			System.out.println("fileByte the encodedString22" +encodedString);
			}
		}
		preview.setFileData(encodedString);
		
		   
	
		return new ResponseEntity<byte[]>(fileByte,HttpStatus.OK);
	}*/
	
	public String encodeFileToBase64Binary1(String fileName) throws IOException {
System.out.println("file na,e"+ fileName);
		File file = new File(fileName);
		byte[] bytes = appUtil.loadFile(file);
		//byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString =null;// new String(encoded);
		encodedString = Base64.getMimeEncoder().encodeToString(bytes);
		System.out.println("the string "+ encodedString);
		return encodedString;
	}
	
	
	
	@GetMapping(value="/updateLockStatus/{auditTypeId}/{auditSeqNo}/{companyId}/{emailId}/{leadStatus}")
	public ResponseEntity<Long> updateLockStatus(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable String emailId,@PathVariable Integer leadStatus){
		auditDelegate.updateLockStatus( auditTypeId,  auditSeqNo, companyId,leadStatus);
		//if(emailId.length()>1){
			auditDelegate.updateLockHolder(auditTypeId, auditSeqNo, companyId, emailId);
		//}
		
		return new ResponseEntity<Long>(1L,HttpStatus.OK);
	}
	
	@GetMapping(value="/updateLockStatusDelAdt/{auditTypeId}/{auditSeqNo}/{companyId}/{emailId}/{leadStatus}")
	public ResponseEntity<Long> updateLockStatusDelAdt(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable String emailId,@PathVariable Integer leadStatus){
		auditDelegate.updateLockStatus( auditTypeId,  auditSeqNo, companyId,leadStatus);
		
		return new ResponseEntity<Long>(1L,HttpStatus.OK);
	}

	@DeleteMapping("/deleteFinding/{auditTypeId}/{auditSeqNo}/{companyId}/{findingSeqNo}")
	public ResponseEntity<String> deleteFinding(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable Integer findingSeqNo) throws IOException {

		return new ResponseEntity<String>(auditDelegate.deleteFinding(auditTypeId,
				auditSeqNo, companyId, findingSeqNo),
				HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteFindingDmlcLinked/{auditTypeId}/{auditSeqNo}/{companyId}/{findingSeqNo}")
	public ResponseEntity<String> deleteFindingDmlcLinked(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId, @PathVariable Integer findingSeqNo) throws IOException {
		System.out.println("auditTypeId123456");
		return new ResponseEntity<String>(auditDelegate.deleteFindingDmlcLinked(auditTypeId,
				auditSeqNo, companyId, findingSeqNo),
				HttpStatus.OK);
	}

	@PostMapping(value = "/synchronize/{auditTypeId}/{companyId}")
	public ResponseEntity<String> synchronize(@RequestBody SyncAudit syncAudit,@PathVariable Integer auditTypeId, @PathVariable Long companyId) throws IOException { 
		
		String pathSrc = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId);
		String status = "";

		Path path = Paths.get(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo() + AppConstant.ZIP);
			Files.write(path, syncAudit.getFileByte());
		
		File zipFile = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + syncAudit.getAuditSeqNo() + AppConstant.ZIP);
		if (zipFile.exists()) {	
				
			if (appUtil.extractAllFiles(zipFile, pathSrc+ AppConstant.SEPARATOR + syncAudit.getAuditSeqNo())) { 		
				if (auditDelegate.synchronizeAudit(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo(),
						syncAudit.getAuditSeqNo().toString(),auditTypeId,companyId)) {
					if (auditDelegate.syncTimeUpdateRmiData(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo(),
							syncAudit.getAuditSeqNo().toString(),auditTypeId,companyId)) {
					}
					status = "OK";
				}
			}
		}

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
	//added by @Ramya for Ticket-659
	@PostMapping(value = "/synchronizeLinkedDMLC/{auditTypeId}/{companyId}")
	public ResponseEntity<String> synchronizeLinkedDMLC(@RequestBody SyncAudit syncAudit,@PathVariable Integer auditTypeId, @PathVariable Long companyId) throws IOException { 
		
		String pathSrc = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId);
		String status = "";

		Path path = Paths.get(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo() + AppConstant.ZIP);
			Files.write(path, syncAudit.getFileByte());
		
		File zipFile = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + syncAudit.getAuditSeqNo() + AppConstant.ZIP);
		if (zipFile.exists()) {	
				
			if (appUtil.extractAllFiles(zipFile, pathSrc+ AppConstant.SEPARATOR + syncAudit.getAuditSeqNo())) { 		
				if (auditDelegate.synchronizeLinkedDMLC(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo(),
						syncAudit.getAuditSeqNo().toString(),auditTypeId,companyId)) {
					if (auditDelegate.syncTimeUpdateRmiData(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo(),
							syncAudit.getAuditSeqNo().toString(),auditTypeId,companyId)) {
					}
					status = "OK";
				}
			}
		}

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/mobileSynchronize/{auditTypeId}/{companyId}")
	public ResponseEntity<String> mobileSynchronize(@RequestBody SyncAudit syncAudit,@PathVariable Integer auditTypeId, @PathVariable Long companyId) throws IOException { 
		
		String pathSrc = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId);
		String status = "";

		Path path = Paths.get(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo() + AppConstant.ZIP);
			Files.write(path, syncAudit.getFileByte());
		
		File zipFile = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + syncAudit.getAuditSeqNo() + AppConstant.ZIP);
		if (zipFile.exists()) {	
//			if (appUtil.extractAllFiles(zipFile, pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo())) { 				
			if (appUtil.unZipArchive(zipFile, pathSrc)) { 				
				if (auditDelegate.synchronizeAudit(pathSrc + AppConstant.SEPARATOR + syncAudit.getAuditSeqNo(),
						syncAudit.getAuditSeqNo().toString(),auditTypeId,companyId)) {
					status = "OK";
				}
			}
		}

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
	
	
	
	@GetMapping(value = "/updateLockHolder/{auditTypeId}/{auditSeqNo}/{lockHolder}/{companyId}")
	public ResponseEntity<Map<String,String>> updateLockHolder(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable String lockHolder, @PathVariable Long companyId){
		 
		return new ResponseEntity<Map<String,String>> (auditDelegate.updateLockHolder(auditTypeId,auditSeqNo,companyId,lockHolder),HttpStatus.OK);
	}
	
	@GetMapping(value = "/updateDocFlag/{auditTypeId}/{auditSeqNo}/{companyId}/{docFlag}")
	public ResponseEntity<Map<String,Object>> updateDocFlag(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			 @PathVariable Long companyId,@PathVariable Integer docFlag){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.updateDocFlag(auditTypeId,auditSeqNo,companyId,docFlag),HttpStatus.OK);
	}
	
	@GetMapping(value = "/checkAuditLockHolder/{auditTypeId}/{auditSeqNo}/{userId}/{companyId}")
	public ResponseEntity<Map<String,String>> checkAuditLockHolder(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable String userId, @PathVariable Long companyId){
		 
		return new ResponseEntity<Map<String,String>> (auditDelegate.checkAuditLockHolder(auditTypeId,auditSeqNo,companyId,userId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSSPDetails/{vesselImoNo}/{auditTypeId}/{companyId}/{auditSubTypeId}/{auditSeqNo}")
	public ResponseEntity<Map<String,Object>> getSSPDetails(@PathVariable Integer vesselImoNo, @PathVariable Integer auditTypeId, @PathVariable Long companyId,
			@PathVariable Integer auditSubTypeId, @PathVariable Integer auditSeqNo){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.getSSPDetails(vesselImoNo,auditTypeId,companyId,auditSubTypeId,auditSeqNo),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getISPSInitialDetails/{vesselImoNo}/{auditTypeId}/{companyId}")
	public ResponseEntity<Map<String,Object>> getISPSInitialDetails(@PathVariable Integer vesselImoNo, @PathVariable Integer auditTypeId, @PathVariable Long companyId){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.getISPSInitialDetails(vesselImoNo,auditTypeId,companyId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getPrevDocDetails/{compImoNo}/{compDocNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> getPrevDocDetails(@PathVariable String compImoNo, @PathVariable String compDocNo, @PathVariable Long companyId){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.getPrevDocDetails(compImoNo,compDocNo,companyId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSspIspsData/{vesselImoNo}/{auditTypeId}/{companyId}/{auditSubTypeId}")
	public ResponseEntity<Map<String,Object>> getSspIspsData(@PathVariable Integer vesselImoNo, @PathVariable Integer auditTypeId, @PathVariable Long companyId,
			@PathVariable Integer auditSubTypeId){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.getSspIspsData(vesselImoNo,auditTypeId,companyId,auditSubTypeId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSSPRevisionNo/{vesselImoNo}/{auditTypeId}/{companyId}/{auditSeqNo}")
	public ResponseEntity<Map<String,Object>> getSSPRevisionNo(@PathVariable Integer vesselImoNo, @PathVariable Integer auditTypeId, @PathVariable Long companyId,
			@PathVariable Integer auditSeqNo){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.getSSPRevisionNo(vesselImoNo,auditTypeId,companyId,auditSeqNo),HttpStatus.OK);
	}
	
	@GetMapping(value = "/checkLinkedWithIspsOrMLC/{auditTypeId}/{auditSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> checkLinkedWithIspsOrMLC(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.checkLinkedWithIspsOrMLC(auditTypeId,auditSeqNo,companyId),HttpStatus.OK);
	}
	
	@GetMapping(value = "/checkAuditLockStatus/{auditTypeId}/{auditSeqNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> checkAuditLockStatus(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.checkAuditLockStatus(auditTypeId,auditSeqNo,companyId),HttpStatus.OK);
	}
	
	@PostMapping(value = "/unlinkFindingFiles/{auditTypeDesc}/{companyId}")
	public ResponseEntity<FindingRptAttach> unlinkFindingFiles(@PathVariable String auditTypeDesc,@RequestBody FindingRptAttach findingRptAttach,@PathVariable Long companyId) {
		
		return new ResponseEntity<FindingRptAttach>(auditDelegate.unlinkFindingFiles(findingRptAttach,auditTypeDesc,companyId), HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/checkAuditStatus/{auditSeqNo}/{auditTypeId}/{companyId}")
	public ResponseEntity<Map<String, Object>> checkLeadStatus(@PathVariable Integer auditSeqNo,@PathVariable Integer auditTypeId,@PathVariable Long companyId){
		return new ResponseEntity<Map<String,Object>>(auditDelegate.checkLeadStatus(auditSeqNo,auditTypeId,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value="/updateReviewerStatus/{audSeqNo}/{audType}/{companyId}/{revStatus}")
	public ResponseEntity<Long> updateReviewerStatus(@PathVariable Integer audSeqNo, @PathVariable Integer audType, @PathVariable Long companyId, @PathVariable Integer revStatus){
		return new ResponseEntity<Long>(auditDelegate.updateReviewerStatus(audSeqNo,audType,companyId,revStatus), HttpStatus.OK);
	}
	
	@GetMapping(value="/getCountOfLockedAuditByCurrUser/{emailId}/{companyId}")
	public ResponseEntity<Map<String, Object>> getCountOfLockedAuditByCurrUser(@PathVariable String emailId, @PathVariable Long companyId){
		return new ResponseEntity<Map<String, Object>>(auditDelegate.getCountOfLockedAuditByCurrUser(emailId,companyId), HttpStatus.OK);
	}
	
	@GetMapping(value="/vesselMissingMailCall/{auditTypeId}/{companyId}/{vesselImoNo}/{vesselName}")
	public ResponseEntity<Long> vesselMissingMailCall(@PathVariable Integer auditTypeId, @PathVariable Long companyId, @PathVariable Integer vesselImoNo, @PathVariable String vesselName){
		return new ResponseEntity<Long>(auditDelegate.vesselMissingMailCall(auditTypeId,companyId,vesselImoNo,vesselName), HttpStatus.OK);
	}
	
	@PostMapping(value="/getRmiProced/{companyId}/{vesselImoNo}")
	public ResponseEntity<VesselNotification> getRmiProced(@RequestBody VesselNotification obj,@PathVariable Long companyId,@PathVariable Integer vesselImoNo){
		
		return new ResponseEntity<VesselNotification>(auditDelegate.getRmiProced(obj,companyId,vesselImoNo), HttpStatus.OK);
	}
	
	
	
	
	@PostMapping(value="/tcApprovalStatus/{companyId}/{vesselImoNo}")
	public ResponseEntity<List<MaVessel>> tcApprovalStatus(@RequestBody String flag,@PathVariable Long companyId, @PathVariable Integer vesselImoNo){
		JSONObject jsonObj = new JSONObject(flag);
		String id = jsonObj.getString("emailId");
		return new ResponseEntity<List<MaVessel>>(auditDelegate.tcApprovalStatus(id,companyId,vesselImoNo), HttpStatus.OK);
	}
	
	
	/* saving and Updating audit code details */
	@PostMapping(value = "/tcDetails")
	public ResponseEntity<VesselApprovalDetails> saveAuditCodes(@RequestBody VesselApprovalDetails vesselApprovalDetails) {
		return new ResponseEntity<VesselApprovalDetails>(auditDelegate.savetcDetails(vesselApprovalDetails), HttpStatus.OK);
	}
	
	@PostMapping(value="/vesselDtlIncomplete/{userId}/{vesselImoNo}/{vesselId}/{companyId}")
	public ResponseEntity<Map<String,Object>> vesselDtlIncomplete(@PathVariable String userId, @PathVariable Integer vesselImoNo, @PathVariable Integer vesselId, @PathVariable Long companyId, @RequestBody PartialVesselLog partialVesselLog){
		return new ResponseEntity<Map<String,Object>>(auditDelegate.vesselDtlIncomplete(vesselImoNo,vesselId,userId,companyId, partialVesselLog), HttpStatus.OK);
	}
	@PostMapping(value="/vesselDtlIncompleteYatch/{userId}/{vesselName}/{vesselId}/{companyId}")
	public ResponseEntity<Map<String,Object>> vesselDtlIncomplete(@PathVariable String userId, @PathVariable String vesselName, @PathVariable Integer vesselId, @PathVariable Long companyId, @RequestBody PartialVesselLog partialVesselLog){
		return new ResponseEntity<Map<String,Object>>(auditDelegate.vesselDtlIncompleteYatch(vesselName,vesselId,userId,companyId, partialVesselLog), HttpStatus.OK);
	}
	@GetMapping(value="/vesselNotExist/{userId}/{vesselImoNo}/{companyId}")
	public ResponseEntity<Map<String,Object>> vesselNotExist(@PathVariable String userId, @PathVariable Integer vesselImoNo, @PathVariable Long companyId){
		return new ResponseEntity<Map<String,Object>>(auditDelegate.vesselNotExist(vesselImoNo,companyId,userId), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/allAuditorSign/{auditSeqNo}/{companyId}")
	public void allAuditorSign(@PathVariable Integer auditSeqNo,@PathVariable Integer companyId) {
			auditDelegate.allAuditorSign(auditSeqNo,companyId);
	}
	
	@GetMapping(value = "/carUpdateRemoveAuditorsSign/{auditSeqNo}/{companyId}/{auditTypeId}")
	public void carUpdateRemoveAuditorsSign(@PathVariable Integer auditSeqNo,@PathVariable Long companyId, @PathVariable Integer auditTypeId) {
			auditDelegate.carUpdateRemoveAuditorsSign(auditSeqNo,companyId,auditTypeId);
	}
	
	@GetMapping(value = "/updateLtrStatus/{auditTypeId}/{auditSeqNo}/{companyId}/{ltrStatus}")
	public ResponseEntity<Map<String,Object>> updateLtrStatus(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			 @PathVariable Long companyId,@PathVariable Integer ltrStatus){
		 
		return new ResponseEntity<Map<String,Object>> (auditDelegate.updateLtrStatus(auditTypeId,auditSeqNo,companyId,ltrStatus),HttpStatus.OK);
	}
	
	@GetMapping(value = "/getReportData/{auditTypeId}/{auditSeqNo}/{companyId}")
	public ResponseEntity<List<Map<String ,Object>>> getReportData(@PathVariable Integer auditTypeId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId) {
		return new ResponseEntity<List<Map<String ,Object>>>(auditDelegate.getReportData(auditTypeId,  auditSeqNo,  companyId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getReportBlobData/{versionId}/{auditSeqNo}/{companyId}")
	public ResponseEntity<byte[]> getReportBlobData(@PathVariable Integer versionId, @PathVariable Integer auditSeqNo,
			@PathVariable Long companyId) {
				
		ReportData r = auditDelegate.getReportBlobData(versionId,  auditSeqNo,  companyId);
		
		return new ResponseEntity<byte[]>(r.getReportBlob(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/createBlob/{currAuditseq}/{companyId}")
	public ResponseEntity<Boolean> createBlobDataforCurr(@PathVariable Integer currAuditseq,@PathVariable Long companyId) {
		return new ResponseEntity<Boolean>(auditDelegate.createBlobData(currAuditseq,  companyId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/createOrgBlob/{orgAuditseq}/{auditTypeId}/{companyId}")
	public ResponseEntity<Boolean> createBlobDataforOrg(@PathVariable Integer orgAuditseq,@PathVariable Integer auditTypeId,@PathVariable Long companyId) {
		return new ResponseEntity<Boolean>(auditDelegate.createBlobDataforOrg(orgAuditseq, auditTypeId, companyId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/signatureGenBlobData/{orgAuditseq}/{auditTypeId}/{companyId}/{versionId}")
	public ResponseEntity<Boolean> signatureGenBlobData(@PathVariable Integer orgAuditseq,@PathVariable Integer auditTypeId,@PathVariable Long companyId,@PathVariable Integer versionId) {
		return new ResponseEntity<Boolean>(auditDelegate.signatureGenBlobData(orgAuditseq, auditTypeId, companyId, versionId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getDMLCReportNos/{vesselImoNo}/{companyId}/{auditTypeId}")
	public ResponseEntity<List<Map<String ,Object>>> getDMLCReportNos(@PathVariable Integer vesselImoNo, @PathVariable Long companyId, @PathVariable Integer auditTypeId) {
		return new ResponseEntity<List<Map<String ,Object>>>(auditDelegate.getDMLCReportNos(vesselImoNo, companyId, auditTypeId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getPreviousFinding/{auditTypeId}/{vesselIMONo}/{companyId}/{companyImoNo}/{companyDoc}")
	public ResponseEntity<List<AuditFinding>> getPreviousFinding(@PathVariable Integer auditTypeId,
			@PathVariable Integer vesselIMONo,@PathVariable Long companyId, @PathVariable String companyImoNo, @PathVariable String companyDoc) {

		List<AuditFinding> auditFindings = auditDelegate.getPreviousFinding( auditTypeId,vesselIMONo,companyId,companyImoNo,companyDoc);

		return new ResponseEntity<List<AuditFinding>>(auditFindings, HttpStatus.OK);
	}
	
	@PostMapping(value = "/dmlcFinCloserEmail/{findingSeqNos}/{dmlcseqNo}/{mlcseqNo}/{companyId}")
	public ResponseEntity<Boolean> dmlcFinCloserEmail(@PathVariable String findingSeqNos, @PathVariable Integer dmlcseqNo, @PathVariable Integer mlcseqNo, @PathVariable Long companyId) {

		return new ResponseEntity<Boolean>(auditDelegate.dmlcFinCloserEmail( findingSeqNos,dmlcseqNo,mlcseqNo,companyId), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/upload")
	public int  pdfupload(@RequestBody StampDetails pdfUploadEntity) {
		
		return auditDelegate.pdfupload(pdfUploadEntity);
	}

	@PostMapping(value = "/uploadPdfInToMachine")
	public int uploadPdfInToMachine(@RequestBody FileToUpload fileToUpload) {
		
		return auditDelegate.uploadPdfInToMachine(fileToUpload);
		
	}
	
	@GetMapping(value = "/copyStampFile/{fileName}")
	public void pdfStampFile(@PathVariable String fileName) {
		auditDelegate.pdfStampFile(fileName);
	}
	
	@PostMapping(value = "/postPageNumbers")
	public ResponseEntity<Resource>  pdfPageNumbers(@RequestBody StampDetails pdfUploadEntity) {
		
		return auditDelegate.pdfPageNumbers(pdfUploadEntity);
	}
	
	@PostMapping(value = "/removeIhmFinalReport/{orgAuditseq}/{auditTypeId}/{companyId}/{versionId}")
	public ResponseEntity<Boolean> removeIhmFinalReport(@PathVariable Integer orgAuditseq,@PathVariable Integer auditTypeId,@PathVariable Long companyId,@PathVariable Integer versionId) {
		return new ResponseEntity<Boolean>(auditDelegate.removeIhmFinalReport(orgAuditseq, auditTypeId, companyId, versionId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getPdfSigned/{status}/{fileName}")
	public ResponseEntity<Resource> auditRptAttach(@PathVariable String status,@PathVariable String fileName) {
		
		return auditDelegate.auditRptAttach(status,fileName);
	}
	
	@GetMapping(value = "/getStamp/{REVIEW_REPORT_NO}/{AUDIT_SEQ_NO}")
	public List<Map<String ,Object>> auditGetStamp(@PathVariable String REVIEW_REPORT_NO,@PathVariable int AUDIT_SEQ_NO) {
		
		return auditDelegate.auditGetStamp(REVIEW_REPORT_NO,AUDIT_SEQ_NO);
	}
	
	@GetMapping(value = "/getFileStamp/{FILE_NAME}/{auditSeqNo}")
	public ResponseEntity<Map<String,Boolean>> getFileStamp(@PathVariable String FILE_NAME,@PathVariable Integer auditSeqNo) {
		
		return auditDelegate.getFileStamp(FILE_NAME,auditSeqNo);
	}
	
	@DeleteMapping(value = "/deleteStamp/{status}/{FILE_NAME}/{USER_ID}")
	public ResponseEntity<String> deleteStamp(@PathVariable String status,@PathVariable String FILE_NAME,@PathVariable int USER_ID) {
		
		return auditDelegate.deleteStamp(status, FILE_NAME, USER_ID);
		
	}
	
	@GetMapping(value = "/send_link/{userName}/{fileType}/{fileName}/{reviewReportNo}")
	public ResponseEntity<Map<String, String>> getDownloadLink(HttpServletRequest request,
			@PathVariable String userName, @PathVariable String fileType, @PathVariable String fileName, @PathVariable String reviewReportNo) {
		
		return auditDelegate.getDownloadLink(userName,fileType,fileName,reviewReportNo);
	}
	
	
	@PostMapping(value = "/sendMailReports")
	public ResponseEntity<String> sendMailReports(@RequestBody FileToUpload fileToUpload ) throws IOException {
		
		return auditDelegate.sendMailReports(fileToUpload);
	}
	
	
	@GetMapping(value = "/getDMLCLocationDate/{vesselImoNo}/{companyId}/{auditSeqNo}/{status}")
	public ResponseEntity<Map<String, Object>> getDMLCLocationDate(@PathVariable Integer vesselImoNo, @PathVariable Long companyId,@PathVariable Integer auditSeqNo, @PathVariable String status) {
		
		return auditDelegate.getDMLCLocationDate(vesselImoNo,companyId,auditSeqNo,status);
	}
	
	@GetMapping(value = "/getCompletionDate/{vesselImoNo}/{companyId}/{auditTypeId}/{status}/{auditSeqNo}")
	public ResponseEntity<Map<String, Object>> getCompletionDate(@PathVariable Integer vesselImoNo, @PathVariable Long companyId,@PathVariable Integer auditTypeId,@PathVariable String status,@PathVariable Integer auditSeqNo) {
		
		return auditDelegate.getCompletionDate(vesselImoNo,companyId,auditTypeId,status,auditSeqNo);
		
	}
	
	@GetMapping(value = "/getCertificateIHM/{VESSEL_IMO_NO}/{auditSeqNo}")
	public Map<String, Object> getCertificateIHM(@PathVariable Integer VESSEL_IMO_NO,@PathVariable Integer auditSeqNo) {
		
		return auditDelegate.getCertificateIHM(VESSEL_IMO_NO,auditSeqNo);
	}
	
	@GetMapping(value = "/getCompletedStatus/{vesselImoNo}/{auditTypeId}")
	public Map<String,Object>  getCompletedStatus(@PathVariable Integer vesselImoNo,@PathVariable Integer auditTypeId) {
		
		return auditDelegate.getCompletedStatus(vesselImoNo,auditTypeId);
		
	}
	
	@PostMapping(value = "/updateVesselDetails")
	public ResponseEntity<HttpStatus>  updateVesselDetails(@RequestBody List<VesselDetailsHistory> vesselDetailsHistory) {
		
		return auditDelegate.updateVesselDetails(vesselDetailsHistory);
	}
	
	@GetMapping(value = "/getVesselHistory/{VESSEL_IMO_NO}/{status}")
	public Map<String, Object> getVesselHistory(@PathVariable Integer VESSEL_IMO_NO,@PathVariable String status) {
		
		return auditDelegate.getVesselHistory(VESSEL_IMO_NO,status);
	}
	
	@GetMapping(value = "/getVesselRefresh/{VESSEL_IMO_NO}")
	public Map<String,Object> getVesselRefresh(@PathVariable Integer VESSEL_IMO_NO) {
		/*ArrayList<VesselRefresh> arVr = new ArrayList<>();
		arVr.add();*/
		return auditDelegate.getVesselRefresh(VESSEL_IMO_NO);
	}
	
	@PostMapping(value = "/updateVesselAuto")
	public Map<String, Object> updateVesselAuto(@RequestBody VesselRefresh vesselRefresh) {
		
		return auditDelegate.updateVesselAuto(vesselRefresh);
		
	}
}