package com.api.central.cycle.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.api.central.audit.entity.AuditCycle;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.config.AppHibSession;
import com.api.central.util.AppConstant;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.api.central.util.AppConstant;

@Repository
@SuppressWarnings({"unused","unchecked","deprecation"})
public class AuditCycleDaoImpl implements AuditCycleDao {
	
	@Autowired
	private AppHibSession hibsession;
	
	@Override
	public Map<String,Object> createAuditCycle(AuditCycle auditCycleData){
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		if(auditCycleData.getCreateOrUpdate()!=null && auditCycleData.getCreateOrUpdate().equals("UPDATE")){
			hibsession.getSession().update(auditCycleData);
		}else if(auditCycleData.getCreateOrUpdate()==null){
		
		hibsession.getSession().saveOrUpdate(auditCycleData);
		
		resultMap.put("updatedData",auditCycleData);
	}else {
          hibsession.getSession().save(auditCycleData);
		
		
		
	}
		resultMap.put("updatedData",auditCycleData);
		
		return resultMap;
		
		
	}
	
	
	
	@Override
	public List<AuditDetail> getAuditCycleHistoryDate( Integer auditTypeId ,Integer auditSubTypeId,Integer vesselImoNo,Long companyId,Integer pageNo,Integer getDefaultSearchCount){
		
		List<AuditDetail> serachList;
		Criteria cr = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))				
				.add(Restrictions.eq("auditTypeId", auditTypeId))
				.add(Restrictions.ne("auditSubTypeId", AppConstant.INTERIM_SUB_TYPE_ID))
				.add(Restrictions.ne("auditSubTypeId", AppConstant.ADDITIONAL_SUB_TYPE_ID))
				.add(Restrictions.ne("auditSubTypeId", AppConstant.INTERMEDIATE_SUB_TYPE_ID))
				.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.ne("certIssueId", AppConstant.RENEWAL_ENDORSED1))
				.add(Restrictions.ne("certIssueId", AppConstant.RENEWAL_ENDORSED2))
				.add(Restrictions.eq("allowNext", 1))
				.add(Restrictions.eq("companyId", companyId));
		         cr.addOrder(Order.desc("auditDate"));
	
             List<AuditDetail> resSerachList = new ArrayList<AuditDetail>();
				serachList =cr.list();
			    
				int index = 0;
				int startRecord =pageNo;
				
				int endRecord = startRecord +getDefaultSearchCount;
				
				if(startRecord<0){
					return serachList;
				}
				for(AuditDetail av : serachList){
					if(index >= startRecord && index < endRecord){
				   resSerachList.add(av);
					}
					index++;
				}
				
		return resSerachList;
	}
	

	@Override
  public List<AuditCycle>  getAuditCycleData(Integer vesselImoNo,Integer auditTypeId,Long companyId)
	{ 
		AuditCycle auditCycleData = new AuditCycle();
		List<AuditCycle> AllData=null;
		Criteria getSearchCount = hibsession.getSession().createCriteria(AuditCycle.class)
				.add(Restrictions.eq("auditTypeId", auditTypeId))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("vesselImoNo", vesselImoNo));
		getSearchCount.addOrder(Order.desc("auditDate"));
		
		 Long getSearchCountval = (long) getSearchCount.list().size();
		
			
			Criteria calculateCycle = hibsession.getSession().createCriteria(AuditCycle.class)
					.add(Restrictions.eq("auditTypeId", auditTypeId))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("vesselImoNo", vesselImoNo))

			        .add(Restrictions.or(Restrictions.isNull("auditStatusId"), Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS)));
			
			        calculateCycle.addOrder(Order.desc("nextRenewal"));
		
			
			      //  Criteria a = 	 calculateCycle.setMaxResults(1);
		          //  auditCycleData =(AuditCycle) a.uniqueResult();
			        AllData = calculateCycle.list();
		     
		            if(getSearchCountval!=0){
		            auditCycleData.setAuditCycleHistirySize(getSearchCountval); 
		            }
		  
		       return  AllData;
		}
	

	@Override
    public AuditCycle  getAuditCreditDate( Integer auditTypeId,Integer auditSubTypeId,Integer vesselImoNo, Long companyId , Integer auditSeqNo){
		 
	   AuditCycle auditCycleData=null;
	   Criteria calculateCycle ;
	   
	     
	        if(auditSeqNo==0){  
	    	       calculateCycle = hibsession.getSession().createCriteria(AuditCycle.class)
						.add(Restrictions.eq("auditTypeId", auditTypeId))
						.add(Restrictions.eq("companyId", companyId))
						.add(Restrictions.eq("vesselImoNo", vesselImoNo));
	    	             calculateCycle.addOrder(Order.desc("cycleGenNo"));
			              
	        }else if(auditSubTypeId==1004){
	    	  
	    	  calculateCycle = hibsession.getSession().createCriteria(AuditCycle.class)
						.add(Restrictions.eq("auditTypeId", auditTypeId))
						.add(Restrictions.eq("companyId", companyId))
						.add(Restrictions.eq("vesselImoNo", vesselImoNo))
						// commented the below two line so that the audit cycle is displayed for renewal after direct intermediate 
						// added 
//						.add(Restrictions.ne("cycleSeqNo", auditSeqNo))
//						.add(Restrictions.le("cycleSeqNo", auditSeqNo));
						.add(Restrictions.eq("cycleSeqNo", auditSeqNo));
	    	             calculateCycle.addOrder(Order.desc("cycleGenNo"));
		    }else {
	     
	             calculateCycle = hibsession.getSession().createCriteria(AuditCycle.class)
					.add(Restrictions.eq("auditTypeId", auditTypeId))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("vesselImoNo", vesselImoNo))
					.add(Restrictions.or( Restrictions.le("cycleSeqNo", auditSeqNo), Restrictions.eq("cycleSeqNo", auditSeqNo)) );
		             calculateCycle.addOrder(Order.desc("cycleGenNo"));
	     
	     
	     }
	     
	     
	     
	     Criteria a = 	 calculateCycle.setMaxResults(1);
	     auditCycleData =(AuditCycle) a.uniqueResult();
	   
	     return auditCycleData;
		
	}
	
	public  List<AuditCycle>  getAllCycleDate( Integer  auditTypeId,  Integer vesselImoNo,  Long companyId){
		List<AuditCycle>  calculateCycle=null;
		 Criteria allData ;
		      allData = hibsession.getSession().createCriteria(AuditCycle.class)
					.add(Restrictions.eq("auditTypeId", auditTypeId))
					.add(Restrictions.eq("companyId", companyId))
					.add(Restrictions.eq("vesselImoNo", vesselImoNo));
		      allData.addOrder(Order.desc("cycleGenNo"));
		      calculateCycle = allData.list();
   	             
		return calculateCycle;
	}
	
}
