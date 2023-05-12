package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.api.annotations.LongDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="DOC_NUMBER_CHANGE")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DOCNumberChange implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQ_NO")
	private Integer seqNo;
	
	@Column(name="COMPANY_IMO_NO")
	private String companyImoNo;
	
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Column(name="DOC_TYPE_NO")
	private String docTypeNo;
	
	@Column(name="DOC_ISSUER")
	private String docIssuer;
	
	@Column(name="DOC_EXPIRY")
	private String docExpiry;
	
	@Column(name="USER_INS")
	private String userIns;
	
	
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
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

	public Timestamp getDateIns() {
		return dateIns;
	}

	public void setDateIns(Timestamp dateIns) {
		this.dateIns = dateIns;
	}
	
	public DOCNumberChange() {
		super();
	}
	
}
	