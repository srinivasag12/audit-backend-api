package com.api.central.rmiaudit.entity;


import java.io.Serializable;

public class RmiIhmCustomers implements Serializable {
	 
	 private static final long serialVersionUID = 1L;
	 
	 private String vendorNumber;
	 private String vendorName;
	 private String vendorAddress;
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorAddress() {
		return vendorAddress;
	}
	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	 
	 
	 
}
