package com.api.central.typeahead.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.api.central.master.entity.MaAttachmentTypes;
import com.api.central.master.entity.MaAuditCodes;
import com.api.central.master.entity.MaAuditRoles;
import com.api.central.master.entity.MaAuditStatus;
import com.api.central.master.entity.MaAuditSubtype;
import com.api.central.master.entity.MaAuditSummary;
import com.api.central.master.entity.MaCertificateIssued;
import com.api.central.master.entity.MaCompany;
import com.api.central.master.entity.MaFindingsCategory;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.master.entity.MasterTableUpdate;
import com.api.central.master.entity.RefreshMasterData;
import com.api.central.audit.entity.AuditAuditorDetail;

public interface TypeaheadDelegate {
	
	List<MaAuditSubtype> getAuditSubtype(Long auditType);

	List<MaAuditStatus> getAuditStatus(Long audType);

	List<MaCertificateIssued> getCertificateIssued(Long audType);

	List<MaFindingsStatus> getFindingStatus(Integer audType, Long companyId);
	
	List<MaFindingsStatus> getFindingStatus(Long companyId);

	List<MaAttachmentTypes> getAttachmentTypes(Long audType);

	List<MaAuditSummary> getAuditSummary(Long audType);

	List<MaAuditCodes> getAuditCodes(Long audType);

	List<MaVesselCompany> getVesselCompanyDetails(String cImoNo);

	List<MaAuditRoles> getAuditRoles();

	List<MaUsers> getUser(Long companyId);

	List<MaAuditStatus> getAllAuditStatus();

	List<MaAuditSubtype> getAllAuditSubTypes();

	Map<String, ArrayList> getDistinctFindingStatus(Long companyId);

	Map<String, Object> getMasterData(String value, Long companyId);

	List<MaFindingsCategory> getFindingCategory();

	List<MaFindingsCategory> getFindingCategory(Long audType, Long companyId);
	
	
	Map<String, Object> getData(RefreshMasterData data, String flag, Long companyId);

	List<MaUsers> getAudDetails(Long companyId);

	List<MasterTableUpdate> updateDbdata(List<MasterTableUpdate> masterTableUpdates);

	List<MasterTableUpdate> getUpdatedDbdata(Long companyId);

	List<MaCompany> getDomainNameCentral(Long companyId);

	List<AuditAuditorDetail> leadAuditorNames(Long companyId);

	Map<String, Object> getDashboardData();

	

}
