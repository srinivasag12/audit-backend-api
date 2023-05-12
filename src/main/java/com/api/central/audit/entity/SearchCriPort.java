package com.api.central.audit.entity;

public class SearchCriPort {
	
	Long portId;
	String portName;
	String countryName;
	Long companyId;
	Integer activeStatus;
	Integer pageNo;
	Integer defaultSearchCount;
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getDefaultSearchCount() {
		return defaultSearchCount;
	}
	public void setDefaultSearchCount(Integer defaultSearchCount) {
		this.defaultSearchCount = defaultSearchCount;
	}
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	public Long getPortId() {
		return portId;
	}
	public void setPortId(Long portId) {
		this.portId = portId;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
