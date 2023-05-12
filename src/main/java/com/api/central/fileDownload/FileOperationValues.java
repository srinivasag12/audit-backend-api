package com.api.central.fileDownload;

public class FileOperationValues {

	private String downloadBaseLink;
	private String downloadLocation;
	private String fromMail;
	private String mailSubject;
	private String mailBody;
	
	
	
	public FileOperationValues(String downloadBaseLink, String downloadLocation, String fromMail, String mailSubject,
			String mailBody) {
		this.downloadBaseLink = downloadBaseLink;
		this.downloadLocation = downloadLocation;
		this.fromMail = fromMail;
		this.mailSubject = mailSubject;
		this.mailBody = mailBody;
	}
	public String getDownloadBaseLink() {
		return downloadBaseLink;
	}
	public void setDownloadBaseLink(String downloadBaseLink) {
		this.downloadBaseLink = downloadBaseLink;
	}
	public String getDownloadLocation() {
		return downloadLocation;
	}
	public void setDownloadLocation(String downloadLocation) {
		this.downloadLocation = downloadLocation;
	}
	public String getFromMail() {
		return fromMail;
	}
	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailBody() {
		return mailBody;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
 
	 
	
	
	
}
