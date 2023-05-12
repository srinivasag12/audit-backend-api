package com.api.central.audit.entity;

import java.io.Serializable;

public class VesselNotification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String vesselName;
	
	private String officialNumber;
	
	private String companyImoNo;
	
	private String vesselCompanyName;
	
	private String docTypeNo;
	
	private String docExpiry;
	
	private String docIssuer;
	
	private String tcApprovalStatus;
	
	
	

	public String getTcApprovalStatus() {
		return tcApprovalStatus;
	}

	public void setTcApprovalStatus(String tcApprovalStatus) {
		this.tcApprovalStatus = tcApprovalStatus;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(String officialNumber) {
		this.officialNumber = officialNumber;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getVesselCompanyName() {
		return vesselCompanyName;
	}

	public void setVesselCompanyName(String vesselCompanyName) {
		this.vesselCompanyName = vesselCompanyName;
	}

	public String getDocTypeNo() {
		return docTypeNo;
	}

	public void setDocTypeNo(String docTypeNo) {
		this.docTypeNo = docTypeNo;
	}

	public String getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(String docExpiry) {
		this.docExpiry = docExpiry;
	}

	public String getDocIssuer() {
		return docIssuer;
	}

	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}
	
	
	
	

}
