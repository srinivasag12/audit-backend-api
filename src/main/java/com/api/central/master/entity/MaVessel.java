/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaVessel.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaVessel.java $
**/
package com.api.central.master.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MA_VESSEL")
@IdClass(MaVesselCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class MaVessel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VESSEL_IMO_NO")
	private Integer vesselImoNo;

	@Column(name = "VESSEL_NAME")
	private String vesselName;
	
	@Column(name = "DOC_TYPE_NUMBER")
	private String docTypeNumber;
			
	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Column(name = "VESSEL_STATUS")
	private String vesselStatus;
	
	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "OFFICIAL_NO")
	private Long officialNo;

	@Column(name = "GRT")
	private Integer grt;

	@Column(name = "COMPANY_IMO_NO")
	private String companyImoNo;

	@Column(name = "PORT_OF_REGISTRY")
	private String portOfRegistry;
	
	@Column(name = "TC_APPROVAL_STATUS")
	private Integer tcApprovalStatus;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name = "DATE_OF_REGISTRY")
	private Date dateOfRegistry;
	
	@Column(name = "USER_INS")
	private String userIns;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name = "DATE_INS")
	private Date dateIns;

	@Column(name = "VESSEL_TYPE")
	private String vesselType;
	
	@Column(name = "VESSEL_ID")
	private Integer vesselId;
	
	@Column(name = "VESSEL_PK")
	private Integer vesselPk;
	
	@Column(name = "VESSEL_UK")
	private Integer vesselUk;
	
	@Column(name = "CLASS_SOCIETY")
	private String classSociety;
	
	@Column(name = "CALL_SIGN")
	private String callSign;
	
	@Transient
	private String vesselTypename;
	
	@Transient
	private MaVesselCompany vesselCompany;
	
	@Column(name = "TRANS_TYPE")
	private String transType;
	
	
	@Column(name = "REG_OWNED_IMO_NUMBER")
	private Integer regOwnedImoNo;
	
	
	
	@ShortDateFormat
	@Column(name = "KEEL_LAID_DATE")
	private Date keelLaidDate;
	
	@Column(name = "OPERATION_CODE")
	private String  operationCode;
	
	@Column(name = "REGISTERED_COMPANY_ADDRESS")
    private String  registeredCompanyAddress;
	
	@Column(name = "REGISTERED_COMPANY_NAME")
	private String   registeredCompanyName;
	
	
	

	public String getRegisteredCompanyAddress() {
		return registeredCompanyAddress;
	}

	public void setRegisteredCompanyAddress(String registeredCompanyAddress) {
		this.registeredCompanyAddress = registeredCompanyAddress;
	}

	public String getRegisteredCompanyName() {
		return registeredCompanyName;
	}

	public void setRegisteredCompanyName(String registeredCompanyName) {
		this.registeredCompanyName = registeredCompanyName;
	}
	
	
	
	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public Date getKeelLaidDate() {
		return keelLaidDate;
	}

	public void setKeelLaidDate(Date keelLaidDate) {
		this.keelLaidDate = keelLaidDate;
	}
	
	public Integer getRegOwnedImoNo() {
		return regOwnedImoNo;
	}

	public void setRegOwnedImoNo(Integer regOwnedImoNo) {
		this.regOwnedImoNo = regOwnedImoNo;
	}

	

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(Long officialNo) {
		this.officialNo = officialNo;
	}

	public Integer getGrt() {
		return grt;
	}

	public void setGrt(Integer grt) {
		this.grt = grt;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}

	public Integer getTcApprovalStatus() {
		return tcApprovalStatus;
	}

	public void setTcApprovalStatus(Integer tcApprovalStatus) {
		this.tcApprovalStatus = tcApprovalStatus;
	}

	public Date getDateOfRegistry() {
		
		return dateOfRegistry;
	}

	public void setDateOfRegistry(Date dateOfRegistry) {
		this.dateOfRegistry = dateOfRegistry;
	}

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public Date getDateIns() {
		
		return dateIns;
	}

	public void setDateIns(Date dateIns) {
		
		this.dateIns = dateIns;
	}

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}

	public Integer getVesselPk() {
		return vesselPk;
	}

	public void setVesselPk(Integer vesselPk) {
		this.vesselPk = vesselPk;
	}

	public Integer getVesselUk() {
		return vesselUk;
	}

	public void setVesselUk(Integer vesselUk) {
		this.vesselUk = vesselUk;
	}

	public String getVesselTypename() {
		return vesselTypename;
	}

	public void setVesselTypename(String vesselTypename) {
		this.vesselTypename = vesselTypename;
	}

	public MaVesselCompany getVesselCompany() {
		return vesselCompany;
	}

	public void setVesselCompany(MaVesselCompany vesselCompany) {
		this.vesselCompany = vesselCompany;
	}

	public String getClassSociety() {
		return classSociety;
	}

	public void setClassSociety(String classSociety) {
		this.classSociety = classSociety;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	public String getVesselStatus() {
		return vesselStatus;
	}

	public void setVesselStatus(String vesselStatus) {
		this.vesselStatus = vesselStatus;
	}

	@Override
	public String toString() {
		return "MaVessel [vesselImoNo=" + vesselImoNo + ", vesselName=" + vesselName + ", docTypeNumber="
				+ docTypeNumber + ", activeStatus=" + activeStatus + ", vesselStatus=" + vesselStatus + ", companyId="
				+ companyId + ", officialNo=" + officialNo + ", grt=" + grt + ", companyImoNo=" + companyImoNo
				+ ", portOfRegistry=" + portOfRegistry + ", tcApprovalStatus=" + tcApprovalStatus + ", dateOfRegistry="
				+ dateOfRegistry + ", userIns=" + userIns + ", dateIns=" + dateIns + ", vesselType=" + vesselType
				+ ", vesselId=" + vesselId + ", vesselPk=" + vesselPk + ", vesselUk=" + vesselUk + ", classSociety="
				+ classSociety + ", callSign=" + callSign + ", vesselTypename=" + vesselTypename + ", vesselCompany="
				+ vesselCompany + ", transType=" + transType + ", regOwnedImoNo=" + regOwnedImoNo + ", keelLaidDate="
				+ keelLaidDate + "]";
	}
	
	
	
}
