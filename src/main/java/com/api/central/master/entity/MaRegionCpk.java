package com.api.central.master.entity;

import java.io.Serializable;



public class MaRegionCpk implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long regionId;


	private Long companyId;


	public Long getRegionId() {
		return regionId;
	}


	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}


	public Long getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}


	@Override
	public String toString() {
		return "MaRegionCpk [regionId=" + regionId + ", companyId=" + companyId + "]";
	}


	public MaRegionCpk() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
