package com.api.central.audit.entity;

import java.io.Serializable;

public class SyncAudit implements Serializable{
 	private static final long serialVersionUID = 1L;
	private Integer auditSeqNo;
	private Long	companyId;
	private Integer auditTypeId;
	private byte[] fileByte;
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
	public Integer getAuditTypeId() {
		return auditTypeId;
	}
	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}
	public byte[] getFileByte() {
		return fileByte;
	}
	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
	}
	public SyncAudit() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
