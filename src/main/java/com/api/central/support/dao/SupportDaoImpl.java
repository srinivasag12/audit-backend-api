package com.api.central.support.dao;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SupportDetail;
import com.api.central.config.AppHibSession;
import com.api.central.util.AppConstant;

@Repository
@SuppressWarnings({"unchecked"})
public class SupportDaoImpl implements SupportDao{

	@Autowired
	private AppHibSession hibsession;


	@Override
	public Map<String,String> saveData(SupportDetail supportDetail) {
		hibsession.getSession().merge(supportDetail);
		Map<String,String> m=new HashMap<>();
		m.put("val", "Success");
		return m;
	}

	@Override
	public List<SupportDetail> getSupportData() {

		return hibsession.getSession().createCriteria(SupportDetail.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("auditSeqNo"))
						.add(Projections.property("vesselImoNo"))
						.add(Projections.property("companyId"))
						.add(Projections.property("auditTypeId"))
						.add(Projections.property("auditSubTypeId"))
						.add(Projections.property("auditReportNo"))
						.add(Projections.property("certificateNo"))
						.add(Projections.property("auditDate"))
						.add(Projections.property("companyImoNo"))
						.add(Projections.property("audCertIssueDesc"))
						.add(Projections.property("certificateNo"))
						.add(Projections.property("leadName"))
						).list();

	}

	@Override
	public List<AuditDetailView> getReterivedAudits(SearchCriteria auditDtls) {


		Criteria reterAudSearch = hibsession.getSession().createCriteria(AuditDetailView.class);

		if(auditDtls.getVesselImoNo() !=null){
			reterAudSearch.add(Restrictions.eq("vesselImoNo", auditDtls.getVesselImoNo()));
		}

		if(auditDtls.getAuditTypeId()!=null){
			reterAudSearch.add(Restrictions.eq("auditTypeId", auditDtls.getAuditTypeId()));
		}

		if(auditDtls.getAuditSubTypeId()!=null){
			reterAudSearch.add(Restrictions.eq("auditSubTypeId", auditDtls.getAuditSubTypeId()));
		}
		
		if(auditDtls.getOfficialNo()!=null){
			reterAudSearch.add(Restrictions.eq("officialNo", auditDtls.getOfficialNo()));
		}

		return reterAudSearch.add(Restrictions.eq("userId", auditDtls.getEmailId()))
				.add(Restrictions.eq("companyId", auditDtls.getCompanyId()))
				.add(Restrictions.eq("audLeadStatus", AppConstant.AUD_LEAD_STATUS))
				.add(Restrictions.eq("lockStatus", AppConstant.RETRIEVED_STATUS))
				.add(Restrictions.eq("auditStatusId", AppConstant.COMMENCED_AUDIT_STATUS))
				.add(Restrictions.or(Restrictions.isNull("auditSummaryId"),Restrictions.ne("auditSummaryId", AppConstant.NOT_APPROVED_SUMMARY)))
				.addOrder(Order.desc("auditDate"))
				.addOrder(Order.desc("auditSeqNo"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("auditSeqNo"), "auditSeqNo")
						.add(Projections.property("vesselImoNo"), "vesselImoNo")
						.add(Projections.property("companyId"), "companyId")
						.add(Projections.property("auditTypeId"), "auditTypeId")
						.add(Projections.property("auditSubTypeId"),"auditSubTypeId")
						.add(Projections.property("auditTypeDesc"), "audTypeDesc")
						.add(Projections.property("audSubTypeDesc"),"audSubTypeDesc")
						.add(Projections.property("vesselName"),"vesselName")
						.add(Projections.property("auditReportNo"),"auditReportNo")
						.add(Projections.property("certificateNo"),"certificateNo")
						.add(Projections.property("auditDate"),"auditDate")
						.add(Projections.property("companyImoNo"),"companyImoNo")
						.add(Projections.property("officialNo"),"officialNo")
						.add(Projections.property("audCertIssueDesc"),"audCertIssueDesc")
						.add(Projections.property("certificateNo"),"certificateNo")
						).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public SupportDetail downloadFile(Integer auditSeqNo, Integer vesselImoNo, Integer auditTypeId,
			Integer auditSubTypeId, Long companyId) {
		
		return (SupportDetail) hibsession.getSession().createCriteria(SupportDetail.class)
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("vesselImoNo", vesselImoNo))
				.add(Restrictions.eq("auditTypeId", auditTypeId))
				.add(Restrictions.eq("auditSubTypeId", auditSubTypeId))
				.uniqueResult();
	}

	@Override
	public Map<String,String>  getLeadName(Integer auditSeqNo, Long companyId) {
		Query q = hibsession.getSession().createSQLQuery("SELECT USER_ID FROM AUDIT_AUDITOR_DETAILS WHERE AUDIT_SEQ_NO="+auditSeqNo +" and COMPANY_ID = "+companyId);
		String s= (String) q.uniqueResult();
		
		Map<String,String> m=new HashMap<>();
		m.put("leadName", s);
		return m;
	}

	@Override
	public List<SupportDetail> getSupportAuditDtls(SearchCriteria searchCriteria) {

		Criteria reterAudSearch = hibsession.getSession().createCriteria(SupportDetail.class);

		if(searchCriteria.getVesselImoNo() !=null){
			reterAudSearch.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
		}

		if(searchCriteria.getAuditTypeId()!=null){
			reterAudSearch.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
		}

		if(searchCriteria.getAuditSubTypeId()!=null){
			reterAudSearch.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
		}
		
		if(searchCriteria.getOfficialNo()!=null){
			reterAudSearch.add(Restrictions.eq("officialNo", searchCriteria.getOfficialNo()));
		}
		
		if(searchCriteria.getOfficialNo()!=null){
			reterAudSearch.add(Restrictions.eq("officialNo", searchCriteria.getOfficialNo()));
		}

		if(searchCriteria.getAuditorUserId()!=null){
			reterAudSearch.add(Restrictions.eq("leadId", searchCriteria.getAuditorUserId())); 
		}
		
		return reterAudSearch
				.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()))
				.addOrder(Order.desc("auditSeqNo"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("auditSeqNo"), "auditSeqNo")
						.add(Projections.property("vesselImoNo"), "vesselImoNo")
						.add(Projections.property("companyId"), "companyId")
						.add(Projections.property("auditTypeId"), "auditTypeId")
						.add(Projections.property("auditSubTypeId"),"auditSubTypeId")
						.add(Projections.property("vesselName"),"vesselName")
						.add(Projections.property("officialNo"),"officialNo")
						.add(Projections.property("audTypeDesc"), "audTypeDesc")
						.add(Projections.property("audSubTypeDesc"), "audSubTypeDesc")
						.add(Projections.property("audCertIssueDesc"), "audCertIssueDesc")
						.add(Projections.property("certificateNo"), "certificateNo")
						.add(Projections.property("auditDate"),"auditDate")
						.add(Projections.property("companyImoNo"),"companyImoNo")
						.add(Projections.property("leadName"),"leadName")
						).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	
	}

}
