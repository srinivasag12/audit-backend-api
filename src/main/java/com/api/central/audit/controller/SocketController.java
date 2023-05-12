package com.api.central.audit.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.api.central.audit.delegate.NotifyService;
import com.api.central.audit.delegate.SocketDelegate;
import com.api.central.audit.entity.NotifyEmailDtls;
import com.api.central.audit.entity.PartialVesselLog;
import com.api.central.master.entity.VesselBean;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;
import com.api.central.util.RestTemplateUtil;


@Controller
public class SocketController {

	@Autowired
	private SocketDelegate service;

	@Autowired
	private NotifyService notifyMail;

	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	ServletContext servletContext;

	@Autowired
	AppUtil appUtil;
	
//	@Autowired
//	private RestTemplate restTemplate;
//	
	@Autowired
	private RestTemplateUtil restUtil;

	@MessageMapping("/notify/{userName}")
	public void greet(@Payload String greeting, @DestinationVariable("userName") String userName) {
		template.convertAndSend("/topic/userNotify/" +userName, service.getMessage(userName));
	}

	@MessageMapping("/notifymail/{userName}/{companyId}")	
	public void notifyEmail(@DestinationVariable("userName") String userName,@DestinationVariable("companyId") int companyid){
		template.convertAndSend("/topic/notify/"+userName+'/'+companyid ,notifyMail.emailCount(userName,companyid));
	}
	
	@RequestMapping(value = "notifyusermail/{userName}/{companyId}", method = RequestMethod.GET)
	public  ResponseEntity<List<Map<String, Object>>> notifyuserEmails(@PathVariable String userName,@PathVariable Long companyId){	
		return new ResponseEntity<List<Map<String, Object>>>(notifyMail.getEmailLogDtls(userName,companyId), HttpStatus.OK);
	}

	@RequestMapping(value = "emailNotify", method = RequestMethod.POST)
	public  ResponseEntity<Map<String, Object>>  closeEmails(@Valid @RequestBody NotifyEmailDtls notifyEmail){	
		return new ResponseEntity<Map<String, Object>>(notifyMail.updateFlag(notifyEmail), HttpStatus.OK);
	}
	
	@Scheduled(cron = "00 00 11 * * ?")
	public void  rmiDailyScheduler() {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders());
		
		List<PartialVesselLog> partialLogData = notifyMail.rmiScheduler();
		
		if(partialLogData.size() > 0){
			for(int count=0;count<partialLogData.size();count++){

				VesselBean vb = restTemplate.exchange(AppConstant.RMI_URL+"/ws2/vesselSpecificDtl/"+partialLogData.get(count).getVesselImoNo()
						+"/"+partialLogData.get(count).getVesselId(), HttpMethod.GET, request,VesselBean.class).getBody();
								
				if(AppUtil.checkNullValue(vb)){
					partialLogData.get(count).setStatus(1);
					notifyMail.updatePartialLogFlag(partialLogData.get(count));
				}
			}
		}
	}
	
	@MessageMapping("/checkVersionId/{userName}")
	public void checkVersion(@DestinationVariable("userName") String userName){
		template.convertAndSend("/topic/VersionId/"+userName ,notifyMail.checkVersionId());
	}


//	@RequestMapping(value = "checkVersion/{type}", method = RequestMethod.GET)
//	public ResponseEntity<byte[]> getVersion(@PathVariable("type") String type){
//		
//		int versionId = 0;
//		if(type.equals("create")){
//			versionId = notifyMail.checkVersionId();
//			if(versionId < 1){
//				versionId = notifyMail.updateVersionId();
//			}
//		}else if(type.equals("update")){
//			versionId = notifyMail.updateVersionId();
//		}else{
//			versionId = 0;
//		}
//
//		String Jsonpath = servletContext.getContextPath() +AppConstant.SEPARATOR+"version.json";
//		byte[]	data = null;
//		if(versionId >0){
//			data = appUtil.convertJson(versionId);
//		}
//		return new ResponseEntity<byte[]>(data,appUtil.setJsonHeaderType(Jsonpath,"version.json"), HttpStatus.OK);
//
//	}

	/*
    @MessageMapping("/score/{userName}")
    @SendTo("/topic/myscores/{userName}")
    public List<SocketBean> getMessage(@DestinationVariable String userName) {
        	System.out.println("userName :"+userName);
        List<SocketBean> scoresList = service.getMessage(userName);

        return scoresList;
    }
	 */}
