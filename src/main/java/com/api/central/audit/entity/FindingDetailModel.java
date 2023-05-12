/*added by @Ramya for Jira id - IRI-5633*/
package com.api.central.audit.entity;

import java.util.ArrayList;
import java.util.List;

public class FindingDetailModel {
	private Integer statusSeqNo;
	
	private Integer findingSeqNo;
	
	private Integer origAuditSeqNo;
 	 
	private Integer currentAuditSeq;
	
	private Long companyId;
	
	private Integer auditTypeId;
 
 	private Integer categoryId;
	
	private Integer statusId;
	
	private String statusDate;
	
 	private Integer nextActionId;
	
	private String dueDate;
	
	private String descriptions;
	
	private String userIns;
	
	private String dateIns;

	private String updateDescription;
	
	private String auditPlace;
	
	private List<FindingRptAttachModel> findingRptAttachs = new ArrayList<FindingRptAttachModel>();

	private String catagoryDesc;
	
	private String statusDesc;
	
	private String nextActionDesc;
 
	private String updatedTypeId;
	
	private Integer vesselImoNo;

	public Integer getStatusSeqNo() {
		return statusSeqNo;
	}

	public void setStatusSeqNo(Integer statusSeqNo) {
		this.statusSeqNo = statusSeqNo;
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

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	public Integer getNextActionId() {
		return nextActionId;
	}

	public void setNextActionId(Integer nextActionId) {
		this.nextActionId = nextActionId;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
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

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getAuditPlace() {
		return auditPlace;
	}

	public void setAuditPlace(String auditPlace) {
		this.auditPlace = auditPlace;
	}

	public List<FindingRptAttachModel> getFindingRptAttachs() {
		return findingRptAttachs;
	}

	public void setFindingRptAttachs(List<FindingRptAttachModel> findingRptAttachs) {
		this.findingRptAttachs = findingRptAttachs;
	}

	public String getCatagoryDesc() {
		return catagoryDesc;
	}

	public void setCatagoryDesc(String catagoryDesc) {
		this.catagoryDesc = catagoryDesc;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getNextActionDesc() {
		return nextActionDesc;
	}

	public void setNextActionDesc(String nextActionDesc) {
		this.nextActionDesc = nextActionDesc;
	}

	public String getUpdatedTypeId() {
		return updatedTypeId;
	}

	public void setUpdatedTypeId(String updatedTypeId) {
		this.updatedTypeId = updatedTypeId;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	@Override
	public String toString() {
		return "FindingDetailModel [statusSeqNo=" + statusSeqNo + ", findingSeqNo=" + findingSeqNo + ", origAuditSeqNo="
				+ origAuditSeqNo + ", currentAuditSeq=" + currentAuditSeq + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + ", categoryId=" + categoryId
				+ ", statusId=" + statusId + ", statusDate=" + statusDate + ", nextActionId=" + nextActionId
				+ ", dueDate=" + dueDate + ", descriptions=" + descriptions + ", userIns=" + userIns + ", dateIns="
				+ dateIns + ", updateDescription=" + updateDescription + ", auditPlace=" + auditPlace
				+ ", findingRptAttachs=" + findingRptAttachs + ", catagoryDesc=" + catagoryDesc + ", statusDesc="
				+ statusDesc + ", nextActionDesc=" + nextActionDesc + ", updatedTypeId=" + updatedTypeId
				+ ", vesselImoNo=" + vesselImoNo + "]";
	}

	
 	
}