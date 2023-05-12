package com.api.central.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MA_REGION")
@IdClass(MaRegionCpk.class)

public class MaRegion {
	
	@Id
	@Column(name = "REGION_ID")
	private Long regionId;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	
    @Column(name = "REGION_NAME")
	private String regionName;
	
	@Column(name = "USER_INS")
	private String userIns;

	
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;


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


	

	public String getRegionName() {
		return regionName;
	}


	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}


	public String getUserIns() {
		return userIns;
	}


	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}


	public java.util.Date getDateIns() {
		return dateIns;
	}


	public void setDateIns(java.util.Date dateIns) {
		this.dateIns = dateIns;
	}
	
	

	

}
