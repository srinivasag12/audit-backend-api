package com.api.central.master.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.api.annotations.LongDateFormat;
import com.api.annotations.ShortDateFormat;
import java.sql.Timestamp;
@Entity
@Table(name = "USER_SESSION_DETAIL")
@IdClass(UserSessionDetailCPK.class)
public class UserSessionDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Id
	@Column(name = "SL_NO")
	private Integer slNo;	

	
	@Column(name = "STATUS")
	private String status;
	
	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;
	
	
	@Column(name = "LOGIN_TIME")
	private String loginTime;
	
	
	@Column(name = "LOGOUT_TIME")
	private String logoutTime;
	
	@Column(name= "LOCATION")
	private String location;  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022
	
	@Column(name= "DEVICE")
	private String device;  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here

	
	
	@Transient
	private Integer updateMode;
	
	
	


	public Integer getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(Integer updateMode) {
		this.updateMode = updateMode;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}
	/**Added by sudharsan 
	 *For Jira-Id IRI-5482
	 *on 16-09-2022 Start Here*/
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	/**Added by sudharsan 
	 *For Jira-Id IRI-5482
	 *on 16-09-2022 End here*/

	@Override
	public String toString() {
		return "UserSessionDetail [userId=" + userId + ", slNo=" + slNo + ", status=" + status + ", companyId="
				+ companyId + ", loginTime=" + loginTime + ", logoutTime=" + logoutTime + ", location=" + location
				+ ", device=" + device + "]";
	}
		

	
	
}