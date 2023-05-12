package com.api.central.audit.entity;
/**
 * @author sourav ghadai
 *
 */
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.annotations.ShortDateFormat;


@Entity
@Table(name="CAR_SEARCH_RESULT")
@IdClass(CarSearchResultCPK.class)
public class CarSearchResult implements Serializable{
	
	
	   private static final long serialVersionUID = 1L;
	    
	    @Id
		@Column(name="AUDIT_SEQ_NO")
		private Integer auditSeqNo;
	    
	    @Id
	    @Column(name="COMPANY_ID")
	    private Long companyId;
		
		@Column(name="VESSEL_IMO_NO")
		private Integer vesselImoNo;
		
		@Formula("(SELECT A.VESSEL_NAME FROM MA_VESSEL A WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO AND A.COMPANY_ID = COMPANY_ID)")
		private String vesselName;
		
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
		
		@Id
		@Column(name="FINDING_SEQ_NO")
		private Integer findingSeqNo;
		
		
		@Column(name="AUDIT_CODE")
		private String auditCode;
		
		@Formula("(SELECT A.AUDIT_ELEMENTS FROM MA_AUDIT_CODES A WHERE A.AUDIT_CODE=AUDIT_CODE AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
		private String auditElement;
		
		@Id
		@Column(name="STATUS_SEQ_NO")
		private Integer statusSeqNo;
		
		@Column(name="FINDING_CATEGORY_ID")
		private Integer categoryId;
		
		@Column(name="STATUS_ID")
		private Integer findingStatusId;
		
		@Column(name="NEXT_ACTION_ID")
		private Integer nextActionId;
		
		@Column(name="DUE_DATE")
		private String dueDate;
		
		@Formula("(SELECT A.FINDING_CATEGORY_DESC FROM MA_FINDINGS_CATEGORY A WHERE A.FINDING_CATEGORY_ID=FINDING_CATEGORY_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
		private String categoryDesc;
		
		@Formula("(SELECT A.FINDING_STATUS_DESC FROM MA_FINDINGS_STATUS A WHERE A.FINDING_STATUS_ID=STATUS_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID  AND A.COMPANY_ID = COMPANY_ID)")
		private String findingStatusDesc;
		
		@Formula("(SELECT A.FINDING_STATUS_DESC FROM MA_FINDINGS_STATUS A WHERE A.FINDING_STATUS_ID=NEXT_ACTION_ID AND A.AUDIT_TYPE_ID = AUDIT_TYPE_ID  AND A.COMPANY_ID = COMPANY_ID)")
		private String nextActionIdDesc;
		
		@Column(name="USER_ID")
		private String leadAuditorUserId;
		
		public String getLeadAuditorUserId() {
			return leadAuditorUserId;
		}

		public void setLeadAuditorUserId(String leadAuditorUserId) {
			this.leadAuditorUserId = leadAuditorUserId;
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


		public Date getAuditDate() {
			return auditDate;
		}

		public void setAuditDate(Date auditDate) {
			this.auditDate = auditDate;
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


		public Integer getFindingStatusId() {
			return findingStatusId;
		}

		public void setFindingStatusId(Integer findingStatusId) {
			this.findingStatusId = findingStatusId;
		}

		public String getVesselName() {
			return vesselName;
		}

		public void setVesselName(String vesselName) {
			this.vesselName = vesselName;
		}

		public String getAuditTypeDesc() {
			return auditTypeDesc;
		}

		public void setAuditTypeDesc(String auditTypeDesc) {
			this.auditTypeDesc = auditTypeDesc;
		}

		public String getAudSubTypeDesc() {
			return audSubTypeDesc;
		}

		public void setAudSubTypeDesc(String audSubTypeDesc) {
			this.audSubTypeDesc = audSubTypeDesc;
		}

		public String getAuditElement() {
			return auditElement;
		}

		public void setAuditElement(String auditElement) {
			this.auditElement = auditElement;
		}

		public String getCategoryDesc() {
			return categoryDesc;
		}

		public void setCategoryDesc(String categoryDesc) {
			this.categoryDesc = categoryDesc;
		}

		public String getFindingStatusDesc() {
			return findingStatusDesc;
		}

		public void setFindingStatusDesc(String findingStatusDesc) {
			this.findingStatusDesc = findingStatusDesc;
		}

		public Long getCompanyId() {
			return companyId;
		}

		public void setCompanyId(Long companyId) {
			this.companyId = companyId;
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

		public String getNextActionIdDesc() {
			return nextActionIdDesc;
		}

		public void setNextActionIdDesc(String nextActionIdDesc) {
			this.nextActionIdDesc = nextActionIdDesc;
		}

		public CarSearchResult() {
			super();
			// TODO Auto-generated constructor stub
		}
		
}
