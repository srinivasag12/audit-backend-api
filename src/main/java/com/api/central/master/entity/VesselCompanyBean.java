package com.api.central.master.entity;

import java.io.Serializable;
import java.sql.Date;
  
public class VesselCompanyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String companyImoNo;

	private String docTypeNo;

	private String docIssuer;

	private java.util.Date docExpiry;

	private String vesselCompanyName;

	private String vesselCompanyAddress;

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getDocTypeNo() {
		return docTypeNo;
	}

	public void setDocTypeNo(String docTypeNo) {
		this.docTypeNo = docTypeNo;
	}

	public String getDocIssuer() {
		return docIssuer;
	}

	public void setDocIssuer(String docIssuer) {
		this.docIssuer = docIssuer;
	}

	public java.util.Date getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(java.util.Date docExpiry) {
		this.docExpiry = docExpiry;
	}

	public String getVesselCompanyName() {
		return vesselCompanyName;
	}

	public void setVesselCompanyName(String vesselCompanyName) {
		this.vesselCompanyName = vesselCompanyName;
	}

	public String getVesselCompanyAddress() {
		return vesselCompanyAddress;
	}

	public void setVesselCompanyAddress(String vesselCompanyAddress) {
		this.vesselCompanyAddress = vesselCompanyAddress;
	}
 

	
 
}
