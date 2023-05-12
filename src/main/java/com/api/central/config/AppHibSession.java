package com.api.central.config;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface AppHibSession {

	public Session getSession();

	public Transaction getTransaction(Session hibSession);

	public void commitTransaction(Transaction tx);

	public void closeSession(Session hibSession);

	public void clearFlushSession(Session hibSession);
}
