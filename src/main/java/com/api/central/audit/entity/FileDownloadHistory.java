package com.api.central.audit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILE_DOWNLOAD_HISTORY")
public class FileDownloadHistory {

	@Id
	@Column(name = "FILE_ID")
	private Integer fileId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "SENT_TO")
	private String sentTo;

	@Column(name = "FILE_NAME")
	private String filename;

	@Column(name = "OTP")
	private String otp;

	@Column(name = "FILE_TYPE")
	private String fileType;

	@Column(name = "STATUS")
	private char status;

	@Column(name = "CREATED_AT")
	private Date created_at;

	@Column(name = "CREATE_BY")
	private String createdBy;
	
	@Column(name = "LINK_ID")
	private Integer linkId;
	
	@Column(name="REVIEW_REPORT_NO")
	private String reviewReportNo;

	public String getReviewReportNo() {
		return reviewReportNo;
	}

	public void setReviewReportNo(String reviewReportNo) {
		this.reviewReportNo = reviewReportNo;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public FileDownloadHistory() {
		
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public FileDownloadHistory(Integer fileId, String userId, String sentTo, String filename, String otp,
			String fileType, char status, Date created_at, String createdBy, Integer linkId,String reviewReportNo) {
		this.fileId = fileId;
		this.userId = userId;
		this.sentTo = sentTo;
		this.filename = filename;
		this.otp = otp;
		this.fileType = fileType;
		this.status = status;
		this.created_at = created_at;
		this.createdBy = createdBy;
		this.linkId = linkId;
		this.reviewReportNo=reviewReportNo;
	}

	
}
