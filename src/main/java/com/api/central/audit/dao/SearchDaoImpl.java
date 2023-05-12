package com.api.central.audit.dao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CertificateData;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateIhmDetail;
import com.api.central.audit.entity.SearchCriPort;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.VesselStatement;
import com.api.central.certificate.dao.CertificateDao;
import com.api.central.config.AppHibSession;
import com.api.central.master.entity.MaPort;
import com.api.central.util.AppConstant;


@Repository
@SuppressWarnings("unchecked")
public class SearchDaoImpl implements SearchDao {

	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	private CertificateDao certificateDao;
	
	private static final Logger log = LoggerFactory.getLogger(SearchDaoImpl.class);
	
	public Long getSearchCount(SearchCriteria searchCriteria) {		
		
		Criteria audCr = hibsession.getSession().createCriteria(AuditDetailView.class);
		
		if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly && searchCriteria.getPlanApprovalAuthorise()!=AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.eq("auditTypeId",AppConstant.IHM_TYPE_ID));
		}else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly && searchCriteria.getPlanApprovalAuthorise()==AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISM_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISPS_TYPE_Id));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.MLC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SSP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.DMLC_TYPE_ID));
		}else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.notIhmUser && !(searchCriteria.getPlanApprovalAuthorise()>0)){
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SOPEP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.STS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SMPEP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.BWS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.VOC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SDR_TYPE_ID));   // Changed by Sudharsan (SWR to SDR)
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.COW_TYPE_ID));   // Added by Kiran
		}else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.notIhmUser && searchCriteria.getPlanApprovalAuthorise()==AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
		}else if(searchCriteria.getPlanApprovalAuthorise()==AppConstant.onlyPlanAprroval){
			  audCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISM_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISPS_TYPE_Id));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.MLC_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.SSP_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.DMLC_TYPE_ID));
		}
		
		//added by ramya for jira id-->5208 on 31-03-2022
		if(searchCriteria.getRetrieveFlag()==true){
			
			searchCriteria.setRoleId(AppConstant.AUDITOR_ROLE_ID);
			
			audCr.add(Restrictions.not(Restrictions.eq("auditorRoleId", AppConstant.AUDIT_REVIEWER_ROLE_ID)))
				 .add(Restrictions.eq("userId", searchCriteria.getEmailId()))
				 .add(Restrictions.ne("reviewStatus", AppConstant.INITIATE_REVIEW_STATUS))
				 .add(Restrictions.ne("lockStatus", AppConstant.OPEN_FOR_CAR_STATUS))
				 .add(Restrictions.ne("lockStatus", AppConstant.LINKED_WITH_MLC))
				 .add(Restrictions.eq("allowNext", AppConstant.NOTACCEPT_STATUS))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SSP_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SOPEP_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.STS_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SMPEP_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.BWS_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.VOC_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SDR_TYPE_ID))   // Changed by Sudharsan (SWR to SDR)
				 .add(Restrictions.ne("auditTypeId",AppConstant.COW_TYPE_ID))
				 .add(Restrictions.ne("publishStatus",AppConstant.ACCEPT_STATUS))
				 .add(Restrictions.ne("isAuditLockedByManager",AppConstant.MANAGER_ROLE_ID))
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY),Restrictions.isNotNull("auditSummaryId"))))		//added by @Ramya on 18-11-2022 for Jira id -IRI-5544
				 
				 //.add(Restrictions.and(Restrictions.ne("publishStatus",AppConstant.ACCEPT_STATUS),Restrictions.ne("certIssueId",AppConstant.RE_ISSUE)))
				 .add(Restrictions.not( Restrictions.and(Restrictions.isNotNull("leadSign"), Restrictions.eq("auditTypeId", AppConstant.DMLC_TYPE_ID))))
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID),Restrictions.eq("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY))))		//added by ramya for jira id-->IRI-5261
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID),Restrictions.eq("sspLtrStatus", "1"))))
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID),Restrictions.isNull("auditDate"))))		//added by @Ramya for jira id-->IRI-5653
				 .add(Restrictions.ne("auditTypeId", AppConstant.IHM_TYPE_ID));
				 
		}
		if(searchCriteria.getLaptopSearch()!=null)			//added by ramya for jira id-->mobile-192
		{	
		if(searchCriteria.getLaptopSearch()==1){
			audCr.add(Restrictions.ne("lockStatus", 7));
		}
		}
		if(searchCriteria.getAuditorUserId()!=null){
			
			audCr.add(Restrictions.eq("userId", searchCriteria.getAuditorUserId())); 
			
		}else if(searchCriteria.getAuditorRoleId()!=null){
			
			if(searchCriteria.getEmailId()!=null){
				audCr.add(Restrictions.eq("userId", searchCriteria.getEmailId())); 
			}else{
				audCr.add(Restrictions.eq("allowNext", AppConstant.ACCEPT_STATUS));
				audCr.add(Restrictions.eq("audLeadStatus", AppConstant.AUD_LEAD_STATUS));
			}
			
		}else if(searchCriteria.getRoleId() == AppConstant.AUDITOR_ROLE_ID){
			
			
			
			audCr.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS));
			
		}
		
		if(searchCriteria.getRoleId()!=null && (searchCriteria.getRoleId() == AppConstant.ADMIN_ROLE_ID || searchCriteria.getRoleId() == AppConstant.MANAGER_ROLE_ID ||searchCriteria.getRoleId() ==AppConstant.OBSERVER_ROLE_ID )){
		
			if(searchCriteria.getAuditorUserId()==null){
				audCr.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS));
			}
		}
		if(searchCriteria.getPlanApprovalAuthorise() > AppConstant.planAprrovalAuthorised && searchCriteria.getRoleId()!=null){
			if(searchCriteria.getAuditorUserId()==null){
				audCr.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS));
			}
		}
		if(searchCriteria.getVesselImoNo()!=null){
			audCr.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
		}
		if(searchCriteria.getCompanyId()!=null){
			audCr.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
		}
        if(searchCriteria.getOfficialNo()!=null){
			audCr.add(Restrictions.eq("officialNo", searchCriteria.getOfficialNo()));
		}
        if(searchCriteria.getCertIssueDate()!=null && searchCriteria.getAuditTypeId()!=null && searchCriteria.getAuditTypeId()!=AppConstant.SSP_TYPE_ID &&  searchCriteria.getAuditTypeId()!=AppConstant.DMLC_TYPE_ID){
			audCr.add(Restrictions.ge("certIssueDate", searchCriteria.getCertIssueDate()));
		}
		
		if(searchCriteria.getCertIssueDate()!=null && searchCriteria.getAuditTypeId()!=null && (searchCriteria.getAuditTypeId()==AppConstant.SSP_TYPE_ID || searchCriteria.getAuditTypeId()==AppConstant.DMLC_TYPE_ID)){
			audCr.add(Restrictions.ge("auditDate", searchCriteria.getCertIssueDate()));
		}
		if(searchCriteria.getCertExpireDate()!=null){
			audCr.add(Restrictions.lt("certExpireDate", searchCriteria.getCertExpireDate()));
		}
		if(searchCriteria.getCertificateNo()!=null){
			audCr.add(Restrictions.eq("certificateNo", searchCriteria.getCertificateNo()));
		}
		if(searchCriteria.getAuditTypeId()!=null){
			audCr.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
		}
		if(searchCriteria.getAuditSubTypeId()!=null){
			audCr.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
		}
			
		if(searchCriteria.getAuditStatusId()!=null){
			audCr.add(Restrictions.eq("auditStatusId", searchCriteria.getAuditStatusId()));
		}
		
		if(searchCriteria.getScope()!=null){
			audCr.add(Restrictions.eq("scope", searchCriteria.getScope()));
		}
		
		if(searchCriteria.getAuditorRoleId()!=null){
			
			if(searchCriteria.getAuditorRoleId() == 1004){
				
				audCr.add(Restrictions.or( Restrictions.eq("auditTypeId", AppConstant.MLC_TYPE_ID), Restrictions.eq("auditTypeId", AppConstant.DMLC_TYPE_ID)));
				audCr.add(Restrictions.eq("auditorRoleId", AppConstant.AUDITOR_ROLE_ID));
				
			}else if(searchCriteria.getAuditorRoleId() == AppConstant.AUDITOR_ROLE_ID){
				
				audCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID)); 
				audCr.add(Restrictions.ne("auditTypeId", AppConstant.DMLC_TYPE_ID));
				audCr.add(Restrictions.eq("auditorRoleId", AppConstant.AUDITOR_ROLE_ID));
				
			}else if(searchCriteria.getAuditTypeId()!=null && searchCriteria.getAuditTypeId()==AppConstant.IHM_TYPE_ID){		//added by @Ramya for Jira id - IRI-4835
				audCr.add(Restrictions.eq("auditorRoleId", AppConstant.AUDITOR_ROLE_ID ));
			}else{
				audCr.add(Restrictions.eq("auditorRoleId", searchCriteria.getAuditorRoleId()));
			}
		}
		
		if(searchCriteria.getAuditorUserId()!=null){
			audCr.add(Restrictions.eq("userId", searchCriteria.getAuditorUserId()));
		}
		if(searchCriteria.getAuditSeqNo().size()>0 && !searchCriteria.getAuditSeqNo().isEmpty()){
			audCr.add(Restrictions.not(Restrictions.in("auditSeqNo", searchCriteria.getAuditSeqNo())));
		}
		
		
		if(searchCriteria.getRoleId() !=null && searchCriteria.getRoleId()==AppConstant.IHM_MANAGER && searchCriteria.getPlanApprovalAuthorise()!=AppConstant.planAprrovalAuthorised){    //Changed by sudharsan for JIRA-ID=5330,5321  on 21-06-2022
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.DMLC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISM_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISPS_TYPE_Id));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SSP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SOPEP_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.STS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SDR_TYPE_ID));    // Changed by Sudharsan (SWR to SDR)
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.COW_TYPE_ID));		// Added by Kiran
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.BWS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.VOC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SMPEP_TYPE_ID));
		
		}else if(searchCriteria.getRoleId() !=null && searchCriteria.getRoleId()==AppConstant.IHM_MANAGER && searchCriteria.getIhmAuthorise()== AppConstant.ACTIVE_STATUS && searchCriteria.getPlanApprovalAuthorise()==AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.DMLC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISM_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISPS_TYPE_Id));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SSP_TYPE_ID));
					
		}
		
		if(searchCriteria.isLogout()){
			audCr.add(Restrictions.eq("lockHolderId", searchCriteria.getEmailId()));
		}
		
		if(searchCriteria.getLaptopSearch()!=null){
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.IHM_TYPE_ID));
		}
		List<AuditDetailView> resSerachList = new ArrayList<AuditDetailView>();						//added by @Ramya for Jira id - IRI-5584
		Set<AuditDetailView> searchList_set= new LinkedHashSet<AuditDetailView>(audCr.list());
		resSerachList = new ArrayList<AuditDetailView>(searchList_set);
		return new Long(resSerachList.size());
			
	}
	
	@Override
	public List<MaPort> getPortSearchResult(SearchCriPort searchCriteria) {
		
		Criteria maPort;
		if(searchCriteria.getCountryName().isEmpty() && searchCriteria.getPortName().isEmpty()){
			maPort = hibsession.getSession().createCriteria(MaPort.class)
					.add(Restrictions.eq("activeFlag", String.valueOf(searchCriteria.getActiveStatus())))
					.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
		}else if(!searchCriteria.getCountryName().isEmpty() && searchCriteria.getPortName().isEmpty()){
			maPort = hibsession.getSession().createCriteria(MaPort.class)
					.add(Restrictions.eq("activeFlag", String.valueOf(searchCriteria.getActiveStatus())))
					.add(Restrictions.eq("countryName", searchCriteria.getCountryName()))
					.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
		}
		else{
				
			maPort = hibsession.getSession().createCriteria(MaPort.class)
				.add(Restrictions.eq("activeFlag",String.valueOf(searchCriteria.getActiveStatus())))
				.add(Restrictions.eq("countryName", searchCriteria.getCountryName()))
				.add(Restrictions.eq("portId", searchCriteria.getPortId()))
				.add(Restrictions.eq("portName", searchCriteria.getPortName()))
				.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
		}
				
		
		List<MaPort> serachList = maPort.list();
		List<MaPort> resSerachList = new ArrayList<MaPort>();
					
		int index = 0;
		int startRecord = searchCriteria.getPageNo();
		int endRecord = startRecord + searchCriteria.getDefaultSearchCount();
		
		if(startRecord<0){
			
			return serachList;	
		}
		
		for(MaPort av : serachList){
			if(index >= startRecord && index < endRecord){
				resSerachList.add(av);
			}
			index++;
		}
		
		log.info("resSerachList = >> " + resSerachList.toString());
  		return resSerachList;	
	}
	
	@Override
	public List<AuditDetailView> getSearchResult(SearchCriteria searchCriteria) {
		Criteria audCr = hibsession.getSession().createCriteria(AuditDetailView.class);	
		
		if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly && searchCriteria.getPlanApprovalAuthorise()!=AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.eq("auditTypeId",AppConstant.IHM_TYPE_ID));
		}else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly && searchCriteria.getPlanApprovalAuthorise()==AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISM_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISPS_TYPE_Id));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.MLC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SSP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.DMLC_TYPE_ID));
		}else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.notIhmUser && !(searchCriteria.getPlanApprovalAuthorise()>0)){
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SOPEP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.STS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SMPEP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.BWS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.VOC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.SDR_TYPE_ID));   // Changed by Sudharsan (SWR to SDR)
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.COW_TYPE_ID));		// Added by Kiran
		}else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.notIhmUser && searchCriteria.getPlanApprovalAuthorise()==AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
		}else if(searchCriteria.getPlanApprovalAuthorise()==AppConstant.onlyPlanAprroval){
			  audCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISM_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.ISPS_TYPE_Id));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.MLC_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.SSP_TYPE_ID));
				audCr.add(Restrictions.ne("auditTypeId",AppConstant.DMLC_TYPE_ID));
		}
		
		if(searchCriteria.getRetrieveFlag()==true){
			
			searchCriteria.setRoleId(AppConstant.AUDITOR_ROLE_ID);
			
			audCr.add(Restrictions.not(Restrictions.eq("auditorRoleId", AppConstant.AUDIT_REVIEWER_ROLE_ID)))
				 .add(Restrictions.eq("userId", searchCriteria.getEmailId()))
				 .add(Restrictions.ne("reviewStatus", AppConstant.INITIATE_REVIEW_STATUS))
				 .add(Restrictions.ne("lockStatus", AppConstant.OPEN_FOR_CAR_STATUS))
				 .add(Restrictions.ne("lockStatus", AppConstant.LINKED_WITH_MLC))
				 .add(Restrictions.eq("allowNext", AppConstant.NOTACCEPT_STATUS))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SSP_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SOPEP_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.STS_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SMPEP_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.BWS_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.VOC_TYPE_ID))
				 .add(Restrictions.ne("auditTypeId",AppConstant.SDR_TYPE_ID))   // Changed by Sudharsan (SWR to SDR)
				 .add(Restrictions.ne("auditTypeId",AppConstant.COW_TYPE_ID))   // Added by Kiran
				 .add(Restrictions.ne("publishStatus",AppConstant.ACCEPT_STATUS))
				 .add(Restrictions.ne("isAuditLockedByManager",AppConstant.MANAGER_ROLE_ID))
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditSummaryId",AppConstant.NOT_APPROVED_SUMMARY),Restrictions.isNotNull("auditSummaryId"))))		//added by @Ramya on 18-11-2022 for Jira id -IRI-5544
				 
				 //.add(Restrictions.and(Restrictions.ne("publishStatus",AppConstant.ACCEPT_STATUS),Restrictions.ne("certIssueId",AppConstant.RE_ISSUE)))
				 .add(Restrictions.not( Restrictions.and(Restrictions.isNotNull("leadSign"), Restrictions.eq("auditTypeId", AppConstant.DMLC_TYPE_ID))))
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID),Restrictions.eq("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY))))		//added by ramya for jira id-->IRI-5261
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID),Restrictions.eq("sspLtrStatus", "1"))))
				 .add(Restrictions.not(Restrictions.and(Restrictions.eq("auditTypeId",AppConstant.DMLC_TYPE_ID),Restrictions.isNull("auditDate"))))		//added by @Ramya for jira id-->IRI-5653
				 .add(Restrictions.ne("auditTypeId", AppConstant.IHM_TYPE_ID));
				 
		}
		if(searchCriteria.getLaptopSearch()!=null)					//added by ramya for jira id-->mobile-192
		{	
		if(searchCriteria.getLaptopSearch()==1){
			audCr.add(Restrictions.ne("lockStatus", 7));
		}
		}
		
		if(searchCriteria.getAuditorUserId()!=null){
			
			audCr.add(Restrictions.eq("userId", searchCriteria.getAuditorUserId())); 
			
		}else if(searchCriteria.getAuditorRoleId()!=null){
			
			if(searchCriteria.getEmailId()!=null){
				audCr.add(Restrictions.eq("userId", searchCriteria.getEmailId())); 
			}else{
				audCr.add(Restrictions.eq("auditStatusId", AppConstant.AUDIT_STATUS));
				audCr.add(Restrictions.eq("audLeadStatus", AppConstant.AUD_LEAD_STATUS));
			}
			
		}else if(searchCriteria.getRoleId() == AppConstant.AUDITOR_ROLE_ID){
			
			/*if(searchCriteria.getEmailId()!=null){
			Criterion rest1= Restrictions.and(Restrictions.eq("userId", searchCriteria.getUserId()), 
					 Restrictions.eq("auditStatusId", AppConstant.COMMENCED_AUDIT_STATUS));         
				
			Criterion rest1= Restrictions.and(Restrictions.eq("userId", searchCriteria.getEmailId()), 
					Restrictions.or( Restrictions.eq("auditStatusId", AppConstant.COMMENCED_AUDIT_STATUS), Restrictions.eq("auditStatusId", AppConstant.VOID_AUDIT_STATUS),Restrictions.eq("auditStatusId", 1005)));
				
			 //Criterion rest1= Restrictions.eq("userId", searchCriteria.getUserId());
				
			 Criterion rest2= Restrictions.and( Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS),
					 Restrictions.or(Restrictions.eq("auditStatusId", AppConstant.AUDIT_STATUS),Restrictions.eq("auditStatusId", AppConstant.CLOSED_AUDIT_STATUS)));
			
			 audCr.add(Restrictions.or(rest1,rest2));
			}else{
				audCr.add(Restrictions.eq("auditStatusId", AppConstant.AUDIT_STATUS));
				audCr.add(Restrictions.eq("audLeadStatus", AppConstant.AUD_LEAD_STATUS));
			}*/
			audCr.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS));
		}
	
	
		/*if(searchCriteria.getRoleId() != AppConstant.ADMIN_ROLE_ID && searchCriteria.getRoleId() != 1003){
		audCr.add(Restrictions.ne("auditStatusId",AppConstant.VOID_AUDIT_STATUS));
		}*/
	
		if(searchCriteria.getRoleId()!=null && (searchCriteria.getRoleId() == AppConstant.ADMIN_ROLE_ID || searchCriteria.getRoleId() == AppConstant.MANAGER_ROLE_ID || searchCriteria.getRoleId() == AppConstant.OBSERVER_ROLE_ID )){
			if(searchCriteria.getAuditorUserId()==null){
				audCr.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS));
			}
		}
		
		if(searchCriteria.getPlanApprovalAuthorise() > AppConstant.planAprrovalAuthorised && searchCriteria.getRoleId()!=null){
			if(searchCriteria.getAuditorUserId()==null){
				audCr.add(Restrictions.eq("audLeadStatus",AppConstant.AUD_LEAD_STATUS));
			}
		}
		
		if(searchCriteria.getCompanyId()!=null){
			audCr.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
		}
		if(searchCriteria.getVesselImoNo()!=null){
			audCr.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
		}
        if(searchCriteria.getOfficialNo()!=null){
			audCr.add(Restrictions.eq("officialNo", searchCriteria.getOfficialNo()));
		}
        if(searchCriteria.getCertIssueDate()!=null && searchCriteria.getAuditTypeId()!=null &&  searchCriteria.getAuditTypeId()!=AppConstant.SSP_TYPE_ID &&  searchCriteria.getAuditTypeId()!=AppConstant.DMLC_TYPE_ID){
			audCr.add(Restrictions.ge("certIssueDate", searchCriteria.getCertIssueDate()));
		}
		
		if(searchCriteria.getCertIssueDate()!=null && searchCriteria.getAuditTypeId()!=null && (searchCriteria.getAuditTypeId()==AppConstant.SSP_TYPE_ID || searchCriteria.getAuditTypeId()==AppConstant.DMLC_TYPE_ID)){
			audCr.add(Restrictions.ge("auditDate", searchCriteria.getCertIssueDate()));
		}
		if(searchCriteria.getCertExpireDate()!=null){
			audCr.add(Restrictions.lt("certExpireDate", searchCriteria.getCertExpireDate()));
		}
		if(searchCriteria.getCertificateNo()!=null){
			audCr.add(Restrictions.eq("certificateNo", searchCriteria.getCertificateNo()));
		}
		if(searchCriteria.getAuditTypeId()!=null){
			audCr.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
		}
		if(searchCriteria.getAuditSubTypeId()!=null){
			audCr.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
		}
		
		if(searchCriteria.getAuditStatusId()!=null){					
			audCr.add(Restrictions.eq("auditStatusId", searchCriteria.getAuditStatusId()));
		}
		
		if(searchCriteria.getScope()!=null){
			audCr.add(Restrictions.eq("scope", searchCriteria.getScope()));
		}
		
		if(searchCriteria.getAuditorRoleId()!=null){
			if(searchCriteria.getAuditorRoleId() == 1004){
				
				audCr.add(Restrictions.or( Restrictions.eq("auditTypeId", AppConstant.MLC_TYPE_ID), Restrictions.eq("auditTypeId", AppConstant.DMLC_TYPE_ID)));
				audCr.add(Restrictions.eq("auditorRoleId", AppConstant.AUDITOR_ROLE_ID));
				
			}else if(searchCriteria.getAuditorRoleId() == AppConstant.AUDITOR_ROLE_ID){
				
				audCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID)); 
				audCr.add(Restrictions.ne("auditTypeId", AppConstant.DMLC_TYPE_ID));
				audCr.add(Restrictions.eq("auditorRoleId", AppConstant.AUDITOR_ROLE_ID));
				
			}else if(searchCriteria.getAuditTypeId()!=null && searchCriteria.getAuditTypeId()==AppConstant.IHM_TYPE_ID){		//added by @Ramya for Jira id - IRI-4835
				audCr.add(Restrictions.eq("auditorRoleId", AppConstant.AUDITOR_ROLE_ID ));
			}else{
				audCr.add(Restrictions.eq("auditorRoleId", searchCriteria.getAuditorRoleId()));
			}
		}
		
		if(searchCriteria.getAuditorUserId()!=null){
			audCr.add(Restrictions.eq("userId", searchCriteria.getAuditorUserId()));
		}
		
		if(searchCriteria.getAuditSeqNo().size()>0 && !searchCriteria.getAuditSeqNo().isEmpty()){
			audCr.add(Restrictions.not(Restrictions.in("auditSeqNo", searchCriteria.getAuditSeqNo())));
		}
		
		if(searchCriteria.isLogout()){
			audCr.add(Restrictions.eq("lockHolderId", searchCriteria.getEmailId()));
		}
		
	
		if(searchCriteria.getRoleId() !=null && searchCriteria.getRoleId()==AppConstant.IHM_MANAGER && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly){
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.DMLC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISM_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISPS_TYPE_Id));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SSP_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SOPEP_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.STS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SDR_TYPE_ID));    // Changed by Sudharsan (SWR to SDR)
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.COW_TYPE_ID));    // Added by Kiran
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.BWS_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.VOC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SMPEP_TYPE_ID));
		
		}else if(searchCriteria.getRoleId() !=null && searchCriteria.getRoleId()==AppConstant.IHM_MANAGER && searchCriteria.getIhmAuthorise()== AppConstant.ACTIVE_STATUS && searchCriteria.getPlanApprovalAuthorise()==AppConstant.planAprrovalAuthorised){
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.DMLC_TYPE_ID));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISM_TYPE_ID)); 
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.ISPS_TYPE_Id));
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.SSP_TYPE_ID));
					
		}
		
		if(searchCriteria.getShortingBy()!=null){
			
			if(searchCriteria.getShortingOrder()!=null){
				if(searchCriteria.getShortingOrder().equals("asc")){
					audCr.addOrder(Order.asc(searchCriteria.getShortingBy()));
				}else if(searchCriteria.getShortingOrder().equals("desc")){
					audCr.addOrder(Order.desc(searchCriteria.getShortingBy()));
				}
			}else{
				audCr.addOrder(Order.asc("searchCriteria.getShortingBy()"));
			}
		}else{
			audCr.addOrder(Order.desc("auditSeqNo"));		//changed by @Ramya for Jira id - IRI-5671
		}
		
		if(searchCriteria.getLaptopSearch()!=null){
			audCr.add(Restrictions.ne("auditTypeId", AppConstant.IHM_TYPE_ID));
		}
		
		/*if(searchCriteria.getPageNo() != null){
			
			if(searchCriteria.getPageNo()<0){
				
				return audCr.list();	
			}
			
			audCr.setFirstResult(searchCriteria.getPageNo());
		}else{
			audCr.setFirstResult(0);
		}
		
		if(searchCriteria.getDefaultSearchCount() != null){
			audCr.setMaxResults(searchCriteria.getDefaultSearchCount());
		}else{
			audCr.setMaxResults(5);
		}*/
		
		List<AuditDetailView> resSerachList = new ArrayList<AuditDetailView>();
		
		List<AuditDetailView> serachList =audCr.list();
		
		
		
		for(AuditDetailView adv : serachList){
			if(adv.getAuditStatusDesc().equalsIgnoreCase("VOID")) {
				adv.setReviewStatus(4);
			}
		}
		//changed by @Ramya for Jira id - IRI-5584
		Set<AuditDetailView> searchList_set= new LinkedHashSet<AuditDetailView>(serachList);  //Added by sudharsan --check why the duplicate values are coming...
		serachList = new ArrayList<AuditDetailView>(searchList_set);
		int index = 0;
		int startRecord = searchCriteria.getPageNo();
		int endRecord = startRecord + searchCriteria.getDefaultSearchCount();
		
		if(startRecord<0){
			
			return serachList;	
		}
		
		for(AuditDetailView av : serachList){
			if(index >= startRecord && index < endRecord){
				av.setIsSignAttached(av.getLeadSign() != null ? true : false);
				av.setReviewerSign(null);
				av.setLeadSign(null);
				for(int j=0;j<av.getCertificateDetail().size();j++){
			    av.getCertificateDetail().get(j).setSeal(null);
			    av.getCertificateDetail().get(j).setIssuerSign(null);
				}
				
				for(int j=0;j<av.getCertificateIhmDetail().size();j++){
				    av.getCertificateIhmDetail().get(j).setSeal(null);
				    av.getCertificateIhmDetail().get(j).setIssuerSign(null);
					}
				
				resSerachList.add(av);
			}
			index++;
		}
		
  		return resSerachList;
	}

	@Override
	public List<AuditDetail> getAuditCertificate(Long companyId) {
		
		Criteria audCert = hibsession.getSession().createCriteria(CertificateDetail.class)
				.add(Restrictions.eq("companyId", companyId))
				//.setProjection(Projections.property("certificateNo"));
		
				.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("certificateNo"))));
		
		List<AuditDetail> certList = audCert.list();
		return certList;
	}
		
	@Override
	public CertificateData auditCertificateData(CertificateData certificatedata, String status) {
		Query query = null;
		switch (status) {	
		
		case AppConstant.NEW:			
			
			query = hibsession.getSession().createQuery("update AuditDetail set certificateData=:certData where auditSeqNo=:auditSeqNo and auditTypeId=:auditTypeId and companyId=:companyId")
			.setParameter("certData", certificatedata.getPdfData64())
			.setParameter("auditSeqNo", certificatedata.getAuditSeqNo())
			.setParameter("companyId", certificatedata.getCompanyId())
			.setParameter("auditTypeId", certificatedata.getAuditTypeId());			
			query.executeUpdate();
			
			break;
			
		case AppConstant.ENDORSE:
			
			query = hibsession.getSession().createQuery("update AuditDetail set certificateData=:certData where auditSeqNo=:auditSeqNo and auditTypeId=:auditTypeId and companyId=:companyId")
			.setParameter("certData", certificatedata.getPdfData64())
			.setParameter("auditSeqNo", certificatedata.getAuditSeqNo())
			.setParameter("companyId", certificatedata.getCompanyId())
			.setParameter("auditTypeId", certificatedata.getAuditTypeId());			
			query.executeUpdate();
			
			Criteria criteria = hibsession.getSession().createCriteria(AuditDetail.class)
					.setProjection(Projections.projectionList().add(Projections.property("certificateData")))
					.add(Restrictions.eq("vesselImoNo", certificatedata.getVesselImoNo()))
					.add(Restrictions.eq("certificateNo", certificatedata.getCertificateNo()))
					.add(Restrictions.ne("auditSubTypeId", 1003))
					.add(Restrictions.ne("auditSubTypeId", 1005))
					.add(Restrictions.le("auditDate", certificatedata.getAuditDate()))
					.setMaxResults(1)
					.addOrder(Order.desc("auditSeqNo"));
			
			 byte[] data = (byte[]) criteria.uniqueResult();
			 certificatedata.setPdfData64(data);
			break;			
		}
		
		return certificatedata;
		
	}

	@Override
	public List<CertificateData> previousAuditCertificate(int vesselImoNum, String certificateNo) {
		List<CertificateData> prevAuditData = new ArrayList<CertificateData>();
		Criteria criteria = hibsession.getSession().createCriteria(AuditDetail.class)
				.setProjection(Projections.projectionList()
				.add(Projections.property("certificateData")))
				.add(Restrictions.eq("vesselImoNo", vesselImoNum))
				.add(Restrictions.eq("certificateNo", certificateNo))
				.add(Restrictions.ne("auditSubTypeId", 1001))
				.add(Restrictions.ne("auditSubTypeId", 1002))
				.add(Restrictions.ne("auditSubTypeId", 1004));
		criteria.addOrder(Order.asc("auditDate"));
		prevAuditData = criteria.list();

		return prevAuditData;
	}

	@Override
	public List<AuditDetailView> getSearchResultReport(SearchCriteria searchCriteria) {
		Criteria audCr = hibsession.getSession().createCriteria(AuditDetailView.class);				   
		
		if(searchCriteria.getUserId()!=null){
			audCr.add(Restrictions.eq("userId", searchCriteria.getUserId()));
		}
						
		if(searchCriteria.getVesselImoNo()!=null){
			audCr.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
		}
		if(searchCriteria.getCompanyImoNo()!=null){
			audCr.add(Restrictions.eq("companyImoNo", searchCriteria.getCompanyImoNo()));
		}
		if(searchCriteria.getCertIssueDate()!=null){
			audCr.add(Restrictions.ge("certIssueDate", searchCriteria.getCertIssueDate()));
		}
		if(searchCriteria.getCertExpireDate()!=null){
			audCr.add(Restrictions.lt("certExpireDate", searchCriteria.getCertExpireDate()));
		}
		if(searchCriteria.getCertificateNo()!=null){
			audCr.add(Restrictions.eq("certificateNo", searchCriteria.getCertificateNo()));
		}
		if(searchCriteria.getAuditTypeId()!=null){
			audCr.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
		}
		if(searchCriteria.getAuditSubTypeId()!=null){
			audCr.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
		}
		
		if(searchCriteria.getAuditStatusId()!=null){					
			audCr.add(Restrictions.eq("auditStatusId", searchCriteria.getAuditStatusId()));
		}
		
		audCr.addOrder(Order.desc("auditDate"));

		/*if(searchCriteria.getAuditSeqNo().size()>0 && !searchCriteria.getAuditSeqNo().isEmpty()){
			audCr.add(Restrictions.not(Restrictions.in("auditSeqNo", searchCriteria.getAuditSeqNo())));
		}*/
						
		List<AuditDetailView> serachList =audCr.list();
		/*for(AuditDetailView av : serachList){
		}*/
		

      return serachList;
	}


	/*@Override
	public List<AuditDetail> getVesselSearchData(Integer imoNo, Long companyId) {
		LocalDate today = LocalDate.now();
		LocalDate previousYear = today.minus(6, ChronoUnit.YEARS);
		Date date = java.sql.Date.valueOf(previousYear);
		Criteria cr = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("vesselImoNo", imoNo)).add(Restrictions.eq("companyId", companyId))
				.addOrder(Order.asc("auditTypeId")).addOrder(Order.desc("auditDate")).addOrder(Order.desc("auditSubTypeId"))
				.add(Restrictions.ge("auditDate", date));

		List<AuditDetail> serachList = cr.list();
		return serachList;

	}*/
	
	@Override
	public List<VesselStatement> getVesselSearchData(Integer imoNo, Long companyId) {
		LocalDate today = LocalDate.now();
		LocalDate previousYear = today.minus(6, ChronoUnit.YEARS);
		Date date = java.sql.Date.valueOf(previousYear);
		Criteria cr = hibsession.getSession().createCriteria(VesselStatement.class)
				
			.add(Restrictions.eq("vesselImoNo", imoNo)).add(Restrictions.eq("companyId", companyId))
				.addOrder(Order.asc("auditTypeId")).addOrder(Order.desc("auditDate")).addOrder(Order.desc("auditSubTypeId"))
				//.add(Restrictions.ge("certIssueDate", date));
		.add(Restrictions.or(Restrictions.ge("auditDate", date), Restrictions.isNull("auditDate")));
		List<VesselStatement> serachList = cr.list();
		
		
	
		return serachList;

	}
	

	@Override
	public Long getReviewData(SearchCriteria searchCriteria) {
		Criteria revCr = hibsession.getSession().createCriteria(AuditDetailView.class)
				.add(Restrictions.eq("auditorRoleId", 1003))
				.add(Restrictions.eq("userId", searchCriteria.getUserId()))
				.add(Restrictions.not(Restrictions.eq("reviewStatus", 0)))
				.add(Restrictions.not(Restrictions.eq("reviewStatus", 3)));
		return (long) revCr.list().size();
	}
	
	@Override
	public List<AuditDetailView> getReviewrResult(SearchCriteria searchCriteria) {
		Criteria revCr = hibsession.getSession().createCriteria(AuditDetailView.class)
				.add(Restrictions.eq("auditorRoleId", 1003))
				.add(Restrictions.eq("userId", searchCriteria.getUserId()))
				.add(Restrictions.not(Restrictions.eq("reviewStatus", 0)))
				.add(Restrictions.not(Restrictions.eq("reviewStatus", 3)));
		
		List<AuditDetailView> resSerachList = new ArrayList<AuditDetailView>();
		
		List<AuditDetailView> serachList =revCr.list();
		
		int index = 0;
		int startRecord = searchCriteria.getPageNo();
		
		if(startRecord<0){
			return serachList;	
		}
		
		for(AuditDetailView av : serachList){
			if(index >= startRecord && index < startRecord+5){
				resSerachList.add(av);
			}
			index++;
		}
		return resSerachList;
	}

	
	@Override
	public List<AuditDetailView> getLatestCreatedVesselCompanyImo(String emailId,Long companyId) {
		Criteria ltstImoList = hibsession.getSession().createCriteria(AuditDetailView.class,"auditView");				   
		ProjectionList projList = Projections.projectionList();
		projList.add( Projections.distinct( Projections.property( "vesselImoNo" ) ) );
		if(emailId!=null){
			ltstImoList.add(Restrictions.eq("auditView.userId",emailId))
			.setMaxResults(3);
		}
		
		ltstImoList.addOrder(Order.desc("auditSeqNo"));
	
		List<AuditDetailView> lstImoList =ltstImoList.list();
		
		
	
      return lstImoList;
	}

	
	
	
	@Override
	public List<CertificateDetail> getAuditCertificateData(Integer vesselImoNo, Long companyId, String certificateNo,
			Integer auditTypeId, java.util.Date auditDate, boolean directInterorAdd) {
		String query = null;
		List<CertificateDetail> serachList = null;
		CertificateDetail certificate = new CertificateDetail();
		
		Criteria criteria = hibsession.getSession().createCriteria(CertificateDetail.class, "audit");
		
		if(certificate.getAuditStatusId() != null)
		{
			criteria.add(Restrictions.ne("audit.auditStatusId", AppConstant.VOID_AUDIT_STATUS));
		}
		if(auditTypeId == 1006) {
	        criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
	        .add(Restrictions.eq("audit.companyId", companyId))
	        .add(Restrictions.eq("audit.auditTypeId", auditTypeId))
	        .add(Restrictions.eq("audit.certificateNo",certificateNo));
		}
		//added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536
		else if (directInterorAdd) {
			
			
			  query = "SELECT CERT_ORDER_NO "
					+ "FROM CERTIFICATE_DETAIL A INNER JOIN "
					+ "(SELECT MAX(CD.CERTIFICATE_NO) AS MX_CERT_NO, MAX(CD.SEQ_NO) AS MX_SEQ_NO ,MAX(CD.AUDIT_SEQ_NO) AS AUDIT_SEQ_NO "
					+ "FROM CERTIFICATE_DETAIL CD WHERE CD.AUDIT_TYPE_ID = :auditTypeId AND CD.VESSEL_IMO_NO= :vesselImoNo "
					+ "AND COMPANY_ID = :companyId AND CD.CERTIFICATE_NO <= :certificateNo AND  CD.AUDIT_SEQ_NO <> "
					+ AppConstant.ISM_NO_AUD_CERT_AUDITSEQ + " AND CD.AUDIT_SEQ_NO <> "
					+ AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ + "AND CD.AUDIT_SEQ_NO <> "
					+ AppConstant.MLC_NO_AUD_CERT_AUDITSEQ + " AND CD.AUDIT_SEQ_NO <> "
					+ AppConstant.IHM_NO_AUD_CERT_AUDITSEQ
					+ " GROUP BY CD.AUDIT_SEQ_NO) B ON A.CERTIFICATE_NO = B.MX_CERT_NO AND A.SEQ_NO = B.MX_SEQ_NO AND B.AUDIT_SEQ_NO = A.AUDIT_SEQ_NO";
			List<Integer> certOrderList = (
					(List<BigDecimal>) hibsession.getSession()
					.createSQLQuery(query)
					.setParameter("vesselImoNo", vesselImoNo)
					.setParameter("auditTypeId", auditTypeId)
					.setParameter("certificateNo", certificateNo) //Added by sudharsan for Jira-ID IRI-5640,IRI-5641,IRI-5642
					.setParameter("companyId", companyId)
					.list()
					).stream().map(x -> Integer.parseInt(x.toPlainString(), 10)).collect(Collectors.toList());

			criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
					.add(Restrictions.eq("audit.companyId", companyId))
					.add(Restrictions.eq("audit.auditTypeId", auditTypeId))
					.add(Restrictions.le("audit.certificateNo", certificateNo));
					//.add(Restrictions.in("audit.certOderNo", certOrderList)); //Commented by sudharsan for Jira-ID = IRI-5694
			//End here  Jira-ID = 5511 , 5534, 5536, 5536
			
		} else {
			criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
					.add(Restrictions.eq("audit.companyId", companyId))
					.add(Restrictions.eq("audit.auditTypeId", auditTypeId))
					.add(Restrictions.eq("audit.certificateNo", certificateNo));
//					.add(Restrictions.eq("audit.activeStatus", 1)); //Commented by sudharsan for Jira-ID = IRI-5690,IRI-5693,IRI-5673
		}
		serachList = criteria.list();

		if (serachList.size() > 0) {
			Map<String, Object> map = null;
			if(serachList.get(0).getAuditTypeId() == AppConstant.ISPS_TYPE_Id && serachList.get(0).getAuditSubTypeId() == AppConstant.INTERIM_SUB_TYPE_ID){	
				map = certificateDao.getConsective(serachList.get(0).getVesselImoNo(), serachList.get(0).getAuditSeqNo(), serachList.get(0).getCompanyId());
			
				for(int i=0;i<serachList.size();i++){
					if(serachList.get(i).getConsecutiveId() != null){
						if(serachList.get(i).getConsecutiveId() == 1001){
							serachList.get(i).setPrevAuditIssueData(map.get("previousIssueDate"));	
						}
					}
				}
			
			
			}
		}
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
	public List<CertificateIhmDetail> getAuditCertificateDataForIhm(Integer vesselImoNo, Long companyId, String certificateNo,Integer auditTypeId, java.util.Date auditDate) {
		
		CertificateIhmDetail certificate = new CertificateIhmDetail();
		
		Criteria criteria = hibsession.getSession().createCriteria(CertificateIhmDetail.class, "audit");
		
		if(certificate.getAuditStatusId() != null)
		{
			criteria.add(Restrictions.ne("audit.auditStatusId", AppConstant.VOID_AUDIT_STATUS));
		}
		
	        criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
	        .add(Restrictions.eq("audit.companyId", companyId))
	        .add(Restrictions.eq("audit.auditTypeId", auditTypeId))
	        .add(Restrictions.eq("audit.certificateNo",certificateNo));
	        
		
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
	
	/*@Override
	public List<VesselCriteria> getVesselStatus(List<VesselCriteria> searchBean) {
		Criteria audCr = hibsession.getSession().createCriteria(VesselCriteria.class);				   
		List<VesselCriteria> serachList =audCr.list();
		
		if(VesselCriteria.getVesselImoNo()!=null){
			audCr.add(Restrictions.eq("userId", VesselCriteria.getCompanyImoNo()));
		}
						
		if(searchCriteria.getVesselImoNo()!=null){
			audCr.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
		}
		if(searchCriteria.getCompanyImoNo()!=null){
			audCr.add(Restrictions.eq("companyImoNo", searchCriteria.getCompanyImoNo()));
		}
		if(searchCriteria.getCertIssueDate()!=null){
			audCr.add(Restrictions.ge("certIssueDate", searchCriteria.getCertIssueDate()));
		}
		if(searchCriteria.getCertExpireDate()!=null){
			audCr.add(Restrictions.lt("certExpireDate", searchCriteria.getCertExpireDate()));
		}
		if(searchCriteria.getCertificateNo()!=null){
			audCr.add(Restrictions.eq("certificateNo", searchCriteria.getCertificateNo()));
		}
		if(searchCriteria.getAuditTypeId()!=null){
			audCr.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
		}
		if(searchCriteria.getAuditSubTypeId()!=null){
			audCr.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
		}
		
		if(searchCriteria.getAuditStatusId()!=null){					
			audCr.add(Restrictions.eq("auditStatusId", searchCriteria.getAuditStatusId()));
		}
		
		audCr.addOrder(Order.desc("auditDate"));

		if(searchCriteria.getAuditSeqNo().size()>0 && !searchCriteria.getAuditSeqNo().isEmpty()){
			audCr.add(Restrictions.not(Restrictions.in("auditSeqNo", searchCriteria.getAuditSeqNo())));
		}
						
		List<AuditDetailView> serachList =audCr.list();
		for(AuditDetailView av : serachList){
		}
		

      return serachList;
	}*/
	
	

	
}
