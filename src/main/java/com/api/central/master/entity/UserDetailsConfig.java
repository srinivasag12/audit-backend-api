/**
* $ Copyright 2018 BSOL Systems- IRI To Present - All rights reserved $
* $ History – File Name UserDetailsConfig.java $
******************  Version 1.0.0 *****************
* & Author – Tharani priya   DateTime – Created Date Thu Mar 30 2017 15:23:44 GMT+0530 (India Standard Time) $
* $ Created in -  File Name with Package Structure /CentralAuditApi/src/main/java/com/api/central/master/entity/UserDetails.java $
**/

package com.api.central.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "USER_DETAILS_CONFIG")
@IdClass(UserDetailsConfigCpk.class)
public class UserDetailsConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "OLD_PASSWORD")
	private String oldPassword;

	@Column(name = "NEW_PASSWORD")
	private String newPassword;

	@Column(name = "CONFIRM_NEW_PASSWORD")
	private String confirmNewPassword;

	@Column(name = "DISPLAY_NAME")
	private String displayName;

	@Column(name = "SEARCH_RESULT")
	private String searchResult;

	@Id
	@Column(name = "USER_ID")
	private String emailId;
	
	@Id
	@Column(name = "SEQUENCE_NUMBER",unique=true)
	private String userId;

	@Id
	@Column(name = "COMPANY_ID")
	private Long companyId;

	@Column(name = "DEFAULT_HOME_SCREEN")
	private String defaultHomeScreen;

	@Column(name = "MANAGER_NAME")
	private String managerName;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "HEADER_COLOUR")
	private String headerColor;

	@Column(name = "BACKGROUND_COLOUR")
	private String backgroundColor;

	@Column(name = "FONT_COLOUR")
	private String fontColor;

	@Column(name = "BUTTON_COLOUR")
	private String buttonColor;

	@Column(name = "HEADER_FONT_COLOUR")
	private String headerFontColor;

	@Column(name = "IDENTITY")
	private byte[] userIdentification;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getDefaultHomeScreen() {
		return defaultHomeScreen;
	}

	public void setDefaultHomeScreen(String defaultHomeScreen) {
		this.defaultHomeScreen = defaultHomeScreen;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getButtonColor() {
		return buttonColor;
	}

	public void setButtonColor(String buttonColor) {
		this.buttonColor = buttonColor;
	}

	public String getHeaderFontColor() {
		return headerFontColor;
	}

	public void setHeaderFontColor(String headerFontColor) {
		this.headerFontColor = headerFontColor;
	}

	public byte[] getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(byte[] userIdentification) {
		this.userIdentification = userIdentification;
	}

	
}
