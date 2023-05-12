/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name VesselCriteria.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/audit/entity/VesselCriteria.java $
**/

package com.api.central.audit.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class VesselCriteria implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private String vesselImoNo;
	
	private String vesselName;
	
	private String officialNo;
	
	private String companyImoNo;
    
	private String companyName;
		    
	private String companyDoc;
		
	private String docExpiry;
		
	private String docIssuer;
	
	private String dateform;

	private String nxtperdte;

	private String resultDate;

	private String certExpireDate;

	private String certIssueDate;

	private String certificateNo;
	
	private String dateforma;

	private String nxtperdtea;

	private String resultDatea;

	private String certExpireDatea;

	private String certIssueDatea;

	private String certificateNoa;
	
	private String auditDate;
	
	private String certificateNos;
	
	private String certIssueDates;
	
	private String certIssueDatesa;
	
	private String certificateNosa;
	
	private String dateformb;

	private String nxtperdteb;

	private String resultDateb;

	private String certExpireDateb;

	private String certIssueDateb;

	private String certificateNob;
	
	private String anivDate;
	
	private String anivDatea;
	
	private String anivDateb;
	


		public String getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(String officialNo) {
		this.officialNo = officialNo;
	}

	public String getAnivDate() {
		return anivDate;
	}

	public void setAnivDate(String anivDate) {
		this.anivDate = anivDate;
	}

	public String getAnivDatea() {
		return anivDatea;
	}

	public void setAnivDatea(String anivDatea) {
		this.anivDatea = anivDatea;
	}

	public String getAnivDateb() {
		return anivDateb;
	}

	public void setAnivDateb(String anivDateb) {
		this.anivDateb = anivDateb;
	}

	public String getDateformb() {
		return dateformb;
	}

	public void setDateformb(String dateformb) {
		this.dateformb = dateformb;
	}

	public String getNxtperdteb() {
		return nxtperdteb;
	}

	public void setNxtperdteb(String nxtperdteb) {
		this.nxtperdteb = nxtperdteb;
	}

	public String getResultDateb() {
		return resultDateb;
	}

	public void setResultDateb(String resultDateb) {
		this.resultDateb = resultDateb;
	}

	public String getCertExpireDateb() {
		return certExpireDateb;
	}

	public void setCertExpireDateb(String certExpireDateb) {
		this.certExpireDateb = certExpireDateb;
	}

	public String getCertIssueDateb() {
		return certIssueDateb;
	}

	public void setCertIssueDateb(String certIssueDateb) {
		this.certIssueDateb = certIssueDateb;
	}

	public String getCertificateNob() {
		return certificateNob;
	}

	public void setCertificateNob(String certificateNob) {
		this.certificateNob = certificateNob;
	}

	public String getCertIssueDatesa() {
		return certIssueDatesa;
	}

	public void setCertIssueDatesa(String certIssueDatesa) {
		this.certIssueDatesa = certIssueDatesa;
	}

	public String getCertificateNosa() {
		return certificateNosa;
	}

	public void setCertificateNosa(String certificateNosa) {
		this.certificateNosa = certificateNosa;
	}

	private List<IspsBean> auditTypeDescIsps=new ArrayList<IspsBean>();
	
	
	private List<sspBean> auditTypeDescSsp = new ArrayList<sspBean>();
	
	private List<DmlcBean> auditTypeDescDmlc = new ArrayList<DmlcBean>();
	
	private List<MlcBean> auditTypeDescMlc=new ArrayList<MlcBean>();
	

	
	public List<MlcBean> getAuditTypeDescMlc() {
		return auditTypeDescMlc;
	}

	public void setAuditTypeDescMlc(List<MlcBean> auditTypeDescMlc) {
		this.auditTypeDescMlc = auditTypeDescMlc;
	}

	public List<DmlcBean> getAuditTypeDescDmlc() {
		return auditTypeDescDmlc;
	}

	public void setAuditTypeDescDmlc(List<DmlcBean> auditTypeDescDmlc) {
		this.auditTypeDescDmlc = auditTypeDescDmlc;
	}

	public String getCertificateNos() {
		return certificateNos;
	}

	public List<sspBean> getAuditTypeDescSsp() {
		return auditTypeDescSsp;
	}

	public void setAuditTypeDescSsp(List<sspBean> auditTypeDescSsp) {
		this.auditTypeDescSsp = auditTypeDescSsp;
	}

	public void setCertificateNos(String certificateNos) {
		this.certificateNos = certificateNos;
	}

	public String getCertIssueDates() {
		return certIssueDates;
	}

	public void setCertIssueDates(String certIssueDates) {
		this.certIssueDates = certIssueDates;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	private List<SubReportBean1> auditTypeDesc = new ArrayList<SubReportBean1>();

	public List<SubReportBean1> getAuditTypeDesc() {
		return auditTypeDesc;
	}

	public void setAuditTypeDesc(List<SubReportBean1> auditTypeDesc) {
		this.auditTypeDesc = auditTypeDesc;
	}

	

	public String getVesselImoNo() {
		return vesselImoNo;
	}


	public void setVesselImoNo(String vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDoc() {
		return companyDoc;
	}

	public void setCompanyDoc(String companyDoc) {
		this.companyDoc = companyDoc;
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

	public String getDateform() {
		return dateform;
	}

	public void setDateform(String dateform) {
		this.dateform = dateform;
	}

	public String getNxtperdte() {
		return nxtperdte;
	}

	public void setNxtperdte(String nxtperdte) {
		this.nxtperdte = nxtperdte;
	}

	

	public String getResultDate() {
		return resultDate;
	}

	public void setResultDate(String resultDate) {
		this.resultDate = resultDate;
	}

	public String getCertExpireDate() {
		return certExpireDate;
	}

	public void setCertExpireDate(String certExpireDate) {
		this.certExpireDate = certExpireDate;
	}

	public String getCertIssueDate() {
		return certIssueDate;
	}

	public void setCertIssueDate(String certIssueDate) {
		this.certIssueDate = certIssueDate;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	

	public String getDateforma() {
		return dateforma;
	}

	public void setDateforma(String dateforma) {
		this.dateforma = dateforma;
	}

	public String getNxtperdtea() {
		return nxtperdtea;
	}

	public void setNxtperdtea(String nxtperdtea) {
		this.nxtperdtea = nxtperdtea;
	}

	public String getResultDatea() {
		return resultDatea;
	}

	public void setResultDatea(String resultDatea) {
		this.resultDatea = resultDatea;
	}

	public String getCertExpireDatea() {
		return certExpireDatea;
	}

	public void setCertExpireDatea(String certExpireDatea) {
		this.certExpireDatea = certExpireDatea;
	}

	public String getCertIssueDatea() {
		return certIssueDatea;
	}

	public void setCertIssueDatea(String certIssueDatea) {
		this.certIssueDatea = certIssueDatea;
	}

	public String getCertificateNoa() {
		return certificateNoa;
	}

	public void setCertificateNoa(String certificateNoa) {
		this.certificateNoa = certificateNoa;
	}

	

	public List<IspsBean> getAuditTypeDescIsps() {
		return auditTypeDescIsps;
	}

	public void setAuditTypeDescIsps(List<IspsBean> auditTypeDescIsps) {
		this.auditTypeDescIsps = auditTypeDescIsps;
	}




	
	

	
	
	

	
	
	
	
	

	

	

	
	
   
   
    
    
    
   
    
  

}
