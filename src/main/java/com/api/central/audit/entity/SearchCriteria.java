package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.sql.Date;
import java.util.List;

public class SearchCriteria {
	
	private Integer vesselImoNo;

	private String companyImoNo;
	
	private java.util.Date certIssueDate;

	private java.util.Date certExpireDate;
	
	private String certificateNo;
	
    private Integer auditTypeId;
    
    private Integer auditSubTypeId;
    
    private Long companyId;
    
    private Integer pageNo;
    
    private Integer scope;
    
    private String userId;
    
    private Integer auditStatusId;
    
    private List<Integer> auditSeqNo;
    
    private boolean retrieveFlag;
    
    private Integer roleId;
    
    private Integer defaultSearchCount;
    
    private boolean logout;
    
    private String emailId;
    
    private Integer activeStatus;
    
    private Integer auditSequenceNo;
    
    private String auditorUserId;
    
    private Integer auditorRoleId;
    
    private String shortingBy;
    
    private String shortingOrder;
   
    private Long officialNo;
    
    private Integer certIssueId;
    
    private Integer checkCertVthAudit;
    
    private Integer ihmAuthorise;
    
    private Integer planApprovalAuthorise;
    
    private Integer laptopSearch;   
    
    public Integer getPlanApprovalAuthorise() {
		return planApprovalAuthorise;
	}

	public void setPlanApprovalAuthorise(Integer planApprovalAuthorise) {
		this.planApprovalAuthorise = planApprovalAuthorise;
	}
			
    public Integer getLaptopSearch() {
		return laptopSearch;
	}

	public void setLaptopSearch(Integer laptopSearch) {
		this.laptopSearch = laptopSearch;
	}

	public Integer getIhmAuthorise() {
		return ihmAuthorise;
	}

	public void setIhmAuthorise(Integer ihmAuthorise) {
		this.ihmAuthorise = ihmAuthorise;
	}

	public boolean getRetrieveFlag() {
		return retrieveFlag;
	}

	public void setRetrieveFlag(boolean retrieveFlag) {
		this.retrieveFlag = retrieveFlag;
	}

	public List<Integer> getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(List<Integer> auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public java.util.Date getCertIssueDate() {
		return certIssueDate;
	}

	public void setCertIssueDate(java.util.Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}

	public java.util.Date getCertExpireDate() {
		return certExpireDate;
	}

	public void setCertExpireDate(java.util.Date certExpireDate) {
		this.certExpireDate = certExpireDate;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	public Integer getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(Integer auditStatusId) {
		this.auditStatusId = auditStatusId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getDefaultSearchCount() {
		return defaultSearchCount;
	}

	public void setDefaultSearchCount(Integer defaultSearchCount) {
		this.defaultSearchCount = defaultSearchCount;
	}

	public boolean isLogout() {
		return logout;
	}

	public void setLogout(boolean logout) {
		this.logout = logout;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getAuditSequenceNo() {
		return auditSequenceNo;
	}

	public void setAuditSequenceNo(Integer auditSequenceNo) {
		this.auditSequenceNo = auditSequenceNo;
	}

	public String getAuditorUserId() {
		return auditorUserId;
	}

	public void setAuditorUserId(String auditorUserId) {
		this.auditorUserId = auditorUserId;
	}

	public Integer getAuditorRoleId() {
		return auditorRoleId;
	}

	public void setAuditorRoleId(Integer auditorRoleId) {
		this.auditorRoleId = auditorRoleId;
	}

	public String getShortingBy() {
		return shortingBy;
	}

	public void setShortingBy(String shortingBy) {
		this.shortingBy = shortingBy;
	}

	public String getShortingOrder() {
		return shortingOrder;
	}

	public void setShortingOrder(String shortingOrder) {
		this.shortingOrder = shortingOrder;
	}

	public Long getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(Long officialNo) {
		this.officialNo = officialNo;
	}

	public Integer getCertIssueId() {
		return certIssueId;
	}

	public void setCertIssueId(Integer certIssueId) {
		this.certIssueId = certIssueId;
	}

	public Integer getCheckCertVthAudit() {
		return checkCertVthAudit;
	}

	public void setCheckCertVthAudit(Integer checkCertVthAudit) {
		this.checkCertVthAudit = checkCertVthAudit;
	}
	
}
