package com.api.central.typeahead.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.audit.entity.AuditAuditorDetail;
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
import com.api.central.typeahead.dao.TypeaheadDao;

@Service
@Transactional(readOnly=true)
public class TypeaheadDelegateImpl implements TypeaheadDelegate{
	
	@Autowired
	private TypeaheadDao typeaheadDao;

	@Override
	public List<MaAuditSubtype> getAuditSubtype(Long auditType) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAuditSubtype(auditType);
	}

	@Override
	public List<MaAuditStatus> getAuditStatus(Long audType) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAuditStatus(audType);
	}

	@Override
	public List<MaCertificateIssued> getCertificateIssued(Long audType) {
		// TODO Auto-generated method stub
		return typeaheadDao.getCertificateIssued(audType);
	}

	@Override
	public List<MaFindingsStatus> getFindingStatus(Integer audType, Long companyId) {
		// TODO Auto-generated method stub
		return typeaheadDao.getFindingStatus(audType,companyId);
	}
	
	@Override
	public List<MaFindingsStatus> getFindingStatus(Long companyId) {
		return typeaheadDao.getFindingStatus(companyId);
	}

	@Override
	public Map<String, ArrayList> getDistinctFindingStatus(Long companyId) {
		// TODO Auto-generated method stub
		
		return typeaheadDao.getDistinctFindingStatus(companyId);
		
	}
	
	@Override
	public List<MaAttachmentTypes> getAttachmentTypes(Long audType) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAttachmentTypes(audType);
	}

	@Override
	public List<MaAuditSummary> getAuditSummary(Long audType) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAuditSummary(audType);
	}

	@Override
	public List<MaAuditCodes> getAuditCodes(Long audType) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAuditCodes(audType);
	}

	@Override
	public List<MaVesselCompany> getVesselCompanyDetails(String cImoNo) {
		// TODO Auto-generated method stub
		return typeaheadDao.getVesselCompanyDetails(cImoNo);
	}

	@Override
	public List<MaAuditRoles> getAuditRoles() {
		// TODO Auto-generated method stub
		return typeaheadDao.getAuditRoles();
	}

	@Override
	public List<MaUsers> getUser(Long companyId) {
		// TODO Auto-generated method stub
		return typeaheadDao.getUser(companyId);
	}

	@Override
	public List<MaAuditStatus> getAllAuditStatus() {
		// TODO Auto-generated method stub
		return typeaheadDao.getAllAuditStatus();
	}

	@Override
	public List<MaAuditSubtype> getAllAuditSubTypes() {
		// TODO Auto-generated method stub
		return typeaheadDao.getAllAuditSubTypes();
	}

	@Override
	public
	Map<String, Object> getMasterData(String value,Long companyId) {
		// TODO Auto-generated method stub
	
		
		return typeaheadDao.getMasterData(value,companyId);
	}
	
	@Override
	@Transactional(readOnly=false)
	public Map<String, Object> getData(RefreshMasterData data,String flag,Long companyId){
		return typeaheadDao.getData(data, flag,companyId);
	}

	@Override
	public List<MaFindingsCategory> getFindingCategory(Long audType, Long companyId) {
		
		return typeaheadDao.getFindingCategory(audType,companyId);
	}
	
	@Override
	public List<MaFindingsCategory> getFindingCategory() {
		
		return typeaheadDao.getFindingCategory();
	}

	@Override
	public List<MaUsers> getAudDetails(Long companyId) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAudDetails(companyId);
	}

	@Override
	@Transactional(readOnly = false)
	public List<MasterTableUpdate> updateDbdata(List<MasterTableUpdate> masterTableUpdates) {
		// TODO Auto-generated method stub
		return typeaheadDao.updateDbdata(masterTableUpdates);
	}

	@Override
	public List<MasterTableUpdate> getUpdatedDbdata(Long companyId) {
		// TODO Auto-generated method stub
		return typeaheadDao.getUpdatedDbdata(companyId);
	}
	
	
	@Override
	public List<AuditAuditorDetail> leadAuditorNames(Long companyId) {
		// TODO Auto-generated method stub
		return typeaheadDao.leadAuditorNames(companyId);
	}

	@Override
	public List<MaCompany> getDomainNameCentral(Long companyId) {
		// TODO Auto-generated method stub
		return typeaheadDao.getAllCompanyDet(companyId);
	}

	@Override
	public Map<String, Object> getDashboardData() {
		return typeaheadDao.getDashboardData();
	}

}
