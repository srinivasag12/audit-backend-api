package com.api.central.master.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VSSL_VESSEL_INFO_V")
	 
public class VesselInfoV {
	
	@Id
	@Column(name = "SEQ_NO")
	private Integer seqNo;
	
	@Column(name = "VESSEL_ID")
	private Integer vesselID;
	
	@Column(name = "IMO_NUMBER")
	private Integer imoNumber;
	
	@Column(name = "NAME")
	private String vesselName;
	  
	@Column(name = "OFFICIAL_NUMBER")
	private Integer officialNumber;
	
	@Column(name = "VESSEL_PK")
	private Integer vesselPK;
	
	@Column(name = "VESSEL_UK")
	private Integer vesselUK;
	
	@Column(name = "CALL_SIGN")
	private String callSign;
	
	@Column(name = "GROSS_TONS")
	private Integer grossTon;
	
	@Column(name = "VESSEL_TYPE")
	private String vesselType;
	
	@Column(name = "TC_APPROVAL_STATUS")
	private String tcApprovalStatus;
	
	@Column(name = "HOME_PORT")
	private String homePort;
	
	@Column(name = "REGISTRATION_DATE")
	private Date registrationDate;
	
	@Column(name = "VESSEL_STATUS")
	private String vesselStatus;
	
	@Column(name = "CLASS_SOCIETY")
	private String classSociety;
	
	@Column(name = "COMPANY_IMO_NUMBER")
	private String companyIMONumber;
	
	@Column(name = "COMPANY_STATUS")
	private String companyStatus;
	
	@Column(name = "DOC_TYPE_NUMBER")
	private String docTypeNumber;
	
	@Column(name = "ISSUE_DATE")
	private Date issueDate;
	
	@Column(name = "DOC_EXPIRY")
	private Date docExpiry;
	
	@Column(name = "DOC_TYPE")
	private String docType;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "COMPANY_ADDRESS")
	private String companyAddress;
	
	@Column(name = "DOC_ISSUER")
	private String docIssuer;

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	public Integer getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(Integer officialNumber) {
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
	  
}
