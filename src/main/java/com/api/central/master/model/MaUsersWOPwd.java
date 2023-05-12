package com.api.central.master.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;

import com.api.central.master.entity.LaptopPin;
import com.api.central.master.entity.MaUserRoles;

public class MaUsersWOPwd {


	
	private String sequenceNo;

	private String firstName;
	private String lastName;
	private Long companyId;
	private String userIns;
	private java.util.Date dateIns;

	private String emailId;
	private Long phoneNo;

	private String address;
	private Integer ispsReview;
	
	private Integer mlcreview;
	private Integer ihmreview;
	private Integer ismreview;

	private Integer activeStatus;
	private byte[] signature;

	private String verificationCode;
	private Long region;
	private Long officialId;
	
	private Long managerOfficialId;
	
	private String title;
	
	private String updateBy;
	
	private String location;
	private String status;
	
	
	private Integer audLeadStatus;
	private Integer generateStatus;
	
	private Integer roleId;
	private Integer observerOrAdditional;
	
	private Integer leadSign;
	
	
	private Integer reviewerSign;
	
	private Integer noReviewerData;
	private Integer auditIsThere;
	
	private Integer auditorId;
	
	public Integer getPlanApprovalReview() {
		return planApprovalReview;
	}

	public void setPlanApprovalReview(Integer planApprovalReview) {
		this.planApprovalReview = planApprovalReview;
	}

	private Integer planApprovalReview;
	
	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Integer getObserverOrAdditional() {
		return observerOrAdditional;
	}

	public void setObserverOrAdditional(Integer observerOrAdditional) {
		this.observerOrAdditional = observerOrAdditional;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	
	
	public Integer getLeadSign() {
		return leadSign;
	}

	public void setLeadSign(Integer leadSign) {
		this.leadSign = leadSign;
	}
	

	
	public Integer getNoReviewerData() {
		return noReviewerData;
	}

	public void setNoReviewerData(Integer noReviewerData) {
		this.noReviewerData = noReviewerData;
	}

	public Integer getAuditIsThere() {
		return auditIsThere;
	}

	public void setAuditIsThere(Integer auditIsThere) {
		this.auditIsThere = auditIsThere;
	}

	

	public Integer getReviewerSign() {
		return reviewerSign;
	}

	public void setReviewerSign(Integer reviewerSign) {
		this.reviewerSign = reviewerSign;
	}

	public Long getManagerOfficialId() {
		return managerOfficialId;
	}

	public void setManagerOfficialId(Long managerOfficialId) {
		this.managerOfficialId = managerOfficialId;
	}

	public Integer getGenerateStatus() {
		return generateStatus;
	}

	public void setGenerateStatus(Integer generateStatus) {
		this.generateStatus = generateStatus;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Long getOfficialId() {
		return officialId;
	}

	public void setOfficialId(Long officialId) {
		this.officialId = officialId;
	}

	
   public Integer getAudLeadStatus() {
		return audLeadStatus;
	}

	public void setAudLeadStatus(Integer audLeadStatus) {
		this.audLeadStatus = audLeadStatus;
	}

	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getRegion() {
		return region;
	}

	public void setRegion(Long region) {
		this.region = region;
	}

	private List<MaUserRoles> roles = new ArrayList<MaUserRoles>();
	
	private List<LaptopPin> userPin = new ArrayList<LaptopPin>();
	
	public List<LaptopPin> getUserPin() {
		return userPin;
	}

	public void setUserPin(List<LaptopPin> userPin) {
		this.userPin = userPin;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(Long phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getIspsReview() {
		return ispsReview;
	}

	public void setIspsReview(Integer ispsReview) {
		this.ispsReview = ispsReview;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public List<MaUserRoles> getRoles() {
		return roles;
	}

	public void setRoles(List<MaUserRoles> roles) {
		this.roles = roles;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public Integer getMlcreview() {
		return mlcreview;
	}

	public void setMlcreview(Integer mlcreview) {
		this.mlcreview = mlcreview;
	}

	public Integer getIhmreview() {
		return ihmreview;
	}

	public void setIhmreview(Integer ihmreview) {
		this.ihmreview = ihmreview;
	}

	public Integer getIsmreview() {
		return ismreview;
	}

	public void setIsmreview(Integer ismreview) {
		this.ismreview = ismreview;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public String toString() {
		return "MaUsersWOPwd [sequenceNo=" + sequenceNo + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", companyId=" + companyId + ", userIns=" + userIns + ", dateIns=" + dateIns + ", emailId=" + emailId
				+ ", phoneNo=" + phoneNo + ", address=" + address + ", ispsReview=" + ispsReview + ", mlcreview="
				+ mlcreview + ", ihmreview=" + ihmreview + ", ismreview=" + ismreview + ", activeStatus=" + activeStatus
				+ ", signature=" + Arrays.toString(signature) + ", verificationCode=" + verificationCode + ", region="
				+ region + ", officialId=" + officialId + ", managerOfficialId=" + managerOfficialId + ", title="
				+ title + ", updateBy=" + updateBy + ", location=" + location + ", status=" + status
				+ ", audLeadStatus=" + audLeadStatus + ", generateStatus=" + generateStatus + ", roleId=" + roleId
				+ ", observerOrAdditional=" + observerOrAdditional + ", leadSign=" + leadSign + ", reviewerSign="
				+ reviewerSign + ", noReviewerData=" + noReviewerData + ", auditIsThere=" + auditIsThere
				+ ", auditorId=" + auditorId + ", roles=" + roles + ", userPin=" + userPin + "]";
	}

	
}
