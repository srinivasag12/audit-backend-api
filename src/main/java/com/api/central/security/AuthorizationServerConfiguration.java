package com.api.central.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
 
    private static String REALM="CentralAuditApi";
     
    @Autowired
    private TokenStore tokenStore;
 
    @Autowired
    private UserApprovalHandler userApprovalHandler;
 
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
 
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	
        clients.inMemory()
            .withClient("auditapp")
            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
            .authorities("ADMIN", "ADUITOR")
            .scopes("read", "write", "trust")
            .secret("secret")
            .accessTokenValiditySeconds(7200).//Access token is only valid  for 2 hours.
            refreshTokenValiditySeconds(28800);//Refresh token is only valid for 8 hours.
    }
 
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {    	
    	endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
        .authenticationManager(authenticationManager).userDetailsService(userDetailsService);
    }
 
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {    	
    	oauthServer.realm(REALM+"/client");
    }
}