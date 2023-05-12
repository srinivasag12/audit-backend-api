package com.api.central.audit.entity;

import java.io.Serializable;

 public class FindingDetailsCPK implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Integer statusSeqNo;
		private Integer currentAuditSeq;
		private AuditFinding auditFinding;


		public Integer getStatusSeqNo() {
			return statusSeqNo;
		}

		public void setStatusSeqNo(Integer statusSeqNo) {
			this.statusSeqNo = statusSeqNo;
		}

		public AuditFinding getAuditFinding() {
			return auditFinding;
		}

		public void setAuditFinding(AuditFinding auditFinding) {
			this.auditFinding = auditFinding;
		}

		public Integer getCurrentAuditSeq() {
			return currentAuditSeq;
		}

		public void setCurrentAuditSeq(Integer currentAuditSeq) {
			this.currentAuditSeq = currentAuditSeq;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((auditFinding == null) ? 0 : auditFinding.hashCode());
			result = prime * result + ((currentAuditSeq == null) ? 0 : currentAuditSeq.hashCode());
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
			FindingDetailsCPK other = (FindingDetailsCPK) obj;
			if (auditFinding == null) {
				if (other.auditFinding != null)
					return false;
			} else if (!auditFinding.equals(other.auditFinding))
				return false;
			if (currentAuditSeq == null) {
				if (other.currentAuditSeq != null)
					return false;
			} else if (!currentAuditSeq.equals(other.currentAuditSeq))
				return false;
			if (statusSeqNo == null) {
				if (other.statusSeqNo != null)
					return false;
			} else if (!statusSeqNo.equals(other.statusSeqNo))
				return false;
			return true;
		}

		
		
	}
