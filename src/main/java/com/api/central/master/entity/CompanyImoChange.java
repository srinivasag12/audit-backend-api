package com.api.central.master.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="COMPANY_IMO_CHANGE")
@IdClass(CompanyImoChangeCpk.class)
public class CompanyImoChange {

	@Id
	@Column(name="SEQ_NO")
	private Long seqNo;
	
	@Id
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Id
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Id
	@Column(name="COMPANY_IMO_NO")
	private String companyImoNo;
	
	@Column(name="USER_INS")
	private String userIns;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DATE_INS")
	private java.util.Date dateIns;
	
	@Formula("(SELECT A.VESSEL_NAME FROM MA_VESSEL A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID=COMPANY_ID)")
	private String vesselName;

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
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

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	
	
	
}
