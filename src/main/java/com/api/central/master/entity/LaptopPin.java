package com.api.central.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LAPTOP_PIN")
@IdClass(LaptopPinCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class LaptopPin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "USER_ID")
	private String emailId;
	
	@Column(name = "LAP_UNIQUE_ID")
	private String lapUniqId;
	
	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;
	
	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;
	
	@Column(name = "MOB_UNIQUE_ID")
	private String mobUniqId;  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022
	
	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns({
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false, nullable = false, unique = true),
	@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID", insertable = false, updatable = false, nullable = false, unique = true) })
	private MaUsers userPin;
	
	
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

	public MaUsers getUserPin() {
		return userPin;
	}

	public void setUserPin(MaUsers userPin) {
		this.userPin = userPin;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLapUniqId() {
		return lapUniqId;
	}

	public void setLapUniqId(String lapUniqId) {
		this.lapUniqId = lapUniqId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
	public String getMobUniqId() {
		return mobUniqId;
	}

	public void setMobUniqId(String mobUniqId) {
		this.mobUniqId = mobUniqId;
	}
	
	/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/

}
