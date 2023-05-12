package com.api.central.master.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "DOC_NUMBER_CHANGE")
@IdClass(DocNumberChangeCpk.class)
public class DocNumberChange {

	@Id
	@Column(name = "SEQ_NO")
	private Long seqNo;

	@Id
	@Column(name = "COMPANY_IMO_NO")
	private String companyImoNo;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Id
	@Column(name = "DOC_TYPE_NO")
	private String docTypeNo;

	@Column(name = "DOC_ISSUER")
	private String docIssuer;

	@Column(name = "DOC_EXPIRY")
	private String docExpiry;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;

	@Formula("(SELECT A.VESSEL_COMPANY_NAME FROM MA_VESSEL_COMPANY A WHERE A.COMPANY_IMO_NO=COMPANY_IMO_NO AND A.COMPANY_ID=COMPANY_ID)")
	private String companyName;

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public String getDocExpiry() {
		return docExpiry;
	}

	public void setDocExpiry(String docExpiry) {
		this.docExpiry = docExpiry;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
