package com.api.central.audit.entity;

import java.io.Serializable;
import java.util.Date;				//changed by @Ramya for Jira id - IRI-5578
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="AUDIT_FINDINGS_DETAILS")
@IdClass(FindingDetailsCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FindingDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="STATUS_SEQ_NO")
	private Integer statusSeqNo;
	

	@Id
	@Column(name="FINDING_SEQ_NO",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Finding No is empty")
	private Integer findingSeqNo;
	
	@Id
	@Column(name="ORIG_SEQ_NO",insertable=false,updatable=false)
	@Range(min=600000,max=999999,message="Orignial Audit No is empty")
	private Integer origAuditSeqNo;
 	 
	@Id
	@Column(name="CUR_AUDIT_SEQ_NO")
	@Range(min=600000,max=999999,message="Current Audit No is empty")
	private Integer currentAuditSeq;
	
	@Id
	@Column(name="COMPANY_ID",insertable=false,updatable=false)
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Column(name="AUDIT_TYPE_ID",insertable=false,updatable=false)
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
	@JsonIgnore
	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID"),
 			@JoinColumn(name = "ORIG_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO"),
 			@JoinColumn(name = "FINDING_SEQ_NO", referencedColumnName = "FINDING_SEQ_NO"),
			@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID")})
	private AuditFinding auditFinding;
 
	@Column(name="FINDING_CATEGORY_ID")
	@Range(min=1000,max=10000,message="Category cannot be empty")
 	private Integer categoryId;
	
	@Column(name="STATUS_ID")
	@Range(min=1000,max=10000,message="Status cannot be empty")
	private Integer statusId;
	
    //@DateTimeFormat(pattern="yyyy-MM-dd")
    @ShortDateFormat
	@Column(name="STATUS_DATE")
 	//@NotEmpty(message="Status Date cannot be empty")
	private Date statusDate;
	
	@Column(name="NEXT_ACTION_ID")
	@Range(min=1000,max=10000,message="Next Action cannot be empty")
 	private Integer nextActionId;
	
	@Column(name="DUE_DATE")
	private String dueDate;
	
	@Column(name="DESCRIPTION")
	private String descriptions;
	

	@Column(name="USER_INS")
	private String userIns;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	private Timestamp dateIns;
	
	@Column(name="UPDATE_DESCRIPTION")
	private String updateDescription;
	
	@Column(name="AUDIT_PLACE")
	private String auditPlace;
	
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(targetEntity = FindingRptAttach.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "findingDetail", orphanRemoval = true)
	@OrderBy(clause = "FILE_SEQ_NO ASC")
	private List<FindingRptAttach> findingRptAttachs = new ArrayList<FindingRptAttach>();

	@Formula("(SELECT A.FINDING_CATEGORY_DESC FROM MA_FINDINGS_CATEGORY A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND  A.FINDING_CATEGORY_ID=FINDING_CATEGORY_ID AND A.COMPANY_ID=COMPANY_ID)")
	private String catagoryDesc;
	
	@Formula("(SELECT A.FINDING_STATUS_DESC FROM MA_FINDINGS_STATUS A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.FINDING_STATUS_ID = STATUS_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String statusDesc;
	
	@Formula("(SELECT A.FINDING_STATUS_DESC FROM MA_FINDINGS_STATUS A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.FINDING_STATUS_ID = NEXT_ACTION_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String nextActionDesc;
 
	@Formula("(SELECT A.AUDIT_TYPE_ID FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO = CUR_AUDIT_SEQ_NO  AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private String updatedTypeId;
	
	
	@Formula("(SELECT A.VESSEL_IMO_NO FROM AUDIT_DETAILS A WHERE A.AUDIT_SEQ_NO = ORIG_SEQ_NO  AND A.COMPANY_ID = COMPANY_ID AND ROWNUM=1)")
	private Integer vesselImoNo;
	
	

 	
	public Integer getVesselImoNo() {
		return vesselImoNo;
	}

	public void setVesselImoNo(Integer vesselImoNo) {
		this.vesselImoNo = vesselImoNo;
	}

	public String getUpdatedTypeId() {
		return updatedTypeId;
	}

	public void setUpdatedTypeId(String updatedTypeId) {
		this.updatedTypeId = updatedTypeId;
	}

	public Integer getStatusSeqNo() {
		return statusSeqNo;
	}

	public void setStatusSeqNo(Integer statusSeqNo) {
		this.statusSeqNo = statusSeqNo;
	}

	public AuditFinding getAuditFinding() {
		return auditFinding;
	}

	public void setAuditFinding(AuditFinding auditFinding) {
		this.auditFinding = auditFinding;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public Timestamp getDateIns() {
		return dateIns;
	}

	public void setDateIns(Timestamp dateIns) {
		this.dateIns = dateIns;
	}

	public List<FindingRptAttach> getFindingRptAttachs() {
		return findingRptAttachs;
	}

	public void setFindingRptAttachs(List<FindingRptAttach> findingRptAttachs) {
		this.findingRptAttachs = findingRptAttachs;
	}

	public String getCatagoryDesc() {
		return catagoryDesc;
	}

	public void setCatagoryDesc(String catagoryDesc) {
		this.catagoryDesc = catagoryDesc;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getNextActionDesc() {
		return nextActionDesc;
	}

	public void setNextActionDesc(String nextActionDesc) {
		this.nextActionDesc = nextActionDesc;
	}

	public FindingDetail() {
		super();
	}
 
	public Integer getFindingSeqNo() {
		return findingSeqNo;
	}

	public void setFindingSeqNo(Integer findingSeqNo) {
		this.findingSeqNo = findingSeqNo;
	}

	public Integer getOrigAuditSeqNo() {
		return origAuditSeqNo;
	}

	public void setOrigAuditSeqNo(Integer origAuditSeqNo) {
		this.origAuditSeqNo = origAuditSeqNo;
	}

	public Integer getCurrentAuditSeq() {
		return currentAuditSeq;
	}

	public void setCurrentAuditSeq(Integer currentAuditSeq) {
		this.currentAuditSeq = currentAuditSeq;
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

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getAuditPlace() {
		return auditPlace;
	}

	public void setAuditPlace(String auditPlace) {
		this.auditPlace = auditPlace;
	}

	public FindingDetail(Integer statusSeqNo, AuditFinding auditFinding) {
		super();
		this.statusSeqNo = statusSeqNo;
		this.auditFinding = auditFinding;
	}

	@Override
	public String toString() {
		return "FindingDetail [statusSeqNo=" + statusSeqNo + ", findingSeqNo=" + findingSeqNo + ", origAuditSeqNo="
				+ origAuditSeqNo + ", currentAuditSeq=" + currentAuditSeq + ", companyId=" + companyId
				+ ", auditTypeId=" + auditTypeId + "]";
	}

	
}
