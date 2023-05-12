/*added by @Ramya for Jira id - IRI-5633*/
package com.api.central.audit.entity;

import java.util.ArrayList;
import java.util.List;

public class AuditFindingModel {


	private Integer findingSeqNo;
	
	private Integer auditSeqNo;
 	 		
	private Long companyId;
	
	private Integer auditTypeId;
	
	private Integer findingStatus;
	
	private String auditDate;
  	
	private String auditCode;
	
	private String userIns;
	
	private String dateIns;
	
	private String serialNo;
	
	private Integer auditStatus;

	private List<FindingDetailModel> findingDetailModel = new ArrayList<FindingDetailModel>();
	
	private AuditDetail auditDetail;

	private String auditElements;
	
	private String auditorName;

	private String audSubTypeDesc;
	
	private Integer auditSubTypeId;

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
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

	public Integer getFindingStatus() {
		return findingStatus;
	}

	public void setFindingStatus(Integer findingStatus) {
		this.findingStatus = findingStatus;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public List<FindingDetailModel> getFindingDetailModel() {
		return findingDetailModel;
	}

	public void setFindingDetailModel(List<FindingDetailModel> findingDetail) {
		this.findingDetailModel = findingDetail;
	}

	public String getAuditElements() {
		return auditElements;
	}

	public void setAuditElements(String auditElements) {
		this.auditElements = auditElements;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}
	

	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	@Override
	public String toString() {
		return "AuditFindingModel [findingSeqNo=" + findingSeqNo + ", auditSeqNo=" + auditSeqNo + ", companyId="
				+ companyId + ", auditTypeId=" + auditTypeId + ", findingStatus=" + findingStatus + ", auditDate="
				+ auditDate + ", auditCode=" + auditCode + ", userIns=" + userIns + ", dateIns=" + dateIns
				+ ", serialNo=" + serialNo + ", auditStatus=" + auditStatus + ", findingDetail=" + findingDetailModel
				+ ", auditElements=" + auditElements + ", auditorName=" + auditorName + ", audSubTypeDesc="
				+ audSubTypeDesc + ", auditSubTypeId=" + auditSubTypeId + "]";
	}
	
	
}
