package com.api.central.rmiaudit.entity;

import java.io.Serializable;

public class RmiCertEndorsementsIhmCpk implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long certificateID;
	
	private Long endorsementId;

	public Long getCertificateID() {
		return certificateID;
	}

	public void setCertificateID(Long certificateID) {
		this.certificateID = certificateID;
	}

	public Long getEndorsementId() {
		return endorsementId;
	}

	public void setEndorsementId(Long endorsementId) {
		this.endorsementId = endorsementId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certificateID == null) ? 0 : certificateID.hashCode());
		result = prime * result + ((endorsementId == null) ? 0 : endorsementId.hashCode());
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
		RmiCertEndorsementsIhmCpk other = (RmiCertEndorsementsIhmCpk) obj;
		if (certificateID == null) {
			if (other.certificateID != null)
				return false;
		} else if (!certificateID.equals(other.certificateID))
			return false;
		if (endorsementId == null) {
			if (other.endorsementId != null)
				return false;
		} else if (!endorsementId.equals(other.endorsementId))
			return false;
		return true;
	}
	
	
}
