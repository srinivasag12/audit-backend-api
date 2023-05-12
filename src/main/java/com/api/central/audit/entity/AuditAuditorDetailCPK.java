package com.api.central.audit.entity;

import java.io.Serializable;

public class AuditAuditorDetailCPK implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
 		private String userId;
 		private AuditDetail auditDetail;
		public AuditDetail getAuditDetail() {
			return auditDetail;
		}
		public void setAuditDetail(AuditDetail auditDetail) {
			this.auditDetail = auditDetail;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((auditDetail == null) ? 0 : auditDetail.hashCode());
			result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
			AuditAuditorDetailCPK other = (AuditAuditorDetailCPK) obj;
			if (auditDetail == null) {
				if (other.auditDetail != null)
					return false;
			} else if (!auditDetail.equals(other.auditDetail))
				return false;
			if (userId == null) {
				if (other.userId != null)
					return false;
			} else if (!userId.equals(other.userId))
				return false;
			return true;
		}
		 
		 
		
	}