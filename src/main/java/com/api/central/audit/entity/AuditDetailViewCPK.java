package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;

public class AuditDetailViewCPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer auditSeqNo;
	
	private Long companyId;
	
	private Integer auditTypeId;

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditSeqNo == null) ? 0 : auditSeqNo.hashCode());
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
		AuditDetailViewCPK other = (AuditDetailViewCPK) obj;
		if (auditSeqNo == null) {
			if (other.auditSeqNo != null)
				return false;
		} else if (!auditSeqNo.equals(other.auditSeqNo))
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

}
