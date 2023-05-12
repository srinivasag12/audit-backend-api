package com.api.central.certificate.dao;

import java.io.UnsupportedEncodingException;

import java.sql.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.BeanUtils; 
import com.api.central.audit.dao.AuditDaoImpl;
import com.api.central.audit.entity.AuditAuditorDetail;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailIhm;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAuditIhm;
import com.api.central.audit.entity.CertificateDetailsCPK;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SspReviewDetail;
import com.api.central.config.AppHibSession;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.rmiaudit.entity.RmiAudit;
import com.api.central.rmiaudit.entity.RmiAuditCertEndorsementModel;
import com.api.central.rmiaudit.entity.RmiAuditCertEndorsements;
import com.api.central.rmiaudit.entity.RmiAuditCertificate;
import com.api.central.rmiaudit.entity.RmiCertEndorsementsIhm;
import com.api.central.rmiaudit.entity.RmiCertificates;
import com.api.central.rmiaudit.entity.RmiIhmCertificates;
import com.api.central.security.EncodeDecode;
import com.api.central.certificate.dao.CertificateDaoImpl;
import com.api.central.typeahead.delegate.LoadMasterService;
import com.api.central.util.AppConstant;
import com.api.central.util.AppSQLConstant;
import com.api.central.util.AppUtil;
import com.api.central.util.MasterDatas;
import com.api.central.util.RestTemplateUtil;
import com.api.central.util.SequenceGenerator;

@Repository
@Transactional
public class CertificateIhmDaoImpl implements CertificateIhmDao {

	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	public LoadMasterService loadMasterService;
	
	@Autowired
	public CertificateDaoImpl certificateDaoImpl;
	
//	@Autowired
//	private RestTemplate restTemplate;
	
	@Autowired
	private RestTemplateUtil restUtil;
	
	
	static Logger log = LoggerFactory.getLogger(CertificateIhmDaoImpl.class);
	
	Map<String,Object> resultMapGen= new HashMap<String,Object>();
	@Override
	public Map<String, Object> generateCertificateIhm(CertificateIhmDetail certificateDetail,Integer count) {
		//System.out.println("certificateDetailvalues"+certificateDetail);
		
		
		
		RmiIhmCertificates rmiCert = null;
		RmiAuditCertEndorsements rmiCertEndor = null;
		RestTemplate restTemplate = new RestTemplate();
		//HttpHeaders headers = new HttpHeaders();
		HttpEntity<RmiIhmCertificates> rmiCertificatesReq = null;
		HttpEntity<RmiAuditCertEndorsements> rmiAuditCertEndorsementsReq = null;
		ResponseEntity<String> response = null;
		if(certificateDetail.getPublishStatus() == 0) {
			
		if(certificateDetail.getDocTypeNumber() != null) {		
			certificateDetail.setDocTypeNo(certificateDetail.getDocTypeNo().replaceAll(certificateDetail.getDocTypeNumber(), "").trim());
		}
		else {
			certificateDetail.setDocTypeNo(null);
		}
		Long l=sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ORDER_NO);
		certificateDetail.setCertOderNo(l.intValue());
		
			
			certificateDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
		
	
		if(certificateDetail.getCertificateNo()==null || certificateDetail.getCertificateNo().equals("")){
			
			certificateDetail.setCertificateNo(AppUtil.getCertificateNoFormat(certificateDetail.getAuditTypeId(),sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),certificateDetail.getAuditSubTypeId()));
		}
		
		if(certificateDetail.getCertIssueId()!=null && (certificateDetail.getCertIssueId() ==AppConstant.RENEWAL_ENDORSED1 ||certificateDetail.getCertIssueId() ==AppConstant.RENEWAL_ENDORSED2|| certificateDetail.getCertIssueId()==AppConstant.ADDITIONAL_SUB_TYPE_ID) ){
			
			if(certificateDetail.getEndorsementID()==null){
				
				certificateDetail.setEndorsementID(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
			}
		}
		

		if(certificateDetail.getAuditSubTypeId() == 1001) {
			 if(certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT || certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) {
				 
				 DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
						 .add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
							.add(Restrictions.eq("socType", certificateDetail.getSocType()))
							.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
							.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
							.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
							.add(Restrictions.eq("certIssueId", certificateDetail.getCertIssueId()))
							.add(Restrictions.ne("publishStatus", AppConstant.AUD_CERT_PUBLISH_STATUS))
									.setProjection(Projections.max("certificateLink"));
				 
					Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
							//.add(Restrictions.eq("auditReportNo", auditReportNo))
							.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
							.add(Restrictions.eq("socType", certificateDetail.getSocType()))
							.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
							.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
							.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
							.add(Restrictions.eq("certIssueId", certificateDetail.getCertIssueId()))
							.add(Restrictions.ne("publishStatus", AppConstant.AUD_CERT_PUBLISH_STATUS))
							.add(Property.forName("certificateLink").eq(maxId));
					  List<CertificateIhmDetail> ls = certDtl.list();
					
					  if(ls.size() > 0) {
					for(int i = 0; i< ls.size(); i++) {
						CertificateIhmDetail cert = ls.get(i);
						certificateDetail.setCertificateNo(cert.getCertificateNo());
						certificateDetail.setUtn(cert.getUtn());
					}
					  }else {
						  certificateDetail.setCertificateNo(AppUtil.getCertificateNoFormatForIhm(certificateDetail.getAuditTypeId(),sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),certificateDetail.getAuditSubTypeId(),certificateDetail.getSocType()));
					  }
					
			 }
			 else {
				 
				 DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
						 .add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
							.add(Restrictions.eq("socType", certificateDetail.getSocType()))
							.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
							.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
							.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
									.setProjection(Projections.max("seqNo"));
				 
					Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
							//.add(Restrictions.eq("auditReportNo", auditReportNo))
							.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
							.add(Restrictions.eq("socType", certificateDetail.getSocType()))
							.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
							.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
							.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
							.add(Property.forName("seqNo").eq(maxId));
					  List<CertificateIhmDetail> ls = certDtl.list();
					
					for(int i = 0; i< ls.size(); i++) {
						CertificateIhmDetail cert = ls.get(i);
						certificateDetail.setCertificateNo(cert.getCertificateNo());
						certificateDetail.setUtn(cert.getUtn());
					}
					
				 
			 }
			 }
		else {
			 if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) {
					
					
					certificateDetail.setCertificateNo(AppUtil.getCertificateNoFormatForIhm(certificateDetail.getAuditTypeId(),sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),certificateDetail.getAuditSubTypeId(),certificateDetail.getSocType()));
				 }
			 else {
				
				 DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
						 .add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
							.add(Restrictions.eq("socType", certificateDetail.getSocType()))
							.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
							.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
									.setProjection(Projections.max("seqNo"));
				 
					Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
							//.add(Restrictions.eq("auditReportNo", auditReportNo))
							
							.add(Restrictions.eq("socType", certificateDetail.getSocType()))
							.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
							.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
							
							.add(Property.forName("seqNo").eq(maxId));
					  List<CertificateIhmDetail> ls = certDtl.list();
					  
					  for(int i = 0; i< ls.size(); i++) {
							CertificateIhmDetail cert = ls.get(i);
							certificateDetail.setCertificateNo(cert.getCertificateNo());
							certificateDetail.setUtn(cert.getUtn());
						}
					  
				 
			 }
		}
		Query getMaxCertSeq = null;

		 DetachedCriteria maxId1 = DetachedCriteria.forClass(CertificateIhmDetail.class)
				 .add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("socType", certificateDetail.getSocType()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
					.add(Restrictions.eq("certIssueId", certificateDetail.getCertIssueId()))
							.setProjection(Projections.max("certificateLink"));
		 
			Criteria certDtl1 = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
					//.add(Restrictions.eq("auditReportNo", auditReportNo))
					.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("socType", certificateDetail.getSocType()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
					.add(Restrictions.eq("certIssueId", certificateDetail.getCertIssueId()))
					.add(Property.forName("certificateLink").eq(maxId1));
			  List<CertificateIhmDetail> ls1 = certDtl1.list();
			  
			// ls1.size() > 0 condition added by cb for the issue id IRI-5436
		if(certificateDetail.getSeqNo()==0 || ls1.size() <= 0 || certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE||ls1.size() > 0){

			 getMaxCertSeq = hibsession.getSession().createQuery(AppSQLConstant.GET_MAX_CERT_SEQ_IHM);
			 getMaxCertSeq.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
				getMaxCertSeq.setLong("companyId", certificateDetail.getCompanyId());
				getMaxCertSeq.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
				
				if(certificateDetail.getSocType().equals("hk")|| certificateDetail.getSocType().equals("eu")) {
					getMaxCertSeq.setString("parm1", "hk");
					getMaxCertSeq.setString("parm2", "eu");
				}
				else {
					getMaxCertSeq.setString("parm1", "exe");
					getMaxCertSeq.setString("parm2", "exe");
				}
				int seqNo = 0;
				
				List list = getMaxCertSeq.list();
				
				seqNo = (list.isEmpty() || list.get(0)==null) ? 1 : (Integer)list.get(0)+1;
				
				certificateDetail.setSeqNo(seqNo);
				
		}
		// else block commented by cb for the issue id IRI-5436
//		else {
//			
//				  
//				 if( ls1.size() > 0) {
//					 	
//						
//						certificateDetail.setSeqNo(ls1.get(0).getSeqNo());
//						
//				 }
//		}
		
		String Cno=certificateDetail.getCertificateNo();
        String Utn=certificateDetail.getUtn();
        String QidEncode="";
        try {
			String addCnoUtn=Cno+" "+Utn;
			Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");			
			String s= (String) q.uniqueResult();
			QidEncode =s;
			//QidEncode = e.EncodeCertiNoUtn(Cno, Utn);
		} 
		catch (Throwable e)
		{
			e.printStackTrace();
		}
      
        certificateDetail.setQid(QidEncode);

        //String newQrCodeUrl=certificateDetail.getQrCodeUrl().substring(0, certificateDetail.getQrCodeUrl().indexOf("viewer")+14);
       
        certificateDetail.setQrCodeUrl(AppConstant.cert_url+QidEncode);
        
        if(certificateDetail.getAuditSubTypeId()!=null && certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT){
        	
        	certificateDetail.setExtendedIssueDate(null);
        	certificateDetail.setExtendedExpireDate(null);
        	certificateDetail.setEndorsedDate(null);
        }
		
        hibsession.clearFlushSession(hibsession.getSession());
		hibsession.getSession().persist(certificateDetail);
		resultMapGen.put("updatedData",certificateDetail);
		
		/*DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
				.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
				.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
				.add(Restrictions.eq("activeStatus", AppConstant.ACTIVE_STATUS))
				.add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
							.setProjection(Projections.max("certificateLink"));
		
	Criteria certificatesToRMi = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
			//.add(Restrictions.eq("auditReportNo", auditReportNo))
			//.add(Restrictions.eq("certIssueId", certificateDetail.getCertIssueId()))
			.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
			.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
			.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
			.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
			.add(Property.forName("certificateLink").eq(maxId));
			
	List<CertificateIhmDetail> allCert= certificatesToRMi.list();
	
	if(allCert.size() > 0 ) {
		if(allCert.stream()
			.filter( item -> (item.getCertIssueId() == AppConstant.FULL_TERM_CERT || item.getCertIssueId() == AppConstant.RE_ISSUE) && (item.getSocType().equals("hk") || item.getSocType().equals("eu")) ).count() > 0) {
			
			List<CertificateIhmDetail> res = allCert.stream()
					.filter( item -> (item.getCertIssueId() == AppConstant.FULL_TERM_CERT || item.getCertIssueId() == AppConstant.RE_ISSUE) && !(item.getSocType().equals("exe")) ).collect(Collectors.toList());
		resultMapGen.put("updatedData",res.get(0));
	}
		else {
			List<CertificateIhmDetail> res = allCert.stream()
					.filter( item -> (item.getCertIssueId() == AppConstant.FULL_TERM_CERT || item.getCertIssueId() == AppConstant.RE_ISSUE) && (item.getSocType().equals("exe")) ).collect(Collectors.toList());
		resultMapGen.put("updatedData",res.get(0));
		}
	}
		*/
		/** Making inactive certificate for IHM after generating full term certificate*/ 
		if(certificateDetail.getAuditTypeId() == AppConstant.IHM_TYPE_ID &&  certificateDetail.getCertIssueId() != AppConstant.FULL_TERM_CERT){
			Query makeInactiveToRest = null;
			if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE ) {

				 if(certificateDetail.getSocType().equals("hk") || certificateDetail.getSocType().equals("eu")) {
						makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_SOC);
						
				 }else {
					 makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_EXE);
				 }
				 makeInactiveToRest.setInteger("certificateLink", certificateDetail.getCertificateLink());
					
			}
			else {
				if(certificateDetail.getSocType().equals("hk") || certificateDetail.getSocType().equals("eu")) {
				makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_SOC);
				}else {
					makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_EXE);
				}
				makeInactiveToRest.setInteger("certIssueId", certificateDetail.getCertIssueId());
			}
		
			
			makeInactiveToRest.setInteger("activeStatus", AppConstant.INACTIVE_STATUS);
			makeInactiveToRest.setLong("companyId", certificateDetail.getCompanyId());
			makeInactiveToRest.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			makeInactiveToRest.setInteger("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ);
			makeInactiveToRest.setInteger("vesselImoNo", certificateDetail.getVesselImoNo());
			
			makeInactiveToRest.executeUpdate();
			
		}
		
		if(certificateDetail.getAuditTypeId() == AppConstant.IHM_TYPE_ID && certificateDetail.getAuditSubTypeId() == AppConstant.AMENDMENT_SUB_TYPE_ID) {
			
			log.info("updateEndorse - {}", certificateDetail.getEndorsedDate().toString());
			Query updateEndorse = hibsession.getSession().createQuery("update AuditDetailIhm set additionalSurvey = :additionalSurvey where companyId = :companyId and auditSubTypeId = :auditSubTypeId and auditTypeId = :auditTypeId and auditSeqNo = :auditSeqNo");
			updateEndorse.setString("additionalSurvey", certificateDetail.getEndorsedDate().toString());
			updateEndorse.setLong("companyId", certificateDetail.getCompanyId());
			updateEndorse.setLong("auditSubTypeId", certificateDetail.getAuditSubTypeId());
			updateEndorse.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			updateEndorse.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			updateEndorse.executeUpdate();
		}
		
		/*****Update Audit Data as of updates on certificate*****/
		Query updateAudit = null;
		if(certificateDetail.getAuditTypeId()!=null){
			updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_DTL_FROM_CERTIFICATE_DTL_IHM);
		}
		updateAudit.setString("certificateNo", certificateDetail.getCertificateNo());
		//updateAudit.setDate("certIssueDate", certificateDetail.getCertIssueDate());
		//updateAudit.setDate("dateOfRegistry", certificateDetail.getDateOfRegistry());
		//updateAudit.setDate("auditDate", certificateDetail.getAuditDate());
		//updateAudit.setInteger("certIssueId", certificateDetail.getCertIssueId());
		//updateAudit.setInteger("grt", certificateDetail.getGrt());
		updateAudit.setLong("companyId", certificateDetail.getCompanyId());
		updateAudit.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
		updateAudit.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
		updateAudit.executeUpdate();
		
		} 
		else {
			//		hibsession.getSession().beginTransaction();
					Query callStoredProcedure = null;
					callStoredProcedure = hibsession.getSession().createSQLQuery("CALL MA_UPDATE_PUBLISH_STATUS_IHM ( :certIssueId , :companyId , :vesselImo , :auditSubTypeId , :auditSeqNo, :socType)")
							.setInteger("certIssueId", certificateDetail.getCertIssueId())
							.setLong("companyId", certificateDetail.getCompanyId())
							.setInteger("vesselImo", certificateDetail.getVesselImoNo())
							.setInteger("auditSubTypeId", certificateDetail.getAuditSubTypeId())
							.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo())
							.setString("socType",certificateDetail.getSocType().equals("exe") ? "exe" : "soc");
				
							callStoredProcedure.executeUpdate();
					//hibsession.getSession().getTransaction().commit();
					
					
						resultMapGen.put("updatedData",certificateDetail);
					
				}
		/*changes made by cb and sudharsan */
			if(certificateDetail.getPublishStatus()==1 && certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE && !certificateDetail.getSocType().equals("exe")) {
				 Query query1 =hibsession.getSession().createQuery(" from CertificateIhmDetail where auditSeqNo= :auditSeqNo and companyId= :companyId and auditSubTypeId= :auditSubTypeId and socType <> :socType");
				 query1.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo());
				 query1.setParameter("companyId", certificateDetail.getCompanyId());
				 query1.setParameter("auditSubTypeId", certificateDetail.getAuditSubTypeId());
				 query1.setParameter("socType", "exe");
				 
		         ArrayList<CertificateIhmDetail> certificateIhmdetails = (ArrayList<CertificateIhmDetail>) query1.list();
				
				 for(CertificateIhmDetail CertificateIhmdetails: certificateIhmdetails ) {
				 Query rmiCert2 = hibsession.getSession().createQuery("update RmiIhmCertificates set certificateStatus = :certificateStatus where reviewSeqNo = :reviewSeqNo and  certificateID <> :certificateID and socType <> :socType");
				 rmiCert2.setParameter("certificateStatus", "IN-ACTIVE");
				 rmiCert2.setParameter("reviewSeqNo", CertificateIhmdetails.getAuditSeqNo());
				 rmiCert2.setParameter("certificateID", CertificateIhmdetails.getCertificateId());
				 rmiCert2.setParameter("socType", "exe");
				 rmiCert2.executeUpdate();
				 
				 }
			}
			
			if(certificateDetail.getPublishStatus()==1 && certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE && certificateDetail.getSocType().equals("exe")) {
				 Query query1 =hibsession.getSession().createQuery(" from CertificateIhmDetail where auditSeqNo= :auditSeqNo and companyId= :companyId and auditSubTypeId= :auditSubTypeId and socType = :socType");
				 query1.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo());
				 query1.setParameter("companyId", certificateDetail.getCompanyId());
				 query1.setParameter("auditSubTypeId", certificateDetail.getAuditSubTypeId());
				 query1.setParameter("socType", "exe");
				 
		         ArrayList<CertificateIhmDetail> certificateIhmdetails = (ArrayList<CertificateIhmDetail>) query1.list();
				
				 for(CertificateIhmDetail CertificateIhmdetails: certificateIhmdetails ) {
				 Query rmiCert2 = hibsession.getSession().createQuery("update RmiIhmCertificates set certificateStatus = :certificateStatus where reviewSeqNo = :reviewSeqNo and  certificateID <> :certificateID and socType = :socType");
				 rmiCert2.setParameter("certificateStatus", "IN-ACTIVE");
				 rmiCert2.setParameter("reviewSeqNo", CertificateIhmdetails.getAuditSeqNo());
				 rmiCert2.setParameter("certificateID", CertificateIhmdetails.getCertificateId());
				 rmiCert2.setParameter("socType", "exe");
				 rmiCert2.executeUpdate();
				 
				 }
			}
			/*changes made by cb and sudharsan end  */
				
				return resultMapGen;
	}
	
	public String populateData(List<CertificateIhmDetail> certificateDetail){
		hibsession.clearFlushSession(hibsession.getSession());
		for(CertificateIhmDetail obj:certificateDetail) {
			
		hibsession.getSession().saveOrUpdate(obj);
		
			}
		return "success";
	}
	public Integer MaxCertLinkNo (CertificateIhmDetail certificateDetail) {
				
		int certificateLink = 0;
		Object linkCert;
		if(certificateDetail.getAuditSubTypeId() == AppConstant.INITIAL_IHM_SUB_TYPE_ID) {
		
				if(certificateDetail.getSocType().equals("hk") || certificateDetail.getSocType().equals("eu")) {
					 linkCert = hibsession.getSession().createSQLQuery("select max(certificate_link_seq)  from certificate_IHM_detail where company_id = :companyId and audit_sub_type_id = :auditSubTypeId and audit_seq_no =  :auditSeqNo and vessel_imo_no = :vesselImoNo and soc_type in (:parm1,:parm2)")
							
							.setParameter("vesselImoNo", certificateDetail.getVesselImoNo())
							.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo())
							.setParameter("companyId", certificateDetail.getCompanyId())
							.setParameter("auditSubTypeId", certificateDetail.getAuditSubTypeId()).setParameter("parm1", "hk")
				.setParameter("parm2", "eu").uniqueResult();
				}else { 
					linkCert = hibsession.getSession().createSQLQuery("select max(certificate_link_seq)  from certificate_IHM_detail where company_id = :companyId and audit_sub_type_id = :auditSubTypeId and audit_seq_no =  :auditSeqNo and vessel_imo_no = :vesselImoNo and soc_type = :socType")
							
							.setParameter("vesselImoNo", certificateDetail.getVesselImoNo())
							.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo())
							.setParameter("companyId", certificateDetail.getCompanyId())
							.setParameter("auditSubTypeId", certificateDetail.getAuditSubTypeId()).setParameter("socType", "exe")
				.uniqueResult();
				}
					
		log.info("linkCert = "+linkCert);

		if(certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT ) {
			certificateLink = 1;
			
		}else  if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) {
			certificateLink = (linkCert != "" && linkCert != null) ? Integer.parseInt(linkCert.toString())+1 : 1;
		}
		else {
			certificateLink =Integer.parseInt(linkCert.toString());
		}
		}
		else {
			String param1 = "";
			String param2 = "";
		//	 linkCert = hibsession.getSession().createSQLQuery("select max(certificate_link_seq)  from certificate_IHM_detail where company_id = :companyId  and audit_seq_no !=  :auditSeqNo and vessel_imo_no = :vesselImoNo and publish_status = :publishStatus ")
			if(certificateDetail.getSocType().equals("hk") || certificateDetail.getSocType().equals("eu")) {
				param1 = "hk";
				param2 = "eu";
			}else {
				param1 = "exe";
				param2 = "exe";
			}
			linkCert = hibsession.getSession().createSQLQuery("select max(certificate_link_seq)  from certificate_IHM_detail where company_id = :companyId  and audit_seq_no !=  :auditSeqNo and vessel_imo_no = :vesselImoNo and publish_status = :publishStatus and audit_seq_no in (select audit_seq_no from audit_details where company_id = :companyId and  vessel_imo_no = :vesselImoNo and audit_type_id = 1006 and audit_status_id != 1004 and audit_summary_id != 1005) and soc_type in (:param1,:param2)")	
					.setParameter("vesselImoNo", certificateDetail.getVesselImoNo())
					.setParameter("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ)
					.setParameter("companyId", certificateDetail.getCompanyId())
					.setParameter("publishStatus",AppConstant.AUD_CERT_PUBLISH_STATUS )
					.setParameter("param1", param1)
					.setParameter("param2", param2)
					.uniqueResult();
			
			log.info("linkCert = "+linkCert);
			if(certificateDetail.getCertIssueId() == AppConstant.ADDITIONAL_ENDORSED ) {
				certificateLink = Integer.parseInt(linkCert.toString());
				
			}else  if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) {
				certificateLink = (linkCert != "" && linkCert != null) ? Integer.parseInt(linkCert.toString())+1 : 1;
			}
		}
		return certificateLink;
		
	}
	public Integer MaxCertLinkNoWithoutAudit (CertificateDetailWithOutAuditIhm certificateDetail) {
		
		int certificateLink = 0;
		
		Object linkCert = hibsession.getSession().createSQLQuery("select max(certificate_link_seq)  from certificate_IHM_detail where company_id = :companyId and audit_sub_type_id = :auditSubTypeId and audit_seq_no =  :auditSeqNo and vessel_imo_no = :vesselImoNo ")
				
				.setParameter("vesselImoNo", certificateDetail.getVesselImoNo())
				.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo())
				.setParameter("companyId", certificateDetail.getCompanyId())
				.setParameter("auditSubTypeId", certificateDetail.getAuditSubTypeId()).uniqueResult();
		
		log.info("linkCert = "+linkCert);

		if(certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT ) {
			certificateLink = 1;
			
		}else  if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) {
			certificateLink = (linkCert != "" && linkCert != null) ? Integer.parseInt(linkCert.toString())+1 : 1;
		}
		else {
			certificateLink =Integer.parseInt(linkCert.toString());
		}
		
		return certificateLink;
		
	}
	public void sendRmiData(CertificateIhmDetail certificateDetail){	
		
		RmiIhmCertificates rmiCert = null;
		RmiCertEndorsementsIhm rmiCertEndor = null;
		RestTemplate restTemplate = new RestTemplate();
	//	HttpHeaders headers = new HttpHeaders();
		HttpEntity<RmiIhmCertificates> rmiCertificatesReq = null;
		HttpEntity<RmiCertEndorsementsIhm> rmiAuditCertEndorsementsReq = null;
		ResponseEntity<String> response = null;
		Long certId,minCertId = null ;
		try {
//			hibsession.getSession().beginTransaction();
			
			DetachedCriteria auditId = DetachedCriteria.forClass(AuditDetailIhm.class)
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.add(Restrictions.eq("activeStatus", AppConstant.VOID_AUDIT_STATUS))
					.add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
								.setProjection(Projections.max("auditSeqNo"));
			
			
			DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.add(Restrictions.eq("activeStatus", AppConstant.ACTIVE_STATUS))
					.add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
					.add(Restrictions.eq("socType", certificateDetail.getSocType()))	
								.setProjection(Projections.max("certificateLink"));
			
		Criteria certificatesToRMi = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
				//.add(Restrictions.eq("auditReportNo", auditReportNo))
				.add(Restrictions.eq("certIssueId", certificateDetail.getCertIssueId()))
				.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
				.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
				.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
				.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
				.add(Property.forName("certificateLink").eq(maxId));
				
				if(certificateDetail.getSocType().equals("exe")) {
					certificatesToRMi.add(Restrictions.eq("socType", certificateDetail.getSocType()));
				}
				else {
					certificatesToRMi.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
				}

		List<CertificateIhmDetail> RMICertificatesResult= certificatesToRMi.list();
	
		if(certificateDetail.getCertIssueId() != AppConstant.ADDITIONAL_ENDORSED) {
		Query minCertificateId = null;
		minCertificateId = hibsession.getSession().createQuery(AppSQLConstant.GET_MIN_CERTIFICATE_ID_FOR_RMI_IHM);
		minCertificateId.setParameter("vessel_imo_no", certificateDetail.getVesselImoNo());
		minCertificateId.setParameter("certIssueId",certificateDetail.getCertIssueId());
		minCertificateId.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo());
		minCertificateId.setParameter("auditSubTypeId", certificateDetail.getAuditSubTypeId());
		
		if(certificateDetail.getSocType().equals("exe")) {
			minCertificateId.setParameter("param1", certificateDetail.getSocType());
			minCertificateId.setParameter("param2", certificateDetail.getSocType());
		}
		else {
			minCertificateId.setParameter("param1", "hk");
			minCertificateId.setParameter("param2", "eu");
		}
		
		
		minCertId = (Long) minCertificateId.uniqueResult();
		
		
		if(certificateDetail.getSocType().equalsIgnoreCase("exe")){
			Query updateCertificatesRMI = null;
			updateCertificatesRMI = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_VOID_CERT_STATUS_FOR_RMI_IHM);
			updateCertificatesRMI.setParameter("certificateStatus", "IN-ACTIVE");
			updateCertificatesRMI.setParameter("reviewSeqNo", certificateDetail.getAuditSeqNo());
			updateCertificatesRMI.setParameter("vesselID", certificateDetail.getVesselId());
			//updateCertificatesRMI.setParameter("certificateID",minCertId);
			updateCertificatesRMI.setParameter("certificateID",certificateDetail.getCertificateId());
			updateCertificatesRMI.setParameter("socType", "exe");
			updateCertificatesRMI.executeUpdate();
		}
		
		else if((certificateDetail.getSocType().equalsIgnoreCase("hk"))||(certificateDetail.getSocType().equalsIgnoreCase("eu"))){
			Query updateCertificatesRMI = null;
			updateCertificatesRMI = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_CERT_STATUS_FOR_RMI_IHM_II);
			updateCertificatesRMI.setParameter("certificateStatus", "IN-ACTIVE");
			updateCertificatesRMI.setParameter("reviewSeqNo", certificateDetail.getAuditSeqNo());
			updateCertificatesRMI.setParameter("vesselID", certificateDetail.getVesselId());

			updateCertificatesRMI.setParameter("certificateID",minCertId);
			updateCertificatesRMI.setParameter("socType", certificateDetail.getSocType());
			updateCertificatesRMI.executeUpdate();
		}
		}
		
		

	
			
			CertificateIhmDetail cert1 = RMICertificatesResult.get(0);
			
			Query certificateId = null;
			certificateId = hibsession.getSession().createQuery(AppSQLConstant.GET_CERTIFICATE_ID_FOR_RMI_IHM);
			certificateId.setParameter("vessel_imo_no", cert1.getVesselImoNo());
			certificateId.setParameter("auditSubTypeId",AppConstant.INITIAL_IHM_SUB_TYPE_ID);
			//certificateId.setParameter("certificateNo", cert.getCertificateNo());
			certificateId.setParameter("auditTypeId", cert1.getAuditTypeId());
			//certificateId.setParameter("auditSeqNo", cert.getCertificateNo());
			
			certId = (Long) certificateId.uniqueResult();
			
		//	cert.setCertificateId(certId);
			
			if( certificateDetail.getCertIssueId() != AppConstant.ADDITIONAL_ENDORSED){
				rmiCert = setRmiCertData(cert1); System.out.println("6dd666d34");
				if(rmiCert != null && cert1.getCertIssueId() != AppConstant.FULL_TERM_CERT){
					//added by sudharsan for INCEDENT_TICKET-671 Start here
					RmiAuditCertificate rmiauditCertificate = setRmiauidtCertData(rmiCert);
					System.out.println("rrmiauditCertificatemiauditCertificate"+rmiauditCertificate);
					HttpEntity<RmiAuditCertificate> rmiCertificatesmodelReq = null;
					rmiCertificatesmodelReq = new HttpEntity<RmiAuditCertificate>(rmiauditCertificate, restUtil.getHeaders());
						//rmiCertificatesReq = new HttpEntity<RmiIhmCertificates>(rmiCert, restUtil.getHeaders());
						response = restTemplate.exchange( AppConstant.RMI_URL+"/ws1/MakeCertIncative/"+rmiauditCertificate.getAuditID()+"/"+minCertId+"/IHM/"+cert1.getCertIssueId()+"/"+cert1.getSocType() ,HttpMethod.GET, rmiCertificatesmodelReq, String.class );
						//added by sudharsan for INCEDENT_TICKET-671 end here
				}
							
			}
			
		
		for(int i = 0; i< RMICertificatesResult.size(); i++) {
			CertificateIhmDetail cert = RMICertificatesResult.get(i);
			
			//Query certificateId = null;
		if(cert.getAuditTypeId()!=null && (cert.getCertIssueId()==AppConstant.ADDITIONAL_ENDORSED )){
			rmiCertEndor = setRmiAuditCertEndorsementsData(cert); 			System.out.println("6dd666");
		//	rmiCertEndor.setCertificateID(certId);
		}else{ 
			rmiCert = setRmiCertData(cert); System.out.println("6dd666d34");
		//	rmiCert.setCertificateID(certId);
		}
		
		 
		
		if(rmiCert != null){
			hibsession.getSession().persist(rmiCert);
			rmiCert.setAuditType("IHM");
			//added by sudharsan for INCEDENT_TICKET-671 Start here
			System.err.println("endorsement:----inside initial"+rmiCert);
			RmiAuditCertificate rmiauditCertificate = setRmiauidtCertData(rmiCert);
			HttpEntity<RmiAuditCertificate> rmiCertificatesmodelReq = null;
			rmiCertificatesmodelReq = new HttpEntity<RmiAuditCertificate>(rmiauditCertificate, restUtil.getHeaders());
			
			response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditCertInfo", rmiCertificatesmodelReq , String.class );
			//added by sudharsan for INCEDENT_TICKET-671 End here
		} if(rmiCertEndor != null){
			hibsession.getSession().persist(rmiCertEndor);
			rmiCertEndor.setAuditType("IHM");
			//added by sudharsan for INCEDENT_TICKET-671 Start here
			System.err.println("endorsement:---- inside amendment"+rmiCertEndor);
			RmiAuditCertEndorsementModel rmiAuditCertEndorsementmodel = setRmiAuditCertEndorsementsModelData(rmiCertEndor);
			System.err.println("endorsement:----"+rmiAuditCertEndorsementmodel);
			HttpEntity<RmiAuditCertEndorsementModel> rmiAuditCertEndorsementmodelreq = new HttpEntity<RmiAuditCertEndorsementModel>(rmiAuditCertEndorsementmodel, restUtil.getHeaders());
			System.err.println("endorsement:----"+rmiAuditCertEndorsementmodelreq);
			//rmiAuditCertEndorsementsReq = new HttpEntity<RmiCertEndorsementsIhm>(rmiCertEndor, restUtil.getHeaders());
			response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditEndorseInfo", rmiAuditCertEndorsementmodelreq , String.class );
			System.err.println("endorsement:----"+response);
			//added by sudharsan for INCEDENT_TICKET-671 End here
		}
		
		}
		
			
	//	hibsession.getSession().getTransaction().commit();
		}
		catch(Exception ex) {
			log.info("sendRmiData ",ex);
		}
	}

public RmiCertEndorsementsIhm setRmiAuditCertEndorsementsData(CertificateIhmDetail certificateDetail){
		
		RmiCertEndorsementsIhm rmiCertEndor = new RmiCertEndorsementsIhm();
		  
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		
		rmiCertEndor.setAuditID(certificateDetail.getAuditSeqNo());
		rmiCertEndor.setCertificateID(certificateDetail.getCertificateId());
		rmiCertEndor.setAuditID(certificateDetail.getAuditSeqNo());
		rmiCertEndor.setEndorsementId(certificateDetail.getEndorsementID() != null ? certificateDetail.getEndorsementID() : sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
		rmiCertEndor.setEndorsementDate(certificateDetail.getEndorsedDate());
		if(certificateDetail.getExtendedEndorsedDate()!=null){
			rmiCertEndor.setEndorsementDate(certificateDetail.getExtendedEndorsedDate());	
		}
		rmiCertEndor.setEndorsementReason(MasterDatas.getCertificateIssueType(certificateDetail.getCertIssueId(),certificateDetail.getAuditTypeId(),certificateDetail.getRoleId())); // need to remove next deployemnt 
		
		rmiCertEndor.setCreatedBy(certificateDetail.getLeadName());
		rmiCertEndor.setCreationDate(sqlDate);
		rmiCertEndor.setEndorsedBy(certificateDetail.getIssuerName());
		rmiCertEndor.setLastUpdateDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		rmiCertEndor.setLastUpdatedBy(certificateDetail.getUserInsName()!=null?certificateDetail.getUserInsName():certificateDetail.getIssuerName());
		rmiCertEndor.setVesselId(certificateDetail.getVesselId());
		rmiCertEndor.setVesselName(certificateDetail.getVesselName());
		rmiCertEndor.setExtendedDate(certificateDetail.getExtendedEndorsedDate());
		
	/*	if(certificateDetail.getAuditTypeId()!=null){
			rmiCertEndor.setAuditID(certificateDetail.getAuditTypeId());
			}*/
		System.err.println("endorsement: "+rmiCertEndor);
		return rmiCertEndor;
	}
public RmiIhmCertificates setRmiCertData(CertificateIhmDetail certificateDetail){
	
	RmiIhmCertificates rmiCert = new RmiIhmCertificates();
	
	Criterion rest1 = Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY);
	
	Criterion rest2 = Restrictions.eq("auditStatusId",AppConstant.VOID_AUDIT_STATUS);
	

	
	Criteria inActiveAuditSeqNo = hibsession.getSession().createCriteria(AuditDetailIhm.class)
			.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
			.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
			
			.add(Restrictions.eq("auditTypeId",certificateDetail.getAuditTypeId()))
			.add(Restrictions.or(rest1,rest2))
			.setProjection(Projections.property("auditSeqNo"));
	List<AuditDetailIhm> auditDetail = (List<AuditDetailIhm>) inActiveAuditSeqNo.list();		
			
	DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
			.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
			.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
			.add(Restrictions.eq("activeStatus", AppConstant.ACTIVE_STATUS))
			.add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
			
						.setProjection(Projections.max("certificateLink"));
	if(auditDetail.size() != 0) {
		maxId.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
	}
	
	Criteria certificatesToRMi = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
			//.add(Restrictions.eq("auditReportNo", auditReportNo))
			.add(Restrictions.ne("certIssueId", certificateDetail.getCertIssueId()))
			.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
			.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
			//.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)))
			//.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
			.add(Property.forName("certificateLink").eq(maxId))
		    .addOrder(Order.asc("seqNo"));
		//	certificatesToRMi.setFirstResult(0);
		//	certificatesToRMi.setMaxResults(1);
			
			if(auditDetail.size() != 0) {
				maxId.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
			}
			
			List<CertificateIhmDetail> fulltermCert = (List<CertificateIhmDetail>) certificatesToRMi.list();
			
	
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
	
	rmiCert.setCertificateID(certificateDetail.getCertificateId());
	rmiCert.setCertificateNumber(certificateDetail.getCertificateNo());
	rmiCert.setReviewSeqNo(certificateDetail.getAuditSeqNo());
	rmiCert.setUniqueTrackingNumber(certificateDetail.getUtn());
	rmiCert.setCertificateURL(certificateDetail.getQrCodeUrl());
	rmiCert.setCertificateIssueType(MasterDatas.getCertificateIssueType(certificateDetail.getCertIssueId() ,certificateDetail.getAuditTypeId(),certificateDetail.getRoleId()));
	
	if(certificateDetail.getExtendedIssueDate() != null) {
		rmiCert.setIssueDate(certificateDetail.getExtendedIssueDate());
	}
	else {
	rmiCert.setIssueDate(certificateDetail.getCertIssueDate());
	}
	
	if(certificateDetail.getExtendedExpireDate() != null) {
		rmiCert.setExpirationDate(certificateDetail.getExtendedExpireDate());
	}
	else {
		rmiCert.setExpirationDate(certificateDetail.getCertExpireDate());
	}
	
	rmiCert.setIssuedBy(certificateDetail.getIssuerName());
	rmiCert.setCertificateStatus(certificateDetail.getActiveStatus() == 1 ? "ACTIVE" : "IN-ACTIVE");
	rmiCert.setVesselName(certificateDetail.getVesselName());
	rmiCert.setVesselID(certificateDetail.getVesselId());
	rmiCert.setCreationDate(certificateDetail.getAuditDate());
	rmiCert.setCreatedBy(certificateDetail.getUserInsName()!=null?certificateDetail.getUserInsName():certificateDetail.getIssuerName());
	rmiCert.setLastUpdateDate(new Date(certificateDetail.getDateIns().getTime()));
	rmiCert.setLastUpdatedBy(certificateDetail.getUserInsName()!=null?certificateDetail.getUserInsName():certificateDetail.getIssuerName());
	rmiCert.setExtendDate(certificateDetail.getExtendedEndorsedDate()); 
	if(certificateDetail.getAuditSubTypeId()!=null && certificateDetail.getAuditSubTypeId()==AppConstant.RENEWAL_SUB_TYPE_ID && certificateDetail.getEndorsedDate()!=null){
		rmiCert.setExtendDate(certificateDetail.getEndorsedDate());	
	}
	
	rmiCert.setCreationDate(sqlDate);
	
	rmiCert.setSocType(certificateDetail.getSocType());
	
	rmiCert.setExtendDate(certificateDetail.getExtendedEndorsedDate()); 
	
	rmiCert.setSurveryCompletionDate(certificateDetail.getCompletionSurveyDate());
	
	rmiCert.setDocumentNo(certificateDetail.getIhmDocumentNo());
	
	if(certificateDetail.getCertIssueId() == AppConstant.EXTENSION || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED1 || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED2)
	{
		if(certificateDetail.getCertIssueId() == AppConstant.EXTENSION && certificateDetail.getSocType().equals("exe")) {
			
			
			if(fulltermCert.get(fulltermCert.size() -1).getCertIssueId() == AppConstant.RENEWAL_ENDORSED1 || fulltermCert.get(fulltermCert.size() -1).getCertIssueId() == AppConstant.RENEWAL_ENDORSED2) {
				rmiCert.setExpirationDate(fulltermCert.get(0).getExtendedExpireDate() != null ? fulltermCert.get(0).getExtendedExpireDate() : fulltermCert.get(0).getCertExpireDate());
			}
			else {
				rmiCert.setExpirationDate(fulltermCert.get(fulltermCert.size() -1).getExtendedExpireDate() != null ? fulltermCert.get(fulltermCert.size() -1).getExtendedExpireDate() : fulltermCert.get(fulltermCert.size() -1).getCertExpireDate());
			}
		}
//		if(certificateDetail.getCertIssueId() == AppConstant.EXTENSION ) {
//			
//		  if(certificateDetail.getSocType().equals("hk")) {
//				rmiCert.setExpirationDate(fulltermCert.get(fulltermCert.size() -1).getExtendedExpireDate() != null ? fulltermCert.get(fulltermCert.size() -1).getExtendedExpireDate() : fulltermCert.get(fulltermCert.size() -1).getCertExpireDate());
//			}
//			else {
//				rmiCert.setExpirationDate(fulltermCert.get(0).getExtendedExpireDate() != null ? fulltermCert.get(0).getExtendedExpireDate() : fulltermCert.get(0).getCertExpireDate());
//			}
//		}
		if((certificateDetail.getSocType().equals("exe") || certificateDetail.getSocType().equals("eu")) && (certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED1 || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED2)) {
			if(certificateDetail.getSocType().equals("eu")) {
			Long initalCount = fulltermCert.stream()
					.filter( item -> item.getCertIssueId() ==AppConstant.EXTENSION && item.getSeqNo() < certificateDetail.getSeqNo()).count();
			if(initalCount > 0) {
				List<CertificateIhmDetail> extRecords = fulltermCert.stream()
				.filter( item -> item.getCertIssueId() ==AppConstant.EXTENSION).collect(Collectors.toList());
				if(extRecords.size() > 0) {
					rmiCert.setExpirationDate(extRecords.get(0).getExtendedExpireDate() != null ? extRecords.get(0).getExtendedExpireDate() : extRecords.get(0).getCertExpireDate());
				}
			}
			else {
				rmiCert.setExpirationDate(fulltermCert.get(0).getExtendedExpireDate() != null ? fulltermCert.get(0).getExtendedExpireDate() : fulltermCert.get(0).getCertExpireDate());
				}
			}
			else {
			rmiCert.setExpirationDate(fulltermCert.get(0).getExtendedExpireDate() != null ? fulltermCert.get(0).getExtendedExpireDate() : fulltermCert.get(0).getCertExpireDate());
			}
		}
	}
	System.err.println("endorsement: "+rmiCert);
	return rmiCert;
}

@Override
public void InsertionOfCertDtls(Integer vesselImoNo, Integer auditSeqNo, String certificateNo, Integer auditTypeId,
		Integer auditSubTypeId, Long companyId, String status) {

	Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL MA_IHM_AMEND_GENERATE_CERT(:vesselImoNo, :auditSeqNo, :certificateNo, :auditTypeId, :auditSubTypeId, :companyId, :status, :socType)")
			.setInteger("vesselImoNo", vesselImoNo)
			.setInteger("auditSeqNo", auditSeqNo)
			.setString("certificateNo", certificateNo.split("-")[0])
			.setInteger("auditTypeId", auditTypeId)
			.setInteger("auditSubTypeId", auditSubTypeId)
			.setLong("companyId", companyId)
			.setString("status", status)
			.setString("socType", "soc");
	
	callStoredProcedure_MYSQL.executeUpdate();

}

@Override
public Map<String, Object> getUtnAndCertificateIdForIhm(String auditReportNo, String certificateNo, Long companyId) {
	
	List<Object[]> obj = null;
	Map<String,Object> resultMap= new HashMap<String,Object>();
	
	Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
			//.add(Restrictions.eq("auditReportNo", auditReportNo))
			.add(Restrictions.eq("companyId", companyId))
			.add(Restrictions.eq("certificateNo", certificateNo))
			.setProjection(Projections.projectionList()
					.add(Projections.property("auditSeqNo"))
					.add(Projections.property("auditTypeId"))
                   	.add(Projections.property("auditSubTypeId"))
					.add(Projections.property("certificateId"))
					.add(Projections.property("utn"))
                   	.add(Projections.property("qrCodeUrl")));
            
	//certDtl.add(Restrictions.in("auditSubTypeId", new Object[]{AppConstant.INTERIM_SUB_TYPE_ID,AppConstant.INITIAL_SUB_TYPE_ID,AppConstant.RENEWAL_SUB_TYPE_ID}));
	certDtl.setMaxResults(1);
	
	obj = certDtl.list();
	
	if(!obj.isEmpty()){
		
		resultMap.put("auditSeqNo"	  , obj.get(0)[0]);
		resultMap.put("auditTypeId"   , obj.get(0)[1]);
		resultMap.put("auditSubTypeId", obj.get(0)[2]);
		resultMap.put("certificateId" , obj.get(0)[3]);
		resultMap.put("utn"			  , obj.get(0)[4]);
		resultMap.put("qrCodeUrl"     , obj.get(0)[5]);
	}
	
	return resultMap;
}

@Override
public Map<String, Object> deleteCertificateForIhm(Integer certIssueId, Long companyId, Integer vesselImo, Integer auditSubTypeId, Integer auditSeqNo, String certNo, String SocType) {
	
	List<Object[]> obj = null;
	Map<String,Object> resultMap= new HashMap<String,Object>();
	String result = "FAILED";
	List<Long> certIdList = null;
	try {
	if(certIssueId == AppConstant.FULL_TERM_CERT) {
		
		
		Criteria  certId= hibsession.getSession().createCriteria(CertificateIhmDetail.class)
				
				.add(Restrictions.eq("certIssueId", certIssueId))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselImoNo", vesselImo))
				.add(Restrictions.eq("auditSubTypeId", auditSubTypeId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.ne("publishStatus", AppConstant.AUD_CERT_PUBLISH_STATUS))
				
				.setProjection(Projections.property("certificateId"));
		
		if(SocType.equals("soc")) {
			certId.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
		}
		else {
			certId.add(Restrictions.in("socType", new Object[]{"exe"}));
		}
		certIdList = (List<Long>) certId.list();	
		
		System.err.println("failing execution"+certIdList);
	Query deleteCertificates = null;
	deleteCertificates = hibsession.getSession().createQuery(AppSQLConstant.DELETE_CERTIFICATES_IHM);
	deleteCertificates.setParameter("certIssueId", certIssueId);
	deleteCertificates.setParameter("companyId", companyId);
	deleteCertificates.setParameter("vesselImo", vesselImo);
	deleteCertificates.setParameterList("certificateId",certIdList);
	//deleteCertificates.setParameter("auditSubTypeId", auditSubTypeId);
	//deleteCertificates.setParameter("auditSeqNo", auditSeqNo);
	deleteCertificates.executeUpdate();
	result = "SUCCESS";
	}
	else{
	
		Criteria certificates = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
			
				.add(Restrictions.eq("certIssueId", certIssueId))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselImoNo", vesselImo))
				.add(Restrictions.eq("auditSubTypeId", auditSubTypeId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("certificateNo", certNo))
				.setProjection(Projections.rowCount());
				
	
		
		List countList = certificates.list();
		Long rowCount = null;
		if (countList!=null) {
             rowCount = (Long) countList.get(0);
            System.out.println("Total Results:" + rowCount);
        }
		if(rowCount > 0) {
			
			
			Criteria  certId= hibsession.getSession().createCriteria(CertificateIhmDetail.class)
					
					.add(Restrictions.eq("certIssueId", certIssueId))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("vesselImoNo", vesselImo))
					.add(Restrictions.eq("auditSubTypeId", auditSubTypeId))
					.add(Restrictions.eq("auditSeqNo", auditSeqNo))
					
					.setProjection(Projections.property("certificateId"));
			
			certIdList = (List<Long>) certId.list();	
				
			
		Query deleteCertificates = null;
		if(certIssueId == AppConstant.RE_ISSUE) {
			if(auditSubTypeId == AppConstant.INITIAL_IHM_SUB_TYPE_ID) {
			deleteCertificates = hibsession.getSession().createQuery(AppSQLConstant.DELETE_CERTIFICATES_IHM_REISSUE);
			deleteCertificates.setParameter("publishStatus", 0);
			deleteCertificates.setParameter("auditSubTypeId", auditSubTypeId);
			deleteCertificates.setParameter("auditSeqNo", auditSeqNo);
			deleteCertificates.setParameter("certIssueId", certIssueId);
			deleteCertificates.setParameter("companyId", companyId);
			deleteCertificates.setParameter("vesselImo", vesselImo);
			if(SocType.equals("soc")) {
				deleteCertificates.setParameter("param1", "hk");
				deleteCertificates.setParameter("param2", "eu");
				
			}
			else {
				deleteCertificates.setParameter("param1", "exe");
				deleteCertificates.setParameter("param2", "exe");
			}
			
			deleteCertificates.executeUpdate();
			}else {
				
				Criteria deleteCertDetails = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
						
						.add(Restrictions.eq("certIssueId", certIssueId))
						.add(Restrictions.eq("companyId", companyId))
						.add(Restrictions.eq("vesselImoNo", vesselImo))
						.add(Restrictions.eq("auditSubTypeId", auditSubTypeId))
						.add(Restrictions.eq("auditSeqNo", auditSeqNo))
						.add(Restrictions.eq("publishStatus", 0));
				
				
				List<CertificateIhmDetail> list = deleteCertDetails.list();
				List<String> certList = new ArrayList<>();
				for(int i=0; i< list.size(); i++) {
					certList.add(list.get(i).getCertificateNo());
					
					deleteCertificates = hibsession.getSession().createQuery(AppSQLConstant.DELETE_CERTIFICATES_IHM_AMEND_REISSUE);
					deleteCertificates.setParameter("certIssueId", certIssueId);
					deleteCertificates.setParameter("companyId", companyId);
					deleteCertificates.setParameter("vesselImo", vesselImo);
					deleteCertificates.setParameter("certificateNo", list.get(i).getCertificateNo());
					
					deleteCertificates.executeUpdate();
				}
				
					
				
			}
		}else {
		deleteCertificates = hibsession.getSession().createQuery(AppSQLConstant.DELETE_CERTIFICATES_IHM);
//		deleteCertificates.setParameter("auditSubTypeId", auditSubTypeId);
//		deleteCertificates.setParameter("auditSeqNo", auditSeqNo);
		deleteCertificates.setParameter("certIssueId", certIssueId);
		deleteCertificates.setParameter("companyId", companyId);
		deleteCertificates.setParameter("vesselImo", vesselImo);
		deleteCertificates.setParameterList("certificateId",certIdList);
		deleteCertificates.executeUpdate();
		}
		
		}
		result = "SUCCESS";
	}
	
	}catch(Exception ex) {
		result = "FAILED";
		log.error("deleteCertificateForIhm Exception",ex);
	}
	resultMap.put("result", result);
	return resultMap;
}

@Override
public Map<String,Object> getSocTypeSelectDefault(Integer certIssueId, Long companyId, Integer vesselImo, Integer auditSubTypeId, Integer auditSeqNo, Integer certLink, String socType) {
	
	Map<String,Object> resultMap= new HashMap<String,Object>();
	
	
	try {
		
		List<AuditDetailIhm> auditDetail = null;//new AuditDetail();
		Criteria calcCriteria = hibsession.getSession().createCriteria(AuditDetailIhm.class);
		
		Criterion rest1 = Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY);
		
		Criterion rest2 = Restrictions.eq("auditStatusId",AppConstant.VOID_AUDIT_STATUS);
		
		calcCriteria.add(Restrictions.or(rest1,rest2));
		calcCriteria.add(Restrictions.eq("auditTypeId", AppConstant.IHM_TYPE_ID)).add(Restrictions.eq("companyId", companyId));
		//calcCriteria.add(Restrictions.eq("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY));
		//calcCriteria.add(Restrictions.in("auditSummaryId", new Object[]{AppConstant.NOT_APPROVED_SUMMARY,AppConstant.VOID_AUDIT_STATUS}));
		calcCriteria.add(Restrictions.eq("vesselImoNo", vesselImo)).addOrder(Order.desc("auditSeqNo"));
		calcCriteria.setProjection(Projections.property("auditSeqNo"));
//	    calcCriteria.createCriteria("certificateIhmDetail").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		auditDetail = (List<AuditDetailIhm>) calcCriteria.list();
		
		if(auditSubTypeId == AppConstant.INITIAL_IHM_SUB_TYPE_ID){
		Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
				//.add(Restrictions.eq("auditReportNo", auditReportNo))
				
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselImoNo", vesselImo))
				.add(Restrictions.eq("auditSubTypeId", auditSubTypeId))
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("certificateLink", certLink))
				.addOrder(Order.desc("seqNo"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("socType"))
						);
		if(socType.equals("soc") ){
			certDtl.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
		}
		else if(socType.equals("exe") ){
			certDtl.add(Restrictions.eq("socType", "exe"));
		}
		if(auditDetail.size() != 0 && auditDetail != null) {
			certDtl.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
			}
		
		resultMap.put("result", certDtl.list());
		}
		else { 
			
			
			DetachedCriteria maxId = DetachedCriteria.forClass(CertificateIhmDetail.class)
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("vesselImoNo", vesselImo))
					.add(Restrictions.eq("activeStatus", AppConstant.ACTIVE_STATUS))
					.add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
								.setProjection(Projections.max("certificateLink"));
			
			if(socType.equals("soc") ){
				maxId.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
			}
			else if(socType.equals("exe") ){
				maxId.add(Restrictions.eq("socType", "exe"));
			}
			
			if(auditDetail.size() != 0 && auditDetail != null) {
				maxId.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
				}
			Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
					//.add(Restrictions.eq("auditReportNo", auditReportNo))
				
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("vesselImoNo", vesselImo))
					.add(Restrictions.eq("activeStatus", AppConstant.ACTIVE_STATUS))
					.add(Restrictions.ne("auditSeqNo", AppConstant.IHM_NO_AUD_CERT_AUDITSEQ))
					.add(Property.forName("certificateLink").eq(maxId))
					
					.setProjection(Projections.projectionList()
							.add(Projections.property("socType"))
							);
			if(auditDetail.size() != 0 && auditDetail != null) {
				certDtl.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
				}
			if(socType.equals("soc") ){
				certDtl.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
			}
			else if(socType.equals("exe") ){
				certDtl.add(Restrictions.eq("socType", "exe"));
			}
			
			resultMap.put("result", certDtl.list());
		}
	}catch(Exception ex) {
	
		log.error("deleteCertificateForIhm Exception",ex);
	}
	
	return resultMap;
}

@Override
public Map<String, Object> getAuditCertDetailsForIhm(Integer auditSeqNo, Long companyId) {

	Map<String,Object> resultMap= new HashMap<String,Object>();
	AuditDetailIhm ad = null;
	MaVessel mv = null;
	
	Criteria adtDtl = hibsession.getSession().createCriteria(AuditDetailIhm.class);
	Criteria vslDtl = hibsession.getSession().createCriteria(MaVessel.class);
	
	adtDtl.add(Restrictions.eq("auditSeqNo", auditSeqNo))
		  .add(Restrictions.eq("companyId", companyId));
		  
	
	ad = (AuditDetailIhm) adtDtl.uniqueResult();
	
	if(ad != null){
		
		vslDtl.add(Restrictions.eq("vesselImoNo", ad.getVesselImoNo()))
		  	  .add(Restrictions.eq("companyId", companyId));
		
		mv = (MaVessel) vslDtl.uniqueResult();
		
		if(mv != null){
			
			Criteria vslCoDtl = hibsession.getSession().createCriteria(MaVesselCompany.class);
				
				vslCoDtl.add(Restrictions.eq("companyImoNo", ad.getCompanyImoNo()))
			  	  .add(Restrictions.eq("companyId", companyId));
				
			mv.setVesselCompany((MaVesselCompany) vslCoDtl.uniqueResult());
		}
	}
	
	resultMap.put("auditDetail", ad);
	resultMap.put("vesselDetail", mv);
	
	return resultMap;
}

@Override
public Map<String, Object> getAllCertificateDetailForIhm(Integer auditTypeId, Integer vesselImo, Long companyId, String socType) {
	
	Map<String,Object> resultMap= new HashMap<String,Object>();
	
	List<CertificateIhmDetail> certDtls = null;
	
	int numOfRecors = 0;
	String utn = null; Long certificateId = null;
	
	List<AuditDetailIhm> auditDetail = null;//new AuditDetail();
	Criteria calcCriteria = hibsession.getSession().createCriteria(AuditDetailIhm.class);
	
	Criterion rest1 = Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY);
	
	Criterion rest2 = Restrictions.eq("auditStatusId",AppConstant.VOID_AUDIT_STATUS);
	
	calcCriteria.add(Restrictions.or(rest1,rest2));
	calcCriteria.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId));
	//calcCriteria.add(Restrictions.eq("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY));
	//calcCriteria.add(Restrictions.in("auditSummaryId", new Object[]{AppConstant.NOT_APPROVED_SUMMARY,AppConstant.VOID_AUDIT_STATUS}));
	calcCriteria.add(Restrictions.eq("vesselImoNo", vesselImo)).addOrder(Order.desc("auditSeqNo"));
	calcCriteria.setProjection(Projections.property("auditSeqNo"));
//    calcCriteria.createCriteria("certificateIhmDetail").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	auditDetail = (List<AuditDetailIhm>) calcCriteria.list();
	
	Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class);
	certDtl.add(Restrictions.eq("auditTypeId", auditTypeId));
	certDtl.add(Restrictions.eq("vesselImoNo", vesselImo));
	certDtl.add(Restrictions.eq("companyId", companyId));
	if(auditDetail.size() != 0 && auditDetail != null) {
	certDtl.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
	}
	
	if(socType.equals("soc") ) {
		certDtl.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
	}
	else if(socType.equals("exe") ){
		certDtl.add(Restrictions.eq("socType", "exe"));
	}
	//certDtl.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	certDtl.addOrder(Order.desc("seqNo"));
	//certDtl.addOrder(Order.desc("certificateId"));
	
	resultMap.put("result", certDtl.list());
	
	return resultMap;
}	




@Override
public Map<String, Object> getIntitalCertForDownloadIhm(Integer auditTypeId, Integer vesselImo, Long companyId) {
	
	Map<String,Object> resultMap= new HashMap<String,Object>();
	
	List<CertificateIhmDetail> certDtls = null;
	
	int numOfRecors = 0;
	String utn = null; Long certificateId = null;
	
	List<AuditDetailIhm> auditDetail = null;//new AuditDetail();
	Criteria calcCriteria = hibsession.getSession().createCriteria(AuditDetailIhm.class);
	
	Criterion rest1 = Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY);
	
	Criterion rest2 = Restrictions.eq("auditStatusId",AppConstant.VOID_AUDIT_STATUS);
	
	calcCriteria.add(Restrictions.or(rest1,rest2));
	calcCriteria.add(Restrictions.eq("auditTypeId", auditTypeId)).add(Restrictions.eq("companyId", companyId))
	.add(Restrictions.eq("auditSubTypeId", AppConstant.INITIAL_IHM_SUB_TYPE_ID));
	
	calcCriteria.add(Restrictions.eq("vesselImoNo", vesselImo)).addOrder(Order.asc("auditSeqNo"));
	calcCriteria.setProjection(Projections.property("auditSeqNo"));

	auditDetail = (List<AuditDetailIhm>) calcCriteria.list();
	
	Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class);
	certDtl.add(Restrictions.eq("auditTypeId", auditTypeId));
	certDtl.add(Restrictions.eq("vesselImoNo", vesselImo));
	certDtl.add(Restrictions.eq("companyId", companyId));
	certDtl.add(Restrictions.eq("auditSubTypeId", AppConstant.INITIAL_IHM_SUB_TYPE_ID));
	if(auditDetail.size() != 0 && auditDetail != null) {
	certDtl.add(Restrictions.not(Restrictions.in("auditSeqNo", auditDetail)));
	}

	certDtl.addOrder(Order.asc("certOderNo"));
	
	resultMap.put("result", certDtl.list());
	
	return resultMap;
}	

@Override
public List<CertificateIhmDetail> getAuditCertificateInActiveForIhm(Integer vesselImoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate) {
	
	CertificateIhmDetail certificate = new CertificateIhmDetail();
	
	Criteria criteria = hibsession.getSession().createCriteria(CertificateIhmDetail.class, "audit");
	
	if(certificate.getAuditStatusId() != null)
	{
		criteria.add(Restrictions.ne("audit.auditStatusId", AppConstant.VOID_AUDIT_STATUS));
	}
	
	criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
	.add(Restrictions.eq("audit.companyId", companyId))
	.add(Restrictions.eq("audit.auditTypeId", auditTypeId))
	.add(Restrictions.eq("audit.certificateNo",certificateNo))
	.add(Restrictions.eq("audit.activeStatus",0));
	/*.add(Restrictions.ge("audit.auditDate",auditDate));*/
	List<CertificateIhmDetail> serachList =criteria.list();
	
	
	if(!serachList.isEmpty())
	{
		for(int i=0;i<serachList.size();i++)
		{
			String Cno=serachList.get(i).getCertificateNo();
	        String Utn=serachList.get(i).getUtn();
	        String QidEncode="";
			try 
			{	
				String addCnoUtn=Cno+" "+Utn;
			Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");
			String s= (String) q.uniqueResult();
			QidEncode =s;
				//QidEncode = e.EncodeCertiNoUtn(Cno, Utn);
			} 
			catch (Throwable e)
			{
				e.printStackTrace();
			} 
	        serachList.get(i).setQid(QidEncode);    
		}
	}
	
	
	return serachList;

}

@Override
public Map<String, Object> getCertSearchResultForIhm(SearchCriteria searchCriteria) {
	
	
	Criteria certSrchCr = hibsession.getSession().createCriteria(CertificateIhmDetail.class);
	
	int ihmNumberOfRec = 0;
	if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly){
		certSrchCr.add(Restrictions.eq("auditTypeId",AppConstant.IHM_TYPE_ID));
	  }else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.notIhmUser){
		  certSrchCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
	  }
	
	if(searchCriteria.getVesselImoNo()!=null){
		certSrchCr.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
	}
	if(searchCriteria.getCompanyId()!=null){
		certSrchCr.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
	}
	if(searchCriteria.getCompanyImoNo()!=null){
		certSrchCr.add(Restrictions.eq("companyImoNo", searchCriteria.getCompanyImoNo()));
	}
	if(searchCriteria.getCertIssueDate()!=null){
		certSrchCr.add(Restrictions.ge("certIssueDate", searchCriteria.getCertIssueDate()));
	}
	if(searchCriteria.getCertExpireDate()!=null){
		certSrchCr.add(Restrictions.lt("certExpireDate", searchCriteria.getCertExpireDate()));
	}
	if(searchCriteria.getCertificateNo()!=null){
		certSrchCr.add(Restrictions.eq("certificateNo", searchCriteria.getCertificateNo()));
	}
	if(searchCriteria.getAuditTypeId()!=null){
		certSrchCr.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
	}
	if(searchCriteria.getAuditSubTypeId()!=null){
		certSrchCr.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
	}
		
	if(searchCriteria.getActiveStatus()!=null){
		certSrchCr.add(Restrictions.eq("activeStatus", searchCriteria.getActiveStatus()));
	}
	
	if(searchCriteria.getOfficialNo()!=null){
	
		certSrchCr.add(Restrictions.eq("officialNo", searchCriteria.getOfficialNo()));
	}
	
	if(searchCriteria.getCertIssueId()!=null){
		
		certSrchCr.add(Restrictions.eq("certIssueId", searchCriteria.getCertIssueId()));
	}
	
	if(searchCriteria.getAuditSequenceNo()!=null){
		certSrchCr.add(Restrictions.eq("auditSeqNo", searchCriteria.getAuditSequenceNo()));
		certSrchCr.add(Restrictions.eq("generateStatus", 1));
	}
	
	if(searchCriteria.getCheckCertVthAudit() != null){
		
        if(searchCriteria.getCheckCertVthAudit() == 1000){
        	certSrchCr.add(Restrictions.in("auditSeqNo", new Object[]{AppConstant.ISM_NO_AUD_CERT_AUDITSEQ,AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ,AppConstant.MLC_NO_AUD_CERT_AUDITSEQ,AppConstant.IHM_NO_AUD_CERT_AUDITSEQ}));
        }else{
        	certSrchCr.add(Restrictions.not(Restrictions.in("auditSeqNo", new Object[]{AppConstant.ISM_NO_AUD_CERT_AUDITSEQ,AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ,AppConstant.MLC_NO_AUD_CERT_AUDITSEQ,AppConstant.IHM_NO_AUD_CERT_AUDITSEQ})));	        	
        }
	}
	
	if(searchCriteria.getRoleId()!=null && searchCriteria.getRoleId()==AppConstant.IHM_MANAGER){
		certSrchCr.add(Restrictions.ne("auditTypeId", AppConstant.ISM_TYPE_ID));	
		certSrchCr.add(Restrictions.ne("auditTypeId", AppConstant.ISPS_TYPE_Id));	
		certSrchCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID));	
			
	}
	
	//certSrchCr.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS));
	
	//certSrchCr.addOrder(Order.desc("auditSeqNo"));
	//certSrchCr.addOrder(Order.desc("certificateNo"));
	certSrchCr.addOrder(Order.desc("auditDate"));
	certSrchCr.addOrder(Order.desc("auditSeqNo"));
	certSrchCr.addOrder(Order.desc("certificateId"));
	
	Map<String,Object> resultMap= new HashMap<String,Object>();
	Map<String,Object> smcCertificates= new HashMap<String,Object>();
	@SuppressWarnings("rawtypes")
	List resSerachList = new ArrayList();
	List finalResSerachList = new ArrayList();
	
	List<CertificateIhmDetail> searchList =certSrchCr.list();
	log.info("searchList ="+searchList);
	int index = 0;
	int startRecord = searchCriteria.getPageNo();
	int endRecord = startRecord + searchCriteria.getDefaultSearchCount();
	
	if(startRecord<0){
		
		resultMap.put("result", searchList);
		return resultMap;	
	}
	
	for(CertificateIhmDetail cd : searchList){
		
			resSerachList.add(cd);
	
		}
	ihmNumberOfRec = searchList.size();
	smcCertificates = certificateDaoImpl.getCertSearchResult2(searchCriteria);
	
	for (Entry<String, Object> entry : smcCertificates.entrySet()) {
	    @SuppressWarnings("rawtypes")
		ArrayList list = (ArrayList) entry.getValue();
	    for(int m = 0; m < list.size(); m++) {
	    	resSerachList.add(list.get(m));
	    }
	    System.out.println("list" + list.toString());
	    
	    
	    break;
	}
	
	for(int m = 0; m < resSerachList.size(); m++) {
		if(index >= startRecord && index < endRecord){
    	 finalResSerachList.add(resSerachList.get(m));
    	}
		index++;
    }
	Integer smcCertificatesCout = (Integer) smcCertificates.get("numOfRecords");
	
	resultMap.put("result", finalResSerachList);
	resultMap.put("numOfRecords", ihmNumberOfRec+smcCertificatesCout);
	
	return resultMap;
}

@Override
public Map<String, Object> getCertificateDetailForIhm(Integer auditSeqNo, Long companyId, Integer seqNo,
		Integer activeStatus, String SocType) {
	
	Map<String,Object> resultMap= new HashMap<String,Object>();
	
	List<CertificateIhmDetail> certDtls = null;
	
	int numOfRecors = 0;
	String utn = null; Long certificateId = null;
	
	Criteria certDtl = hibsession.getSession().createCriteria(CertificateIhmDetail.class)
									.add(Restrictions.eq("auditSeqNo", auditSeqNo))
									.add(Restrictions.eq("companyId", companyId))
									.add(Restrictions.eq("seqNo", seqNo));
	if(SocType.equals("soc")) {
		certDtl.add(Restrictions.in("socType", new Object[]{"hk","eu"}));
	}
	else {
		certDtl.add(Restrictions.eq("socType", "exe"));
	}
	resultMap.put("result", certDtl.uniqueResult());
	
	return resultMap;
}


@Override
public Object checkCertificateGenerateIhm(CertificateDetailWithOutAuditIhm certificateDetail) {
	
	if(certificateDetail.getCertificateId() != null && certificateDetail.getCertificateNo() != null && !certificateDetail.getCertificateNo().trim().isEmpty()){
		
		return 1;
	}else{
		 String sql = "SELECT PUBLISH_STATUS FROM CERTIFICATE_IHM_DETAIL WHERE AUDIT_SEQ_NO = :auditSeqNo AND AUDIT_TYPE_ID=:auditTypeId "
				+ "AND VESSEL_IMO_NO= :vesselImo AND COMPANY_ID = :companyId AND PUBLISH_STATUS = :publishStatus  AND ROWNUM=:rowNum ORDER BY SEQ_NO DESC";
		
		Object countVal =  hibsession.getSession().createSQLQuery(sql)
				.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo())
				.setParameter("auditTypeId", certificateDetail.getAuditTypeId())
			    .setParameter("vesselImo", certificateDetail.getVesselImoNo())
				.setParameter("companyId", certificateDetail.getCompanyId())
				.setParameter("publishStatus", 0)
				.setParameter("rowNum", 1)
				.uniqueResult();
		
		return countVal;
		
		
	}
	
}

@Override
public Map<String, Object> generateCertificateWithOutAuditIhm(CertificateDetailWithOutAuditIhm certificateDetail) {
	
	Map<String,Object> resultMap= new HashMap<String,Object>();
	
	RmiIhmCertificates rmiCert = null;
	RmiAuditCertEndorsements rmiCertEndor = null;
	RestTemplate restTemplate = new RestTemplate();
	//HttpHeaders headers = new HttpHeaders();
	HttpEntity<RmiIhmCertificates> rmiCertificatesReq = null;
	HttpEntity<RmiAuditCertEndorsements> rmiAuditCertEndorsementsReq = null;
	ResponseEntity<String> response = null;
	
	
	if(certificateDetail.getPublishStatus() == 0) {
		if(certificateDetail.getDocTypeNumber() != null) {		
			certificateDetail.setDocTypeNo(certificateDetail.getDocTypeNo().replaceAll(certificateDetail.getDocTypeNumber(), "").trim());
		}
		else {
			certificateDetail.setDocTypeNo(null);
		}
		
	Long l=sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ORDER_NO);
	certificateDetail.setCertOderNo(l.intValue());
	certificateDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
	

	if(certificateDetail.getCertIssueId()!=null && (certificateDetail.getCertIssueId() ==AppConstant.RENEWAL_ENDORSED1 ||certificateDetail.getCertIssueId() ==AppConstant.RENEWAL_ENDORSED2|| certificateDetail.getCertIssueId()==AppConstant.ADDITIONAL_SUB_TYPE_ID) ){
		
		if(certificateDetail.getEndorsementID()==null){
			
			certificateDetail.setEndorsementID(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
		}
	}

	 if(certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT || certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE) {
			
			certificateDetail.setCertificateNo(AppUtil.getCertificateNoFormatForIhm(certificateDetail.getAuditTypeId(),sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),certificateDetail.getAuditSubTypeId(),certificateDetail.getSocType()));
		 }
	 else {
		 
		 DetachedCriteria maxId = DetachedCriteria.forClass(CertificateDetailWithOutAuditIhm.class)
				 .add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("socType", certificateDetail.getSocType()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
							.setProjection(Projections.max("seqNo"));
		 
			Criteria certDtl = hibsession.getSession().createCriteria(CertificateDetailWithOutAuditIhm.class)
					//.add(Restrictions.eq("auditReportNo", auditReportNo))
					.add(Restrictions.eq("auditSeqNo", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("socType", certificateDetail.getSocType()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.add(Restrictions.eq("auditSubTypeId", certificateDetail.getAuditSubTypeId()))
					.add(Property.forName("seqNo").eq(maxId));
			  List<CertificateDetailWithOutAuditIhm> ls = certDtl.list();
			
			for(int i = 0; i< ls.size(); i++) {
				CertificateDetailWithOutAuditIhm cert = ls.get(i);
				certificateDetail.setCertificateNo(cert.getCertificateNo());
				certificateDetail.setUtn(cert.getUtn());
			}
			
		 
	 }
	 
	String Cno=certificateDetail.getCertificateNo();
    String Utn=certificateDetail.getUtn();
    String QidEncode="";
    try 
	{
		String addCnoUtn=Cno+" "+Utn;
		Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");			String s= (String) q.uniqueResult();
		QidEncode =s;
		//QidEncode = e.EncodeCertiNoUtn(Cno, Utn);
	}  
	catch (Throwable e) 
	{
		e.printStackTrace();
	}
    certificateDetail.setQid(QidEncode);
            
  //String newQrCodeUrl=certificateDetail.getQrCodeUrl().substring(0, certificateDetail.getQrCodeUrl().indexOf("viewer")+14);
   
    certificateDetail.setQrCodeUrl(AppConstant.cert_url+QidEncode);
    


		Query getMaxCertSeq = hibsession.getSession().createQuery(AppSQLConstant.GET_MAX_CERT_SEQ_IHM);
		getMaxCertSeq.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
		getMaxCertSeq.setLong("companyId", certificateDetail.getCompanyId());
		getMaxCertSeq.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
		//getMaxCertSeq.setString("socType", certificateDetail.getSocType());
		//Added by sudharsan for Jira-ID = IRI-5343
		if(certificateDetail.getSocType().equals("hk")|| certificateDetail.getSocType().equals("eu")) {
			getMaxCertSeq.setString("parm1", "hk");
			getMaxCertSeq.setString("parm2", "eu");
		}
		else {
			getMaxCertSeq.setString("parm1", "exe");
			getMaxCertSeq.setString("parm2", "exe");
		}
		//End here IRI-5343

		int seqNo = 0;

		List list = getMaxCertSeq.list();

		seqNo = (list.isEmpty() || list.get(0)==null) ? 1 : (Integer)list.get(0)+1;

		certificateDetail.setSeqNo(seqNo);
	

	CertificateIhmDetail certDetail = new CertificateIhmDetail();

	BeanUtils.copyProperties(certificateDetail,certDetail);


	//rmiCert = setRmiCertData(certDetail);

	hibsession.getSession().persist(certificateDetail);

	resultMap.put("updatedData",certificateDetail);

	if(certificateDetail.getCertIssueId() != AppConstant.FULL_TERM_CERT){

		Query makeInactiveToRest = null;
		if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE ) {
			 if(certificateDetail.getSocType().equals("hk") || certificateDetail.getSocType().equals("eu")) {
					makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_SOC);
					
			 }else {
				 makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_REISSUE_EXE);
			 }
			 makeInactiveToRest.setInteger("certificateLink", certificateDetail.getCertificateLink());
		}
		else {
			if(certificateDetail.getSocType().equals("hk") || certificateDetail.getSocType().equals("eu")) {
				makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_SOC);
				}else {
					makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_IHM_EXE);
				}
			makeInactiveToRest.setInteger("certIssueId", certificateDetail.getCertIssueId());
		}

		makeInactiveToRest.setInteger("activeStatus", AppConstant.ACTIVE_STATUS);
		makeInactiveToRest.setLong("companyId", certificateDetail.getCompanyId());
		makeInactiveToRest.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
		makeInactiveToRest.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
		makeInactiveToRest.setInteger("auditSubTypeId", certificateDetail.getAuditSubTypeId());
		makeInactiveToRest.setInteger("vesselImoNo", certificateDetail.getVesselImoNo());
		makeInactiveToRest.executeUpdate();

	}

	

	
}
	else {
		//		hibsession.getSession().beginTransaction();
				Query callStoredProcedure = null;
				callStoredProcedure = hibsession.getSession().createSQLQuery("CALL MA_UPDATE_PUBLISH_STATUS_IHM ( :certIssueId , :companyId , :vesselImo , :auditSubTypeId , :auditSeqNo, :socType)")
						.setInteger("certIssueId", certificateDetail.getCertIssueId())
						.setLong("companyId", certificateDetail.getCompanyId())
						.setInteger("vesselImo", certificateDetail.getVesselImoNo())
						.setInteger("auditSubTypeId", certificateDetail.getAuditSubTypeId())
						.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo())
						.setString("socType","soc");;
			
				callStoredProcedure.executeUpdate();
				//hibsession.getSession().getTransaction().commit();
				
				
				resultMap.put("updatedData",certificateDetail);
			}
	return resultMap;
}


//Added by sudharsan and Chibi for JIRA-ID=5377&5378
@Override
public void updateCertificateStatus(Integer auditSeqNo, Long companyId, Integer auditSubTypeId) {
     RestTemplate restTemplate = new RestTemplate();
     String auditType="IHM";
	 ResponseEntity<String> response= null;
	 String IN_ACTIVE= "IN-ACTIVE";

     
     
	 Query query1 =hibsession.getSession().createQuery(" from CertificateIhmDetail where auditSeqNo= :auditSeqNo and companyId= :companyId and auditSubTypeId= :auditSubTypeId");
	 query1.setParameter("auditSeqNo", auditSeqNo);
	 query1.setParameter("companyId", companyId);
	 query1.setParameter("auditSubTypeId", auditSubTypeId);
	 ArrayList<CertificateIhmDetail> certificateIhmdetails = (ArrayList<CertificateIhmDetail>) query1.list();
	 
	 
	 
	 if(certificateIhmdetails.size()>=0){
		 for(CertificateIhmDetail certificateIhmdetails1: certificateIhmdetails){
			 
			 Query updateLocalRMICert =hibsession.getSession().createQuery("update RmiIhmCertificates set certificateStatus= :certificateStatus where reviewSeqNo= :reviewSeqNo");
		     updateLocalRMICert.setParameter("certificateStatus", IN_ACTIVE);
		     updateLocalRMICert.setParameter("reviewSeqNo", auditSeqNo);
		     updateLocalRMICert.executeUpdate();
		     
					RmiIhmCertificates rmiCert = new RmiIhmCertificates();
				rmiCert.setReviewSeqNo(certificateIhmdetails1.getAuditSeqNo());
				rmiCert.setAuditType(certificateIhmdetails1.getAuditTypeDesc());
				rmiCert.setCertificateID(certificateIhmdetails1.getCertificateId());
				rmiCert.setCertificateIssueType(certificateIhmdetails1.getCertIssueDesc());
				rmiCert.setSocType(certificateIhmdetails1.getSocType());
				//added by sudharsan for INCEDENT_TICKET-671 Start here
				RmiAuditCertificate rmiauditCertificate = setRmiauidtCertData(rmiCert);
				
				HttpEntity<RmiAuditCertificate>rmiCertificatesReq = new HttpEntity<RmiAuditCertificate>(rmiauditCertificate, restUtil.getHeaders());
				//HttpEntity<RmiIhmCertificates>rmiCertificatesReq = new HttpEntity<RmiIhmCertificates>(rmiCert, restUtil.getHeaders());
				response = restTemplate.exchange( AppConstant.RMI_URL+"/ws1/MakeVoidCertInactive/"+rmiauditCertificate.getAuditID()+"/"+rmiauditCertificate.getCertificateID()+"/"+auditType+"/"+rmiauditCertificate.getSocType() ,HttpMethod.GET, rmiCertificatesReq, String.class );
				//added by sudharsan for INCEDENT_TICKET-671 End here			
				
		 }
		 
	 }
	 
}
//End here
//added by sudharsan for INCEDENT_TICKET-671 Start here
public RmiAuditCertEndorsementModel setRmiAuditCertEndorsementsModelData(RmiCertEndorsementsIhm rmiCertEndor){
	System.err.println("insidermiauditcertificateforIHM endorsement = "+rmiCertEndor);
	RmiAuditCertEndorsementModel rmiCertEndor_Model = new RmiAuditCertEndorsementModel();
	  
	rmiCertEndor_Model.setAuditID(rmiCertEndor.getAuditID());
	rmiCertEndor_Model.setReviewSeqNo(rmiCertEndor.getAuditID());
		rmiCertEndor_Model.setCertificateID(rmiCertEndor.getCertificateID());
	rmiCertEndor_Model.setEndorsementID(rmiCertEndor.getEndorsementId() );
	rmiCertEndor_Model.setEndorsementDate(rmiCertEndor.getEndorsementDate()!=null?rmiCertEndor.getEndorsementDate().toString():null);
	rmiCertEndor_Model.setEndorsementReason(rmiCertEndor.getEndorsementReason());
	rmiCertEndor_Model.setCreatedBy(rmiCertEndor.getCreatedBy());
	rmiCertEndor_Model.setCreationDate(rmiCertEndor.getCreationDate()!=null?rmiCertEndor.getCreationDate().toString():null);
	rmiCertEndor_Model.setEndorsedBy(rmiCertEndor.getEndorsedBy());
	rmiCertEndor_Model.setLastUpdateDate(rmiCertEndor.getLastUpdateDate()!=null?rmiCertEndor.getLastUpdateDate().toString():null);
	rmiCertEndor_Model.setLastUpdatedBy(rmiCertEndor.getLastUpdatedBy());
	rmiCertEndor_Model.setVesselId(rmiCertEndor.getVesselId());
	rmiCertEndor_Model.setVesselName(rmiCertEndor.getVesselName());
	rmiCertEndor_Model.setExtendDate(rmiCertEndor.getExtendedDate()!=null?rmiCertEndor.getExtendedDate().toString():null);
	rmiCertEndor_Model.setAuditType(rmiCertEndor.getAuditType());
		System.err.println("insidermiauditcertificateforIHM endorsement = "+rmiCertEndor);
	System.err.println("insidermiauditcertificateforIHM endorsement = "+rmiCertEndor_Model);

	return rmiCertEndor_Model;
}
public RmiAuditCertificate setRmiauidtCertData(RmiIhmCertificates rmiCertificates) {
	
	RmiAuditCertificate rmiCert = new RmiAuditCertificate();
	
	rmiCert.setAuditID(rmiCertificates.getReviewSeqNo());
	rmiCert.setReviewSeqNo(rmiCertificates.getReviewSeqNo());
	rmiCert.setCertificateID(rmiCertificates.getCertificateID());
	rmiCert.setCertificateNumber(rmiCertificates.getCertificateNumber());
	rmiCert.setUniqueTrackingNumber(rmiCertificates.getUniqueTrackingNumber());
	rmiCert.setCertificateURL(rmiCertificates.getCertificateURL());
	rmiCert.setCertificateIssueType(rmiCertificates.getCertificateIssueType());
    rmiCert.setIssueDate(rmiCertificates.getIssueDate()!=null?rmiCertificates.getIssueDate().toString():null);
	rmiCert.setExpirationDate(rmiCertificates.getExpirationDate()!=null?rmiCertificates.getExpirationDate().toString():null);
	rmiCert.setIssuedBy(rmiCertificates.getIssuedBy());
	rmiCert.setCertificateStatus(rmiCertificates.getCertificateStatus());
	rmiCert.setVesselName(rmiCertificates.getVesselName());
	rmiCert.setVesselID(rmiCertificates.getVesselID());
	rmiCert.setCreationDate(rmiCertificates.getCreationDate()!=null?rmiCertificates.getCreationDate().toString():null);
	rmiCert.setCreatedBy(rmiCertificates.getCreatedBy());
	rmiCert.setLastUpdateDate(rmiCertificates.getLastUpdateDate()!=null?rmiCertificates.getLastUpdateDate().toString():null);
	rmiCert.setLastUpdatedBy(rmiCertificates.getLastUpdatedBy());
	rmiCert.setExtendDate(rmiCertificates.getExtendDate()!=null?rmiCertificates.getExtendDate().toString():null); 
	rmiCert.setAuditType(rmiCertificates.getAuditType());
	rmiCert.setSocType(rmiCertificates.getSocType());
	rmiCert.setDocumentNo(rmiCertificates.getDocumentNo());
	rmiCert.setSurveryCompletionDate(rmiCertificates.getSurveryCompletionDate()!=null?rmiCertificates.getSurveryCompletionDate().toString():null);
	
	System.err.println("insidermiauditcertificateforIHM = "+rmiCert);
	System.err.println("insidermiauditcertificateforIHM = "+rmiCertificates);
	
	return rmiCert;
}
//added by sudharsan for INCEDENT_TICKET-671 End here
}
