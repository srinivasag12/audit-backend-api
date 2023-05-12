package com.api.central.audit.entity;
import java.io.Serializable;

public class AuditRptAttachCPK implements Serializable{

		private static final long serialVersionUID = 1L;

		private Integer seqNo;
		private AuditDetail auditDetail;

	 
		 

		public Integer getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(Integer seqNo) {
			this.seqNo = seqNo;
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
			AuditRptAttachCPK other = (AuditRptAttachCPK) obj;
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
			return true;
		}

		
 
	}