package com.api.central.master.entity;

import java.io.Serializable;

public class MaIhmExemptionReasonCpk implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer reasonId;

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reasonId == null) ? 0 : reasonId.hashCode());
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
		MaIhmExemptionReasonCpk other = (MaIhmExemptionReasonCpk) obj;
		if (reasonId == null) {
			if (other.reasonId != null)
				return false;
		} else if (!reasonId.equals(other.reasonId))
			return false;
		return true;
	}
	
	public MaIhmExemptionReasonCpk() {
		super();

	}
}
