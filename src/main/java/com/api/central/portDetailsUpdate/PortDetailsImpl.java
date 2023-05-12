package com.api.central.portDetailsUpdate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Date 27 june 2022
 * CR 512
 * Author - rupesh
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.central.audit.entity.CertificateDetail;
import com.api.central.util.AppConstant;
import com.api.central.util.RestTemplateUtil;

@Service
public class PortDetailsImpl implements PortsDetails {

	static Logger log = LoggerFactory.getLogger(PortDetailsImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RestTemplateUtil restUtil;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Session hibsession;
	Transaction tx;
	List<RmiCertificateDetails> certificateDetails;

	@Override
	public String updatePortDetails() {
		try {
			log.info("Fetching the currect session");
			hibsession = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			log.info("No Session Found creating new one");
			hibsession = sessionFactory.openSession();
		}

		return getCertificateData();
	}

	private String getCertificateData() {
		ListCertDetails certDetail = new ListCertDetails();
		String errorMsg = "Data Not Updated. please check the Log";
		try {
			Criteria cr = hibsession.createCriteria(CertificateDetail.class)
					.setProjection(Projections.projectionList().add(Projections.property("auditSeqNo"), "auditSeqNo")
							.add(Projections.property("certificateId"), "certificateId")
							.add(Projections.property("auditPlace"), "auditPlace")
							.add(Projections.property("certificateNo"), "certificateNo")
							.add(Projections.property("utn"), "utn"))
					.setResultTransformer(Transformers.aliasToBean(CertificateDetail.class));

			List<CertificateDetail> CertificateDetailList = cr.list();
			List<RmiCertificateDetails> certDetails = CertificateDetailList.stream()
					.map(p -> new RmiCertificateDetails(p.getAuditSeqNo(), p.getCertificateId(), p.getCertificateNo(),
							p.getUtn(), decodeString(p.getAuditPlace())))
					.collect(Collectors.toList());

			certDetail.setListDetails(certDetails);
			if (!callRMIApi(certDetail))
				return errorMsg;
			saveDataToRMICertificate(certDetail);
			return "Data  Updated Successfuly";

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return errorMsg;
		}
	}

	private boolean callRMIApi(ListCertDetails certDetail) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			log.info("sending certificate details to RMI DB ");
			HttpEntity<ListCertDetails> certDetailsReq = new HttpEntity<ListCertDetails>(certDetail,
					restUtil.getHeaders());
			ResponseEntity<int[]> response = restTemplate.postForEntity(AppConstant.RMI_URL + "/batchUpdate/ports",
					certDetailsReq, int[].class);

			if (HttpStatus.OK.equals(response.getStatusCode())) {
				return true;
			} else
				return false;

		} catch (Exception e) {
			log.error(" Exception happen " + e.getMessage());
			return false;
		}
	}

	private int[] saveDataToRMICertificate(ListCertDetails certDetai) {
		certificateDetails = certDetai.getListDetails();
		return this.jdbcTemplate.batchUpdate(
				"update RMI_CERTIFICATES set place_of_issue = ? where AUDIT_ID =? and CERTIFICATE_ID =? and CERTIFICATE_NUMBER =? and UNIQUE_TRACKING_NUMBER =?",
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, certificateDetails.get(i).getPlaceOfIssueEnc());
						ps.setInt(2, certificateDetails.get(i).getAuditId());
						ps.setLong(3, certificateDetails.get(i).getCetificateId());
						ps.setString(4, certificateDetails.get(i).getCertificateNumber());
						ps.setString(5, certificateDetails.get(i).getUniqueTrackingNo());
					}

					public int getBatchSize() {
						return certificateDetails.size();
					}

				});

	}
	
	
	private String decodeString(String ecnString) {
		try {
			return URLDecoder.decode(new String(Base64.getDecoder().decode(ecnString)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("not able to covert - {}", ecnString);
		}
		return null;
	}

}
