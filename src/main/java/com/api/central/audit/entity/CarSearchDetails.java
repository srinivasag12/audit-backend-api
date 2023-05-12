package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;

@Entity
@IdClass(CarSearchResultCPK.class)
@Table(name="CAR_SEARCH_DETAILS")
public class CarSearchDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AUDIT_SEQ_NO")
	private Integer auditSeqNo;
	
	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;
	
	@Column(name="VESSEL_IMO_NO")
	private Integer vesselImoNo;
	
	@Column(name="AUDIT_TYPE_ID")
	private Integer auditTypeId;
	
	@Formula("(SELECT A.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditTypeDesc;
	
	@Column(name="AUDIT_SUB_TYPE_ID")
	private Integer auditSubTypeId;
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	private Date auditDate;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Formula("(SELECT A.FIRST_NAME FROM MA_USERS A WHERE A.SEQUENCE_NUMBER=USER_ID AND A.COMPANY_ID = COMPANY_ID)")// AND A.COMPANY_ID = COMPANY_ID)")
	private String firstName;
	
	@Formula("(SELECT A.LAST_NAME FROM MA_USERS A WHERE  A.SEQUENCE_NUMBER=USER_ID AND A.COMPANY_ID = COMPANY_ID)")// AND A.COMPANY_ID = COMPANY_ID)")
	private String lastName;
	
	@Id
	@Column(name="FINDING_SEQ_NO")
	private Integer findingSeqNo;
	
	@Column(name="AUDIT_CODE")
	private String auditCode;
	
	@Formula("(SELECT A.AUDIT_ELEMENTS FROM MA_AUDIT_CODES A WHERE A.AUDIT_CODE=AUDIT_CODE AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String auditElement;
	
	@Id
	@Column(name="STATUS_SEQ_NO")
	private Integer statusSeqNo;
	
	@Column(name="FINDING_CATEGORY_ID")
	private Integer categoryId;
	
	@Formula("(SELECT A.FINDING_CATEGORY_DESC FROM MA_FINDINGS_CATEGORY A WHERE A.FINDING_CATEGORY_ID=FINDING_CATEGORY_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String categoryDesc;
	
	@Column(name="STATUS_ID")
	private Integer findingStatusId;
	
	@Formula("(SELECT A.FINDING_STATUS_DESC FROM MA_FINDINGS_STATUS A WHERE A.FINDING_STATUS_ID=STATUS_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID  AND A.COMPANY_ID = COMPANY_ID)")
	private String findingStatusDesc;
	
	@Column(name="CUR_AUDIT_SEQ_NO")
	private Integer currentAuditSeq;
	
	@Column(name="ORIG_SEQ_NO")
	private Integer origSeqNo;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="STATUS_DATE")
	private Date statusDate;
	
	@Column(name="NEXT_ACTION_ID")
	private Integer nextActionId;
	
	@Formula("(SELECT A.FINDING_STATUS_DESC FROM MA_FINDINGS_STATUS A WHERE A.FINDING_STATUS_ID=NEXT_ACTION_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String nextActionDesc;
	
	
	@Column(name="DUE_DATE")
	private String dueDate;
	
	@Column(name="DESCRIPTION")
	private String descriptions;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	@Column(name="UPDATE_DESCRIPTION")
	private String updateDescription;
	
	@Column(name="SERIAL_NO")
	private String serialNo;
	
	@Column(name="AUDIT_PLACE")
	private String auditPlace;

	public Integer getAuditseqNo() {
		return auditSeqNo;
	}

	public void setAuditseqNo(Integer auditseqNo) {
		this.auditSeqNo = auditseqNo;
	}

	public Integer getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(Integer auditTypeId) {
		this.auditTypeId = auditTypeId;
	}

	public String getAuditTypeDesc() {
		return auditTypeDesc;
	}

	public void setAuditTypeDesc(String auditTypeDesc) {
		this.auditTypeDesc = auditTypeDesc;
	}

	public Integer getAuditSubTypeId() {
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}

	public String getAudSubTypeDesc() {
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}

	public String getAuditElement() {
		return auditElement;
	}

	public void setAuditElement(String auditElement) {
		this.auditElement = auditElement;
	}

	public Integer getStatusSeqNo() {
		return statusSeqNo;
	}

	public void setStatusSeqNo(Integer statusSeqNo) {
		this.statusSeqNo = statusSeqNo;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public Integer getFindingStatusId() {
		return findingStatusId;
	}

	public void setFindingStatusId(Integer findingStatusId) {
		this.findingStatusId = findingStatusId;
	}

	public String getFindingStatusDesc() {
		return findingStatusDesc;
	}

	public void setFindingStatusDesc(String findingStatusDesc) {
		this.findingStatusDesc = findingStatusDesc;
	}

	public Integer getCurrentAuditSeq() {
		return currentAuditSeq;
	}

	public void setCurrentAuditSeq(Integer currentAuditSeq) {
		this.currentAuditSeq = currentAuditSeq;
	}

	public Integer getOrigSeqNo() {
		return origSeqNo;
	}

	public void setOrigSeqNo(Integer origSeqNo) {
		this.origSeqNo = origSeqNo;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public Integer getNextActionId() {
		return nextActionId;
	}

	public void setNextActionId(Integer nextActionId) {
		this.nextActionId = nextActionId;
	}

	public String getNextActionDesc() {
		return nextActionDesc;
	}

	public void setNextActionDesc(String nextActionDesc) {
		this.nextActionDesc = nextActionDesc;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Timestamp getDateIns() {
		return dateIns;
	}

	public void setDateIns( Timestamp dateIns) {
		this.dateIns = dateIns;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public CarSearchDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAuditPlace() {
		return auditPlace;
	}

	public void setAuditPlace(String auditPlace) {
		this.auditPlace = auditPlace;
	}

	@Override
	public String toString() {
		return "CarSearchDetails [auditSeqNo=" + auditSeqNo + ", companyId=" + companyId + ", auditTypeId="
				+ auditTypeId + "]";
	}	
	

}
