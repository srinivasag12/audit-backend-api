/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaAttachmentTypesCpk.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaAttachmentTypesCpk.java $
**/
package com.api.central.master.entity;

import java.io.Serializable;

public class MaAttachmentTypesCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long attachmentTypeId;

	private Long companyId;

	private Long auditTypeId;
	
	private Long auditSubTypeId;

	public Long getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Long auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Long getAttachmentTypeId() {
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Long attachmentTypeId) {
		this.attachmentTypeId = attachmentTypeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	

	public Long getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Long auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachmentTypeId == null) ? 0 : attachmentTypeId.hashCode());
		result = prime * result + ((auditSubTypeId == null) ? 0 : auditSubTypeId.hashCode());
		result = prime * result + ((auditTypeId == null) ? 0 : auditTypeId.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
		MaAttachmentTypesCpk other = (MaAttachmentTypesCpk) obj;
		if (attachmentTypeId == null) {
			if (other.attachmentTypeId != null)
				return false;
		} else if (!attachmentTypeId.equals(other.attachmentTypeId))
			return false;
		if (auditSubTypeId == null) {
			if (other.auditSubTypeId != null)
				return false;
		} else if (!auditSubTypeId.equals(other.auditSubTypeId))
			return false;
		if (auditTypeId == null) {
			if (other.auditTypeId != null)
				return false;
		} else if (!auditTypeId.equals(other.auditTypeId))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

	public MaAttachmentTypesCpk() {
		super();
	}

}
