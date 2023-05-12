package com.api.central.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.api.central.config.AppHibSession;
import com.api.central.fileDownload.FileOperationValues;
import com.api.central.master.model.Token;
import com.api.central.refreshVesselData.RefreshVesselImpl;
import com.google.gson.Gson;

@Service
public class RestTemplateUtil {
	static Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	FileOperationValues values;
	private Session hibsession;

	private Long createdAT = null;
	private String jwtToken = null;
	public static final int Refresh_token_after_sec = 82800;
	public static final String emails = "iri@bsolsystems.com";

	public HttpHeaders getHeaders() {
		try {
			log.info("Fetching the currect session");
			hibsession = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			log.info("No Session Found creating new one");
			hibsession = sessionFactory.openSession();
		}
		if (createdAT == null)
			getToken();
		else {
			log.info("Checking Token validity");
			long seconds = TimeUnit.MILLISECONDS.toSeconds((new Date().getTime() - createdAT));
			if (jwtToken == null || seconds > Refresh_token_after_sec) {
				log.info("Generating new Token");
				getToken();
			}
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", jwtToken);
		return headers;
	}

	public void getToken() {

		RestTemplate restTemplate = new RestTemplate();
		Gson gson = new Gson();
		log.info("Adding Headers");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		log.info("Query RMI For Token");
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add("username", "B$01Aud!trmi@b$0lsystems.c8m");
		requestBody.add("password", "B$01Aud!trmi&");

		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(AppConstant.RMI_URL + "/getToken", HttpMethod.POST,
				formEntity, String.class);
		log.info("returning token");
		Token tkn = gson.fromJson(response.getBody(), Token.class);
		createdAT = new Date().getTime();
		jwtToken = tkn.getToken();

		if (AppConstant.isStaggingApplication)
			sendTokenEmail(jwtToken);
	}

	public void sendTokenEmail(String todaystoken) {
		log.info("Sending Email to iri Team");
		org.hibernate.Query emailOtp = hibsession
				.createSQLQuery("CALL MAIL_SENDING( :from_mail, :to_mail, :cc_mail, :subject,:body)")
				.setString("from_mail", values.getFromMail()).setString("to_mail", emails).setString("cc_mail", "")
				.setString("subject", "Token For the Day").setString("body", "Today token is : " + todaystoken
						+ " Generated on " + new Date() + "and valid for next 24 Hr.");
		emailOtp.executeUpdate();
	}
}
