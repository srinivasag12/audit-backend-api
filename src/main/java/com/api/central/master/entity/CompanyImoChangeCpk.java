package com.api.central.master.entity;

import java.io.Serializable;

public class CompanyImoChangeCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long seqNo;

	private Integer vesselImoNo;

	private Long companyId;

	private String companyImoNo;

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyImoNo == null) ? 0 : companyImoNo.hashCode());
		result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
		result = prime * result + ((vesselImoNo == null) ? 0 : vesselImoNo.hashCode());
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
		CompanyImoChangeCpk other = (CompanyImoChangeCpk) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyImoNo == null) {
			if (other.companyImoNo != null)
				return false;
		} else if (!companyImoNo.equals(other.companyImoNo))
			return false;
		if (seqNo == null) {
			if (other.seqNo != null)
				return false;
		} else if (!seqNo.equals(other.seqNo))
			return false;
		if (vesselImoNo == null) {
			if (other.vesselImoNo != null)
				return false;
		} else if (!vesselImoNo.equals(other.vesselImoNo))
			return false;
		return true;
	}

	public CompanyImoChangeCpk() {
		super();
	}

}
