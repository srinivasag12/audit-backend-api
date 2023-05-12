package com.api.central.rmiaudit.entity;

import java.io.Serializable;
import java.util.Arrays;

public class RmiAuditSigner implements Serializable {
	 
	 private static final long serialVersionUID = 1L;
	 private byte[] signature;
	 private String signer;
	 private String title;
	 private byte[] seal;
	 private Integer officialId;
	 private String ism;
	 private String msa;
	 private String mlc;
	 private String ihm;
	 private String planApproval;
	 
	 private String status;
	 private String location;
	 private String firstName;
	 private String lastName;
	 private Integer auditorId;
     private String emailAddress;
     private String address;
     
     
	 
	 
	 
     public String getPlanApproval() {
 		return planApproval;
 	}
 	public void setPlanApproval(String planApproval) {
 		this.planApproval = planApproval;
 	}
	 
	 
	 
	 public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	 
	 public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}
	
	
	 
	 
	 
	 
	 
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIhm() {
		return ihm;
	}
	public void setIhm(String ihm) {
		this.ihm = ihm;
	}
	public String getIsm() {
		return ism;
	}
	public void setIsm(String ism) {
		this.ism = ism;
	}
	public String getMsa() {
		return msa;
	}
	public void setMsa(String msa) {
		this.msa = msa;
	}
	public String getMlc() {
		return mlc;
	}
	public void setMlc(String mlc) {
		this.mlc = mlc;
	}
	/**
	 * @return the signature
	 */
	public byte[] getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	/**
	 * @return the signer
	 */
	public String getSigner() {
		return signer;
	}
	/**
	 * @param signer the signer to set
	 */
	public void setSigner(String signer) {
		this.signer = signer;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the seal
	 */
	public byte[] getSeal() {
		return seal;
	}
	/**
	 * @param seal the seal to set
	 */
	public void setSeal(byte[] seal) {
		this.seal = seal;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Integer getOfficialId() {
		return officialId;
	}
	public void setOfficialId(Integer officialId) {
		this.officialId = officialId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RmiAuditSigner [signature=" + Arrays.toString(signature) + ", signer=" + signer + ", title=" + title
				+ ", seal=" + Arrays.toString(seal) + "]";
	}
	 
}
