package com.api.central.master.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@IdClass(MasterTableUpdateCpk.class)
@Table(name = "MASTER_TABLE_UPDATE")
@JsonInclude(value = Include.NON_NULL)
public class MasterTableUpdate {
	


	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TABLE_NAME")
	private String tableName;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;
	
	@Column(name="USER_INS")
	private String userIns;
	
	@Column(name="DATE_INS")
	private Date dateIns;
	
	@Column(name="TABLE_UPDATION")
	private Integer tableUpdation;

	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public Date getDateIns() {
		return dateIns;
	}

	public void setDateIns(Date dateIns) {
		this.dateIns = dateIns;
	}

	public Integer getTableUpdation() {
		return tableUpdation;
	}

	public void setTableUpdation(Integer tableUpdation) {
		this.tableUpdation = tableUpdation;
	}

	

	

	
	
	
	
	


}
