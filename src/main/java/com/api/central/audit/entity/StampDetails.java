package com.api.central.audit.entity;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*@author Deepa Rajanna
*
 */

@Entity
@Table(name="STAMP_DETAILS")
public class StampDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="USER_ID")
	private Integer userId;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="MODIFIED_DATE")
	private String modifiedDate;
	
	@Column(name="CREATED_BY")
	private String emailId;
	
	@Column(name="PAGE_NO")
	private String pagenumbers;
	
	@Column(name="CREATED_DATE")
	private String createdDate;
	
	@Column(name="STAMP_DATE")
	private String stampDate;
	
	@Column(name="REVIEW_REPORT_NO")
	private String reviewReportNo;
	
	@Column(name="TOTAL_NO_PAGES")
	private Integer totalNoPages;
	
	private byte[] FileByte;
	
	private byte[] signByte;
	
	private byte[] sealByte;
	
	private String seal;
	
	private String sign;
	
	private String corx;
	
	private String pointers;
	
	private String cory;
	
	private String signer;
	
	private String signerName;
	
	private String status;
	
	private Integer auditSeqNo;
	
	private	String userName;

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getPointers() {
		return pointers;
	}

	public void setPointers(String pointers) {
		this.pointers = pointers;
	}

	public String getCorx() {
		return corx;
	}

	public void setCorx(String corx) {
		this.corx = corx;
	}

	public String getCory() {
		return cory;
	}

	public void setCory(String cory) {
		this.cory = cory;
	}
	
	public Integer getTotalNoPages() {
		return totalNoPages;
	}

	public void setTotalNoPages(Integer totalNoPages) {
		this.totalNoPages = totalNoPages;
	}

	public String getReviewReportNo() {
		return reviewReportNo;
	}

	public void setReviewReportNo(String reviewReportNo) {
		this.reviewReportNo = reviewReportNo;
	}

	public String getStampDate() {
		return stampDate;
	}

	public void setStampDate(String stampDate) {
		this.stampDate = stampDate;
	}

	 
	
	
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifieddate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSeal() {
		return seal;
	}

	public void setSeal(String seal) {
		this.seal = seal;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public byte[] getSignByte() {
		return signByte;
	}

	public void setSignByte(byte[] signByte) {
		this.signByte = signByte;
	}
	
	public byte[] getSealByte() {
		return sealByte;
	}

	public void setSealByte(byte[] sealByte) {
		this.sealByte = sealByte;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public void setPagenumbers(String pagenumbers) {
		this.pagenumbers = pagenumbers;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileByte() {
		return FileByte;
	}

	public void setFileByte(byte[] fileByte) {
		FileByte = fileByte;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "StampDetails [userId=" + userId + ", fileName=" + fileName + ", modifiedDate=" + modifiedDate
				+ ", emailId=" + emailId + ", pagenumbers=" + pagenumbers + ", createdDate=" + createdDate
				+ ", stampDate=" + stampDate + ", reviewReportNo=" + reviewReportNo + ", totalNoPages=" + totalNoPages
				+ ", seal=" + seal + ", sign=" + sign + ", corx=" + corx + ", cory=" + cory + ", userName=" + userName + "]";
	}
	
	
}
