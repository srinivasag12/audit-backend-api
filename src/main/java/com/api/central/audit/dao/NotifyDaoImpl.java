package com.api.central.audit.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.api.central.audit.entity.NotifyEmailDtls;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.config.AppHibSession;

@Repository
@SuppressWarnings("unchecked")
public class NotifyDaoImpl implements NotifyDao{


	@Autowired
	private AppHibSession hibsession;

	@Override
	public void changFlag(NotifyEmailDtls notifyEmail){

		hibsession.getSession().createSQLQuery("Update Audit_email_notify_dtls SET "
				+ "to_mail_status_id = CASE WHEN to_mail = :username THEN 0 WHEN to_mail IS NOT NULL AND to_mail_status_id = 1 THEN 1 WHEN to_mail_status_id = 0 THEN 0 END, "
				+ "cc_mail_status_id = CASE WHEN cc_mail = :username THEN 0 WHEN cc_mail IS NOT NULL AND cc_mail_status_id = 1 THEN 1 WHEN cc_mail_status_id = 0 THEN 0 END "
				+ "where email_seq = :emailseq AND mail_seq_no = :mailseq AND company_id = :companyId")
				.setParameter("username", notifyEmail.getUsername())
				.setParameter("emailseq", notifyEmail.getEmailId())
				.setParameter("mailseq", notifyEmail.getMailSeq())
				.setParameter("companyId", notifyEmail.getCompanyId()).executeUpdate();
	}

	@Override
	public Map<String ,Object> getEmailDtls(int emailId, Long companyId) {
		
		List<Map<String ,Object>> notifyDtls = hibsession.getSession().createSQLQuery("select A.FROM_MAIL,A.SUBJECT,A.MESSAGE from AUDIT_EMAIL_LOG_DTLS a where A.EMAIL_SEQ= :emailid AND COMPANY_ID= :companyid")
				.setParameter("emailid", emailId).setParameter("companyid", companyId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		if(notifyDtls.size() ==1 ){
			return notifyDtls.get(0);
		}
		return new HashMap<String ,Object>();
	}
	
	@Override
	public List<Map<String, Object>> emailLogDtls(String username,Long companyid) {

		String sql = "SELECT A.EMAIL_SEQ, B.MAIL_SEQ_NO, :userName as username, A.subject, A.COMPANY_ID, ROW_NUMBER() OVER (ORDER BY A.EMAIL_SEQ  desc) as totrows "
				+ "FROM AUDIT_EMAIL_LOG_DTLS A INNER JOIN AUDIT_EMAIL_NOTIFY_DTLS B ON B.EMAIL_SEQ = A.EMAIL_SEQ AND A.COMPANY_ID = B.COMPANY_ID "
				+ "WHERE (B.to_mail = :userName OR B.cc_mail = :userName) "
				+ "AND (B.to_mail = :userName AND to_mail_status_id = 1) "
				+ "OR (B.cc_mail = :userName AND cc_mail_status_id = 1) AND A.COMPANY_ID = :companyid order by A.EMAIL_SEQ desc";

		List<Map<String ,Object>> notifyDtls = hibsession.getSession().createSQLQuery(sql)
				.setParameter("userName", username)
				.setParameter("companyid", companyid)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		return notifyDtls;
	}
	
	@Override
	public Integer countEmails(String userName, int companyid) {
				
		String sql = "SELECT count(A.EMAIL_SEQ)"
				+ "FROM AUDIT_EMAIL_LOG_DTLS A INNER JOIN AUDIT_EMAIL_NOTIFY_DTLS B ON B.EMAIL_SEQ = A.EMAIL_SEQ AND A.COMPANY_ID = B.COMPANY_ID "
				+ "WHERE (B.to_mail = :userName OR B.cc_mail = :userName) "
				+ "AND (B.to_mail = :userName AND to_mail_status_id = 1) "
				+ "OR (B.cc_mail = :userName AND cc_mail_status_id = 1) AND A.COMPANY_ID = :companyid order by A.EMAIL_SEQ desc";
		
		return Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("userName", userName).setParameter("companyid", companyid).uniqueResult().toString());
	}
	
	@Override
	public List<PartialVesselLog> getRmiDtls() {
		
		return hibsession.getSession().createCriteria(PartialVesselLog.class)
				.add(Restrictions.eq("status", 0)).list();
	}

	@Override
	public void updatePartialStatus(PartialVesselLog partialVesselLog) {
		hibsession.getSession().saveOrUpdate(partialVesselLog);
	}
	
	@Override
	public String getVersionId() {	//changed by @ramya for jira id - IRI-5559
		return hibsession.getSession().createSQLQuery("select max(version_id) as version_id from laptop_versions").uniqueResult().toString();
	}

	@Override
	public Integer updateVersionId(int updateVersionId) {
		hibsession.getSession().createSQLQuery("insert into laptop_versions(version_id) values (:versionId)").setParameter("versionId", updateVersionId).executeUpdate();		
		return updateVersionId;
	}
}