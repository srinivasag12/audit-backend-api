package com.api.central.master.entity;

import java.io.Serializable;

public class UserSessionDetailCPK implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long companyId;
	
	private Integer slNo;	
	
	private String userId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((slNo == null) ? 0 : slNo.hashCode());
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
		UserSessionDetailCPK other = (UserSessionDetailCPK) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (slNo == null) {
			if (other.slNo != null)
				return false;
		} else if (!slNo.equals(other.slNo))
			return false;
		return true;
	}
	public UserSessionDetailCPK() {
		super();
		
	}
	
}
