package com.api.central.audit.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Dileep Naduvinamani
 *
 */

public class FileToUpload implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;

	private String filePath;

	private byte[] FileByte;

	private String reportName;

	private String userMailId;

	private Boolean approvaDoc;

	private Boolean stampDoc;

	private Boolean reviewDocPr;

	private Boolean reviewDocFr;

	private Boolean receiptDoc;

	private Boolean ihmCertDocHk;

	private Boolean ihmCertDocEu;

	private Boolean ihmCertDocEx;

	private String userName;

	private String vesselName;

	private String officialNo;

	private Integer vesselImoNo;

	private Integer auditTypeId;

	private Integer auditSubTypeId;

	private String auditReportNo;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getFileByte() {
		return FileByte;
	}

	public void setFileByte(byte[] fileByte) {
		FileByte = fileByte;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getUserMailId() {
		return userMailId;
	}

	public void setUserMailId(String userMailId) {
		this.userMailId = userMailId;
	}

	public Boolean getApprovaDoc() {
		return approvaDoc;
	}

	public void setApprovaDoc(Boolean approvaDoc) {
		this.approvaDoc = approvaDoc;
	}

	public Boolean getStampDoc() {
		return stampDoc;
	}

	public void setStampDoc(Boolean stampDoc) {
		this.stampDoc = stampDoc;
	}

	public Boolean getReviewDocPr() {
		return reviewDocPr;
	}

	public void setReviewDocPr(Boolean reviewDocPr) {
		this.reviewDocPr = reviewDocPr;
	}

	public Boolean getReviewDocFr() {
		return reviewDocFr;
	}

	public void setReviewDocFr(Boolean reviewDocFr) {
		this.reviewDocFr = reviewDocFr;
	}

	public Boolean getReceiptDoc() {
		return receiptDoc;
	}

	public void setReceiptDoc(Boolean receiptDoc) {
		this.receiptDoc = receiptDoc;
	}

	public Boolean getIhmCertDocHk() {
		return ihmCertDocHk;
	}

	public void setIhmCertDocHk(Boolean ihmCertDocHk) {
		this.ihmCertDocHk = ihmCertDocHk;
	}

	public Boolean getIhmCertDocEu() {
		return ihmCertDocEu;
	}

	public void setIhmCertDocEu(Boolean ihmCertDocEu) {
		this.ihmCertDocEu = ihmCertDocEu;
	}

	public Boolean getIhmCertDocEx() {
		return ihmCertDocEx;
	}

	public void setIhmCertDocEx(Boolean ihmCertDocEx) {
		this.ihmCertDocEx = ihmCertDocEx;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getOfficialNo() {
		return officialNo;
	}

	public void setOfficialNo(String officialNo) {
		this.officialNo = officialNo;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public String getAuditReportNo() {
		return auditReportNo;
	}

	public void setAuditReportNo(String auditReportNo) {
		this.auditReportNo = auditReportNo;
	}

	@Override
	public String toString() {
		return "FileToUpload [fileName=" + fileName + ", filePath=" + filePath + ", FileByte="
				+ Arrays.toString(FileByte) + ", reportName=" + reportName + ", userMailId=" + userMailId
				+ ", approvaDoc=" + approvaDoc + ", stampDoc=" + stampDoc + ", reviewDocPr=" + reviewDocPr
				+ ", reviewDocFr=" + reviewDocFr + ", receiptDoc=" + receiptDoc + ", ihmCertDocHk=" + ihmCertDocHk
				+ ", ihmCertDocEu=" + ihmCertDocEu + ", ihmCertDocEx=" + ihmCertDocEx + ", userName=" + userName
				+ ", vesselName=" + vesselName + ", officialNo=" + officialNo + ", vesselImoNo=" + vesselImoNo
				+ ", auditTypeId=" + auditTypeId + ", auditSubTypeId=" + auditSubTypeId + ", auditReportNo="
				+ auditReportNo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(FileByte);
		result = prime * result + ((approvaDoc == null) ? 0 : approvaDoc.hashCode());
		result = prime * result + ((auditReportNo == null) ? 0 : auditReportNo.hashCode());
		result = prime * result + ((auditSubTypeId == null) ? 0 : auditSubTypeId.hashCode());
		result = prime * result + ((auditTypeId == null) ? 0 : auditTypeId.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((ihmCertDocEu == null) ? 0 : ihmCertDocEu.hashCode());
		result = prime * result + ((ihmCertDocEx == null) ? 0 : ihmCertDocEx.hashCode());
		result = prime * result + ((ihmCertDocHk == null) ? 0 : ihmCertDocHk.hashCode());
		result = prime * result + ((officialNo == null) ? 0 : officialNo.hashCode());
		result = prime * result + ((receiptDoc == null) ? 0 : receiptDoc.hashCode());
		result = prime * result + ((reportName == null) ? 0 : reportName.hashCode());
		result = prime * result + ((reviewDocFr == null) ? 0 : reviewDocFr.hashCode());
		result = prime * result + ((reviewDocPr == null) ? 0 : reviewDocPr.hashCode());
		result = prime * result + ((stampDoc == null) ? 0 : stampDoc.hashCode());
		result = prime * result + ((userMailId == null) ? 0 : userMailId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((vesselImoNo == null) ? 0 : vesselImoNo.hashCode());
		result = prime * result + ((vesselName == null) ? 0 : vesselName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileToUpload other = (FileToUpload) obj;
		if (!Arrays.equals(FileByte, other.FileByte))
			return false;
		if (approvaDoc == null) {
			if (other.approvaDoc != null)
				return false;
		} else if (!approvaDoc.equals(other.approvaDoc))
			return false;
		if (auditReportNo == null) {
			if (other.auditReportNo != null)
				return false;
		} else if (!auditReportNo.equals(other.auditReportNo))
			return false;
		if (auditSubTypeId == null) {
			if (other.auditSubTypeId != null)
				return false;
		} else if (!auditSubTypeId.equals(other.auditSubTypeId))
			return false;
		if (auditTypeId == null) {
			if (other.auditTypeId != null)
				return false;
		} else if (!auditTypeId.equals(other.auditTypeId))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (ihmCertDocEu == null) {
			if (other.ihmCertDocEu != null)
				return false;
		} else if (!ihmCertDocEu.equals(other.ihmCertDocEu))
			return false;
		if (ihmCertDocEx == null) {
			if (other.ihmCertDocEx != null)
				return false;
		} else if (!ihmCertDocEx.equals(other.ihmCertDocEx))
			return false;
		if (ihmCertDocHk == null) {
			if (other.ihmCertDocHk != null)
				return false;
		} else if (!ihmCertDocHk.equals(other.ihmCertDocHk))
			return false;
		if (officialNo == null) {
			if (other.officialNo != null)
				return false;
		} else if (!officialNo.equals(other.officialNo))
			return false;
		if (receiptDoc == null) {
			if (other.receiptDoc != null)
				return false;
		} else if (!receiptDoc.equals(other.receiptDoc))
			return false;
		if (reportName == null) {
			if (other.reportName != null)
				return false;
		} else if (!reportName.equals(other.reportName))
			return false;
		if (reviewDocFr == null) {
			if (other.reviewDocFr != null)
				return false;
		} else if (!reviewDocFr.equals(other.reviewDocFr))
			return false;
		if (reviewDocPr == null) {
			if (other.reviewDocPr != null)
				return false;
		} else if (!reviewDocPr.equals(other.reviewDocPr))
			return false;
		if (stampDoc == null) {
			if (other.stampDoc != null)
				return false;
		} else if (!stampDoc.equals(other.stampDoc))
			return false;
		if (userMailId == null) {
			if (other.userMailId != null)
				return false;
		} else if (!userMailId.equals(other.userMailId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (vesselImoNo == null) {
			if (other.vesselImoNo != null)
				return false;
		} else if (!vesselImoNo.equals(other.vesselImoNo))
			return false;
		if (vesselName == null) {
			if (other.vesselName != null)
				return false;
		} else if (!vesselName.equals(other.vesselName))
			return false;
		return true;
	}

}
