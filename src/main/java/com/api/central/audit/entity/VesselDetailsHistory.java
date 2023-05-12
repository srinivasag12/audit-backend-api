package com.api.central.audit.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Id;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;

public class VesselDetailsHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="AUDIT_SEQ_NO")
	private Integer auditSeqNo;
	
	@Column(name="DATE_INS")
	@ShortDateFormat
	private Date dateIns;
	
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	//vessel info starts
	
	@Column(name = "VESSEL_NAME")
	private String vesselNameAud;
		
	@Column(name = "VESSEL_TYPE")
	private String vesselTypeAud;
	
	@Column(name = "VESSEL_ID")
	private Integer vesselId;
	
	@Column(name = "REG_OWNED_IMO_NUMBER")
	private Integer regOwnedImoNo;
	
	@Column(name = "REGISTERED_COMPANY_ADDRESS")
    private String  registeredCompanyAddress;
	
	@Column(name = "REGISTERED_COMPANY_NAME")
	private String   registeredCompanyName;
	
	@Column(name = "KEEL_LAID_DATE")
	@ShortDateFormat
	private Date keelLaidDate;
	
	@Column(name = "PORT_OF_REGISTRY")
	private String portOfRegistry;
	
	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}

	private String statusUpdate;
	
	public String getStatusUpdate() {
		return statusUpdate;
	}

	public void setStatusUpdate(String statusUpdate) {
		this.statusUpdate = statusUpdate;
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

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}

	@Column(name="GRT")
	private Integer grt;
	
	@Column(name="COMPANY_IMO_NO")
	private String companyImoNo;
	
	@Column(name="DATE_OF_REGISTRY")
	@ShortDateFormat
	private Date dateOfRegistry;
	
	@Column(name = "DOC_ISSUER")
	private String docIssuerAud;
	
	@Column(name="DOC_EXPIRY")
	@ShortDateFormat
	private Date docExpiryAud;

	@Column(name = "VESSEL_ADDRESS")
	private String vesselAdrress;

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Date getDateIns() {
		return dateIns;
	}

	public void setDateIns(Date dateIns) {
		this.dateIns = dateIns;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public String getVesselNameAud() {
		return vesselNameAud;
	}

	public void setVesselNameAud(String vesselNameAud) {
		this.vesselNameAud = vesselNameAud;
	}

	public String getVesselTypeAud() {
		return vesselTypeAud;
	}

	public void setVesselTypeAud(String vesselTypeAud) {
		this.vesselTypeAud = vesselTypeAud;
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

	public Date getDateOfRegistry() {
		return dateOfRegistry;
	}

	public void setDateOfRegistry(Date dateOfRegistry) {
		this.dateOfRegistry = dateOfRegistry;
	}

	public String getDocIssuerAud() {
		return docIssuerAud;
	}

	public void setDocIssuerAud(String docIssuerAud) {
		this.docIssuerAud = docIssuerAud;
	}

	public Date getDocExpiryAud() {
		return docExpiryAud;
	}

	public void setDocExpiryAud(Date docExpiryAud) {
		this.docExpiryAud = docExpiryAud;
	}

	public String getVesselAdrress() {
		return vesselAdrress;
	}

	public void setVesselAdrress(String vesselAdrress) {
		this.vesselAdrress = vesselAdrress;
	}
	
	@Override
	public String toString() {
		return "VesselDetailsHistory [auditSeqNo=" + auditSeqNo + ", dateIns=" + dateIns + ", vesselImoNo="
				+ vesselImoNo + ", vesselNameAud=" + vesselNameAud + ", vesselTypeAud=" + vesselTypeAud + ", grt=" + grt
				+ ", companyImoNo=" + companyImoNo + ", dateOfRegistry=" + dateOfRegistry + ", docIssuerAud="
				+ docIssuerAud + ", docExpiryAud=" + docExpiryAud + ", companyAddressAud=" + vesselAdrress + "]";
	}

}
