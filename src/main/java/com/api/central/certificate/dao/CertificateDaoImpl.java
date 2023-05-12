package com.api.central.certificate.dao;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.BeanUtils; 
import com.api.central.audit.dao.AuditDaoImpl;
import com.api.central.audit.entity.AuditAuditorDetail;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.AuditListCertInactive;
import com.api.central.audit.entity.CertificateDetail;
import com.api.central.audit.entity.CertificateDetailWithOutAudit;
import com.api.central.audit.entity.CertificateDetailsCPK;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SspReviewDetail;
import com.api.central.config.AppHibSession;
import com.api.central.master.entity.MaFindingsStatus;
import com.api.central.master.entity.MaVessel;
import com.api.central.master.entity.MaVesselCompany;
import com.api.central.rmiaudit.entity.RmiAudit;
import com.api.central.rmiaudit.entity.RmiAuditCertEndorsementModel;
import com.api.central.rmiaudit.entity.RmiAuditCertEndorsements;
import com.api.central.rmiaudit.entity.RmiAuditCertificate;
import com.api.central.rmiaudit.entity.RmiCertificates;
import com.api.central.security.EncodeDecode;
import com.api.central.typeahead.delegate.LoadMasterService;
import com.api.central.util.AppConstant;
import com.api.central.util.AppSQLConstant;
import com.api.central.util.AppUtil;
import com.api.central.util.MasterDatas;
import com.api.central.util.RestTemplateUtil;
import com.api.central.util.SequenceGenerator;


@Repository
@SuppressWarnings({"unused","unchecked","deprecation"})
public class CertificateDaoImpl implements CertificateDao{

	@Autowired
	private AppHibSession hibsession;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	public LoadMasterService loadMasterService;
	
//	@Autowired
//	private RestTemplate restTemplate;
	
	@Autowired
	private RestTemplateUtil restUtil;
	
	static Logger log = LoggerFactory.getLogger(CertificateDaoImpl.class);
	
	
	@Override
	public List<CertificateDetail> getAuditCertificateInActive(Integer vesselImoNo, Long companyId,
			String certificateNo, Integer auditTypeId, java.util.Date auditDate, boolean directInterorAdd) {

		CertificateDetail certificate = new CertificateDetail();
		
		Criteria criteria = hibsession.getSession().createCriteria(CertificateDetail.class, "audit");
		
		if(certificate.getAuditStatusId() != null)
		{
			criteria.add(Restrictions.ne("audit.auditStatusId", AppConstant.VOID_AUDIT_STATUS));
		}
		//added by sudharsan for Jira-ID = 5511 , 5534, 5536, 5536
		else if (directInterorAdd) {
			String query = "SELECT CERT_ORDER_NO "
						+ "FROM CERTIFICATE_DETAIL A INNER JOIN (SELECT MAX(CD.CERTIFICATE_NO) AS MX_CERT_NO, MAX(CD.SEQ_NO) AS MX_SEQ_NO ,MAX(CD.AUDIT_SEQ_NO) AS AUDIT_SEQ_NO "
						+ "FROM CERTIFICATE_DETAIL CD WHERE CD.AUDIT_TYPE_ID = :auditTypeId AND CD.VESSEL_IMO_NO= :vesselImoNo "
						+ "AND COMPANY_ID = :companyId AND CD.CERTIFICATE_NO <= :certificateNo AND  CD.AUDIT_SEQ_NO <> "
						+ AppConstant.ISM_NO_AUD_CERT_AUDITSEQ + " AND CD.AUDIT_SEQ_NO <> "
						+ AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ + "AND CD.AUDIT_SEQ_NO <> "
						+ AppConstant.MLC_NO_AUD_CERT_AUDITSEQ + " AND CD.AUDIT_SEQ_NO <> "
						+ AppConstant.IHM_NO_AUD_CERT_AUDITSEQ
						+ " GROUP BY CD.AUDIT_SEQ_NO) B ON A.CERTIFICATE_NO = B.MX_CERT_NO AND A.SEQ_NO = B.MX_SEQ_NO AND B.AUDIT_SEQ_NO = A.AUDIT_SEQ_NO";
				List<Integer> certOrderList = (
						(List<BigDecimal>) hibsession.getSession()
						.createSQLQuery(query)
						.setParameter("vesselImoNo", vesselImoNo)
						.setParameter("auditTypeId", auditTypeId)
						.setParameter("certificateNo", certificateNo) //Added by sudharsan for Jira-ID IRI-5640,IRI-5641,IRI-5642
						.setParameter("companyId", companyId)
						.list()
						).stream().map(x -> Integer.parseInt(x.toPlainString(), 10)).collect(Collectors.toList());

				criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
						.add(Restrictions.eq("audit.companyId", companyId))
						.add(Restrictions.eq("audit.auditTypeId", auditTypeId))
						.add(Restrictions.le("audit.certificateNo", certificateNo));
					//	.add(Restrictions.in("audit.certOderNo", certOrderList)); //Commented by sudharsan for Jira-ID = IRI-5694
		}//End here Jira-ID = 5511 , 5534, 5536, 5536
		else {
			criteria.add(Restrictions.eq("audit.vesselImoNo", vesselImoNo))
					.add(Restrictions.eq("audit.companyId", companyId))
					.add(Restrictions.eq("audit.auditTypeId", auditTypeId))
					.add(Restrictions.eq("audit.certificateNo", certificateNo))
					.add(Restrictions.eq("audit.activeStatus", 0));
		}

		/* .add(Restrictions.ge("audit.auditDate",auditDate)); */
		List<CertificateDetail> serachList = criteria.list();

		if (serachList.size() > 0) {
			Map<String, Object> map = null;
			if(serachList.get(0).getAuditTypeId() == AppConstant.ISPS_TYPE_Id && serachList.get(0).getAuditSubTypeId() == AppConstant.INTERIM_SUB_TYPE_ID){	
				map = getConsective(serachList.get(0).getVesselImoNo(), serachList.get(0).getAuditSeqNo(), serachList.get(0).getCompanyId());
			
				for(int i=0;i<serachList.size();i++){
					if(serachList.get(i).getConsecutiveId() != null){
						if(serachList.get(i).getConsecutiveId() == 1001){
							serachList.get(i).setPrevAuditIssueData(map.get("previousIssueDate"));	
						}
					}
				}
			
			
			}
		}
		if(!serachList.isEmpty())
		{
			for(int i=0;i<serachList.size();i++)
			{
				String Cno=serachList.get(i).getCertificateNo();
		        String Utn=serachList.get(i).getUtn();
		        String QidEncode="";
				try 
				{	
					String addCnoUtn=Cno+" "+Utn;
				Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");
				String s= (String) q.uniqueResult();
				QidEncode =s;
					//QidEncode = e.EncodeCertiNoUtn(Cno, Utn);
				} 
				catch (Throwable e)
				{
					e.printStackTrace();
				} 
		        serachList.get(i).setQid(QidEncode);    
			}
		}
		
		
		return serachList;

	}
	public RmiCertificates setRmiCertData(CertificateDetail certificateDetail) {
				
		RmiCertificates rmiCert = new RmiCertificates();
		System.err.println("insidecertificatedaoIMPL 184"+certificateDetail);
		rmiCert.setAuditID(certificateDetail.getAuditSeqNo());
		rmiCert.setCertificateID(certificateDetail.getCertificateId());
		rmiCert.setCertificateNumber(certificateDetail.getCertificateNo());
		rmiCert.setUniqueTrackingNumber(certificateDetail.getUtn());
		rmiCert.setCertificateURL(certificateDetail.getQrCodeUrl());
		rmiCert.setCertificateIssueType(MasterDatas.getCertificateIssueType(certificateDetail.getCertIssueId() ,certificateDetail.getAuditTypeId(),certificateDetail.getRoleId()));
		//rmiCert.setCertificateReasons(certificateDetail.getNotes());
		// Added by sudharsan for Jira-ID = 5678 start here
				if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1001){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_1);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1002){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_2);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1003){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_3);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1004){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_4);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1005){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_5);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1006){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_6);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1007){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_7);
				}
				else if(certificateDetail.getCertReissueReason()!=null && certificateDetail.getCertReissueReason() == 1008){
					rmiCert.setCertificateReasons(AppConstant.REISSUEREASON_8);
				}
				// Added by sudharsan for Jira-ID = 5678 End here
		//Added by sudharsan for TICKET-512 On 22-06-2022
	    String decoded = new String(Base64.getDecoder().decode(certificateDetail.getAuditPlace()));
	
	    	String newDecoded = null;
			try {
				newDecoded = URLDecoder.decode(decoded, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	    	rmiCert.setPlaceofissue(newDecoded);
	    	//End here
		if(certificateDetail.getExtendedIssueDate() != null) {
			rmiCert.setIssueDate(certificateDetail.getExtendedIssueDate() );
		}
		else {
			rmiCert.setIssueDate(certificateDetail.getCertIssueDate());
		}
		if(certificateDetail.getExtendedExpireDate() != null) {
			rmiCert.setExpirationDate(certificateDetail.getExtendedExpireDate() );
		}
		else {
			rmiCert.setExpirationDate(certificateDetail.getCertExpireDate());
		}
		
		rmiCert.setIssuedBy(certificateDetail.getIssuerName());
		rmiCert.setCertificateStatus(certificateDetail.getActiveStatus() == 1 ? "ACTIVE" : "IN-ACTIVE");
		rmiCert.setVesselName(certificateDetail.getVesselName());
		rmiCert.setVesselID(certificateDetail.getVesselId());
		rmiCert.setVesselUK(certificateDetail.getVesselUk());
		rmiCert.setVesselPK(certificateDetail.getVesselPk());
		rmiCert.setVesselIMONumber(certificateDetail.getVesselImoNo().toString());
		rmiCert.setGrossTonnage(certificateDetail.getGrt());
		rmiCert.setClassSociety(certificateDetail.getClassSociety());
		rmiCert.setOfficialNumber(certificateDetail.getOfficialNo().toString());
		rmiCert.setCallSign(certificateDetail.getCallSign());
		rmiCert.setDocTypeNumber(certificateDetail.getDocTypeNumber());
		rmiCert.setCompanyIMONumber(certificateDetail.getCompanyImoNo());
		rmiCert.setDocType(certificateDetail.getDocTypeNo().replaceAll(certificateDetail.getDocTypeNumber(), "").trim());
		rmiCert.setDocIssuer(certificateDetail.getDocIssuer());
		rmiCert.setDocExpiry(certificateDetail.getDocExpiry());
		rmiCert.setCreationDate(certificateDetail.getAuditDate());
		rmiCert.setCreatedBy(certificateDetail.getLeadName());
		rmiCert.setLastUpdateDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));  //modified by sudharsan for INCEDENT_TICKET-671
		rmiCert.setLastUpdatedBy(certificateDetail.getUserInsName()!=null?certificateDetail.getUserInsName():certificateDetail.getIssuerName());
		rmiCert.setExtendDate(certificateDetail.getExtendedEndorsedDate()); 
		if(certificateDetail.getAuditSubTypeId()!=null && certificateDetail.getAuditSubTypeId()==AppConstant.RENEWAL_SUB_TYPE_ID && certificateDetail.getEndorsedDate()!=null){
			rmiCert.setExtendDate(certificateDetail.getEndorsedDate());	
		}
		if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE){
			rmiCert.setIssueDate(certificateDetail.getExtendedIssueDate());
			rmiCert.setExpirationDate(certificateDetail.getExtendedExpireDate());
		}
		if(certificateDetail.getCertIssueId() == AppConstant.EXTENSION ) {
			if( certificateDetail.getAuditTypeId() == AppConstant.ISM_TYPE_ID &&  certificateDetail.getAuditSubTypeId() == AppConstant.INTERIM_SUB_TYPE_ID) {
			rmiCert.setIssueDate(certificateDetail.getExtendedEndorsedDate());
			}
			rmiCert.setExpirationDate(certificateDetail.getExtendedExpireDate());
		}
		if(certificateDetail.getAuditTypeId()!=null){
		rmiCert.setAuditType(MasterDatas.getAuditTypeDesc(certificateDetail.getAuditTypeId()));
		}
		System.err.println("insidecertificatedaoIMPL 254"+rmiCert);
		return rmiCert;
	}
	
	public RmiAuditCertEndorsements setRmiAuditCertEndorsementsData(CertificateDetail certificateDetail){
		
		RmiAuditCertEndorsements rmiCertEndor = new RmiAuditCertEndorsements();
		  
		rmiCertEndor.setAuditID(certificateDetail.getAuditSeqNo());
		rmiCertEndor.setCertificateID(certificateDetail.getCertificateId());
		rmiCertEndor.setEndorsementID(certificateDetail.getEndorsementID() != null ? certificateDetail.getEndorsementID() : sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
		rmiCertEndor.setEndorsementDate(certificateDetail.getEndorsedDate());
		if(certificateDetail.getExtendedEndorsedDate()!=null){
			rmiCertEndor.setEndorsementDate(certificateDetail.getExtendedEndorsedDate());	
		}
		rmiCertEndor.setEndorsementReason(MasterDatas.getCertificateIssueType(certificateDetail.getCertIssueId(),certificateDetail.getAuditTypeId(),certificateDetail.getRoleId())); // need to remove next deployemnt 
		rmiCertEndor.setCompanyId(certificateDetail.getCompanyId());
		rmiCertEndor.setCreatedBy(certificateDetail.getLeadName());
		rmiCertEndor.setCreationDate(certificateDetail.getAuditDate());
		rmiCertEndor.setEndorsedBy(certificateDetail.getIssuerName());
		rmiCertEndor.setLastUpdateDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		rmiCertEndor.setLastUpdatedBy(certificateDetail.getUserInsName()!=null?certificateDetail.getUserInsName():certificateDetail.getIssuerName());
		rmiCertEndor.setVesselId(certificateDetail.getVesselId());
		rmiCertEndor.setVesselName(certificateDetail.getVesselName());
		rmiCertEndor.setExtendDate(certificateDetail.getExtendedEndorsedDate());
		if(certificateDetail.getAuditTypeId()!=null){
			rmiCertEndor.setAuditType(MasterDatas.getAuditTypeDesc(certificateDetail.getAuditTypeId()));
			}
		
		return rmiCertEndor;
	}

	@SuppressWarnings("null")
	@Override
	public Map<String, Object> generateCertificate(CertificateDetail certificateDetail) {
		//System.out.println("certificateDetailvalues"+certificateDetail);
		System.err.println("certificateDetail for lastUpdateddate"+certificateDetail);
		//added by sudharsan for INCEDENT_TICKET-671
		try{
		TimeZone zone = TimeZone.getDefault();      
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
		isoFormat.setTimeZone(TimeZone.getTimeZone(zone.getID().toString()));
		java.util.Date date_ins = isoFormat.parse(certificateDetail.getDateIns().toString());
		certificateDetail.setDateIns(date_ins);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//INCEDENT_TICKET-671 End here
		RmiCertificates rmiCert = null;
		RmiAuditCertEndorsements rmiCertEndor = null;
		RestTemplate restTemplate = new RestTemplate();
	//	HttpHeaders headers = new HttpHeaders();
		HttpEntity<RmiCertificates> rmiCertificatesReq = null;
		HttpEntity<AuditListCertInactive> rmiCertificatesReqmap = null;
		HttpEntity<RmiAuditCertEndorsements> rmiAuditCertEndorsementsReq = null;
		ResponseEntity<String> response = null;
		Long count;
		Map<String,Object> resultMap= new HashMap<String,Object>();
		certificateDetail.setDocTypeNo(certificateDetail.getDocTypeNo().replaceAll(certificateDetail.getDocTypeNumber(), "").trim());
		
		//Query certificateOrderID=hibsession.getSession().createSQLQuery(AppSQLConstant.CERTIFICATE_ORDER_NO);
		Long l=sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ORDER_NO);
		certificateDetail.setCertOderNo(l.intValue());
		if(certificateDetail.getCertificateId()==null){
			
			certificateDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
		}
		
		if(certificateDetail.getCertificateNo()==null || certificateDetail.getCertificateNo().equals("")){
			
			certificateDetail.setCertificateNo(AppUtil.getCertificateNoFormat(certificateDetail.getAuditTypeId(),sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),certificateDetail.getAuditSubTypeId()));
		}
		
		if(certificateDetail.getAuditSubTypeId()!=null && (certificateDetail.getAuditSubTypeId()==AppConstant.INTERMEDIATE_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId()==AppConstant.ADDITIONAL_SUB_TYPE_ID) && certificateDetail.getCertIssueId() != AppConstant.RE_ISSUE){
			
			if(certificateDetail.getEndorsementID()==null){
				
				certificateDetail.setEndorsementID(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
			}
		}
		
		if(certificateDetail.getSeqNo()==0){

			Query getMaxCertSeq = hibsession.getSession().createQuery(AppSQLConstant.GET_MAX_CERT_SEQ);
			
			getMaxCertSeq.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			getMaxCertSeq.setLong("companyId", certificateDetail.getCompanyId());
			getMaxCertSeq.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			
			int seqNo = 0;
			
			List list = getMaxCertSeq.list();
			
			seqNo = (list.isEmpty() || list.get(0)==null) ? 1 : (Integer)list.get(0)+1;
			
			certificateDetail.setSeqNo(seqNo);

			int certificateLink;
			
			Object linkCert = hibsession.getSession().createSQLQuery("select max(certificate_link_seq) from certificate_detail where vessel_imo_no = :vesselImoNo and company_id = :companyId and  audit_type_id = :auditTypeId and audit_seq_no not in (600001,600002,600003,600004)")
					.setParameter("vesselImoNo", certificateDetail.getVesselImoNo())
					.setParameter("companyId", certificateDetail.getCompanyId())
					.setParameter("auditTypeId", certificateDetail.getAuditTypeId()).uniqueResult();;
			
			log.info("linkCert = "+linkCert);

			if(certificateDetail.getCertIssueId() == AppConstant.INTERIM_CERT || certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT || certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE){
				certificateLink = (linkCert != "" && linkCert != null) ? Integer.parseInt(linkCert.toString())+1 : 1;
			}else{
				certificateLink = (linkCert != "" && linkCert != null) ? Integer.parseInt(linkCert.toString()) : 1;
			}

			certificateDetail.setCertificateLink(certificateLink);
			
		}
		
		Criteria audAdtrDtl = hibsession.getSession().createCriteria(CertificateDetail.class)
			
				.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
				.add(Restrictions.eq("auditTypeId", certificateDetail.getAuditTypeId() ))
				.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo() ))
				.add(Restrictions.ne("certIssueId", certificateDetail.getCertIssueId() ));
		
		if(certificateDetail.getAuditTypeId() == AppConstant.ISM_TYPE_ID) {
			audAdtrDtl.add(Restrictions.ne("auditSeqNo", AppConstant.ISM_NO_AUD_CERT_AUDITSEQ));
		}
		else if(certificateDetail.getAuditTypeId() == AppConstant.ISPS_TYPE_Id) {
			audAdtrDtl.add(Restrictions.ne("auditSeqNo", AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ));
		}
		else if(certificateDetail.getAuditTypeId() == AppConstant.MLC_TYPE_ID) {
			audAdtrDtl.add(Restrictions.ne("auditSeqNo", AppConstant.MLC_NO_AUD_CERT_AUDITSEQ));	
				}
		
		audAdtrDtl.setProjection(Projections.rowCount());
		count = (Long) audAdtrDtl.uniqueResult();
		
		if(certificateDetail.getAuditTypeId()!=AppConstant.IHM_TYPE_ID){
		if( certificateDetail.getAuditSubTypeId() !=null && ((certificateDetail.getAuditSubTypeId() == AppConstant.INTERMEDIATE_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID)) && certificateDetail.getCertIssueId() != AppConstant.RE_ISSUE && certificateDetail.getCertIssueId() != AppConstant.EXTENSION ){
			
			rmiCertEndor = setRmiAuditCertEndorsementsData(certificateDetail);
			
		}
		
		else{
			
			rmiCert = setRmiCertData(certificateDetail);
		}
		
			if(count == 0 && (certificateDetail.getAuditSubTypeId() == AppConstant.INTERMEDIATE_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID) && certificateDetail.getCertIssueId() != AppConstant.RE_ISSUE) {
				rmiCert = setRmiCertData(certificateDetail);
			}
			
		}else if(certificateDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID){
			
			if(certificateDetail.getAuditTypeId()!=null && (certificateDetail.getCertIssueId()!=AppConstant.FULL_TERM_CERT  && certificateDetail.getCertIssueId()!=AppConstant.RE_ISSUE )){
				rmiCertEndor = setRmiAuditCertEndorsementsData(certificateDetail); 			System.out.println("6dd666");
			}else{ 
				rmiCert = setRmiCertData(certificateDetail); System.out.println("6dd666d34");
			}
			
			
		}
		
		String Cno=certificateDetail.getCertificateNo();
        String Utn=certificateDetail.getUtn();
        String QidEncode="";
        try {
			String addCnoUtn=Cno+" "+Utn;
			Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");			String s= (String) q.uniqueResult();
			QidEncode =s;
			//QidEncode = e.EncodeCertiNoUtn(Cno, Utn);
		} 
		catch (Throwable e)
		{
			e.printStackTrace();
		}
      
        certificateDetail.setQid(QidEncode);

        //String newQrCodeUrl=certificateDetail.getQrCodeUrl().substring(0, certificateDetail.getQrCodeUrl().indexOf("viewer")+14);
       
        certificateDetail.setQrCodeUrl(AppConstant.cert_url+QidEncode);
        
        if(certificateDetail.getAuditSubTypeId()!=null && certificateDetail.getAuditSubTypeId() == AppConstant.RENEWAL_SUB_TYPE_ID && certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT){
        	
        	certificateDetail.setExtendedIssueDate(null);
        	certificateDetail.setExtendedExpireDate(null);
        	certificateDetail.setEndorsedDate(null);
        }
		
		hibsession.getSession().saveOrUpdate(certificateDetail);
		
		resultMap.put("updatedData",certificateDetail);
		
		/**** make inactive to rest certificate***/
		
		if((certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE || certificateDetail.getCertIssueId() == AppConstant.INTERIM_CERT || certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT || certificateDetail.getCertIssueId() == AppConstant.INTERMEDAITE_ENDORSED || certificateDetail.getCertIssueId() == AppConstant.ADDITIONAL_ENDORSED) && (certificateDetail.getAuditTypeId() != AppConstant.IHM_TYPE_ID || certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE ) ){
			
			Query makeInactiveToRest = null;
			
			 if (certificateDetail.getCertIssueId() == AppConstant.INTERMEDAITE_ENDORSED || certificateDetail.getCertIssueId() == AppConstant.ADDITIONAL_ENDORSED){
				makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL_WHEN_INTERORADITIONAL);
				makeInactiveToRest.setInteger("certIssueId", AppConstant.RE_ISSUE);
			}else{
				makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL);
			}
			 
			makeInactiveToRest.setInteger("activeStatus", AppConstant.INACTIVE_STATUS);
			makeInactiveToRest.setLong("companyId", certificateDetail.getCompanyId());
			makeInactiveToRest.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			makeInactiveToRest.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			makeInactiveToRest.setInteger("seqNo", certificateDetail.getSeqNo());
			makeInactiveToRest.executeUpdate();
		
		}else if((certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED1 || certificateDetail.getCertIssueId() == AppConstant.EXTENSION )&& certificateDetail.getAuditTypeId() != AppConstant.IHM_TYPE_ID){
			
			Query makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_EXTN);
			
			makeInactiveToRest.setInteger("activeStatus", AppConstant.INACTIVE_STATUS);
			makeInactiveToRest.setLong("companyId", certificateDetail.getCompanyId());
			makeInactiveToRest.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			makeInactiveToRest.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			makeInactiveToRest.setInteger("seqNo", certificateDetail.getSeqNo());
			makeInactiveToRest.executeUpdate();
		
		}
		
		/** Making inactive certificate for IHM after generating full term certificate*/ 
		if(certificateDetail.getAuditTypeId() == AppConstant.IHM_TYPE_ID &&  certificateDetail.getCertIssueId() != AppConstant.FULL_TERM_CERT){
			Query makeInactiveToRest = null;
			makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_DTL);
			makeInactiveToRest.setInteger("activeStatus", AppConstant.INACTIVE_STATUS);
			makeInactiveToRest.setLong("companyId", certificateDetail.getCompanyId());
			makeInactiveToRest.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			makeInactiveToRest.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			makeInactiveToRest.setInteger("seqNo", certificateDetail.getSeqNo());
			makeInactiveToRest.executeUpdate();
			
		}
		
		
		/***update or save to local RMI certificate tables***/
		//commented by cb
//		if(rmiCert != null){
//			hibsession.getSession().saveOrUpdate(rmiCert);
//		}else if(rmiCertEndor != null){
//			hibsession.getSession().saveOrUpdate(rmiCertEndor);
//		}
		//condition added by cb for extension data not inserting in our RMI DB and the certificate data are inserted in our RMI DB after 
		//generate should be inserted after publish IRI-5423 and IRI-5424
				if(rmiCert != null && certificateDetail.getPublishStatus() == 1 ){
					if(certificateDetail.getCertIssueId()!= AppConstant.EXTENSION) {
					hibsession.getSession().saveOrUpdate(rmiCert);
					}else if(certificateDetail.getCertIssueId()== AppConstant.EXTENSION) {
						Query query1=hibsession.getSession().createQuery(AppSQLConstant.UPDATE_SMC_VOID_STATUS_CERTIFICATE_RMI_EXTENSION1);
						query1.setParameter("certificateStatus","IN-ACTIVE");
						query1.setParameter("auditID",certificateDetail.getAuditSeqNo());
						query1.executeUpdate();
						
						Query query2=hibsession.getSession().createQuery(AppSQLConstant.UPDATE_SMC_VOID_STATUS_CERTIFICATE_RMI_EXTENSION2);
						query2.setParameter("certificateStatus", "IN-ACTIVE");
						query2.setParameter("certificateNumber", certificateDetail.getCertificateNo());
						query2.executeUpdate();
						
						
						hibsession.getSession().save(rmiCert);
					}
				}else if(rmiCertEndor != null && certificateDetail.getPublishStatus() == 1){
					hibsession.getSession().saveOrUpdate(rmiCertEndor);
				}
		
		
		
		/***If certificate publish then sync to RMI Interface***/
		if(certificateDetail.getPublishStatus()==1){
			List<RmiCertificates> rmiCertAuditList = null;
			
			Criteria auditIdDetails = hibsession.getSession().createCriteria(CertificateDetail.class)
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("auditTypeId", certificateDetail.getAuditTypeId()))
					.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo()))
					.setProjection(Projections.projectionList()
							.add(Projections.property("auditSeqNo")));
					List<Integer> result = auditIdDetails.list();
					
					
					
					AuditListCertInactive arr =new AuditListCertInactive();
					
//					Map<String,Object> arr = new HashMap<>();
//					arr.put("auditIds", result);
//					if(rmiCert != null) {
//					arr.put("certificateId", rmiCert.getCertificateID());
//					}else {
//						arr.put("certificateId", rmiCertEndor.getCertificateID());
//					}
//					arr.put("certIssueId", certificateDetail.getCertIssueId());
//					arr.put("certIssueDesc",MasterDatas.getCertificateIssueType(certificateDetail.getCertIssueId() ,certificateDetail.getAuditTypeId(),certificateDetail.getRoleId()));
					
					
					arr.setAuditIds(result);
					arr.setCertIssueId(certificateDetail.getCertIssueId());
					arr.setCertIssueDesc(MasterDatas.getCertificateIssueType(certificateDetail.getCertIssueId() ,certificateDetail.getAuditTypeId(),certificateDetail.getRoleId()));
					if(rmiCert != null) {
					arr.setCertificateId(rmiCert.getCertificateID().intValue());
					}else {
						arr.setCertificateId(rmiCertEndor.getCertificateID().intValue());
					}
			/*for(int i=0;i < result.size();i++) {
				/*RmiCertificates rmiAuditList = new RmiCertificates();
				rmiAuditList.setAuditID(result.get(i).getSeqNo());
				rmiCertAuditList.add(rmiAuditList);
				rmiCertAuditList.add(i, resul);
			}*/
//					for(Integer i:result) {
//						RmiCertificates rmiAuditList = new RmiCertificates();
//						rmiAuditList.setAuditID(i);
//						rmiCertAuditList.add(rmiAuditList);
//					}
//			
//condition added by cb for IRI-5432 if we create an SMC interim or fullterm after that create another SMC interim or fullterm the previous audit
//certificate is not turning to inactive in RMI
					if(certificateDetail.getCertIssueId()==AppConstant.FULL_TERM_CERT||certificateDetail.getCertIssueId()==AppConstant.INTERIM_CERT) {
						Query getAuditSeqNo=hibsession.getSession().createQuery(AppSQLConstant.GET_AUDIT_SEQ_NO_CERTIFICATE_DETAIL);
						getAuditSeqNo.setParameter("auditTypeId",certificateDetail.getAuditTypeId());
						getAuditSeqNo.setParameter("vesselImoNo",certificateDetail.getVesselImoNo());
						
					List<Integer>auditSeqNo=getAuditSeqNo.list();
					
					Query makeInactiveToRest =	hibsession.getSession().createQuery(AppSQLConstant.UPDATE_SMC_INACTIVE_CERTIFICATE_RMI_NXT_AUDIT);
					makeInactiveToRest.setParameter("certificateStatus1", "IN-ACTIVE");
					makeInactiveToRest.setParameterList("auditID", auditSeqNo);
					makeInactiveToRest.setParameter("certificateStatus2", "ACTIVE");
					makeInactiveToRest.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo());
					makeInactiveToRest.executeUpdate();
					}
					
				
			if(certificateDetail.getCertIssueId() == AppConstant.RE_ISSUE || certificateDetail.getCertIssueId() == AppConstant.EXTENSION) {
				
				Query makeInactiveToRest = null;
				makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_RMI_CERTIFICATE);
				makeInactiveToRest.setString("activeStatus","IN-ACTIVE");
				
				makeInactiveToRest.setInteger("audit_id", rmiCert.getAuditID());
				makeInactiveToRest.setString("vesselImo", rmiCert.getVesselIMONumber());
				makeInactiveToRest.setLong("certificateId", rmiCert.getCertificateID());
				makeInactiveToRest.executeUpdate();
				
				//condition added by cb for IRI-5431 create an SMC initial generate and publish the certificate now create intermediate 
				//generate and publish the certificate now reissue ammendment from manager in the RMI table the initial certificate is not turning back to inactive
			if(certificateDetail.getAuditSubTypeId()==AppConstant.INTERMEDIATE_SUB_TYPE_ID||certificateDetail.getAuditSubTypeId()==AppConstant.ADDITIONAL_SUB_TYPE_ID) {
				Query getAuditSeqNo=hibsession.getSession().createQuery(AppSQLConstant.GET_AUDIT_SEQ_NO_CERTIFICATE_DETAIL);
				getAuditSeqNo.setParameter("auditTypeId",certificateDetail.getAuditTypeId());
				getAuditSeqNo.setParameter("vesselImoNo",certificateDetail.getVesselImoNo());
				
			List<Integer>auditSeqNo=getAuditSeqNo.list();
			Query makeInactiveToRest2 =	hibsession.getSession().createQuery(AppSQLConstant.UPDATE_SMC_INACTIVE_CERTIFICATE_RMI_NXT_AUDIT);
			makeInactiveToRest2.setParameter("certificateStatus1", "IN-ACTIVE");
			makeInactiveToRest2.setParameterList("auditID", auditSeqNo);
			makeInactiveToRest2.setParameter("certificateStatus2", "ACTIVE");
			makeInactiveToRest2.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo());
			makeInactiveToRest2.executeUpdate();
			}
			
			}
			
			
			if(rmiCert != null){
				//added by sudharsan for INCEDENT_TICKET-671 Start here
				RmiAuditCertificate  model_rmiCert = setRmiauidtCertData(rmiCert);
				System.err.println("before sending the date to RMI 614"+model_rmiCert);
				HttpEntity<RmiAuditCertificate> model_rmiCertificatesReq = new HttpEntity<RmiAuditCertificate>(model_rmiCert, restUtil.getHeaders());
				//rmiCertificatesReq = new HttpEntity<RmiCertificates>(rmiCert, restUtil.getHeaders());
				response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditCertInfo", model_rmiCertificatesReq , String.class );
				//INCEDENT_TICKET-671 End here
			} if(rmiCertEndor != null){
				//added by sudharsan for INCEDENT_TICKET-671 Start here
				RmiAuditCertEndorsementModel rmiAuditCertEndorsementModel = setRmiAuditCertEndorsementsModelData(rmiCertEndor);
				HttpEntity<RmiAuditCertEndorsementModel> rmiAuditCertEndorsementModelreq = null;
				rmiAuditCertEndorsementModelreq = new HttpEntity<RmiAuditCertEndorsementModel>(rmiAuditCertEndorsementModel, restUtil.getHeaders());
				//rmiAuditCertEndorsementsReq = new HttpEntity<RmiAuditCertEndorsements>(rmiCertEndor, restUtil.getHeaders());
				response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditEndorseInfo", rmiAuditCertEndorsementModelreq , String.class );
				//INCEDENT_TICKET-671 End here
			}
				if(rmiCert != null){
								
					rmiCertificatesReqmap = new HttpEntity<AuditListCertInactive>(arr, restUtil.getHeaders());
				System.out.println(AppConstant.RMI_URL+"/ws1/MakeCertIncative/"+rmiCert.getAuditID()+"/"+rmiCert.getCertificateID()+"/SMC/"+certificateDetail.getCertIssueId()+"/"+rmiCert.getCertificateIssueType());
				//response = restTemplate.getForEntity( AppConstant.RMI_URL+"/ws1/MakeCertIncative/"+rmiCert.getAuditID()+"/"+rmiCert.getCertificateID()+"/SMC/"+certificateDetail.getCertIssueId() , String.class );
				response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/MakeMultipleCertIncative" ,rmiCertificatesReqmap, String.class );		
				} 
		}
		
		
		/*****Update Audit Data as of updates on certificate*****/
		Query updateAudit = null;
		if(certificateDetail.getAuditTypeId()!=null && certificateDetail.getAuditTypeId()==AppConstant.IHM_TYPE_ID){
			updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_DTL_FROM_CERTIFICATE_DTL_IHM);
		}else{
			updateAudit = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_AUDIT_DTL_FROM_CERTIFICATE_DTL);
		}
		updateAudit.setString("certificateNo", certificateDetail.getCertificateNo());
		//updateAudit.setDate("certIssueDate", certificateDetail.getCertIssueDate());
		//updateAudit.setDate("dateOfRegistry", certificateDetail.getDateOfRegistry());
		if(certificateDetail.getAuditTypeId()!=null && certificateDetail.getAuditTypeId()!=AppConstant.IHM_TYPE_ID){
		updateAudit.setTimestamp("certIssueDate", certificateDetail.getCertIssueDate());
		updateAudit.setTimestamp("certExpireDate", certificateDetail.getCertExpireDate());
		}
		//updateAudit.setDate("auditDate", certificateDetail.getAuditDate());
		updateAudit.setInteger("certIssueId", certificateDetail.getCertIssueId());
		//updateAudit.setInteger("grt", certificateDetail.getGrt());
		updateAudit.setLong("companyId", certificateDetail.getCompanyId());
		updateAudit.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
		updateAudit.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
		updateAudit.executeUpdate();
		return resultMap;
	}
	
	
	@Override
	public void InsertionOfCertDtls(Integer vesselImoNo, Integer auditSeqNo, String certificateNo, Integer auditTypeId,
			Integer auditSubTypeId, Long companyId, String status) {

		Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL REISSUE_CERTIFICATE_INTERORADD(:vesselImoNo, :auditSeqNo, :certificateNo, :auditTypeId, :auditSubTypeId, :companyId, :status)")
				.setInteger("vesselImoNo", vesselImoNo)
				.setInteger("auditSeqNo", auditSeqNo)
				.setString("certificateNo", certificateNo.split("-")[0])
				.setInteger("auditTypeId", auditTypeId)
				.setInteger("auditSubTypeId", auditSubTypeId)
				.setLong("companyId", companyId)
				.setString("status", status);
		
		callStoredProcedure_MYSQL.executeUpdate();

	}
	
	
	@Override
	public Map<String, Object> generateCertificateWithOutAudit(CertificateDetailWithOutAudit certificateDetail) {
		
		RmiCertificates rmiCert = null;
		RmiAuditCertEndorsements rmiCertEndor = null;
		RestTemplate restTemplate = new RestTemplate();
		//HttpHeaders headers = new HttpHeaders();
		HttpEntity<RmiCertificates> rmiCertificatesReq = null;
		HttpEntity<RmiAuditCertEndorsements> rmiAuditCertEndorsementsReq = null;
		ResponseEntity<String> response = null;
		Long l=sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ORDER_NO);
		certificateDetail.setCertOderNo(l.intValue());
		certificateDetail.setDocTypeNo(certificateDetail.getDocTypeNo().replaceAll(certificateDetail.getDocTypeNumber(), "").trim());

		

		Map<String,Object> resultMap= new HashMap<String,Object>();

		if(certificateDetail.getCertificateId()==null){

			certificateDetail.setCertificateId(sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_ID_SEQ));
		}

		if(certificateDetail.getCertificateNo()==null || certificateDetail.getCertificateNo().equals("")){
			
			certificateDetail.setCertificateNo(AppUtil.getCertificateNoFormat(certificateDetail.getAuditTypeId(),sequenceGenerator.generateaSequence(AppConstant.CERTIFICATE_SEQUENCE).toString(),certificateDetail.getAuditSubTypeId()));
		}
		
		String Cno=certificateDetail.getCertificateNo();
        String Utn=certificateDetail.getUtn();
        String QidEncode="";
        try 
		{
			String addCnoUtn=Cno+" "+Utn;
			Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");			String s= (String) q.uniqueResult();
			QidEncode =s;
			//QidEncode = e.EncodeCertiNoUtn(Cno, Utn);
		}  
		catch (Throwable e) 
		{
			e.printStackTrace();
		}
        certificateDetail.setQid(QidEncode);
                
      //String newQrCodeUrl=certificateDetail.getQrCodeUrl().substring(0, certificateDetail.getQrCodeUrl().indexOf("viewer")+14);
       
        certificateDetail.setQrCodeUrl(AppConstant.cert_url+QidEncode);
        


		if(certificateDetail.getSeqNo()==0){

			Query getMaxCertSeq = hibsession.getSession().createQuery(AppSQLConstant.GET_MAX_CERT_SEQ);
			getMaxCertSeq.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			getMaxCertSeq.setLong("companyId", certificateDetail.getCompanyId());
			getMaxCertSeq.setInteger("auditTypeId", certificateDetail.getAuditTypeId());

			int seqNo = 0;

			List list = getMaxCertSeq.list();

			seqNo = (list.isEmpty() || list.get(0)==null) ? 1 : (Integer)list.get(0)+1;

			certificateDetail.setSeqNo(seqNo);
		}

		CertificateDetail certDetail = new CertificateDetail();

		BeanUtils.copyProperties(certificateDetail,certDetail);


		rmiCert = setRmiCertData(certDetail);

		hibsession.getSession().saveOrUpdate(certificateDetail);

		resultMap.put("updatedData",certificateDetail);

		if(certificateDetail.getCertIssueId() == AppConstant.INTERIM_CERT || certificateDetail.getCertIssueId() == AppConstant.FULL_TERM_CERT){

			Query makeInactiveToRest = hibsession.getSession().createQuery(AppSQLConstant.MAKE_INACTIVE_CERTIFICATE_WITHOUT_AUDIT_DTL);

			makeInactiveToRest.setInteger("activeStatus", AppConstant.ACTIVE_STATUS);
			makeInactiveToRest.setLong("companyId", certificateDetail.getCompanyId());
			makeInactiveToRest.setInteger("auditTypeId", certificateDetail.getAuditTypeId());
			makeInactiveToRest.setInteger("auditSeqNo", certificateDetail.getAuditSeqNo());
			makeInactiveToRest.setInteger("vesselImoNo", certificateDetail.getVesselImoNo());
			makeInactiveToRest.setInteger("seqNo", certificateDetail.getSeqNo());
			makeInactiveToRest.executeUpdate();

		}

		if(rmiCert != null){
			hibsession.getSession().saveOrUpdate(rmiCert);
		}else if(rmiCertEndor != null){
			hibsession.getSession().saveOrUpdate(rmiCertEndor);
		}

		if(certificateDetail.getPublishStatus()==1){

			if(rmiCert != null){
				//added by sudharsan for INCEDENT_TICKET-671 Start here
				RmiAuditCertificate  model_rmiCert = setRmiauidtCertData(rmiCert);
				System.err.println("before sending the date to RMI 780"+model_rmiCert);
				HttpEntity<RmiAuditCertificate> model_rmiCertificatesReq  = new HttpEntity<RmiAuditCertificate>(model_rmiCert, restUtil.getHeaders());
//				rmiCertificatesReq = new HttpEntity<RmiCertificates>(rmiCert, restUtil.getHeaders());
				response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditCertInfo", model_rmiCertificatesReq , String.class );
				//INCEDENT_TICKET-671 end here
			}else if(rmiCertEndor != null){
				//added by sudharsan for INCEDENT_TICKET-671 Start here
				RmiAuditCertEndorsementModel rmiAuditCertEndorsementModel = setRmiAuditCertEndorsementsModelData(rmiCertEndor);
				HttpEntity<RmiAuditCertEndorsementModel> rmiAuditCertEndorsementModelreq = null;
				rmiAuditCertEndorsementModelreq = new HttpEntity<RmiAuditCertEndorsementModel>(rmiAuditCertEndorsementModel, restUtil.getHeaders());
				//rmiAuditCertEndorsementsReq = new HttpEntity<RmiAuditCertEndorsements>(rmiCertEndor, restUtil.getHeaders());
				response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditEndorseInfo", rmiAuditCertEndorsementModelreq , String.class );
				//INCEDENT_TICKET-671 End here
			}
		}


		return resultMap;
	}
	
	@Override
	public Object checkCertificateGenerate(CertificateDetailWithOutAudit certificateDetail) {
		
		if(certificateDetail.getCertificateId() != null && certificateDetail.getCertificateNo() != null && !certificateDetail.getCertificateNo().trim().isEmpty()){
			
			return 1;
		}else{
			
			if(certificateDetail.getAuditTypeId()!=AppConstant.IHM_TYPE_ID){
			String sql = "SELECT PUBLISH_STATUS FROM CERTIFICATE_DETAIL WHERE AUDIT_SEQ_NO = :auditSeqNo AND AUDIT_TYPE_ID=:auditTypeId "
					+ "AND AUDIT_SUB_TYPE_ID = :auditSubType AND VESSEL_IMO_NO= :vesselImo AND COMPANY_ID = :companyId AND PUBLISH_STATUS = :publishStatus  AND ROWNUM=:rowNum ORDER BY SEQ_NO DESC";
			
			Object countVal =  hibsession.getSession().createSQLQuery(sql)
					.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo())
					.setParameter("auditTypeId", certificateDetail.getAuditTypeId())
				
					.setParameter("auditSubType", certificateDetail.getAuditSubTypeId())
					.setParameter("vesselImo", certificateDetail.getVesselImoNo())
					.setParameter("companyId", certificateDetail.getCompanyId())
					.setParameter("publishStatus", 0)
					.setParameter("rowNum", 1)
					.uniqueResult();
			
			return countVal;
			}else { String sql = "SELECT PUBLISH_STATUS FROM CERTIFICATE_DETAIL WHERE AUDIT_SEQ_NO = :auditSeqNo AND AUDIT_TYPE_ID=:auditTypeId "
					+ "AND VESSEL_IMO_NO= :vesselImo AND COMPANY_ID = :companyId AND PUBLISH_STATUS = :publishStatus  AND ROWNUM=:rowNum ORDER BY SEQ_NO DESC";
			
			Object countVal =  hibsession.getSession().createSQLQuery(sql)
					.setParameter("auditSeqNo", certificateDetail.getAuditSeqNo())
					.setParameter("auditTypeId", certificateDetail.getAuditTypeId())
				    .setParameter("vesselImo", certificateDetail.getVesselImoNo())
					.setParameter("companyId", certificateDetail.getCompanyId())
					.setParameter("publishStatus", 0)
					.setParameter("rowNum", 1)
					.uniqueResult();
			
			return countVal;
			}
			
		}
		
	}
	
	
	@Override
	public List<CertificateDetail> getCertificateData(Long companyId, String utn) {
		Criteria cr = hibsession.getSession().createCriteria(CertificateDetail.class)
				.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("utn", utn))
				.addOrder(Order.asc("auditDate"))
				.add(Restrictions.eq("companyId", companyId));
		List<CertificateDetail> certList = cr.list();
		return certList;
	}
	@Override
	public List<CertificateDetail> getCertificateData(Long companyId, String utn,String certNo) {
		
		Criteria cr = hibsession.getSession().createCriteria(CertificateDetail.class)
				.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("utn", utn))
				.addOrder(Order.asc("auditDate"))
				.add(Restrictions.eq("companyId", companyId));
		List<CertificateDetail> certList = cr.list();
		
		if(certList.isEmpty())
		{
			CertificateDetail c=new CertificateDetail();
			Criteria cr2 = hibsession.getSession().createCriteria(AuditDetail.class)
				.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS))
				.add(Restrictions.eq("certificateNo", certNo))
				.addOrder(Order.desc("auditSeqNo"))
				.add(Restrictions.eq("companyId", companyId));
			List<AuditDetail> certList2 = cr2.list();
			if(!certList2.isEmpty())
			{
				String isSync=Integer.toString(certList2.get(0).getLockStatus());
				c.setIsSync(isSync);
				certList.add(c);
			}
			else
			{
				c.setIsSync("0");
				certList.add(c);
			}
		
		}
		return certList;
	}

	@Override
	public Map<String, Object> checkRelatedToCurrAdt(Integer auditSeqNo, Long companyId, String userId) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		Criteria audAdtrDtl = hibsession.getSession().createCriteria(AuditAuditorDetail.class)
				.add(Restrictions.eq("auditSeqNo", auditSeqNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("userId", userId));
		
		if(audAdtrDtl.list().size()>0){
			resultMap.put("status",1);
			resultMap.put("auditorDetail",audAdtrDtl.uniqueResult());
		}else{
			resultMap.put("status",0);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> syncTimeUpdateRmiData(CertificateDetail certificateDetail) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		RmiCertificates rmiCert = null;
		RmiAuditCertEndorsements rmiCertEndor = null;
		RestTemplate restTemplate = new RestTemplate();
		//HttpHeaders headers = new HttpHeaders();
		HttpEntity<RmiCertificates> rmiCertificatesReq = null;
		HttpEntity<RmiAuditCertEndorsements> rmiAuditCertEndorsementsReq = null;
		ResponseEntity<String> response = null;
		
		String Cno=certificateDetail.getCertificateNo();
        String Utn=certificateDetail.getUtn();
        String QidEncode="";
        try {
			String addCnoUtn=Cno+" "+Utn;
			Query q = hibsession.getSession().createSQLQuery("select F_ENCRYPT('"+addCnoUtn+"') from dual");			
			String s= (String) q.uniqueResult();
			QidEncode =s;
		} 
		catch (Throwable e)
		{
			e.printStackTrace();
		}
      
        certificateDetail.setQid(QidEncode);
        certificateDetail.setQrCodeUrl(AppConstant.cert_url+QidEncode);
        
        Criteria audAdtrDtl = hibsession.getSession().createCriteria(CertificateDetail.class)
    			
				.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
				.add(Restrictions.eq("auditTypeId", certificateDetail.getAuditTypeId() ))
				.add(Restrictions.eq("vesselImoNo", certificateDetail.getVesselImoNo() ))
				.add(Restrictions.ne("certIssueId", certificateDetail.getCertIssueId() ));
		
		if(certificateDetail.getAuditTypeId() == AppConstant.ISM_TYPE_ID) {
			audAdtrDtl.add(Restrictions.ne("auditSeqNo", AppConstant.ISM_NO_AUD_CERT_AUDITSEQ));
		}
		else if(certificateDetail.getAuditTypeId() == AppConstant.ISPS_TYPE_Id) {
			audAdtrDtl.add(Restrictions.ne("auditSeqNo", AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ));
		}
		else if(certificateDetail.getAuditTypeId() == AppConstant.MLC_TYPE_ID) {
			audAdtrDtl.add(Restrictions.ne("auditSeqNo", AppConstant.MLC_NO_AUD_CERT_AUDITSEQ));	
				}
		
		audAdtrDtl.setProjection(Projections.rowCount());
		Long count = (Long) audAdtrDtl.uniqueResult();
		
		if(count == 0 && (certificateDetail.getAuditSubTypeId() == AppConstant.INTERMEDIATE_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID) && certificateDetail.getCertIssueId() != AppConstant.RE_ISSUE) {

			Criteria rmiCertCri = hibsession.getSession().createCriteria(RmiCertificates.class)
					.add(Restrictions.eq("auditID", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("certificateID", certificateDetail.getCertificateId()))
					.add(Restrictions.eq("certificateNumber", certificateDetail.getCertificateNo()));
			
			if(rmiCertCri.list().size() == 0){
				rmiCert = setRmiCertData(certificateDetail);
				hibsession.getSession().saveOrUpdate(rmiCert);
			}
	    }
        
		if(!(certificateDetail.getAuditSubTypeId() == AppConstant.INTERMEDIATE_SUB_TYPE_ID || certificateDetail.getAuditSubTypeId() == AppConstant.ADDITIONAL_SUB_TYPE_ID || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED1 || certificateDetail.getCertIssueId() == AppConstant.RENEWAL_ENDORSED2)){
			
			Criteria rmiCertCri = hibsession.getSession().createCriteria(RmiCertificates.class)
					.add(Restrictions.eq("auditID", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("certificateID", certificateDetail.getCertificateId()))
					.add(Restrictions.eq("certificateNumber", certificateDetail.getCertificateNo()));
			
			if(rmiCertCri.list().size() == 0){
				rmiCert = setRmiCertData(certificateDetail);
				hibsession.getSession().saveOrUpdate(rmiCert);
			}
			
		}else{
			
			Criteria rmiCertEndorCri = hibsession.getSession().createCriteria(RmiAuditCertEndorsements.class)
					.add(Restrictions.eq("auditID", certificateDetail.getAuditSeqNo()))
					.add(Restrictions.eq("companyId", certificateDetail.getCompanyId()))
					.add(Restrictions.eq("endorsementID", certificateDetail.getEndorsementID()));
			
			if(rmiCertEndorCri.list().size() == 0){
				rmiCertEndor = setRmiAuditCertEndorsementsData(certificateDetail);
				hibsession.getSession().saveOrUpdate(rmiCertEndor);
			}
			
		}

		if(rmiCert != null){
			//added by sudharsan for INCEDENT_TICKET-671 Start here
			RmiAuditCertificate  model_rmiCert = setRmiauidtCertData(rmiCert);
			System.err.println("before sending the date to RMI 999"+model_rmiCert);
			HttpEntity<RmiAuditCertificate> model_rmiCertificatesReq = new HttpEntity<RmiAuditCertificate>(model_rmiCert, restUtil.getHeaders());
			//rmiCertificatesReq = new HttpEntity<RmiCertificates>(rmiCert, restUtil.getHeaders());
			response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditCertInfo", model_rmiCertificatesReq , String.class );
			//INCEDENT_TICKET-671 End here
		}
		
		if(rmiCertEndor != null){
			//added by sudharsan for INCEDENT_TICKET-671 Start here
			RmiAuditCertEndorsementModel rmiAuditCertEndorsementModel = setRmiAuditCertEndorsementsModelData(rmiCertEndor);
			HttpEntity<RmiAuditCertEndorsementModel> rmiAuditCertEndorsementModelreq = null;
			rmiAuditCertEndorsementModelreq = new HttpEntity<RmiAuditCertEndorsementModel>(rmiAuditCertEndorsementModel, restUtil.getHeaders());
			//rmiAuditCertEndorsementsReq = new HttpEntity<RmiAuditCertEndorsements>(rmiCertEndor, restUtil.getHeaders());
			response = restTemplate.postForEntity( AppConstant.RMI_URL+"/ws1/insertAuditEndorseInfo", rmiAuditCertEndorsementModelreq , String.class );
			//INCEDENT_TICKET-671 End here
		}
		
		resultMap.put("status", 200);
		resultMap.put("result", "success");
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getCertificateDetailnew(Integer auditSeqNo, Long companyId, Integer seqNo,
			Integer activeStatus) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		List<CertificateDetail> certDtls = null;
		
		int numOfRecors = 0;
		String utn = null; Long certificateId = null;
		
		Criteria certDtl = hibsession.getSession().createCriteria(CertificateDetail.class)
										.add(Restrictions.eq("auditSeqNo", auditSeqNo))
										.add(Restrictions.eq("companyId", companyId))
										.add(Restrictions.eq("seqNo", seqNo));
		
		resultMap.put("result", certDtl.uniqueResult());
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getAllIhmCertificateDetail(Integer auditTypeId, Integer vesselImo, Long companyId) {
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		List<CertificateDetail> certDtls = null;
		
		int numOfRecors = 0;
		String utn = null; Long certificateId = null;
		
		Criteria certDtl = hibsession.getSession().createCriteria(CertificateDetail.class)
										.add(Restrictions.eq("auditTypeId", auditTypeId))
										.add(Restrictions.eq("vesselImoNo", vesselImo))
										.add(Restrictions.eq("companyId", companyId))
										.addOrder(Order.desc("seqNo"));
		
		resultMap.put("result", certDtl.list());
		
		return resultMap;
	}
	@Override
	public Map<String, Object> getCertSearchResult2(SearchCriteria searchCriteria) {
		
		
		Criteria certSrchCr = hibsession.getSession().createCriteria(CertificateDetail.class);
		
		
		if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.ihmUserOnly){
			certSrchCr.add(Restrictions.eq("auditTypeId",AppConstant.IHM_TYPE_ID));
		  }else if(searchCriteria.getIhmAuthorise()!=null && searchCriteria.getIhmAuthorise()==AppConstant.notIhmUser){
			  certSrchCr.add(Restrictions.ne("auditTypeId",AppConstant.IHM_TYPE_ID));
		  }
		
		if(searchCriteria.getVesselImoNo()!=null){
			certSrchCr.add(Restrictions.eq("vesselImoNo", searchCriteria.getVesselImoNo()));
		}
		if(searchCriteria.getCompanyId()!=null){
			certSrchCr.add(Restrictions.eq("companyId", searchCriteria.getCompanyId()));
		}
		if(searchCriteria.getCompanyImoNo()!=null){
			certSrchCr.add(Restrictions.eq("companyImoNo", searchCriteria.getCompanyImoNo()));
		}
		if(searchCriteria.getCertIssueDate()!=null){
			certSrchCr.add(Restrictions.ge("certIssueDate", searchCriteria.getCertIssueDate()));
		}
		if(searchCriteria.getCertExpireDate()!=null){
			certSrchCr.add(Restrictions.lt("certExpireDate", searchCriteria.getCertExpireDate()));
		}
		if(searchCriteria.getCertificateNo()!=null){
			certSrchCr.add(Restrictions.eq("certificateNo", searchCriteria.getCertificateNo()));
		}
		if(searchCriteria.getAuditTypeId()!=null){
			certSrchCr.add(Restrictions.eq("auditTypeId", searchCriteria.getAuditTypeId()));
		}
		if(searchCriteria.getAuditSubTypeId()!=null){
			certSrchCr.add(Restrictions.eq("auditSubTypeId", searchCriteria.getAuditSubTypeId()));
		}
			
		if(searchCriteria.getActiveStatus()!=null){
			certSrchCr.add(Restrictions.eq("activeStatus", searchCriteria.getActiveStatus()));
		}
		
		if(searchCriteria.getOfficialNo()!=null){
		
			certSrchCr.add(Restrictions.eq("officialNo", searchCriteria.getOfficialNo()));
		}
		
		if(searchCriteria.getCertIssueId()!=null){
			
			certSrchCr.add(Restrictions.eq("certIssueId", searchCriteria.getCertIssueId()));
		}
		
		if(searchCriteria.getAuditSequenceNo()!=null){
			certSrchCr.add(Restrictions.eq("auditSeqNo", searchCriteria.getAuditSequenceNo()));
			certSrchCr.add(Restrictions.eq("generateStatus", 1));
		}
		
		if(searchCriteria.getCheckCertVthAudit() != null){
			
	        if(searchCriteria.getCheckCertVthAudit() == 1000){
	        	certSrchCr.add(Restrictions.in("auditSeqNo", new Object[]{AppConstant.ISM_NO_AUD_CERT_AUDITSEQ,AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ,AppConstant.MLC_NO_AUD_CERT_AUDITSEQ,AppConstant.IHM_NO_AUD_CERT_AUDITSEQ}));
	        }else{
	        	certSrchCr.add(Restrictions.not(Restrictions.in("auditSeqNo", new Object[]{AppConstant.ISM_NO_AUD_CERT_AUDITSEQ,AppConstant.ISPS_NO_AUD_CERT_AUDITSEQ,AppConstant.MLC_NO_AUD_CERT_AUDITSEQ,AppConstant.IHM_NO_AUD_CERT_AUDITSEQ})));	        	
	        }
		}
		
		if(searchCriteria.getRoleId()!=null && searchCriteria.getRoleId()==AppConstant.IHM_MANAGER){
			certSrchCr.add(Restrictions.ne("auditTypeId", AppConstant.ISM_TYPE_ID));	
			certSrchCr.add(Restrictions.ne("auditTypeId", AppConstant.ISPS_TYPE_Id));	
			certSrchCr.add(Restrictions.ne("auditTypeId", AppConstant.MLC_TYPE_ID));	
				
		}
		
		//certSrchCr.add(Restrictions.ne("auditStatusId", AppConstant.VOID_AUDIT_STATUS));
		
		//certSrchCr.addOrder(Order.desc("auditSeqNo"));
		//certSrchCr.addOrder(Order.desc("certificateNo"));
		certSrchCr.addOrder(Order.desc("auditDate"));
		certSrchCr.addOrder(Order.desc("auditSeqNo"));
		certSrchCr.addOrder(Order.desc("seqNo"));
		
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		List<CertificateDetail> resSerachList = new ArrayList<CertificateDetail>();
		
		List<CertificateDetail> searchList =certSrchCr.list();
		log.info("searchList ="+searchList);
		int index = 0;
		int startRecord = searchCriteria.getPageNo();
		int endRecord = startRecord + searchCriteria.getDefaultSearchCount();
		
		if(startRecord<0){
			
			resultMap.put("result", searchList);
			return resultMap;	
		}
		
		for(CertificateDetail cd : searchList){
			
			resSerachList.add(cd);
		
				}
		
		resultMap.put("result", resSerachList);
		resultMap.put("numOfRecords", searchList.size());
		
		return resultMap;
	}

	@Override
	public Map<String, Object> publishCertificate(Integer auditSeqNo, Long companyId, Integer seqNo) {
		
		int result = 0;
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		Query updatePublishStatus = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_PUBLISH_STATUS);
		
		updatePublishStatus.setInteger("publishStatus", 1);
		updatePublishStatus.setLong("companyId", companyId);
		updatePublishStatus.setInteger("auditSeqNo", auditSeqNo);
		updatePublishStatus.setInteger("seqNo", seqNo);
		result =  updatePublishStatus.executeUpdate();
		
		if(result>0){
			resultMap.put("updtRslt", true);
		}
		else{
			resultMap.put("updtRslt", false);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getUtnAndCertificateId(String auditReportNo, String certificateNo, Long companyId) {
		
		List<Object[]> obj = null;
		Map<String,Object> resultMap= new HashMap<String,Object>();
		
		Criteria certDtl = hibsession.getSession().createCriteria(CertificateDetail.class)
				//.add(Restrictions.eq("auditReportNo", auditReportNo))
				.add(Restrictions.eq("companyId", companyId))
				.add(Restrictions.eq("certificateNo", certificateNo))
				.setProjection(Projections.projectionList()
						.add(Projections.property("auditSeqNo"))
						.add(Projections.property("auditTypeId"))
	                   	.add(Projections.property("auditSubTypeId"))
						.add(Projections.property("certificateId"))
						.add(Projections.property("utn"))
	                   	.add(Projections.property("qrCodeUrl")));
	            
		certDtl.add(Restrictions.in("auditSubTypeId", new Object[]{AppConstant.INTERIM_SUB_TYPE_ID,AppConstant.INITIAL_SUB_TYPE_ID,AppConstant.INTERMEDIATE_SUB_TYPE_ID,AppConstant.RENEWAL_SUB_TYPE_ID}));
		certDtl.setMaxResults(1);
		
		obj = certDtl.list();
		
		if(!obj.isEmpty()){
			
			resultMap.put("auditSeqNo"	  , obj.get(0)[0]);
			resultMap.put("auditTypeId"   , obj.get(0)[1]);
			resultMap.put("auditSubTypeId", obj.get(0)[2]);
			resultMap.put("certificateId" , obj.get(0)[3]);
			resultMap.put("utn"			  , obj.get(0)[4]);
			resultMap.put("qrCodeUrl"     , obj.get(0)[5]);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getAuditCertDetails(Integer auditSeqNo, Long companyId) {

		Map<String,Object> resultMap= new HashMap<String,Object>();
		AuditDetail ad = null;
		MaVessel mv = null;
		
		Criteria adtDtl = hibsession.getSession().createCriteria(AuditDetail.class);
		Criteria vslDtl = hibsession.getSession().createCriteria(MaVessel.class);
		
		adtDtl.add(Restrictions.eq("auditSeqNo", auditSeqNo))
			  .add(Restrictions.eq("companyId", companyId));
		
		ad = (AuditDetail) adtDtl.uniqueResult();
		
		if(ad != null){
			
			vslDtl.add(Restrictions.eq("vesselImoNo", ad.getVesselImoNo()))
			  	  .add(Restrictions.eq("companyId", companyId));
			
			mv = (MaVessel) vslDtl.uniqueResult();
			
			if(mv != null){
				
				Criteria vslCoDtl = hibsession.getSession().createCriteria(MaVesselCompany.class);
					
					vslCoDtl.add(Restrictions.eq("companyImoNo", ad.getCompanyImoNo()))
				  	  .add(Restrictions.eq("companyId", companyId));
					
				mv.setVesselCompany((MaVesselCompany) vslCoDtl.uniqueResult());
			}
		}
		
		resultMap.put("auditDetail", ad);
		resultMap.put("vesselDetail", mv);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getConsective(Integer vesselImo, Integer auditSeqNo, Long companyId) {
		
		Map<String,Object> map= new HashMap<String,Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		String sql = " WITH GETAUDITSEQ AS (SELECT AUDIT_SEQ_NO FROM AUDIT_DETAILS WHERE AUDIT_STATUS_ID <> :auditStatusId AND VESSEL_IMO_NO = :vesselImo "
						+ "AND COMPANY_ID = :companyId AND AUDIT_TYPE_ID = :auditTypeId AND AUDIT_SEQ_NO < :auditSeqNo AND AUDIT_SUB_TYPE_ID = :auditSubtype AND ROWNUM = :rowNum ORDER BY AUDIT_SEQ_NO DESC) "
						+ "SELECT (CASE WHEN A.EXTENDED_ISSUE_DATE IS NOT NULL THEN TO_DATE (A.EXTENDED_ISSUE_DATE, 'dd-MM-RRRR') ELSE TO_DATE (A.CERT_ISSUED_DATE, 'dd-MM-RRRR') END) AS VAL "
						+ "FROM CERTIFICATE_DETAIL A , GETAUDITSEQ B WHERE A.AUDIT_SEQ_NO = B.AUDIT_SEQ_NO AND ROWNUM = :rowNum AND A.COMPANY_ID = :companyId AND A.VESSEL_IMO_NO = :vesselImo AND "
						+ "A.AUDIT_TYPE_ID = :auditTypeId AND A.AUDIT_SUB_TYPE_ID = :auditSubtype ORDER BY A.SEQ_NO DESC";
		
			
		List<Map<String ,Object>> notifyDtls = hibsession.getSession().createSQLQuery(sql)
				.setParameter("auditStatusId", AppConstant.VOID_AUDIT_STATUS)
				.setParameter("vesselImo", vesselImo)
				.setParameter("companyId", companyId)
				.setParameter("auditTypeId", AppConstant.ISPS_TYPE_Id)
				.setParameter("auditSeqNo", auditSeqNo)
				.setParameter("auditSubtype", AppConstant.INTERIM_SUB_TYPE_ID)
				.setParameter("rowNum", 1)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		if(notifyDtls.size()>0){
			Timestamp ts = (Timestamp)notifyDtls.get(0).get("VAL");
			if(ts!=null){
				Date date = Date.valueOf(ts.toLocalDateTime().toLocalDate());
				dateFormat.format(date);
				map.put("previousIssueDate", dateFormat.format(date));		
			}
		}	
		return map;
	}

	@Override
	public Boolean getCurrentCertCount(Integer auditSeqNo, Long companyId, Integer auditTypeId, Integer subTypeId,
			Integer vesselIMONo, Integer CertType) {
		String sql = "SELECT COUNT(AUDIT_SEQ_NO) FROM CERTIFICATE_DETAIL WHERE AUDIT_SEQ_NO = :auditSeqNo  AND VESSEL_IMO_NO = :vesselIMONo AND AUDIT_TYPE_ID= :auditTypeId  AND AUDIT_SUB_TYPE_ID = :subTypeId AND PUBLISH_STATUS = :publishStatus AND COMPANY_ID=:companyId AND CERTIFICATE_ISSUE_ID= :CertType";

		int count = Integer.parseInt(hibsession.getSession().createSQLQuery(sql)
				.setParameter("auditSeqNo", auditSeqNo).setParameter("vesselIMONo", vesselIMONo).setParameter("auditTypeId", auditTypeId)
				.setParameter("subTypeId", subTypeId).setParameter("publishStatus", 1).setParameter("companyId", companyId).setParameter("CertType", CertType).uniqueResult().toString());

		return count==1 ? true : false;
	}

	@Override
	public String decryptCrypto(String decryptStr) {
		Query q = hibsession.getSession().createSQLQuery("select F_DECRYPT('"+decryptStr+"') from dual");
		String s= (String) q.uniqueResult();
		return s;
	}
	
	@Override
	public Map<String, Object> UpdateDirectIntermediateIssueExpiryDate(Integer auditTypeId, Integer auditSeqNo, Long companyId,java.util.Date certExpireDate, java.util.Date certIssueDate,Integer certIssueId ,Integer vesselIMONo) {
		
		Query updateCerTificate = hibsession.getSession().createQuery(AppSQLConstant.UPDATE_ISSUE_EXPIRY_DRCTINTERADD);
		updateCerTificate.setParameter("auditTypeId", auditTypeId);
		updateCerTificate.setParameter("auditSeqNo", auditSeqNo);
		updateCerTificate.setParameter("companyId", companyId);
		updateCerTificate.setParameter("certExpireDate", certExpireDate);
		updateCerTificate.setParameter("certIssueDate", certIssueDate);
		updateCerTificate.setParameter("certIssueId", certIssueId);
		
		log.info("hhhh"+updateCerTificate);
		int result = updateCerTificate.executeUpdate();

		Map<String,Object> map = new HashMap<String,Object>();

		if(result == 1){
			map.put("success", true);
			return map;
		}
		map.put("success", false);
		return map;
	}
	
	//condition written by cb for making the RMI certificate inactive when audit is made void TICKET-552//
	@Override
	public void updateCertificateStatusSMC(Integer auditSeqNo, Long companyid, Integer auditSubtypeid) {
	     RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> response= null;
		 String IN_ACTIVE= "IN-ACTIVE";

	     
	     
		 Query query1 =hibsession.getSession().createQuery(" from CertificateDetail where auditSeqNo= :auditSeqNo and companyId= :companyId and auditSubTypeId= :auditSubTypeId");
		 query1.setParameter("auditSeqNo", auditSeqNo);
		 query1.setParameter("companyId", companyid);
		 query1.setParameter("auditSubTypeId", auditSubtypeid);
		 ArrayList<CertificateDetail> certificatedetails = (ArrayList<CertificateDetail>) query1.list();
		 
		 
		 
		 if(certificatedetails.size()>=0){
			 for(CertificateDetail certificatedetails1: certificatedetails){
				 
				 Query updateLocalRMICert =hibsession.getSession().createQuery("update RmiCertificates set certificateStatus= :certificateStatus where auditID= :auditID");
			     updateLocalRMICert.setParameter("certificateStatus", IN_ACTIVE);
			     updateLocalRMICert.setParameter("auditID", auditSeqNo);
			     updateLocalRMICert.executeUpdate();
			     
						RmiCertificates rmiCert = new RmiCertificates();
					rmiCert.setAuditID(certificatedetails1.getAuditSeqNo());
					rmiCert.setCertificateID(certificatedetails1.getCertificateId());
					rmiCert.setVesselIMONumber(certificatedetails1.getVesselImoNo().toString());
					rmiCert.setCertificateNumber(certificatedetails1.getCertificateNo());
					
					
					HttpEntity<RmiCertificates>rmiCertificatesReq = new HttpEntity<RmiCertificates>(rmiCert, restUtil.getHeaders());
					response = restTemplate.exchange( AppConstant.RMI_URL+"/ws1/MakeVoidCertInactiveSMC/"+rmiCert.getAuditID()+"/"+rmiCert.getCertificateID()+"/"+rmiCert.getVesselIMONumber()+"/"+rmiCert.getCertificateNumber() ,HttpMethod.GET, rmiCertificatesReq, String.class );
									
					
			 }
			 
		 }
		
	}
	//added by sudharsan for INCEDENT_TICKET-671 Start here
	public RmiAuditCertificate setRmiauidtCertData(RmiCertificates rmiCertificates) {
		
		RmiAuditCertificate rmiCert = new RmiAuditCertificate();
		log.info(rmiCertificates.toString());
		rmiCert.setAuditID(rmiCertificates.getAuditID());
		rmiCert.setCertificateID(rmiCertificates.getCertificateID());
		rmiCert.setCertificateNumber(rmiCertificates.getCertificateNumber());
		rmiCert.setUniqueTrackingNumber(rmiCertificates.getUniqueTrackingNumber());
		rmiCert.setCertificateURL(rmiCertificates.getCertificateURL());
		rmiCert.setCertificateIssueType(rmiCertificates.getCertificateIssueType());
		rmiCert.setCertificateReasons(rmiCertificates.getCertificateReasons());
	    rmiCert.setPlaceofissue(rmiCertificates.getPlaceofissue());
	    rmiCert.setIssueDate(rmiCertificates.getIssueDate()!=null?rmiCertificates.getIssueDate().toString():null);
		rmiCert.setExpirationDate(rmiCertificates.getExpirationDate()!=null?rmiCertificates.getExpirationDate().toString():null);
		rmiCert.setIssuedBy(rmiCertificates.getIssuedBy());
		rmiCert.setCertificateStatus(rmiCertificates.getCertificateStatus());
		rmiCert.setVesselName(rmiCertificates.getVesselName());
		rmiCert.setVesselID(rmiCertificates.getVesselID());
		rmiCert.setVesselUK(rmiCertificates.getVesselUK());
		rmiCert.setVesselPK(rmiCertificates.getVesselPK());
		rmiCert.setVesselIMONumber(rmiCertificates.getVesselIMONumber()!=null?rmiCertificates.getVesselIMONumber().toString():null);
		rmiCert.setGrossTonnage(rmiCertificates.getGrossTonnage());
		rmiCert.setClassSociety(rmiCertificates.getClassSociety());
		rmiCert.setOfficialNumber(rmiCertificates.getOfficialNumber()!=null?rmiCertificates.getOfficialNumber().toString():null);
		rmiCert.setCallSign(rmiCertificates.getCallSign());
		rmiCert.setDocTypeNumber(rmiCertificates.getDocTypeNumber());
		rmiCert.setCompanyIMONumber(rmiCertificates.getCompanyIMONumber());
		rmiCert.setDocType(rmiCertificates.getDocType());
		rmiCert.setDocIssuer(rmiCertificates.getDocIssuer());
		rmiCert.setDocExpiry(rmiCertificates.getDocExpiry()!=null?rmiCertificates.getDocExpiry().toString():null);
		rmiCert.setCreationDate(rmiCertificates.getCreationDate()!=null?rmiCertificates.getCreationDate().toString():null);
		rmiCert.setCreatedBy(rmiCertificates.getCreatedBy());
		rmiCert.setLastUpdateDate((new java.sql.Date(Calendar.getInstance().getTime().getTime())).toString());
		rmiCert.setLastUpdatedBy(rmiCertificates.getLastUpdatedBy());
		rmiCert.setExtendDate(rmiCertificates.getExtendDate()!=null?rmiCertificates.getExtendDate().toString():null); 
		rmiCert.setAuditType(rmiCertificates.getAuditType());
		
		System.err.println("certificatedaoIMPL1424---notIHM"+rmiCert);
		
		return rmiCert;
	}

public RmiAuditCertEndorsementModel setRmiAuditCertEndorsementsModelData(RmiAuditCertEndorsements rmiCertEndor){
		
		RmiAuditCertEndorsementModel rmiCertEndor_Model = new RmiAuditCertEndorsementModel();
		  
		rmiCertEndor_Model.setAuditID(rmiCertEndor.getAuditID());
		rmiCertEndor_Model.setCertificateID(rmiCertEndor.getCertificateID());
		rmiCertEndor_Model.setEndorsementID(rmiCertEndor.getEndorsementID() );
		rmiCertEndor_Model.setEndorsementDate(rmiCertEndor.getEndorsementDate()!=null?rmiCertEndor.getEndorsementDate().toString():null);
		rmiCertEndor_Model.setEndorsementReason(rmiCertEndor.getEndorsementReason());
		rmiCertEndor_Model.setCompanyId(rmiCertEndor.getCompanyId());
		rmiCertEndor_Model.setCreatedBy(rmiCertEndor.getCreatedBy());
		rmiCertEndor_Model.setCreationDate(rmiCertEndor.getCreationDate()!=null?rmiCertEndor.getCreationDate().toString():null);
		rmiCertEndor_Model.setEndorsedBy(rmiCertEndor.getEndorsedBy());
		rmiCertEndor_Model.setLastUpdateDate(rmiCertEndor.getLastUpdateDate()!=null?rmiCertEndor.getLastUpdateDate().toString():null);
		rmiCertEndor_Model.setLastUpdatedBy(rmiCertEndor.getLastUpdatedBy());
		rmiCertEndor_Model.setVesselId(rmiCertEndor.getVesselId());
		rmiCertEndor_Model.setVesselName(rmiCertEndor.getVesselName());
		rmiCertEndor_Model.setExtendDate(rmiCertEndor.getExtendDate()!=null?rmiCertEndor.getExtendDate().toString():null);
		rmiCertEndor_Model.setAuditType(rmiCertEndor.getAuditType());
	
		System.err.println("certificatedaoIMPL1424---notIHM"+rmiCertEndor_Model);
		
		return rmiCertEndor_Model;
	}
//added by sudharsan for INCEDENT_TICKET-671 End here
@Override
public void MakePreviousCertificateInactive(Integer auditSeqNo, String certificateNo, Integer auditTypeId,
		Long companyId, Integer vesselImoNo, Integer seqNo, Long certificateId, String status) {
	
	Query callStoredProcedure_MYSQL = hibsession.getSession().createSQLQuery("CALL MAKE_PREVIOUS_CERTIFICATE_INACTIVE (:auditSeqNo, :certificateNo, :auditTypeId, :companyId, :vesselImoNo, :seqNo, :certificateId, :status)")
			.setInteger("auditSeqNo", auditSeqNo)
			.setString("certificateNo", certificateNo)
			.setInteger("auditTypeId", auditTypeId)
			.setLong("companyId", companyId)
			.setInteger("vesselImoNo", vesselImoNo)
			.setInteger("seqNo", seqNo)
			.setLong("certificateId", certificateId)
			.setString("status",status);
	
	callStoredProcedure_MYSQL.executeUpdate();
	
}
}
