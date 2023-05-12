/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaUsers.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaUsers.java $
**/

package com.api.central.master.entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;


import com.api.central.util.AppConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MA_USERS")
@IdClass(MaUsersCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class MaUsers {

	
	@Column(name = "SEQUENCE_NUMBER",unique=true)
	private String sequenceNo;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "PASSWORD")
	private String password;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;

	@Id
	@Column(name = "USER_ID")
	private String emailId;

	@Column(name = "PHONE_NO")
	private Long phoneNo;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "ISPS_REVIEW")
	private Integer ispsReview;
	
	@Column(name = "MLC_REVIEW")
	private Integer mlcreview;
	
	@Column(name = "IHM_REVIEW")
	private Integer ihmreview;

	@Column(name = "ISM_REVIEW")
	private Integer ismreview;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Column(name = "SIGNATURE")
	private byte[] signature;

	@Column(name = "VERIFICATION_CODE")
	private String verificationCode;
	
	
	@Column(name = "REGION_ID")
	private Long region;
	
	@Column(name = "OFFICIAL_ID")
	private Long officialId;
	
	@Column(name = "MANAGER_OFFICIAL_ID")
	private Long managerOfficialId;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name ="UPDATED_BY")
	private String updateBy;
	
	@Column(name="LOCATION")
	private String location;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="PLAN_APPROVAL_REVIEW")
	private Integer planApprovalReview;
	
	
	
	
	
	
	public Integer getPlanApprovalReview() {
		return planApprovalReview;
	}

	public void setPlanApprovalReview(Integer planApprovalReview) {
		this.planApprovalReview = planApprovalReview;
	}

	@Formula("(SELECT COUNT(A.AUD_LEAD_STATUS) FROM AUDIT_AUDITOR_DETAILS A WHERE A.USER_ID=USER_ID AND A.COMPANY_ID = COMPANY_ID AND A.AUD_LEAD_STATUS="+AppConstant.ACCEPT_STATUS+")")
	private Integer audLeadStatus;
	
	@Formula("(SELECT COUNT(A.ACTIVE_STATUS) FROM CERTIFICATE_DETAIL A WHERE A.ISSUER_ID=USER_ID AND A.COMPANY_ID = COMPANY_ID AND A.ACTIVE_STATUS=1)")
	private Integer generateStatus;
	
	@Formula("(SELECT A.ROLE_ID FROM MA_USER_ROLES A WHERE A.USER_ID=USER_ID AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer roleId;
	
	@Formula("(SELECT  COUNT(B.AUDIT_SEQ_NO)   FROM AUDIT_AUDITOR_DETAILS B  , AUDIT_DETAILS C WHERE B.COMPANY_ID = COMPANY_ID AND  B.USER_ID = USER_ID   AND B.AUD_LEAD_STATUS != 1 AND B.AUD_SIGNATURE IS NULL AND B.AUDIT_ROLE_ID!=1002 AND C.AUDIT_STATUS_ID!=1004 AND C.AUDIT_TYPE_ID!=1006 AND B.AUDIT_SEQ_NO=C.AUDIT_SEQ_NO AND C.AUDIT_TYPE_ID NOT BETWEEN 1007 AND 1013)")		//changed by @Ramya for Ticket-691
	private Integer observerOrAdditional;
	
	@Formula("(SELECT COUNT(A.AUDIT_SEQ_NO)   FROM AUDIT_AUDITOR_DETAILS A , AUDIT_DETAILS B WHERE A.COMPANY_ID = COMPANY_ID AND   A.AUD_LEAD_STATUS = 1  AND A.AUD_SIGNATURE IS NULL AND A.USER_ID=USER_ID AND A.AUDIT_SEQ_NO=B.AUDIT_SEQ_NO AND B.AUDIT_STATUS_ID!=1004 AND B.AUDIT_TYPE_ID!=1006 AND B.AUDIT_TYPE_ID NOT BETWEEN 1007 AND 1013)")		//changed by @Ramya for Ticket-691
	private Integer leadSign;
	
	
	@Formula("(SELECT COUNT(A.AUDIT_SEQ_NO) FROM AUDIT_AUDITOR_DETAILS A,AUDIT_DETAILS C  WHERE A.COMPANY_ID = COMPANY_ID  AND A.AUDIT_ROLE_ID =1003  AND  A.AUD_SIGNATURE IS  NULL AND   A.AUDIT_SEQ_NO=C.AUDIT_SEQ_NO AND C.AUDIT_STATUS_ID!=1004 AND C.AUDIT_TYPE_ID!=1006 AND A.COMPANY_ID=C.COMPANY_ID AND A.AUDIT_SEQ_NO IN (SELECT B.AUDIT_SEQ_NO FROM AUDIT_AUDITOR_DETAILS B WHERE B.USER_ID = USER_ID AND B.AUDIT_ROLE_ID!=1003 AND  B.COMPANY_ID = COMPANY_ID) AND C.AUDIT_TYPE_ID NOT BETWEEN 1007 AND 1013)")//changed by @Ramya for Ticket-691
	private Integer reviewerSign;
	
	@Formula("(SELECT COUNT(B.AUDIT_SEQ_NO) FROM  AUDIT_AUDITOR_DETAILS  B WHERE B.AUDIT_ROLE_ID=1003 AND B.COMPANY_ID = COMPANY_ID AND B.AUDIT_SEQ_NO  IN ( SELECT A.AUDIT_SEQ_NO FROM AUDIT_AUDITOR_DETAILS A WHERE A.USER_ID=USER_ID AND A.AUDIT_ROLE_ID!=1003 AND B.COMPANY_ID = COMPANY_ID ))")
	private Integer noReviewerData;
	
	@Formula("(SELECT COUNT(B.AUDIT_SEQ_NO)  FROM AUDIT_AUDITOR_DETAILS B , AUDIT_DETAILS C WHERE B.COMPANY_ID = COMPANY_ID  AND  B.USER_ID = USER_ID  AND  C.AUDIT_STATUS_ID!=1004 AND C.AUDIT_TYPE_ID!=1006 AND B.AUDIT_SEQ_NO=C.AUDIT_SEQ_NO AND C.REVIEW_STATUS!=2 AND C.AUDIT_TYPE_ID NOT BETWEEN 1007 AND 1013)")		//changed by @Ramya for Ticket-691
	private Integer auditIsThere;
	
	
	@Column(name="AUDITOR_ID")
	private Integer auditorId;
	
	

	
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

	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(targetEntity = MaUserRoles.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userRole", orphanRemoval = true)
	private List<MaUserRoles> roles = new ArrayList<MaUserRoles>();
	
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(targetEntity = LaptopPin.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userPin", orphanRemoval = true)
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return "MaUsers [sequenceNo=" + sequenceNo + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", password=" + password + ", companyId=" + companyId + ", userIns=" + userIns + ", dateIns="
				+ dateIns + ", emailId=" + emailId + ", phoneNo=" + phoneNo + ", address=" + address + ", ispsReview="
				+ ispsReview + ", mlcreview=" + mlcreview + ", ihmreview=" + ihmreview + ", ismreview=" + ismreview
				+ ", activeStatus=" + activeStatus + ", signature=" + Arrays.toString(signature) + ", verificationCode="
				+ verificationCode + ", region=" + region + ", officialId=" + officialId + ", managerOfficialId="
				+ managerOfficialId + ", title=" + title + ", updateBy=" + updateBy + ", audLeadStatus=" + audLeadStatus
				+ ", generateStatus=" + generateStatus + ", reviewerSign=" + reviewerSign + ", roles=" + roles
				+ ", userPin=" + userPin + "]";
	}
	
	

}
