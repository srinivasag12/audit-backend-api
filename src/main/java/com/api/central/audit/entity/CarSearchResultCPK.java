package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;

public class CarSearchResultCPK implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer auditSeqNo;
	
	private Integer findingSeqNo;
	
	private Integer statusSeqNo;
	
	private Long companyId;

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public Integer getStatusSeqNo() {
		return statusSeqNo;
	}

	public void setStatusSeqNo(Integer statusSeqNo) {
		this.statusSeqNo = statusSeqNo;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditSeqNo == null) ? 0 : auditSeqNo.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((findingSeqNo == null) ? 0 : findingSeqNo.hashCode());
		result = prime * result + ((statusSeqNo == null) ? 0 : statusSeqNo.hashCode());
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
		CarSearchResultCPK other = (CarSearchResultCPK) obj;
		if (auditSeqNo == null) {
			if (other.auditSeqNo != null)
				return false;
		} else if (!auditSeqNo.equals(other.auditSeqNo))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (findingSeqNo == null) {
			if (other.findingSeqNo != null)
				return false;
		} else if (!findingSeqNo.equals(other.findingSeqNo))
			return false;
		if (statusSeqNo == null) {
			if (other.statusSeqNo != null)
				return false;
		} else if (!statusSeqNo.equals(other.statusSeqNo))
			return false;
		return true;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public CarSearchResultCPK() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
