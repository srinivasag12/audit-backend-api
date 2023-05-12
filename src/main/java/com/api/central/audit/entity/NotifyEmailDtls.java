package com.api.central.audit.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotifyEmailDtls implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
		
	@JsonProperty(value="EMAIL_SEQ")
	private int emailId;
		
	@JsonProperty(value="MAIL_SEQ_NO")
	private int mailSeq;
		
	@JsonProperty(value="SUBJECT")
	private String subject;
	
	@JsonProperty(value="USERNAME")
	private String username;
		
	@JsonProperty(value="MESSAGE")
	private String message;
		
	@JsonProperty(value="FROM_MAIL")
	private String fromMail;
	
	@JsonProperty(value="COMPANY_ID")
	private Long companyId;	

	public int getEmailId() {
		return emailId;
	}

	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}

	public int getMailSeq() {
		return mailSeq;
	}

	public void setMailSeq(int mailSeq) {
		this.mailSeq = mailSeq;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "NotifyDtls [emailId=" + emailId + ", mailSeq=" + mailSeq + ", subject=" + subject + ", username=" + username
				+ ", message=" + message + ", fromMail=" + fromMail + ", companyId =" + companyId +"]";
	}

}