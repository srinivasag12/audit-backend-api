package com.api.central.util;
import java.math.BigDecimal;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.config.AppHibSession;
@Transactional
@Repository 
public class SequenceGenerator {
	
	@Autowired
	AppHibSession hibSession;
			
	public Long generateaSequence(String query){
		
 		Long longId = null;
		 
		Query q = hibSession.getSession().createSQLQuery("SELECT "+query+".nextval from dual");
		BigDecimal id = (BigDecimal) q.list().get(0);
		longId=id.longValue();
 		return longId;
		}
	
	public String generateaSequence(String sequenceName,Long companyId){
		
 		Query q = hibSession.getSession().createSQLQuery("SELECT (SELECT COMPANY_NAME FROM MA_COMPANY WHERE COMPANY_ID= "+ companyId +" ) || ' ' || "+sequenceName+".NEXTVAL FROM DUAL");
		
		return (String) q.list().get(0);
		}
	
	public Integer getLastSequence(String query){
		
	Integer integerId = null;
		 
		Query q = hibSession.getSession().createSQLQuery("SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME ='"+query+"'");
		BigDecimal id = (BigDecimal) q.list().get(0);
		
		integerId = id.intValue();
 		return integerId;
		}
}
    
