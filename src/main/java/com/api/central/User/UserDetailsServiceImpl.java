package com.api.central.User;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.api.central.security.OAuth2RevokeConfiguration;


@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService{

	private UserRepository userRepository;

	@Autowired
	private TokenStore tokenStore;


	public UserDetailsServiceImpl(UserRepository userRepository){
		this.userRepository=userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	

		/*Collection<OAuth2AccessToken> AToken = tokenStore.findTokensByClientIdAndUserName("auditapp",username.toLowerCase());
		String refreshToken = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getParameter("refresh_token");
		
		for(OAuth2AccessToken accessToken : AToken){
			if(accessToken != null){
				OAuth2RevokeConfiguration.revokeToken(accessToken, tokenStore,refreshToken);
			}
		}*/

		try {
			UserDetail user = userRepository.findUser(username);
			if (user == null) {
				System.out.println("user not found with the provided username");
				return null;
			}

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),getAuthorities(user));
		}
		catch (Exception e){
			throw new UsernameNotFoundException("User not found");
		}
	}

	private Set<GrantedAuthority> getAuthorities(UserDetail user){
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for(Roles role : user.getRoles()) {			
			authorities.add(new SimpleGrantedAuthority(role.getRolename()));
		}
		return authorities;
	}

}
