package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
public class CarSearchCriteria {
	
	private Integer vesselImoNo;

    private Integer auditTypeId;
    
    private Integer auditSubTypeId;
    
    private Integer findingStatusId;
    
    private Long companyId;
    
    private Integer pageNo;
    
    private Integer defaultSearchCount;
    
    private String leadAuditorUserId;
    
    private Integer categoryId;
    

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getDefaultSearchCount() {
		return defaultSearchCount;
	}

	public String getLeadAuditorUserId() {
		return leadAuditorUserId;
	}

	public void setLeadAuditorUserId(String leadAuditorUserId) {
		
		this.leadAuditorUserId = leadAuditorUserId;
	}

	public void setDefaultSearchCount(Integer defaultSearchCount) {
		this.defaultSearchCount = defaultSearchCount;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
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

	public Integer getFindingStatusId() {
		return findingStatusId;
	}

	public void setFindingStatusId(Integer findingStatusId) {
		this.findingStatusId = findingStatusId;
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

	/*@Override
	public String toString() {
		return "CarSearchCriteria [vesselImoNo=" + vesselImoNo + ", auditTypeId=" + auditTypeId + ", auditSubTypeId="
				+ auditSubTypeId + ", findingStatusId=" + findingStatusId + ", companyId=" + companyId + ", pageNo="
				+ pageNo + "]";
	}*/
    
    
    
	

}
