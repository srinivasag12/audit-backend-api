package com.api.central.master.entity;

import java.io.Serializable;

public class DocNumberChangeCpk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long seqNo;

	private String companyImoNo;

	private Long companyId;

	private String docTypeNo;

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getDocTypeNo() {
		return docTypeNo;
	}

	public void setDocTypeNo(String docTypeNo) {
		this.docTypeNo = docTypeNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyImoNo == null) ? 0 : companyImoNo.hashCode());
		result = prime * result + ((docTypeNo == null) ? 0 : docTypeNo.hashCode());
		result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
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
		DocNumberChangeCpk other = (DocNumberChangeCpk) obj;
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
		if (docTypeNo == null) {
			if (other.docTypeNo != null)
				return false;
		} else if (!docTypeNo.equals(other.docTypeNo))
			return false;
		if (seqNo == null) {
			if (other.seqNo != null)
				return false;
		} else if (!seqNo.equals(other.seqNo))
			return false;
		return true;
	}

	public DocNumberChangeCpk() {
		super();
	}

}
