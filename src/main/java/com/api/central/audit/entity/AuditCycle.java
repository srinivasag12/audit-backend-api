package com.api.central.audit.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="AUDIT_CYCLE")
@IdClass(AuditCycleCPK.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuditCycle implements Serializable{
	
	@Id
	@Column(name="COMPANY_ID")
	@Range(min=1,max=100,message="Company Name cannot be empty")
	private Long companyId;
	
	@Id
	@Range(min=7,max=999999999,message="Vessel IMO No cannot be empty")
	@Column(name="VESSEL_IMO_NO" )
	private Integer vesselImoNo;
	

	@Id
	@Column(name="AUDIT_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditTypeId;
	
	
	@Column(name="ITERATOR" )
	private Integer iterator;
	
	@Column(name="VESSEL_ID")
	private Integer vesselId;
	
	@Column(name="COMPANY_IMO_NO")
	@NotEmpty(message="Company IMO cannot be empty")
	private String companyImoNo;
	
	@Column(name="COMPANY_DOC")
	private String companyDoc;
	
	@Column(name="DOC_TYPE_NUMBER")
	private String docTypeNumber;
	
   
	@Column(name="AUDIT_SEQ_NO")
	private Integer auditSeqNo;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="NEXT_INTERMEDIATE_START")
	private Date nextIntermediateStart;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="NEXT_INTERMEDIATE_END")
	private Date nextIntermediateEnd;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="NEXT_RENEWAL")
	private Date nextRenewal;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name="USER_INS")
	@NotNull
	private String userIns;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@UpdateTimestamp
	@LongDateFormat
	@Column(name="DATE_INS")
	@NotNull
	private Timestamp dateIns;
	
	

	@Formula("(SELECT CONCAT(A.FIRST_NAME || ' ',A.LAST_NAME) FROM MA_USERS A WHERE A.USER_ID=(SELECT B.USER_ID FROM AUDIT_AUDITOR_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.AUD_LEAD_STATUS = 1)  AND A.COMPANY_ID = COMPANY_ID)")
	private String leadAuditorName;
	
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="CREDIT_DATE")
	private Date creditDate;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	@ShortDateFormat
	@Column(name="AUDIT_DATE")
	private Date auditDate;
	
	
	@Column(name="AUDIT_SUB_TYPE_ID")
	@Range(min=1000,max=10000,message="Audit Type cannot be empty")
	private Integer auditSubTypeId;
	
	
	@Formula("(SELECT A.VESSEL_NAME FROM  MA_VESSEL A  WHERE A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID)")
	private String vesselName;
	
	
	@Formula("(SELECT A.AUDIT_SUBTYPE_DESC FROM MA_AUDIT_SUBTYPE A WHERE A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND A.AUDIT_SUB_TYPE_ID = AUDIT_SUB_TYPE_ID AND A.COMPANY_ID = COMPANY_ID)")
	private String audSubTypeDesc;	
	
	@Formula("(SELECT B.AUDIT_TYPE_DESC FROM MA_AUDIT_TYPE B WHERE B.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND B.COMPANY_ID = COMPANY_ID)")
	private String audTypeDesc;
	
	@Transient
	Long AuditCycleHistirySize;
	
	@Column(name="LEAD_NAME")
	private String leadNameFromAuditCycle;
	
   @Column(name="ROLE_ID")
   private Integer roleId;
	
  
   @Column(name="SUB_ITERATOR" )
   private Integer subIterator; 
   


    @Column(name="AUDIT_STATUS_ID")	
	@Range(min=0,max=1000000,message="Audit Status cannot be empty")
	private Integer auditStatusId;
    
    @Id
    @Column(name="CYCLE_SEQ_NO")	
	private Integer cycleSeqNo;
 
    
  

	@Column(name="CYCLE_GEN_NO")	
	private Integer cycleGenNo;
	

	@Formula("(SELECT B.AUDIT_SUB_TYPE_ID FROM AUDIT_DETAILS B WHERE B.AUDIT_SEQ_NO=CYCLE_SEQ_NO AND  B.COMPANY_ID = COMPANY_ID AND ROWNUM =1)")
	private Integer auditSubTypeCycleGen;
  
	
	@ShortDateFormat
	@Column(name="NEXT_RENEWAL_START")
	private Date nextRenewalStart;
	
	

@JsonIgnore
	@OneToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID" ,insertable = false, updatable = false),
	  	@JoinColumn(name = "AUDIT_TYPE_ID", referencedColumnName = "AUDIT_TYPE_ID" ,insertable = false, updatable = false),
	    @JoinColumn(name = "AUDIT_SEQ_NO", referencedColumnName = "AUDIT_SEQ_NO" , insertable = false, updatable = false),
			
			})
	
	private AuditDetail auditDetail;



    @Formula("(SELECT CREDIT_DATE FROM AUDIT_DETAILS B WHERE B.AUDIT_SEQ_NO=AUDIT_SEQ_NO AND B.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND B.COMPANY_ID = COMPANY_ID AND ROWNUM =1)")
    private Date creditDateOfCurrentAudit;
    
	@Formula("(SELECT COUNT(*)  FROM AUDIT_DETAILS A WHERE  A.ALLOW_NEXT=1  AND A.VESSEL_IMO_NO=VESSEL_IMO_NO  AND A.COMPANY_ID = COMPANY_ID AND A.AUDIT_TYPE_ID=AUDIT_TYPE_ID AND  A.AUDIT_SUB_TYPE_ID=1004 AND ROWNUM=1)")
	private Integer RenewalCompltedForCycle;
	
	
	public Integer getRenewalCompltedForCycle() {
		return RenewalCompltedForCycle;
	}

	public void setRenewalCompltedForCycle(Integer renewalCompltedForCycle) {
		RenewalCompltedForCycle = renewalCompltedForCycle;
	}


    @Transient
    private String createOrUpdate;
    
    
    @ShortDateFormat
	@Column(name="INTERMEDIATE_DUE_DATE")
	private Date intermediateDueDate;
	
	@ShortDateFormat
	@Column(name="NEXT_RENEWAL_DUE_DATE")
	private Date nextRenewalDueDate;
	
	
   
    


	public Date getIntermediateDueDate() {
		return intermediateDueDate;
	}


	public void setIntermediateDueDate(Date intermediateDueDate) {
		this.intermediateDueDate = intermediateDueDate;
	}


	public Date getNextRenewalDueDate() {
		return nextRenewalDueDate;
	}


	public void setNextRenewalDueDate(Date nextRenewalDueDate) {
		this.nextRenewalDueDate = nextRenewalDueDate;
	}


	public String getCreateOrUpdate() {
		return createOrUpdate;
	}


	public void setCreateOrUpdate(String createOrUpdate) {
		this.createOrUpdate = createOrUpdate;
	}


	public Date getCreditDateOfCurrentAudit() {
		return creditDateOfCurrentAudit;
	}


	public void setCreditDateOfCurrentAudit(Date creditDateOfCurrentAudit) {
		this.creditDateOfCurrentAudit = creditDateOfCurrentAudit;
	}


	public Date getNextRenewalStart() {
	return nextRenewalStart;
}


public void setNextRenewalStart(Date nextRenewalStart) {
	this.nextRenewalStart = nextRenewalStart;
}


	public Integer getAuditSubTypeCycleGen() {
	return auditSubTypeCycleGen;
}


public void setAuditSubTypeCycleGen(Integer auditSubTypeCycleGen) {
	this.auditSubTypeCycleGen = auditSubTypeCycleGen;
}


	public Long getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Integer getIterator() {
		return iterator;
	}

	public void setIterator(Integer iterator) {
		this.iterator = iterator;
	}

	public Integer getVesselId() {
		return vesselId;
	}

	public void setVesselId(Integer vesselId) {
		this.vesselId = vesselId;
	}

	public String getCompanyImoNo() {
		return companyImoNo;
	}

	public void setCompanyImoNo(String companyImoNo) {
		this.companyImoNo = companyImoNo;
	}

	public String getCompanyDoc() {
		return companyDoc;
	}

	public void setCompanyDoc(String companyDoc) {
		this.companyDoc = companyDoc;
	}

	public String getDocTypeNumber() {
		return docTypeNumber;
	}

	public void setDocTypeNumber(String docTypeNumber) {
		this.docTypeNumber = docTypeNumber;
	}

	public Integer getAuditSeqNo() {
		return auditSeqNo;
	}

	public void setAuditSeqNo(Integer auditSeqNo) {
		this.auditSeqNo = auditSeqNo;
	}

	public Date getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}

	public Date getNextIntermediateStart() {
		return nextIntermediateStart;
	}

	public void setNextIntermediateStart(Date nextIntermediateStart) {
		this.nextIntermediateStart = nextIntermediateStart;
	}

	public Date getNextIntermediateEnd() {
		return nextIntermediateEnd;
	}

	public void setNextIntermediateEnd(Date nextIntermediateEnd) {
		this.nextIntermediateEnd = nextIntermediateEnd;
	}

	public Date getNextRenewal() {
		return nextRenewal;
	}

	public void setNextRenewal(Date nextRenewal) {
		this.nextRenewal = nextRenewal;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
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
	
	public String getLeadAuditorName() {
		return leadAuditorName;
	}

	public void setLeadAuditorName(String leadAuditorName) {
		this.leadAuditorName = leadAuditorName;
	}
	
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	
	public Integer getAuditSubTypeId() { System.out.println("56838788738787");
		return auditSubTypeId;
	}

	public void setAuditSubTypeId(Integer auditSubTypeId) {
		this.auditSubTypeId = auditSubTypeId;
	}
	
	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	
	
	public String getAudSubTypeDesc() { 
		return audSubTypeDesc;
	}

	public void setAudSubTypeDesc(String audSubTypeDesc) {
		this.audSubTypeDesc = audSubTypeDesc;
	}
	
	public String getAudTypeDesc() {
		return audTypeDesc;
	}



	public void setAudTypeDesc(String audTypeDesc) {
		this.audTypeDesc = audTypeDesc;
	}
	


	public Long getAuditCycleHistirySize() {
		return AuditCycleHistirySize;
	}



	public void setAuditCycleHistirySize(Long auditCycleHistirySize) {
		this.AuditCycleHistirySize = auditCycleHistirySize;
	}

	public String getLeadNameFromAuditCycle() {
		return leadNameFromAuditCycle;
	}


	public void setLeadNameFromAuditCycle(String leadNameFromAuditCycle) {
		this.leadNameFromAuditCycle = leadNameFromAuditCycle;
	}

	public Integer getRoleId() {
	return roleId;
    }


    public void setRoleId(Integer roleId) {
	this.roleId = roleId;
   }

    
    
    
	public Integer getSubIterator() {
	return subIterator;
    }


    public void setSubIterator(Integer subIterator) {
 	this.subIterator = subIterator;
    }
    
    public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}
	
	public Integer getAuditStatusId() {
			return auditStatusId;
	}	


	public void setAuditStatusId(Integer auditStatusId) {
			this.auditStatusId = auditStatusId;
	}
	
	 
	   public Integer getCycleSeqNo() {
			return cycleSeqNo;
		}


		public void setCycleSeqNo(Integer cycleSeqNo) {
			this.cycleSeqNo = cycleSeqNo;
		}
		
		  public Integer getCycleGenNo() {
				return cycleGenNo;
			}


			public void setCycleGenNo(Integer cycleGenNo) {
				this.cycleGenNo = cycleGenNo;
			}


			@Override
			public String toString() {
				return "AuditCycle [companyId=" + companyId + ", vesselImoNo=" + vesselImoNo + ", auditTypeId="
						+ auditTypeId + ", iterator=" + iterator + ", vesselId=" + vesselId + ", companyImoNo="
						+ companyImoNo + ", companyDoc=" + companyDoc + ", docTypeNumber=" + docTypeNumber
						+ ", auditSeqNo=" + auditSeqNo + ", nextIntermediateStart=" + nextIntermediateStart
						+ ", nextIntermediateEnd=" + nextIntermediateEnd + ", nextRenewal=" + nextRenewal
						+ ", activeStatus=" + activeStatus + ", userIns=" + userIns + ", dateIns=" + dateIns
						+ ", leadAuditorName=" + leadAuditorName + ", creditDate=" + creditDate + ", auditDate="
						+ auditDate + ", auditSubTypeId=" + auditSubTypeId + ", vesselName=" + vesselName
						+ ", audSubTypeDesc=" + audSubTypeDesc + ", audTypeDesc=" + audTypeDesc
						+ ", AuditCycleHistirySize=" + AuditCycleHistirySize + ", leadNameFromAuditCycle="
						+ leadNameFromAuditCycle + ", roleId=" + roleId + ", subIterator=" + subIterator
						+ ", auditStatusId=" + auditStatusId + ", cycleSeqNo=" + cycleSeqNo + ", cycleGenNo="
						+ cycleGenNo + ", auditSubTypeCycleGen=" + auditSubTypeCycleGen + ", nextRenewalStart="
						+ nextRenewalStart + ", auditDetail=" + auditDetail + "]";
			}


			










	
	
	
	
}
