package com.api.central.portDetailsUpdate;

/**
 * Date 27 june 2022
 * CR 512
 * Author - rupesh
 */
public class RmiCertificateDetails {

	private Integer auditId;
	private Long cetificateId;
	private String certificateNumber;
	private String uniqueTrackingNo;
	private String placeOfIssueEnc;
	// private String certificateIssueType;

	public RmiCertificateDetails(Integer auditId, Long cetificateId, String certificateNumber,
			String uniqueTrackingNo, String placeOfIssueEnc) {
		super();
		this.auditId = auditId;
		this.cetificateId = cetificateId;
		this.certificateNumber = certificateNumber;
		this.uniqueTrackingNo = uniqueTrackingNo;
		this.placeOfIssueEnc = placeOfIssueEnc;
	}

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Long getCetificateId() {
		return cetificateId;
	}

	public void setCetificateId(Long cetificateId) {
		this.cetificateId = cetificateId;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getUniqueTrackingNo() {
		return uniqueTrackingNo;
	}

	public void setUniqueTrackingNo(String uniqueTrackingNo) {
		this.uniqueTrackingNo = uniqueTrackingNo;
	}

	public String getPlaceOfIssueEnc() {
		return placeOfIssueEnc;
	}

	public void setPlaceOfIssueEnc(String placeOfIssueEnc) {
		this.placeOfIssueEnc = placeOfIssueEnc;
	}

	// public String getCertificateIssueType() {
	// return certificateIssueType;
	// }
	//
	// public void setCertificateIssueType(String certificateIssueType) {
	// this.certificateIssueType = certificateIssueType;
	// }

	@Override
	public String toString() {
		return "RmiCertificateDetails [auditId=" + auditId + ", cetificateId=" + cetificateId + ", certificateNumber="
				+ certificateNumber + ", uniqueTrackingNo=" + uniqueTrackingNo + ", placeOfIssueEnc=" + placeOfIssueEnc;
	}

}
