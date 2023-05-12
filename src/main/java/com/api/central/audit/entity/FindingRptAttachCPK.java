package com.api.central.audit.entity;
import java.io.Serializable;

public class FindingRptAttachCPK implements Serializable {

		private static final long serialVersionUID = 1L;

		private Integer fileSeqNo;
		
		private FindingDetail findingDetail;
		
		
 	 
		public Integer getFileSeqNo() {
			return fileSeqNo;
		}

		public void setFileSeqNo(Integer fileSeqNo) {
			this.fileSeqNo = fileSeqNo;
		}

		public FindingDetail getFindingDetail() {
			return findingDetail;
		}

		public void setFindingDetail(FindingDetail findingDetail) {
			this.findingDetail = findingDetail;
		}
         
		

		
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fileSeqNo == null) ? 0 : fileSeqNo.hashCode());
			result = prime * result + ((findingDetail == null) ? 0 : findingDetail.hashCode());
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
			FindingRptAttachCPK other = (FindingRptAttachCPK) obj;
			if (fileSeqNo == null) {
				if (other.fileSeqNo != null)
					return false;
			} else if (!fileSeqNo.equals(other.fileSeqNo))
				return false;
			if (findingDetail == null) {
				if (other.findingDetail != null)
					return false;
			} else if (!findingDetail.equals(other.findingDetail))
				return false;
			return true;
		}

		public FindingRptAttachCPK() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}