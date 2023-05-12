package com.api.central.audit.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



@Entity
@Table(name="REPORT_HISTORY")
@IdClass(ReportDataCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReportData  implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="AUDIT_SEQ_NO",insertable=false,updatable=false)
	private Integer auditSeqNo;
	
	@Id
	@Column(name="COMPANY_ID",insertable=false,updatable=false)
	private Long companyId;
	
	@Id
	@Column(name="VERSION_ID")
	private Integer versionId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID",insertable=false,updatable=false)
	private Integer auditTypeId;
	
	@Column(name="AUDIT_PLACE")
	private String auditPlace;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="AUDIT_DATE")
	private java.util.Date auditDate;

	
	@Column(name="REPORT_BLOB")
	private byte[] reportBlob;
	
	@Column(name="LEAD_AUDITOR_NAME")
	private String leadAuditorName;
		
	@Column(name="FILE_NAME")
	private String fileName;
	
/*	@JsonIgnore
	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"),
			@JoinColumn(name = "AUDIT_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID")
			})
	
	private AuditDetail auditDetail;*/

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getAuditPlace() {
		return auditPlace;
	}

	public void setAuditPlace(String auditPlace) {
		this.auditPlace = auditPlace;
	}

	public java.util.Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(java.util.Date auditDate) {
		this.auditDate = auditDate;
	}

	public byte[] getReportBlob() {
		return reportBlob;
	}

	public void setReportBlob(byte[] reportBlob) {
		this.reportBlob = reportBlob;
	}

	public String getLeadAuditorName() {
		return leadAuditorName;
	}

	public void setLeadAuditorName(String leadAuditorName) {
		this.leadAuditorName = leadAuditorName;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	/*public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}
*/
	
	
	
}
