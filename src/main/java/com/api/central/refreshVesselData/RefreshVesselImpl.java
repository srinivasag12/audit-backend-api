package com.api.central.refreshVesselData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.central.util.AppConstant;
import com.api.central.util.RestTemplateUtil;
import com.google.gson.Gson;

@Service
public class RefreshVesselImpl implements RefreshVessel {

	static Logger log = LoggerFactory.getLogger(RefreshVesselImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	// @Autowired
	// private RestTemplate restTemplate;

	@Autowired
	private RestTemplateUtil restUtil;

	private Session hibsession;
	Transaction tx;

	private Map<String, String> map = new HashMap<>();

	@Override
	public Map<String, String> getUpdatredVesselData() {
		try {
			log.info("Fetching the currect session");
			hibsession = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			log.info("No Session Found creating new one");
			hibsession = sessionFactory.openSession();
		}
		return getVeselIMO();
	}

	private Map<String, String> getVeselIMO() {
		try {
			log.info("Fetching IMOs from audit details table");
			SQLQuery query = hibsession.createSQLQuery("select distinct mv.VESSEL_IMO_NO from MA_VESSEL mv");
			@SuppressWarnings("unchecked")
			List<String> stringList = (List<String>) query.list();
			log.debug("imos found {}", stringList);
			List<Integer> newIntegerList = stringList.stream().map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());

			return getUdatedData(newIntegerList);
		} catch (Exception ex) {
			log.error("Error Occured while fetching IMOs from audit details table");
			map.put("result", "Failed");
			return map;
		}
	}

	private Map<String, String> getUdatedData(List<Integer> imos) {
		RestTemplate restTemplate = new RestTemplate();
		Gson gson = new Gson();
		VesselIMOList vsl = new VesselIMOList();
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_JSON);
		vsl.setImos(imos);
		System.err.println(imos);

		try {
			log.info("Getting Vessel details from RMI DB of the imos");
			HttpEntity<VesselIMOList> imoDetialsReq = new HttpEntity<VesselIMOList>(vsl, restUtil.getHeaders());
			ResponseEntity<String> response = restTemplate.postForEntity(AppConstant.RMI_URL + "/ws2/updateVesselData",
					imoDetialsReq, String.class);

			log.info("Got response");
			return updatedData(new ArrayList<VesselRefresh>(
					Arrays.asList(gson.fromJson(response.getBody(), VesselRefresh[].class))));
		} catch (Exception e) {
			log.error("Unable to fetch vessel details of the given imos");
			e.printStackTrace();
			map.put("result", "Failed");
			return map;
		}
	}

	private Map<String, String> updatedData(List<VesselRefresh> vesselInfo) {
		 Calendar calendar = Calendar.getInstance();
		 int increment = 0;
		try {
			log.info("Creating transaction");
			tx = hibsession.beginTransaction();
			log.info("Deleting the existing data from the table vessel_refresh");
			hibsession.createQuery("delete from VesselRefresh").executeUpdate();
			log.info("Saving data to the Session");
			hibsession.flush();
			for (VesselRefresh refrshVesselInfo : vesselInfo) {
				refrshVesselInfo.setId((calendar.getTimeInMillis())+(increment++));
				hibsession.saveOrUpdate(refrshVesselInfo);
			}
			log.info("Comming Tx and saving data to Db");
			tx.commit();
			map.put("result", "Sucess");
			return map;
		} catch (HibernateException ex) {
			log.error("Unable to save the data to the DB");
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			map.put("result", "Failed");
			return map;
		} finally {
			hibsession.close();
		}
	}

}
