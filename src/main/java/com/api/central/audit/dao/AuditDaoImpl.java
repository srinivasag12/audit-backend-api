package com.api.central.audit.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Date;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import com.api.central.audit.entity.AuditAuditorDetail;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.AuditFindingModel;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.DOCNumberChange;
import com.api.central.audit.entity.FindingDetail;
import com.api.central.audit.entity.FindingDetailModel;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.audit.entity.FindingRptAttachModel;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.audit.entity.ReportData;
import com.api.central.audit.entity.SspReviewDetail;
import com.api.central.audit.entity.StampDetails;
import com.api.central.audit.entity.VesselApprovalDetails;
import com.api.central.audit.entity.VesselDetailsHistory;
import com.api.central.audit.entity.VesselNotification;
import com.api.central.certificate.controller.CertificateIhmController;
import com.api.central.certificate.dao.CertificateDao;
import com.api.central.certificate.dao.CertificateDaoImpl;
import com.api.central.certificate.dao.CertificateIhmDao;
import com.api.central.config.AppHibSession;
import com.api.central.fileDownload.FileOperationValues;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.model.ReportTypes;
import com.api.central.refreshVesselData.VesselRefresh;
import com.api.central.rmiaudit.entity.RmiAudit;
import com.api.central.rmiaudit.entity.RmiAuditModel;
import com.api.central.util.AppConstant;
import com.api.central.util.AppSQLConstant;
import com.api.central.util.AppUtil;
import com.api.central.util.MasterDatas;
import com.api.central.util.RestTemplateUtil;
import com.api.central.util.SequenceGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
@SuppressWarnings({"unused","unchecked","deprecation"})
public class AuditDaoImpl implements AuditDao {
	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	FileOperationValues values;
	
	@Autowired
	private CertificateIhmController certificateIhmController;  //Added by sudharsan and Chibi for JIRA-ID=5377&5378

	
	@Autowired
	private CertificateDaoImpl certificateDaoImpl;
	
//	@Autowired
//	private RestTemplate restTemplate;
	
	@Autowired
	private RestTemplateUtil restUtil;
	
	private static final Logger log = LoggerFactory.getLogger(AuditDaoImpl.class);

	
	@Override
	public AuditDetail createOrUpdateISM( AuditDetail auditDetail, String status,String updateMeth) {
	
		RmiAudit rmiAudit = null;

		
		rmiAudit = new RmiAudit();
		
		boolean synchData = auditDetail.isSync();

		
		rmiAudit.setAuditId(auditDetail.getAuditSeqNo());
		rmiAudit.setCompanyId(auditDetail.getCompanyId());
		rmiAudit.setAuditStatus(MasterDatas.getAuditStatusDesc(auditDetail.getAuditTypeId(), auditDetail.getAuditStatusId()));
		rmiAudit.setAuditSubType(MasterDatas.getAuditSubTypeDesc(auditDetail.getAuditTypeId(), auditDetail.getAuditSubTypeId()));
		rmiAudit.setAuditType(MasterDatas.getAuditTypeDesc(auditDetail.getAuditTypeId()));
		rmiAudit.setCreatedBy(auditDetail.getLeadAuditorName());
		rmiAudit.setCreationDate(auditDetail.getAuditDate());
		rmiAudit.setLastUpdatedBy(auditDetail.getUserInsName());
		rmiAudit.setLastUpdatedDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		rmiAudit.setVoidReasons(auditDetail.getVoidReason());
		rmiAudit.setVesselId(auditDetail.getVesselId());
		rmiAudit.setVesselName(auditDetail.getVesselName());
		log.info("asdfasdf"+rmiAudit);
		System.err.println("asfasdf"+rmiAudit);
		log.info("asfasdf"+auditDetail);
		System.err.println("asfasdf"+auditDetail);		
	
		switch (status) {

		case AppConstant.CREATE:

			if (auditDetail.getCertificateDetail().size()>0){

				if(!(auditDetail.getAuditSubTypeId() == AppConstant.INTERMEDIATE_SUB_TYPE_ID || auditDetail.getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID)){
					auditDetail.getCertificateDetail().get(0).setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
				}else{
					auditDetail.getCertificateDetail().get(0).setEndorsementID(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
				}
			}

			hibsession.getSession().save(auditDetail);
			if(auditDetail.getAuditAuditorDetail()!=null)
			{
				String table_updation = null;
				Query getTableUpdateUser = hibsession.getSession().createQuery(AppSQLConstant.GET_TABLE_UPDATION_USERS);
				getTableUpdateUser.setLong("companyId", auditDetail.getCompanyId());
				int userTableUpdate = (int) getTableUpdateUser.uniqueResult();
				Query updateMasterTableUpdate = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_MASTER_TABLE_UPDATE_USERS);
				updateMasterTableUpdate.setInteger("table_updation", userTableUpdate+1);
				updateMasterTableUpdate.setLong("companyId", auditDetail.getCompanyId());
				updateMasterTableUpdate.executeUpdate();
				
			}
			RestTemplate restTemplate = new RestTemplate();
		//	HttpHeaders headers = new HttpHeaders();
			//Added by sudharsan for INCEDENT_TICKET-671
			RmiAuditModel rmiAuditModel = setRMiAuditModel(rmiAudit);
			log.info("before sending to rmi164"+rmiAuditModel.toString());
					System.err.println("before sending to rmi164"+rmiAuditModel.toString());
			HttpEntity<RmiAuditModel> request = new HttpEntity<RmiAuditModel>(rmiAuditModel, restUtil.getHeaders());
			//HttpEntity<RmiAudit> request = new HttpEntity<RmiAudit>(rmiAudit, restUtil.getHeaders());
			//INCEDENT_TICKET-671 End here
			try{

			ResponseEntity<String> response = restTemplate.postForEntity(AppConstant.RMI_URL+"/ws1/insertAuditInfo", request , String.class );
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		case AppConstant.UPDATE:
			//Added by sudharsan and Chibi for JIRA-ID=5377&5378
			if (auditDetail.getAuditStatusId()==AppConstant.VOID_AUDIT_STATUS){
				if(auditDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID){
					certificateIhmController.updateCertificateStatus(auditDetail.getAuditSeqNo(),auditDetail.getCompanyId(),auditDetail.getAuditSubTypeId());
				}
				// condition added by cb for updating the certificate to inactive in the RMI for SMC when audit is made void TICKET-552//
				else if((auditDetail.getAuditTypeId()==AppConstant.ISM_TYPE_ID||auditDetail.getAuditTypeId()==AppConstant.ISPS_TYPE_Id||auditDetail.getAuditTypeId()==AppConstant.MLC_TYPE_ID)
						&&(auditDetail.getAuditSubTypeId()==AppConstant.INTERIM_SUB_TYPE_ID||auditDetail.getAuditSubTypeId()==AppConstant.INITIAL_SUB_TYPE_ID||auditDetail.getAuditSubTypeId()==AppConstant.RENEWAL_SUB_TYPE_ID||auditDetail.getAuditSubTypeId()==AppConstant.INTERMEDIATE_SUB_TYPE_ID||auditDetail.getAuditSubTypeId()==AppConstant.ADDITIONAL_SUB_TYPE_ID)){
					certificateDaoImpl.updateCertificateStatusSMC(auditDetail.getAuditSeqNo(), auditDetail.getCompanyId(), auditDetail.getAuditSubTypeId());
				}
			}
			//End here
			
		
			/* updating data to rmi  */
			if(auditDetail.getAuditAuditorDetail()!=null && auditDetail.getAuditStatusId()!=null && (auditDetail.getAuditStatusId()==AppConstant.COMMENCED_AUDIT_STATUS || auditDetail.getAuditStatusId()==AppConstant.COMPLETED_AUDIT_STATUS) ) {
				AuditDetail auditDetails = auditDetail;
				
				if(updateMeth.equals("Sync")){
					Criteria cri = hibsession.getSession().createCriteria(AuditDetail.class)
							.add(Restrictions.eq("auditSeqNo",auditDetails.getAuditSeqNo()))
							.add(Restrictions.eq("companyId", auditDetail.getCompanyId()));
					List<AuditDetail> audDet = cri.list();
					
					System.out.println(audDet);
					
					audDet.forEach(audAction -> {
						auditDetails.setVesselNameAud(audAction.getVesselNameAud());
						auditDetails.setVesselTypeAud(audAction.getVesselTypeAud());
						auditDetails.setOfficialNoAud(audAction.getOfficialNoAud());
						auditDetails.setDocTypeNoAud(audAction.getDocTypeNoAud());
						auditDetails.setDocIssuerAud(audAction.getDocIssuerAud());
						auditDetails.setDocExpiryAud(audAction.getDocExpiryAud());
						auditDetails.setCompanyAddressAud(audAction.getCompanyAddressAud());
					});
				}
				
				auditDetails.setUserInsName(auditDetail.getUserInsName());
			    auditDetail.getAuditAuditorDetail().forEach(action-> { 
				if(action.getAudSignature()!=null && action.getAuditRoleID()==AppConstant.AUDIT_AUDITOR_ROLE_ID){
				
					RmiAudit audit = new RmiAudit();
					
					RestTemplate UpdateRmi = new RestTemplate();
//					//HttpHeaders headersUpdate = new HttpHeaders();
					
					audit.setAuditStatus(MasterDatas.getAuditStatusDesc(auditDetails.getAuditTypeId(), auditDetails.getAuditStatusId()));
					audit.setCreationDate(auditDetails.getAuditDate());

					audit.setLastUpdatedDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
					audit.setAuditId(auditDetails.getAuditSeqNo());
					audit.setAuditType(MasterDatas.getAuditTypeDesc(auditDetails.getAuditTypeId()));
					audit.setVoidReasons("");
					
					try{
						//Added by sudharsan for INCEDENT_TICKET-671 Start here
						RmiAuditModel rmiAuditModel_audit = setRMiAuditModel(audit);
						log.info("before sending to rmi230"+rmiAuditModel_audit.toString());
						System.err.println("before sending to rmi230"+rmiAuditModel_audit.toString());
						HttpEntity<RmiAuditModel> requestUpdate = new HttpEntity<RmiAuditModel>(rmiAuditModel_audit, restUtil.getHeaders());
						//HttpEntity<RmiAudit> requestUpdate = new HttpEntity<RmiAudit>(audit, restUtil.getHeaders());
						ResponseEntity<String> response = UpdateRmi.postForEntity(AppConstant.RMI_URL+"/ws1/updateAuditInfo", requestUpdate , String.class );
						//INCEDENT_TICKET-671 End here
					}catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					
				}
			});
		}
			
			/* update RMI DATA fron mananger or Admin  auditDetail.getRoleId() */
			if( auditDetail.getAuditAuditorDetail()!=null && auditDetail.getAuditStatusId()!=null &&  ( auditDetail.getAuditStatusId()==AppConstant.VOID_AUDIT_STATUS || auditDetail.getAuditStatusId()==AppConstant.REOPEN_AUDIT_STATUS ) ){
				
				RmiAudit audit = new RmiAudit();
				
				audit.setAuditStatus(MasterDatas.getAuditStatusDesc(auditDetail.getAuditTypeId(), auditDetail.getAuditStatusId()));
				audit.setCreationDate(auditDetail.getAuditDate());

				audit.setLastUpdatedDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				audit.setAuditId(auditDetail.getAuditSeqNo());
				
				audit.setVoidReasons(auditDetail.getVoidReason());
				
				auditDetail.getAuditAuditorDetail().forEach(action-> {
					if(action.getAuditRoleID()==AppConstant.AUDIT_AUDITOR_ROLE_ID){
												
						RestTemplate restTemplate1 = new RestTemplate();
						//HttpHeaders headers1 = new HttpHeaders();
						
						RmiAuditModel rmiAuditModel1 = setRMiAuditModel(audit);
						//Added by sudharsan for INCEDENT_TICKET-671 Start here
						log.info("before sending to rmi266"+rmiAuditModel1.toString());
						System.err.println("before sending to rmi266"+rmiAuditModel1.toString());
						HttpEntity<RmiAuditModel> request1 = new HttpEntity<RmiAuditModel>(rmiAuditModel1, restUtil.getHeaders());
						//HttpEntity<RmiAudit> request1 = new HttpEntity<RmiAudit>(audit, restUtil.getHeaders());
						//INCEDENT_TICKET-671 End here
						try{

						ResponseEntity<String> response = restTemplate1.postForEntity(AppConstant.RMI_URL+"/ws1/updateAuditInfo", request1 , String.class );
						}catch (Exception e) {
							e.printStackTrace();
							// TODO: handle exception
						}
					}
				});
				
				
			}
			
			 
			
			if(auditDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID  &&  auditDetail.getIhmReasonCertificate()!=null && auditDetail.getIhmReasonCertificate()==1){
				
				Query hmReasonCertificate = hibsession.getSession().createQuery(AppSQLConstant.hmReasonCertificateActive);
				hmReasonCertificate.setInteger("activeStatus",1 );
				hmReasonCertificate.setLong("companyId", auditDetail.getCompanyId());
				hmReasonCertificate.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
				hmReasonCertificate.setLong("companyId1", auditDetail.getCompanyId());
				hmReasonCertificate.setInteger("auditSeqNo1", auditDetail.getAuditSeqNo());
				hmReasonCertificate.executeUpdate();
			}else if(auditDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID  && auditDetail.getIhmReasonCertificate()!=null && auditDetail.getIhmReasonCertificate()==0){
				
				Query hmReasonCertificate = hibsession.getSession().createQuery(AppSQLConstant.hmReasonCertificateInActive);
				hmReasonCertificate.setInteger("activeStatus",0 );
				hmReasonCertificate.setLong("companyId", auditDetail.getCompanyId());
				hmReasonCertificate.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
				hmReasonCertificate.executeUpdate();
				
			}
			
			
			if(auditDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID && auditDetail.getIhmReasonCertificate()==null && auditDetail.getAuditSummaryId()!= null && auditDetail.getAuditSummaryId() == AppConstant.NOT_APPROVED_SUMMARY){
				Query hmReasonCertificate = hibsession.getSession().createQuery(AppSQLConstant.hmReasonCertificateInActive);
				hmReasonCertificate.setInteger("activeStatus",0 );	
				hmReasonCertificate.setLong("companyId", auditDetail.getCompanyId());
				hmReasonCertificate.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
				hmReasonCertificate.executeUpdate();
			}else{
				if(auditDetail.getMaxSeqNo() == null){
					// added for MLC inactive certificate production issue 
					if(auditDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID) {
					Query hmReasonCertificate = hibsession.getSession().createQuery(AppSQLConstant.hmReasonCertificateInActive);
					hmReasonCertificate.setInteger("activeStatus",1 );	
					hmReasonCertificate.setLong("companyId", auditDetail.getCompanyId());
					hmReasonCertificate.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
					hmReasonCertificate.executeUpdate();
					}
				}else{
					/** After generating approval letter making inactive for certificate*/
					Query makeInactiveToRest = null;
					makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL);
					makeInactiveToRest.setInteger("activeStatus", AppConstant.INACTIVE_STATUS);
					makeInactiveToRest.setLong("companyId", auditDetail.getCompanyId());
					makeInactiveToRest.setInteger("auditTypeId", auditDetail.getAuditTypeId());
					makeInactiveToRest.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
					makeInactiveToRest.setInteger("seqNo",auditDetail.getMaxSeqNo());
					makeInactiveToRest.executeUpdate();
				}
			}
			
			auditDetail = (AuditDetail) hibsession.getSession().merge(auditDetail);	
			

		}


		hibsession.getSession().saveOrUpdate(rmiAudit);

		

		log.info("auditDetailSync ="+synchData);

		

		if(synchData == false) {
			/*auditDetail.setSync(true);
			
			createBlob(auditDetail,1001);*/
			
			saveUptoReviewSignature(auditDetail.getAuditSeqNo(),auditDetail.getAuditTypeId(),auditDetail.getCompanyId());
					
		} else {
			// only update at sync time
			if(auditDetail.getCertificateDetail()!=null && auditDetail.getCertificateDetail().size()>0){

				for(CertificateDetail certificateDetail  : auditDetail.getCertificateDetail()) {
					
					String Cno=certificateDetail.getCertificateNo();
			        String Utn=certificateDetail.getUtn();
			        String QidEncode="";
			        try {
						String addCnoUtn=Cno+" "+Utn;
						Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");			
						String s= (String) q.uniqueResult();
						QidEncode =s;
					} 
					catch (Throwable e)
					{
						e.printStackTrace();
					}
			      
			        certificateDetail.setQid(QidEncode);
			        certificateDetail.setQrCodeUrl(AppConstant.cert_url+QidEncode);
				}

			}
		}
		
		/*else{
			blobDataforOrg(auditDetail.getAuditSeqNo(),auditDetail.getAuditTypeId(),auditDetail.getCompanyId());	
		}*/
		
		//blobDataforOrg(auditDetail.getAuditSeqNo(),auditDetail.getAuditTypeId(),auditDetail.getCompanyId());

		/*if(auditDetail.getGrt()!=null){

			Query updateVslGrt = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_VESSEL_GRT_FROM_CERTIFICATE_DTL);

			updateVslGrt.setString("grt", auditDetail.getGrt());
			updateVslGrt.setInteger("vesselImoNo", auditDetail.getVesselImoNo());
			updateVslGrt.setLong("companyId", auditDetail.getCompanyId());

			updateVslGrt.executeUpdate();
		}*/

		return auditDetail;
	}



	@Override
	public ReportData saveReportHistoryData(ReportData reportData) {



		hibsession.getSession().saveOrUpdate(reportData);

		//reportData = (ReportData) hibsession.getSession().merge(reportData);
		return reportData;
	}



	@Override
	public AuditDetail getAuditDetail(int auditTypeId, int auditSeqNo, Long companyId) {
		AuditDetail auditDetail = null;//new AuditDetail();
		Criteria calcCriteria = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo));
		auditDetail = (AuditDetail) calcCriteria.uniqueResult();

//		if (!auditDetail.getCertificateDetail().isEmpty()) {
//			
//			for(int j=0;j<auditDetail.getCertificateDetail().size();j++){
//				auditDetail.getCertificateDetail().get(j).setSeal(null);
//				auditDetail.getCertificateDetail().get(j).setIssuerSign(null);
//			}
//		}
		return auditDetail;
	}
	
	@Override
	public AuditDetailIhm getAuditDetailForIhm(int auditTypeId, int auditSeqNo, Long companyId) {
		AuditDetailIhm auditDetail = null;//new AuditDetail();
		Criteria calcCriteria = hibsession.getSession().createCriteria(AuditDetailIhm.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo));
		auditDetail = (AuditDetailIhm) calcCriteria.uniqueResult();
		
		
			
//		if (!auditDetail.getCertificateDetail().isEmpty()) {
//			for(int j=0;j<auditDetail.getCertificateDetail().size();j++){
//				auditDetail.getCertificateDetail().get(j).setSeal(null);
//				auditDetail.getCertificateDetail().get(j).setIssuerSign(null);
//			}
//		}
		
		
		return auditDetail;
	}
	
	@Override
	
	public List<AuditDetailIhm> getAllIhmAuditDetail(int auditTypeId, int imoNo, Long companyId) {
		List<AuditDetailIhm> auditDetail = null;//new AuditDetail();
		Criteria calcCriteria = hibsession.getSession().createCriteria(AuditDetailIhm.class);
		
		Criterion rest1 = Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY);
		
		Criterion rest2 = Restrictions.eq("auditStatusId",AppConstant.VOID_AUDIT_STATUS);
		
		//calcCriteria.add(Restrictions.or(,rest2));
		
		calcCriteria.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId));
		
		calcCriteria.add(Restrictions.not(Restrictions.or(rest1,rest2)));
		//calcCriteria.add(Restrictions.not(Restrictions.in("auditSummaryId", new Object[]{AppConstant.NOT_APPROVED_SUMMARY,AppConstant.VOID_AUDIT_STATUS})));
		calcCriteria.add(Restrictions.eq("vesselImoNo", imoNo)).addOrder(Order.desc("auditSeqNo"));
		//calcCriteria.createCriteria("certificateDetail").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		auditDetail = (List<AuditDetailIhm>) calcCriteria.list();
		log.info("reacbbkbkjb");
		return auditDetail;
	}
	
	@Override
	public List<AuditDetail> getSspReviewDetail(int imo,int auditTypeId, Long companyId) {
	
		Criteria cr = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("vesselImoNo", imo))
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId));
				
		List<AuditDetail> list = cr.list();

		return list;
	}

	@Override
	public AuditFindingModel auditFindings(AuditFindingModel auditFinding, String status) {
		//added by @Ramya for Jira id - IRI-5633 -- START
		if(status.equals("Create")){
		Query auditFindingData = hibsession.getSession().createSQLQuery(AppSQLConstant.INSERT_FINDINGS);
		auditFindingData.setParameter("findingSeqNo", auditFinding.getFindingSeqNo());
		auditFindingData.setParameter("auditDate", auditFinding.getAuditDate());
		auditFindingData.setParameter("auditCode", auditFinding.getAuditCode());
		auditFindingData.setParameter("companyId", auditFinding.getCompanyId());
		auditFindingData.setParameter("userIns", auditFinding.getUserIns());
//		auditFindingData.setParameter("dateIns", auditFinding.getDateIns());
		auditFindingData.setParameter("auditSeqNo", auditFinding.getAuditSeqNo());
		auditFindingData.setParameter("auditTypeId", auditFinding.getAuditTypeId());
		auditFindingData.setParameter("findingStatus", auditFinding.getFindingStatus());
		auditFindingData.setParameter("serialNo",auditFinding.getSerialNo());
		auditFindingData.setParameter("auditStatus", 0);
		auditFindingData.executeUpdate();
		
		if(auditFinding.getFindingDetailModel()!=null){
			
			for(FindingDetailModel data: auditFinding.getFindingDetailModel()){
				
				Query findingDetailData = hibsession.getSession().createSQLQuery(AppSQLConstant.INSERT_FINDING_DETAILS);
				findingDetailData.setParameter("statusSeqNo",data.getStatusSeqNo());
				findingDetailData.setParameter("currentAuditSeq",data.getCurrentAuditSeq());
				findingDetailData.setParameter("origAuditSeqNo",data.getOrigAuditSeqNo());
				findingDetailData.setParameter("findingSeqNo",data.getFindingSeqNo());
				findingDetailData.setParameter("findingCategoryId",data.getCategoryId());
				findingDetailData.setParameter("statusId",data.getStatusId());
				findingDetailData.setParameter("statusDate",data.getStatusDate());
				findingDetailData.setParameter("nextActionId",data.getNextActionId());
				findingDetailData.setParameter("dueDate",data.getDueDate());
				findingDetailData.setParameter("description",data.getDescriptions());
				findingDetailData.setParameter("companyId",data.getCompanyId());
				findingDetailData.setParameter("userIns",data.getUserIns());
//				findingDetailData.setParameter("dateIns",data.getDateIns());
				findingDetailData.setParameter("auditTypeId",data.getAuditTypeId());
				findingDetailData.setParameter("updateDescription",data.getUpdateDescription());
				findingDetailData.setParameter("auditPlace",data.getAuditPlace());
				findingDetailData.executeUpdate();
				
				if(data.getFindingRptAttachs()!=null){
					Query findingRptAttachData = hibsession.getSession().createSQLQuery(AppSQLConstant.INSERT_FINDING_RPT_DETAILS);
					for(FindingRptAttachModel rptData: data.getFindingRptAttachs()){
					findingRptAttachData.setParameter("currentAuditSeq", rptData.getCurrentAuditSeq());
					findingRptAttachData.setParameter("origAuditSeqNo", rptData.getOrigAuditSeqNo());
					findingRptAttachData.setParameter("findingSeqNo", rptData.getFindingSeqNo());
					findingRptAttachData.setParameter("fileSeqNo", rptData.getFileSeqNo());
					findingRptAttachData.setParameter("fileName", rptData.getFileName());
					findingRptAttachData.setParameter("flag", rptData.getOwnerFlag());
					findingRptAttachData.setParameter("companyId", rptData.getCompanyId());
					findingRptAttachData.setParameter("userIns", rptData.getUserIns());
//					findingRptAttachData.setParameter("dateIns", rptData.getDateIns());
					findingRptAttachData.setParameter("statusSeqNo", rptData.getStatusSeqNo());
					findingRptAttachData.setParameter("auditTypeId", rptData.getAuditTypeId());
					findingRptAttachData.executeUpdate();
				}
				}
			}
		}
		}
		else if(status.equals("Update")){
		Query auditFindingData = hibsession.getSession().createSQLQuery(AppSQLConstant.UPDATE_FINDINGS);

			auditFindingData.setParameter("auditDate", auditFinding.getAuditDate());
			auditFindingData.setParameter("auditCode", auditFinding.getAuditCode());
			auditFindingData.setParameter("companyId", auditFinding.getCompanyId());
			auditFindingData.setParameter("userIns", auditFinding.getUserIns());
//			auditFindingData.setParameter("dateIns", auditFinding.getDateIns());
			auditFindingData.setParameter("auditTypeId", auditFinding.getAuditTypeId());
			auditFindingData.setParameter("findingStatus", auditFinding.getFindingStatus());
			auditFindingData.setParameter("serialNo",auditFinding.getSerialNo());
			auditFindingData.setParameter("auditStatus", auditFinding.getAuditStatus()!=null?auditFinding.getAuditStatus():0);
			auditFindingData.setParameter("auditSeqNo", auditFinding.getAuditSeqNo());
			auditFindingData.setParameter("findingSeqNo", auditFinding.getFindingSeqNo());
			
			auditFindingData.executeUpdate();
			
			if(auditFinding.getFindingDetailModel()!=null){
				
				for(FindingDetailModel data: auditFinding.getFindingDetailModel()){
					Query findingDetailDataExists = hibsession.getSession().createSQLQuery(AppSQLConstant.FINDING_DETAIL_EXISTS);
					findingDetailDataExists.setParameter("statusSeqNo",data.getStatusSeqNo());
					findingDetailDataExists.setParameter("findingSeqNo",data.getFindingSeqNo());
					findingDetailDataExists.setParameter("origAuditSeqNo",data.getOrigAuditSeqNo());
					System.out.println("rresudet"+findingDetailDataExists.uniqueResult());
					int cc =Integer.parseInt(findingDetailDataExists.uniqueResult().toString());
					System.out.println("CCCdet "+cc);
					if(cc!=0){
					Query findingDetailData = hibsession.getSession().createSQLQuery(AppSQLConstant.UPDATE_FINDING_DETAILS);

					findingDetailData.setParameter("currentAuditSeq",data.getCurrentAuditSeq());

					findingDetailData.setParameter("findingCategoryId",data.getCategoryId());
					findingDetailData.setParameter("statusId",data.getStatusId());
					findingDetailData.setParameter("statusDate",data.getStatusDate());
					findingDetailData.setParameter("nextActionId",data.getNextActionId());
					findingDetailData.setParameter("dueDate",data.getDueDate());
					findingDetailData.setParameter("description",data.getDescriptions());
					findingDetailData.setParameter("companyId",data.getCompanyId());
					findingDetailData.setParameter("userIns",data.getUserIns());
//					findingDetailData.setParameter("dateIns",data.getDateIns());
					findingDetailData.setParameter("auditTypeId",data.getAuditTypeId());
					findingDetailData.setParameter("updateDescription",data.getUpdateDescription());
					findingDetailData.setParameter("auditPlace",data.getAuditPlace());
					
					findingDetailData.setParameter("statusSeqNo",data.getStatusSeqNo());
					findingDetailData.setParameter("findingSeqNo",data.getFindingSeqNo());
					findingDetailData.setParameter("origAuditSeqNo",data.getOrigAuditSeqNo());

					findingDetailData.executeUpdate();
					
					if(data.getFindingRptAttachs()!=null){
						for(FindingRptAttachModel rptData: data.getFindingRptAttachs()){
							
							Query findingRptAttachDataExists = hibsession.getSession().createSQLQuery(AppSQLConstant.FINDING_RPT_ATTACHEMENT_EXISTS);
							findingRptAttachDataExists.setParameter("origAuditSeqNo", rptData.getOrigAuditSeqNo());
							findingRptAttachDataExists.setParameter("findingSeqNo", rptData.getFindingSeqNo());
							findingRptAttachDataExists.setParameter("fileSeqNo", rptData.getFileSeqNo());
							findingRptAttachDataExists.setParameter("statusSeqNo", rptData.getStatusSeqNo());
							System.out.println("rresurpt"+findingRptAttachDataExists.uniqueResult());
							int ccc =Integer.parseInt(findingRptAttachDataExists.uniqueResult().toString());
							System.out.println("CCCrpt "+ccc);
							
							if(ccc!=0){
								Query findingRptAttachData = hibsession.getSession().createSQLQuery(AppSQLConstant.UPDATE_FINDING_RPT_DETAILS);
							
						findingRptAttachData.setParameter("currentAuditSeq", rptData.getCurrentAuditSeq());
						findingRptAttachData.setParameter("flag", rptData.getOwnerFlag());
						findingRptAttachData.setParameter("companyId", rptData.getCompanyId());
						findingRptAttachData.setParameter("userIns", rptData.getUserIns());
//						findingRptAttachData.setParameter("dateIns", rptData.getDateIns());
						findingRptAttachData.setParameter("auditTypeId", rptData.getAuditTypeId());
						
						findingRptAttachData.setParameter("origAuditSeqNo", rptData.getOrigAuditSeqNo());
						findingRptAttachData.setParameter("findingSeqNo", rptData.getFindingSeqNo());
						findingRptAttachData.setParameter("fileSeqNo", rptData.getFileSeqNo());
						findingRptAttachData.setParameter("statusSeqNo", rptData.getStatusSeqNo());
						findingRptAttachData.setParameter("fileName", rptData.getFileName());
						findingRptAttachData.executeUpdate();
							}
							else{
								Query findingRptAttachData = hibsession.getSession().createSQLQuery(AppSQLConstant.INSERT_FINDING_RPT_DETAILS);

								findingRptAttachData.setParameter("currentAuditSeq", rptData.getCurrentAuditSeq());
								findingRptAttachData.setParameter("origAuditSeqNo", rptData.getOrigAuditSeqNo());
								findingRptAttachData.setParameter("findingSeqNo", rptData.getFindingSeqNo());
								findingRptAttachData.setParameter("fileSeqNo", rptData.getFileSeqNo());
								findingRptAttachData.setParameter("fileName", rptData.getFileName());
								findingRptAttachData.setParameter("flag", rptData.getOwnerFlag());
								findingRptAttachData.setParameter("companyId", rptData.getCompanyId());
								findingRptAttachData.setParameter("userIns", rptData.getUserIns());
//								findingRptAttachData.setParameter("dateIns", rptData.getDateIns());
								findingRptAttachData.setParameter("statusSeqNo", rptData.getStatusSeqNo());
								findingRptAttachData.setParameter("auditTypeId", rptData.getAuditTypeId());
								findingRptAttachData.executeUpdate();

							}
					}
					}
				}else{
					Query insFindingDetailData = hibsession.getSession().createSQLQuery(AppSQLConstant.INSERT_FINDING_DETAILS);
					insFindingDetailData.setParameter("statusSeqNo",data.getStatusSeqNo());
					insFindingDetailData.setParameter("currentAuditSeq",data.getCurrentAuditSeq());
					insFindingDetailData.setParameter("origAuditSeqNo",data.getOrigAuditSeqNo());
					insFindingDetailData.setParameter("findingSeqNo",data.getFindingSeqNo());
					insFindingDetailData.setParameter("findingCategoryId",data.getCategoryId());
					insFindingDetailData.setParameter("statusId",data.getStatusId());
					insFindingDetailData.setParameter("statusDate",data.getStatusDate());
					insFindingDetailData.setParameter("nextActionId",data.getNextActionId());
					insFindingDetailData.setParameter("dueDate",data.getDueDate());
					insFindingDetailData.setParameter("description",data.getDescriptions());
					insFindingDetailData.setParameter("companyId",data.getCompanyId());
					insFindingDetailData.setParameter("userIns",data.getUserIns());
//					insFindingDetailData.setParameter("dateIns",data.getDateIns());
					insFindingDetailData.setParameter("auditTypeId",data.getAuditTypeId());
					insFindingDetailData.setParameter("updateDescription",data.getUpdateDescription());
					insFindingDetailData.setParameter("auditPlace",data.getAuditPlace());
					insFindingDetailData.executeUpdate();
				}
				}
				}
		}
		
		//added by @Ramya for Jira id - IRI-5633 - END
		return auditFinding;
	}

	@Override
	public FindingRptAttach findingRptAttchment(FindingRptAttach findingRptAttach) {

		hibsession.getSession().saveOrUpdate(findingRptAttach);

		return findingRptAttach;
	}

	@Override
	public List<AuditFinding> getPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq, String companyImoNo, String companyDoc){
		List<AuditFinding> auditFindings = new ArrayList<>();

		log.info("function getPreviousFinding auditDate ="+auditDate);
		
		Criteria criteria = hibsession.getSession().createCriteria(AuditFinding.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId))

				//.add(Restrictions.eq("findingStatus", AppConstant.FINDING_STATUS))
				.add(Restrictions.eq("companyId", companyId)).add(Restrictions.le("auditDate", auditDate))
				.add(Restrictions.ne("auditSeqNo", currentAuditSeq))
				.createAlias("auditDetail", "ad", Criteria.INNER_JOIN).add(Restrictions.le("ad.auditDate", auditDate))
				.add(Restrictions.eq("ad.auditTypeId", auditTypeId)).add(Restrictions.eq("ad.companyId", companyId))
				.add(Restrictions.eq("ad.vesselImoNo", vesselIMONo)).add(Restrictions.ne("ad.auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				//.add(Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY))
				.add(Restrictions.or(Restrictions.isNull("ad.auditSummaryId"), Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)))
				.add(Restrictions.eq("ad.companyImoNo", companyImoNo))
				.add(Restrictions.eq("ad.companyDoc", companyDoc));
		auditFindings = criteria.list();

		return auditFindings;
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<AuditFinding> getPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq) {
		List<AuditFinding> auditFindings = new ArrayList<>();
		@SuppressWarnings("deprecation")
		Criteria criteria = hibsession.getSession().createCriteria(AuditFinding.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId))
				//.add(Restrictions.eq("findingStatus", AppConstant.FINDING_STATUS))
				.add(Restrictions.eq("companyId", companyId)).add(Restrictions.le("auditDate", auditDate))
				.add(Restrictions.ne("auditSeqNo", currentAuditSeq))
				.createAlias("findingDetail", "fd", Criteria.INNER_JOIN).add(Restrictions.ne("fd.categoryId", 1004))
				.add(Restrictions.eq("fd.auditTypeId", auditTypeId)).add(Restrictions.eq("fd.companyId", companyId))
 				.createAlias("auditDetail", "ad", Criteria.INNER_JOIN).add(Restrictions.le("ad.auditDate", auditDate))
				.add(Restrictions.eq("ad.auditTypeId", auditTypeId)).add(Restrictions.eq("ad.companyId", companyId))
				.add(Restrictions.eq("ad.vesselImoNo", vesselIMONo)).add(Restrictions.ne("ad.auditStatusId", AppConstant.VOID_AUDIT_STATUS)) ;
		auditFindings = criteria.list();

		return auditFindings;
	}*/

	@Override
	public List<AuditDetail> getLaptopPreviousAudit(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq) {
		List<AuditDetail> auditDetailViews = new ArrayList<AuditDetail>();
		Criteria criteria = hibsession.getSession().createCriteria(AuditDetail.class);
		if(auditDate!=null) {
			criteria.add(Restrictions.lt("auditDate", auditDate));
		}
		//criteria.add(Restrictions.lt("auditDate", auditDate));
		criteria.add(Restrictions.eq("auditTypeId", auditTypeId));
		//criteria.add(Restrictions.in("auditStatusId", new Object[]{AppConstant.COMPLETED_AUDIT_STATUS,AppConstant.CLOSED_AUDIT_STATUS}));
		criteria.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS));
		criteria.add(Restrictions.or(Restrictions.isNull("auditSummaryId"), Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)));
		//criteria.add(Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY));
		criteria.add(Restrictions.eq("companyId", companyId));
		criteria.add(Restrictions.eq("vesselImoNo", vesselIMONo));
		auditDetailViews = criteria.list();

		return auditDetailViews;
	}

	@Override
	public List<AuditFinding> getLaptopPreviousFinding(int auditTypeId, Date auditDate, int vesselIMONo, long companyId,
			int currentAuditSeq) {
		
		List<AuditFinding> auditFindings = new ArrayList<>();

		log.info("function getPreviousFinding auditDate ="+auditDate);
		
		Criteria criteria = hibsession.getSession().createCriteria(AuditFinding.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId))

				//.add(Restrictions.eq("findingStatus", AppConstant.FINDING_STATUS))
				.add(Restrictions.eq("companyId", companyId)).add(Restrictions.le("auditDate", auditDate))
				.add(Restrictions.ne("auditSeqNo", currentAuditSeq))
				.createAlias("auditDetail", "ad", Criteria.INNER_JOIN).add(Restrictions.le("ad.auditDate", auditDate))
				.add(Restrictions.eq("ad.auditTypeId", auditTypeId)).add(Restrictions.eq("ad.companyId", companyId))
				.add(Restrictions.eq("ad.vesselImoNo", vesselIMONo)).add(Restrictions.ne("ad.auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				//.add(Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY))
				.add(Restrictions.or(Restrictions.isNull("ad.auditSummaryId"), Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)));
				
		auditFindings = criteria.list();

		return auditFindings;
//		
//		List<AuditFinding> auditFindings = new ArrayList<>();
//		Criteria criteria = hibsession.getSession().createCriteria(AuditFinding.class);
//		criteria.add(Restrictions.eq("auditTypeId", auditTypeId));
//		//criteria.add(Restrictions.eq("findingStatus", AppConstant.FINDING_STATUS));
//		criteria.add(Restrictions.eq("companyId", companyId));
//		criteria.add(Restrictions.le("auditDate", auditDate));
//		criteria.add(Restrictions.ne("auditSeqNo", currentAuditSeq));
//		criteria.createAlias("auditDetail", "ad", Criteria.INNER_JOIN);
//		criteria.add(Restrictions.le("ad.auditDate", auditDate));
//		criteria.add(Restrictions.eq("ad.auditTypeId", auditTypeId));
//		criteria.add(Restrictions.eq("ad.companyId", companyId));
//		criteria.add(Restrictions.eq("ad.vesselImoNo", vesselIMONo));
//		criteria.add(Restrictions.ne("ad.auditStatusId", AppConstant.VOID_AUDIT_STATUS));
//		//criteria.add(Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY));
//		criteria.add(Restrictions.or(Restrictions.isNull("ad.auditSummaryId"), Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)));
//		auditFindings = criteria.list();
//		
//		
//		List<AuditFinding> orgauditFindings = auditFindings; 
//		
////		
////		orgauditFindings.stream().filter(obj->obj.getFindingDetail().stream().filter(fi->fi.getCurrentAuditSeq()==currentAuditSeq)).collect(Collectors.toList());
////		
////		
////		
//		
//		
//
//log.info("orgauditFindings are = "+orgauditFindings);
//
//		for(int j=0;j<auditFindings.size();j++){
//
//		if(auditFindings.get(j).getFindingStatus() == 1){
//
//		List<FindingDetail> afd = new ArrayList<>();
//
//		for(int i =0;i<auditFindings.get(j).getFindingDetail().size();i++){
//
//log.info("auditFindings.get(j).getFindingDetail() are = "+auditFindings.get(j).getFindingDetail());
//
//
//log.info("auditFindings.get(j).getFindingDetail() seq no = "+auditFindings.get(j).getFindingDetail().get(i).getCurrentAuditSeq());
//
//		if(auditFindings.get(j).getFindingDetail().get(i).getCurrentAuditSeq() == currentAuditSeq){
//            afd.add(auditFindings.get(j).getFindingDetail().get(i));
//		}else{
//		//afd.add(auditFindings.get(j).getFindingDetail().get(i));
//		}
//
//		}
//		if(afd.isEmpty()){
//		orgauditFindings.remove(j);
//		}else{
//		orgauditFindings.get(j).setFindingDetail(afd);
//		}
//		}
//		}
//
//		return orgauditFindings;
	}


	@Override
	public List<AuditFinding> currentFinding(int auditTypeId, long companyId, int currentAuditSeq) {
		List<AuditFinding> auditFindings = new ArrayList<>();
		Criteria criteria = hibsession.getSession().createCriteria(AuditFinding.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", currentAuditSeq));
		/*.createAlias("findingDetail", "fd", Criteria.INNER_JOIN)
				.add(Restrictions.le("fd.origAuditSeqNo", currentAuditSeq))
				.add(Restrictions.le("fd.currentAuditSeq", currentAuditSeq))
				.add(Restrictions.eq("fd.auditTypeId", auditTypeId)).add(Restrictions.eq("fd.companyId", companyId));*/
		auditFindings = criteria.list();

		return auditFindings;
	}

	@Override
	public Map<String, Object> getPreviousAuditDetail(int auditTypeId, int vesselIMONo, Long companyId) {

		Map<String,Object> prevAuditDtl = new HashMap<String,Object>();

		List<AuditDetailView> auditDetails = new ArrayList<AuditDetailView>();

		long initalCount = 0L, renewalCount=0L;

		Criteria criteria = hibsession.getSession().createCriteria(AuditDetailView.class)
				.add(Restrictions.eq("companyId", companyId)).add(Restrictions.eq("vesselImoNo", vesselIMONo))
				.add(Restrictions.eq("auditTypeId", auditTypeId))
				//.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("audLeadStatus", AppConstant.AUD_LEAD_STATUS));
		criteria.addOrder(Order.desc("auditDate"));
		auditDetails = criteria.list();
		//Added by sudharsan for Jira-Id 5517 start here
		for(AuditDetailView adv :auditDetails){
			adv.setIsSignAttached(adv.getLeadSign()!=null?true:false);
		}
		//Added by sudharwsan for Jira-Id 5517 end here
		criteria.setProjection(Projections.property("auditSeqNo"));

		Criteria carFindMaxStatusDate = hibsession.getSession().createCriteria(FindingDetail.class)
				.add(Restrictions.eq("currentAuditSeq", AppConstant.CAR_UPDATED_CURRENT_SEQ))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditTypeId",auditTypeId))
				.add(Restrictions.eq("vesselImoNo",vesselIMONo))
				.setProjection(Projections.max("statusDate"));

		if(criteria.list().size()>0){
			carFindMaxStatusDate.add(Restrictions.in("origAuditSeqNo", criteria.list()));
		}

		initalCount = auditDetails.stream()
				.filter( item -> item.getAuditSubTypeId() ==AppConstant.INITIAL_SUB_TYPE_ID && item.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS).count();


		renewalCount = auditDetails.stream()
				.filter( item -> item.getAuditSubTypeId()==AppConstant.RENEWAL_SUB_TYPE_ID && item.getAuditStatusId() != AppConstant.VOID_AUDIT_STATUS).count();



		prevAuditDtl.put("prevAuditDtl", auditDetails);
		prevAuditDtl.put("carFindMaxStatusDate", carFindMaxStatusDate.uniqueResult());
		prevAuditDtl.put("initalCount", initalCount);
		prevAuditDtl.put("renewalCount", renewalCount);

		return prevAuditDtl;
	}

	@Override
	public String deleteFinding(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo) {

		Query findingRptAttch = hibsession.getSession().createQuery(AppSQLConstant.DELETE_AUDIT_FINDINDG_RPT_ATTACH);
		findingRptAttch.setLong("companyId", companyId);
		findingRptAttch.setInteger("auditTypeId", auditTypeId);
		findingRptAttch.setInteger("auditSeqNo", auditSeqNo);
		findingRptAttch.setInteger("findingSeqNo", findingSeqNo);
		findingRptAttch.executeUpdate();

		findingRptAttch = hibsession.getSession().createQuery(AppSQLConstant.DELETE_AUDIT_FINDING_DETAIL);
		findingRptAttch.setLong("companyId", companyId);
		findingRptAttch.setInteger("auditTypeId", auditTypeId);
		findingRptAttch.setInteger("auditSeqNo", auditSeqNo);
		findingRptAttch.setInteger("findingSeqNo", findingSeqNo);
		findingRptAttch.executeUpdate();

		findingRptAttch = hibsession.getSession().createQuery(AppSQLConstant.DELETE_AUDIT_FINDING);
		findingRptAttch.setLong("companyId", companyId);
		findingRptAttch.setInteger("auditTypeId", auditTypeId);
		findingRptAttch.setInteger("auditSeqNo", auditSeqNo);
		findingRptAttch.setInteger("findingSeqNo", findingSeqNo);
		findingRptAttch.executeUpdate();
		return "Success";
	}

	@Override
	public void updateLockStatus(int auditTypeId, int auditSeqNo, Long companyId) {
		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_STATUS);
		updateAudit.setInteger("lockStatus", 1);
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSeqNo);
		updateAudit.executeUpdate();		
	}
	
	//	update lock holder while sync back to central
	@Override
	public void updateLockHolderSync(Integer auditTypeId, Integer auditSequenceNo, Long companyId, String lockHolder) {

		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_HOLDER);
		updateAudit.setString("lockHolder", "");
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSequenceNo);
		updateAudit.executeUpdate();

	}

	@Override
	public Map<String, String> updateLockHolder(Integer auditTypeId, Integer auditSequenceNo, Long companyId, String  lockHolder) {

		Map<String,String> map = new HashMap<String,String>();

		Criteria checkLockHolder = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSequenceNo)).setProjection(Projections.property("lockHolder"));
		List<Object> lockHolders = checkLockHolder.list();

		for(Object obj : lockHolders){

			if(obj!=null && !lockHolder.equals(" ")){
				map.put("data", "Audit Previously Locked");
				map.put("lockHolder",(String) obj );
				return map;
			}
		}

		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_HOLDER);
		if(lockHolder.equals(" ")){
			updateAudit.setString("lockHolder", "");
		}else{
			updateAudit.setString("lockHolder", lockHolder);
		}
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSequenceNo);
		updateAudit.executeUpdate();
		map.put("data", "Success");

		return map;
	}

	@Override
	public void updateLockStatus(Integer auditTypeId, Integer auditSequenceNo, Long companyId, int i) {

		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_STATUS);
		updateAudit.setInteger("lockStatus", i);
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSequenceNo);
		updateAudit.executeUpdate();

	}


	@Override
	public Map<String, String> checkAuditLockHolder(Integer auditTypeId, Integer auditSeqNo, Long companyId,
			String userId) {

		Map<String,String> map = new HashMap<String,String>();

		Criteria checkLockHolder = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo)).setProjection(Projections.property("lockHolder"));
		List<Object> lockHolders = checkLockHolder.list();

		for(Object obj : lockHolders){

			if(obj==null || obj.equals(userId) ){
				map.put("data", "Allow");
				return map;
			}else{
				map.put("data", "Audit Previously Locked");
				map.put("lockHolder",(String) obj );
				return map;
			}	
		}
		map.put("data", "Allow");
		return map;
	}


	@Override
	public Map<String, Object> checkAuditLockStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId) {
		Map<String,Object> map = new HashMap<String,Object>();

		Criteria checkLockStatus = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo)).setProjection(Projections.property("lockStatus"));
		List<Object> lockStatus = checkLockStatus.list();

		Criteria auditors = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo));

		map.put("auditors", auditors.list());

		for(Object obj : lockStatus){

			if(obj!=null){
				map.put("data", (Integer)obj);
				return map;
			}	
		}
		map.put("data", 0);
		return map;
	}

	@Override
	public Boolean unlinkFindingFiles(FindingRptAttach findingRptAttach) {


		/*FindingRptAttachCPK findingRptAttachCPK = new FindingRptAttachCPK(findingRptAttach.getFileSeqNo(), findingRptAttach.getFindingDetail());

		FindingRptAttach findingRptAttachDel = hibsession.getSession().get(FindingRptAttach.class, new FindingRptAttachCPK(findingRptAttach.getFileSeqNo(), findingRptAttach.getFindingDetail()));

		hibsession.getSession().delete(findingRptAttach);*/

		Query findingRptAttchDelete = hibsession.getSession().createQuery("delete from FindingRptAttach where companyId=:companyId and auditTypeId=:auditTypeId and origAuditSeqNo=:origAuditSeqNo and findingSeqNo=:findingSeqNo and statusSeqNo=:statusSeqNo and fileSeqNo=:fileSeqNo");
		findingRptAttchDelete.setLong("companyId", findingRptAttach.getCompanyId());
		findingRptAttchDelete.setInteger("auditTypeId", findingRptAttach.getAuditTypeId());
		findingRptAttchDelete.setInteger("origAuditSeqNo", findingRptAttach.getOrigAuditSeqNo());
		findingRptAttchDelete.setInteger("findingSeqNo", findingRptAttach.getFindingSeqNo());
		findingRptAttchDelete.setInteger("statusSeqNo", findingRptAttach.getStatusSeqNo());
		findingRptAttchDelete.setInteger("fileSeqNo", findingRptAttach.getFileSeqNo());
		findingRptAttchDelete.executeUpdate();

		/*hibsession.getSession().delete(findingRptAttachDel);*/

		return true;
	}

	@Override
	public Map<String, Object> checkLeadStatus(Integer auditSeqNo, Integer auditTypeId, Long companyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria audCri = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditTypeId",auditTypeId))
				.setProjection(Projections.property("auditStatusId"));
		
		Criteria auditorCri = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("companyId",companyId))
				.add(Restrictions.eq("auditTypeId", auditTypeId));
		/*.add(Restrictions.eq("audLeadStatus", 1))
				.setProjection(Projections.property("userId"));	*/	
		
		List<Map<String ,Object>> firstLastNames  = hibsession.getSession().createSQLQuery("select first_name,last_name,user_id from ma_users where user_id = (select lock_holder from audit_details  where audit_seq_no= "+auditSeqNo+" and audit_type_id = "+auditTypeId+"  )").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();				
		
		map.put("auditStatus", audCri.uniqueResult());
		map.put("auditors", auditorCri.list());
		map.put("firstLastNames", firstLastNames);
		return map;
	}

	@Override
	public Long updateReviewerStatus(Integer audSeqNo, Integer audType, Long companyId, Integer revStatus) {
		Query query = hibsession.getSession().createQuery("update AuditDetail set REVIEW_STATUS=:reviewStatus, AUDIT_STATUS_ID=:auditStatusId  where auditSeqNo=:auditSeqNo and auditTypeId=:auditTypeId and companyId=:companyId")
				.setParameter("reviewStatus", revStatus)
				.setParameter("auditSeqNo", audSeqNo)
				.setParameter("auditTypeId", audType)
				.setParameter("companyId", companyId);
		if(revStatus==2){
			query.setParameter("auditStatusId", 1002);
			Query audQuery = hibsession.getSession().createQuery("update AuditAuditorDetail set audSignature=:audSign where auditSeqNo=:auditSeqNo and auditTypeId=:auditTypeId and companyId=:companyId and auditRoleID=1003")
					.setParameter("auditSeqNo", audSeqNo)
					.setParameter("auditTypeId", audType)
					.setParameter("companyId", companyId);

			Criteria sign = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
					.add(Restrictions.eq("auditSeqNo",audSeqNo))
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("companyId",companyId))
					.setProjection(Projections.property("userId"))
					.setMaxResults(1);

			Criteria audCri = hibsession.getSession().createCriteria(MaUsers.class)
					.add(Restrictions.eq("sequenceNo", sign.uniqueResult()))
					.setProjection(Projections.property("signature"))
					.setMaxResults(1);			
			audQuery.setParameter("audSign", audCri.uniqueResult());
			audQuery.executeUpdate();
		}else if(revStatus==3){
			query.setParameter("auditStatusId", 1001);
		}
		query.executeUpdate();
		return 1L;
	}


	@Override
	public Map<String, Object> getSSPDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId, Integer auditSubTypeId,Integer iniRenCreated){

		Map<String, Object> sSPDetails = new HashMap<String, Object>();

		List<Object[]> l = null;

		List<AuditFinding> auditFindings = new ArrayList<>();

		Integer auditSeqNo = null;
		String auditorName =  null, auditReportNo = null;
		Date dueDate = null;

		Criteria ispsSSPDtl = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
				.setProjection(Projections.projectionList()
						.add(Projections.property("auditSeqNo"))
						.add(Projections.property("leadAuditorName"))
						.add(Projections.property("auditReportNo"))
						.add(Projections.property("certExpireDate")));

		if(auditTypeId == AppConstant.ISPS_TYPE_Id){

			ispsSSPDtl.add(Restrictions.eq("auditTypeId",AppConstant.SSP_TYPE_ID));
			ispsSSPDtl.add(Restrictions.ne("auditStatusId",AppConstant.COMMENCED_AUDIT_STATUS));

		}else if(auditTypeId == AppConstant.MLC_TYPE_ID){
			ispsSSPDtl.add(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID));
		}

		ispsSSPDtl.addOrder(Order.desc("auditSeqNo"));

		//ispsSSPDtl.setMaxResults(1);

		l = ispsSSPDtl.list();

		if(l.size()>0){
			auditSeqNo    = (Integer) l.get(0)[0];
			auditorName   = (String) l.get(0)[1];
			auditReportNo = (String) l.get(0)[2];
			dueDate       = (Date) l.get(0)[3];
		
			
				Criteria sspData = hibsession.getSession().createCriteria(SspReviewDetail.class)
						.add(Restrictions.eq("companyId", companyId))
						.add(Restrictions.eq("auditTypeId",auditTypeId))
						.add(Restrictions.eq("sspDmlcAuditSeqNo",auditSeqNo))
						.setProjection(Projections.rowCount());
				log.info(" {}",sspData.uniqueResult());
				
		}

		sSPDetails.put("auditSeqNo", auditSeqNo);
		sSPDetails.put("leadAuditorName", auditorName);
		sSPDetails.put("auditReportNo", auditReportNo);
		sSPDetails.put("amendmentCount", l.size());
		sSPDetails.put("auditFindings", auditFindings);
		sSPDetails.put("dueDate", dueDate);

		return sSPDetails;
	}

	@Override
	public Map<String, Object> getSSPRevisionNo(Integer vesselImoNo, Integer auditTypeId, Long companyId,
			Integer auditSeqNo) {

		Map<String, Object> sSPAuditCount = new HashMap<String, Object>();

		Criteria sSPDtlCnt = hibsession.getSession().createCriteria(AuditDetailView.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditTypeId",auditTypeId))
				.add(Restrictions.lt("auditSeqNo",auditSeqNo))
				.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS))
				.setProjection(Projections.rowCount());


		//Integer count = (Integer)sSPDtlCnt.uniqueResult();

		sSPAuditCount.put("count", sSPDtlCnt.uniqueResult());
		return sSPAuditCount;
	}

	@Override
	public Map<String, Object> getSspIspsData(Integer vesselImoNo, Integer auditTypeId, Long companyId,
			Integer auditSubTypeId) {

		Map<String, Object> sspIspsChk = new HashMap<String, Object>();

		if(auditSubTypeId == AppConstant.SSP_INITIAL_AUD_SUBTYPEID ){
			return null;
		}else{
			Criteria sspMaxSeq = hibsession.getSession().createCriteria(AuditDetailView.class)
					.add(Restrictions.eq("vesselImoNo", vesselImoNo))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("auditTypeId",AppConstant.SSP_TYPE_ID))
					.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
					.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS))
					.setProjection(Projections.max("auditSeqNo"));

			Criteria ispsMaxSeq = hibsession.getSession().createCriteria(AuditDetailView.class)
					.add(Restrictions.eq("vesselImoNo", vesselImoNo))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("auditTypeId",AppConstant.ISPS_TYPE_Id))
					.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
					.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS))
					.setProjection(Projections.max("auditSeqNo"));

			sspIspsChk.put("sspMaxSeq", sspMaxSeq.uniqueResult());
			sspIspsChk.put("ispsMaxSeq", ispsMaxSeq.uniqueResult());
			return sspIspsChk;
		}
	}

	@Override
	public Map<String, Object> getCountOfLockedAuditByCurrUser(String emailId, Long companyId) {

		Map<String, Object> lockCount = new HashMap<String, Object>();

		Query unlockAllLockAdt = hibsession.getSession().createQuery(AppSQLConstant.UNLOCK_ALL_LOCKED_AUDIT_OF_CURR_USER);

		unlockAllLockAdt.setString("noLockHolder", "");
		unlockAllLockAdt.setLong("companyId", companyId);
		unlockAllLockAdt.setString("currLockHolder", emailId);
		unlockAllLockAdt.setInteger("lockStatus", AppConstant.RETRIEVED_STATUS);

		int x = unlockAllLockAdt.executeUpdate();

		Criteria countLock = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("lockHolder", emailId))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
				.setProjection(Projections.rowCount()); 

		lockCount.put("lockCount",countLock.uniqueResult());

		return lockCount;
	}

	@Override
	public Map<String, Object> checkLinkedWithIspsOrMLC(Integer auditTypeId, Integer auditSeqNo,
			Long companyId) {


		Map<String, Object> checkLinked = new HashMap<String, Object>();

		Criteria checkLinkedAudit = hibsession.getSession().createCriteria(SspReviewDetail.class)
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("sspDmlcAuditSeqNo",auditSeqNo))
				.setProjection(Projections.rowCount());

		if(auditTypeId == AppConstant.SSP_TYPE_ID){

			checkLinkedAudit.add(Restrictions.eq("auditTypeId", AppConstant.ISPS_TYPE_Id));
		}else if(auditTypeId == AppConstant.DMLC_TYPE_ID){

			checkLinkedAudit.add(Restrictions.eq("auditTypeId", AppConstant.MLC_TYPE_ID));
		}

		checkLinked.put("linkedCount",checkLinkedAudit.uniqueResult());

		return checkLinked;
	}

	@Override
	public Long vesselMissingMailCall(Integer auditTypeId, Long companyId, Integer vesselImoNo, String vesselName) {
		Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL IMO_Vessel_Chk_PRC(:vesselIMO, :companyId)");
		callStoredProcedure_MYSQL.setString("vesselIMO", vesselImoNo.toString());
		callStoredProcedure_MYSQL.setInteger("companyId", companyId.intValue());
		callStoredProcedure_MYSQL.executeUpdate();

		return 1l;

	}

	@Override
	public Map<String, Object> getPrevDocDetails(String compImoNo, String compDocNo, Long companyId) {
		//Query prevDocDtl = hibsession.getSession().createQuery(AppSQLConstant.GET_PREV_DOC_DETAILS);

		Criteria prevDocDtl = hibsession.getSession().createCriteria(DOCNumberChange.class)
				.add(Restrictions.eq("companyImoNo", compImoNo))
				.add(Restrictions.eq("docTypeNo",compDocNo))
				.add(Restrictions.eq("companyId",companyId))
				.setMaxResults(1);

		prevDocDtl.addOrder(Order.desc("seqNo"));

		Map<String, Object> prevDoc = new HashMap<String, Object>();

		prevDoc.put("prevDocDtl",prevDocDtl.list());
		return prevDoc;
	}

	@Override
	public Map<String, Object> updateDocFlag(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer docFlag) {

		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_DOC_FLAG);
		updateAudit.setInteger("docFlag", docFlag);
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSeqNo);
		int result = updateAudit.executeUpdate();

		Map<String,Object> map = new HashMap<String,Object>();

		if(result == 1){
			map.put("success", true);
			return map;
		}
		map.put("success", false);
		return map;
	}

	@Override
	public void callSendMailCreateProc(AuditDetail audData) {

		Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL AUD_CREATE_MAIL_SEND_PRC(:auditSeqNo, :auditTypeId, :companyId)");
		callStoredProcedure_MYSQL.setLong("auditSeqNo", audData.getAuditSeqNo());
		callStoredProcedure_MYSQL.setLong("companyId", audData.getCompanyId());
		callStoredProcedure_MYSQL.setLong("auditTypeId", audData.getAuditTypeId());
		callStoredProcedure_MYSQL.executeUpdate();
	}

	@Override
	public List<MaVessel> tcApprovalStatus(String id,Long companyId, Integer vesselImoNo) {
		System.out.print(id);
		Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL VesselApprovalCheck(:vesselIMO,:companyId,:id)");
		callStoredProcedure_MYSQL.setString("vesselIMO", vesselImoNo.toString());
		callStoredProcedure_MYSQL.setInteger("companyId", companyId.intValue());
		callStoredProcedure_MYSQL.setString("id", id.toString());
		callStoredProcedure_MYSQL.executeUpdate();

		return null;

	}

	@Override
	public VesselApprovalDetails SavetcApprovalStatus(VesselApprovalDetails vesselApprovalDetails) {
		hibsession.getSession().update(vesselApprovalDetails);
		return vesselApprovalDetails;
	}

	@Override
	public Map<String, Object> getSignature(String emailId, Long companyId) {

		Map<String, Object> mapResult = new HashMap<String, Object>();

		Criteria audCri = hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("emailId", emailId))
				.add(Restrictions.eq("companyId", companyId));

		mapResult.put("userDetail", audCri.uniqueResult());

		return mapResult;
	}

	@Override
	public Map<String, Object> vesselDtlIncomplete(Integer vesselImoNo, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog) {
		
		Map<String, Object> mapResult = new HashMap<String, Object>();

		if(partialVesselLog.getDueDate()!=null){
			
		
		hibsession.getSession().saveOrUpdate(partialVesselLog);

		Query query = hibsession.getSession().createSQLQuery("CALL VESSEL_DETAILS_INCOMPLETE(:vesselImoNo,:vesselId,:userId,:companyId,:auditType,:dueDate)")
				.setInteger("vesselImoNo",vesselImoNo)
				.setInteger("vesselId",vesselId)
				.setString("userId",userId)
				.setLong("companyId",companyId)
				.setInteger("auditType",partialVesselLog.getAuditTypeId())
				.setDate("dueDate", partialVesselLog.getDueDate());
				

		query.executeUpdate();
		
	}
		
		List<PartialVesselLog> partialData = null;
		
		Criteria criteria = hibsession.getSession().createCriteria(PartialVesselLog.class)
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselId", vesselId))
				.add(Restrictions.eq("vesselImoNo", vesselImoNo));
		
		partialData = criteria.list();
		
		mapResult.put("result", "success");
		mapResult.put("partialData",partialData);
		
		return mapResult;
	}

	@Override
	public Map<String, Object> vesselNotExist(Integer vesselImoNo, Long companyId, String userId) {

		Map<String, Object> mapResult = new HashMap<String, Object>();

		Query query = hibsession.getSession().createSQLQuery("CALL VESSEL_DETAILS_INCOMPLETE(:vesselImoNo,:companyId,:userId)")
				.setInteger("vesselImoNo",vesselImoNo)
				.setLong("companyId",companyId)
				.setString("userId",userId);

		query.executeUpdate();

		mapResult.put("result", "success");
		return mapResult;
	}

	@Override
	public Long checkSameAudit(Integer auditTypeId, Integer vesselImoNo, Long companyId) {

		AuditDetail auditDetail = new AuditDetail();
		Criteria criteria = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselImoNo", vesselImoNo)).add(Restrictions.eq("auditStatusId", AppConstant.COMMENCED_AUDIT_STATUS))
				.add(Restrictions.eq("allowNext", 0))
				//.add(Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY));
				.add(Restrictions.or(Restrictions.isNull("auditSummaryId"), Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)));
		
		//.add(Restrictions.isNull("reviewerSign"));
		
		criteria.setProjection(Projections.rowCount());
		Long count = (Long)criteria.uniqueResult();

		return count;
	}
	@Override
	public void allAuditorSign(Integer  auditSeqNo,Integer companyId){
		Query query = hibsession.getSession().createSQLQuery("CALL LeadInitateReviewPrc (:pAudSeqNo,:pCompanyId)")
				.setInteger("pAudSeqNo",auditSeqNo)
				.setInteger("pCompanyId",companyId);
		query.executeUpdate();
	}

	@Override
	public void carUpdateRemoveAuditorsSign(Integer auditSeqNo, Long companyId, Integer auditTypeId) {

		removeAllAuditorSign(auditSeqNo,companyId,auditTypeId);
		updateAuditLockAndReviewStatus(auditSeqNo,companyId,auditTypeId);
	}

	@Override
	public void removeAllAuditorSign(Integer auditSeqNo, Long companyId, Integer auditTypeId) {

		Query removeAllAuditorSign = hibsession.getSession().createQuery(AppSQLConstant.REMOVE_ALL_AUDITOR_SIGN);

		removeAllAuditorSign.setInteger("auditSeqNo", auditSeqNo);
		removeAllAuditorSign.setLong("companyId", companyId);
		removeAllAuditorSign.setInteger("auditTypeId", auditTypeId);

		removeAllAuditorSign.executeUpdate();
	}

	@Override
	public void updateAuditLockAndReviewStatus(Integer auditSeqNo, Long companyId, Integer auditTypeId) {

		Query updateAuditLockAndReviewStatus = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_LOCK_AND_REVIEW_STATUS);

		updateAuditLockAndReviewStatus.setInteger("auditSeqNo", auditSeqNo);
		updateAuditLockAndReviewStatus.setLong("companyId", companyId);
		updateAuditLockAndReviewStatus.setInteger("auditTypeId", auditTypeId);
		updateAuditLockAndReviewStatus.setInteger("lockStatus", AppConstant.OPEN_FOR_CAR_STATUS);
		updateAuditLockAndReviewStatus.setInteger("reviewStatus", AppConstant.REVERT_REVIEW_STATUS);
		//updateAuditLockAndReviewStatus.setInteger("allowNext", AppConstant.NOTACCEPT_STATUS);

		updateAuditLockAndReviewStatus.executeUpdate();
	}

	@Override
	public Map<String, Object> updateLtrStatus(Integer auditTypeId, Integer auditSeqNo, Long companyId, Integer ltrStatus) {

		Query updatesspReview = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_LETTER_fLAG_STATUS);
		updatesspReview.setInteger("ltrStatus", ltrStatus);
		updatesspReview.setLong("companyId", companyId);
		updatesspReview.setInteger("auditTypeId", auditTypeId);
		updatesspReview.setInteger("auditSeqNo", auditSeqNo);
		int result = updatesspReview.executeUpdate();

		Map<String,Object> map = new HashMap<String,Object>();

		if(result == 1){
			map.put("success", true);
			return map;
		}
		map.put("success", false);
		return map;
	}




	@Override
	public List<Map<String ,Object>> getReportData(Integer auditTypeId, Integer auditSeqNo, Long companyId) {
		ReportData reportData = new ReportData();
		List<Map<String ,Object>> criteria;
		/* recently DISTINCT added 
		criteria = hibsession.getSession().createSQLQuery("SELECT DISTINCT A.VERSION_ID,A.AUDIT_PLACE,A.AUDIT_DATE,A.LEAD_AUDITOR_NAME,A.FILE_NAME, B.STATUS_DATE" +
				" FROM REPORT_HISTORY A, AUDIT_FINDINGS_DETAILS B where A.COMPANY_ID = B.COMPANY_ID AND A.AUDIT_TYPE_ID = B.AUDIT_TYPE_ID"+
				 " AND A.AUDIT_SEQ_NO = B.ORIG_SEQ_NO AND B.STATUS_SEQ_NO =(SELECT MAX(B.STATUS_SEQ_NO) FROM AUDIT_FINDINGS_DETAILS B WHERE "
				 + "B.COMPANY_ID = :companyId AND B.AUDIT_TYPE_ID = :auditType AND B.ORIG_SEQ_NO = :auditSeq) AND B.ORIG_SEQ_NO = :auditSeq ORDER BY A.VERSION_ID")
				.setParameter("auditType", auditTypeId)
				.setParameter("auditSeq", auditSeqNo)
				.setParameter("companyId", companyId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list(); */
		
		criteria = hibsession.getSession().createSQLQuery(" SELECT DISTINCT (A.VERSION_ID), A.AUDIT_PLACE,  A.AUDIT_DATE, A.LEAD_AUDITOR_NAME,A.FILE_NAME, B.STATUS_DATE" +
               " FROM REPORT_HISTORY A, AUDIT_FINDINGS_DETAILS B where A.COMPANY_ID = B.COMPANY_ID AND A.AUDIT_TYPE_ID = B.AUDIT_TYPE_ID" +
                "  AND A.AUDIT_SEQ_NO = B.ORIG_SEQ_NO AND B.STATUS_SEQ_NO =(SELECT MAX(C.STATUS_SEQ_NO) FROM AUDIT_FINDINGS_DETAILS C WHERE " +
                " C.COMPANY_ID =  :companyId AND C.AUDIT_TYPE_ID =  :auditType  AND C.ORIG_SEQ_NO = :auditSeq   ) AND  B.FINDING_SEQ_NO =(SELECT MAX(C.FINDING_SEQ_NO) FROM AUDIT_FINDINGS_DETAILS C WHERE" + 
                " C.COMPANY_ID =  :companyId AND C.AUDIT_TYPE_ID =  :auditType  AND C.ORIG_SEQ_NO = :auditSeq   ) AND  B.ORIG_SEQ_NO = :auditSeq ORDER BY A.VERSION_ID")
				.setParameter("auditType", auditTypeId)
				.setParameter("auditSeq", auditSeqNo)
				.setParameter("companyId", companyId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		
		if(criteria.isEmpty()){
		
			criteria = hibsession.getSession().createSQLQuery("SELECT VERSION_ID,AUDIT_PLACE,AUDIT_DATE,LEAD_AUDITOR_NAME,FILE_NAME FROM REPORT_HISTORY WHERE"
					+ " AUDIT_TYPE_ID = :auditType AND AUDIT_SEQ_NO = :auditSeq AND COMPANY_ID = :companyId")
					.setParameter("auditType", auditTypeId)
					.setParameter("auditSeq", auditSeqNo)
					.setParameter("companyId", companyId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		}
		
		/*ProjectionList projections = Projections.projectionList()
 		.add(Projections.property("report.leadAuditorName"))
		.add(Projections.property("report.versionId"))
		.add(Projections.property("report.auditPlace"))
		.add(Projections.property("report.auditDate"))
		.add(Projections.property("report.fileName"));
		criteria.setProjection(projections);
		List<ReportData> reportList =criteria.list();

		Map<String,Object> reportHistData = new HashMap<String,Object>();*/

		return  criteria;


	}


	@Override
	public  ReportData getReportBlobData(Integer versionId, Integer auditSeqNo, Long companyId) {


		ReportData  reportData =(ReportData) hibsession.getSession().createCriteria(ReportData.class)
				.add(Restrictions.eq("versionId", versionId)).add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo)).uniqueResult();

		return reportData;

	}

	@Override
	public boolean blobData(Integer currAuditseq, Long companyId) {
		List<Map<String ,Object>> auditSeqData = hibsession.getSession().createSQLQuery("SELECT DISTINCT (ORIG_SEQ_NO) ,AUDIT_TYPE_ID"
				+ " FROM audit_findings_details WHERE CUR_AUDIT_SEQ_NO = :curAuditseqNo and company_id=:companyid")				
				.setParameter("curAuditseqNo", currAuditseq)
				.setParameter("companyid", companyId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		AuditDetail audit;

		log.info("auditSeqData ="+auditSeqData);
		log.info("auditSeqData ="+auditSeqData.size());
		
		for(int i=0;i<auditSeqData.size();i++){
			log.info("auditSeqData ="+auditSeqData.get(i));
			log.info("auditSeqData ="+auditSeqData.get(i).get("ORIG_SEQ_NO"));
			log.info("auditSeqData ="+auditSeqData.get(i).get("ORIG_SEQ_NO").toString());
			blobDataforOrg(Integer.parseInt(auditSeqData.get(i).get("ORIG_SEQ_NO").toString()),Integer.parseInt(auditSeqData.get(i).get("AUDIT_TYPE_ID").toString()),companyId);
		}
		return true;
	}


	@Override
	public boolean blobDataforOrg(Integer orgAuditseq,Integer auditTypeId, Long companyId) {

		AuditDetail audit = new AuditDetail();
		
		audit = auditData(orgAuditseq, auditTypeId, companyId);
		
		log.info("auditsBefore ="+audit.getMakeFinal());
		
		
		int versionId = 0;
		if(audit.getMakeFinal() == 1)
		{
		if(audit.getAuditStatusId() == 1005){
			
			versionId = 1004;
			
		}/*else if(getCount(orgAuditseq,1010,companyId) > 0){				
			
			versionId = 1004;
		}*/else if(getCount(orgAuditseq,1007,companyId) > 0){
			
			versionId = 1003;
		}
		log.info("audit ="+audit);
		
		}else{
			
			if(audit.getLeadSign() == null){
				versionId = 1001;
				log.info("LeadSignature  ="+audit.getLeadSign());
			}
			
			
		}
		
		if(versionId > 0){
			createBlob(audit,versionId);
		}
		return true;
	}
	
	
	@Override
	public boolean signatureGenBlobData(Integer orgAuditseq,Integer auditTypeId, Long companyId, Integer versionId) {

		AuditDetail audit = new AuditDetail();
		
		audit = auditData(orgAuditseq, auditTypeId, companyId);

		createBlob(audit,versionId);
		
		return true;
	}
	

	public boolean saveUptoReviewSignature(Integer auditseq,Integer auditTypeId, Long companyId){
	
		AuditDetail audit = null;//new AuditDetail();

		audit = auditData(auditseq, auditTypeId, companyId);
		
		log.info("LeadSignature 1="+audit.getMakeFinal());
		
		if(audit.getAuditStatusId() == 1005){
			
			createBlob(audit,1004);
			
		}else if(audit.getMakeFinal() == 1)
		{
			
			Object counts = hibsession.getSession().createSQLQuery("select count(ORIG_SEQ_NO) from audit_findings_details where ORIG_SEQ_NO = :orgSeqNo and CUR_AUDIT_SEQ_NO=:currentAuditSeq and company_id=:companyid")				
			.setParameter("orgSeqNo", audit.getAuditSeqNo())
			.setParameter("currentAuditSeq", 600000)
			.setParameter("companyid", audit.getCompanyId()).uniqueResult();

			
			
			if(audit.getMakeFinal() == 1 && Integer.parseInt(counts.toString()) > 0){
				
				blobDataforOrg(audit.getAuditSeqNo(),audit.getAuditTypeId(),audit.getCompanyId());
			}
			else if(audit.getMakeFinal() == 1){

				createBlob(audit,1002);
			}
			
		}else{
			createBlob(audit,1001);
		}

		
		return true;
	}
	public AuditDetail auditData(Integer auditseq,Integer auditTypeId, Long companyId){

		AuditDetail audit = null;//new AuditDetail();
		
		audit = getAuditDetail(auditTypeId, auditseq, companyId);
		
		List<Map<String ,Object>> VesselDtls = hibsession.getSession().createSQLQuery("SELECT A.VESSEL_NAME,B.AUDIT_TYPE_DESC,A.VESSEL_TYPE,A.OFFICIAL_NO,D.DOC_ISSUER,C.VESSEL_COMPANY_ADDRESS,C.VESSEL_COMPANY_NAME,D.DOC_EXPIRY FROM MA_VESSEL A, MA_AUDIT_TYPE B, MA_VESSEL_COMPANY C,DOC_NUMBER_CHANGE D "
				+" WHERE A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID = C.COMPANY_ID AND A.COMPANY_ID = D.COMPANY_ID AND D.SEQ_NO =(SELECT MAX(E.SEQ_NO)  FROM DOC_NUMBER_CHANGE E WHERE E.COMPANY_IMO_NO = :companyImoNo AND E.DOC_TYPE_NO = :docTypeNumber AND E.COMPANY_ID= :companyId) "
				+ "AND A.VESSEL_IMO_NO=:vesselImono AND B.AUDIT_TYPE_ID=:auditTypeId AND A.COMPANY_ID =:companyId AND C.COMPANY_IMO_NO =:companyImoNo")				
				.setParameter("vesselImono", audit.getVesselImoNo())
				.setParameter("auditTypeId", audit.getAuditTypeId())
				.setParameter("companyId", audit.getCompanyId())
				.setParameter("docTypeNumber",audit.getCompanyDoc())
				.setParameter("companyImoNo", audit.getCompanyImoNo()).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		log.info("Vessel Details ="+VesselDtls);
		
		if(VesselDtls.size() > 0){
			audit.setVesselName(VesselDtls.get(0).get("VESSEL_NAME").toString());
			audit.setAudTypeDesc(VesselDtls.get(0).get("AUDIT_TYPE_DESC").toString());
			audit.setVesselTypeName(VesselDtls.get(0).get("VESSEL_TYPE").toString());
			if(VesselDtls.get(0).get("DOC_ISSUER") != null){
				audit.setDocIssuer(VesselDtls.get(0).get("DOC_ISSUER").toString());
			}else{
				audit.setDocIssuer("");
			}
			audit.setCompanyAddress(VesselDtls.get(0).get("VESSEL_COMPANY_ADDRESS").toString());
			//audit.setDocExpiry((Date)VesselDtls.get(0).get("DOC_EXPIRY"));
			audit.setCompanyName(VesselDtls.get(0).get("VESSEL_COMPANY_NAME").toString());
			audit.setOfficialNo(VesselDtls.get(0).get("OFFICIAL_NO").toString());
			
			Timestamp ts = (Timestamp)VesselDtls.get(0).get("DOC_EXPIRY");
			if(ts!=null){
				Date date = Date.valueOf(ts.toLocalDateTime().toLocalDate());
				audit.setDocExpiry(date);
			}
		}

		return audit;
	}

	public Integer getCount(Object orgSeqNo, int versionId, Long companyId){

		Object count = hibsession.getSession().createSQLQuery("select count(ORIG_SEQ_NO) from audit_findings_details where ORIG_SEQ_NO = :orgSeqNo and NEXT_ACTION_ID=:nextActionId and company_id=:companyid")				
				.setParameter("orgSeqNo", orgSeqNo)
				.setParameter("nextActionId", versionId)
				.setParameter("companyid", companyId).uniqueResult();

		return Integer.parseInt(count.toString());
	}

	public ReportData setReportData(AuditDetail audit, Integer versionId, Map<String, Object> blobData){

		ReportData reportData = new ReportData();

		reportData.setAuditSeqNo(audit.getAuditSeqNo());
		reportData.setAuditPlace(audit.getAuditPlace());
		reportData.setAuditDate(audit.getAuditDate());
		reportData.setCompanyId(audit.getCompanyId());
		reportData.setLeadAuditorName(audit.getLeadAuditorName());
		reportData.setVersionId(versionId);
		reportData.setAuditTypeId(audit.getAuditTypeId());
		
		System.out.println("audit ==56656"+ audit);
		
		
		for(int i=0; i< audit.getAuditRptAttach().size();i++){
			System.out.println("audit ==66655"+ audit.getAuditRptAttach().get(i).getFileByte());
			audit.getAuditRptAttach().get(i).setFileByte(null);
		}
		
		
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = null;
		try {
			jsonResp = mapperObj.writeValueAsString(blobData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			reportData.setReportBlob(jsonResp.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		log.info("JSON Data ="+jsonResp);
		reportData.setFileName(AppUtil.filePath(audit));

		return reportData;
	}
	
	

	@Override
	public void callDeleteUpdatedStatusOnAudVoidProc(AuditDetail audData) {
		
		Query deleteUpdatedStatusOnAudVoid = hibsession.getSession().createSQLQuery("CALL DEL_UPDTD_STS_ON_AUD_VOID(:auditSeqNo, :companyId)");
		
		deleteUpdatedStatusOnAudVoid.setInteger("auditSeqNo", audData.getAuditSeqNo());
		deleteUpdatedStatusOnAudVoid.setLong("companyId", audData.getCompanyId());
		
		deleteUpdatedStatusOnAudVoid.executeUpdate();	
	}



	@Override 
	public VesselNotification getRmiProced(VesselNotification flag,Long companyId,Integer vesselImoNo) {


	String vAdminManagers =  hibsession.getSession().createSQLQuery("SELECT LISTAGG (USER_ID, ',') WITHIN GROUP (ORDER BY USER_ID) FROM MA_USER_ROLES WHERE ROLE_ID = "+AppConstant.MANAGER_ROLE_ID+" AND COMPANY_ID = "+companyId).uniqueResult().toString();
	String v_from_to = hibsession.getSession().createSQLQuery("select get_mail_id_pkg_val('test') from dual").uniqueResult().toString();

	Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL audit_send_mail_prc(:veeselId,:companyId,:fromMail,:toMail,:ccMail,:subject,:body)");
	callStoredProcedure_MYSQL.setString("veeselId", null);
	callStoredProcedure_MYSQL.setString("fromMail",v_from_to.split(",")[0]);
	callStoredProcedure_MYSQL.setString("toMail",v_from_to.split(",")[1]+","+vAdminManagers);
	callStoredProcedure_MYSQL.setString("ccMail",vAdminManagers);
	callStoredProcedure_MYSQL.setLong("companyId", companyId);
	callStoredProcedure_MYSQL.setString("subject", "Change of Vessel Details found for IMO No.:"+vesselImoNo);
	callStoredProcedure_MYSQL.setString("body", "There is a change of Vessel Details between RMI data with the Master data stored in DB.The List of Change is given below: "

	+"\n"+((flag.getVesselName()!=null) ? "VesselName"+" "+flag.getVesselName() : "")
	+((flag.getOfficialNumber()!=null) ? "OfficialNumber"+" "+flag.getOfficialNumber()+"\n"  : "\t")
	+((flag.getCompanyImoNo()!=null) ?"CompanyImoNumber"+" "+flag.getCompanyImoNo()+"\n" : "")
	+((flag.getOfficialNumber()!=null) ?"CompanyName"+" "+flag.getVesselCompanyName()+"\n" : "")
	+((flag.getVesselCompanyName()!=null) ?"DOC Type & No."+" "+flag.getDocTypeNo()+"\n" : "")
	+((flag.getDocExpiry()!=null) ?"DOC Expiry"+" "+flag.getDocExpiry()+"\n" : "")
	+((flag.getTcApprovalStatus()!=null) ?"tc Status"+" "+flag.getTcApprovalStatus()+"\n" : "")
	+((flag.getDocIssuer()!=null) ?"DOC Issuer"+" "+flag.getDocIssuer()+"\n" : ""));
	callStoredProcedure_MYSQL.executeUpdate();


	return null;
	}
	
	public boolean createBlob(AuditDetail auditDetail, Integer flag) {

		Map<String,Object>	blobData=new HashMap<String,Object>();
		
		log.info("inside create blod ="+auditDetail);

		/*if(auditDetail.isSync() == false){*/
			List<AuditFinding>	previousFinding =  getPreviousFinding(auditDetail.getAuditTypeId(), auditDetail.getAuditDate(),auditDetail.getVesselImoNo() , auditDetail.getCompanyId(),
					auditDetail.getAuditSeqNo(), auditDetail.getCompanyImoNo(),auditDetail.getCompanyDoc());

			blobData.put("auditDtls", auditDetail);
			blobData.put("previousFinding", previousFinding);
			
			
			if(!auditDetail.getSspReviewDetail().isEmpty() && auditDetail.getSspReviewDetail().size()>0 && auditDetail.getSspReviewDetail().get(0).getSspDmlcAuditSeqNo() != null){
				log.info("sspDMLCAuditSeqNo = "+auditDetail.getSspReviewDetail().get(0).getSspDmlcAuditSeqNo());
				
				blobData.put("dmlcFinding", currentFinding(AppConstant.DMLC_TYPE_ID, auditDetail.getCompanyId() ,auditDetail.getSspReviewDetail().get(0).getSspDmlcAuditSeqNo()));
			}
			
			
		/*}else{
			blobData.put("auditDtls", auditDetail);
			log.info("blobdata = "+blobData);
		}*/
		saveReportHistoryData(setReportData(auditDetail,flag,blobData));
		
		return true;
	}



	@Override
	public Boolean getNextAdtCrtStatus(Integer auditTypeId, Long companyId, Integer auditSeqNo, Integer vesselImoNo) {
		
		AuditDetail auditDetail = new AuditDetail();
		Criteria criteria = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId)).add(Restrictions.gt("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("vesselImoNo", vesselImoNo)).add(Restrictions.eq("auditStatusId", AppConstant.COMMENCED_AUDIT_STATUS))
				.add(Restrictions.eq("allowNext", 0))
				.add(Restrictions.or(Restrictions.isNull("auditSummaryId"), Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)));
		//.add(Restrictions.isNull("reviewerSign"));

		criteria.setProjection(Projections.rowCount());
		Long count = (Long)criteria.uniqueResult();

		if(count>0l){
			return true;
		}
		
		return false;
	}



	@Override
	public void deleteFindingsSyncTime(AuditDetail auditDetail, List<AuditFinding> findings) {
		
		String deleteAuditFindings  = "delete from AuditFinding 	where auditSeqNo=:auditSeqNo and companyId=:companyId";
		String deleteFindingDetails = "delete from FindingDetail 	where origAuditSeqNo=:auditSeqNo and companyId=:companyId";
		String deleteFindingRptAtch = "delete from FindingRptAttach where origAuditSeqNo=:auditSeqNo and companyId=:companyId";
		
		StringBuilder listSeq = new StringBuilder();
		listSeq.append("(");

		if (!findings.isEmpty()) {
			
			for(AuditFinding finding : findings){
				listSeq.append(finding.getFindingSeqNo() + ",");
			}
			
			listSeq.setLength(listSeq.length()-1);
			listSeq.append(")");
			
			deleteAuditFindings = deleteAuditFindings + " and findingSeqNo not in "+listSeq;
			deleteFindingDetails = deleteFindingDetails + " and findingSeqNo not in "+listSeq;
			deleteFindingRptAtch = deleteFindingRptAtch + " and findingSeqNo not in "+listSeq;
		}
		
		Query deleteFindings = hibsession.getSession().createQuery(deleteFindingRptAtch);

		deleteFindings.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
		deleteFindings.setLong("companyId", auditDetail.getCompanyId());
		
		deleteFindings.executeUpdate();
		
		deleteFindings = hibsession.getSession().createQuery(deleteFindingDetails);

		deleteFindings.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
		deleteFindings.setLong("companyId", auditDetail.getCompanyId());
		
		deleteFindings.executeUpdate();
		
		deleteFindings = hibsession.getSession().createQuery(deleteAuditFindings);

		deleteFindings.setInteger("auditSeqNo", auditDetail.getAuditSeqNo());
		deleteFindings.setLong("companyId", auditDetail.getCompanyId());
		
		deleteFindings.executeUpdate();
	}



	@Override
	public Map<String, Object> getISPSInitialDetails(Integer vesselImoNo, Integer auditTypeId, Long companyId) {

		Map<String, Object> iSPSInitialDetails = new HashMap<String, Object>();

		Criteria ispsSSPDtl = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("auditTypeId", AppConstant.ISPS_TYPE_Id))
				.add(Restrictions.eq("auditSubTypeId", AppConstant.INITIAL_SUB_TYPE_ID));
		
		if(ispsSSPDtl.list().size()>0){
			iSPSInitialDetails.put("iSPSInitialExist", true);
			return iSPSInitialDetails;
		}
		iSPSInitialDetails.put("iSPSInitialExist", false);
		
		return iSPSInitialDetails;
	}



	@Override
	public List<Map<String, Object>> getDMLCReportNos(Integer vesselImoNo, Long companyId, Integer auditTypeId) {
		
		log.info("params - {}{}{}",vesselImoNo,companyId,auditTypeId);
		Integer reviewTypeId = null;
		if(auditTypeId==AppConstant.ISPS_TYPE_Id) {
			reviewTypeId = AppConstant.SSP_TYPE_ID;
		} else {
			reviewTypeId = AppConstant.DMLC_TYPE_ID;
		}
		log.info("reviewTypeId - {}", reviewTypeId);
		
		Map<String, Object> dmlcReportNo = null;
		List<Map<String, Object>> dmlcReportNos = new ArrayList<Map<String, Object>>();
		List<Object[]> list = null;
		
		Criteria dmlcReport = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("auditTypeId", reviewTypeId))
				//.add(Restrictions.ne("lockStatus",AppConstant.OPEN_FOR_CAR_STATUS))
				//.add(Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY))
				.add(Restrictions.or(Restrictions.isNull("auditSummaryId"), Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)))
				
				.setProjection(Projections.projectionList()
				.add(Projections.property("auditSeqNo"))
				.add(Projections.property("auditReportNo"))
				.add(Projections.property("vesselImoNo"))
				.add(Projections.property("companyId"))
				.add(Projections.property("closeMeetingDate"))
				.add(Projections.property("auditPlace"))
				.add(Projections.property("leadAuditorName"))
				.add(Projections.property("sspRevisionNo")))
				.addOrder(Order.desc("auditSeqNo"));

		list = dmlcReport.list();
		
		for(Object[] obj:list){
			
			dmlcReportNo = new HashMap<String, Object>();
			
			dmlcReportNo.put("auditSeqNo",obj[0]);
			dmlcReportNo.put("auditReportNo",obj[1]);
			dmlcReportNo.put("vesselImoNo",obj[2]);
			dmlcReportNo.put("companyId",obj[3]);
			dmlcReportNo.put("dmlcissuedate",obj[4]);
			dmlcReportNo.put("dmlcissueplace",obj[5]);
			if(obj.length>6){
				dmlcReportNo.put("leadAuditorName",obj[6]);
			}
			if(obj.length>7){
				dmlcReportNo.put("sspRevisionNo",obj[7]);
			}
			
			dmlcReportNos.add(dmlcReportNo);
			
		}
			
		return dmlcReportNos;
	}



	@Override
	public List<AuditFinding> getPreviousFinding(Integer auditTypeId, Integer vesselIMONo, Long companyId,
			String companyImoNo, String companyDoc) {

		List<AuditFinding> auditFindings = new ArrayList<>();
		Criteria criteria = hibsession.getSession().createCriteria(AuditFinding.class);
		criteria.add(Restrictions.eq("auditTypeId", auditTypeId));
		criteria.add(Restrictions.eq("findingStatus", AppConstant.FINDING_STATUS));
		criteria.add(Restrictions.eq("companyId", companyId));
		criteria.createAlias("auditDetail", "ad", Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("ad.auditTypeId", auditTypeId));
		criteria.add(Restrictions.eq("ad.companyId", companyId));
		criteria.add(Restrictions.eq("ad.vesselImoNo", vesselIMONo));
		criteria.add(Restrictions.ne("ad.auditStatusId", AppConstant.VOID_AUDIT_STATUS));
		//criteria.add(Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY));
		criteria.add(Restrictions.or(Restrictions.isNull("ad.auditSummaryId"), Restrictions.ne("ad.auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)));
		criteria.add(Restrictions.eq("ad.companyImoNo", companyImoNo));
		criteria.add(Restrictions.eq("ad.companyDoc", companyDoc));
		auditFindings = criteria.list();
		
		return auditFindings;
	}



	@Override
	public Map<String, Object> getAuditRelatedData(Integer auditSeqNo, Long companyId) {
		
		List<Object[]> list = null;
		Map<String,Object> auditDetails = null;
		
		Criteria auditData = hibsession.getSession().createCriteria(AuditDetail.class)
				
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.ne("auditSeqNo",auditSeqNo))
				
				.setProjection(Projections.projectionList()
				.add(Projections.property("auditSeqNo"))
				.add(Projections.property("companyId"))
				.add(Projections.property("allowNext"))
				.add(Projections.property("lockStatus"))
				.add(Projections.property("reviewStatus")));
				
		list = auditData.list();
		
		if(list.size()>0){
			
			auditDetails = new HashMap<String,Object>();
			Object[] obj = (Object[]) list.get(0);
			
			auditDetails.put("auditSeqNo",  obj[0]);
			auditDetails.put("companyId",   obj[1]);
			auditDetails.put("allowNext",   obj[2]);
			auditDetails.put("lockStatus",  obj[3]);
			auditDetails.put("reviewStatus",obj[4]);
		}
		
		return auditDetails;
	}



	@Override
	public Boolean dmlcFinCloserEmail(String findingSeqNos, Integer dmlcseqNo, Integer mlcseqNo, Long companyId) {
		

		Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL DMLC_CLOSE_IN_MLC_PRC(:p_dmlc_audit_seq_no, :p_mlc_audit_seq_no, :p_finding_seq_no, :p_company_id)")
				.setInteger("p_dmlc_audit_seq_no", dmlcseqNo)
				.setInteger("p_mlc_audit_seq_no", mlcseqNo)
				.setString("p_finding_seq_no", findingSeqNos)
				.setLong("p_company_id", companyId);
		   
		
		callStoredProcedure_MYSQL.executeUpdate();
		
		
		return true;
	}
	@Override
	public String getDbmsCryptoEncode(String CnoUtn) {
		Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+CnoUtn+"') from dual");
		String s= (String) q.uniqueResult();
		return s;
	}
	
	@Override
	public String deleteFindingDmlcLinked(int auditTypeId, int auditSeqNo, Long companyId, int findingSeqNo){
		
		Query findingRptAttch = hibsession.getSession().createQuery(AppSQLConstant.DELETE_AUDIT_FINDINDG_RPT_ATTACH_DMLC_LINK);
		findingRptAttch.setLong("companyId", companyId);
		findingRptAttch.setInteger("auditTypeId", auditTypeId);
		findingRptAttch.setInteger("currentAuditSeq", auditSeqNo);
		findingRptAttch.executeUpdate();

		findingRptAttch = hibsession.getSession().createQuery(AppSQLConstant.DELETE_AUDIT_FINDING_DETAIL_DMLC_LINK);
		findingRptAttch.setLong("companyId", companyId);
		findingRptAttch.setInteger("auditTypeId", auditTypeId);
		findingRptAttch.setInteger("currentAuditSeq", auditSeqNo);
		findingRptAttch.executeUpdate();
		
		return "Success";
	}



	
	
	@Override
	public Map<String, Object> vesselDtlIncompleteYatch(String vesselName, Integer vesselId, String userId, Long companyId, PartialVesselLog partialVesselLog) {
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
System.out.println("data of partila "+ partialVesselLog);
		if(partialVesselLog.getDueDate()!=null){
			
		
		hibsession.getSession().saveOrUpdate(partialVesselLog);

		/*Query query = hibsession.getSession().createSQLQuery("CALL VESSEL_DETAILS_INCOMPLETE(:vesselImoNo,:vesselId,:userId,:companyId,:auditType,:dueDate)")
				.setInteger("vesselImoNo",vesselImoNo)
				.setInteger("vesselId",vesselId)
				.setString("userId",userId)
				.setLong("companyId",companyId)
				.setInteger("auditType",partialVesselLog.getAuditTypeId())
				.setDate("dueDate", partialVesselLog.getDueDate());
				

		query.executeUpdate(); */
		
	}
		

		List<PartialVesselLog> partialData = null;
		
		Criteria criteria = hibsession.getSession().createCriteria(PartialVesselLog.class)
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselId", vesselId))
				.add(Restrictions.eq("vesselName", vesselName));
		
		partialData = criteria.list();
		
		mapResult.put("result", "success");
		mapResult.put("partialData",partialData);
		
		return mapResult;
	}
	

	@Override
	public boolean removeIhmFinalReport(Integer orgAuditseq,Integer auditTypeId, Long companyId, Integer versionId) {
		String delQuery = "DELETE FROM ReportData WHERE auditSeqNo = :auditSeqNo AND auditTypeId = :auditTypeId AND companyId = :companyId AND versionId = :versionId";
		Query deleteIhmReport = hibsession.getSession().createQuery(delQuery)
				.setParameter("auditSeqNo", orgAuditseq)
		        .setParameter("auditTypeId", auditTypeId)
		        .setParameter("companyId", companyId)
		        .setParameter("versionId", versionId);
		
		log.info("delQuery => "+delQuery);
		return deleteIhmReport.executeUpdate() == 1 ? true: false;
	}
	
	@Override
	public void stampDBCall(StampDetails pdfUploadEntity) {
		Log.info(pdfUploadEntity.toString());
		Log.info("inside stored procedure function");
		Query callStoredProcedure_stamp = hibsession.getSession().createSQLQuery("CALL UPSERT_STAMP_DETAILS( :FILE_NAME, :CREATED_BY, :CREATED_DATE, :MODIFIED_DATE,:PAGE_NO, :USER_ID,:REVIEW_REPORT_NO,:STAMP_DATE, :TOTAL_NO_PAGES,:ISSIGN,:ISSEAL,:POINTERS,:SIGNER,:SIGNER_NAME,:AUDIT_SEQ_NO)")
				.setString("FILE_NAME", pdfUploadEntity.getFileName())
				.setString("CREATED_BY", pdfUploadEntity.getEmailId())
				.setString("CREATED_DATE", pdfUploadEntity.getCreatedDate())
				.setString("MODIFIED_DATE", pdfUploadEntity.getModifiedDate())
				.setString("PAGE_NO", pdfUploadEntity.getPagenumbers())
				.setInteger("USER_ID", pdfUploadEntity.getUserId())
				.setString("STAMP_DATE", pdfUploadEntity.getStampDate())
				.setString("REVIEW_REPORT_NO", pdfUploadEntity.getReviewReportNo())
				.setInteger("TOTAL_NO_PAGES", pdfUploadEntity.getTotalNoPages())
				.setString("ISSIGN", String.valueOf(pdfUploadEntity.getSign()))
				.setString("ISSEAL", String.valueOf(pdfUploadEntity.getSeal()))
				.setString("POINTERS", String.valueOf(pdfUploadEntity.getPointers()))
				.setString("SIGNER", String.valueOf(pdfUploadEntity.getSigner()))
				.setString("SIGNER_NAME", String.valueOf(pdfUploadEntity.getSignerName()))
				.setInteger("AUDIT_SEQ_NO", pdfUploadEntity.getAuditSeqNo());
				callStoredProcedure_stamp.executeUpdate();
	}
	
	@Override
	public List<Map<String ,Object>> auditGetStamp(String REVIEW_REPORT_NO, int AUDIT_SEQ_NO) {
		
		List<Map<String ,Object>> auditSeqData = hibsession.getSession().createSQLQuery("SELECT * FROM STAMP_DETAILS WHERE REVIEW_REPORT_NO = :REVIEW_REPORT_NO and AUDIT_SEQ_NO = :AUDIT_SEQ_NO").setString("REVIEW_REPORT_NO", REVIEW_REPORT_NO).setInteger("AUDIT_SEQ_NO", AUDIT_SEQ_NO).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		return auditSeqData;
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String,Boolean>>  getFileStamp(String FILE_NAME,Integer AUDIT_SEQ_NO) {
		
		List<Map<String ,Object>> calcCriteria = hibsession.getSession().createSQLQuery("SELECT * FROM STAMP_DETAILS WHERE FILE_NAME = :FILE_NAME and AUDIT_SEQ_NO = :AUDIT_SEQ_NO").setString("FILE_NAME", FILE_NAME).setInteger("AUDIT_SEQ_NO", AUDIT_SEQ_NO).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		Integer count = calcCriteria.size();
		
		Boolean boo=false;
		
		if(count==0)
			boo = false;
		else if(count>=1)
			boo = true;
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("fileExists", boo);
		return new ResponseEntity<Map<String, Boolean>>(map,HttpStatus.OK);
	}
	
	
	@Transactional
	@Override
	public void delStampDet(String FILE_NAME, int USER_ID) {
		String stampDbDelete = "DELETE FROM StampDetails WHERE FILE_NAME=:FILE_NAME AND USER_ID=:USER_ID";
		
		Query stampDbDeleteQuery = hibsession.getSession().createQuery(stampDbDelete);
		stampDbDeleteQuery.setString("FILE_NAME", FILE_NAME);
		stampDbDeleteQuery.setInteger("USER_ID", USER_ID);
		stampDbDeleteQuery.executeUpdate();
	}
	
	@Transactional
	@Override
	public void getDownLink(String otp,Integer linkId,String userName, String fileType, String fileName, String reviewReportNo){
		// Sending Link
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
				 emailOtp.executeUpdate();
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String, Object>> getDMLCLocationDate(Integer vesselImoNo, Long companyId,Integer auditSeqNo, String status) {
		
		List<Map<String ,Object>> placenLocation  = null;
			
		if(status.contains("mCert")){	
			
			placenLocation  = hibsession.getSession().createSQLQuery("SELECT DMLCII_ISSUE_DATE,DMLCII_ISSUE_LOCATION FROM CERTIFICATE_DETAIL WHERE CERT_ORDER_NO =(SELECT max(CERT_ORDER_NO) FROM CERTIFICATE_DETAIL WHERE VESSEL_IMO_NO ="+vesselImoNo+" and AUDIT_SEQ_NO = "+auditSeqNo+" and COMPANY_ID = "+companyId+" and AUDIT_TYPE_ID =1003)").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
		}
		else if(status.contains("dmlc")){
			
			placenLocation  = hibsession.getSession().createSQLQuery("SELECT CLOSE_MEETING_DATE,AUDIT_PLACE FROM AUDIT_DETAILS WHERE AUDIT_TYPE_ID =1005 AND AUDIT_SUB_TYPE_ID=1001 AND VESSEL_IMO_NO ="+vesselImoNo+" and COMPANY_ID = "+companyId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("placeLocation", placenLocation);
		
		return new ResponseEntity<Map<String, Object>>(map,HttpStatus.OK);
		
	}
	
	@Transactional
	@Override
	public ResponseEntity<Map<String, Object>> getCompletionDate(Integer vesselImoNo, Long companyId,Integer auditTypeId,String status,Integer auditSeqNo) {
		List<Map<String ,Object>> completionDate = null;
		if(status.contains("completion")){
			completionDate  = hibsession.getSession().createSQLQuery("SELECT COMPLETION_DATE FROM CERTIFICATE_DETAIL WHERE CERT_ORDER_NO=(SELECT max(CERT_ORDER_NO) FROM CERTIFICATE_DETAIL WHERE VESSEL_IMO_NO ="+vesselImoNo+" and COMPANY_ID = "+companyId+" and AUDIT_SEQ_NO = "+auditSeqNo+" and AUDIT_TYPE_ID = "+auditTypeId+" AND (CERTIFICATE_ISSUE_ID=1008 OR CERTIFICATE_ISSUE_ID=1002))").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		}else if(status.contains("consecutive")){
			completionDate  = hibsession.getSession().createSQLQuery("SELECT COMPLETION_DATE FROM CERTIFICATE_DETAIL WHERE CERT_ORDER_NO=(SELECT max(CERT_ORDER_NO) FROM CERTIFICATE_DETAIL WHERE VESSEL_IMO_NO ="+vesselImoNo+" and COMPANY_ID = "+companyId+" and AUDIT_TYPE_ID =1002  and AUDIT_SEQ_NO = "+auditSeqNo+" AND audit_sub_type_id=1001 AND (certificate_issue_id=1008 OR certificate_issue_id=1001))").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		}else if(status.contains("extendedEndorsed")){
			completionDate  = hibsession.getSession().createSQLQuery("SELECT EXTENDED_ENDORSED_DATE FROM CERTIFICATE_DETAIL WHERE CERT_ORDER_NO=(SELECT max(CERT_ORDER_NO) FROM CERTIFICATE_DETAIL WHERE VESSEL_IMO_NO ="+vesselImoNo+" and COMPANY_ID = "+companyId+" and AUDIT_TYPE_ID =1001 and AUDIT_SEQ_NO = "+auditSeqNo+" AND audit_sub_type_id=1001 AND certificate_issue_id=1003)").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("completionDate", completionDate);
		return new ResponseEntity<Map<String, Object>>(map,HttpStatus.OK);
	}
	
	@Transactional
	@Override
	public Map<String, Object> getCertificateIHM(Integer VESSEL_IMO_NO,Integer auditSeqNo) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
				.add(Restrictions.eq("auditTypeId", AppConstant.IHM_TYPE_ID))
				.add(Restrictions.eq("vesselImoNo", VESSEL_IMO_NO))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.addOrder(Order.desc("seqNo"));

		resultMap.put("result", certDtl.list());
		return resultMap;
	}
	
	@Transactional
	@Override
	public Map<String,Object>  getCompletedStatus(Integer vesselImoNo,Integer auditTypeId) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		List result = new ArrayList();
		List<Object[]> obj = null;
		Map<String,Object> finalResult= new HashMap<String,Object>();
			Criteria certDtl = hibsession.getSession().createCriteria(AuditDetail.class)
					.add(Restrictions.eq("vesselImoNo", vesselImoNo))
					.add(Restrictions.eq("auditTypeId", auditTypeId))
					.addOrder(Order.asc("auditSeqNo"))
					.setProjection(Projections.projectionList()
							.add(Projections.property("reviewStatus"))
							.add(Projections.property("auditStatusId"))
							.add(Projections.property("auditSeqNo"))
							.add(Projections.property("allowNext"))
							.add(Projections.property("auditSummaryId")));
			
			obj = certDtl.list();
			for(int i=0;i<obj.size();i++){
					resultMap= new HashMap<String,Object>();
					resultMap.put("reviewStatus"	  , obj.get(i)[0]);
					resultMap.put("auditStatusId"   , obj.get(i)[1]);
					resultMap.put("auditSeqNo", obj.get(i)[2]);
					resultMap.put("allowNext" , obj.get(i)[3]);
					resultMap.put("auditSummaryId" , obj.get(i)[4]);
					result.add(resultMap);
			}
			finalResult.put("result", result);
			//resultMap.put("result", certDtl.list());
		
		return finalResult;
		
	}
	
	@Override
	public void updateVesselDetailsDB(List<VesselDetailsHistory> vesselDetailsHistory) {
		for (VesselDetailsHistory vessDet : vesselDetailsHistory) {
			Query callStoredProcedure_vesselUpdate = hibsession.getSession().createSQLQuery("CALL UPSERT_VESSEL_DETAILS( :AUDIT_SEQ_NO, :DATE_INS, :VESSEL_IMO_NO, :VESSEL_NAME,:VESSEL_TYPE, :GRT, :COMPANY_IMO_NO,:DATE_OF_REGISTRY, :DOC_ISSUER, :DOC_EXPIRY,:VESSEL_ADDRESS,:VESSEL_ID,:REG_OWNED_IMO_NUMBER,:KEEL_LAID_DATE,:REGISTERED_COMPANY_ADDRESS,:REGISTERED_COMPANY_NAME,:statusUpdate,:PORT_OF_REGISTRY)")
					.setInteger("AUDIT_SEQ_NO", vessDet.getAuditSeqNo())
					.setDate("DATE_INS",vessDet.getDateIns())
					.setInteger("VESSEL_IMO_NO", vessDet.getVesselImoNo())
					.setString("VESSEL_NAME", vessDet.getVesselNameAud())
					.setString("VESSEL_TYPE", vessDet.getVesselTypeAud())
					.setInteger("GRT", vessDet.getGrt())
					.setString("COMPANY_IMO_NO", vessDet.getCompanyImoNo())
					.setDate("DATE_OF_REGISTRY", vessDet.getDateOfRegistry())
					.setString("DOC_ISSUER", vessDet.getDocIssuerAud())
					.setDate("DOC_EXPIRY", vessDet.getDocExpiryAud())
					.setString("VESSEL_ADDRESS", vessDet.getVesselAdrress())
					.setInteger("VESSEL_ID", vessDet.getVesselId())
					.setInteger("REG_OWNED_IMO_NUMBER", vessDet.getRegOwnedImoNo())
					.setDate("KEEL_LAID_DATE", vessDet.getKeelLaidDate())
					.setString("REGISTERED_COMPANY_ADDRESS", vessDet.getRegisteredCompanyAddress())
					.setString("REGISTERED_COMPANY_NAME", vessDet.getRegisteredCompanyName())
					.setString("statusUpdate", vessDet.getStatusUpdate())
					.setString("PORT_OF_REGISTRY", vessDet.getPortOfRegistry());
			callStoredProcedure_vesselUpdate.executeUpdate();
		}
	}
	
	@Transactional
	@Override
	public Map<String, Object> getVesselHistory(Integer vesselOrAudi,String status) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		List<Map<String ,Object>>  vesselDet = null;
		if(status.equals("vesselImoNO")){
			//vesselDet= hibsession.getSession().createSQLQuery("SELECT DISTINCT VESSEL_NAME, DATE_INS,VESSEL_TYPE, GRT, COMPANY_IMO_NO, DATE_OF_REGISTRY, DOC_ISSUER, DOC_EXPIRY,VESSEL_ADDRESS,VESSEL_ID, REG_OWNED_IMO_NUMBER, KEEL_LAID_DATE, REGISTERED_COMPANY_ADDRESS, REGISTERED_COMPANY_NAME, PORT_OF_REGISTRY FROM (SELECT SL_NO,VESSEL_NAME, DATE_INS,VESSEL_TYPE, GRT, COMPANY_IMO_NO, DATE_OF_REGISTRY, DOC_ISSUER, DOC_EXPIRY,VESSEL_ADDRESS,VESSEL_ID, REG_OWNED_IMO_NUMBER, KEEL_LAID_DATE, REGISTERED_COMPANY_ADDRESS, REGISTERED_COMPANY_NAME, PORT_OF_REGISTRY FROM VESSEL_DETAILS_HISTORY WHERE  VESSEL_IMO_NO = :vesselOrAudi GROUP BY SL_NO,VESSEL_NAME, DATE_INS,VESSEL_TYPE, GRT, COMPANY_IMO_NO, DATE_OF_REGISTRY, DOC_ISSUER, DOC_EXPIRY,VESSEL_ADDRESS,VESSEL_ID, REG_OWNED_IMO_NUMBER, KEEL_LAID_DATE, REGISTERED_COMPANY_ADDRESS, REGISTERED_COMPANY_NAME, PORT_OF_REGISTRY ORDER BY SL_NO ASC)").setInteger("vesselOrAudi", vesselOrAudi).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			vesselDet= hibsession.getSession().createSQLQuery("SELECT * FROM VESSEL_DETAILS_HISTORY WHERE VESSEL_IMO_NO = :vesselOrAudi ORDER BY SL_NO DESC").setInteger("vesselOrAudi", vesselOrAudi).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		}
		else if(status.equals("auditSeqNo"))
			vesselDet= hibsession.getSession().createSQLQuery("SELECT * FROM VESSEL_DETAILS_HISTORY WHERE AUDIT_SEQ_NO = :vesselOrAudi ORDER BY SL_NO DESC").setInteger("vesselOrAudi", vesselOrAudi).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		resultMap.put("result", vesselDet);
		return resultMap;
	}
	
	
	@Transactional
	@Override
	public Map<String,Object> getVesselRefresh(Integer imoNumber) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		List result = new ArrayList();
		List<Object[]> obj = null;
		
		Map<String,Object> finalResult= new HashMap<String,Object>();
		Criteria calcCriteria = hibsession.getSession().createCriteria(VesselRefresh.class)
				.add(Restrictions.eq("imoNumber", imoNumber))
				.setProjection(Projections.projectionList()
						.add(Projections.property("vesselID"))
						.add(Projections.property("imoNumber"))
						.add(Projections.property("vesselName"))
						.add(Projections.property("officialNumber"))
						.add(Projections.property("vesselPK"))
						.add(Projections.property("vesselUK"))
						.add(Projections.property("callSign"))
						.add(Projections.property("grossTon"))
						.add(Projections.property("vesselType"))
						.add(Projections.property("tcApprovalStatus"))
						.add(Projections.property("homePort"))
						.add(Projections.property("registrationDate"))
						.add(Projections.property("vesselStatus"))
						.add(Projections.property("classSociety"))
						.add(Projections.property("companyIMONumber"))
						.add(Projections.property("companyStatus"))
						.add(Projections.property("docTypeNumber"))
						.add(Projections.property("issueDate"))
						.add(Projections.property("docExpiry"))
						.add(Projections.property("docType"))
						.add(Projections.property("customerName"))
						.add(Projections.property("companyAddress"))
						.add(Projections.property("docIssuer"))
						.add(Projections.property("keelLaidDate"))
						.add(Projections.property("regOwnedImoNo"))
						.add(Projections.property("operationCode"))
						.add(Projections.property("registeredCompanyAddress"))
						.add(Projections.property("registeredCompanyName")));
		
		
		obj = calcCriteria.list();
		for(int i=0;i<obj.size();i++){
			
			String issuDate="",docExpiry="",keelLaidDate="",registrationDate="";
			
			if(obj.get(i)[17] != null)
				issuDate = new SimpleDateFormat("yyyy-MM-dd").format(obj.get(i)[17]);
			
			if(obj.get(i)[18] != null)
				docExpiry = new SimpleDateFormat("yyyy-MM-dd").format(obj.get(i)[18]);
			
			if(obj.get(i)[23] != null)
				keelLaidDate = new SimpleDateFormat("yyyy-MM-dd").format(obj.get(i)[23]);
			
			if(obj.get(i)[11] != null)
				registrationDate = new SimpleDateFormat("yyyy-MM-dd").format(obj.get(i)[11]);
			
			/*Long timestamp =(long) obj.get(i)[17];
			Date issuDate = new Date(timestamp);
			
			timestamp = (long) obj.get(i)[18];
			Date docExpiry = new Date(timestamp);
			
			timestamp = (long) obj.get(i)[23];
			Date keelLaidDate = new Date(timestamp);
			
			timestamp = (long) obj.get(i)[11];
			Date registrationDate = new Date(timestamp);*/
			
				
				resultMap= new HashMap<String,Object>();
				resultMap.put("vesselID"	  , obj.get(i)[0]);
				resultMap.put("imoNumber"   , obj.get(i)[1]);
				resultMap.put("vesselName", obj.get(i)[2]);
				resultMap.put("officialNumber" , obj.get(i)[3]);
				resultMap.put("vesselPK" , obj.get(i)[4]);
				resultMap.put("vesselUK"	  , obj.get(i)[5]);
				resultMap.put("callSign"   , obj.get(i)[6]);
				resultMap.put("grossTon", obj.get(i)[7]);
				resultMap.put("vesselType" , obj.get(i)[8]);
				resultMap.put("tcApprovalStatus" , obj.get(i)[9]);
				resultMap.put("homePort"	  , obj.get(i)[10]);
				resultMap.put("registrationDate"   , registrationDate);
				resultMap.put("vesselStatus", obj.get(i)[12]);
				resultMap.put("classSociety" , obj.get(i)[13]);
				resultMap.put("companyIMONumber"   , obj.get(i)[14]);
				resultMap.put("companyStatus", obj.get(i)[15]);
				resultMap.put("docTypeNumber" , obj.get(i)[16]);
				resultMap.put("issueDate" , issuDate);
				resultMap.put("docExpiry"	  , docExpiry);
				resultMap.put("docType"   , obj.get(i)[19]);
				resultMap.put("customerName", obj.get(i)[20]);
				resultMap.put("companyAddress" , obj.get(i)[21]);
				resultMap.put("docIssuer" , obj.get(i)[22]);
				resultMap.put("keelLaidDate"	  , keelLaidDate);
				resultMap.put("regOwnedImoNo"   , obj.get(i)[24]);
				resultMap.put("operationCode" , obj.get(i)[25]);
				resultMap.put("registeredCompanyAddress" , obj.get(i)[26]);
				resultMap.put("registeredCompanyName" , obj.get(i)[27]);
				
				result.add(resultMap);
		}
		finalResult.put("result", result);
		
		return finalResult;
	
	}
	
	@Transactional
	@Override
	public Map<String, Object> updateVesselAuto(VesselRefresh vesselRefresh) {
		
		Query updateVessel = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_VESSEL_REFRESH);
		updateVessel.setInteger("imoNumber", vesselRefresh.getImoNumber());
		updateVessel.setInteger("vesselID", vesselRefresh.getVesselID());
		updateVessel.setString("vesselName", vesselRefresh.getVesselName());
		updateVessel.setLong("officialNumber", vesselRefresh.getOfficialNumber());
		updateVessel.setInteger("vesselPK", vesselRefresh.getVesselPK());
		updateVessel.setInteger("vesselUK", vesselRefresh.getVesselUK());
		updateVessel.setString("callSign", vesselRefresh.getCallSign());
		updateVessel.setInteger("grossTon", vesselRefresh.getGrossTon());
		updateVessel.setString("vesselType", vesselRefresh.getVesselType());
		updateVessel.setString("tcApprovalStatus", vesselRefresh.getTcApprovalStatus());
		updateVessel.setString("homePort", vesselRefresh.getHomePort());
		updateVessel.setDate("registrationDate", vesselRefresh.getRegistrationDate());
		updateVessel.setString("vesselStatus", vesselRefresh.getVesselStatus());
		updateVessel.setString("classSociety", vesselRefresh.getClassSociety());
		updateVessel.setString("companyIMONumber", vesselRefresh.getCompanyIMONumber());
		updateVessel.setString("companyStatus", vesselRefresh.getCompanyStatus());
		updateVessel.setString("docTypeNumber", vesselRefresh.getDocTypeNumber());
		updateVessel.setDate("issueDate", vesselRefresh.getIssueDate());
		updateVessel.setDate("docExpiry", vesselRefresh.getDocExpiry());
		updateVessel.setString("docType", vesselRefresh.getDocType());
		updateVessel.setString("customerName", vesselRefresh.getCustomerName());
		updateVessel.setString("companyAddress", vesselRefresh.getCompanyAddress());
		updateVessel.setString("docIssuer", vesselRefresh.getDocIssuer());
		updateVessel.setDate("keelLaidDate", vesselRefresh.getKeelLaidDate());
		updateVessel.setInteger("regOwnedImoNo", vesselRefresh.getRegOwnedImoNo());
		updateVessel.setString("operationCode", vesselRefresh.getOperationCode());
		updateVessel.setString("registeredCompanyAddress", vesselRefresh.getRegisteredCompanyAddress());
		updateVessel.setString("registeredCompanyName", vesselRefresh.getRegisteredCompanyName());
		
		int result = updateVessel.executeUpdate();

		Map<String,Object> map = new HashMap<String,Object>();

		if(result == 1){
			map.put("success", true);
		}else
		{
			Calendar calendar = Calendar.getInstance();
			int increment = 0;
			vesselRefresh.setId((calendar.getTimeInMillis())+(increment++));
			hibsession.getSession().saveOrUpdate(vesselRefresh);
			map.put("success", false);
		}
		hibsession.getSession().flush();
		return map;
	}
	
	@Transactional
	@Override
	public Integer getAuditType(Integer auditSeqNo) {
	
		Criteria auditData = hibsession.getSession().createCriteria(AuditDetail.class)
				
				.add(Restrictions.eq("auditSeqNo",auditSeqNo))
				
				.setProjection(Projections.projectionList()
				.add(Projections.property("auditTypeId")));
		
		Integer auditTypeId= (Integer) auditData.uniqueResult();
				
		return auditTypeId;
	}
	//Added by sudharsan for INCEDENT_TICKET-671
	public RmiAuditModel setRMiAuditModel(RmiAudit rmiAudit){
		RmiAuditModel rmiAuditModel = new RmiAuditModel();
		
		
		rmiAuditModel.setAuditId(rmiAudit.getAuditId());
		rmiAuditModel.setCompanyId(rmiAudit.getCompanyId());
		rmiAuditModel.setAuditStatus(rmiAudit.getAuditStatus());
		rmiAuditModel.setAuditSubType(rmiAudit.getAuditSubType());
		rmiAuditModel.setAuditType(rmiAudit.getAuditType());
		rmiAuditModel.setCreatedBy(rmiAudit.getCreatedBy());
		rmiAuditModel.setCreationDate(rmiAudit.getCreationDate()!=null?rmiAudit.getCreationDate().toString():null);
		rmiAuditModel.setLastUpdatedBy(rmiAudit.getLastUpdatedBy());
		rmiAuditModel.setLastUpdatedDate(rmiAudit.getLastUpdatedDate()!=null?rmiAudit.getLastUpdatedDate().toString():null);
		rmiAuditModel.setVoidReasons(rmiAudit.getVoidReasons());
		rmiAuditModel.setVesselId(rmiAudit.getVesselId());
		rmiAuditModel.setVesselName(rmiAudit.getVesselName());
		
		return rmiAuditModel;
	}
	//INCEDENT_TICKET-671 End here
	
	//added by @Ramya for Ticket-659
	@Override
	public void updateStatusId(Integer auditTypeId, Integer auditSeqNo, Long companyId, int i) {
		Query updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_STATUS_ID);
		updateAudit.setInteger("auditStatusId", i);
		updateAudit.setLong("companyId", companyId);
		updateAudit.setInteger("auditTypeId", auditTypeId);
		updateAudit.setInteger("auditSeqNo", auditSeqNo);
		updateAudit.executeUpdate();	
		
	}


	//added by @Ramya for Jira id - IRI-5633

	@Override
	public AuditFinding auditFindingsSave(AuditFinding auditFinding, String status) {
		switch (status) {
		case AppConstant.CREATE:
			/*
			hibsession.getSession().flush();*/
		/*	hibsession.getSession().clear();*/
			
			hibsession.getSession().saveOrUpdate(auditFinding);
		case AppConstant.UPDATE:
			auditFinding = (AuditFinding) hibsession.getSession().merge(auditFinding);
		}

		return auditFinding;
	}

}