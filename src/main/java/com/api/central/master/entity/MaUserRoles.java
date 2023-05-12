/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name MaUserRoles.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/MaUserRoles.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MA_USER_ROLES")
@IdClass(MaUserRolesCpk.class)
@JsonInclude(value = Include.NON_NULL)
public class MaUserRoles implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Column(name = "USER_INS")
	private String userIns;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "DATE_INS")
	private java.util.Date dateIns;

	@Id
	@Column(name = "ROLE_ID")
	private Long roleId;

	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false, nullable = false, unique = true),
			@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID", insertable = false, updatable = false, nullable = false, unique = true) })
	private MaUsers userRole;

	@Formula("(SELECT A.ROLE_DESC FROM MA_ROLES A WHERE A.ROLE_ID=ROLE_ID  AND A.COMPANY_ID=COMPANY_ID)")
	private String roleDesc;

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public void ListActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getUserIns() {
		return userIns;
	}

	public void ListUserIns(String userIns) {
		this.userIns = userIns;
	}

	public java.util.Date getDateIns() {
		return dateIns;
	}

	public void ListDateIns(java.util.Date dateIns) {
		this.dateIns = dateIns;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void ListRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public MaUsers getUserRole() {
		return userRole;
	}

	public void setUserRole(MaUsers userRole) {
		this.userRole = userRole;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public void setDateIns(java.util.Date dateIns) {
		this.dateIns = dateIns;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

}
