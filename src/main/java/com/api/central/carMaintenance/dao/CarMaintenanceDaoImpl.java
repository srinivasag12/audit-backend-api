/**
*
*/

package com.api.central.carMaintenance.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.api.central.audit.entity.AuditAuditorDetail;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.CarSearchCriteria;
import com.api.central.audit.entity.CarSearchDetails;
import com.api.central.audit.entity.CarSearchResult;
import com.api.central.audit.entity.FindingDetail;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.config.AppHibSession;
import com.api.central.util.AppConstant;


@Repository
public class CarMaintenanceDaoImpl implements CarMaintenanceDao {

	@Autowired
	private AppHibSession hibsession;
	
	@Override
	public Long getHistorySearchCount(CarSearchCriteria carSearchCriteria) {
		// TODO Auto-generated method stub
	
		Criteria carSrchCr = hibsession.getSession().createCriteria(CarSearchResult.class);
		
		if(carSearchCriteria.getVesselImoNo()!=null){
			carSrchCr.add(Restrictions.eq("vesselImoNo", carSearchCriteria.getVesselImoNo()));
		}
		if(carSearchCriteria.getCompanyId()!=null){
			carSrchCr.add(Restrictions.eq("companyId", carSearchCriteria.getCompanyId()));
		}
		if(carSearchCriteria.getAuditTypeId()!=null){
			carSrchCr.add(Restrictions.eq("auditTypeId", carSearchCriteria.getAuditTypeId()));
		}
		if(carSearchCriteria.getAuditSubTypeId()!=null){
			carSrchCr.add(Restrictions.eq("auditSubTypeId", carSearchCriteria.getAuditSubTypeId()));
		}
		if(carSearchCriteria.getFindingStatusId()!=null){
			carSrchCr.add(Restrictions.eq("findingStatusId", carSearchCriteria.getFindingStatusId()));
		}
		
		if(carSearchCriteria.getCategoryId()!=null){
			
			carSrchCr.add(Restrictions.eq("categoryId", carSearchCriteria.getCategoryId()));
		    }
		
		return new Long(carSrchCr.list().size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CarSearchResult> getHistorySearchResult(CarSearchCriteria carSearchCriteria) {
		// TODO Auto-generated method stub

		Criteria carSrchCr = hibsession.getSession().createCriteria(CarSearchResult.class);
		
		if(carSearchCriteria.getCompanyId()!=null){
			carSrchCr.add(Restrictions.eq("companyId", carSearchCriteria.getCompanyId()));
		}
		if(carSearchCriteria.getVesselImoNo()!=null){
			carSrchCr.add(Restrictions.eq("vesselImoNo", carSearchCriteria.getVesselImoNo()));
		}
		if(carSearchCriteria.getAuditTypeId()!=null){
			carSrchCr.add(Restrictions.eq("auditTypeId", carSearchCriteria.getAuditTypeId()));
		}
		
		if(carSearchCriteria.getAuditSubTypeId()!=null){
			carSrchCr.add(Restrictions.eq("auditSubTypeId", carSearchCriteria.getAuditSubTypeId()));
		}
		
		if(carSearchCriteria.getFindingStatusId()!=null){
			carSrchCr.add(Restrictions.eq("findingStatusId", carSearchCriteria.getFindingStatusId()));
		}
	
		if(carSearchCriteria.getLeadAuditorUserId()!=null){
			carSrchCr.add(Restrictions.eq("leadAuditorUserId", carSearchCriteria.getLeadAuditorUserId()));
		} 
		
        if(carSearchCriteria.getCategoryId()!=null){
		    carSrchCr.add(Restrictions.eq("categoryId", carSearchCriteria.getCategoryId()));
		}
		
        carSrchCr.addOrder(Order.desc("auditSeqNo")).addOrder(Order.desc("findingSeqNo"));		//changed by @Ramya for Jira id - IRI-5671
		
		
		/*if(carSearchCriteria.getPageNo() != null){
			
			if(carSearchCriteria.getPageNo()<0){
				
				return carSrchCr.list();	
			}
			
			carSrchCr.setFirstResult(carSearchCriteria.getPageNo());
		}else{
			carSrchCr.setFirstResult(0);
		}
		
		if(carSearchCriteria.getDefaultSearchCount() != null){
			carSrchCr.setMaxResults(carSearchCriteria.getDefaultSearchCount());
		}else{
			carSrchCr.setMaxResults(5);
		}*/
		
		List<CarSearchResult> carSerachList =carSrchCr.list();
		
		List<CarSearchResult> resCarSerachList = new ArrayList<CarSearchResult>();
		
		int index = 0;
		int startRecord = carSearchCriteria.getPageNo();
		int endRecord = startRecord + carSearchCriteria.getDefaultSearchCount();
		
		if(startRecord<0){
			
				for(CarSearchResult rslt:carSerachList){
					try {
						rslt.setAuditElement(URLDecoder.decode(rslt.getAuditElement(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				return carSerachList;
		}
		
		for(CarSearchResult cr : carSerachList){
			
			if(index >= startRecord && index < endRecord){
				resCarSerachList.add(cr);
			}
			index++;
		}
		
		return resCarSerachList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getHistoryFindingData(Integer auditSeqNo, Integer findingSeqNo) {
		
		Criteria crFind = hibsession.getSession().createCriteria(CarSearchDetails.class)
		        .add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("findingSeqNo", findingSeqNo))
				.addOrder(Order.asc("statusSeqNo"));
		
		Criteria crAudStatus = hibsession.getSession().createCriteria(AuditDetail.class)
			        .add(Restrictions.eq("allowNext", 1))
			        .add(Restrictions.eq("auditSeqNo",auditSeqNo))  // Added by sudharsan for JIRA-ID 5384 on 25-07-2022
					.setProjection(Projections.property("auditSeqNo"));

		Criteria crFindAttach = hibsession.getSession().createCriteria(FindingRptAttach.class)
        .add(Restrictions.eq("findingSeqNo", findingSeqNo));
		
		 Criterion rest1= Restrictions.and(Restrictions.eq("origAuditSeqNo", auditSeqNo), 
		           Restrictions.in("currentAuditSeq", crAudStatus.list()));
		           
		 Criterion rest2= Restrictions.and(Restrictions.eq("currentAuditSeq",600000), 
		           Restrictions.eq("origAuditSeqNo",auditSeqNo));         
		           
		 Criterion rest3= Restrictions.and(Restrictions.eq("currentAuditSeq",auditSeqNo), 
		           Restrictions.eq("origAuditSeqNo", auditSeqNo));
		
		crFindAttach.add(Restrictions.or(rest1, rest2,rest3));
		
		Map<String,Object> carSearchDetail = new HashMap<String,Object>();
		
        carSearchDetail.put("findingList", crFind.list());
        carSearchDetail.put("findingAttachList", crFindAttach.list());
        
		return carSearchDetail;
		
	}

	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> checkAuditCompleted(Integer vesselImoNo, Integer auditTypeId, java.util.Date auditDate, Integer auditStatusId) {
		// TODO Auto-generated method stub
		
		try{
		Criteria crAudit = hibsession.getSession().createCriteria(AuditDetail.class)
		        .add(Restrictions.eq("auditStatusId",auditStatusId))
		        .add(Restrictions.eq("vesselImoNo",vesselImoNo))
		        .add(Restrictions.eq("auditTypeId",auditTypeId))
		        .add(Restrictions.gt("auditDate",auditDate))
		        .setProjection(Projections.rowCount());
		
		Long count = (Long)crAudit.uniqueResult();
		
		Criteria lastSameAudit = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("auditStatusId",AppConstant.COMPLETED_AUDIT_STATUS))
		        .add(Restrictions.eq("vesselImoNo",vesselImoNo))
		        .add(Restrictions.eq("auditTypeId",auditTypeId))
		        .add(Restrictions.ge("auditDate",auditDate))
		        .addOrder(Order.desc("auditSeqNo"))
		        .setProjection(Projections.projectionList()
			            .add(Projections.property("auditSeqNo"))
			            .add(Projections.property("auditDate")));
		
		@SuppressWarnings("unchecked")
		List<Object[]> l = lastSameAudit.list();
		
		/*
		for(Object[] arr : l){
			
				auditSeqNo = arr[0];
				auditDate = arr[1];
		}*/
		
		Map<String, Object> status = 	new HashMap<String, Object>();
		
		status.put("lastAuditDate", l.get(0)[1]);
		
		if(count == 0L){
			status.put("status", true);
			return status;
		}
		status.put("status", false);
		return status;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}}

	@Override
	public FindingDetail updateCarMaintainanceFinding(FindingDetail findingDetail,String updateFlag) {
		
		Query query = hibsession.getSession().createQuery("update AuditFinding set findingStatus=:findingStatus where findingSeqNo=:findingSeqNo and auditSeqNo=:auditSeqNo and companyId=:companyId and auditTypeId=:auditTypeId");
		
		if(findingDetail.getStatusId()==AppConstant.CLOSE_FINDING_STATUS_ID){
			
			if(updateFlag.equals("Delete")){
				query.setParameter("findingStatus", 0);
			}else if(updateFlag.equals("Create") || updateFlag.equals("Update")){
				query.setParameter("findingStatus", 1);
			}
			
			query.setParameter("findingSeqNo", findingDetail.getFindingSeqNo());
			query.setParameter("auditSeqNo", findingDetail.getOrigAuditSeqNo());
			query.setParameter("companyId", findingDetail.getCompanyId());
			query.setParameter("auditTypeId", findingDetail.getAuditTypeId());
			
			query.executeUpdate();
		}
		
		if(updateFlag.equals("Update")){
			
			findingDetail = (FindingDetail) hibsession.getSession().merge(findingDetail);
			
		}else if(updateFlag.equals("Create")){
			
			hibsession.getSession().save(findingDetail);
			
		}else if(updateFlag.equals("Delete")){
			
			hibsession.getSession().delete(findingDetail);
			
		}
		
		return findingDetail;
		
	}

	@Override
	public AuditFinding updateCarMaintainance(AuditFinding auditFinding) {
		
		hibsession.getSession().saveOrUpdate(auditFinding);
		
		return auditFinding;
	}

	@Override
	public Boolean unlinkFindingFiles(FindingRptAttach findingRptAttach) {
		// TODO Auto-generated method stub
		
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

	@SuppressWarnings("unchecked")
	@Override
	public List<CarSearchResult> getHistorySearchResultReport(CarSearchCriteria searchBean) {

		// TODO Auto-generated method stub

		Criteria carSrchCr = hibsession.getSession().createCriteria(CarSearchResult.class);
						
		if(searchBean.getVesselImoNo()!=null){
			carSrchCr.add(Restrictions.eq("vesselImoNo", searchBean.getVesselImoNo()));
		}
		if(searchBean.getAuditTypeId()!=null){
			carSrchCr.add(Restrictions.eq("auditTypeId", searchBean.getAuditTypeId()));
		}
		
		if(searchBean.getAuditSubTypeId()!=null){
			carSrchCr.add(Restrictions.eq("auditSubTypeId", searchBean.getAuditSubTypeId()));
		}
		
		if(searchBean.getFindingStatusId()!=null){
			carSrchCr.add(Restrictions.eq("findingStatusId", searchBean.getFindingStatusId()));
		}
		
		List<CarSearchResult> carSerachList =carSrchCr.list();
		
		for(CarSearchResult rslt:carSerachList){
			try {
				rslt.setAuditElement(URLDecoder.decode(rslt.getAuditElement(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
      return carSerachList;
	
	
	}

	@Override
	public Map<String, Object> checkAuditorAndNextAdtData(Integer auditSeqNo, Long companyId, String userId, Integer findingSeqNo) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		AuditDetail adtDtl = null;
		
		Criteria audAdtrDtl = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("userId", userId));
		
		if(audAdtrDtl.list().size()>0){
			
			Criteria auditDtl = hibsession.getSession().createCriteria(AuditDetail.class)
					.add(Restrictions.eq("auditSeqNo", auditSeqNo))
					.add(Restrictions.eq("companyId", companyId));
			
			Criteria auditFinding = hibsession.getSession().createCriteria(AuditFinding.class)
					.add(Restrictions.eq("auditSeqNo", auditSeqNo))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("findingSeqNo", findingSeqNo));
			
			adtDtl = (AuditDetail) auditDtl.uniqueResult();
			
			if(adtDtl.getAuditTypeId()!=AppConstant.DMLC_TYPE_ID){
				
				Criteria nextAuditDtl = hibsession.getSession().createCriteria(AuditDetail.class)
						.add(Restrictions.eq("vesselImoNo", adtDtl.getVesselImoNo()))
						.add(Restrictions.eq("auditTypeId", adtDtl.getAuditTypeId()))
						.add(Restrictions.gt("auditSeqNo", auditSeqNo))
						.add(Restrictions.eq("companyId", companyId))
						.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
						.add(Restrictions.eq("allowNext", AppConstant.NOTACCEPT_STATUS));
				
				resultMap.put("openAuditDetail",nextAuditDtl.uniqueResult());
			}
			
			
			resultMap.put("status",1);
			resultMap.put("lockStatus",adtDtl.getLockStatus());
			resultMap.put("auditorDetail",audAdtrDtl.uniqueResult());
			resultMap.put("auditFinding",auditFinding.uniqueResult());
		}else{
			resultMap.put("status",0);
		}
		
		return resultMap;
	}
}
