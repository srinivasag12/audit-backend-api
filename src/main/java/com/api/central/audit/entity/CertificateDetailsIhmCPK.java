package com.api.central.audit.entity;
import java.io.Serializable;

public class CertificateDetailsIhmCPK implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private Integer seqNo;
		
		private String socType;
		
		private AuditDetailIhm auditDetail;
		
		public AuditDetailIhm getAuditDetail() {
			return auditDetail;
		}

		public void setAuditDetail(AuditDetailIhm auditDetail) {
			this.auditDetail = auditDetail;
		}

		public Integer getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(Integer seqNo) {
			this.seqNo = seqNo;
		}
		
		public String getSocType() {
			return socType;
		}

		public void setSocType(String socType) {
			this.socType = socType;
		}
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			
			result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
			result = prime * result + ((socType == null) ? 0 : socType.hashCode());
			result = prime * result + ((auditDetail == null) ? 0 : auditDetail.hashCode());
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
			CertificateDetailsIhmCPK other = (CertificateDetailsIhmCPK) obj;
			if (auditDetail == null) {
				if (other.auditDetail != null)
					return false;
			} else if (!auditDetail.equals(other.auditDetail))
				return false;
			if (seqNo == null) {
				if (other.seqNo != null)
					return false;
			} else if (!seqNo.equals(other.seqNo))
				return false;
			if (socType == null) {
				if (other.socType != null)
					return false;
			} else if (!socType.equals(other.socType))
				return false;
			return true;
		}
		
		public CertificateDetailsIhmCPK() {
			super();
		}

	
}