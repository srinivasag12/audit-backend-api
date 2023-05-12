/**
*
*/
package com.api.central.carMaintenance.dao;

/**
 * @author sourav ghadai
 *
 */
import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.api.central.audit.entity.AuditFinding;
import com.api.central.audit.entity.CarSearchCriteria;
import com.api.central.audit.entity.CarSearchResult;
import com.api.central.audit.entity.FindingDetail;
import com.api.central.audit.entity.FindingRptAttach;

public interface CarMaintenanceDao {

	Long getHistorySearchCount(CarSearchCriteria carSearchCriteria);

	List<CarSearchResult> getHistorySearchResult(CarSearchCriteria carSearchCriteria);

	Map<String, Object> getHistoryFindingData(Integer auditSeqNo, Integer findingSeqNo);

	
	Map<String, Object> checkAuditCompleted(Integer vesselImoNo, Integer auditTypeId, java.util.Date auditDate, Integer auditStatusId);

	FindingDetail updateCarMaintainanceFinding(FindingDetail findingDetail,String updateFlag);

	AuditFinding updateCarMaintainance(AuditFinding auditFinding);

	Boolean unlinkFindingFiles(FindingRptAttach findingRptAttach);

	List<CarSearchResult> getHistorySearchResultReport(CarSearchCriteria searchBean);

	Map<String, Object> checkAuditorAndNextAdtData(Integer auditSeqNo, Long companyId, String userId, Integer findingSeqNo);

}
