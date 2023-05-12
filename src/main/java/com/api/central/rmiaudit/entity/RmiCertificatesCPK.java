package com.api.central.rmiaudit.entity;

import java.io.Serializable;

public class RmiCertificatesCPK implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private long certificateID;
		
		private String  certificateNumber;
		
		public long getCertificateID() {
			return certificateID;
		}

		public void setCertificateId(long certificateID) {
			this.certificateID = certificateID;
		}

		public String getCertificateNumber() {
			return certificateNumber;
		}

		public void setCertificateNumber(String certificateNumber) {
			this.certificateNumber = certificateNumber;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (certificateID ^ (certificateID >>> 32));
			result = prime * result + ((certificateNumber == null) ? 0 : certificateNumber.hashCode());
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
			RmiCertificatesCPK other = (RmiCertificatesCPK) obj;
			if (certificateID != other.certificateID)
				return false;
			if (certificateNumber == null) {
				if (other.certificateNumber != null)
					return false;
			} else if (!certificateNumber.equals(other.certificateNumber))
				return false;
			return true;
		}

	}