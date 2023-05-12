package com.api.central.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppHibSessionImpl implements AppHibSession {
 
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Session getSession() {
		Session hibSession = this.sessionFactory.getCurrentSession();
		return hibSession;
	}

	@Override
	public Transaction getTransaction(Session hibSession) {
		Transaction tx = hibSession.beginTransaction();
		return tx;
	}

	@Override
	public void commitTransaction(Transaction tx) {
		tx.commit();
	}

	@Override
	public void closeSession(Session hibSession) {
		hibSession.close();
	}

	@Override
	public void clearFlushSession(Session hibSession) {
		hibSession.clear();
		hibSession.flush();
	}
}
