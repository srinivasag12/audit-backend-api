package com.api.central.audit.delegate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

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

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.api.central.audit.controller.RMIServiceController;
import com.api.central.audit.dao.AuditDao;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.AuditFindingModel;
import com.api.central.audit.entity.AuditRptAttach;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.FileDownloadHistory;
import com.api.central.audit.entity.FileToUpload;
import com.api.central.audit.entity.FindingDetail;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.audit.entity.ReportData;
import com.api.central.audit.entity.StampDetails;
import com.api.central.audit.entity.VesselApprovalDetails;
import com.api.central.audit.entity.VesselDetailsHistory;
import com.api.central.audit.entity.VesselNotification;
import com.api.central.certificate.dao.CertificateDao;
import com.api.central.certificate.delegate.CertificateDelegate;
import com.api.central.fileDownload.FileDownloadRepository;
import com.api.central.master.entity.MaVessel;
import com.api.central.refreshVesselData.VesselRefresh;
import com.api.central.rmiaudit.entity.RmiAuditSigner;
import com.api.central.rmiaudit.entity.RmiAuditUTN;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;
import com.api.central.util.RestTemplateUtil;
import com.api.central.util.SequenceGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import sun.misc.BASE64Decoder;



@Service
@Transactional(readOnly = true)
public class AuditDelegateImpl implements AuditDelegate {

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private CertificateDao certificateDao;
	
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private CertificateDelegate certificateDelegate;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	private RMIServiceController rmi;
	
	@Autowired
	private FileDownloadRepository repo;
	
//	@Autowired
//	private RestTemplate restTemplate;
//	
	@Autowired
	private RestTemplateUtil restUtil;
	
	static Logger log = LoggerFactory.getLogger(AuditDelegateImpl.class);
	
	@Autowired
	AppUtil appUtil;
	@Override
	@Transactional(readOnly = false)
	public AuditDetail createOrUpdateISM(AuditDetail auditDetail, String status,Integer auditTypeId,Long companyId,String updateMeth) {
		log.info("opening metting date: "+auditDetail.getOpenMeetingDate());
		 
		if (!auditDetail.getAuditAuditorDetail().isEmpty()) {
			auditDetail.getAuditAuditorDetail().forEach(auditorDetails -> auditorDetails.setAuditDetail(auditDetail));
		}
		
		if (!auditDetail.getCertificateDetail().isEmpty()) {
			auditDetail.getCertificateDetail().forEach(certificateDetail -> certificateDetail.setAuditDetail(auditDetail));
		}
		if (!auditDetail.getSspReviewDetail().isEmpty()) {
			auditDetail.getSspReviewDetail().forEach(auditorDetails -> auditorDetails.setAuditDetail(auditDetail));
		}
		
		if (!auditDetail.getAuditRptAttach().isEmpty()) {
			auditDetail.getAuditRptAttach().forEach((auditReport) -> {
				auditReport.setAuditDetail(auditDetail);
				File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
						+ AppConstant.SEPARATOR + auditDetail.getAuditSeqNo());

				if (!directory.exists()) {
					directory.mkdirs();
				}
				if (directory.isDirectory()) {
					if (auditReport.getFileByte() != null && auditReport.getFileByte().length > 0) {

						Path path = Paths.get(directory + AppConstant.SEPARATOR + auditReport.getFileName());
						try {
							Files.write(path, auditReport.getFileByte());
						} catch (IOException e) {
							System.out.println("File Not Found :" + auditReport.getFileName());
							e.printStackTrace();
						}
					}
				}
			});
		}
		
 
		
		return auditDao.createOrUpdateISM(auditDetail, status,updateMeth);
	}

	
	
	
	@Override
	@Transactional(readOnly = false)
	public ReportData saveReportHistoryData(ReportData reportData,Integer auditTypeId,Long companyId) {
		
	/*	AuditDetail auditDetail = new AuditDetail(reportData.getAuditSeqNo(),reportData.getCompanyId(),reportData.getAuditTypeId());
		
		reportData.setAuditDetail(auditDetail);*/
		
		return auditDao.saveReportHistoryData(reportData);
	}
	
	
	@Override
	public AuditDetail getAuditDetail(int auditTypeId, int auditSeqNo, Long companyId) {
		return auditDao.getAuditDetail(auditTypeId, auditSeqNo, companyId);
	}
	
	@Override
	public AuditDetailIhm getAuditDetailForIhm(int auditTypeId, int auditSeqNo, Long companyId) {
		return auditDao.getAuditDetailForIhm(auditTypeId, auditSeqNo, companyId);
	}
	
	@Override
	public List<AuditDetailIhm> getAllIhmAuditDetail(int auditTypeId, int imoNo, Long companyId) {
		return auditDao.getAllIhmAuditDetail(auditTypeId, imoNo, companyId);
	}
	
	@Override
	public List<AuditDetail> getSspReviewDetail(int imo, int auditTypeId, Long companyId) {
		return auditDao.getSspReviewDetail(imo, auditTypeId, companyId);
	}

	//changed by @Ramya for Jira id - IRI-5633
	@Override
	@Transactional(readOnly = false)
	public AuditFindingModel auditFindings(AuditFindingModel auditFinding, String status, String findingStatus,Integer auditTypeId,Long companyId) {
		auditFinding.setAuditDetail(new AuditDetail(auditFinding.getAuditSeqNo(), auditFinding.getCompanyId(),
				auditFinding.getAuditTypeId()));
		if (auditFinding != null) {
			Integer auditSeqNo = AppConstant.NEW_FINDING.equals(findingStatus) ? auditFinding.getAuditSeqNo()
					: AppConstant.PREVIOUS_FINDING.equals(findingStatus) ? auditFinding.getAuditSeqNo() : 0;
			Integer findingSeqNo = auditFinding.getFindingSeqNo();
			AuditFindingModel finding = auditFinding;
			if (!auditFinding.getFindingDetailModel().isEmpty()) {
				auditFinding.getFindingDetailModel().forEach((findingDetail) -> {
					File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
							+ appUtil.setAuditType(auditTypeId) + AppConstant.SEPARATOR + auditSeqNo + AppConstant.SEPARATOR
							+ findingSeqNo + AppConstant.SEPARATOR + findingDetail.getStatusSeqNo());
					if (!directory.exists()) {
						directory.mkdirs();
					}
					System.out.println(directory.getAbsolutePath());
					if (!findingDetail.getFindingRptAttachs().isEmpty()) {
						findingDetail.getFindingRptAttachs().forEach((findingReportAttach) -> {
							
								if (findingReportAttach.getFindingFileByte() != null) {
									Path path = Paths
											.get(directory + AppConstant.SEPARATOR + findingReportAttach.getFileName());
									try {
										Files.write(path, findingReportAttach.getFindingFileByte());
									} catch (Exception e) {
										
										e.printStackTrace();
									}
								}
							
//							findingReportAttach.setFindingDetail(findingDetail);
						});
					}
				});
			}
		}
		auditFinding = auditDao.auditFindings(auditFinding, status);
		return auditFinding;
	}
	
	//added by @Ramya for Jira id - IRI-5633 - START
	@Override
	@Transactional(readOnly = false)
	public AuditFinding auditFindingsSave(AuditFinding auditFinding, String status, String findingStatus,Integer auditTypeId,Long companyId) {
		auditFinding.setAuditDetail(new AuditDetail(auditFinding.getAuditSeqNo(), auditFinding.getCompanyId(),
				auditFinding.getAuditTypeId()));
		if (auditFinding != null) {
			Integer auditSeqNo = AppConstant.NEW_FINDING.equals(findingStatus) ? auditFinding.getAuditSeqNo()
					: AppConstant.PREVIOUS_FINDING.equals(findingStatus) ? auditFinding.getAuditSeqNo() : 0;
			Integer findingSeqNo = auditFinding.getFindingSeqNo();
			AuditFinding finding = auditFinding;
			if (!auditFinding.getFindingDetail().isEmpty()) {
				auditFinding.getFindingDetail().forEach((findingDetail) -> {
					File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
							+ appUtil.setAuditType(auditTypeId) + AppConstant.SEPARATOR + auditSeqNo + AppConstant.SEPARATOR
							+ findingSeqNo + AppConstant.SEPARATOR + findingDetail.getStatusSeqNo());
					if (!directory.exists()) {
						directory.mkdirs();
					}
					System.out.println(directory.getAbsolutePath());
					findingDetail.setAuditFinding(finding);
					if (!findingDetail.getFindingRptAttachs().isEmpty()) {
						findingDetail.getFindingRptAttachs().forEach((findingReportAttach) -> {
							
								if (findingReportAttach.getFindingFileByte() != null) {
									Path path = Paths
											.get(directory + AppConstant.SEPARATOR + findingReportAttach.getFileName());
									try {
										Files.write(path, findingReportAttach.getFindingFileByte());
									} catch (Exception e) {
										
										e.printStackTrace();
									}
								}
							
							findingReportAttach.setFindingDetail(findingDetail);
						});
					}
				});
			}
		}
		auditFinding = auditDao.auditFindingsSave(auditFinding, status);
		return auditFinding;
	}
	//added by @Ramya for Jira id - IRI-5633 - END

	@Override
	public List<AuditFinding> getPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq, String companyImoNo, String companyDoc) {
		List<AuditFinding> auditFindings = new ArrayList<AuditFinding>();
		auditFindings = auditDao.getPreviousFinding(auditTypeId, auditDate, vesselIMONo, companyId, currentAuditSeq, companyImoNo, companyDoc);
		auditFindings.forEach(af -> {
			af.getFindingDetail().forEach(fd ->{
				if(fd.getCategoryId().equals(AppConstant.FINDING_OBS)){
					fd = new FindingDetail();
				}
 			});
		});	
		
		return auditFindings;
	}

	@Override
	public Map<String, Object> getPreviousAuditDetail(int auditTypeId, int vesselIMONo, Long companyId) {
		//Map<String, Object> auditDetails = new ArrayList<AuditDetailView>();

		///auditDetails = auditDao.getPreviousAuditDetail(auditTypeId, vesselIMONo, companyId);

		//return auditDetails;
		return auditDao.getPreviousAuditDetail(auditTypeId, vesselIMONo, companyId);
	}

	public RmiAuditUTN getUTN() {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		
		RmiAuditUTN ras = restTemplate.exchange(AppConstant.RMI_URL+"/ws1/getUTN", HttpMethod.GET, request,RmiAuditUTN.class).getBody();
		
		return ras;
	}

	@Override
	public boolean getLaptopPreviousFinding(int auditTypeId, int auditSeqNo, Long companyId) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
			if (auditSeqNo > 0) {
				String currentDrc = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
						+ AppConstant.SEPARATOR + auditSeqNo;
				AuditDetail auditDetail = new AuditDetail();
				
				Integer auditSubTypeId = null;
				
				auditDetail = auditDao.getAuditDetail(auditTypeId, auditSeqNo, companyId);
				
				auditSubTypeId = auditDetail.getAuditSubTypeId();
				
				if(auditDetail.getCertificateDetail().isEmpty() &&  (auditSubTypeId == 1001 || auditSubTypeId == 1002 || auditSubTypeId == 1004)){
					
					auditDetail.setUtn(getUTN().getUtnString());
					auditDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
				
				}else if(auditDetail.getCertificateDetail().isEmpty() &&  (auditSubTypeId == 1003 || auditSubTypeId == 1005)){
					
					Map<String,Object> map = certificateDelegate.getUtnAndCertificateId(auditDetail.getAuditReportNo(), auditDetail.getCertificateNo(), companyId);
					
					if(map.get("utn") != null && map.get("utn") != ""  && map.get("certificateId") != null && map.get("certificateId") != ""){
						auditDetail.setUtn((String) map.get("utn"));
						auditDetail.setCertificateId((Long) map.get("certificateId"));
					}else{
						auditDetail.setUtn(getUTN().getUtnString());
						auditDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					
					}
				}else if(!auditDetail.getCertificateDetail().isEmpty() && auditDetail.getCertificateDetail().size()>0){
					
					auditDetail.setCertificateId(auditDetail.getCertificateDetail().get(0).getCertificateId());
					auditDetail.setUtn(auditDetail.getCertificateDetail().get(0).getUtn());
					
				}
				try {
					String addCnoUtn=auditDetail.getCertificateNo()+" "+auditDetail.getUtn();
					auditDetail.setQid(auditDao.getDbmsCryptoEncode(addCnoUtn));
					//auditDetail.setQid(e.EncodeCertiNoUtn(auditDetail.getCertificateNo(), auditDetail.getUtn()));
				}  catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<CertificateDetailWithOutAudit> certificateVthoutAuditList = new ArrayList<CertificateDetailWithOutAudit>();
				CertificateDetailWithOutAudit certificateVthoutAudit = null;
					
				if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.RENEWAL_SUB_TYPE_ID){
					
					certificateVthoutAudit = new CertificateDetailWithOutAudit();
					certificateVthoutAudit.setAuditSeqNo(auditSeqNo);
					certificateVthoutAudit.setVesselImoNo(auditDetail.getVesselImoNo());
					certificateVthoutAudit.setAuditTypeId(auditTypeId);
					certificateVthoutAudit.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					certificateVthoutAudit.setCertificateNo((auditDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT ? auditDetail.getCertificateNo() : (AppUtil.getCertificateNoFormat(auditTypeId,sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),auditSubTypeId))));
					certificateVthoutAudit.setUtn(getUTN().getUtnString());
					certificateVthoutAudit.setCompanyId(auditDetail.getCompanyId());
					try {
						String addCnoUtn=certificateVthoutAudit.getCertificateNo()+" "+certificateVthoutAudit.getUtn();

						certificateVthoutAudit.setQid(auditDao.getDbmsCryptoEncode(addCnoUtn));

						//certificateVthoutAudit.setQid(e.EncodeCertiNoUtn(certificateVthoutAudit.getCertificateNo(), certificateVthoutAudit.getUtn()));
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					certificateVthoutAuditList.add(certificateVthoutAudit);
					
				}
				
				if(auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID){
										
					certificateVthoutAudit = new CertificateDetailWithOutAudit();
					certificateVthoutAudit.setAuditSeqNo((auditTypeId == AppConstant.ISM_TYPE_ID ? AppConstant.ISM_NO_AUD_CERT_AUDITSEQ : (auditTypeId == AppConstant.ISPS_TYPE_Id ? AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ : AppConstant.MLC_NO_AUD_CERT_AUDITSEQ)));
					certificateVthoutAudit.setVesselImoNo(auditDetail.getVesselImoNo());
					certificateVthoutAudit.setAuditTypeId(auditTypeId);
					certificateVthoutAudit.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					certificateVthoutAudit.setCertificateNo(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString());
					certificateVthoutAudit.setUtn(getUTN().getUtnString());
					certificateVthoutAudit.setCompanyId(auditDetail.getCompanyId());
					try {
						String addCnoUtn=certificateVthoutAudit.getCertificateNo()+" "+certificateVthoutAudit.getUtn();

						certificateVthoutAudit.setQid(auditDao.getDbmsCryptoEncode(addCnoUtn));

						//certificateVthoutAudit.setQid(e.EncodeCertiNoUtn(certificateVthoutAudit.getCertificateNo(), certificateVthoutAudit.getUtn()));
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					certificateVthoutAuditList.add(certificateVthoutAudit);	
				}
				
				auditDetail.setCertificateWithoutAudit(certificateVthoutAuditList);
				
				if(auditDetail.getOfficialId()!= null && auditDetail.getOfficialId() > 0){
					
					ResponseEntity<RmiAuditSigner> rmiData = rmi.auditorSignAndSeal(auditDetail.getOfficialId(),auditDetail.getCompanyId());
					
					log.info("rmiData ="+rmiData);
					
					log.info("Signature ="+rmiData.getBody().getSignature());
					
					auditDetail.setSeal(rmiData.getBody().getSeal());
					auditDetail.setSigner(rmiData.getBody().getSigner());
					auditDetail.setTitle(rmiData.getBody().getTitle());
					auditDetail.setSignature(rmiData.getBody().getSignature());
					
				}
				if (auditDetail != null) {
					File file = new File(currentDrc);
					file.mkdirs();
					mapper.writeValue(new File(currentDrc + AppConstant.SEPARATOR + auditSeqNo + AppConstant.JSON),
							auditDetail);

					List<AuditDetail> detailViews = new ArrayList<AuditDetail>();
					detailViews = auditDao.getLaptopPreviousAudit(auditTypeId, auditDetail.getAuditDate(),
							auditDetail.getVesselImoNo(), companyId, auditSeqNo);
					System.out.println("Audit Detail View :" + detailViews.size());
					log.info("detailViews123"+detailViews);
					if (!detailViews.isEmpty()) {
						List<AuditFinding> auditFindings = new ArrayList<AuditFinding>();

						auditFindings = auditDao.getLaptopPreviousFinding(auditTypeId, auditDetail.getAuditDate(),
								auditDetail.getVesselImoNo(), companyId, auditSeqNo);
						System.out.println(" auditFindings View :" + auditFindings.size());
						currentDrc = currentDrc + AppConstant.SEPARATOR + "pf";
						File currentFile = new File(currentDrc);
						currentFile.mkdirs();
						if (!auditFindings.isEmpty()) {
							System.out.println("auditFindings not zero");
							Set<Integer> auditSeqNos = auditFindings.stream().map(AuditFinding::getAuditSeqNo)
									.collect(Collectors.toSet());
							System.out.println(" detailViews Size:" + detailViews.size());
							/*detailViews.removeIf(audit -> {
								return !auditSeqNos.stream().anyMatch(seqNo -> seqNo.equals(audit.getAuditSeqNo()));

							});*/
							System.out.println(" detailViews Size:" + detailViews.size());

							mapper = new ObjectMapper();
							mapper.writeValue(new File(
									currentFile + AppConstant.SEPARATOR + "pf" + AppConstant.JSON),
									auditFindings);
						}
 
 						for (AuditDetail detailView : detailViews) {
							System.out.println("detailView :"+detailView.getAuditSeqNo());
							String findingPath = currentDrc + AppConstant.SEPARATOR + detailView.getAuditSeqNo();
 							/*
							 * Move the source directory to the destination
							 * directory. The destination directory must not
							 * exists prior to the move process.
							 */
 							detailView.getAuditFinding().forEach(auditFinding ->{
								System.out.println("detailView.getAuditFinding() ;"+auditFinding.getFindingSeqNo());
								//if(auditFinding.getFindingStatus().equals(AppConstant.FINDING_STATUS)){
 								String	srcDirPath = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
										+ appUtil.setAuditType(auditTypeId) + AppConstant.SEPARATOR + detailView.getAuditSeqNo()+ AppConstant.SEPARATOR +auditFinding.getFindingSeqNo();
 									try {
 										FileUtils.copyDirectory(new File(srcDirPath), new File(findingPath+AppConstant.SEPARATOR +auditFinding.getFindingSeqNo()), true);
									} catch (Exception e) {
										System.out.println("Exception in Transfer File :"+e);
 										e.printStackTrace();
									}
 								//}
							});
						}
						detailViews.forEach(audit -> {
							audit.setAuditFinding(new ArrayList<AuditFinding>());
							/*audit.setAuditAuditorDetail(new ArrayList<AuditAuditorDetail>());*/
							audit.setAuditRptAttach(new ArrayList<AuditRptAttach>());
						});

						mapper = new ObjectMapper();
						mapper.writeValue(new File(
								currentFile + AppConstant.SEPARATOR + AppConstant.AUDIT_DETAIL + AppConstant.JSON),detailViews);
					}
				}
			}
		
		return true;
	}

	
	@Override
	@Transactional(readOnly = false)
	public String deleteFinding(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo) throws IOException {

		String status = auditDao.deleteFinding(auditTypeId, auditSeqNo, companyId, findingSeqNo);

		Path rootPath = Paths.get(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + auditSeqNo + AppConstant.SEPARATOR + findingSeqNo);
		
		if (rootPath.isAbsolute()) {
			Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
					.peek(System.out::println).forEach(File::delete);
		}
		return status;
	}
	
	@Override
	@Transactional(readOnly = false)
	public String deleteFindingDmlcLinked(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo) throws IOException {

		String status = auditDao.deleteFindingDmlcLinked(auditTypeId, auditSeqNo, companyId, findingSeqNo);

		Path rootPath = Paths.get(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
				+ AppConstant.SEPARATOR + auditSeqNo + AppConstant.SEPARATOR + findingSeqNo);
		
		if (rootPath.isAbsolute()) {
			Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
					.peek(System.out::println).forEach(File::delete);
		}
		return status;
	}

	@Override
	public List<AuditFinding> currentFinding(int auditTypeId, long companyId, int currentAuditSeq) {

		List<AuditFinding> auditFindings = auditDao.currentFinding(auditTypeId, companyId, currentAuditSeq);

		return auditFindings;
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean syncTimeUpdateRmiData(String path, String auditSeqNo ,Integer auditTypeId,Long companyId) throws IOException {
		ObjectMapper mapper;
		mapper = new ObjectMapper();
		AuditDetail auditDetail = new AuditDetail();
		auditDetail = mapper.readValue(new File(path + AppConstant.SEPARATOR + auditSeqNo + AppConstant.JSON),
				AuditDetail.class);
		
		if (auditDetail.getCertificateDetail().size()>0 && auditDetail.getCertificateDetail().get(0).getPublishStatus() == 1){
			Map<String,Object> map = certificateDelegate.syncTimeUpdateRmiData(auditDetail.getCertificateDetail().get(0));
			if((Integer)map.get("status")==200){
				return true;
			}
		}
		
		return true;
	}
	//added by @Ramya for Ticket-659
	@Override
	@Transactional(readOnly = false)
	public boolean synchronizeLinkedDMLC(String path, String auditSeqNo ,Integer auditTypeId,Long companyId) throws IOException {
		ObjectMapper mapper;
		 
			mapper = new ObjectMapper();
			AuditDetail auditDetail = new AuditDetail();
			
			auditDetail = mapper.readValue(new File(path + AppConstant.SEPARATOR + auditSeqNo + AppConstant.JSON),
					AuditDetail.class);
	List<AuditFinding> findings = new ArrayList<AuditFinding>();
	findings.addAll(auditDetail.getAuditFinding());
	auditDetail.setAuditFinding(new ArrayList<AuditFinding>());
	
	auditDetail.setSync(true);
	
	if(auditDetail.getAuditStatusId()!=null && auditDetail.getAuditStatusId()==AppConstant.COMPLETED_AUDIT_STATUS){
		//auditDetail.setAuditComplteLaptop(1);
		auditDetail.setAuditComplteLaptop(0);
	}else { auditDetail.setAuditComplteLaptop(0); }

	deleteFindingsSyncTime(auditDetail,findings);
	
	if (!findings.isEmpty()) {
		for (AuditFinding auditFinding : findings) {
			deleteFinding(auditFinding.getAuditTypeId(), auditFinding.getAuditSeqNo(), auditFinding.getCompanyId(), auditFinding.getFindingSeqNo());
		}
		auditDao.updateStatusId(auditTypeId, auditDetail.getAuditSeqNo(), companyId,auditDetail.getAuditStatusId());
	}
	
	if (!findings.isEmpty()) {
		for (AuditFinding auditFinding : findings) {
			System.out.println("Finding data"+auditFinding.getAuditSeqNo());
			auditFindingsSave(auditFinding, AppConstant.CREATE, AppConstant.NEW_FINDING,auditTypeId,companyId);
		}
	}
	String previousPath = path + AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING;
	List<AuditFinding> auditFindings = new ArrayList<AuditFinding>();
	System.out.println("checking Previous Finding path Exisit or not :" + previousPath);
	if (new File(previousPath+ AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING + AppConstant.JSON).exists()) {
		System.out.println("Previous Finding Exisit :" + previousPath);
		auditFindings = mapper.readValue(
				new File(
						previousPath + AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING + AppConstant.JSON),
				mapper.getTypeFactory().constructCollectionType(List.class, AuditFinding.class));
		if (!auditFindings.isEmpty()) {
			Set<Integer> auditSeqNos = auditFindings.stream().map(AuditFinding::getAuditSeqNo)
					.collect(Collectors.toSet());

			for (Integer auditSeq : auditSeqNos) {
				File destSrc = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
						+ appUtil.setAuditType(auditTypeId) + AppConstant.SEPARATOR + auditSeq);

			File sourceFile = new File(previousPath + AppConstant.SEPARATOR + auditSeq);
			if(sourceFile.exists() && !destSrc.exists()){
				FileUtils.moveDirectory(sourceFile, destSrc);
			}else if(sourceFile.exists() && destSrc.exists()){
				FileUtils.copyDirectory(sourceFile, destSrc);
			}	
			}

			for (AuditFinding auditFinding : auditFindings) {
				auditFindingsSave(auditFinding, AppConstant.UPDATE, AppConstant.PREVIOUS_FINDING,auditTypeId,companyId);
			}
		}
	}
	return true;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean synchronizeAudit(String path, String auditSeqNo ,Integer auditTypeId,Long companyId) throws IOException {
		ObjectMapper mapper;
		 
			mapper = new ObjectMapper();
			AuditDetail auditDetail = new AuditDetail();
		//	AuditDetail auditDetails;
			
			auditDetail = mapper.readValue(new File(path + AppConstant.SEPARATOR + auditSeqNo + AppConstant.JSON),
					AuditDetail.class);
			
			Integer auditSequenceNo = auditDetail.getAuditSeqNo();
 			
			if(auditDetail.getCertificateWithoutAudit()!=null)
			{

				if(auditDetail.getCertificateWithoutAudit().size()>0)
				{
					for(CertificateDetailWithOutAudit certificateDetailWithOutAudit1 : auditDetail.getCertificateWithoutAudit()) {
						certificateDetailWithOutAudit1.setSeqNo(0);
						certificateDao.generateCertificateWithOutAudit(certificateDetailWithOutAudit1);
					}
				}
			}
			//System.out.println("Finding Size" + auditDetail.getAuditFinding().size());

			if(auditDetail.getCertificateDetail()!=null && auditDetail.getCertificateDetail().size()>0){

				for(CertificateDetail certificateDetail  : auditDetail.getCertificateDetail()) {
					
					Long l=sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ORDER_NO);
					certificateDetail.setCertOderNo(l.intValue());
					
					if(certificateDetail.getCertIssueId() == AppConstant.INTERMEDAITE_ENDORSED || certificateDetail.getCertIssueId() == AppConstant.ADDITIONAL_ENDORSED || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED1 || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED2){						
						certificateDetail.setEndorsementID(certificateDetail.getEndorsementID() != null ? certificateDetail.getEndorsementID() : sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					}
				}

			}
			
			List<AuditFinding> findings = new ArrayList<AuditFinding>();
			findings.addAll(auditDetail.getAuditFinding());
			auditDetail.setAuditFinding(new ArrayList<AuditFinding>());
			
			auditDetail.setSync(true);
			
		//	auditDetails = auditDetail;
			//due to vessel refresh functionality, take out setAuditComplteLaptop(1)
			if(auditDetail.getAuditStatusId()!=null && auditDetail.getAuditStatusId()==AppConstant.COMPLETED_AUDIT_STATUS){
				//auditDetail.setAuditComplteLaptop(1);
				auditDetail.setAuditComplteLaptop(0);
			}else { auditDetail.setAuditComplteLaptop(0); }
			
			createOrUpdateISM(auditDetail, AppConstant.UPDATE,auditTypeId,companyId,"Sync");

			//delete findings which all are deleted laptop side
			deleteFindingsSyncTime(auditDetail,findings);
			
			if (!findings.isEmpty()) {
				for (AuditFinding auditFinding : findings) {
					deleteFinding(auditFinding.getAuditTypeId(), auditFinding.getAuditSeqNo(), auditFinding.getCompanyId(), auditFinding.getFindingSeqNo());
				}
			}
			
			if (!findings.isEmpty()) {
				for (AuditFinding auditFinding : findings) {
					System.out.println("Finding data"+auditFinding.getAuditSeqNo());
					auditFindingsSave(auditFinding, AppConstant.CREATE, AppConstant.NEW_FINDING,auditTypeId,companyId);
				}
			}
			
			
		//	log.info("auditDetailDelegate1 ="+auditDetails);
		//	auditDetails.setAuditFinding(findings);

			

			//createBlob(auditDetails,1001);
		//	createBlobDataforOrg(auditDetails.getAuditSeqNo(),auditDetails.getAuditTypeId(),auditDetails.getCompanyId());
		//	auditDetails.setAuditFinding(new ArrayList<AuditFinding>());
			
		//	auditDetails = getAuditDetail(auditDetail.getAuditTypeId(),auditDetail.getAuditSeqNo(),auditDetail.getCompanyId());
			
		//	log.info("auditDetailDelegateFinal ="+auditDetails);

			String previousPath = path + AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING;
			List<AuditFinding> auditFindings = new ArrayList<AuditFinding>();
			System.out.println("checking Previous Finding path Exisit or not :" + previousPath);
			if (new File(previousPath+ AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING + AppConstant.JSON).exists()) {
				System.out.println("Previous Finding Exisit :" + previousPath);
				auditFindings = mapper.readValue(
						new File(
								previousPath + AppConstant.SEPARATOR + AppConstant.PREVIOUS_FINDING + AppConstant.JSON),
						mapper.getTypeFactory().constructCollectionType(List.class, AuditFinding.class));
				if (!auditFindings.isEmpty()) {
					Set<Integer> auditSeqNos = auditFindings.stream().map(AuditFinding::getAuditSeqNo)
							.collect(Collectors.toSet());

					for (Integer auditSeq : auditSeqNos) {
						File destSrc = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
								+ appUtil.setAuditType(auditTypeId) + AppConstant.SEPARATOR + auditSeq);

					/*	try {
							if (destSrc.exists()) {
 								Files.walk(
										Paths.get(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
												+ appUtil.setAuditType(auditTypeId) + AppConstant.SEPARATOR + auditSeq),
										FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
										.map(Path::toFile).peek(System.out::println).forEach(File::delete);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}*/

						// FileUtils.cleanDirectory(directory);
					File sourceFile = new File(previousPath + AppConstant.SEPARATOR + auditSeq);
					if(sourceFile.exists() && !destSrc.exists()){
						FileUtils.moveDirectory(sourceFile, destSrc);
					}else if(sourceFile.exists() && destSrc.exists()){
						FileUtils.copyDirectory(sourceFile, destSrc);
					}	
					}

					for (AuditFinding auditFinding : auditFindings) {
						auditFindingsSave(auditFinding, AppConstant.UPDATE, AppConstant.PREVIOUS_FINDING,auditTypeId,companyId);
					}
				}
			}
			
		//	log.info("auditDetailFindingsDelegate ="+auditFindings.get(0));
			
		//	createBlobData(auditDetail.getAuditSeqNo(),auditDetail.getCompanyId());
			
			if(auditDetail.getLockStatus()!=null && auditDetail.getLockStatus()!=3){
				auditDao.updateLockStatus(auditTypeId, auditSequenceNo, companyId,0);
			}
			
			auditDao.updateLockHolderSync(auditTypeId,auditSequenceNo,companyId,"");
			
			/*// all ready return in contriller
			if (auditDetail.getCertificateDetail().size()>0 && auditDetail.getCertificateDetail().get(0).getPublishStatus() == 1){
				Map<String,Object> map = certificateDelegate.syncTimeUpdateRmiData(auditDetail.getCertificateDetail().get(0));
				if((Integer)map.get("status")==200){
					return true;
				}
			}*/
			
			return true;
		 
	}

	private void deleteFindingsSyncTime(AuditDetail auditDetail, List<AuditFinding> findings) {
		
		auditDao.deleteFindingsSyncTime(auditDetail,findings);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateLockStatus(int auditTypeId, int auditSeqNo, Long companyId, Integer leadStatus) {
		auditDao.updateLockStatus(auditTypeId, auditSeqNo, companyId, leadStatus);
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, String> updateLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId, String lockHolder) {
		return auditDao.updateLockHolder(auditTypeId, auditSeqNo, companyId,lockHolder);
		
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, String> checkAuditLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId,
			String userId) {
		
		return  auditDao.checkAuditLockHolder(auditTypeId, auditSeqNo, companyId,userId);
	}

	@Override
	public Map<String, Object> checkAuditLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId) {

		return  auditDao.checkAuditLockStatus(auditTypeId, auditSeqNo, companyId);
	}
	
	/*@Override
	public Map<String, Object> checkAuditLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public FindingRptAttach unlinkFindingFiles(FindingRptAttach findingRptAttach, String auditTypeDesc, Long companyId) {
		findingRptAttach.setFindingDetail(new FindingDetail(findingRptAttach.getStatusSeqNo(),new AuditFinding(findingRptAttach.getFindingSeqNo(), findingRptAttach.getOrigAuditSeqNo(), new AuditDetail(findingRptAttach.getOrigAuditSeqNo(), findingRptAttach.getCompanyId(), findingRptAttach.getAuditTypeId()))));
		Boolean status = auditDao.unlinkFindingFiles(findingRptAttach);
		
		if(status){
		File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
				+ auditTypeDesc + AppConstant.SEPARATOR + findingRptAttach.getOrigAuditSeqNo() + AppConstant.SEPARATOR
				+ findingRptAttach.getFindingSeqNo() + AppConstant.SEPARATOR + findingRptAttach.getStatusSeqNo() + AppConstant.SEPARATOR+ findingRptAttach.getFileName());
		if (directory.exists()) {
			directory.delete();
		}
		}
		return findingRptAttach;
	}

	@Override
	public Map<String, Object> checkLeadStatus(Integer auditSeqNo, Integer auditTypeId,Long companyId) {
		return auditDao.checkLeadStatus( auditSeqNo,  auditTypeId,  companyId);
	}

	@Override
	public Long updateReviewerStatus(Integer audSeqNo, Integer audType, Long companyId, Integer revStatus) {
		
		return auditDao.updateReviewerStatus(audSeqNo,audType,companyId,revStatus);
	}
	
	@Override
	public Map<String, Object> getSSPDetails(Integer vesselImoNo,Integer auditTypeId, Long companyId, Integer auditSubTypeId, Integer auditSeqNo) {
		
		return  auditDao.getSSPDetails(vesselImoNo,auditTypeId, companyId, auditSubTypeId,auditSeqNo);
	}

	@Override
	public Map<String, Object> getSSPRevisionNo(Integer vesselImoNo, Integer auditTypeId, Long companyId,
			Integer auditSeqNo) {

		return  auditDao.getSSPRevisionNo(vesselImoNo,auditTypeId, companyId, auditSeqNo);
	}

	@Override
	public Map<String, Object> getSspIspsData(Integer vesselImoNo, Integer auditTypeId, Long companyId,
			Integer auditSubTypeId) {
		
		return  auditDao.getSspIspsData(vesselImoNo,auditTypeId, companyId, auditSubTypeId);
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> getCountOfLockedAuditByCurrUser(String emailId, Long companyId) {

		return  auditDao.getCountOfLockedAuditByCurrUser(emailId, companyId);
	}

	@Override
	public Map<String, Object> checkLinkedWithIspsOrMLC(Integer auditTypeId, Integer auditSeqNo, Long companyId) {

		return  auditDao.checkLinkedWithIspsOrMLC(auditTypeId, auditSeqNo, companyId);
	}

	@Override
	public Long vesselMissingMailCall(Integer auditTypeId, Long companyId, Integer vesselImoNo, String vesselName) {
	
		return  auditDao.vesselMissingMailCall(auditTypeId, companyId, vesselImoNo,vesselName);
	}

	@Override
	public Map<String, Object> getPrevDocDetails(String compImoNo, String compDocNo, Long companyId) {
		return  auditDao.getPrevDocDetails(compImoNo, compDocNo, companyId);
		}

	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> updateDocFlag(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer docFlag) {
		
		return auditDao.updateDocFlag(auditTypeId, auditSeqNo, companyId, docFlag);
	}

	@Override
	public void callSendMailCreateProc(AuditDetail audData) {
		
		auditDao.callSendMailCreateProc(audData);
		
	}

	@Override
	public List<MaVessel> tcApprovalStatus(String id,Long companyId, Integer vesselImoNo) {
		
		return  auditDao.tcApprovalStatus(id, companyId, vesselImoNo);
	}

	@Override
	@Transactional(readOnly = false)
	public VesselApprovalDetails savetcDetails(VesselApprovalDetails vesselApprovalDetails) {
		
		return auditDao.SavetcApprovalStatus(vesselApprovalDetails);
	}

	@Override
	public Map<String, Object> getSignature(String emailId, Long companyId) {
		
		return auditDao.getSignature(emailId,companyId);
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> vesselDtlIncomplete(Integer vesselImoNo, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog) {
		
		return auditDao.vesselDtlIncomplete(vesselImoNo,vesselId,userId,companyId, partialVesselLog);
	}

	@Override
	public Map<String, Object> vesselNotExist(Integer vesselImoNo, Long companyId, String userId) {
		
		return auditDao.vesselNotExist(vesselImoNo,companyId,userId);
	}

	@Override
	public Map<String, Object> getAuditDetailAndCheckSameAudit(Integer auditTypeId, Integer auditSeqNo, Long companyId,
			Integer vesselImoNo) {
		
		Map<String,Object> map= new HashMap<String,Object>();
		
		map.put("auditDetail", auditDao.getAuditDetail(auditTypeId, auditSeqNo, companyId));
		map.put("sameAuditCount", auditDao.checkSameAudit(auditTypeId, vesselImoNo, companyId));
		
		return map;
	}
	
	@Override
	public void allAuditorSign(Integer  auditSeqNo,Integer companyId )
	{
		auditDao.allAuditorSign(auditSeqNo,companyId);
	}
	
	@Override
	public void carUpdateRemoveAuditorsSign(Integer  auditSeqNo, Long companyId, Integer auditTypeId )
	{
		auditDao.carUpdateRemoveAuditorsSign(auditSeqNo,companyId,auditTypeId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> updateLtrStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer ltrStatus) {
		
		return auditDao.updateLtrStatus(auditTypeId, auditSeqNo, companyId, ltrStatus);
	}

	
	@Override
	public List<Map<String ,Object>> getReportData(Integer auditTypeId, Integer auditSeqNo, Long companyId) {
		return auditDao.getReportData(auditTypeId, auditSeqNo, companyId);
	}

	@Override
	public ReportData getReportBlobData(Integer versionId, Integer auditSeqNo, Long companyId) {
		return auditDao.getReportBlobData(versionId, auditSeqNo, companyId);
	}

	@Override
	public void callDeleteUpdatedStatusOnAudVoidProc(AuditDetail audData) {
		
		auditDao.callDeleteUpdatedStatusOnAudVoidProc(audData);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean createBlobData(Integer currAuditseq, Long companyId) {
		return auditDao.blobData(currAuditseq, companyId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean createBlob(AuditDetail auditDetail, Integer flag) {
		
		return auditDao.createBlob(auditDetail, flag);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean createBlobDataforOrg(Integer orgAuditseq,Integer auditTypeId, Long companyId) {
		return auditDao.blobDataforOrg(orgAuditseq, auditTypeId,companyId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean signatureGenBlobData(Integer orgAuditseq,Integer auditTypeId, Long companyId, Integer versionId) {
		return auditDao.signatureGenBlobData(orgAuditseq, auditTypeId,companyId, versionId);
	}




	@Override
	public VesselNotification getRmiProced(VesselNotification flag,Long companyId, Integer vesselImo) {
		
		return auditDao.getRmiProced(flag,companyId,vesselImo);
	}




	@Override
	public Map<String, Object> getAuditDetailAndNextAdtCrtStatus(Integer auditTypeId, Long companyId,
			Integer auditSeqNo, Boolean status, Integer vesselImoNo) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		Boolean nxtAdtCrtSts = false;
		
		AuditDetail auditDetail= auditDao.getAuditDetail(auditTypeId, auditSeqNo, companyId);
		System.out.println("******"+auditDetail);
		
		if(status){
			
			nxtAdtCrtSts = auditDao.getNextAdtCrtStatus(auditTypeId, companyId, auditSeqNo, vesselImoNo);
			
		}
		
		map.put("auditDetail", auditDetail);
		map.put("nxtAdtCrtSts", nxtAdtCrtSts);
		
		return map;
	}




	@Override
	public Map<String, Object> getISPSInitialDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId) {
		
		return auditDao.getISPSInitialDetails(vesselImoNo,auditTypeId,companyId);
	}




	@Override
	public List<Map<String, Object>> getDMLCReportNos(Integer vesselImoNo, Long companyId, Integer auditTypeId) {
		
		return auditDao.getDMLCReportNos(vesselImoNo,companyId,auditTypeId);
	}




	@Override
	public List<AuditFinding> getPreviousFinding(Integer auditTypeId, Integer vesselIMONo, Long companyId,
			String companyImoNo, String companyDoc) {
		
		return auditDao.getPreviousFinding(auditTypeId,vesselIMONo,companyId,companyImoNo,companyDoc);
	}




	@Override
	public Map<String, Object> getAuditRelatedData(Integer auditSeqNo, Long companyId) {
		
		return auditDao.getAuditRelatedData(auditSeqNo, companyId);
	}
	
	
	@Override
	public Boolean dmlcFinCloserEmail(String findingSeqNos, Integer dmlcseqNo, Integer mlcseqNo, Long companyId) {
		return auditDao.dmlcFinCloserEmail(findingSeqNos, dmlcseqNo, mlcseqNo, companyId);
	}




	
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> vesselDtlIncompleteYatch(String vesselName, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog) {
		
		return auditDao.vesselDtlIncompleteYatch(vesselName,vesselId,userId,companyId, partialVesselLog);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean removeIhmFinalReport(Integer orgAuditseq,Integer auditTypeId, Long companyId, Integer versionId) {
		log.info("orgAuditseq ="+orgAuditseq);
		return auditDao.removeIhmFinalReport(orgAuditseq, auditTypeId,companyId, versionId);
	}
	
	

	@Override
	public boolean getMobilePreviousFinding(int auditTypeId, int auditSeqNo, Long companyId) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
			if (auditSeqNo > 0) {
				/*String currentDrc = servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + appUtil.setAuditType(auditTypeId)
						+ AppConstant.SEPARATOR + auditSeqNo; */
				String currentDrc = auditSeqNo + AppConstant.SEPARATOR + auditSeqNo;;
				
				AuditDetail auditDetail = new AuditDetail();
				
				Integer auditSubTypeId = null;
				
				auditDetail = auditDao.getAuditDetail(auditTypeId, auditSeqNo, companyId);
				
				auditSubTypeId = auditDetail.getAuditSubTypeId();
				
				if(auditDetail.getCertificateDetail().isEmpty() &&  (auditSubTypeId == 1001 || auditSubTypeId == 1002 || auditSubTypeId == 1004)){
					
					auditDetail.setUtn(getUTN().getUtnString());
					auditDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
				
				}else if(auditDetail.getCertificateDetail().isEmpty() &&  (auditSubTypeId == 1003 || auditSubTypeId == 1005)){
					
					Map<String,Object> map = certificateDelegate.getUtnAndCertificateId(auditDetail.getAuditReportNo(), auditDetail.getCertificateNo(), companyId);
					
					if(map.get("utn") != null && map.get("utn") != ""  && map.get("certificateId") != null && map.get("certificateId") != ""){
						auditDetail.setUtn((String) map.get("utn"));
						auditDetail.setCertificateId((Long) map.get("certificateId"));
					}else{
						auditDetail.setUtn(getUTN().getUtnString());
						auditDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					
					}
				}else if(!auditDetail.getCertificateDetail().isEmpty() && auditDetail.getCertificateDetail().size()>0){
					
					auditDetail.setCertificateId(auditDetail.getCertificateDetail().get(0).getCertificateId());
					auditDetail.setUtn(auditDetail.getCertificateDetail().get(0).getUtn());
					
				}
				try {
					String addCnoUtn=auditDetail.getCertificateNo()+" "+auditDetail.getUtn();
					auditDetail.setQid(auditDao.getDbmsCryptoEncode(addCnoUtn));
					//auditDetail.setQid(e.EncodeCertiNoUtn(auditDetail.getCertificateNo(), auditDetail.getUtn()));
				}  catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<CertificateDetailWithOutAudit> certificateVthoutAuditList = new ArrayList<CertificateDetailWithOutAudit>();
				CertificateDetailWithOutAudit certificateVthoutAudit = null;
					
				if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.RENEWAL_SUB_TYPE_ID){
					
					certificateVthoutAudit = new CertificateDetailWithOutAudit();
					certificateVthoutAudit.setAuditSeqNo(auditSeqNo);
					certificateVthoutAudit.setVesselImoNo(auditDetail.getVesselImoNo());
					certificateVthoutAudit.setAuditTypeId(auditTypeId);
					certificateVthoutAudit.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					certificateVthoutAudit.setCertificateNo((auditDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT ? auditDetail.getCertificateNo() : (AppUtil.getCertificateNoFormat(auditTypeId,sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),auditSubTypeId))));
					certificateVthoutAudit.setUtn(getUTN().getUtnString());
					certificateVthoutAudit.setCompanyId(auditDetail.getCompanyId());
					try {
						String addCnoUtn=certificateVthoutAudit.getCertificateNo()+" "+certificateVthoutAudit.getUtn();

						certificateVthoutAudit.setQid(auditDao.getDbmsCryptoEncode(addCnoUtn));

						//certificateVthoutAudit.setQid(e.EncodeCertiNoUtn(certificateVthoutAudit.getCertificateNo(), certificateVthoutAudit.getUtn()));
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					certificateVthoutAuditList.add(certificateVthoutAudit);
					
				}
				
				if(auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID){
										
					certificateVthoutAudit = new CertificateDetailWithOutAudit();
					certificateVthoutAudit.setAuditSeqNo((auditTypeId == AppConstant.ISM_TYPE_ID ? AppConstant.ISM_NO_AUD_CERT_AUDITSEQ : (auditTypeId == AppConstant.ISPS_TYPE_Id ? AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ : AppConstant.MLC_NO_AUD_CERT_AUDITSEQ)));
					certificateVthoutAudit.setVesselImoNo(auditDetail.getVesselImoNo());
					certificateVthoutAudit.setAuditTypeId(auditTypeId);
					certificateVthoutAudit.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
					certificateVthoutAudit.setCertificateNo(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString());
					certificateVthoutAudit.setUtn(getUTN().getUtnString());
					certificateVthoutAudit.setCompanyId(auditDetail.getCompanyId());
					try {
						String addCnoUtn=certificateVthoutAudit.getCertificateNo()+" "+certificateVthoutAudit.getUtn();

						certificateVthoutAudit.setQid(auditDao.getDbmsCryptoEncode(addCnoUtn));

						//certificateVthoutAudit.setQid(e.EncodeCertiNoUtn(certificateVthoutAudit.getCertificateNo(), certificateVthoutAudit.getUtn()));
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					certificateVthoutAuditList.add(certificateVthoutAudit);	
				}
				
				auditDetail.setCertificateWithoutAudit(certificateVthoutAuditList);
				
				if(auditDetail.getOfficialId()!= null && auditDetail.getOfficialId() > 0){
					
					ResponseEntity<RmiAuditSigner> rmiData = rmi.auditorSignAndSeal(auditDetail.getOfficialId(),auditDetail.getCompanyId());
					
					log.info("rmiData ="+rmiData);
					
					log.info("Signature ="+rmiData.getBody().getSignature());
					
					auditDetail.setSeal(rmiData.getBody().getSeal());
					auditDetail.setSigner(rmiData.getBody().getSigner());
					auditDetail.setTitle(rmiData.getBody().getTitle());
					auditDetail.setSignature(rmiData.getBody().getSignature());
					
				}
				if (auditDetail != null) {
					File file = new File(currentDrc);
					file.mkdirs();
					mapper.writeValue(new File(currentDrc + AppConstant.SEPARATOR + auditSeqNo + AppConstant.JSON),
							auditDetail);

					List<AuditDetail> detailViews = new ArrayList<AuditDetail>();
					detailViews = auditDao.getLaptopPreviousAudit(auditTypeId, auditDetail.getAuditDate(),
							auditDetail.getVesselImoNo(), companyId, auditSeqNo);
					System.out.println("Audit Detail View :" + detailViews.size());
					log.info("detailViews123"+detailViews);
					if (!detailViews.isEmpty()) {
						List<AuditFinding> auditFindings = new ArrayList<AuditFinding>();

						auditFindings = auditDao.getLaptopPreviousFinding(auditTypeId, auditDetail.getAuditDate(),
								auditDetail.getVesselImoNo(), companyId, auditSeqNo);
						System.out.println(" auditFindings View :" + auditFindings.size());
						currentDrc = currentDrc + AppConstant.SEPARATOR + "pf";
						File currentFile = new File(currentDrc);
						currentFile.mkdirs();
						if (!auditFindings.isEmpty()) {
							System.out.println("auditFindings not zero");
							Set<Integer> auditSeqNos = auditFindings.stream().map(AuditFinding::getAuditSeqNo)
									.collect(Collectors.toSet());
							System.out.println(" detailViews Size:" + detailViews.size());
							/*detailViews.removeIf(audit -> {
								return !auditSeqNos.stream().anyMatch(seqNo -> seqNo.equals(audit.getAuditSeqNo()));

							});*/
							System.out.println(" detailViews Size:" + detailViews.size());

							mapper = new ObjectMapper();
							mapper.writeValue(new File(
									currentFile + AppConstant.SEPARATOR + "pf" + AppConstant.JSON),
									auditFindings);
						}
 
 						for (AuditDetail detailView : detailViews) {
							System.out.println("detailView :"+detailView.getAuditSeqNo());
							String findingPath = currentDrc + AppConstant.SEPARATOR + detailView.getAuditSeqNo();
 							/*
							 * Move the source directory to the destination
							 * directory. The destination directory must not
							 * exists prior to the move process.
							 */
 							detailView.getAuditFinding().forEach(auditFinding ->{
								System.out.println("detailView.getAuditFinding() ;"+auditFinding.getFindingSeqNo());
								//if(auditFinding.getFindingStatus().equals(AppConstant.FINDING_STATUS)){
 								String	srcDirPath =  AppConstant.SEPARATOR + detailView.getAuditSeqNo()+ AppConstant.SEPARATOR +auditFinding.getFindingSeqNo();
 									try {
 										FileUtils.copyDirectory(new File(srcDirPath), new File(findingPath+AppConstant.SEPARATOR +auditFinding.getFindingSeqNo()), true);
									} catch (Exception e) {
										System.out.println("Exception in Transfer File :"+e);
 										e.printStackTrace();
									}
 								//}
							});
						}
						detailViews.forEach(audit -> {
							audit.setAuditFinding(new ArrayList<AuditFinding>());
							/*audit.setAuditAuditorDetail(new ArrayList<AuditAuditorDetail>());*/
							audit.setAuditRptAttach(new ArrayList<AuditRptAttach>());
						});

						mapper = new ObjectMapper();
						mapper.writeValue(new File(
								currentFile + AppConstant.SEPARATOR + "pad" + AppConstant.JSON),detailViews);
					}
				}
			}
		
		return true;
	}
	
	@Override
	public int  pdfupload(StampDetails pdfUploadEntity) {
		int pageNumbers =0;
		File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "pdfSignUploads");
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File directoryOut = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "output");
		if (!directoryOut.exists()) {
			directoryOut.mkdirs();
		}
		File directoryPre = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "stampPreview");
		if (!directoryPre.exists()) {
			directoryPre.mkdirs();
		}
		if (directory.isDirectory() && pdfUploadEntity.getFileByte() != null) {
			

				Path path = Paths.get(directory + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
				try {
					Files.write(path, pdfUploadEntity.getFileByte());
					PDDocument document= PDDocument.load(new File(directory + AppConstant.SEPARATOR + pdfUploadEntity.getFileName()));
			        PDDocumentInformation info = document.getDocumentInformation();
			        pageNumbers=document.getNumberOfPages();
			        document.close();
					return pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + pdfUploadEntity.getFileName());
					e.printStackTrace();
					return pageNumbers;
				}
		}
		else
			return pageNumbers;
	}
	
	public static void copyFileUsingApache(File from, File to) throws IOException {
		FileUtils.copyFile(from, to);
	}
	
	@Override
	public int uploadPdfInToMachine(FileToUpload fileToUpload) {
		int returnVal = 0;
		File approvalDir = null;
		File CertificateDirHk = null;
		File CertificateDirEu = null;
		File CertificateDirEx = null;
		File stampDir = null;
		File reviewDir = null;
		File receiptDir = null;

		String userName = fileToUpload.getUserName().toUpperCase();

		if (fileToUpload.getReportName().equals("ApprovalReport")) {
			approvalDir = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "ApprovalFiles");
			fileToUpload.setFileName("Approval_"+fileToUpload.getFileName());
		}
		if (fileToUpload.getReportName().equals("CertificateHk")) {
			CertificateDirHk = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "CertificateHK");
			fileToUpload.setFileName("CertificateHK_"+fileToUpload.getFileName());
		}
		if (fileToUpload.getReportName().equals("CertificateEu")) {
			CertificateDirEu = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "CertificateEU");
			fileToUpload.setFileName("CertificateEU_"+fileToUpload.getFileName());
		}
		if (fileToUpload.getReportName().equals("CertificateEx")) {
			CertificateDirEx = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "CertificateEX");
			fileToUpload.setFileName("CertificateEX_"+fileToUpload.getFileName());
		}

		if (fileToUpload.getReportName().equals("StampReport")) {
			stampDir = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "Stamp");
			fileToUpload.setFileName("Stamp.pdf");
		}

		if (fileToUpload.getReportName().equals("ReviewReport")) {
			reviewDir = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "Review");
			fileToUpload.setFileName("PrintReport_"+fileToUpload.getFileName());
		}

		if (fileToUpload.getReportName().equals("ReciptLetter")) {
			receiptDir = new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\" + "Receipt");
			fileToUpload.setFileName("Receipt_"+fileToUpload.getFileName());
		}

		if (approvalDir != null) {
			if (!approvalDir.exists()) {
				approvalDir.mkdirs();
			}
			if (approvalDir.isDirectory() && fileToUpload.getFileByte() != null && approvalDir != null) {
				Path path = Paths.get(approvalDir + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(approvalDir + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
			}
		}

		if (CertificateDirHk != null) {
			if (!CertificateDirHk.exists()) {
				CertificateDirHk.mkdirs();
			}
			if (CertificateDirHk.isDirectory() && fileToUpload.getFileByte() != null && CertificateDirHk != null) {
				Path path = Paths.get(CertificateDirHk + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(CertificateDirHk + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
			}

		}

		if (CertificateDirEu != null) {
			if (!CertificateDirEu.exists()) {
				CertificateDirEu.mkdirs();
			}
			if (CertificateDirEu.isDirectory() && fileToUpload.getFileByte() != null && CertificateDirEu != null) {
				Path path = Paths.get(CertificateDirEu + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(CertificateDirEu + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
			}

		}

		if (CertificateDirEx != null) {
			if (!CertificateDirEx.exists()) {
				CertificateDirEx.mkdirs();
			}
			if (CertificateDirEx.isDirectory() && fileToUpload.getFileByte() != null && CertificateDirEx != null) {
				Path path = Paths.get(CertificateDirEx + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(CertificateDirEx + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
			}

		}

		if (stampDir != null) {
			if (!stampDir.exists()) {
				stampDir.mkdirs();
			}
			if (stampDir.isDirectory() && fileToUpload.getFileByte() != null && stampDir != null) {
				Path path = Paths.get(stampDir + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(stampDir + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
				
				
				
				/*try (SevenZOutputFile sevenZOutput = new SevenZOutputFile(
						new File(stampDir+"\\"+"reports_compress.7z"))) {
					File folderToZip = new File(stampDir+"");
					Files.walk(folderToZip.toPath()).forEach(p -> {
						File file = p.toFile();
						if (!file.isDirectory()) {
							try (FileInputStream fis = new FileInputStream(file)) {
								SevenZArchiveEntry entry_1 = sevenZOutput.createArchiveEntry(file, file.toString());
								sevenZOutput.putArchiveEntry(entry_1);
								sevenZOutput.write(Files.readAllBytes(file.toPath()));
								sevenZOutput.closeArchiveEntry();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					sevenZOutput.finish();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				
			}
			
			
		}

		if (reviewDir != null) {
			if (!reviewDir.exists()) {
				reviewDir.mkdirs();
			}
			if (reviewDir.isDirectory() && fileToUpload.getFileByte() != null && reviewDir != null) {
				Path path = Paths.get(reviewDir + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(reviewDir + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
			}
		}

		if (receiptDir != null) {
			if (!receiptDir.exists()) {
				receiptDir.mkdirs();
			}
			if (receiptDir.isDirectory() && fileToUpload.getFileByte() != null && receiptDir != null) {
				Path path = Paths.get(receiptDir + AppConstant.SEPARATOR + fileToUpload.getFileName());
				try {
					Files.write(path, fileToUpload.getFileByte());
					PDDocument document = PDDocument
							.load(new File(receiptDir + AppConstant.SEPARATOR + fileToUpload.getFileName()));
					PDDocumentInformation info = document.getDocumentInformation();
					int pageNumbers = document.getNumberOfPages();
					document.close();
					returnVal = pageNumbers;
				} catch (IOException e) {
					System.out.println("File Not Found :" + fileToUpload.getFileName());
					e.printStackTrace();
					returnVal = 0;
				}
			}
		}

		return returnVal;

	}
	
	@Transactional
	@Override
	public void pdfStampFile(String fileName) {
		try {
			
				File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "pdfSignUploads"+AppConstant.SEPARATOR);
				
				File directoryOut = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "output"+AppConstant.SEPARATOR + fileName);
				
					FileUtils.copyFileToDirectory(directoryOut, directory);
				 
				String[] strFNPrev = fileName.split(".pdf");
				File directoryPre = new File(servletContext.getContextPath() + AppConstant.SEPARATOR +"stampPreview"+ AppConstant.SEPARATOR);
					FileUtils.copyFileToDirectory(directoryOut, directoryPre);
				File renamePreSrc = new File(servletContext.getContextPath() + AppConstant.SEPARATOR +"stampPreview"+ AppConstant.SEPARATOR + fileName);	
				File renamePreDest = new File(servletContext.getContextPath() + AppConstant.SEPARATOR +"stampPreview"+ AppConstant.SEPARATOR + strFNPrev[0]+"_preview.pdf");
				renamePreSrc.renameTo(renamePreDest);
			
			} catch (IOException e) {
			    e.printStackTrace();
			}
	}
	
	@Transactional
	@Override
	public ResponseEntity<Resource>  pdfPageNumbers(StampDetails pdfUploadEntity) {
		String[] strFNPrev = pdfUploadEntity.getFileName().split(".pdf");
		String pathOutput = servletContext.getContextPath() + AppConstant.SEPARATOR +"stampPreview"+ AppConstant.SEPARATOR + strFNPrev[0]+"_preview.pdf";
		String pathInput = servletContext.getContextPath() + AppConstant.SEPARATOR +"pdfSignUploads"+ AppConstant.SEPARATOR + pdfUploadEntity.getFileName();
	    try{ 
	    	
	    	Integer auditTypeId = auditDao.getAuditType(pdfUploadEntity.getAuditSeqNo());
	    	if(auditTypeId==1006){
		    	log.info(pdfUploadEntity.toString());
		    	File file = new File(".").getAbsoluteFile();
		    	File root = file.getParentFile();
		    	Image imageseal=null,imagesign=null;
		    	while (root.getParentFile() != null) {
		    	    root = root.getParentFile();
		    	}
		    	
			    File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "pdfSignUploads");
			    
			    Path pathSrc = Paths.get(directory + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
			    
			    String[] initPagCnt,corxCnt,coryCnt,sealCnt,signCnt,signerCnt;
			    
			    
			    corxCnt=pdfUploadEntity.getCorx().split(",");
			    coryCnt=pdfUploadEntity.getCory().split(",");
			    sealCnt=pdfUploadEntity.getSeal().split(",");
			    signCnt=pdfUploadEntity.getSign().split(",");
			    signerCnt=pdfUploadEntity.getSigner().split(",");
			    
			    List<Integer> numCnt = new ArrayList<Integer>();
			    Set<Integer> prevPgCnt= new HashSet<Integer>();
			    List<Integer> Cntx = new ArrayList<Integer>();
			    List<Integer> Cnty = new ArrayList<Integer>();
			    List<String> sealACnt = new ArrayList<String>();
			    List<String> signACnt = new ArrayList<String>();
			    List<String> signerACnt = new ArrayList<String>();
			    
			   
			    	initPagCnt = pdfUploadEntity.getPagenumbers().split("&");
			    	
			    	for(int pg =0;pg<initPagCnt.length;pg++)	{	
			    		String[] strCnt = stampGetPageNo(initPagCnt[pg]);
			    		for(int no =0; no<strCnt.length;no++){
			    			if(strCnt[no].contains("All")){
					    		prevPgCnt.add(1);
					    		for(int allno =1;allno<=pdfUploadEntity.getTotalNoPages();allno++){
					    			numCnt.add( allno);
							    	Cntx.add(Integer.parseInt(corxCnt[pg]));
							    	Cnty.add(Integer.parseInt(coryCnt[pg]));
							    	sealACnt.add(sealCnt[pg]);
							    	signACnt.add(signCnt[pg]);
							    	signerACnt.add(signerCnt[pg]);
							    	
					    		}
					    	}else{
						    	prevPgCnt.add(Integer.parseInt(strCnt[no]));
						    	numCnt.add(Integer.parseInt(strCnt[no]));
						    	Cntx.add(Integer.parseInt(corxCnt[pg]));
						    	Cnty.add(Integer.parseInt(coryCnt[pg]));
						    	sealACnt.add(sealCnt[pg]);
						    	signACnt.add(signCnt[pg]);
						    	signerACnt.add(signerCnt[pg]);
					    	}
			    		}
			    		
			    	}
			     
			    log.info("prev"+prevPgCnt.toArray());
			    log.info("pagno"+numCnt);
			    log.info("x cor"+Cntx);
			    log.info("y cor"+Cnty);
			    log.info("seal"+sealACnt);
			    log.info("sign"+signACnt);
			    log.info("signer"+signerACnt);
			    
			    Path pathdest = Paths.get(servletContext.getContextPath() +"\\" + "output" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
			     	String contextPath_[] = servletContext.getRealPath(AppConstant.SEPARATOR).split(":");
			        
		
				Path pathdestPre = Paths.get(servletContext.getContextPath() + "\\" + "stampPreview" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());	 
				
				String[] strFN = pdfUploadEntity.getFileName().split(".pdf");
				Path pdfdestPre = Paths.get(servletContext.getContextPath() + "\\" + "stampPreview" + AppConstant.SEPARATOR + strFN[0] + "_preview.pdf");	 
				
					        BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(pdfUploadEntity.getSealByte()));
					        BufferedImage bufSign = ImageIO.read(new ByteArrayInputStream(pdfUploadEntity.getSignByte()));
					        
					        ImageIO.write(bufSign, "png", new File("newLabel2.png"));
					        
					        ImageIO.write(bufImg, "png", new File("newLabel.png"));
					        
					        PdfReader  reader = new PdfReader(pathSrc.toString());
					        reader.unethicalreading=true;
					       
						    
						    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfdestPre.toString()));
						        
						    BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED);
						    int tSize = 18;
						    String nameAuditor = pdfUploadEntity.getSignerName();
						        for(int i=0;i<numCnt.size();i++){
							    						        	
						        	PdfContentByte over = stamper.getOverContent(numCnt.get(i));
						        	
						        	
							        if(signACnt.get(i).toString().contains("true")){
								        imagesign= Image.getInstance("newLabel2.png");
								        imagesign.scaleAbsolute(80, 80);
								        imagesign.setAbsolutePosition(Cntx.get(i), Cnty.get(i));
								        over.addImage(imagesign);
								        
							        }
							        if(sealACnt.get(i).toString().contains("true")){
								        imageseal = Image.getInstance("newLabel.png");
								        imageseal.scaleAbsolute(80, 80);
								        imageseal.setAbsolutePosition(Cntx.get(i), Cnty.get(i));
								        over.addImage(imageseal);
							        }
							        if(signerACnt.get(i).toString().contains("true")){
							        	
								        over.setFontAndSize(bf, tSize);
									    over.beginText();
									    over.showTextAligned(Element.ALIGN_CENTER, nameAuditor, Cntx.get(i)+60, Cnty.get(i)-10, 0);
									    over.endText();
							        }
						        }
						        stamper.close();
						        reader.close();
						        PdfReader readerPre = new PdfReader(pdfdestPre.toString());
						        readerPre.selectPages(prevPgCnt.toString());
						        PdfStamper stamperPre = new PdfStamper(readerPre, new FileOutputStream(pathdestPre.toString()));
						        stamperPre.close();
						        readerPre.close();
						        if(pdfUploadEntity.getStatus().contains("save")){
							        try {
							        	PdfReader readerPre1 = new PdfReader(pdfdestPre.toString());
								        PdfStamper stamperPre1 = new PdfStamper(readerPre1, new FileOutputStream(pathdest.toString()));
								        readerPre1.close();
								        stamperPre1.close();
								        auditDao.stampDBCall(pdfUploadEntity);
							     
					
										File from = new File(contextPath_[0] + ":" + servletContext.getContextPath() + AppConstant.SEPARATOR
												+ "output" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
										File to = new File(AppConstant.FILE_PATH_REPORT + pdfUploadEntity.getUserName() + "//" + "Stamp" + "//"
												+ "Stamp_" + pdfUploadEntity.getFileName());
										copyFileUsingApache(from, to);
									} catch (Exception e) {
										e.printStackTrace();
									}
		             
						        }
						        if(pdfUploadEntity.getStatus().contains("sspDmlc")){
							        try {
							        	PdfReader readerPre1 = new PdfReader(pdfdestPre.toString());
								        PdfStamper stamperPre1 = new PdfStamper(readerPre1, new FileOutputStream(pathdest.toString()));
								        readerPre1.close();
								        stamperPre1.close();
					
										File from = new File(contextPath_[0] + ":" + servletContext.getContextPath() + AppConstant.SEPARATOR
												+ "output" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
										File to = new File(AppConstant.FILE_PATH_REPORT + pdfUploadEntity.getUserName() + "//" + "Stamp" + "//"
												+ "Stamp_" + pdfUploadEntity.getFileName());
										copyFileUsingApache(from, to);
									} catch (Exception e) {
										e.printStackTrace();
									}
			             
							}
					
	    	}else if(auditTypeId==1004 || auditTypeId == 1005 || (auditTypeId>=AppConstant.SOPEP_TYPE_ID && auditTypeId<=AppConstant.COW_TYPE_ID)){   // Changed by kiran (SDR_TYPE_ID to COW_TYPE_ID)
	    		log.info(pdfUploadEntity.toString());
		    	File file = new File(".").getAbsoluteFile();
		    	File root = file.getParentFile();
		    	String approvedImg="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";
		    	BASE64Decoder decoder = new BASE64Decoder();
		        byte[] approvedBytes = decoder.decodeBuffer(approvedImg);
		        Image imageseal=null,imageApp,imageDate;
		    	while (root.getParentFile() != null) {
		    	    root = root.getParentFile();
		    	}
		    	
			    File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR + "pdfSignUploads");
			    
			    Path pathSrc = Paths.get(directory + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
			    
			    String[] initPagCnt,corxCnt,coryCnt,sealCnt,signCnt,signerCnt;
			    
			    
			    corxCnt=pdfUploadEntity.getCorx().split(",");
			    coryCnt=pdfUploadEntity.getCory().split(",");
			    sealCnt=pdfUploadEntity.getSeal().split(",");
			    signCnt=pdfUploadEntity.getSign().split(",");
			    signerCnt=pdfUploadEntity.getSigner().split(",");
			    
			    List<Integer> numCnt = new ArrayList<Integer>();
			    Set<Integer> prevPgCnt= new HashSet<Integer>();
			    List<Integer> Cntx = new ArrayList<Integer>();
			    List<Integer> Cnty = new ArrayList<Integer>();
			    List<String> sealACnt = new ArrayList<String>();
			    List<String> signACnt = new ArrayList<String>();
			    List<String> signerACnt = new ArrayList<String>();
			    
			   
			    	initPagCnt = pdfUploadEntity.getPagenumbers().split("&");
			    	
			    	for(int pg =0;pg<initPagCnt.length;pg++)	{	
			    		String[] strCnt = stampGetPageNo(initPagCnt[pg]);
			    		for(int no =0; no<strCnt.length;no++){
			    			if(strCnt[no].contains("All")){
					    		prevPgCnt.add(1);
					    		for(int allno =1;allno<=pdfUploadEntity.getTotalNoPages();allno++){
					    			numCnt.add( allno);
							    	Cntx.add(Integer.parseInt(corxCnt[pg]));
							    	Cnty.add(Integer.parseInt(coryCnt[pg]));
							    	sealACnt.add(sealCnt[pg]);
							    	signACnt.add(signCnt[pg]);
							    	signerACnt.add(signerCnt[pg]);
							    	
					    		}
					    	}else{
						    	prevPgCnt.add(Integer.parseInt(strCnt[no]));
						    	numCnt.add(Integer.parseInt(strCnt[no]));
						    	Cntx.add(Integer.parseInt(corxCnt[pg]));
						    	Cnty.add(Integer.parseInt(coryCnt[pg]));
						    	sealACnt.add(sealCnt[pg]);
						    	signACnt.add(signCnt[pg]);
						    	signerACnt.add(signerCnt[pg]);
					    	}
			    		}
			    		
			    	}
			     
			    log.info("prev"+prevPgCnt.toArray());
			    log.info("pagno"+numCnt);
			    log.info("x cor"+Cntx);
			    log.info("y cor"+Cnty);
			    log.info("seal"+sealACnt);
			    log.info("sign"+signACnt);
			    log.info("signer"+signerACnt);
			    
			    Path pathdest = Paths.get(servletContext.getContextPath() +"\\" + "output" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
			     	String contextPath_[] = servletContext.getRealPath(AppConstant.SEPARATOR).split(":");
			        
		
				Path pathdestPre = Paths.get(servletContext.getContextPath() + "\\" + "stampPreview" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());	 
				
				String[] strFN = pdfUploadEntity.getFileName().split(".pdf");
				Path pdfdestPre = Paths.get(servletContext.getContextPath() + "\\" + "stampPreview" + AppConstant.SEPARATOR + strFN[0] + "_preview.pdf");	 
				String dateImg =null;
				/*Added by ramya on 25-03-2022 for jira Id--> 5160 */
				if(auditTypeId>=1007 && auditTypeId<=1013){	//added by ramya for COW stamp img (Jira id-->5204)
					dateImg=servletContext.getContextPath()+AppConstant.SEPARATOR+"src"+AppConstant.SEPARATOR+"main"+AppConstant.SEPARATOR+"resources"+AppConstant.SEPARATOR+"img"+AppConstant.SEPARATOR+auditTypeId+".png";
				}
				else{
					dateImg ="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";
					byte[] dateSign = decoder.decodeBuffer(dateImg);
					BufferedImage dateBufImg = ImageIO.read(new ByteArrayInputStream(dateSign));
					ImageIO.write(dateBufImg, "png", new File("newLabel3.png"));
				}
				
					        BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(pdfUploadEntity.getSealByte()));
					        
					        BufferedImage appBufImg = ImageIO.read(new ByteArrayInputStream(approvedBytes));
					        
					        
					        ImageIO.write(appBufImg, "png", new File("newLabel2.png"));
					        
					        ImageIO.write(bufImg, "png", new File("newLabel.png"));
					        
					        
					        PdfReader  reader = new PdfReader(pathSrc.toString());
					        reader.unethicalreading=true;
					       
						    
						    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfdestPre.toString()));
						        
						    BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED);
						    int tSize = 14;
						    String nameAuditor = pdfUploadEntity.getSignerName();
						        for(int i=0;i<numCnt.size();i++){
							    						        	
						        	PdfContentByte over = stamper.getOverContent(numCnt.get(i));
						        	
						        	
							        if(signACnt.get(i).toString().contains("true")){
							        	if(Cntx.get(i)==490 && Cnty.get(i)==40){
							        		imageApp= Image.getInstance("newLabel2.png");
								        	imageApp.scaleAbsolute(70, 50);
								        	if(auditTypeId!=1012){
												imageApp.setAbsolutePosition(430, 90);
								        	}
								        	else{
								        		imageApp.setAbsolutePosition(430, 110);
								        	}
											over.addImage(imageApp);
							        	}else{
								        	imageApp= Image.getInstance("newLabel2.png");
								        	imageApp.scaleAbsolute(70, 50);
								        	if(auditTypeId!=1012){
												imageApp.setAbsolutePosition(Cntx.get(i)+30, Cnty.get(i)+70);
								        	}
								        	else{
								        		imageApp.setAbsolutePosition(Cntx.get(i)+30, Cnty.get(i)+90);
								        	}
											over.addImage(imageApp);
							        	}
								        
							        }
							        if(sealACnt.get(i).toString().contains("true")){
							        	if(Cntx.get(i)==490 && Cnty.get(i)==40){
									        imageseal = Image.getInstance("newLabel.png");
									        imageseal.scaleAbsolute(80, 80);
									        imageseal.setAbsolutePosition(Cntx.get(i), Cnty.get(i));
									        over.addImage(imageseal);
							        	}else
							        	{
							        		imageseal = Image.getInstance("newLabel.png");
									        imageseal.scaleAbsolute(80, 80);
									        imageseal.setAbsolutePosition(Cntx.get(i)+90, Cnty.get(i)+20);
									        over.addImage(imageseal);
							        	}
							        }
							        if(signerACnt.get(i).toString().contains("true")){
							        	
							        	if(Cntx.get(i)==490 && Cnty.get(i)==40){
							        		over.setFontAndSize(bf, tSize);
										    over.beginText();
										    if(auditTypeId!=1012)
										    {
										    over.showTextAligned(Element.ALIGN_CENTER, pdfUploadEntity.getStampDate(), 480,80, 0);
										    }else
										    {over.showTextAligned(Element.ALIGN_CENTER, pdfUploadEntity.getStampDate(), 480,100, 0);		//added by ramya for jira id-->IRI-5241
										    }
										    over.endText();
										    if(auditTypeId>=1007 && auditTypeId<=1013){			//added by ramya for jira id -->5160
										    	imageDate= Image.getInstance(dateImg);
										    }
										    else{
										    	imageDate= Image.getInstance("newLabel3.png");
										    }	
								        	if(auditTypeId!=1012){
										    	imageDate.scaleAbsolute(180, 70);
										    }
										    else{
										    	imageDate.scaleAbsolute(180, 90);
										    }
								        	imageDate.setAbsolutePosition(400, 20);
									        over.addImage(imageDate);
							        	}else{
							        		over.setFontAndSize(bf, tSize);
										    over.beginText();
										    if(auditTypeId!=1012){
										    over.showTextAligned(Element.ALIGN_CENTER, pdfUploadEntity.getStampDate(), Cntx.get(i)+80, Cnty.get(i)+60, 0);
										    }
										    else
										    {
										    	over.showTextAligned(Element.ALIGN_CENTER, pdfUploadEntity.getStampDate(), Cntx.get(i)+80, Cnty.get(i)+80, 0);			//added by ramya for jira id-->IRI-5241
											   }
										    over.endText();
										    if(auditTypeId>=1007 && auditTypeId<=1013){			//added by ramya for jira id -->5160
										    	imageDate= Image.getInstance(dateImg);
										    }
										    else{
										    	imageDate= Image.getInstance("newLabel3.png");
										    }	
								        	if(auditTypeId!=1012){
										    	imageDate.scaleAbsolute(180, 70);
										    }
										    else{
										    	imageDate.scaleAbsolute(180, 90);
										    }
								        	imageDate.setAbsolutePosition(Cntx.get(i), Cnty.get(i));
									        over.addImage(imageDate);
							        	}
							        	
								        
							        	
							        }
						        }
						        stamper.close();
						        reader.close();
						        PdfReader readerPre = new PdfReader(pdfdestPre.toString());
						        readerPre.selectPages(prevPgCnt.toString());
						        PdfStamper stamperPre = new PdfStamper(readerPre, new FileOutputStream(pathdestPre.toString()));
						        stamperPre.close();
						        readerPre.close();
						        if(pdfUploadEntity.getStatus().contains("save")){
							        try {
							        	PdfReader readerPre1 = new PdfReader(pdfdestPre.toString());
								        PdfStamper stamperPre1 = new PdfStamper(readerPre1, new FileOutputStream(pathdest.toString()));
								        readerPre1.close();
								        stamperPre1.close();
								        auditDao.stampDBCall(pdfUploadEntity);
							     
					
										File from = new File(contextPath_[0] + ":" + servletContext.getContextPath() + AppConstant.SEPARATOR
												+ "output" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
										File to = new File(AppConstant.FILE_PATH_REPORT + pdfUploadEntity.getUserName() + "//" + "Stamp" + "//"
												+ "Stamp_" + pdfUploadEntity.getFileName());
										copyFileUsingApache(from, to);
									} catch (Exception e) {
										e.printStackTrace();
									}
		             
						        }
						        if(pdfUploadEntity.getStatus().contains("sspDmlc")){
							        try {
							        	PdfReader readerPre1 = new PdfReader(pdfdestPre.toString());
								        PdfStamper stamperPre1 = new PdfStamper(readerPre1, new FileOutputStream(pathdest.toString()));
								        readerPre1.close();
								        stamperPre1.close();
					
										File from = new File(contextPath_[0] + ":" + servletContext.getContextPath() + AppConstant.SEPARATOR
												+ "output" + AppConstant.SEPARATOR + pdfUploadEntity.getFileName());
										File to = new File(AppConstant.FILE_PATH_REPORT + pdfUploadEntity.getUserName() + "//" + "Stamp" + "//"
												+ "Stamp_" + pdfUploadEntity.getFileName());
										copyFileUsingApache(from, to);
									} catch (Exception e) {
										e.printStackTrace();
									}
			             
							}
	    	}
		    	return new ResponseEntity<Resource>(appUtil.getFileByteStream(pathOutput), appUtil.setHeaderStreamType(pathOutput, pdfUploadEntity.getFileName()),
						HttpStatus.OK);
		   
		} catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<Resource>(appUtil.getFileByteStream(pathInput), appUtil.setHeaderStreamType(pathInput, pdfUploadEntity.getFileName()),
					HttpStatus.OK);
		}
	}
	
	public String[] stampGetPageNo(String initPagCnt) {
		
		Set<String> pages = new HashSet<String>();
		String[] pagnoCnt=initPagCnt.split(",");
		for(int k =0;k<pagnoCnt.length;k++){
			String[] hyphenCnt=pagnoCnt[k].split("-");
			if(hyphenCnt.length==1)
				pages.add(hyphenCnt[0]);
			else if(hyphenCnt.length==2){
    				for(int j=Integer.parseInt(hyphenCnt[0]);j <=Integer.parseInt(hyphenCnt[1]); j++){
    					pages.add(Integer.toString(j));
    				}
			}
		}
		log.info("setpagno"+pages);
		String[] pgArray = pages.toArray(new String[pages.size()]);
		
		return pgArray;
	}
	
	public static String toString(boolean b) {
        return b ? "true" : "false";
    }
	
	@Transactional
	@Override
	public ResponseEntity<Resource> auditRptAttach(String status,String fileName) {
		
		
		String path = servletContext.getContextPath() + AppConstant.SEPARATOR +status+ AppConstant.SEPARATOR + fileName;
		
		return new ResponseEntity<Resource>(appUtil.getFileByteStream(path), appUtil.setHeaderStreamType(path, fileName),
				HttpStatus.OK);
	}
	
	@Transactional
	@Override
	public List<Map<String ,Object>> auditGetStamp(String REVIEW_REPORT_NO,int AUDIT_SEQ_NO) {
		
		return auditDao.auditGetStamp(REVIEW_REPORT_NO,AUDIT_SEQ_NO);
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String,Boolean>> getFileStamp(String FILE_NAME,Integer auditSeqNo) {
		return auditDao.getFileStamp(FILE_NAME, auditSeqNo);
	}
	
	@Transactional
	@Override
	public ResponseEntity<String> deleteStamp(String status, String FILE_NAME, int USER_ID) {
		
		try{
			String[] strFNPrev = FILE_NAME.split(".pdf");
			String pathPreview = servletContext.getContextPath() + AppConstant.SEPARATOR +"stampPreview"+ AppConstant.SEPARATOR + strFNPrev[0]+"_preview.pdf";
			String pathsrc = servletContext.getContextPath() +AppConstant.SEPARATOR + "pdfSignUploads"+AppConstant.SEPARATOR+ FILE_NAME;
			String pathprev = servletContext.getContextPath() +AppConstant.SEPARATOR + "stampPreview"+AppConstant.SEPARATOR+ FILE_NAME;
			if(status.equals("close")){
				File directorydelete = new File(pathsrc);
				if(directorydelete.exists()){
					appUtil.deleteStampFile(pathsrc);
					appUtil.deleteStampFile(pathprev);
					appUtil.deleteStampFile(pathPreview);
				}
		    }else if(status.equals("perDelete")){
		    	String pathOutput = servletContext.getContextPath() +AppConstant.SEPARATOR + "output"+AppConstant.SEPARATOR + FILE_NAME;
		    	File directorydelete = new File(pathsrc);
				if(directorydelete.exists()){
					appUtil.deleteStampFile(pathsrc);
				}	
				
				File filedelete = new File(pathOutput);
				if(filedelete.exists()){
					appUtil.deleteStampFile(pathOutput);
					appUtil.deleteStampFile(pathprev);
					appUtil.deleteStampFile(pathPreview);
				}
				
				auditDao.delStampDet(FILE_NAME, USER_ID);
				
			    /*String stampDbDelete = "DELETE FROM StampDetails WHERE FILE_NAME=:FILE_NAME AND USER_ID=:USER_ID";
				
		    	Query stampDbDeleteQuery = hibsession.getSession().createQuery(stampDbDelete);
		    	stampDbDeleteQuery.setString("FILE_NAME", FILE_NAME);
		    	stampDbDeleteQuery.setInteger("USER_ID", USER_ID);
		    	stampDbDeleteQuery.executeUpdate();*/
		    }
			return new ResponseEntity<String> (HttpStatus.OK);
		}
	    catch(IOException e){
	    	log.info(e.toString());
	    	return new ResponseEntity<String> (HttpStatus.OK);
	    }
		
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String, String>> getDownloadLink(String userName, String fileType, String fileName, String reviewReportNo) {
		Map<String, String> downloadMap = new HashMap<>();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		Calendar calcal = Calendar.getInstance();
		java.util.Date date = calcal.getTime();

		// get the latest id;
		Integer count = (int) repo.count();
		String otp = new AppUtil().getRandomString();
		Integer linkId = (int) (100000 * Math.random());
		FileDownloadHistory hist = new FileDownloadHistory(count+1, userDetails.getUsername(), userName, fileName, otp,
				fileType, 'A', date, userDetails.getUsername(), linkId, reviewReportNo );
		
		repo.save(hist);
		
		auditDao.getDownLink(otp,linkId,userName,fileType,fileName,reviewReportNo);

		/*// Sending Link
		 Query callStoredProcedureDownloadFile =
		 hibsession.getSession().createSQLQuery(
		 "CALL MAIL_SENDING( :from_mail, :to_mail, :cc_mail, :subject,:body)")
		 .setString("from_mail", values.getFromMail())
		 .setString("to_mail", userName ) // to whom we will send
		 .setString("cc_mail","")
		 .setString("subject",values.getMailSubject())
		 .setString("body",values.getMailBody()+values.getDownloadBaseLink()+ linkId) ;
		 callStoredProcedureDownloadFile.executeUpdate();
		 
		// Sending Link
				 Query emailOtp =
				 hibsession.getSession().createSQLQuery(
				 "CALL MAIL_SENDING( :from_mail, :to_mail, :cc_mail, :subject,:body)")
				 .setString("from_mail", values.getFromMail())
				 .setString("to_mail", userName ) // to whom we will send
				 .setString("cc_mail","")
				 .setString("subject","Passcode for file Download")
				 .setString("body","Your Passcode is : "+otp) ;
				 emailOtp.executeUpdate();*/

		downloadMap.put("msg", "Download Link Sent");
		return new ResponseEntity<Map<String, String>>(downloadMap, HttpStatus.OK);
	}
	
	
	@Transactional
	@Override
	public ResponseEntity<String> sendMailReports(@RequestBody FileToUpload fileToUpload ) throws IOException { 
  
		 
		String userName = fileToUpload.getUserName().toUpperCase();
		if(fileToUpload.getAuditSubTypeId().equals(AppConstant.AMENDMENT_SUB_TYPE_ID)){
			fileToUpload.setFileName(fileToUpload.getFileName()+"_IHM_"+"AMENDMENT_"+fileToUpload.getAuditReportNo());
		}else{
			fileToUpload.setFileName(fileToUpload.getFileName()+"_IHM_"+"INITIAL_"+fileToUpload.getAuditReportNo());
		}
		   
		 Path path = Paths.get(AppConstant.FILE_PATH_REPORT + userName + "\\"+"test");
		 try {
				Files.walk(path)
				    .sorted(Comparator.reverseOrder())
				    .map(Path::toFile)
				    .forEach(File::delete);
			} catch (IOException e) {
				 
			}
			 
			 if (!path.toFile().exists()) {
				 path.toFile().mkdirs();
		    	  
				}
		 
		if (fileToUpload.getReviewDocFr()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "Review" + "//"
							+ "PrintReport_Final"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "PrintReport_Final"+fileToUpload.getFileName() + ".pdf"));
		}
		
		if (fileToUpload.getReviewDocPr()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "Review" + "//"
							+ "PrintReport_Preliminary"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "PrintReport_Preliminary"+fileToUpload.getFileName() + ".pdf"));
			 
		}
		
		if (fileToUpload.getReceiptDoc()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "Receipt" + "//"
							+ "Receipt_"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "Receipt_"+fileToUpload.getFileName() + ".pdf"));
			 
		}
		if (fileToUpload.getApprovaDoc()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "ApprovalFiles" + "//"
							+ "Approval_"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "Approval_"+fileToUpload.getFileName() + ".pdf"));
		}
		if (fileToUpload.getStampDoc()) {
		 

			File sFile = new File(AppConstant.FILE_PATH_REPORT + userName + "//" + "Stamp" + "//");
			File[] sourceFiles = sFile.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(fileToUpload.getAuditReportNo()+".pdf") ) {
						
						return true;
					} else {
						return false;
					}
				}
			});

			for (File fSource : sourceFiles) {
				File fTarget = new File(new File(AppConstant.FILE_PATH_REPORT + userName + "//" + "Test" + "//"),
						fSource.getName());
				try {
					FileUtils.copyFile(fSource, fTarget);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		if (fileToUpload.getIhmCertDocHk()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "CertificateHK" + "//"
							+ "CertificateHK_"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "CertificateHK_"+fileToUpload.getFileName() + ".pdf"));
		}
		if (fileToUpload.getIhmCertDocEu()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "CertificateEU" + "//"
							+ "CertificateEU_"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "CertificateEU_"+fileToUpload.getFileName() + ".pdf"));
		}
		if (fileToUpload.getIhmCertDocEx()) {
			Files.move(
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "CertificateEX" + "//"
							+ "CertificateEX_"+fileToUpload.getFileName() + ".pdf"),
					Paths.get(AppConstant.FILE_PATH_REPORT + userName + "//" + "test" + "//"
							+ "CertificateEX_"+fileToUpload.getFileName() + ".pdf"));
		}
		
		
		Path fileToDeletePath = Paths.get(AppConstant.FILE_UPLOAD_DIR + userName + "\\"+"reports_compress.7z");
		try {
			Files.delete(fileToDeletePath);
		} catch (IOException e1) {
			 
			e1.printStackTrace();
		}
	    
		try (SevenZOutputFile sevenZOutput = new SevenZOutputFile(
				new File(AppConstant.FILE_UPLOAD_DIR + userName + "\\"+"reports_compress.7z"))) {
			File  folderToZip = new File(AppConstant.FILE_UPLOAD_DIR + userName+"\\"+"test");
			Files.walk(folderToZip.toPath()).forEach(p -> {
				File file = p.toFile();
				if (!file.isDirectory()) {
					try (FileInputStream fis = new FileInputStream(file)) {
						SevenZArchiveEntry entry_1 = sevenZOutput.createArchiveEntry(file, file.toString());
						sevenZOutput.putArchiveEntry(entry_1);
						sevenZOutput.write(Files.readAllBytes(file.toPath()));
						sevenZOutput.closeArchiveEntry();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			sevenZOutput.finish();
			sevenZOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
		}
		
		fileToUpload.setFilePath(
				AppConstant.FILE_PATH_REPORT + userName +"//"+"reports_compress.7z");
		fileToUpload.setFileName("reports_compress.7z");
		mailNotificationforReport(fileToUpload);
		

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	private void mailNotificationforReport(FileToUpload fileToUpload) {
		// Recipient's email ID needs to be mentioned.
	      String to = fileToUpload.getUserMailId();

	      // Sender's email ID needs to be mentioned
	     // String from = "smtpiritesting@bsolsystems.com";
	      String from = "noreply@bsolsystems.com";
	      //String from = "testd794@gmail.com";
	      
	      final String username = "testd794";//change accordingly
	      final String password = "Dileep123";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      //String host = "smtp.gmail.com";
	      String host = "m.outlook.com";

	      Properties props = new Properties();
	     /* properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", "25");
	        properties.put("mail.smtp.ssl.enable", "true");
	        properties.put("mail.smtp.auth", "true");
         */
	      
	      props.put("mail.smtp.starttls.enable", "true");
		  props.put("mail.smtp.port", "25");
		  props.put("mail.smtp.host", host);
		  props.put("mail.smtp.auth", "true");
		  props.put("mail.smtp.ssl.trust",host);
		  
	      // Get the Session object.
	        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                //return new PasswordAuthentication("smtpiritesting@bsolsystems.com", "Bsol@123");
	            	return new PasswordAuthentication("noreply@bsolsystems.com", "Bsol@123");
	            }
	        });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));

	         // Set Subject: header field
	         message.setSubject("IHM reviews reports for vessel: "+fileToUpload.getVesselName()+"("+fileToUpload.getOfficialNo()+") "+fileToUpload.getVesselImoNo());

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText("");

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new FileDataSource(fileToUpload.getFilePath());
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(fileToUpload.getFileName());
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String, Object>> getDMLCLocationDate(Integer vesselImoNo, Long companyId,Integer auditSeqNo, String status) {
				
		return auditDao.getDMLCLocationDate(vesselImoNo,companyId,auditSeqNo,status);
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String, Object>> getCompletionDate(Integer vesselImoNo, Long companyId,Integer auditTypeId,String status,Integer auditSeqNo) {
		
		return auditDao.getCompletionDate(vesselImoNo,companyId,auditTypeId,status,auditSeqNo);
	}
	
	@Transactional
	@Override
	public Map<String, Object> getCertificateIHM(Integer VESSEL_IMO_NO,Integer auditSeqNo) {
		
		return auditDao.getCertificateIHM(VESSEL_IMO_NO,auditSeqNo);
	}
	
	@Transactional
	@Override
	public Map<String,Object>  getCompletedStatus(Integer vesselImoNo,Integer auditTypeId) {
		
		return auditDao.getCompletedStatus(vesselImoNo,auditTypeId);
		
	}
	
	@Transactional
	@Override
	public ResponseEntity<HttpStatus>  updateVesselDetails(List<VesselDetailsHistory> vesselDetailsHistory) {
		
		auditDao.updateVesselDetailsDB(vesselDetailsHistory);
					     
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
								
	}
	
	@Transactional
	@Override
	public Map<String, Object> getVesselHistory(Integer VESSEL_IMO_NO,String status) {
		
		return auditDao.getVesselHistory(VESSEL_IMO_NO,status);
	}
	
	@Transactional
	@Override
	public Map<String,Object> getVesselRefresh(Integer VESSEL_IMO_NO) {
		
		return auditDao.getVesselRefresh(VESSEL_IMO_NO);
	}
	
	@Transactional
	@Override
	public Map<String, Object> updateVesselAuto(VesselRefresh vesselRefresh) {
		
		return auditDao.updateVesselAuto(vesselRefresh);
	}
}
