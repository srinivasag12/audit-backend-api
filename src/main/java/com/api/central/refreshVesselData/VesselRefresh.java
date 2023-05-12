package com.api.central.refreshVesselData;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "VESSEL_REFRESH")
@JsonInclude(Include.NON_NULL)
public class VesselRefresh implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UNIQIDENTITY")
	private Long id;
	private Integer vesselID;
	private Integer imoNumber;
	private String vesselName;
	private Long officialNumber;
	private Integer vesselPK;
	private Integer vesselUK;
	private String callSign;
	private Integer grossTon;
	private String vesselType;
	private String tcApprovalStatus;
	private String homePort;
	@ShortDateFormat
	private Date registrationDate;
	private String vesselStatus;
	private String classSociety;
	private String companyIMONumber;
	private String companyStatus;
	private String docTypeNumber;
	@ShortDateFormat
	private Date issueDate;
	@ShortDateFormat
	private Date docExpiry;
	private String docType;
	private String customerName;
	private String companyAddress;
	private String docIssuer;
	@ShortDateFormat
	private Date keelLaidDate;
	private Integer regOwnedImoNo;
	private String operationCode;
	private String registeredCompanyAddress;
	private String registeredCompanyName;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getRegOwnedImoNo() {
		return regOwnedImoNo;
	}

	public void setRegOwnedImoNo(Integer regOwnedImoNo) {
		this.regOwnedImoNo = regOwnedImoNo;
	}

	public Date getKeelLaidDate() {
		return keelLaidDate;
	}

	public void setKeelLaidDate(Date keelLaidDate) {
		this.keelLaidDate = keelLaidDate;
	}

	public Integer getVesselID() {
		return vesselID;
	}

	public void setVesselID(Integer vesselID) {
		this.vesselID = vesselID;
	}

	public Integer getImoNumber() {
		return imoNumber;
	}

	public void setImoNumber(Integer imoNumber) {
		this.imoNumber = imoNumber;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public Long getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(Long officialNumber) {
		this.officialNumber = officialNumber;
	}

	public Integer getVesselPK() {
		return vesselPK;
	}

	public void setVesselPK(Integer vesselPK) {
		this.vesselPK = vesselPK;
	}

	public Integer getVesselUK() {
		return vesselUK;
	}

	public void setVesselUK(Integer vesselUK) {
		this.vesselUK = vesselUK;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	public Integer getGrossTon() {
		return grossTon;
	}

	public void setGrossTon(Integer grossTon) {
		this.grossTon = grossTon;
	}

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}

	public String getTcApprovalStatus() {
		return tcApprovalStatus;
	}

	public void setTcApprovalStatus(String tcApprovalStatus) {
		this.tcApprovalStatus = tcApprovalStatus;
	}

	public String getHomePort() {
		return homePort;
	}

	public void setHomePort(String homePort) {
		this.homePort = homePort;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getVesselStatus() {
		return vesselStatus;
	}

	public void setVesselStatus(String vesselStatus) {
		this.vesselStatus = vesselStatus;
	}

	public String getClassSociety() {
		return classSociety;
	}

	public void setClassSociety(String classSociety) {
		this.classSociety = classSociety;
	}

	public String getCompanyIMONumber() {
		return companyIMONumber;
	}

	public void setCompanyIMONumber(String companyIMONumber) {
		this.companyIMONumber = companyIMONumber;
	}

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(Date docExpiry) {
		this.docExpiry = docExpiry;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getDocIssuer() {
		return docIssuer;
	}

	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}

	@Override
	public String toString() {
		return "VesselRefresh [id=" + id + ", vesselID=" + vesselID + ", imoNumber=" + imoNumber + ", vesselName="
				+ vesselName + ", officialNumber=" + officialNumber + ", vesselPK=" + vesselPK + ", vesselUK="
				+ vesselUK + ", callSign=" + callSign + ", grossTon=" + grossTon + ", vesselType=" + vesselType
				+ ", tcApprovalStatus=" + tcApprovalStatus + ", homePort=" + homePort + ", registrationDate="
				+ registrationDate + ", vesselStatus=" + vesselStatus + ", classSociety=" + classSociety
				+ ", companyIMONumber=" + companyIMONumber + ", companyStatus=" + companyStatus + ", docTypeNumber="
				+ docTypeNumber + ", issueDate=" + issueDate + ", docExpiry=" + docExpiry + ", docType=" + docType
				+ ", customerName=" + customerName + ", companyAddress=" + companyAddress + ", docIssuer=" + docIssuer
				+ ", keelLaidDate=" + keelLaidDate + ", regOwnedImoNo=" + regOwnedImoNo + ", operationCode="
				+ operationCode + ", registeredCompanyAddress=" + registeredCompanyAddress + ", registeredCompanyName="
				+ registeredCompanyName + "]";
	}

}
