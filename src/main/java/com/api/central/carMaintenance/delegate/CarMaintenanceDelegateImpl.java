package com.api.central.carMaintenance.delegate;
/**
 * @author sourav ghadai
 *
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.CarSearchCriteria;
import com.api.central.audit.entity.CarSearchResult;
import com.api.central.audit.entity.FindingDetail;
import com.api.central.audit.entity.FindingRptAttach;
import com.api.central.carMaintenance.dao.CarMaintenanceDao;
import com.api.central.util.AppConstant;
@Service
@Transactional(readOnly=true)
public class CarMaintenanceDelegateImpl implements CarMaintenanceDelegate {
	
	@Autowired
	private CarMaintenanceDao carMaintenanceDao;
	
	@Autowired
	ServletContext servletContext;
	
	@Override
	public Long getHistorySearchCount(CarSearchCriteria carSearchCriteria) {
		// TODO Auto-generated method stub
		return carMaintenanceDao.getHistorySearchCount(carSearchCriteria);	
	}

	@Override
	public List<CarSearchResult> getHistorySearchResult(CarSearchCriteria carSearchCriteria) {
		// TODO Auto-generated method stub
		return carMaintenanceDao.getHistorySearchResult(carSearchCriteria);
	}

	@Override
	public Map<String, Object> getHistoryFindingData(Integer auditSeqNo, Integer findingSeqNo, Long companyId) {
		// TODO Auto-generated method stub
		return carMaintenanceDao.getHistoryFindingData(auditSeqNo,findingSeqNo);
	}

	@Override
	public Map<String, Object> checkAuditCompleted(Integer vesselImoNo, Integer auditTypeId, java.util.Date auditDate, Integer auditStatusId, Long companyId) {
		// TODO Auto-generated method stub
		return carMaintenanceDao.checkAuditCompleted(vesselImoNo, auditTypeId, auditDate, auditStatusId);
	}

	@Override
	@Transactional(readOnly=false)
	public FindingDetail updateCarMaintainanceFinding(FindingDetail findingDetail,String updateFlag, String auditTypeDesc, Long companyId) {
 		if (findingDetail != null) {
 		int auditSeqNo = findingDetail.getOrigAuditSeqNo();
 		int findingSeqNo = findingDetail.getFindingSeqNo();
				
					File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR + auditTypeDesc + AppConstant.SEPARATOR
							+ auditSeqNo + AppConstant.SEPARATOR + findingSeqNo + AppConstant.SEPARATOR + findingDetail.getStatusSeqNo());
 					if (!directory.exists()) {
						directory.mkdirs();
					}
					System.out.println(directory.getAbsolutePath());
					findingDetail.setAuditFinding(new AuditFinding(findingSeqNo, findingDetail.getOrigAuditSeqNo(), new AuditDetail(findingDetail.getOrigAuditSeqNo(), findingDetail.getCompanyId(), findingDetail.getAuditTypeId())));
					
					if (!findingDetail.getFindingRptAttachs().isEmpty()) {
						
						findingDetail.getFindingRptAttachs().forEach((findingReportAttach) -> {
							try {
								if(findingReportAttach.getFindingFileByte()!=null){
								Path path = Paths.get(directory +AppConstant.SEPARATOR+ findingReportAttach.getFileName());
 								Files.write(path, findingReportAttach.getFindingFileByte());
								}
							} catch (Exception e) {
 								e.printStackTrace();
							}
							findingReportAttach.setFindingDetail(findingDetail);
 						});
					
 		        }
		}
		
		return carMaintenanceDao.updateCarMaintainanceFinding(findingDetail,updateFlag);
	 
	}

 
	@Override
	@Transactional(readOnly=false)
	public FindingRptAttach unlinkFindingFiles(FindingRptAttach findingRptAttach, String auditTypeDesc,Long companyId) {
		// TODO Auto-generated method stub
		
		findingRptAttach.setFindingDetail(new FindingDetail(findingRptAttach.getStatusSeqNo(),new AuditFinding(findingRptAttach.getFindingSeqNo(), findingRptAttach.getOrigAuditSeqNo(), new AuditDetail(findingRptAttach.getOrigAuditSeqNo(), findingRptAttach.getCompanyId(), findingRptAttach.getAuditTypeId()))));
		Boolean status = carMaintenanceDao.unlinkFindingFiles(findingRptAttach);
		
		if(status){
		File directory = new File(servletContext.getContextPath() +AppConstant.SEPARATOR +companyId+  AppConstant.SEPARATOR
				+ auditTypeDesc + AppConstant.SEPARATOR + findingRptAttach.getOrigAuditSeqNo() + AppConstant.SEPARATOR
				+ findingRptAttach.getFindingSeqNo() + AppConstant.SEPARATOR + findingRptAttach.getStatusSeqNo() + AppConstant.SEPARATOR+ findingRptAttach.getFileName());
		if (directory.exists()) {
			directory.delete();
		}
		}
		return findingRptAttach;
	}

	@Override
	public List<CarSearchResult> getHistorySearchResultReport(CarSearchCriteria searchBean) {
		// TODO Auto-generated method stub
		return carMaintenanceDao.getHistorySearchResultReport(searchBean);
	}

	@Override
	public Map<String, Object> checkAuditorAndNextAdtData(Integer auditSeqNo, Long companyId, String userId, Integer findingSeqNo) {
		// TODO Auto-generated method stub
		return carMaintenanceDao.checkAuditorAndNextAdtData(auditSeqNo,companyId,userId,findingSeqNo);
	}


}
