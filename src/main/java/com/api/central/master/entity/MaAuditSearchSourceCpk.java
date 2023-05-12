/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAuditSearchSourceCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAuditSearchSourceCpk.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

public class MaAuditSearchSourceCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long auditSourceId;

	public Long getAuditSourceId() {
		return auditSourceId;
	}

	public void setAuditSourceId(Long auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public MaAuditSearchSourceCpk() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditSourceId == null) ? 0 : auditSourceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaAuditSearchSourceCpk other = (MaAuditSearchSourceCpk) obj;
		if (auditSourceId == null) {
			if (other.auditSourceId != null)
				return false;
		} else if (!auditSourceId.equals(other.auditSourceId))
			return false;
		return true;
	}

}
