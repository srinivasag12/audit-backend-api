package com.api.central.audit.entity;

import java.io.Serializable;

public class AuditFindingCPK implements Serializable {

	 
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer findingSeqNo;
 	
	private AuditDetail auditDetail;

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}
 
	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditDetail == null) ? 0 : auditDetail.hashCode());
		result = prime * result + ((findingSeqNo == null) ? 0 : findingSeqNo.hashCode());
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
		AuditFindingCPK other = (AuditFindingCPK) obj;
		if (auditDetail == null) {
			if (other.auditDetail != null)
				return false;
		} else if (!auditDetail.equals(other.auditDetail))
			return false;
		if (findingSeqNo == null) {
			if (other.findingSeqNo != null)
				return false;
		} else if (!findingSeqNo.equals(other.findingSeqNo))
			return false;
		return true;
	}

	public AuditFindingCPK() {
		super();
		// TODO Auto-generated constructor stub
	}
 
 

}
