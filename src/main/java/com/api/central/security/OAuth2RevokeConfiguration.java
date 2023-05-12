package com.api.central.security;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.audit.delegate.AuditDelegate;
import com.api.central.master.delegate.MasterDelegate;
import com.api.central.master.entity.MaUsers;
import com.api.central.master.entity.UserSessionDetail;
import com.api.central.master.model.MaUsersWOPwd;
import com.api.central.util.AppConstant;


@RestController
public class OAuth2RevokeConfiguration {

	@Autowired
	private TokenStore tokenStore;	

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private MasterDelegate masterDelegate;
	
	@Autowired
	private AuditDelegate  auditDelegate;

	@GetMapping(value = "/oauth/revoke")
	public ResponseEntity<Map<String,String>> logout(HttpServletRequest request) {
		
		String authAccess = request.getHeader("authorization");
		String username = request.getParameter("username").toString().toLowerCase().trim();
		
		Long companyId = request.getParameter("companyId").toString() != null ? Long.parseLong( request.getParameter("companyId").toString()) : null;
		
		System.out.println("username = "+username);
		
		Map<String,String> msg = new HashMap<>();
		HttpStatus checkStatus = null;
		String refresh_Token = "";
		if (authAccess != null) {
			String accesstokn = authAccess.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(accesstokn);
			if(accessToken != null){
				revokeToken(accessToken,tokenStore,refresh_Token);
				msg.put("message", "access token as revoked");
				checkStatus = HttpStatus.OK;
				UserSessionDetail userSession = new UserSessionDetail();
				userSession.setUpdateMode(1);
				userSession.setUserId(username);
				userSession.setCompanyId(companyId);
				masterDelegate.saveSessionDetail(userSession);
				
			}else{
				msg.put("error", "Invalid access token: "+accesstokn);
				checkStatus = HttpStatus.CONFLICT;
			}
		}else{			
			msg.put("error", "access token is empty");
			checkStatus = HttpStatus.BAD_REQUEST;
		}
		
		Boolean removeRtoken = true;
		Set<String> rToken = redisTemplate.keys("refresh:*");
		if(rToken.size()>0){
			getRefreshToken(rToken,tokenStore,username,removeRtoken);
		}	
		
		auditDelegate.getCountOfLockedAuditByCurrUser(username, companyId);
		
		return new ResponseEntity<Map<String,String>>(msg,checkStatus);
	}


	@GetMapping(value = "/oauth/login")
	public ResponseEntity<Map<String,Object>> checkUserLogin(HttpServletRequest request){
		masterDelegate.getMobileVersion("Laptop", request.getParameter("un"), "3");
		boolean checkUserd = masterDelegate.checkUserPwd(request.getParameter("un"),request.getParameter("pd"));
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(checkUserd){
			
			Boolean checkUserStatus = 	masterDelegate.checkActiveStatus(request.getParameter("un"),(long) 0);
			Boolean checkUser = false;

			if(!checkUserStatus){
				revokeUserLogin(request);
				map.put("val", "Inactive");
				return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
			}else{

				Boolean removeRtoken = false;
				String userName = request.getParameter("un").trim().toLowerCase();
				Collection<OAuth2AccessToken> AToken = tokenStore.findTokensByClientIdAndUserName("auditapp",userName);

				if(AToken.size() > 0)
					checkUser = true;

				Set<String> rToken = redisTemplate.keys("refresh:*");
				String device=masterDelegate.getPreviousDevice(userName); //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 
				if(rToken.size()>0 && !checkUser){
					checkUser = getRefreshToken(rToken,tokenStore,userName ,removeRtoken);
						
				}
				
				map.put("device", device);  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 
				map.put("val", checkUser);
				
				
				
				
				return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);

			}

		}else{
			map.put("val", "invalied");
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}


	}	

	@GetMapping(value = "/oauth/revoke/login")
	public ResponseEntity<Boolean> revokeUserLogin(HttpServletRequest request){						

		String userName = request.getParameter("username").trim().toLowerCase();
		String device = request.getParameter("device").trim();  //Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022
		
		Collection<OAuth2AccessToken> AToken = tokenStore.findTokensByClientIdAndUserName("auditapp",userName);		
		String refreshToken = "";
		//Added by sudharsan for JIRA-id IRI-5484,IRI-5485,IRI-5486
		List<MaUsersWOPwd> userList = null;
		userList=masterDelegate.getCompanydetails(userName);
		
		for(OAuth2AccessToken accessToken : AToken){
			if(accessToken != null){
				OAuth2RevokeConfiguration.revokeToken(accessToken, tokenStore,refreshToken);
				UserSessionDetail userSession = new UserSessionDetail();
				userSession.setUpdateMode(1);
				userSession.setUserId(userName);
				masterDelegate.saveSessionDetail(userSession);
			}
		}
		Boolean removeRtoken = true;
		Set<String> rToken = redisTemplate.keys("refresh:*");
		if(rToken.size()>0){
			getRefreshToken(rToken,tokenStore,userName,removeRtoken);
		}
		/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 Start here*/
		if(device.equalsIgnoreCase(AppConstant.DEVICE_DESKTOP)||device.equalsIgnoreCase(AppConstant.DEVICE_MOBILE)){
			UserSessionDetail userSession = new UserSessionDetail();
			userSession.setUserId(userName);
			userSession.setCompanyId(userList.get(0).getCompanyId());//changed by sudharsan for JIRA-id IRI-5484,IRI-5485,IRI-5486
			userSession.setDevice(device);
			userSession.setStatus("Active");
			userSession.setLocation(masterDelegate.getUserIP(request));
			masterDelegate.updateUserLoginFlag(request.getParameter("username"),1);
			
			masterDelegate.saveSessionDetail(userSession);
		}
		else{
			masterDelegate.updateUserLoginFlag(request.getParameter("username"),1);
		}
		/**Added by sudharsan For Jira-Id IRI-5482 on 16-09-2022 End here*/
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	public static boolean revokeToken(OAuth2AccessToken accessToken, TokenStore tokenStore, String refreshToken){

		if(refreshToken != null && refreshToken != ""){
			tokenStore.removeAccessToken(accessToken);
		}else{
			System.out.println("inside revoking");
			tokenStore.removeAccessToken(accessToken);
			tokenStore.removeRefreshToken(accessToken.getRefreshToken());
		}		
		return true;
	}

	public boolean getRefreshToken(Set<String> rToken, TokenStore tokenstore, String uname, Boolean removeRtoken){

		boolean checkRtoken = false;

		for(String rvalue : rToken){
			String[] str = rvalue.split(":");
			OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(str[1]);
			OAuth2Authentication authentication = tokenstore.readAuthenticationForRefreshToken(refreshToken);
			if(authentication.getName().equals(uname)){
				if(removeRtoken){
					tokenStore.removeRefreshToken(refreshToken);
				}else{
					return true;
				}
			}
		}
		return checkRtoken;
	}
	
}