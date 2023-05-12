package com.api.central.typeahead.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ParameterMode;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.audit.entity.AuditAuditorDetail;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.config.AppHibSession;
import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditRoles;
import com.api.central.master.entity.MaAuditSearchSource;
import com.api.central.master.entity.MaAuditStatus;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaAuditSummary;
import com.api.central.master.entity.MaAuditType;
import com.api.central.master.entity.MaCertificateIssued;
import com.api.central.master.entity.MaCompany;
import com.api.central.master.entity.MaFindingsCategory;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaIhmExemptionReason;
import com.api.central.master.entity.MaPort;
import com.api.central.master.entity.MaRoles;
import com.api.central.master.entity.MaUserRoles;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.MaVesselType;
import com.api.central.master.entity.MasterTableUpdate;
import com.api.central.master.entity.RefreshMasterData;
import com.api.central.master.entity.UserDetailsConfig;

@Repository
@Transactional(readOnly=true)
@SuppressWarnings("unchecked")
public class TypeaheadDaoImpl implements TypeaheadDao{
	
	@Autowired
	private AppHibSession hibsession;

	private static final Logger log = LoggerFactory.getLogger(TypeaheadDaoImpl.class);
			
	@Override
	public List<MaAuditSubtype> getAuditSubtype(Long auditType) {
		 
			Criteria cr = hibsession.getSession().createCriteria(MaAuditSubtype.class)
					.add(Restrictions.eq("auditTypeId", auditType))
					.add(Restrictions.eq("activeStatus",1));
			List<MaAuditSubtype> subTypeList = cr.list();
			return subTypeList;
		 
	}

	@Override
	public List<MaAuditStatus> getAuditStatus(Long audType) {
		// TODO Auto-generated method stub
		 
			Criteria cri = hibsession.getSession().createCriteria(MaAuditStatus.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("activeStatus",1));
			List<MaAuditStatus> statusList = cri.list();
			return statusList;
		 
	}

	@Override
	public List<MaCertificateIssued> getCertificateIssued(Long audType) {
	 
			Criteria cr= hibsession.getSession().createCriteria(MaCertificateIssued.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("activeStatus",1));
			List<MaCertificateIssued> cerIssuedList = cr.list();
			return cerIssuedList;
		 
	}

	@Override
	public List<MaFindingsCategory> getFindingCategory(Long audType, Long companyId) {
		 
			Criteria cr=hibsession.getSession().createCriteria(MaFindingsCategory.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("companyId",companyId))
					.add(Restrictions.eq("activeStatus",1));
			List<MaFindingsCategory> categoryList = cr.list();
			return categoryList;
		 
	}

	@Override
	public List<MaFindingsStatus> getFindingStatus(Integer audType, Long companyId) {
		 
			Criteria cr=hibsession.getSession().createCriteria(MaFindingsStatus.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("activeStatus",1))
					.addOrder(Order.asc("findingsStatusId"));
			List<MaFindingsStatus> statusList = cr.list();
			return statusList;
		 
	}
	
	@Override
	public List<MaFindingsStatus> getFindingStatus(Long companyId) {
		 
			Criteria cr=hibsession.getSession().createCriteria(MaFindingsStatus.class)
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("activeStatus",1))
					.addOrder(Order.asc("findingsStatusId"));
			List<MaFindingsStatus> statusList = cr.list();
			return statusList;
		 
	}

	@Override
	public List<MaAttachmentTypes> getAttachmentTypes(Long audType) {
	 
			Criteria cr=hibsession.getSession().createCriteria(MaAttachmentTypes.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("activeStatus",1));			
			return cr.list();
		 
	}

	@Override
	public List<MaAuditSummary> getAuditSummary(Long audType) {
		 
			Criteria cr=hibsession.getSession().createCriteria(MaAuditSummary.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("activeStatus",1));
			List<MaAuditSummary> summaryList=cr.list();
			return summaryList;
	 
	}

	@Override
	public List<MaAuditCodes> getAuditCodes(Long audType) {
	 
			Criteria cr=hibsession.getSession().createCriteria(MaAuditCodes.class)
					.add(Restrictions.eq("auditTypeId", audType))
					.add(Restrictions.eq("activeStatus",1));
			List<MaAuditCodes> auditCodesList = cr.list();
			return auditCodesList;
	 
	}

	@Override
	public List<MaVesselCompany> getVesselCompanyDetails(String cImoNo) {
 			Criteria cr=hibsession.getSession().createCriteria(MaVesselCompany.class)
					.add(Restrictions.eq("companyImoNo", cImoNo));
			List<MaVesselCompany> compnyList=cr.list();
			return compnyList;
 	}

	@Override
	public List<MaAuditRoles> getAuditRoles(Long companyId) {
		 
			Criteria cr=hibsession.getSession().createCriteria(MaAuditRoles.class)
					.add(Restrictions.eq("companyId", companyId));
			List<MaAuditRoles> rolesList=cr.list();
			return rolesList;
 	}
	
	
	@Override
	public List<MaAuditRoles> getAuditRoles() {
		 
			Criteria cr=hibsession.getSession().createCriteria(MaAuditRoles.class);
					
			List<MaAuditRoles> rolesList=cr.list();
			return rolesList;
 	}
	
	@Override
	public List<MaAuditSearchSource> getAuditSearchSource(Long companyId){
		Criteria cr=hibsession.getSession().createCriteria(MaAuditSearchSource.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaAuditSearchSource> auditList=cr.list();
		return auditList;
	
	}
	
	@Override
	public List<MaAuditSummary> getallAuditSummary(Long companyId){
		Criteria cr=hibsession.getSession().createCriteria(MaAuditSummary.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaAuditSummary> auditsumList=cr.list();
		return auditsumList;
	
	}
	
	@Override
	public List<MaUsers> getUser(Long companyId) {

		Criteria roles = hibsession.getSession().createCriteria(MaUserRoles.class)
				//.add(Restrictions.eq("activeStatus", 1)).add(Restrictions.eq("roleId", new Long(1001)))
				
				.add(Restrictions.eq("activeStatus", 1)).add(Restrictions.or(Restrictions.eq("roleId", new Long(1001)), Restrictions.eq("roleId", new Long(1004))))	
				
				.setProjection(Projections
						.distinct(Projections.projectionList().add(Projections.property("userRole.emailId"))));

		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class).add(Restrictions.eq("activeStatus", 1))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.in("emailId", roles.list()));
		List<MaUsers> usersList = cr.list();
		for (MaUsers user : usersList) {
			user.setSignature(null);
			Hibernate.initialize(user.getRoles());
		}
		return usersList;

	}

	@Override
	public List<MaAuditStatus> getAllAuditStatus(Long companyId) {
	 
			Criteria cri = hibsession.getSession().createCriteria(MaAuditStatus.class).
					add(Restrictions.eq("companyId", companyId));
			List<MaAuditStatus> statusList = cri.list();
			return statusList;
 	}
	
	
	@Override
	public List<MaAuditStatus> getAllAuditStatus() {
	 
			Criteria cri = hibsession.getSession().createCriteria(MaAuditStatus.class);
				
			List<MaAuditStatus> statusList = cri.list();
			return statusList;
 	}
	@Override
	public List<MaAttachmentTypes> getAllAttachmentTypes(Long companyId) {
		Criteria cri = hibsession.getSession().createCriteria(MaAttachmentTypes.class).
				add(Restrictions.eq("companyId", companyId));
				List<MaAttachmentTypes> attachList = cri.list();
		return attachList;
	}
	
	@Override
	public List<MaIhmExemptionReason> redisUpdateExemptionReasons() {
		Criteria cri = hibsession.getSession().createCriteria(MaIhmExemptionReason.class);
				List<MaIhmExemptionReason> attachList = cri.list();
		return attachList;
	}
	
	
	
	@Override
	public List<MaCertificateIssued> getAllCertificates(Long companyId) {
		Criteria cri = hibsession.getSession().createCriteria(MaCertificateIssued.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaCertificateIssued> certList = cri.list();
		return certList;
	}
	
	@Override
	public List<MaCompany> getAllCompanyDet(Long companyId) {
		Criteria cri = hibsession.getSession().createCriteria(MaCompany.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaCompany> cmpnyList = cri.list();
		return cmpnyList;
	}

	@Override
	public List<MaFindingsStatus> getAllFindingStatus(Long companyId) {
		Criteria cri = hibsession.getSession().createCriteria(MaFindingsStatus.class)
				.add(Restrictions.eq("companyId", companyId));
		List<MaFindingsStatus> findingsList = cri.list();
		return findingsList;
	}
	@Override
	public List<MaAuditSubtype> getAllAuditSubTypes(Long companyId) {
		 
			Criteria cr = hibsession.getSession().createCriteria(MaAuditSubtype.class)
					.add(Restrictions.eq("companyId", companyId));
			
			List<MaAuditSubtype> subTypeList = cr.list();
			
			return subTypeList;
		 
	}

	
	@Override
	public List<MaAuditSubtype> getAllAuditSubTypes() {
		 
			Criteria cr = hibsession.getSession().createCriteria(MaAuditSubtype.class);
					
			
			List<MaAuditSubtype> subTypeList = cr.list();
			
			return subTypeList;
		 
	}
	
	
	@Override
	public Map<String, ArrayList> getDistinctFindingStatus(Long companyId) {
		// TODO Auto-generated method stub
			
					Criteria cr=hibsession.getSession().createCriteria(MaFindingsStatus.class)
							.setProjection(Projections.projectionList()
									.add(Projections.distinct(Projections.property("findingstStatusDesc")))
							        .add(Projections.property("findingsStatusId")))
							        .add(Restrictions.eq("companyId", companyId));
					
					/*Disjunction or = Restrictions.disjunction();
					 or.add(Restrictions.eq("findingsStatusId", 1001));
					 or.add(Restrictions.eq("findingsStatusId", 1005));
					 
					cr.add(or);*/
					
					cr.addOrder(Order.asc("findingsStatusId"));
					        
							/*cr.add(Restrictions.ne("findingsStatusId", 1009));
							cr.add(Restrictions.ne("findingsStatusId", 1007));*/
							
					List<Object[]> rows = cr.list();
					Map<String, ArrayList> map = new HashMap<String, ArrayList>();
					ArrayList al = new ArrayList();
					
					
					
					for (Object[] row : rows) {
						
						Map<String,Object> findSta = new HashMap<String,Object>();
						
						findSta.put("findingsStatusId", row[1]);
						
						findSta.put("findingstStatusDesc",row[0]);

				        al.add(findSta);
					}
					
					map.put("distinctFindingSts",al);
					
					return map;
	}

		
	@Override
	public
	Map<String, Object> getMasterData(String val,Long companyId) {
		
		Map<String,Object> maps = new HashMap<String,Object>();
		
		 Criteria cr=hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.setProjection(Projections.projectionList().add(Projections.property("auditSeqNo")))
				.add(Restrictions.eq("companyId",companyId))
				.add(Restrictions.eq("userId",val));		
				 
		if(cr.list().size()>0){
			
			Criteria crm = hibsession.getSession().createCriteria(AuditDetail.class).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("vesselImoNo"))))
					.add(Restrictions.eq("companyId",companyId))
					.add(Restrictions.in("auditSeqNo", cr.list()));
			
			Criteria vesselCr=hibsession.getSession().createCriteria(MaVessel.class)
					.add(Restrictions.eq("companyId",companyId))
					.add(Restrictions.in("vesselImoNo", crm.list()));
			
			maps.put("MaVessel", vesselCr.list());
			
			vesselCr.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("companyImoNo"))));
		
			Criteria vesselCmpnyCr=hibsession.getSession().createCriteria(MaVesselCompany.class)
					.add(Restrictions.eq("companyId",companyId))
					.add(Restrictions.in("companyImoNo", vesselCr.list()));
			
			maps.put("MaVesselCompany", vesselCmpnyCr.list());
			
		}else{
			
			maps.put("MaVessel", null);
			
			maps.put("MaVesselCompany", null);
			
		}
			        
		return maps;
	
	}

	@Override
	public Map<String, Object> getData(RefreshMasterData refreshMasterData,String flag,Long companyId) {

		Map<String,Object> maps = new HashMap<String,Object>();
		Criteria cr=hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.setProjection(Projections.projectionList().add(Projections.property("auditSeqNo")))
				.add(Restrictions.eq("companyId",companyId))	
				.add(Restrictions.eq("userId",flag));	
		
		//added by @Ramya for Jira id - IRI-5700 - START
		long cri_length= cr.list().size();
		List<?> vessel_check=cr.list();
		Disjunction or = Restrictions.disjunction();
	
		Criteria crm = hibsession.getSession().createCriteria(AuditDetail.class).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("vesselImoNo"))))
				.add(Restrictions.eq("companyId",companyId));
				
				if(cri_length>0){
					if(cri_length>1000){
						List<?> subList = vessel_check.subList(0, 1000);
						while(vessel_check.size()>1000){
							or.add(Restrictions.in("auditSeqNo", subList));
							vessel_check.subList(0, 1000).clear();
							}
						}
					or.add(Restrictions.in("auditSeqNo", vessel_check));
					crm.add(or);
					//added by @Ramya for Jira id - IRI-5700 - END		
		Criteria vesselCr=hibsession.getSession().createCriteria(MaVessel.class)
		.add(Restrictions.eq("companyId",companyId))
		.add(Restrictions.in("vesselImoNo", crm.list()));

		if(refreshMasterData.getVessel()==1)
		{
		
			
			maps.put("MaVessel", vesselCr.list());
		}
		
		
		vesselCr.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("companyImoNo"))));
		
		Criteria vesselCmpnyCr=hibsession.getSession().createCriteria(MaVesselCompany.class)
				.add(Restrictions.eq("companyId",companyId))
				.add(Restrictions.in("companyImoNo", vesselCr.list()));
		
		if(refreshMasterData.getVesselcompany()==1)
		{
			
		
			
			
			
			maps.put("MaVesselCompany", vesselCmpnyCr.list());
			}}
		
		
			else{
				
				maps.put("MaVessel", null);
				maps.put("MaVesselCompany", null);
			}
		
		
			
				
			
			
		
		
		
		
		if(refreshMasterData.getAttachmentTypes()==1)
		{
		Criteria attachmentCr=hibsession.getSession().createCriteria(MaAttachmentTypes.class)
				.add(Restrictions.eq("companyId",companyId))
				.add(Restrictions.eq("activeStatus",1));
		maps.put("MaAttachmentTypes", attachmentCr.list());
		}
		
		if(refreshMasterData.getAuditCodes()==1)
		{
		Criteria auditCodesCr=hibsession.getSession().createCriteria(MaAuditCodes.class)
				.add(Restrictions.eq("companyId",companyId))
				.add(Restrictions.eq("activeStatus",1));
		
		maps.put("MaAuditCodes", auditCodesCr.list());
		}
		
		
		if(refreshMasterData.getAuditSearchSource()==1)
		{
		Criteria auditSearchCr=hibsession.getSession().createCriteria(MaAuditSearchSource.class).add(Restrictions.eq("companyId",companyId))
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaAuditSearchSource", auditSearchCr.list());
		}
		
		
		if(refreshMasterData.getAuditStatus()==1)
		{
		Criteria auditStatusCr=hibsession.getSession().createCriteria(MaAuditStatus.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaAuditStatus", auditStatusCr.list());
		}
		
		
		if(refreshMasterData.getAuditSubtype()==1)
		{
		Criteria auditSubtypeCr=hibsession.getSession().createCriteria(MaAuditSubtype.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaAuditSubtype", auditSubtypeCr.list());
		}
		
		if(refreshMasterData.getAuditSummary()==1)
		{
		Criteria auditSummaryCr=hibsession.getSession().createCriteria(MaAuditSummary.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaAuditSummary", auditSummaryCr.list());
		}
		
		if(refreshMasterData.getAuditType()==1)
		{
		Criteria auditTypeCr=hibsession.getSession().createCriteria(MaAuditType.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaAuditType", auditTypeCr.list());
		}
		
		if(refreshMasterData.getCertificateIssued()==1)
		{
		Criteria certificateIssuedCr=hibsession.getSession().createCriteria(MaCertificateIssued.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaCertificateIssued", certificateIssuedCr.list());
		}
		
		if(refreshMasterData.getCompany()==1)
		{
		Criteria CompanyCr=hibsession.getSession().createCriteria(MaCompany.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaCompany", CompanyCr.list());
		}
		
		if(refreshMasterData.getFindingsCategory()==1)
		{
		Criteria findingsCategoryCr=hibsession.getSession().createCriteria(MaFindingsCategory.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaFindingsCategory", findingsCategoryCr.list());
		}
		
		
		if(refreshMasterData.getFindingsStatus()==1)
		{
		Criteria findingsStatusCr=hibsession.getSession().createCriteria(MaFindingsStatus.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaFindingsStatus", findingsStatusCr.list());
		}
		
		if(refreshMasterData.getRoles()==1)
		{
		Criteria MaRolesCr=hibsession.getSession().createCriteria(MaRoles.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaRoles", MaRolesCr.list());
		}
		
		if(refreshMasterData.getUsers()==1)
		{
		/*Criteria maUsersCr=hibsession.getSession().createCriteria(MaUsers.class)
				.add(Restrictions.eq("emailId",flag))
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaUsers", maUsersCr.list());*/
			
			Criteria users = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
					.add(Restrictions.eq("userIns", flag))
					.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("userId"))));
			System.out.println(users.list().isEmpty());
			Criteria crUser;
			
			if(users.list().isEmpty()){
				crUser=hibsession.getSession().createCriteria(MaUsers.class)
						.add(Restrictions.eq("emailId",flag))
						.add(Restrictions.eq("companyId",companyId));
			}else{
				
				crUser = hibsession.getSession().createCriteria(MaUsers.class)
						.add(Restrictions.eq("activeStatus", 1))
						.add(Restrictions.eq("companyId", companyId))
						.add(Restrictions.in("emailId", users.list()));
				
			}
					
			maps.put("MaUsers", crUser.list());
		
		}
		
		if(refreshMasterData.getVesselType()==1)
		{
		Criteria maVesselTypeCr=hibsession.getSession().createCriteria(MaVesselType.class)
				.add(Restrictions.eq("companyId",companyId));
		maps.put("MaVesselType", maVesselTypeCr.list());
		
		}
		
		if(refreshMasterData.getAuditRoles()==1)
		{
		Criteria auditorRoles = hibsession.getSession().createCriteria(MaAuditRoles.class)
				.add(Restrictions.eq("companyId",companyId));	
		maps.put("maAuditRoles", auditorRoles.list());
		}
		
			
		if(refreshMasterData.getConfigDetails()==1)
		{
		Criteria configDetails = hibsession.getSession().createCriteria(UserDetailsConfig.class)
				.add(Restrictions.eq("emailId",flag))
				.add(Restrictions.eq("companyId",companyId));	
		maps.put("configDetails", configDetails.list());
		}
		
		
		if(refreshMasterData.getPort()==1)
		{
		Criteria port = hibsession.getSession().createCriteria(MaPort.class)
				.add(Restrictions.eq("companyId",companyId));	
		maps.put("port", port.list());
		}
		
		
		Criteria masterTableUpdate = hibsession.getSession().createCriteria(MasterTableUpdate.class)
				.add(Restrictions.eq("companyId",companyId));	
		maps.put("masterTableUpdate", masterTableUpdate.list());
	
		
		
		return maps;
	}

	@Override
	public List<MaFindingsCategory> getFindingCategory() {

		Criteria cr=hibsession.getSession().createCriteria(MaFindingsCategory.class)
				.add(Restrictions.eq("activeStatus",1));
		List<MaFindingsCategory> categoryList = cr.list();
		return categoryList;
	}
	
	@Override
	public List<MaFindingsCategory> getAllFindingCategory(Long companyId) {

		Criteria cr=hibsession.getSession().createCriteria(MaFindingsCategory.class)
				.add(Restrictions.eq("companyId", companyId));
				
		List<MaFindingsCategory> categoryList = cr.list();
		return categoryList;
	}

	@Override
	public List<MaUsers> getAudDetails(Long companyId) {
		
		Criteria roles = hibsession.getSession().createCriteria(MaUserRoles.class)
				.add(Restrictions.eq("activeStatus", 1))
				.setProjection(Projections
						.distinct(Projections.projectionList().add(Projections.property("userRole.emailId"))));

		Criteria cr = hibsession.getSession().createCriteria(MaUsers.class).add(Restrictions.eq("activeStatus", 1))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.in("emailId", roles.list()));
		List<MaUsers> usersList = cr.list();
		for (MaUsers user : usersList) {
			Hibernate.initialize(user.getRoles());
		}
		return usersList;
	}

	@Override
	public List<MasterTableUpdate> updateDbdata(List<MasterTableUpdate> masterTableUpdates) {
		
		for(MasterTableUpdate m:masterTableUpdates){
		
		hibsession.getSession().merge(m);
		
		}
		return masterTableUpdates;
	}

	@Override
	public List<MasterTableUpdate> getUpdatedDbdata(Long companyId) {
		Criteria cr = hibsession.getSession().createCriteria(MasterTableUpdate.class)
				.add(Restrictions.eq("companyId", companyId));
				
		List<MasterTableUpdate> tableList = cr.list();
		return tableList;
	}
	
	@Override
	public List<AuditAuditorDetail> leadAuditorNames(Long companyId) {
		System.out.println(companyId);
		Criteria LeasList = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.add(Restrictions.eq("auditRoleID",  new Long(1001)))
				.add(Restrictions.eq("companyId",companyId));
		List<AuditAuditorDetail> leadAuditorList = LeasList.list();
		return leadAuditorList;
	}

	@Override
	public Map<String, Object> getDashboardData() {
		
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("getDashboardData Called");
		
		List<Map<String ,Object>> currentYearAuditCount  = hibsession.getSession().createSQLQuery("SELECT EXTRACT(YEAR FROM DATE_INS) YEAR, EXTRACT(MONTH FROM DATE_INS) MONTH, COUNT(DISTINCT AUDIT_SEQ_NO) AUDIT_COUNT FROM AUDIT_DETAILS WHERE EXTRACT(YEAR FROM DATE_INS)=TO_CHAR(SYSDATE, 'YYYY') GROUP BY EXTRACT(YEAR FROM DATE_INS), EXTRACT(MONTH FROM DATE_INS)").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String ,Object>> historyOfAuditCount  = hibsession.getSession().createSQLQuery("SELECT EXTRACT(YEAR FROM DATE_INS) YEAR, EXTRACT(MONTH FROM DATE_INS) MONTH, COUNT(DISTINCT AUDIT_SEQ_NO) AUDIT_COUNT FROM AUDIT_DETAILS WHERE EXTRACT(YEAR FROM DATE_INS)<=EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12)) GROUP BY EXTRACT(YEAR FROM DATE_INS), EXTRACT(MONTH FROM DATE_INS)").setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		map.put("currentYearAuditCount", currentYearAuditCount);
		map.put("historyOfAuditCount", historyOfAuditCount);
		
		ProcedureCall query = hibsession.getSession().createStoredProcedureCall("DASHBOARD_DETAIL");
        query.registerParameter(1,Integer.class, ParameterMode.OUT);
        query.registerParameter(2,Integer.class, ParameterMode.OUT);
        query.registerParameter(3,Integer.class, ParameterMode.OUT);
        query.registerParameter(4,Integer.class, ParameterMode.OUT);
        
        query.registerParameter(5,Integer.class, ParameterMode.OUT);
        query.registerParameter(6,Integer.class, ParameterMode.OUT);
        query.registerParameter(7,Integer.class, ParameterMode.OUT);
        query.registerParameter(8,Integer.class, ParameterMode.OUT);
        query.registerParameter(9,Integer.class, ParameterMode.OUT);
        query.registerParameter(10,Integer.class, ParameterMode.OUT);

        ProcedureOutputs procedureOutputs = query.getOutputs();
        map.put("totalNoOfAudits" ,(Integer) procedureOutputs.getOutputParameterValue(1));
		map.put("generatedCertificates", (Integer) procedureOutputs.getOutputParameterValue(2));
		map.put("publishedCertificates", (Integer) procedureOutputs.getOutputParameterValue(3));
		map.put("usersCreated", (Integer) procedureOutputs.getOutputParameterValue(4));
		
		map.put("ismAudit", (Integer) procedureOutputs.getOutputParameterValue(5));
		map.put("ispsAudit", (Integer) procedureOutputs.getOutputParameterValue(6));
		map.put("mlcReview", (Integer) procedureOutputs.getOutputParameterValue(7));
		map.put("sspReview", (Integer) procedureOutputs.getOutputParameterValue(8));
		map.put("dmlcReview", (Integer) procedureOutputs.getOutputParameterValue(9));
		map.put("ihmReview", (Integer) procedureOutputs.getOutputParameterValue(9));

		return map;
	}
}
