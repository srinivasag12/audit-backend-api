/*added by @Ramya for Jira id - IRI-5633*/
package com.api.central.audit.entity;

public class FindingRptAttachModel {
	
	private Integer fileSeqNo;
 
	private Integer findingSeqNo;
	
	private Integer origAuditSeqNo;
 	 
	private Integer currentAuditSeq;
	
	private Long companyId;
	
	private Integer auditTypeId;
	
	private Integer statusSeqNo;
	
	private String fileName;
	
	private Long ownerFlag;
	
	private String userIns;
	
	private String dateIns;
	
	private byte[] findingFileByte;
	 
	public Integer getFileSeqNo() {
		return fileSeqNo;
	}

	public void setFileSeqNo(Integer fileSeqNo) {
		this.fileSeqNo = fileSeqNo;
	}

	public String getFileName() {
		return fileName;
	}
 
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
 
	public Long getOwnerFlag() {
		return ownerFlag;
	}
 
	public void setOwnerFlag(Long ownerFlag) {
		this.ownerFlag = ownerFlag;
	}
 
	public String getUserIns() {
		return userIns;
	}
 
	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}
 
	public String getDateIns() {
		return dateIns;
	}

 
	public void setDateIns(String dateIns) {
		this.dateIns = dateIns;
	}
 
	public byte[] getFindingFileByte() {
		return findingFileByte;
	}

	public void setFindingFileByte(byte[] findingFileByte) {
		this.findingFileByte = findingFileByte;
	}

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public Integer getOrigAuditSeqNo() {
		return origAuditSeqNo;
	}

	public void setOrigAuditSeqNo(Integer origAuditSeqNo) {
		this.origAuditSeqNo = origAuditSeqNo;
	}

	public Integer getCurrentAuditSeq() {
		return currentAuditSeq;
	}

	public void setCurrentAuditSeq(Integer currentAuditSeq) {
		this.currentAuditSeq = currentAuditSeq;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getStatusSeqNo() {
		return statusSeqNo;
	}

	public void setStatusSeqNo(Integer statusSeqNo) {
		this.statusSeqNo = statusSeqNo;
	}

	
	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public FindingRptAttachModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public String toString() {
		return "FindingRptAttach [fileSeqNo=" + fileSeqNo + ", findingSeqNo=" + findingSeqNo + ", origAuditSeqNo="
				+ origAuditSeqNo + ", currentAuditSeq=" + currentAuditSeq + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + ", statusSeqNo=" + statusSeqNo + ", fileName=" + fileName
				+ ", ownerFlag=" + ownerFlag + "]";
	}*/

	
 
}
