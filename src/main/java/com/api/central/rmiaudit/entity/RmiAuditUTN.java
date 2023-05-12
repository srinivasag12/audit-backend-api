package com.api.central.rmiaudit.entity;

import java.io.Serializable;

public class RmiAuditUTN implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String utnString;

	/**
	 * @return the utnString
	 */
	public String getUtnString() {
		return utnString;
	}

	/**
	 * @param utnString the utnString to set
	 */
	public void setUtnString(String utnString) {
		this.utnString = utnString;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RmiAuditUTN [utnString=" + utnString + "]";
	}
	
	
}